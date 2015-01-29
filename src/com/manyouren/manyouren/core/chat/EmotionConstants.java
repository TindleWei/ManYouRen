/**
* @Package com.manyouren.android.core.chat    
* @Title: EmotionConstants.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-6-28 下午12:30:57 
* @version V1.0   
*/
package com.manyouren.manyouren.core.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-6-28 下午12:30:57 
 *  
 */
public class EmotionConstants {
	
	public static String UserID = "";

	public static List<String> mEmoticons = new ArrayList<String>();
	public static Map<String, Integer> mEmoticonsId = new HashMap<String, Integer>();
	public static List<String> mEmoticons_Zem = new ArrayList<String>();
	public static List<String> mEmoticons_Zemoji = new ArrayList<String>();

	static{

		Context context = RootApplication.getInstance().getApplicationContext();
		
		for (int i = 1; i < 33; i++) {
			String emoticonsName = "[zem" + i + "]";
			int emoticonsId = context.getResources().getIdentifier("zem" + i,
					"drawable", context.getPackageName());
			mEmoticons.add(emoticonsName);
			mEmoticons_Zem.add(emoticonsName);
			mEmoticonsId.put(emoticonsName, emoticonsId);
		}
		for (int i = 1; i < 33; i++) {
			String emoticonsName = "[zemoji" + i + "]";
			int emoticonsId = context.getResources().getIdentifier("zemoji_e" + i,
					"drawable", context.getPackageName());
			mEmoticons.add(emoticonsName);
			mEmoticons_Zemoji.add(emoticonsName);
			mEmoticonsId.put(emoticonsName, emoticonsId);
		}
	}
}
