/**
 * @Package com.manyouren.android.ui.user    
 * @Title: UserActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-6 下午12:21:23 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.Toaster;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.controller.PlanController;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.entity.PhotoEntity;
import com.manyouren.manyouren.entity.PlanEntity;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.ui.CircleTransform;
import com.manyouren.manyouren.ui.chatnew.ui.activity.ChatActivity;
import com.manyouren.manyouren.ui.plan.GalleryUrlActivity;
import com.manyouren.manyouren.ui.usernew.UserPhotosActivity;
import com.manyouren.manyouren.util.DateUtils;
import com.manyouren.manyouren.util.Logot;
import com.squareup.picasso.Picasso;

/**
 * 
 * @author firefist_wei
 * @date 2014-7-6 下午12:21:23
 * 
 */
public class UserActivity extends BaseActivity {

	public static final String TAG = "UserActivity";

	public static final int MENUITEM_ADD_FRIEND = 11004;

	public static final int MENUITEM_DELETE_FRIEND = 11005;

	@InjectView(R.id.user_info_layout)
	LinearLayout user_info_layout;

	@InjectView(R.id.user_plan_layout)
	LinearLayout user_plan_layout;

	@InjectView(R.id.user_album_layout)
	LinearLayout user_album_layout;
	
	@InjectView(R.id.tv_album_title)
	TextView tv_album_tile;
	
	@InjectView(R.id.tv_plan_title)
	TextView tv_plan_tile;

	//
	// Header
	//
	@InjectView(R.id.tv_name)
	TextView userName;

	@InjectView(R.id.iv_avatar)
	ImageView userAvatar;

	@InjectView(R.id.tv_sex)
	TextView tv_sex;

	@InjectView(R.id.tv_city)
	TextView tv_city;

	@InjectView(R.id.btn_send_msg)
	Button sendBtn;

	//
	// 基本资料
	//
	@InjectView(R.id.tv_signText)
	TextView tv_signText;

	@InjectView(R.id.ll_signText)
	LinearLayout ll_signText;

	@InjectView(R.id.tv_profession)
	TextView tv_profession;

	@InjectView(R.id.ll_profession)
	LinearLayout ll_profession;

	@InjectView(R.id.tv_hobbyText)
	TextView tv_hobbyText;

	@InjectView(R.id.ll_hobbyText)
	LinearLayout ll_hobbyText;

	@InjectView(R.id.tv_beenThere)
	TextView tv_beenThere;

	@InjectView(R.id.ll_beenThere)
	LinearLayout ll_beenThere;

	//
	// Plan
	//
	@InjectView(R.id.tv_plan_destination)
	TextView tv_plan_destination;

	@InjectView(R.id.tv_plan_time)
	TextView tv_plan_time;

	@InjectView(R.id.tv_plan_postscript)
	TextView tv_plan_postscript;

	@InjectView(R.id.user_plan_image_lay)
	LinearLayout userPlanImageLay;

	@InjectView(R.id.user_plan_image1)
	protected ImageView tv_planImage1;

	@InjectView(R.id.user_plan_image2)
	protected ImageView tv_planImage2;

	@InjectView(R.id.user_plan_image3)
	protected ImageView tv_planImage3;

	//
	// Album
	//
	@InjectView(R.id.user_ablum_image1)
	protected ImageView user_ablum_image1;

	@InjectView(R.id.user_ablum_image2)
	protected ImageView user_ablum_image2;

	@InjectView(R.id.user_ablum_image3)
	protected ImageView user_ablum_image3;

	//
	// Primary
	//
	private Context context;

	private UserEntity userEntity;

	private String strId, strAvatar, strName, strObjectId;

	String[] urls = new String[3];

	public List<PlanEntity> planList = new ArrayList<PlanEntity>();

