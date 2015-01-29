package com.manyouren.manyouren.ui.chatnew.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.service.PicassoService;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.service.greendao.GreenUser;
import com.manyouren.manyouren.ui.chatnew.avobject.User;
import com.manyouren.manyouren.ui.chatnew.entity.Msg;
import com.manyouren.manyouren.ui.chatnew.service.ChatService;
import com.manyouren.manyouren.ui.chatnew.service.EmotionService;
import com.manyouren.manyouren.ui.chatnew.service.UserService;
import com.manyouren.manyouren.ui.chatnew.ui.activity.ChatActivity;
import com.manyouren.manyouren.ui.chatnew.ui.activity.ImageBrowerActivity;
import com.manyouren.manyouren.ui.chatnew.ui.activity.LocationActivity;
import com.manyouren.manyouren.ui.chatnew.ui.view.PlayButton;
import com.manyouren.manyouren.ui.chatnew.ui.view.ViewHolder;
import com.manyouren.manyouren.ui.chatnew.util.PathUtils;
import com.manyouren.manyouren.ui.chatnew.util.PhotoUtil;
import com.manyouren.manyouren.ui.chatnew.util.TimeUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.List;

public class ChatMsgAdapter extends BaseListAdapter<Msg> {
	int msgViewTypes = 8;

	public static interface MsgViewType {
		int COME_TEXT = 0;
		int TO_TEXT = 1;
		int COME_IMAGE = 2;
		int TO_IMAGE = 3;
		int COME_AUDIO = 4;
		int TO_AUDIO = 5;
		int COME_LOCATION = 6;
		int TO_LOCATION = 7;
	}

	public ChatMsgAdapter(Context ctx, List<Msg> datas) {
		super(ctx, datas);
	}

	public int getItemPosById(String objectId) {
		for (int i = 0; i < getCount(); i++) {
			Msg itemMsg = datas.get(i);
			if (itemMsg.getObjectId().equals(objectId)) {
				return i;
			}
		}
		return -1;
	}

	public Msg getItem(String objectId) {
		for (Msg msg : datas) {
			if (msg.getObjectId().equals(objectId)) {
				return msg;
			}
		}
		return null;
	}

	public int getItemViewType(int position) {
		Msg entity = datas.get(position);
		boolean comeMsg = entity.isComeMessage();
		Msg.Type type = entity.getType();
		switch (type) {
		case Text:
			return comeMsg ? MsgViewType.COME_TEXT : MsgViewType.TO_TEXT;
		case Image:
			return comeMsg ? MsgViewType.COME_IMAGE : MsgViewType.TO_IMAGE;
		case Audio:
			return comeMsg ? MsgViewType.COME_AUDIO : MsgViewType.TO_AUDIO;
		case Location:
			return comeMsg ? MsgViewType.COME_LOCATION
					: MsgViewType.TO_LOCATION;
		}
		throw new IllegalStateException("position's type is wrong");
	}

	public int getViewTypeCount() {
		return msgViewTypes;
	}

