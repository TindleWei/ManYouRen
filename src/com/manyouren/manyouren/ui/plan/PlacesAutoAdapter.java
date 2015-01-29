package com.manyouren.manyouren.ui.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.services.core.PoiItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.core.user.PoiListController;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

/**
 * 
 * @author firefist_wei
 * @date 2014-7-1 下午4:03:45
 * 
 */
public class PlacesAutoAdapter extends ScenicsAdapter implements
		Filterable {
	
	static List<ScenicsEntity> items = new ArrayList<ScenicsEntity>();
	
	public static int REASCH_FINISH = 1;
	private Context context = null;
	private String oldStr = "";
	private Filter filter;
	
	public PlacesAutoAdapter(Context context, final LayoutInflater inflater) {
		super( inflater, items);
		this.context = context;
	}

	@SuppressLint("HandlerLeak")
	@Override
	public Filter getFilter() {
		filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence desti) {
				FilterResults filterResults = new FilterResults();
				if (desti != null) {

					if (!oldStr.equals(desti.toString())) {
						fetchRequest(desti.toString());
					}
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results != null && results.count > 0)
					notifyDataSetChanged();
				else
					notifyDataSetInvalidated();
			}

			private void updateAdapter() {
				
				FilterResults filterResults = new FilterResults();
				// Assign the data to the FilterResults
				filterResults.values = items;
				filterResults.count = items.size();
				publishResults(oldStr, filterResults);
			}
		};
		return filter;
	}
	
	/**
	 * 这个方法是下拉框增加历史记录的
	 */
	public void setList(ArrayList<ScenicsEntity> list){
		items = list;
		notifyDataSetChanged();
	}
	
	public void fetchRequest(String pName) {

		String url = AsyncHttpManager.PLACE_SEARCH_URL;

		RequestParams params = new RequestParams();
		params.put("pName", pName);

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						items = new ArrayList<ScenicsEntity>();
					}

					@Override
					public void onEmpty() {
						super.onEmpty();
						items = new ArrayList<ScenicsEntity>();
						notifyDataSetChanged();
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();

						try {
							String json = new JSONObject(getResult())
									.getString("message");

							items = new Gson().fromJson(json,
									new TypeToken<List<ScenicsEntity>>() {
									}.getType());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						notifyDataSetChanged();
					}
				});

	}

}
