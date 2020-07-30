package com.jfkj.im.mvp.inputcode;

import com.jfkj.im.Bean.LoginBean;
import com.jfkj.im.mvp.BaseView;
import com.lzy.okgo.model.Response;

import okhttp3.ResponseBody;

public interface InputcodeView extends BaseView {


    public void loginregister(Response<String> responseBody);
    public void getloginregisterFail(String s);


    //获取图形验证码
    public void getBitmaSuccess(ResponseBody responseBody);
    public void getBitmapfail(String s);

    public void getCodeSuccess(ResponseBody responseBody);
    public void getCodefail(String fail);



    public void updatePassword(ResponseBody responseBody);//重置密码



    public void verificationParameterSuccess(String s,String token, LoginBean loginBean);
}
