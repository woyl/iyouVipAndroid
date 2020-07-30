package com.jfkj.im.TIM.redpack.chatroom;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.event.GameStateEvent;
import com.jfkj.im.event.UpdateMessageEvent;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMValueCallBack;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

import static com.jfkj.im.TIM.helper.CustomMessage.RED_PACKAGE_MST_TYPE_THREE;

public class AnswerUtrils {

    private Context mContext;

    public AnswerUtrils(Context mContext) {
        this.mContext = mContext;
    }

    public void getData(TIMValueCallBack<List<AnswerBeans>> callBack){
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(mContext)
                    .url(ApiStores.base_url + "/square/getTestQuestion")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(mContext).getString(Utils.TOKEN))
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
//                                ToastUtils.showShortToast(jsonObject.getString("message"));
                                if (jsonObject.getString("code").equals("200")) {
                                    JSONObject object = new JSONObject(jsonObject.getString("data"));
                                    callBack.onSuccess(JSON.parseArray(object.getString("array"), AnswerBeans.class));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtils.showShortToast(R.string.nonetwork);
        }
    }

    public static void getSelfAnswer(String gameId,TIMValueCallBack<List<AnswerSelfBean>> callBack){
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.GAMEID,gameId);
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(App.getAppContext())
                    .url(ApiStores.base_url + "/square/loadSquareGameQuestion")
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
//                                ToastUtils.showShortToast(jsonObject.getString("message"));
                                String code = jsonObject.getString("code");
                                String message = jsonObject.getString("message");
                                if (code.equals("200")) {
                                    JSONObject object = new JSONObject(jsonObject.getString("data"));
                                    List<AnswerSelfBean> list = JSON.parseArray(object.getString("array"), AnswerSelfBean.class);
                                    callBack.onSuccess(list);
                                }else if (code.equals("60024")){//已经参加过了
                                    ToastUtils.showShortToast("你已参加过了");
                                } else {
                                    callBack.onSuccess(null);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtils.showShortToast(R.string.nonetwork);
        }
    }

    public static void getChatSelfAnswer(String gameId,TIMValueCallBack<List<AnswerSelfBean>> callBack){
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.GAMEID,gameId);
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(App.getAppContext())
                    .url(ApiStores.base_url + "/square/loadSquareGameQuestion")
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
//                                ToastUtils.showShortToast(jsonObject.getString("message"));
                                String code = jsonObject.getString("code");
                                String message = jsonObject.getString("message");
                                if (code.equals("200")) {
//                                    JSONObject object = new JSONObject(jsonObject.getString("data"));
//                                    List<AnswerSelfBean> list = JSON.parseArray(object.getString("array"), AnswerSelfBean.class);
//                                    callBack.onSuccess(list);
                                    Intent intent = new Intent(App.getAppContext(),CharacterActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("orderId",gameId);
                                    intent.putExtra("type","2");
                                    App.getAppContext().startActivity(intent);
                                }else if (code.equals("60024")){//已经参加过了
//                                    ToastUtils.showShortToast("你已参加过了");
                                    SPUtils.getInstance(App.getAppContext()).put(gameId,RED_PACKAGE_MST_TYPE_THREE);
                                    EventBus.getDefault().post(new UpdateMessageEvent(true));
                                } else {
                                    callBack.onSuccess(null);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtils.showShortToast(R.string.nonetwork);
        }
    }


    public static void getOtherUser(String gameId,TIMValueCallBack<List<AnswerBeans>> callBack){
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.GAMEID,gameId);
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(App.getAppContext())
                    .url(ApiStores.base_url + "/square/loadSquareGameQuestion")
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
//                                ToastUtils.showShortToast(jsonObject.getString("message"));
                                String code = jsonObject.getString("code");
                                String message = jsonObject.getString("message");
                                if (code.equals("200")) {
                                    JSONObject object = new JSONObject(jsonObject.getString("data"));
                                    List<AnswerBeans> list = JSON.parseArray(object.getString("array"), AnswerBeans.class);
                                    callBack.onSuccess(list);
                                }else if (code.equals("60024")){//已经参加过了
                                    ToastUtils.showShortToast("你已参加过了");
                                } else {
                                    callBack.onSuccess(null);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            ToastUtils.showShortToast(R.string.nonetwork);
        }
    }
}
