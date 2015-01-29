/**
 * @Package com.manyouren.android.core.plan    
 * @Title: PlansActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-16 下午2:08:26 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.plan;

import static com.manyouren.manyouren.config.Constants.Extra.PLANS_ITEM;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.HttpConfig;
import com.manyouren.manyouren.controller.PlanController;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.entity.PlanEntity;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.service.location.LocationSourceActivity;
import com.manyouren.manyouren.ui.CircleTransform;
import com.manyouren.manyouren.ui.RoundedTransformation;
import com.manyouren.manyouren.ui.chatnew.ui.activity.ChatActivity;
import com.manyouren.manyouren.ui.user.ImageViewActivity;
import com.manyouren.manyouren.ui.user.UserActivity;
import com.manyouren.manyouren.ui.user.UserHeadActivity;
import com.manyouren.manyouren.util.DateUtils;
import com.squareup.picasso.Picasso;

/**
 * 
 * @author firefist_wei
 * @date 2014-6-16 下午2:08:26
 * 
 */
@SuppressLint("NewApi")
public class PlanActivity extends BaseActivity implements OnClickListener {

	private PlanEntity planItem;

	@InjectView(R.id.iv_avatar)
	protected ImageView avatar;

	@InjectView(R.id.tv_name)
	protected TextView name;

	@InjectView(R.id.tv_age)
	protected TextView age;

	@InjectView(R.id.tv_from)
	protected TextView from;

	@InjectView(R.id.tv_destination)
	protected TextView desti;

	@InjectView(R.id.tv_city)
	protected TextView tv_city;

	@InjectView(R.id.tv_startdate)
	protected TextView startdate;

	@InjectView(R.id.tv_postscript)
	protected TextView postscript;

	@InjectView(R.id.tv_createtime)
	protected TextView tv_createtime;

	@InjectView(R.id.iv_plan_pho1)
	protected ImageView pho1;

	@InjectView(R.id.iv_plan_pho2)
	protected ImageView pho2;

	@InjectView(R.id.iv_plan_pho3)
	protected ImageView pho3;

	@InjectView(R.id.btn_send_msg)
	protected Button send_msg;

	@InjectView(R.id.layout_user)
	RelativeLayout userLayout;

	@InjectView(R.id.iv_plan_for)
	public ImageView iv_plan_for;

	@InjectView(R.id.iv_plan_with)
	ImageView iv_plan_with;
	@InjectView(R.id.iv_plan_seek)
	ImageView iv_plan_seek;

	@InjectView(R.id.layout_plan_map)
	RelativeLayout layout_map;

	@InjectView(R.id.layout_plan_photo)
	LinearLayout layout_photo;

	@InjectView(R.id.layout_plan)
	LinearLayout layout_plan;

	String[] urls = new String[3];

	Context context;

