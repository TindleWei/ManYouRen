/**
 * @Package com.manyouren.android.core.chat    
 * @Title: ChatFragment.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-6-17 上午10:41:48 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.chat;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.github.kevinsawicki.wishlist.Toaster;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.controller.ChatController;
import com.manyouren.manyouren.entity.ChatEntity;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.service.greendao.GreenChat;
import com.manyouren.manyouren.service.jpush.JPushUtils;
import com.manyouren.manyouren.ui.HeaderFooterListAdapter;
import com.manyouren.manyouren.ui.ItemPullListFragment;
import com.manyouren.manyouren.ui.ItemPullListFragment2;
import com.manyouren.manyouren.ui.ThrowableLoader;
import com.manyouren.manyouren.ui.user.UserAddActivity;
import com.manyouren.manyouren.util.Ln;
import com.manyouren.manyouren.util.Logot;

/**
 * @author firefist_wei
 * @date 2014-6-17 上午10:41:48
 * 
 */
@Deprecated
public class ChatFragmentOld extends ItemPullListFragment2<ChatEntity> {

	public static final String TAG = "ChatFragment";

	RelativeLayout titleView = null;

	ImageView iv_add = null;

	Context context = null;

	GetDataTask getDataTask = null;

	private int Page = 1;

	protected HeaderFooterListAdapter<ChatListAdapter> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getActivity();
	}

	@Override
	protected void configureList(Activity activity, ListView listView) {
		super.configureList(activity, listView);
		Logot.outError(TAG, "configureList");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Logot.outError(TAG, "onActivityCreated");
		initView();
		initEvent();
		init();
	}

	@Override
	protected void initView() {
		
		titleView = (RelativeLayout) getActivity()
				.findViewById(R.id.rl_title);
//		titleView.addView(getActivity().getLayoutInflater().inflate(
//				R.layout.headerview_chat, null));
//  	iv_add = (ImageView) getActivity().findViewById(R.id.iv_add);
		
	}

	@Override
	protected void init() {
		listView.setMode(Mode.PULL_FROM_START);

		adapter = new HeaderFooterListAdapter<ChatListAdapter>(getListView(),
				new ChatListAdapter(getActivity().getLayoutInflater(), items));
		listView.setAdapter(adapter);
		setListShown(true, false);
	}

	@Override
	protected void initEvent() {

//		iv_add.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				startActivity(new Intent(getActivity(), UserAddActivity.class));
//				getActivity().overridePendingTransition(R.anim.left_in,
//						R.anim.left_out);
//			}
//		});

		listView.getRefreshableView().setOnItemLongClickListener(
				new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {

						final ChatEntity chatEntity = ((ChatEntity) listView
								.getRefreshableView().getItemAtPosition(
										position));
						final int pos = position
								- listView.getRefreshableView()
										.getHeaderViewsCount();

						new AlertDialog.Builder(context)
								.setTitle(
										"与" + chatEntity.getUserName() + "的对话")
								.setItems(new String[] { "置顶对话", "删除对话" },
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												switch (which) {
												case 0:
													if (ChatController
															.topChat(context,
																	chatEntity)) {
														chatHandler
																.sendEmptyMessageDelayed(
																		1, 500);
														Toaster.showShort(
																getActivity(),
																"置顶成功");
													} else {
														Toaster.showShort(
																getActivity(),
																"置顶失败");
													}
													break;
												case 1:
													if (ChatController
															.deleteChat(
																	context,
																	chatEntity)) {
														items.remove(pos);
														adapter.getWrappedAdapter()
																.setItems(
																		items.toArray());
														adapter.getWrappedAdapter()
																.notifyDataSetChanged();

														Toaster.showShort(
																getActivity(),
																"删除成功");
													} else {
														Toaster.showShort(
																getActivity(),
																"删除失败");
													}
													break;
												}
											}
										}).create().show();
						return false;
					}
				});
	}

	@Override
	public Loader<List<ChatEntity>> onCreateLoader(final int id,
			final Bundle args) {
		return new ThrowableLoader<List<ChatEntity>>(getActivity(), items) {

			@Override
			public List<ChatEntity> loadData() throws Exception {

				try {
					if (getActivity() != null) {
						items = ChatController.getLocalChats(context, Page);
						return items;
					} else {
						return Collections.emptyList();
					}
				} catch (Exception e) {
					Activity activity = getActivity();
					if (activity != null)
						activity.finish();
					return items;
				}
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<List<ChatEntity>> loader,
			List<ChatEntity> items) {
		super.onLoadFinished(loader, items);
		// adapter.getWrappedAdapter().setItems(items.toArray());
		// showList();
	}

	@Override
	public void onListItemClick(final ListView list, final View view,
			final int position, final long id) {
		ChatEntity chatEntity = ((ChatEntity) list.getItemAtPosition(position));

		de.greenrobot.daoexample.Chat chat = GreenChat.getInstance(context)
				.getChatById(chatEntity.getChatId());
		chat.setUnreadNum(0);
		GreenChat.getInstance(context).saveChat(chat);

		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(chatEntity.getToUserId());
		userEntity.setAvatar0(chatEntity.getAvatar());
		userEntity.setUserName(chatEntity.getUserName());

		Intent intent = new Intent(getActivity(), ChatActivity.class).putExtra(
				"ToUserId", String.valueOf(chatEntity.getToUserId())).putExtra("UserEntity",
				userEntity);
		startActivity(intent);
		getActivity()
				.overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	@Override
	protected int getErrorMessage(Exception exception) {
		return R.string.error_loading_checkins;
	}

	@Override
	public void onListDropDown() {
		new GetDataTask(true).execute();
	}

	@Override
	public void onListLoadMore() {
		new GetDataTask(false).execute();
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		private boolean isDropDown;

		public GetDataTask(boolean isDropDown) {
			this.isDropDown = isDropDown;
		}

		@Override
		protected String[] doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

				if (isDropDown == true) {
					Page = 1;
					chatHandler.sendEmptyMessage(1);
				} else {
					Page++;
					chatHandler.sendEmptyMessage(2);
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {

			if (listView != null)
				listView.onRefreshComplete();
			super.onPostExecute(result);
		}
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
				items = ChatController.getLocalChats(context, 1);
				adapter.getWrappedAdapter().setItems(items.toArray());
				adapter.getWrappedAdapter().notifyDataSetChanged();

				break;
			case 2:
				items.addAll(ChatController.getLocalChats(context, Page));

				adapter.getWrappedAdapter().setItems(items.toArray());
				adapter.getWrappedAdapter().notifyDataSetChanged();
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

		context = getActivity();
		chatHandler.sendEmptyMessage(1);

	}

	@Override
	public void onDestroyView() {
		listView.setAdapter(null);
		super.onDestroyView();
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
		if (listView != null)
			listView.onRefreshComplete();
		if (getDataTask != null)
			getDataTask.cancel(true);

		super.onDestroy();
	}

	/**
	 * message from HomeTabActivity
	 * 
	 * @return void
	 */
	public static void showNewMessage() {

	}

}
