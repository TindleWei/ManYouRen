package com.manyouren.manyouren.core.chat;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.ui.chat.EmoticonsTextView;

import android.content.Context;
import android.view.View;
import android.view.View.OnLongClickListener;

public class TextMessageItem extends MessageItem implements OnLongClickListener {

	private EmoticonsTextView mEtvContent;

	public TextMessageItem(ChatMessage msg, Context context) {
		super(msg, context);
	}
	

	@Override
	protected void onInitViews() {
		View view = mInflater.inflate(R.layout.message_text, null);
		mLayoutMessageContainer.addView(view);
		mEtvContent = (EmoticonsTextView) view
				.findViewById(R.id.message_etv_msgtext);
//		mEtvContent.setText(mMsg.getContent());
		mEtvContent.setOnLongClickListener(this);
		mLayoutMessageContainer.setOnLongClickListener(this);
	}

	@Override
	protected void onFillMessage() {

	}

	@Override
	public boolean onLongClick(View v) {
		super.onLongClick(v);
		return false;
	}

}
