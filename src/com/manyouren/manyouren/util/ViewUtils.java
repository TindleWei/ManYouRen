/**
* @Package com.manyouren.android.util    
* @Title: ViewUtils.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-6-9 下午3:19:30 
* @version V1.0   
*/
package com.manyouren.manyouren.util;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import android.view.View;

import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.TouchDelegate;
import android.view.View;

/** 
 * Utilities for working with the {@link View} class
 *
 * @author firefist_wei
 * @date 2014-6-9 下午3:19:30 
 *  
 */
public class ViewUtils {
	  /**
	   * Set visibility of given view to be gone or visible
	   * <p>
	   * This method has no effect if the view visibility is currently invisible
	   *
	   * @param view
	   * @param gone
	   * @return view
	   */
	  public static <V extends View> V setGone(final V view, final boolean gone) {
	    if (view != null)
	      if (gone) {
	        if (GONE != view.getVisibility())
	          view.setVisibility(GONE);
	      } else {
	        if (VISIBLE != view.getVisibility())
	          view.setVisibility(VISIBLE);
	      }
	    return view;
	  }
	  
	  /**
	   * Set visibility of given view to be invisible or visible
	   * <p>
	   * This method has no effect if the view visibility is currently gone
	   *
	   * @param view
	   * @param invisible
	   * @return view
	   */
	  public static <V extends View> V setInvisible(final V view,
	      final boolean invisible) {
	    if (view != null)
	      if (invisible) {
	        if (INVISIBLE != view.getVisibility())
	          view.setVisibility(INVISIBLE);
	      } else {
	        if (VISIBLE != view.getVisibility())
	          view.setVisibility(VISIBLE);
	      }
	    return view;
	  }
	  
	  /**
	   * Increases the hit rect of a view. This should be used when an icon is small and cannot be easily tapped on.
	   * Source: http://stackoverflow.com/a/1343796/5210
	   * @param amount The amount of dp's to be added to all four sides of the view hit purposes.
	   * @param delegate The view that needs to have its hit rect increased.
	   */
	  public static void increaseHitRectBy(final int amount, final View delegate) {
	    increaseHitRectBy(amount, amount, amount, amount, delegate);
	  }
	  
	  /**
	   * Increases the hit rect of a view. This should be used when an icon is small and cannot be easily tapped on.
	   * Source: http://stackoverflow.com/a/1343796/5210
	   * @param top The amount of dp's to be added to the top for hit purposes.
	   * @param left The amount of dp's to be added to the left for hit purposes.
	   * @param bottom The amount of dp's to be added to the bottom for hit purposes.
	   * @param right The amount of dp's to be added to the right for hit purposes.
	   * @param delegate The view that needs to have its hit rect increased.
	   */
	  public static void increaseHitRectBy(final int top, final int left, final int bottom, final int right, final View delegate) {
	    final View parent = (View) delegate.getParent();
	    if (parent != null && delegate.getContext() != null) {
	      parent.post(new Runnable() {
	        // Post in the parent's message queue to make sure the parent
	        // lays out its children before we call getHitRect()
	        public void run() {
	          final float densityDpi = delegate.getContext().getResources().getDisplayMetrics().densityDpi;
	          final Rect r = new Rect();
	          delegate.getHitRect(r);
	          r.top -= transformToDensityPixel(top, densityDpi);
	          r.left -= transformToDensityPixel(left, densityDpi);
	          r.bottom += transformToDensityPixel(bottom, densityDpi);
	          r.right += transformToDensityPixel(right, densityDpi);
	          parent.setTouchDelegate(new TouchDelegate(r, delegate));
	        }
	      });
	    }
	  }
	  
	  public static int transformToDensityPixel(int regularPixel, DisplayMetrics displayMetrics) {
	      return transformToDensityPixel(regularPixel, displayMetrics.densityDpi);
	  }

	  public static int transformToDensityPixel(int regularPixel, float densityDpi) {
	      return (int) (regularPixel * densityDpi);
	  }

	  private ViewUtils() {

	  }
}
