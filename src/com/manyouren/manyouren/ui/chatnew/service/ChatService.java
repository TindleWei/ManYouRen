package com.manyouren.manyouren.ui.chatnew.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVMessage;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.Group;
import com.avos.avoscloud.Session;
import com.avos.avoscloud.SessionManager;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.service.jpush.JPushUtils;
import com.manyouren.manyouren.service.jpush.NotificationHelper;
import com.manyouren.manyouren.ui.chat.ChatFragmentOld;
import com.manyouren.manyouren.ui.chat.chatnew.ChatFragment;
import com.manyouren.manyouren.ui.chatnew.avobject.ChatGroup;
import com.manyouren.manyouren.ui.chatnew.avobject.User;
import com.manyouren.manyouren.ui.chatnew.db.DBMsg;
import com.manyouren.manyouren.ui.chatnew.entity.Conversation;
import com.manyouren.manyouren.ui.chatnew.entity.Msg;
import com.manyouren.manyouren.ui.chatnew.ui.activity.ChatActivity;
import com.manyouren.manyouren.ui.chatnew.util.AVOSUtils;
import com.manyouren.manyouren.ui.chatnew.util.Logger;
import com.manyouren.manyouren.ui.chatnew.util.NetAsyncTask;
import com.manyouren.manyouren.ui.chatnew.util.Utils;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.PreferenceUtils;

/**
 * Created by lzw on 14-7-9.
 */
public class ChatService {
	private static final int REPLY_NOTIFY_ID = 1;

	public static <T extends AVUser> String getPeerId(T user) {
		return user.getObjectId();
	}

	public static String getSelfId() {
		return getPeerId(User.curUser());
	}

	public static <T extends AVUser> void withUsersToWatch(List<T> users,
			boolean watch) {
		List<String> peerIds = new ArrayList<String>();
		for (AVUser user : users) {
			peerIds.add(getPeerId(user));
		}
		String selfId = getPeerId(User.curUser());
		Session session = SessionManager.getInstance(selfId);
		if (watch) {
			session.watchPeers(peerIds);
		}
	}

	public static <T extends AVUser> void withUserToWatch(T user, boolean watch) {
		List<T> users = new ArrayList<T>();
		users.add(user);
		withUsersToWatch(users, watch);
	}

	public static Session getSession() {
		return SessionManager.getInstance(getPeerId(User.curUser()));
	}

	public static void sendResponseMessage(Msg msg) {
		Msg resMsg = new Msg();
		resMsg.setType(Msg.Type.Response);
		resMsg.setToPeerId(msg.getFromPeerId());
		resMsg.setFromPeerId(getSelfId());
		resMsg.setContent(msg.getTimestamp() + "");
		resMsg.setObjectId(msg.getObjectId());
		resMsg.setRoomType(Msg.RoomType.Single);
		resMsg.setStatus(Msg.Status.SendStart);
		resMsg.setConvid(AVOSUtils.convid(getSelfId(), msg.getFromPeerId()));
		// 自定义 4 属性
		resMsg.setFromAvatar(msg.getFromAvatar());
		resMsg.setFromName(msg.getFromName());
		resMsg.setToAvatar(msg.getToAvatar());
		resMsg.setToName(msg.getToName());

		Session session = getSession();
		AVMessage avMsg = resMsg.toAVMessage();
		session.sendMessage(avMsg);
	}

	public static Msg sendAudioMsg(String peerId, String path, String msgId,
			Group group, UserEntity toEntity, UserEntity fromEntity)
			throws IOException, AVException {
		return sendFileMsg(peerId, msgId, Msg.Type.Audio, path, group,
				toEntity, fromEntity);
	}

	public static Msg sendImageMsg(String peerId, String filePath,
			String msgId, Group group, UserEntity toEntity,
			UserEntity fromEntity) throws IOException, AVException {
		return sendFileMsg(peerId, msgId, Msg.Type.Image, filePath, group,
				toEntity, fromEntity);
	}

	public static Msg sendFileMsg(String peerId, String objectId,
			Msg.Type type, String filePath, Group group, UserEntity toEntity,
			UserEntity fromEntity) throws IOException, AVException {
		AVFile file = AVFile.withAbsoluteLocalPath(objectId, filePath);
		file.save();
		String url = file.getUrl();
		Msg msg = createAndSendMsg(peerId, type, url, objectId, group,
				toEntity, fromEntity);
		DBMsg.insertMsg(msg);
		return msg;
	}

