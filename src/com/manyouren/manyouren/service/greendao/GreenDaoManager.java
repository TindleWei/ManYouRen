/**
 * @Package com.manyouren.android.service.greendao    
 * @Title: GreenDaoManager.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-24 下午10:24:51 
 * @version V1.0   
 */
package com.manyouren.manyouren.service.greendao;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.daoexample.DaoMaster;
import de.greenrobot.daoexample.DaoMaster.DevOpenHelper;
import de.greenrobot.daoexample.ChatDao;
import de.greenrobot.daoexample.DaoSession;
import de.greenrobot.daoexample.User;
import de.greenrobot.daoexample.UserDao;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-24 下午10:24:51
 * 
 */
public class GreenDaoManager {

	private DaoMaster daoMaster;

	private DaoSession daoSession;

	private UserDao userDao;

	Context context;

	private void init() {

		DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,
				"mayouren_db", null);

		SQLiteDatabase db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		userDao = daoSession.getUserDao();

	}

	public boolean isSaved(int Id) {
		QueryBuilder<User> qb = userDao.queryBuilder();
		qb.where(UserDao.Properties.Id.eq(Id));
		qb.buildCount().count();
		return qb.buildCount().count() > 0 ? true : false;
	}

	public List<User> getAllUser() {
		return userDao.loadAll();
	}

	public String getUserNameById(int Id) {
		QueryBuilder<User> qb = userDao.queryBuilder();
		qb.where(UserDao.Properties.Id.eq(Id));
		if (qb.list().size() > 0) {
			return qb.list().get(0).getUserName();
		} else {
			return null;
		}
	}
	
	public void getRowId(int Id){
		ChatDao chatDao = daoSession.getChatDao();
		chatDao.loadByRowId(Id);
	}
	
	public void addToUserTab(User user){
		userDao.insert(user);
	}
	
	public void update(User user){
		userDao.insertOrReplace(user);
		userDao.insertInTx(user);
	}
	
	public void clearUser(){
		userDao.deleteAll();
	}
	
	public void deleteUser(int Id){
		QueryBuilder<User> qb = userDao.queryBuilder();
		DeleteQuery<User> dq = qb.where(UserDao.Properties.Id.eq(Id)).buildDelete();
		dq.executeDeleteWithoutDetachingEntities();
	}

}
