package com.manyouren.manyouren.ui.user;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ListView;

import com.manyouren.manyouren.base.BaseActivityFragment;
import com.manyouren.manyouren.base.BaseFragment;
import com.manyouren.manyouren.base.BaseListFragment;

public class UserAlbumFragmentActivity extends BaseActivityFragment{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActionBar("个人相册");

		final FragmentManager fm = getSupportFragmentManager();

		if (fm.findFragmentById(android.R.id.content) == null) {
			final UserAlbumFragment fragment = new UserAlbumFragment();
			fm.beginTransaction().add(android.R.id.content, fragment).commit();
		}
	}

}

class UserAlbumFragment extends BaseListFragment{

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

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onListItemClick(ListView parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
	
}
