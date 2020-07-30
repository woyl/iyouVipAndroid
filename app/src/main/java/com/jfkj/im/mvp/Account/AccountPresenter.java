package com.jfkj.im.mvp.Account;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.AccountBalance;
import com.jfkj.im.entity.AddMoneyBean;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.okhttp.HttpErrorMsgObserver;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class AccountPresenter extends BasePresenter<AccountView> {

    public AccountPresenter(AccountView accountView){
        attachView(accountView);
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

    public void account(){

        mvpView.showLoading();

        Map<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.CFROM,Utils.ANDROID);

        addSubscription(apiStores.czModeList(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    mvpView.hideLoading();
                    mvpView.AccountSuccess(model.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String msg) {
                OkLogger.e(msg);
            }

            @Override
            public void onFinish() {
                mvpView.hideLoading();
            }
        });
    }


    /**
     * 生成订单
     */
    public void  play(String alwId, String money,String cexchangeid){

        OkLogger.e(alwId  + " \t\t " + money + "\t\t" + cexchangeid);

        Map<String,String> maps = new HashMap<>();
        maps.put("payWayId",alwId);
        maps.put("money",money);
        maps.put("exchangeId",cexchangeid);
        maps.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        maps.put(Utils.OSNAME, Utils.ANDROID);
        maps.put(Utils.CHANNEL, Utils.ANDROID);
        maps.put(Utils.DEVICENAME, Utils.getdeviceName());
        maps.put(Utils.DEVICEID, Utils.ANDROID);
        maps.put(Utils.REQTIME, AppUtils.getReqTime());
        maps.put(Utils.CFROM,Utils.ANDROID);

        addSubscription(apiStores.addMoney(maps), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                try {
                    String string = model.string();

                    JSONObject jsonObject=new JSONObject(string);
                    if(jsonObject.getString("code").equals("200")){
                        AddMoneyBean addMoneyBean=JSON.parseObject(string,AddMoneyBean.class);
                        mvpView.playSuccess(addMoneyBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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


    public void accountBalance() {
        Map<String, String> mapHasp = new HashMap<>();
        mapHasp.put(Utils.OSNAME, Utils.ANDROID);
        mapHasp.put(Utils.CHANNEL, Utils.ANDROID);
        mapHasp.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        mapHasp.put(Utils.DEVICENAME, Utils.getdeviceName());
        mapHasp.put(Utils.DEVICEID, Utils.ANDROID);
        mapHasp.put(Utils.REQTIME, AppUtils.getReqTime());

        addSubscription(apiStores.accountBalance(mapHasp), new HttpErrorMsgObserver<AccountBalance>() {
            @Override
            public void onNext(AccountBalance accountBalance) {

                if (accountBalance.getCode().equals("200") ) {


                    UserInfoManger.saveUserBalance(accountBalance.getData().getJewel());
                    UserInfoManger.saveUserCost(accountBalance.getData().getJewelCost() + "");
                    UserInfoManger.saveUserGift(accountBalance.getData().getGoldCoin() + "");
                    UserInfoManger.saveUpgradeAmount(accountBalance.getData().getUpgradeAmount() + "");
                    UserInfoManger.savaAccumulatedGifts(accountBalance.getData().getGoldIncome() + "");
                    mvpView.accountBalanceSuccess();

                }
            }
        });
    }



}
