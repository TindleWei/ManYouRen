package com.manyouren.manyouren.ui.chatnew.ui.activity;

import com.manyouren.manyouren.R;

import android.os.Bundle;

/**
 * Created by lzw on 14-9-24.
 */
public class NotifyPrefActivity extends BaseActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.notify_pref_layout);
    initActionBar(R.string.notifySetting);
  }
}
