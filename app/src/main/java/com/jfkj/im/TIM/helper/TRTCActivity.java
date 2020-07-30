package com.jfkj.im.TIM.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.base.BaseTimActivity;
import com.jfkj.im.TIM.signature.GenerateTestUserSig;
import com.jfkj.im.TIM.utils.DemoLog;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.listener.CountDownTimeListener;
import com.jfkj.im.service.VideoFloatService;
import com.jfkj.im.utils.ExecutorServiceUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.TimeCountUtil;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMManager;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import java.lang.ref.WeakReference;

import static com.tencent.trtc.TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL;
import static com.tencent.trtc.TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FIT;

public class TRTCActivity extends BaseTimActivity implements View.OnClickListener, CountDownTimeListener {

    private static final String TAG = TRTCActivity.class.getSimpleName();

    public static final String KEY_ROOM_ID = "room_id";

    private TextView mHangupIv, trtc_iv_camera;
    private TXCloudVideoView mLocalPreviewView;
//    public static TXCloudVideoView mSubVideoView;
    TextView tv_time;
    private TRTCCloud mTRTCCloud;
    int timein = 0;
    private TRTCCloudDef.TRTCParams mTRTCParams;

    private RelativeLayout rl_video, rl_bt;
    private boolean isShow = true;

    private TimeCountUtil timeCountUtil;
    private ImageView img_zoom;
    private Context mContext;
    private int roomId;


    TRTCCloudListener mTRTCCloudListener = new TRTCCloudListener() {


        @Override
        public void onError(int errCode, String errMsg, Bundle extraInfo) {
            Log.d(TAG, "sdk callback onError");
//            Toast.makeText(mContext, "onError: " + errMsg + "[" + errCode+ "]" , Toast.LENGTH_SHORT).show();
            if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
//                finishVideoCall();
            }
            finish();
        }

