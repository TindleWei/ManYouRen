package com.manyouren.manyouren.ui.plan;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.github.kevinsawicki.wishlist.Toaster;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.core.user.SortAdapter;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.ui.user.CityListActivity;
import com.manyouren.manyouren.ui.user.ClearEditText;
import com.manyouren.manyouren.ui.user.SideBar;
import com.manyouren.manyouren.ui.user.CityListActivity.SortCity;
import com.manyouren.manyouren.ui.usernew.AccountSignupActivity;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.PhotoUtils;

public class ScenicsListActivity extends BaseActivity implements
		OnClickListener {

	@InjectView(R.id.et_scenics)
	private ListView scenicList;

	@InjectView(R.id.layout_city)
	private RelativeLayout layout_city;

	@InjectView(R.id.ib_search_clear)
	private ImageButton ib_clear;

	@InjectView(R.id.et_city)
	private TextView selectCityBt;

	public static final int ACTION_REQUEST_SELEC_CITY = 14;

	private AutoCompleteTextView autoCompView;

	PlacesAutoAdapter pacAdapter = null;

	ScenicsAdapter scenicsAdapter;

	Double destLatitude = 0.0;
	Double destLongitude = 0.0;

	ArrayList<ScenicsEntity> poiList = new ArrayList<ScenicsEntity>();

	private Handler handler = new Handler() {

		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case -1: // fail
				refreshListView();
				break;
			case 1: // success
				refreshListView();
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_place);

		setActionBar("选择目的地");

		initView();
		initEvent();
		init();
	}

	@Override
	protected void initView() {
		autoCompView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
		autoCompView.setThreshold(1);
		selectCityBt.setText(Constants.PLACE_NOW);
	}

	@Override
	protected void initEvent() {

		ib_clear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				autoCompView.setText("");
			}
		});

		autoCompView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				fetchRequest(s.toString());

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		/*
		 * pacAdapter = new PlacesAutoCompleteAdapter(ScenicsListActivity.this,
		 * android.R.layout.simple_dropdown_item_1line);
		 * pacAdapter.setUpdatehandler(handler);
		 * autoCompView.setAdapter(pacAdapter);
		 */

		autoCompView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) { // 判断是否是 Search 键
						if (actionId == EditorInfo.IME_ACTION_SEARCH) { // 隐藏软键盘

							Logot.outError("SEARCH", autoCompView.getText().toString()
									.trim());
							fetchRequest(autoCompView.getText().toString()
									.trim());

							InputMethodManager imm = (InputMethodManager) v
									.getContext().getSystemService(
											Context.INPUT_METHOD_SERVICE);
							if (imm.isActive()) {
								imm.hideSoftInputFromWindow(
										v.getApplicationWindowToken(), 0);
							}

							((InputMethodManager) autoCompView.getContext()
									.getSystemService(
											Context.INPUT_METHOD_SERVICE))
									.hideSoftInputFromWindow(getCurrentFocus()
											.getWindowToken(),
											InputMethodManager.HIDE_NOT_ALWAYS);

							autoCompView.dismissDropDown();

							return true;
						}
						return false;
					}

				});
		layout_city.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				startActivityForResult(new Intent(context,
						CityListActivity.class), ACTION_REQUEST_SELEC_CITY);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
			}
		});
	}

	View headView = null;

	private void refreshListView() {
		if (poiList.size() == 0) {
			headView = getLayoutInflater().inflate(
					R.layout.include_scenics_head, null);
			if (headView != null) {

				scenicList.removeHeaderView(headView);
			}
			scenicList.setAdapter(null);
			scenicList.addHeaderView(headView);

			TextView tv_scenics = (TextView) headView
					.findViewById(R.id.tv_scenics_name);
			Button add = (Button) headView.findViewById(R.id.btn_scenics_add);

			tv_scenics.setText(autoCompView.getText().toString().trim());

			add.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ScenicsListActivity.this,
							PlanPublishActivity.class);
					intent.putExtra("address", autoCompView.getText()
							.toString().trim());
					intent.putExtra("scenicId", "0");
					startActivity(intent);
					finish();
				}
			});
		}

		autoCompView.dismissDropDown();
		scenicsAdapter = new ScenicsAdapter(getLayoutInflater(), poiList);
		scenicList.setAdapter(scenicsAdapter);
		scenicList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				ScenicsEntity poiItem = (ScenicsEntity) adapterView
						.getItemAtPosition(position); // 获取搜索到地址的相关热点信息
				Intent intent = new Intent(ScenicsListActivity.this,
						PlanPublishActivity.class);
				intent.putExtra("address", poiItem.getpName());
				intent.putExtra("scenicId", poiItem.getScenicId());
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {

			switch (requestCode) {
			case ACTION_REQUEST_SELEC_CITY:
				selectCityBt.setText(data.getStringExtra("CityName"));
				String startCityStr = selectCityBt.getText().toString();
				break;

			case 31:
				selectCityBt.setText(data.getStringExtra("CityName"));
				break;
			}
		}
	}

	@Override
	public void onClick(View view) {

	}

	@Override
	protected void init() {
		fetchRequest(Constants.PLACE_NOW);
	}

	public void fetchRequest(String pName) {

		String url = AsyncHttpManager.PLACE_SEARCH_URL;

		RequestParams params = new RequestParams();
		params.put("pName", pName);

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						poiList = new ArrayList<ScenicsEntity>();
					}

					@Override
					public void onEmpty() {
						super.onEmpty();
						poiList = new ArrayList<ScenicsEntity>();
						refreshListView();
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();

						try {
							String json = new JSONObject(getResult())
									.getString("message");

							poiList = new Gson().fromJson(json,
									new TypeToken<List<ScenicsEntity>>() {
									}.getType());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						refreshListView();
					}
				});

	}

}
