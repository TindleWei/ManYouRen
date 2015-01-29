/**
 * @Package com.manyouren.android.controller    
 * @Title: UserController.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-24 上午3:20:25 
 * @version V1.0   
 */
package com.manyouren.manyouren.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.HttpConfig;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.service.AsyncHttpLoader;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.service.greendao.GreenUser;
import com.manyouren.manyouren.util.DateUtils;
import com.manyouren.manyouren.util.JSONUtils;
import com.manyouren.manyouren.util.Logot;

import de.greenrobot.daoexample.User;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-24 上午3:20:25
 * 
 */
public class UserController {
	/**
	 * <!- Http Controller -->
	 */

	/**
	 * @Description: TODO
	 * 
	 * @return
	 * @return UserEntity
	 */
	public static UserEntity fetchLoginUser(Context context, String url,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, HttpConfig.LOGIN_URL, hashMap,
				handler, BaseController.CONTROLLER_LOGIN);
		return null;
	}

	public static UserEntity fetchSignupUser(Context context, String url,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, url, hashMap, handler,
				BaseController.CONTROLLER_SIGNUP);
		return null;
	}

	public static void chageUserInfo(Context context,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, HttpConfig.USER_CHANGE_INFO,
				hashMap, handler, BaseController.CONTROLLER_USER_CHANGE_INFO);
	}

	// 关注或拉黑某人
	public static void followUser(Context context,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, HttpConfig.USER_FOLLOW_URL,
				hashMap, handler, BaseController.CONTROLLER_ADD_FRIEND);
	}

	public static void refreshLocation(Context context,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, HttpConfig.USER_LOCATION_REFRESH,
				hashMap, handler, BaseController.CONTROLLER_REFRESH_LOACTION);
	}

	/**
	 * 通过UserId获取用户信息
	 * 
	 */
	public static void fectchUserById(Context context,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, "", hashMap, handler, 0);
	}

	/**
	 * <!- DB Controller -->
	 */

	/**
	 * @param context
	 * @param userId
	 * @return
	 */
	public static UserEntity getUserById(Context context, long userId) {

		User greenUser = GreenUser.getInstance(context).getUserById(userId);

		UserEntity user = new UserEntity();
		user.setUserId(greenUser.getId());
		user.setUserName(greenUser.getUserName());
		user.setBirthday(DateUtils.getTime(greenUser.getBirthday()));
		user.setGender(greenUser.getGender());
		user.setHomeland(greenUser.getHomeland());
		user.setResidence(greenUser.getResidence());
		user.setWant2Go(greenUser.getWant2Go());
		user.setSchool(greenUser.getSchool());
		user.setBeenThere(greenUser.getBeenThere());
		user.setFrequency(greenUser.getFrequency());
		user.setMagazine(greenUser.getMagazine());
		user.setHobbyText(greenUser.getHobbyText());
		user.setVehicleText(greenUser.getVehicle());
		user.setProfession(greenUser.getProfession());
		user.setCompany(greenUser.getCompany());

		user.setAvatar0(greenUser.getAvatar0());
		user.setAvatar1(greenUser.getAvatar1());
		user.setAvatar2(greenUser.getAvatar2());
		user.setAvatar3(greenUser.getAvatar3());
		user.setEmail(greenUser.getEmail());
		return user;
	}

	/**
	 * @Description: TODO
	 * @param context
	 * @param jsonString
	 * @return void
	 * @throws
	 */
	public static void saveUser(Context context, String jsonString) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonString).getJSONObject("message");

			Constants.USER_ID = jsonObject.getString("userId");

			Map<String, String> map = JSONUtils
					.parseKeyAndValueToMap(jsonObject);

			UserEntity user = MapToUser(context, map);
			
			PreferenceHelper.setResidence(user.getResidence());

			Constants.USER_ID = map.get("userId") + "";
			saveToGreenDao(context, user);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description: TODO
	 * 
	 * @return void
	 */
	public static UserEntity MapToUser(Context context, Map<String, String> map) {
		UserEntity user = new UserEntity();
		user.setUserId(Long.valueOf(map.get("userId")));

		user.setUserName(map.get("username"));
		user.setBirthday(map.get("birthday"));
		user.setGender(Integer.valueOf(map.get("gender")));
		user.setHomeland(map.get("homeland"));
		user.setResidence(map.get("residence"));
		user.setWant2Go(map.get("want2Go"));
		user.setSchool(map.get("school"));
		user.setBeenThere(map.get("beenThere"));
		user.setFrequency(Integer.valueOf(map.get("frequency")));
		user.setMagazine(map.get("magazine"));
		user.setHobbyText(map.get("hobbyText"));
		user.setVehicleText(map.get("vehicleTexxt"));
		user.setProfession(map.get("profession"));
		user.setCompany(map.get("company"));

		user.setAvatar0(map.get("avatar0"));
		user.setAvatar1(map.get("avatar1"));
		user.setAvatar2(map.get("avatar2"));
		user.setAvatar3(map.get("avatar3"));
		user.setEmail(map.get("email")); // 计划获取的user没有email

		return user;
	}

	public static void saveSessionId(String sessionId) {
		AsyncHttpLoader.SessionId = sessionId;
	}

	/**
	 * saveToGreenDao
	 * 
	 * @param user
	 * @return void
	 */
	public static void saveToGreenDao(Context context, UserEntity entity) {
		Long Id = entity.getUserId();
		if (GreenUser.getInstance(context).isSaved(Id)) {
			GreenUser.getInstance(context)
					.saveUser(changeEntityToGreen(entity));
		} else {
			GreenUser.getInstance(context).insertUser(
					changeEntityToGreen(entity));
		}
		Logot.outInfo("TAG", "GreenUser Success");
	}

	/**
	 * GreenDao User
	 * 
	 * @param user
	 * @return
	 * @return User
	 */
	public static User changeEntityToGreen(UserEntity entity) {

		User user = new User();
		user.setId(entity.getUserId());
		user.setUserName(entity.getUserName());
		user.setBirthday(com.manyouren.manyouren.util.DateUtils.getDate(entity
				.getBirthday()));
		user.setGender(entity.getGender());
		user.setHomeland(entity.getHomeland());
		user.setResidence(entity.getResidence());
		user.setWant2Go(entity.getWant2Go());
		user.setSchool(entity.getSchool());
		user.setBeenThere(entity.getBeenThere());
		user.setFrequency(entity.getFrequency());
		user.setMagazine(entity.getMagazine());
		user.setHobbyText(entity.getHobbyText());
		user.setVehicle(entity.getVehicleText());
		user.setProfession(entity.getProfession());
		user.setCompany(entity.getCompany());

		user.setAvatar0(entity.getAvatar0());
		user.setAvatar1(entity.getAvatar1());
		user.setAvatar2(entity.getAvatar2());
		user.setAvatar3(entity.getAvatar3());
		return user;
	}

	public static int getAgeFromDateString(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date old = (Date) sdf.parse(str);
			Date now = new Date();
			long diff = now.getTime() - old.getTime();
			long days = diff / (1000 * 60 * 60 * 24);
			int year = (int) days / 365;
			return year + 1;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static int getAgeFromDate(Date date) {
		Date old = date;
		Date now = new Date();
		long diff = now.getTime() - old.getTime();
		long days = diff / (1000 * 60 * 60 * 24);
		int year = (int) days / 365;
		return year + 1;
	}

	/**
	 * @Description: 获取不同分辨率的头像
	 */
	public static String getAvatarDiff(String jsonString) {
		String str = AsyncHttpManager.UPLOADS_PREFIX + jsonString;
		Logot.outError("AVATAR", str);
		return str;
	}
	
	public static String getAvatarDiff(String jsonString, int type) {
		// 0:avatar 1:thumb
		String avatar = null;
		try {
			avatar = AsyncHttpManager.UPLOADS_PREFIX
					+ new JSONArray(jsonString).get(0);
		} catch (JSONException e) {
			e.printStackTrace();
			return "no avatar";
		}
		return avatar;
	}

	/*public static String getAvatarDiff(String jsonString, int type) {
		// 0:avatar 1:thumb
		String avatar = null;
		try {
			avatar = HttpConfig.UPLOADS_PREFIX
					+ new JSONObject(jsonString).getString(type == 0 ? "origin"
							: "thumb");
		} catch (JSONException e) {
			e.printStackTrace();
			return "no avatar";
		}
		return avatar;
	}*/
}
