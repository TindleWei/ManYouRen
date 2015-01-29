/**
 * @Package com.manyouren.android.ui.discovery    
 * @Title: PeopleNearbyActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-10 下午6:21:09 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.discovery.old;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.PreferConfig;
import com.manyouren.manyouren.controller.DiscoveryController;
import com.manyouren.manyouren.entity.UserNearbyEntity;
import com.manyouren.manyouren.service.greendao.GreenUser;
import com.manyouren.manyouren.ui.HeaderFooterListAdapter;
import com.manyouren.manyouren.ui.ThrowableLoader;
import com.manyouren.manyouren.ui.discovery.NearbyFilterDialog;
import com.manyouren.manyouren.ui.discovery.PeopleNearbyAdapter;
import com.manyouren.manyouren.ui.user.UserActivity;
import com.manyouren.manyouren.util.Logot;
import com.manyouren.manyouren.util.PreferenceUtils;
import com.manyouren.manyouren.util.ScreenUtils;

import de.greenrobot.daoexample.User;

/**
 * 
 * @author firefist_wei
 * @date 2014-7-10 下午6:21:09
 * 
 */
public class PeopleNearbyActivity extends NearbyPullListActivity<UserNearbyEntity>
		implements View.OnClickListener {

	private static final String TAG = "Animator";

	PeopleNearbyActivity instance = null;

	Context context = null;

	private static final int MENUITEM_PEOPLE_FILTER = 11002;

	NearbyStatusPopupWindow statusPopupWindow;

	int mWidth;

	int mHeaderHeight;

	ImageView iv_controller;

	LinearLayout layout_bottom;

	boolean isPanelShown = false;

	protected HeaderFooterListAdapter<PeopleNearbyAdapter> adapter;

	GetDataTask getDataTask = null;

	public static int FILTER_SEX = -1;

	public static int FILTER_LOC = -1;

	private ObjectAnimator objectAnimator;

	private AnimatorSet animatorSet;

	private ValueAnimator valueAnimator;
	
	private GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		instance = this;
		context = this;
		
		setActionBar("捡人");
		initView();
		initEvent();
		init();
	}

	@Override
	protected void initView() {
		gridView = (GridView)findViewById(R.id.gv_status);

		gridView.setAdapter(new StatusGridViewAdapter(context));
	}

	@Override
	protected void initEvent() {
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				panelOff();
				Toast.makeText(context, position+"",1000).show();
			}		
		});
		
	}

	@Override
	protected void init() {
		initFilter();
		initBottomPanel();

	}

	private void initFilter() {

		FILTER_SEX = PreferenceUtils.getInt(context,
				PreferConfig.PREFER_NEARBY_FILTER_SEX, -1);
		FILTER_LOC = PreferenceUtils.getInt(context,
				PreferConfig.PREFER_NEARBY_FILTER_LOC, -1);
	}

	private void initBottomPanel() {

		iv_controller = (ImageView) findViewById(R.id.iv_controller);

		layout_bottom = (LinearLayout) findViewById(R.id.layout_bottom);

		// animatorSet = (AnimatorSet)AnimatorInflater.loadAnimator(this,
		// R.animator.animator_panel);
		// animatorSet.setTarget(layout_bottom);

		layout_bottom.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});

		iv_controller.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isPanelShown == false) {

					animatorSet = new AnimatorSet();
					animatorSet.playTogether(ObjectAnimator.ofFloat(
							layout_bottom, "alpha", 0, 1), ObjectAnimator
							.ofFloat(layout_bottom, "translationY", 800f, 0f));
					animatorSet.setDuration(1000);
					animatorSet.setInterpolator(new OvershootInterpolator());

					objectAnimator = ObjectAnimator.ofFloat(iv_controller,
							"rotation", 0f, 90f);
					objectAnimator.setDuration(1000);
					objectAnimator.setInterpolator(new OvershootInterpolator());

					animatorSet.start();
					objectAnimator.start();
					layout_bottom.setVisibility(View.VISIBLE);

					isPanelShown = true;

				} else {
					panelOff();
				}
			}
		});
	}
	
	public void panelOff(){
		if (isPanelShown == false) {
			return;
		}
		animatorSet = new AnimatorSet();
		animatorSet.playTogether(ObjectAnimator.ofFloat(
				layout_bottom, "alpha", 1, 0), ObjectAnimator
				.ofFloat(layout_bottom, "translationY", 0f, 800f));
		animatorSet.setDuration(600);
		animatorSet.setInterpolator(new AnticipateInterpolator());

		objectAnimator = ObjectAnimator.ofFloat(iv_controller,
				"rotation", 90f, 0f);
		objectAnimator.setDuration(600);
		objectAnimator
				.setInterpolator(new AnticipateInterpolator());

		animatorSet.start();
		objectAnimator.start();
		layout_bottom.postDelayed(new Runnable() {

			@Override
			public void run() {
				layout_bottom.setVisibility(View.VISIBLE);

			}
		}, 100);

		isPanelShown = false;
	}

	@Override
	protected void configureList(Activity activity, ListView listView) {

		adapter = new HeaderFooterListAdapter<PeopleNearbyAdapter>(listView,
				new PeopleNearbyAdapter(getLayoutInflater(), items));

		listView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem done = menu.add(0, MENUITEM_PEOPLE_FILTER, 0, "FILTER");
		done.setIcon(this.getResources().getDrawable(
				R.drawable.ic_action_filter));
		done.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		// This is the home button in the top left corner of the screen.
		case MENUITEM_PEOPLE_FILTER:

			Dialog dialog = new NearbyFilterDialog(context,
					handler);

			Window dialogWindow = dialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.TOP);
			dialogWindow.setAttributes(lp);

			dialog.show();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public Loader<List<UserNearbyEntity>> onCreateLoader(int id, Bundle args) {

		return new ThrowableLoader<List<UserNearbyEntity>>(this, items) {

			@Override
			public List<UserNearbyEntity> loadData() throws Exception {
				if (instance != null) {

					// MyLocation.getLocation(context);
					setListShown(true);
					
					progressBar.setVisibility(View.VISIBLE);

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							fetchData();
						}
					});
					return DiscoveryController.nearbyList;

				} else {
					return Collections.emptyList();
				}
			}
		};
	}

	@Override
	protected int getErrorMessage(Exception exception) {
		return R.string.error_loading_news;
	}

	@Override
	public void onListDropDown() {
		getDataTask = new GetDataTask(true);
		getDataTask.execute();

	}

	@Override
	public void onListLoadMore() {
		getDataTask = new GetDataTask(false);
		getDataTask.execute();

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

			}
			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					if (isDropDown == true) {
						fetchData();
					} else {
						fetchData();
					}
				}
			});
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {

			if (isDropDown) {
				//adapter.getWrappedAdapter().notifyDataSetChanged();

				listView.onRefreshComplete();
			} else {
				//adapter.getWrappedAdapter().notifyDataSetChanged();

				/*
				 * if (moreDataCount >= MORE_DATA_MAX_COUNT) {
				 * listView.setHasMore(false); }
				 */
				listView.onRefreshComplete();
			}
			super.onPostExecute(result);
		}
	}

	public void onListItemClick(ListView l, View v, int position, long id) {

		UserNearbyEntity user = ((UserNearbyEntity) l.getItemAtPosition(position));

		startActivity(new Intent(context, UserActivity.class).putExtra(
				"UserId", user.getUserId() + "").putExtra(
				"UserAvatar", user.getAvatar0())
				.putExtra("UserName", user.getUsername()));

		overridePendingTransition(R.anim.left_in, R.anim.left_out);
	}

	@Override
	public void onDestroy() {
		listView.setAdapter(null);
		if (getDataTask != null)
			getDataTask.cancel(true);

		super.onDestroy();
	}

	public void fetchData() {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();

		if (FILTER_SEX == -1)
			hashMap.put("SearchForm[gender]", "");
		else
			hashMap.put("SearchForm[gender]", FILTER_SEX + "");

		if (FILTER_LOC == -1)
			;
			//hashMap.put("SearchForm[residence]", "");
		else
			hashMap.put("SearchForm[residence]", FILTER_LOC + "");
		
		Logot.outError("NEARY",hashMap.toString());

		DiscoveryController.getNearbyPeople(context, hashMap, handler);
	}

	Handler handler = new Handler() {
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case -1: // fail
				if (progressBar != null)
					if (progressBar.isShown()) {
						hide(progressBar);
					}
				
				if(items.isEmpty()){
					TextView tv = new TextView(PeopleNearbyActivity.this);
					tv.setGravity(Gravity.CENTER);
					tv.setText("没有数据，下拉看看");
					tv.setTextColor(0xff909090);
					tv.setPadding(0, 0, 0, ScreenUtils.getIntPixels(context, 60));
					tv.setTextSize(14);
					listView.setEmptyView(tv);
				}
				break;

			case 0: // finish
				
				// MyLocation.stopLocation();
				break;

			case 1: // success
				if (progressBar != null)
					if (progressBar.isShown()) {
						hide(progressBar);
					}

				items = DiscoveryController.nearbyList;

				// remove user himself
				/*
				 * NearbyUser himself = null; for (NearbyUser nearbyUser :
				 * items) { if (String.valueOf(nearbyUser.getUserId()).equals(
				 * Constants.USER_ID)) { himself = nearbyUser; } }
				 */

				adapter.getWrappedAdapter().setItems(items.toArray());
				adapter.getWrappedAdapter().notifyDataSetChanged();

				break;
			case 3: // empty
				if (progressBar != null)
					if (progressBar.isShown()) {
						hide(progressBar);
					}
				
				items = Collections.emptyList();

				adapter.getWrappedAdapter().setItems(items.toArray());
				adapter.getWrappedAdapter().notifyDataSetChanged();
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {

	}

}

 class StatusGridViewAdapter extends BaseAdapter {

	private Context context;

	private int size = 0;

	public StatusGridViewAdapter(Context context) {
		this.context = context;
	}

	public StatusGridViewAdapter(Context context, int size) {
		this.context = context;
		this.size = size;

	}

	@Override
	public int getCount() {
		if (size != 0) {
			return size;
		}

		return mPhotoIds.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		Holder holder = null;

		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.gridview_status_item, null);

			// @date: 6/24
			// to solve gridview height problem
			// http://stackoverflow.com/questions/16819135/set-fixed-gridview-row-height
//			int rowHigh = ScreenUtils.getIntPixels(context, 100);
//			view.setLayoutParams(new GridView.LayoutParams(
//					GridView.AUTO_FIT, rowHigh));

			holder = new Holder();
			holder.image = (ImageView) view.findViewById(R.id.iv_photo);
			holder.text = (TextView) view.findViewById(R.id.tv_tag);
			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();
		}

		holder.image.setImageDrawable(context.getResources().getDrawable(
				mPhotoIds[position]));
		holder.text.setText(mTags[position]);

		return view;
	}

	class Holder {
		ImageView image;
		TextView text;
	}

	public Integer[] mPhotoIds = { R.drawable.icon_nearby_none,
			R.drawable.icon_nearby_food, R.drawable.icon_nearby_photo, 
			R.drawable.icon_nearby_wine, R.drawable.icon_nearby_shop, 
			R.drawable.icon_nearby_music, R.drawable.icon_nearby_vacation, 
			R.drawable.icon_nearby_bike, R.drawable.icon_nearby_travel,
			R.drawable.icon_nearby_travel,R.drawable.icon_nearby_travel,
			R.drawable.icon_nearby_travel,R.drawable.icon_nearby_travel};

	public String[] mTags = { "找美食小吃", "找美食小吃", "游名胜古迹", "体验当地酒吧",
			"逛公园美景", "寻求当地旅游建议", "摄个影拍个照", "周边泡个温泉度个假", "短途自驾游",
			"登上山顶放声呐喊","下午茶、喝个咖啡","电影、话剧、音乐剧","徒步、单车旅行"};

}
