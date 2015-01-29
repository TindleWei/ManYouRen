package com.manyouren.manyouren.ui.plan;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;

import com.github.kevinsawicki.wishlist.Toaster;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.base.BaseListFragment;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.PreferConfig;
import com.manyouren.manyouren.entity.PlanEntity;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.service.location.MyLocation;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.PreferenceUtils;
import static com.manyouren.manyouren.config.Constants.Extra.PLANS_ITEM;

public class PlanFragment extends BaseListFragment<PlanEntity> implements
		OnClickListener {

	public static final String TAG = "PlanFragment";

	@InjectView(R.id.iv_filter)
	ImageView iv_filter;

	@InjectView(R.id.iv_add)
	ImageView iv_publish;

	// @InjectView(R.id.tv_title)
	// TextView tv_title;

	@InjectView(R.id.iv_search_clear)
	private ImageView iv_clear;

	@InjectView(R.id.iv_search_dropdown)
	private ImageView iv_dropdown;

	private AutoCompleteTextView autoCompView;

	protected PlansListAdapter mAdapter;

	public static int FILTER_SEX = -1; // 属性Prefer存储

	public static int FILTER_LOC = -1; // 属性Prefer存储

	public static int FILTER_MONTH = -1; // 该属性临时的

	public static String FILTER_DEST = ""; // 该属性临时的

	public static int REQUESTCODE_PUBLISH = 1; // 发布计划的返回情况

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_plan, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initView();
		initEvent();
		init();
	}

	@Override
	protected void initView() {
		autoCompView = (AutoCompleteTextView) getActivity().findViewById(
				R.id.autocomplete);
	}

	@Override
	protected void initEvent() {

		iv_clear.setOnClickListener(this);
		iv_dropdown.setOnClickListener(this);
		iv_filter.setOnClickListener(this);
		iv_publish.setOnClickListener(this);

		final PlacesAutoAdapter placeAdapter = new PlacesAutoAdapter(context,
				getActivity().getLayoutInflater());
		autoCompView.setThreshold(1);
		autoCompView.setAdapter(placeAdapter);

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

				showProgressBar();
				mPage = 1;
				loadData(mPage);
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

							showProgressBar();
							mPage = 1;
							loadData(mPage);

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

	}

	@Override
	protected void init() {

		mAdapter = new PlansListAdapter(getActivity().getLayoutInflater(),
				items);
		mListView.setAdapter(mAdapter);

		initFilter();

		hideProgressBar();
		if (items.size() == 0) {
			showProgressBar();
			loadData(1);
			onRefresh();
		}
	}

	public void initFilter() {

		FILTER_SEX = PreferenceUtils.getInt(context,
				PreferConfig.PREFER_PLAN_FILTER_SEX, -1);
		FILTER_LOC = PreferenceUtils.getInt(context,
				PreferConfig.PREFER_PLAN_FILTER_LOC, -1);
	}

	@SuppressLint("ShowToast")
	@Override
	public void onRefresh() {
		MyLocation.getLocation(context, 1, locHandler);
		// loadData(1); 先定位，再获取计划

	}

	@Override
	public void onLoadMore() {
		mPage++;
		loadData(mPage);

	}

	@Override
	protected void onListItemClick(ListView parent, View view, int position,
			long id) {
		PlanEntity plan = ((PlanEntity) parent.getItemAtPosition(position));

		startActivity(new Intent(getActivity(), PlanActivity.class).putExtra(
				PLANS_ITEM, plan));
		getActivity()
				.overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	private void loadData(final int page) {

		Logot.outError("LoadData", "loadData");
		// showProgressBar();
		initFilter();

		String url = AsyncHttpManager.PLAN_FILTER_URL;
		if (page != 1) {
			url += "?page=" + page;
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
						if (items.size() == 0)
							showErrorInfo();
						else
							Toaster.showShort(activity, "网络出错");
						loadFinish();
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						parseJson(getResult());
						hideProgressBar();
						loadFinish();
					}

					@Override
					public void onError() {
						super.onError();
						hideProgressBar();
						loadFinish();
					}

					@Override
					public void onEmpty() {
						super.onEmpty();
						if (mPage == 1) {
							items = new ArrayList<PlanEntity>();
						}
						showEmptyInfo();
						loadFinish();
					}
				});
	}

	public void parseJson(String jsonString) {

		String json = "";
		try {
			json = (new JSONObject(jsonString).getJSONArray("message"))
					.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		items = new Gson().fromJson(json, new TypeToken<List<PlanEntity>>() {
		}.getType());

	}

	public void loadFinish() {

		if (items.size() < 10) {
			setPullLoad(false);
		} else {
			setPullLoad(true);
		}
		mAdapter.setItems(items.toArray());
		mAdapter.notifyDataSetChanged();

		mListView.stopRefresh();
		mListView.stopLoadMore();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search_clear:
			autoCompView.setText("");
			FILTER_DEST = "";
			mPage = 1;
			loadData(mPage);
			break;

		case R.id.iv_search_dropdown:
			break;

		case R.id.iv_filter:
			Dialog dialog = new PlanFilterDialog(getActivity(), filterHandler);

			Window dialogWindow = dialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.TOP);
			dialogWindow.setAttributes(lp);
			dialog.show();
			break;

		case R.id.iv_add:
			startActivityForResult(new Intent(getActivity(),
					ScenicsListActivity.class), REQUESTCODE_PUBLISH);
			getActivity().overridePendingTransition(R.anim.left_in,
					R.anim.left_out);
			break;
		}

	}

	Handler locHandler = new Handler() {
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 1: // success
				mPage = 1;
				loadData(mPage);

				break;
			case -1:// failure
				loadFinish();
				Toast.makeText(context, "网络异常，定位失败", 1000).show();
				break;

			default:
				break;
			}
		};
	};

	public static String dialogJson = "";

	private Handler filterHandler = new Handler() {
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case -1: // loading
				showProgressBar();
				break;

			case 0: // fail
				hideProgressBar();
				loadFinish();
				showErrorInfo();
				break;

			case 1: // success
				if (!dialogJson.equals(""))
					parseJson(dialogJson);

				hideProgressBar();
				loadFinish();
				break;

			case 2: // empty
				items = new ArrayList<PlanEntity>();
				hideProgressBar();
				loadFinish();
				showEmptyInfo();
				break;
			}
		};
	};

}