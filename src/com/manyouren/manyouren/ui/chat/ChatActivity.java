/**
 * @Package com.manyouren.android.ui.chat    
 * @Title: ChatActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-27 下午10:43:27 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVUtils;
import com.avos.avoscloud.Session;
import com.avos.avoscloud.SessionManager;
import com.google.gson.Gson;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.controller.ChatController;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.core.chat.AudioPlayerSingleton;
import com.manyouren.manyouren.core.chat.ChatMessage;
import com.manyouren.manyouren.core.chat.ChatMessage.MessageType;
import com.manyouren.manyouren.entity.MessageEntity;
import com.manyouren.manyouren.entity.PlanEntity;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.service.greendao.GreenChat;
import com.manyouren.manyouren.service.greendao.GreenUser;
import com.manyouren.manyouren.service.jpush.JPushUtils;
import com.manyouren.manyouren.ui.user.UserActivity;
import com.manyouren.manyouren.ui.user.UserPlansActivity;
import com.manyouren.manyouren.util.PhotoUtils;
import com.manyouren.manyouren.util.ScreenUtils;

public class ChatActivity extends BaseVoiceActivity {

	public static final int IMAGE_REQUEST = 0;
	public static final int LOCATION_REQUEST = 1;
	public static final int TAKE_CAMERA_REQUEST = 2;
	public static final int INTENT_REQUEST_GET_PLAN = 10;
	public static final int PAGE_SIZE = 20;
	
	public static boolean singleChat;
	public static final String CHAT_USER_ID = "chatUserId";
	public static final String GROUP_ID = "groupId";
	public static final String SINGLE_CHAT = "singleChat";
	
//	User chatUser;
//	Group group;
//	ChatGroup chatGroup;
//	String audioId;

	String toUserId = "";
	public static UserEntity toUserEntity = new UserEntity();

	String meUserId = "";
	public static String meUserAvatar = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setActionBar("对话");

		meUserId = PreferenceHelper.getUserId();
		meUserAvatar = UserController.getAvatarDiff(GreenUser.getInstance(this)
				.getUserAvatar(Long.valueOf(meUserId)));
		toUserId = getIntent().getStringExtra("ToUserId");
		toUserEntity = (UserEntity) getIntent().getSerializableExtra(
				"UserEntity");

		getActionBar().setTitle(toUserEntity.getUserName());

		initView();
		initEvent();
		init();
		initAVMessage();
	}

	String targetPeerId;
	String selfId;
	Session session;

	private void initAVMessage() {
		selfId = AVInstallation.getCurrentInstallation().getInstallationId();
		session = SessionManager.getInstance(selfId);

		// 下面这个不知道是干嘛的，先写上
		if (!AVUtils.isBlankString(getIntent().getExtras().getString(
				Session.AV_SESSION_INTENT_DATA_KEY))) {
			String msg = getIntent().getExtras().getString(
					Session.AV_SESSION_INTENT_DATA_KEY);
			ChatMessage message = new Gson().fromJson(msg, ChatMessage.class);
			mMessages.add(message);
			mAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 从数据库获取数据
	 * 
	 */
	private void getGreenData() {

		List<MessageEntity> msgList = ChatController.getLocalMsgs(context,
				Long.valueOf(toUserId));
		List<ChatMessage> list = new ArrayList<ChatMessage>();

		for (int i = 0; i < msgList.size(); i++) {
			MessageEntity entity = msgList.get(i);

			if (entity.getMessageType().equals("RECEIVER")) {

			} else if (entity.getMessageType().equals("SEND")) {

			}

			if (entity.getContentType().equals("TEXT")) {

			} else if (entity.getContentType().equals("IMAGE")) {

			} else if (entity.getContentType().equals("MAP")) {

			} else if (entity.getContentType().equals("VOICE")) {

			} else if (entity.getContentType().equals("PLAN")) {

			}

//			if (entity.getContentType().equals("VOICE")) {
//				ChatMessage msg = new ChatMessage(entity.getAvatar(),
//						entity.getTime(), entity.getContent(), contentType,
//						messageType);
//				list.add(msg);
//			} else {
//				list.add(new ChatMessage(entity.getAvatar(), entity.getTime(),
//						entity.getContent(), contentType, messageType));
//			}
		}
		mMessages = list;
//		chatHandler.sendEmptyMessage(3);
	}

	@Override
	protected void initView() {
		super.initView();
		initVoiceView(context);
	}

	@Override
	protected void initEvent() {
		super.initEvent();
	}

	@Override
	protected void init() {
		getGreenData();

		mInputView.setEditText(emotion_et);

		// Add it to optimize UI bottom
		TextView mTextView = new TextView(this);
		mTextView.setText("   ");
		mTextView.setHeight(ScreenUtils.getIntPixels(this, 72));
		LinearLayout mLinearLayout = new LinearLayout(this);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mLinearLayout.addView(mTextView, mLayoutParams);
		mListView.addFooterView(mLinearLayout);

		mAdapter = new ChatAdapter(ChatActivity.this, mMessages);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_textditor_ib_plus:
			if (!mLayoutPlus.isShown()) {
				showPlusBar();

				if (mInputView.isShown()) {
					mInputView.setVisibility(View.GONE);
				}
				if (mLayoutSpeaker.isShown()) {
					mLayoutSpeaker.setVisibility(View.GONE);
				}
				hideKeyBoard();
			} else {
				hidePlusBar();
			}
			break;

		case R.id.chat_textditor_ib_emote:
			if (mLayoutPlus.isShown()) {
				hidePlusBar();
			}
			if (mLayoutSpeaker.isShown()) {
				mLayoutSpeaker.setVisibility(View.GONE);
			}
			iv_KeyBoard.setVisibility(View.VISIBLE);
			iv_Emotion.setVisibility(View.GONE);
			emotion_et.requestFocus();
			if (mInputView.isShown()) {
				hideKeyBoard();
			} else {
				hideKeyBoard();
				mInputView.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.chat_textditor_ib_keyboard:
			if (mLayoutPlus.isShown()) {
				hidePlusBar();
			}
			iv_KeyBoard.setVisibility(View.GONE);
			iv_Emotion.setVisibility(View.VISIBLE);
			showKeyBoard();
			break;

		case R.id.chat_textditor_btn_send:

			String content = emotion_et.getText().toString().trim();

			if (!TextUtils.isEmpty(content)) {
				emotion_et.setText(null);
//				fetchRequest("TEXT", content);
			}
			break;

		case R.id.message_plus_layout_picture:
			PhotoUtils.selectPhoto(ChatActivity.this);
			hidePlusBar();
			break;

		case R.id.message_plus_layout_location:

//			mMessages.add(new ChatMessage(meUserAvatar, System
//					.currentTimeMillis(), null, CONTENT_TYPE.MAP,
//					MESSAGE_TYPE.SEND));
			mAdapter.notifyDataSetChanged();
			mListView.setSelection(mMessages.size());
			hidePlusBar();
			break;

		case R.id.message_plus_layout_plan:

			Intent planIntent = new Intent(ChatActivity.this,
					UserPlansActivity.class);
			planIntent.putExtra("extraType", "sendPlan");
			startActivityForResult(planIntent, INTENT_REQUEST_GET_PLAN);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);

			hidePlusBar();
			break;

		case R.id.message_plus_layout_more:
			hidePlusBar();
			break;

		case R.id.right_layout:

			try {
				String avatarString = (new JSONObject(toUserEntity.getAvatar0()))
						.getString("avatar");
				Bundle userIntent = new Bundle();
				userIntent.putString("UserId", "" + toUserEntity.getUserId());
				userIntent.putString("UserAvatar", avatarString);
				userIntent.putString("UserName", toUserEntity.getUserName());
				startActivity(UserActivity.class, userIntent);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;

		// editor voice image
		case R.id.chat_textditor_iv_audio:
			if (mLayoutPlus.isShown()) {
				hidePlusBar();
			}

			if (mInputView.isShown()) {
				mInputView.setVisibility(View.GONE);
			}

			hideKeyBoard();

			if (mLayoutSpeaker.isShown()) {
				mLayoutSpeaker.setVisibility(View.GONE);
			} else {
				mLayoutSpeaker.setVisibility(View.VISIBLE);
			}
			break;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (TextUtils.isEmpty(s)) {
			iv_audio.setVisibility(View.VISIBLE);
			iv_send.setVisibility(View.GONE);
		} else {
			iv_audio.setVisibility(View.GONE);
			iv_send.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onBackPressed() {

		AudioPlayerSingleton.getInstance().stopPlayer();

		if (mLayoutPlus.isShown()) {
			hidePlusBar();

		} else if (mLayoutSpeaker.isShown()) {
			mLayoutSpeaker.setVisibility(View.GONE);

		} else if (mInputView.isShown()) {
			iv_KeyBoard.setVisibility(View.GONE);
			iv_Emotion.setVisibility(View.VISIBLE);
			mInputView.setVisibility(View.GONE);

		} else if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
			iv_KeyBoard.setVisibility(View.VISIBLE);
			iv_Emotion.setVisibility(View.GONE);
			hideKeyBoard();

		} else {
			super.onBackPressed();
		}
		return;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {

			switch (requestCode) {
			case PhotoUtils.INTENT_REQUEST_CODE_ALBUM:

				Uri uri = data.getData();
				ContentResolver cr = this.getContentResolver();

				Bitmap bitmap = PhotoUtils.getBitmapFromUri(cr, uri);
				if (bitmap != null) {
					String path = PhotoUtils.savePhotoToSDCard(bitmap);
					PhotoUtils.fliterPhoto(context, ChatActivity.this, path);
					// PhotoUtils.cropPhoto(context, instance, path);
				}
				break;
			case PhotoUtils.INTENT_REQUEST_CODE_CAMERA:
				if (mCameraImagePath != null) {
					mCameraImagePath = PhotoUtils
							.savePhotoToSDCard(PhotoUtils.CompressionPhoto(
									mScreenWidth, mCameraImagePath, 2));
					PhotoUtils.fliterPhoto(context, ChatActivity.this,
							mCameraImagePath);
				}
				mCameraImagePath = null;
				break;

			case PhotoUtils.INTENT_REQUEST_CODE_FLITER:
				String path = data.getStringExtra("path");
				if (path != null) {
//					fetchRequest("IMAGE", path);
				}
				break;

			case INTENT_REQUEST_GET_PLAN: // get plan to send

				PlanEntity planEntity = (PlanEntity) data.getExtras()
						.getSerializable("PlanEntity");
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject jsonContent = new JSONObject(map);
//				fetchRequest("PLAN", jsonContent.toString());

				break;
			}
		}
	}

	public void refreshAdapter() {
		mAdapter.notifyDataSetChanged();
		mListView.setSelection(mMessages.size());
	}

	private MessageReceiver mMessageReceiver;

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		// filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.setPriority(990);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public static final String MESSAGE_RECEIVED_ACTION = "CHAT_MESSAGE_RECEIVED_ACTION";

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				android.os.Message msg = new android.os.Message();
				msg.what = 3;
				getGreenData();
			}
		}
	}

	@Override
	protected void onResume() {

		JPushUtils.isChatForeground = true;
		registerMessageReceiver();
		getGreenData();
		refreshAdapter();
		super.onResume();
//		if (singleChat) {
//		      MsgReceiver.registerMsgListener(this);
//		    } else {
//		      GroupMsgReceiver.registerMsgListener(this);
//		    }
	};

	@Override
	protected void onPause() {
		
//		if (singleChat) {
//		      MsgReceiver.unregisterMsgListener();
//		    } else {
//		      GroupMsgReceiver.unregisterMsgListener();
//		    }

		JPushUtils.isChatForeground = false;
		JPushUtils.unreadNum = 0;

		AudioPlayerSingleton.getInstance().stopPlayer();

		// 去掉unreadNum
		de.greenrobot.daoexample.Chat chat = GreenChat.getInstance(context)
				.getChatBytoUserId(Long.valueOf(toUserId));
		if (chat != null) {
			chat.setUnreadNum(0);
			GreenChat.getInstance(context).saveChat(chat);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	public void sendVoice(String path) {
		fetchRequest(2, path);
	}

	/**
	 * upload and save data
	 * 
	 * @return void
	 */
	private void fetchRequest(int msgType, String content) {

		ChatMessage message = new ChatMessage();
		message.setFromPeerId(selfId);
		message.setMsgContent(content);
		message.setFromUserId(toUserId);
		message.setToPeerIds(Arrays.asList(targetPeerId));

		// Status(-1), Text(0), Image(1), Audio(2), Map(3), Plan(4);
		if (msgType == 0) { // text
			message.setMessageType(MessageType.Text);
		} else if (msgType == 1) { // image
			message.setMessageType(MessageType.Text);
		} else if (msgType == 2) { // audio
			message.setMessageType(MessageType.Text);
		} else if (msgType == 3) { // map
			message.setMessageType(MessageType.Text);
		} else if (msgType == 4) { // plan
			message.setMessageType(MessageType.Text);
		}
		session.sendMessage(message.makeAVMessage());

		mMessages.add(message);
		mAdapter.notifyDataSetChanged();
		mListView.setSelection(mMessages.size());

		/*
		 * ChatController.saveSendGreenMessage(contentType, context,
		 * Long.valueOf(toUserId), content,
		 * UserController.getAvatarDiff(toUserEntity.getAvatar0()),
		 * toUserEntity.getUserName(), length);
		 */
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		super.onTouch(v, event);
		return false;
	}

}