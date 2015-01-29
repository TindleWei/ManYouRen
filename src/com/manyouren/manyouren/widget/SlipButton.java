package com.manyouren.manyouren.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.manyouren.manyouren.R;
/**
 * 
 * @ClassName: SlipButton 
 * @Description: 滑动开关按钮
 * @author LiXiaolong
 * @date 2012-11-16 上午10:17:00 
 *
 */
public class SlipButton extends FrameLayout implements OnClickListener{
	/** 控件显示 **/
	private final int VISIBLE = View.VISIBLE;
	/** 控件隐藏 **/
	private final int GONE = View.GONE;
	/** 按钮图片的宽度（实际像素宽度除以1.5） **/
	private final float BUTTON_W = 51.333333f;
	/** 按钮背景图与thumb图片的比例（按钮宽除以thumb图片的宽） **/
	private final float MULTIPLES = 0.5844155844155844f;// 0.3703703703704
	/** 开和关背景图切换延迟时间 **/
	private final long DELAYMILLIS = 145; 
	/** 滑动动画时间 **/
	private final int DURATION = 150;
	/** 监听器名称 **/
	private String listenerName;
	/** 上下文 **/
	private final Context mContext;
	/** 可点击标记 **/
	private boolean isClicked = false;
	/** 记录当前按钮是否打开,true为打开,flase为关闭 **/
	private boolean isChecked = true;
	/** 是否设置了状态改变监听器 **/
	private boolean isSetListener = false;
	/** 状态改变监听器 **/
	private OnChangedListener mOnChangedListener;
	/** 开关背景图 **/
	private ImageView switcher_on, switcher_off;
	/** 滑动动画 **/
	private TranslateAnimation ll = null;
	private TranslateAnimation lr = null;
	private TranslateAnimation rl = null;
	private TranslateAnimation rr = null;

	/**
	 * 
	 * <p>Constructors: </p> 
	 * <p>Description: </p> 
	 * @param context
	 */
	public SlipButton(Context context) {
		super(context);
		mContext = context;
		initButton();
	}
	/**
	 * 
	 * <p>Constructors: </p> 
	 * <p>Description: </p> 
	 * @param context
	 * @param attrs
	 */
	public SlipButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initButton();
	}
	
	/**
	 * 初始化开关按钮
	 */
	private void initButton(){
		if (isInEditMode()) {
            return;
		}
		isChecked = true;
		int witch = LayoutParams.WRAP_CONTENT;
		int height = LayoutParams.WRAP_CONTENT;
		DisplayMetrics dm = new DisplayMetrics();//屏幕分辨率容器
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		float dens = dm.density;
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(witch, height);
		RelativeLayout switchParent = new RelativeLayout(mContext);
		switchParent.setLayoutParams(params);
		this.setLayoutParams(params);
		this.setOnClickListener(this);
		switcher_on = new ImageView(mContext);
		switcher_on.setLayoutParams(params);
		switcher_on.setClickable(false);
		switcher_on.setImageResource(R.drawable.my_switch_on);
		switcher_off = new ImageView(mContext);
		switcher_off.setLayoutParams(params);
		switcher_off.setClickable(false);
		switcher_off.setImageResource(R.drawable.my_switch_off);
		switchParent.addView(switcher_off);
		switchParent.addView(switcher_on);
		this.addView(switchParent);
//		RelativeLayout spase = new RelativeLayout(mContext);
//		spase.setLayoutParams(params);
//		spase.setBackgroundResource(R.drawable.my_switch_frame);
//		this.addView(spase);
		float boxWitch = BUTTON_W * dens;
		int step = (int) (boxWitch - (boxWitch * MULTIPLES));
		ll = new TranslateAnimation(0, -step, 0, 0) ;
		lr = new TranslateAnimation(-step, 0, 0, 0) ;
		rl = new TranslateAnimation(step, 0, 0, 0);
		rr = new TranslateAnimation(0, step, 0, 0);
		ll.setDuration(DURATION);
		lr.setDuration(DURATION);
		rl.setDuration(DURATION);
		rr.setDuration(DURATION);
	}
	
	/**
	 * 开关按钮点击事件
	 */
	@Override
	public void onClick(View v) {
		if(!isClicked){
			isClicked = true;
			if(isChecked){
				isChecked = false;
			}else{
				isChecked = true;
			}
			changeState(isChecked);
		}
	}
	
	/**
	 * 切换开关按钮状态
	 * @param checked
	 */
	private void changeState(boolean checked){
		this.smoothChanged(checked);
		if(isSetListener){
			mOnChangedListener.OnChanged(listenerName, checked);
		}
		isChecked = checked;
	}
	/**
	 * 设置开关按钮状态
	 * @param checked 开关按钮状态
	 */
	public void setChecked(boolean checked){
		if(checked){
			switcher_on.setVisibility(VISIBLE);
			switcher_off.setVisibility(GONE);
			isClicked = false;
		}else{
			switcher_off.setVisibility(VISIBLE);
			switcher_on.setVisibility(GONE);
			isClicked = false;
		}
		isChecked = checked;
	}
	/**
	 * 使用动画设置开关按钮状态
	 * @param checked 开关按钮状态
	 */
	public void setCheckedByAnim(boolean checked){
		this.smoothChanged(checked);
		isChecked = checked;
	}
	/**
	 * 动画设置开关按钮状态
	 * @param checked
	 */
	private void smoothChanged(boolean checked){
		Handler h = new Handler();
		if(checked){
			switcher_on.setVisibility(VISIBLE);
			switcher_on.startAnimation(lr);
			switcher_off.startAnimation(rr);
			h.postDelayed(new Runnable() {
				@Override
				public void run() {
					switcher_off.setVisibility(GONE);
					isClicked = false;
				}
			}, DELAYMILLIS);
		}else{
			switcher_on.startAnimation(ll);
			switcher_off.setVisibility(VISIBLE);
			switcher_off.startAnimation(rl);
			h.postDelayed(new Runnable() {
				@Override
				public void run() {
					switcher_on.setVisibility(GONE);
					isClicked = false;
				}
			}, DELAYMILLIS);
		}
	}
	
	/**
	 * 状态改变监听器
	 * @author LiXiaolong
	 *
	 */
	public interface OnChangedListener{
		public void OnChanged(String name, boolean isChecked);
	}
	
	/**
	 * 设置状态改变监听器
	 * @param name
	 * @param listener
	 */
	public void setOnChangedListener(String name, OnChangedListener listener){
		listenerName = name;
		isSetListener = true;
		mOnChangedListener = listener;
	}
	
	/**
	 * 获取按钮当前状态
	 * @return
	 */
	public boolean isChecked() {
		return isChecked;
	}
}
