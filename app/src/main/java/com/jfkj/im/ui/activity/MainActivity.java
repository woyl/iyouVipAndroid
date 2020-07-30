package com.jfkj.im.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.jfkj.im.App;
import com.jfkj.im.Bean.AppVersion;
import com.jfkj.im.Bean.GiftchatBean;
import com.jfkj.im.Bean.InteractionBean;
import com.jfkj.im.Bean.TodayStarBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.TUIKit;
import com.jfkj.im.TIM.base.IMEventListener;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.bean.ConversationMessage;
import com.jfkj.im.TIM.modules.chat.GroupChatManagerKit;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.redpack.LastMessageUtils;
import com.jfkj.im.TIM.redpack.chatroom.ChatRoomUtil;
import com.jfkj.im.TIM.signature.GenerateTestUserSig;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.TIM.zhf.ConversationManagerKit;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.AddMoneyBean;
import com.jfkj.im.entity.EventMessage;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.event.GroupUnreadMessageNumEvent;
import com.jfkj.im.event.ShowVipUpgradeDialogFragmentEvent;
import com.jfkj.im.event.UpChatRoomEvent;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.Home.HomePresenter;
import com.jfkj.im.mvp.Home.HomeView;
import com.jfkj.im.receiver.OPPOPushImpl;
import com.jfkj.im.receiver.ThirdPushTokenMgr;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.Urls;
import com.jfkj.im.service.FloatViewService;
import com.jfkj.im.ui.dialog.AppUpdateDialog;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.ui.dialog.Dialog_gift;
import com.jfkj.im.ui.dialog.ExitDialog;
import com.jfkj.im.ui.dialog.LoginOutDialog;
import com.jfkj.im.ui.dialog.NoticeDialog;
import com.jfkj.im.ui.dialog.ReceiveDialog;
import com.jfkj.im.ui.dialog.TipsBaseDialogFragment;
import com.jfkj.im.ui.dialog.UpgradeDialog;
import com.jfkj.im.ui.dialog.VipUpgradeOneDialogFragment;
import com.jfkj.im.ui.dialog.VipUpgradeTwoDialogFragment;
import com.jfkj.im.ui.discovery.DiscoveryFragment;
import com.jfkj.im.ui.fragment.ClubFragment;
import com.jfkj.im.ui.fragment.InteractionFragment;
import com.jfkj.im.ui.home.LazyHomeFragment;
import com.jfkj.im.ui.mine.fragment.NewMineFragment;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.StatusBarUtil;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.floatview.view.FloatViewImpl;
import com.jfkj.im.view.floatview.widget.FloatViewLayout;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;
import com.meizu.cloud.pushsdk.PushManager;
import com.meizu.cloud.pushsdk.util.MzSystemUtils;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriendPendencyItem;
import com.tencent.imsdk.friendship.TIMFriendPendencyRequest;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;
import com.tencent.imsdk.session.SessionWrapper;
import com.tencent.imsdk.utils.IMFunc;
import com.vivo.push.PushClient;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.android.cache.Sp;
import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.namee.permissiongen.PermissionGen;
import okhttp3.ResponseBody;
import retrofit2.http.Url;

import static com.jfkj.im.utils.Utils.REFRUSH_USER_BALANCE;
import static com.tencent.imsdk.friendship.TIMPendencyType.TIM_PENDENCY_BOTH;
import static com.tencent.imsdk.friendship.TIMPendencyType.TIM_PENDENCY_COME_IN;

//首页主要是处理离线消息之类的
public class MainActivity extends BaseActivity<HomePresenter> implements HomeView, View.OnClickListener, ConversationManagerKit.MessageUnreadWatcher {

    @BindView(R.id.rl_home)
    RelativeLayout rlHome;

    ExitDialog homeexitDialog;

    @BindView(R.id.btn_home)
    ImageView btn_home;
    @BindView(R.id.btn_interaction)
    ImageView btn_interaction;
    @BindView(R.id.btn_club)
    ImageView btn_club;
    @BindView(R.id.btn_find)
    ImageView btn_find;
    @BindView(R.id.btn_mine)
    ImageView btn_mine;
    @BindView(R.id.tv_interaction_number)
    TextView tv_interaction_number;
    @BindView(R.id.tv_group_number)
    TextView tv_group_number;
    @BindView(R.id.tv_find)
    TextView tv_find;
    Intent intent;
    Dialog_gift popupwindow_gift;
    String sort = "";
    HomePresenter homePresenter;
    private ArrayList<Fragment> fragments;
    private int position = 0;
    private Fragment tempFragemnt;
    List<InteractionBean> recomdmessage = new ArrayList< >();
    public static String latitude;
    public static String longitude;
    ReceiveDialog receivedialog;
    AuthenticationDialog authenticationDialog;
    private boolean isCheckHome = true;
    private LazyHomeFragment homeFragment;
    private UpgradeDialog dialog;
    private AppUpdateDialog mAppUpdateDialog = null;
    int totalcount = 0;
    String av_chat_room_id;
    private NoticeDialog noticeDialog;
    private Context context;


    //
    @BindView(R.id.layout_float)
    FloatViewLayout  layout_float;
    private Runnable runnable;
    private Handler handler;
    private TextView tv_message, tv_message_1;
    private CircleImageView head_iv, head_iv_1;
    private ViewFlipper notice_content;
    private View view;

    //权限申请
    private static final String[] permissions = {Manifest.permission.SYSTEM_ALERT_WINDOW};
    private static final int PERMISSION_SYSTEM_ALERT_WINDOW = 128;
    private String PERMISSION_TIPS = "为保证软件正常使用请开启悬浮窗权限";
    private boolean isFrist = false;

