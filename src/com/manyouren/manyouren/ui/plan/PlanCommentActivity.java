/**
 * @Package com.manyouren.android.ui.plan    
 * @Title: PlanCommentActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-9-4 下午1:35:58 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.plan;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.Toaster;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.controller.PlanController;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.entity.PlanCommentEntity;
import com.manyouren.manyouren.entity.PlanEntity;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.util.DateUtils;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.WidgetUtils;
import com.squareup.picasso.Picasso;

/**
 * PlanCommentActivity
 * 
 * @author firefist_wei
 * @date 2014-9-4 下午1:35:58
 * 
 */

// @ContentView(R.layout.activity_plancomment)
public class PlanCommentActivity extends BaseActivity {

	@InjectView(R.id.listView)
	PullToRefreshListView listView;

	@InjectView(R.id.pb_loading)
	ProgressBar progressBar;

	@InjectView(R.id.btn_comment_send)
	Button iv_send;

	@InjectView(R.id.et_commment_content)
	EditText et_content;
	
	@InjectView(R.id.tv_error_msg)
	TextView tv_error_msg;

	View planHeadView;

	private TextView tv_destination;
	private TextView tv_city;
	private TextView tv_date;
	private TextView tv_postscript;
	private LinearLayout layout_plan_photo;
	private ImageView iv_pho1;
	private ImageView iv_pho2;
	private ImageView iv_pho3;
	private ImageView iv_plan_for;
	private ImageView iv_plan_with;
	private ImageView iv_plan_seek;
	private TextView tv_createTime;

	String[] urls = new String[3]; // plan images

	private PlanCommentAdapter adapter;

	protected List<PlanCommentEntity> items = new ArrayList<PlanCommentEntity>();

	private int mPage = 1;

	private volatile boolean requestAvaliable = false; // flag to determine

	private volatile long requestVariable = 0; // time number to count

	private String planId;
	
	private String planAuthor; // plan的作者的uid

	private PlanEntity planEntity; // 这个PlanEntity中没有UserEntity
	
