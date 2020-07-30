package com.jfkj.im.mvp.party;

import com.jfkj.im.Bean.AllMyPartysRedBean;
import com.jfkj.im.Bean.BaseBeans;
import com.jfkj.im.Bean.PartyBean;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class PartyPresenter extends BasePresenter<PartyView> {

    public PartyPresenter(PartyView view) {
        attachView(view);
    }

    public void getPartyRegister(String pageNo,String pageSize){
        mvpView.showLoading();
        Map<String,String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.PAGESIZE,pageSize);
        map.put(Utils.PAGENO,pageNo);
        addSubscription(apiStores.partyRegiser(map), new ApiCallback<BaseBeans<PartyBean>>() {
            @Override
            public void onSuccess(BaseBeans<PartyBean> model) {
                mvpView.PartySuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.PartyFails(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }

    public void getMyParty(String pageNo,String pageSize){
        mvpView.showLoading();
        Map<String,String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.PAGESIZE,pageSize);
        map.put(Utils.PAGENO,pageNo);
        addSubscription(apiStores.myParty(map), new ApiCallback<BaseBeans<PartyBean>>() {
            @Override
            public void onSuccess(BaseBeans<PartyBean> model) {
                mvpView.PartySuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.PartyFails(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }


    public void allMyPartysRedType(){
        Map<String,String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());


        addSubscription(apiStores.allMyPartysRedType(map), new ApiCallback<AllMyPartysRedBean>() {
            @Override
            public void onSuccess(AllMyPartysRedBean model) {
              mvpView.myPartysRedTypeSuccess(model);
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
