package com.jfkj.im.mvp.Circlefriend;

import com.jfkj.im.App;
import com.jfkj.im.entity.CircleBean;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class CirclefriendPresenter extends BasePresenter<CirclefriendView> {

    public CirclefriendPresenter (CirclefriendView circlefriendView){
        attachView(circlefriendView);
    }
    public void Circlefriend(String pageNo){
        mvpView.showLoading();
        Map<String, String> map = new HashMap<>();
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.PAGENO, pageNo);
        map.put(Utils.PAGESIZE, 10+"");


        addSubscription(apiStores.circlefriend(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.CirclefriendSuccess(model);
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

}
