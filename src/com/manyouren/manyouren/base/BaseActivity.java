/**
 * @Package com.manyouren.android.ui    
 * @Title: BaseActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-9-3 下午4:07:28 
 * @version V1.0   
 */
package com.manyouren.manyouren.base;

import java.io.Serializable;
import roboguice.activity.RoboFragmentActivity;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.manyouren.manyouren.R;
import com.umeng.analytics.MobclickAgent;

/**
 * BaseActivity
 * 
 * @author firefist_wei@foxmail.com
 * @date 2014-11-13
 * 
 */

public abstract class BaseActivity extends RoboFragmentActivity {

	protected Context context;
	
	protected Activity activity;
	
	protected ProgressDialog pd;
	
	/**
	 * 屏幕的宽度、高度、密度
	 */
	protected int mScreenWidth;
	protected int mScreenHeight;
	protected float mDensity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		activity = this;
		
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(context);
	}
	
	@Override
	protected void onPause() {
		super.onStop();
		MobclickAgent.onPause(context);
	}

	/** 初始化视图 **/
	protected abstract void initView();

	/** 初始化事件 **/
	protected abstract void initEvent();

	/** 初始化数据 **/
	protected abstract void init();

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.right_in, R.anim.right_out);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Get intent extra
	 */
	@SuppressWarnings("unchecked")
	protected <V extends Serializable> V getSerializableExtra(final String name) {
		return (V) getIntent().getSerializableExtra(name);
	}

	/**
	 * Get intent extra
	 */
	protected int getIntExtra(final String name) {
		return getIntent().getIntExtra(name, -1);
	}

	/**
	 * Get intent extra
	 */
	protected String getStringExtra(final String name) {
		return getIntent().getStringExtra(name);
	}

	/**
	 * Get intent extra
	 */
	protected String[] getStringArrayExtra(final String name) {
		return getIntent().getStringArrayExtra(name);
	}

	/** 通过Class跳转界面 **/
	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	/** 含有Bundle通过Class跳转界面 **/
	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	/** 通过Action跳转界面 **/
	protected void startActivity(String action) {
		startActivity(action, null);
	}

	/** 含有Bundle通过Action跳转界面 **/
	protected void startActivity(String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	/**
	 * 设置ActionBar
	 */
	public void setActionBar(String title) {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		actionBar.setLogo(R.drawable.ic_action_logo);
		actionBar.setTitle(title);
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}
	
	@Override
	protected void onDestroy() {
		if(pd!=null && pd.isShowing())
			pd.dismiss();
		super.onDestroy();
	}

}

