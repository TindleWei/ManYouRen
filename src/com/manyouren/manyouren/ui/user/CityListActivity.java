package com.manyouren.manyouren.ui.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import roboguice.inject.InjectView;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.baseold.BaActivity;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.core.user.CharacterParser;
import com.manyouren.manyouren.core.user.PinyinComparator;
import com.manyouren.manyouren.core.user.SortAdapter;
import com.manyouren.manyouren.service.location.MyLocation;
import com.manyouren.manyouren.ui.user.SideBar.OnTouchingLetterChangedListener;
import com.manyouren.manyouren.util.Logot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CityListActivity extends BaseActivity {

	@InjectView(R.id.listview)
	private ListView mListView;

	@InjectView(R.id.sidebar)
	private SideBar mSideBar;

	@InjectView(R.id.tv_dialog)
	private TextView tv_dialog;

	@InjectView(R.id.et_search)
	private ClearEditText mClearEditText;

	private SortAdapter mAdapter;

	private TextView tv_city_location;

	// 汉字转换成拼音的类
	private CharacterParser characterParser;

	// 根据拼音来排列ListView里面的数据类
	private PinyinComparator pinyinComparator;

	private List<SortCity> cityList;

	Context mContext;

	Handler myHandler = new Handler() {
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case 1:
				tv_city_location.setText(Constants.PLACE_NOW);
				tv_city_location.setEnabled(true);
				tv_city_location.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra("CityName", tv_city_location.getText()
								.toString());
						setResult(RESULT_OK, intent);
						finish();
					}
				});
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_city);

		mContext = this;

		setActionBar("城市");

		MyLocation.getLocation(context, 2, myHandler);

		initView();
		initEvent();
		init();
	}

	@Override
	protected void initView() {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();

		mSideBar.setTextView(tv_dialog);

		View headView = LayoutInflater.from(context).inflate(
				R.layout.header_city_location, null);
		tv_city_location = (TextView) headView
				.findViewById(R.id.tv_city_location);

		mListView.addHeaderView(headView);

	}

	@Override
	protected void initEvent() {
		// 设置右侧触摸监听
		mSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				if (mAdapter == null)
					return;
				int position = mAdapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					mListView.setSelection(position);
				}
			}
		});

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			public void afterTextChanged(Editable s) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position < mListView.getHeaderViewsCount()) {
					if (!tv_city_location.isEnabled()) {
						return;
					}
					Intent intent = new Intent();
					intent.putExtra("CityName", tv_city_location.getText()
							.toString());
					setResult(RESULT_OK, intent);
					finish();
				} else {
					Intent intent = new Intent();
					intent.putExtra(
							"CityName",
							cityList.get(
									position - mListView.getHeaderViewsCount())
									.getCity());
					setResult(RESULT_OK, intent);
					finish();
				}
			}

		});
	}

	@Override
	protected void init() {
		cityList = filledData(mContext.getResources().getStringArray(
				R.array.citys));

		// 根据a-z进行排序源数据
		Collections.sort(cityList, pinyinComparator);
		mAdapter = new SortAdapter(mContext, cityList);

		mListView.setAdapter(mAdapter);

	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<SortCity> filledData(String[] data) {
		List<SortCity> mSortList = new ArrayList<SortCity>();

		for (int i = 0; i < data.length; i++) {
			SortCity sortModel = new SortCity();
			sortModel.setCity(data[i]);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(data[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetter(sortString.toUpperCase());
			} else {
				sortModel.setSortLetter("#");
			}
			mSortList.add(sortModel);
		}

		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		cityList = getFilterData(filterStr);

		if (cityList == null || cityList.isEmpty())
			cityList = filledData(mContext.getResources().getStringArray(
					R.array.citys));
		// 根据a-z进行排序
		Collections.sort(cityList, pinyinComparator);
		mAdapter.updateListView(cityList);
	}

	public List<SortCity> getFilterData(String filterStr) {
		List<SortCity> filterDateList = new ArrayList<SortCity>();

		if (TextUtils.isEmpty(filterStr)) {

		} else {
			filterDateList.clear();
			for (SortCity sortModel : cityList) {
				String name = sortModel.getCity();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}
		return filterDateList;
	}

	public class SortCity implements Serializable {

		private String city;
		private String sortLetter;

		/**
		 * @return the city
		 */
		public String getCity() {
			return city;
		}

		/**
		 * @param city
		 *            the city to set
		 */
		public void setCity(String city) {
			this.city = city;
		}

		/**
		 * @return the sortLetter
		 */
		public String getSortLetter() {
			return sortLetter;
		}

		/**
		 * @param sortLetter
		 *            the sortLetter to set
		 */
		public void setSortLetter(String sortLetter) {
			this.sortLetter = sortLetter;
		}

	}

}