    private LocationClient locationClient;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homePresenter = new HomePresenter(this);

        homePresenter.getGiftList();
        homePresenter.getGif();
        context = this;

        init();
        homePresenter.selectAdvertisement();
        homePresenter.getRanking("1");
        homePresenter.getRanking("2");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("sendgift");
        intentFilter.addAction("praise");
        intentFilter.addAction("dynamic");

        registerReceiver(broadcastReceiver, intentFilter);
        ConversationManagerKit.getInstance().addUnreadWatcher(this);
        GroupChatManagerKit.getInstance();

        NotificationManagerCompat notification = NotificationManagerCompat.from(this);
        boolean isEnabled = notification.areNotificationsEnabled();

        if (!isEnabled) {
            playNotice();
        }

        if (IMFunc.isBrandHuawei()) {
            getToken();
        }

        checkVersion();

        chatExit();

    }



    //兼容android8.0以及之前版本获取Notification.Builder方法
    private Notification.Builder getNotificationBuilder(){
        Notification.Builder builder = new Notification.Builder(this)
                .setAutoCancel(true)//是否自动取消，设置为true，点击通知栏 ，移除通知
                .setContentTitle("通知栏消息标题")
                .setContentText("通知栏消息具体内容")
                .setSmallIcon(R.drawable.logo)//通知栏消息小图标，不设置是不会显示通知的
                //ledARGB 表示灯光颜色、ledOnMs 亮持续时间、ledOffMs 暗的时间
                .setLights(Color.RED, 3000, 3000)
                //.setVibrate(new long[]{100,100,200})//震动的模式，第一次100ms，第二次100ms，第三次200ms
                //.setStyle(new Notification.BigTextStyle())
                ;
        //没加版本判断会报Call requires API level 26 (current min is 16):android.app.Notification.Builder#Builder）错误
        //builder.setChannelId("channel_id");
        //通过版本号判断兼容了低版本没有通知渠道方法的问题，只有当版本号大于26（Build.VERSION_CODES.O）时才使用渠道相关方法
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            //builder的channelId需和下面channel的保持一致；
            builder.setChannelId("channel_id");
            NotificationChannel channel = new
                    NotificationChannel("channel_id","channel_name",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setBypassDnd(true);//设置可以绕过请勿打扰模式
            channel.canBypassDnd();//可否绕过请勿打扰模式
            //锁屏显示通知
            channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
            channel.shouldShowLights();//是否会闪光
            channel.enableLights(true);//闪光
            //指定闪光时的灯光颜色，为了兼容低版本在上面builder上通过setLights方法设置了
            //channel.setLightColor(Color.RED);
            channel.canShowBadge();//桌面launcher消息角标
            channel.enableVibration(true);//是否允许震动
            //震动模式，第一次100ms，第二次100ms，第三次200ms，为了兼容低版本在上面builder上设置了
            //channel.setVibrationPattern(new long[]{100,100,200});
            channel.getAudioAttributes();//获取系统通知响铃声音的配置
            channel.getGroup();//获取通知渠道组
            //绑定通知渠道
            getNotificationManager().createNotificationChannel(channel);
        }
        return builder;
    };

    //获取系统服务
    private NotificationManager mNotificationManager;
    private NotificationManager getNotificationManager() {
        if (mNotificationManager == null){
            mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }



    public void showNt(Context context){
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.notify_layout);

        views.setImageViewResource(R.id.iv_notify,R.mipmap.icon_activity_brg);
        views.setTextViewText(R.id.tv_content,"谁谁谁赞了你");
        views.setTextColor(R.id.tv_content, Color.BLACK);
//        views.setProgressBar(R.id.progerssbar,100,50,false);
        Notification.Builder builder = getNotificationBuilder();
//        final Notification.Builder builder = new Notification.Builder(context);


        builder.setContent(views)
                .setTicker("通知：您有30亿要继承")
                .setContentTitle("西红柿首富")
                .setSmallIcon(R.drawable.logo)             //   .setContentText("只有在3天时间花完3亿，才可以继承30亿，加油吧骚年")
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis()); //设置点击通知后执行的动作

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("message", "只有在3天时间花完3亿，才可以继承30亿，加油吧骚年\n西红柿首富剧组通知你带薪入组\n时间:" );        //用当前时间充当通知的id，这里是为了区分不同的通知，如果是同一个id，前者就会被后者覆盖
        int requestId = (int) new Date().getTime();        //第一个参数连接上下文的context ¬
