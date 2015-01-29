/**
 * @Package com.manyouren.android.ui.user    
 * @Title: SettingsActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-13 下午5:19:27 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.user;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.github.kevinsawicki.wishlist.LightDialog;
import com.github.kevinsawicki.wishlist.Toaster;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.PreferConfig;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpLoader;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.ui.CircleTransform;
import com.manyouren.manyouren.ui.usernew.AccountLoginActivity;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.PackageUtils;
import com.manyouren.manyouren.util.PreferenceUtils;
import com.manyouren.manyouren.widget.SlipButton;
import com.manyouren.manyouren.widget.SlipButton.OnChangedListener;
import com.squareup.picasso.Picasso;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

public class SettingsActivity extends BaseActivity implements OnClickListener {

	private UserEntity userEntity;

	TextView tv_version;

	Button btn_logout;

	TextView checkVersion;

	TextView feedback;

	private SlipButton pushTipBtn;

	private SlipButton hideUserBtn;

	String PUSH_TIP = "push_tip";
	String HIDE_TIP = "hide_tip";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_setting);

		setActionBar("设置");

		initView();
		initEvent();
		init();
	}

	@Override
	protected void init() {

		try {
			userEntity = UserController.getUserById(context,
					Long.valueOf(Constants.USER_ID));
		} catch (Exception e) {
		}

		try {
			tv_version.setText("版本号 " + getVersionName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		String version = packInfo.versionName;
		return version;
	}

	@Override
	protected void initView() {

		tv_version = (TextView) findViewById(R.id.tv_version);
		checkVersion = (TextView) findViewById(R.id.checkVersion_tv);
		feedback = (TextView) findViewById(R.id.feedback_tv);
		btn_logout = (Button) findViewById(R.id.btn_logout);
		pushTipBtn = (SlipButton) findViewById(R.id.push_switch_Btn);
		hideUserBtn = (SlipButton) findViewById(R.id.hide_switch_Btn);

	}

	@Override
	protected void initEvent() {
		tv_version.setOnClickListener(this);
		btn_logout.setOnClickListener(this);
		feedback.setOnClickListener(this);
		checkVersion.setOnClickListener(this);

		pushTipBtn.setOnChangedListener(PUSH_TIP, mOnChangedListener);
		boolean isPushTip = PreferenceUtils.getBoolean(context, "push_notify",
				true);
		pushTipBtn.setChecked(isPushTip);

		hideUserBtn.setOnChangedListener(HIDE_TIP, mOnChangedListener);
		boolean isHideTip = PreferenceUtils.getBoolean(context,
				"hide_location", false);
		hideUserBtn.setChecked(isHideTip);
	}

	private final OnChangedListener mOnChangedListener = new OnChangedListener() {
		@Override
		public void OnChanged(String name, final boolean isChecked) {
			if (name.equals(PUSH_TIP)) {
				PreferenceUtils.putBoolean(context, "push_notify", isChecked);
			} else if (name.equals(HIDE_TIP)) {
				PreferenceUtils.putBoolean(context, "hide_location", isChecked);

				String url = AsyncHttpManager.USER_HIDE_URL;
				RequestParams params = new RequestParams();
				params.put("invisible", isChecked ? "0" : "1"); // 0 隐身 1 取消隐身
				params.put("userId", PreferenceHelper.getUserId());
				AsyncHttpManager.getClient(context).post(url, params,
						new AsyncHttpHandler() {
							public void onSuccessed() {
								hideUserBtn.setChecked(isChecked);
							}
						});
			}
		}
	};

	public void onClick(View v) {
		v.setPressed(true);
		switch (v.getId()) {
		case R.id.checkVersion_tv:

			fetchNewVersion();
			break;
		case R.id.feedback_tv:
			// umeng
			FeedbackAgent agent = new FeedbackAgent(context);
			agent.startFeedbackActivity();
			break;

		case R.id.btn_logout:
			final LightDialog lightDialog = LightDialog.create(context, "是否退出",
					"您确定退出登录吗?");
			lightDialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							lightDialog.cancel();
						}

					});
			lightDialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							PreferenceUtils.putBoolean(context,
									PreferConfig.PREFER_REQUEST_NEWACCOUNT,
									true);
							lightDialog.cancel();
							AVUser.logOut();
							finish();

							startActivity(new Intent(context,
									AccountLoginActivity.class)
									.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
											| Intent.FLAG_ACTIVITY_NEW_TASK));
							// the codes below is no use
							// .addFlags(FLAG_ACTIVITY_CLEAR_TOP |
							// FLAG_ACTIVITY_SINGLE_TOP)
						}

					});
			lightDialog.show();
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void fetchNewVersion() {
		String url = AsyncHttpManager.CHECK_VERSION_URL;
		RequestParams params = new RequestParams();
		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onSuccessed() {
						super.onSuccessed();
						String code = "";
						try {
							code = new JSONObject(getResult()).getJSONObject(
									"message").getString("newestVersion");

						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (code != null && !code.equals(""))
							checkUpdate(code);

					}
				});
	}

	public void checkUpdate(String code) {
		try{
			int serverCode = Integer.valueOf(code);
			if (getVersionCode(context) > serverCode) {
				Toaster.showShort(activity, "版本可更新Version:" + serverCode);
			} else {
				Toaster.showShort(activity, "当前已是最新版本");
			}
		} catch (Exception e){	
		}
	}

	public static int getVersionCode(Context context)// 获取版本号(内部识别号)
	{
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

}
