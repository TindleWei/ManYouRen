/**
 * @Package com.manyouren.android.ui.user    
 * @Title: UserPlansActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-16 上午2:23:22 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.Toaster;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.entity.PlanEntity;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.ui.HeaderFooterListAdapter;
import com.manyouren.manyouren.ui.ItemPullListActivity;
import com.manyouren.manyouren.ui.ThrowableLoader;
import com.manyouren.manyouren.ui.chat.ChatActivity;
import com.manyouren.manyouren.ui.plan.PlanCommentActivity;
import com.manyouren.manyouren.ui.plan.ScenicsListActivity;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.ScreenUtils;

@TargetApi(11)
public class UserPlansActivity extends ItemPullListActivity<PlanEntity> {
	
	@InjectView(R.id.tv_error_msg)
	TextView tv_error_msg;

	UserPlansActivity instance = null;

	Context context = null;

	private static final int MENUITEM_PLAN_PUBLISH = 11004;
	
	private static final int REQUESTCODE_PUBLISH = 10005;

	public static List<PlanEntity> userPlansList = new ArrayList<PlanEntity>();

	protected HeaderFooterListAdapter<UserPlansAdapter> adapter;

	GetDataTask getDataTask = null;

	String extraType = null;

	String uid = "";

	int mPage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		context = this;

		uid = getStringExtra("uid");
		if (uid == null || uid.equals(""))
			uid = PreferenceHelper.getUserId();
		Logot.outError("UID",uid);

		if (uid.equals(Constants.USER_ID))
			setActionBar("旅行计划");
		else
			setActionBar("我的计划");

		initView();
		initEvent();
		init();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		if(!uid.equals("")&&uid.equals(PreferenceHelper.getUserId())){
			return true;
		}
		MenuItem done = menu.add(0, MENUITEM_PLAN_PUBLISH, 0, "Upload");
		done.setIcon(this.getResources().getDrawable(
				R.drawable.ic_action_newplan));
		done.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case MENUITEM_PLAN_PUBLISH:
			startActivityForResult(new Intent(context,
					ScenicsListActivity.class), REQUESTCODE_PUBLISH);
			overridePendingTransition(R.anim.left_in,
					R.anim.left_out);
			//postRequest();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void initView() {

		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	protected void initEvent() {

		if (uid.equals(PreferenceHelper.getUserId()))
			listView.getRefreshableView().setOnItemLongClickListener(
					new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> parent,
								View view, final int position, long id) {

							final PlanEntity planEntity = ((PlanEntity) listView
									.getRefreshableView().getItemAtPosition(
											position));
							final String  planId = planEntity.getPlanId();
							
							final int pos = position-listView
									.getRefreshableView().getHeaderViewsCount();

							new AlertDialog.Builder(context)
									.setTitle(planEntity.getpName())
									.setItems(
											new String[] { "删除计划" },
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													switch (which) {
													case 0:
														postDeleteRequest(planId, pos);
														break;
													}
												}
											}).create().show();
							return false;
						}
					});
	}

	@Override
	protected void init() {

		if (getIntent().getExtras() != null) {
			extraType = getIntent().getStringExtra("extraType");
		}
	}

	@Override
	protected void configureList(Activity activity, ListView listView) {

		adapter = new HeaderFooterListAdapter<UserPlansAdapter>(listView,
				new UserPlansAdapter(getLayoutInflater(), items));
		listView.setAdapter(adapter);

	}

	@Override
	public Loader<List<PlanEntity>> onCreateLoader(int arg0, Bundle arg1) {

		return new ThrowableLoader<List<PlanEntity>>(this, items) {

			@Override
			public List<PlanEntity> loadData() throws Exception {
				if (instance != null) {

					showList();
					progressBar.setVisibility(View.VISIBLE);

					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							fetchData(1);
						}
					});

					return items;
				} else {
					return Collections.emptyList();
				}
			}
		};
	}

	@Override
	protected int getErrorMessage(Exception exception) {
		return R.string.error_loading_news;
	}

	public void onListItemClick(ListView list, View view, int position, long id) {

		// 这里有两种情况

		// 第一种，聊天时，发计划对话
		if (extraType != null) {
			Intent intent = new Intent(UserPlansActivity.this,
					ChatActivity.class);
			intent.putExtra("PlanEntity",
					((PlanEntity) list.getItemAtPosition(position)));

			PlanEntity entity = ((PlanEntity) list.getItemAtPosition(position));

			setResult(RESULT_OK, intent);

			finish();

		} else { // 第二种，计划的信息和评论

			Intent intent = new Intent(UserPlansActivity.this,
					PlanCommentActivity.class);
			intent.putExtra("PlanEntity",
					((PlanEntity) list.getItemAtPosition(position)));
			intent.putExtra("planId",
					((PlanEntity) list.getItemAtPosition(position)).getPlanId()
							+ "");

			startActivity(intent);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
		}
	}

	@Override
	public void onListDropDown() {
		getDataTask = new GetDataTask(true);
		getDataTask.execute();

	}
	
	public int DATA_MAX_COUNT = 20;

	@Override
	public void onListLoadMore() {
		 if (items.size() < DATA_MAX_COUNT) {
			 Toaster.showShort(UserPlansActivity.this, "没有更多数据");
		 }else{
			 getDataTask = new GetDataTask(false);
				getDataTask.execute();
		 }

	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		private boolean isDropDown;

		public GetDataTask(boolean isDropDown) {
			this.isDropDown = isDropDown;
		}

		@Override
		protected String[] doInBackground(Void... params) {
			try {
				Thread.sleep(1000);

			} catch (InterruptedException e) {

			}

			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					if (isDropDown == true) {
						fetchData(1);
					} else {
						mPage++;
						fetchData(mPage);
					}
				}
			});
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {

			if (isDropDown) {

				listView.onRefreshComplete();
			} else {

				
				 /*if (moreDataCount >= MORE_DATA_MAX_COUNT) {
				  listView.setHasMore(false); }*/
				 
				listView.onRefreshComplete();
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onDestroy() {
		listView.setAdapter(null);
		if (getDataTask != null)
			getDataTask.cancel(true);
		super.onDestroy();
	}

	public void fetchData(int page) {
		String url = AsyncHttpManager.USRER_PLAN_URL;
		if (page != 1) {
			url += "?page=" + page;
		}
		RequestParams params = new RequestParams();
		params.put("userId", uid);
		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						Logot.outError("FecthData", "fai");
						if (progressBar != null)
							if (progressBar.isShown()) {
								hide(progressBar);
							}
						if (items.isEmpty()) {
							TextView tv = new TextView(UserPlansActivity.this);
							tv.setGravity(Gravity.CENTER);
							tv.setText("没有数据，下拉看看");
							tv.setTextColor(0xff909090);
							tv.setPadding(0, 0, 0,
									ScreenUtils.getIntPixels(context, 60));
							tv.setTextSize(14);

							listView.setEmptyView(tv);
						}
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						Logot.outError("FecthData", "suc");
						tv_error_msg.setVisibility(View.GONE);
						if (progressBar != null)
							if (progressBar.isShown()) {
								hide(progressBar);
							}

						String json = "";
						try {
							json = (new JSONObject(getResult()).getJSONArray("message")).toString();
							items = new Gson().fromJson(json, new TypeToken<List<PlanEntity>>()
									{}.getType());
						} catch (JSONException e) {
							e.printStackTrace();
						}

						adapter.getWrappedAdapter().setItems(items.toArray());
						adapter.getWrappedAdapter().notifyDataSetChanged();
					}

					@Override
					public void onEmpty() {
						super.onEmpty();
						Logot.outError("FecthData", "emp");
						tv_error_msg.setVisibility(View.VISIBLE);
						tv_error_msg.setText("暂无计划");
						if (progressBar != null)
							if (progressBar.isShown()) {
								hide(progressBar);
							}

						items = Collections.emptyList();

						adapter.getWrappedAdapter().setItems(items.toArray());
						adapter.getWrappedAdapter().notifyDataSetChanged();
					}
				});
	}

	
	public void postDeleteRequest(String planId, final int pos) {
		if(!uid.equals(PreferenceHelper.getUserId()))
			return;
		
		String url = AsyncHttpManager.PLAN_DELETE_URL;
		RequestParams params = new RequestParams();
		params.put("planId", planId);
		params.put("userId", uid);
		
		AsyncHttpManager.getClient(context).post(url, params, new AsyncHttpHandler(){
			@Override
			public void onSuccessed() {
				super.onSuccessed();
				Toaster.showShort(UserPlansActivity.this, "删除计划成功");
				items.remove(pos);
				adapter.getWrappedAdapter().setItems(items.toArray());
				adapter.getWrappedAdapter().notifyDataSetChanged();
				
			}
			
			@Override
			public void onFailured() {
				super.onFailured();
				Toaster.showShort(UserPlansActivity.this, "删除计划失败");
			}
		});
		
	}

}
