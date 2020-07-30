package com.jfkj.im;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

//import com.huawei.android.hms.agent.HMSAgent;
import com.baidu.mapapi.SDKInitializer;
import com.jfkj.im.TIM.TUIKit;
import com.jfkj.im.TIM.base.IMEventListener;
import com.jfkj.im.TIM.config.CustomFaceConfig;
import com.jfkj.im.TIM.config.GeneralConfig;
import com.jfkj.im.TIM.config.TUIKitConfigs;
import com.jfkj.im.TIM.helper.CustomAVCallUIController;
import com.jfkj.im.TIM.redpack.FriendsUtils;
import com.jfkj.im.TIM.redpack.LastMessageUtils;
import com.jfkj.im.TIM.redpack.chatroom.ChatRoomController;
import com.jfkj.im.TIM.redpack.group.GroupUnreadMessageNumUtils;
import com.jfkj.im.TIM.signature.GenerateTestUserSig;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.PrivateConstants;
import com.jfkj.im.adapter.chat.EmojiManager;
import com.jfkj.im.event.GuideEvent;
import com.jfkj.im.manager.MyActivityManager;
import com.jfkj.im.receiver.OPPOPushImpl;
import com.jfkj.im.retrofit.Urls;
import com.jfkj.im.service.FloatViewService;
import com.jfkj.im.utils.BackgroupManagerUtils;
import com.jfkj.im.utils.ExecutorServiceUtils;
import com.jfkj.im.utils.MediaLoader;
import com.jfkj.im.utils.PermissionsCheckUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.kk.taurus.playerbase.config.PlayerConfig;
import com.kk.taurus.playerbase.config.PlayerLibrary;
import com.meizu.cloud.pushsdk.PushManager;
import com.meizu.cloud.pushsdk.util.MzSystemUtils;
import com.nc.player.IjkPlayerFactory;
import com.nc.player.player.VideoViewConfig;
import com.nc.player.player.VideoViewManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.session.SessionWrapper;
import com.tencent.imsdk.utils.IMFunc;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveConstants;
import com.vivo.push.PushClient;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.zhy.autolayout.config.AutoLayoutConifg;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import org.xutils.x;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import java.util.List;
import java.util.Objects;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.client.android.BuildConfig;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;

//在全局默认广场的id  为0  设置成setGroupid(0)
//时间的设置 DateTimeUtil.getTimeFormatText(new Date(msg.getMsgTime() * 1000))

////type  0礼物   1 性格测试  2 红包 （用俱乐部的，暂没用） 3冒险游戏（用俱乐部的，暂没用） 4提示（领取礼物后的） 5点赞 6评论 7官方系统通知  8群聊系统通知   9活动通知  10广场红包性格测试通知

//sendType  3  广场       1单聊  2群聊  4

