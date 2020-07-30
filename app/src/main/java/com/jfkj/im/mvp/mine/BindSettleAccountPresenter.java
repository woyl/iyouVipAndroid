package com.jfkj.im.mvp.mine;

import com.jfkj.im.App;
import com.jfkj.im.Bean.GetBindBankBean;
import com.jfkj.im.entity.BindSettleAccountBean;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class BindSettleAccountPresenter extends BasePresenter<BindSettleAccountView> {


    public BindSettleAccountPresenter(BindSettleAccountView bindSettleAccountView) {
        attachView(bindSettleAccountView);

    }



    public void bindBank(String bankCard, String bankName, String bankBranchName){
        Map<String,String > map  = new HashMap<>();
        map.put("bankCard",bankCard);
        map.put("bankName",bankName);
        map.put("bankBranchName",bankBranchName);


        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());


        addSubscription(apiStores.bindBank(map), new ApiCallback<BindSettleAccountBean>() {

            @Override
            public void onSuccess(BindSettleAccountBean model) {
                mvpView.bindSuccess(model);
            }

            @Override
            public void onFailure(String msg) {
                
            }

            @Override
            public void onFinish() {

            }
        });

    }

    public void getBindBank(){
        Map<String,String > map  = new HashMap<>();

        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());


        addSubscription(apiStores.getBindBank(map), new ApiCallback<GetBindBankBean>() {

            @Override
            public void onSuccess(GetBindBankBean model) {
                if (model.getCode().equals("200")){
                    mvpView.getBindSuccess(model.getData().getRealName());
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




}
