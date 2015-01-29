/**
 * @Package com.manyouren.android.ui.user    
 * @Title: SigupSlideActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-8-12 下午3:00:17 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.usernew;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.github.kevinsawicki.wishlist.Toaster;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.HomeActivity;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.PreferConfig;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.service.AVService;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpLoader;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.service.greendao.GreenUser;
import com.manyouren.manyouren.ui.user.ActivityForResultUtil;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.PhotoUtils;
import com.manyouren.manyouren.util.PreferenceUtils;
import com.manyouren.manyouren.widget.crop.Crop;

/**
 * 
 * @author weizepeng
 * @date 2014-11-16
 * 
 */
public class AccountSignupActivity extends FragmentActivity {

	private static final int NUM_PAGES = 3;

	private ViewPager mPager;

	private PagerAdapter mPagerAdapter;

	public static File mHeadFile = null;

	public static String mHeadPath = "";

	public static Bitmap mHeadBitmap = null;

	public static String email = "";

	public static String password = "";

	public static String name = "";

	public static String birthday = "";

	public static int gender = -1;

	public static String liveland = "";

	private Context context;

	private volatile boolean requestAvaliable = false; // flag to determine

	private volatile long requestVariable = 0; // time number to count

	private SignupFirstFragment firstFragment = null;

