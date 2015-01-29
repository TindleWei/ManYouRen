package com.manyouren.manyouren.ui.chatnew.service;

import com.manyouren.manyouren.ui.chatnew.entity.Msg;


public interface MsgListener {
  public String getListenerId();

  public void onMessage(Msg msg);

  public void onMessageFailure(Msg msg);

  public void onMessageSent(Msg msg);


}
