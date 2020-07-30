package com.jfkj.im.mvp.Authentication;

import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class AuthenticationPresenter extends BasePresenter<AuthenticationView> {

    public AuthenticationPresenter(AuthenticationView view){
        attachView(view);
    }
    public void AuthenticationPresenter(){
        Map<String,String> map=new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.CHANNEL);
        map.put(Utils.APPVERSION,Utils.getVersionCode() + "");

       addSubscription(apiStores.editorInfo(map), new ApiCallback<ResponseBody>() {
           @Override
           public void onSuccess(ResponseBody model) {
               try {
                   mvpView.AuthenticationSuccess(model.string());
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }

           @Override
           public void onFailure(String msg) {

           }

           @Override
           public void onFinish() {

           }
       });
    }
}
