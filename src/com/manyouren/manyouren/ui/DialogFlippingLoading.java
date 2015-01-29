/**
* @Package com.manyouren.android.ui    
* @Title: DialogFlippingLoading.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-6-23 上午10:31:00 
* @version V1.0   
*/
package com.manyouren.manyouren.ui;

import android.content.Context;
import android.widget.TextView;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.baseold.BaseDialog;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-6-23 上午10:31:00 
 *  
 */
public class DialogFlippingLoading  extends BaseDialog {

	private FlippingImageView mFivIcon;
	private TextView mHtvText;
	private String mText;

	public DialogFlippingLoading(Context context, String text) {
		super(context);
		mText = text;
		init();
	}

	private void init() {
		setContentView(R.layout.dialog_flipping_loading);
		mFivIcon = (FlippingImageView) findViewById(R.id.loadingdialog_fiv_icon);
		mHtvText = (TextView) findViewById(R.id.loadingdialog_htv_text);
		mFivIcon.startAnimation();
		mHtvText.setText(mText);
	}

	public void setText(String text) {
		mText = text;
		mHtvText.setText(mText);
	}

	@Override
	public void dismiss() {
		if (isShowing()) {
			super.dismiss();
		}
	}
}
