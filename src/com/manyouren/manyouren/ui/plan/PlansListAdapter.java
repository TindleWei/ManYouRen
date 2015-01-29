/**
 * @Package com.manyouren.android.core.plan    
 * @Title: PlansListAdapter.java 
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-16 下午2:09:21 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.plan;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.controller.PlanController;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.entity.PlanEntity;
import com.manyouren.manyouren.ui.AlternatingColorListAdapter;
import com.manyouren.manyouren.ui.CircleTransform;
import com.manyouren.manyouren.util.DateUtils;
import com.squareup.picasso.Picasso;

/**
 * 
 * @author firefist_wei
 * @date 2014-6-16 下午2:09:21
 * 
 */
@SuppressLint("NewApi")
public class PlansListAdapter extends AlternatingColorListAdapter<PlanEntity> {

	Context context = null;

	/**
	 * @param inflater
	 * @param items
	 * @param selectable
	 */
	public PlansListAdapter(final LayoutInflater inflater,
			final List<PlanEntity> items, final boolean selectable) {
		super(R.layout.plans_list_item, inflater, items, selectable);
	}

	/**
	 * @param inflater
	 * @param items
	 */
	public PlansListAdapter(final LayoutInflater inflater,
			final List<PlanEntity> items) {
		super(R.layout.plans_list_item, inflater, items);
	}

	@Override
	protected int[] getChildViewIds() {
		return new int[] { R.id.iv_avatar, R.id.tv_destination, R.id.tv_date,
				R.id.tv_note, R.id.iv_with, R.id.iv_for, R.id.iv_seek,
				R.id.tv_plan_from, R.id.tv_city, R.id.tv_man_from };
	}

	@Override
	protected void update(int position, PlanEntity item) {
		super.update(position, item);

		if (item.getScenicId().equals("0")) {

			setText(1, item.getMyPName());
			setText(8, "");
		} else {
			String placeStr = item.getpName();
			if (placeStr.length() > 6) {
				placeStr = placeStr.substring(0, 6) + "···";
			}

			if (Constants.PLACE_NOW.equals(item.getcName())) {

				setText(1, placeStr);
				setText(8, "");
			} else {
				if (item.getStartCity().equals(item.getcName())) {
					setText(1, placeStr);
					setText(8, "  " + item.getcName());
				} else {
					setText(1, item.getStartCity() + "→" + item.getcName());
					setText(8, "");
				}
			}
		}

		setText(2,
				DateUtils.getPlanDigitalDate(item.getStartDate(),
						item.getEndDate()));

		setText(3, item.getPostscript());

		try {
			String avatarStr = UserController.getAvatarDiff(item.getAvatar0());

			Picasso.with(RootApplication.getInstance()).load(avatarStr)
					.transform(new CircleTransform())
					.placeholder(R.drawable.gravatar_icon).into(imageView(0));
		} catch (Exception e) {

		}

		setText(7, "出发位置 " + item.getStartCity());

		try {
			int res_id = PlanController.res_for[Integer.valueOf(item.getType())];
			imageView(4).setImageResource(res_id);

			res_id = PlanController.res_with[Integer
					.valueOf(item.getTogether())];
			imageView(5).setImageResource(res_id);

			res_id = PlanController.res_seek[Integer.valueOf(item.getPurpose())];
			imageView(6).setImageResource(res_id);
		} catch (Exception e) {

		}

		textView(9).setText(item.getResidence());
		textView(9)
				.setBackgroundDrawable(
						RootApplication
								.getInstance()
								.getResources()
								.getDrawable(
										item.getGender().equals("0") ? R.drawable.ic_city_female_bg
												: R.drawable.ic_city_male_bg));
	}
}
