package com.jfkj.im.mvp.party;

import com.jfkj.im.Bean.BaseBean;
import com.jfkj.im.Bean.PartyInfoBean;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;

import java.util.HashMap;
import java.util.Map;

public class ReleasePartyPresenter extends BasePresenter<ReleasePartyView> {

    public ReleasePartyPresenter(ReleasePartyView partyDetailsView) {
        attachView(partyDetailsView);

    }

    public void addParty(Map<String,String> maps){
        mvpView.showLoading();

        addSubscription(apiStores.addParty(maps), new ApiCallback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean model) {
                mvpView.playSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                mvpView.playError();
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }

        });

    }


}
