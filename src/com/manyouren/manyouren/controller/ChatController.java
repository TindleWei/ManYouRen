/**
 * @Package com.manyouren.android.controller    
 * @Title: ChatController.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-20 下午4:27:29 
 * @version V1.0   
 */
package com.manyouren.manyouren.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.HttpConfig;
import com.manyouren.manyouren.entity.ChatEntity;
import com.manyouren.manyouren.entity.MessageEntity;
import com.manyouren.manyouren.entity.PlanEntity;
import com.manyouren.manyouren.service.AsyncHttpLoader;
import com.manyouren.manyouren.service.greendao.GreenChat;
import com.manyouren.manyouren.service.greendao.GreenMessage;
import com.manyouren.manyouren.service.greendao.GreenUser;
import com.manyouren.manyouren.util.Logot;

import de.greenrobot.daoexample.Chat;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-20 下午4:27:29
 * 
 */
public class ChatController {

	public static List<ChatEntity> getChats() {

		List<ChatEntity> list = new ArrayList<ChatEntity>();
		ChatEntity chat = new ChatEntity();
		chat.setUserName("官方消息");
		chat.setChatid(1);
		chat.setToUserId(10000);
		chat.setContent("欢迎加入漫游人！");
		chat.setTime(new Date().getTime());
		chat.setUnreadNum(0);
		chat.setAvatar("http://hb.my399.com/res/1/20130313/86071363188922398.jpg");

		list.add(chat);
		return list;
	}

	public static void fetchSendMsg(Context context,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, HttpConfig.CHAT_SEND_URL,
				hashMap, handler, BaseController.CONTROLLER_CHAT_SEND);
	}

	/**
	 * @Description: TODO
	 * 
	 * @param context
	 * @return
	 * @return List<ChatEntity>
	 */
	public static List<ChatEntity> getLocalChats(Context context, int page) {

		// List<Chat> chatList = GreenChat.getInstance(context).getRecentChat();
		List<Chat> chatList = GreenChat.getChatAgain(context, page);

		List<ChatEntity> list = new ArrayList<ChatEntity>();

		for (int i = 0; i < chatList.size(); i++) {
			Chat chat = chatList.get(i);
			ChatEntity entity = new ChatEntity();

			entity.setAvatar(chat.getAvatar());
			entity.setChatid(chat.getId());
			entity.setContent(chat.getContent());
			entity.setTime(chat.getTime());
			entity.setToUserId(chat.getToUserId());
			entity.setUnreadNum(chat.getUnreadNum());
			entity.setUserName(chat.getUserName());

			list.add(entity);
		}
		return list;
	}

	public static boolean deleteChat(Context context, ChatEntity chatEntity) {
		long chatId = chatEntity.getChatId();
		GreenChat.getInstance(context).deleteChat(chatId);

		long toUserId = chatEntity.getToUserId();
		GreenMessage.getInstance(context).deleteSomeoneMessage(toUserId);

		return true;
	}
	
	/** 
	* 从聊天界面删除message
	*/
	public static boolean deleteMessage(Context context, MessageEntity msgEntity) {
		long Id = msgEntity.getMsgId();
		GreenMessage.getInstance(context).deleteMsgById(Id);

		return true;
	}

	public static boolean topChat(Context context, ChatEntity chatEntity) {
		long chatId = chatEntity.getChatId();
		Chat chat = GreenChat.getInstance(context).getChatById(chatId);
		chat.setTime(new Date().getTime());
		GreenChat.getInstance(context).saveChat(chat);

		return true;
	}

	public static List<MessageEntity> getLocalMsgs(Context context,
			long toUserId) {

		List<de.greenrobot.daoexample.Message> msgList = GreenMessage
				.getInstance(context).getSomeonMessage(toUserId);
		List<MessageEntity> list = new ArrayList<MessageEntity>();

		for (int i = 0; i < msgList.size(); i++) {
			de.greenrobot.daoexample.Message msg = msgList.get(i);
			MessageEntity entity = new MessageEntity();

			entity.setAvatar(msg.getAvatar());
			entity.setContent(msg.getContent());
			entity.setContentType(msg.getContentType());
			entity.setMessageType(msg.getMessageType());
			entity.setMsgId(msg.getId());
			entity.setTime(msg.getTime());
			entity.setToUserId(msg.getToUserId());
			entity.setUserName(msg.getUserName());
			entity.setLength(msg.getLength());

			list.add(entity);
		}
		return list;
	}

	/**
	 * 没错，这个就是丧心病况的清楚聊天记录
	 * 
	 * @param context
	 * @return void
	 */
	public static void deleteAllChatAndMessage(Context context) {
		GreenChat.getInstance(context).deleteAll();
		GreenMessage.getInstance(context).deleteAll();
	}

	/**
	 * @Description: TODO
	 * 
	 * @return void
	 */
	public static void saveSendGreenMessage(String contentType,
			Context context, long ToUserId, String text, String ToUserAvatar, String ToUserName, String length) {

		long toUserId = Long.valueOf(ToUserId);
		long time = new Date().getTime();

		GreenChat greenChat = GreenChat.getInstance(context);

		if (greenChat.isSaved(toUserId)) {
			Chat chat = greenChat.getChatBytoUserId(toUserId);
			chat.setUnreadNum(0);
			chat.setUserName(ToUserName);
			//chat.setAvatar(ToUserAvatar);
			chat.setTime(time);

			if (contentType.equals("TEXT")) {
				chat.setContent(text);
			} else if (contentType.equals("IMAGE")) {
				chat.setContent("[图片]");
			} else if (contentType.equals("MAP")) {
				chat.setContent("[地图]");
			} else if (contentType.equals("PLAN")) {
				chat.setContent("[计划]");
			} else if (contentType.equals("VOICE")) {
				chat.setContent("[语音]");
			}
			greenChat.saveChat(chat);

		} else {
			Chat chat = new Chat();

			chat.setAvatar(ToUserAvatar);
			chat.setTime(time);
			chat.setToUserId(toUserId);
			chat.setUserName(ToUserName);
			chat.setUnreadNum(0);

			if (contentType.equals("TEXT")) {
				chat.setContent(text);
			} else if (contentType.equals("IMAGE")) {
				chat.setContent("[图片]");
			} else if (contentType.equals("MAP")) {
				chat.setContent("[地图]");
			} else if (contentType.equals("PLAN")) {
				chat.setContent("[计划]");
			} else if (contentType.equals("VOICE")) {
				chat.setContent("[语音]");
			}
			greenChat.insertChat(chat);
		}

		de.greenrobot.daoexample.Message message = new de.greenrobot.daoexample.Message();

		//因为我是发送者，所以Message Item储存我的头像
		message.setAvatar(UserController.getAvatarDiff(GreenUser.getInstance(context)
				.getUserAvatar(Long.valueOf(Constants.USER_ID))));
		message.setToUserId(toUserId);
		message.setTime(time);
		message.setUserName(ToUserName);
		message.setMessageType("SEND");
		message.setContent(text);
		
		if (contentType.equals("TEXT")) {
			message.setContentType("TEXT");

		} else if (contentType.equals("IMAGE")) {
			message.setContentType("IMAGE");

		} else if (contentType.equals("MAP")) {
			message.setContentType("MAP");

		} else if (contentType.equals("PLAN")) {
			message.setContentType("PLAN");

		} else if (contentType.equals("VOICE")) {
			message.setLength(length);
			message.setContentType("VOICE");
		}
		GreenMessage.getInstance(context).insertMessage(message);

	}
}
