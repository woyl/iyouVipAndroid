package com.jfkj.im.mvp.crushicetasl;

import com.jfkj.im.mvp.BaseView;

public interface CrushIceSubmitTaskView extends BaseView {
    void submitSuccess(String msg);
    void onFails(String error);
}
