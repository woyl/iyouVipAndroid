package com.jfkj.im.mvp.Authentication;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface AuthenticationView extends BaseView {

    public void AuthenticationSuccess(String responseBody);
}
