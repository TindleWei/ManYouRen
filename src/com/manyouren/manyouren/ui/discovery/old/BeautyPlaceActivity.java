/**
* @Package com.manyouren.android.ui.discovery    
* @Title: BeautifulPlaceActivity.java 
* @Description: TODO
* @author firefist_wei firefist.wei@gmail.com   
* @date 2014-7-13 上午10:55:20 
* @version V1.0   
*/
package com.manyouren.manyouren.ui.discovery.old;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.controller.DiscoveryController;
import com.manyouren.manyouren.entity.UserEntity;
import com.manyouren.manyouren.ui.ItemListActivity;
import com.manyouren.manyouren.ui.ThrowableLoader;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @date 2014-7-10 下午6:21:09
 * 
 */
public class BeautyPlaceActivity extends ItemListActivity<UserEntity> {

	BeautyPlaceActivity instance = null;

	Context context = null;
	
	private static final int MENUITEM_PLACE_SEARCH = 11003;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		instance = this;

		context = this;

		init();

		getActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		getActionBar().setTitle("美地");
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	private void init() {
		setEmptyText(R.string.no_news);

	}

	@Override
	protected void configureList(Activity activity, ListView listView) {
		super.configureList(activity, listView);

		listView.setFastScrollEnabled(true);
		listView.setDividerHeight(0);

		/*getListAdapter().addHeader(
				activity.getLayoutInflater().inflate(R.layout.place_header,
						null));*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem done = menu.add(0, MENUITEM_PLACE_SEARCH, 0, "SEARCH");
		done.setIcon(this.getResources().getDrawable(R.drawable.ic_holo_search));
		done.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		// This is the home button in the top left corner of the screen.
		case MENUITEM_PLACE_SEARCH:
			return true;
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.right_in,
					R.anim.right_out);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public Loader<List<UserEntity>> onCreateLoader(int id, Bundle args) {

		final List<UserEntity> initialItems = items;
		// return null;

		return new ThrowableLoader<List<UserEntity>>(this, items) {

			@Override
			public List<UserEntity> loadData() throws Exception {
				if (instance != null) {

					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							/*
							 * A primary condition for this appears to be
							 * creating an asynchronous call from a thread that
							 * does not have it's own looper. This will prevent
							 * handler from initializing, thereby causing a
							 * NullPointerException when handler is used to call
							 * Runnable r on line 412 of
							 * AsyncHttpResponseHandler in commit
							 */

							 //DiscoveryController.getPeople();
						}
					});

					// return Collections.emptyList();
					return Collections.emptyList();
					// return
					// serviceProvider.getService(getActivity()).getNews();
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
	protected SingleTypeAdapter<UserEntity> createAdapter(List<UserEntity> items) {
		return new BeautyPlaceAdapter(this.getLayoutInflater(), items);
	}

	public void onListItemClick(ListView l, View v, int position, long id) {

	}

	@Override
	public void onDestroy() {
		setListAdapter(null);

		super.onDestroy();
	}

	Handler handler = new Handler() {
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
			case -1:
				break;
			case 0:
				break;
			case 1:
				break;
			}

		}
	};

}
