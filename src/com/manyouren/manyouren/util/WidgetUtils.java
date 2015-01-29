/**
* @Package com.manyouren.android.util    
* @Title: WidgetUtils.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-9-5 下午9:53:24 
* @version V1.0   
*/
package com.manyouren.manyouren.util;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-9-5 下午9:53:24 
 *  
 */
public class WidgetUtils {
	
	/**
	 * get imei
	 * 
	 * @return
	 */
	public static String getIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		if (TextUtils.isEmpty(imei)) {
			imei = "imeiisempty";
		}
		return imei;
	}

	/** 
	* 隐藏软键盘
	*
	*/
	public static void hideKeyBoard(Activity activity,EditText editText){
		try{
		((InputMethodManager) editText.getContext()
				.getSystemService(
						Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(
						activity.getCurrentFocus()
								.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/** 
	* 隐藏软键盘
	*
	*/
	public static void hideKeyBoard(View view){
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(view.getApplicationWindowToken(),
					0);
		}
	}
}
