package com.manyouren.manyouren.ui.discovery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.Toaster;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseListFragment;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.PreferConfig;
import com.manyouren.manyouren.controller.DiscoveryController;
import com.manyouren.manyouren.entity.PlanEntity;
import com.manyouren.manyouren.entity.UserNearbyEntity;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.service.greendao.GreenUser;
import com.manyouren.manyouren.ui.user.UserActivity;
import com.manyouren.manyouren.util.PreferenceUtils;

public class NearbyFragment extends BaseListFragment<UserNearbyEntity> {

	private static final int MENUITEM_PEOPLE_FILTER = 11002;

	private PeopleNearbyAdapter mAdapter = null;

	public static int FILTER_SEX = -1;

	public static int FILTER_LOC = -1;

	@InjectView(R.id.iv_add_status)
	ImageView iv_add;

	@InjectView(R.id.iv_filter_status)
	ImageView iv_filter;

	// 重写这个方法
	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_nearby, null);
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
	}

	@Override
	protected void initEvent() {
		iv_filter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Dialog dialog = new NearbyFilterDialog(context, filterHandler);

				Window dialogWindow = dialog.getWindow();
				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
				dialogWindow.setGravity(Gravity.TOP);
				dialogWindow.setAttributes(lp);

				dialog.show();
			}
		});

		iv_add.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(context,
						NearbyStatusActivityFragment.class).putExtra(
						"UserStatus", PreferenceHelper.getUserStatus()));
				activity.overridePendingTransition(R.anim.left_in,
						R.anim.left_out);
			}
		});
	}
	
	public static String dialogJson="";

	Handler filterHandler = new Handler() {
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
				if(!dialogJson.equals(""))
				parseJson(dialogJson);
				
				hideProgressBar();
				loadFinish();
				break;
				
			case 2: //empty
				items = new ArrayList<UserNearbyEntity>();
				hideProgressBar();
				loadFinish();
				showEmptyInfo();
				break;
			}
		}
	};
	
	public void parseJson(String jsonString) {

		String json = "";
		try {
			json = (new JSONObject(jsonString).getJSONArray("message"))
					.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		items = new Gson().fromJson(json, new TypeToken<List<UserNearbyEntity>>() {
		}.getType());
	}

	@Override
	protected void init() {
		mAdapter = new PeopleNearbyAdapter(getActivity().getLayoutInflater(),
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

	private void initFilter() {

		FILTER_SEX = PreferenceUtils.getInt(context,
				PreferConfig.PREFER_NEARBY_FILTER_SEX, -1);
		FILTER_LOC = PreferenceUtils.getInt(context,
				PreferConfig.PREFER_NEARBY_FILTER_LOC, -1);
	}

	@Override
	public void onRefresh() {
		mPage=1;
		loadData(1);
	}

	@Override
	public void onLoadMore() {
		mPage++;
		loadData(mPage);

	}

	@Override
	protected void onListItemClick(ListView parent, View view, int position,
			long id) {
		UserNearbyEntity user = ((UserNearbyEntity) parent
				.getItemAtPosition(position));

		startActivity(new Intent(context, UserActivity.class)
				.putExtra("objectId", user.getObjectId())
				.putExtra("UserId", user.getUserId() + "")
				.putExtra("UserAvatar", user.getAvatar0())
				.putExtra("UserName", user.getUsername()));
		activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	private void loadData(final int page) {
		// showProgressBar();
		initFilter();

		String url = AsyncHttpManager.NEARBY_PEOPLE_URL;
		if (page != 1) {
			url += "?page=" + page;
		}
		RequestParams params = new RequestParams();
		params.put("latitude", PreferenceHelper.getLatitude());
		params.put("longitude", PreferenceHelper.getLongitude());
		params.put("currentCity", PreferenceHelper.getCity());
		// params.put("residence", (GreenUser.getInstance(context)
		// .getUserById(Long.valueOf(PreferenceHelper.getUserId())))
		// .getResidence());
		params.put("residence", GreenUser.getInstance(context).getUserById(Long.valueOf(PreferenceHelper.getUserId()))
				.getResidence());
		params.put("userId", PreferenceHelper.getUserId());

		params.put("gender", FILTER_SEX == -1 ? "" : FILTER_SEX + "");
		if (FILTER_LOC == -1)
			;// hashMap.put("SearchForm[residence]", "");
		else
			params.put("residenceFlag", FILTER_LOC + "");

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						if (items.size() == 0)
							showErrorInfo();
						else
							Toaster.showShort(getActivity(), "网络出错");
						loadFinish();
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						parseNeabyJson(getResult());

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
						showEmptyInfo();
						loadFinish();
					}
				});

	}

	public void parseNeabyJson(String jsonString) {

		String json = null;
		try {
			json = (new JSONObject(jsonString).getJSONArray("message"))
					.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Gson gson = new Gson();
		items = gson.fromJson(json, new TypeToken<List<UserNearbyEntity>>() {
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

}
