/**
 * @Package com.manyouren.android.ui.user    
 * @Title: UserPlansAdapter.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-16 上午3:50:01 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.user;

import java.util.List;

import android.view.LayoutInflater;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.controller.PlanController;
import com.manyouren.manyouren.entity.PlanEntity;
import com.manyouren.manyouren.ui.AlternatingColorListAdapter;
import com.manyouren.manyouren.util.DateUtils;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-7-16 上午3:50:01
 * 
 */
public class UserPlansAdapter extends AlternatingColorListAdapter<PlanEntity> {
	/**
	 * @param inflater
	 * @param items
	 * @param selectable
	 */
	public UserPlansAdapter(final LayoutInflater inflater,
			final List<PlanEntity> items, final boolean selectable) {
		super(R.layout.userplans_list_item, inflater, items, selectable);
	}

	/**
	 * @param inflater
	 * @param items
	 */
	public UserPlansAdapter(final LayoutInflater inflater,
			final List<PlanEntity> items) {
		super(R.layout.userplans_list_item, inflater, items);
	}

	@Override
	protected int[] getChildViewIds() {
		return new int[] { R.id.tv_destination, R.id.tv_startdate,
				R.id.iv_plan_for, R.id.iv_plan_with, R.id.iv_plan_seek,
				R.id.tv_postscript, R.id.tv_createtime};
	}

	@Override
	protected void update(final int position, final PlanEntity item) {
		super.update(position, item);

		if (item.getScenicId().equals("0")) {
			setText(0, item.getMyPName());
		} else{
			setText(0, item.getpName());
		}
		
		setText(1,
				DateUtils.getPlanDigitalDate(item.getStartDate(), item.getEndDate()));
		
		int res_id = PlanController.res_for[Integer.valueOf(item.getType())];
		imageView(2).setImageResource(res_id);

		res_id = PlanController.res_with[Integer.valueOf(item.getTogether())];
		imageView(3).setImageResource(res_id);

		res_id = PlanController.res_seek[Integer.valueOf(item.getPurpose())];
		imageView(4).setImageResource(res_id);
		
		setText(5, item.getPostscript());
		
		setText(6, "发布时间："+ DateUtils.getCreateTime(Long.valueOf(item.getCreateTime())));
	}

}
