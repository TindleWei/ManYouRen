/**
 * @Package com.manyouren.android.util    
 * @Title: MapUtils.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-13 上午11:11:56 
 * @version V1.0   
 */
package com.manyouren.manyouren.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-13 上午11:11:56
 * 
 */
public class MapUtils {

	/** default separator between key and value **/
	public static final String DEFAULT_K_V_SEPARATOR = ":";
	/** default separator between key-value pairs **/
	public static final String DEFAULT_K_V_PAIR_SEPARATOR = ",";

	/**
	 * is null or its size is 0
	 * 
	 * @param sourceMap
	 * @return
	 * @return boolean
	 */
	public static <K, V> boolean isEmpty(Map<K, V> sourceMap) {
		return (sourceMap == null || sourceMap.size() == 0);
	}

	/**
	 * add key-value pair to map
	 * 
	 * @param map
	 * @param key
	 * @param value
	 * @return boolean
	 */
	public static boolean putMapNotEmptyKey(Map<String, String> map,
			String key, String value) {
		if (map == null || StringUtils.isEmpty(key))
			return false;
		map.put(key, value);
		return true;
	}

	/**
	 * join map
	 * 
	 * @param map
	 * @return String
	 */
	public static String toJson(Map<String, String> map) {
		if (map == null || map.size() == 0) {
			return null;
		}
		StringBuilder paras = new StringBuilder();
		paras.append("{");
		Iterator<Map.Entry<String, String>> ite = map.entrySet().iterator();
		while (ite.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) ite
					.next();
			paras.append("\"").append(entry.getKey()).append("\":\"")
					.append(entry.getValue()).append("\"");
			if (ite.hasNext()) {
				paras.append(",");
			}
		}
		paras.append("}");
		return paras.toString();
	}

	public static Map<String, String> parseToMap(String source) {

		return parseToMap(source, DEFAULT_K_V_SEPARATOR,
				DEFAULT_K_V_PAIR_SEPARATOR, true);
	}

	public static Map<String, String> parseToMap(String source,
			boolean ignoreSpace) {
		return parseToMap(source, DEFAULT_K_V_SEPARATOR,
				DEFAULT_K_V_PAIR_SEPARATOR, true);
	}

	/**
	 * @Description: TODO
	 * 
	 * <pre>
	 * parseKeyAndValueToMap("","","",true)=null
	 * parseKeyAndValueToMap(null,"","",true)=null
	 * parseKeyAndValueToMap("a:b,:","","",true)={(a,b)}
	 * parseKeyAndValueToMap("a:b,:d","","",true)={(a,b)}
	 * parseKeyAndValueToMap("a:b,c:d","","",true)={(a,b),(c,d)}
	 * parseKeyAndValueToMap("a=b, c = d","=",",",true)={(a,b),(c,d)}
	 * parseKeyAndValueToMap("a=b, c = d","=",",",false)={(a, b),( c , d)}
	 * parseKeyAndValueToMap("a=b, c=d","=", ",", false)={(a,b),( c,d)}
	 * parseKeyAndValueToMap("a=b; c=d","=", ";", false)={(a,b),( c,d)}
	 * parseKeyAndValueToMap("a=b, c=d", ",", ";", false)={(a=b, c=d)}
	 * </pre>
	 * @param source
	 * @param separator
	 * @param pairSeparator
	 * @param ignoreSpace
	 * @return Map<String,String>
	 */
	public static Map<String, String> parseToMap(String source,
			String separator, String pairSeparator, boolean ignoreSpace) {
		if (StringUtils.isEmpty(source))
			return null;

		if (StringUtils.isEmpty(separator))
			separator = DEFAULT_K_V_SEPARATOR;

		if (StringUtils.isEmpty(pairSeparator))
			pairSeparator = DEFAULT_K_V_PAIR_SEPARATOR;

		Map<String, String> keyAndValueMap = new HashMap<String, String>();
		String[] keyAndValueArray = source.split(pairSeparator);
		if (keyAndValueArray == null) {
			return null;
		}
		int seperator;
		for (String valueEntity : keyAndValueArray) {
			if (!StringUtils.isEmpty(valueEntity)) {
				seperator = valueEntity.indexOf(separator);
				if (seperator != -1) {
					if (ignoreSpace) {
						putMapNotEmptyKey(keyAndValueMap, valueEntity
								.substring(0, seperator).trim(), valueEntity
								.substring(seperator + 1).trim());
					} else {
						putMapNotEmptyKey(keyAndValueMap,
								valueEntity.substring(0, seperator),
								valueEntity.substring(seperator + 1));
					}
				}
			}
		}
		return keyAndValueMap;
	}
}
