package com.jfkj.im.mvp.mine;

import com.google.protobuf.Api;
import com.jfkj.im.App;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.CardInfoBean;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class CarryOverPresenter extends BasePresenter<CarryOverView> {

    public CarryOverPresenter(CarryOverView view) {
        attachView(view);
    }


    public void settlementApplication(String money){
        Map<String,String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put("money",money);
        addSubscription(apiStores.settlementApplication(map), new ApiCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse model) {
                if(model.isSuccess()){
                    mvpView.settlementSuccess(model);
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


    public void getBankCardInfo(){
        Map<String,String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        addSubscription(apiStores.bankCardInfo(map), new ApiCallback<CardInfoBean>() {
            @Override
            public void onSuccess(CardInfoBean model) {
                mvpView.showBankCardInfo(model);
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
