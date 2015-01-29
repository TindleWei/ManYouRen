/**   
* @Title: FriendListActivity.java 
* @Package com.manyouren.android.account 
* @Description: TODO(用一句话描述该文件做什么) 
* @author ssz 31807077_qq_com   
* @date 2014-7-3 上午11:45:47 
* @version V1.0   
*/
package com.manyouren.manyouren.ui.user;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.core.user.PoiListAdapter;
import com.manyouren.manyouren.core.user.PoiListController;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.widget.RefreshListView;

/** 
 * @包名: com.manyouren.android.account
 * @描述: TODO(景点列表) 
 * @作者 ssz 31807077@qq.com
 * @日期 2014-7-3 上午11:45:47 
 * @版本 V 1.0
 *  
 */
public class PoiListActivity extends BaseActivity {
	
	Context context;
	
	private RefreshListView poiList;
	ProgressBar pb_loading;
	EditText searchValue;
	
	PoiListAdapter poiListAdapter;
	MyHandler handler;
	
	List<PoiItem> pois = new ArrayList<PoiItem>();
	
	private static final int EVENT_POI_ADD = 72001;
	private static final int EVENT_POI_CITY = 72003;
	
	int page=0;
	int perpage = 20;
	String city = Constants.PLACE_NOW;
	String searchStr = "";
	int requestCode = 0;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_poilist);
		
		context = this;
		setActionBar("选择地点");
		
		handler = new MyHandler(PoiListActivity.this);
		
		initView();
	}
	
	@Override
	protected void initView() {
		
		poiList = (RefreshListView)findViewById(R.id.lst_poi);
		pb_loading = (ProgressBar)findViewById(R.id.pb_loading);
		searchValue = (EditText)findViewById(R.id.searchValue);
		
		poiListAdapter = new PoiListAdapter(context,pois);
		
		poiList.setAdapter(poiListAdapter);
		
		
		PoiListController.getPoiList(context, page, perpage,city,searchStr,handler,requestCode);
		
		poiList.setOnBottomListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				page ++ ;
				requestCode ++ ;
				PoiListController.getPoiList(context, page, perpage,city,searchStr,handler,requestCode);
			}
			
		});
		
		poiList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				try{
					PoiItem poi = pois.get(position);
					LatLonPoint p = poi.getLatLonPoint();
					Intent intent = new Intent();
					intent.setClass(PoiListActivity.this, AlbumAddActivity.class);
					Logot.outError("PoiList","poi:"+p.getLongitude()+";"+p.getLatitude());
					intent.putExtra("lng", p.getLongitude());
					intent.putExtra("lat", p.getLatitude());
					intent.putExtra("city", city);
					intent.putExtra("guid", poi.getPoiId());
					intent.putExtra("name", poi.getTitle());
					intent.putExtra("addr", poi.getSnippet());
					//intent.putExtra("lng", selectedPoi.get("lng").toString());
					setResult(RESULT_OK,intent);
					finish();
				}catch(Exception e){
					Logot.outError("PoiList","select poi error:"+e.toString());
				}
			}
			
		});
		
		searchValue.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(searchValue.getText().toString().trim() != searchStr){
					requestCode ++ ;
					page = 0;
					searchStr = searchValue.getText().toString().trim();
					Log.i("PoiListActivity","searchStr:"+searchStr);
					pois = new ArrayList<PoiItem>();
					poiListAdapter = new PoiListAdapter(context,pois);
					poiListAdapter.notifyDataSetChanged();
					poiList.setAdapter(poiListAdapter);
					pb_loading.setVisibility(View.VISIBLE);
					handler.removeCallbacksAndMessages(null);
					PoiListController.getPoiList(context, page, perpage,city,searchStr,handler,requestCode);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}
			
		});
	}
	
	static class MyHandler extends  Handler{
		WeakReference<PoiListActivity> mActivity;
		MyHandler(PoiListActivity activity){
			 mActivity = new WeakReference<PoiListActivity>(activity);
		 }
		public void handleMessage(Message msg) {
			final PoiListActivity activity = mActivity.get();
			activity.pb_loading.setVisibility(View.GONE);
			activity.poiList.onBottomComplete();
			if(msg.what == activity.requestCode){
				List<PoiItem> tmpPoiList = PoiListController.savedList;
				if(activity.pois == null)
					activity.pois = new ArrayList<PoiItem>();
				activity.pois.addAll(tmpPoiList);
				activity.poiListAdapter.notifyDataSetChanged();
			}else{
				//没有找到POI点
			}
		}
	};
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*MenuItem cityMenu = menu.add(0, EVENT_POI_CITY, 0, "城市");
		cityMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}
}
