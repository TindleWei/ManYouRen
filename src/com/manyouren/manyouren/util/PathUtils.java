package com.manyouren.manyouren.util;


import com.manyouren.manyouren.RootApplication;
import android.os.Environment;

public class PathUtils {

	public static String getImageCachePath(){
		return getCachePath()+"/images";
	}

	public static String getCachePath() {
		String path = RootApplication.getInstance().getExternalCacheDir().getAbsolutePath();
		return path;
	}
	
	public static String getFilePath(){
		String path = "";
		return path;
	}



}
