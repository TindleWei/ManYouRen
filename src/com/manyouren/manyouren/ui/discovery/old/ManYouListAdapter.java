package com.manyouren.manyouren.ui.discovery.old;

import java.util.ArrayList;
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
import com.manyouren.manyouren.ui.CircleTransform;
import com.manyouren.manyouren.ui.plan.GalleryUrlActivity;
import com.manyouren.manyouren.ui.user.ListItemGridView;
import com.manyouren.manyouren.util.DateUtils;
import com.manyouren.manyouren.util.Logot;
import com.squareup.picasso.Picasso;

public class ManYouListAdapter extends BaseAdapter {
	private static final List<Album> EMPTY = new ArrayList<Album>();
	ManYouHolder holder;
	Context context;
	List<Album> albumList;
	ArrayList<ArrayList<String>> p = new ArrayList<ArrayList<String>>();

	public ManYouListAdapter(Context c, List<Album> items) {
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
					.inflate(R.layout.item_manyoulist, null);
			holder = new ManYouHolder();

			holder.iv_portrait = (ImageView) convertView
					.findViewById(R.id.manyoulist_item_photo_image);
			holder.tv_nickname = (TextView) convertView
					.findViewById(R.id.tv_nickname);
			holder.tv_description = (TextView) convertView
					.findViewById(R.id.tv_description);

			holder.tv_ratingtype = (TextView) convertView
					.findViewById(R.id.tv_ratingtype);
			holder.rb_rating = (RatingBar) convertView
					.findViewById(R.id.rb_rating);

			holder.tv_pubtime = (TextView) convertView
					.findViewById(R.id.tv_pubtime);

			holder.tv_location = (TextView) convertView
					.findViewById(R.id.tv_location);

			holder.lig_picgrid = (ListItemGridView) convertView
					.findViewById(R.id.lig_picgrid);

			convertView.setTag(holder);
		} else {
			holder = (ManYouHolder) convertView.getTag();
		}
		String[] picStr = null;
		if (albumList != null && !albumList.isEmpty()) {
			Album a = albumList.get(position);

			Picasso.with(context).load(a.getSmallAvatar())
					.transform(new CircleTransform())
					.placeholder(R.drawable.gravatar_icon)
					.into(holder.iv_portrait);
			if (a.getUsername() == null || a.getUsername().equals(""))
				holder.tv_nickname.setText(String.valueOf(a.getUserId()));
			else
				holder.tv_nickname.setText(a.getUsername());
			holder.tv_description.setText(a.getContent());

			try{
				holder.rb_rating.setRating(a.getRating());
			}catch(Exception e){
				// i don't know why, occur a bug here
			}
			
			holder.tv_location
					.setText(a.getAlbumcity() + "." + a.getLocation());

			holder.tv_pubtime.setText(DateUtils.getCreateTime(Long
					.valueOf(a.getDateline())));

			String pics = a.getPics();
			final ArrayList<String> tp = new ArrayList<String>();
			final ArrayList<String> p = new ArrayList<String>();
			int picsize = 0;
			if (!pics.equals("[]")) {
				try {
					Logot.outError("AlbumListAdapter", "json :" + pics.toString());
					JSONObject picObj = new JSONObject(pics);
					// JSONArray thumbs = picObj.getJSONArray("thumb");
					JSONArray thumbs = picObj.getJSONArray("origin");
					JSONArray original = picObj.getJSONArray("origin");
					picsize = thumbs.length();
					picStr = new String[picsize];
					for (int i = 0; i < picsize; i++) {
						tp.add(HttpConfig.UPLOADS_PREFIX + thumbs.getString(i));
						p.add(HttpConfig.UPLOADS_PREFIX + original.getString(i));
						picStr[i] = HttpConfig.UPLOADS_PREFIX
								+ original.getString(i);
					}
				} catch (Exception e) {
				}
			}

			PicAdapter picAdapter = null;
			if (!p.isEmpty()) {
				picAdapter = new PicAdapter(context, tp);
			}

			holder.lig_picgrid.setNumColumns(1);
			
			final String[] picS = picStr;
			if (picAdapter != null) {
				holder.lig_picgrid.setAdapter(picAdapter);
				holder.lig_picgrid.setVisibility(View.VISIBLE);
				holder.lig_picgrid
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

	private static class ManYouHolder {
		ImageView iv_portrait;
		TextView tv_nickname;
		TextView tv_description;
		TextView tv_ratingtype;
		RatingBar rb_rating;
		TextView tv_location;
		TextView tv_pubtime;
		ListItemGridView lig_picgrid;
	}

	public void setItems(List<Album> items) {
		if (items != null)
			this.albumList = items;
		else
			this.albumList = EMPTY;
		notifyDataSetChanged();
	}
}

