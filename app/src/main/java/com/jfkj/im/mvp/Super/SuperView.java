package com.jfkj.im.mvp.Super;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface SuperView  extends BaseView {

    public void SuperSuccess(ResponseBody responseBody);
}
