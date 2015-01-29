/**
* @Package com.manyouren.android.service.jpush    
* @Title: BreathLightService.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-9-14 下午3:02:25 
* @version V1.0   
*/
package com.manyouren.manyouren.service.jpush;


import com.manyouren.manyouren.util.Logot;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class BreathLightService extends Service {
	
	private static final int NOTIFICATION_ID = 330;
	private static final String INTENT_ACTION_SCHEDULE = "I_AM_BREATH_LIGHT";
	private static final String INTENT_ACTION_CANCEL = "I_AM_BREATH_CANCEL";
	private static final int NOTIFICATION_REQUEST_CODE = 1000;
	private static final int NOTIFICATION_ALARM_LENGTH = 10 * 1000;
	private static final int NOTIFICATION_LED_ONMS = 5 * 1000;
	private static final int NOTIFICATION_LED_OFFMS = 8 * 1000;
	private AlarmManager mAlarmManager;
	private NotificationManager mNotificationManager;
	private BroadcastReceiver mReceiver;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		scheduleAlarm();
		startReceiver();
		Logot.outInfo("BUG", "呼吸灯执行了！");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		destroy();
	}

	private void scheduleAlarm() {
		if (mAlarmManager == null) {
			mAlarmManager = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
		}
		// schedule notification
		Intent intent = new Intent(INTENT_ACTION_SCHEDULE);
		PendingIntent clock = PendingIntent.getBroadcast(this,
				NOTIFICATION_REQUEST_CODE, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		mAlarmManager
				.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
						SystemClock.elapsedRealtime(),
						NOTIFICATION_ALARM_LENGTH, clock);
		// schedule cancel notification
		Intent cancelIntent = new Intent(INTENT_ACTION_CANCEL);
		PendingIntent cancel = PendingIntent.getBroadcast(this,
				NOTIFICATION_REQUEST_CODE, cancelIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime() + NOTIFICATION_LED_ONMS,
				NOTIFICATION_ALARM_LENGTH, cancel);
	}

	private void startReceiver() {
		if (mReceiver == null) {
			mReceiver = new BroadcastReceiver() {

				@Override
				public void onReceive(Context context, Intent intent) {
					if (intent.getAction().equals(INTENT_ACTION_SCHEDULE)) {
						showNotification();
					} else if (intent.getAction().equals(INTENT_ACTION_CANCEL)) {
						if (mNotificationManager != null) {
							mNotificationManager.cancelAll();
						}
					}
				}
			};
			IntentFilter filter = new IntentFilter();
			filter.addAction(INTENT_ACTION_CANCEL);
			filter.addAction(INTENT_ACTION_SCHEDULE);
			registerReceiver(mReceiver, filter);
		}
	}

	private void destroy() {
		if (mAlarmManager != null) {
			Intent intent = new Intent(INTENT_ACTION_SCHEDULE);
			PendingIntent clock = PendingIntent.getBroadcast(this,
					NOTIFICATION_REQUEST_CODE, intent,
					PendingIntent.FLAG_CANCEL_CURRENT);
			mAlarmManager.cancel(clock);
			mAlarmManager = null;
		}
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}

	private void showNotification() {
		if (mNotificationManager == null) {
			mNotificationManager = (NotificationManager) getSystemService(Activity.NOTIFICATION_SERVICE);
		}
		mNotificationManager.cancel(NOTIFICATION_ID);
		Notification notice = new Notification();
//		notice.defaults |= Notification.DEFAULT_LIGHTS;
		notice.ledARGB= 0xff00ff00;
		notice.ledOnMS = 1;
		notice.ledOffMS = 0;
		notice.flags = Notification.FLAG_SHOW_LIGHTS;
		Intent intent = new Intent();
		PendingIntent des = PendingIntent.getActivity(this,
				NOTIFICATION_REQUEST_CODE, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		notice.setLatestEventInfo(this, null, null, des);
		mNotificationManager.notify(NOTIFICATION_ID, notice);
	}
}

