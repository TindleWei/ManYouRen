package com.manyouren.manyouren.core.chat;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.ui.CircleTransform;
import com.manyouren.manyouren.ui.chat.ChatActivity;
import com.manyouren.manyouren.ui.plan.PlanActivity;
import com.manyouren.manyouren.ui.user.UserActivity;
import com.manyouren.manyouren.util.Logot;
import com.squareup.picasso.Picasso;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public abstract class MessageItem implements OnLongClickListener {

	protected Context mContext;
	protected View mRootView;

	/**
	 * TimeStampContainer
	 */
	private RelativeLayout mLayoutTimeStampContainer;
	private TextView mHtvTimeStampTime;

	/**
	 * MessageContainer
	 */
	protected LinearLayout mLayoutMessageContainer;

	/**
	 * RightContainer
	 */
	private LinearLayout mLayoutRightContainer;
	private ImageView mIvPhotoView;

	protected LayoutInflater mInflater;
	protected ChatMessage mMsg;

	protected int mBackground;

	public MessageItem(ChatMessage msg, Context context) {
		mMsg = msg;
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	public static MessageItem getInstance(ChatMessage msg, Context context) {
		MessageItem messageItem = null;

//		switch (msg.getContentType()) {
//
//		case TEXT:
//			messageItem = new TextMessageItem(msg, context);
//			break;
//
//		case IMAGE:
//			messageItem = new ImageMessageItem(msg, context);
//
//			Logot.outInfo("TAG", "MessageItem image");
//			break;
//
//		case MAP:
//			messageItem = new MapMessageItem(msg, context);
//			break;
//
//		case VOICE:
//			messageItem = new VoiceMessageItem(msg, context);
//			
//			break;
//
//		case PLAN:
//			messageItem = new PlanMessageItem(msg, context);
//
//			break;
//
//		}
//		messageItem.init(msg.getMessageType());
		return messageItem;
	}

//	private void init(MESSAGE_TYPE messageType) {
//		switch (messageType) {
//		case RECEIVER:
//			mRootView = mInflater.inflate(R.layout.message_group_receive, null);
//			mBackground = R.drawable.bg_message_box_receive;
//			break;
//
//		case SEND:
//			mRootView = mInflater.inflate(R.layout.message_group_send_template,
//					null);
//			mBackground = R.drawable.bg_message_box_send;
//			break;
//		}
//		if (mRootView != null) {
//			initViews(mRootView);
//		}
//	}

	protected void initViews(View view) {
		mLayoutTimeStampContainer = (RelativeLayout) view
				.findViewById(R.id.message_layout_timecontainer);
		mHtvTimeStampTime = (TextView) view
				.findViewById(R.id.message_timestamp_htv_time);

		mLayoutMessageContainer = (LinearLayout) view
				.findViewById(R.id.message_layout_messagecontainer);
		mLayoutMessageContainer.setBackgroundResource(mBackground);

		mLayoutRightContainer = (LinearLayout) view
				.findViewById(R.id.message_layout_rightcontainer);
		mIvPhotoView = (ImageView) view.findViewById(R.id.message_iv_userphoto);
		onInitViews();
	}

	public void fillContent() {

		fillStatus();
		fillMessage();
		fillPhotoView();
	}

//	public void showTimeStamp() {
//		fillTimeStamp();
//	}

	protected void fillMessage() {
		onFillMessage();
	}

//	protected void fillTimeStamp() {
//		mLayoutTimeStampContainer.setVisibility(View.VISIBLE);
//		if (mMsg.getTime() != 0) {
//			mHtvTimeStampTime.setText(" "
//					+ formatDate(mContext, mMsg.getTime()) + " ");
//		}
//	}
	
	public static String formatDate(Context context, long date) {
		int format_flags = android.text.format.DateUtils.FORMAT_ABBREV_ALL
				| android.text.format.DateUtils.FORMAT_SHOW_DATE
				| android.text.format.DateUtils.FORMAT_SHOW_DATE
				| android.text.format.DateUtils.FORMAT_SHOW_TIME;
		return android.text.format.DateUtils.formatDateTime(context, date,
				format_flags);
	}

	protected void fillStatus() {

	}

	protected void fillPhotoView() {
		mLayoutRightContainer.setVisibility(View.VISIBLE);

//		if (mMsg.getMessageType().equals(Message.ChatMessage.SEND)) {
//			Picasso.with(RootApplication.getInstance())
//					.load(ChatActivity.meUserAvatar)
//					.transform(new CircleTransform())
//					.placeholder(R.drawable.gravatar_icon).into(mIvPhotoView);
//
//			mIvPhotoView.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					//个人界面
//				}
//			});
//
//		} else {
//			Picasso.with(RootApplication.getInstance()).load(mMsg.getAvatar())
//					.transform(new CircleTransform())
//					.placeholder(R.drawable.gravatar_icon).into(mIvPhotoView);
//
//			mIvPhotoView.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					
//					//这里的头像是这样的： 
//					//http://www.travelman.com.cn/uploads/63e779631cb363e821e3ed894e3688640.jpg
//					mContext.startActivity(new Intent(mContext, UserActivity.class)
//					.putExtra("UserId",
//							ChatActivity.toUserEntity.getUserId() + "")
//					.putExtra("UserAvatar",
//							ChatActivity.toUserEntity.getAvatar0())
//					.putExtra("UserName",
//							ChatActivity.toUserEntity.getUserName()));
//				}
//			});
//		}

	}

	protected void refreshAdapter() {
		((ChatActivity) mContext).refreshAdapter();
	}

	public View getRootView() {
		return mRootView;
	}

	protected abstract void onInitViews();

	protected abstract void onFillMessage();

	@Override
	public boolean onLongClick(View v) {

		new AlertDialog.Builder(mContext)
				.setItems(new String[] { "删除该消息" },
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									Toast.makeText(mContext, "暂时删除不了亲", 1000)
											.show();
									break;
								}
							}
						}).create().show();
		return false;
	}

}