//         第二个参数是对PendingIntent的描述，请求值不同Intent就不同
//         第三个参数是一个Intent对象，包含跳转目标
//         第四个参数有4种状态
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);        //发出通知，参数是（通知栏的id，设置内容的对象）
        getNotificationManager().notify(requestId, builder.build());
    }





    /**
     * 查询是否有未读标识
     */

    private void allMyPartysRedType(){
        Map<String,String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME,Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());

        OkGo.<String>post(Urls.base_url + "/party/allMyPartysRedType")
                .tag(mActivity)
                .headers(Utils.TOKEN, SPUtils.getInstance(mActivity).getString(Utils.TOKEN))
                .headers(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                .params(map)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            String strRedType = jsonObject.getJSONObject("data").getString("redType");
                            if("1".equals(strRedType)){
                                tv_find.setVisibility(View.VISIBLE);
                                SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.NEW_POINT, true);
                            }else{
                                if (SPUtils.getInstance(mActivity).getBoolean(Utils.APPID + Utils.ISDYNAMIC, false) || !SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.MESSAGE_NUMBER, "0").equals("0")){
                                    tv_find.setVisibility(View.VISIBLE);
                                } else {
                                    tv_find.setVisibility(View.GONE);
                                }
                                SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.NEW_POINT, false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    /**
     * 广场入口
     */
    private void chatExit() {
        layout_float.setmFloatView(new FloatViewImpl() {
            @Override
            public View createFloatView() {
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.window_filer, null);
                //滚动
                View view1 = LayoutInflater.from(mActivity).inflate(R.layout.layout_notice, null);
                View view2 = LayoutInflater.from(mActivity).inflate(R.layout.layout_notice, null);
                tv_message = view1.findViewById(R.id.tv_message);
                head_iv = view1.findViewById(R.id.head_iv);
                tv_message_1 = view2.findViewById(R.id.tv_message);
                head_iv_1 = view2.findViewById(R.id.head_iv);
                notice_content  = view.findViewById(R.id.notice_content);
                notice_content.addView(view1);
                notice_content.addView(view2);
                return view;
            }

            @Override
            public int setFloatViewSideOffset() {
                return super.setFloatViewSideOffset();
            }

            @Override
            public boolean setEnableBackground() {
                return false;
            }
        });
        addWindowBt();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkLogger.e("AV CHAT ROOM ID --------------->" + SPUtils.getInstance(App.getAppContext()).getString(Utils.AV_CHAT_ROOM_ID));
                sendMessage(SPUtils.getInstance(App.getAppContext()).getString(Utils.AV_CHAT_ROOM_ID));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upDateChatRoom(UpChatRoomEvent event) {
        if (event.isUpDateMessage()) {
            getLastMessage();
        }
    }

    private void addWindowBt() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                getLastMessage();
            }
        };
        handler.postDelayed(runnable, 500);
    }

    private void getLastMessage() {
        String chatRoomId = SPUtils.getInstance(App.getAppContext()).getString(Utils.AV_CHAT_ROOM_ID);
        if (TextUtils.isEmpty(chatRoomId)){
            chatRoomId = Urls.square_chat;
        }
        if (TextUtils.equals("null",chatRoomId)){
            chatRoomId = Urls.square_chat;
        }
        LastMessageUtils.getLastMessage(TIMConversationType.Group, chatRoomId, new TIMValueCallBack<List<String>>() {
            @Override
            public void onError(int code, String desc) {
                OkLogger.e(desc);
            }

            @Override
            public void onSuccess(List<String> timMessages) {


                Glide.with(mActivity).load(timMessages.get(1)).error(R.drawable.logo).into(head_iv);
                Glide.with(mActivity).load(timMessages.get(1)).error(R.drawable.logo).into(head_iv_1);


                OkLogger.e("extra",timMessages.get(0) + " \t\t\t----------- " + timMessages.get(1));
                SPUtils.getInstance(App.getAppContext()).put(Urls.square_chat + "extra",timMessages.get(0));
                SPUtils.getInstance(App.getAppContext()).put(Urls.square_chat + "head" ,timMessages.get(1));

                tv_message.setText(timMessages.get(0));
                tv_message_1.setText(timMessages.get(0));

                if (SPUtils.getInstance(mActivity).getBoolean(Utils.IS_NEW_MESSAGE_ROLL)){
                    notice_content.startFlipping();
                    notice_content.stopFlipping();
                } else {
                    notice_content.stopFlipping();
                }
            }
        },mActivity);
    }

    private void sendMessage(String groupId) {

        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(TIMConversationType.Group);
        chatInfo.setId(groupId);
        chatInfo.setChatName("广场");
        chatInfo.setChatRoom(true);

        ConversationMessage conversationMessage = new ConversationMessage();
        conversationMessage.setConversationId(groupId);
        conversationMessage.setGroup(true);
        conversationMessage.setId(groupId);
        conversationMessage.setTitle("广场");

        Intent intent = new Intent(mActivity, ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.putExtra(Utils.CONVERSATIONMESSAGE, conversationMessage);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void requestPermissionShow() {
        TipsBaseDialogFragment tipsBaseDialogFragment
                = new TipsBaseDialogFragment(true, Gravity.CENTER, PERMISSION_TIPS, "取消", "确定", true);
        tipsBaseDialogFragment.setResponListener(new ResponListener<Boolean>() {
            @Override
            public void Rsp(Boolean s) {
                if (s) {
//                        requestPermission_zi();
                    requestPermission(context);
                } else {
                    TipsBaseDialogFragment tipsBaseDialogFragment
                            = new TipsBaseDialogFragment(true, Gravity.CENTER, "确认拒绝悬浮窗权限将不能显示广场入口", "取消", "确定", true);
                    tipsBaseDialogFragment.setResponListener(new ResponListener<Boolean>() {
                        @Override
                        public void Rsp(Boolean s) {
                            if (!s) {
//                                    requestPermission_zi();
                                requestPermission(context);
                            }
                        }
                    });
                    tipsBaseDialogFragment.show(getSupportFragmentManager(), "");
                }
            }
        });
        tipsBaseDialogFragment.show(getSupportFragmentManager(), "");
    }

    private void requestPermission_zi() {
//        if (Build.VERSION.SDK_INT < 19) {
//            // 不用跳转
//        } else if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 23) {
//            if (RomUtils.checkIsMiuiRom() || RomUtils.checkIsMeizuRom() || RomUtils.checkIsHuaweiRom() || RomUtils.checkIs360Rom()) {
//                // 分机型跳转
//            } else {
//                // 不用跳转
//            }
//        } else if (Build.VERSION.SDK_INT >= 23) {
//            // 通用跳转
//        }
        highVersionJump2PermissionActivity(context);
    }

    /**
     * 跳转应用详情界面
     *
     * @param context
     */
    private void jump2DetailActivity(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }

    /**
     * 请求悬浮窗权限
     *
     * @param context 上下文
     */
    private void requestPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 23) {
            jump2DetailActivity(context);
        } else if (Build.VERSION.SDK_INT >= 23) {
            highVersionJump2PermissionActivity(context);
        }
    }

    /**
     * Android 6.0 版本及之后的跳转权限申请界面
     *
     * @param context 上下文
     */
    private void highVersionJump2PermissionActivity(Context context) {
        try {
            Class clazz = Settings.class;
            Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
            Intent intent = new Intent(field.get(null).toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            Log.e("TAG", Log.getStackTraceString(e));
        }
    }


//
//    private void getMessagae() {
//        LastMessageUtils.getFwLastMessage(TIMConversationType.Group, SPUtils.getInstance(App.getAppContext()).getString(Utils.AV_CHAT_ROOM_ID), new TIMValueCallBack<List<TIMMessage>>() {
//            @Override
//            public void onError(int code, String desc) {
//            }
//
//            @Override
//            public void onSuccess(List<TIMMessage> timMessages) {
//                if (timMessages == null) {
//                    getFwLastMessage();
//                } else {
//                    String[] strings = ChatRoomController.onNewMessage(timMessages, getApplicationContext());
//                    if (strings.length > 0) {
//                        FriendsUtils.getUsersProfileLoaclService(strings[0], new TIMValueCallBack<TIMUserProfile>() {
//                            @Override
//                            public void onError(int code, String desc) {
//                            }
//
//                            @Override
//                            public void onSuccess(TIMUserProfile timUserProfile) {
//                                if (timUserProfile != null && !TextUtils.isEmpty(timUserProfile.getFaceUrl())) {
//
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
//                    String[] strings = ChatRoomController.onNewMessage(timMessages, getApplicationContext());
//                    if (strings.length > 0) {
//                        TIMUserProfile profile = FriendsUtils.queryUsersProfileUser(strings[0]);
//                        if (profile != null && !TextUtils.isEmpty(profile.getFaceUrl())) {
//                            Intent intent = new Intent(MainActivity.this, FloatViewService.class);
//                            intent.putExtra("img", profile.getFaceUrl());
//                            intent.putExtra("text", strings[1]);
//                            intent.putExtra("new",false);
//                            //启动FloatViewService
//                            startService(intent);
//                        }
//                    }
//                }
//            }
//        });
//    }

    /**
     * 获取token
     */
    private void getToken() {
        OkLogger.e("进入token");
        // get token
        new Thread() {
            @Override
            public void run() {
                try {
                    String appId = AGConnectServicesConfig.fromContext(MainActivity.this).getString("client/app_id");
                    String pushtoken = HmsInstanceId.getInstance(MainActivity.this).getToken(appId, "HCM");
                    OkLogger.e("pushtoken ======" + pushtoken);
                    if (!TextUtils.isEmpty(pushtoken)) {
                        ThirdPushTokenMgr.getInstance().setThirdPushToken(pushtoken);
                        ThirdPushTokenMgr.getInstance().setPushTokenToTIM();
                    }
                } catch (Exception e) {
                    OkLogger.e("======" + e.toString());
                }
            }
        }.start();
    }


    public void playNotice() {
        noticeDialog = new NoticeDialog(this, new CommonDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_submit:
                        noticeDialog.cancel();
                        Intent intent = new Intent();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                            intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0
                            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                            intent.putExtra("app_package", context.getPackageName());
                            intent.putExtra("app_uid", context.getApplicationInfo().uid);
                            startActivity(intent);
                        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {  //4.4
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + context.getPackageName()));
                        } else if (Build.VERSION.SDK_INT >= 15) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                        }
                        startActivity(intent);
                        break;

                    case R.id.tv_cancel:
                        noticeDialog.cancel();

                        break;
                }

            }
        });

        noticeDialog.show();
    }

    @Override
    protected void onStop() {
//        if (BackgroupManagerUtils.isBackground(mActivity)) {
//            destroyServise();
//        }
        super.onStop();
    }


    /**
     * 检测版本更新
     */
    private void checkVersion() {
        homePresenter.getVersionAndroid(MainActivity.this);
    }

    private void showExitDialog() {
        LoginOutDialog exitDialog = new LoginOutDialog(this, R.style.dialogstyle, new CommonDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_submit:
                        homePresenter.loginOut();
                        break;
                }
            }
        });
        if (!MainActivity.this.isFinishing())//xActivity即为本界面的Activity
        {
            exitDialog.show();
        }
    }


    // 普通事件的处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshUserInfo(EventMessage message) {
        if (message.getType() == REFRUSH_USER_BALANCE) {
            //充值完成 刷新个人账户
            //刷新VIP 等级
            homePresenter.getUserDetail(SPUtils.getInstance(mActivity).getString(Utils.USERID));
            //刷新账户余额
            homePresenter.accountBalance();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void showDialog(ShowVipUpgradeDialogFragmentEvent dialogFragmentEvent) {
        if (dialogFragmentEvent != null) {
            if (!TextUtils.isEmpty(dialogFragmentEvent.getContent())) {
                String old = dialogFragmentEvent.getContent();
                String content = old.substring(12);
                String content_1 = old.substring(old.indexOf("P"), old.length());
                Log.i("msg", "..........content............." + content);
                Log.i("msg", "..........content_1............." + content_1);
                String vip = "恭喜升级至VIP" + content;
                String bt = "";
                if (TextUtils.equals(content, "3")) {
                    bt = "创建俱乐部功能已解锁";
                } else if (TextUtils.equals(content, "6")) {
                    bt = "自定义冒险游戏已解锁";
                } else if (TextUtils.equals(content, "14")) {
                    bt = "视频聊天功能已解锁";
                } else if (TextUtils.equals(content, "20")) {
                    bt = "隐藏在线状态功能已解锁";
                } else if (TextUtils.equals(content, "50")) {
                    bt = "隐藏地理位置功能已解锁";
                } else if (TextUtils.equals(content, "100")) {
                    bt = "超级俱乐部功能已解锁";
                }
                if (TextUtils.equals(content, "3") || TextUtils.equals(content, "6") || TextUtils.equals(content, "14") || TextUtils.equals(content, "20") || TextUtils.equals(content, "50") || TextUtils.equals(content, "100")) {
                    VipUpgradeTwoDialogFragment vipUpgradeTwoDialogFragment = new VipUpgradeTwoDialogFragment(true, Gravity.CENTER, vip, bt);
                    vipUpgradeTwoDialogFragment.show(getSupportFragmentManager(), "");
                } else {
                    VipUpgradeOneDialogFragment vipUpgradeOneDialogFragment = new VipUpgradeOneDialogFragment(true, Gravity.CENTER, vip);
                    vipUpgradeOneDialogFragment.show(getSupportFragmentManager(), "");
                }

            }
        }
    }


    // 监听做成静态可以让每个子类重写时都注册相同的一份。
    private IMEventListener mIMEventListener = new IMEventListener() {
        @Override
        public void onForceOffline() {
            //dialog
            showExitDialog();
//            ToastUtil.toastLongMessage("您的帐号已在其它终端登录");
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public HomePresenter createPresenter() {
        return homePresenter;
    }

    public void init() {
        receivedialog = new ReceiveDialog(this);
        authenticationDialog = new AuthenticationDialog(this);
        authenticationDialog.setActivity(this);

//        btn_home.setOnClickListener(this);
//        btn_interaction.setOnClickListener(this);
//        btn_club.setOnClickListener(this);
//        btn_find.setOnClickListener(this);
//        btn_mine.setOnClickListener(this);

        Utils.APPID = SPUtils.getInstance(mActivity).getString(Utils.USERID);
        //   homePresenter.getUserDetail("76751335294238720");
        intent = getIntent();
        SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.ADDFRIENDNUMBER, 0);

        homeexitDialog = new ExitDialog(this);
        fragments = new ArrayList<>();
        homeFragment = LazyHomeFragment.getInstance(null, null);
        fragments.add(homeFragment);
        fragments.add(InteractionFragment.getInstance());
        fragments.add(ClubFragment.getInstance());
        fragments.add(DiscoveryFragment.getInstance());
        fragments.add(NewMineFragment.getInstance());
        initListener();
        checkLocation();

        Utils.ROOMID = SPUtils.getInstance(mActivity).getInt(Utils.SEQROOM, 0);
        Utils.CIRCLEROOMID = SPUtils.getInstance(mActivity).getString(Utils.CIRCLEID);
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, Utils.APPID));//  76751335294238720 783313

        String AVCHATROOMID = SPUtils.getInstance(mActivity).getString(Utils.AVCHATROOMID);

        logintim();
    }

    public void logintim() {
        TUIKit.login(SPUtils.getInstance(mActivity).getString(Utils.USERID), SPUtils.getInstance(mActivity).getString(Utils.USER_SIG), new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                SPUtils.getInstance(mActivity).put(Constants.AUTO_LOGIN, true);
                ChatRoomUtil.applyJoinGroup();
                pushinit();
                homePresenter.getUserDetail(SPUtils.getInstance(mActivity).getString(Utils.USERID));
                homePresenter.pickFemaleAdd();
                if (SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.UpSex) != null) {
                    if (SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.UpSex).length() > 0) {
                        String json = SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.UpSex);

                        List<String> stringList = JSON.parseArray(json, String.class);
                        // homePresenter.pickFemaleAdd();
                    }
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                switch (errCode) {
                    case 6014:
                        toastShow("请重新登录");
                        break;
                    case 6026:
                        toastShow("请重新登录，登录错误");
                        break;
                    case 6206:
                        toastShow("UserSig 过期");
                        break;
                    case 6208:
                        toastShow("其他终端登录同一个帐号，引起已登录的帐号被踢，需重新登录");
                        break;
                    case 7501:
                        toastShow("登录正在执行中");
                        break;
                    case 7502:
                        toastShow("请重新登录，登录失败");
                        break;
                    case 7503:
                        toastShow("初始化失败，内部错误");
                        break;
                    case 7504:
                        toastShow("未初始化，内部错误");
                        break;
                    case 7505:
                        toastShow("包格式错误，内部错误");
                        break;
                    case 7506:
                        toastShow("解密失败，内部错误");
                        break;
                    case 7507:
                        toastShow("请求失败，内部错误");
                        break;
                    case 7508:
                        toastShow("请求超时，内部错误");
                        break;
                }
                logintim();
            }
        });
    }

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
            System.out.println(">>>>>>>>>>>>>>>>>>" + (System.currentTimeMillis() - current));
            // 添加自定初始化配置
