package com.jfkj.im.mvp.Userhead;

import com.jfkj.im.App;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

public class UserheadPresenter extends BasePresenter<UserheadView> {

    public UserheadPresenter(UserheadView userheadView) {
        attachView(userheadView);
    }

    public void getuploadImage(String type, String mobileno, MultipartBody.Part file) {
        mvpView.showLoading();
        addSubscription(apiStores.uploadImage(type,mobileno,file), new ApiCallback<ResponseBody>() {
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

    //如果是男生注册用户  那就就使用这个接口
    public void registerman(String mobileno, String password, String birthday, String gender, String head,
                            String nickname, String longitude, String latitude, String position) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.MOBILENO, mobileno);
        map.put(Utils.PASSWORD, password);
        map.put(Utils.Birthday, birthday);
        map.put(Utils.GENDER, gender);
        map.put(Utils.HEAD, head);
        map.put(Utils.NICKNAME, nickname);
        map.put(Utils.LONGITUDE, longitude);
        map.put(Utils.LATITUDE, latitude);
        map.put(Utils.POSITION, position);
        map.put(Utils.OSNAME, Utils.ANDROID);

        addSubscription(apiStores.register(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.registSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.registfail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
