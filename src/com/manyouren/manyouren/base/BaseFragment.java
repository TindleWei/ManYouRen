/**
* @Package com.manyouren.android.ui    
* @Title: BaseFragment.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-9-3 下午4:07:37 
* @version V1.0   
*/
package com.manyouren.manyouren.base;

import roboguice.fragment.RoboFragment;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.kevinsawicki.wishlist.ViewFinder;
import com.manyouren.manyouren.R;

/** 
 * BaseFragment
 *
 * @author firefist_wei
 * @date 2014-9-3 下午4:07:37 
 *  
 */
public abstract class BaseFragment extends RoboFragment {
	
	/**
     * View finder bound to the value last specified to
     * {@link #onViewCreated(android.view.View, Bundle)}
     */
    protected ViewFinder finder;
    
    protected Context context;
    
    protected Activity activity;

    /**
     * Is this fragment usable from the UI-thread
     *
     * @return true if usable, false otherwise
     */
    protected boolean isUsable() {
        return getActivity() != null;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        activity = getActivity();
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        finder = new ViewFinder(view);
    }
    
    /** 初始化视图 **/
	protected abstract void initView();

	/** 初始化事件 **/
	protected abstract void initEvent();
	
	/** 初始化数据 **/
	protected abstract void init();
	
	/** 通过Class跳转界面 **/
	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	/** 含有Bundle通过Class跳转界面 **/
	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.left_in,
				R.anim.left_out);
	}
	
	/** 通过Action跳转界面 **/
	protected void startActivity(String action) {
		startActivity(action, null);
	}

	/** 含有Bundle通过Action跳转界面 **/
	protected void startActivity(String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.left_in,
				R.anim.left_out);
	}
    


}