	public List<String> photoUrls = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);

		context = this;
		setActionBar("用户资料");

		if (getIntent().getExtras() != null) {
			strId = getIntent().getStringExtra("UserId");
			strAvatar = getIntent().getStringExtra("UserAvatar");
			strName = getIntent().getStringExtra("UserName");
			strObjectId = getIntent().getStringExtra("objectId");

			userEntity = new UserEntity();
			userEntity.setObjectId(strObjectId);
			userEntity.setUserId(Long.valueOf(strId));
			userEntity.setAvatar0(strAvatar);
			userEntity.setUserName(strName);
			// userEntity.setObjectId(objectId)
		}

		if (userEntity != null) {
			getActionBar().setTitle(userEntity.getUserName());
		}
		initView();
		initEvent();
		init();
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initEvent() {

		user_plan_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (planList.size() > 0) {
					Bundle bundle = new Bundle();
					bundle.putString("uid", userEntity.getUserId() + "");
					startActivity(UserPlansActivity.class, bundle);
				}
			}
		});

		user_album_layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (photoUrls.size() > 0) {
					Bundle bundle = new Bundle();
					bundle.putString("uid", userEntity.getUserId() + "");
					startActivity(UserPhotosActivity.class, bundle);
				}
			}
		});

		userAvatar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String[] headUrl = new String[1];
				headUrl[0] = UserController.getAvatarDiff(userEntity
						.getAvatar0());

				startActivity(new Intent(UserActivity.this,
						UserHeadActivity.class).putExtra(
						GalleryUrlActivity.GALLERY_URLS, headUrl).putExtra(
						GalleryUrlActivity.GALLERY_INDEX, 0));
			}
		});

		sendBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(UserActivity.this,
						ChatActivity.class);
				intent.putExtra(ChatActivity.CHAT_USER_ID,
						userEntity.getObjectId());
				intent.putExtra("UserEntity", userEntity);
				startActivity(intent);
				overridePendingTransition(R.anim.left_in, R.anim.left_out);
			}
		});
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.clear();
		if ((String.valueOf(userEntity.getUserId()).equals(PreferenceHelper
				.getUserId()))) {
			return true;
		}
		if (relation == 0) {
			MenuItem done = menu.add(0, MENUITEM_ADD_FRIEND, 0, "关注");
			done.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		} else if (relation == 1) {
			MenuItem done = menu.add(0, MENUITEM_DELETE_FRIEND, 0, "已关注");
			done.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}

		return true;
	}

	int relation = -1; // 0 不是好友， 1 是好友

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		/*
		 * MenuItem done = menu.add(0, MENUITEM_ADD_FRIEND, 0, "关注");
		 * done.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		 * 
		 * if
		 * (!(String.valueOf(userEntity.getUserId()).equals(Constants.USER_ID)))
		 * { done.setVisible(true); } else { done.setVisible(false); }
		 */
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		// This is the home button in the top left corner of the screen.
		case MENUITEM_ADD_FRIEND:
			followUser(1); // 关注
			return true;
		case MENUITEM_DELETE_FRIEND:
			disFollowUser(1); // 取消
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void init() {

		/**
		 * uid username useravatar会从上一个界面传过来 如果是用户自己，本地找数据； 如果不是，可以通过ID请求数据。
		 * 
		 */
		if (strId.equals(Constants.USER_ID)) {
			userEntity = UserController.getUserById(this,
					Long.valueOf(Constants.USER_ID));
		}
		userName.setText(userEntity.getUserName());

		if (userEntity.getAvatar0() != null
				&& userEntity.getAvatar0().length() > 0)
			Picasso.with(RootApplication.getInstance())
					.load(UserController.getAvatarDiff(userEntity.getAvatar0()))
					.transform(new CircleTransform())
					.placeholder(R.drawable.gravatar_icon).into(userAvatar);

		if (!(String.valueOf(userEntity.getUserId()).equals(Constants.USER_ID))) {
			sendBtn.setVisibility(View.VISIBLE);
		} else {
			sendBtn.setVisibility(View.GONE);
		}
		fetchData();
	}

	private void fetchData() {

		fetchUserRelation();

		// 从用户角度体验，将此流程改为线性的
		// 通过userId获取用户资料
		fetchUserInfo(userEntity.getUserId());

		// 获取某人的计划
		// fetchUserPlan(userEntity.getUserId());

		// 获取某人的相册
		// fetchUserAlbum(userEntity.getUserId());

	}

	public void showViewInfo(UserEntity userEntity) {

		tv_sex.setVisibility(View.VISIBLE);
		tv_city.setVisibility(View.VISIBLE);

		tv_sex.setText(""
				+ DateUtils.getAgeFromDateString(userEntity.getBirthday()));
		tv_sex.setBackgroundDrawable(RootApplication
				.getInstance()
				.getResources()
				.getDrawable(
						userEntity.getGender() == 0 ? R.drawable.ic_age_female_bg
								: R.drawable.ic_age_male_bg));

		if (!userEntity.getResidence().equals("")) {
			tv_city.setText("来自" + userEntity.getResidence());
		}
		if (!userEntity.getProfession().equals("")) {
			tv_profession.setText(userEntity.getProfession());
			ll_profession.setVisibility(View.VISIBLE);
		} else {
			ll_profession.setVisibility(View.GONE);
		}
		if (!userEntity.getHobbyText().equals("")) {
			tv_hobbyText.setText(userEntity.getHobbyText());
			ll_hobbyText.setVisibility(View.VISIBLE);
		} else {
			ll_hobbyText.setVisibility(View.GONE);
		}
		if (!userEntity.getBeenThere().equals("")) {
			tv_beenThere.setText(userEntity.getBeenThere());
			ll_hobbyText.setVisibility(View.VISIBLE);
		} else {
			ll_beenThere.setVisibility(View.GONE);
		}

	}

	public void showViewPlan() {
		if (planList.size() == 0) {
			tv_plan_tile.setText("计划  暂无");
			return;
		}else{
			tv_plan_tile.setText("计划");
		}
		PlanEntity planEntity = planList.get(0);

		tv_plan_destination.setText(planEntity.getpName());
		tv_plan_time.setText(DateUtils.getPlanDigitalDate(
				planEntity.getStartDate(), planEntity.getEndDate()));
		tv_plan_postscript.setText(planEntity.getPostscript());

		tv_plan_time.setVisibility(View.VISIBLE);
		tv_plan_postscript.setVisibility(View.VISIBLE);

		List<String> listImages = PlanController.getPlanImages(planEntity
				.getImages());

		if (listImages.size() > 0) {
			userPlanImageLay.setVisibility(View.VISIBLE);
			for (int i = 0; i < listImages.size(); i++) {

				Picasso.with(context)
						.load(listImages.get(i))
						.resize(400, 400)
						.centerCrop()
						.placeholder(R.drawable.gravatar_image)
						.into(i == 0 ? tv_planImage1 : (i == 1 ? tv_planImage2
								: tv_planImage3));
				(i == 0 ? tv_planImage1 : (i == 1 ? tv_planImage2
						: tv_planImage3)).setVisibility(View.VISIBLE);
				urls[i] = listImages.get(i);
			}
		}
	}

	public void showViewAlbum() {
		
		if (photoUrls.size() == 0) {
			tv_album_tile.setText("相册  暂无");
			return;
		} else {
			tv_album_tile.setText("相册");
		}
		for (int i = 0; i < photoUrls.size(); i++) {

			Logot.outError("ALBUM", photoUrls.get(i));

			Picasso.with(context)
					.load(photoUrls.get(i))
					.resize(400, 400)
					.centerCrop()
					.placeholder(R.drawable.gravatar_image)
					.into(i == 0 ? user_ablum_image1
							: (i == 1 ? user_ablum_image2 : user_ablum_image3));
			(i == 0 ? user_ablum_image1 : (i == 1 ? user_ablum_image2
					: user_ablum_image3)).setVisibility(View.VISIBLE);
		}

	}

	public void fetchUserInfo(Long uid) {

		Logot.outError("fecthUserInfo: ", "" + String.valueOf(uid));
		String url = AsyncHttpManager.USRER_INFO_URL;

		RequestParams params = new RequestParams();
		params.put("userId", String.valueOf(uid));

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						fetchUserPlan(userEntity.getUserId());
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();

						Logot.outError(TAG, "json: " + getResult());
						String json = "";
						try {
							json = (new JSONObject(getResult())
									.getJSONObject("message")).toString();
						} catch (JSONException e) {
							e.printStackTrace();
						}
						userEntity = new Gson()
								.fromJson(json, UserEntity.class);
						showViewInfo(userEntity);

						fetchUserPlan(userEntity.getUserId());
					}
				});
	}

	public void fetchUserPlan(Long uid) {
		String url = AsyncHttpManager.USRER_PLAN_URL;

		RequestParams params = new RequestParams();
		params.put("userId", String.valueOf(uid));

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						fetchUserAlbum(userEntity.getUserId());
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						fetchUserAlbum(userEntity.getUserId());

						String json = "";
						try {
							json = (new JSONObject(getResult())
									.getJSONArray("message")).toString();
							planList = new Gson().fromJson(json,
									new TypeToken<List<PlanEntity>>() {
									}.getType());
							showViewPlan();

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onEmpty() {
						super.onEmpty();
						fetchUserAlbum(userEntity.getUserId());
						showViewPlan();
					}
				});
	}

	public void fetchUserAlbum(Long uid) {
		String url = AsyncHttpManager.USER_ALBUM_URL;

		RequestParams params = new RequestParams();
		params.put("userId", String.valueOf(uid));

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						String json = "";
						List<PhotoEntity> list = new ArrayList<PhotoEntity>();
						try {
							json = (new JSONObject(getResult())
									.getJSONArray("message")).toString();
						} catch (JSONException e) {
							e.printStackTrace();
						}
						list = new Gson().fromJson(json,
								new TypeToken<List<PhotoEntity>>() {
								}.getType());

						for (int i = 0; i < list.size(); i++) {
							JSONArray array;
							try {
								array = new JSONArray(list.get(i).getImages());
								photoUrls.add(AsyncHttpManager.UPLOADS_PREFIX
										+ array.get(0));
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

						showViewAlbum();
					}

					@Override
					public void onEmpty() {
						super.onEmpty();
						showViewAlbum();
					}
				});
	}

	private void followUser(final int type) {

		pd = ProgressDialog.show(context, null, "正在请求关注", true, true);

		String url = AsyncHttpManager.USER_FOCUS__URL;

		RequestParams params = new RequestParams();
		params.put("userId", PreferenceHelper.getUserId());
		params.put("subId", String.valueOf(userEntity.getUserId()));
		params.put("type", String.valueOf(type));

		Logot.outError("followUser", "userId: " + PreferenceHelper.getUserId());
		Logot.outError("followUser", "subId: " + strId);

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onSuccessed() {
						super.onSuccessed();
						if (pd != null)
							pd.dismiss();
						if (type == 1)
							Toaster.showShort(UserActivity.this, "关注成功!");
						relation = 1;
						invalidateOptionsMenu();
					}

					@Override
					public void onFailured() {
						super.onFailured();
						if (pd != null)
							pd.dismiss();
						Toaster.showShort(UserActivity.this, "关注失败!");
					}

					@Override
					public void onEmpty() {
						super.onEmpty();
						if (pd != null)
							pd.dismiss();
						Toaster.showShort(UserActivity.this, "关注失败!");
					}
				});
	}

	private void disFollowUser(final int type) {
		pd = ProgressDialog.show(context, null, "正在取消关注", true, true);

		String url = AsyncHttpManager.USER_CANCEL_URL;

		RequestParams params = new RequestParams();
		params.put("subId", String.valueOf(userEntity.getUserId()));
		params.put("userId", PreferenceHelper.getUserId());
		params.put("type", String.valueOf(type));

		Logot.outError("followUser", "userId: " + PreferenceHelper.getUserId());
		Logot.outError("followUser", "subId: " + strId);

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onSuccessed() {
						super.onSuccessed();
						if (pd != null)
							pd.dismiss();
						if (type == 1)
							Toaster.showShort(UserActivity.this, "取消成功");
						relation = 0;
						invalidateOptionsMenu();
					}

					@Override
					public void onFailured() {
						super.onFailured();
						if (pd != null)
							pd.dismiss();
						Toaster.showShort(UserActivity.this, "取消失败");
					}

					@Override
					public void onEmpty() {
						super.onEmpty();
						if (pd != null)
							pd.dismiss();
						Toaster.showShort(UserActivity.this, "关注失败!");
					}
				});
	}

	public void fetchUserRelation() {
		if (strId.equals(PreferenceHelper.getUserId())) {
			return;
		}
		Logot.outError("FetchUserRelation", "start!");

		String url = AsyncHttpManager.USER_RELATION_URL;
		RequestParams params = new RequestParams();
		params.put("userId", PreferenceHelper.getUserId());
		params.put("subId", String.valueOf(userEntity.getUserId()));

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						// 0.对方将您拉黑了
						// 1.好友
						// 2.你关注了他（关注）
						// 3.他关注了你（他是你的粉丝）
						// 4.你们没有关系
						try {
							String json = new JSONObject(getResult())
									.getString("message").toString();
							Logot.outError("FetchUserRelation", "result: " + json);
							if (json.equals("1")) {
								relation = 1;
							} else if (json.equals("2")) {
								relation = 1;
							} else {
								relation = 0;
							}
							// 这里处理
							// relation = 1;
						} catch (JSONException e) {
							e.printStackTrace();
						}
						invalidateOptionsMenu();
					}

					@Override
					public void onError() {
						super.onError();
						Logot.outError("FetchUserRelation error", getResult());
					}
				});
	}

}
