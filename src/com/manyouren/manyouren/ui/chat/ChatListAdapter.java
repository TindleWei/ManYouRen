/**
 * @Package com.manyouren.android.core.chat    
 * @Title: ChatListAdapter.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-17 上午11:08:06 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.chat;

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.entity.ChatEntity;
import com.manyouren.manyouren.ui.AlternatingColorListAdapter;
import com.manyouren.manyouren.ui.CircleTransform;
import com.manyouren.manyouren.util.Logot;
import com.squareup.picasso.Picasso;

/**
 * 
 * @author firefist_wei
 * @date 2014-6-17 上午11:08:06
 * 
 */
public class ChatListAdapter extends AlternatingColorListAdapter<ChatEntity> {

	/**
	 * @param inflater
	 * @param items
	 * @param selectable
	 */
	public ChatListAdapter(final LayoutInflater inflater,
			final List<ChatEntity> items, final boolean selectable) {
		super(R.layout.chat_list_item, inflater, items, selectable);
	}

	/**
	 * @param inflater
	 * @param items
	 */
	public ChatListAdapter(final LayoutInflater inflater,
			final List<ChatEntity> items) {
		super(R.layout.chat_list_item, inflater, items);
	}

	@Override
	protected int[] getChildViewIds() {
		return new int[] { R.id.iv_avatar, R.id.tv_name, R.id.tv_date,
				R.id.tv_content, R.id.tv_unread_number };
	}

	@SuppressLint("NewApi")
	@Override
	protected void update(int position, ChatEntity item) {
		super.update(position, item);

		setText(1, item.getUserName());

		String time = (new SimpleDateFormat("MM月dd日 HH:mm").format(item
				.getTime()));
		setText(2, time);
		setText(3, item.getContent());

		Logot.outError("HEAD", "at chatListAdapter: " + item.getAvatar().toString());

		if (item.getAvatar() != null && item.getAvatar().length() > 0) {
			Picasso.with(RootApplication.getInstance()).load(item.getAvatar())
					.transform(new CircleTransform())
					.placeholder(R.drawable.gravatar_icon).into(imageView(0));
		}

		Logot.outError("BUG", item.getUnreadNum()+"");
		
		
		if (item.getUnreadNum() > 0) {
			textView(4).setVisibility(View.VISIBLE);
			setText(4, item.getUnreadNum()+"");
			if (item.getUnreadNum() > 9);
				textView(4).setBackgroundDrawable(
						RootApplication.getInstance().getResources()
								.getDrawable(R.drawable.chat_unread_num_oval));
		}else{
			textView(4).setVisibility(View.GONE);
		}

	}

}
