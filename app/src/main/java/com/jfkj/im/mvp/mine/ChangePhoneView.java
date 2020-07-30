package com.jfkj.im.mvp.mine;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/1/6
 * </pre>
 */
public interface ChangePhoneView extends BaseView {
    void mobileVerification();

    void showCodeSuccess(ResponseBody o);

    void getPhoneCodeSuccess();

    void changeSuccess();
}
