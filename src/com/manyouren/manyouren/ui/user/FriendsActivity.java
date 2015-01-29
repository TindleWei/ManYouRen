/**
 * @Package com.manyouren.android.ui    
 * @Title: UserFriendsActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-8-2 上午12:41:27 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.controller.FriendController;
import com.manyouren.manyouren.controller.UserController;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.ui.AlternatingColorListAdapter;
import com.manyouren.manyouren.ui.CircleTransform;
import com.manyouren.manyouren.ui.ItemPullListFragment;
import com.manyouren.manyouren.ui.ThrowableLoader;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.TabPageIndicator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

/**
 * 
 * @author firefist_wei
 * @date 2014-8-2 上午12:41:27
 * 
 */
public class FriendsActivity extends BaseActivity {
	private static final String[] CONTENT = new String[] { "朋友", "关注", "被关注" };

	private static final int TYPE_FREINDS = 0;
	private static final int TYPE_FOLLOW = 1;
	private static final int TYPE_FANS = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends);

		setActionBar("我的好友");

		FragmentPagerAdapter adapter = new IndicatorAdapter(
				getSupportFragmentManager());

		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);

		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
	}

	class IndicatorAdapter extends FragmentPagerAdapter {
		public IndicatorAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return FriendsFragment.newInstance(position);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position].toUpperCase();
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
	}
	
	/**
	 * This is Fragment
	 * 
	 * @author firefist_wei
	 * @date 2014-8-2 上午2:03:30
	 * 
	 */
	public static class FriendsFragment extends ItemPullListFragment<UserEntity> {

		protected FriendsAdapter adapter;
		
		int type = 0;

		public FriendsFragment(int type) {
			this.type = type;
		}

		public static FriendsFragment newInstance(int type) {
			FriendsFragment fragment = new FriendsFragment(type);

			return fragment;
		}
		
		@Override
		public void onResume() {
			super.onResume();
			
			if (type == 0)
				items = FriendController.friendsList;
			else if (type == 1)
				items = FriendController.followList;
			else if (type == 2)
				items = FriendController.fansList;

			if (listView != null) {
				adapter.setItems(
						items.toArray());
				adapter.notifyDataSetChanged();
			}
		}

		@Override
		public void onCreateOptionsMenu(Menu optionsMenu, MenuInflater inflater) {

		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			
			items = new ArrayList<UserEntity>();
			adapter = new FriendsAdapter(getActivity().getLayoutInflater(), items);
			listView.setAdapter(adapter);
		
		}

		@Override
		protected void configureList(Activity activity, ListView listView) {
			super.configureList(activity, listView);
		}

		@Override
		public Loader<List<UserEntity>> onCreateLoader(int arg0, Bundle arg1) {
			return new ThrowableLoader<List<UserEntity>>(getActivity(), items) {

				@Override
				public List<UserEntity> loadData() throws Exception {

					if (getActivity() != null) {

						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {

								switch (type) {
								case TYPE_FREINDS:
									FriendController.getFriendsList(
											getActivity(), TYPE_FREINDS,
											handler);
									break;
								case TYPE_FOLLOW:
									FriendController
											.getFriendsList(getActivity(),
													TYPE_FOLLOW, handler);
									break;
								case TYPE_FANS:
									FriendController.getFriendsList(
											getActivity(), TYPE_FANS, handler);
									break;
								}
							}
						});
						Thread.sleep(600);

						if (type == 0)
							items = FriendController.friendsList;
						else if (type == 1)
							items = FriendController.followList;
						else if (type == 2)
							items = FriendController.fansList;
						return items;

					} else {
						return Collections.emptyList();
					}
				}
			};
		}

		@Override
		public void onDestroyView() {
			//setListAdapter(null);

			super.onDestroyView();
		}

		public void onListItemClick(ListView l, View v, int position, long id) {
			UserEntity friends = ((UserEntity) l.getItemAtPosition(position));

			
			startActivity(new Intent(getActivity(), UserActivity.class)
					.putExtra("UserId", friends.getUserId() + "")
					.putExtra("UserAvatar", friends.getAvatar0())
					.putExtra("UserName", friends.getUserName()));

			getActivity().overridePendingTransition(R.anim.left_in,
					R.anim.left_out);

		}

		@Override
		protected int getErrorMessage(Exception exception) {
			return 0;
		}
		
		@Override
		public void onListDropDown() {
			switch (type) {
			case TYPE_FREINDS:
				FriendController.getFriendsList(
						getActivity(), TYPE_FREINDS,
						handler);
				break;
			case TYPE_FOLLOW:
				FriendController
						.getFriendsList(getActivity(),
								TYPE_FOLLOW, handler);
				break;
			case TYPE_FANS:
				FriendController.getFriendsList(
						getActivity(), TYPE_FANS, handler);
				break;
			}
		};

		Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case -1: // fail
					refreshFinished();
					break;
				case 1:
					refreshFinished();
					if (type == 0)
						items = FriendController.friendsList;
					else if (type == 1)
						items = FriendController.followList;
					else if (type == 2)
						items = FriendController.fansList;

					if (listView != null) {
						adapter.setItems(
								items.toArray());
						adapter
								.notifyDataSetChanged();
					}
					break;
				}
			}
		};
		
		public void refreshFinished() {
			if (progressBar != null)
				if (progressBar.isShown()) {
					hide(progressBar);
				}
			if (listView != null && listView.isRefreshing()) {
				listView.onRefreshComplete();
			}
		}

		@Override
		protected void initView() {
			
		}

		@Override
		protected void initEvent() {
			
		}

		@Override
		protected void init() {
			
		}
	}

	/**
	 * This is Adapter
	 * @author firefist_wei
	 * @date 2014-8-2 上午2:03:52
	 * 
	 */
	public static class FriendsAdapter extends
			AlternatingColorListAdapter<UserEntity> {

		public FriendsAdapter(final LayoutInflater inflater,
				final List<UserEntity> items, final boolean selectable) {
			super(R.layout.item_friendslist, inflater, items, selectable);
		}

		public FriendsAdapter(final LayoutInflater inflater,
				final List<UserEntity> items) {
			super(R.layout.item_friendslist, inflater, items);
		}

		@Override
		protected int[] getChildViewIds() {
			return new int[] { R.id.iv_friends_avatar, R.id.iv_friends_name };
		}

		@Override
		protected void update(final int position, final UserEntity item) {
			super.update(position, item);

			Picasso.with(RootApplication.getInstance())
					.load(UserController.getAvatarDiff(item.getAvatar0()))
					.transform(new CircleTransform())
					.placeholder(R.drawable.gravatar_icon).into(imageView(0));

			setText(1,item.getUserName());
		}
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}
}
