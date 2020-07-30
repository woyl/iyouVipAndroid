package com.jfkj.im.mvp.ChatRoute;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface ChatRouteView extends BaseView {

    public void GetImSuccess(ResponseBody responseBody);

    public void GetImfail(String s);
}
