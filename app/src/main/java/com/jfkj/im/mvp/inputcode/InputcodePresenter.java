package com.jfkj.im.mvp.inputcode;

import android.util.Log;

import com.jfkj.im.App;
import com.jfkj.im.Bean.CustomerServiceBean;
import com.jfkj.im.Bean.LoginBean;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.activity.WebActivity;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.ResponseBody;

public class InputcodePresenter extends BasePresenter<InputcodeView> {

    public InputcodePresenter(InputcodeView view) {
        attachView(view);
    }


    //获取图片验证码
    public void getCode(String phone) {
        Map<String, String> map = new HashMap();
        map.put(Utils.MOBILENO, phone);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.CHANNEL_ANDROID);
        addSubscription(apiStores.getStaticCode(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.getBitmaSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getBitmapfail(msg);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    //验证验证码是否正确
    public void phoneLoginYZM(String mobileno, String types, String code, String longitude, String latitude, String locality) {

        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.MOBILENO, mobileno);
        map.put(Utils.TYPES, types);
        map.put(Utils.code, code);
        map.put(Utils.LONGITUDE, longitude);
        map.put(Utils.LATITUDE, latitude);
        map.put(Utils.POSITION, locality);

        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.CHANNEL_ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.DEVICEID, UUID.randomUUID().toString());

        OkGo.<String>post(ApiStores.base_url+"/user/phoneLoginYZM")
                .tag(App.getAppContext())
                .headers(Utils.TOKEN,SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                .headers(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(map)
                .execute(new com.lzy.okgo.callback.StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mvpView.loginregister(response);
                        mvpView.hideLoading();
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        mvpView.hideLoading();
                    }
                });
    }

    //验证验证码是否正确
    public void updatePassword(String mobileno, String password, String code) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.MOBILENO, mobileno);
        map.put(Utils.code, code);
        map.put(Utils.PASSWORD, password);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.CHANNEL_ANDROID);
        map.put(Utils.DEVICEID, UUID.randomUUID().toString());
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        addSubscription(apiStores.updatePassword(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.updatePassword(model);
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

    //获取验证码
    public void verification(String mobileno, String types, String code, String areacode) {
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.MOBILENO, mobileno);
        map.put(Utils.TYPES, types);
        map.put(Utils.IMGYZM, code);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.AREACODE, areacode);
        addSubscription(apiStores.getVPhone(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.getCodeSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getCodefail(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

//    public void verificationParameter(String token, LoginBean loginBean) {
//        Map<String, String> map = new HashMap<>();
//        map.put(Utils.OSNAME, Utils.ANDROID);
//        map.put(Utils.CHANNEL, Utils.ANDROID);
//        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
//        map.put(Utils.DEVICEID,  Utils.ANDROID);
//        map.put(Utils.DEVICENAME,Utils.getdeviceName());
//        map.put(Utils.REQ_TIME,AppUtils.getReqTime() );
//
//        Map<String, String> headmap = new HashMap<>();
////        headmap.put(Utils.SIGN, SignUtils.getInstance().getSignToken(map));
//        headmap.put(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()));
//        headmap.put(Utils.TOKEN, token);
//        OkHttpUtils.post()
//                .tag(App.getAppContext())
//                .url(ApiStores.base_url + "/user/verificationParameter")
//                .headers(headmap)
//                .params(map)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        mvpView.verificationParameterSuccess(response, token, loginBean);
//                    }
//                });
//    }


    public void getUserInfo(String token,LoginBean loginBean){
        Map<String,String> map = new HashMap<>();

        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.DEVICEID,  Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.REQ_TIME,AppUtils.getReqTime() );


        Map<String, String> headmap = new HashMap<>();
        headmap.put(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()));
        headmap.put(Utils.TOKEN, token);

        OkHttpUtils.post()
            .tag(this)
                .url(ApiStores.base_url + "/user/getUserInfo")
                .headers(headmap)
                .params(map)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        mvpView.verificationParameterSuccess(response, token, loginBean);

                    }
                });
    }

}
