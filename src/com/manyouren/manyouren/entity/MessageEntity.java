/**
 * @Package com.manyouren.android.entity    
 * @Title: MessageEntity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-7 上午12:46:14 
 * @version V1.0   
 */
package com.manyouren.manyouren.entity;

import java.util.Date;

/**
 * 
 * @author firefist_wei
 * @date 2014-7-7 上午12:46:14
 * 
 */
public class MessageEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = 4093542049129654659L;

	private long msgId;

	private long toUserId;

	private String avatar;

	private String userName;

	private long time;

	private String content;

	private String contentType; // TEXT IMAGE VOICE PLAN MAP

	private String messageType; // RECEIVER SEND

	private String length;

	public MessageEntity() {
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	/**
	 * @return the toUserId
	 */
	public long getToUserId() {
		return toUserId;
	}

	/**
	 * @param toUserId
	 *            the toUserId to set
	 */
	public void setToUserId(long toUserId) {
		this.toUserId = toUserId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the msgId
	 */
	public long getMsgId() {
		return msgId;
	}

	/**
	 * @param msgId
	 *            the msgId to set
	 */
	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	/**
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar
	 *            the avatar to set
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType
	 *            the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the messageType
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * @param messageType
	 *            the messageType to set
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

}
