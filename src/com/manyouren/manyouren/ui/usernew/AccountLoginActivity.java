package com.manyouren.manyouren.ui.usernew;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import roboguice.inject.InjectView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.github.kevinsawicki.wishlist.Toaster;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.HomeActivity;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.PreferConfig;
import com.manyouren.manyouren.controller.ChatController;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpLoader;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.ui.TextWatcherAdapter;
import com.manyouren.manyouren.util.PreferenceUtils;
import com.manyouren.manyouren.util.ScreenUtils;
import com.manyouren.manyouren.util.WidgetUtils;

/**
 * 
 * @author weizepeng
 * @last 2014-11-13
 * 
 */
public class AccountLoginActivity extends BaseActivity {

	@InjectView(R.id.layout_back)
	private LinearLayout layout_back;

	@InjectView(R.id.layout_relative_header)
	private RelativeLayout layout_relative_header;

	@InjectView(R.id.layout_editbox)
	private LinearLayout layout_editbox;

	@InjectView(R.id.layout_btns)
	private RelativeLayout layout_btns;

	@InjectView(R.id.et_password)
	private EditText passwordText;

	@InjectView(R.id.b_signin)
	private Button signInButton;

	@InjectView(R.id.b_signup)
	private Button signUpButton;

	@InjectView(R.id.btn_next)
	private Button btn_next;

	@InjectView(R.id.iv_header_logo)
	private ImageView iv_header_logo;

	@InjectView(R.id.tv_header_title)
	private TextView tv_header_title;

	private final TextWatcher watcher = validationTextWatcher();

	private AutoCompleteTextView emailText;

	private ObjectAnimator objectAnimator;

	private AnimatorSet animatorSet;

	public static int FLAG_MODE = 0; // initial 0; login 1; signup 2;

	private String email;

