package com.jfkj.im.mvp.mine;

import com.jfkj.im.App;
import com.jfkj.im.entity.ResultRecordBean;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;

import java.util.HashMap;
import java.util.Map;

public class ResultRecordPresenter extends BasePresenter<ResultRecordView> {

    public ResultRecordPresenter(ResultRecordView view) {
        attachView(view);
    }


    public void settlementRecord(int pageNo, int pageSize){

        Map<String,String> map  = new HashMap<>();

        map.put("pageNo",String.valueOf(pageNo));
        map.put("pageSize",String.valueOf(pageSize));
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(apiStores.settlementRecord(map), new ApiCallback<ResultRecordBean>() {
            @Override
            public void onSuccess(ResultRecordBean model) {
                if(model.getCode().equals("200")){
                    mvpView.showMentRecord(model);
                }else {
                    ToastUtils.showShortToast(model.getMessage());
                }
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
