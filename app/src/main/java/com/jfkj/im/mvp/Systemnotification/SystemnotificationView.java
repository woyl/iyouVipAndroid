package com.jfkj.im.mvp.Systemnotification;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface SystemnotificationView  extends BaseView {

    public void loadsystemnotiSuccess(ResponseBody responseBody);
   public void loadsystemfail(String s);
}
