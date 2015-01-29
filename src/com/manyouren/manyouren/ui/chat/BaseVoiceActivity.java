package com.manyouren.manyouren.ui.chat;

import java.io.IOException;

import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import com.google.inject.Inject;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.core.chat.AudioPlayerSingleton;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.RecordUtil;
import com.manyouren.manyouren.util.ScreenUtils;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Toast;

public abstract class BaseVoiceActivity extends BaseChatActivity{

	@Inject
	protected Vibrator vibrator;
	
	protected int voiceLength =0;
	
	/*
	 * The part go on
	 */
	protected Animation recordingAnimation;

	protected RecordUtil mRecordUtil;

	protected static final int RECORD_NO = 0;
	protected static final int RECORD_ING = 1;
	protected static final int RECORD_ED = 2;

	protected static final int MAX_TIME = 60;
	protected static final int MIN_TIME = 2;

	protected int mRecord_State = 0;
	protected float mRecord_Time;
	protected double mRecord_Volume;
	protected boolean mPlayState;
	protected int mPlayCurrentPosition;

	protected final String TEMP_PATH = Environment
			.getExternalStorageDirectory().toString()
			+ "/ManYouRen/Temp/Audio/";
	protected final String FILE_HEAD = "aud";
	protected final String FILE_TAIL = "uid";
	protected static String mRecordPath = null;

	protected boolean audioCancel = false;
	protected int centerX, centerY, radium = 0;

	public void initVoiceView(Context context) {

		if (radium == 0) {
			int w = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			mSpeakerBtn.measure(w, h);

			centerX = (int) (mScreenWidth / 2);
			centerY = (int) (mScreenHeight - 20);
			radium = ScreenUtils.getIntPixels(context, 180);
			Logot.outInfo("TAG", "X:" + centerX + "Y:" + centerY + "R:"
					+ radium);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logot.outError("BaseVoiceActivity", "onCreate()");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if (vibrator != null)
			vibrator.cancel();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		AudioPlayerSingleton.getInstance().stopPlayer();
		
		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:

			if (v.getId() == R.id.chat_textditor_eet_editer) {

				if (mLayoutPlus.isShown()) {
					hidePlusBar();
				}

				if (mInputView.isShown()) {
					mInputView.setVisibility(View.GONE);
				}

				if (mLayoutSpeaker.isShown()) {
					mLayoutSpeaker.setVisibility(View.GONE);
				}
			}

			if (v.getId() == R.id.chat_audiobtn) {

				mSpeakerTime.setVisibility(View.VISIBLE);

				vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				long[] pattern = { 50, 50, 0, 0 }; // 停止 开启 停止 开启
				vibrator.vibrate(pattern, 1);
				mSpeakerTime.postDelayed(new Runnable() {

					@Override
					public void run() {
						vibrator.cancel();
					}
				}, 100);

				mSpeakerBtn.setImageDrawable(getResources().getDrawable(
						R.drawable.btn_speaker_red));

				if (mRecord_State != RECORD_ING) {
					mRecord_State = RECORD_ING;

					mRecordPath = TEMP_PATH + FILE_HEAD
							+ String.valueOf(System.currentTimeMillis())
							+ FILE_TAIL + ".amr";
					mRecordUtil = new RecordUtil(mRecordPath);

					try {
						mRecordUtil.start();
					} catch (IOException e) {
					}

					new Thread(new Runnable() {

						@Override
						public void run() {
							mRecord_Time = 0;
							while (mRecord_State == RECORD_ING) {
								if (mRecord_Time >= MAX_TIME) {
									mRecordHandler.sendEmptyMessage(2);
								} else {
									try {
										Thread.sleep(200);
										mRecord_Time += 0.2;
										if (mRecord_State == RECORD_ING) {
											mRecord_Volume = mRecordUtil
													.getAmplitude();
											mRecordHandler.sendEmptyMessage(3);
										}
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
						}
					}).start();
				}
			}

			break;
		case MotionEvent.ACTION_UP:

			mSpeakerTime.setVisibility(View.GONE);
			mSpeakerTip.setVisibility(View.GONE);

			mSpeakerBtn.setImageDrawable(getResources().getDrawable(
					R.drawable.btn_speaker_navy));

			if (mRecord_State == RECORD_ING) {
				mRecord_State = RECORD_ED;
				try {
					mRecordUtil.stop();
					mRecord_Volume = 0;
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (audioCancel == true) {
					Toast.makeText(context, "录音已取消", Toast.LENGTH_SHORT).show();
					// Here to delte temp
					audioCancel = false;
					mRecord_State = RECORD_NO;
					mRecord_Time = 0;
					mSpeakerTime.setText("0'");
					break;
				}

				if (mRecord_Time <= MIN_TIME) {
					Toast.makeText(context, "录音时间过短", Toast.LENGTH_SHORT)
							.show();
					mRecord_State = RECORD_NO;
					mRecord_Time = 0;
					mSpeakerTime.setText("0'");
					break;
				}
				voiceLength = (int) mRecord_Time;
				sendVoice(mRecordPath);

				// 存储 发送
				// +界面变化
			}
			break;

		case MotionEvent.ACTION_MOVE:
			if (mRecord_State == RECORD_ING) {

				if (Math.abs(event.getX() - centerX) > radium
						|| event.getY() < 0 || event.getX() - centerX > 0) {
					// Bug: event.getX()-centerX >0
					audioCancel = true;
					mSpeakerTip.setVisibility(View.VISIBLE);
				} else {
					audioCancel = false;
					mSpeakerTip.setVisibility(View.GONE);
				}
			}
			break;
		}
		return false;
	}
	
	Handler mRecordHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 2:
				if (mRecord_State == RECORD_ING) {
					mRecord_State = RECORD_ED;
					try {
						mRecordUtil.stop();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			case 3:
				mSpeakerTime.setText((int) mRecord_Time + " '");
				break;
			}
		}
	};
	
	public void sendVoice(String path){
		
	}

}
