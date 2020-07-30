package com.jfkj.im.mvp.Loginregisternext;

import android.graphics.Bitmap;

import com.jfkj.im.App;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class LoginregisternextPresenter extends BasePresenter<LoginregisternextView> {

    public LoginregisternextPresenter(LoginregisternextView loginregisternextView){
        attachView(loginregisternextView);
    }

    public void getCode(String phone){
        mvpView.showLoading();
        Map<String, String> map = new HashMap();
        map.put(Utils.MOBILENO,phone);

        addSubscription(apiStores.getStaticCode(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                 mvpView.getCodeSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.hideLoading();
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
    public void verification(String mobileno, String types, String code,String areacode){
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.MOBILENO, mobileno);
        map.put(Utils.TYPES, types);
        map.put(Utils.IMGYZM, code);
        map.put(Utils.AREACODE, areacode);
        map.put(Utils.CHANNEL, Utils.ANDROID);

        addSubscription(apiStores.getVPhone(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.loginregister(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getloginregisterFail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
