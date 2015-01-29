package com.manyouren.manyouren.ui.chatnew.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.FindCallback;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.ui.chatnew.adapter.AddFriendAdapter;
import com.manyouren.manyouren.ui.chatnew.avobject.User;
import com.manyouren.manyouren.ui.chatnew.service.UserService;
import com.manyouren.manyouren.ui.chatnew.ui.view.xlist.XListView;
import com.manyouren.manyouren.ui.chatnew.util.ChatUtils;
import com.manyouren.manyouren.ui.chatnew.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends BaseActivity implements OnClickListener, XListView.IXListViewListener, OnItemClickListener {
  EditText searchNameEdit;
  Button searchBtn;
  List<User> users = new ArrayList<User>();//change it first , then adapter
  XListView listView;
  AddFriendAdapter adapter;
  String searchName = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.add_friend_activity);
    initView();
    search(searchName);
  }

  private void initView() {
    initActionBar(RootApplication.instance.getString(R.string.findFriends));
    searchNameEdit = (EditText) findViewById(R.id.searchNameEdit);
    searchBtn = (Button) findViewById(R.id.searchBtn);
    searchBtn.setOnClickListener(this);
    initXListView();
  }

  private void initXListView() {
    listView = (XListView) findViewById(R.id.searchList);
    listView.setPullLoadEnable(false);
    listView.setPullRefreshEnable(false);
    listView.setXListViewListener(this);

    adapter = new AddFriendAdapter(this, users);
    listView.setAdapter(adapter);
    listView.setOnItemClickListener(this);
  }

  @Override
  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
    // TODO Auto-generated method stub
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.searchBtn:
        searchName = searchNameEdit.getText().toString();
        if (searchName != null) {
          adapter.clear();
          search(searchName);
        }
        break;
    }
  }

  private void search(String searchName) {
    /*UserService.searchUser(searchName, adapter.getCount(), new FindCallback<User>() {
      @Override
      public void done(List<User> users, AVException e) {
        stopLoadMore();
        if (e != null) {
          e.printStackTrace();
          Utils.toast(ctx, R.string.pleaseCheckNetwork);
        } else {
          ChatUtils.handleListResult(listView, adapter, users);
        }
      }
    });*/
  }


  @Override
  public void onRefresh() {
  }

  @Override
  public void onLoadMore() {
    search(searchName);
  }

  private void stopLoadMore() {
    if (listView.getPullLoading()) {
      listView.stopLoadMore();
    }
  }
}
