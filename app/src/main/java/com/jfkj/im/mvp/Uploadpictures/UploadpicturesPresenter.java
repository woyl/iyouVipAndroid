package com.jfkj.im.mvp.Uploadpictures;

import android.util.Log;

import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

public class UploadpicturesPresenter extends BasePresenter<UploadpicturesView> {

    public UploadpicturesPresenter(UploadpicturesView view){
        attachView(view);
    }
    public void getuploadImage(String url,HashMap<String,String> map, MultipartBody.Part file){
        mvpView.showLoading();
        addSubscription(apiStores.uploadImages(url,map, file), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {

                mvpView.uploadImageSuccess(model);
            }

            @Override
            public void onFailure(String msg) {

                mvpView.uploadImagefail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
}
