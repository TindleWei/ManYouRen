package com.manyouren.manyouren.ui.chatnew.service;

import com.avos.avoscloud.AVCloud;
import com.avos.avoscloud.AVException;
import com.manyouren.manyouren.ui.chatnew.avobject.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lzw on 14-9-29.
 */
public class CloudService {
  public static void addFriendForBoth(User toUser, User fromUser) throws AVException {
    callCloudRelationFn(toUser, fromUser, "addFriend");
  }

  public static void removeFriendForBoth(User toUser, User fromUser) throws AVException {
    callCloudRelationFn(toUser, fromUser, "removeFriend");
  }

  public static void callCloudRelationFn(User toUser, User fromUser, String functionName) throws AVException {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("fromUserId", fromUser.getObjectId());
    map.put("toUserId", toUser.getObjectId());
    Object res = AVCloud.callFunction(functionName, map);
  }
}
