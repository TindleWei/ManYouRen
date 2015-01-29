package com.manyouren.manyouren.service.jpush;

import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.HttpConfig;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.service.greendao.GreenChat;
import com.manyouren.manyouren.service.greendao.GreenMessage;
import com.manyouren.manyouren.ui.chat.BaseChatActivity;
import com.manyouren.manyouren.ui.chat.ChatActivity;
import com.manyouren.manyouren.ui.chat.ChatFragmentOld;
import com.manyouren.manyouren.util.DateUtils;
import com.manyouren.manyouren.util.Logot;

import de.greenrobot.daoexample.Chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "MyReceiver";

	private static String notify_name = "";
	private static String notify_content = "";

	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle bundle = intent.getExtras();
		Logot.outInfo("TAG", "接收到的数据: " + intent.getAction() + ", extras: "
				+ printBundle(bundle));

		/*if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			JPushInterface.reportNotificationOpened(context,
					bundle.getString(JPushInterface.EXTRA_MSG_ID));

			JPushUtils.unreadNum += 1;
			processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Logot.outInfo(TAG, "[MyReceiver] 接收到推送下来的通知");

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Logot.outInfo(TAG, "[MyReceiver] 用户点击打开了通知");
		}*/
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			/*if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}*/
		}
		return sb.toString();
	}

	public void processCustomMessage(Context context, Bundle bundle) {

		saveMessage(context, bundle);

		Logot.outError("TAG", "isMainForeground:" + JPushUtils.isMainForeground);
		Logot.outError("TAG", "isChatForeground:" + JPushUtils.isChatForeground);

		// Send message to UI (MainActivity) only when UI is up
		if (JPushUtils.isMainForeground == false
				&& JPushUtils.isChatForeground == false) {
			Logot.outError("TAG", "判断界面在哪  1");

			NotificationHelper.showMessageNotification(context, "新消息来了",
					"您收到了新消息");

		} else if (JPushUtils.isMainForeground == false
				&& JPushUtils.isChatForeground == true) {

			Logot.outError("TAG", "判断界面在哪  2");

			Intent messageIntent = new Intent(
					ChatActivity.MESSAGE_RECEIVED_ACTION);
			messageIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			/*
			 * messageIntent.putExtra(Constants.Extra.CHAT_TOID,
			 * JPushUtils.toUserId);
			 * messageIntent.putExtra(Constants.Extra.NOTIFY_TO_CHAT,
			 * JPushUtils.userEntity);
			 */

			context.sendBroadcast(messageIntent);

		} else if (JPushUtils.isMainForeground == true
				&& JPushUtils.isChatForeground == false) {
			Logot.outError("TAG", "判断界面在哪  3");

			Intent chatIntent = new Intent(ChatFragmentOld.CHAT_RECEIVED_ACTION);
			/*
			 * chatIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			 * chatIntent.putExtra(Constants.Extra.CHAT_TOID,
			 * JPushUtils.toUserId);
			 * chatIntent.putExtra(Constants.Extra.NOTIFY_TO_CHAT,
			 * JPushUtils.userEntity);
			 */

			context.sendBroadcast(chatIntent);
			// 打开呼吸灯
			// 出现bug 1.呼吸灯没有开启， 2.notification出现后就消逝
			// Intent breathItent = new Intent(context,
			// BreathLightService.class);
			// context.startService(breathItent);

			if (JPushUtils.isChatFragment == false) {
				NotificationHelper.showMessageNotification(context, "来自"
						+ notify_name + "的消息", notify_content);
			}

		}
	}

	public void saveMessage(Context context, Bundle bundle) {

		//Logot.outError("TAG", bundle.getString(JPushInterface.EXTRA_EXTRA));

		// saveMessageList(context,
		// bundle.getString(JPushInterface.EXTRA_EXTRA));

		//saveInGreenChat(context, bundle.getString(JPushInterface.EXTRA_EXTRA));
	}

	/**
	 * 
	 * @return void
	 */
	private void saveInGreenChat(Context context, String jsonString) {

		try {
			JSONObject jsonObject = new JSONObject(jsonString);

			long toUserId = Long.valueOf(jsonObject.getString("sender"));
			long time = new Date().getTime();
			int type = Integer.valueOf(jsonObject.getString("type"));

			GreenChat greenChat = GreenChat.getInstance(context);

			Chat chat;
			if (greenChat.isSaved(toUserId)) {
				chat = greenChat.getChatBytoUserId(toUserId);
				chat.setUnreadNum(chat.getUnreadNum() + 1);
			} else {
				chat = new Chat();
				chat.setToUserId(toUserId);
				chat.setUnreadNum(1);
			}
			
			chat.setAvatar(UserController.getAvatarDiff(jsonObject
					.getString("avatar")));
			chat.setUserName(jsonObject.getString("username"));
			chat.setTime(time);
			chat.setContent(jsonObject.getString("text"));

			// notify_content
			notify_content = jsonObject.getString("text");
			// notify_name
			notify_name = jsonObject.getString("username");

			
			if (type == 0) {
				// 文字
				notify_content = jsonObject.getString("text");

			} else if (type == 1) {
				// 图片
				notify_content = "[图片]";
				notify_content = "[图片]";

			} else if (type == 2) {
				// 语音
				notify_content = "[语音]";
				chat.setContent("[语音]");

			} else if (type == 3) {
				// 计划
				notify_content = "[计划]";
				notify_content = "[计划]";
			}
			greenChat.saveChat(chat);

			de.greenrobot.daoexample.Message message = new de.greenrobot.daoexample.Message();

			message.setAvatar(UserController.getAvatarDiff(jsonObject
					.getString("avatar")));
			message.setToUserId(toUserId);
			message.setTime(time);
			message.setUserName(jsonObject.getString("username"));
			message.setMessageType("RECEIVER");
			message.setContent(jsonObject.getString("text"));
			
			if (type == 0) {
				// 文字
				message.setContentType("TEXT");

			} else if (type == 1) {
				// 图片
				message.setContentType("IMAGE");
				message.setContent(HttpConfig.UPLOADS_PREFIX
						+ jsonObject.getJSONObject("file").getString("origin"));

			} else if (type == 2) {
				// 语音
				message.setContentType("VOICE");
				message.setContent(HttpConfig.UPLOADS_PREFIX
						+ jsonObject.getJSONObject("file").getString("origin"));
				message.setLength(jsonObject.getString("length"));

			} else if (type == 3) {
				// 计划
				message.setContentType("PLAN");
				message.setContent(jsonObject.getString("plan").toString());
			}
			
			GreenMessage.getInstance(context).insertMessage(message);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断jsonObject是否有该key
	 * 
	 */
	public boolean hasJsonCode(JSONObject jsonObject, String codeName) {

		boolean flag = false;
		try {
			flag = jsonObject.has(codeName)
					&& jsonObject.getString(codeName).toString() != null
					&& !jsonObject.getString(codeName).equals("null");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return flag;
	}
}