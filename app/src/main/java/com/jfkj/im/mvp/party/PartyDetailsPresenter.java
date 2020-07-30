package com.jfkj.im.mvp.party;

import android.util.Log;

import com.jfkj.im.Bean.PartyInfoBean;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;

import java.util.HashMap;
import java.util.Map;

public class PartyDetailsPresenter extends BasePresenter<PartyDetailsView> {

    public PartyDetailsPresenter(PartyDetailsView partyDetailsView) {
        attachView(partyDetailsView);
    }



    public void getPartyDetails(String partyId){
        mvpView.showLoading();

        Map<String,String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        map.put("partyId",partyId);

        addSubscription(apiStores.partyInfo(map), new ApiCallback<PartyInfoBean>() {
            @Override
            public void onSuccess(PartyInfoBean model) {
                mvpView.getPartyDetailsSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                OkLogger.e(msg);
            }

            @Override
            public void onFinish() {

            }
        });

    }
}
