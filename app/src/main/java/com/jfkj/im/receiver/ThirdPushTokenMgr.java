package com.jfkj.im.receiver;

import android.text.TextUtils;

import com.jfkj.im.TIM.utils.DemoLog;
import com.jfkj.im.TIM.utils.PrivateConstants;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushToken;
import com.tencent.imsdk.utils.IMFunc;

public class ThirdPushTokenMgr {
    public static final boolean USER_GOOGLE_FCM = false;
    private static final String TAG = ThirdPushTokenMgr.class.getSimpleName();
    private String mThirdPushToken;

    public static ThirdPushTokenMgr getInstance() {
        return ThirdPushTokenHolder.instance;
    }

    public String getThirdPushToken() {
        return mThirdPushToken;
    }

    public void setThirdPushToken(String mThirdPushToken) {
        this.mThirdPushToken = mThirdPushToken;
    }

    public void setPushTokenToTIM() {
        String token = ThirdPushTokenMgr.getInstance().getThirdPushToken();
        if (TextUtils.isEmpty(token)) {
            DemoLog.i(TAG, "setPushTokenToTIM third token is empty");
            return;
        }
        TIMOfflinePushToken param = null;
        if (USER_GOOGLE_FCM) {
            param = new TIMOfflinePushToken(PrivateConstants.GOOGLE_FCM_PUSH_BUZID, token);
        } else if (IMFunc.isBrandXiaoMi()) {
            param = new TIMOfflinePushToken(PrivateConstants.XM_PUSH_BUZID, token);
        } else if (IMFunc.isBrandHuawei()) {
            param = new TIMOfflinePushToken(PrivateConstants.HW_PUSH_BUZID, token);
        } else if (IMFunc.isBrandMeizu()) {
            param = new TIMOfflinePushToken(PrivateConstants.MZ_PUSH_BUZID, token);
        } else if (IMFunc.isBrandOppo()) {
            param = new TIMOfflinePushToken(PrivateConstants.OPPO_PUSH_BUZID, token);
        } else if (IMFunc.isBrandVivo()) {
            param = new TIMOfflinePushToken(PrivateConstants.VIVO_PUSH_BUZID, token);
        } else {
            return;
        }
        TIMManager.getInstance().setOfflinePushToken(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                DemoLog.d(TAG, "setOfflinePushToken err code = " + code);
            }

            @Override
            public void onSuccess() {
                DemoLog.d(TAG, "setOfflinePushToken success");
            }
        });
    }

    private static class ThirdPushTokenHolder {
        private static final ThirdPushTokenMgr instance = new ThirdPushTokenMgr();
    }
}
