package com.manyouren.manyouren;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.Session;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.entity.PlanEntity;
import com.manyouren.manyouren.service.AVService;
import com.manyouren.manyouren.ui.chatnew.avobject.Avatar;
import com.manyouren.manyouren.ui.chatnew.avobject.ChatGroup;
import com.manyouren.manyouren.ui.chatnew.avobject.UpdateInfo;
import com.manyouren.manyouren.ui.chatnew.avobject.User;
import com.manyouren.manyouren.ui.chatnew.util.AVOSUtils;
import com.manyouren.manyouren.ui.chatnew.util.Logger;
import com.manyouren.manyouren.ui.chatnew.util.PhotoUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import de.greenrobot.daoexample.DaoMaster;
import de.greenrobot.daoexample.DaoMaster.OpenHelper;
import de.greenrobot.daoexample.DaoSession;

/**
 * ManYouRen application
 */
public class RootApplication extends Application {

	public static final String DB_NAME = "myrchat.db3";
	public static final int DB_VER = 2;

	public static boolean debug = false;
	
	public static Session session;
	private static Map<String, User> usersCache = new HashMap<String, User>();
	public static Map<String, ChatGroup> chatGroupsCache = new HashMap<String, ChatGroup>();
	List<User> friends = new ArrayList<User>();
	
	public static RootApplication instance;


	private static DaoMaster daoMaster;
	private static DaoSession daoSession;

	// 用来存放主页面的计划
	private ArrayList<PlanEntity> mPlanList = new ArrayList<PlanEntity>();

	public void setPlanList(ArrayList<PlanEntity> list) {
		if (list != null)
			mPlanList = list;
		else
			mPlanList = new ArrayList<PlanEntity>();
	}

	public ArrayList<PlanEntity> getPlanList() {
		return mPlanList;
	}

	/**
	 * Create main application
	 */
	public RootApplication() {
		instance = this;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		if (instance == null)
			instance = this;

		AVOSCloud.initialize(this,
				"tb7fs1bsceybfrdlf0rq7n379uep5t1fo5711ketanpbdd0f",
				"0z03v7b7slg4aqpein392c4s4cwixbeef6dad26pnoi90dw1");
		
		User.registerSubclass(User.class);
	    User.registerSubclass(ChatGroup.class);
	    User.registerSubclass(Avatar.class);
	    User.registerSubclass(UpdateInfo.class);

	    AVInstallation.getCurrentInstallation().saveInBackground();
	    PushService.setDefaultPushCallback(instance, HomeActivity.class);
	    AVOSUtils.showInternalDebugLog();
	    
	    if (RootApplication.debug) {
	      Logger.level = Logger.VERBOSE;
	    } else {
	      Logger.level = Logger.NONE;
	    }
	    initImageLoader(instance);
	    //initBaidu();
	    openStrictMode();

	}
	
	public void openStrictMode() {
	    if (RootApplication.debug) {
	      StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
	          .detectDiskReads()
	          .detectDiskWrites()
	          .detectNetwork()   // or .detectAll() for all detectable problems
	          .penaltyLog()
	          .build());
	      StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
	          .detectLeakedSqlLiteObjects()
	          .detectLeakedClosableObjects()
	          .penaltyLog()
	              //.penaltyDeath()
	          .build());
	    }
	  }
	
	/**
	   * 初始化ImageLoader
	   */
	  public static void initImageLoader(Context context) {
	    File cacheDir = StorageUtils.getOwnCacheDirectory(context,
	        "manyouren/Cache");
	    ImageLoaderConfiguration config = PhotoUtil.getImageLoaderConfig(context, cacheDir);
	    // Initialize ImageLoader with configuration.
	    ImageLoader.getInstance().init(config);
	  }

	  public static User lookupUser(String userId) {
		    return usersCache.get(userId);
		  }

		  public static void registerUserCache(String userId, User user) {
		    usersCache.put(userId, user);
		  }

		  public static void registerUserCache(User user) {
		    registerUserCache(user.getObjectId(), user);
		  }

		  public static void registerBatchUserCache(List<User> users) {
		    for (User user : users) {
		      registerUserCache(user);
		    }
		  }

		  public static ChatGroup lookupChatGroup(String groupId) {
		    return chatGroupsCache.get(groupId);
		  }

		  public static void registerChatGroupsCache(List<ChatGroup> chatGroups) {
		    for (ChatGroup chatGroup : chatGroups) {
		      chatGroupsCache.put(chatGroup.getObjectId(), chatGroup);
		    }
		  }

		  public List<User> getFriends() {
		    return friends;
		  }

		  public void setFriends(List<User> friends) {
		    this.friends = friends;
		  }

	public static RootApplication getInstance() {
		return instance;
	}

	/**
	 * get DaoMaster
	 * 
	 * @param context
	 * @return DaoMaster
	 */
	public static DaoMaster getDaoMaster(Context context) {
		if (daoMaster == null) {
			OpenHelper helper = new DaoMaster.DevOpenHelper(context,
					Constants.DB_NAME, null);
			daoMaster = new DaoMaster(helper.getWritableDatabase());
		}
		return daoMaster;
	}

	public static DaoSession getDaoSession(Context context) {
		if (daoSession == null) {
			if (daoMaster == null) {
				daoMaster = getDaoMaster(context);
			}
			daoSession = daoMaster.newSession();
		}
		return daoSession;
	}
}
