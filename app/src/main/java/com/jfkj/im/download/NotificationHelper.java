package com.jfkj.im.download;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;

import com.jfkj.im.R;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.app.NotificationCompat.PRIORITY_MAX;

/**
 * 通知栏工具类 如果更新需要显示在通知栏 可以使用这个类来处理
 */

public class NotificationHelper {
    static final String NOTIFICATION_CHANNEL_ID = "CHANNEL_experts";
    static final String NOTIFICATION_NAME = "Downloading calls";
    private static final long NOTIFICATION_PUBLISH_INTERVAL = 500;//500毫秒更新一次进度条

    private final Context mContext;
    private final NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private final int mNotifyId;
    private long mNextPublishTime;

    public NotificationHelper(Context context, int notifyId) {
        this.mContext = context;
        mNotifyId = notifyId;
        mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_LOW);
//            channel.enableVibration(false);
//            channel.setVibrationPattern(new long[]{0});
            mNotificationManager.createNotificationChannel(channel);
        }
        mNextPublishTime = SystemClock.uptimeMillis();
    }

    public Notification createNotification() {
        return getBuilder().build();
    }

    public void waitDownload() {
        NotificationCompat.Builder builder = getBuilder().setProgress(0, 0, true).setContentText("正在等待");
        mNotificationManager.notify(mNotifyId, builder.build());
    }

    public void progressDownload(int progress) {
        final long curr = SystemClock.uptimeMillis();
        if (curr - mNextPublishTime >= NOTIFICATION_PUBLISH_INTERVAL) {
            NotificationCompat.Builder builder = getBuilder().setProgress(100, progress, false).setContentText("下载中");
            mNotificationManager.notify(mNotifyId, builder.build());
            mNextPublishTime = curr + NOTIFICATION_PUBLISH_INTERVAL;
        }


    }

    public void successDownload() {
//        NotificationCompat.Builder builder = getBuilder().setProgress(100,100,false).setContentText("下载完成");
//        mNotificationManager.notify(mNotifyId, builder.build());
        mNotificationManager.cancel(mNotifyId);
    }

    public void errorDownload() {
//        NotificationCompat.Builder builder = getBuilder().setContentText("下载失败").setProgress(100, 0, false).setAutoCancel(true);
//        mNotificationManager.notify(mNotifyId, builder.build());
        mNotificationManager.cancel(mNotifyId);
    }

    public void cancel() {
        mNotificationManager.cancel(mNotifyId);
    }

    private NotificationCompat.Builder getBuilder() {
        if (mBuilder == null) {
            mBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
            mBuilder.setTicker("正在下载").setPriority(PRIORITY_MAX)
                    .setContentTitle("新版下载")
                    .setOngoing(true)
                    .setSmallIcon(R.mipmap.system_iv)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.system_iv))
                    .setAutoCancel(false)
            ;
        }
        return mBuilder;
    }


    public static Bitmap getBitmap(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        int w = d.getIntrinsicWidth();
        int h = d.getIntrinsicHeight();

        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }


}