	public View getView(int position, View conView, ViewGroup parent) {
		Msg msg = datas.get(position);
		int itemViewType = getItemViewType(position);
		boolean isComMsg = msg.isComeMessage();
		if (conView == null) {
			conView = createViewByType(itemViewType);
		}
		TextView sendTimeView = ViewHolder.findViewById(conView,
				R.id.sendTimeView);
		TextView contentView = ViewHolder.findViewById(conView,
				R.id.textContent);
		ImageView imageView = ViewHolder.findViewById(conView, R.id.imageView);
		ImageView avatarView = ViewHolder.findViewById(conView, R.id.avatar);
		PlayButton playBtn = ViewHolder.findViewById(conView, R.id.playBtn);
		TextView locationView = ViewHolder.findViewById(conView,
				R.id.locationView);

		View statusSendFailed = ViewHolder.findViewById(conView,
				R.id.status_send_failed);
		View statusSendSucceed = ViewHolder.findViewById(conView,
				R.id.status_send_succeed);
		View statusSendStart = ViewHolder.findViewById(conView,
				R.id.status_send_start);

		// timestamp
		if (position == 0
				|| TimeUtils.haveTimeGap(
						datas.get(position - 1).getTimestamp(),
						msg.getTimestamp())) {
			sendTimeView.setVisibility(View.VISIBLE);
			sendTimeView.setText(TimeUtils.millisecs2DateString(msg
					.getTimestamp()));
		} else {
			sendTimeView.setVisibility(View.GONE);
		}

		String fromPeerId = msg.getFromPeerId();
		User user = RootApplication.lookupUser(fromPeerId);
		if (user == null) {
			throw new RuntimeException("cannot find user");
		}
		if(msg.getFromUserId().equals(PreferenceHelper.getUserId())){
			PicassoService.setCirclePhoto(
					UserController.getAvatarDiff(GreenUser.getInstance(RootApplication.getInstance())
							.getUserById(Long.valueOf(PreferenceHelper.getUserId())).getAvatar0()),
					avatarView);
		} else {
			PicassoService.setCirclePhoto(
					UserController.getAvatarDiff(msg.getFromAvatar()),
					avatarView);
		}
		

		Msg.Type type = msg.getType();
		if (type == Msg.Type.Text) {
			contentView.setText(EmotionService.replace(ctx, msg.getContent()));
		} else if (type == Msg.Type.Image) {
			String localPath = PathUtils.getChatFileDir() + msg.getObjectId();
			String url = msg.getContent();
			displayImageByUri(imageView, localPath, url);
			setImageOnClickListener(localPath, url, imageView);
		} else if (type == Msg.Type.Audio) {
			initPlayBtn(msg, playBtn);
		} else if (type == Msg.Type.Location) {
			setLocationView(msg, locationView);
		}
		if (isComMsg == false) {
			hideStatusViews(statusSendStart, statusSendFailed,
					statusSendSucceed);
			setSendFailedBtnListener(statusSendFailed, msg);
			switch (msg.getStatus()) {
			case SendFailed:
				statusSendFailed.setVisibility(View.VISIBLE);
				break;
			case SendSucceed:
				statusSendSucceed.setVisibility(View.VISIBLE);
				break;
			case SendStart:
				statusSendStart.setVisibility(View.VISIBLE);
				break;
			}
			if (ChatActivity.singleChat == false) {
				statusSendSucceed.setVisibility(View.GONE);
			}
		}
		return conView;
	}

	private void setSendFailedBtnListener(View statusSendFailed, final Msg msg) {
		statusSendFailed.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ChatService.resendMsg(msg);
				notifyDataSetChanged();
			}
		});
	}

	private void hideStatusViews(View statusSendStart, View statusSendFailed,
			View statusSendSucceed) {
		statusSendFailed.setVisibility(View.GONE);
		statusSendStart.setVisibility(View.GONE);
		statusSendSucceed.setVisibility(View.GONE);
	}

	public void setLocationView(Msg msg, TextView locationView) {
		try {
			String content = msg.getContent();
			if (content != null && !content.equals("")) {
				String address = content.split("&")[0];
				final String latitude = content.split("&")[1];
				final String longtitude = content.split("&")[2];
				locationView.setText(address);
				locationView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(ctx, LocationActivity.class);
						intent.putExtra("type", "scan");
						intent.putExtra("latitude",
								Double.parseDouble(latitude));
						intent.putExtra("longtitude",
								Double.parseDouble(longtitude));
						ctx.startActivity(intent);
					}
				});
			}
		} catch (Exception e) {
		}
	}

	private void initPlayBtn(Msg msg, PlayButton playBtn) {
		playBtn.setPath(msg.getAudioPath());
	}

	private void setImageOnClickListener(final String path, final String url,
			ImageView imageView) {
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ctx, ImageBrowerActivity.class);
				intent.putExtra("path", path);
				intent.putExtra("url", url);
				ctx.startActivity(intent);
			}
		});
	}

	public static void displayImageByUri(ImageView imageView, String localPath,
			String url) {
		File file = new File(localPath);
		ImageLoader imageLoader = UserService.imageLoader;
		if (file.exists()) {
			imageLoader.displayImage("file://" + localPath, imageView,
					PhotoUtil.normalImageOptions);
		} else {
			imageLoader.displayImage(url, imageView,
					PhotoUtil.normalImageOptions);
		}
	}

	public View createViewByType(int itemViewType) {
		int[] types = new int[] { MsgViewType.COME_TEXT, MsgViewType.TO_TEXT,
				MsgViewType.COME_IMAGE, MsgViewType.TO_IMAGE,
				MsgViewType.COME_AUDIO, MsgViewType.TO_AUDIO,
				MsgViewType.COME_LOCATION, MsgViewType.TO_LOCATION };
		int[] layoutIds = new int[] { R.layout.chat_item_msg_text_left,
				R.layout.chat_item_msg_text_right,
				R.layout.chat_item_msg_image_left,
				R.layout.chat_item_msg_image_right,
				R.layout.chat_item_msg_audio_left,
				R.layout.chat_item_msg_audio_right,
				R.layout.chat_item_msg_location_left,
				R.layout.chat_item_msg_location_right };
		int i;
		for (i = 0; i < types.length; i++) {
			if (itemViewType == types[i]) {
				break;
			}
		}
		return inflater.inflate(layoutIds[i], null);
	}
}
