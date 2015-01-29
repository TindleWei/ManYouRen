/**
 * @Package com.manyouren.android.service.greendao    
 * @Title: GreenUser.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-25 上午11:25:21 
 * @version V1.0   
 */
package com.manyouren.manyouren.service.greendao;

import java.util.List;

import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.config.Constants;

import android.content.Context;
import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.daoexample.Chat;
import de.greenrobot.daoexample.DaoSession;
import de.greenrobot.daoexample.User;
import de.greenrobot.daoexample.UserDao;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-25 上午11:25:21
 * 
 */
public class GreenUser {

	private static GreenUser instance;

	private UserDao userDao;

	private GreenUser() {
	}

	public static GreenUser getInstance(Context context) {
		if (instance == null) {
			instance = new GreenUser();
			DaoSession daoSession = RootApplication.getDaoSession(context);
			instance.userDao = daoSession.getUserDao();
		}
		return instance;
	}

	public void insertUser(User entity) {
		userDao.insert(entity);
	}
	
	public long saveUser(User entity){
		return userDao.insertOrReplace(entity);
	}

	public List<User> getUserList(long Id) {
		QueryBuilder<User> qb = userDao.queryBuilder();
		qb.where(UserDao.Properties.Id.eq(Id));
		return qb.list();
	}

	public List<User> getAllUser() {
		return userDao.loadAll();
	}

	public boolean isSaved(long Id) {
		QueryBuilder<User> qb = userDao.queryBuilder();
		qb.where(UserDao.Properties.Id.eq(Id));
		qb.buildCount().count();
		return qb.buildCount().count() > 0 ? true : false;// 查找收藏表
	}
	
	public void deleteById(long Id)
    {
        QueryBuilder<User> qb = userDao.queryBuilder();
        DeleteQuery<User> bd = qb.where(UserDao.Properties.Id.eq(Id)).buildDelete();
        bd.executeDeleteWithoutDetachingEntities();
    }
	
	public void clearAll(){
		QueryBuilder<User> qb = userDao.queryBuilder();
        DeleteQuery<User> bd = qb.where(UserDao.Properties.Id.notEq(Constants.USER_ID)).buildDelete();
        bd.executeDeleteWithoutDetachingEntities();
	}
	
	public void clearAllUser()
    {
        userDao.deleteAll();
    }
	
	public String getUserAvatar(long Id){
		QueryBuilder<User> qb = userDao.queryBuilder();
		qb.where(UserDao.Properties.Id.eq(Id));
		if (qb.list().size() > 0) {
			return qb.list().get(0).getAvatar0();
		} else {
			return null;
		}
	}
	
    public User getUserById(long Id)
    {
        QueryBuilder<User> qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Id.eq(Id));
        return qb.unique();
    }

}
