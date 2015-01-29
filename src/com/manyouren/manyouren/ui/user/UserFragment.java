/**
 * @Package com.manyouren.android.core.user    
 * @Title: UserFragment.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-16 下午10:19:36 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.user;

import org.json.JSONObject;

import roboguice.inject.InjectView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.base.BaseFragment;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.service.PicassoService;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.ui.CircleTransform;
import com.manyouren.manyouren.ui.usernew.UserPhotosActivity;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.widget.PullScrollView;
import com.squareup.picasso.Picasso;

/**
 * 
 * @author firefist_wei
 * @date 2014-6-16 下午10:19:36
 * 
 */
public class UserFragment extends BaseFragment implements OnClickListener {

	private UserEntity userEntity;

	private TextView userName;

	private ImageView userAvatar;

	private ImageView mHeadImg;

	private String avatarString;

	@InjectView(R.id.tv_age)
	private TextView tv_age;

	@InjectView(R.id.tv_residence)
	private TextView tv_residence;

	@InjectView(R.id.layout_my_plan)
	RelativeLayout layout_my_plan;

	@InjectView(R.id.layout_my_album)
	RelativeLayout layout_my_album;

	@InjectView(R.id.layout_my_friends)
	RelativeLayout layout_my_friends;

	@InjectView(R.id.layout_my_settings)
	RelativeLayout layout_my_settings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_user, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		initView();
		initEvent();
		init();
	}

	@Override
	protected void initView() {
		userName = (TextView) getActivity().findViewById(R.id.tv_name_me);
		userAvatar = (ImageView) getActivity().findViewById(R.id.iv_avatar_me);

		mHeadImg = (ImageView) getActivity().findViewById(R.id.background_img);
	}

	@Override
	protected void initEvent() {
		
		layout_my_plan.setOnClickListener(this);
		layout_my_album.setOnClickListener(this);
		layout_my_friends.setOnClickListener(this);
		layout_my_settings.setOnClickListener(this);

		userAvatar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String[] headUrl = new String[1];
				headUrl[0] = avatarString;

				startActivity(new Intent(getActivity(), UserInfoActivity.class));
				activity.overridePendingTransition(R.anim.left_in,
						R.anim.left_out);
			}
		});
	}

	@SuppressLint("NewApi")
	@Override
	protected void init() {

		userEntity = UserController.getUserById(getActivity(),
				Long.valueOf(PreferenceHelper.getUserId()));

		if (userEntity.getUserName().toString().equals("")) {
			userName.setText("漫游号：" + userEntity.getUserId());
		} else {
			userName.setText(userEntity.getUserName());
		}	

		PicassoService.setCirclePhoto(
				UserController.getAvatarDiff(userEntity.getAvatar0()),
				userAvatar);
		
		Logot.outError("Fuck avatar", UserController.getAvatarDiff(userEntity.getAvatar0()));

		tv_age.setText(UserController.getAgeFromDateString(userEntity.getBirthday())
				+ "");
		tv_age.setBackgroundDrawable(context.getResources().getDrawable(
				userEntity.getGender() == 1 ? R.drawable.ic_age_male_bg
						: R.drawable.ic_age_female_bg));

		tv_residence.setText("来自" + userEntity.getResidence());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_my_plan:
			startActivity(new Intent(getActivity(),
					 UserPlansActivity.class));
			activity.overridePendingTransition(R.anim.left_in,
					R.anim.left_out);
			break;

		case R.id.layout_my_album:
			startActivity(new Intent(getActivity(),
					 UserPhotosActivity.class));
			activity.overridePendingTransition(R.anim.left_in,
					R.anim.left_out);
			break;

		case R.id.layout_my_friends:
			startActivity(new Intent(getActivity(),
					 FriendsActivity.class));
			activity.overridePendingTransition(R.anim.left_in,
					R.anim.left_out);
			break;

		case R.id.layout_my_settings:
			startActivity(new Intent(getActivity(),
					 SettingsActivity.class));
			activity.overridePendingTransition(R.anim.left_in,
					R.anim.left_out);
			break;

		default:
			break;
		}
	}
}
