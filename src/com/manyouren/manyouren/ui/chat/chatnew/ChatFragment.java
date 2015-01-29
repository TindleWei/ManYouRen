package com.manyouren.manyouren.ui.chat.chatnew;

import roboguice.inject.InjectView;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

import com.avos.avoscloud.AVException;
import com.github.kevinsawicki.wishlist.Toaster;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;
import com.manyouren.manyouren.service.jpush.JPushUtils;
import com.manyouren.manyouren.ui.chat.ChatFragmentOld.ChatReceiver;
import com.manyouren.manyouren.ui.chatnew.db.DBMsg;
import com.manyouren.manyouren.ui.chatnew.entity.Msg;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseListFragment;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.controller.ChatController;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.entity.ChatEntity;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.ui.chatnew.adapter.RecentMessageAdapter;
import com.manyouren.manyouren.ui.chatnew.entity.Conversation;
import com.manyouren.manyouren.ui.chatnew.service.ChatService;
import com.manyouren.manyouren.ui.chatnew.ui.activity.AddFriendActivity;
import com.manyouren.manyouren.ui.chatnew.ui.activity.ChatActivity;
import com.manyouren.manyouren.util.Logot;

public class ChatFragment extends BaseListFragment<Conversation> {

	private static final String TAG = "ChatFragment";

	private RecentMessageAdapter mAdapter = null;

	@InjectView(R.id.iv_add_ppl)
	ImageView iv_add_ppl;
	
	@InjectView(R.id.tv_title_chat)
	TextView tv_title_chat;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_chat, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		initEvent();
		init();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(AsyncHttpManager.checkNetwork(context)==true)
			tv_title_chat.setText("消息");
		else
			tv_title_chat.setText("消息-未连接");
	}

	@Override
	protected void initView() {
	}

	@Override
	protected void initEvent() {

		iv_add_ppl.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(context, AddFriendActivity.class));
				activity.overridePendingTransition(R.anim.left_in,
						R.anim.left_out);
			}
		});

		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				final Conversation conver = ((Conversation) mListView
						.getItemAtPosition(position));
				final int pos = position - mListView.getHeaderViewsCount();

				String title = conver.msg.getFromUserId().equals(
						PreferenceHelper.getUserId()) ? conver.msg.getToName()
						: conver.msg.getFromName();
				new AlertDialog.Builder(context)
						.setTitle("与" + title + "的对话")
						.setItems(new String[] { "删除对话" },
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										switch (which) {
										case 0:
											DBMsg.deleteMsg(conver.msg);
											items.remove(pos);
											mAdapter.notifyDataSetChanged();
											break;
										}
									}
								}).create().show();

				return false;
			}
		});
	}

	@Override
	protected void init() {
		hideProgressBar();
		mAdapter = new RecentMessageAdapter(getActivity());
		mListView.setAdapter(mAdapter);
		mListView.setPullRefreshEnable(false);

		hideProgressBar();
		if (items.size() == 0) {
			Logot.outError("ChatFragment", "init refresh");
			showProgressBar();
			onRefresh();
		}

	}

	@Override
	protected void onListItemClick(ListView parent, View view, int position,
			long id) {

		Conversation recent = (Conversation) mAdapter.getItem(position
				- mListView.getHeaderViewsCount());

		if (recent.msg.getRoomType() == Msg.RoomType.Single) {

			UserEntity userEntity = new UserEntity();
			Intent intent = new Intent(context, ChatActivity.class);

			if (recent.msg.getFromUserId().equals(PreferenceHelper.getUserId())) {
				userEntity.setUserId(Long.valueOf(recent.msg.getToUserId()));
				userEntity.setUserName(recent.msg.getToName());
				userEntity.setAvatar0(recent.msg.getToAvatar());
				userEntity.setObjectId(recent.msg.getToPeerId());

				intent.putExtra(ChatActivity.CHAT_USER_ID,
						recent.msg.getToPeerId());
				intent.putExtra("UserEntity", userEntity);
			} else {
				userEntity.setUserId(Long.valueOf(recent.msg.getFromUserId()));
				userEntity.setUserName(recent.msg.getFromName());
				userEntity.setAvatar0(recent.msg.getFromAvatar());
				userEntity.setObjectId(recent.msg.getFromPeerId());

				intent.putExtra(ChatActivity.CHAT_USER_ID,
						recent.msg.getFromPeerId());
				intent.putExtra("UserEntity", userEntity);
			}

			startActivity(intent);
			activity.overridePendingTransition(R.anim.left_in, R.anim.left_out);
		} else {
			ChatActivity.goGroupChat(activity, recent.chatGroup.getObjectId());
		}
	}

	private boolean hidden;

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.hidden = hidden;
		Logot.outError("Hide", "" + hidden);
		if (!hidden) {
			onRefresh();
		}
	}

	@Override
	public void onRefresh() {
		if (AsyncHttpManager.checkNetwork(context) == false) {
			Toast.makeText(context, "网络未连接", 600);
			return;
		}
		loadData(1);
	}

	@Override
	public void onLoadMore() {
		mPage++;
		loadData(mPage);
	}

	private void loadData(final int page) {
		try {
			items = ChatService.getConversationsAndCache();
			Logot.outError(TAG, "loadData item size " + items.size());
		} catch (AVException e) {
			e.printStackTrace();
		} finally {
			loadFinish();
		}
	}

	public void loadFinish() {
		hideProgressBar();

		if (items.size() < 10) {
			setPullLoad(false);
		} else {
			setPullLoad(true);
		}
		mAdapter.setDatas(items);
		mAdapter.notifyDataSetChanged();

		mListView.stopRefresh();
		mListView.stopLoadMore();
	}
	
	private ChatReceiver mChatReceiver;

	public void registerMessageReceiver() {
		mChatReceiver = new ChatReceiver();
		IntentFilter filter = new IntentFilter();
		// filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.setPriority(1000);
		filter.addAction(CHAT_RECEIVED_ACTION);
		getActivity().registerReceiver(mChatReceiver, filter);
	}

	public static final String CHAT_RECEIVED_ACTION = "CHAT_RECEIVED_ACTION";

	public class ChatReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (CHAT_RECEIVED_ACTION.equals(intent.getAction())) {

				chatHandler.sendEmptyMessage(1);
			}
		}
	}
	
	private Handler chatHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case 1: // 重新加载
				try {
					items = ChatService.getConversationsAndCache();
				} catch (AVException e) {
					e.printStackTrace();
				}
				mAdapter.setDatas(items);
				mAdapter.notifyDataSetChanged();

				break;

			}
		};
	};
	
	@Override
	public void onResume() {
		super.onResume();
		Logot.outError(TAG, "onResume()");
		registerMessageReceiver();
		JPushUtils.isChatFragment = true;
		onRefresh();

	}

	@Override
	public void onPause() {
		JPushUtils.isChatFragment = false;
		super.onPause();
	}

	@Override
	public void onDestroy() {
		if (mChatReceiver != null)
			getActivity().unregisterReceiver(mChatReceiver);

		super.onDestroy();
	}


}
