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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.config.HttpConfig;
import com.manyouren.manyouren.entity.PlanCommentEntity;
import com.manyouren.manyouren.entity.PlanEntity;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.service.AsyncHttpLoader;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.greendao.GreenPlan;
import com.manyouren.manyouren.service.greendao.GreenUser;
import com.manyouren.manyouren.ui.user.UserPlansActivity;
import com.manyouren.manyouren.util.DateUtils;
import com.manyouren.manyouren.util.JSONUtils;
import com.manyouren.manyouren.util.Logot;

import de.greenrobot.daoexample.User;

/**
 * 
 * @author firefist_wei
 * @date 2014-6-20 下午3:56:28
 * 
 */
public class PlanController {

	// 用来判断是否分页
	public static Boolean isFirstPage = true;

	// 某个计划的评论数据
	public static List<PlanCommentEntity> commentList = new ArrayList<PlanCommentEntity>();

	/**
	 * 发布计划
	 */
	public static void fetchPublishPlan(Context context, String url,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, url, hashMap, handler,
				BaseController.CONTROLLER_PLAN_PUBLSIH);
	}

	/**
	 * 获取计划
	 */
	public static boolean fetchFilterPlan(Context context, String suffixUrl,
			HashMap<String, Object> hashMap, Handler handler) {

		if (suffixUrl == null) {
			isFirstPage = true;
			AsyncHttpLoader.fetchRequest(context, HttpConfig.PLAN_FILTER_URL,
					hashMap, handler, BaseController.CONTROLLER_PLAN_FILTER);

		} else {
			isFirstPage = false;
			AsyncHttpLoader.fetchRequest(context, HttpConfig.PLAN_FILTER_URL
					+ suffixUrl, hashMap, handler,
					BaseController.CONTROLLER_PLAN_NEXT);
		}
		return true;
	}

	/**
	 * 获取某个用户的计划
	 */
	public static boolean fetchUserPlan(Context context,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, HttpConfig.USER_PLAN_URL,
				hashMap, handler, BaseController.CONTROLLER_USER_PLANS);
		return true;
	}

	/**
	 * 删除计划
	 */
	public static boolean deletePlan(Context context,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, HttpConfig.USER_PLAN_DELETE,
				hashMap, handler, BaseController.CONTROLLER_DELETE_PLAN);
		return true;
	}

	/**
	 * 获取评论数据
	 */
	public static boolean fetchPlanComment(Context context, String suffixUrl,
			HashMap<String, Object> hashMap, Handler handler) {

		if (suffixUrl == null) {
			isFirstPage = true;
			AsyncHttpLoader.fetchRequest(context, HttpConfig.PLAN_COMMENT_URL,
					hashMap, handler, BaseController.CONTROLLER_PLAN_COMMENT);

		} else {
			isFirstPage = false;
			AsyncHttpLoader.fetchRequest(context, HttpConfig.PLAN_COMMENT_URL
					+ suffixUrl, hashMap, handler,
					BaseController.CONTROLLER_PLAN_COMMENT);
		}
		return true;
	}

	/**
	 * 发送评论
	 */
	public static boolean sendPlanComment(Context context,
			HashMap<String, Object> hashMap, Handler handler) {
		AsyncHttpLoader.fetchRequest(context, HttpConfig.PLAN_SEND_COMMENT_URL,
				hashMap, handler, BaseController.CONTROLLER_PLAN_SEND_COMMENT);
		return true;
	}

	/**
	 * 解析Plan数据
	 */
	/*
	 * public static void parsePlanJson(Context context, String jsonString) {
	 * JSONArray jsonArray; ArrayList<PlanEntity> list =
	 * RootApplication.getInstance() .getPlanList();
	 * 
	 * if (isFirstPage) { list = new ArrayList<PlanEntity>(); } try { jsonArray
	 * = new JSONObject(jsonString).getJSONArray("message");
	 * 
	 * for (int i = 0; i < jsonArray.length(); i++) { Map<String, String> map =
	 * JSONUtils .parseKeyAndValueToMap(jsonArray.getJSONObject(i));
	 * 
	 * PlanEntity plan = mapToPlan(context, map); UserEntity user =
	 * UserController.MapToUser(context, map); plan.setUserEntity(user);
	 * list.add(plan); saveToGreenDao(context, plan, user); } } catch
	 * (JSONException e) { e.printStackTrace(); }
	 * RootApplication.getInstance().setPlanList(list); }
	 */

	/**
	 * 解析Plan数据
	 */
	/*
	 * public static void parseUserPlanJson(Context context, String jsonString)
	 * { JSONArray jsonArray; if (isFirstPage) { UserPlansActivity.userPlansList
	 * = new ArrayList<PlanEntity>(); } try { jsonArray = new
	 * JSONObject(jsonString).getJSONArray("message");
	 * 
	 * for (int i = 0; i < jsonArray.length(); i++) { Map<String, String> map =
	 * JSONUtils .parseKeyAndValueToMap(jsonArray.getJSONObject(i));
	 * 
	 * PlanEntity plan = mapToPlan(context, map);
	 * UserPlansActivity.userPlansList.add(plan); } } catch (JSONException e) {
	 * e.printStackTrace(); } }
	 */

	public static void parseCommentJson(Context context, String jsonString) {
		String json = null;
		try {
			json = (new JSONObject(jsonString).getJSONArray("message"))
					.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Gson gson = new Gson();
		commentList = gson.fromJson(json,
				new TypeToken<List<PlanCommentEntity>>() {
				}.getType());

	}

	public static List<String> list_for = new ArrayList<String>();
	static {
		list_for.add("游玩");
		list_for.add("度假");
		list_for.add("出差");
	}

	public static String[] tip_for = { "游览赏玩,高兴就好", "身心的调节与放松，是一种心灵的体验",
			"暂时到外地工作" };

	public static int[] res_for = { R.drawable.ic_planfor_play_g,
			R.drawable.ic_planfor_holiday_g, R.drawable.ic_planfor_bussiness_g };

	public static List<String> list_with = new ArrayList<String>();
	static {
		list_with.add("自己");
		list_with.add("朋友");
		list_with.add("家人");
	}

	public static String[] tip_with = { "自己一个人独自旅行", "与朋友一起，希望结识更多的人",
			"一家人一起出行" };

	public static int[] res_with = { R.drawable.ic_planwith_single_b,
			R.drawable.ic_planwith_friends_b, R.drawable.ic_planwith_family_b };

	public static List<String> list_seek = new ArrayList<String>();
	static {
		list_seek.add("伙伴");
		list_seek.add("建议");
		list_seek.add("拼车");
	}

	public static String[] tip_seek = { "寻找一起出行的伙伴", "希望得到旅行的建议和帮助",
			"可以拼车一同出行", };

	public static int[] res_seek = { R.drawable.ic_planseek_partner_y,
			R.drawable.ic_planseek_advice_y, R.drawable.ic_planseek_car_y };
	/*public static int[] res_seek = { R.drawable.ic_planseek_partner,
		R.drawable.ic_planseek_advice, R.drawable.ic_planseek_car };*/

	/**
	 * 将json中的images路径存入List
	 * 
	 */
	public static List<String> getPlanImages(String jsonString) {
		// 0:origin 1:thumb
		List<String> list = new ArrayList<String>();
		try {

			JSONArray imagesArray = new JSONArray(jsonString);

			for (int i = 0; i < imagesArray.length(); i++) {
				list.add(AsyncHttpManager.UPLOADS_PREFIX
						+ imagesArray.getString(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}
