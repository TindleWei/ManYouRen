package com.manyouren.manyouren.core.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.ui.CircleTransform;
import com.manyouren.manyouren.ui.plan.GalleryFileActivity;
import com.manyouren.manyouren.ui.plan.GalleryUrlActivity;
import com.manyouren.manyouren.ui.plan.PlanActivity;
import com.manyouren.manyouren.ui.user.UserHeadActivity;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.PhotoUtils;
import com.squareup.picasso.Picasso;

public class ImageMessageItem extends MessageItem implements
		OnLongClickListener, OnClickListener {

	private ImageView mIvImage;
	private LinearLayout mLayoutLoading;
	private ImageView mIvLoading;
	private TextView mHtvLoadingText;

	private AnimationDrawable mAnimation;
	private int mProgress;
	private Bitmap mBitmap;

	public ImageMessageItem(ChatMessage msg, Context context) {
		super(msg, context);
	}

	@Override
	protected void onInitViews() {

		Logot.outInfo("TAG", "ImageMessgeItem onInitViews()");

		View view = mInflater.inflate(R.layout.message_image, null);
		mLayoutMessageContainer.addView(view);
		mIvImage = (ImageView) view.findViewById(R.id.message_iv_msgimage);
		mLayoutLoading = (LinearLayout) view
				.findViewById(R.id.message_layout_loading);
		mIvLoading = (ImageView) view.findViewById(R.id.message_iv_loading);
		mHtvLoadingText = (TextView) view
				.findViewById(R.id.message_htv_loading_text);
		mIvImage.setOnClickListener(this);
		mIvImage.setOnLongClickListener(this);
	}

	@Override
	protected void onFillMessage() {
		Logot.outInfo("TAG", "ImageMessgeItem onFillMessage()");

//		if (mMsg.getMessageType().equals(MESSAGE_TYPE.RECEIVER)) {
//			
//			Picasso.with(RootApplication.getInstance())
//					.load(mMsg.getContent())
//					.placeholder(R.drawable.gravatar_icon).into(mIvImage);
//			Logot.outInfo("TAG", "msgItem:" + "end");
//			
//		} else {
//			Logot.outInfo("TAG", "hi path :" + mMsg.getContent());
//			mBitmap = PhotoUtils.getBitmapFromFile(mMsg.getContent());
//			
//			if (mBitmap != null) {
//				mIvImage.setImageBitmap(mBitmap);
//			}
//		}
		 //mHandler.sendEmptyMessage(0);
	}

	public void getImage() {

	}

	@Override
	public void onClick(View v) {
		
//		String[] headUrl = new String[1];
//
//		if(mMsg.getMessageType().equals(MESSAGE_TYPE.RECEIVER)){
//			headUrl[0] = mMsg.getContent(); //这个是url
//
//			mContext.startActivity(new Intent(mContext, GalleryUrlActivity.class)
//					.putExtra(GalleryUrlActivity.GALLERY_URLS, headUrl)
//					.putExtra(GalleryUrlActivity.GALLERY_INDEX, 0));
//		}else{
//			
//			headUrl[0] = mMsg.getContent(); //这个是本地文件路径
//
//			mContext.startActivity(new Intent(mContext, GalleryFileActivity.class)
//					.putExtra(GalleryFileActivity.GALLERY_FILES, headUrl)
//					.putExtra(GalleryFileActivity.GALLERY_INDEX, 0));
//		}
	}

	@Override
	public boolean onLongClick(View v) {
		super.onLongClick(v);
		return false;
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				startLoadingAnimation();
				break;

			case 1:
				updateLoadingProgress();
				break;

			case 2:
				stopLoadingAnimation();
				break;
			}
		}
	};

	private void startLoadingAnimation() {
		mAnimation = new AnimationDrawable();
		mAnimation.addFrame(getDrawable(R.drawable.ic_loading_msgplus_01), 300);
		mAnimation.addFrame(getDrawable(R.drawable.ic_loading_msgplus_02), 300);
		mAnimation.addFrame(getDrawable(R.drawable.ic_loading_msgplus_03), 300);
		mAnimation.addFrame(getDrawable(R.drawable.ic_loading_msgplus_04), 300);
		mAnimation.setOneShot(false);
		mIvImage.setVisibility(View.GONE);
		mLayoutLoading.setVisibility(View.VISIBLE);
		mIvLoading.setVisibility(View.VISIBLE);
		mHtvLoadingText.setVisibility(View.VISIBLE);
		mIvLoading.setImageDrawable(mAnimation);
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				mAnimation.start();
			}
		});
		mHandler.sendEmptyMessage(1);
	}

	private void stopLoadingAnimation() {
		if (mAnimation != null) {
			mAnimation.stop();
			mAnimation = null;
		}
		mLayoutLoading.setVisibility(View.GONE);
		mHtvLoadingText.setVisibility(View.GONE);
		mIvImage.setVisibility(View.VISIBLE);
		if (mBitmap != null) {
			mIvImage.setImageBitmap(mBitmap);
		}
	}

	private void updateLoadingProgress() {
		if (mProgress < 100) {
			mProgress++;
			mHtvLoadingText.setText(mProgress + "%");
			mHandler.sendEmptyMessageDelayed(1, 100);
		} else {
			mProgress = 0;
			mHandler.sendEmptyMessage(2);
		}
	}

	@SuppressWarnings("deprecation")
	private Drawable getDrawable(int id) {
		return new BitmapDrawable(BitmapFactory.decodeResource(
				mContext.getResources(), id));
	}

}