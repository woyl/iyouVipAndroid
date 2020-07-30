package com.jfkj.im.TIM.redpack;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.redpack.chatroom.AnswerRedpackage;
import com.jfkj.im.TIM.redpack.game.GameRedPackageBean;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.event.JumpRedPackageDetailsEvent;
import com.jfkj.im.event.RedPackageEvent;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.LoadingDialog;
import com.jfkj.im.ui.dialog.RedPackageDialogFragment;
import com.jfkj.im.ui.fragment.ChatFragment;
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

import okhttp3.Call;

import static com.jfkj.im.TIM.helper.CustomMessage.INVITE_CUS_TYPE_THREE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_ONE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_TWO;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_MSG_TYPE_TWO;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PACKAGE_STATES_TWO;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PAGE_TYPE_CODE_FIV;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PAGE_TYPE_CODE_FOUR;

public class RedPackageUtil {

    private ChatFragment chatFragment;
    private RedPackageEvent event;
    private String redId;
    private TestingBean testingBean;
    private GameRedPackageBean gameRedPackageBean;
    private RedPackageCustom redPackageCustom;
    private ChatInfo mChatInfo;
    private LoadingDialog loadingDialog;
    private String type;



    public RedPackageUtil(ChatFragment chatFragment, RedPackageEvent data, ChatInfo mChatInfo) {
        this.chatFragment = chatFragment;
        this.event = data;
        this.mChatInfo = mChatInfo;
        loadingDialog = new LoadingDialog(chatFragment.getContext(), R.style.dialog);
    }

    public RedPackageUtil() {
    }

    /**
     * 检查红包
     */
    public void testing(String type) {
        this.type = type;

        loadingDialog.show();
        if (event.getCustomMessage().getSendType().equals(READ_PACKAGE_MSG_TYPE_TWO)) {
            gameRedPackageBean = event.getCustomMessage().getGameRedPackageBean();
            redPackageCustom = event.getCustomMessage().getPackageCustom();
            if (redPackageCustom == null) {
                ToastUtils.showShortToast("获取红包信息失败!");
                loadingDialog.dismiss();
                return;
            }
            redId = redPackageCustom.getRedId();
        } else {
            redId = event.getCustomMessage().getRedId();
        }

        if (TextUtils.isEmpty(redId)) {
            loadingDialog.dismiss();
            ToastUtils.showShortToast("获取红包信息失败!");
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.CHANNEL_ANDROID);
        map.put(Utils.CHANNEL, Utils.CHANNEL_ANDROID);
        map.put(Utils.DEVICEID, UserInfoManger.getRegistrationID() + "");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.REDID, redId);

