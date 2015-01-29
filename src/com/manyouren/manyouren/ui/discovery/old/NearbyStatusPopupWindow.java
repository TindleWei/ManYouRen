/**
 * @Package com.manyouren.android.ui.discovery    
 * @Title: NearbyStatusPopupWindow.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-17 上午3:34:04 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.discovery.old;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.github.kevinsawicki.wishlist.Toaster;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.baseold.BasePopupWindow;
import com.manyouren.manyouren.util.ScreenUtils;

/**
 * 
 * @author firefist_wei
 * @date 2014-7-17 上午3:34:04
 * 
 */
public class NearbyStatusPopupWindow extends BasePopupWindow {

	private GridView gridView;

	public NearbyStatusPopupWindow(Context context,
			int width, int height) {
		super(LayoutInflater.from(context).inflate(
				R.layout.popupwindow_nearby_status, null), width, height);
		setAnimationStyle(R.style.Popup_Animation_Alpha);

	}


	@Override
	public void initViews() {
		gridView = (GridView) findViewById(R.id.gv_status);
		gridView.setAdapter(new StatusGridViewAdapter(mContentView.getContext()));

	}

	@Override
	public void initEvents() {
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				mOnStatusPopupItemClickListener.onStatusItemClick(position);
				dismiss();
				//Toast.makeText(mContentView.getContext(), position+"",1000).show();
			}		
		});
	}

	@Override
	public void init() {

	}

	private onStatusPopupItemClickListener mOnStatusPopupItemClickListener;
	
	public void setOnStatusPopupItemClickListener(
			onStatusPopupItemClickListener listener) {
		mOnStatusPopupItemClickListener = listener;
	}

	public interface onStatusPopupItemClickListener {
		void onStatusItemClick(int position);
	}

	public class StatusGridViewAdapter extends BaseAdapter {

		private Context context;

		private int size = 0;

		public StatusGridViewAdapter(Context context) {
			this.context = context;
		}

		public StatusGridViewAdapter(Context context, int size) {
			this.context = context;
			this.size = size;

		}

		@Override
		public int getCount() {
			if (size != 0) {
				return size;
			}

			return mPhotoIds.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = convertView;
			Holder holder = null;

			if (view == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				view = inflater.inflate(R.layout.gridview_status_item, null);

				// @date: 6/24
				// to solve gridview height problem
				// http://stackoverflow.com/questions/16819135/set-fixed-gridview-row-height
//				int rowHigh = ScreenUtils.getIntPixels(context, 100);
//				view.setLayoutParams(new GridView.LayoutParams(
//						GridView.AUTO_FIT, rowHigh));

				holder = new Holder();
				holder.image = (ImageView) view.findViewById(R.id.iv_photo);
				holder.text = (TextView) view.findViewById(R.id.tv_tag);
				view.setTag(holder);
			} else {
				holder = (Holder) view.getTag();
			}

			holder.image.setImageDrawable(context.getResources().getDrawable(
					mPhotoIds[position]));
			holder.text.setText(mTags[position]);

			return view;
		}

		class Holder {
			ImageView image;
			TextView text;
		}

		public Integer[] mPhotoIds = { R.drawable.icon_nearby_none,
				R.drawable.icon_nearby_food, R.drawable.icon_nearby_photo, 
				R.drawable.icon_nearby_wine, R.drawable.icon_nearby_shop, 
				R.drawable.icon_nearby_music, R.drawable.icon_nearby_vacation, 
				R.drawable.icon_nearby_bike, R.drawable.icon_nearby_travel};

		public String[] mTags = { "无状态", "美食&农家乐", "摄影&拍照", "酒吧&夜店",
				"咖啡&茶", "电影&话剧&音乐会", "温泉&度假", "自驾&骑车", "旅行&探险"};

	}
}
