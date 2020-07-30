package com.jfkj.im.mvp.Userhead;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface UserheadView extends BaseView {

     public void uploadImageSuccess(ResponseBody responseBody);
     public void uploadImagefail(String s);

     public void registSuccess(ResponseBody responseBody);
     public void registfail(String s);
}
