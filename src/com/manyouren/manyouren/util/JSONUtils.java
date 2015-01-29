/**
 * @Package com.manyouren.android.util    
 * @Title: JSONUtils.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-12 下午10:18:15 
 * @version V1.0   
 */
package com.manyouren.manyouren.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-12 下午10:18:15
 * 
 */
public class JSONUtils {

	/**
	 * get String array from jsonObject
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return String[]
	 */
	public static String[] getStringArray(JSONObject jsonObject, String key,
			String[] defaultValue) {
		if (jsonObject == null || StringUtils.isEmpty(key)) {
			return defaultValue;
		}
		try {
			JSONArray statusArray = jsonObject.getJSONArray(key);
			if (statusArray != null) {
				String[] value = new String[statusArray.length()];
				for (int i = 0; i < statusArray.length(); i++) {
					value[i] = statusArray.getString(i);
				}
				return value;
			}
		} catch (JSONException e) {
			e.printStackTrace();

			return defaultValue;
		}
		return defaultValue;
	}

	/**
	 * get String array from jsonData
	 * 
	 * @param jsonData
	 * @param key
	 * @param defaultValue
	 * @return String[]
	 */
	public static String[] getStringArray(String jsonData, String key,
			String[] defaultValue) {
		if (StringUtils.isEmpty(jsonData)) {
			return defaultValue;
		}
		try {
			JSONObject jsonObject = new JSONObject(jsonData);
			return getStringArray(jsonObject, key, defaultValue);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return defaultValue;
	}

	/**
	 * get jsonObject from jsonObject
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return JSONObject
	 */
	public static JSONObject getJSONObject(JSONObject jsonObject, String key,
			JSONObject defaultValue) {
		if (jsonObject == null || StringUtils.isEmpty(key)) {
			return defaultValue;
		}
		try {
			return jsonObject.getJSONObject(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return defaultValue;
	}

	/**
	 * get JSONArray from jsonObject
	 * 
	 * @param jsonObject
	 * @param key
	 * @param defaultValue
	 * @return JSONArray
	 */
	public static JSONArray getJSONArray(JSONObject jsonObject, String key,
			JSONArray defaultValue) {
		if (jsonObject == null || StringUtils.isEmpty(key)) {
			return defaultValue;
		}
		try {
			return jsonObject.getJSONArray(key);
		} catch (JSONException e) {

		}
		return defaultValue;
	}

	/**
	 * parse key-value pairs to map.
	 * 
	 * @param sourceObj
	 * @return
	 * @return Map<String,String>
	 * @throws JSONException 
	 */
	public static Map<String, String> parseKeyAndValueToMap(JSONObject sourceObj) throws JSONException{
		if(sourceObj == null)
			return null;
		Map<String, String> keyAndValueMap = new HashMap<String, String>();
		for (Iterator iter = sourceObj.keys(); iter.hasNext();){
			String key = (String)iter.next();
			MapUtils.putMapNotEmptyKey(keyAndValueMap, key, sourceObj.getString(key));
		}
		return keyAndValueMap;
	}

	/**
	 * parse string key-value pairs to map.
	 * 
	 * @param source
	 * @return Map<String,String>
	 */
	public static Map<String, String> parseKeyAndValueToMap(String source) {
		if (StringUtils.isEmpty(source)) {
			return null;
		}
		try {
			JSONObject jsonObject = new JSONObject(source);
			return parseKeyAndValueToMap(jsonObject);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
