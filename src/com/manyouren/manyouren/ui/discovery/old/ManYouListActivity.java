/**   
 * @Title: AlbumListActivity.java 
 * @Package com.manyouren.android.account 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ssz 31807077_qq_com   
 * @date 2014-7-3 下午3:31:28 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.discovery.old;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.controller.AlbumController;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.core.user.Album;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.ui.CircleTransform;
import com.manyouren.manyouren.ui.user.AlbumAddActivity;
import com.manyouren.manyouren.ui.user.AlbumListAdapter;
import com.manyouren.manyouren.ui.user.UserPlansActivity;
import com.manyouren.manyouren.util.ScreenUtils;
import com.manyouren.manyouren.widget.PullScrollView;
import com.manyouren.manyouren.widget.RefreshListView;
import com.manyouren.manyouren.widget.RefreshListView.OnDropDownListener;
import com.squareup.picasso.Picasso;

@SuppressLint({ "NewApi" })
public class ManYouListActivity extends BaseActivity {

	Context context;
	MyHandler handler;

	private PullToRefreshListView albumListView;

	TextView tv_errmsg;
	private List<Album> albumArr;

	private ProgressBar pb_loading;

	private ManYouListAdapter albumAdapter;
	
	View headView;

	private int page = 1;
	boolean isDropDown = true;
	private static final int EVENT_ALBUM_ADDED = 52001;
	private static final int MENUITEM_ALBUM_ADD = 12001;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_albumlist);

		context = this;
		setActionBar("漫游圈");
		initView();
		initEvent();
		init();
	}

	@Override
	protected void initView() {

		albumListView = (PullToRefreshListView) findViewById(R.id.refresh_list);
		pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
		tv_errmsg = (TextView) findViewById(R.id.tv_errmsg);
		
		headView = LayoutInflater.from(context).inflate(
				R.layout.include_header_avatar, null);

		albumListView.getRefreshableView().addHeaderView(headView);

		albumAdapter = new ManYouListAdapter(context, albumArr);
		albumListView.setAdapter(albumAdapter);
	}

	@Override
	protected void initEvent() {

		albumListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				page = 1;
				albumArr = new ArrayList<Album>();
				isDropDown = true;
				AlbumController.getManyouList(context, page, handler);
			}
		});

		albumListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				try {
					Album albumItem = albumArr.get(position
							- albumListView.getRefreshableView()
									.getHeaderViewsCount());
					startActivity(new Intent(context, QuanCommentActivity.class)
							.putExtra("album_item", albumItem));
					overridePendingTransition(R.anim.left_in, R.anim.left_out);
					
				} catch (Exception e) {
				}
			}
		});
	}

	@Override
	protected void init() {
		handler = new MyHandler(ManYouListActivity.this);
		AlbumController.getManyouList(context, page, handler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem done = menu.add(0, MENUITEM_ALBUM_ADD, 0, "发照片");
		done.setIcon(this.getResources().getDrawable(R.drawable.ic_action_camera));
		done.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENUITEM_ALBUM_ADD:
			startActivityForResult(new Intent(ManYouListActivity.this,
					AlbumAddActivity.class), EVENT_ALBUM_ADDED);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == EVENT_ALBUM_ADDED) {
				page = 1;
				albumArr = new ArrayList<Album>();
				isDropDown = true;
				AlbumController.getManyouList(context, page, handler);
			}
		}
	}

	static class MyHandler extends Handler {
		WeakReference<ManYouListActivity> mActivity;

		MyHandler(ManYouListActivity activity) {
			mActivity = new WeakReference<ManYouListActivity>(activity);
		}
		@Override
		public void handleMessage(Message msg) {
			final ManYouListActivity aActivity = mActivity.get();
			aActivity.pb_loading.setVisibility(View.GONE);
			
			
			switch (msg.what) {
			case -1: // fail
				aActivity.tv_errmsg.setText("网络访问异常");
				aActivity.tv_errmsg.setVisibility(View.VISIBLE);
				break;
			case 0: // finish
				break;
			case 3: // empty data
				aActivity.tv_errmsg.setVisibility(View.VISIBLE);
				if(aActivity.albumListView!=null){
					if(aActivity.albumListView.isRefreshing())
						aActivity.albumListView.onRefreshComplete();
				}
				break;
			case 1:
				aActivity.albumArr = AlbumController.albumList;
				if (aActivity.albumArr.isEmpty()) {
					aActivity.tv_errmsg.setText("还没有照片呢");
					aActivity.tv_errmsg.setVisibility(View.VISIBLE);
				}else{
					aActivity.tv_errmsg.setVisibility(View.GONE);
				}
				
				if (aActivity.isDropDown) {
					aActivity.albumAdapter = new ManYouListAdapter(aActivity,
							aActivity.albumArr);
					aActivity.albumListView.setAdapter(aActivity.albumAdapter);
					aActivity.albumAdapter.notifyDataSetChanged();

					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"MM-dd HH:mm:ss", Locale.CHINA);
					aActivity.albumListView.onRefreshComplete();
				} else {
					// albumArr.add(new Album());
					if (aActivity.albumAdapter == null)
						aActivity.albumAdapter = new ManYouListAdapter(
								aActivity, aActivity.albumArr);
					aActivity.albumAdapter.notifyDataSetChanged();
					aActivity.albumListView.onRefreshComplete();
				}
				break;
			}
		}
	}

}
