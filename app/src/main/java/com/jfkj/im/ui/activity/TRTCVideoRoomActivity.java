package com.jfkj.im.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.DialogFragment;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ToastUtils;
import com.jfkj.im.Bean.UsertailBean;
import com.jfkj.im.Bean.chat.Message;
import com.jfkj.im.Bean.chat.MsgBody;
import com.jfkj.im.Bean.chat.MsgSendStatus;
import com.jfkj.im.Bean.chat.MsgType;
import com.jfkj.im.R;
import com.jfkj.im.TIM.signature.GenerateTestUserSig;
import com.jfkj.im.mvp.BaseActivity;

import com.jfkj.im.mvp.TRTCVideoRoom.TRTCVideoRoomPresenter;
import com.jfkj.im.mvp.TRTCVideoRoom.TRTCVideoRoomView;
import com.jfkj.im.utils.ExecutorServiceUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.utils.trtc.trtc.customcapture.TestRenderVideoFrame;
import com.jfkj.im.utils.trtc.trtc.customcapture.TestSendCustomData;
import com.jfkj.im.utils.trtc.trtc.sdkadapter.ConfigHelper;
import com.jfkj.im.utils.trtc.trtc.sdkadapter.TRTCCloudManager;
import com.jfkj.im.utils.trtc.trtc.sdkadapter.TRTCCloudManagerListener;
import com.jfkj.im.utils.trtc.trtc.sdkadapter.bgm.TRTCBgmManager;
import com.jfkj.im.utils.trtc.trtc.sdkadapter.cdn.CdnPlayManager;
import com.jfkj.im.utils.trtc.trtc.sdkadapter.feature.AudioConfig;
import com.jfkj.im.utils.trtc.trtc.sdkadapter.feature.PkConfig;
import com.jfkj.im.utils.trtc.trtc.sdkadapter.feature.VideoConfig;
import com.jfkj.im.utils.trtc.trtc.sdkadapter.remoteuser.RemoteUserConfigHelper;
import com.jfkj.im.utils.trtc.trtc.sdkadapter.remoteuser.TRTCRemoteUserManager;
import com.jfkj.im.utils.trtc.trtc.widget.TRTCBeautySettingPanel;
import com.jfkj.im.utils.trtc.trtc.widget.bgm.BgmSettingFragmentDialog;
import com.jfkj.im.utils.trtc.trtc.widget.cdnplay.CdnPlayerSettingFragmentDialog;
import com.jfkj.im.utils.trtc.trtc.widget.feature.FeatureSettingFragmentDialog;
import com.jfkj.im.utils.trtc.trtc.widget.remoteuser.RemoteUserManagerFragmentDialog;
import com.jfkj.im.utils.trtc.trtc.widget.videolayout.TRTCVideoLayoutManager;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCStatistics;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class TRTCVideoRoomActivity extends BaseActivity<TRTCVideoRoomPresenter> implements View.OnClickListener, TRTCCloudManagerListener, TRTCCloudManager.IView, TRTCRemoteUserManager.IView, ITXLivePlayListener, TRTCVideoRoomView {

    public static final String KEY_ROLE = "role";
    public static final String KEY_CUSTOM_CAPTURE = "custom_capture";
    public static final String KEY_VIDEO_FILE_PATH = "file_path";
    public static final String KEY_RECEIVED_VIDEO = "auto_received_video";
    public static final String KEY_RECEIVED_AUDIO = "auto_received_audio";
    private static final String TAG = "TRTCVideoRoomActivity";

    private TRTCCloud mTRTCCloud;                 // SDK 核心类
    private TRTCCloudDef.TRTCParams mTRTCParams;                // 进房参数
    private int mAppScene;                  // 推流模式，文件头第一点注释
    private TRTCCloudManager mTRTCCloudManager;          // 提供核心的trtc
    private TRTCRemoteUserManager mTRTCRemoteUserManager;
    private TRTCBgmManager mBgmManager;
    private CdnPlayManager mCdnPlayManager;
    TRTCVideoRoomPresenter trtcVideoRoomPresenter;
    private TRTCVideoLayoutManager mTRTCVideoLayout;           // 视频 View 的管理类，包括：预览自身的 View、观看其他主播的 View。
    private TextView mTvRoomId;                  // 标题栏
    private ImageView mIvSwitchRole;              // 切换角色按钮
    private FeatureSettingFragmentDialog mFeatureSettingFragmentDialog; //更多设置面板
    private BgmSettingFragmentDialog mBgmSettingFragmentDialog;//音效设置面板
    private RemoteUserManagerFragmentDialog mRemoteUserManagerFragmentDialog;
    private TRTCBeautySettingPanel mTRTCBeautyPanel;           // 美颜设置控件
    private CdnPlayerSettingFragmentDialog mCdnPlayerSettingFragmentDialog;
    private ImageView mIvSwitchCamera;
    private ImageView mIvEnableAudio;
    private Group mCdnPlayViewGroup;
    private TXCloudVideoView mCdnPlayView;
    private Button mSwitchCdnBtn;
    private int mLogLevel = 0;  // 日志等级
    private boolean isCdnPlay = false;
    private String mMainUserId = ""; //主播id
    private boolean mIsCustomCaptureAndRender = false;
    private String mVideoFilePath;             // 视频文件路径
    private TestSendCustomData mCustomCapture;             // 外部采集
    private TestRenderVideoFrame mCustomRender;              // 外部渲染
    private Group mRoleAudienceGroup;
    private TRTCBeautySettingPanel mBeautyPanelTrtc;
    private ImageView mIvMoreTrtc;
    private boolean mReceivedVideo = true;
    private boolean mReceivedAudio = true;
    @BindView(R.id.call_btn_refuse_red_iv)
    ImageView call_btn_refuse_red_iv;
    Intent getintent;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.call_btn_small_iv)
    ImageView call_btn_small_iv;

    String receiveid;
    int roomId;
    int timein = 0;
    UsertailBean.DataBean usertailBean;
    public static final String mSenderId = "right";
    public static final String mTargetId = "left";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getintent = getIntent();
        mAppScene = 0;
        roomId = Integer.parseInt(getintent.getStringExtra(Utils.VROOMID));
        receiveid = getintent.getStringExtra(Utils.USERID);
        tv.setVisibility(View.GONE);
        mIsCustomCaptureAndRender = getintent.getBooleanExtra(KEY_CUSTOM_CAPTURE, false);
        mReceivedVideo = getintent.getBooleanExtra(KEY_RECEIVED_VIDEO, true);
        mReceivedAudio = getintent.getBooleanExtra(KEY_RECEIVED_AUDIO, true);
        trtcVideoRoomPresenter = new TRTCVideoRoomPresenter(this);
        trtcVideoRoomPresenter.TRTCVideoRoom(receiveid);


        mTRTCParams.role = 20;
        call_btn_refuse_red_iv.setOnClickListener(this);
        call_btn_small_iv.setOnClickListener(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("hangup");
        registerReceiver(broadcastReceiver, intentFilter);
        // 应用运行时，保持不锁屏、全屏化
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 初始化
        initTRTCSDK();
        // 初始化 View
        initViews();
        // 初始化外部采集和渲染
        if (mIsCustomCaptureAndRender) {
            initCustomCapture();
        }
        // 开始进房
        enterRoom();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.trtc_activity_video_room;
    }

    @Override
    public TRTCVideoRoomPresenter createPresenter() {
        return trtcVideoRoomPresenter;
    }

    private void initCustomCapture() {
        mVideoFilePath = getIntent().getStringExtra(KEY_VIDEO_FILE_PATH);
        mCustomCapture = new TestSendCustomData(this, mVideoFilePath, true);
        mCustomRender = new TestRenderVideoFrame(mTRTCParams.userId, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
    }

    private void enterRoom() {
        VideoConfig videoConfig = ConfigHelper.getInstance().getVideoConfig();
        AudioConfig audioConfig = ConfigHelper.getInstance().getAudioConfig();

        if (mTRTCParams.role == TRTCCloudDef.TRTCRoleAnchor) {

            startLocalPreview();
            videoConfig.setEnableVideo(true);

            mTRTCCloudManager.startLocalAudio();
            audioConfig.setEnableAudio(true);
            // 耳返
            mTRTCCloudManager.enableEarMonitoring(audioConfig.isEnableEarMonitoring());
        } else {
            videoConfig.setEnableVideo(false);
            audioConfig.setEnableAudio(false);
        }
        mTRTCCloudManager.enterRoom();
    }

    /**
     * 退房
     */
    private void exitRoom() {
        stopLocalPreview();
        mTRTCCloudManager.exitRoom();
    }

    private void initViews() {
        // 界面底部功能键初始化
        findViewById(R.id.trtc_iv_mode).setOnClickListener(this);
        findViewById(R.id.trtc_iv_beauty).setOnClickListener(this);
        mIvSwitchCamera = (ImageView) findViewById(R.id.trtc_iv_camera);
        mIvSwitchCamera.setOnClickListener(this);
        mIvEnableAudio = (ImageView) findViewById(R.id.trtc_iv_mic);
        mIvEnableAudio.setOnClickListener(this);
        findViewById(R.id.trtc_iv_log).setOnClickListener(this);
        findViewById(R.id.trtc_iv_setting).setOnClickListener(this);
        findViewById(R.id.trtc_ib_back).setOnClickListener(this);
        findViewById(R.id.trtc_iv_music).setOnClickListener(this);
        mTvRoomId = (TextView) findViewById(R.id.trtc_tv_room_id);
        mTvRoomId.setText(String.valueOf(mTRTCParams.roomId));
        mCdnPlayView = (TXCloudVideoView) findViewById(R.id.trtc_cdn_play_view);
        mCdnPlayViewGroup = (Group) findViewById(R.id.trtc_cdn_view_group);
        mCdnPlayViewGroup.setVisibility(View.GONE);
        mSwitchCdnBtn = (Button) findViewById(R.id.btn_switch_cdn);
        mRoleAudienceGroup = (Group) findViewById(R.id.group_role_audience);
        mIvMoreTrtc = (ImageView) findViewById(R.id.trtc_iv_more);
        mIvMoreTrtc.setOnClickListener(this);

        // 美颜参数回调设置
        mTRTCBeautyPanel = (TRTCBeautySettingPanel) findViewById(R.id.trtc_beauty_panel);
        mTRTCBeautyPanel.setTRTCCloudManager(mTRTCCloudManager);

        // 更多设置面板
        mFeatureSettingFragmentDialog = new FeatureSettingFragmentDialog();
        mFeatureSettingFragmentDialog.setTRTCCloudManager(mTRTCCloudManager, mTRTCRemoteUserManager);
        mFeatureSettingFragmentDialog.setId(String.valueOf(mTRTCParams.roomId), mTRTCParams.userId);

        // BGM、音效设置面板
        mBgmSettingFragmentDialog = new BgmSettingFragmentDialog();
        mBgmSettingFragmentDialog.setTRTCBgmManager(mBgmManager);

        // 成员列表面板
        mRemoteUserManagerFragmentDialog = new RemoteUserManagerFragmentDialog();
        mRemoteUserManagerFragmentDialog.setTRTCRemoteUserManager(mTRTCRemoteUserManager);
        RemoteUserConfigHelper.getInstance().clear();

        // 界面视频 View 的管理类
        mTRTCVideoLayout = (TRTCVideoLayoutManager) findViewById(R.id.trtc_video_view_layout);
        mTRTCVideoLayout.setMySelfUserId(mTRTCParams.userId);

        // 观众或者主播的控制面板
        mIvSwitchRole = (ImageView) findViewById(R.id.trtc_iv_switch_role);
        mIvSwitchRole.setOnClickListener(this);
        mSwitchCdnBtn.setOnClickListener(this);

        if (mTRTCParams.role == TRTCCloudDef.TRTCRoleAnchor) {
            mRoleAudienceGroup.setVisibility(View.VISIBLE);
            mIvSwitchRole.setVisibility(View.GONE);
            mSwitchCdnBtn.setVisibility(View.GONE);
        } else {
            mRoleAudienceGroup.setVisibility(View.GONE);
            mIvSwitchRole.setVisibility(View.VISIBLE);
            // 观众需要显示cdn按钮
            mSwitchCdnBtn.setVisibility(View.VISIBLE);
        }

        mIvSwitchCamera.setImageResource(mTRTCCloudManager.isFontCamera() ? R.drawable.trtc_ic_camera_front : R.drawable.trtc_ic_camera_back);
    }

    /**
     * 初始化 SDK
     */
    private void initTRTCSDK() {
        mTRTCCloud = TRTCCloud.sharedInstance(this);

        mTRTCCloudManager = new TRTCCloudManager(mTRTCCloud, mTRTCParams, mAppScene);
        mTRTCCloudManager.setViewListener(this);
        mTRTCCloudManager.setTRTCListener(this);
        mTRTCCloudManager.initTRTCManager(mIsCustomCaptureAndRender, mReceivedVideo, mReceivedAudio);
        mTRTCRemoteUserManager = new TRTCRemoteUserManager(mTRTCCloud, this, mIsCustomCaptureAndRender);
        mTRTCRemoteUserManager.setMixUserId(mTRTCParams.userId);
        mBgmManager = new TRTCBgmManager(mTRTCCloud, mTRTCParams);

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.trtc_ib_back) {
            finish();
        } else if (id == R.id.trtc_iv_switch_role) {
            switchRole();
        } else if (id == R.id.trtc_iv_mode) {
            int mode = mTRTCVideoLayout.switchMode();
            ((ImageView) v).setImageResource(mode == TRTCVideoLayoutManager.MODE_FLOAT ? R.drawable.ic_float : R.drawable.ic_gird);
        } else if (id == R.id.trtc_iv_beauty) {
            mTRTCBeautyPanel.setVisibility(mTRTCBeautyPanel.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        } else if (id == R.id.trtc_iv_camera) {
            mTRTCCloudManager.switchCamera();
            ((ImageView) v).setImageResource(mTRTCCloudManager.isFontCamera() ? R.drawable.trtc_ic_camera_front : R.drawable.trtc_ic_camera_back);
        } else if (id == R.id.trtc_iv_mic) {
            AudioConfig audioConfig = ConfigHelper.getInstance().getAudioConfig();
            audioConfig.setEnableAudio(!audioConfig.isEnableAudio());
            mTRTCCloudManager.muteLocalAudio(!audioConfig.isEnableAudio());
            ((ImageView) v).setImageResource(audioConfig.isEnableAudio() ? R.drawable.mic_enable : R.drawable.mic_disable);
        } else if (id == R.id.trtc_iv_log) {
            mLogLevel = (mLogLevel + 1) % 3;
            ((ImageView) v).setImageResource((0 == mLogLevel) ? R.drawable.log2 : R.drawable.log);
            mTRTCCloudManager.showDebugView(mLogLevel);
        } else if (id == R.id.trtc_iv_setting) {
            showDialogFragment(mFeatureSettingFragmentDialog, "FeatureSettingFragmentDialog");
        } else if (id == R.id.trtc_iv_more) {
            if (isCdnPlay) {
                if (mCdnPlayerSettingFragmentDialog == null) {
                    // cdn播放设置
                    mCdnPlayerSettingFragmentDialog = new CdnPlayerSettingFragmentDialog();
                    if (mCdnPlayManager == null) {
                        mCdnPlayManager = new CdnPlayManager(mCdnPlayView, this);
                    }
                    mCdnPlayerSettingFragmentDialog.setCdnPlayManager(mCdnPlayManager);
                }
                showDialogFragment(mCdnPlayerSettingFragmentDialog, "CdnPlayerSettingFragmentDialog");
            } else {
                showDialogFragment(mRemoteUserManagerFragmentDialog, "RemoteUserManagerFragmentDialog");
                if (mRemoteUserManagerFragmentDialog.isVisible()) {
                    mIvMoreTrtc.setImageResource(R.drawable.trtc_ic_member_show);
                } else {
                    mIvMoreTrtc.setImageResource(R.drawable.trtc_ic_member_dismiss);
                }
            }
        } else if (id == R.id.trtc_iv_music) {
            showDialogFragment(mBgmSettingFragmentDialog, "BgmSettingFragmentDialog");
        } else if (id == R.id.btn_switch_cdn) {
            toggleCdnPlay();
        } else if (id == R.id.call_btn_refuse_red_iv) {
            toastShow("视频通话结束");

//            Intent intent = new Intent("videocalltype");
//            intent.putExtra("videocalltype", "我挂断通话");
//            sendBroadcast(intent);

            Intent intentvideocalltype = new Intent(mActivity, ChatActivity.class);
            intentvideocalltype.putExtra(Utils.RECEIVEID, receiveid);
            intentvideocalltype.putExtra(Utils.CHAT_HEAD_URL, usertailBean.getHead());
            intentvideocalltype.putExtra(Utils.NICKNAME, usertailBean.getUserNickName());
            intentvideocalltype.putExtra(Utils.HEADIMGURL, usertailBean.getHead());
            intentvideocalltype.putExtra(Utils.ISPRIVATEFRIEND, true);
            intentvideocalltype.putExtra(Utils.VIDEOCALL_TYPE, "我挂断通话");
            startActivity(intentvideocalltype);
            finish();
        } else if (id == R.id.call_btn_small_iv) {

        }
    }

    @Override
    public boolean moveTaskToBack(boolean nonRoot) {
        return super.moveTaskToBack(nonRoot);
    }

    /**
     * 界面中触发cdn播放
     */
    private void toggleCdnPlay() {
        if (mCdnPlayManager == null) {
            mCdnPlayManager = new CdnPlayManager(mCdnPlayView, this);
        }
        if (isCdnPlay) {
            //cdn播放的情况下，需要切换成正常模式
            isCdnPlay = false;
            mTRTCVideoLayout.setVisibility(View.VISIBLE);
            mCdnPlayViewGroup.setVisibility(View.GONE);
            mCdnPlayManager.stopPlay();
            enterRoom();
            mSwitchCdnBtn.setText("切换CDN播放");
            mIvMoreTrtc.setImageResource(R.drawable.trtc_ic_member_dismiss);
        } else {
            isCdnPlay = true;
            exitRoom();
            mCdnPlayViewGroup.setVisibility(View.VISIBLE);
            mTRTCVideoLayout.setVisibility(View.GONE);
            mCdnPlayManager.initPlayUrl(mTRTCParams.roomId, mMainUserId);
            mCdnPlayManager.startPlay();
            mSwitchCdnBtn.setText("切换UDP播放");
            mIvMoreTrtc.setImageResource(R.drawable.trtc_ic_setting);
        }
    }

    /**
     * 展示dialog界面
     */
    private void showDialogFragment(DialogFragment dialogFragment, String tag) {
        if (dialogFragment != null) {
            if (dialogFragment.isVisible()) {
                try {
                    dialogFragment.dismissAllowingStateLoss();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                dialogFragment.show(getSupportFragmentManager(), tag);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        exitRoom();
        mTRTCCloudManager.destroy();
        mTRTCRemoteUserManager.destroy();
        if (mCdnPlayManager != null) {
            mCdnPlayManager.destroy();
        }
        mBgmManager.destroy();
        TRTCCloud.destroySharedInstance();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }


    }

    WindowManager mWindowManager;

    /**
     * 切换角色
     */
    private void switchRole() {
        AudioConfig audioConfig = ConfigHelper.getInstance().getAudioConfig();
        VideoConfig videoConfig = ConfigHelper.getInstance().getVideoConfig();
        // 目标的切换角色
        int targetRole = mTRTCCloudManager.switchRole();
        // 如果当前角色是主播
        if (targetRole == TRTCCloudDef.TRTCRoleAnchor) {

            mIvSwitchRole.setImageResource(R.drawable.linkmic);
            mSwitchCdnBtn.setVisibility(View.GONE);
            mRoleAudienceGroup.setVisibility(View.VISIBLE);
            // cdn播放中，需要先停止cdn播放，cdn播放里面已经包含了进房的逻辑，所以不用再执行startLocalPreview
            if (isCdnPlay) {
                toggleCdnPlay();
                mCdnPlayViewGroup.setVisibility(View.GONE);
                mIvMoreTrtc.setImageResource(R.drawable.trtc_ic_member_dismiss);
            } else {
                // 开启本地预览
                startLocalPreview();
                videoConfig.setEnableVideo(true);
                // 开启本地声音
                mTRTCCloudManager.startLocalAudio();
                audioConfig.setEnableAudio(true);
            }
        } else {
            // 关闭本地预览
            stopLocalPreview();
            videoConfig.setEnableVideo(false);
            // 关闭音频采集
            mTRTCCloudManager.stopLocalAudio();
            audioConfig.setEnableAudio(false);
            mIvSwitchRole.setImageResource(R.drawable.linkmic2);
            mSwitchCdnBtn.setVisibility(View.VISIBLE);
            mRoleAudienceGroup.setVisibility(View.GONE);
        }
        mIvSwitchCamera.setImageResource(mTRTCCloudManager.isFontCamera() ? R.drawable.trtc_ic_camera_front : R.drawable.trtc_ic_camera_back);
        mIvEnableAudio.setImageResource(audioConfig.isEnableAudio() ? R.drawable.mic_enable : R.drawable.mic_disable);
    }

    private void startLocalPreview() {
        TXCloudVideoView localVideoView = mTRTCVideoLayout.allocCloudVideoView(mTRTCParams.userId, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
        if (!mIsCustomCaptureAndRender) {
            // 开启本地预览
            mTRTCCloudManager.setLocalPreviewView(localVideoView);
            mTRTCCloudManager.startLocalPreview();
        } else {
            if (mCustomCapture != null) {
                mCustomCapture.start();
            }
            // 设置 TRTC SDK 的状态为本地自定义渲染，视频格式为纹理
            mTRTCCloudManager.setLocalVideoRenderListener(mCustomRender);
            if (mCustomRender != null) {
                TextureView textureView = new TextureView(this);
                localVideoView.addVideoView(textureView);
                mCustomRender.start(textureView);
            }
        }
    }

    private void stopLocalPreview() {
        if (!mIsCustomCaptureAndRender) {
            // 关闭本地预览
            mTRTCCloudManager.stopLocalPreview();
        } else {
            if (mCustomCapture != null) {
                mCustomCapture.stop();
            }
            if (mCustomRender != null) {
                mCustomRender.stop();
            }
        }
        mTRTCVideoLayout.recyclerCloudViewView(mTRTCParams.userId, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
    }

    /**
     * 显示连麦loading
     */
    private void startLinkMicLoading() {
        FrameLayout layout = (FrameLayout) findViewById(R.id.trtc_fl_link_loading);
        layout.setVisibility(View.VISIBLE);

        ImageView imageView = (ImageView) findViewById(R.id.trtc_iv_link_loading);
        imageView.setImageResource(R.drawable.trtc_linkmic_loading);
        AnimationDrawable animation = (AnimationDrawable) imageView.getDrawable();
        if (animation != null) {
            animation.start();
        }
    }

    /**
     * 隐藏连麦loading
     */
    private void stopLinkMicLoading() {
        FrameLayout layout = (FrameLayout) findViewById(R.id.trtc_fl_link_loading);
        layout.setVisibility(View.GONE);

        ImageView imageView = (ImageView) findViewById(R.id.trtc_iv_link_loading);
        AnimationDrawable animation = (AnimationDrawable) imageView.getDrawable();
        if (animation != null) {
            animation.stop();
        }
    }


    private void onVideoChange(String userId, int streamType, boolean available) {
        if (available) {
            // 首先需要在界面中分配对应的TXCloudVideoView
            TXCloudVideoView renderView = mTRTCVideoLayout.findCloudViewView(userId, streamType);
            if (renderView == null) {
                renderView = mTRTCVideoLayout.allocCloudVideoView(userId, streamType);
            }
            // 启动远程画面的解码和显示逻辑
            if (renderView != null) {
                mTRTCRemoteUserManager.remoteUserVideoAvailable(userId, streamType, renderView);
            }
            if (!userId.equals(mMainUserId)) {
                mMainUserId = userId;
            }
        } else {
            mTRTCRemoteUserManager.remoteUserVideoUnavailable(userId, streamType);
            if (streamType == TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB) {
                // 辅路直接移除画面，不会更新状态。主流需要更新状态，所以保留
                mTRTCVideoLayout.recyclerCloudViewView(userId, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB);
            }
        }
        if (streamType == TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG) {
            // 根据当前视频流的状态，展示相关的 UI 逻辑。
            mTRTCVideoLayout.updateVideoStatus(userId, available);
        }
        mTRTCRemoteUserManager.updateCloudMixtureParams();
    }

    /**
     * 加入房间回调
     *
     * @param elapsed 加入房间耗时，单位毫秒
     */
    @Override
    public void onEnterRoom(long elapsed) {
        if (elapsed >= 0) {
            //    Toast.makeText(this, "加入房间成功，耗时 " + elapsed + " 毫秒", Toast.LENGTH_SHORT).show();
            // 发起云端混流
            mTRTCRemoteUserManager.updateCloudMixtureParams();
        } else {
            Toast.makeText(this, "加入房间失败", Toast.LENGTH_SHORT).show();
            exitRoom();
        }
    }

    /**
     * ERROR 大多是不可恢复的错误，需要通过 UI 提示用户
     * 然后执行退房操作
     *
     * @param errCode   错误码 TXLiteAVError
     * @param errMsg    错误信息
     * @param extraInfo 扩展信息字段，个别错误码可能会带额外的信息帮助定位问题
     */
    @Override
    public void onError(int errCode, String errMsg, Bundle extraInfo) {
        Toast.makeText(this, "onError: " + errMsg + "[" + errCode + "]", Toast.LENGTH_SHORT).show();
        // 执行退房
        exitRoom();
        finish();
    }


    @Override
    public void onUserEnter(String userId) {
    }

    /**
     * 主播{@link TRTCCloudDef#TRTCRoleAnchor}离开了当前视频房间
     * 主播离开房间，要释放相关资源。
     * 1. 释放主画面、辅路画面
     * 2. 如果您有混流的需求，还需要重新发起混流，保证混流的布局是您所期待的。
     *
     * @param userId 用户标识
     * @param reason 离开原因代码，区分用户是正常离开，还是由于网络断线等原因离开。
     */
    @Override
    public void onUserExit(String userId, int reason) {
        mTRTCRemoteUserManager.removeRemoteUser(userId);
        // 回收分配的渲染的View
        mTRTCVideoLayout.recyclerCloudViewView(userId, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
        mTRTCVideoLayout.recyclerCloudViewView(userId, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB);
        // 更新混流参数
        mTRTCRemoteUserManager.updateCloudMixtureParams();
    }

    /**
     * 若当对应 userId 的主播有上行的视频流的时候，该方法会被回调，available 为 true；
     * 若对应的主播通过{@link TRTCCloud#muteLocalVideo(boolean)}，该方法也会被回调，available 为 false。
     * Demo 在收到主播有上行流的时候，会通过{@link TRTCCloud#startRemoteView(String, TXCloudVideoView)} 开始渲染
     * Demo 在收到主播停止上行的时候，会通过{@link TRTCCloud#stopRemoteView(String)} 停止渲染，并且更新相关 UI
     *
     * @param userId    用户标识
     * @param available 画面是否开启
     */
    @Override
    public void onUserVideoAvailable(String userId, boolean available) {
        onVideoChange(userId, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG, available);
    }


    /**
     * 是否有辅路上行的回调，Demo 中处理方式和主画面的一致 {@li ean)}
     *
     * @param userId    用户标识
     * @param available 屏幕分享是否开启
     */
    @Override
    public void onUserSubStreamAvailable(final String userId, boolean available) {
        onVideoChange(userId, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SUB, available);
    }

    /**
     * 是否有音频上行的回调
     * <p>
     * 您可以根据您的项目要求，设置相关的 UI 逻辑，比如显示对端闭麦的图标等
     *
     * @param userId    用户标识
     * @param available true：音频可播放，false：音频被关闭
     */
    @Override
    public void onUserAudioAvailable(String userId, boolean available) {
    }

    /**
     * 视频首帧渲染回调
     * <p>
     * 一般客户可不关注，专业级客户质量统计等；您可以根据您的项目情况决定是否进行统计或实现其他功能。
     *
     * @param userId     用户 ID
     * @param streamType 视频流类型
     * @param width      画面宽度
     * @param height     画面高度
     */
    @Override
    public void onFirstVideoFrame(String userId, int streamType, int width, int height) {
        Log.i(TAG, "onFirstVideoFrame: userId = " + userId + " streamType = " + streamType + " width = " + width + " height = " + height);
    }

    /**
     * 音量大小回调
     * <p>
     * 您可以用来在 UI 上显示当前用户的声音大小，提高用户体验
     *
     * @param userVolumes 所有正在说话的房间成员的音量（取值范围0 - 100）。即 userVolumes 内仅包含音量不为0（正在说话）的用户音量信息。其中本地进房 userId 对应的音量，表示 local 的音量，也就是自己的音量。
     * @param totalVolume 所有远端成员的总音量, 取值范围 [0, 100]
     */
    @Override
    public void onUserVoiceVolume(ArrayList<TRTCCloudDef.TRTCVolumeInfo> userVolumes, int totalVolume) {
        for (int i = 0; i < userVolumes.size(); ++i) {
            mTRTCVideoLayout.updateAudioVolume(userVolumes.get(i).userId, userVolumes.get(i).volume);
        }
    }

    /**
     * SDK 状态数据回调
     * <p>
     * 一般客户无需关注，专业级客户可以用来进行统计相关的性能指标；您可以根据您的项目情况是否实现统计等功能
     *
     * @param statics 状态数据
     */
    @Override
    public void onStatistics(TRTCStatistics statics) {
    }

    /**
     * 跨房连麦会结果回调
     *
     * @param userID
     * @param err
     * @param errMsg
     */
    @Override
    public void onConnectOtherRoom(final String userID, final int err, final String errMsg) {
        PkConfig pkConfig = ConfigHelper.getInstance().getPkConfig();
        stopLinkMicLoading();
        if (err == 0) {
            pkConfig.setConnected(true);
            Toast.makeText(this, "跨房连麦成功", Toast.LENGTH_LONG).show();
        } else {
            pkConfig.setConnected(false);

            Toast.makeText(this, "跨房连麦失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 断开跨房连麦结果回调
     *
     * @param err
     * @param errMsg
     */
    @Override
    public void onDisConnectOtherRoom(final int err, final String errMsg) {
        PkConfig pkConfig = ConfigHelper.getInstance().getPkConfig();
        pkConfig.reset();
    }

    /**
     * 网络行质量回调
     * <p>
     * 您可以用来在 UI 上显示当前用户的网络质量，提高用户体验
     *
     * @param localQuality  上行网络质量
     * @param remoteQuality 下行网络质量
     */
    @Override
    public void onNetworkQuality(TRTCCloudDef.TRTCQuality localQuality, ArrayList<TRTCCloudDef.TRTCQuality> remoteQuality) {
        mTRTCVideoLayout.updateNetworkQuality(localQuality.userId, localQuality.quality);
        for (TRTCCloudDef.TRTCQuality qualityInfo : remoteQuality) {
            mTRTCVideoLayout.updateNetworkQuality(qualityInfo.userId, qualityInfo.quality);
        }
    }

    /**
     * 音效播放回调
     *
     * @param effectId
     * @param code     0：表示播放正常结束；其他为异常结束，暂无异常值
     */
    @Override
    public void onAudioEffectFinished(int effectId, int code) {
        Toast.makeText(this, "effect id = " + effectId + " 播放结束" + " code = " + code, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecvCustomCmdMsg(String userId, int cmdID, int seq, byte[] message) {
        String msg = "";
        if (message != null && message.length > 0) {
            try {
                msg = new String(message, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ToastUtils.showLong("收到" + userId + "的消息：" + msg);
        }
    }

    @Override
    public void onRecvSEIMsg(String userId, byte[] data) {
        String msg = "";
        if (data != null && data.length > 0) {
            try {
                msg = new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ToastUtils.showLong("收到" + userId + "的消息：" + msg);
        }
    }

    @Override
    public void onAudioVolumeEvaluationChange(boolean enable) {
        if (enable) {
            mTRTCVideoLayout.showAllAudioVolumeProgressBar();
        } else {
            mTRTCVideoLayout.hideAllAudioVolumeProgressBar();
        }
    }

    @Override
    public void onStartLinkMic() {
        startLinkMicLoading();
    }

    @Override
    public void onMuteLocalVideo(boolean isMute) {
        mTRTCVideoLayout.updateVideoStatus(mTRTCParams.userId, !isMute);
    }

    @Override
    public void onMuteLocalAudio(boolean isMute) {
        mIvEnableAudio.setImageResource(!isMute ? R.drawable.mic_enable : R.drawable.mic_disable);
    }

    @Override
    public TXCloudVideoView getRemoteUserViewById(String userId, int steamType) {
        TXCloudVideoView view = mTRTCVideoLayout.findCloudViewView(userId, steamType);
        if (view == null) {
            view = mTRTCVideoLayout.allocCloudVideoView(userId, steamType);
        }
        return view;
    }

    @Override
    public void onRemoteViewStatusUpdate(String userId, boolean enableVideo) {
        mTRTCVideoLayout.updateVideoStatus(userId, enableVideo);
    }

    @Override
    public void onPlayEvent(int event, Bundle param) {

    }

    @Override
    public void onNetStatus(Bundle status) {

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "hangup":
                    Intent intentvideocalltype = new Intent(mActivity, ChatActivity.class);
                    intentvideocalltype.setAction("videocalltype");
                    intentvideocalltype.setAction("Hangupcall");
                    intentvideocalltype.putExtra(Utils.RECEIVEID, receiveid);
                    intentvideocalltype.putExtra(Utils.CHAT_HEAD_URL, intent.getStringExtra(Utils.CHAT_HEAD_URL));
                    intentvideocalltype.putExtra(Utils.NICKNAME, intent.getStringExtra(Utils.NICKNAME));
                    intentvideocalltype.putExtra(Utils.ISPRIVATEFRIEND, true);
                    intentvideocalltype.putExtra(Utils.VIDEOCALL_TYPE, "对方挂断通话");
                    startActivity(intentvideocalltype);
                    finish();
                    List<Message> list = new ArrayList<>();

                    if (SPUtils.getInstance(mActivity).getString(Utils.APPID + intent.getStringExtra(Utils.USERID)) != null) {
                        if (SPUtils.getInstance(mActivity).getString(Utils.APPID + intent.getStringExtra(Utils.USERID)).trim().length() > 0) {
                            list.addAll(JSON.parseArray(SPUtils.getInstance(mActivity).getString(Utils.APPID + intent.getStringExtra(Utils.USERID)), Message.class));

                        }
                    }

                    Intent intentvideoca = new Intent("Hangupcall");
                    intentvideoca.setAction("Hangupcall");
                    intentvideoca.putExtra("Hangupcall", "挂断通话");
                    intentvideoca.putExtra(Utils.RECEIVEID, intent.getStringExtra(Utils.USERID));
                    intentvideoca.putExtra(Utils.CHAT_HEAD_URL, intent.getStringExtra(Utils.CHAT_HEAD_URL));
                    intentvideoca.putExtra(Utils.NICKNAME, intent.getStringExtra(Utils.NICKNAME));
                    intentvideoca.putExtra(Utils.ISPRIVATEFRIEND, true);
                    sendBroadcast(intentvideoca);

                    Message mMessgaeImage = getBaseReceiveMessage(MsgType.SENDVIDEOCALL);
                    MsgBody mImageMsgBody = new MsgBody();
                    mImageMsgBody.setMessage("结束通话");
                    mImageMsgBody.setReceive_head_url(intent.getStringExtra(Utils.CHAT_HEAD_URL));
                    mImageMsgBody.setCount(mImageMsgBody.getCount() + 1);
                    mImageMsgBody.setThumbUrl(intent.getStringExtra(Utils.CHAT_HEAD_URL));
                    mMessgaeImage.setSenderId(intent.getStringExtra(Utils.USERID));
                    mMessgaeImage.setBody(mImageMsgBody);
                    list.add(mMessgaeImage);
                    SPUtils.getInstance(mActivity).put(Utils.APPID + intent.getStringExtra(Utils.USERID), JSON.toJSONString(list));

                    break;
            }
        }
    };

    private Message getBaseReceiveMessage(MsgType msgType) {
        Message mMessgae = new Message();
        mMessgae.setUuid(Utils.getMsgid() + "");
        mMessgae.setSenderId(mTargetId);
        mMessgae.setTargetId(mSenderId);
        mMessgae.setSentTime(System.currentTimeMillis());
        mMessgae.setSentStatus(MsgSendStatus.SENDING);
        mMessgae.setMsgType(msgType);
        return mMessgae;
    }

    @Override
    public void TRTCVideoRoomSuccess(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.getString("code").equals("200")) {
                usertailBean = JSON.parseObject(s, UsertailBean.class).getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