//            customConfig();
            System.out.println(">>>>>>>>>>>>>>>>>>" + (System.currentTimeMillis() - current));

            if (IMFunc.isBrandXiaoMi()) {
                // 小米离线推送
                MiPushClient.registerPush(this, Constants.XM_PUSH_APPID, Constants.XM_PUSH_APPKEY);
            }
            if (IMFunc.isBrandHuawei()) {
                // 华为离线推送
//                boolean init = HMSAgent.init(this);
//                OkLogger.e(init + "");
            }
            if (MzSystemUtils.isBrandMeizu(this)) {
                // 魅族离线推送
                PushManager.register(this, Constants.MZ_PUSH_APPID, Constants.MZ_PUSH_APPKEY);
            }
            if (IMFunc.isBrandVivo()) {
                // vivo 离线推送
                PushClient.getInstance(getApplicationContext()).initialize();
            }

            if (IMFunc.isBrandOppo()) {

                PushClient.getInstance(getApplicationContext()).initialize();
                com.heytap.mcssdk.PushManager.getInstance().register(this, Constants.OPPO_PUSH_APPKEY, Constants.OPPO_PUSH_APPSECRET, new OPPOPushImpl());

                createNotificationChannel(this);
            }
        }


    }

    public void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "oppotest";
            String description = "this is opptest";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;


            NotificationChannel channel = new NotificationChannel(GenerateTestUserSig.SDKAPPID + "", name, importance);
            channel.setDescription(description);
            String id = channel.getId();
            OkLogger.e("ChannelID = " + id);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void logout_two() {
        SPUtils.getInstance(this).put(Utils.TOKEN, "");
        Intent kick_out_intent = new Intent(App.getAppContext(), Loginregister_phone_Activity.class);
        kick_out_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(kick_out_intent);

        ApiClient.onDestroy();
        finish();
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {

            switch (code) {
                case 0:
                    break;
                case 6002:
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000);
                    break;
                default:

            }
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:

                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;

            }
        }
    };

    private void checkLocation() {
        PermissionsUtil.requestPermission(mActivity, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permissions) {
                SPUtils.getInstance(mActivity).put(Utils.PERMISSION, true);

                initLocationOption();

                UserInfoManger.saveIsGranted(true);
            }

            @Override
            public void permissionDenied(@NonNull String[] permissions) {
                SPUtils.getInstance(mActivity).put(Utils.PERMISSION, false);

            }
        }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private void initLocationOption() {
        locationClient = new LocationClient(getApplicationContext());
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(myLocationListener);
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationOption.setCoorType("gcj02");
        locationOption.setIsNeedAddress(true);
        locationOption.setIsNeedLocationDescribe(true);
        locationOption.setNeedDeviceDirect(false);
        locationOption.setLocationNotify(true);
        locationOption.setIgnoreKillProcess(true);
        locationOption.setIsNeedLocationDescribe(true);
        locationOption.setIsNeedLocationPoiList(true);
        locationOption.SetIgnoreCacheException(false);
        locationOption.setOpenGps(true);
        locationOption.setIsNeedAltitude(false);
        locationOption.setOpenAutoNotifyMode();
        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        locationClient.setLocOption(locationOption);
        locationClient.start();
    }

    @Override
    public void updateUnread(int count) {
        totalcount = count;
        if (count > 0) {
            tv_interaction_number.setVisibility(View.VISIBLE);
        } else {
            tv_interaction_number.setVisibility(View.GONE);
        }
        String unreadStr = "" + count;
        if (count > 99) {
            unreadStr = "...";
        }
        if (unreadStr.equals("0")) {
            tv_interaction_number.setVisibility(View.GONE);
        } else {
            tv_interaction_number.setVisibility(View.VISIBLE);
        }
        tv_interaction_number.setText(unreadStr);
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            latitude = location.getLatitude() + "";
            longitude = location.getLongitude() + "";
            UserInfoManger.savecity(location.getCity());
            UserInfoManger.saveLongitude(location.getLongitude() + "");
            UserInfoManger.saveLatitude(location.getLatitude() + "");
            homePresenter.getCity(location.getCity());
        }
    }

    private void showLocation(Location location) {
        if (location != null) {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            UserInfoManger.saveLongitude(longitude);
            UserInfoManger.saveLatitude(latitude);
            Log.d("@@@", "维度：" + latitude + "\n经度" + longitude);

        }
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getNum(GroupUnreadMessageNumEvent groupNum) {

        if (groupNum.getId().equals(Urls.i_you_circle) || groupNum.getId().equals(Urls.square_chat)) {
            return;
        }

        //加入俱乐未处理部消息数量
        String gSize = SPUtils.getInstance(App.getAppContext()).getString(UserInfoManger.getUserId() + "GROUP_JOIN_USER_PROCESSING", "");


        if(!gSize.equals("")){
            groupNum.setUnreadMessageNum(groupNum.getUnreadMessageNum() + Integer.parseInt(gSize));
        }

        long num = groupNum.getUnreadMessageNum();

        if (SPUtils.getInstance(mActivity).getLong(UserInfoManger.getUserId()+Utils.UN_READ_NUM) != -1) {
            num += SPUtils.getInstance(mActivity).getLong(UserInfoManger.getUserId()+Utils.UN_READ_NUM);
        }

        if ( num > 0) {
            if (num > 99) {
                tv_group_number.setText("...");
            } else {
                tv_group_number.setText(num + "");
            }
            tv_group_number.setVisibility(View.VISIBLE);
        } else {
            tv_group_number.setVisibility(View.GONE);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        TUIKit.addIMEventListener(mIMEventListener);

//
//        if (SPUtils.getInstance(mActivity).getBoolean(Utils.APPID + Utils.ISDYNAMIC, false) || !SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.MESSAGE_NUMBER, "0").equals("0")) {
//            tv_find.setVisibility(View.VISIBLE);
//        } else {
//            tv_find.setVisibility(View.GONE);
//        }

        refreshfriendnumber();

        //刷新俱乐部红点标识
        allMyPartysRedType();
    }

    // 普通事件的处理
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(AddMoneyBean carrier) {
        dialog = new UpgradeDialog(this, new CommonDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        }, carrier);
//        dialog.setData();
        dialog.show();
    }

    public void initListener() {
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);
        homePresenter = new HomePresenter(this);

        rlHome.performClick();

        PermissionGen.needPermission(this, 100
                , new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION});
    }

    public void initSelector(ImageView btn1, ImageView btn2, ImageView btn3, ImageView btn4, ImageView btn5) {
        if (position == 0) {
            btn1.setBackgroundResource(R.mipmap.icon_tab_home_true);
        } else {
            btn1.setBackgroundResource(R.mipmap.icon_tab_home);
        }
        if (position == 1) {

            btn2.setBackgroundResource(R.mipmap.icon_tab_interactive_true);
        } else {
            btn2.setBackgroundResource(R.mipmap.icon_tab_interactive);
        }
        if (position == 2) {
            btn3.setBackgroundResource(R.mipmap.icon_tab_club_true);
        } else {
            btn3.setBackgroundResource(R.mipmap.icon_tab_club);
        }

        if (position == 3) {

            btn4.setBackgroundResource(R.mipmap.icon_tab_find_true);
        } else {
            btn4.setBackgroundResource(R.mipmap.icon_tab_find);
        }
        if (position == 4) {
            btn5.setBackgroundResource(R.mipmap.icon_tab_mine_true);
        } else {
            btn5.setBackgroundResource(R.mipmap.icon_tab_mine);
        }
    }


    @OnClick({R.id.rl_home, R.id.rl_interactive, R.id.rl_club, R.id.rl_find, R.id.rl_mine})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_home:
                isCheckHome = true;
                StatusBarUtil.setTranslucentStatus(this);
                StatusBarUtil.setStatusBarDarkTheme(this, false);
                position = 0;
                break;
            case R.id.rl_interactive:
                isCheckHome = false;
                StatusBarUtil.setStatusBarDarkTheme(this, false);
                position = 1;
                refreshfriendnumber();
                break;
            case R.id.rl_club:
                isCheckHome = false;
                StatusBarUtil.setStatusBarDarkTheme(this, false);
                position = 2;
                break;
            case R.id.rl_find:
                isCheckHome = false;
                StatusBarUtil.setStatusBarDarkTheme(this, false);
                position = 3;

                break;
            case R.id.rl_mine:
                isCheckHome = false;
                StatusBarUtil.setTranslucentStatus(this);
                StatusBarUtil.setStatusBarDarkTheme(this, false);
                position = 4;
                break;
        }
        initSelector(btn_home, btn_interaction, btn_club, btn_find, btn_mine);
        Fragment fragment = getFragment(position);
        switchFragment(tempFragemnt, fragment);
        if (homeFragment != null) {
            homeFragment.setIsCheckHome(isCheckHome);
        }
        if (position == 3) {
            Intent intent_main_find = new Intent("main_find");
            sendBroadcast(intent_main_find);
        }
    }

    public void refreshfriendnumber() {

        List<TIMFriendPendencyItem> items = new ArrayList<>();
        TIMFriendPendencyRequest timFriendPendencyRequest = new TIMFriendPendencyRequest();
        timFriendPendencyRequest.setTimPendencyGetType(TIM_PENDENCY_BOTH);
        TIMFriendshipManager.getInstance().getPendencyList(timFriendPendencyRequest, new TIMValueCallBack<TIMFriendPendencyResponse>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(TIMFriendPendencyResponse timFriendPendencyResponse) {
                List<TIMFriendPendencyItem> timFriendPendencyItemList = timFriendPendencyResponse.getItems();

                if (timFriendPendencyItemList.size() > 0) {
                    for (TIMFriendPendencyItem timFriendPendencyItem : timFriendPendencyItemList) {
                        if (!timFriendPendencyItem.getAddWording().equals("isTempy")) {
                            if (timFriendPendencyItem.getType() == TIM_PENDENCY_COME_IN) {
                                items.add(timFriendPendencyItem);
                            }
                        }
                    }
                }
                Intent intent = new Intent("addfriend");
                intent.putExtra(Utils.NUMBER, items.size());
                sendBroadcast(intent);
                SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.ADDFRIENDNUMBER, items.size());
                int toalcount = totalcount + items.size();

                if (toalcount > 0) {
                    tv_interaction_number.setVisibility(View.VISIBLE);
                } else {
                    tv_interaction_number.setVisibility(View.GONE);
                }
                String unreadStr = "" + toalcount;
                if (toalcount > 99) {
                    unreadStr = "...";
                }
                if (unreadStr.equals("0")) {
                    tv_interaction_number.setVisibility(View.GONE);
                } else {
                    tv_interaction_number.setVisibility(View.VISIBLE);
                }
                tv_interaction_number.setText(unreadStr);

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            homeexitDialog.show();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private Fragment getFragment(int currentposition) {
        if (fragments != null && fragments.size() > 0) {
            Fragment baseFragment = fragments.get(currentposition);
            return baseFragment;
        }
        return null;
    }

    //切换的时候 避免重新走一次fragment生命周期  避免重新实例化
    private void switchFragment(Fragment fromframent, Fragment nextFragment) {
        if (tempFragemnt != nextFragment) {
            tempFragemnt = nextFragment;
            if (nextFragment != null) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (!nextFragment.isAdded()) {
                    if (fromframent != null) {
                        transaction.hide(fromframent);
                    }
                    transaction.add(R.id.main_fl, nextFragment).commit();
                } else {
                    if (fromframent != null) {
                        transaction.hide(fromframent);
                    }
                    transaction.show(nextFragment).commit();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
//        destroyServise();
        locationClient.stop();
        mHandler.removeCallbacks(runnable);
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        ChatRoomUtil.quitGroup();
        ApiClient.onDestroy();
    }

    public void destroyServise() {
        // 销毁悬浮窗
        Intent intent = new Intent(MainActivity.this, FloatViewService.class);
        //终止FloatViewService
        stopService(intent);
    }

    @Override
    public void getDataSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);

            if (jsonObject.getString("code").equals("11001")) {
                toastShow(jsonObject.getString("message"));
                SPUtils.getInstance(App.getAppContext()).put(Utils.TOKEN, "");
                Intent kick_out_intent = new Intent(App.getAppContext(), Loginregister_phone_Activity.class);
                kick_out_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(kick_out_intent);
                finish();
            }

            JSONObject data = jsonObject.getJSONObject("data");
            if (jsonObject.getString("code").equals("200")) {
                SPUtils.getInstance(mActivity).put(Utils.HEAD_URL, data.getString("head"));
                SPUtils.getInstance(mActivity).put(Utils.USER_NAME, data.getString("userNickName"));
                if (jsonObject.getJSONObject("data").getInt("gender") == 1) {
                    Utils.ISSEX = "男";
                    if (intent.getIntExtra("isSendJewel", 0) == 1) {
                        receivedialog.show();
                    }
                } else {
                    Utils.ISSEX = "女";
                    if (intent.getIntExtra("isSendJewel", 0) == 1) {
                        authenticationDialog.show();
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getDataFail(String msg) {
        Toast.makeText(mActivity, "没有网络", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void GetImSuccess(ResponseBody responseBody) {

    }

    @Override
    public void GetImfail(String s) {

    }

    @Override
    public void UserSigSuccess(ResponseBody responseBody) {

    }

    @Override
    public void UserSigfail(String s) {

    }

    @Override
    public void getgiftlist(String s) {
        SPUtils.getInstance(mActivity).put(Utils.GETGIFTLIST, s);
    }

    @Override
    public void showGiftList(List<GiftData.DataBean.ArrayBean> array) {
        Utils.giftList.clear();
        Utils.giftList.addAll(array);
    }

    @Override
    public void loginOutSuccess(ResponseBody responseBody) {

        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            //   toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                homeexitDialog.dismiss();
                logout();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 更新
     */
    @Override
    public void showUpdateDialog(AppVersion appVersion) {
        //token 过期
        if (appVersion.getCode().equals("11001")) {
            toastShow(appVersion.getMessage());
            SPUtils.getInstance(App.getAppContext()).put(Utils.TOKEN, "");
            Intent kick_out_intent = new Intent(App.getAppContext(), Loginregister_phone_Activity.class);
            kick_out_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(kick_out_intent);
            finish();
        } else if (appVersion != null && !TextUtils.isEmpty(appVersion.getData().getAppVersion())) {

            if (Integer.valueOf(appVersion.getData().getAppVersion()) > AppUtils.getVersionCode(this)) {
                mAppUpdateDialog = new AppUpdateDialog(this, R.style.dialogstyle, appVersion, this);
                mAppUpdateDialog.show();
            }


        }
    }

    @Override
    public void getTodayStar(List<TodayStarBean> todayStarBean) {

    }

    /**
     * 退出登录
     */
    private void logout() {
        SPUtils.getInstance(this).put(Utils.TOKEN, "");
        Intent kick_out_intent = new Intent(App.getAppContext(), Loginregister_phone_Activity.class);
        kick_out_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(kick_out_intent);
        if (Utils.channel != null) {
            Utils.channel.close();
        }
        ApiClient.onDestroy();
        finish();
    }


    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "sendgift":

                    if (Utils.netWork()) {
                        List<GiftData.DataBean.ArrayBean> giftList = new ArrayList<>();
                        String s = SPUtils.getInstance(mActivity).getString(Utils.GETGIFTLIST);
                        GiftchatBean giftchatBean = JSON.parseObject(s, GiftchatBean.class);
                        List<GiftchatBean.DataBean.ArrayBean> array = giftchatBean.getData().getArray();
                        for (int i = 0; i < array.size(); i++) {
                            GiftchatBean.DataBean.ArrayBean arrayBean = array.get(i);
                            GiftData.DataBean.ArrayBean bean = new GiftData.DataBean.ArrayBean(arrayBean.getGiftId(), arrayBean.getTop(), arrayBean.getPrice(), arrayBean.getPictureUrl(), arrayBean.getName(), arrayBean.getState());
                            giftList.add(bean);
                        }
                        popupwindow_gift = new Dialog_gift(mActivity, R.style.Dialog_Light, giftList, intent.getStringExtra(Utils.NICKNAME));
                        popupwindow_gift.setUserId(intent.getStringExtra(Utils.RECEIVEID));
                        popupwindow_gift.show();
                    } else {
                        toastShow(R.string.nonetwork);
                    }
                    break;
                case "dynamic":
                    SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.ISDYNAMIC, true);
                    tv_find.setVisibility(View.VISIBLE);
                    break;
                case "praise":

                    tv_find.setVisibility(View.VISIBLE);
                    break;
                case "hideRed":
                    tv_find.setVisibility(View.GONE);
                    break;

                default:
                    break;
            }
        }
    };

    // 如果位置发生变化，重新显示
    LocationListener mListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        // 如果位置发生变化，重新显示
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }
    };

}
