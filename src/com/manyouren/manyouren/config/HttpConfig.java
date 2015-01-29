/**
 * @Package com.manyouren.android.config    
 * @Title: HttpCinfig.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-12 上午11:04:17 
 * @version V1.0   
 */
package com.manyouren.manyouren.config;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-12 上午11:04:17
 * 
 */
public class HttpConfig {

	
	public static final String BASE_URL = "http://www.travelman.com.cn/server/travelman/";
	// http://www.travelman.com.cn/2/ http://192.168.0.14/manyouren/0813/
	// http://192.168.0.14/manyouren/2/

	public static final String UPLOADS_PREFIX = "http://www.travelman.com.cn/server/"
			+ "uploads/";

	public static final String SIGNUP_URL = BASE_URL
			+ "index.php/user/default/registration";

	public static final String LOGIN_URL = BASE_URL
			+ "index.php/user/default/login";

	//计划模块
	
	public static final String PLAN_FILTER_URL = BASE_URL + "index.php/plan";

	public static final String PLAN_PUBLISH_URL = BASE_URL
			+ "index.php/plan/publish";

	public static final String PLAN_SEARCH_URL = BASE_URL
			+ "index.php/plan/tip";
	
	public static final String PLAN_COMMENT_URL = BASE_URL
			+ "index.php/plan/detail";
	
	public static final String PLAN_SEND_COMMENT_URL = BASE_URL
			+ "index.php/plan/comment";
	
	//聊天模块

	public static final String CHAT_SEND_URL = BASE_URL
			+ "index.php/push/chat";

	//发现模块
	
	public static final String NEARBY_PEOPLE_URL = BASE_URL
			+ "index.php/jianren";
	
	public static final String USER_ALBUM_URL = BASE_URL
			+ "index.php//user/photo/list";

	public static final String USER_ALBUM_PUBLISH_URL = BASE_URL
			+ "index.php/manyouquan/default/publish";

	public static final String MANYOU_LIST_URL = BASE_URL
			+ "index.php/manyouquan";
	
	public static final String QUAN_COMMENT_LIST = BASE_URL
			+ "index.php/manyouquan/comment/list";
	
	public static final String QUAN_SEND_COMMENT = BASE_URL
			+ "index.php/manyouquan/comment";
	
	//用户模块

	public static final String USER_PLAN_URL = BASE_URL
			+ "index.php/user/plan";

	public static final String USER_PLAN_DELETE = BASE_URL
			+ "index.php/user/plan/delete";
	
	public static final String USER_CHANGE_INFO = BASE_URL
			+ "index.php/user/edit";

	public static final String USER_LOCATION_REFRESH = BASE_URL
			+ "index.php/user/refresh";

	public static final String USER_FOLLOW_URL = BASE_URL
			+ "index.php/user/relation/add";

	public static String FRIEND_URL = BASE_URL
			+ "index.php/user/relation/friends";

	public static String FRIEND_FANS_URL = BASE_URL
			+ "index.php/user/relation/fans";

	public static String FRIEND_FOLLOW__URL = BASE_URL
			+ "index.php/user/relation/follows";

}
