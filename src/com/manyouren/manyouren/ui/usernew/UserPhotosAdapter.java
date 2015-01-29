package com.manyouren.manyouren.ui.usernew;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.widget.RatingBar;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.controller.PlanController;
import com.manyouren.manyouren.entity.PhotoEntity;
import com.manyouren.manyouren.ui.AlternatingColorListAdapter;
import com.manyouren.manyouren.util.DateUtils;
import com.manyouren.manyouren.util.Logot;
import com.squareup.picasso.Picasso;

public class UserPhotosAdapter extends AlternatingColorListAdapter<PhotoEntity> {

	/**
	 * @param inflater
	 * @param items
	 */
	public UserPhotosAdapter(final LayoutInflater inflater,
			final List<PhotoEntity> items) {
		super(R.layout.item_user_photo, inflater, items);
	}

	@Override
	protected int[] getChildViewIds() {
		return new int[] { R.id.iv_photo, R.id.tv_descript, R.id.tv_time,
				R.id.rb_rating, R.id.tv_tag };
	}

	@SuppressLint("NewApi")
	@Override
	protected void update(final int position, final PhotoEntity item) {
		super.update(position, item);

		List<String> listImages = PlanController
				.getPlanImages(item.getImages());
		
		Logot.outError("ITEM PHOTO", listImages.get(0));

		Picasso.with(RootApplication.getInstance())
				.load(listImages.get(0))
				.placeholder(R.drawable.gravatar_image).into(imageView(0));

		try {
			textView(1).setText(item.getContent());

			textView(2).setText(DateUtils.getCreateTime(Long.valueOf(item.getCreateTime())));

			((RatingBar) view(3)).setRating(Float.valueOf(item.getScore()));

			textView(4).setText(tags.indexOf(Integer.valueOf(item.getTags())));
		} catch (Exception e) {

		}

	}

	static List<String> tags = new ArrayList<String>();
	static {
		tags.add("吃喝");
		tags.add("风景");
		tags.add("娱乐");
		tags.add("住宿");
	}
}