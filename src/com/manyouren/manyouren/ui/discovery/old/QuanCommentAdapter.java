/**
 * @Package com.manyouren.android.ui.discovery    
 * @Title: QuanCommentAdapter.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-9-14 下午5:54:12 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.discovery.old;

import java.util.List;

import android.view.LayoutInflater;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.entity.PhotoCommentEntity;
import com.manyouren.manyouren.service.PicassoService;
import com.manyouren.manyouren.ui.AlternatingColorListAdapter;
import com.manyouren.manyouren.util.DateUtils;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-9-14 下午5:54:12
 * 
 */
public class QuanCommentAdapter extends
		AlternatingColorListAdapter<PhotoCommentEntity> {

	public QuanCommentAdapter(final LayoutInflater inflater,
			final List<PhotoCommentEntity> items) {
		super(R.layout.plan_comment_item, inflater, items);
	}

	@Override
	protected int[] getChildViewIds() {
		return new int[] { R.id.iv_avatar, R.id.tv_name, R.id.iv_gender,
				R.id.tv_content, R.id.tv_time };
	}

	@Override
	protected void update(final int position, final PhotoCommentEntity item) {
		super.update(position, item);

		PicassoService.setCirclePhoto(
				UserController.getAvatarDiff(item.getAvatar0()), imageView(0));

		setText(1, item.getUsername());

		setText(3, item.getContent());

		setText(4, DateUtils.getCreateTime(item.getCreateTime()));

	}

}
