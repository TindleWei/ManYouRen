/**
 * @Package com.manyouren.android.util    
 * @Title: NetworkUtils.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-13 下午12:09:57 
 * @version V1.0   
 */
package com.manyouren.manyouren.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-6-13 下午12:09:57
 * 
 */
public class NetworkUtils {
	private Context mContext;
	public State wifiState = null;
	public State mobileState = null;

	public NetworkUtils(Context context) {
		mContext = context;
	}

	public enum NetWorkState {
		WIFI, MOBILE, NONE;

	}

	/**
	 * 获取当前的网络状态
	 * 
	 * @return
	 */
	public NetWorkState getConnectState() {

		try {
			ConnectivityManager manager = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			manager.getActiveNetworkInfo();
			wifiState = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.getState();
			mobileState = manager.getNetworkInfo(
					ConnectivityManager.TYPE_MOBILE).getState();
			if (wifiState != null && mobileState != null
					&& State.CONNECTED != wifiState
					&& State.CONNECTED == mobileState) {
				return NetWorkState.MOBILE;
			} else if (wifiState != null && mobileState != null
					&& State.CONNECTED != wifiState
					&& State.CONNECTED != mobileState) {
				return NetWorkState.NONE;
			} else if (wifiState != null && State.CONNECTED == wifiState) {
				return NetWorkState.WIFI;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return NetWorkState.NONE;
		}
		return NetWorkState.NONE;
	}
}