        OkHttpUtils.post()
                .tag(this)
                .url(ApiStores.base_url + "/group/checkRedPacket")
                .addHeader(Utils.TOKEN, SPUtils.getInstance(chatFragment.getActivity()).getString(Utils.TOKEN))
                .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(map)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                loadingDialog.dismiss();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    loadingDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    switch (code) {
                        case "200":  //正在领取
                            testingBean = JSON.parseObject(jsonObject.getString("data"), TestingBean.class);
                            showDialog(code,message);
                            break;
                        case "60015": //60015已过期 60016领完 60018你已领过
//                            ToastUtils.showLongToast(message);
                            SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + redId, INVITE_CUS_TYPE_THREE);
//                            EventBus.getDefault().post(new UpdateMessageEvent(true));
                            showDialog(code,message);
                            break;
                        case "60016": //已过期 60016领完 60018你已领过
                            SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + redId, RED_PAGE_TYPE_CODE_FIV);
                            showDialog(code,message);
                            break;
                        case "60018": //已过期 60016领完 60018你已领过
                            SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + redId, RED_PAGE_TYPE_CODE_FOUR);
                            showDialog(code,message);
                            break;
                        case "60023"://需要完成任务才能领取红包
                            ToastUtils.showLongToast(message);
                            break;
                        default:
                            SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + redId, RED_PACKAGE_STATES_TWO);
                            showDialog(code,message);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showDialog(String code,String message) {

        RedPackageDialogFragment dialog = new RedPackageDialogFragment(true, Gravity.CENTER, chatFragment.getActivity());
        Bundle bundle = new Bundle();
        if (event.getCustomMessage().getSendType().equals(READ_PACKAGE_MSG_TYPE_TWO)) {
            if (!TextUtils.isEmpty(redPackageCustom.getRedDesc())) {
                if(redPackageCustom.getCusType().equals("2")){
                    bundle.putString("content", "冒险游戏");
                }else{
                    bundle.putString("content", redPackageCustom.getRedDesc());
                }


            } else {
                if (redPackageCustom.getCusType().equals(READ_PACKAGE_CUS_TYPE_ONE)) {
                    bundle.putString("content", redPackageCustom.getRedDesc());
                } else if (redPackageCustom.getCusType().equals(READ_PACKAGE_CUS_TYPE_TWO)) {
                    bundle.putString("content", "冒险游戏");
                }
            }
            bundle.putString("headUrl", redPackageCustom.getAvatarUrl());
            bundle.putString("nickName", redPackageCustom.getSendName());
        } else {
            bundle.putString("content", event.getCustomMessage().getRedDesc());
            bundle.putString("headUrl", event.getCustomMessage().getAvatarUrl());
            bundle.putString("nickName", event.getCustomMessage().getSendName());
        }
        bundle.putString("redPackId", redId);
        bundle.putString("id", mChatInfo.getId());
        /**
         * 红包状态 1 正在领取 2 已领取完 3过期退款
         * */
        bundle.putString("state", message);
        bundle.putString("code",code);
        bundle.putString("type",type);
        bundle.putParcelable("TestingBean",testingBean);
        bundle.putString("redType",event.getCustomMessage().getCusType());
        dialog.setArguments(bundle);
        if (chatFragment.getFragmentManager() != null) {
            dialog.show(chatFragment.getFragmentManager(), "");
        }
    }

    public void sendChatRoomRedPack(String groupId, String money, String number, String content,
                                    TIMValueCallBack<AnswerRedpackage> callBack,
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
        map.put(Utils.SIZE, number);
        map.put(Utils.SENDWORD, content);
        OkHttpUtils.post()
                .tag(this)
                .url(ApiStores.base_url + "/square/sendSquareRedPacket")
                .addHeader(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(map)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callBack.onError(id, "");
                callBack2.onError(id, "");
                callBack3.onError(id, "");
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    switch (code) {
                        case "200":
                            AnswerRedpackage answerRedpackage = JSON.parseObject(jsonObject.getString("data"), AnswerRedpackage.class);
                            callBack.onSuccess(answerRedpackage);
                            break;
                        case "50000": //{"code":"50000","message":"钻石余额不足","data":{}}
                            callBack2.onSuccess(true);
                            break;
                        case "60030":  //红包发放金额最低为3500钻石
                            callBack3.onSuccess(message);
                            break;
                        case "60031":  //单个红包不能超过一个
                            callBack3.onSuccess(message);
                            break;
                        default:
                            ToastUtils.showShortToast(jsonObject.getString("message"));
                            callBack.onSuccess(null);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //发普通红包
    public void sendRedPackage(String groupId, String money, String number, String content,
                               TIMValueCallBack<SendRedPackageBean> callBack,
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
        map.put(Utils.SIZE, number);
        map.put(Utils.SENDWORD, content);
        String urls = ApiStores.base_url + "/group/sendRedPacked";
        ;
        OkHttpUtils.post()
                .tag(this)
                .url(urls)
                .addHeader(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(map)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callBack.onError(id, "");
                callBack2.onError(id, "");
                callBack3.onError(id, "");
            }

            @Override
            public void onResponse(String response, int id) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    switch (code) {
                        case "200":
                            SendRedPackageBean sendRedPackageBean = JSON.parseObject(jsonObject.getString("data"), SendRedPackageBean.class);
                            callBack.onSuccess(sendRedPackageBean);
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
//                        ToastUtils.showShortToast(jsonObject.getString("message"));
                            callBack.onSuccess(null);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void taskRedpackage(String groupId, String taskRedpackId,
                               TIMValueCallBack<SendRedPackageBean> callBack,
                               TIMValueCallBack<Boolean> callBack2) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.CHANNEL_ANDROID);
        map.put(Utils.CHANNEL, Utils.CHANNEL_ANDROID);
        map.put(Utils.DEVICEID, UserInfoManger.getRegistrationID() + "");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.GROUPID, groupId);
        map.put(Utils.REDID, taskRedpackId);
        String urls = ApiStores.base_url + "/group/submitGameRedPacket";
        OkHttpUtils.post()
                .tag(this)
                .url(urls)
                .addHeader(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(map)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callBack.onError(id, "");
                callBack2.onError(id, "");
            }

            @Override
            public void onResponse(String response, int id) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    if (code.equals("200")) {
                        SendRedPackageBean sendRedPackageBean = JSON.parseObject(jsonObject.getString("data"), SendRedPackageBean.class);
                        callBack.onSuccess(sendRedPackageBean);
                    } else if (code.equals("50000")) {//{"code":"50000","message":"钻石余额不足","data":{}}
                        callBack2.onSuccess(true);
                    } else {
//                        ToastUtils.showShortToast(jsonObject.getString("message"));
                        callBack.onSuccess(null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void taskGroupEverydayRedpackage(String groupId, String taskRedpackId,
                               TIMValueCallBack<SendRedPackageBean> callBack,
                               TIMValueCallBack<Boolean> callBack2) {
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.CHANNEL_ANDROID);
        map.put(Utils.CHANNEL, Utils.CHANNEL_ANDROID);
        map.put(Utils.DEVICEID, UserInfoManger.getRegistrationID() + "");
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.REQ_TIME, AppUtils.getReqTime());
        map.put(Utils.GROUPID, groupId);
//        map.put(Utils.REDID, taskRedpackId);
        String urls = ApiStores.base_url + "/group/submitRedPacketByGroupTask";
        OkHttpUtils.post()
                .tag(this)
                .url(urls)
                .addHeader(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(map)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callBack.onError(id, "");
                callBack2.onError(id, "");
            }

            @Override
            public void onResponse(String response, int id) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    if (code.equals("200")) {
                        SendRedPackageBean sendRedPackageBean = JSON.parseObject(jsonObject.getString("data"), SendRedPackageBean.class);
                        callBack.onSuccess(sendRedPackageBean);
                    } else if (code.equals("50000")) {//{"code":"50000","message":"钻石余额不足","data":{}}
                        callBack2.onSuccess(true);
                    } else {
//                        ToastUtils.showShortToast(jsonObject.getString("message"));
                        callBack.onSuccess(null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
