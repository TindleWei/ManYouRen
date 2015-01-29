package com.manyouren.manyouren;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.Session;
import com.avos.avoscloud.SessionManager;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.PreferConfig;
import com.manyouren.manyouren.service.AVService;
import com.manyouren.manyouren.service.jpush.JPushUtils;
import com.manyouren.manyouren.service.location.MyLocation;
import com.manyouren.manyouren.ui.chat.chatnew.ChatFragment;
import com.manyouren.manyouren.ui.chatnew.avobject.User;
import com.manyouren.manyouren.ui.discovery.NearbyFragment;
import com.manyouren.manyouren.ui.plan.PlanFragment;
import com.manyouren.manyouren.ui.user.UserFragment;
import com.manyouren.manyouren.util.PreferenceUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * @Title: HomeTabActivity.java
 * @Package com.manyouren.android.ui
 * @author firefist_wei firefist.wei@gmail.com
 * @date 2014-6-5 下午5:57:04
 * @version V1.0
 */

public class HomeActivity extends BaseActivity implements OnClickListener {

	public static final int TAB_PLAN = 0;
	public static final int TAB_DISC = 1;
	public static final int TAB_CHAT = 2;
	public static final int TAB_ME = 3;

	@InjectView(R.id.viewpager)
	private ViewPager viewPager;

	@InjectView(R.id.main_tab_plan)
	private RadioButton main_tab_plan;

	@InjectView(R.id.main_tab_chat)
	private RadioButton main_tab_chat;

	@InjectView(R.id.main_tab_disc)
	private RadioButton main_tab_disc;

	@InjectView(R.id.main_tab_me)
	private RadioButton main_tab_me;

	int mCurTabId = 0;

	FragmentAdapter adapter = null;

	PlanFragment planFragment = null;

	ChatFragment chatFragment = null;

	NearbyFragment discFragment = null;

	UserFragment meFragment = null;

	public static ArrayList<Fragment> pagerItemList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);

		checkLocation();
		initView();
		initEvent();
		init();

		AVAnalytics.trackAppOpened(getIntent());
		AVService.initPushService(this);

		// ObjectId设为PeerId
		if (AVUser.getCurrentUser() != null) {
			String selfId = AVUser.getCurrentUser().getObjectId();
			Session session = SessionManager.getInstance(selfId);
			List<String> peerIds = new LinkedList<String>();
			peerIds.add(selfId);
			session.open(peerIds);

			RootApplication.registerUserCache(User.curUser());
		}

	}

	private void checkLocation() {
		MyLocation.getLocation(context, 0);

		if (Constants.PLACE_NOW.equals("")) {
			Constants.PLACE_NOW = PreferenceUtils.getString(context,
					PreferConfig.PREFER_INITIAL_LOCATION, "西安");
		}
	}

	@Override
	protected void initView() {
	}

	@Override
	protected void initEvent() {
		main_tab_plan.setOnClickListener(this);
		main_tab_chat.setOnClickListener(this);
		main_tab_disc.setOnClickListener(this);
		main_tab_me.setOnClickListener(this);

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int id) {
				switch (id) {
				case TAB_PLAN:
					main_tab_plan.setChecked(true);
					break;
				case TAB_DISC:
					main_tab_disc.setChecked(true);
					break;
				case TAB_CHAT:
					main_tab_chat.setChecked(true);
					break;
				case TAB_ME:
					main_tab_me.setChecked(true);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	protected void init() {

		planFragment = new PlanFragment();
		chatFragment = new ChatFragment();
		discFragment = new NearbyFragment();
		meFragment = new UserFragment();

		pagerItemList = new ArrayList<Fragment>();
		pagerItemList.add(planFragment);
		pagerItemList.add(discFragment);
		pagerItemList.add(chatFragment);
		pagerItemList.add(meFragment);

		adapter = new FragmentAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);

		if (getIntent().getExtras() != null) {
			if (getIntent().getStringExtra("show_page") != null
					&& getIntent().getStringExtra("show_page").equals("1")) {
				viewPager.setCurrentItem(2);
				mCurTabId = 2;
				main_tab_chat.setChecked(true);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_tab_plan:
			viewPager.setCurrentItem(TAB_PLAN);
			break;
		case R.id.main_tab_chat:
			viewPager.setCurrentItem(TAB_CHAT);
			break;
		case R.id.main_tab_disc:
			viewPager.setCurrentItem(TAB_DISC);
			break;
		case R.id.main_tab_me:
			viewPager.setCurrentItem(TAB_ME);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(context);
		JPushUtils.isMainForeground = true;
		Date date = new Date();
		PreferenceUtils.putLong(context, "lastUseTime", date.getTime());
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(context);
		JPushUtils.isMainForeground = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Date date = new Date();
		PreferenceUtils.putLong(context, "lastUseTime", date.getTime());
	}

	long exitTime = 0;

	@Override
	public void onBackPressed() {

		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次,退出程序!",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
		}
	}

	public class FragmentAdapter extends FragmentPagerAdapter {
		public final static int TAB_COUNT = 4;

		public FragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			Fragment fragment = null;

			if (position < pagerItemList.size()) {
				fragment = pagerItemList.get(position);
			} else {
				fragment = pagerItemList.get(0);
			}
			if (fragment == null) {
				if (position == 0) {
					fragment = new PlanFragment();
				} else if (position == 1) {
					fragment = new NearbyFragment();
				} else if (position == 2) {
					fragment = new ChatFragment();
				} else if (position == 3) {
					fragment = new UserFragment();
				}
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return pagerItemList.size();
		}
	}

}
