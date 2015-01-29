package com.manyouren.manyouren.service.avos;

import java.util.HashMap;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVMessage;
import com.avos.avoscloud.AVMessageReceiver;
import com.avos.avoscloud.LogUtil;
import com.avos.avoscloud.Session;
import com.avos.avospush.notification.NotificationCompat;
import com.manyouren.manyouren.HomeActivity;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.core.chat.ChatMessage;
import com.manyouren.manyouren.util.Logot;

public class ChatMessageReceiver extends AVMessageReceiver {

	@Override
	public void onSessionOpen(Context context, Session session) {
	}

	@Override
	public void onSessionPaused(Context context, Session session) {
		LogUtil.avlog.d("这里掉线了");
	}

	@Override
	public void onSessionResumed(Context context, Session session) {
		LogUtil.avlog.d("重新连接上了");
	}

	@Override
	public void onMessage(Context context, Session session, AVMessage msg) {
		LogUtil.avlog.d("onMessageReceived " + msg.getMessage());
		JSONObject j = JSONObject.parseObject(msg.getMessage());
		ChatMessage message = new ChatMessage();
		MessageListener listener = sessionMessageDispatchers.get(msg
				.getFromPeerId());

		if (j.containsKey("content")) {

			message.fromAVMessage(msg);
			// 如果Activity在屏幕上不是active的时候就选择发送 通知

			if (listener == null) {
				LogUtil.avlog
						.d("Activity inactive, about to send notification.");
				NotificationManager nm = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);

				/*String ctnt = message.getMessageFrom() + "："
						+ message.getMessageContent();*/
				Intent resultIntent = new Intent(context,
						HomeActivity.class);
				resultIntent
						.putExtra(
								"show_page",
								"1");
				
				resultIntent.putExtra(Session.AV_SESSION_INTENT_DATA_KEY,
						JSON.toJSONString(message));
				resultIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

				PendingIntent pi = PendingIntent.getActivity(context, -1,
						resultIntent, PendingIntent.FLAG_ONE_SHOT);

				Notification notification = new NotificationCompat.Builder(
						context)
						.setContentTitle(
								context.getString(R.string.notif_title))
//						.setContentText(ctnt)
						.setContentIntent(pi)
						.setSmallIcon(R.drawable.ic_launcher)
						.setLargeIcon(
								BitmapFactory.decodeResource(
										context.getResources(),
										R.drawable.ic_launcher))
						.setAutoCancel(true).build();
				nm.notify(233, notification);
				LogUtil.avlog.d("notification sent");
			} else {
				listener.onMessage(JSON.toJSONString(message));
			}
		}
	}

	@Override
	public void onMessageSent(Context context, Session session, AVMessage msg) {
		LogUtil.avlog.d("message fromPeerId=" + msg.getFromPeerId());
		LogUtil.avlog.d("message to peerIds=" + msg.getToPeerIds());
	}

	@Override
	public void onMessageFailure(Context context, Session session, AVMessage msg) {
		LogUtil.avlog.d("message failed :" + msg.getMessage());
	}

	@Override
	public void onStatusOnline(Context context, Session session,
			List<String> peerIds) {
		LogUtil.avlog.d("status online :" + peerIds.toString());
	}

	@Override
	public void onStatusOffline(Context context, Session session,
			List<String> peerIds) {
		LogUtil.avlog.d("status offline :" + peerIds.toString());
	}

	@Override
	public void onError(Context context, Session session, Throwable e) {
		//Logot.outError("session error", (Exception) e);
	}

	public static void registerSessionListener(String peerId,
			MessageListener listener) {
		sessionMessageDispatchers.put(peerId, listener);
	}

	public static void unregisterSessionListener(String peerId) {
		sessionMessageDispatchers.remove(peerId);
	}

	static HashMap<String, MessageListener> sessionMessageDispatchers = new HashMap<String, MessageListener>();
}
