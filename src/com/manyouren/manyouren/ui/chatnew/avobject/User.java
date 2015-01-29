package com.manyouren.manyouren.ui.chatnew.avobject;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVUser;

/**
 * Created by lzw on 14-6-26.
 */
@AVClassName("_User")
public class User extends AVUser {

	public static User curUser;
	private String sortLetters;

	public User() {
	}

	public static User curUser() {
		
		AVUser avUser = getCurrentUser(User.class);
		User user = User.cast(avUser, User.class);
		return user;

	}

	public static String curUserId() {
		User user = curUser();
		if (user != null) {
			return user.getObjectId();
		} else {
			return null;
		}
	}
	
	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	
	/*
	 * public static final String USERNAME = "username"; public static final
	 * String PASSWORD = "password"; public static final String AVATAR =
	 * "avatar"; public static final String FRIENDS = "friends"; public static
	 * final String LOCATION = "location"; public static final String SEX =
	 * "sex";
	 */
	// AVFile avatar;
	// AVGeoPoint location;
	// private boolean sex;// true is male ; false is female
	
	/*
	 * public AVFile getAvatar() { return getAVFile(AVATAR); }
	 * 
	 * public void setAvatar(AVFile avatar) { put(AVATAR, avatar); }
	 * 
	 * public String getAvatarUrl() { AVFile avatar = getAvatar(); if (avatar !=
	 * null) { return avatar.getUrl(); } else { return null; } }
	 * 
	 * public void addFriend(User user) { getRelation(FRIENDS).add(user); }
	 * 
	 * public void removeFriend(User user) { getRelation(FRIENDS).remove(user);
	 * }
	 * 
	 * public AVGeoPoint getLocation() { return getAVGeoPoint(LOCATION); }
	 * 
	 * public void setLocation(AVGeoPoint location) { put(LOCATION, location); }
	 * 
	 * public boolean getSex() { return getBoolean(SEX); }
	 * 
	 * public void setSex(boolean isMale) { put(SEX, isMale); }
	 * 
	 * public String getSexInfo() { return getSex() ?
	 * RootApplication.getInstance() .getString(R.string.male) :
	 * RootApplication.getInstance() .getString(R.string.female); }
	 */


}
