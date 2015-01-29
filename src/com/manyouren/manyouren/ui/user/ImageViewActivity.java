/**
 * @Package com.manyouren.android.ui.user    
 * @Title: UserAvatarActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-24 上午3:57:59 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.widget.gestureimg.GestureImageView;
import com.squareup.picasso.Picasso;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-24 上午3:57:59
 * 
 */
public class ImageViewActivity extends FragmentActivity {

	private static final String KEY_SMALL_URL = "smallUrl";

	private static final String KEY_SOURCE_URL = "sourceUrl";

	public static void createAndStartActivity(Activity currentActivity,
			String smallUrl, String sourceUrl) {
		Intent intent = new Intent(currentActivity, ImageViewActivity.class);
		intent.putExtra(KEY_SMALL_URL, smallUrl);
		intent.putExtra(KEY_SOURCE_URL, sourceUrl);
		currentActivity.startActivity(intent);
	}

	ProgressBar progressBar;
	GestureImageView zoomableImageView;
	String imageUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_imageview);

		zoomableImageView = (GestureImageView) findViewById(R.id.iv_zoom);

		progressBar = (ProgressBar) findViewById(R.id.pb_loading);

		getActionBar().setTitle("头像");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));

		imageUrl = getSourceUrl();
		if (imageUrl == null) {
			finish();
			return;
		}

		/*
		 * Picasso.with(BootstrapApplication.getInstance()).load(imageUrl)
		 * .placeholder(R.drawable.gravatar_icon).into(zoomableImageView);
		 */

		invalidate();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * @Description: TODO
	 * 
	 * @return void
	 */
	private void invalidate() {

		new Thread() {
			@Override
			public void run() {

				zoomableImageView.setVisibility(View.INVISIBLE);
				progressBar.setVisibility(View.VISIBLE);
				Picasso.with(RootApplication.getInstance()).load(imageUrl)
						.placeholder(R.drawable.gravatar_icon)
						.into(zoomableImageView);
				handler.sendEmptyMessage(1);

			}
		}.start();

	}

	public String getSmallUrl() {
		String smallUrl = getIntent().getStringExtra(KEY_SMALL_URL);
		if (smallUrl != null)
			return smallUrl;
		return null;
	}

	public String getSourceUrl() {
		return getIntent().getStringExtra(KEY_SOURCE_URL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return false;
	}

	Handler handler = new Handler() {
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				zoomableImageView.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.INVISIBLE);
				
				break;
			}
		};
	};

}
