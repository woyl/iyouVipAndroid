package com.jfkj.im.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.heytap.mcssdk.callback.PushCallback;
import com.heytap.mcssdk.mode.SubscribeResult;
import com.jfkj.im.TIM.utils.DemoLog;

import java.util.List;

public class OPPOPushImpl implements PushCallback {
    private static final String TAG = OPPOPushImpl.class.getSimpleName();

    @Override
    public void onRegister(int responseCode, String registerID) {
        DemoLog.i(TAG, "onRegister responseCode: " + responseCode + " registerID: " + registerID);

        ThirdPushTokenMgr.getInstance().setThirdPushToken(registerID);
        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
    }

    @Override
    public void onUnRegister(int responseCode) {
        DemoLog.i(TAG, "onUnRegister responseCode: " + responseCode);
    }

    @Override
    public void onSetPushTime(int responseCode, String s) {
        DemoLog.i(TAG, "onSetPushTime responseCode: " + responseCode + " s: " + s);
    }

    @Override
    public void onGetPushStatus(int responseCode, int status) {
        DemoLog.i(TAG, "onGetPushStatus responseCode: " + responseCode + " status: " + status);
    }

    @Override
    public void onGetNotificationStatus(int responseCode, int status) {
        DemoLog.i(TAG, "onGetNotificationStatus responseCode: " + responseCode + " status: " + status);
    }

    @Override
    public void onGetAliases(int responseCode, List<SubscribeResult> alias) {
        DemoLog.i(TAG, "onGetAliases responseCode: " + responseCode + " alias: " + alias);
    }

    @Override
    public void onSetAliases(int responseCode, List<SubscribeResult> alias) {
        DemoLog.i(TAG, "onSetAliases responseCode: " + responseCode + " alias: " + alias);
    }

    @Override
    public void onUnsetAliases(int responseCode, List<SubscribeResult> alias) {
        DemoLog.i(TAG, "onUnsetAliases responseCode: " + responseCode + " alias: " + alias);
    }

    @Override
    public void onSetUserAccounts(int responseCode, List<SubscribeResult> accounts) {
        DemoLog.i(TAG, "onSetUserAccounts responseCode: " + responseCode + " accounts: " + accounts);
    }

    @Override
    public void onUnsetUserAccounts(int responseCode, List<SubscribeResult> accounts) {
        DemoLog.i(TAG, "onUnsetUserAccounts responseCode: " + responseCode + " accounts: " + accounts);
    }

    @Override
    public void onGetUserAccounts(int responseCode, List<SubscribeResult> accounts) {
        DemoLog.i(TAG, "onGetUserAccounts responseCode: " + responseCode + " accounts: " + accounts);
    }

    @Override
    public void onSetTags(int responseCode, List<SubscribeResult> tags) {
        DemoLog.i(TAG, "onSetTags responseCode: " + responseCode + " tags: " + tags);
    }

    @Override
    public void onUnsetTags(int responseCode, List<SubscribeResult> tags) {
        DemoLog.i(TAG, "onUnsetTags responseCode: " + " tags: " + tags);
    }

    @Override
    public void onGetTags(int responseCode, List<SubscribeResult> tags) {
        DemoLog.i(TAG, "onGetTags responseCode: " + responseCode + " tags: " + tags);
    }
}
