/**
 * @Package com.manyouren.android.controller    
 * @Title: PlanController.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-20 下午3:56:28 
 * @version V1.0   
 */
package com.manyouren.manyouren.core.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Handler;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.Query;
import com.amap.api.services.poisearch.Scenic;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.util.Logot;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-20 下午3:56:28
 * 
 */
public class PoiListController {
	public static List<String> suggestionList = new ArrayList<String>();
	public static List<PoiItem> savedList = new ArrayList<PoiItem>();
	public static String errorMessage = "未知错误!";
	
	public static void getPoiList(Context context,int page,int perpage,String city,
			String searchStr,final Handler handler,final int requestCode) {
		LatLonPoint lp = null;

		//String deepType = "景点";
		Logot.outInfo("PoiListController","searchStr:"+searchStr);
		final Query poiQuery = new PoiSearch.Query(searchStr, "风景名胜", city);
		
		poiQuery.setPageSize(perpage);// 设置每页最多返回多少条poiitem
		poiQuery.setPageNum(page);// 设置查第一页
		PoiSearch poiSearch = new PoiSearch(context, poiQuery);
		
		poiSearch.setOnPoiSearchListener(new OnPoiSearchListener(){
			
			@Override
			public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPoiSearched(PoiResult result, int rCode) {
				// TODO Auto-generated method stub
				if (rCode == 0 && result != null ) {
					if (result.getQuery() != null) {// 搜索poi的结果
						if (result.getQuery().equals(poiQuery)) {// 是否是同一条
							PoiResult poiResult = result;
							savedList = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
							Logot.outInfo("PoiListController","poilistsize:"+savedList.size());
							handler.sendEmptyMessage(requestCode);
						}
					}else{
						suggestionList = result.getSearchSuggestionKeywords();
						handler.sendEmptyMessage(0);
						errorMessage = "没有找到景点";
						Logot.outInfo("PoiListController","error:"+errorMessage);
					}
				} 
			}
			
		});
		
//		poiSearch.setBound(new SearchBound(lp, 2000, true));//
		poiSearch.searchPOIAsyn();// 异步搜索
		
	}

	public static List<PoiItem> getSavedList() {
		return savedList;
	}
}
