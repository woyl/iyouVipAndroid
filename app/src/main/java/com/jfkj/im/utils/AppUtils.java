package com.jfkj.im.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.data.UserInfoManger;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/4
 * </pre>
 */
public class AppUtils {

    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getReqTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }


    public static String getChannel(Context context){
//        try {
//            PackageManager packageManager = context.getPackageManager();
//            if (packageManager != null) {
//                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
//                    if (applicationInfo.metaData != null) {
//                        String channel = applicationInfo.metaData.get("UMENG_CHANNEL");
//                        AppConfig.CHANNEL = channel;
//                        LogService.i("Channel","当前的渠道为:"+channel );
//                        return channel;
//                    }
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    /**
     * 获取手机品牌
     *
     * @return 返回手机品牌
     */
    public static String getBRAND() {
        return Build.BRAND;
    }

    @SuppressLint("HardwareIds")
    public static String getIMEI(Context context) {
        try {
            String imei = null;
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                imei = "empty";
                return imei;
            }
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    imei = telephonyManager.getImei();
                } else {
                    imei = telephonyManager.getDeviceId();
                }
            }
            return TextUtils.isEmpty(imei) ? "empty" : imei;
        }catch (Exception e){
            return "android";
        }

    }

    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取图标 bitmap
     * @param context
     */
    public static synchronized Bitmap getBitmap(Context context) {
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
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }


    private static void install(Activity context,File file){
        //安装apk

            //    File outputFile = new File(FileUtil.APK_DIR, FileUtil.APK_NAME);
             //   Log.d("@@@", "outputFile:+ " + file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(getFileUri(context, file),
                        "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
    }

    public static void installApk(Context context, Activity activity,File file) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean b = context.getPackageManager().canRequestPackageInstalls();
            if(b){
                install(activity,file);
            }else{
                Uri packageURI = Uri.parse("package:"+activity.getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);

                activity.startActivityForResult(intent, 99);
            }
        }else{
            install(activity,file);
        }


        //        PermissionsUtil.requestPermission(context, new PermissionListener() {
//            @Override
//            public void permissionGranted(@NonNull String[] permissions) {
//                //安装apk
//                File outputFile = new File(FileUtil.APK_DIR, FileUtil.APK_NAME);
//                Log.d("@@@", "outputFile:+ " + outputFile);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(getFileUri(context, outputFile),
//                        "application/vnd.android.package-archive");
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
//            @Override
//            public void permissionDenied(@NonNull String[] permissions) {
//                ToastUtil.toastShortMessage("没有安装权限");
//                Uri packageURI = Uri.parse("package:"+activity.getPackageName());
//                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,packageURI);
//                activity.startActivityForResult(intent, 99);
//            }
//        }, Manifest.permission.REQUEST_INSTALL_PACKAGES);

    }

    public static Intent getInstallAppIntent(Context context, File file) {
        if (file == null)
            return null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type;

        type = "application/vnd.android.package-archive";
//        if (Build.VERSION.SDK_INT < 23) {
//        } else {
//            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(getFileExtension(file));
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri contentUri = FileProvider.getUriForFile(context, "com.your.package.fileProvider", file);
//            intent.setDataAndType(contentUri, type);
//        }
        intent.setDataAndType(Uri.fromFile(file), type);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }




    public static Uri getFileUri(Context context, File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = getNFileUri(context, file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    /**
     * android7.0 获取文件Uri
     *
     * @param context
     * @param file
     * @return
     */
    private static Uri getNFileUri(Context context, File file) {
        Uri fileUri = FileProvider.getUriForFile(context,
                context.getPackageName() + ".fileProvider", file);
        return fileUri;
    }


}
