package com.manyouren.manyouren.ui;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.baseold.BaActivity;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.PhotoUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ViewFlipper;

public class ImageFactoryActivity extends BaseActivity {
	private ViewFlipper mVfFlipper;
	private Button mBtnLeft;
	private Button mBtnRight;

	private ImageFactoryCrop mImageFactoryCrop;
	private ImageFactoryFliter mImageFactoryFliter;
	private String mPath;
	private String mNewPath;
	private int mIndex = 1;//0 crop  1 filter
	private String mType;

	public static final String TYPE = "type";
	public static final String CROP = "crop";
	public static final String FLITER = "fliter";
	
	protected int mScreenWidth;
	protected int mScreenHeight;
	protected float mDensity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imagefactory);
		setActionBar("图片处理");
		
		DisplayMetrics metric = new DisplayMetrics();
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;
		
		initView();
		initEvent();
		init();
	}

	@Override
	protected void init() {
		mPath = getIntent().getStringExtra("path");
		
		mType = getIntent().getStringExtra(TYPE);
		mNewPath = new String(mPath);
		if (CROP.equals(mType)) {
			mIndex = 0;
		} else if (FLITER.equals(mType)) {
			mIndex = 1;
			mVfFlipper.showPrevious();
		}
		initImageFactory();
	}

	@Override
	protected void initView() {
		
		mVfFlipper = (ViewFlipper) findViewById(R.id.imagefactory_vf_viewflipper);
		mBtnLeft = (Button) findViewById(R.id.imagefactory_btn_left);
		mBtnRight = (Button) findViewById(R.id.imagefactory_btn_right);
	}

	@Override
	protected void initEvent() {
		mBtnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mIndex == 0) {
					setResult(RESULT_CANCELED);
					finish();
				} else {
					if (FLITER.equals(mType)) {
						setResult(RESULT_CANCELED);
						finish();
					} else {
						mIndex = 0;
						initImageFactory();
						mVfFlipper.setInAnimation(ImageFactoryActivity.this,
								R.anim.right_in);
						mVfFlipper.setOutAnimation(ImageFactoryActivity.this,
								R.anim.right_out);
						mVfFlipper.showPrevious();
					}
				}
			}
		});
		mBtnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mIndex == 1) {
					mNewPath = PhotoUtils.savePhotoToSDCard(mImageFactoryFliter
							.getBitmap());
					Intent intent = new Intent();
					intent.putExtra("path", mNewPath);
					setResult(RESULT_OK, intent);
					finish();
				} else {
					mNewPath = PhotoUtils.savePhotoToSDCard(mImageFactoryCrop
							.cropAndSave());
					mIndex = 1;
					initImageFactory();
					mVfFlipper.setInAnimation(ImageFactoryActivity.this,
							R.anim.left_in);
					mVfFlipper.setOutAnimation(ImageFactoryActivity.this,
							R.anim.left_out);
					mVfFlipper.showNext();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (mIndex == 0) {
			setResult(RESULT_CANCELED);
			finish();
		} else {
			if (FLITER.equals(mType)) {
				setResult(RESULT_CANCELED);
				finish();
			} else {
				mIndex = 0;
				initImageFactory();
				mVfFlipper.setInAnimation(ImageFactoryActivity.this,
						R.anim.right_in);
				mVfFlipper.setOutAnimation(ImageFactoryActivity.this,
						R.anim.right_out);
				mVfFlipper.showPrevious();
			}
		}
	}


	private void initImageFactory() {
		switch (mIndex) {
		case 0:
			if (mImageFactoryCrop == null) {
				mImageFactoryCrop = new ImageFactoryCrop(this,
						mVfFlipper.getChildAt(0));
			}
			mImageFactoryCrop.init(mPath, mScreenWidth, mScreenHeight);
			
			getActionBar().setTitle("裁剪图片");
			mBtnLeft.setText("取    消");
			mBtnRight.setText("确    认");

			break;

		case 1:
			if (mImageFactoryFliter == null) {
				mImageFactoryFliter = new ImageFactoryFliter(this,
						mVfFlipper.getChildAt(1));
			}
			mImageFactoryFliter.init(mNewPath);
			
			Logot.outError("TAG   FUCK", mNewPath+"");
			
			getActionBar().setTitle("图片");
			//getActionBar().setTitle("图片滤镜");
			mBtnLeft.setText("取    消");
			mBtnRight.setText("完    成");
			break;
		}
	}
	
	private static final int MENUITEM_ROTATE = 1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem rotate = menu.add(0, MENUITEM_ROTATE, 0, "Roate");
		rotate.setIcon(this.getResources().getDrawable(
				R.drawable.ic_action_rotate_right));
		rotate.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case MENUITEM_ROTATE:
			if(mIndex==0){
				if (mImageFactoryCrop != null) {
					mImageFactoryCrop.Rotate();
				}
			}else{
				if (mImageFactoryFliter != null) {
					mImageFactoryFliter.Rotate();
				}
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}


