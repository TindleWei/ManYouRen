/**
 * @Package com.manyouren.android.service.location    
 * @Title: MyLocation.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-9 下午9:31:09 
 * @version V1.0   
 */
package com.manyouren.manyouren.service.location;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.PreferConfig;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.PreferenceUtils;

/**
 * 高德定位
 * 
 * @author firefist_wei
 * @date 2014-7-9 下午9:31:09
 * 
 */
public class MyLocation {

	private static final int LOC_DEFAULT = 0;
	private static final int LOC_PLAN = 1;
	private static final int LOC_NEARBY = 2;
	private static final int LOC_CITY = 3;

	private static int mType = 0;
	private static Handler mHandler = null;

	private static LocationManagerProxy aMapLocManager = null;

	private static AMapLocation aMapLocation;// 用于判断定位超时

	private static Handler handler = new Handler();

	public static void getLocation(Context context, int type) {
		getLocation(context, type, null);
	}

	public static void getLocation(Context context, int type, Handler myHandler) {
		mType = type;
		mHandler = myHandler;
		
		if(AsyncHttpManager.checkNetwork(context)==false){
			if(myHandler!=null)
			myHandler.sendEmptyMessage(-1);
			return;
		}

		aMapLocManager = LocationManagerProxy.getInstance(context);
		aMapLocManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 500, 10,
				aMapLocationListener);

		handler.postDelayed(runnable, 10000);// 设置超过10秒还没有定位到就停止定位

	}

	static AMapLocationListener aMapLocationListener = new AMapLocationListener() {

		@Override
		public void onLocationChanged(AMapLocation location) {
			if (location != null) {
				aMapLocation = location;// 判断超时机制
				Double geoLat = location.getLatitude();
				Double geoLng = location.getLongitude();
				String cityCode = "";
				String desc = "";
				Bundle locBundle = location.getExtras();
				if (locBundle != null) {
					cityCode = locBundle.getString("citycode");
					desc = locBundle.getString("desc");
				}
				String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
						+ "\n精    度    :" + location.getAccuracy() + "米"
						+ "\n定位方式:" + location.getProvider() + "\n定位时间:"
						+ AMapUtil.convertToTime(location.getTime())
						+ "\n城市编码:" + cityCode + "\n位置描述:" + desc + "\n省:"
						+ location.getProvince() + "\n市:" + location.getCity()
						+ "\n区(县):" + location.getDistrict() + "\n区域编码:" + location
						.getAdCode());

				Constants.LATITUDE = String.valueOf(geoLat);
				Constants.LONGITUDE = String.valueOf(geoLng);

				String city = location.getCity();
				if (city.endsWith("市")) {
					city = city.substring(0, city.length() - 1);
					Logot.outInfo("TAG", city);
				}
				Logot.outError("TAG", str);

				Constants.PLACE_NOW = city;

				PreferenceUtils.putString(RootApplication.getInstance(),
						PreferConfig.PREFER_CURRENT_CITY, city);
				PreferenceUtils.putString(RootApplication.getInstance(),
						PreferConfig.PREFER_INITIAL_LATITUDE,
						Constants.LATITUDE);
				PreferenceUtils.putString(RootApplication.getInstance(),
						PreferConfig.PREFER_INITIAL_LONGITUDE,
						Constants.LONGITUDE);

				stopLocation();

				switch (mType) {
				case 1:
					if (mHandler != null)
						mHandler.sendEmptyMessage(1);
					break;
				case 2:
					if (mHandler != null)
						mHandler.sendEmptyMessage(1);
					break;
				case 3:
					if (mHandler != null)
						mHandler.sendEmptyMessage(1);
					break;
				}
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onLocationChanged(Location location) {
		}

	};

	static Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (aMapLocation == null) {
				stopLocation();// 销毁掉定位
				switch (mType) {
				case 1:
					if (mHandler != null)
						mHandler.sendEmptyMessage(-1);
					break;
				case 2:
					if (mHandler != null)
						mHandler.sendEmptyMessage(-1);
					break;
				case 3:
					if (mHandler != null)
						mHandler.sendEmptyMessage(-1);
					break;
				}
			}
		}
	};

	/**
	 * 销毁定位
	 */
	public static void stopLocation() {
		if (aMapLocManager != null) {
			aMapLocManager.removeUpdates(aMapLocationListener);
			aMapLocManager.destory();
		}
		aMapLocManager = null;
	}

}
