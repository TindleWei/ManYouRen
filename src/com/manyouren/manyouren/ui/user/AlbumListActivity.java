/**   
 * @Title: AlbumListActivity.java 
 * @Package com.manyouren.android.account 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author ssz 31807077_qq_com   
 * @date 2014-7-3 下午3:31:28 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.user;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.controller.AlbumController;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.core.user.Album;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.ui.discovery.old.QuanCommentActivity;
import com.manyouren.manyouren.ui.usernew.UserPhotoPublishActivity;
import com.manyouren.manyouren.util.Logot;

public class AlbumListActivity extends BaseActivity {
	private PullToRefreshListView albumListView;

	private List<Album> albumArr;

	TextView tv_errmsg;

	private ProgressBar pb_loading;

	private AlbumListAdapter albumAdapter;

	private boolean isDropDown = true;

	View headView;

	private UserEntity userEntity;
	MyHandler handler;
	Context context;
	private int page = 1;
	private static final int REQUEST_ALBUM_ADDED = 52001;
	private static final int MENUITEM_ALBUM_ADD = 12001;

	UserEntity user;
	String uid="";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_albumlist);

		context = this;
		setActionBar("相册");
		
		uid = getStringExtra("uid");
		if(uid==null|| uid.equals("")){
			user = UserController.getUserById(context,
					Long.valueOf(Constants.USER_ID));
		}else{
			user = new UserEntity();
			user.setUserId(Long.valueOf(uid));
		}
		
		Logot.outError("BUG", user.toString());

		initView();
		initHeadView();
		initEvent();
		init();
	}

	private void initHeadView() {

	}
	
	@Override
	protected void initView() {

		handler = new MyHandler(AlbumListActivity.this);

		albumListView = (PullToRefreshListView) findViewById(R.id.refresh_list);
		pb_loading = (ProgressBar) findViewById(R.id.pb_loading);
		tv_errmsg = (TextView) findViewById(R.id.tv_errmsg);

		albumAdapter = new AlbumListAdapter(context, albumArr);

		headView = LayoutInflater.from(context).inflate(
				R.layout.include_header_avatar, null);

		albumListView.getRefreshableView().addHeaderView(headView);

		albumListView.setAdapter(albumAdapter);

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
			startActivityForResult(new Intent(AlbumListActivity.this,
					UserPhotoPublishActivity.class), MENUITEM_ALBUM_ADD);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void init() {
		onListDropDown();

	}

	@Override
	protected void initEvent() {

		albumListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
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

		albumListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				if (PullToRefreshBase.Mode.PULL_FROM_START == albumListView
						.getCurrentMode()) {
					onListDropDown();
				} else if (PullToRefreshBase.Mode.PULL_FROM_END == albumListView
						.getCurrentMode()) {
					onListLoadMore();
				}
			}
		});
	}

	private void onListDropDown() {
		isDropDown = true;
		page = 1;
		albumArr = new ArrayList<Album>();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("Photo[userId]", user.getUserId() + "");
		AlbumController.getAlbumList(context, page, hashMap, handler);

	}

	private void onListLoadMore() {
		isDropDown = true;
		page = 1;
		albumArr = new ArrayList<Album>();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("Photo[userId]", user.getUserId() + "");
		AlbumController.getAlbumList(context, page, hashMap, handler);

	}

	 class MyHandler extends Handler {
		WeakReference<AlbumListActivity> mActivity;

		MyHandler(AlbumListActivity activity) {
			mActivity = new WeakReference<AlbumListActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			final AlbumListActivity aActivity = mActivity.get();
			aActivity.pb_loading.setVisibility(View.GONE);
			Log.i("AlbumListActivity", "msg what:" + msg.what);

			switch (msg.what) {

			case -1: // fail
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
					aActivity.tv_errmsg.setVisibility(View.VISIBLE);
				}else{
					aActivity.tv_errmsg.setVisibility(View.GONE);
				}
				if (aActivity.isDropDown) {
					aActivity.albumAdapter = new AlbumListAdapter(aActivity,
							aActivity.albumArr);
					aActivity.albumListView.setAdapter(aActivity.albumAdapter);

					aActivity.albumAdapter.notifyDataSetChanged();

					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"MM-dd HH:mm:ss", Locale.CHINA);
					aActivity.albumListView.onRefreshComplete();

				} else {
					
					if (aActivity.albumAdapter == null)
						aActivity.albumAdapter = new AlbumListAdapter(
								aActivity, aActivity.albumArr);
					aActivity.albumAdapter.notifyDataSetChanged();
					aActivity.albumListView.onRefreshComplete();
				}
				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_ALBUM_ADDED) {
				Album album = data.getParcelableExtra("album");
				if (albumArr == null)
					albumArr = new ArrayList<Album>();
				albumArr.add(0, album);
				if (albumAdapter == null)
					albumAdapter = new AlbumListAdapter(context, albumArr);
				else
					albumAdapter.notifyDataSetChanged();
			}
		}
	}
}
