package com.manyouren.manyouren.ui.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.config.HttpConfig;
import com.manyouren.manyouren.core.user.Album;
import com.manyouren.manyouren.core.user.PicAdapter;
import com.manyouren.manyouren.ui.plan.GalleryUrlActivity;
import com.manyouren.manyouren.util.Logot;

public class AlbumListAdapter extends BaseAdapter {
	private static final List<Album> EMPTY = new ArrayList<Album>();
	AlbumHolder holder;
	Context context;
	List<Album> albumList;
	// String[] months = { "Jan", "Feb", "", "Mar", "Apr", "May", "Jun", "Jul",
	// "Aug", "Sept", "Oct", "Nov", "Dec" };
	String[] months = { "一月", "二月", "", "三月", "四月", "五月", "六月", "七月", "八月",
			"九月", "十月", "十一月", "十二月" };
	ArrayList<ArrayList<String>> p = new ArrayList<ArrayList<String>>();

	public AlbumListAdapter(Context c, List<Album> items) {
		context = c;
		this.albumList = items;
	}

	public int getCount() {
		return albumList == null ? 0 : albumList.size();
	}

	public Object getItem(int position) {
		return albumList == null ? null : albumList.get(position);
	}

	public long getItemId(int position) {
		return albumList == null ? 0 : position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context.getApplicationContext())
					.inflate(R.layout.item_albumlist, null);
			holder = new AlbumHolder();
			holder.label_year = (TextView) convertView
					.findViewById(R.id.tv_year);

			holder.label_month = (TextView) convertView
					.findViewById(R.id.tv_month);
			holder.label_day = (TextView) convertView.findViewById(R.id.tv_day);
			holder.img_albumType = (ImageView) convertView
					.findViewById(R.id.iv_albumtype);
			holder.label_location = (TextView) convertView
					.findViewById(R.id.tv_location);
			holder.label_text = (TextView) convertView
					.findViewById(R.id.tv_description);
			holder.picgrid = (ListItemGridView) convertView
					.findViewById(R.id.picgrid);
			holder.rb_rating = (RatingBar) convertView
					.findViewById(R.id.rb_rating);
			convertView.setTag(holder);
		} else {
			holder = (AlbumHolder) convertView.getTag();
		}
		holder.picgrid.setVisibility(View.GONE);
		String[] picStr = null;
		if (albumList != null && !albumList.isEmpty()) {
			Album a = albumList.get(position);
			// Calendar calendar = Calendar.getInstance();
			// calendar.setTimeInMillis(a.getDateline()*1000);

			// dateline 是long型的秒数
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = sdf.format(new Date(Long.valueOf(a
					.getDateline()) * 1000));

			String year = dateString.substring(0, 4);
			int month = Integer.parseInt(dateString.substring(5, 7));
			String day = dateString.substring(8, 10);

			holder.label_year.setText(year);
			holder.label_month.setText(months[month]);
			holder.label_day.setText(day + "日");
			holder.label_location.setText(a.getAlbumcity() + " "
					+ a.getLocation());
			holder.label_text.setText(a.getContent());

			Logot.outError("TAG", a.getUserId() + "");
			Logot.outError("TAG", a.getRating() + "");
			if (a.getRating() > 0)
				holder.rb_rating.setRating(a.getRating());

			String pics = a.getPics();
			final ArrayList<String> tp = new ArrayList<String>();
			final ArrayList<String> p = new ArrayList<String>();
			int picsize = 0;
			String picResource = a.getPicResource();
			if (!pics.equals("[]")) {
				try {
					JSONObject picObj = new JSONObject(pics);
					// JSONArray thumbs = picObj.getJSONArray("thumb");
					JSONArray thumbs = picObj.getJSONArray("origin");
					JSONArray original = picObj.getJSONArray("origin");
					picsize = thumbs.length();
					picStr = new String[picsize];
					for (int i = 0; i < picsize; i++) {
						tp.add((picResource.equals("url") ? HttpConfig.UPLOADS_PREFIX
								: "")
								+ thumbs.getString(i));
						p.add((picResource.equals("url") ? HttpConfig.UPLOADS_PREFIX
								: "")
								+ original.getString(i));
						picStr[i] = HttpConfig.UPLOADS_PREFIX
								+ original.getString(i);
					}
				} catch (Exception e) {
					Logot.outError("AlbumListAdapter", "json error:" + e.toString());
				}
			}

			PicAdapter picAdapter = null;
			if (!p.isEmpty()) {
				picAdapter = new PicAdapter(context, tp, picResource);
			}

			switch (picsize) {
			case 1:
				holder.picgrid.setNumColumns(1);
				break;
			case 2:
				holder.picgrid.setNumColumns(2);
				break;
			case 4:
				holder.picgrid.setNumColumns(2);
				break;
			default:
				holder.picgrid.setNumColumns(3);
				break;
			}

			final String[] picS = picStr;
			if (picAdapter != null) {
				holder.picgrid.setAdapter(picAdapter);
				holder.picgrid.setVisibility(View.VISIBLE);
				holder.picgrid
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								String pic = p.get(position);
								Log.i("AlbumListAdapter", "picurl:" + pic);
								context.startActivity(new Intent(context,
										GalleryUrlActivity.class)
										.putExtra(
												GalleryUrlActivity.GALLERY_URLS,
												picS)
										.putExtra(
												GalleryUrlActivity.GALLERY_INDEX,
												position));
							}
						});
			}
		}
		return convertView;
	}

	private static class AlbumHolder {
		TextView label_year;
		TextView label_month;
		TextView label_day;
		ImageView img_albumType;
		TextView label_location;
		TextView label_text;
		ListItemGridView picgrid;
		RatingBar rb_rating;
	}

	public void setItems(List<Album> items) {
		if (items != null)
			this.albumList = items;
		else
			this.albumList = EMPTY;
		notifyDataSetChanged();
	}
}
