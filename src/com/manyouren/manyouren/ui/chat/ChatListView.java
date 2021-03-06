package com.manyouren.manyouren.ui.chat;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ChatListView extends ListView {

	public ChatListView(Context context) {
		super(context);
		init();
	}

	public ChatListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ChatListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setStackFromBottom(true);
		setFastScrollEnabled(true);
	}
}

