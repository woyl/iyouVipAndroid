package com.jfkj.im;

import android.content.Context;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/1/19
 * </pre>
 */
public class PushSDK  {

    public static void init(Context context) {
        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(context);
        Log.i("JPUSHRegistrationID:", JPushInterface.getRegistrationID(context));
    }

    public static String getRegistrationID(Context context) {
        return JPushInterface.getRegistrationID(context);
    }

    public static void resumePush(Context context) {
        JPushInterface.resumePush(context);
    }

    public static void stopPush(Context context) {
        JPushInterface.stopPush(context);
    }

    public static void clearLocalNotifications(Context context) {
        JPushInterface.clearLocalNotifications(context);
    }
}
