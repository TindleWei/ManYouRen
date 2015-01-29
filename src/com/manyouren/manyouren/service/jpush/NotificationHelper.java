package com.manyouren.manyouren.service.jpush;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.HomeActivity;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.ui.chat.ChatActivity;
import com.manyouren.manyouren.util.PreferenceUtils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class NotificationHelper {

	/**
	 * 调用系统的Notification
	 */
	public static void showMessageNotification(Context context, String title,
			String content) {
		
		boolean isPushTip = PreferenceUtils.getBoolean(context,"push_notify",true);
		if(isPushTip==false)
			return;

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.ic_actionbar_logo)
				.setContentTitle(title)
				.setContentText(content)
				.setLargeIcon(
						BitmapFactory.decodeResource(context.getResources(),
								R.drawable.notify_icon));

		Intent intent = new Intent(context, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("show_page", "1");
		// intent.putExtra(Constants.Extra.CHAT_TOID, JPushUtils.toUserId);
		// intent.putExtra(Constants.Extra.NOTIFY_TO_CHAT,
		// JPushUtils.userEntity);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(ChatActivity.class);
		stackBuilder.addNextIntent(intent);

		PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntent);
		builder.setTicker("新消息来了");
		builder.setNumber(JPushUtils.unreadNum);
		builder.setAutoCancel(true);
		builder.setDefaults(Notification.DEFAULT_ALL);

		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		mNotificationManager.notify(329, builder.build());

	}

	/**
	 * 调用自定义Notification
	 * 
	 */
	public static void showMyNotification(Context context, String title,
			String content) {

	}

}
