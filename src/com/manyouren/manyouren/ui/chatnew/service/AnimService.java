package com.manyouren.manyouren.ui.chatnew.service;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.ui.chatnew.util.SimpleAnimationListener;

/**
 * Created by lzw on 14-10-6.
 */
public class AnimService {
  public Animation popupFromBottomAnim, hideToBottomAnim;
  Context ctx;
  public static AnimService instance;


  public AnimService(Context ctx) {
    this.ctx = ctx;
    popupFromBottomAnim = AnimationUtils.loadAnimation(RootApplication.getInstance(), R.anim.popup_from_bottom);
    hideToBottomAnim = AnimationUtils.loadAnimation(RootApplication.getInstance(), R.anim.slide_out_to_bottom);
  }

  public void hideView(final View view) {
    view.startAnimation(hideToBottomAnim);
    hideToBottomAnim.setAnimationListener(new SimpleAnimationListener() {
      @Override
      public void onAnimationEnd(Animation animation) {
        view.setVisibility(View.GONE);
      }
    });
  }

  public static AnimService getInstance() {
    if (instance == null) {
      if (RootApplication.getInstance() == null) {
        throw new NullPointerException("App context is null");
      }
      instance = new AnimService(RootApplication.getInstance());
    }
    return instance;
  }
}
