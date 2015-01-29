package com.manyouren.manyouren.ui.chat;

import java.util.List;

import com.manyouren.manyouren.core.chat.ImageMessageItem;
import com.manyouren.manyouren.core.chat.ChatMessage;
import com.manyouren.manyouren.core.chat.MessageItem;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
public class ChatAdapter extends BaseObjectListAdapter {

	private long lastTimeSaved = 0;
	
	public ChatAdapter(Context context,
			List<ChatMessage> mMessages) {
		super(context, mMessages);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChatMessage msg = (ChatMessage) getItem(position);
		MessageItem messageItem = MessageItem.getInstance(msg, mContext);
		messageItem.fillContent();
		
//		if(position>0){
//			// msg.getTime() 是毫秒值
//			if((msg.getTime()-lastTimeSaved)/1000/60 > 5){
//				messageItem.showTimeStamp();
//			}
//		}else{
//			messageItem.showTimeStamp();
//		}
//		lastTimeSaved = msg.getTime();
		
		View view = messageItem.getRootView();
		return view;
	}
}
