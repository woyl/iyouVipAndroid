package com.jfkj.im.mvp.LoadUserGroupList;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface LoadUserGroupListView extends BaseView {

    public void LoadUserGroupSuccess(ResponseBody responseBody);

    public void LoadUserGroupSuccessfail(String s);
}
