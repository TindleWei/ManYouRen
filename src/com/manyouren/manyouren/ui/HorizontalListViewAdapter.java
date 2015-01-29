/**
* @Package com.manyouren.android.ui    
* @Title: HorizontalListViewAdapter.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-7-23 上午9:33:35 
* @version V1.0   
*/
package com.manyouren.manyouren.ui;

import java.util.List;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.util.ImageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-7-23 上午9:33:35 
 *  
 */
public class HorizontalListViewAdapter extends BaseAdapter{
	private int[] mIconIDs;
	private List<String> mTitles;
	private Context mContext;
	private LayoutInflater mInflater;
	Bitmap iconBitmap;
	private int selectIndex = -1;
	
	public HorizontalListViewAdapter(Context context, List<String> titles, int[] ids){
		this.mContext = context;
		this.mIconIDs = ids;
		this.mTitles = titles;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mIconIDs.length;
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

		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.horizontal_list_item, null);
			holder.mImage=(ImageView)convertView.findViewById(R.id.img_list_item);
			holder.mTitle=(TextView)convertView.findViewById(R.id.text_list_item);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		if(position == selectIndex){
			convertView.setSelected(true);
		}else{
			convertView.setSelected(false);
		}
		
		holder.mTitle.setText(mTitles.get(position));
		iconBitmap = getPropThumnail(mIconIDs[position]);
		holder.mImage.setImageBitmap(iconBitmap);

		return convertView;
	}

	private static class ViewHolder {
		private TextView mTitle ;
		private ImageView mImage;
	}
	private Bitmap getPropThumnail(int id){
		Drawable d = mContext.getResources().getDrawable(id);
		Bitmap b = ImageUtils.drawableToBitmap(d);
//		Bitmap bb = BitmapUtil.getRoundedCornerBitmap(b, 100);
		int w = mContext.getResources().getDimensionPixelOffset(R.dimen.thumnail_default_width);
		int h = mContext.getResources().getDimensionPixelSize(R.dimen.thumnail_default_height);
		
		Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(b, w, h);
		
		return thumBitmap;
	}
	public void setSelectIndex(int i){
		selectIndex = i;
	}
}