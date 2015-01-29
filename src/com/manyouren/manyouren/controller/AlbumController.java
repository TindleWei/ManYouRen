/**
 * @Package com.manyouren.android.controller    
 * @Title: PlanController.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-20 下午3:56:28 
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
import com.manyouren.manyouren.config.HttpConfig;
import com.manyouren.manyouren.core.user.Album;
import com.manyouren.manyouren.entity.PhotoCommentEntity;
import com.manyouren.manyouren.entity.PlanCommentEntity;
import com.manyouren.manyouren.service.AsyncHttpLoader;
import com.manyouren.manyouren.util.JSONUtils;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-20 下午3:56:28
 * 
 */
public class AlbumController {

	public static List<Album> albumList = new ArrayList<Album>();
	public static List<PhotoCommentEntity> commentList = new ArrayList<PhotoCommentEntity>();
	public static int albumPage = 1;
	public static int manyouPage = 1;
	public static int commentPage = 1;
	public static int perpage = 5;

	public static boolean getAlbumList(Context context, int page, HashMap<String, Object> hashMap,
			final Handler handler) {
		albumPage = page;
		AsyncHttpLoader.fetchRequest(context, HttpConfig.USER_ALBUM_URL, hashMap, handler,
				BaseController.CONTROLLER_USER_ALBUMS);
		return true;
	}

	/**
	 * 获取漫游圈列表
	 * 
	 */
	public static boolean getManyouList(Context context, int p,
			final Handler handler) {
		manyouPage = p;
		AsyncHttpLoader.getRequest(context, HttpConfig.MANYOU_LIST_URL,
				handler, BaseController.CONTROLLER_MANYOU_LIST);
		return true;
	}

	/**
	 * @Description: TODO
	 * 
	 */
	public static void parseAlbumJson(Context context, String jsonString) {
		JSONArray jsonArray;
		if (albumPage == 1)
			albumList = new ArrayList<Album>();
		else if (albumList == null)
			albumList = new ArrayList<Album>();
		try {
			jsonArray = new JSONObject(jsonString).getJSONArray("message");

			for (int i = 0; i < jsonArray.length(); i++) {
				Map<String, String> map = JSONUtils
						.parseKeyAndValueToMap(jsonArray.getJSONObject(i));
				albumList.add(mapToAlbum(context, map));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: AlbumController
	 * @Description: TODO(parse comment list)
	 * @throws
	 */
	public static void parseCommentJson(Context context, String jsonString) {
		
		String json = null;
		try {
			json = (new JSONObject(jsonString).getJSONArray("message")).toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Gson gson = new Gson();
		commentList = gson.fromJson(json,
				new TypeToken<List<PhotoCommentEntity>>() {
				}.getType());
	}

	/**
	 * map key & value to planEntity
	 * 
	 * @param context
	 * @param map
	 * @return void
	 */
	public static Album mapToAlbum(Context context, Map<String, String> map) {
		Album album = new Album();

		album.setId(Long.parseLong(map.get("photoId")));
		album.setUserId(Long.parseLong(map.get("userId")));
		album.setUsername(map.get("username").toString());

		album.setSmallAvatar(UserController.getAvatarDiff(map.get("avatar0")));
		
		album.setDateline(map.get("createTime"));
		album.setAlbumcity(map.get("city"));
		// album.setAlbumtype(Integer.parseInt(map.get("altumtype")));
		album.setAlbumtype(1);
		album.setContent(map.get("content"));
		album.setLocation(map.get("location"));
		// album.setLikenum(Integer.parseInt(map.get("likenum")));
		album.setPics(map.get("images"));
		
		album.setGender(map.containsKey("gender") ? Integer.parseInt(map.get(
				"gender").toString()) : 0);

		if (map.get("score").equals("null")) {
			album.setRating(0);
		} else {
			album.setRating(Float.parseFloat(map.get("score")));
		}
		return album;
	}

	/** 
	* 发布照片
	*
	*/
	public static void fetchPublishAlbum(Context context, String url,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, url, hashMap, handler,
				BaseController.CONTROLLER_ALBUM_PUBLSIH);
	}
	
	/** 
	* 获取评论
	*/
	public static void fetchQuanComment(Context context, String url,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, url, hashMap, handler,
				BaseController.CONTROLLER_QUAN_COMMENT);
	}
	
	/**
	 * 发送评论
	 */
	public static boolean sendQuanComment(Context context,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, HttpConfig.QUAN_SEND_COMMENT,
				hashMap, handler, BaseController.CONTROLLER_QUAN_SEND_COMMENT);
		return true;
	}
}