/**
 * @Package com.manyouren.android    
 * @Title: SplashActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-8-6 下午2:38:28 
 * @version V1.0   
 */
package com.manyouren.manyouren;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;

import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.PreferConfig;
import com.manyouren.manyouren.service.jpush.JPushUtils;
import com.manyouren.manyouren.ui.usernew.AccountLoginActivity;
import com.manyouren.manyouren.util.PreferenceUtils;
import com.umeng.update.UmengUpdateAgent;

/**
 * 欢迎界面
 *
 * @author firefist_wei
 * @date 2014-8-6 下午2:38:28
 * 
 */
public class SplashActivity extends FragmentActivity {

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = SplashActivity.this;

		setContentView(R.layout.welcome_splash);
		
		boolean needLogin = PreferenceUtils.getBoolean(mContext,
				PreferConfig.PREFER_REQUEST_NEWACCOUNT, true);
		if (needLogin) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Log.e("TAG","run");
					startActivity(new Intent(mContext, AccountLoginActivity.class));
					finish();
				}
			}, 1500);
			return;
		} 
		
		Date now = new Date();
		long lastTime = PreferenceUtils.getLong(mContext, "lastUseTime", now.getTime());
		long dis = now.getTime() - lastTime;
		if(dis>1000*60*30 || JPushUtils.isMainForeground==false){
			Constants.USER_ID = String.valueOf(PreferenceUtils.getString(mContext,
					PreferConfig.PREFER_INITIAL_USERID));
			startActivity(new Intent(mContext, HomeActivity.class));
			finish();
		}else{
			
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					checkLoginState();
					// Intent intent = new Intent
					// (mContext,AccountLoginActivity.class);
					// startActivity(intent);
					// finish();
				}
			}, 1500);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void checkLoginState() {
		boolean needLogin = PreferenceUtils.getBoolean(mContext,
				PreferConfig.PREFER_REQUEST_NEWACCOUNT, true);
		if (!needLogin) {
			UmengUpdateAgent.update(this);
			Constants.USER_ID = String.valueOf(PreferenceUtils.getString(mContext,
					PreferConfig.PREFER_INITIAL_USERID));
			startActivity(new Intent(mContext, HomeActivity.class));
		} else {
			startActivity(new Intent(mContext, AccountLoginActivity.class));
		}
		finish();
	}
}
