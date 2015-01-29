/**
 * @Package com.manyouren.android.ui.plan    
 * @Title: PlanCommentAdapter.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-9-4 下午3:39:52 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.plan;

import java.util.List;

import android.util.Log;
import android.view.LayoutInflater;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.entity.PlanCommentEntity;
import com.manyouren.manyouren.service.PicassoService;
import com.manyouren.manyouren.ui.AlternatingColorListAdapter;
import com.manyouren.manyouren.ui.CircleTransform;
import com.manyouren.manyouren.util.DateUtils;
import com.squareup.picasso.Picasso;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-9-4 下午3:39:52
 * 
 */
public class PlanCommentAdapter extends
		AlternatingColorListAdapter<PlanCommentEntity> {

	public PlanCommentAdapter(final LayoutInflater inflater,
			final List<PlanCommentEntity> items) {
		super(R.layout.plan_comment_item, inflater, items);
	}

	@Override
	protected int[] getChildViewIds() {
		return new int[] { R.id.iv_avatar, R.id.tv_name, R.id.iv_gender,
				  R.id.tv_content, R.id.tv_time };
	}
	
	@Override
	protected void update(final int position, final PlanCommentEntity item) {
		super.update(position, item);
		
		PicassoService.setCirclePhoto(UserController.getAvatarDiff(item.getAvatar()), imageView(0));
		
		setText(1, item.getUserName());
		
		setText(3, item.getContent());
		
		setText(4, DateUtils.getCreateTime(item.getCreateTime()));
		
	}

}
