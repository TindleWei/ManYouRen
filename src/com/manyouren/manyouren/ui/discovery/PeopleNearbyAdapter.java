/**
 * @Package com.manyouren.android.ui.discovery    
 * @Title: PeopleNearbyAdapter.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-11 下午12:04:02 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.discovery;

import java.util.List;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.entity.UserNearbyEntity;
import com.manyouren.manyouren.ui.AlternatingColorListAdapter;
import com.manyouren.manyouren.ui.RoundedTransformation;
import com.manyouren.manyouren.util.Logot;
import com.squareup.picasso.Picasso;

/**
 * 
 * @author firefist_wei
 * @date 2014-7-11 下午12:04:02
 * 
 */
public class PeopleNearbyAdapter extends
		AlternatingColorListAdapter<UserNearbyEntity> {

	/**
	 * @param inflater
	 * @param items
	 */
	public PeopleNearbyAdapter(final LayoutInflater inflater,
			final List<UserNearbyEntity> items) {
		super(R.layout.nearby_list_item, inflater, items);
	}

	@Override
	protected int[] getChildViewIds() {
		return new int[] { R.id.iv_avatar, R.id.tv_name, R.id.tv_age,
				R.id.tv_distance, R.id.tv_status };
	}

	@SuppressLint("NewApi")
	@Override
	protected void update(final int position, final UserNearbyEntity item) {
		super.update(position, item);

		Picasso.with(RootApplication.getInstance())
				.load(UserController.getAvatarDiff(item.getAvatar0()))
				.transform(new RoundedTransformation(16,2))
				.placeholder(R.drawable.gravatar_image).into(imageView(0));

		setText(1, item.getUsername() + "");

		Logot.outError("Birthday",item.getBirthday());
		textView(2).setText(UserController.getAgeFromDateString(item.getBirthday())
				+ "");
		textView(2).setBackgroundDrawable(RootApplication.getInstance().getResources().getDrawable(
				item.getGender().equals("1") ? R.drawable.ic_age_male_bg
						: R.drawable.ic_age_female_bg));

		// DecimalFormat df = new DecimalFormat("#.00");
		try{
			setText(3, String.format("%.2f", Double.valueOf(item.getDistance()))
					+ "km"+" | "+"来自"+item.getResidence());
		}catch(Exception e){	
		}


		if(item.getStatusText().equals("")){
			setText(4, " ");
		}else{
			setText(4, item.getStatusText());
		}
		
	}
}
