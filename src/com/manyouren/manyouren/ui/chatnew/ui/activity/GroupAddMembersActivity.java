package com.manyouren.manyouren.ui.chatnew.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.manyouren.manyouren.R;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.ui.chatnew.adapter.GroupAddMembersAdapter;
import com.manyouren.manyouren.ui.chatnew.avobject.ChatGroup;
import com.manyouren.manyouren.ui.chatnew.avobject.User;
import com.manyouren.manyouren.ui.chatnew.service.GroupService;
import com.manyouren.manyouren.ui.chatnew.util.SimpleNetTask;
import com.manyouren.manyouren.ui.chatnew.util.UIUtils;
import com.manyouren.manyouren.ui.chatnew.util.Utils;

/**
 * Created by lzw on 14-10-11.
 */
public class GroupAddMembersActivity extends BaseActivity {
  public static final int OK = 0;
  GroupAddMembersAdapter adapter;
  ListView userList;
  List<User> users;
  public static ChatGroup chatGroup;
  public static List<User> members;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.group_add_members_layout);
    findView();
    initData();
    initList();
    initActionBar();
  }

  private void initData() {
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuItem add = menu.add(0, OK, 0, R.string.sure);
    UIUtils.alwaysShowMenuItem(add);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    int id = item.getItemId();
    if (id == OK) {
      addMembers();
    }
    return super.onMenuItemSelected(featureId, item);
  }

  private void addMembers() {
    final List<User> checkedUsers = adapter.getCheckedDatas();
    new SimpleNetTask(ctx) {
      @Override
      protected void doInBack() throws Exception {
        GroupService.inviteMembers(chatGroup, checkedUsers);
      }

      @Override
      protected void onSucceed() {
        Utils.toast(R.string.inviteSucceed);
        finish();
      }
    }.execute();
  }

  private void initList() {
	RootApplication app = RootApplication.getInstance();
    users = app.getFriends();
    List<User> restUsers = removeMembers(users, members);
    adapter = new GroupAddMembersAdapter(ctx, restUsers);
    userList.setAdapter(adapter);
  }

  private List<User> removeMembers(List<User> users, List<User> members) {
    List<User> restUsers = new ArrayList<User>(users);
    restUsers.removeAll(members);
    return restUsers;
  }

  private void findView() {
    userList = (ListView) findViewById(R.id.userList);
  }

}
