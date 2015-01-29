/**
* @Package com.manyouren.android.ui.discovery    
* @Title: BeautyPlaceAdapter.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-7-13 上午11:10:51 
* @version V1.0   
*/
package com.manyouren.manyouren.ui.discovery.old;

import java.util.List;

import android.view.LayoutInflater;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.ui.AlternatingColorListAdapter;

/** 
 * @Description: TODO
 *
 * @author firefist_wei
 * @date 2014-7-13 上午11:10:51 
 *  
 */
public class BeautyPlaceAdapter extends AlternatingColorListAdapter<UserEntity> {
	/**
	 * @param inflater
	 * @param items
	 * @param selectable
	 */
	public BeautyPlaceAdapter(final LayoutInflater inflater,
			final List<UserEntity> items, final boolean selectable) {
		super(R.layout.beautyplace_list_item, inflater, items, selectable);
	}

	/**
	 * @param inflater
	 * @param items
	 */
	public BeautyPlaceAdapter(final LayoutInflater inflater,
			final List<UserEntity> items) {
		super(R.layout.beautyplace_list_item, inflater, items);
	}


	@Override
	protected int[] getChildViewIds() {
		return new int[]{R.id.tv_title, R.id.tv_summary,
                R.id.tv_date};
	}

	@Override
    protected void update(final int position, final UserEntity item) {
        super.update(position, item);

//        setText(0, item.getTitle());
//        setText(1, item.getContent());
    }

}
