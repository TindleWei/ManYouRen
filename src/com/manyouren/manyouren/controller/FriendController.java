/**
 * @Package com.manyouren.android.controller    
 * @Title: PlanController.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-20 下午3:56:28 
 * @version V1.0   
 */
package com.manyouren.manyouren.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.config.HttpConfig;
import com.manyouren.manyouren.core.user.CharacterParser;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpLoader;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.util.JSONUtils;
import com.manyouren.manyouren.util.Logot;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-20 下午3:56:28
 * 
 */
public class FriendController {

	public static CharacterParser characterParser = CharacterParser
			.getInstance();
	public static List<UserEntity> friendsList = new ArrayList<UserEntity>();
	public static List<UserEntity> fansList = new ArrayList<UserEntity>();
	public static List<UserEntity> followList = new ArrayList<UserEntity>();

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	public static List<UserEntity> filterData(String filterStr) {
		List<UserEntity> filterDateList = new ArrayList<UserEntity>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = friendsList;
		} else {
			filterDateList.clear();
			for (UserEntity sortModel : friendsList) {
				String name = sortModel.getUserName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}
		return filterDateList;
	}

	public static boolean getFriendsList(Context context, int type,
			final Handler handler) {
		// type 0: friends
		// type 1: follow
		// type 2: fans
		switch (type) {
		case 0:
			String url0 = AsyncHttpManager.USER_FREINDS_URL;
			RequestParams params0 = new RequestParams();
			params0.put("userId", PreferenceHelper.getUserId());
			params0.put("type", "0"); // 好友
			AsyncHttpManager.getClient(context).post(url0, params0,
					new AsyncHttpHandler() {
						@Override
						public void onSuccessed() {
							super.onSuccessed();
							String json = "";
							try {
								json = new JSONObject(getResult())
										.getString("message");
								friendsList = new Gson().fromJson(json,
										new TypeToken<List<UserEntity>>() {
										}.getType());
							} catch (JSONException e) {
								e.printStackTrace();
							}
							handler.sendEmptyMessage(1);
						}
						@Override
						public void onEmpty() {
							super.onEmpty();
							friendsList = new ArrayList<UserEntity>();
							handler.sendEmptyMessage(1);
						}

						@Override
						public void onFailured() {
							super.onFailured();
							handler.sendEmptyMessage(-1);
						}	
					});

			break;
		case 1: // 关注

			String url1 = AsyncHttpManager.USER_FREINDS_URL;
			RequestParams params1 = new RequestParams();
			params1.put("userId", PreferenceHelper.getUserId());
			params1.put("type", "1");
			AsyncHttpManager.getClient(context).post(url1, params1,
					new AsyncHttpHandler() {
				@Override
				public void onSuccessed() {
					super.onSuccessed();
					String json = "";
					try {
						json = new JSONObject(getResult())
								.getString("message");
						followList = new Gson().fromJson(json,
								new TypeToken<List<UserEntity>>() {
								}.getType());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					handler.sendEmptyMessage(1);
				}
				@Override
				public void onEmpty() {
					super.onEmpty();
					followList = new ArrayList<UserEntity>();
					handler.sendEmptyMessage(1);
				}

				@Override
				public void onFailured() {
					super.onFailured();
					handler.sendEmptyMessage(-1);
				}	
			});

			break;
		case 2: // 粉丝

			String url2 = AsyncHttpManager.USER_FREINDS_URL;
			RequestParams params2 = new RequestParams();
			params2.put("userId", PreferenceHelper.getUserId());
			params2.put("type", "2"); 
			AsyncHttpManager.getClient(context).post(url2, params2,
					new AsyncHttpHandler() {
				@Override
				public void onSuccessed() {
					super.onSuccessed();
					String json = "";
					try {
						json = new JSONObject(getResult())
								.getString("message");
						fansList = new Gson().fromJson(json,
								new TypeToken<List<UserEntity>>() {
								}.getType());
					} catch (JSONException e) {
						e.printStackTrace();
					}
					handler.sendEmptyMessage(1);
				}
				@Override
				public void onEmpty() {
					super.onEmpty();
					fansList = new ArrayList<UserEntity>();
					handler.sendEmptyMessage(1);
				}

				@Override
				public void onFailured() {
					super.onFailured();
					handler.sendEmptyMessage(-1);
				}	
					});
			break;
		}

		return true;
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

}