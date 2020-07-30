package com.jfkj.im.TIM.redpack.game;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;


public class GameUtils {

    private TIMConversation conversation;

    public GameUtils(String groupId) {
        conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, groupId);
    }

    public void genAdventureGameRedPack(Context context, String groupId, String money, String num, String content, String gameType,
                                        IUIKitCallBack callBack,
                                        TIMValueCallBack<Boolean> callBack2,
                                        TIMValueCallBack<String> callBack3) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.CHANNEL_ANDROID);
        map.put(Utils.CHANNEL, Utils.CHANNEL_ANDROID);
        map.put(Utils.DEVICEID, UserInfoManger.getRegistrationID() + "");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.GROUPID, groupId);
        map.put(Utils.MONEY, money);
        map.put(Utils.SIZE, num);
        map.put(Utils.CONTENT, content);
        map.put(Utils.GAME_TYPE, gameType);
        OkHttpUtils.post()
                .tag(this)
                .url(ApiStores.base_url + "/group/genAdventureGame")
                .addHeader(Utils.TOKEN, SPUtils.getInstance(context.getApplicationContext()).getString(Utils.TOKEN))
                .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(map)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callBack.onError("",id,"");
                callBack2.onError(id,"");
                callBack3.onError(id,"");
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    switch (code) {
                        case "200":
                            GameRedPackageBean gameRedPackageBean = JSON.parseObject(jsonObject.getString("data"), GameRedPackageBean.class);
                            callBack.onSuccess(gameRedPackageBean);
                            break;
                        case "50000": //{"code":"50000","message":"钻石余额不足","data":{}}
                            callBack2.onSuccess(true);
                            break;
                        case "60011":  //红包的数量超过群成员人数
                            callBack3.onSuccess(message);
                            break;
                        case "60032":  //单个红包不能低于10钻石
                            callBack3.onSuccess(message);
                            break;
                        default:
                            callBack.onError("", id, jsonObject.getString("message"));
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
