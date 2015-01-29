package com.manyouren.manyouren.ui.chatnew.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.ui.chatnew.avobject.User;
import com.manyouren.manyouren.ui.chatnew.util.ChatUtils;

/**
 * Created by lzw on 14-10-11.
 */
public class GroupUsersAdapter extends BaseListAdapter<User> {
  public GroupUsersAdapter(Context ctx, List<User> datas) {
    super(ctx, datas);
  }

  @Override
  public View getView(int position, View conView, ViewGroup parent) {
    if (conView == null) {
      conView = View.inflate(ctx, R.layout.group_user_item, null);
    }
    User user = datas.get(position);
    //ChatUtils.setUserView(conView, user);
    return conView;
  }
}
