package com.jfkj.im.mvp.Recordvideo;

import com.jfkj.im.App;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Part;

public class RecordvideopRresenter extends BasePresenter<RecordvideoView> {

    public RecordvideopRresenter(RecordvideoView recordvideoView) {
        attachView(recordvideoView);
    }
    //上传视频
     public void uploadfile(String url,String fileType,String pathType,String userId,@Part MultipartBody.Part file){
         mvpView.showLoading();
         addSubscription(apiStores.uploadSingleFileVideo(url,fileType,pathType,userId,file), new ApiCallback<ResponseBody>() {
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
     //注册接口
    public void uploaddata(String mobileno, String password, String birthday, String gender, String head, String nickname, String longitude, String latitude, String position, String Photo1, String Photo2, String userVideo) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.MOBILENO, mobileno);
        map.put(Utils.PASSWORD, password);
        map.put(Utils.Birthday, birthday);
        map.put(Utils.GENDER, gender);
        map.put(Utils.HEAD, head);
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.NICKNAME, nickname);
        map.put(Utils.LONGITUDE, longitude);
        map.put(Utils.LATITUDE, latitude);
        map.put(Utils.POSITION, position);
        map.put(Utils.PHOTO1, Photo1);
        map.put(Utils.PHOTO2, Photo2);
        map.put(Utils.USERVIDEO, userVideo);

        addSubscription(apiStores.register(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.uploadSuccess(model);
            }
            @Override
            public void onFailure(String msg) {
                mvpView.uploadfaail(msg);
            }
            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }
    //注册接口
    public void setExamine( String Photo1, String Photo2, String userVideo) {
        Map<String, String> map = new HashMap<>();
        map.put(Utils.PHOTO1, Photo1);
        map.put(Utils.PHOTO2, Photo2);
        map.put(Utils.USERVIDEO, userVideo);

        addSubscription(apiStores.setExamine(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    mvpView.setExamine(model.string());
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
