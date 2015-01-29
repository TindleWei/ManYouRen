package com.manyouren.manyouren.ui.chat;

import java.util.ArrayList;
import java.util.List;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.core.chat.ChatMessage;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class BaseObjectListAdapter extends BaseAdapter {

	protected Context mContext;
	protected LayoutInflater mInflater;
	protected List<ChatMessage> mDatas = new ArrayList<ChatMessage>();

	public BaseObjectListAdapter(Context context,
			List<ChatMessage> mMessages) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		if (mMessages != null) {
			mDatas = mMessages;
		}
	}
	
	public void setData(List<ChatMessage> mMessages){
		if (mMessages != null) {
			mDatas = mMessages;
		}
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	public List<ChatMessage> getDatas() {
		return mDatas;
	}
}
