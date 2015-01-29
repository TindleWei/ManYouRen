/**
 * @Package com.manyouren.android.ui.user    
 * @Title: UserInfoActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-8-18 下午1:35:20 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kevinsawicki.wishlist.Toaster;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpLoader;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.service.greendao.GreenUser;
import com.manyouren.manyouren.ui.CircleTransform;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.PhotoUtils;
import com.manyouren.manyouren.widget.crop.Crop;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

/**
 * 
 * @author firefist_wei
 * @date 2014-8-18 下午1:35:20
 * 
 */
@SuppressLint("NewApi")
public class UserInfoActivity extends BaseActivity implements OnClickListener {

	public final static int MENUITEM_EDIT_INFO = 10031;

	@InjectView(R.id.tv_uid)
	TextView tv_uid;

	@InjectView(R.id.tv_name)
	TextView tv_name;

	@InjectView(R.id.tv_age)
	TextView tv_age;

	@InjectView(R.id.tv_from)
	TextView tv_from;

	@InjectView(R.id.tv_wantgo)
	TextView tv_wantgo;

	@InjectView(R.id.tv_play_prefer)
	TextView tv_play_prefer;

	@InjectView(R.id.tv_out_transform)
	TextView tv_out_transform;

	@InjectView(R.id.tv_out_times)
	TextView tv_out_times;

	@InjectView(R.id.tv_went_places)
	TextView tv_went_places;

	@InjectView(R.id.iv_avatar)
	ImageView iv_avatar;

	@InjectView(R.id.iv_gender)
	ImageView iv_gender;

	@InjectView(R.id.ll_name)
	LinearLayout ll_name;

	@InjectView(R.id.rl_gender)
	RelativeLayout rl_gender;

	@InjectView(R.id.ll_age)
	LinearLayout ll_age;

	@InjectView(R.id.ll_from)
	LinearLayout ll_from;

	@InjectView(R.id.ll_out_times)
	LinearLayout ll_out_times;

	@InjectView(R.id.ll_out_transform)
	LinearLayout ll_out_transform;

	@InjectView(R.id.ll_play_prefer)
	LinearLayout ll_play_prefer;

	@InjectView(R.id.ll_wantgo)
	RelativeLayout ll_wantgo;

	@InjectView(R.id.ll_went_places)
	LinearLayout ll_went_places;

	UserEntity userEntity = null;

	private Context context;

	private String avatarPath = "";

	private File avatarFile = null;

	int gender = -1;

	String birthday = "";

	String residence = "";

