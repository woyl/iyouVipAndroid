package com.jfkj.im.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jfkj.im.data.UserInfoManger;

import org.jetbrains.annotations.NotNull;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * <pre>
 * Description:  极光 自定义消息接收器
 * @author :   ys
 * @date :         2020/1/19
 * </pre>
 */
public class MyReceiver extends JPushMessageReceiver {
    private String TAG = "@@@";

    @Override
    public void onRegister(Context context, String s) {
        super.onRegister(context, s);
        JPushInterface.getRegistrationID(context);

        Log.d(TAG, "JPUSHRegistrationID_MyReceiver" + JPushInterface.getRegistrationID(context));
        UserInfoManger.saveRegistrationID(JPushInterface.getRegistrationID(context));
        Log.d(TAG, "JPUShaftersave" + UserInfoManger.getRegistrationID());
    }


}
