package com.jfkj.im.manager;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;

public class MyActivityManager {
    private static MyActivityManager sInstance = new MyActivityManager();

    private WeakReference<AppCompatActivity> sCurrentActivityWeakRef;

    private Object activityUpdateLock = new Object();
    private MyActivityManager() {

    }

    public static MyActivityManager getInstance() {
        return sInstance;
    }

    public AppCompatActivity getCurrentActivity() {
        AppCompatActivity currentActivity = null;
        synchronized (activityUpdateLock){
            if (sCurrentActivityWeakRef != null) {
                currentActivity = sCurrentActivityWeakRef.get();
            }
        }
        return currentActivity;
    }

    public void setCurrentActivity(AppCompatActivity activity) {
        synchronized (activityUpdateLock){
            sCurrentActivityWeakRef = new WeakReference<AppCompatActivity>(activity);
        }

    }
}
