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
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.R.id;
import com.manyouren.manyouren.ui.HeaderFooterListAdapter;
import com.manyouren.manyouren.ui.ThrowableLoader;

/**
 * @Description: TODO
 * 
 * @author firefist_wei
 * @param <E>
 * @date 2014-7-10 下午9:50:17
 * 
 */
public abstract class ItemListActivity<E> extends FragmentActivity implements
		android.support.v4.app.LoaderManager.LoaderCallbacks<List<E>> {

	private static final String FORCE_REFRESH = "forceRefresh";

	protected static boolean isForceRefresh(final Bundle args) {
		return args != null && args.getBoolean(FORCE_REFRESH, false);
	}

	protected List<E> items = Collections.emptyList();

	protected ListView listView;

	/**
	 * Empty view
	 */
	protected TextView emptyView;

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
		setContentView(R.layout.item_list);

		listView = (ListView) findViewById(android.R.id.list);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onListItemClick((ListView) parent, view, position, id);
			}
		});
		progressBar = (ProgressBar) findViewById(id.pb_loading);

		emptyView = (TextView) findViewById(android.R.id.empty);

		configureList(ItemListActivity.this, getListView());

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
		listView.setAdapter(createAdapter());
	}

	/**
	 * Detach from list view.
	 */
	@Override
	public void onDestroy() {
		listShown = false;
		emptyView = null;
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
		getListAdapter().getWrappedAdapter().setItems(items.toArray());
		showList();
	}

	/**
	 * Create adapter to display items
	 * 
	 * @return adapter
	 */
	protected HeaderFooterListAdapter<SingleTypeAdapter<E>> createAdapter() {
		final SingleTypeAdapter<E> wrapped = createAdapter(items);
		return new HeaderFooterListAdapter<SingleTypeAdapter<E>>(getListView(),
				wrapped);
	}

	/**
	 * Create adapter to display items
	 * 
	 * @param items
	 * @return adapter
	 */
	protected abstract SingleTypeAdapter<E> createAdapter(final List<E> items);

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
		return listView;
	}

	/**
	 * Get list adapter
	 * 
	 * @return list adapter
	 */
	@SuppressWarnings("unchecked")
	protected HeaderFooterListAdapter<SingleTypeAdapter<E>> getListAdapter() {
		if (listView != null) {
			return (HeaderFooterListAdapter<SingleTypeAdapter<E>>) listView
					.getAdapter();
		}
		return null;
	}

	/**
	 * Set list adapter to use on list view
	 * 
	 * @param adapter
	 * @return this fragment
	 */
	protected ItemListActivity<E> setListAdapter(final ListAdapter adapter) {
		if (listView != null) {
			listView.setAdapter(adapter);
		}
		return this;
	}

	private ItemListActivity<E> fadeIn(final View view, final boolean animate) {
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

	private ItemListActivity<E> show(final View view) {
		ViewUtils.setGone(view, false);
		return this;
	}

	private ItemListActivity<E> hide(final View view) {
		ViewUtils.setGone(view, true);
		return this;
	}

	/**
	 * Set list shown or progress bar show
	 * 
	 * @param shown
	 * @return this fragment
	 */
	public ItemListActivity<E> setListShown(final boolean shown) {
		return setListShown(shown, true);
	}

	/**
	 * Set list shown or progress bar show
	 * 
	 * @param shown
	 * @param animate
	 * @return this fragment
	 */
	public ItemListActivity<E> setListShown(final boolean shown,
			final boolean animate) {
		if (!isUsable()) {
			return this;
		}

		if (shown == listShown) {
			if (shown) {
				// List has already been shown so hide/show the empty view with
				// no fade effect
				if (items.isEmpty()) {
					hide(listView).show(emptyView);
				} else {
					hide(emptyView).show(listView);
				}
			}
			return this;
		}

		listShown = shown;

		if (shown) {
			if (!items.isEmpty()) {
				hide(progressBar).hide(emptyView).fadeIn(listView, animate)
						.show(listView);
			} else {
				hide(progressBar).hide(listView).fadeIn(emptyView, animate)
						.show(emptyView);
			}
		} else {
			hide(listView).hide(emptyView).fadeIn(progressBar, animate)
					.show(progressBar);
		}
		return this;
	}

	/**
	 * Set empty text on list fragment
	 * 
	 * @param message
	 * @return this fragment
	 */
	protected ItemListActivity<E> setEmptyText(final String message) {
		if (emptyView != null) {
			emptyView.setText(message);
		}
		return this;
	}

	/**
	 * Set empty text on list fragment
	 * 
	 * @param resId
	 * @return this fragment
	 */
	protected ItemListActivity<E> setEmptyText(final int resId) {
		if (emptyView != null) {
			emptyView.setText(resId);
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

	/**
	 * Is this fragment still part of an activity and usable from the UI-thread?
	 * 
	 * @return true if usable on the UI-thread, false otherwise
	 */
	protected boolean isUsable() {
		return this != null;
	}

}
