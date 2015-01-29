/**
* @Package com.manyouren.android.service.greendao    
* @Title: GreenChat.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-7-7 上午2:53:19 
* @version V1.0   
*/
package com.manyouren.manyouren.service.greendao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.manyouren.manyouren.RootApplication;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.daoexample.Chat;
import de.greenrobot.daoexample.ChatDao;
import de.greenrobot.daoexample.ChatDao.Properties;
import de.greenrobot.daoexample.DaoSession;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-7-7 上午2:53:19 
 *  
 */
public class GreenChat {
	
	private static GreenChat instance;
	
	private ChatDao chatDao;
	
	private GreenChat(){
		
	}
	
	public static GreenChat getInstance(Context context){
		if(instance == null){
			instance = new GreenChat();
			DaoSession daoSession = RootApplication.getDaoSession(context);
			instance.chatDao = daoSession.getChatDao();
		}
		return instance;
	}
	
	/** 
	* @Description: TODO
	*
	* @param toUserId
	* @return boolean
	*/
	public boolean isSaved(long toUserId) {
		QueryBuilder<Chat> qb = chatDao.queryBuilder();
		qb.where(ChatDao.Properties.ToUserId.eq(toUserId));
		return qb.list().size() > 0 ? true : false;
	}
	
	/** 
	* @Description: TODO
	*
	* @param Id
	* @return Chat
	*/
	public Chat getChatById(long Id){
		QueryBuilder<Chat> qb = chatDao.queryBuilder();
        qb.where(ChatDao.Properties.Id.eq(Id));
        return qb.unique();
	}
	
	/** 
	* @Description: TODO
	*
	* @param toUserId
	* @return Chat
	*/
	public Chat getChatBytoUserId(long toUserId)
    {
        QueryBuilder<Chat> qb = chatDao.queryBuilder();
        qb.where(ChatDao.Properties.ToUserId.eq(toUserId));
        return qb.unique();
    }
	
	/** 
	* @Description: TODO
	*
	* @param toUserId
	* @return int
	*/
	public int getUnreadNum(long toUserId){
		QueryBuilder<Chat> qb = chatDao.queryBuilder();
		qb.where(ChatDao.Properties.ToUserId.eq(toUserId));
		if (qb.list().size() > 0) {
			return qb.list().get(0).getUnreadNum();
		} else {
			return 0;
		}
	}
	
	/** 
	* @Description: TODO
	*
	* @param entity
	* @return void
	*/
	public void insertChat(Chat entity){
		chatDao.insert(entity);
	}
	
	/** 
	* insert or update
	*
	* @param entity
	* @return long
	*/
	public long saveChat(Chat entity){
		return chatDao.insertOrReplace(entity);
	}
	
	/** 
	* @Description: TODO
	*
	* @return List<Chat>
	*/
	public List<Chat> getAllChat(){
		return chatDao.loadAll();
	}
	
	/** 
	* @Description: TODO
	*
	* @return List<Chat>
	*/
	public List<Chat> getRecentChat(){
		QueryBuilder<Chat> qb = chatDao.queryBuilder();
		qb.where(Properties.Time.isNotNull());
		qb.orderDesc(Properties.Time);
		qb.limit(40);
		return qb.list();
	}
	
	public static List<Chat> getChatAgain(Context context, int page){
		DaoSession daoSession = RootApplication.getDaoSession(context);
		ChatDao chatDao = daoSession.getChatDao();
		
		QueryBuilder<Chat> qb = chatDao.queryBuilder();
		qb.where(Properties.Time.isNotNull());
		qb.orderDesc(Properties.Time);
		
		if((int)qb.count() >= page * 20){
			return qb.list().subList((page-1)*20, page*20);
		}else{
			return qb.list().subList((page-1)*20, (int)qb.count());
		}
	}
	
	/** 
	* @Description: TODO
	*
	* @param id
	* @return void
	*/
	public void deleteChat(long id){
		chatDao.deleteByKey(id);
	}
	
	/** 
	* @Description: TODO
	*
	* @return void
	*/
	public void deleteAll(){
		chatDao.deleteAll();
	}

}
