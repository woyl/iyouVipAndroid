package com.jfkj.im.TIM.redpack.ice;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;

import com.alibaba.fastjson.JSON;
import com.jfkj.im.App;
import com.jfkj.im.Bean.chat.IceCheckRedpacketBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.event.RedPackageIceEvent;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.LoadingDialog;
import com.jfkj.im.ui.dialog.RedPackageDialogFragment;
import com.jfkj.im.ui.dialog.RedPackageIceDialogFragment;
import com.jfkj.im.ui.fragment.ChatFragment;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

import static com.jfkj.im.TIM.helper.CustomMessage.INVITE_CUS_TYPE_THREE;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PACKAGE_STATES_TWO;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PAGE_TYPE_CODE_FIV;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PAGE_TYPE_CODE_FOUR;

public class RedpacketIceUtils {
    private ChatFragment chatFragment;
    private RedPackageIceEvent event;
    private ChatInfo mChatInfo;
    private LoadingDialog loadingDialog;
    private String redId,content,nickName,head;
    private RedpacketIceBean redpacketIceBean;

    public RedpacketIceUtils(ChatFragment chatFragment, RedPackageIceEvent data, ChatInfo mChatInfo) {
        this.chatFragment = chatFragment;
        this.event = data;
        this.mChatInfo = mChatInfo;
        loadingDialog = new LoadingDialog(chatFragment.getContext(), R.style.dialog);
    }

    public void checkRed(){
        loadingDialog.show();
        redId = event.getCustomMessage().getRedId();
        content = event.getCustomMessage().getTaskContent();
        nickName = event.getCustomMessage().getSendName();
        head = event.getCustomMessage().getHead();
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
                .url(ApiStores.base_url + "/task/checkTaskRedPacket")
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
                    String codes = jsonObject.getString("code");
                    String messages = jsonObject.getString("message");
                    String data = jsonObject.getString("data");
                    IceCheckRedpacketBean iceCheckRedpacketBean = JSON.parseObject(data,IceCheckRedpacketBean.class);
                    String message = iceCheckRedpacketBean.getMessage();
                    String code = iceCheckRedpacketBean.getCode();
                    switch (iceCheckRedpacketBean.getCode()) {
                        case "200":  //正在领取
                            redpacketIceBean = JSON.parseObject(jsonObject.getString("data"), RedpacketIceBean.class);
                            showDialog(code,message);
                            break;
                        case "60015": //60015已过期 60016领完 60018你已领过
                            ToastUtils.showLongToast(message);
                            SPUtils.getInstance(App.getAppContext()).put(Utils.APPID + redId, INVITE_CUS_TYPE_THREE);
//                            EventBus.getDefault().post(new UpdateMessageEvent(true));
//                            showDialog(code,message);
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

    private void showDialog(String code, String message) {
        RedPackageIceDialogFragment dialog = new RedPackageIceDialogFragment(true, Gravity.CENTER, chatFragment.getActivity());
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        bundle.putString("headUrl", head);
        bundle.putString("nickName", nickName);
        bundle.putString("redPackId", redId);
        bundle.putString("id", mChatInfo.getId());
        /**
         * 红包状态 1 正在领取 2 已领取完 3过期退款
         * */
        bundle.putString("state", message);
        bundle.putString("code",code);
//        bundle.putString("redType",event.getCustomMessage().getCusType());
        dialog.setArguments(bundle);
        if (chatFragment.getFragmentManager() != null) {
            dialog.show(chatFragment.getFragmentManager(), "");
        }
    }
}
