/**
 * @Package com.manyouren.plan    
 * @Title: PlanFragment.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-7 下午6:51:00 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.plan;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.kevinsawicki.wishlist.Toaster;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.PreferConfig;
import com.manyouren.manyouren.controller.PlanController;
import com.manyouren.manyouren.entity.PlanEntity;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.service.greendao.GreenPlan;
import com.manyouren.manyouren.service.greendao.GreenUser;
import com.manyouren.manyouren.service.location.MyLocation;
import com.manyouren.manyouren.ui.HeaderFooterListAdapter;
import com.manyouren.manyouren.ui.ItemPullListFragment;
import com.manyouren.manyouren.ui.ThrowableLoader;
import com.manyouren.manyouren.util.Ln;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.PreferenceUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import static com.manyouren.manyouren.config.Constants.Extra.PLANS_ITEM;

/**
 * @ClassName: PlanFragment
 * @author firefist_wei firefist.wei@gmail.com
 * @date 2014-6-7 下午6:51:00
 * 
 *       add: 执行顺序是：1.configureList() 2.onActivityCreated() 3.onCreateLoader()
 *       4.onResume()
 * 
 */
@Deprecated
public class PlanFragmentOld extends ItemPullListFragment<PlanEntity> {

	public static final String TAG = "PlanFragment";

	Context context = null;

	LinearLayout titleView;

	ImageView iv_filter;

	ImageView iv_publish;

	TextView tv_title;

	private ImageView iv_clear;

	private ImageView iv_dropdown;

	private AutoCompleteTextView autoCompView;

	protected HeaderFooterListAdapter<PlansListAdapter> adapter;

	GetDataTask getDataTask = null;

	private int Page = 1;

	public static int FILTER_SEX = -1; // 属性Prefer存储

	public static int FILTER_LOC = -1; // 属性Prefer存储

	public static int FILTER_MONTH = -1; // 该属性临时的

	public static String FILTER_DEST = ""; // 该属性临时的

	public static int REQUESTCODE_PUBLISH = 1; // 发布计划的返回情况

	boolean isFirstLoad = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getActivity();

