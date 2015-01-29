package com.manyouren.manyouren.base;

import java.util.Collections;
import java.util.List;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.widget.progress.VerticalProgressMsg;
import com.manyouren.manyouren.widget.xlist.XListView;
import com.manyouren.manyouren.widget.xlist.XListView.IXListViewListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public abstract class BaseListFragment<E> extends BaseFragment implements
		IXListViewListener {

	protected List<E> items = Collections.emptyList();

	protected XListView mListView;

	protected VerticalProgressMsg progressMsg;
	
	protected int mPage = 1;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_baselist, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mListView = (XListView) view.findViewById(R.id.xListView);
		progressMsg = (VerticalProgressMsg) view.findViewById(R.id.progressMsg);
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		config();
	}
	
	public void setPullRefresh(boolean flag){
		mListView.setPullRefreshEnable(flag);
	}
	
	public void setPullLoad(boolean flag){
		mListView.setPullLoadEnable(flag);
	}

	private void config() {

		setPullRefresh(true);
		setPullLoad(false);
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onListItemClick((ListView) parent, view, position, id);
			}
		});
	}
	
	protected abstract  void onListItemClick(ListView parent, View view, int position,
			long id);

	public void showProgressBar() {
		progressMsg.showProgressBar();
	}

	public void hideProgressBar() {
		progressMsg.hideProgressBar();
	}

	public void showErrorInfo() {
//		if(items.size()==0)
//		progressMsg.showErrorInfo();
//		else
			Toast.makeText(context, "网络异常", 1000).show();
	}

	public void showEmptyInfo() {
		if(items.size()==0)
		progressMsg.showEmptyInfo();
		else
			Toast.makeText(context, "没有更多数据", 1000).show();
	}

}
