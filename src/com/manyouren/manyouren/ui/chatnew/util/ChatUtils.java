package com.manyouren.manyouren.ui.chatnew.util;

import android.app.Application;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.avos.avoscloud.*;
import com.manyouren.manyouren.C;
import com.manyouren.manyouren.RootApplication;
import com.manyouren.manyouren.ui.chatnew.adapter.BaseListAdapter;
import com.manyouren.manyouren.ui.chatnew.avobject.User;
import com.manyouren.manyouren.ui.chatnew.service.PrefDao;
import com.manyouren.manyouren.ui.chatnew.service.UserService;
import com.manyouren.manyouren.ui.chatnew.ui.view.ViewHolder;
import com.manyouren.manyouren.ui.chatnew.ui.view.xlist.XListView;

import java.util.List;
import com.manyouren.manyouren.R;

/**
 * Created by lzw on 14-9-30.
 */

public class ChatUtils {
  public static void handleListResult(XListView listView, BaseListAdapter adapter, List datas) {
    if (Utils.isListNotEmpty(datas)) {
      adapter.addAll(datas);
      if (datas.size() == C.PAGE_SIZE) {
        listView.setPullLoadEnable(true);
      } else {
        listView.setPullLoadEnable(false);
      }
    } else {
      listView.setPullLoadEnable(false);
      if (adapter.getCount() == 0) {
        Utils.toast(R.string.noResult);
      } else {
        Utils.toast(R.string.dataLoadFinish);
      }
    }
  }

/*  public static void updateUserInfo() {
    User user = User.curUser();
    if (user != null) {
      user.fetchInBackground(User.FRIENDS, new GetCallback<AVObject>() {
        @Override
        public void done(AVObject avObject, AVException e) {
          if(e==null){
            User avUser = (User) avObject;
            RootApplication.registerUserCache(avUser);
          }
        }
      });
    }
  }*/

/*  public static void updateUserLocation() {
    PrefDao prefDao = PrefDao.getCurUserPrefDao(RootApplication.getInstance());
    AVGeoPoint lastLocation = prefDao.getLocation();
    if (lastLocation != null) {
      final User user = User.curUser();
      final AVGeoPoint location = user.getLocation();
      if (location == null || !Utils.doubleEqual(location.getLatitude(), lastLocation.getLatitude())
          || !Utils.doubleEqual(location.getLongitude(), lastLocation.getLongitude())) {
        user.setLocation(lastLocation);
        user.saveInBackground(new SaveCallback() {
          @Override
          public void done(AVException e) {
            if (e != null) {
              e.printStackTrace();
            } else {
              Logger.v("lastLocation save " + user.getLocation());
            }
          }
        });
      }
    }
  }*/

  public static void stopRefresh(XListView xListView) {
    if (xListView.getPullRefreshing()) {
      xListView.stopRefresh();
    }
  }

/*  public static void setUserView(View conView, User user) {
    ImageView avatarView = ViewHolder.findViewById(conView, R.id.avatar);
    TextView nameView = ViewHolder.findViewById(conView, R.id.username);
    UserService.displayAvatar(user.getAvatarUrl(), avatarView);
    nameView.setText(user.getUsername());
  }*/
}
