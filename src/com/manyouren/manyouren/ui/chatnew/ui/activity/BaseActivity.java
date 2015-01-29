package com.manyouren.manyouren.ui.chatnew.ui.activity;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.ui.chatnew.util.Utils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.WindowManager;

/**
 * Created by lzw on 14-9-17.
 */
public class BaseActivity extends FragmentActivity {
	Activity ctx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		ctx = this;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	void hideSoftInputView() {
		Utils.hideSoftInputView(this);
	}

	void setSoftInputMode() {
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	void initActionBar() {
		initActionBar(null);
	}

	void initActionBar(String title) {
		ActionBar actionBar = getActionBar();
		if (title != null) {
			actionBar.setTitle(title);
		}
		actionBar.setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		actionBar.setLogo(R.drawable.ic_action_logo);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	void initActionBar(int id) {
		initActionBar(RootApplication.instance.getString(id));
	}
	
	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}
}
