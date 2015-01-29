/**
* @Package com.manyouren.android.service.location    
* @Title: LocationNetworkActivity.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-7-9 下午8:33:47 
* @version V1.0   
*/
package com.manyouren.manyouren.service.location;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.manyouren.manyouren.R;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-7-9 下午8:33:47 
 *  
 */
public class LocationNetworkActivity extends Activity implements 
		AMapLocationListener, Runnable  {
	
	private LocationManagerProxy aMapLocManager = null;
	private TextView myLocation;
	private AMapLocation aMapLocation;// 用于判断定位超时
	private Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationnetwork_activity);
		myLocation = (TextView) findViewById(R.id.myLocation);
		aMapLocManager = LocationManagerProxy.getInstance(this);
		
		aMapLocManager.requestLocationUpdates(
				LocationProviderProxy.AMapNetwork, 2000, 10, this);
		handler.postDelayed(this, 5000);// 设置超过12秒还没有定位到就停止定位
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		stopLocation();// 停止定位
	}
	
	/**
	 * 销毁定位
	 */
	private void stopLocation() {
		if (aMapLocManager != null) {
			aMapLocManager.removeUpdates(this);
			aMapLocManager.destory();
		}
		aMapLocManager = null;
	}

	/**
	 * 此方法已经废弃
	 */
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}
	
	/**
	 * 混合定位回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			this.aMapLocation = location;// 判断超时机制
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
					+ AMapUtil.convertToTime(location.getTime()) + "\n城市编码:"
					+ cityCode + "\n位置描述:" + desc + "\n省:"
					+ location.getProvince() + "\n市:" + location.getCity()
					+ "\n区(县):" + location.getDistrict() + "\n区域编码:" + location
					.getAdCode());
			myLocation.setText(str);
		}
	}
	
	@Override
	public void run() {
		if (aMapLocation == null) {
			myLocation.setText("5秒内还没有定位成功，停止定位");
			stopLocation();// 销毁掉定位
		}
	}

}
