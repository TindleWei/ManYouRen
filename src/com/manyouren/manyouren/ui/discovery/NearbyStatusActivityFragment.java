package com.manyouren.manyouren.ui.discovery;

import java.util.ArrayList;
import java.util.List;
import roboguice.inject.InjectView;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseActivityFragment;
import com.manyouren.manyouren.base.BaseFragment;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;

public class NearbyStatusActivityFragment extends BaseActivityFragment {

	public static final int MENE_DONE = 1001;
	
	public static  String  userStatus = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBar("改变状态");
		
		userStatus = getStringExtra("UserStatus");

		final FragmentManager fm = getSupportFragmentManager();

		// Create the list fragment and add it as our sole content.
		if (fm.findFragmentById(android.R.id.content) == null) {
			final NearbyStatusFragment fragment = new NearbyStatusFragment();
			fm.beginTransaction().add(android.R.id.content, fragment).commit();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*MenuItem done = menu.add(0, MENE_DONE, 0, "DONE");
		done.setIcon(this.getResources().getDrawable(
				R.drawable.ic_action_accept));
		done.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		// This is the home button in the top left corner of the screen.
		case MENE_DONE:
			//upload

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

class NearbyStatusFragment extends BaseFragment {

	@InjectView(R.id.listView)
	private ListView mListView;
	
	@InjectView(R.id.tv_status_now)
	TextView tv_status_now;
	
	@InjectView(R.id.et_change_status)
	EditText et_change_status;
	
	@InjectView(R.id.btn_user_define)
	Button btn_user_define;
	
	private NearbyStatusAdapter mAdapter;
	
	private int lastPos = -1;
	
	public String[] mTags = { "找美食小吃", "游名胜古迹", "体验当地酒吧",
			"逛公园美景", "寻求当地旅游建议", "摄个影拍个照", "周边泡个温泉度个假", "短途自驾游",
			"登上山顶放声呐喊","下午茶、喝个咖啡","电影、话剧、音乐剧","徒步、单车旅行"};
	
	private List<StatusItem> items = new ArrayList<StatusItem>();


	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_nearbystatus, container, false);
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		initEvent();
		init();
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initEvent() {
		
		btn_user_define.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!et_change_status.isShown()){
					et_change_status.setVisibility(View.VISIBLE);
					btn_user_define.setText("完成");
				}else{
					if(!et_change_status.getText().toString().trim().equals("")){
						NearbyStatusActivityFragment.userStatus = et_change_status.getText().toString().trim();
						postRequest();
						et_change_status.setVisibility(View.GONE);
						tv_status_now.setText("当前状态: "+NearbyStatusActivityFragment.userStatus);
						et_change_status.setText(null);
						btn_user_define.setText("自定义");
					}else{
						et_change_status.setVisibility(View.GONE);
						et_change_status.setText(null);
						btn_user_define.setText("自定义");
					}
				}
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				items.get(position).setSelected(true);
				if(lastPos==position){
					return;
				}	
				if(lastPos>=0){
					items.get(lastPos).setSelected(false);
				}
				lastPos = position;
				mAdapter.setItems(items);
				mAdapter.notifyDataSetChanged();
				
				et_change_status.setVisibility(View.GONE);
				tv_status_now.setText("当前状态: "+items.get(position).getStatus());
				btn_user_define.setText("自定义");
				
				NearbyStatusActivityFragment.userStatus = items.get(position).getStatus();
				
				postRequest();

			}
		});

	}

	@Override
	protected void init() {
		for(String status: mTags){
			items.add(new StatusItem(status, false));
		}
		mAdapter = new NearbyStatusAdapter(getActivity().getLayoutInflater(), items);
		mListView.setAdapter(mAdapter);
		
		if(!NearbyStatusActivityFragment.userStatus.equals("")){
			tv_status_now.setText("当前状态: "+NearbyStatusActivityFragment.userStatus);
		} else {
			tv_status_now.setText("当前状态: "+"写下你现在的状态吧");
		}
		
	}
	
	public void postRequest(){
		String url = AsyncHttpManager.USER_CHANGE_STATUS_URL;
		
		RequestParams params = new RequestParams();
		params.put("userId", PreferenceHelper.getUserId());
		params.put("statusText", NearbyStatusActivityFragment.userStatus);
		final String status = NearbyStatusActivityFragment.userStatus;
		
		AsyncHttpManager.getClient(context).post(url, params, new AsyncHttpHandler(){
			@Override
			public void onFailured() {
				super.onFailured();
				Toast.makeText(context, "状态更新失败", 600).show();
				
			}
			
			@Override
			public void onSuccessed() {
				super.onSuccessed();
				Toast.makeText(context, "状态更新成功", 600).show();
				PreferenceHelper.setUserStatus(status);
			}
		});
	}

}
