package com.jfkj.im.mvp.updatePassword;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface UpdatePasswordView extends BaseView {


    public void updatepasswordsuccess(ResponseBody responseBody);
    public void updatepasswordfail(String s);
}
