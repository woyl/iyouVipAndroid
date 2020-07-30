package com.jfkj.im.mvp.Loginpassword;

import com.jfkj.im.Bean.LoginBean;
import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface LoginpasswordView extends BaseView {

    public void loginSuccess(String responseBody);
    public void loginfail(String s);


    //获取图形验证码
    public void getBitmaSuccess(ResponseBody responseBody);


    public void getCodeSuccess(ResponseBody responseBody);

    public void verificationParameterSuccess(String s,String token, LoginBean loginBean);

}