	private String replyId = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_plancomment);

		setActionBar("计划评论");

		planId = getStringExtra("planId");
		//planAuthor = getStringExtra("author");
		planEntity = getSerializableExtra("PlanEntity");
		planAuthor = planEntity.getUserId();

		initView();
		initEvent();
		init();
	}

	@Override
	protected void initView() {
		progressBar.setVisibility(View.VISIBLE);

		planHeadView = LayoutInflater.from(context).inflate(
				R.layout.include_plan_header, null);
		listView.getRefreshableView().addHeaderView(planHeadView);
	}

	@Override
	protected void initEvent() {
		iv_send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!et_content.getText().toString().isEmpty()) {
					Logot.outError("TAG", "send click");
					sendComment();
				} else {
					Toaster.showShort(PlanCommentActivity.this, "没有内容发送");
				}
			}
		});

		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				if (PullToRefreshBase.Mode.PULL_FROM_START == listView
						.getCurrentMode()) {
					items = new ArrayList<PlanCommentEntity>();
					fetchData(1);
				} else if (PullToRefreshBase.Mode.PULL_FROM_END == listView
						.getCurrentMode()) {
					mPage++;
					fetchData(mPage);
				}

			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position < listView.getRefreshableView().getHeaderViewsCount())
					return;

				PlanCommentEntity comment = adapter.getItem(position
						- listView.getRefreshableView().getHeaderViewsCount());

				if (et_content.getText().toString().trim().equals("")
						|| et_content.getText().toString().startsWith("@")) {
					replyId = comment.getReplyId();
					et_content.setText("@" + comment.getUserName() + ": ");
				}

			}
		});
	}

	@Override
	protected void init() {

		initPlanHeadView();

		adapter = new PlanCommentAdapter(getLayoutInflater(), items);

		listView.setAdapter(adapter);

		fetchData(1);
	}

	private void initPlanHeadView() {

		tv_destination = (TextView) planHeadView
				.findViewById(R.id.tv_destination);
		tv_city = (TextView) planHeadView.findViewById(R.id.tv_city);
		tv_date = (TextView) planHeadView.findViewById(R.id.tv_startdate);
		tv_postscript = (TextView) planHeadView
				.findViewById(R.id.tv_postscript);
		iv_pho1 = (ImageView) planHeadView.findViewById(R.id.iv_plan_pho1);
		iv_pho2 = (ImageView) planHeadView.findViewById(R.id.iv_plan_pho2);
		iv_pho3 = (ImageView) planHeadView.findViewById(R.id.iv_plan_pho3);
		layout_plan_photo = (LinearLayout) planHeadView
				.findViewById(R.id.layout_plan_photo);
		iv_plan_for = (ImageView) planHeadView.findViewById(R.id.iv_plan_for);
		iv_plan_with = (ImageView) planHeadView.findViewById(R.id.iv_plan_with);
		iv_plan_seek = (ImageView) planHeadView.findViewById(R.id.iv_plan_seek);
		tv_createTime = (TextView) planHeadView
				.findViewById(R.id.tv_createtime);

		if (planEntity.getScenicId().equals("0")) {
			tv_destination.setText(planEntity.getpName()+" (未验证)");
			tv_city.setText(" ");
			
		} else {
			tv_destination.setText(planEntity.getMyPName());
			tv_city.setText(" " + planEntity.getcName());
		}
		
		tv_date.setText(DateUtils.getPlanDigitalDate(planEntity.getStartDate(),
				planEntity.getEndDate()));
		tv_postscript.setText(planEntity.getPostscript());

		tv_createTime.setText("发布于"
				+ DateUtils.getCreateTime(Long.valueOf(planEntity
						.getCreateTime())));

		if (planEntity.getImages() != null
				&& planEntity.getImages().length() > 0) {
			List<String> listImages = PlanController.getPlanImages(planEntity
					.getImages());

			for (int i = 0; i < listImages.size(); i++) {

				Picasso.with(context).load(listImages.get(i)).resize(400, 400)
						.centerCrop().placeholder(R.drawable.gravatar_image)
						.into(i == 0 ? iv_pho1 : (i == 1 ? iv_pho2 : iv_pho3));
				(i == 0 ? iv_pho1 : (i == 1 ? iv_pho2 : iv_pho3))
						.setVisibility(View.VISIBLE);
				urls[i] = listImages.get(i);
			}
		}

		if (urls[0] == null) {
			layout_plan_photo.setVisibility(View.GONE);
		} else {
			layout_plan_photo.setVisibility(View.VISIBLE);
		}

		int res_id = PlanController.res_for[Integer.valueOf(planEntity
				.getType())];
		iv_plan_for.setImageResource(res_id);

		res_id = PlanController.res_with[Integer.valueOf(planEntity
				.getTogether())];
		iv_plan_with.setImageResource(res_id);

		res_id = PlanController.res_seek[Integer.valueOf(planEntity
				.getPurpose())];
		iv_plan_seek.setImageResource(res_id);
	}

	private void fetchData(int page) {
		String url = AsyncHttpManager.PLAN_COMMENT_URL;
		if (page != 1) {
			url += "?page=" + page;
		}

		RequestParams params = new RequestParams();
		params.put("planId", planId);
		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onSuccessed() {
						super.onSuccessed();
						
						tv_error_msg.setVisibility(View.GONE);
						
						String json = "";
						try {
							json = (new JSONObject(getResult()).getJSONArray("message")).toString();
							items = new Gson().fromJson(json, new TypeToken<List<PlanCommentEntity>>()
									{}.getType());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
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
					}

					@Override
					public void onFailured() {
						super.onFailured();
						
						if (progressBar != null)
							if (progressBar.isShown()) {
								progressBar.setVisibility(View.GONE);
							}
						if (listView != null && listView.isRefreshing()) {
							listView.onRefreshComplete();
						}
					}

					@Override
					public void onEmpty() {
						super.onEmpty();
						
						if(items.size()==0)
						tv_error_msg.setVisibility(View.VISIBLE);

						if (progressBar != null)
							if (progressBar.isShown()) {
								progressBar.setVisibility(View.GONE);
							}
						if (listView != null && listView.isRefreshing()) {
							listView.onRefreshComplete();
						}
						adapter.notifyDataSetChanged();
					}
				});
	}


	public void sendComment() {
		String content = et_content.getText().toString().trim();

		String url = AsyncHttpManager.PLAN_SEND_COMMENT_URL;

		RequestParams params = new RequestParams();
		params.put("planId", planId);
		params.put("userId", PreferenceHelper.getUserId());
		params.put("content", content);
		params.put("author", planAuthor);
		if (content.startsWith("@")) {
			params.put("replyId", replyId);
		}
		
		Logot.outError("TAG","send comment run");

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
			
			@Override
			public void onFailured() {
				super.onFailured();
				Toaster.showShort(PlanCommentActivity.this, "发送是失败，请再试一下");
			}
			
			@Override
					public void onSuccessed() {
						super.onSuccessed();
						Toaster.showShort(PlanCommentActivity.this, "发送成功");

						UserEntity user = UserController.getUserById(context,
								Long.valueOf(Constants.USER_ID));
						items.add(0, new PlanCommentEntity(0, Long.valueOf(planId),
								new Date().getTime() / 1000, user.getUserId(),
								et_content.getText().toString(), "0", user.getAvatar0(),
								user.getUserName()));

						et_content.setText(null);
						WidgetUtils.hideKeyBoard(PlanCommentActivity.this, et_content);

						adapter.setItems(items);
						adapter.notifyDataSetChanged();

						listView.getRefreshableView().smoothScrollToPosition(0);
						// 这两个都可以
						// listView.getRefreshableView().setSelection(0);
					}
		});
	}

}
