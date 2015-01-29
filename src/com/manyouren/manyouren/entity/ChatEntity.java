/**
* @Package com.manyouren.android.entity    
* @Title: ChatEntity.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-6-17 上午10:46:47 
* @version V1.0   
*/
package com.manyouren.manyouren.entity;

import java.io.Serializable;
import java.util.Date;
/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-6-17 上午10:46:47 
 *  
 */
public class ChatEntity implements Serializable{
	
	/** serialVersionUID */
	private static final long serialVersionUID = -1211802458009529774L;
	
	private long chatId;
	
	private long toUserId;
	
	private long time;
	
	private String content;

	private String userName;
	
	private String avatar;
	
	private int unreadNum;

	public ChatEntity(){
		
	}
	
	/**
	 * @return the chatid
	 */
	public long getChatId() {
		return chatId;
	}

	/**
	 * @param chatid the chatid to set
	 */
	public void setChatid(long chatId) {
		this.chatId = chatId;
	}

	/**
	 * @return the toUserId
	 */
	public long getToUserId() {
		return toUserId;
	}

	/**
	 * @param toUserId the toUserId to set
	 */
	public void setToUserId(long toUserId) {
		this.toUserId = toUserId;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time the time to set
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
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar the avatar to set
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return the unreadNum
	 */
	public int getUnreadNum() {
		return unreadNum;
	}

	/**
	 * @param unreadNum the unreadNum to set
	 */
	public void setUnreadNum(int unreadNum) {
		this.unreadNum = unreadNum;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
