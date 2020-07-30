package com.jfkj.im.mvp.Systemnotification;

import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;

import okhttp3.ResponseBody;

public class SystemnotificationPresenter extends BasePresenter<SystemnotificationView> {

    public SystemnotificationPresenter(SystemnotificationView systemnotificationView){
        attachView(systemnotificationView);
    }
    public void loadsystemtification(){
//        addSubscription(apiStores.getVPhone(""), new ApiCallback<ResponseBody>() {
//            @Override
//            public void onSuccess(ResponseBody model) {
//
//            }
//
//            @Override
//            public void onFailure(String msg) {
//
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//        });
    }
}