	public static Msg sendTextMsg(String peerId, String content, Group group,
			UserEntity toEntity, UserEntity fromEntity) {
		Msg.Type type = Msg.Type.Text;
		Msg msg = sendMsgAndInsertDB(peerId, type, content, group, toEntity,
				fromEntity);
		return msg;
	}

	public static Msg createAndSendMsg(String peerId, Msg.Type type,
			String content, Group group, UserEntity toEntity,
			UserEntity fromEntity) {
		String objectId = Utils.uuid();
		return createAndSendMsg(peerId, type, content, objectId, group,
				toEntity, fromEntity);
	}

	public static Msg sendMsgAndInsertDB(String peerId, Msg.Type type,
			String content, Group group, UserEntity toEntity,
			UserEntity fromEntity) {
		Msg msg = createAndSendMsg(peerId, type, content, group, toEntity,
				fromEntity);
		DBMsg.insertMsg(msg);
		return msg;
	}

	public static Msg createAndSendMsg(String peerId, Msg.Type type,
			String content, String objectId, Group group,
			UserEntity toUserEntity, UserEntity fromUserEntity) {
		Msg msg;
		msg = new Msg();
		msg.setStatus(Msg.Status.SendStart);
		msg.setContent(content);
		msg.setTimestamp(System.currentTimeMillis());
		msg.setFromPeerId(getSelfId());
		String convid;
		if (group == null) {
			String toPeerId = peerId;
			msg.setToPeerId(toPeerId);
			msg.setRoomType(Msg.RoomType.Single);
			convid = AVOSUtils.convid(ChatService.getSelfId(), toPeerId);
		} else {
			msg.setRoomType(Msg.RoomType.Group);
			convid = group.getGroupId();
		}
		msg.setObjectId(objectId);
		msg.setConvid(convid);
		msg.setType(type);
		// 自定义的6属性
		msg.setFromUserId(String.valueOf(fromUserEntity.getUserId()));
		msg.setFromAvatar(fromUserEntity.getAvatar0());
		msg.setFromName(fromUserEntity.getUserName());
		msg.setToUserId(String.valueOf(toUserEntity.getUserId()));
		msg.setToAvatar(toUserEntity.getAvatar0());
		msg.setToName(toUserEntity.getUserName());

		return sendMessage(group, msg);
	}

	public static Msg sendMessage(Group group, Msg msg) {
		AVMessage avMsg = msg.toAVMessage();
		Session session = getSession();
		if (group == null) {
			session.sendMessage(avMsg);
		} else {
			group.sendMessage(avMsg);
		}
		return msg;
	}

	public static void openSession() {
		Session session = getSession();
		if (session.isOpen() == false) {
			session.open(new LinkedList<String>());
		}
	}

	public static Msg sendLocationMessage(String peerId, String address,
			double latitude, double longtitude, Group group,
			UserEntity toEntity, UserEntity fromEntity) {
		String content = address + "&" + latitude + "&" + longtitude;
		return sendMsgAndInsertDB(peerId, Msg.Type.Location, content, group,
				toEntity, fromEntity);
	}

	public static List<Conversation> getConversationsAndCache()
			throws AVException {
		Logot.outError("ChatFragment", "getConversation start");

		List<Msg> msgs = DBMsg.getRecentMsgs(User.curUserId());
		Logot.outError("ChatFragment", "getConversation msgs.size " + msgs.size());

		try {
			// 这里存在问题
			cacheUserOrChatGroup(msgs);
		} catch (Exception e) {

		}
		Logot.outError("ChatFragment", "getConversation next");
		ArrayList<Conversation> conversations = new ArrayList<Conversation>();
		for (Msg msg : msgs) {
			Conversation conversation = new Conversation();
			if (msg.getRoomType() == Msg.RoomType.Single) {
				String chatUserId = msg.getChatUserId();
				conversation.toUser = RootApplication.lookupUser(chatUserId);
			} else {
				conversation.chatGroup = RootApplication.lookupChatGroup(msg
						.getConvid());
			}
			conversation.msg = msg;
			conversations.add(conversation);
		}
		Logot.outError("ChatFragment", "getConversation return");
		return conversations;
	}

