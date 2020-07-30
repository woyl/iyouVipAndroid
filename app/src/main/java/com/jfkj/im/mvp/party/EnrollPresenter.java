package com.jfkj.im.mvp.party;

import com.jfkj.im.Bean.BaseBean;
import com.jfkj.im.Bean.PartyInfoBean;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.BaseView;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class EnrollPresenter extends BasePresenter<EnrollView> {

    public EnrollPresenter(EnrollView partyDetailsView) {
        attachView(partyDetailsView);
    }


    public void joinParty(String arriveTime, String partyId){
        mvpView.showLoading();

        Map<String,String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put("partyId",partyId);
        map.put("arriveTime",arriveTime);


        addSubscription(apiStores.joinParty(map), new ApiCallback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean model) {
                mvpView.joinPartySuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.joinPartyError();
            }

            @Override
            public void onFinish() {

            }
        });

    }



}
