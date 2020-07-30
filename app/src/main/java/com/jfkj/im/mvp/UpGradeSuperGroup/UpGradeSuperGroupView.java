package com.jfkj.im.mvp.UpGradeSuperGroup;


import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface UpGradeSuperGroupView extends BaseView {

    void UpGradeSuperGroupSuccess(ResponseBody responseBody);

    public void loadGroupInfoSuccess(ResponseBody userDetai);
}
