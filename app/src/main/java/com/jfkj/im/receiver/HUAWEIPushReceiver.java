package com.jfkj.im.receiver;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;
import com.huawei.hms.support.api.push.PushReceiver;
import com.lzy.okgo.utils.OkLogger;


public class HUAWEIPushReceiver extends HmsMessageService {

    private static final String TAG = HUAWEIPushReceiver.class.getSimpleName();

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        OkLogger.e("onNewToken = " + s);

        if(!TextUtils.isEmpty(s)){
            ThirdPushTokenMgr.getInstance().setThirdPushToken(s);
            ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
        }
    }


    @Override
    public void onMessageReceived(RemoteMessage  message) {
        Log.i(TAG, "onMessageReceived is called");
        if (message == null) {
            Log.e(TAG, "Received message entity is null!");
            return;
        }
        Log.e(TAG, "getCollapseKey: " + message.getCollapseKey()
                + "\n getData: " + message.getData()
                + "\n getFrom: " + message.getFrom()
                + "\n getTo: " + message.getTo()
                + "\n getMessageId: " + message.getMessageId()
//                + "\n getOriginalPriority: " + message.getOriginalPriority()
//                + "\n getPriority: " + message.getPriority()
                + "\n getSendTime: " + message.getSentTime()
                + "\n getMessageType: " + message.getMessageType()
                + "\n getTtl: " + message.getTtl());
        RemoteMessage.Notification notification = message.getNotification();
        if (notification != null) {
            Log.i(TAG, "\n getImageUrl: " + notification.getImageUrl()
                    + "\n getTitle: " + notification.getTitle()
                    + "\n getTitleLocalizationKey: " + notification.getTitleLocalizationKey()
//                    + "\n getTitleLocalizationArgs: " + Arrays.toString(notification.getTitleLocalizationArgs())
                    + "\n getBody: " + notification.getBody()
                    + "\n getBodyLocalizationKey: " + notification.getBodyLocalizationKey()
//                    + "\n getBodyLocalizationArgs: " + Arrays.toString(notification.getBodyLocalizationArgs())
                    + "\n getIcon: " + notification.getIcon()
                    + "\n getSound: " + notification.getSound()
                    + "\n getTag: " + notification.getTag()
                    + "\n getColor: " + notification.getColor()
                    + "\n getClickAction: " + notification.getClickAction()
                    + "\n getChannelId: " + notification.getChannelId()
                    + "\n getLink: " + notification.getLink()
                    + "\n getNotifyId: " + notification.getNotifyId());
        }
        Boolean judgeWhetherIn10s = false;
        // If the messages are not processed in 10 seconds, the app needs to use WorkManager for processing.
        if (judgeWhetherIn10s) {
            startWorkManagerJob(message);
        } else {
            // Process message within 10s
            processWithin10s(message);
        }
    }
    private void startWorkManagerJob(RemoteMessage message) {
        Log.e(TAG, "Start new job processing.");
    }
    private void processWithin10s(RemoteMessage message) {
        Log.e(TAG, "Processing now.");
    }
}