package com.manyouren.manyouren.ui.discovery;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.ui.AlternatingColorListAdapter;

@SuppressLint("NewApi")
public class NearbyStatusAdapter extends
		AlternatingColorListAdapter<StatusItem> {

	/**
	 * @param inflater
	 * @param items
	 */
	public NearbyStatusAdapter(final LayoutInflater inflater,
			final List<StatusItem> items) {
		super(R.layout.list_items_status, inflater, items);
	}

	@Override
	protected int[] getChildViewIds() {
		return new int[] { R.id.tv_status, R.id.iv_select };
	}

	@Override
	protected void update(int position, StatusItem item) {
		super.update(position, item);
		setText(0, item.getStatus());
		imageView(1).setVisibility(
				item.getSelected() == true ? View.VISIBLE : View.INVISIBLE);
	}
}

class StatusItem {
	String status;
	boolean selected;

	public StatusItem(String status, boolean selected) {
		this.status = status;
		this.selected = selected;
	}

	public String getStatus() {
		return status;
	}

	public boolean getSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
	}
}