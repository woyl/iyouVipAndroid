package com.jfkj.im.mvp.updatePassword;

import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class UpdatePasswordPresenter extends BasePresenter<UpdatePasswordView> {

    public UpdatePasswordPresenter(UpdatePasswordView passwordView) {
        attachView(passwordView);
    }

    //更新密码
    public void getdata( String mobileno,String password) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.MOBILENO, mobileno);
        map.put(Utils.PASSWORD, password);

        addSubscription(apiStores.updatePassword(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.updatepasswordsuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.updatepasswordfail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