	public static void cacheUserOrChatGroup(List<Msg> msgs) throws AVException {
		Set<String> uncachedIds = new HashSet<String>();
		Set<String> uncachedChatGroupIds = new HashSet<String>();
		for (Msg msg : msgs) {
			if (msg.getRoomType() == Msg.RoomType.Single) {
				String chatUserId = msg.getChatUserId();
				uncachedIds.add(chatUserId);
				Logot.outError("ChatFragment", "uncachedIds");
				// if (RootApplication.lookupUser(chatUserId) == null) {
				// uncachedIds.add(chatUserId);
				// }
			} else {
				String groupId = msg.getConvid();
				if (RootApplication.lookupChatGroup(groupId) == null) {
					uncachedChatGroupIds.add(groupId);
				}
			}
		}
		UserService.cacheUser(new ArrayList<String>(uncachedIds));
		GroupService
				.cacheChatGroups(new ArrayList<String>(uncachedChatGroupIds));
		Logot.outError("ChatFragment", "cacheUserOrChatGroup return");
	}

	public static void closeSession() {
		Session session = ChatService.getSession();
		session.close();
	}

	public static Group getGroupById(String groupId) {
		return getSession().getGroup(groupId);
	}

	public static void notifyMsg(Context context, Msg msg, Group group)
			throws JSONException {

		DBMsg.insertMsg(msg);
		if (!PreferenceUtils.getBoolean(context, "push_notify", true)) {
			return;
		}
		Logot.outError("NOTIFY", "0");
		
		if (JPushUtils.isMainForeground == true
				&& JPushUtils.isChatForeground == false) {
			
			Logot.outError("NOTIFY", "2");
			// chatFragment界面
			Intent chatIntent = new Intent(ChatFragment.CHAT_RECEIVED_ACTION);

			context.sendBroadcast(chatIntent);
			
		} else if (JPushUtils.isMainForeground == false
				&& JPushUtils.isChatForeground == true) {
			Logot.outError("NOTIFY", "3");
			
			// chatActivity界面
			int icon = context.getApplicationInfo().icon;
			Intent intent;
			if (group == null) {

				UserEntity userEntity = new UserEntity();
				userEntity.setUserId(Long.valueOf(msg.getFromUserId()));
				userEntity.setAvatar0(msg.getFromAvatar());
				userEntity.setUserName(msg.getFromName());

				intent = new Intent(context, ChatActivity.class);
				intent.putExtra(ChatActivity.CHAT_USER_ID, msg.getFromPeerId());
				intent.putExtra("UserEntity", userEntity);

			} else {
				intent = ChatActivity.getGroupChatIntent(context,
						group.getGroupId());
			}
			return;

		}
		
		Logot.outError("NOTIFY", "1");

		int icon = context.getApplicationInfo().icon;
		Intent intent;
		if (group == null) {

			UserEntity userEntity = new UserEntity();
			userEntity.setUserId(Long.valueOf(msg.getFromUserId()));
			userEntity.setAvatar0(msg.getFromAvatar());
			userEntity.setUserName(msg.getFromName());

			intent = new Intent(context, ChatActivity.class);
			intent.putExtra(ChatActivity.CHAT_USER_ID, msg.getFromPeerId());
			intent.putExtra("UserEntity", userEntity);

		} else {
			intent = ChatActivity.getGroupChatIntent(context,
					group.getGroupId());
		}

		// why Random().nextInt()
		// http://stackoverflow.com/questions/13838313/android-onnewintent-always-receives-same-intent
		PendingIntent pend = PendingIntent.getActivity(context,
				new Random().nextInt(), intent, 0);
		Notification.Builder builder = new Notification.Builder(context);
		CharSequence notifyContent = msg.getNotifyContent();
		CharSequence username = msg.getFromName();
		builder.setContentIntent(pend).setSmallIcon(icon)
				.setWhen(System.currentTimeMillis())
				.setTicker(username + "\n" + notifyContent)
				.setContentTitle(username).setContentText(notifyContent)
				.setAutoCancel(true);
		NotificationManager man = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = builder.getNotification();
		PrefDao prefDao = PrefDao.getCurUserPrefDao(context);
		if (prefDao.isVoiceNotify()) {
			notification.defaults |= Notification.DEFAULT_SOUND;
		}
		if (prefDao.isVibrateNotify()) {
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		man.notify(REPLY_NOTIFY_ID, notification);	

	}

	public static void onMessage(Context context, AVMessage avMsg,
			MsgListener listener, Group group) {
		final Msg msg = Msg.fromAVMessage(avMsg);
		if (group == null) {
			String selfId = getSelfId();
			msg.setToPeerId(selfId);
		}
		if (msg.getType() != Msg.Type.Response) {
			responseAndReceiveMsg(context, msg, listener, group);
		} else {
			Logger.d("onResponseMessage " + msg.getContent());
			DBMsg.updateStatusAndTimestamp(msg);
			MsgListener _listener = filterMsgListener(listener, msg, group);
			if (_listener != null) {
				_listener.onMessage(msg);
			}
		}
	}

	public static void responseAndReceiveMsg(final Context context,
			final Msg msg, final MsgListener listener, final Group group) {
		if (group == null) {
			sendResponseMessage(msg);
		}
		new NetAsyncTask(context, false) {
			@Override
			protected void doInBack() throws Exception {
				if (msg.getType() == Msg.Type.Audio) {
					File file = new File(msg.getAudioPath());
					String url = msg.getContent();
					Utils.downloadFileIfNotExists(url, file);
				}
				if (group != null) {
					GroupService.cacheChatGroupIfNone(group.getGroupId());
				} else {
					String fromId = msg.getFromPeerId();
					UserService.cacheUserIfNone(fromId);
				}
			}

			@Override
			protected void onPost(Exception e) {
				if (e != null) {
					Utils.toast(context, "badNetwork");
				} else {
					DBMsg.insertMsg(msg);
					MsgListener _msgListener = filterMsgListener(listener, msg,
							group);
					if (_msgListener == null) {
						if (User.curUser() != null) {
							PrefDao prefDao = PrefDao
									.getCurUserPrefDao(context);
							if (prefDao.isNotifyWhenNews()) {
								notifyMsg(context, msg, group);
							}
						}
					} else {
						listener.onMessage(msg);
					}
				}
			}

		}.execute();
	}

	public static MsgListener filterMsgListener(MsgListener msgListener,
			Msg msg, Group group) {
		if (msgListener != null) {
			String listenerId = msgListener.getListenerId();
			if (group == null) {
				String chatUserId = msg.getChatUserId();
				if (chatUserId.equals(listenerId)) {
					return msgListener;
				}
			} else {
				if (group.getGroupId().equals(listenerId)) {
					return msgListener;
				}
			}
		}
		return null;
	}

	public static void onMessageSent(AVMessage avMsg, MsgListener listener,
			Group group) {
		Msg msg = Msg.fromAVMessage(avMsg);
		if (msg.getType() != Msg.Type.Response) {
			msg.setStatus(Msg.Status.SendSucceed);
			DBMsg.updateStatus(msg);
			msg.setFromPeerId(User.curUserId());
			MsgListener _listener = filterMsgListener(listener, msg, group);
			if (_listener != null) {
				_listener.onMessageSent(msg);
			}
		}
	}

	public static void updateStatusToFailed(AVMessage avMsg,
			MsgListener msgListener) {
		Msg msg = Msg.fromAVMessage(avMsg);
		if (msg.getType() != Msg.Type.Response) {
			msg.setStatus(Msg.Status.SendFailed);
			DBMsg.updateStatus(msg);
			if (msgListener != null) {
				msgListener.onMessageFailure(msg);
			}
		}
	}

	public static void onMessageError(Throwable throwable,
			MsgListener msgListener) {
		String errorMsg = throwable.getMessage();
		Logger.d("error " + errorMsg);
		if (errorMsg != null && errorMsg.startsWith("{")) {
			AVMessage avMsg = new AVMessage(errorMsg);
			updateStatusToFailed(avMsg, msgListener);
		}
	}

	public static List<User> findGroupMembers(ChatGroup chatGroup)
			throws AVException {
		List<String> members = chatGroup.getMembers();
		return UserService.findUsers(members);
	}

	public static void resendMsg(Msg msg) {
		Group group = null;
		if (msg.getRoomType() == Msg.RoomType.Single) {
			String groupId = msg.getConvid();
			Session session = ChatService.getSession();
			group = session.getGroup(groupId);
		}
		sendMessage(group, msg);
		msg.setStatus(Msg.Status.SendStart);
		DBMsg.updateStatus(msg);
	}

	public static void cancelNotification(Context ctx) {
		Utils.cancelNotification(ctx, REPLY_NOTIFY_ID);
	}
}