	private AccountSignupFragment2 fragment2 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup_slide);

		context = this;

		email = getIntent().getStringExtra("bundle_email");
		password = getIntent().getStringExtra("bundle_password");

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setLogo(
				getResources().getDrawable(R.drawable.ic_action_logo));
		getActionBar().setTitle("注册 1/3");

		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.slide_pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				invalidateOptionsMenu();
			}
		});

		resetData();
	}

	private void resetData() {
		name = "";
		birthday = "";
		gender = -1;
		liveland = "";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		getMenuInflater().inflate(R.menu.activity_signup_slide, menu);

		if (mPager.getCurrentItem() == 0) {
			menu.findItem(R.id.action_previous).setTitle("返回").setEnabled(true);
		}

		// Add either a "next" or "finish" button to the action bar, depending
		// on which page
		// is currently selected.
		MenuItem item = menu
				.add(Menu.NONE,
						R.id.action_next,
						Menu.NONE,
						(mPager.getCurrentItem() == mPagerAdapter.getCount() - 1) ? R.string.action_finish
								: R.string.action_next);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			final Intent homeIntent = new Intent(this,
					AccountLoginActivity.class);
			startActivity(homeIntent);
			overridePendingTransition(R.anim.right_in, R.anim.right_out);
			finish();
			return true;

		case R.id.action_previous:
			// Go to the previous step in the wizard. If there is no previous
			// step,
			// setCurrentItem will do nothing.
			if (mPager.getCurrentItem() == 0) {

				startActivity(new Intent(this, AccountLoginActivity.class));
				overridePendingTransition(R.anim.right_in, R.anim.right_out);
				finish();
				return true;
			}

			getActionBar().setTitle("注册  " + (mPager.getCurrentItem()) + "/3");

			mPager.setCurrentItem(mPager.getCurrentItem() - 1);

			return true;

		case R.id.action_next:

			if (mPager.getCurrentItem() == 0) {
				name = firstFragment.getText();
				if (name.equals("")) {
					Toaster.showShort(AccountSignupActivity.this, "昵称为空");
					return true;
				}
				if (mHeadFile == null) {
					Toaster.showShort(AccountSignupActivity.this, "头像为空");
					return true;
				}

			} else if (mPager.getCurrentItem() == 1) {
				if (gender == -1) {
					Toaster.showShort(AccountSignupActivity.this, "性别为空");
					return true;
				}
				if (birthday.equals("")) {
					Toaster.showShort(AccountSignupActivity.this, "生日为空");
					return true;
				}
				// 判断birthday是否大于18岁
				if (isLessThan18(birthday)) {
					Toaster.showShort(AccountSignupActivity.this,
							"年龄未满18岁，无法注册");
					return true;
				}

			} else if (mPager.getCurrentItem() == 2) {
				name = firstFragment.getText();
				if (name.equals("")) {
					Toaster.showShort(AccountSignupActivity.this, "昵称为空");
					return true;
				}

				if (gender == -1) {
					Toaster.showShort(AccountSignupActivity.this, "性别为空");
					return true;
				}
				if (birthday.equals("")) {
					Toaster.showShort(AccountSignupActivity.this, "生日为空");
					return true;
				}
				// 判断birthday是否大于18岁
				if (isLessThan18(birthday)) {
					Toaster.showShort(AccountSignupActivity.this,
							"年龄未满18岁，无法注册");
					return true;
				}

				if (liveland.equals("")) {
					Toaster.showShort(AccountSignupActivity.this, "现居地为空");
					return true;
				}
			}

			if (mPager.getCurrentItem() + 1 == 3) {
				handleSignup();

			} else {
				// Advance to the next step in the wizard. If there is no next
				// step,
				// setCurrentItem
				// will do nothing.
				mPager.setCurrentItem(mPager.getCurrentItem() + 1);

				if (mPager.getCurrentItem() + 1 <= 3) {
					getActionBar().setTitle(
							"注册  " + (mPager.getCurrentItem() + 1) + "/3");
				}
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public boolean isLessThan18(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(strDate);
			Date dateNow = new Date();
			long time = dateNow.getTime() - date.getTime();
			if (time > 567648000000l) {
				return false;
			} else {
				return true;
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment}
	 * objects, in sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (firstFragment == null) {
					firstFragment = SignupFirstFragment.getInstance();
				}
				return firstFragment;
				// return mFragment0;
			case 1:
				return AccountSignupFragment.create(position);
				// return mFragment1;
			case 2:
				fragment2 = new AccountSignupFragment2();
				return fragment2;
				// return mFragment2;
			}
			throw new IllegalStateException("No fragment at position "
					+ position);
			// return SignupSlideFragment.create(position);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Fragment f = (Fragment) super.instantiateItem(container, position);

			return f;
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	public void handleSignup() {
		showProgress();
		// 空头像时
		// 处理
		postRequest1();

	}

	/**
	 * Hide progress dialog
	 */
	@SuppressWarnings("deprecation")
	protected void hideProgress() {
		dismissDialog(0);
	}

	/**
	 * Show progress dialog
	 */
	@SuppressWarnings("deprecation")
	protected void showProgress() {
		showDialog(0);
	}

	protected Dialog onCreateDialog(int id) {
		final ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMessage(getText(R.string.message_signing_up));
		dialog.setIndeterminate(true);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {

			}
		});
		return dialog;
	}

	private void postRequest1() {

		AVService.signUp(email, password, email, new SignUpCallback() {

			@Override
			public void done(AVException e) {
				if (e == null) {
					postRequest2();
				} else {
					Toaster.showShort(AccountSignupActivity.this, "注册失败");
				}
			}
		});

	}

	private void postRequest2() {
		String url = AsyncHttpManager.SIGNUP_URL;
		RequestParams params = new RequestParams();
		params.put("username", name);
		params.put("password", password);
		params.put("email", email);
		params.put("residence", liveland.toString());
		params.put("birthday", birthday.toString());
		params.put("gender", gender + "");
		params.put("objectId", AVUser.getCurrentUser().getObjectId());

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						hideProgress();
						Toaster.showLong(AccountSignupActivity.this, "注册失败");
						try {
							if (AVUser.getCurrentUser() != null)
								AVUser.getCurrentUser().delete();
						} catch (AVException e1) {
							e1.printStackTrace();
						}
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						UserController.saveUser(context, getResult());
						savePrefers();
						postRequest3();
					}

					@Override
					public void onError() {
						super.onError();
						hideProgress();
					}
				});

	}

	String avatarSuffix = "";

	private void postRequest3() {
		Logot.outError("TAG", "postRequest3 ");

		// 可以不上传头像，直接注册成功
		if (mHeadPath.equals("")) {
			hideProgress();
			Toaster.showLong(AccountSignupActivity.this, "注册成功，欢迎加入漫游人！");

			startActivity(new Intent(context, HomeActivity.class));
			// startActivity(new Intent(context,
			// GuideActivity.class));
			finish();
			return;
		}

		File avatarFile = new File(AccountSignupActivity.mHeadPath);

		// 上传头像
		String url = AsyncHttpManager.UPLOADS_URL;
		RequestParams params = new RequestParams();
		try {
			params.put("image[0]", (File) avatarFile);
			params.put("userId", PreferenceHelper.getUserId());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						Logot.outError("TAG", "postRequest3 fail");
						hideProgress();
						Toaster.showLong(AccountSignupActivity.this,
								"头像上传失败，但是注册成功！");
						startActivity(new Intent(context, HomeActivity.class));
						finish();
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						try {
							Logot.outError("TAG", "postRequest3 success");
							// 这个URL只存后缀
							avatarSuffix = (String) new JSONArray(
									new JSONObject(getResult())
											.getString("message")).get(0);

						} catch (JSONException e) {
							e.printStackTrace();
						}
						postRequest4();

					}

					@Override
					public void onError() {
						super.onError();
						hideProgress();
						Toaster.showLong(AccountSignupActivity.this,
								"头像上传失败，但是注册成功！");
						startActivity(new Intent(context, HomeActivity.class));
						finish();
					}
				});
	}

	private void postRequest4() {

		// 上传头像
		String url = AsyncHttpManager.USER_CHANGE_AVATAR_URL;
		RequestParams params = new RequestParams();

		params.put("avatar0", avatarSuffix);
		params.put("userId", PreferenceHelper.getUserId());

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						Logot.outError("TAG", "postRequest4 fail");
						hideProgress();
						Toaster.showLong(AccountSignupActivity.this,
								"头像上传失败，但是注册成功！");
						startActivity(new Intent(context, HomeActivity.class));
						finish();
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						Logot.outError("TAG", "postRequest4 success");
						// 头像存入本地数据库
						de.greenrobot.daoexample.User user = GreenUser
								.getInstance(context).getUserById(
										Long.valueOf(Constants.USER_ID));
						user.setAvatar0(avatarSuffix);
						GreenUser.getInstance(context).saveUser(user);

						hideProgress();
						Toaster.showLong(AccountSignupActivity.this,
								"注册成功，欢迎加入漫游人！");

						startActivity(new Intent(context, HomeActivity.class));
						// startActivity(new Intent(context,
						// GuideActivity.class));
						finish();
					}

					@Override
					public void onError() {
						super.onError();
						hideProgress();
						Toaster.showLong(AccountSignupActivity.this,
								"头像上传失败，但是注册成功！");
						startActivity(new Intent(context, HomeActivity.class));
						finish();
					}
				});
	}

	public void savePrefers() {
		PreferenceUtils.putString(context, PreferConfig.PREFER_INITIAL_USERID,
				Constants.USER_ID);

		PreferenceUtils.putString(context,
				PreferConfig.PREFER_INITIAL_SESSIONID,
				AsyncHttpLoader.SessionId);

		PreferenceUtils.putBoolean(context,
				PreferConfig.PREFER_REQUEST_NEWACCOUNT, false);

		PreferenceUtils.putString(context, PreferConfig.PREFER_INITIAL_EMAIL,
				AccountSignupActivity.email);

		PreferenceUtils
				.putInt(context, PreferConfig.PREFER_PLAN_FILTER_LOC, -1);
		PreferenceUtils
				.putInt(context, PreferConfig.PREFER_PLAN_FILTER_SEX, -1);
	}

	@Override
	public void onBackPressed() {

		if (mPager.getCurrentItem() != 0) {
			getActionBar().setTitle("注册  " + (mPager.getCurrentItem()) + "/3");
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
		} else {
			startActivity(new Intent(this, AccountLoginActivity.class));
			overridePendingTransition(R.anim.right_in, R.anim.right_out);
			finish();
		}
	}

	public static Map<String, String> greenMap = null;

	private Handler handler = new Handler() {

	};

	public static class SignupFirstFragment extends Fragment {

		private ImageView iv_avatar;
		private EditText et_name;
		private Context context;

		public SignupFirstFragment() {

		}

		public static SignupFirstFragment getInstance() {
			return new SignupFirstFragment();
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			context = getActivity();
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			View rootView = (ViewGroup) inflater.inflate(
					R.layout.fragment_signup_slide_1, container, false);
			iv_avatar = (ImageView) rootView.findViewById(R.id.iv_avatar);
			et_name = (EditText) rootView.findViewById(R.id.et_name);

			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			if (AccountSignupActivity.mHeadBitmap != null) {
				iv_avatar.setImageBitmap(PhotoUtils
						.toRoundBitmap(AccountSignupActivity.mHeadBitmap));
			}

			iv_avatar.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {

					Logot.outError("iv_avatar", "onClick");
					Crop.pickImage(getActivity());
				}
			});
		}

		private void beginCrop(Uri source) {
			Uri outputUri = Uri.fromFile(new File(getActivity().getCacheDir(),
					"cropped"));
			new Crop(source).output(outputUri).asSquare().start(getActivity());
		}

		private void handleCrop(int resultCode, Intent result) {
			if (resultCode == RESULT_OK) {
				// iv_avatar.setImageURI(Crop.getOutput(result));
				Logot.outError("handleCrop", "handleCrop");
				Bitmap bitmap = null;
				try {
					bitmap = MediaStore.Images.Media.getBitmap(getActivity()
							.getContentResolver(), Crop.getOutput(result));
					Logot.outError("handleCrop", (bitmap == null) + "");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				iv_avatar.setImageBitmap(PhotoUtils.toRoundBitmap(bitmap));
				savePhoto(bitmap);
				Logot.outError("savePhoto", "savePhoto" + "");

			} else if (resultCode == Crop.RESULT_ERROR) {
				Toast.makeText(getActivity(),
						Crop.getError(result).getMessage(), Toast.LENGTH_SHORT)
						.show();
			}
		}

		public void myActivityResult(int requestCode, int resultCode,
				Intent data) {

			if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
				beginCrop(data.getData());
				Logot.outError("beginCrop", "beginCrop");
			} else if (requestCode == Crop.REQUEST_CROP) {
				handleCrop(resultCode, data);
				Logot.outError("handleCrop", "handleCrop");
			}
		}

		private void savePhoto(Bitmap bitmap) {
			mHeadBitmap = bitmap;

			AccountSignupActivity.mHeadPath = PhotoUtils
					.savePhotoToSDCard(bitmap);
			CompressFormat format = Bitmap.CompressFormat.JPEG;
			int quality = 100;
			OutputStream stream = null;
			try {
				stream = new FileOutputStream(AccountSignupActivity.mHeadPath);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (bitmap.compress(format, quality, stream)) {
				AccountSignupActivity.mHeadFile = new File(
						AccountSignupActivity.mHeadPath);
			}
		}

		public String getText() {
			return et_name.getText().toString().trim();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		firstFragment.myActivityResult(requestCode, resultCode, data);

		AccountSignupFragment2.myActivityResult(requestCode, resultCode, data);
	}

}
