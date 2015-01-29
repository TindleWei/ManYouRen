package com.manyouren.manyouren.ui.chatnew.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.avos.avoscloud.AVException;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.ui.chatnew.avobject.User;
import com.manyouren.manyouren.ui.chatnew.service.UserService;
import com.manyouren.manyouren.ui.chatnew.ui.view.ViewHolder;
import com.manyouren.manyouren.ui.chatnew.util.NetAsyncTask;

import java.util.List;

public class AddFriendAdapter extends BaseListAdapter<User> {
  public AddFriendAdapter(Context context, List<User> list) {
    super(context, list);
  }

  @Override
  public View getView(int position, View conView, ViewGroup parent) {
    if (conView == null) {
      conView = inflater.inflate(R.layout.item_add_friend, null);
    }
    final User contact = datas.get(position);
    TextView nameView = ViewHolder.findViewById(conView, R.id.name);
    ImageView avatarView = ViewHolder.findViewById(conView, R.id.avatar);
    Button addBtn = ViewHolder.findViewById(conView, R.id.add);
    //String avatarUrl = contact.getAvatarUrl();
    //UserService.displayAvatar(avatarUrl, avatarView);
    nameView.setText(contact.getUsername());
    addBtn.setText(R.string.add);
    addBtn.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        
      }
    });
    return conView;
  }

}
