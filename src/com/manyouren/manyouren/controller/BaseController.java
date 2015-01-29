/**
 * @Package com.manyouren.android.controller    
 * @Title: BaseContoller.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-2 下午10:18:18 
 * @version V1.0   
 */
package com.manyouren.manyouren.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.service.greendao.GreenUser;
import com.manyouren.manyouren.util.JSONUtils;

import de.greenrobot.daoexample.User;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-7-2 下午10:18:18
 * 
 */
public class BaseController {
	
	//登陆模块

	public static final int CONTROLLER_SIGNUP = 1;

	public static final int CONTROLLER_LOGIN = 2;
	
	//计划模块

	public static final int CONTROLLER_PLAN_PUBLSIH = 3;

	//public static final int CONTROLLER_PLAN_SHOW = 4;
	
	public static final int CONTROLLER_PLAN_FILTER = 5;
	
	public static final int CONTROLLER_PLAN_COMMENT = 51;
	
	public static final int CONTROLLER_PLAN_SEND_COMMENT = 52;

	//对话模块
	
	public static final int CONTROLLER_CHAT_SEND = 6;
	
	//发现模块

	public static final int CONTOLLER_NEARBY_PEOPLE = 7;

	public static final int CONTROLLER_ALBUM_PUBLSIH = 11;

	public static final int CONTROLLER_MANYOU_LIST = 12;

	public static final int CONTROLLER_QUAN_COMMENT = 14;
	
	public static final int CONTROLLER_QUAN_SEND_COMMENT = 141;
	
	
	//用户模块
	
	public static final int CONTROLLER_USER_CHANGE_INFO = 81;

	public static final int CONTROLLER_USER_PLANS = 8;

	public static final int CONTROLLER_PLAN_NEXT = 9;

	public static final int CONTROLLER_USER_ALBUMS = 10;
	
	public static final int CONTROLLER_DELETE_PLAN = 13;
	
	public static final int CONTROLLER_FRIENDS_LIST = 15;

	public static final int CONTROLLER_FRIENDS_FOLLOW = 16;
	
	public static final int CONTROLLER_FRIENDS_FANS = 17;
	
	public static final int CONTROLLER_ADD_FRIEND = 18;
	
	public static final int CONTROLLER_REFRESH_LOACTION = 19;

	/**
	 * @Description: TODO
	 * 
	 * @param requestName
	 * @return void
	 */
	public static void handleBack(Context context, int backName, String result) {
		switch (backName) {
		case CONTROLLER_SIGNUP:
			UserController.saveUser(context, result);
			
			break;
		case CONTROLLER_LOGIN:
			UserController.saveUser(context, result);

			break;
		case CONTROLLER_PLAN_PUBLSIH:

			break;
			
		case CONTROLLER_PLAN_FILTER:
//			GreenPlan.getInstance(context).deleteAll();
//			GreenUser.getInstance(context).clearAll();
//			
//			if(result.equals("")){
//				RootApplication.getInstance().setPlanList(null);
//			}else{
//				PlanController.parsePlanJson(context, result);
//			}
			
			break;
			
		case CONTROLLER_PLAN_NEXT:
			//PlanController.parsePlanJson(context, result);
			break;


		
		case CONTROLLER_CHAT_SEND:
			break;

		case CONTOLLER_NEARBY_PEOPLE:
			DiscoveryController.parseNeabyJson(context, result);
			break;

		case CONTROLLER_USER_PLANS:
			//PlanController.parseUserPlanJson(context, result);
			break;

		case CONTROLLER_DELETE_PLAN:
			break;

		case CONTROLLER_USER_ALBUMS:
			AlbumController.parseAlbumJson(context, result);

			break;

		case CONTROLLER_ALBUM_PUBLSIH:

			break;

		case CONTROLLER_MANYOU_LIST:
			AlbumController.parseAlbumJson(context, result);
			break;

		case CONTROLLER_QUAN_COMMENT:
			AlbumController.parseCommentJson(context, result);
			break;
			
		case CONTROLLER_QUAN_SEND_COMMENT:
			break;

		case CONTROLLER_FRIENDS_LIST:
			//FriendController.parseFriendsJson(context, result);
			break;
			
		case CONTROLLER_FRIENDS_FOLLOW:
			//FriendController.parseFollowJson(context, result);
			break;
			
		case CONTROLLER_FRIENDS_FANS:
			//FriendController.parseFansJson(context, result);
			break;
		
		case CONTROLLER_PLAN_COMMENT:
			PlanController.parseCommentJson(context, result);
			break;
			
		case CONTROLLER_PLAN_SEND_COMMENT:
			
			break;
			
		case CONTROLLER_USER_CHANGE_INFO:
			
			break;
		case CONTROLLER_REFRESH_LOACTION:
			
			break;

		default:
			break;
		}

	}

}
