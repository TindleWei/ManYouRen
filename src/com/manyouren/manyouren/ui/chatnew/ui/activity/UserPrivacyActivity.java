package com.manyouren.manyouren.ui.chatnew.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.ui.CircleTransform;
import com.manyouren.manyouren.widget.SlipButton;
import com.manyouren.manyouren.widget.SlipButton.OnChangedListener;
import com.squareup.picasso.Picasso;

public class UserPrivacyActivity extends BaseActivity implements
		OnClickListener {

	ImageView iv_avatar;

	TextView tv_name;

	private SlipButton stopTipBtn;

	String STOP_TIP = "stop_tip";
	
	String uid = "";
	String userName="";
	String userAvatar="";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_userprivacy);

		setActionBar("好友");
		
		uid = getIntent().getStringExtra("uid");
		userName = getIntent().getStringExtra("userName");
		userAvatar = getIntent().getStringExtra("userAvatar"); 

		initView();
		initEvent();
		init();
	}

	@Override
	protected void initView() {
		iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
		tv_name = (TextView) findViewById(R.id.tv_name);
		stopTipBtn = (SlipButton) findViewById(R.id.stop_switch_Btn);
	}

	@Override
	protected void initEvent() {
		iv_avatar.setOnClickListener(this);
		tv_name.setOnClickListener(this);

		stopTipBtn.setOnChangedListener(STOP_TIP, mOnChangedListener);
	}

	@Override
	protected void init() {
		tv_name.setText(userName);
		Picasso.with(RootApplication.getInstance()).load(UserController.getAvatarDiff(userAvatar))
		.transform(new CircleTransform())
		.placeholder(R.drawable.gravatar_icon).into(iv_avatar);
		
		stopTipBtn.setChecked(false);

	}

	private final OnChangedListener mOnChangedListener = new OnChangedListener() {
		@Override
		public void OnChanged(String name, final boolean isChecked) {
			if (name.equals(STOP_TIP)) {
			}
		}
	};

	public void onClick(View v) {
		v.setPressed(true);
		switch (v.getId()) {
		case R.id.iv_avatar:
			break;
		case R.id.tv_name:
			break;
		}
	}

}
