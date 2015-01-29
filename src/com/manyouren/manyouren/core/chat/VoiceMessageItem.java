package com.manyouren.manyouren.core.chat;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.manyouren.manyouren.R;

public class VoiceMessageItem extends MessageItem implements OnClickListener,
		OnLongClickListener {

	private ImageView iv_start;
	private TextView tv_time;

	private Context context;

	int time = 0;
	String length = "";

	Timer timer = new Timer();

	Handler handler = new Handler() {
		@Override
		public void dispatchMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0: // 停止

				iv_start.setImageDrawable(context.getResources().getDrawable(
						R.drawable.ico_green_play));
				tv_time.setText(length + "'");
				time = 0;

				timer.cancel();
				break;

			case 1: // 播放
				iv_start.setImageDrawable(context.getResources().getDrawable(
						R.drawable.ico_green_pause));
				tv_time.setText("0'");

				timer = new Timer();
				timer.schedule(task, 1000, 1000);

				break;
			case 2:
				time++;
				tv_time.setText(time + "'");
				break;
			}
		};
	};

	public VoiceMessageItem(ChatMessage msg, Context context) {
		super(msg, context);
		this.context = context;
		// 获取语音地址
	}

	@Override
	protected void onInitViews() {
		View view = mInflater.inflate(R.layout.message_voice, null);
		mLayoutMessageContainer.addView(view);
		iv_start = (ImageView) view.findViewById(R.id.iv_voice_play);
		tv_time = (TextView) view.findViewById(R.id.tv_voice_time);
		view.setOnClickListener(this);
		view.setOnLongClickListener(this);
	}

	@Override
	protected void onFillMessage() {
//		tv_time.setText(mMsg.getLength() + "'");
//		length = mMsg.getLength();
	}

	@Override
	public boolean onLongClick(View v) {
		super.onLongClick(v);
		return false;
	}

	private TimerTask task;

	public void initTask() {
		task = new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(2);
			}
		};
	}

	@Override
	public void onClick(View view) {
		initTask();
		// Toast.makeText(context, mMsg.getContent(), 1000).show();
//		tv_time.setText(mMsg.getLength() + "'");
//		AudioPlayerSingleton.getInstance().stopPlayer();
//		AudioPlayerSingleton.getInstance().startPlayer(handler,
//				mMsg.getContent());
	}

}
