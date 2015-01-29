package com.manyouren.manyouren.service;

import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.PreferConfig;
import com.manyouren.manyouren.util.PreferenceUtils;

public class PreferenceHelper {
	
	private String userId;
	private String city;
	private String latitude;
	private String longitude;
	private String residence;
	private String userStatus;

	public static String getUserStatus() {
		return PreferenceUtils.getString(RootApplication.getInstance(),
				PreferConfig.PREFER_USER_STATUS, "");
	}

	public static void setUserStatus(String userStatus) {
		PreferenceUtils.putString(RootApplication.getInstance(),
				PreferConfig.PREFER_USER_STATUS, userStatus);
	}

	public static String getResidence() {
		return PreferenceUtils.getString(RootApplication.getInstance(),
				PreferConfig.PREFER_USER_RESIDENCE, "北京");
	}

	public static void setResidence(String residence) {
		PreferenceUtils.putString(RootApplication.getInstance(),
				PreferConfig.PREFER_USER_RESIDENCE, residence);
	}

	public static String getUserId(){
		return PreferenceUtils.getString(RootApplication.getInstance(),
				PreferConfig.PREFER_INITIAL_USERID, "0");
	}
	
	public static void setUserId(String uid){
		PreferenceUtils.putString(RootApplication.getInstance(),
				PreferConfig.PREFER_INITIAL_USERID, uid);
	}

	public static String getCity(){
		return PreferenceUtils.getString(RootApplication.getInstance(),
				PreferConfig.PREFER_CURRENT_CITY, "北京");
	}
	
	public static void setCity(String city){
		PreferenceUtils.putString(RootApplication.getInstance(),
				PreferConfig.PREFER_CURRENT_CITY, city);
	}
	
	public static String getLatitude(){
		return PreferenceUtils.getString(RootApplication.getInstance(),
				PreferConfig.PREFER_INITIAL_LATITUDE,
				"0.0");
	}
	
	public static void setLatitude(String latitude){
		PreferenceUtils.putString(RootApplication.getInstance(),
				PreferConfig.PREFER_INITIAL_LATITUDE,
				latitude);
	}
	
	public static String getLongitude(){
		return PreferenceUtils.getString(RootApplication.getInstance(),
				PreferConfig.PREFER_INITIAL_LONGITUDE,
				"0.0");
	}
	
	public static void setLongitude(String longitude){
		PreferenceUtils.putString(RootApplication.getInstance(),
				PreferConfig.PREFER_INITIAL_LONGITUDE,
				longitude);
	}
}
