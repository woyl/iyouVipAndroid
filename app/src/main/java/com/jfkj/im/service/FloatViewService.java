package com.jfkj.im.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.bumptech.glide.Glide;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.bean.ConversationMessage;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.ui.activity.ChatActivity;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMConversationType;

public class FloatViewService extends Service {


    private static final String TAG = "FloatViewService";
    //定义浮动窗口布局
    private LinearLayout mFloatLayout;
    private WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;

    private ImageView head_iv, head_iv_1;
    private TextView tv_message_1, tv_message;
    private ViewFlipper notice_content;
    private View view1, view2;

    private String text,img;
    private boolean isNewMessage;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        createFloatView();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        text = intent.getStringExtra("text");
        img = intent.getStringExtra("img");
        isNewMessage = intent.getBooleanExtra("new",false);
        Glide.with(App.getAppContext()).load(img).into(head_iv);
        Glide.with(App.getAppContext()).load(img).into(head_iv_1);
        tv_message.setText(text);
        tv_message_1.setText(text);
        if (isNewMessage){
            notice_content.startFlipping();
        } else {
            notice_content.stopFlipping();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressWarnings("static-access")
    @SuppressLint({"InflateParams", "RtlHardcoded"})
    private void createFloatView() {
        wmParams = new WindowManager.LayoutParams();
        //通过getApplication获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager) getApplication().getSystemService(getApplication().WINDOW_SERVICE);
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            }
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.BOTTOM;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 152;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.window_filer, null);
        //浮动窗口按钮
        view1 = LayoutInflater.from(this).inflate(R.layout.layout_notice, null);
        view2 = LayoutInflater.from(this).inflate(R.layout.layout_notice, null);

        notice_content = mFloatLayout.findViewById(R.id.notice_content);

        head_iv = view1.findViewById(R.id.head_iv);
        tv_message = view1.findViewById(R.id.tv_message);

        head_iv_1 = view2.findViewById(R.id.head_iv);
        tv_message_1 = view2.findViewById(R.id.tv_message);

        notice_content.addView(view1);
        notice_content.addView(view2);
        //添加mFloatLayout
        mWindowManager.addView(mFloatLayout, wmParams);

        mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //设置监听浮动窗口的触摸移动

        mFloatLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(SPUtils.getInstance(App.getAppContext()).getString(Utils.AV_CHAT_ROOM_ID));
            }
        });

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

        Intent intent = new Intent(App.getAppContext(), ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.putExtra(Utils.CONVERSATIONMESSAGE, conversationMessage);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatLayout != null) {
            //移除悬浮窗口
            mWindowManager.removeView(mFloatLayout);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }
}