	Double latitude = 0.0;
	Double longitude = 0.0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_plan);

		context = this;

		if (getIntent() != null && getIntent().getExtras() != null) {
			planItem = (PlanEntity) getIntent().getExtras().getSerializable(
					PLANS_ITEM);
		}

		setActionBar(getResources().getString(R.string.title_plan));

		if(!planItem.getScenicId().equals("0")){
			longitude = Double.valueOf(planItem.getLon());
			latitude = Double.valueOf(planItem.getLat());
		}

		initView();
		initEvent();
		init();
	}

	@Override
	protected void init() {

		name.setText(planItem.getUsername());

		from.setText("来自" + planItem.getResidence());
		

		if (planItem.getScenicId().equals("0")) {
			desti.setText(planItem.getMyPName()+" (未验证)");
			tv_city.setText("");
		}else{
			desti.setText(planItem.getpName());
			tv_city.setText("  " + planItem.getcName());
		}


		startdate.setText(DateUtils.getPlanDigitalDate(planItem.getStartDate(),
				planItem.getEndDate()));

		postscript.setText(planItem.getPostscript());

		tv_createtime.setText("发布于"
				+ DateUtils.getCreateTime(Long.valueOf(planItem.getCreateTime())));

		if (planItem.getAvatar0() == null
				|| planItem.getAvatar0().equals("")) {
			// 头像为空的
		} else {
			Picasso.with(this)
					.load(UserController.getAvatarDiff(planItem
							.getAvatar0())).transform(new CircleTransform())
					.placeholder(R.drawable.gravatar_icon).into(avatar);
		}

		age.setText(""
				+ UserController.getAgeFromDateString(planItem
						.getBirthday()));
		age.setBackgroundDrawable(RootApplication
				.getInstance()
				.getResources()
				.getDrawable(
						planItem.getGender().equals("0") ? R.drawable.ic_age_female_bg
								: R.drawable.ic_age_male_bg));

		if (planItem.getImages() != null && planItem.getImages().length() > 0) {
			List<String> listImages = PlanController.getPlanImages(planItem
					.getImages());

			layout_photo.setVisibility(View.VISIBLE);

			for (int i = 0; i < listImages.size(); i++) {

				Picasso.with(context).load(listImages.get(i)).resize(400, 400)
						.centerCrop().placeholder(R.drawable.gravatar_image)
						.into(i == 0 ? pho1 : (i == 1 ? pho2 : pho3));
				(i == 0 ? pho1 : (i == 1 ? pho2 : pho3))
						.setVisibility(View.VISIBLE);
				urls[i] = listImages.get(i);
			}
		}

		final LinearLayout layout_photo = (LinearLayout) findViewById(R.id.layout_plan_photo);

		if (urls[0] == null) {
			layout_photo.setVisibility(View.GONE);
		}

		int res_id = PlanController.res_for[Integer.valueOf(planItem.getType())];
		iv_plan_for.setImageResource(res_id);

		res_id = PlanController.res_with[Integer.valueOf(planItem.getTogether())];
		iv_plan_with.setImageResource(res_id);

		res_id = PlanController.res_seek[Integer.valueOf(planItem.getPurpose())];
		iv_plan_seek.setImageResource(res_id);

	}

	@Override
	protected void initView() {

		if (longitude == 0.0 && latitude == 0.0) {
			layout_map.setVisibility(View.GONE);
		}
	}

	@Override
	protected void initEvent() {
		send_msg.setOnClickListener(this);

		avatar.setOnClickListener(this);

		userLayout.setOnClickListener(this);

		pho1.setOnClickListener(this);

		pho2.setOnClickListener(this);

		pho3.setOnClickListener(this);

		iv_plan_for.setOnClickListener(this);

		iv_plan_with.setOnClickListener(this);

		iv_plan_seek.setOnClickListener(this);

		layout_map.setOnClickListener(this);

		layout_plan.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_plan:

			Bundle bundle = new Bundle();
			bundle.putString("planId", "" + planItem.getPlanId());
			bundle.putString("author", planItem.getUserId());
			bundle.putSerializable("PlanEntity", planItem);
			startActivity(PlanCommentActivity.class, bundle);
			break;

		case R.id.btn_send_msg:
			
			UserEntity userEntity = new UserEntity();
			userEntity.setUserId(Long.valueOf(planItem.getUserId()));
			userEntity.setUserName(planItem.getUsername());
			userEntity.setAvatar0(planItem.getAvatar0());
			userEntity.setObjectId(planItem.getObjectId());
			
			Intent intent = new Intent(context, ChatActivity.class);
		    intent.putExtra(ChatActivity.CHAT_USER_ID, planItem.getObjectId());
		    intent.putExtra("UserEntity", userEntity);
		    startActivity(intent);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);

			/*UserEntity userEntity = new UserEntity();
			userEntity.setUserId(Long.valueOf(planItem.getUserId()));
			userEntity.setUserName(planItem.getUsername());
			userEntity.setAvatar0(planItem.getAvatar0());
			
			startActivity(new Intent(PlanActivity.this, ChatActivity.class)
					.putExtra("ToUserId",planItem.getUserId())
					.putExtra("UserEntity",
							userEntity));*/
			break;

		case R.id.iv_avatar:

			// 头像为空时
			if (planItem.getAvatar0() == null
					|| planItem.getAvatar0().equals("")) {
				break;
			}
			String[] headUrl = new String[1];
			headUrl[0] = UserController.getAvatarDiff(planItem
					.getAvatar0());
			startActivity(new Intent(PlanActivity.this, UserHeadActivity.class)
					.putExtra(GalleryUrlActivity.GALLERY_URLS, headUrl)
					.putExtra(GalleryUrlActivity.GALLERY_INDEX, 0));
			/*
			 * ImageViewActivity.createAndStartActivity(PlanActivity.this,
			 * planItem.getUser().getAvatarSmall(), planItem.getUser()
			 * .getAvatarSmall());
			 */
			break;

		case R.id.layout_user:

			// 注意头像为空的问题
			startActivity(new Intent(PlanActivity.this, UserActivity.class)
					.putExtra(Constants.Extra.CHAT_TOID,
							String.valueOf(planItem.getUserId()))
					.putExtra("UserId",
							planItem.getUserId() + "")
					.putExtra("UserAvatar",
							planItem.getAvatar0())
					.putExtra("UserName",
							planItem.getUsername()));
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
			break;

		case R.id.iv_plan_pho1:

			startActivity(new Intent(PlanActivity.this,
					GalleryUrlActivity.class).putExtra(
					GalleryUrlActivity.GALLERY_URLS, urls).putExtra(
					GalleryUrlActivity.GALLERY_INDEX, 0));
			break;

		case R.id.iv_plan_pho2:

			startActivity(new Intent(PlanActivity.this,
					GalleryUrlActivity.class).putExtra(
					GalleryUrlActivity.GALLERY_URLS, urls).putExtra(
					GalleryUrlActivity.GALLERY_INDEX, 1));
			break;

		case R.id.iv_plan_pho3:

			startActivity(new Intent(PlanActivity.this,
					GalleryUrlActivity.class).putExtra(
					GalleryUrlActivity.GALLERY_URLS, urls).putExtra(
					GalleryUrlActivity.GALLERY_INDEX, 2));
			break;

		case R.id.iv_plan_for:
		case R.id.iv_plan_with:
		case R.id.iv_plan_seek:

			View Layout = getLayoutInflater().inflate(
					R.layout.dialog_plan_type,
					(ViewGroup) findViewById(R.id.dialog_layout_root));
			ImageView iv_for = (ImageView) (Layout
					.findViewById(R.id.dialog_iv_for));
			ImageView iv_with = (ImageView) (Layout
					.findViewById(R.id.dialog_iv_with));
			ImageView iv_seek = (ImageView) (Layout
					.findViewById(R.id.dialog_iv_seek));

			TextView tv_for = (TextView) (Layout
					.findViewById(R.id.dialog_tv_for));
			TextView tv_with = (TextView) (Layout
					.findViewById(R.id.dialog_tv_with));
			TextView tv_seek = (TextView) (Layout
					.findViewById(R.id.dialog_tv_seek));

			TextView tip_for = (TextView) (Layout.findViewById(R.id.tip_tv_for));
			TextView tip_with = (TextView) (Layout
					.findViewById(R.id.tip_tv_with));
			TextView tip_seek = (TextView) (Layout
					.findViewById(R.id.tip_tv_seek));

			int res_id = PlanController.res_for[Integer.valueOf(planItem.getType())];
			iv_for.setImageResource(res_id);

			res_id = PlanController.res_with[Integer.valueOf(planItem.getTogether())];
			iv_with.setImageResource(res_id);

			res_id = PlanController.res_seek[Integer.valueOf(planItem.getPurpose())];
			iv_seek.setImageResource(res_id);

			tv_for.setText(PlanController.list_for.get(Integer.valueOf(planItem.getType())));

			tv_with.setText(PlanController.list_with.get(Integer.valueOf(planItem.getTogether())));

			tv_seek.setText(PlanController.list_seek.get(Integer.valueOf(planItem.getPurpose())));

			tip_for.setText(PlanController.tip_for[Integer.valueOf(planItem.getType())]);

			tip_with.setText(PlanController.tip_with[Integer.valueOf(planItem.getTogether())]);

			tip_seek.setText(PlanController.tip_seek[Integer.valueOf(planItem.getPurpose())]);

			new AlertDialog.Builder(this).setView(Layout).setCancelable(true)
					.show();
			break;

		case R.id.layout_plan_map:
			startActivity(new Intent(PlanActivity.this,
					LocationSourceActivity.class).putExtra("lat", latitude+"")
					.putExtra("lon", longitude+""));

			overridePendingTransition(R.anim.left_in, R.anim.left_out);
			break;
		}
	}
}
