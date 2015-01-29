/**   
* @Title: AlbumListAdapter.java 
* @Package com.manyouren.android.ui.user 
* @Description: TODO(用一句话描述该文件做什么) 
* @author ssz 31807077_qq_com   
* @date 2014-7-14 下午12:53:11 
* @version V1.0   
*/
package com.manyouren.manyouren.core.user;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.manyouren.manyouren.R;

/** 
 * @包名: com.manyouren.android.core.user
 * @描述: TODO(这里用一句话描述这个类的作用) 
 * @作者 ssz 31807077_qq_com
 * @日期 2014-7-14 下午12:53:11 
 * @版本 V 1.0
 *  
 */
public class PoiListAdapter extends BaseAdapter{
	PoiHolder holder;
	Context context;
	List<PoiItem> poiList;
	public PoiListAdapter(Context c,List<PoiItem> items){
		context = c;
		this.poiList = items;
	}
	/* 
	* @see android.widget.Adapter#getCount() 
	*/
	public int getCount() {
		// TODO Auto-generated method stub
		return poiList==null?0:poiList.size();
	}

	/* 
	* @see android.widget.Adapter#getItem(int) 
	*/
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return poiList==null?null:poiList.get(position);
	}

	/* 
	* @see android.widget.Adapter#getItemId(int) 
	*/
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return poiList==null?0:position;
	}

	/* 
	* @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup) 
	*/
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			convertView = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.item_poilist, null);
			holder = new PoiHolder();
			holder.tv_poi_title = (TextView)convertView.findViewById(R.id.tv_poi_title);
			
			holder.tv_poi_distance = (TextView)convertView.findViewById(R.id.tv_poi_distance);
			holder.tv_poi_address = (TextView)convertView.findViewById(R.id.tv_poi_address);
			convertView.setTag(holder);
		}else{
			holder = (PoiHolder)convertView.getTag();
		}
		if(poiList!=null&&!poiList.isEmpty()){
			PoiItem a = poiList.get(position);
			
			holder.tv_poi_title.setText(a.getTitle());
//			holder.tv_poi_distance.setText(a.getDistance()+"米");
			holder.tv_poi_address.setText(a.getSnippet());
		}
		return convertView;
	}
	
	private static class PoiHolder{
		TextView tv_poi_title;
		TextView tv_poi_distance;
		TextView tv_poi_address;
	}
}
