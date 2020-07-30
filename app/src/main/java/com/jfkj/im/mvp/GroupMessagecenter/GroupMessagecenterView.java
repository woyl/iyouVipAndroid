package com.jfkj.im.mvp.GroupMessagecenter;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface GroupMessagecenterView  extends BaseView {

  public void handlerInviteGroupSuccess(ResponseBody responseBody,String id);
  public void handlerInviteGroupfail(String s);


  public void handlerJoinGroupSuccess(String responseBody,String id);
  public void handlerJoinGroupfail(String responseBody);



  public void replyJoinGroupMessageSuccess(String responseBody,String applyid);
}
