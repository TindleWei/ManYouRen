/**
* @Package com.manyouren.android.config    
* @Title: Constants.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-6-16 下午1:43:41 
* @version V1.0   
*/
package com.manyouren.manyouren.config;

/** 
 * ManYouRen Constants
 *
 * @author firefist_wei
 * @date 2014-6-16 下午1:43:41 
 *  
 */
public final class Constants {
	
	public static String USER_ID = "";
	
	public static String USER_NAME = "";
	
	public static String PLACE_NOW = "";
	
	public static String LONGITUDE = ""; //精度 108.951933
	
	public static String LATITUDE = "";  //纬度    34.172777
	
	private Constants(){}
	
	public static String getUserId(){
		//若为空， 从SharedPreference读取
		return USER_ID;
	}
	
	public static final String DB_NAME = "manyouren-db";
	
	/**
	 * firefist.wei puzzle text:
     * All HTTP is done through a REST style API built for demonstration purposes on Parse.com
     * Thanks to the nice people at Parse for creating such a nice system for us to use for bootstrap!
     */

    public static final class Http {
        private Http() {}
        
        /**
         * Base URL for all requests
         */
        public static final String URL_BASE = "https://api.parse.com";
        
        /**
         * List Plans URL
         */
        public static final String URL_PLANS_FRAG = "/1/classes/News";
        public static final String URL_PLANS = URL_BASE + URL_PLANS_FRAG;

    }
	
	public static final class Extra {
        private Extra() {}
        
        public static final String CHAT_TOID = "chat_toid";
        
        public static final String PLAN_TO_CHAT = "plan_to_chat";
        
        public static final String NOTIFY_TO_CHAT = "notify_to_chat";
        
        public static final String CHAT_TO_CHAT = "chat_to_chat";
        
        public static final String PLANS_ITEM = "plans_item";
        
        public static final String OTHERS = "others";
       

        public static final String USER = "user";

    }
	
	public static final class Intent {
        private Intent() {}

        /**
         * Action prefix for all intents created
         */
        public static final String INTENT_PREFIX = "com.manyouren.android.";

    }
	
	public static class Notification {
        private Notification() {
        }

        public static final int TIMER_NOTIFICATION_ID = 1000; // Why 1000? Why not? :)
    }

}
