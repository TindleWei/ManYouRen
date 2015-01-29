package com.manyouren.manyouren.ui.chatnew.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.avos.avoscloud.Group;
import com.manyouren.manyouren.R;
import com.manyouren.manyouren.ui.chatnew.adapter.GroupUsersAdapter;
import com.manyouren.manyouren.ui.chatnew.avobject.ChatGroup;
import com.manyouren.manyouren.ui.chatnew.avobject.User;
import com.manyouren.manyouren.ui.chatnew.service.ChatService;
import com.manyouren.manyouren.ui.chatnew.service.GroupEventListener;
import com.manyouren.manyouren.ui.chatnew.service.GroupMsgReceiver;
import com.manyouren.manyouren.ui.chatnew.service.GroupService;
import com.manyouren.manyouren.ui.chatnew.util.SimpleNetTask;
import com.manyouren.manyouren.ui.chatnew.util.UIUtils;
import com.manyouren.manyouren.ui.chatnew.util.Utils;

/**
 * Created by lzw on 14-10-11.
 */
public class GroupDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener,
    AdapterView.OnItemLongClickListener, GroupEventListener {
  public static final int ADD_MEMBERS = 0;
  private static final int QUIT_GROUP = 1;
  public static ChatGroup chatGroup;
  public static List<User> members = new ArrayList<User>();

  GridView usersGrid;
  GroupUsersAdapter usersAdapter;
  boolean isOwner;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.group_detail_activity);
    initData();
    findView();
    initActionBar(chatGroup.getTitle());
    initGrid();
    refresh();
    GroupMsgReceiver.addListener(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    if (isOwner) {
      MenuItem invite = menu.add(0, ADD_MEMBERS, 0, R.string.invite);
      UIUtils.alwaysShowMenuItem(invite);
    } else {
      MenuItem quit = menu.add(0, QUIT_GROUP, 0, R.string.quitGroup);
      UIUtils.alwaysShowMenuItem(quit);
    }
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    int menuId = item.getItemId();
    if (menuId == ADD_MEMBERS) {
      GroupAddMembersActivity.chatGroup = chatGroup;
      GroupAddMembersActivity.members = members;
      Utils.goActivity(ctx, GroupAddMembersActivity.class);
    } else if (menuId == QUIT_GROUP) {
      Group group = GroupService.getGroup(chatGroup);
      group.quit();
    }
    return super.onMenuItemSelected(featureId, item);
  }

  private void refresh() {
    new SimpleNetTask(ctx) {
      List<User> subMembers = new ArrayList<User>();

      @Override
      protected void doInBack() throws Exception {
        chatGroup.fetch();
        subMembers = ChatService.findGroupMembers(chatGroup);
      }

      @Override
      protected void onSucceed() {
        usersAdapter.clear();
        usersAdapter.addAll(subMembers);
      }
    }.execute();
  }

  private void initGrid() {
    usersAdapter = new GroupUsersAdapter(ctx, members);
    usersGrid.setAdapter(usersAdapter);
    usersGrid.setOnItemClickListener(this);
    usersGrid.setOnItemLongClickListener(this);
  }

  private void initData() {
    isOwner = GroupService.isGroupOwner(chatGroup, User.curUser());
  }

  private void findView() {
    usersGrid = (GridView) findViewById(R.id.usersGrid);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    User user = (User) parent.getAdapter().getItem(position);
    //PersonInfoActivity.goPersonInfo(ctx, user.getObjectId());
  }

  @Override
  public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
    final User user = (User) parent.getAdapter().getItem(position);
    boolean isTheOwner = GroupService.isGroupOwner(chatGroup, user);
    if (isTheOwner == false) {
      new AlertDialog.Builder(ctx).setMessage(R.string.kickTips)
          .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              GroupService.kickMember(chatGroup, user);
            }
          }).setNegativeButton(R.string.cancel, null).show();
    }
    return true;
  }

  @Override
  public void onJoined(Group group) {

  }

  @Override
  public void onMemberJoin(Group group, List<String> joinedPeerIds) {
    boolean curGroup = isCurGroup(group);
    if (curGroup) {
      refresh();
    }
  }

  private boolean isCurGroup(Group group) {
    return group.getGroupId().equals(chatGroup.getObjectId());
  }

  @Override
  public void onMemberLeft(Group group, List<String> leftPeerIds) {
    if (isCurGroup(group)) {
      refresh();
    }
  }

  @Override
  public void onQuit(Group group) {
    if (group.getGroupId().equals(chatGroup.getObjectId())) {
      finish();
      ChatActivity.instance.finish();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    GroupMsgReceiver.removeListener(this);
  }
}