	int frequency = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfo);
		context = this;
		setActionBar("个人资料");

		initView();
		initEvent();
		init();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:

			finish();
			overridePendingTransition(R.anim.right_in, R.anim.right_out);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		/*
		 * initView(); initEvent(); init();
		 */
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initEvent() {

		ll_name.setOnClickListener(this);
		tv_name.setOnClickListener(this);
		iv_avatar.setOnClickListener(this);

		rl_gender.setOnClickListener(this);

		ll_age.setOnClickListener(this);

		ll_from.setOnClickListener(this);
		ll_out_times.setOnClickListener(this);
		ll_out_transform.setOnClickListener(this);
		ll_play_prefer.setOnClickListener(this);
		ll_wantgo.setOnClickListener(this);
		ll_went_places.setOnClickListener(this);

	}

	@Override
	protected void init() {

		userEntity = UserController.getUserById(context,
				Long.valueOf(PreferenceHelper.getUserId()));

		Picasso.with(RootApplication.getInstance())
				.load(UserController.getAvatarDiff(userEntity.getAvatar0()))
				.transform(new CircleTransform())
				.placeholder(R.drawable.gravatar_icon).into(iv_avatar);

		tv_uid.setText("漫游号：" + userEntity.getUserId());

		iv_gender.setImageDrawable(context.getResources().getDrawable(
				userEntity.getGender() == 1 ? R.drawable.bg_icon_man
						: R.drawable.bg_icon_woman));

		tv_name.setText(userEntity.getUserName());

		tv_age.setText(UserController.getAgeFromDateString(userEntity
				.getBirthday()) + "");

		tv_from.setText(userEntity.getResidence());

		tv_wantgo.setText(userEntity.getWant2Go());

		tv_play_prefer.setText(userEntity.getHobbyText());

		tv_out_transform.setText(userEntity.getVehicleText());

		tv_out_times.setText(userEntity.getFrequency() == 0 ? "一般" : "经常");

		tv_went_places.setText(userEntity.getBeenThere());

		if (gender == -1)
			gender = userEntity.getGender();

	}

	@Override
	public void onClick(View view) {

		Bundle bundle = null;

		switch (view.getId()) {

		case R.id.ll_name:
		case R.id.tv_name:
			MobclickAgent.onEvent(context, "update_userInfo");
			bundle = new Bundle();
			bundle.putString("title", "修改昵称");
			bundle.putString("key", "username");
			bundle.putString("value", userEntity.getUserName());
			startActivity(InfoEditActivity.class, bundle);
			break;

		case R.id.rl_gender:
			Toaster.showShort(UserInfoActivity.this, "性别无法修改");

			boolean cant = false;
			if (cant)
				new AlertDialog.Builder(context)
						.setTitle("性别")
						.setSingleChoiceItems(new String[] { "女", "男" },
								gender, new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (which == 0) {
											HashMap<String, Object> map = new HashMap<String, Object>();
											map.put("gender", "0");
											postInfoRequest(map);
											gender = 0;
											iv_gender
													.setImageResource(R.drawable.bg_icon_woman);

										} else {
											HashMap<String, Object> map = new HashMap<String, Object>();
											map.put("gender", "1");
											postInfoRequest(map);
											gender = 1;
											iv_gender
													.setImageResource(R.drawable.bg_icon_man);
										}
										dialog.dismiss();
									}
								}).show();
			break;

		case R.id.ll_age:
			Toaster.showShort(UserInfoActivity.this, "生日无法修改");
			boolean cant2 = false;
			if (cant2) {
				DialogDatePicker myDateFragment = new DialogDatePicker();
				myDateFragment.show(getFragmentManager(), null);
			}

			break;

		case R.id.ll_from:
			startActivityForResult(new Intent(context, CityListActivity.class),
					31);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);

			break;

		case R.id.ll_out_times:
			new AlertDialog.Builder(context)
					.setTitle("出行频率")
					.setSingleChoiceItems(new String[] { "一般", "经常" },
							frequency, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (which == 0) {
										HashMap<String, Object> map = new HashMap<String, Object>();
										map.put("frequency", "0");
										postInfoRequest(map);
										frequency = 0;
										tv_out_times.setText("一般");
									} else {

										HashMap<String, Object> map = new HashMap<String, Object>();
										map.put("frequency", "1");
										postInfoRequest(map);
										frequency = 1;
										tv_out_times.setText("经常");
									}
									dialog.dismiss();
								}
							}).show();
			break;

		case R.id.ll_out_transform:

			bundle = new Bundle();
			bundle.putString("title", "交通工具");
			bundle.putString("key", "vehicleText");
			bundle.putString("value", userEntity.getVehicleText());
			startActivity(InfoEditActivity.class, bundle);
			break;

		case R.id.ll_play_prefer:
			bundle = new Bundle();
			bundle.putString("title", "游玩偏好");
			bundle.putString("key", "hobbyText");
			bundle.putString("value", userEntity.getHobbyText());
			startActivity(InfoEditActivity.class, bundle);
			break;

		case R.id.ll_wantgo:

			bundle = new Bundle();
			bundle.putString("title", "想去哪");
			bundle.putString("key", "want2Go");
			bundle.putString("value", userEntity.getWant2Go());
			startActivity(InfoEditActivity.class, bundle);
			break;

		case R.id.ll_went_places:

			bundle = new Bundle();
			bundle.putString("title", "去过哪");
			bundle.putString("key", "beenThere");
			bundle.putString("value", userEntity.getBeenThere());
			startActivity(InfoEditActivity.class, bundle);
			break;

		case R.id.iv_avatar:

			Crop.pickImage(activity);

			// Intent intent = new Intent(Intent.ACTION_PICK, null);
			// intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
			// "image/*");
			// startActivityForResult(intent,
			// ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_LOCATION);

			break;
		}
	}

	private void beginCrop(Uri source) {
		Uri outputUri = Uri
				.fromFile(new File(context.getCacheDir(), "cropped"));
		new Crop(source).output(outputUri).asSquare().start(activity);
	}

	private void handleCrop(int resultCode, Intent result) {
		if (resultCode == RESULT_OK) {
			Logot.outError("iv_avatar", "beginCrop");
//			iv_avatar.setImageURI(Crop.getOutput(result));
//			iv_avatar.getDrawable();

			Bitmap bitmap=null;
			try {
				bitmap = MediaStore.Images.Media.getBitmap(
						this.getContentResolver(), Crop.getOutput(result));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			iv_avatar.setImageBitmap(PhotoUtils.toRoundBitmap(bitmap));
			savePhoto(bitmap);

			pd = ProgressDialog.show(context, null, "正在上传", true, true, null);
			uploadAvatar();

		} else if (resultCode == Crop.RESULT_ERROR) {
			Toast.makeText(context, Crop.getError(result).getMessage(),
					Toast.LENGTH_SHORT).show();
		}
	}

	@SuppressLint("ValidFragment")
	class DialogDatePicker extends DialogFragment {

		private Calendar calendar;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			Dialog dialog = null;
			calendar = Calendar.getInstance();

			dialog = new DatePickerDialog(context,
					new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {

							birthday = (year
									+ "-"
									+ ((monthOfYear + 1) < 10 ? "0"
											+ (monthOfYear + 1)
											: (monthOfYear + 1)) + "-" + (dayOfMonth < 10 ? "0"
									+ dayOfMonth
									: dayOfMonth));

							if (!userEntity.getBirthday().equals(birthday)) {
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("birthday", birthday);
								postInfoRequest(map);
								tv_age.setText(UserController
										.getAgeFromDateString(birthday) + "");
							} else {
								Toaster.showShort(UserInfoActivity.this, "没有改变");
							}

						}
					}, calendar.get(Calendar.YEAR), calendar
							.get(Calendar.MONTH), calendar
							.get(Calendar.DAY_OF_MONTH));

			return dialog;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
			beginCrop(data.getData());
			Logot.outError("beginCrop", "beginCrop");
		} else if (requestCode == Crop.REQUEST_CROP) {
			handleCrop(resultCode, data);
			Logot.outError("handleCrop", "handleCrop");
		} else if (requestCode == 31) {
			tv_from.setText(data.getStringExtra("CityName"));
			residence = data.getStringExtra("CityName");

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("residence", residence);
			postInfoRequest(map);
		}

		// if (resultCode == RESULT_OK) {
		//
		// switch (requestCode) {
		// /**
		// * 通过照相修改头像
		// */
		// case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CAMERA:
		//
		// break;
		// /**
		// * 通过本地修改头像
		// */
		// case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_LOCATION:
		// Uri uri = null;
		// if (data == null) {
		// Toast.makeText(context, "取消上传", Toast.LENGTH_SHORT).show();
		// return;
		// }
		// if (resultCode == Activity.RESULT_OK) {
		// if (!Environment.getExternalStorageState().equals(
		// Environment.MEDIA_MOUNTED)) {
		// Toast.makeText(context, "SD不可用", Toast.LENGTH_SHORT)
		// .show();
		// return;
		// }
		// uri = data.getData();
		// startPhotoZoom(uri);
		// } else {
		// Toast.makeText(context, "照片获取失败", Toast.LENGTH_SHORT)
		// .show();
		// }
		// break;
		// /**
		// * 裁剪修改的头像
		// */
		// case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CROP:
		// if (data == null) {
		// Toast.makeText(context, "取消上传", Toast.LENGTH_SHORT).show();
		// return;
		// } else {
		// saveCropPhoto(data);
		//
		// pd = ProgressDialog.show(context, null, "正在上传", true, true,
		// null);
		// uploadAvatar();
		// }
		// break;
		//
		// case 31:
		// tv_from.setText(data.getStringExtra("CityName"));
		// residence = data.getStringExtra("CityName");
		//
		// HashMap<String, Object> map = new HashMap<String, Object>();
		// map.put("residence", residence);
		// postInfoRequest(map);
		// break;
		//
		// }
		// }
	}

	/**
	 * 系统裁剪照片
	 * 
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 400);
		intent.putExtra("outputY", 400);
		intent.putExtra("scale", true);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true)
				.putExtra("scaleUpIfNeeded", true)
				// 黑边
				.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		startActivityForResult(intent,
				ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CROP);
	}

	/**
	 * 保存裁剪的照片
	 * 
	 * @param data
	 */
	private void saveCropPhoto(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			// bitmap = PhotoUtil.toRoundCorner(bitmap, 15);
			if (bitmap != null) {
				iv_avatar.setImageBitmap(PhotoUtils.toRoundBitmap(bitmap));
				savePhoto(bitmap);
			}

		} else {
			Toast.makeText(context, "获取裁剪照片错误", Toast.LENGTH_SHORT).show();
		}
	}

	private void savePhoto(Bitmap bitmap) {

		avatarPath = PhotoUtils.savePhotoToSDCard(bitmap);

		CompressFormat format = Bitmap.CompressFormat.JPEG;
		int quality = 100;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(avatarPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (bitmap.compress(format, quality, stream)) {
			avatarFile = new File(avatarPath);
		}
	}

	String avatarSuffix = "";

	public void uploadAvatar() {
		String url = AsyncHttpManager.UPLOADS_URL;

		RequestParams params = new RequestParams();
		try {
			params.put("image[0]", (File) avatarFile);
			params.put("userId", PreferenceHelper.getUserId());
		} catch (FileNotFoundException e) {
		}

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						Toaster.showShort(UserInfoActivity.this, "头像上传失败");
						if (pd != null && pd.isShowing())
							pd.dismiss();
					}
					
					@Override
					public void onError() {
						super.onError();
						Toaster.showShort(UserInfoActivity.this, "头像上传失败");
						if (pd != null && pd.isShowing())
							pd.dismiss();
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();

						try {
							Logot.outError("UserInfoActivity result",
									getResult());
							avatarSuffix = (String) new JSONArray(
									new JSONObject(getResult())
											.getString("message")).get(0);
							Logot.outError("UserInfoActivity L620",
									avatarSuffix);

							// 更新头像
							postAvatarRequest();

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	public void postAvatarRequest() {
		String url = AsyncHttpManager.USER_CHANGE_AVATAR_URL;

		RequestParams params = new RequestParams();
		params.put("avatar0", avatarSuffix);
		params.put("userId", PreferenceHelper.getUserId());

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						if (pd != null && pd.isShowing())
							pd.dismiss();
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						if (pd != null && pd.isShowing())
							pd.dismiss();

						// 存入本地数据库
						de.greenrobot.daoexample.User user = GreenUser
								.getInstance(context).getUserById(
										Long.valueOf(PreferenceHelper.getUserId()));
						user.setAvatar0(avatarSuffix);
						GreenUser.getInstance(context).saveUser(user);

						Picasso.with(RootApplication.getInstance())
								.load(UserController
										.getAvatarDiff(avatarSuffix))
								.transform(new CircleTransform())
								.placeholder(R.drawable.gravatar_icon)
								.into(iv_avatar);

						Toaster.showShort(UserInfoActivity.this, "上传成功");
					}

					@Override
					public void onError() {
						super.onError();
						if (pd != null && pd.isShowing())
							pd.dismiss();
					}
				});
	}

	public void postInfoRequest(final HashMap<String, Object> map) {
		String url = AsyncHttpManager.USER_CHANGE_INFO_URL;

		RequestParams params = new RequestParams();
		map.put("userId", PreferenceHelper.getUserId());
		params = AsyncHttpLoader.parseHashMap(map, params);

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						Toaster.showShort(UserInfoActivity.this, "上传失败");
						if (pd != null && pd.isShowing())
							pd.dismiss();
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						if (pd != null && pd.isShowing())
							pd.dismiss();

						de.greenrobot.daoexample.User user = GreenUser
								.getInstance(context).getUserById(
										Long.valueOf(Constants.USER_ID));

						if (map.containsKey("gender")) {
							user.setGender(gender);

						} else if (map.containsKey("birthday")) {

							try {
								user.setBirthday(new SimpleDateFormat(
										"yyyy-MM-dd").parse(birthday));
							} catch (ParseException e) {
								e.printStackTrace();
							}
						} else if (map.containsKey("residence")) {
							user.setResidence(residence);
							tv_from.setText(residence);
						} else if (map.containsKey("residence")) {
							user.setFrequency(frequency);
						}
						GreenUser.getInstance(context).saveUser(user);
						Toaster.showShort(UserInfoActivity.this, "修改成功");
					}
				});

	}

}
