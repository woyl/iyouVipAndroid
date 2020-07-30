package com.jfkj.im.mvp.Rank;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface RankView  extends BaseView {

    public void RankSuccess(ResponseBody responseBody);
}
