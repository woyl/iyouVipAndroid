package com.jfkj.im.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.jfkj.im.App;
import com.jfkj.im.ui.activity.Loginregister_phone_Activity;
import com.jfkj.im.ui.activity.MainActivity;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;

import cn.jpush.android.api.JPushInterface;

public class JGBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String title2 = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String fileHtml = bundle.getString(JPushInterface.EXTRA_RICHPUSH_HTML_PATH);
        Log.e("onReceive", "--------------------------------------------------");
        Log.e("onReceive", "title : " + title);
        Log.e("onReceive", "title2 : " + title2);
        Log.e("onReceive", "message: " + message);
        Log.e("onReceive", "content: 测试有值 " + content);
        Log.e("onReceive", "extras: " + extras);
        Log.e("onReceive", "fileHtml: " + fileHtml);
    //    if(Utils.CHANNEL.())
        Intent againconnectintent = new Intent("againconnect");
        App.getAppContext().sendBroadcast(againconnectintent);
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
        //    Log.d(" >", "[MyReceiver] 接收 Registration Id : " + regId);
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
          //  Log.d(" >", "收到了自定义消息。消息内容是：" +      bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction()))     {
            //Log.d(" >", "收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
           // Log.d(" >", "用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
          if(SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN)!=null){
                if(SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN).trim().length()>0){
                    Intent i = new Intent(context, MainActivity.class);  //自定义打开的界面
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }else {
                    Intent i = new Intent(context, Loginregister_phone_Activity.class);  //自定义打开的界面
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
          }else {
              Intent i = new Intent(context, Loginregister_phone_Activity.class);  //自定义打开的界面
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              context.startActivity(i);
          }

        }
    }

}