package com.manyouren.manyouren.core.user;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.util.ImageUtils;
import com.squareup.picasso.Picasso;

public class SmilyUserAdapter extends BaseAdapter {
//	ImageLoader imageLoade;
	ArrayList<String> pics;
	Context context;
	String type = "url";
	public SmilyUserAdapter(Context c,ArrayList<String> p){
		pics = p;
		context = c;
	}
	public SmilyUserAdapter(Context c,ArrayList<String> p,String type){
		pics = p;
		context = c;
		this.type = type;
	}
	public static class SHolder{
		ImageView userPortrait;
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return pics==null?0:pics.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return pics==null?null:pics.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return pics==null?0:position;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SHolder sHolder=null;
		if(convertView==null){
			convertView = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.gridview_smily_item, null);
			sHolder = new SHolder();
			sHolder.userPortrait = (ImageView)convertView.findViewById(R.id.userPortrait);
			convertView.setTag(sHolder);
		}else{
			sHolder = (SHolder)convertView.getTag();
		}
//		final String f = pics.get(position);
//		if(type.equals("url")){
//			if(!f.equals(""))
//				Picasso.with(context).load(f).resize(400, 400)
//					.centerCrop().placeholder(R.drawable.no_photo)
//					.into(sHolder.userPortrait);
//			else
//				sHolder.userPortrait.setImageResource(R.drawable.no_photo);
//		}else if(type.equals("filepath")){
//			if(!f.equals(""))
//				sHolder.userPortrait.setImageBitmap(ImageUtils.getBitmap(f, 300, 300));
//			else
//				sHolder.userPortrait.setImageResource(R.drawable.bg_photo_add_button);
//		}
		return convertView;
	}
}
