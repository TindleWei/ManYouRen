package com.manyouren.manyouren.ui.usernew;

import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.kevinsawicki.wishlist.Toaster;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.base.BaseActivityFragment;
import com.manyouren.manyouren.base.BaseListFragment;
import com.manyouren.manyouren.entity.PhotoEntity;
import com.manyouren.manyouren.service.AsyncHttpHandler;
import com.manyouren.manyouren.service.AsyncHttpManager;
import com.manyouren.manyouren.service.PreferenceHelper;

public class UserPhotosActivity extends BaseActivityFragment {

	public static final int MENUITEM_ALBUM_ADD = 1001;

	String uid = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBar("我的相册");

		uid = getStringExtra("uid");
		if (uid == null || uid.equals("")) {
			uid = PreferenceHelper.getUserId();
		} else {
		}

		final FragmentManager fm = getSupportFragmentManager();
		// Create the list fragment and add it as our sole content.
		if (fm.findFragmentById(android.R.id.content) == null) {
			final PhotoListFragment fragment = new PhotoListFragment();
			Bundle b = new Bundle();
			b.putSerializable("uid", uid);
			fragment.setArguments(b);
			fm.beginTransaction().add(android.R.id.content, fragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem done = menu.add(0, MENUITEM_ALBUM_ADD, 0, "发照片");
		done.setIcon(this.getResources().getDrawable(R.drawable.ic_action_camera));
		done.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case MENUITEM_ALBUM_ADD:
			startActivityForResult(new Intent(context,
					UserPhotoPublishActivity.class), MENUITEM_ALBUM_ADD);
			overridePendingTransition(R.anim.left_in, R.anim.left_out);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

class PhotoListFragment extends BaseListFragment<PhotoEntity> {

	private static final int MENUITEM_PEOPLE_FILTER = 11002;

	private UserPhotosAdapter mAdapter = null;
	
	String uid = "";
	
	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		uid = getArguments().getString("uid");
		initView();
		initEvent();
		init();
	}
	
	@Override
	protected void initView() {
	}

	@Override
	protected void initEvent() {
	}

	@Override
	protected void init() {
		mAdapter = new UserPhotosAdapter(getActivity().getLayoutInflater(),
				items);
		mListView.setAdapter(mAdapter);

		hideProgressBar();
		if (items.size() == 0) {
			showProgressBar();
			onRefresh();
		}
	}

	@Override
	public void onRefresh() {
		mPage=1;
		loadData(1);
	}

	@Override
	public void onLoadMore() {
		mPage++;
		loadData(mPage);
	}

	@Override
	protected void onListItemClick(ListView parent, View view, int position,
			long id) {
	}
	
	private void loadData(final int page) {

		String url = AsyncHttpManager.USER_ALBUM_URL;
		if (page != 1) {
			url += "?page=" + page;
		}
		RequestParams params = new RequestParams();
		params.put("userId", uid);

		AsyncHttpManager.getClient(context).post(url, params,
				new AsyncHttpHandler() {
					@Override
					public void onFailured() {
						super.onFailured();
						if (items.size() == 0)
							showErrorInfo();
						else
							Toaster.showShort(getActivity(), "网络出错");
						loadFinish();
					}

					@Override
					public void onSuccessed() {
						super.onSuccessed();
						parseJson(getResult());
						//Logot.outError("JSON", getResult());
						hideProgressBar();
						loadFinish();
					}

					@Override
					public void onError() {
						super.onError();
						hideProgressBar();
						loadFinish();
					}

					@Override
					public void onEmpty() {
						super.onEmpty();
						showEmptyInfo();
						loadFinish();
					}
				});
	}
	
	public void loadFinish() {

		if (items.size() < 10) {
			setPullLoad(false);
		} else {
			setPullLoad(true);
		}
		mAdapter.setItems(items.toArray());
		mAdapter.notifyDataSetChanged();

		mListView.stopRefresh();
		mListView.stopLoadMore();
	}
 
	public void parseJson(String jsonString) {

		String json = "";
		try {
			json = (new JSONObject(jsonString).getJSONArray("message"))
					.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		items = new Gson().fromJson(json, new TypeToken<List<PhotoEntity>>() {
		}.getType());
	}
}
