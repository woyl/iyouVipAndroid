package com.jfkj.im.mvp.CheckGroupName;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface CheckGroupNameView extends BaseView {

    public void CheckGroupNameSuccess(ResponseBody responseBody);
    public void CheckGroupNamefail(String s);
}