	private String password;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login_main);
		checkFirstUse();
		resetView();
	}

	/**
	 * 检测是否开启引导界面
	 */
	public void checkFirstUse() {
		try {
			if (PreferenceUtils.getBoolean(context, "firstUse", true)) {
				// 开始引导界面
				/*
				 * Intent intent = new Intent(AccountLoginActivity.this,
				 * YinDaoActivity.class); startActivity(intent); finish();
				 */
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 重置界面
	 */
	public void resetView() {
		FLAG_MODE = 0;
		initView();
		initEvent();
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (FLAG_MODE != 0) {
			changeHeaderLayout(1);
		}
		resetView();
	}

	@Override
	protected void init() {
		updateUIWithValidation();
		initAnimValue();
	}

	@Override
	protected void initView() {
		emailText = (AutoCompleteTextView) this.findViewById(R.id.et_email);
		emailText.setText(PreferenceUtils.getString(context,
				PreferConfig.PREFER_INITIAL_EMAIL, ""));
	}

	@Override
	protected void initEvent() {
		emailText.addTextChangedListener(watcher);
		passwordText.addTextChangedListener(watcher);

		emailText.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line,
				userEmailAccounts()));

		passwordText.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event != null && KeyEvent.ACTION_DOWN == event.getAction()
						&& keyCode == KeyEvent.KEYCODE_ENTER
						&& signInButton.isEnabled()) {
					handleLogin(signInButton);
					return true;
				}
				return false;
			}
		});

		passwordText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE
						&& signInButton.isEnabled()) {
					handleLogin(signInButton);
				}
				return false;
			}
		});

		layout_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				changeHeaderLayout(0);
			}
		});

	}

	/**
	 * @date: 7/1 just leave this and finish it in future
	 * 
	 * @return List<String>
	 */
	private List<String> userEmailAccounts() {
		List<String> list = new ArrayList<String>();
		String emailString = PreferenceUtils.getString(context,
				PreferConfig.PREFER_INITIAL_EMAIL);
		if (email != null)
			list.add(emailString);
		return list;
	}

	/**
	 * 监控密码的输入
	 */
	private TextWatcher validationTextWatcher() {
		return new TextWatcherAdapter() {
			public void afterTextChanged(final Editable gitDirEditText) {
				updateUIWithValidation();
			}
		};
	}

	/**
	 * 密码的输入，引起按钮的响应
	 */
	private void updateUIWithValidation() {
		final boolean populated = populated(emailText)
				&& populated(passwordText);
		btn_next.setEnabled(populated);
	}

	/**
	 * 判断文字的长度
	 */
	private boolean populated(final EditText editText) {
		return editText.getText().toString().trim().length() > 0;
	}

	/**
	 * 验证邮箱的正则表达式
	 */
	private boolean isEmailEnabled(String email) {
		Pattern pattern = Pattern
				.compile("^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})");
		Matcher mc = pattern.matcher(email);
		return mc.matches();
	}

	/*
	 * Logo图片位置坐标 Logo x0, y0 是大Logo Logo x1, y1 是小Logo
	 */
	float LOGO_X0, LOGO_X1, LOGO_Y0, LOGO_Y1;
	/* 标题栏的Y0, Y1 */
	float TITLEBAR_Y0, TITLEBAR_Y1;
	int ANIM_TIME = 600;

	/**
	 * 位置坐标初始化
	 */
	private void initAnimValue() {
		TITLEBAR_Y0 = 0f;
		TITLEBAR_Y1 = -ScreenUtils.getIntPixels(context, 220 - 56);

		LOGO_X0 = iv_header_logo.getX();
		LOGO_X1 = ScreenUtils.getIntPixels(context, 8);
		LOGO_Y0 = iv_header_logo.getY();
		LOGO_Y1 = ScreenUtils.getIntPixels(context, 40);
	}

	/**
	 * 整个动画的改变函数
	 */
	private void changeHeaderLayout(int flag_mode) {

		if (layout_editbox.isShown() && emailText.isShown()) {
			WidgetUtils.hideKeyBoard(AccountLoginActivity.this, emailText);
		}

		if (FLAG_MODE == 0) {
			/* 是初始界面,跳到登录或注册 */
			FLAG_MODE = flag_mode;
			signInButton.setEnabled(false);
			signUpButton.setEnabled(false);
			updateUIWithValidation();
			layout_editbox.setVisibility(View.VISIBLE);

			/* 首先是 HeaderLayout */
			objectAnimator = ObjectAnimator.ofFloat(layout_relative_header,
					"translationY", 0f,
					-ScreenUtils.getIntPixels(context, 220 - 56));
			objectAnimator.setDuration(600);
			objectAnimator.setInterpolator(new DecelerateInterpolator());
			objectAnimator.start();

			/* Logo 也要随着 HeaderLayout 的上移而改变 */
			animatorSet = new AnimatorSet();
			iv_header_logo
					.setPivotY(layout_relative_header.getLayoutParams().height);
			iv_header_logo.setPivotX(0);
			animatorSet.playTogether(
					ObjectAnimator.ofFloat(iv_header_logo, "scaleX", 1f, 0.4f),
					ObjectAnimator.ofFloat(iv_header_logo, "scaleY", 1f, 0.4f),
					ObjectAnimator.ofFloat(iv_header_logo, "X",
							iv_header_logo.getX(),
							ScreenUtils.getIntPixels(context, 32)),
					ObjectAnimator.ofFloat(iv_header_logo, "Y",
							iv_header_logo.getY(),
							ScreenUtils.getIntPixels(context, 40)));
			animatorSet.setDuration(600);
			animatorSet.setInterpolator(new DecelerateInterpolator());
			animatorSet.start();

			/* 接下来是 EidtboxLayout 的上移 */
			AnimatorSet animatorSet2 = new AnimatorSet();
			animatorSet2.playTogether(
					ObjectAnimator.ofFloat(layout_editbox, "translationY", 0f,
							-ScreenUtils.getIntPixels(context, 128)),
					ObjectAnimator.ofFloat(layout_editbox, "alpha", 1f, 1f));
			animatorSet2.setDuration(600);
			animatorSet2.setInterpolator(new DecelerateInterpolator());
			animatorSet2.start();

			/* 接着是 登陆 注册 按钮的下移 */
			AnimatorSet animatorSet3 = new AnimatorSet();
			animatorSet3.playTogether(
					ObjectAnimator.ofFloat(layout_btns, "translationY", 0f,
							ScreenUtils.getIntPixels(context, 400)),
					ObjectAnimator.ofFloat(layout_btns, "alpha", 1f, 0f));
			animatorSet3.setDuration(600);
			animatorSet3.setInterpolator(new AccelerateInterpolator());
			animatorSet3.start();

			// 下来要弹出软键盘
			emailText.postDelayed(new Runnable() {

				@Override
				public void run() {
					// 设置光标的位置，出现在最后
					emailText.setSelection(emailText.getText().length());
					emailText.setFocusable(true);
					emailText.setFocusableInTouchMode(true);
					emailText.requestFocus();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(emailText,
							InputMethodManager.SHOW_IMPLICIT);
				}
			}, 600);

			// 这里要出现底部按钮
			btn_next.postDelayed(new Runnable() {

				@Override
				public void run() {
					AnimatorSet animatorSet3 = new AnimatorSet();
					animatorSet3.playTogether(ObjectAnimator.ofFloat(btn_next,
							"alpha", 0f, 1f));

					animatorSet3.setDuration(400);
					animatorSet3.setInterpolator(new AccelerateInterpolator());
					animatorSet3.start();

					btn_next.setVisibility(View.VISIBLE);
					layout_back.setVisibility(View.VISIBLE);
				}
			}, 600);

		} else { // 不是初始界面,跳到初始界面

			FLAG_MODE = flag_mode; // 0
			signInButton.setEnabled(true);
			signUpButton.setEnabled(true);

			updateUIWithValidation();
			layout_editbox.setVisibility(View.INVISIBLE);

			objectAnimator = ObjectAnimator.ofFloat(layout_relative_header,
					"translationY",
					-ScreenUtils.getIntPixels(context, 220 - 56), 0);
			objectAnimator.setDuration(600);
			objectAnimator.setInterpolator(new DecelerateInterpolator());
			objectAnimator.start();

			animatorSet = new AnimatorSet();
			iv_header_logo
					.setPivotY(layout_relative_header.getLayoutParams().height);
			iv_header_logo.setPivotX(0);
			animatorSet.playTogether(
					ObjectAnimator.ofFloat(iv_header_logo, "scaleX", 0.4f, 1f),
					ObjectAnimator.ofFloat(iv_header_logo, "scaleY", 0.4f, 1f),
					ObjectAnimator.ofFloat(iv_header_logo, "X",
							iv_header_logo.getX(), mScreenWidth / 2
									- ScreenUtils.getIntPixels(context, 48)),
					ObjectAnimator.ofFloat(iv_header_logo, "Y",
							iv_header_logo.getY(),
							ScreenUtils.getIntPixels(context, 84)));

			animatorSet.setDuration(600);
			animatorSet.setInterpolator(new DecelerateInterpolator());
			animatorSet.start();

			// 接下来是EidtBox
			AnimatorSet animatorSet2 = new AnimatorSet();
			animatorSet2.playTogether(
					ObjectAnimator.ofFloat(layout_editbox, "translationY",
							-ScreenUtils.getIntPixels(context, 128), 0f),
					ObjectAnimator.ofFloat(layout_editbox, "alpha", 1f, 0f));

			animatorSet2.setDuration(400);
			animatorSet2.setInterpolator(new DecelerateInterpolator());
			animatorSet2.start();

			// 接着是原始按钮的上移
			AnimatorSet animatorSet3 = new AnimatorSet();
			animatorSet3.playTogether(
					ObjectAnimator.ofFloat(layout_btns, "translationY",
							ScreenUtils.getIntPixels(context, 200), 0f),
					ObjectAnimator.ofFloat(layout_btns, "alpha", 0f, 1f));

			animatorSet3.setDuration(600);
			animatorSet3.setInterpolator(new DecelerateInterpolator());
			animatorSet3.start();

			// 底部按钮消失
			btn_next.postDelayed(new Runnable() {

				@Override
				public void run() {
					AnimatorSet animatorSet3 = new AnimatorSet();
					animatorSet3.playTogether(ObjectAnimator.ofFloat(btn_next,
							"alpha", 1f, 0f));
					animatorSet3.setDuration(400);
					animatorSet3.setInterpolator(new AccelerateInterpolator());
					animatorSet3.start();
					layout_back.setVisibility(View.GONE);
				}
			}, 0);
		}
	}

	/**
	 * 改变布局 显示登录
	 */
	public void handleLogin(final View view) {
		tv_header_title.setText("登录");
		btn_next.setText("完 成");
		changeHeaderLayout(1);
	}

	/**
	 * 改变布局 显示注册
	 */
	public void handleSignup(final View view) {
		tv_header_title.setText("注册");
		btn_next.setText("下一步");
		changeHeaderLayout(2);
	}

	/**
	 * 此方法用来处理 注册的下一步 和 登录的完成
	 */
	public void handleNext(final View view) {
		if (FLAG_MODE == 1) { // 登录
			email = emailText.getText().toString().trim();
			password = passwordText.getText().toString().trim();
			showProgress();
			fetchRequest(email, password);

		} else if (FLAG_MODE == 2) { // 注册
			email = emailText.getText().toString().trim();
			password = passwordText.getText().toString().trim();

			if (email.length() == 0 || password.length() == 0)
				Toaster.showLong(AccountLoginActivity.this,
						R.string.emptyfail_signup);
			else if (!isEmailEnabled(email))
				Toaster.showLong(AccountLoginActivity.this,
						R.string.invalid_mail_signup);
			else if (password.length() < 6)
				Toaster.showLong(AccountLoginActivity.this,
						R.string.short_password_signup);
			else {
				// 开始邮箱验证
				showProgress(1);
				checkEmailRequest();
			}
		}
	}

	/**
	 * 验证邮箱
	 */
	private void checkEmailRequest() {
		String url = AsyncHttpManager.EMAIL_VERIFY__URL;

		RequestParams params = new RequestParams();
		/* userId里面可以是userId/用户邮箱/注册手机。 */
		params.put("userId", email);

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onSuccessed() {
						super.onSuccessed();
						hideProgress(1);

						String json = new String();
						try {
							json = new JSONObject(getResult())
									.getString("message");
						} catch (JSONException e) {
							e.printStackTrace();
						}

						if (json.equals("0")) {
							Toaster.showShort(AccountLoginActivity.this,
									"该邮箱已存在");
						} else {
							Bundle bundle = new Bundle();
							bundle.putString("bundle_email", email);
							bundle.putString("bundle_password", password);
							startActivity(AccountSignupActivity.class, bundle);
							overridePendingTransition(R.anim.left_in,
									R.anim.left_out);
							finish();
						}
					}

					@Override
					public void onFailured() {
						super.onFailured();
						hideProgress(1);
						Toaster.showShort(AccountLoginActivity.this, "请求失败");
					}

					@Override
					public void onError() {
						super.onError();
						hideProgress(1);
					}
				});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		final ProgressDialog dialog = new ProgressDialog(this);
		if (id == 0)
			dialog.setMessage(getText(R.string.message_signing_in));
		else
			dialog.setMessage("正在验证邮箱");
		dialog.setIndeterminate(true);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {

			}
		});
		return dialog;
	}

	@SuppressWarnings("deprecation")
	protected void hideProgress(int i) {
		removeDialog(i);
	}

	/**
	 * Show progress dialog
	 */
	@SuppressWarnings("deprecation")
	protected void showProgress(int i) {
		showDialog(i);
	}

	/**
	 * Hide progress dialog
	 */
	@SuppressWarnings("deprecation")
	protected void hideProgress() {
		removeDialog(0);
	}

	/**
	 * Show progress dialog
	 */
	@SuppressWarnings("deprecation")
	protected void showProgress() {
		showDialog(0);
	}

	/**
	 * AVOS 登录
	 */
	private void fetchRequest0(final String email, final String password) {
		AVUser.logInInBackground(email, password, new LogInCallback<AVUser>() {

			@Override
			public void done(AVUser user, AVException e) {
				if (user != null) {
					hideProgress();
					Toaster.showLong(AccountLoginActivity.this, "欢迎回来！");
					startActivity(HomeActivity.class);
					finish();
				} else {
					hideProgress();
					Toaster.showShort(AccountLoginActivity.this, "网络异常，注册失败");
				}
			}
		});
	}

	/**
	 * 登录请求
	 */
	private void fetchRequest(final String email, final String password) {

		String url = AsyncHttpManager.LOGIN__URL;

		RequestParams params = new RequestParams();
		params.put("username", email);
		params.put("password", password);

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						hideProgress();
						Toaster.showShort(AccountLoginActivity.this,
								"网络异常，注册失败");
					}

					@Override
					public void onError() {
						super.onError();
						hideProgress();
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						// 这里有个小Bug,注意一下
						FLAG_MODE = 0;
						UserController.saveUser(context, getResult());
						savePrefers();
						fetchRequest0(email, password);

					}
				});
	}

	/**
	 * 本地保存信息
	 */
	public void savePrefers() {
		// 如果登录的ID与上一次的ID不一致，就删除所有聊天记录
		// 注销的时候会记录上次的ID
		if (!Constants.USER_ID.equals(PreferenceUtils.getString(
				AccountLoginActivity.this, PreferConfig.PREFER_LAST_LOGIN_UID,
				""))) {
			ChatController.deleteAllChatAndMessage(AccountLoginActivity.this);
		}
		PreferenceUtils.putString(context, PreferConfig.PREFER_LAST_LOGIN_UID,
				Constants.USER_ID);

		PreferenceUtils.putString(context, PreferConfig.PREFER_INITIAL_USERID,
				Constants.USER_ID);

		PreferenceUtils.putString(context,
				PreferConfig.PREFER_INITIAL_SESSIONID,
				AsyncHttpLoader.SessionId);

		PreferenceUtils.putBoolean(context,
				PreferConfig.PREFER_REQUEST_NEWACCOUNT, false);

		PreferenceUtils.putString(context, PreferConfig.PREFER_INITIAL_EMAIL,
				emailText.getText().toString().trim());

		PreferenceUtils.putString(context,
				PreferConfig.PREFER_INITIAL_PASSWORD, passwordText.getText()
						.toString().trim());

		PreferenceUtils
				.putInt(context, PreferConfig.PREFER_PLAN_FILTER_LOC, -1);
		PreferenceUtils
				.putInt(context, PreferConfig.PREFER_PLAN_FILTER_SEX, -1);
	}

	@Override
	public void onBackPressed() {

		if (FLAG_MODE != 0) {
			changeHeaderLayout(0);
		} else {
			finish();
		}
	}

}
