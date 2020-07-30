package com.jfkj.im.TIM.redpack.ice;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.Bean.IceSubmitBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMValueCallBack;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

public class IceUtils {

    public static void giveTask(String taskId, String userId, TIMValueCallBack<GiveThumbsUpBean> callBack,TIMValueCallBack<Boolean> callBack2){
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.TASK_ID,taskId);
        map.put(Utils.USERID,userId);
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(App.getAppContext())
                    .url(ApiStores.base_url + "/task/taskAdmire")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            callBack.onError(id,"");
                            callBack2.onError(id,"");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("code").equals("200")) {
                                    GiveThumbsUpBean giveThumbsUpBean = JSON.parseObject(jsonObject.getString("data"),GiveThumbsUpBean.class);
                                    callBack.onSuccess(giveThumbsUpBean);
                                } else if (jsonObject.getString("code").equals("50000")) {
                                    callBack2.onSuccess(true);
                                }
                                else {
                                    callBack.onSuccess(null);
                                    ToastUtils.showLongToast(jsonObject.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtils.showLongToast(R.string.nonetwork);
        }
    }

    public static void submitTask(String url,String taskId, String userId, TIMValueCallBack<IceSubmitBean> callBack){
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());


        map.put(Utils.TASK_ID,taskId);
        map.put(Utils.USERID,userId);
        map.put(Utils.URL,url);
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(App.getAppContext())
                    .url(ApiStores.base_url + "/task/confirmTask")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            callBack.onError(id,"");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("code").equals("200")) {
                                    IceSubmitBean giveThumbsUpBean = JSON.parseObject(jsonObject.getString("data"),IceSubmitBean.class);
                                    callBack.onSuccess(giveThumbsUpBean);
                                } else {
                                 //   ToastUtils.showLongToast(jsonObject.getString("message"));
                                    callBack.onSuccess(null);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtils.showLongToast(R.string.nonetwork);
        }
    }
}
