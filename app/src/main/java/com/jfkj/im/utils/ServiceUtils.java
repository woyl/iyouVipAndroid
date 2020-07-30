package com.jfkj.im.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;

public class ServiceUtils {
    /**
     * 判断服务是否开启
     * @param context
     * @param ServiceName
     *      服务的完整路径(例:com.example.service)
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (TextUtils.isEmpty(ServiceName)) {
            return false;
        }
        ActivityManager myManager =
                (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService =
                null;
        if (myManager != null) {
            runningService = (ArrayList<ActivityManager.RunningServiceInfo>)
                    myManager.getRunningServices(30);
        }
        if (runningService != null) {
            for (int i = 0; i < runningService.size(); i++) {
                if (runningService.get(i).service.getClassName().toString()
                        .equals(ServiceName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
