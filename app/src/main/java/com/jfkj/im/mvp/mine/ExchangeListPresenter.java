package com.jfkj.im.mvp.mine;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.TIM.component.photoview.Util;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.SettleAccountBean;
import com.jfkj.im.entity.UserPraiseBean;
import com.jfkj.im.model.MineModel;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
import com.jfkj.im.okhttp.LoginException;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/1/16
 * </pre>
 */
public class ExchangeListPresenter extends BasePresenter<ExchangeView> {
    private MineModel mModel;

    public ExchangeListPresenter(ExchangeView view){
        attachView(view);
        mModel = new MineModel();
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
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put("IRHZ",IRHZ);

        addSubscription(apiStores.exchangeList(map), new ApiCallback<ExChangeBean>() {
            @Override
            public void onSuccess(ExChangeBean model) {
                if(model.getCode().equals("200")){
                    mvpView.showList(model);
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


    public void settlement(){
        Map<String,String> map  = new HashMap<>();

        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(apiStores.settlement(map), new ApiCallback<SettleAccountBean>() {
            @Override
            public void onSuccess(SettleAccountBean model) {
                mvpView.showSettlement(model);
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


    public void exchange(String exchangeId, String irhz) {
        Map<String,String> map = new HashMap<>();
        map.put("exchangeId",exchangeId);
        map.put("IRHZ",irhz);
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        addSubscription(mModel.exchangeDiamond(map), new HttpErrorMsgObserver<BaseResponse>() {
            @Override
            public void onNext(BaseResponse o) {
                OkLogger.e("兑换---" + o.getMessage());
                if (o.isSuccess()){
                    mvpView.exchangeSuccess();
                }else {
                    ToastUtils.showShortToast(o.message);
                }
            }
        });
    }


}
