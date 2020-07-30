package com.jfkj.im.mvp.createGroup;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface CreateGroupView extends BaseView {

    public void CreateGroupSuccess(ResponseBody responseBody);
    public void CreateGroupfail(String s);

    public void updateGroupSuccess(String s);
}
