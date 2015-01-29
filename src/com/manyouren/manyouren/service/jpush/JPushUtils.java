package com.manyouren.manyouren.service.jpush;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.util.Log;

import com.manyouren.manyouren.entity.UserEntity;

/* JPush接收到的信息：[msg:{
 * send_uid
 * receive_uid
 * content
 * time
 * }]
 * */
public class JPushUtils {

	public static boolean isMainForeground = false;
	
	public static boolean isChatForeground = false;
	
	public static boolean isChatFragment = false;

	public static int unreadNum = 0;
	
	public static String toUserId = "";
	
	public static UserEntity userEntity = null;



}
