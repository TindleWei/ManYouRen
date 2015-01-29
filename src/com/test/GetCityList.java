package com.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.Query;
import com.google.gson.Gson;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.MapUtils;
import com.manyouren.manyouren.util.SDLogUtils;

public class GetCityList {

	public static List<PoiItem> savedList = new ArrayList<PoiItem>();
	
	static String Mycity ="";

	public static void getData(String city) {
		Mycity = city;
		for (int i = 1; i < 20; i++) {
			getPoiList(RootApplication.getInstance(), i, 100, city, city);
		}
	}

	public static void getPoiList(Context context, int page, int perpage,
			String city, String searchStr) {
		LatLonPoint lp = null;

		String deepType = "景点";
		Logot.outInfo("PoiListController", "searchStr:" + searchStr);
		final Query poiQuery = new PoiSearch.Query("", "风景名胜", city);

		poiQuery.setPageSize(perpage);// 设置每页最多返回多少条poiitem
		poiQuery.setPageNum(page);// 设置查第一页
		PoiSearch poiSearch = new PoiSearch(context, poiQuery);

		poiSearch.setOnPoiSearchListener(new OnPoiSearchListener() {

			@Override
			public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPoiSearched(PoiResult result, int rCode) {
				// TODO Auto-generated method stub
				if (rCode == 0 && result != null) {
					if (result.getQuery() != null) {// 搜索poi的结果
						if (result.getQuery().equals(poiQuery)) {// 是否是同一条
							PoiResult poiResult = result;
							savedList = poiResult.getPois();
							String json = "{\"savedList\":[";
							for (PoiItem p : savedList) {
								Logot.outError("PoiItem",p.toString());
								String cCode = p.getCityCode();
								String cName = p.getCityName();
								String pName = p.getTitle();
								LatLonPoint llp = p.getLatLonPoint();
								double lat = llp.getLatitude();
								double lon = llp.getLongitude();
								String adName = p.getAdName();
								String adCode = p.getAdCode();
								Map<String, String> map = new HashMap<String, String>();
								map.put("cCode", cCode);
								map.put("cName", cName);
								map.put("pName", pName);
								map.put("lat", lat+"");
								map.put("lon", lon+"");
								map.put("adName", adName);
								map.put("adCode", adCode);
								String item = MapUtils.toJson(map);
								json += item +",";
							}
							json += "]}";
//							Gson gson = new Gson();
//							String str = gson.toJson(savedList);
							Logot.outError("PoiItem",json);
							try {
								SDLogUtils.saveToSDCard("/CityLog3/",Mycity, json);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}
				}
			}

		});
		poiSearch.searchPOIAsyn();// 异步搜索

	}
}
