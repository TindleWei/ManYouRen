/**
 * @Package com.manyouren.android.service.location    
 * @Title: LocationSourceActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-9 下午8:11:30 
 * @version V1.0   
 */
package com.manyouren.manyouren.service.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkRouteResult;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.HomeActivity;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.config.Constants;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-7-9 下午8:11:30
 * 
 */
@SuppressLint("ShowToast")
public class LocationSourceActivity extends BaseActivity implements LocationSource,
		AMapLocationListener {

	private AMap aMap;
	private MapView mapView;
	private UiSettings mUiSettings;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private LatLonPoint endLatLonPoint;
	private LatLonPoint myLatLonPoint;
	private int mode = RouteSearch.DrivingDefault;
	private DriveRouteResult routeResult;
	private RouteSearch routeSearch;
	public static final int ROUTE_SEARCH_RESULT = 2002;
	public static final int ROUTE_SEARCH_ERROR = 2004;
	public DirectionsOverlay directionsOverlay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locationsource_activity);

		setActionBar("漫游地图");
		
		mapView = (MapView) findViewById(R.id.map);
		String lat = getIntent().getStringExtra("lat");
		String lon = getIntent().getStringExtra("lon");
		if(lat!=null && lon!=null){
		endLatLonPoint = new LatLonPoint(Double.valueOf(lat),
				Double.valueOf(lon));
		}
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.right_in, R.anim.right_out);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * 初始化
	 */
	@Override
	protected void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
			mUiSettings = aMap.getUiSettings();
			mUiSettings.setScaleControlsEnabled(true);
			mUiSettings.setCompassEnabled(true);
			routeSearch = new RouteSearch(this);
		}
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(0.1f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setMyLocationRotateAngle(180);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLocationChanged(Location location) {
		/**
		 * 此方法已经废弃 参数改为AMapLocation
		 */
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

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
			float bearing = aMap.getCameraPosition().bearing;
			myLatLonPoint = new LatLonPoint(aLocation.getLatitude(),
					aLocation.getLongitude());
			aMap.setMyLocationRotateAngle(bearing);// 设置小蓝点旋转角度
			searchRouteResult(myLatLonPoint, endLatLonPoint);
			deactivate();
		}

	}

	public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
		// progDialog = ProgressDialog.show(MainActivity.this, null,
		// "正在获取线路....",
		// true, true);
		if(endLatLonPoint ==null || startPoint==null){
			return;
		}
		RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint,
				endPoint);
		final DriveRouteQuery s = new DriveRouteQuery(fromAndTo, mode, null,
				null, "");
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					routeResult = routeSearch.calculateDriveRoute(s);
					routeHandler.sendMessage(Message.obtain(routeHandler,
							ROUTE_SEARCH_RESULT));
				} catch (AMapException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	private Handler routeHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == ROUTE_SEARCH_RESULT) {
				if (routeResult != null && routeResult.getPaths().size() > 0) {
					onDriveRouteSearched(0);
				}
			}
		}
	};

	/**
	 * 驾车结果回调
	 */
	public void onDriveRouteSearched(int rCode) {
		if (rCode == 0) {
			if (routeResult != null && routeResult.getPaths() != null
					&& routeResult.getPaths().size() > 0) {
				DrivePath drivePath = routeResult.getPaths().get(0);
				aMap.clear();// 清理地图上的所有覆盖物
				DirectionsOverlay drivingRouteOverlay = new DirectionsOverlay(
						this, aMap, drivePath, myLatLonPoint, endLatLonPoint);
				drivingRouteOverlay.removeFromMap();
				drivingRouteOverlay.addToMap();
				drivingRouteOverlay.zoomToSpan();
			}
		} else if (rCode == 27) {
			Toast.makeText(this, "网络错误", 1).show();
		} else if (rCode == 32) {
			Toast.makeText(this, "key错误", 2).show();
		} else {
			Toast.makeText(this, "其它错误", 3).show();
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}

}
