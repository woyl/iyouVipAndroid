package com.jfkj.im.mvp.applyJoinGroup;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface ApplyJoinGroupView extends BaseView {

    public void getApplyJoinGroupSuucess(ResponseBody responseBody);

    public void getApplyJoinGroupfail(String s);
}
