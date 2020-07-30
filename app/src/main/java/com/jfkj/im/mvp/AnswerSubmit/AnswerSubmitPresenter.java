package com.jfkj.im.mvp.AnswerSubmit;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.selectpackets.SelectpacketsView;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class AnswerSubmitPresenter extends BasePresenter<AnswerSubmitView> {



    public AnswerSubmitPresenter(AnswerSubmitView selectpacketsView) {
        attachView(selectpacketsView);
    }

    /**
     *  钻石兑换汇率
     * @param IRHZ  1：人民币充值钻石   2：金币兑换钻石
     */
    public void getExchangeList(String IRHZ){

        Map<String,String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put("IRHZ",IRHZ);
        addSubscription(apiStores.exchangeList(map), new ApiCallback<ExChangeBean>() {
            @Override
            public void onSuccess(ExChangeBean model) {
                if(model.getCode().equals("200")){

                    SPUtils.getInstance(App.getAppContext()).put(Utils.EXCHANGELIST, JSON.toJSONString(model));
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

    public void getExchange(){
        String string = SPUtils.getInstance(App.getAppContext()).getString(Utils.EXCHANGELIST);
        mvpView.showList(JSON.parseObject(string,ExChangeBean.class));
    }
}


