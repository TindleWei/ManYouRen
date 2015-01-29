/**   
* @Title: AutoResizingListView.java 
* @Package com.manyouren.android.widget 
* @Description: TODO(用一句话描述该文件做什么) 
* @author ssz 31807077_qq_com   
* @date 2014-7-30 下午1:09:24 
* @version V1.0   
*/
package com.manyouren.manyouren.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/** 
 * @包名: com.manyouren.android.widget
 * @描述: TODO(这里用一句话描述这个类的作用) 
 * @作者 ssz 31807077_qq_com
 * @日期 2014-7-30 下午1:09:24 
 * @版本 V 1.0
 *  
 */
public class AutoResizingListView extends ListView {
	public AutoResizingListView(Context context) {
        super(context);
    }

    public AutoResizingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoResizingListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override public void setAdapter(final ListAdapter adapter) {
        super.setAdapter(adapter);

        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override public void onChanged() {
                setHeight();
            }
        });

        setHeight();
    }

    private void setHeight() {
        ListAdapter adapter = getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, this);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = this.getLayoutParams();
        int dividerHeights = getDividerHeight() * (adapter.getCount() == 0 ? 0 : adapter.getCount() - 1);
        params.height = totalHeight + dividerHeights;
        setLayoutParams(params);
    }
}
