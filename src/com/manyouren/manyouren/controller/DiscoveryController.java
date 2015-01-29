/**
 * @Package com.manyouren.android.controller    
 * @Title: DiscorveryController.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-11 下午2:54:54 
 * @version V1.0   
 */
package com.manyouren.manyouren.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.HttpConfig;
import com.manyouren.manyouren.entity.PhotoCommentEntity;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.entity.UserNearbyEntity;
import com.manyouren.manyouren.service.AsyncHttpLoader;
import com.manyouren.manyouren.util.DateUtils;
import com.manyouren.manyouren.util.JSONUtils;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-7-11 下午2:54:54
 * 
 */
public class DiscoveryController {

	/**
	 * @Fields nearbyList : 存储附近的人，for PeopleNearbyActivity
	 */
	public static List<UserNearbyEntity> nearbyList = new ArrayList<UserNearbyEntity>();

	public static int[] res_status = { R.drawable.icon_nearby_none,
			R.drawable.icon_nearby_food, R.drawable.icon_nearby_photo,
			R.drawable.icon_nearby_wine, R.drawable.icon_nearby_shop,
			R.drawable.icon_nearby_music, R.drawable.icon_nearby_vacation,
			R.drawable.icon_nearby_bike, R.drawable.icon_nearby_travel };

	public static List<String> list_nearby_status = new ArrayList<String>();
	static {
		list_nearby_status.add("想做点什么");
		list_nearby_status.add("美食&农家乐");
		list_nearby_status.add("摄影&拍照");
		list_nearby_status.add("酒吧&夜店");
		list_nearby_status.add("咖啡&茶");
		list_nearby_status.add("电影&话剧&音乐会");
		list_nearby_status.add("温泉&度假");
		list_nearby_status.add("自驾&骑车");
		list_nearby_status.add("旅行&探险");
	}


	public static List<UserNearbyEntity> getNearbyPeople(Context context,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, HttpConfig.NEARBY_PEOPLE_URL,
				hashMap, handler, BaseController.CONTOLLER_NEARBY_PEOPLE);
		return null;
	}

	/**
	 * @Description: TODO
	 * 
	 * @param context
	 * @param result
	 * @return void
	 */
	public static void parseNeabyJson(Context context, String jsonString) {
		
		String json = null;
		try {
			json = (new JSONObject(jsonString).getJSONArray("message")).toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Gson gson = new Gson();
		nearbyList = gson.fromJson(json,
				new TypeToken<List<UserNearbyEntity>>() {
				}.getType());

	}



}
