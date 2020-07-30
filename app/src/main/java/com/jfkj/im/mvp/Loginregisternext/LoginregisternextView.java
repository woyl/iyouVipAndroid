package com.jfkj.im.mvp.Loginregisternext;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface LoginregisternextView extends BaseView {
    //获取验证码返回的信息
    public void getCodeSuccess(ResponseBody responseBody);

    public void getCodefail(String s);


    public void loginregister(ResponseBody responseBody);
    public void getloginregisterFail(String s);
}
