/**
 * @Package com.manyouren.android.ui.chat    
 * @Title: BaseMessageActivity.java 
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-27 下午10:44:11 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.chat;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.baseold.BaActivity;
import com.manyouren.manyouren.core.chat.ChatMessage;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.RecordUtil;
import com.manyouren.manyouren.util.ScreenUtils;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BaseChatActivity extends BaseActivity implements
		OnClickListener, OnTouchListener, TextWatcher {

	@InjectView(R.id.chat_textditor_ib_plus)
	protected ImageView iv_Plus;

	@InjectView(R.id.chat_textditor_ib_keyboard)
	protected ImageView iv_KeyBoard;

	@InjectView(R.id.chat_textditor_ib_emote)
	protected ImageView iv_Emotion;

	@InjectView(R.id.chat_textditor_btn_send)
	protected ImageView iv_send;

	@InjectView(R.id.chat_textditor_iv_audio)
	protected ImageView iv_audio;

	@InjectView(R.id.fullscreen_mask)
	protected LinearLayout mLayoutFullScreenMask;

	@InjectView(R.id.message_plus_layout_bar)
	protected LinearLayout mLayoutPlus;

	@InjectView(R.id.message_plus_layout_picture)
	protected LinearLayout mLayoutPlusPictureBtn;

	@InjectView(R.id.message_plus_layout_location)
	protected LinearLayout mLayoutPlusLocationBtn;

	@InjectView(R.id.message_plus_layout_plan)
	protected LinearLayout mLayoutPlusPlanBtn;

	@InjectView(R.id.message_plus_layout_more)
	protected LinearLayout mLayoutPlusMoreBtn;

	@InjectView(R.id.layout_speaker_bar)
	protected LinearLayout mLayoutSpeaker;

	@InjectView(R.id.chat_audiobtn)
	protected ImageView mSpeakerBtn;
	
	@InjectView(R.id.tv_voice_time)
	protected TextView mSpeakerTime;
	
	@InjectView(R.id.tv_voice_tip)
	protected TextView mSpeakerTip;

	public static List<ChatMessage> mMessages = new ArrayList<ChatMessage>();

	protected ChatListView mListView;

	protected ChatAdapter mAdapter;

	protected EmoteInputView mInputView; // 表情选择的View

	protected EmoticonsEditText emotion_et; // 可显示表情的EditText

	protected String mCameraImagePath;

	protected int mCheckId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logot.outError("BaseChatActivity", "onCreate()");
		setContentView(R.layout.activity_chat);

		//initView();
		//initEvent();
	}
	
	@Override
	protected void initView(){

		mListView = (ChatListView) findViewById(R.id.chat_clv_list);
		mInputView = (EmoteInputView) findViewById(R.id.chat_eiv_inputview);
		emotion_et = (EmoticonsEditText) findViewById(R.id.chat_textditor_eet_editer);
	}

	@Override
	protected void initEvent(){
		iv_Plus.setOnClickListener(this);
		iv_Emotion.setOnClickListener(this);
		iv_KeyBoard.setOnClickListener(this);
		iv_send.setOnClickListener(this);
		iv_audio.setOnClickListener(this);
		emotion_et.addTextChangedListener(this);
		emotion_et.setOnTouchListener(this);

		mLayoutFullScreenMask.setOnTouchListener(this);
		mLayoutPlusPictureBtn.setOnClickListener(this);
		mLayoutPlusLocationBtn.setOnClickListener(this);
		mLayoutPlusPlanBtn.setOnClickListener(this);
		mLayoutPlusMoreBtn.setOnClickListener(this);

		mSpeakerBtn.setOnTouchListener(this);
	}

	protected void showKeyBoard() {
		if (mInputView.isShown()) {
			mInputView.setVisibility(View.GONE);
		}
		emotion_et.requestFocus();
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.showSoftInput(emotion_et, 0);
	}

	protected void hideKeyBoard() {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(BaseChatActivity.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
	}

	protected void showPlusBar() {
		mLayoutFullScreenMask.setEnabled(false);
		mLayoutPlus.setEnabled(true);
		mLayoutPlusPictureBtn.setEnabled(true);
		mLayoutPlusLocationBtn.setEnabled(true);
		mLayoutPlusPlanBtn.setEnabled(true);
		mLayoutPlusMoreBtn.setEnabled(true);
		Animation animation = AnimationUtils.loadAnimation(
				BaseChatActivity.this, R.anim.controller_enter);
		mLayoutPlus.setAnimation(animation);
		mLayoutPlus.setVisibility(View.VISIBLE);
		mLayoutFullScreenMask.setVisibility(View.VISIBLE);
	}

	protected void hidePlusBar() {
		mLayoutFullScreenMask.setEnabled(false);
		mLayoutPlus.setEnabled(false);
		mLayoutPlusPictureBtn.setEnabled(false);
		mLayoutPlusLocationBtn.setEnabled(false);
		mLayoutPlusPlanBtn.setEnabled(false);
		mLayoutPlusMoreBtn.setEnabled(false);
		mLayoutFullScreenMask.setVisibility(View.GONE);
		Animation animation = AnimationUtils.loadAnimation(
				BaseChatActivity.this, R.anim.controller_exit);
		animation.setInterpolator(AnimationUtils.loadInterpolator(
				BaseChatActivity.this, android.R.anim.anticipate_interpolator));
		mLayoutPlus.setAnimation(animation);
		mLayoutPlus.setVisibility(View.GONE);
	}

	
}