/*

<!--
    俱乐部消息类型：

    一，群聊自定义类型数据：

    消息发送者用户信息参数：(必填,tipsType=3,4,5,6时,后台以管理员身份发送可不填)
    sendId(发送者id)，
    avatarUrl（信息发送者头像 url），
    sendName（信息发送者昵称），
    VIP（发送者VIP等级），
    消息类型 (必填)
    sendType：（消息类型，1单聊，2群组，这里固定为2，辅助判断）
    cusType:
    1.群红包：（类型特有字段：redId：后台接口返回的红包id，redDesc:红包祝福语）
    2.冒险游戏红包 （类型特有字段：redId：后台接口返回的红包id，redDesc:红包祝福语）

    3.冒险游戏提交的任务 （类型特有字段：redId：后台接口返回的红包id，redDesc：游戏任务内容，taskUrl:视频地址链接，taskImage:视频封面图片链接），提交前先checkAdventureTask检测

    4.冒险游戏替代任务红包 （由后台发送，类型特有字段：redId：后台接口返回的红包id，redDesc:红包祝福语）
    5.群聊每日红包 ------由后台定时发送（类型特有字段：redId：后台接口返回的红包id，redDesc:红包祝福语）
    6.系统半透明tips----由后台发送 (类型特有字段：tipsType：类型， text：需要显示的文本，receiveId：能看到该tips的用户ID，为空时表示全部能看)

    （tipsType类型：

    1.俱乐部红包被领取时：（后台以红包领取人身份发送，用户信息参数必填，仅receiveId里的用户（红包领取人和红包发送人）能看到。这里的显示内容后台无需提供。
    类型特有字段：redSendId:红包发送人id。
    redIsDone:红包是否被领完了
    根据redSendId ，redIsDone，sendId与当前用户id来判断显示，额外判断redSendId是不是自己领自己的红包。
    redIsDone = 0：
    红包领取人显示：你领取了xxx的红包。
    红包发送人显示：xxx领取了你的俱乐部红包。
    redIsDone = 1：
    红包领取人显示：你领取了xxx的红包。
    红包发送人显示：xxx领取了你的俱乐部红包，你的红包已被领完。

    2.领取冒险游戏红包参与提示： (后台以领取人身份发送，用户信息参数必填，仅receiveId里的用户（红包领取人和红包发送人）能看到。类型特有字段：redSendId:红包发送人id。根据redSendId ，sendId和当前用户id来判断显示，红包领取人：你参与了xxx的冒险。红包发送人：xxx参与了你的冒险游戏。这里的内容后台无需提供。）

    3.冒险游戏红包领取完成需做任务，（后台以管理员身份发送,无需用户信息参数，所有人都能看，前端显示text的内容，类型特有字段：gameUserId:需完成任务人的id。额外操作：与当前用户id匹配，调接口开始倒计时。进入群组聊天也调一次，有就倒计时，没有就不显示。）

    4.冒险游戏任务未按时完成，所有人都来鄙视Ta（后台以管理员身份发送，无需用户信息参数，所有人都能看，前端显示text的内容）

    5.俱乐部聊天冻结，（后台以管理员身份发送，无需用户信息参数。所有人都能看，前端显示text的内容）

    6.新用户加入俱乐部涉黄暴力提示（1.后台以管理员身份拉入群聊时，发送一条,无需用户信息参数。2.用户申请入群通过后，发送一条，无需用户信息参数。仅receiveId里的用户可看到,前端显示text的内容)）

    ***********类型特有字段：表示该类型时，字段必传。

    ********根据不同cusType，自定义消息添加不同参数，iOS和安卓，后台需统一。******


    二，群聊无需自定义，SDK消息类型（***需额外添加自定义字段VIP等级）
    1.文字
    2.语音
    3.图片
    4.短视频
 -->


 */
public class App extends Application {
    private static App INSTANCE = null;
    //现在  礼物的发送类型和文件的发送类型是一样的
    private static final String TAG = Application.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        //推送注册
      //  pushinit();

