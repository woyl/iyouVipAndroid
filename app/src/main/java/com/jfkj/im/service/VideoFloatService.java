package com.jfkj.im.service;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.helper.CustomAVCallUIController;
import com.jfkj.im.TIM.helper.TRTCActivity;
import com.jfkj.im.TIM.helper.TRTCListener;
import com.jfkj.im.TIM.signature.GenerateTestUserSig;
import com.jfkj.im.TIM.utils.DemoLog;
import com.jfkj.im.listener.OnDragTouchListener;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.utils.media.ScreenUtil;
import com.tencent.imsdk.TIMManager;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import java.lang.ref.WeakReference;

import static com.tencent.trtc.TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL;

public class VideoFloatService extends Service {


    private static final String TAG = "FloatViewService";
    //定义浮动窗口布局
    private WindowManager.LayoutParams wmParams;
    //创建浮动窗口设置布局参数的对象
    private WindowManager mWindowManager;

    private TXCloudVideoView txCloudVideoView;
    private View view;

    private TRTCCloud mTRTCCloud;
    private TRTCCloudDef.TRTCParams mTRTCParams;
    public static final String KEY_ROOM_ID = "room_id";



    @Override
    public void onCreate() {
        super.onCreate();
        createFloatView();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int roomId = intent.getIntExtra(KEY_ROOM_ID, 0);


        // 获取进房参数
        int sdkAppId = GenerateTestUserSig.SDKAPPID;
        String userId = TIMManager.getInstance().getLoginUser();
        String userSig = SPUtils.getInstance(this).getString(Utils.USER_SIG);
        mTRTCParams = new TRTCCloudDef.TRTCParams(sdkAppId, userId, userSig, roomId, "", "");

        // 开始进房
        enterRoom();
        return super.onStartCommand(intent, flags, startId);
    }

    private void enterRoom() {
        if (mTRTCCloud == null){
            mTRTCCloud = TRTCCloud.sharedInstance(App.getAppContext());
        }
        TRTCListener.getInstance().addTRTCCloudListener(mTRTCCloudListener);
        mTRTCCloud.setListener(mTRTCCloudListener);
//        mTRTCCloud.startLocalAudio();
//        mTRTCCloud.startLocalPreview(true, txCloudVideoView);
        mTRTCCloud.enterRoom(mTRTCParams, TRTC_APP_SCENE_VIDEOCALL);
        CustomAVCallUIController.getInstance().onEnterRoom(System.currentTimeMillis());
    }

    private void finishVideoCall() {
//        CustomAVCallUIController.getInstance().hangup();
        mTRTCCloud.exitRoom();
        TRTCActivity.TRTCActivityWeek.getInstance().get().finish();
    }


    private TRTCCloudListener mTRTCCloudListener = new TRTCCloudListener() {


        @Override
        public void onError(int errCode, String errMsg, Bundle extraInfo) {
            Toast.makeText(App.getAppContext(), "onError: " + errMsg + "[" + errCode + "]", Toast.LENGTH_SHORT).show();
            if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
                finishVideoCall();
            }
        }

        @Override
        public void onExitRoom(int reason) {
            super.onExitRoom(reason);
//            CustomAVCallUIController.getInstance().onExitRoom(0);
            finishVideoCall();
            DemoLog.i(TAG, "onExitRoom " + reason);
        }

        @Override
        public void onRemoteUserEnterRoom(String userId) {
            super.onRemoteUserEnterRoom(userId);
            DemoLog.i(TAG, "onRemoteUserEnterRoom " + userId);
        }

        @Override
        public void onRemoteUserLeaveRoom(String userId, int reason) {
            super.onRemoteUserLeaveRoom(userId, reason);
            // 对方超时掉线
            if (reason == 1) {
                finishVideoCall();
            }
        }

        @Override
        public void onFirstVideoFrame(String userId, int steamType, int width, int height) {
            DemoLog.e(TAG, "onFirstVideoFrame " + userId + " " + steamType + " " + width + " " + height);
            super.onFirstVideoFrame(userId, steamType, width, height);
            if (!TextUtils.equals(userId, TIMManager.getInstance().getLoginUser())) {
                ViewGroup.LayoutParams params = txCloudVideoView.getLayoutParams();
                final int FIXED = 280;
                params.width = FIXED;
                params.height = FIXED * height / width;
                txCloudVideoView.setLayoutParams(params);
            }
        }

        @Override
        public void onUserVideoAvailable(String userId, boolean available) {
            super.onUserVideoAvailable(userId, available);
            DemoLog.e(TAG, "onUserVideoAvailable " + userId + " " + available);
            if (available) {
                mTRTCCloud.setRemoteViewFillMode(userId, TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FIT);
                mTRTCCloud.startRemoteView(userId, txCloudVideoView);
            }
        }

    };

    @SuppressLint({"InflateParams", "RtlHardcoded", "ClickableViewAccessibility"})
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
//        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.RIGHT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 152;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局
        //浮动窗口按钮
        view = inflater.inflate(R.layout.layout_video_float, null);

        txCloudVideoView = view.findViewById(R.id.sub_video);

        //添加mFloatLayout
        mWindowManager.addView(view, wmParams);


