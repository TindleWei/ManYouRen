package com.manyouren.manyouren.util;

import android.util.Log;
/**
 * 日志输出管理
 * @author liubo
 *
 */
public class Logot {
	private static final boolean DEBUG = true;
	public static void outInfo(String tag,String msg){
		if(DEBUG){
			Log.i(tag, msg);
		}
	}
	public static void outError(String tag,String msg){
		if(DEBUG){
			Log.e(tag, msg);
		}
	}
	public static void outDebug(String tag,String msg,Throwable tr){
		if(DEBUG){
			Log.d(tag, msg, tr);
		}
	}
	
}