        EventBus.getDefault().register(this);



    }


    public void initBugly(){
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setAppChannel(getChannelName());  //设置渠道
        strategy.setAppVersion(Utils.getVersionCode() + "");      //App的版本
        strategy.setAppPackageName("com.jfkj.vip");  //App的包名


        CrashReport.initCrashReport(getApplicationContext(), Urls.BuglyAppID, false);
    }

    /**
     * 获取渠道名
     * @return 如果没有获取成功，那么返回值为空
     */
    public  String getChannelName() {

        String channelName = null;
        try {
            PackageManager packageManager = this.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.
                        getApplicationInfo( this.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = String.valueOf(applicationInfo.metaData.get("UMENG_CHANNEL"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channelName;
    }



    @Subscribe
    public void init(GuideEvent success) {
        if (success.isIs()){
            AutoLayoutConifg.getInstance().useDeviceSize();//屏幕适配
            AutoSizeConfig.getInstance().setCustomFragment(true);
            AutoSize.initCompatMultiProcess(this);
            INSTANCE = this;//获取全局上下文
            SPUtils.getInstance(this);
            ToastUtils.init(false, this);
            MultiDex.install(this);
            JPushInterface.setDebugMode(BuildConfig.DEBUG);
            JPushInterface.init(this);
            x.Ext.init(this);
            ExecutorServiceUtils.getInstance().init(this);
            EmojiManager.init(this);//初始化

            VideoViewManager.setConfig(VideoViewConfig.newBuilder()
                    .setPlayerFactory(IjkPlayerFactory.create())
                    .build());

            //如果您想使用默认的网络状态事件生产者，请添加此行配置。
            //并需要添加权限 android.permission.ACCESS_NETWORK_STATE
            PlayerConfig.setUseDefaultNetworkEventProducer(true);
            //初始化库
            PlayerLibrary.init(this);

            CustomAVCallUIController.getInstance().onCreate();
            IMEventListener imEventListener = new IMEventListener() {
                @Override
                public void onNewMessages(List<TIMMessage> msgs) {

                    GroupUnreadMessageNumUtils.getConversationNumbers();
                    CustomAVCallUIController.getInstance().onNewMessage(msgs);
                    CustomAVCallUIController.getInstance().onNewMessage(msgs,getAppContext());
                }
            };
            TUIKit.addIMEventListener(imEventListener);

            TUIKitConfigs configs = TUIKit.getConfigs();
            configs.setSdkConfig(new TIMSdkConfig(GenerateTestUserSig.SDKAPPID));
            configs.setCustomFaceConfig(new CustomFaceConfig());
            configs.setGeneralConfig(new GeneralConfig());

            TUIKit.init(this, GenerateTestUserSig.SDKAPPID, configs);


            Album.initialize(AlbumConfig.newBuilder(this).setAlbumLoader(new MediaLoader()).build());

            // 三个参数分别是上下文、应用的appId、是否检查签名（默认为false）
            IWXAPI mWxApi = WXAPIFactory.createWXAPI(this, PrivateConstants.WeChatAppID, true);
// 注册
            mWxApi.registerApp(PrivateConstants.WeChatAppID);




            SDKInitializer.initialize(this);


            registerOnclick();

//            EventBus.getDefault().post("initSuccess");
//            EventBus.getDefault().unregister(this);

            initBugly();
            LitePal.initialize(this);
        }
    }







    private void registerOnclick() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                MyActivityManager.getInstance().setCurrentActivity((AppCompatActivity) activity);
//                if (!BackgroupManagerUtils.isServiceRunning(getApplicationContext(),"com.jfkj.im.service.FloatViewService")) {
//                    if (PermissionsCheckUtils.checkFloatPermission(INSTANCE)) {
//                        getLastMessage();
//                    }
//                }
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

//    private void getLastMessage() {
//        LastMessageUtils.getLastMessage(TIMConversationType.Group, SPUtils.getInstance(App.getAppContext()).getString(Utils.AV_CHAT_ROOM_ID), new TIMValueCallBack<List<TIMMessage>>() {
//            @Override
//            public void onError(int code, String desc) {
//            }
//
//            @Override
//            public void onSuccess(List<TIMMessage> timMessages) {
//                if (timMessages.size() == 0) {
//                    getFwLastMessage();
//                } else {
//                    String[] strings = ChatRoomController.onNewMessage(timMessages, INSTANCE);
//                    if (strings.length > 0) {
//                        FriendsUtils.getUsersProfileLoaclService(strings[0], new TIMValueCallBack<TIMUserProfile>() {
//                            @Override
//                            public void onError(int code, String desc) {
//                            }
//
//                            @Override
//                            public void onSuccess(TIMUserProfile timUserProfile) {
//                                if (timUserProfile != null && !TextUtils.isEmpty(timUserProfile.getFaceUrl())) {
//                                    if (PermissionsCheckUtils.checkFloatPermission(INSTANCE)) {
//                                        Intent intent = new Intent(INSTANCE, FloatViewService.class);
//                                        intent.putExtra("img", timUserProfile.getFaceUrl());
//                                        intent.putExtra("text", strings[1]);
//                                        if (TextUtils.isEmpty(SPUtils.getInstance(INSTANCE).getString(Utils.IS_NEW_MESSAGE))){
//                                            SPUtils.getInstance(INSTANCE).put(Utils.IS_NEW_MESSAGE,strings[1]);
//                                            intent.putExtra("new",false);
//                                            //启动FloatViewService
//                                        } else if (TextUtils.equals(SPUtils.getInstance(INSTANCE).getString(Utils.IS_NEW_MESSAGE),strings[1])){
//                                            intent.putExtra("new",false);
//                                            //启动FloatViewService
//                                        } else if (!TextUtils.equals(SPUtils.getInstance(INSTANCE).getString(Utils.IS_NEW_MESSAGE),strings[1])){
//                                            intent.putExtra("new",true);
//                                        } else {
//                                            intent.putExtra("new",false);
//                                        }
//                                        Objects.requireNonNull(INSTANCE).startService(intent);
//                                    }
//                                }
//                            }
//                        });
//                    }
//                }
//            }
//        });
//    }
//
//    private void getFwLastMessage() {
//        LastMessageUtils.getLocalLastMessage(TIMConversationType.Group, SPUtils.getInstance(App.getAppContext()).getString(Utils.AV_CHAT_ROOM_ID), new TIMValueCallBack<List<TIMMessage>>() {
//            @Override
//            public void onError(int code, String desc) {
//            }
//
//            @Override
//            public void onSuccess(List<TIMMessage> timMessages) {
//                if (timMessages != null && timMessages.size() > 0) {
//                    String[] strings = ChatRoomController.onNewMessage(timMessages, INSTANCE);
//                    if (strings.length > 0) {
//                        TIMUserProfile profile = FriendsUtils.queryUsersProfileUser(strings[0]);
//                        if (profile != null && !TextUtils.isEmpty(profile.getFaceUrl())) {
//                            if (PermissionsCheckUtils.checkFloatPermission(INSTANCE)) {
//                                Intent intent = new Intent(INSTANCE, FloatViewService.class);
//                                intent.putExtra("img", profile.getFaceUrl());
//                                intent.putExtra("text", strings[1]);
//                                intent.putExtra("new",false);
//                                //启动FloatViewService
//                                Objects.requireNonNull(INSTANCE).startService(intent);
//                            }
//                        }
//                    }
//                }
//            }
//        });
//    }



    private void pushinit() {
        // 判断是否是在主线程
        if (SessionWrapper.isMainProcess(getApplicationContext())) {
            /**
             * TUIKit 的初始化函数
             *
             * @param context  应用的上下文，一般为对应应用的 ApplicationContext
             * @param sdkAppID 您在腾讯云注册应用时分配的 SDKAppID
             * @param configs  TUIKit 的相关配置项，一般使用默认即可，需特殊配置参考 API 文档
             */
            long current = System.currentTimeMillis();
//            TUIKit.init(this, "Constants.SDKAPPID", BaseUIKitConfigs.getDefaultConfigs());
            System.out.println(">>>>>>>>>>>>>>>>>>"+(System.currentTimeMillis()-current));
            // 添加自定初始化配置
//            customConfig();
            System.out.println(">>>>>>>>>>>>>>>>>>"+(System.currentTimeMillis()-current));

            if(IMFunc.isBrandXiaoMi()){
                // 小米离线推送
                MiPushClient.registerPush(this, Constants.XM_PUSH_APPID, Constants.XM_PUSH_APPKEY);
            }
            if(IMFunc.isBrandHuawei()){
                // 华为离线推送
//                boolean init = HMSAgent.init(this);
//                OkLogger.e(init + "");
            }
            if(MzSystemUtils.isBrandMeizu(this)){
                // 魅族离线推送
                PushManager.register(this, Constants.MZ_PUSH_APPID, Constants.MZ_PUSH_APPKEY);
            }
            if(IMFunc.isBrandVivo()){
                // vivo 离线推送
                PushClient.getInstance(getApplicationContext()).initialize();
            }

              if (IMFunc.isBrandOppo()) {

                PushClient.getInstance(getApplicationContext()).initialize();
                com.heytap.mcssdk.PushManager.getInstance().register(this,Constants.OPPO_PUSH_APPKEY, Constants.OPPO_PUSH_APPSECRET,new OPPOPushImpl());

             //   createNotificationChannel(this);
            }
        }



    }





    /**
     * 按照 OPPO 官网要求，在 OPPO Android 8.0 及以上系统版本必须配置 ChannelID，否则推送消息无法展示。您需要先在 App 中创建对应的 ChannelID（例如 tuikit）
     * @param
     */
//    public void createNotificationChannel(Context context) {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "oppotest";
//            String description = "this is opptest";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//
//
//            NotificationChannel channel = new NotificationChannel(GenerateTestUserSig.SDKAPPID + "", name, importance);
//            channel.setDescription(description);
//            String id = channel.getId();
//            OkLogger.e("ChannelID = " + id);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }




    public static Context getAppContext() {
        return INSTANCE;
    }
}
