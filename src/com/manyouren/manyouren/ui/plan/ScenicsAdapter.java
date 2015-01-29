package com.manyouren.manyouren.ui.plan;

import java.util.List;

import android.view.LayoutInflater;

import com.amap.api.services.core.PoiItem;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.ui.AlternatingColorListAdapter;

public class ScenicsAdapter extends
		AlternatingColorListAdapter<ScenicsEntity> {

	
	public ScenicsAdapter(final LayoutInflater inflater,
			final List<ScenicsEntity> items) {
		super(R.layout.activity_select_place_item, inflater, items);
	}

	@Override
	protected int[] getChildViewIds() {
		return new int[] { R.id.scenic_name, R.id.scenic_address};
	}
	
	@Override
	protected void update(final int position, final ScenicsEntity item) {
		super.update(position, item);
		
		setText(0, item.getpName());
		
		setText(1, item.getAdName());
	}
}

class ScenicsEntity{
	
	String scenicId;
	String pName;
	String cName;
	String adName;
	String lon;
	String lat;
	
	public String getAdName() {
		return adName;
	}
	public void setAdName(String adName) {
		this.adName = adName;
	}
	public String getScenicId() {
		return scenicId;
	}
	public void setScenicId(String scenicId) {
		this.scenicId = scenicId;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
}
