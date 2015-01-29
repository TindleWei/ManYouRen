package com.manyouren.manyouren.service;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.NetworkUtils;

public class AsyncHttpManager {
	
	@SuppressWarnings("unused")
	public static boolean checkNetwork(Context context) {

		NetworkUtils.NetWorkState state = new NetworkUtils(context)
				.getConnectState();
		if (state.equals(NetworkUtils.NetWorkState.NONE)) {
			//Logot.outError("TAG", "网络未连接");
			return false;
		} else {
			//Logot.outError("TAG", "网络正常");
			return true;
		}
	}

	public static final String TAG = "AsyncHttpLoader";

	private static AsyncHttpClient client = null;

	public static AsyncHttpClient getClient(Context context) {

		client = new AsyncHttpClient();
		client.setTimeout(5000);
		client.addHeader("User-Agent", "android");

		return client;
	}

	// 下面都是常量
	//public static final String BASE_URL = "http://182.92.241.113/1/api/";
	//测试环境 
	public static final String BASE_URL ="http://182.92.241.113/dev/2/api/";

	public static final String UPLOADS_URL= "http://182.92.241.113/uploads/";
	
	public static final String UPLOADS_PREFIX = "http://182.92.241.113/images/";

	// 注册
	public static final String SIGNUP_URL = BASE_URL + "r1";

	// 检测邮箱是否已经被注册
	public static final String EMAIL_VERIFY__URL = BASE_URL + "r2";

	// 登陆
	public static final String LOGIN__URL = BASE_URL + "r3";

	// 查看附近的计划
	public static final String PLAN_FILTER_URL = BASE_URL + "r4";

	// 创建一个计划
	public static final String PLAN_PUBLISH_URL = BASE_URL + "r5";

	// 查看一个计划的评论
	public static final String PLAN_COMMENT_URL = BASE_URL + "r6";

	// 评论一个计划
	public static final String PLAN_SEND_COMMENT_URL = BASE_URL + "r7";

	// r8 ~ r9 预留
	//R12：查看某个人的计划列表
	public static final String USRER_PLAN_URL = BASE_URL + "r12";

	// 发布照片
	public static final String PHOTO_PUBLISH__URL = BASE_URL + "r15";

	// 查看漫游圈
	public static final String QUAN_URL = BASE_URL + "r16";

	// 查看个人相册
	public static final String USER_ALBUM_URL = BASE_URL + "r17";

	// 删除某个分享
	public static final String PHOTO_DELETE_URL = BASE_URL + "r20";

	// 查看个人资料
	public static final String USRER_INFO_URL = BASE_URL + "r23";

	// 关注/拉黑某人
	public static final String USER_FOCUS__URL = BASE_URL + "r24";

	// 得到某个关系(好友，关注，粉丝）
	public static final String USER_FREINDS_URL = BASE_URL + "r25";

	// 删除某个关系
	public static final String USER_CANCEL_URL = BASE_URL + "r26";

	// 修改个人资料(不包含头像）
	public static final String USER_CHANGE_INFO_URL = BASE_URL + "r27";

	// 修改个人状态
	public static final String USER_CHANGE_STATUS_URL = BASE_URL + "r28";

	// 修改头像
	public static final String USER_CHANGE_AVATAR_URL = BASE_URL + "r29";

	// 查看附近的人
	public static final String NEARBY_PEOPLE_URL = BASE_URL + "r33";

	// 隐身
	public static final String USER_HIDE_URL = BASE_URL + "r34";

	// 请求两个用户之间的最新距离
	public static final String USER_DISTANCE_URL = BASE_URL + "r35";
	
	// 查看两个用户的关系
	public static final String USER_RELATION_URL = BASE_URL + "r36";

	// R40 目的地搜索
	public static final String PLACE_SEARCH_URL = BASE_URL + "r40";
	
	//删除某个计划
	public static final String PLAN_DELETE_URL = BASE_URL + "r42";
	
	//版本更新
	public static final String CHECK_VERSION_URL = BASE_URL + "r100";

}
