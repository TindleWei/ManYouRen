package com.manyouren.manyouren.core.chat;

import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVMessage;
import com.avos.avoscloud.AVUtils;

public class ChatMessage extends AVMessage{
	MessageType messageType;
	AVMessage avMessage;
	
	String fromUserId;
	String fromAvatar;
	String createdAt;
	String msgContent; //存文字，[图片]，[语音]的url，[地图],  [计划]的json
	String msgDetail; //信息, 可以为空，也可以为语音的length
	
	public enum MessageType {
		Status(-1), Text(0), Image(1), Audio(2), Map(3), Plan(4);

		private final int type;

		MessageType(int type) {
			this.type = type;
		}

		public int getType() {
			return this.type;
		}
	}

	public ChatMessage() {
		avMessage = new AVMessage();
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getFromAvatar() {
		return fromAvatar;
	}

	public void setFromAvatar(String fromAvatar) {
		this.fromAvatar = fromAvatar;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getMsgDetail() {
		return msgDetail;
	}

	public void setMsgDetail(String msgDetail) {
		this.msgDetail = msgDetail;
	}

	/**
	 *  AVMessage method
	 */
	public void setFromPeerId(String peerId) {
		this.avMessage.setFromPeerId(peerId);
	}

	public String getFromPeerId() {
		return avMessage.getFromPeerId();
	}

	public String getGroupId() {
		return this.avMessage.getGroupId();
	}

	public void setGroupId(String groupId) {
		this.avMessage.setGroupId(groupId);
	}
	
	public void setToPeerIds(List<String> peerIds) {
		this.avMessage.setToPeerIds(peerIds);
	}

	public List<String> getToPeerIds() {
		return this.avMessage.getToPeerIds();
	}

	/**
	 *  AVMessage  Assemble Method
	 */
	@SuppressWarnings("unchecked")
	public void fromAVMessage(AVMessage message) {
		this.avMessage = message;
		if (!AVUtils.isBlankString(avMessage.getMessage())) {
			HashMap<String, Object> params = JSON.parseObject(
					avMessage.getMessage(), HashMap.class);
			this.msgContent = (String) params.get("content");
			this.messageType = (MessageType
					.valueOf((String) params.get("type")));
		}
	}

	public AVMessage makeAVMessage() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("type", this.messageType);
		params.put("content", this.msgContent);
		avMessage.setMessage(JSON.toJSONString(params));
		return avMessage;
	}

}
