package com.jfkj.im.TIM.redpack.chatroom;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.Bean.UserInfoBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.component.photoview.Util;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.activity.Loginregister_phone_Activity;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class ChatRoomUtil {
    public static void CreateChatRoom(TIMValueCallBack callBack) {
        TIMGroupManager.CreateGroupParam createGroupParam = new TIMGroupManager.CreateGroupParam("AVChatRoom", "广场");
        createGroupParam.setGroupId(SPUtils.getInstance(App.getAppContext()).getString(Utils.AV_CHAT_ROOM_ID));
        TIMGroupManager.getInstance().createGroup(createGroupParam, new TIMValueCallBack<String>() {
            @Override
            public void onError(int code, String desc) {
                Log.e("msg", "create group failed. code: " + code + " errmsg: " + desc);
                callBack.onError(code, desc);
            }

            @Override
            public void onSuccess(String s) {
                Log.e("msg", "create group succ, groupId:" + s);
                callBack.onSuccess(s);
            }
        });
    }

    public static void quitGroup() {
        if (!TextUtils.isEmpty(SPUtils.getInstance(App.getAppContext()).getString(Utils.AV_CHAT_ROOM_ID))) {
            //退出群组
            TIMGroupManager.getInstance().quitGroup(
                    SPUtils.getInstance(App.getAppContext()).getString(Utils.AV_CHAT_ROOM_ID),  //群组 ID
                    new TIMCallBack() {
                        @Override
                        public void onError(int code, String desc) {
                            //错误码 code 和错误描述 desc，可用于定位请求失败原因
                            //错误码 code 含义请参见错误码表

                        }

                        @Override
                        public void onSuccess() {

                        }
                    });      //回调
        }
    }

    //加入广场
    public static void applyJoinGroup() {
        if (!TextUtils.isEmpty(SPUtils.getInstance(App.getAppContext()).getString(Utils.AV_CHAT_ROOM_ID))) {
            TIMGroupManager.getInstance().applyJoinGroup(SPUtils.getInstance(App.getAppContext()).getString(Utils.AV_CHAT_ROOM_ID), "some reason", new TIMCallBack() {
                @java.lang.Override
                public void onError(int code, String desc) {
                    //接口返回了错误码 code 和错误描述 desc，可用于原因
                    //错误码 code 列表请参见错误码表
                    OkLogger.e(code +"加入群聊失败\t\t" + desc);
                }

                @java.lang.Override
                public void onSuccess() {
                    TIMConversation timConversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, SPUtils.getInstance(App.getAppContext()).getString(Utils.AV_CHAT_ROOM_ID));
                    TIMMessage timMessage=new TIMMessage();
                    timConversation.getMessage(999,timMessage, new TIMValueCallBack<List<TIMMessage>>() {
                        @Override
                        public void onError(int code, String desc) {

                        }

                        @Override
                        public void onSuccess(List<TIMMessage> timMessages) {

                        }
                    });
                }
            });
        }
            TIMGroupManager.getInstance().applyJoinGroup(Utils.CIRCLEROOMID, "some reason", new TIMCallBack() {
                            @java.lang.Override
                public void onError(int code, String desc) {
                    OkLogger.e(code +"加入群聊失败\t\t" + desc);
                }
                @java.lang.Override
                public void onSuccess() {

                }
            });

    }

    //性格测试是否结束
    public static void getSquareTips(TIMValueCallBack<Integer> callBack) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        if (Utils.netWork()) {
            OkHttpUtils.post()
                    .tag(App.getAppContext())
                    .url(ApiStores.base_url + "/square/getSquareTips")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            callBack.onError(id, "");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
//                                ToastUtils.showShortToast(jsonObject.getString("message"));
                                if (jsonObject.getString("code").equals("200")) {
                                    ChatTipsBean chatTipsBean = JSON.parseObject(jsonObject.getString("data"), ChatTipsBean.class);
                                    callBack.onSuccess(chatTipsBean.getIsPopup());
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
