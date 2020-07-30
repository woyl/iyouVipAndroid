package com.jfkj.im.mvp.party;

import com.jfkj.im.Bean.BaseBean;
import com.jfkj.im.Bean.PartyInfoBean;
import com.jfkj.im.Bean.SignInBean;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class SignInPresenter extends BasePresenter<SignInView> {

    public SignInPresenter(SignInView partyDetailsView) {
        attachView(partyDetailsView);
    }



    public void confirmArrival(String userid , String partyId){
        mvpView.showLoading();

        Map<String,String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put("partyId"  , partyId);
        map.put("userId"  , userid);

        addSubscription(apiStores.confirmArrival(map), new ApiCallback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean model) {
                mvpView.confirmArrivalSuccess(model);
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


    public void inviteAllUsers(int pageNo, int pageSize , String partyId){

        mvpView.showLoading();

        Map<String,String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.PAGENO,pageNo + "");
        map.put(Utils.PAGESIZE , pageSize  + "");
        map.put("partyId"  , partyId);


        addSubscription(apiStores.inviteAllUsers(map), new ApiCallback<SignInBean>() {
            @Override
            public void onSuccess(SignInBean model) {
                mvpView.getSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.getError();
            }

            @Override
            public void onFinish() {
            mvpView.hideLoading();
            }
        });



    }

}
