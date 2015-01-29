package com.manyouren.manyouren.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.R.id;
import com.manyouren.manyouren.R.layout;
import com.manyouren.manyouren.base.BaseFragment;
import com.manyouren.manyouren.config.Constants;
import com.manyouren.manyouren.config.PreferConfig;
import com.manyouren.manyouren.util.PreferenceUtils;
import com.manyouren.manyouren.util.ScreenUtils;
import com.github.kevinsawicki.wishlist.Toaster;
import com.github.kevinsawicki.wishlist.ViewUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.Collections;
import java.util.List;

/**
 * Base fragment for displaying a list of items that loads with a progress bar
 * visible
 * 
 * @param <E>
 */
public abstract class ItemPullListFragment2<E> extends BaseFragment implements
		LoaderCallbacks<List<E>> {

	private static final String FORCE_REFRESH = "forceRefresh";
	
	protected List<E> items = Collections.emptyList();

	protected PullToRefreshListView listView;

	protected ProgressBar progressBar;

	protected boolean listShown = false;

	protected static boolean isForceRefresh(final Bundle args) {
		return args != null && args.getBoolean(FORCE_REFRESH, false);
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (!items.isEmpty()) {
			setListShown(true, false);
		}
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(layout.item_pull_list2, null);
	}

	@Override
	public void onDestroyView() {
		listShown = false;
		progressBar = null;
		listView = null;
		super.onDestroyView();
	}

	@Override
	public void onViewCreated(View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		listView = (PullToRefreshListView) view.findViewById(R.id.refresh_list);
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
				if (PullToRefreshBase.Mode.PULL_FROM_START == listView
						.getCurrentMode()) {
					onListDropDown();
				} else if (PullToRefreshBase.Mode.PULL_FROM_END == listView
						.getCurrentMode()) {
					onListLoadMore();
				}
			}
		});

		progressBar = (ProgressBar) view.findViewById(id.pb_loading);
		configureList(getActivity(), getListView());

		/**
		 * Add Sound Event Listener
		 */
		// SoundPullEventListener<ListView> soundListener = new
		// SoundPullEventListener<ListView>(
		// getActivity());
		// // soundListener.addSoundEvent(State.PULL_TO_REFRESH,
		// R.raw.pull_event);
		// // soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
		// soundListener.addSoundEvent(State.REFRESHING,
		// R.raw.refreshing_sound);
		// listView.setOnPullEventListener(soundListener);

		// listView.setMode(Mode.BOTH);//Mode.PULL_FROM_START

		ListView actualListView = listView.getRefreshableView();

		registerForContextMenu(actualListView);
	}

	protected void configureList(Activity activity, ListView listView) {

	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(final Menu optionsMenu,
			final MenuInflater inflater) {
		//inflater.inflate(R.menu.bootstrap, optionsMenu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (!isUsable()) {
			return false;
		}
		switch (item.getItemId()) {

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void forceRefresh() {
		final Bundle bundle = new Bundle();
		bundle.putBoolean(FORCE_REFRESH, true);
		refresh(bundle);
	}

	public void refresh() {
		refresh(null);
	}

	private void refresh(final Bundle args) {
		if (!isUsable()) {
			return;
		}
		getActivity().setProgressBarIndeterminateVisibility(true);

		getLoaderManager().restartLoader(0, args, this);

	}

	protected abstract int getErrorMessage(final Exception exception);

	public void onLoadFinished(final Loader<List<E>> loader, final List<E> items) {

		getActivity().setProgressBarIndeterminateVisibility(false);

		final Exception exception = getException(loader);
		if (exception != null) {
			showError(getErrorMessage(exception));
			showList();
			return;
		}

		this.items = items;
		// getListAdapter().getWrappedAdapter().setItems(items.toArray());
		showList();
	}

	protected void showList() {
		setListShown(true, isResumed());
	}

	@Override
	public void onLoaderReset(final Loader<List<E>> loader) {
		// Intentionally left blank
	}

	protected void showError(final int message) {
		Toaster.showLong(getActivity(), message);
	}

	protected Exception getException(final Loader<List<E>> loader) {
		if (loader instanceof ThrowableLoader) {
			return ((ThrowableLoader<List<E>>) loader).clearException();
		} else {
			return null;
		}
	}

	protected void refreshWithProgress() {
		items.clear();
		setListShown(false);
		refresh();
	}

	public ListView getListView() {
		return listView.getRefreshableView();
	}

	private ItemPullListFragment2<E> fadeIn(final View view,
			final boolean animate) {
		if (view != null) {
			if (animate) {
				view.startAnimation(AnimationUtils.loadAnimation(getActivity(),
						android.R.anim.fade_in));
			} else {
				view.clearAnimation();
			}
		}
		return this;
	}

	public ItemPullListFragment2<E> show(final View view) {
		ViewUtils.setGone(view, false);
		return this;
	}

	public ItemPullListFragment2<E> hide(final View view) {
		ViewUtils.setGone(view, true);
		return this;
	}

	public ItemPullListFragment2<E> setListShown(final boolean shown) {
		return setListShown(shown, true);
	}

	public ItemPullListFragment2<E> setListShown(final boolean shown,
			final boolean animate) {
		if (!isUsable()) {
			return this;
		}
		if (shown == listShown) {
			if (shown) {
				show(listView);
			}
			return this;
		}
		listShown = shown;

		if (shown) {
			if (!items.isEmpty()) {
				hide(progressBar);//.fadeIn(listView, animate).
				show(listView);
			} else {
				hide(progressBar);//.fadeIn(listView, animate).
				show(listView);

				if (items.isEmpty()) {
					TextView tv = new TextView(getActivity());
					tv.setGravity(Gravity.CENTER);
					tv.setText("没有数据，下拉看看");
					tv.setTextColor(0xff909090);
					tv.setPadding(0, 0, 0,
							ScreenUtils.getIntPixels(getActivity(), 60));
					tv.setTextSize(14);

					listView.setEmptyView(tv);
				}
			}
		} else {
			hide(listView);//.fadeIn(progressBar, animate).
			show(progressBar);
		}
		return this;
	}

	public void onListItemClick(final ListView l, final View v,
			final int position, final long id) {
	}

	public void onListDropDown() {

	}

	public void onListLoadMore() {

	}

	protected boolean isUsable() {
		return getActivity() != null;
	}
}
