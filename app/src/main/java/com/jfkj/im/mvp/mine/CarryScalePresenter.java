package com.jfkj.im.mvp.mine;

import com.jfkj.im.App;
import com.jfkj.im.entity.CarryScaleBean;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class CarryScalePresenter extends BasePresenter<CarryScaleView> {

    public CarryScalePresenter(CarryScaleView view) {
        attachView(view);
    }


    /**
     * 获取用户结算比例
     */
    public void getUserCashRate(){
        Map<String,String> maps = new HashMap<>();
        maps.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        maps.put(Utils.OSNAME, Utils.ANDROID);
        maps.put(Utils.CHANNEL, Utils.ANDROID);
        maps.put(Utils.DEVICENAME, Utils.getdeviceName());
        maps.put(Utils.DEVICEID, Utils.ANDROID);
        maps.put(Utils.REQTIME, AppUtils.getReqTime());


        addSubscription(apiStores.getUserCashRate(maps), new ApiCallback<CarryScaleBean>() {
            @Override
            public void onSuccess(CarryScaleBean bean) {

                mvpView.onUserCashRate(bean);
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
