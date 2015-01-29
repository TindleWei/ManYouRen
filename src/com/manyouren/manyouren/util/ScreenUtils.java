/**
* @Package com.manyouren.android.util    
* @Title: ServiceUtils.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-6-10 上午11:44:45 
* @version V1.0   
*/
package com.manyouren.manyouren.util;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-6-10 上午11:44:45 
 *  
 */
import static android.content.Context.WINDOW_SERVICE;
import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * Helpers for dealing with system services
 */
public class ScreenUtils {

    /**
     * Get default display
     *
     * @param context
     * @return display
     */
    public static Display getDisplay(final Context context) {
        return ((WindowManager) context.getSystemService(WINDOW_SERVICE))
                .getDefaultDisplay();
    }

    /**
     * Get default display
     *
     * @param view
     * @return display
     */
    public static Display getDisplay(final View view) {
        return getDisplay(view.getContext());
    }

    /**
     * Get default display width
     *
     * @param context
     * @return display
     */
    @SuppressWarnings("deprecation")
    public static int getDisplayWidth(final Context context) {
        return getDisplay(context).getWidth();
    }

    /**
     * Get default display width
     *
     * @param view
     * @return display
     */
    public static int getDisplayWidth(final View view) {
        return getDisplayWidth(view.getContext());
    }

    /**
     * Get pixels from dps
     *
     * @param view
     * @param dp
     * @return pixels
     */
    public static float getPixels(final View view, final int dp) {
        return getPixels(view.getResources(), dp);
    }

    /**
     * Get pixels from dps
     *
     * @param resources
     * @param dp
     * @return pixels
     */
    public static float getPixels(final Resources resources, final int dp) {
        return TypedValue.applyDimension(COMPLEX_UNIT_DIP, dp,
                resources.getDisplayMetrics());
    }

    /**
     * Get pixels from dps
     *
     * @param view
     * @param dp
     * @return pixels
     */
    public static int getIntPixels(final View view, final int dp) {
        return getIntPixels(view.getResources(), dp);
    }

    /**
     * Get pixels from dps
     *
     * @param context
     * @param dp
     * @return pixels
     */
    public static int getIntPixels(final Context context, final int dp) {
        return getIntPixels(context.getResources(), dp);
    }

    /**
     * Get pixels from dps
     *
     * @param resources
     * @param dp
     * @return pixels
     */
    public static int getIntPixels(final Resources resources, final int dp) {
        float pixels = TypedValue.applyDimension(COMPLEX_UNIT_DIP, dp,
                resources.getDisplayMetrics());
        return (int) Math.floor(pixels + 0.5F);
    }
    
    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dp2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dp(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
}
