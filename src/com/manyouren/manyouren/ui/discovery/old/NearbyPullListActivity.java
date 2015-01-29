/**
 * @Package com.manyouren.android.ui.discovery    
 * @Title: ItemListActivity.java 
 * @Description: TODO
 * @author firefist_wei firefist.wei@gmail.com   
 * @date 2014-7-10 下午9:50:17 
 * @version V1.0   
 */
package com.manyouren.manyouren.ui.discovery.old;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import com.github.kevinsawicki.wishlist.Toaster;
import com.github.kevinsawicki.wishlist.ViewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.R.id;
import com.manyouren.manyouren.base.BaseActivity;
import com.manyouren.manyouren.ui.ThrowableLoader;
import com.manyouren.manyouren.util.ScreenUtils;

/**
 * 
 * @author firefist_wei
 * @param <E>
 * @date 2014-7-10 下午9:50:17
 * 
 */
public abstract class NearbyPullListActivity<E> extends BaseActivity implements
		android.support.v4.app.LoaderManager.LoaderCallbacks<List<E>> {

	private static final String FORCE_REFRESH = "forceRefresh";

	protected static boolean isForceRefresh(final Bundle args) {
		return args != null && args.getBoolean(FORCE_REFRESH, false);
	}

	protected List<E> items = Collections.emptyList();

	protected PullToRefreshListView listView;

	/**
	 * Progress bar
	 */
	protected ProgressBar progressBar;

	/**
	 * Is the list currently shown?
	 */
	protected boolean listShown;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearbypeople);

		listView = (PullToRefreshListView) findViewById(R.id.refresh_list);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onListItemClick((ListView) parent, view, position, id);
			}
		});
		
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// Update the LastUpdatedLabel
				// refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				 if (PullToRefreshBase.Mode.PULL_FROM_START == listView.getCurrentMode()) {
					 onListDropDown();
				 }else if (PullToRefreshBase.Mode.PULL_FROM_END == listView.getCurrentMode()){
					 onListLoadMore();
				 }
			}

		});
		progressBar = (ProgressBar) findViewById(id.pb_loading);

		configureList(NearbyPullListActivity.this, getListView());
		
		/**
		 * Add Sound Event Listener
		 */
		/*SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(
				this);
		// soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
		// soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
		soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
		listView.setOnPullEventListener(soundListener);*/

		// listView.setMode(Mode.BOTH);//Mode.PULL_FROM_START

		ListView actualListView = listView.getRefreshableView();

		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);
		

		if (!items.isEmpty()) {
			setListShown(true, false);
		}

		getSupportLoaderManager().initLoader(0, null, this);

	}

	/**
	 * Configure list after view has been created
	 * 
	 * @param activity
	 * @param listView
	 */
	protected void configureList(final Activity activity,
			final ListView listView) {
	}

	/**
	 * Detach from list view.
	 */
	@Override
	public void onDestroy() {
		listShown = false;
		progressBar = null;
		listView = null;

		super.onDestroy();
	}

	/**
	 * Force a refresh of the items displayed ignoring any cached items
	 */
	protected void forceRefresh() {
		final Bundle bundle = new Bundle();
		bundle.putBoolean(FORCE_REFRESH, true);
		refresh(bundle);
	}

	/**
	 * Refresh the fragment's list
	 */
	public void refresh() {
		refresh(null);
	}

	private void refresh(final Bundle args) {
		if (!isUsable()) {
			return;
		}

		setProgressBarIndeterminateVisibility(true);

		getSupportLoaderManager().restartLoader(0, args, this);
	}

	/**
	 * Get error message to display for exception
	 * 
	 * @param exception
	 * @return string resource id
	 */
	protected abstract int getErrorMessage(final Exception exception);

	public void onLoadFinished(final Loader<List<E>> loader, final List<E> items) {

		setProgressBarIndeterminateVisibility(false);

		final Exception exception = getException(loader);
		if (exception != null) {
			showError(getErrorMessage(exception));
			showList();
			return;
		}

		this.items = items;
		showList();
	}


	/**
	 * Set the list to be shown
	 */
	protected void showList() {
		setListShown(true);
	}

	public void onLoaderReset(final Loader<List<E>> loader) {
		// Intentionally left blank
	}

	/**
	 * Show exception in a Toast
	 * 
	 * @param message
	 */
	protected void showError(final int message) {
		Toaster.showLong(this, message);
	}

	/**
	 * Get exception from loader if it provides one by being a
	 * {@link ThrowableLoader}
	 * 
	 * @param loader
	 * @return exception or null if none provided
	 */
	protected Exception getException(final Loader<List<E>> loader) {
		if (loader instanceof ThrowableLoader) {
			return ((ThrowableLoader<List<E>>) loader).clearException();
		} else {
			return null;
		}
	}

	/**
	 * Refresh the list with the progress bar showing
	 */
	protected void refreshWithProgress() {
		items.clear();
		setListShown(false);
		refresh();
	}

	/**
	 * Get {@link ListView}
	 * 
	 * @return listView
	 */
	public ListView getListView() {
		return listView.getRefreshableView();
	}


	public NearbyPullListActivity<E> fadeIn(final View view, final boolean animate) {
		if (view != null) {
			if (animate) {
				view.startAnimation(AnimationUtils.loadAnimation(this,
						android.R.anim.fade_in));
			} else {
				view.clearAnimation();
			}
		}
		return this;
	}

	public NearbyPullListActivity<E> show(final View view) {
		ViewUtils.setGone(view, false);
		return this;
	}

	public NearbyPullListActivity<E> hide(final View view) {
		ViewUtils.setGone(view, true);
		return this;
	}

	/**
	 * Set list shown or progress bar show
	 * 
	 * @param shown
	 * @return this fragment
	 */
	public NearbyPullListActivity<E> setListShown(final boolean shown) {
		return setListShown(shown, true);
	}

	/**
	 * Set list shown or progress bar show
	 * 
	 * @param shown
	 * @param animate
	 * @return this fragment
	 */
	public NearbyPullListActivity<E> setListShown(final boolean shown,
			final boolean animate) {
		if (!isUsable()) {
			return this;
		}

		if (shown == listShown) {
			if (shown) {
				// List has already been shown so hide/show the empty view with
				// no fade effect
				show(listView);
				
			}
			return this;
		}

		listShown = shown;

		if (shown) {
			if (!items.isEmpty()) {
				hide(progressBar).fadeIn(listView, animate)
						.show(listView);
			} else {
				hide(progressBar).fadeIn(listView, animate)
				.show(listView);
				
				/*if(items.isEmpty()){
					TextView tv = new TextView(NearbyPullListActivity.this);
					tv.setGravity(Gravity.CENTER);
					tv.setText("没有数据，下拉看看");
					tv.setTextColor(0xff909090);
					tv.setPadding(0, 0, 0, ScreenUtils.getIntPixels(this, 60));
					tv.setTextSize(14);

					listView.setEmptyView(tv);
				}*/
				
//				hide(progressBar).hide(listView).fadeIn(emptyView, animate)
//						.show(emptyView);
			}
		} else {
			hide(listView).fadeIn(progressBar, animate)
					.show(progressBar);
		}
		return this;
	}


	/**
	 * Callback when a list view item is clicked
	 * 
	 * @param l
	 * @param v
	 * @param position
	 * @param id
	 */
	public void onListItemClick(final ListView l, final View v,
			final int position, final long id) {
	}
	
	public void onListDropDown() {

	}

	public void onListLoadMore() {

	}

	/**
	 * Is this fragment still part of an activity and usable from the UI-thread?
	 * 
	 * @return true if usable on the UI-thread, false otherwise
	 */
	protected boolean isUsable() {
		return this != null;
	}
	
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.right_in, R.anim.right_out);
	}

}
