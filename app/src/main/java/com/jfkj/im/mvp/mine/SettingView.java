package com.jfkj.im.mvp.mine;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/11/20
 * </pre>
 */
public interface SettingView extends BaseView {
    void showVersion();




    void loginOutSuccess(ResponseBody responseBody);
}