		FILTER_SEX = PreferenceUtils.getInt(context,
				PreferConfig.PREFER_PLAN_FILTER_SEX, -1);
		FILTER_LOC = PreferenceUtils.getInt(context,
				PreferConfig.PREFER_PLAN_FILTER_LOC, -1);
	}

	@Override
	protected void configureList(Activity activity, ListView listView) {
		Logot.outError(TAG, "configureList()");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Logot.outError(TAG, "onActivityCreated()");
		initView();
		initEvent();
		init();
	}

	@Override
	protected void initView() {

		titleView = (LinearLayout) getActivity()
				.findViewById(R.id.header_title);

		titleView.addView(getActivity().getLayoutInflater().inflate(
				R.layout.headerview_plan, null));
		titleView.addView(getActivity().getLayoutInflater().inflate(
				R.layout.plan_item_des_search, null));

		autoCompView = (AutoCompleteTextView) titleView
				.findViewById(R.id.autocomplete);

		iv_clear = (ImageView) titleView.findViewById(R.id.iv_search_clear);
		iv_dropdown = (ImageView) titleView
				.findViewById(R.id.iv_search_dropdown);

		iv_filter = (ImageView) titleView.findViewById(R.id.iv_filter);
		iv_publish = (ImageView) titleView.findViewById(R.id.iv_add);
		tv_title = (TextView) titleView.findViewById(R.id.tv_title);

	}

	@Override
	protected void init() {
		//items = GreenPlan.getInstance(context).getRecentPlan();

		adapter = new HeaderFooterListAdapter<PlansListAdapter>(getListView(),
				new PlansListAdapter(getActivity().getLayoutInflater(), items));

		listView.setAdapter(adapter);
		adapter.getWrappedAdapter().notifyDataSetChanged();

		tv_title.setText(Constants.PLACE_NOW);
	}

	@Override
	public void onResume() {
		super.onResume();
		Logot.outError(TAG, "onResume()");
		//items = GreenPlan.getInstance(context).getRecentPlan();

		adapter.getWrappedAdapter().setItems(items.toArray());
		adapter.getWrappedAdapter().notifyDataSetChanged();
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	protected void initEvent() {

		iv_clear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				autoCompView.setText("");
				FILTER_DEST = "";
				Page = 1;
				fetchData(Page);
				show(progressBar);
			}
		});

		final PlacesAutoAdapter placeAdapter = new PlacesAutoAdapter(
				context, getActivity().getLayoutInflater());
		autoCompView.setThreshold(1);
		autoCompView.setAdapter(placeAdapter);

		iv_dropdown.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		autoCompView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				String str = (String) adapterView.getItemAtPosition(position);

				// 隐藏软键盘
				InputMethodManager imm = (InputMethodManager) view.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(
							view.getApplicationWindowToken(), 0);
				}

				FILTER_MONTH = -1;
				FILTER_DEST = str;

				Page = 1;
				fetchData(Page);
				show(progressBar);
			}
		});

		autoCompView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						/* 判断是否是 Search 键 */
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							/* 隐藏软键盘 */
							((InputMethodManager) autoCompView.getContext()
									.getSystemService(
											Context.INPUT_METHOD_SERVICE))
									.hideSoftInputFromWindow(
											getActivity().getCurrentFocus()
													.getWindowToken(),
											InputMethodManager.HIDE_NOT_ALWAYS);

							autoCompView.dismissDropDown();

							FILTER_MONTH = -1;
							FILTER_DEST = autoCompView.getText().toString();

							Page = 1;
							fetchData(Page);
							show(progressBar);

							if (FILTER_DEST != "")
								tv_title.setText(FILTER_DEST);
							else
								tv_title.setText(Constants.PLACE_NOW);

							return true;
						}
						return false;
					}
				});

		autoCompView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					// iv_dropdown.setVisibility(View.GONE);
					iv_clear.setVisibility(View.VISIBLE);
				} else {
					iv_clear.setVisibility(View.GONE);
					// iv_dropdown.setVisibility(View.VISIBLE);
					FILTER_DEST = "";
					tv_title.setText(Constants.PLACE_NOW);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				FILTER_DEST = autoCompView.getText().toString();
			}
		});

		iv_filter.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Dialog dialog = new PlanFilterDialog(getActivity(),
						 filterHandler);
				Window dialogWindow = dialog.getWindow();
				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
				dialogWindow.setGravity(Gravity.TOP);
				dialogWindow.setAttributes(lp);
				dialog.show();
			}
		});

		final String[] cityList = context.getResources().getStringArray(
				R.array.citys);

		iv_publish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				startActivityForResult(new Intent(getActivity(),
						ScenicsListActivity.class), REQUESTCODE_PUBLISH);
				getActivity().overridePendingTransition(R.anim.left_in,
						R.anim.left_out);

			}
		});

	}

	// @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == getActivity().RESULT_OK) {

			if (requestCode == REQUESTCODE_PUBLISH) {
				show(progressBar);
				onListDropDown();
			}
		}
	}

	@Override
	public Loader<List<PlanEntity>> onCreateLoader(int id, Bundle args) {

		return new ThrowableLoader<List<PlanEntity>>(getActivity(), items) {

			@Override
			public List<PlanEntity> loadData() throws Exception {

				try {
					if (getActivity() != null) {
						if (items.size() == 0) {

							if (progressBar != null)
								show(progressBar);
							// add thread for bug
							// AsyncHttpResponseHandler at line 412
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Page = 1;
									fetchData(Page);
								}
							});
						}
						return items;

					} else {
						return Collections.emptyList();
					}
				} catch (Exception e) {
					return items;
				}
			}
		};
	}

	/**
	 * @date: 6/26 i add it because of list first show problem it is useful
	 */
	@Override
	public void onLoadFinished(Loader<List<PlanEntity>> loader,
			List<PlanEntity> items) {
		super.onLoadFinished(loader, items);
		// 暂不处理
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		PlanEntity plans = ((PlanEntity) list.getItemAtPosition(position));

		startActivity(new Intent(getActivity(), PlanActivity.class).putExtra(
				PLANS_ITEM, plans));
		getActivity()
				.overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	@Override
	protected int getErrorMessage(Exception exception) {
		return R.string.error_loading_news;
	}

	@Override
	public void onListDropDown() {
		getDataTask = new GetDataTask(true);
		getDataTask.execute();
	}

	@Override
	public void onListLoadMore() {
		getDataTask = new GetDataTask(false);
		getDataTask.execute();
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
				e.printStackTrace();
			}

			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {

					if (isDropDown == true) {
						MyLocation.getLocation(context, 1, locHandler);

					} else {
						Page++;
						fetchData(Page);
					}
				}
			});

			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {

			if (isDropDown) {
				tv_title.setText(Constants.PLACE_NOW);
				adapter.getWrappedAdapter().notifyDataSetChanged();
				listView.onRefreshComplete();
			} else {
				adapter.getWrappedAdapter().notifyDataSetChanged();
				listView.onRefreshComplete();
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onDestroyView() {
		if (listView != null)
			listView.onRefreshComplete();
		listView.setAdapter(null);
		if (getDataTask != null)
			getDataTask.cancel(true);
		super.onDestroyView();
	}

	private Handler filterHandler = new Handler() {
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case -1: // fail
				refreshFinished();

				break;
			case 0: // finish
				break;
			case 1: // success
				refreshFinished();
				getSavedItems();
				tv_title.setText(Constants.PLACE_NOW);

				break;
			case 3: // empty
				refreshFinished();
				getSavedItems();
			}
		};
	};

	public void refreshFinished() {
		if (progressBar != null)
			if (progressBar.isShown()) {
				hide(progressBar);
			}
		if (listView != null && listView.isRefreshing()) {
			listView.onRefreshComplete();
		}
	}

	public void getSavedItems() {

		//items = RootApplication.getInstance().getPlanList();

		adapter.getWrappedAdapter().setItems(items.toArray());
		adapter.getWrappedAdapter().notifyDataSetChanged();
	}

	public void fetchData(final int page) {

		String url = AsyncHttpManager.PLAN_FILTER_URL;

		if (page != 1) {
			url += "?page=" + Page;
		}

		RequestParams params = new RequestParams();
		params.put("userId", PreferenceHelper.getUserId());
		params.put("latitude", PreferenceHelper.getLatitude());
		params.put("longitude", PreferenceHelper.getLongitude());
		params.put("currentCity", PreferenceHelper.getCity());
		params.put("residence", PreferenceHelper.getResidence());

		if (FILTER_SEX == -1)
			params.put("gender", "");
		else
			params.put("gender", FILTER_SEX + "");

		if (FILTER_LOC == -1)
			params.put("residenceFlag", "");
		else
			params.put("residenceFlag", FILTER_LOC + "");

		if (FILTER_DEST.equals(""))
			params.put("location", "");
		else {
			params.put("location", FILTER_DEST);
		}

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						refreshFinished();
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						
						String json = "";
						JSONArray array = null;
						try {
							json = (new JSONObject(getResult()).getJSONArray("message")).toString();
							array = new JSONObject(getResult()).getJSONArray("message");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						items = new Gson().fromJson(json, new TypeToken<List<PlanEntity>>()
								{}.getType());
				
//						for(int i=0; i< array.length(); i++){
//							PlanEntity entity = new PlanEntity();
//							try {
//								JSONObject obj = (JSONObject) array.get(i);
//								entity.setAdName(obj.getString("adName"));
//								entity.setAvatar0(obj.getString("avatar0"));
//								entity.setBeenThere(obj.getString("beenThere"));
//								entity.setBirthday(obj.getString("birthday"));
//								entity.setcName(obj.getString("cName"));
//								entity.setCompany(obj.getString("company"));
//								entity.setCreateTime(Integer.valueOf(obj.getString("createTime")));
//								entity.setEndDate(obj.getString("endDate"));
//								entity.setFlight(obj.getString("flight"));
//								entity.setFrequency(obj.getString("frequency"));
//								entity.setGender(Integer.valueOf(obj.getString("gender")));
//								entity.setHobbyText(obj.getString("hobbyText"));
//								entity.setHomeland(obj.getString("homeland"));
//								entity.setImages(obj.getString("images"));
//								entity.setLat(obj.getString("lat"));
//								entity.setLike(Integer.valueOf(obj.getString("like")));
//								entity.setLive(Integer.valueOf(obj.getString("live")));
//								entity.setLon(obj.getString("lon"));
//								entity.setMagazine(obj.getString("magazine"));
//								entity.setPlanId(Integer.valueOf(obj.getString("planId")));
//								entity.setpName(obj.getString("pName"));
//								entity.setPostscript(obj.getString("postscript"));
//								entity.setProfession(obj.getString("profession"));
//								entity.setPurpose(Integer.valueOf(obj.getString("purpose")));
//								entity.setResidence(obj.getString("residence"));
//								entity.setScenicId(obj.getString("scenicId"));
//								entity.setSchool(obj.getString("school"));
//								entity.setStartCity(obj.getString("startCity"));
//								entity.setStatusText(obj.getString("statusText"));
//								entity.setTogether(Integer.valueOf(obj.getString("together")));
//								entity.setType(Integer.valueOf(obj.getString("type")));
//								entity.setUserId(Integer.valueOf(obj.getString("userId")));
//								entity.setUsername(obj.getString("username"));
//								entity.setVehicle(Integer.valueOf(obj.getString("vehicle")));
//								entity.setVehicleText(obj.getString("vehicleText"));
//								entity.setWant2Go(obj.getString("want2Go"));
//								
//							} catch (JSONException e) {
//							}
//							items.add(entity);
//						}
						

//						if (page == 0) {
//							GreenPlan.getInstance(context).deleteAll();
//							GreenUser.getInstance(context).clearAll();
//
//							if (getResult().equals("")) {
//								RootApplication.getInstance().setPlanList(null);
//							} else {
//								PlanController.parsePlanJson(context,
//										getResult());
//							}
//						} else {
//							PlanController.parsePlanJson(context,
//									getResult());
//						}

						refreshFinished();
						getSavedItems();
						tv_title.setText(Constants.PLACE_NOW);
					}

					@Override
					public void onError() {
						super.onError();
						refreshFinished();
					}

					@Override
					public void onEmpty() {
						super.onEmpty();

						//GreenPlan.getInstance(context).deleteAll();
						GreenUser.getInstance(context).clearAll();
						RootApplication.getInstance().setPlanList(null);

						refreshFinished();
						getSavedItems();
					}
				});

	}

	Handler locHandler = new Handler() {
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 100: // success
				// GreenPlan.getInstance(context).deleteAll();
				// getSavedItems();
				Page = 1;
				fetchData(Page);

				break;
			case 1001:// failure
				getSavedItems();

			default:
				break;
			}
		};
	};

}
