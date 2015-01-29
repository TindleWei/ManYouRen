package com.manyouren.manyouren.widget.progress;

import roboguice.inject.InjectView;

import com.manyouren.manyouren.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VerticalProgressMsg extends RelativeLayout{
	
	@InjectView(R.id.view_progress)
	ProgressBar progress;
	
	@InjectView(R.id.view_progress_info)
	TextView info;
	
	private RelativeLayout mContainer;

	public VerticalProgressMsg(Context context) {
		super(context);
		initView(context);
	}
	
	 public VerticalProgressMsg(Context context, AttributeSet attrs) {  
	        super(context, attrs); 
	        initView(context);
	}

	private void initView(Context context) {
		mContainer = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_vertical_progressbar, null);
		addView(mContainer);
		progress = (ProgressBar)mContainer.findViewById(R.id.view_progress);
		info = (TextView)mContainer.findViewById(R.id.view_progress_info);
		
	}
	
	public void showProgressBar(){
		progress.setVisibility(View.VISIBLE);
		info.setVisibility(View.GONE);
	}
	
	public void hideProgressBar(){
		progress.setVisibility(View.GONE);
		info.setVisibility(View.GONE);
	}
	
	public void showErrorInfo(){
		progress.setVisibility(View.GONE);
		info.setVisibility(View.VISIBLE);
		info.setText("网络异常");
	}
	
	public void showEmptyInfo(){
		progress.setVisibility(View.GONE);
		info.setVisibility(View.VISIBLE);
		info.setText("数据为空");
	}


}
