package com.jfkj.im.TIM.modules.group.info;

import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.redpack.group.GroupInfoBean;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.TIM.utils.TUIKitLog;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.retrofit.Urls;
import com.jfkj.im.utils.AppManager;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMValueCallBack;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;


public class GroupInfoPresenter {

    private GroupInfoLayout mInfoLayout;
    private GroupInfoProvider mProvider;

    public GroupInfoPresenter(GroupInfoLayout layout) {
        this.mInfoLayout = layout;
        mProvider = new GroupInfoProvider();
    }

    public GroupInfoPresenter(){

    }

    public void loadGroupInfo(String groupId, final IUIKitCallBack callBack) {
        mProvider.loadGroupInfo(groupId, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                callBack.onSuccess(data);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                TUIKitLog.e("loadGroupInfo", errCode + ":" + errMsg);
                callBack.onError(module, errCode, errMsg);
                ToastUtil.toastLongMessage(errMsg);
            }
        });
    }

    public void modifyGroupName(final String name, TIMValueCallBack<Object> callBack) {
        mProvider.modifyGroupInfo(name, TUIKitConstants.Group.MODIFY_GROUP_NAME, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                callBack.onSuccess(data);
                mInfoLayout.onGroupInfoModified(name, TUIKitConstants.Group.MODIFY_GROUP_NAME);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                TUIKitLog.e("modifyGroupName", errCode + ":" + errMsg);
                ToastUtil.toastLongMessage(errMsg);
                callBack.onError(errCode,errMsg);
            }
        });
    }

    public void modifyGroupNotice(final String notice) {
        mProvider.modifyGroupInfo(notice, TUIKitConstants.Group.MODIFY_GROUP_NOTICE, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                mInfoLayout.onGroupInfoModified(notice, TUIKitConstants.Group.MODIFY_GROUP_NOTICE);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                TUIKitLog.e("modifyGroupNotice", errCode + ":" + errMsg);
                ToastUtil.toastLongMessage(errMsg);
            }
        });
    }


    public String getNickName() {
        String nickName = "";
        if (mProvider.getSelfGroupInfo() != null) {
            if (mProvider.getSelfGroupInfo().getDetail() != null) {
                nickName = mProvider.getSelfGroupInfo().getDetail().getNameCard();
            }
        }
        return nickName == null ? "" : nickName;
    }

    public void modifyMyGroupNickname(final String nickname) {
        mProvider.modifyMyGroupNickname(nickname, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                mInfoLayout.onGroupInfoModified(nickname, TUIKitConstants.Group.MODIFY_MEMBER_NAME);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                TUIKitLog.e("modifyMyGroupNickname", errCode + ":" + errMsg);
                ToastUtil.toastLongMessage(errMsg);
            }
        });
    }

    public void deleteGroup() {
        mProvider.deleteGroup(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                ((Activity) mInfoLayout.getContext()).finish();
                AppManager.getAppManager().finishAllActivity();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                TUIKitLog.e("deleteGroup", errCode + ":" + errMsg);
                ToastUtil.toastLongMessage(errMsg);
            }
        });
    }

    public void setTopConversation(boolean flag) {
        mProvider.setTopConversation(flag);
    }

    public void setNoMessage(boolean flag,IUIKitCallBack callBack){
        mProvider.setNoMessage(flag,callBack);
    }

    public void quitGroup() {
        mProvider.quitGroup(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                ((Activity) mInfoLayout.getContext()).finish();
                AppManager.getAppManager().finishAllActivity();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ((Activity) mInfoLayout.getContext()).finish();
                TUIKitLog.e("quitGroup", errCode + ":" + errMsg);
            }
        });
    }

    public void modifyGroupInfo(int value, int type) {
        mProvider.modifyGroupInfo(value, type, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                mInfoLayout.onGroupInfoModified(data, TUIKitConstants.Group.MODIFY_GROUP_JOIN_TYPE);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastLongMessage("modifyGroupInfo fail :" + errCode + "=" + errMsg);
            }
        });
    }

    //加载群组介绍页面
    public void loadGroupInfo(String groupId, Context mContext,final IUIKitCallBack callBack){
        HashMap<String, String> map = new HashMap<>();
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, "1");
        map.put(Utils.REQTIME, AppUtils.getReqTime());
        map.put(Utils.GROUPID,groupId);


        if (Utils.netWork()) {


            OkHttpUtils.post()
                    .tag(mContext)
                        .url(Urls.base_url + "/group/loadGroupInfo")
                    .addHeader(Utils.TOKEN, SPUtils.getInstance(mContext.getApplicationContext()).getString(Utils.TOKEN))
                    .addHeader(Utils.SIGN,  MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            callBack.onError("",id,"");
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("code").equals("200")) {
                                    GroupInfoBean groupInfoBean = JSON.parseObject(jsonObject.getString("data"),GroupInfoBean.class);
                                    callBack.onSuccess(groupInfoBean);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {

        }
    }
}
