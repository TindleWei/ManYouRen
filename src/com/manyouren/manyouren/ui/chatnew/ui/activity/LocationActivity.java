package com.manyouren.manyouren.ui.chatnew.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.github.kevinsawicki.wishlist.Toaster;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.service.location.AMapUtil;

public class LocationActivity extends BaseActivity implements
		OnGeocodeSearchListener, OnClickListener {
	
	public static final int MENE_DONE = 1001;
	private ProgressDialog progDialog = null;
	private GeocodeSearch geocoderSearch;
	private String addressName;
	private AMap aMap;
	private MapView mapView;
	private LatLonPoint latLonPoint = null;
	private Marker geoMarker;
	private Marker regeoMarker;
	
	EditText et_desti  =null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.geocoder_activity);
		
		initActionBar("地图");
		/*
		 * 设置离线地图存储目录，在下载离线地图或初始化地图设置; 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
		 * 则需要在离线地图下载和使用地图页面都进行路径设置
		 */
		// Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
		// MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
		
		/**
		 * 响应逆地理编码按钮
		 */
		latLonPoint = new LatLonPoint(Double.valueOf(PreferenceHelper.getLatitude()), 
				Double.valueOf(PreferenceHelper.getLongitude()));
		getAddress(latLonPoint);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem done = menu.add(0, MENE_DONE, 0, "DONE");
		done.setIcon(this.getResources().getDrawable(
				R.drawable.ic_action_accept));
		done.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		// This is the home button in the top left corner of the screen.
		case MENE_DONE:
			gotoChatPage();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void gotoChatPage() {
	    if (latLonPoint != null) {
	      Intent intent = new Intent();
	      intent.putExtra("y", latLonPoint.getLongitude());// 经度
	      intent.putExtra("x", latLonPoint.getLatitude());// 维度
	      intent.putExtra("address", addressName);
	      setResult(RESULT_OK, intent);
	      this.finish();
	    } else {
	    }
	  }

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
			regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
		}
		
		ImageView iv_search = (ImageView)findViewById(R.id.iv_search);
		et_desti = (EditText)findViewById(R.id.et_desti);
		iv_search.setOnClickListener(this);
		
		geocoderSearch = new GeocodeSearch(this);
		geocoderSearch.setOnGeocodeSearchListener(this);
		progDialog = new ProgressDialog(this);
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

	/**
	 * 显示进度条对话框
	 */
	public void showDialog() {
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		progDialog.setMessage("正在获取地址");
		progDialog.show();
	}

	/**
	 * 隐藏进度条对话框
	 */
	public void dismissDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 响应地理编码
	 */
	public void getLatlon(final String name) {
		showDialog();

		GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}

	/**
	 * 响应逆地理编码
	 */
	public void getAddress(final LatLonPoint latLonPoint) {
		showDialog();
		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}

	/**
	 * 地理编码查询回调
	 */
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getGeocodeAddressList() != null
					&& result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
				geoMarker.setPosition(AMapUtil.convertToLatLng(address
						.getLatLonPoint()));
				
				addressName = address.getFormatAddress();
				String toastStr = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
						+ address.getFormatAddress();
				Toaster.showShort(LocationActivity.this, toastStr);
			} else {
				Toaster.showShort(LocationActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			Toaster.showShort(LocationActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			Toaster.showShort(LocationActivity.this, R.string.error_key);
		} else {
			Toaster.showShort(LocationActivity.this,
					getString(R.string.error_other) + rCode);
		}
	}

	/**
	 * 逆地理编码回调
	 */
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		dismissDialog();
		if (rCode == 0) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
				addressName = result.getRegeocodeAddress().getFormatAddress()
						+ "附近";
				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						AMapUtil.convertToLatLng(latLonPoint), 15));
				regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
				Toaster.showShort(LocationActivity.this, addressName);
			} else {
				Toaster.showShort(LocationActivity.this, R.string.no_result);
			}
		} else if (rCode == 27) {
			Toaster.showShort(LocationActivity.this, R.string.error_network);
		} else if (rCode == 32) {
			Toaster.showShort(LocationActivity.this, R.string.error_key);
		} else {
			Toaster.showShort(LocationActivity.this,
					getString(R.string.error_other) + rCode);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/**
		 * 响应地理编码按钮
		 */
		case R.id.iv_search:
			getLatlon(et_desti.getText().toString());

			break;

		default:
			break;
		}

	}
}
