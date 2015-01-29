package com.manyouren.manyouren.core.user;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.util.ImageUtils;
import com.manyouren.manyouren.util.Logot;
import com.squareup.picasso.Picasso;

public class PicAdapter extends BaseAdapter {
	ArrayList<String> pics;
	Context context;
	PicAdapter instance;
	String type = "url";
	boolean delEnable = false;
	public PicAdapter(Context c,ArrayList<String> p){
		pics = p;
		context = c;
		instance = this;
	}
	public PicAdapter(Context c,ArrayList<String> p,String type){
		pics = p;
		context = c;
		this.type = type;
		instance = this;
	}
	public static class PHolder{
		ImageView userPortrait;
		ImageButton btn_delpic;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		PHolder pHolder=null;
		if(convertView==null){
			convertView = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.gridview_user_album_item, null);
			pHolder = new PHolder();
			pHolder.userPortrait = (ImageView)convertView.findViewById(R.id.userPortrait);
			pHolder.btn_delpic = (ImageButton)convertView.findViewById(R.id.btn_delpic);
			convertView.setTag(pHolder);
		}else{
			pHolder = (PHolder)convertView.getTag();
		}
		final String f = pics.get(position);
		if(type.equals("url")){
			if(!f.equals("")){
				Picasso.with(context).load(f).resize(400, 400)
					.centerCrop().placeholder(R.drawable.empty_photo_default)
					.into(pHolder.userPortrait);
				if(delEnable)
					pHolder.btn_delpic.setVisibility(View.VISIBLE);
			}
			else{
				pHolder.userPortrait.setImageResource(R.drawable.no_photo);
				pHolder.btn_delpic.setVisibility(View.GONE);
			}
		}else if(type.equals("filepath")){
			if(!f.equals("")){
				Logot.outInfo("PicAdapter","filepath:"+f);
				pHolder.userPortrait.setImageBitmap(ImageUtils.getBitmap(f, 300, 300));
				if(delEnable)
					pHolder.btn_delpic.setVisibility(View.VISIBLE);
			}
			else{
				pHolder.userPortrait.setImageResource(R.drawable.bg_photo_add_button);
				pHolder.btn_delpic.setVisibility(View.GONE);
			}
		}
		pHolder.btn_delpic.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				pics.remove(position);
				if(pics.size()==0)
					pics.add("");
				instance.notifyDataSetChanged();
			}
			
		});
		return convertView;
	}
	
	public void setDelEnable(boolean canDel){
		delEnable = canDel;
	}
}
