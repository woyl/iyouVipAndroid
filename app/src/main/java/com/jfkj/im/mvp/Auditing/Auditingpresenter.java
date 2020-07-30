package com.jfkj.im.mvp.Auditing;

import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Part;

public class Auditingpresenter extends BasePresenter<AuditingView> {

    public Auditingpresenter(AuditingView auditingView){
        attachView(auditingView);
    }
    public void uploadFile(@Part MultipartBody.Part file){
        mvpView.showLoading();
        addSubscription(apiStores.uploadFile(file), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.uploadFileSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.uploadFilefail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
    public void getData(String Photo1,String Photo2,String userVideo){
        mvpView.showLoading();
        Map<String,String> map=new HashMap<>();
        map.put("Photo1",Photo1);
        map.put("Photo2",Photo2);
        map.put("userVideo",userVideo);
        addSubscription(apiStores.userExamine(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.uploadSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.uploadfail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
