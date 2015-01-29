package com.manyouren.manyouren.ui.chatnew.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.service.PicassoService;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.service.greendao.GreenUser;
import com.manyouren.manyouren.ui.chatnew.avobject.ChatGroup;
import com.manyouren.manyouren.ui.chatnew.avobject.User;
import com.manyouren.manyouren.ui.chatnew.entity.Conversation;
import com.manyouren.manyouren.ui.chatnew.entity.Msg;
import com.manyouren.manyouren.ui.chatnew.service.EmotionService;
import com.manyouren.manyouren.ui.chatnew.ui.view.ViewHolder;
import com.manyouren.manyouren.ui.chatnew.util.PhotoUtil;
import com.manyouren.manyouren.ui.chatnew.util.TimeUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RecentMessageAdapter extends BaseListAdapter<Conversation> {

	private LayoutInflater inflater;
	private Context ctx;

	public RecentMessageAdapter(Context context) {
		super(context);
		this.ctx = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Conversation item = datas.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.conversation_item, parent,
					false);
		}
		ImageView recentAvatarView = ViewHolder.findViewById(convertView,
				R.id.iv_recent_avatar);
		TextView recentNameView = ViewHolder.findViewById(convertView,
				R.id.recent_name_text);
		TextView recentMsgView = ViewHolder.findViewById(convertView,
				R.id.recent_msg_text);
		TextView recentTimeView = ViewHolder.findViewById(convertView,
				R.id.recent_time_text);
		TextView recentUnreadView = ViewHolder.findViewById(convertView,
				R.id.recent_unread);

		Msg msg = item.msg;
		if (msg.getRoomType() == Msg.RoomType.Single) {
			// User user = item.toUser;
			
			if(msg.getFromUserId().equals(PreferenceHelper.getUserId())){
				PicassoService.setCirclePhoto(
						UserController.getAvatarDiff(msg.getToAvatar()),
						recentAvatarView);
				recentNameView.setText(msg.getToName());
			} else {
				PicassoService.setCirclePhoto(
						UserController.getAvatarDiff(msg.getFromAvatar()),
						recentAvatarView);
				recentNameView.setText(msg.getFromName());
			}	

			// String avatar = user.getAvatarUrl();
			// if (avatar != null && !avatar.equals("")) {
			// ImageLoader.getInstance().displayImage(avatar, recentAvatarView,
			// PhotoUtil.avatarImageOptions);
			// } else {
			// recentAvatarView.setImageResource(R.drawable.gravatar_image);
			// }		
			} else {
				ChatGroup chatGroup = item.chatGroup;
				recentNameView.setText(chatGroup.getTitle());
				recentAvatarView.setImageResource(R.drawable.gravatar_image);
			}

		recentTimeView.setText(TimeUtils.millisecs2DateString(msg
				.getTimestamp()));

		int num = 0;// unread count
		if (msg.getType() == Msg.Type.Text) {
			CharSequence spannableString = EmotionService.replace(ctx,
					msg.getContent());
			recentMsgView.setText(spannableString);

		} else if (msg.getType() == Msg.Type.Image) {
			recentMsgView.setText("["
					+ RootApplication.instance.getString(R.string.image) + "]");

		} else if (msg.getType() == Msg.Type.Location) {
			String all = msg.getContent();
			if (all != null && !all.equals("")) {
				String address = all.split("&")[0];
				recentMsgView.setText("["
						+ RootApplication.instance.getString(R.string.position)
						+ "]" + address);
			}
		} else if (msg.getType() == Msg.Type.Audio) {
			recentMsgView.setText("["
					+ RootApplication.instance.getString(R.string.audio) + "]");
		}

		if (num > 0) {
			recentUnreadView.setVisibility(View.VISIBLE);
			recentUnreadView.setText(num + "");
		} else {
			recentUnreadView.setVisibility(View.GONE);
		}
		return convertView;
	}
}
