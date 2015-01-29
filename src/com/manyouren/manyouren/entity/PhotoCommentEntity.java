/**
 * @Package com.manyouren.android.entity    
 * @Title: PhotoCommentEntity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-9-4 下午3:26:29 
 * @version V1.0   
 */
package com.manyouren.manyouren.entity;

import java.io.Serializable;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-9-4 下午3:26:29
 * 
 */
public class PhotoCommentEntity implements Serializable {

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 6520099822720569945L;

	private long commentId;

	private long photoId;

	private long createTime;

	private long userId;

	private String content;

	private float score;

	private long replyId;

	private String avatar0;

	private String username;

	public PhotoCommentEntity() {

	}

	public PhotoCommentEntity(long commentId, long photoId, long createTime,
			long userId, String content, float score, long replyId,
			String avatar0, String username) {
		super();
		this.commentId = commentId;
		this.photoId = photoId;
		this.createTime = createTime;
		this.userId = userId;
		this.content = content;
		this.score = score;
		this.replyId = replyId;
		this.avatar0 = avatar0;
		this.username = username;
	}

	/**
	 * @return the avatar0
	 */
	public String getAvatar0() {
		return avatar0;
	}

	/**
	 * @param avatar0 the avatar0 to set
	 */
	public void setAvatar0(String avatar0) {
		this.avatar0 = avatar0;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the photoId
	 */
	public long getPhotoId() {
		return photoId;
	}

	/**
	 * @param photoId
	 *            the photoId to set
	 */
	public void setPhotoId(long photoId) {
		this.photoId = photoId;
	}

	/**
	 * @return the score
	 */
	public float getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(float score) {
		this.score = score;
	}

	/**
	 * @return the commentId
	 */
	public long getCommentId() {
		return commentId;
	}

	/**
	 * @param commentId
	 *            the commentId to set
	 */
	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	/**
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
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
	 * @return the replyId
	 */
	public long getReplyId() {
		return replyId;
	}

	/**
	 * @param replyId
	 *            the replyId to set
	 */
	public void setReplyId(long replyId) {
		this.replyId = replyId;
	}

}