//
//        view.measure(View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
//                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //设置监听浮动窗口的触摸移动

        OnDragTouchListener onDragTouchListener = new OnDragTouchListener(true, mWindowManager, wmParams);
        onDragTouchListener.setOnDraggableClickListener(new OnDragTouchListener.OnDraggableClickListener() {
            @Override
            public void onDragged(View v, int left, int top, ViewGroup.LayoutParams params) {
//                wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//                wmParams.x = left;
//                wmParams.y = top;
//                mWindowManager.updateViewLayout(view,wmParams);
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.getAppContext(), TRTCActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(App.getAppContext(), TRTCActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
            }
        });

//        view.setOnTouchListener(onDragTouchListener);

        view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        downX = event.getX();
//                        downY = event.getY();
////                        txCloudVideoView.bringToFront();
//                        break;
                    case MotionEvent.ACTION_UP:
//                        txCloudVideoView.layout(l, t, r, b);
                        if (Math.abs(event.getRawX() - downX) <
                                ScreenUtil.dpToPx(5) &&
                                Math.abs(event.getRawY() - downY) <
                                        ScreenUtil.dpToPx(5)) {
                            Intent intent = new Intent(App.getAppContext(), TRTCActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            return false;
                        }
                        break;
//                    case MotionEvent.ACTION_MOVE:
//
//                        float moveX = event.getX() - downX;
//                        float moveY = event.getY() - downY;
//
//                        //计算偏移量 设置偏移量 = 3 时 为判断点击事件和滑动事件的峰值
//                        if (Math.abs(moveX) > 3 || Math.abs(moveY) > 3) { // 偏移量的绝对值大于 3 为 滑动时间 并根据偏移量计算四点移动后的位置
//                            l = (int) (txCloudVideoView.getLeft() + moveX);
//                            r = l + txCloudVideoView.getWidth();
//                            t = (int) (txCloudVideoView.getTop() + moveY);
//                            b = t + txCloudVideoView.getHeight();
//                            //不划出边界判断,最大值为边界值
//                            // 如果你的需求是可以划出边界 此时你要计算可以划出边界的偏移量 最大不能超过自身宽度或者是高度  如果超过自身的宽度和高度 view 划出边界后 就无法再拖动到界面内了 注意
//                            if (l < 0) { // left 小于 0 就是滑出边界 赋值为 0 ; right 右边的坐标就是自身宽度 如果可以划出边界 left right top bottom 最小值的绝对值 不能大于自身的宽高
//                                l = 0;
//                                r = l + txCloudVideoView.getWidth();
//                            } else if (r > ScreenUtil.getScreenWidth()) { // 判断 right 并赋值
//                                r = ScreenUtil.getScreenWidth();
//                                l = r - txCloudVideoView.getWidth();
//                            }
//                            if (t < 0) { // top
//                                t = 0;
//                                b = t + txCloudVideoView.getHeight();
//                            } else if (b > ScreenUtil.getScreenHeight()) { // bottom
//                                b = ScreenUtil.getScreenHeight();
//                                t = b - txCloudVideoView.getHeight();
//                            }
//                            wmParams.x = l;
//                            wmParams.y = t;
//                            mWindowManager.updateViewLayout(view,wmParams);
////                            txCloudVideoView.layout(l, t, r, b); // 重置view在layout 中位置
////                            setRelativeViewLocation(txCloudVideoView, l, t, r, b);
////                            isDrag=true;  // 重置 拖动为 true
//                        } else {
////                            isDrag=false; // 小于峰值3时 为点击事件
//                        }
//                        break;
//                    case MotionEvent.ACTION_CANCEL:
//                        break;
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getRawX();
                        downY = event.getRawY();
                        l = wmParams.x;
                        t = wmParams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int deltaX = (int) (event.getRawX() - downX);
                        int deltaY = (int) (event.getRawY() - downY);
//                        l = (int) (txCloudVideoView.getLeft() + deltaX);
//                        r = l + txCloudVideoView.getWidth();
//                        t = (int) (txCloudVideoView.getTop() + deltaY);
//                        b = t + txCloudVideoView.getHeight();
//                        //不划出边界判断,最大值为边界值
//                        // 如果你的需求是可以划出边界 此时你要计算可以划出边界的偏移量 最大不能超过自身宽度或者是高度  如果超过自身的宽度和高度 view 划出边界后 就无法再拖动到界面内了 注意
//                        if (l < 0) { // left 小于 0 就是滑出边界 赋值为 0 ; right 右边的坐标就是自身宽度 如果可以划出边界 left right top bottom 最小值的绝对值 不能大于自身的宽高
//                            l = 0;
//                            r = l + txCloudVideoView.getWidth();
//                        } else if (r > ScreenUtil.getScreenWidth()) { // 判断 right 并赋值
//                            r = ScreenUtil.getScreenWidth();
//                            l = r - txCloudVideoView.getWidth();
//                        }
//                        if (t < 0) { // top
//                            t = 0;
//                            b = t + txCloudVideoView.getHeight();
//                        } else if (b > ScreenUtil.getScreenHeight()) { // bottom
//                            b = ScreenUtil.getScreenHeight();
//                            t = b - txCloudVideoView.getHeight();
//                        }
                        wmParams.x = l + deltaX;
                        wmParams.y = t + deltaY;
                        mWindowManager.updateViewLayout(v, wmParams);
                        break;
                }
                return true;
            }
        });
    }

    private float downX = 0;
    private float downY = 0;
    private int l, r, t, b; // 上下左右四点移动后的偏移量


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (view != null) {
            //移除悬浮窗口
            mWindowManager.removeView(view);
        }
        TRTCListener.getInstance().removeTRTCCloudListener(mTRTCCloudListener);
    }
}
