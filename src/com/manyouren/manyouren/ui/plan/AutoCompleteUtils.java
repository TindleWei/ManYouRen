/**
 * @Package com.manyouren.android.ui.plan    
 * @Title: AutoCompleteUtils.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-1 下午3:57:48 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.plan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.manyouren.manyouren.config.HttpConfig;
import com.manyouren.manyouren.service.AsyncHttpLoader;
import com.manyouren.manyouren.util.Logot;

import android.util.Log;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-7-1 下午3:57:48
 * 
 */
public class AutoCompleteUtils {
	
	public static HashMap<String,String> cityMap = new HashMap<String, String>();

	public static ArrayList<String> autocomplete(String input) {
		ArrayList<String> resultList = null;

		Map<String, String> params = new HashMap<String, String>();

		//params.put("DropList[cityName]", Constants.PLACE_NOW);
		params.put("DropList[scenicName]", input);
		
		Logot.outError("FFF", "input:" + input);

		InputStream inputStream = null;
		String jsonResults = "";
		try {
			Logot.outError("FFF","url: "+HttpConfig.PLAN_SEARCH_URL);
			inputStream = postPlaceSearch(HttpConfig.PLAN_SEARCH_URL, params);
			Logot.outError("FFF", "inputStream:" + inputStream.toString());
			
			jsonResults = readInputStream(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// Create a JSON object hierarchy from the results
			Logot.outError("FFF", "json:" + jsonResults.toString());
			
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			
			if (jsonObj.getInt("errorCode") == 0) {
				Logot.outError("FFF", "Place 获取成功");

				JSONArray predsJsonArray = jsonObj.getJSONArray("message");

				// Extract the Place descriptions from the results
				resultList = new ArrayList<String>(predsJsonArray.length());
				
				// @date: 7/28
				// there will be a trouble
				// check for this in future
				// cityMap = new HashMap<String,String>();
				
				for (int i = 0; i < predsJsonArray.length(); i++) {
					
					//{"secenicId":"12","name":"西安城墙","province":"陕西","city":"西安"},
					//{"secenicId":"26","name":"西安事变旧址\t","province":"陕西","city":"西安"}
					resultList.add(predsJsonArray.getJSONObject(i).getString(
							"name"));
					cityMap.put(predsJsonArray.getJSONObject(i).getString(
							"name"), predsJsonArray.getJSONObject(i).getString(
							"city"));
				}
			} else {
				Logot.outError("FFF", "Place 获取失败");
			}

		} catch (JSONException e) {
			Logot.outDebug("FFF", "Cannot process JSON results", e);
		}

		return resultList;
	}

	public static String readInputStream(InputStream inputStream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();

	}

	public static InputStream postPlaceSearch(String path,
			Map<String, String> params) throws Exception {

		StringBuilder data = new StringBuilder();
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				data.append(entry.getKey()).append("=");
				data.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
				data.append("&");
			}
			data.deleteCharAt(data.length() - 1);
		}
		byte[] entity = data.toString().getBytes();
		HttpURLConnection conn = (HttpURLConnection) new URL(path)
				.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(2000);
		
		//加session有问题，所以这个方法把它去掉了
		//好吧，现在又加上了
		conn.setRequestProperty("Cookie", "PHPSESSID="
				+ AsyncHttpLoader.SessionId);
		
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setDoOutput(true);
		
		Logot.outError("TAG","SESSIONID: "+AsyncHttpLoader.SessionId);

		//conn.setRequestProperty("Content-Length", String.valueOf(entity.length));
		OutputStream outStream = conn.getOutputStream();
		outStream.write(entity);
		if (conn.getResponseCode() == 200) {
			return conn.getInputStream();
		}
		
		Logot.outError("FFF", "Code: "+conn.getResponseCode());
		return null;
	}
}
