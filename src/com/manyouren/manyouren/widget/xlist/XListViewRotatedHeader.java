package com.manyouren.manyouren.widget.xlist;

import com.manyouren.manyouren.R;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class XListViewRotatedHeader extends LinearLayout {

	static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();

	static final int ROTATION_ANIMATION_DURATION = 1200;

	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;

	private TextView mHintTextView;
	private int mState = STATE_NORMAL;

	protected ImageView mHeaderImage = null;
	protected ProgressBar mHeaderProgress = null;

	private Animation mRotateAnimation = null;
	private Matrix mHeaderImageMatrix = null;

	private float mRotationPivotX = 0, mRotationPivotY = 0;

	private LinearLayout mContainer;

	public XListViewRotatedHeader(Context context) {
		super(context);
		initView(context);
	}

	public XListViewRotatedHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		// 初始情况，设置下拉刷新view高度为0
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.pull_to_refresh_header_horizontal, null);
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);

		mHeaderImage.setScaleType(ScaleType.MATRIX);
		mHeaderImageMatrix = new Matrix();
		mHeaderImage.setImageMatrix(mHeaderImageMatrix);

		mRotateAnimation = new RotateAnimation(0, 720,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		mRotateAnimation.setRepeatMode(Animation.RESTART);
	}

	public void setState(int state) {
		if (state == mState)
			return;

		if (state == STATE_REFRESHING) { // 显示进度
			mHeaderImage.startAnimation(mRotateAnimation);

		} else { // 显示箭头图片
			
			/*float scaleOfLayout = Math.abs(newScrollValue) / (float) itemDimension;
			float angle;
			angle = scaleOfLayout * 90f;

			mHeaderImageMatrix.setRotate(angle, mRotationPivotX,
					mRotationPivotY);
			mHeaderImage.setImageMatrix(mHeaderImageMatrix);*/
		}

		switch (state) {
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				mHeaderImage.startAnimation(mRotateAnimation);
			}
			if (mState == STATE_REFRESHING) {
				mHeaderImage.clearAnimation();
				if (null != mHeaderImageMatrix) {
					mHeaderImageMatrix.reset();
					mHeaderImage.setImageMatrix(mHeaderImageMatrix);
				}
			}
			mHintTextView.setText(R.string.xlistview_header_hint_normal);
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				mHeaderImage.clearAnimation();
				if (null != mHeaderImageMatrix) {
					mHeaderImageMatrix.reset();
					mHeaderImage.setImageMatrix(mHeaderImageMatrix);
				}
				mHeaderImage.startAnimation(mRotateAnimation);
				mHintTextView.setText(R.string.xlistview_header_hint_ready);
			}
			break;
		case STATE_REFRESHING:
			mHintTextView.setText(R.string.xlistview_header_hint_loading);
			break;
		default:
		}

		mState = state;
	}

	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer
				.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		return mContainer.getHeight();
	}

}
