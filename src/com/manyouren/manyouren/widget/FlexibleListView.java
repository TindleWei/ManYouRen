/**
 * @Package com.manyouren.android.widget    
 * @Title: SpringScrollListView.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-20 下午4:49:05 
 * @version V1.0   
 */
package com.manyouren.manyouren.widget;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

;

/**
 * unfinished
 * 
 * @author firefist_wei
 * @date 2014-6-20 下午4:49:05
 * 
 */
public class FlexibleListView extends ListView implements OnScrollListener {

	private Context context = null;
	
	private ScheduledExecutorService schedulor = Executors
			.newScheduledThreadPool(1);

	private View headView = null;

	private View tailView = null;

	private boolean isPullDownState = false;

	private boolean isPullUpState = false;

	private int firstItemIndex = 0;
	
	private int lastItemIndex = 0;
	
	private int startPullDownY = 0;
	
	private int startPullUpY = 0;
	
	private static final float PULL_FACTOR =  0.5f;
	
	private static final int PULL_BACK_REDUCE_STEP = 10;

	private static final int PULL_BACK_TASK_PERIOD = 200;

	private static final int PULL_DOWN_BACK_ACTION = 1;

	private static final int PULL_UP_BACK_ACTION = 2;

	/**
	 * Description:
	 * 
	 * @param context
	 */
	public FlexibleListView(Context context) {
		super(context);
		this.context = context;
		initFlexListView(true, true);
		
	}

	/**
	 * @Description: TODO
	 * 
	 * @param b
	 * @param c
	 * @return void
	 */
	private void initFlexListView(boolean isHeadViewNeed, boolean isTailViewNeed) {
		if (isHeadViewNeed) {
			setOnScrollListener(this);
			headView = new View(this.getContext());
			headView.setBackgroundColor(Color.parseColor("#4F9D9D"));
			headView.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.FILL_PARENT, 0));
			this.addHeaderView(headView);
		}

		if (isTailViewNeed) {
			setOnScrollListener(this);
			tailView = new View(this.getContext());
			tailView.setBackgroundColor(Color.parseColor("#4F9D9D"));
			tailView.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.FILL_PARENT, 0));
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		case MotionEvent.ACTION_UP:
			if (!isPullDownState && isPullUpState)
				break;
			if (isPullDownState) {
				schedulor.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						mHandler.sendEmptyMessage(PULL_DOWN_BACK_ACTION);
					}
				}, 0, PULL_BACK_TASK_PERIOD, TimeUnit.NANOSECONDS);
				isPullDownState = false;
			} else if (isPullUpState) {
				schedulor = Executors.newScheduledThreadPool(1);
				schedulor.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						mHandler.sendEmptyMessage(PULL_UP_BACK_ACTION);
					}
				}, 0, PULL_BACK_TASK_PERIOD, TimeUnit.NANOSECONDS);
				isPullUpState = false;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (isPullDownState && firstItemIndex == 0) {
				startPullDownY = (int) ev.getY();
				//setPullType(PULL_DOWN_BACK_ACTION);
			} else if (isPullUpState && lastItemIndex == getCount()) {
				startPullUpY = (int) ev.getY();
				//setPullType(PULL_UP_BACK_ACTION);
			}
			
			if(!isPullDownState && !isPullUpState){
				break;
			}
			
			if(isPullDownState) {
				int tempY = (int) ev.getY();
				int moveY = tempY - startPullDownY;
				if (moveY < 0) {
					isPullDownState = false;
					break;
				}
				headView.setLayoutParams(new AbsListView.LayoutParams(
						LayoutParams.FILL_PARENT, (int) (moveY * PULL_FACTOR)));
				headView.invalidate();
				
			}else if(isPullUpState){
				int tempY = (int) ev.getY();
				int moveY = startPullUpY - tempY;
				if (moveY < 0){
					isPullUpState = false;
					break;
				}
				tailView.setLayoutParams(new AbsListView.LayoutParams(
						LayoutParams.FILL_PARENT, (int) (moveY * PULL_FACTOR)));
				tailView.invalidate();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		//currentScrollState = scrollState;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstItemIndex = firstVisibleItem;
		this.lastItemIndex = firstVisibleItem + visibleItemCount;

	}
	
	private String getScrollStateString(int flag) {
		String str = "";
		switch(flag) {
		
		}
		return str;
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case PULL_DOWN_BACK_ACTION:
				AbsListView.LayoutParams headerParams = (LayoutParams) headView
						.getLayoutParams();
				//decrease the height
				headerParams.height -= PULL_BACK_REDUCE_STEP;
				headView.setLayoutParams(headerParams);
				headView.invalidate();
				if (headerParams.height <= 0) {
					schedulor.shutdownNow();
				}
				break;
			case PULL_UP_BACK_ACTION:
				AbsListView.LayoutParams footerParams = (LayoutParams) tailView
					.getLayoutParams();
				footerParams.height -= PULL_BACK_REDUCE_STEP;
				tailView.setLayoutParams(footerParams);
				tailView.invalidate();
				if (footerParams.height <0) {
					schedulor.shutdownNow();
				}
				break;
			}
		};
	};
	


}