        @Override
        public void onExitRoom(int reason) {
            super.onExitRoom(reason);

            DemoLog.i(TAG, "onExitRoom " + reason);
//            finish();
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
//                finishVideoCall();
//                finish();
            }
        }

        @Override
        public void onFirstVideoFrame(String userId, int steamType, int width, int height) {
            DemoLog.e(TAG, "onFirstVideoFrame " + userId + " " + steamType + " " + width + " " + height);
            super.onFirstVideoFrame(userId, steamType, width, height);
            if (!TextUtils.equals(userId, TIMManager.getInstance().getLoginUser())) {
//                ViewGroup.LayoutParams params = mSubVideoView.getLayoutParams();
//                final int FIXED = 280;
//                params.width = FIXED;
//                params.height = FIXED * height / width;
//                mSubVideoView.setLayoutParams(params);
            }
        }

        @Override
        public void onUserVideoAvailable(String userId, boolean available) {
            super.onUserVideoAvailable(userId, available);
            DemoLog.e(TAG, "onUserVideoAvailable " + userId + " " + available);
            if (available) {
//                mUserId = userId;
//                mTRTCCloud.setRemoteViewFillMode(userId, TRTC_VIDEO_RENDER_MODE_FIT);
//                mTRTCCloud.startRemoteView(userId, mSubVideoView);
            }
        }

    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        DemoLog.i(TAG, "onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        // 设置布局
        setContentView(R.layout.trtc_activity);
        mLocalPreviewView = findViewById(R.id.local_video_preview);
//        mSubVideoView = findViewById(R.id.sub_video);
        mHangupIv = findViewById(R.id.hangup);
        tv_time = findViewById(R.id.tv_time);
        trtc_iv_camera = findViewById(R.id.trtc_iv_camera);

        rl_video = findViewById(R.id.rl_video);
        rl_bt = findViewById(R.id.rl);
        img_zoom = findViewById(R.id.img_zoom);

        rl_video.setOnClickListener(this);
        img_zoom.setOnClickListener(this);
        mContext = this;

        TRTCActivityWeek week = new TRTCActivityWeek(this);


        mHangupIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DemoLog.i(TAG, "mHangupIv click");
                finishVideoCall();
//                finish();
            }
        });

        ExecutorServiceUtils.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        timein = timein + 1000;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_time.setText(ms2HMS(timein));
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        // 获取进房参数
        roomId = getIntent().getIntExtra(KEY_ROOM_ID, 0);
        int sdkAppId = GenerateTestUserSig.SDKAPPID;
        String userId = TIMManager.getInstance().getLoginUser();
        String userSig = SPUtils.getInstance(this).getString(Utils.USER_SIG);

        mTRTCParams = new TRTCCloudDef.TRTCParams(sdkAppId, userId, userSig, roomId, "", "");
        mTRTCCloud = TRTCCloud.sharedInstance(App.getAppContext());

        // 开始进房
        enterRoom();

        trtc_iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTRTCCloud.switchCamera();
            }
        });

        if (timeCountUtil == null) {
            timeCountUtil = new TimeCountUtil(10000, 1000, this);
        }
    }

    public static String ms2HMS(int _ms) {
        String HMStime;
        _ms /= 1000;
        int hour = _ms / 3600;
        int mint = (_ms % 3600) / 60;
        int sed = _ms % 60;
        String hourStr = String.valueOf(hour);
        if (hour < 10) {
            hourStr = "0" + hourStr;
        }
        String mintStr = String.valueOf(mint);
        if (mint < 10) {
            mintStr = "0" + mintStr;
        }
        String sedStr = String.valueOf(sed);
        if (sed < 10) {
            sedStr = "0" + sedStr;
        }
        HMStime = hourStr + ":" + mintStr + ":" + sedStr;
        return HMStime;
    }

    private void enterRoom() {
//        TRTCListener.getInstance().addTRTCCloudListener(mTRTCCloudListener);
//        TRTCListener.getInstance().addTRTCCloudListener(new TRTCCloudListerers(mSubVideoView,mTRTCCloud,mTRTCParams));

        mTRTCCloud.setListener(TRTCListener.getInstance());
        mTRTCCloud.startLocalAudio();
        mTRTCCloud.startLocalPreview(true, mLocalPreviewView);
        mTRTCCloud.enterRoom(mTRTCParams, TRTC_APP_SCENE_VIDEOCALL);

        Intent intent = new Intent(mContext, VideoFloatService.class);
        intent.putExtra(KEY_ROOM_ID, roomId);
        mContext.startService(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DemoLog.i(TAG, "onBackPressed");
        finishVideoCall();
    }

    public static class TRTCActivityWeek {
        private static WeakReference<TRTCActivity> activityWeakReference;

        public TRTCActivityWeek(TRTCActivity activity) {
            activityWeakReference = new WeakReference<TRTCActivity>(activity);
        }

        public static WeakReference<TRTCActivity> getInstance(){
            return activityWeakReference;
        }
    }

    public void finishVideoCall() {
        CustomAVCallUIController.getInstance().hangup();
        mTRTCCloud.exitRoom();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        TRTCListener.getInstance().removeTRTCCloudListener(mTRTCCloudListener);
        if (timeCountUtil != null) {
            timeCountUtil.cancel();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_video:
                timeCountUtil.start();
                isShow = !isShow;
                if (isShow) {
                    rl_bt.setVisibility(View.VISIBLE);
                    img_zoom.setVisibility(View.VISIBLE);
                } else {
                    rl_bt.setVisibility(View.GONE);
                    img_zoom.setVisibility(View.GONE);
                }
                break;
            case R.id.img_zoom:
                animation();
                break;
        }
    }

    private void animation() {
        moveTaskToBack(true);
    }

    @Override
    public void onTimeFinish() {
        rl_bt.setVisibility(View.GONE);
        img_zoom.setVisibility(View.GONE);
    }

    @Override
    public void onTimeTick(long millisUntilFinished) {

    }
}
