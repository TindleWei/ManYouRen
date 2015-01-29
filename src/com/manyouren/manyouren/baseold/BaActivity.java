package com.manyouren.manyouren.baseold;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.ui.DialogFlippingLoading;
import com.manyouren.manyouren.util.NetworkUtils;
import com.umeng.analytics.MobclickAgent;

public abstract class BaActivity extends FragmentActivity {
	protected NetworkUtils mNetWorkUtils;
	protected DialogFlippingLoading mLoadingDialog;

	/**
	 * 屏幕的宽度、高度、密度
	 */
	protected int mScreenWidth;
	protected int mScreenHeight;
	protected float mDensity;

	protected List<AsyncTask<Void, Void, Boolean>> mAsyncTasks = new ArrayList<AsyncTask<Void, Void, Boolean>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mNetWorkUtils = new NetworkUtils(this);
		mLoadingDialog = new DialogFlippingLoading(this, "请求提交中");

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onStop();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		clearAsyncTask();
		super.onDestroy();
	}

	/** 初始化视图 **/
	protected abstract void initViews();

	/** 初始化事件 **/
	protected abstract void initEvents();

	protected void putAsyncTask(AsyncTask<Void, Void, Boolean> asyncTask) {
		mAsyncTasks.add(asyncTask.execute());
	}

	protected void clearAsyncTask() {
		Iterator<AsyncTask<Void, Void, Boolean>> iterator = mAsyncTasks
				.iterator();
		while (iterator.hasNext()) {
			AsyncTask<Void, Void, Boolean> asyncTask = iterator.next();
			if (asyncTask != null && !asyncTask.isCancelled()) {
				asyncTask.cancel(true);
			}
		}
		mAsyncTasks.clear();
	}

	protected void showLoadingDialog(String text) {
		if (text != null) {
			mLoadingDialog.setText(text);
		}
		mLoadingDialog.show();
	}

	protected void dismissLoadingDialog() {
		if (mLoadingDialog.isShowing()) {
			mLoadingDialog.dismiss();
		}
	}


	/** 显示自定义Toast提示(来自res) **/
	protected void showCustomToast(int resId) {
		View toastRoot = LayoutInflater.from(BaActivity.this).inflate(
				R.layout.toast_common, null);
		((TextView) toastRoot.findViewById(R.id.toast_text))
				.setText(getString(resId));
		Toast toast = new Toast(BaActivity.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}

	/** 显示自定义Toast提示(来自String) **/
	protected void showCustomToast(String text) {
		View toastRoot = LayoutInflater.from(BaActivity.this).inflate(
				R.layout.toast_common, null);
		((TextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
		Toast toast = new Toast(BaActivity.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}


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
		overridePendingTransition(R.anim.left_in,
				R.anim.left_out);
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
		overridePendingTransition(R.anim.left_in,
				R.anim.left_out);
	}

	/** 含有标题和内容的对话框 **/
	protected AlertDialog showAlertDialog(String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message).show();
		return alertDialog;
	}

	/** 含有标题、内容、两个按钮的对话框 **/
	protected AlertDialog showAlertDialog(String title, String message,
			String positiveText,
			DialogInterface.OnClickListener onPositiveClickListener,
			String negativeText,
			DialogInterface.OnClickListener onNegativeClickListener) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message)
				.setPositiveButton(positiveText, onPositiveClickListener)
				.setNegativeButton(negativeText, onNegativeClickListener)
				.show();
		return alertDialog;
	}

	/** 含有标题、内容、图标、两个按钮的对话框 **/
	protected AlertDialog showAlertDialog(String title, String message,
			int icon, String positiveText,
			DialogInterface.OnClickListener onPositiveClickListener,
			String negativeText,
			DialogInterface.OnClickListener onNegativeClickListener) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message).setIcon(icon)
				.setPositiveButton(positiveText, onPositiveClickListener)
				.setNegativeButton(negativeText, onNegativeClickListener)
				.show();
		return alertDialog;
	}

	/** 默认退出 **/
	protected void defaultFinish() {
		super.finish();
	}
	
	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(R.anim.right_in,
				R.anim.right_out);
	}
	
	/*@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getRepeatCount() == 0) {
			this.finish();
			overridePendingTransition(R.anim.right_in,
					R.anim.right_out);
		}
		return super.dispatchKeyEvent(event);

	}*/
	
	/**
	 * 设置ActionBar
	 * 
	 */
	public void setActionBar(String title) {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		actionBar.setLogo(R.drawable.ic_actionbar_logo_white);
		actionBar.setTitle(title);
	}
	
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
}
