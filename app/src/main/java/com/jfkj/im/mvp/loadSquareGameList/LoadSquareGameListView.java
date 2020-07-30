package com.jfkj.im.mvp.loadSquareGameList;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface LoadSquareGameListView extends BaseView {

    public void LoadSquareGameListSuccess(ResponseBody responseBody);
}
