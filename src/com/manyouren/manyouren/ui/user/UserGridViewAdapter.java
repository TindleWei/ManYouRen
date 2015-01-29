/**
 * @Package com.manyouren.android.ui.user    
 * @Title: UserGridViewAdapter.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-23 下午11:05:35 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.user;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.util.ScreenUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-23 下午11:05:35
 * 
 */
public class UserGridViewAdapter extends BaseAdapter {

	private Context context;

	private int size = 0;
	
	public Integer[] mPhotoIds = { R.drawable.ic_simple_user,
			R.drawable.ic_simple_plan, R.drawable.ic_simple_place,
			R.drawable.ic_simple_settings2 };

	public String[] mTags = { "好  友", "计 划", "相 册", "设 置" };

	public UserGridViewAdapter(Context context) {
		this.context = context;
	}
	
	public UserGridViewAdapter(Context context, String[] mTags) {
		this.context = context;
		this.mTags = mTags;
	}

	/** 
	* 这个其他用户的界面， size = 3
	*
	* @param context
	* @param size 
	*/
	public UserGridViewAdapter(Context context, int size) {
		this.context = context;
		this.size = size;
		mTags[0] = "追随者";

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
			view = inflater.inflate(R.layout.gridview_user_item2, null);

			// @date: 6/24
			// to solve gridview height problem
			// http://stackoverflow.com/questions/16819135/set-fixed-gridview-row-height
			int rowHigh = ScreenUtils.getIntPixels(context, 100);
			view.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT,
					rowHigh));

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

	static class Holder {
		ImageView image;
		TextView text;
	}

	// public Integer[] mPhotoIds = { R.drawable.icon_user_friend,
	// R.drawable.icon_user_plan, R.drawable.icon_user_photo,
	// R.drawable.icon_user_settings };

	

}
