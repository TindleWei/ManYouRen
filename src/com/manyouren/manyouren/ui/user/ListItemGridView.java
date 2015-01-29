package com.manyouren.manyouren.ui.user;

import android.content.Context;
import android.widget.GridView;

public class ListItemGridView extends GridView {

	public ListItemGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public ListItemGridView(android.content.Context context,android.util.AttributeSet attrs)  
    {  
        super(context, attrs);  
    }  
	/** 
     * 设置不滚动 
     */  
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)  
    {  
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
                MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, expandSpec);  
  
    }  
}
