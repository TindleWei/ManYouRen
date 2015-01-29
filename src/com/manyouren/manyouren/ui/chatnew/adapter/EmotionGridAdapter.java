package com.manyouren.manyouren.ui.chatnew.adapter;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.ui.chatnew.ui.view.ViewHolder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by lzw on 14-9-25.
 */
public class EmotionGridAdapter extends BaseListAdapter<String> {

  public EmotionGridAdapter(Context ctx) {
    super(ctx);
  }

  @Override
  public View getView(int position, View conView, ViewGroup parent) {
    if (conView == null) {
      LayoutInflater inflater = LayoutInflater.from(ctx);
      conView = inflater.inflate(R.layout.chat_emotion_item, null);
    }
    ImageView emotionImageView = ViewHolder.findViewById(conView, R.id.emotionImageView);
    String pkgName = ctx.getPackageName();
    String emotion = (String) getItem(position);
    emotion = emotion.substring(1);
    Drawable drawable = ctx.getResources().getDrawable(ctx.getResources().getIdentifier(emotion, "drawable", pkgName));
    emotionImageView.setImageDrawable(drawable);
    return conView;
  }
}
