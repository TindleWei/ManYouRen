/**
 * @Package com.manyouren.android.service.greendao    
 * @Title: GreenMessage.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-7 上午2:53:38 
 * @version V1.0   
 */
package com.manyouren.manyouren.service.greendao;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-7-7 上午2:53:38 
 *  
 */
import java.util.List;

import com.manyouren.manyouren.RootApplication;

import android.content.Context;
import android.util.Log;
import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.daoexample.DaoSession;
import de.greenrobot.daoexample.MessageDao.Properties;
import de.greenrobot.daoexample.Chat;
import de.greenrobot.daoexample.Message;
import de.greenrobot.daoexample.MessageDao;
import de.greenrobot.daoexample.User;
import de.greenrobot.daoexample.UserDao;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-7-7 上午2:53:05
 * 
 */
public class GreenMessage {

	private static GreenMessage instance;

	private MessageDao messageDao;

	private GreenMessage() {

	}

	public static GreenMessage getInstance(Context context) {
		if (instance == null) {
			instance = new GreenMessage();
			DaoSession daoSession = RootApplication.getDaoSession(context);
			instance.messageDao = daoSession.getMessageDao();
		}
		return instance;
	}

	public void insertMessage(Message entity) {
		messageDao.insert(entity);
	}

	/**
	 * insert or update
	 * 
	 * @param entity
	 * @return long
	 */
	public long saveMessage(Message entity) {
		return messageDao.insertOrReplace(entity);
	}

	public List<Message> getAllMessage() {
		return messageDao.loadAll();
	}

	public void deleteMsgById(long id) {
		messageDao.deleteByKey(id);
	}

	public void deleteMessage(Message msg) {
		messageDao.delete(msg);
	}

	public void deleteAll() {
		messageDao.deleteAll();
	}

	/**
	 * @Description: TODO
	 * 
	 * @param toUserId
	 * @return void
	 */
	public void deleteSomeoneMessage(long toUserId) {

		QueryBuilder<Message> qb = messageDao.queryBuilder();
		DeleteQuery<Message> bd = qb.where(
				MessageDao.Properties.ToUserId.eq(toUserId)).buildDelete();
		bd.executeDeleteWithoutDetachingEntities();
	}

	public List<Message> getSomeonMessage(long toUserId) {
		QueryBuilder<Message> qb = messageDao.queryBuilder();
		qb.where(MessageDao.Properties.ToUserId.eq(toUserId));
		qb.orderAsc(Properties.Time);
		return qb.list();
	}
	
}
