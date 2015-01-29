/**
 * @Package com.manyouren.android.core.plan    
 * @Title: PlansActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-16 下午2:08:26 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.discovery.old;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import roboguice.inject.InjectView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.github.kevinsawicki.wishlist.Toaster;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.HttpConfig;
import com.manyouren.manyouren.controller.AlbumController;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.core.user.Album;
import com.manyouren.manyouren.core.user.PicAdapter;
import com.manyouren.manyouren.entity.PhotoCommentEntity;
import com.manyouren.manyouren.entity.PlanCommentEntity;
import com.manyouren.manyouren.entity.PlanEntity;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.service.location.LocationSourceActivity;
import com.manyouren.manyouren.ui.CircleTransform;
import com.manyouren.manyouren.ui.plan.GalleryUrlActivity;
import com.manyouren.manyouren.ui.plan.PlanActivity;
import com.manyouren.manyouren.ui.plan.PlanCommentAdapter;
import com.manyouren.manyouren.ui.user.UserActivity;
import com.manyouren.manyouren.ui.user.UserHeadActivity;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.WidgetUtils;
import com.manyouren.manyouren.widget.AutoResizingListView;
import com.squareup.picasso.Picasso;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-16 下午2:08:26
 * 
 */
public class QuanCommentActivity extends BaseActivity {

	@InjectView(R.id.listView)
	PullToRefreshListView listView;

	@InjectView(R.id.pb_loading)
	ProgressBar progressBar;

	@InjectView(R.id.btn_comment_send)
	Button iv_send;

	@InjectView(R.id.et_commment_content)
	EditText et_content;
	
	@InjectView(R.id.tv_errmsg)
	TextView tv_errmsg;

	Album albumItem;

	private QuanCommentAdapter adapter;

	protected List<PhotoCommentEntity> items = new ArrayList<PhotoCommentEntity>();

	private int Page = 1;

	private volatile boolean requestAvaliable = false; // flag to determine

	private volatile long requestVariable = 0; // time number to count

	private String photoId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_quancomment);

		setActionBar("漫游圈评论");

		if (getIntent() != null && getIntent().getExtras() != null) {
			albumItem = (Album) getIntent().getParcelableExtra("album_item");

			photoId = String.valueOf(albumItem.getId());
		}

		initView();
		initEvent();
		init();

	}

	@Override
	protected void init() {

		adapter = new QuanCommentAdapter(getLayoutInflater(), items);
		listView.setAdapter(adapter);
		fetchData();
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initEvent() {

		iv_send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!et_content.getText().toString().isEmpty()) {

					et_content.setText(null);
					sendComment();
					iv_send.setEnabled(false);
					iv_send.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							iv_send.setEnabled(true);
						}
					},500);
					
				} else {
					Toaster.showShort(QuanCommentActivity.this, "没有内容发送");
				}
			}
		});

		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// items = new ArrayList<PlanCommentEntity>();
				fetchData();
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 监听要去掉Header
			}
		});

	}

	private void fetchData() {
		fetchCommentData(Page);

		commentHandler.post(new Runnable() {

			@Override
			public void run() {

				requestAvaliable = false;
				requestVariable = 0;

				while (requestAvaliable == false && requestVariable < 5000) {
					try {
						Thread.sleep(200);
						requestVariable += 200;

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if (progressBar != null)
					if (progressBar.isShown()) {
						progressBar.setVisibility(View.GONE);
					}

				if (listView != null && listView.isRefreshing()) {
					listView.onRefreshComplete();
				}
			}
		});

	}

	public void fetchCommentData(int Page) {

		HashMap<String, Object> hashMap = new HashMap<String, Object>();

		hashMap.put("Photo[photoId]", photoId);

		String url = HttpConfig.QUAN_COMMENT_LIST;
		
		Logot.outError("BUG", hashMap.toString());

		if (Page == 1) {
			AlbumController.fetchQuanComment(context, url, hashMap,
					commentHandler);
		} else {
			String addUrl = "&page=" + Page;
			AlbumController.fetchQuanComment(context, url + addUrl, hashMap,
					commentHandler);
		}
	}

	public void sendComment() {

		HashMap<String, Object> hashMap = new HashMap<String, Object>();

		hashMap.put("PhotoComment[photoId]", photoId);
		hashMap.put("PhotoComment[content]", et_content.getText().toString());
		// hashMap.put("PhotoComment[replyId]", planId);

		AlbumController.sendQuanComment(context, hashMap, sendHandler);

	}

	Handler commentHandler = new Handler() {

		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case -1: // fail
				tv_errmsg.setVisibility(View.GONE);
				break;
			case 1: // success
				tv_errmsg.setVisibility(View.GONE);
				items = AlbumController.commentList;
				adapter.setItems(items);
				// adapter.setItems(items.toArray());

				requestAvaliable = true;

				if (progressBar != null)
					if (progressBar.isShown()) {
						progressBar.setVisibility(View.GONE);
					}

				if (listView != null && listView.isRefreshing()) {
					listView.onRefreshComplete();
				}
				adapter.notifyDataSetChanged();

				break;
			case 3: // empty
				tv_errmsg.setVisibility(View.VISIBLE);
				
				requestAvaliable = false;

				if (progressBar != null)
					if (progressBar.isShown()) {
						progressBar.setVisibility(View.GONE);
					}
				if (listView != null && listView.isRefreshing()) {
					listView.onRefreshComplete();
				}
				adapter.notifyDataSetChanged();
			}
		};
	};

	Handler sendHandler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case -1: // fail
				Toaster.showShort(QuanCommentActivity.this, "发送是失败，请再试一下");
				break;

			case 1: // success
				Toaster.showShort(QuanCommentActivity.this, "发送成功");

				UserEntity user = UserController.getUserById(context,
						Long.valueOf(Constants.USER_ID));
				items.add(0, new PhotoCommentEntity(0, Long.valueOf(photoId),
						new Date().getTime() / 1000, user.getUserId(),
						et_content.getText().toString(), 0.0f, 0, user.getAvatar0(),
						user.getUserName()));


				et_content.setText(null);

				WidgetUtils.hideKeyBoard(QuanCommentActivity.this, et_content);

				adapter.setItems(items);
				adapter.notifyDataSetChanged();

				listView.getRefreshableView().smoothScrollToPosition(0);
				// 这两个都可以
				// listView.getRefreshableView().setSelection(0);

				break;
			}
		};
	};

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}

}
