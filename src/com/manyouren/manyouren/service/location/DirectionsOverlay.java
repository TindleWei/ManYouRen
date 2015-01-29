package com.manyouren.manyouren.service.location;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.manyouren.manyouren.R;

public class DirectionsOverlay extends DrivingRouteOverlay implements
		OnMarkerClickListener {

	private BitmapDescriptor startBitmap;
	private BitmapDescriptor endBitmap;
	private BitmapDescriptor driverBitmap;
	private Context context;
	public DirectionsOverlay(Context context, AMap arg1, DrivePath arg2,
			LatLonPoint arg3, LatLonPoint arg4) {
		super(context, arg1, arg2, arg3, arg4);
		this.context = context;
		new BitmapDescriptorFactory();
	}

	/*public void setStartBitmap(BitmapDescriptor startBitmap) {
		this.startBitmap = startBitmap;
	}

	public void setEndBitmap(BitmapDescriptor endBitmap) {
		this.endBitmap = endBitmap;
	}*/

	protected BitmapDescriptor getDriveBitmapDescriptor() {

		super.getStartBitmapDescriptor();
		driverBitmap = BitmapDescriptorFactory.fromResource(R.drawable.custom_tab_indicator_divider);
		return driverBitmap;
	}


	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
