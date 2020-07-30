package com.jfkj.im.view;


import android.content.Context;

import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;


import androidx.annotation.Nullable;

import com.jfkj.im.R;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloudDef;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Module: TRTCVideoLayout
 * <p>
 * Function:
 * <p>
 * 此 TRTCVideoLayout 封装了{@link TXCloudVideoView} 以及业务逻辑 UI 控件
 * 作用：
 * 1. 实现了手势监听，配合 { TRTCVideoLayoutManager} 能够实现自由拖动 View。
 * 详情可见：{@link TRTCVideoViewLayout#initGestureListener()}
 * 实现原理：利用 RelativeLayout 的 margin 实现了能够在父容器自由定位的特性；需要注意，{@link TRTCVideoViewLayout} 不能增加约束规则，如 alignParentRight 等，否则无法自由定位。
 * <p>
 * 2. 对{@link TXCloudVideoView} 与逻辑 UI 进行组合，在 muteLocal、音量回调等情况，能够进行 UI 相关的变化。若您的项目中，也相关的业务逻辑，可以参照 Demo 的相关实现。
 */
public  class TRTCVideoViewLayout extends RelativeLayout implements View.OnClickListener {
    public  WeakReference<IVideoLayoutListener> mWefListener;
    private TXCloudVideoView mVideoView;
    private OnClickListener  mClickListener;
    private GestureDetector  mSimpleOnGestureListener;
    private ProgressBar      mPbAudioVolume;
    private LinearLayout     mLlController;
    private Button           mBtnMuteVideo, mBtnMuteAudio, mBtnFill;
    private FrameLayout                         mLlNoVideo;
    private TextView                            mTvContent;
    private ImageView                           mIvNoS;
    private ViewGroup                           mVgFuc;
    private HashMap<Integer, Integer>           mNoSMap      = null;
    private boolean                             mMoveable;
    private boolean                             mEnableFill  = false;
    private boolean                             mEnableAudio = true;
    private boolean                             mEnableVideo = true;


    public TRTCVideoViewLayout(Context context) {
        this(context, null);
    }

    public TRTCVideoViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFuncLayout();
        initGestureListener();
        initNoS();
    }

    public TXCloudVideoView getVideoView() {
        return mVideoView;
    }

    public void updateNetworkQuality(int quality) {
        if (quality < TRTCCloudDef.TRTC_QUALITY_Excellent) {
            quality = TRTCCloudDef.TRTC_QUALITY_Excellent;
        }
        if (quality > TRTCCloudDef.TRTC_QUALITY_Down) {
            quality = TRTCCloudDef.TRTC_QUALITY_Down;
        }

        if (mIvNoS != null) {
            mIvNoS.setImageResource(mNoSMap.get(Integer.valueOf(quality).intValue()));
        }
    }

    public void setBottomControllerVisibility(int visibility) {
        if (mLlController != null)
            mLlController.setVisibility(visibility);
    }

    public void updateNoVideoLayout(String text, int visibility) {
        if (mTvContent != null) {
            mTvContent.setText(text);
        }
        if (mLlNoVideo != null) {
            mLlNoVideo.setVisibility(visibility);
        }
    }

    public void setAudioVolumeProgress(int progress) {
        if (mPbAudioVolume != null) {
            mPbAudioVolume.setProgress(progress);
        }
    }

    public void setAudioVolumeProgressBarVisibility(int visibility) {
        if (mPbAudioVolume != null) {
            mPbAudioVolume.setVisibility(visibility);
        }
    }

    private void initNoS() {
        mNoSMap = new HashMap<>();
        mNoSMap.put(Integer.valueOf(TRTCCloudDef.TRTC_QUALITY_Down), Integer.valueOf(R.drawable.signal1));
        mNoSMap.put(Integer.valueOf(TRTCCloudDef.TRTC_QUALITY_Vbad), Integer.valueOf(R.drawable.signal2));
        mNoSMap.put(Integer.valueOf(TRTCCloudDef.TRTC_QUALITY_Bad), Integer.valueOf(R.drawable.signal3));
        mNoSMap.put(Integer.valueOf(TRTCCloudDef.TRTC_QUALITY_Poor), Integer.valueOf(R.drawable.signal4));
        mNoSMap.put(Integer.valueOf(TRTCCloudDef.TRTC_QUALITY_Good), Integer.valueOf(R.drawable.signal5));
        mNoSMap.put(Integer.valueOf(TRTCCloudDef.TRTC_QUALITY_Excellent), Integer.valueOf(R.drawable.signal6));
    }


    private void initFuncLayout() {
        mVgFuc = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_trtc_func, this, true);
        mVideoView = (TXCloudVideoView) mVgFuc.findViewById(R.id.trtc_tc_cloud_view);
        mPbAudioVolume = (ProgressBar) mVgFuc.findViewById(R.id.trtc_pb_audio);
        mLlController = (LinearLayout) mVgFuc.findViewById(R.id.trtc_ll_controller);
        mBtnMuteVideo = (Button) mVgFuc.findViewById(R.id.trtc_btn_mute_video);
        mBtnMuteVideo.setOnClickListener(this);
        mBtnMuteAudio = (Button) mVgFuc.findViewById(R.id.trtc_btn_mute_audio);
        mBtnMuteAudio.setOnClickListener(this);
        mBtnFill = (Button) mVgFuc.findViewById(R.id.trtc_btn_fill);
        mBtnFill.setOnClickListener(this);
        mLlNoVideo = (FrameLayout) mVgFuc.findViewById(R.id.trtc_fl_no_video);
        mTvContent = (TextView) mVgFuc.findViewById(R.id.trtc_tv_content);
        mIvNoS = (ImageView) mVgFuc.findViewById(R.id.trtc_iv_nos);
        ToggleButton muteBtn = (ToggleButton) mVgFuc.findViewById(R.id.mute_in_speaker);
        muteBtn.setOnClickListener(this);
    }


    private void initGestureListener() {
        mSimpleOnGestureListener = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mClickListener != null) {
                    mClickListener.onClick(TRTCVideoViewLayout.this);
                }
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (!mMoveable) return false;
                ViewGroup.LayoutParams params = TRTCVideoViewLayout.this.getLayoutParams();
                // 当 TRTCVideoView 的父容器是 RelativeLayout 的时候，可以实现拖动
                if (params instanceof LayoutParams) {
                    LayoutParams layoutParams = (LayoutParams) TRTCVideoViewLayout.this.getLayoutParams();
                    int          newX         = (int) (layoutParams.leftMargin + (e2.getX() - e1.getX()));
                    int          newY         = (int) (layoutParams.topMargin + (e2.getY() - e1.getY()));

                    layoutParams.leftMargin = newX;
                    layoutParams.topMargin = newY;

                    TRTCVideoViewLayout.this.setLayoutParams(layoutParams);
                }
                return true;
            }
        });
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mSimpleOnGestureListener.onTouchEvent(event);
            }
        });
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mClickListener = l;
    }


    public void setMoveable(boolean enable) {
        mMoveable = enable;
    }

    @Override
    public void onClick(View v) {
        IVideoLayoutListener listener = mWefListener != null ? mWefListener.get() : null;
        if (listener == null) return;
        int id = v.getId();
        if (id == R.id.trtc_btn_fill) {
            mEnableFill = !mEnableFill;
            if (mEnableFill) {
                v.setBackgroundResource(R.drawable.fill_scale);
            } else {
                v.setBackgroundResource(R.drawable.fill_adjust);
            }
            listener.onClickFill(this, mEnableFill);
        } else if (id == R.id.trtc_btn_mute_audio) {
            mEnableAudio = !mEnableAudio;
            if (mEnableAudio) {
                v.setBackgroundResource(R.drawable.remote_audio_enable);
            } else {
                v.setBackgroundResource(R.drawable.remote_audio_disable);
            }
            listener.onClickMuteAudio(this, !mEnableAudio);
        } else if (id == R.id.trtc_btn_mute_video) {
            mEnableVideo = !mEnableVideo;
            if (mEnableVideo) {
                v.setBackgroundResource(R.drawable.remote_video_enable);
            } else {
                v.setBackgroundResource(R.drawable.remote_video_disable);
            }
            listener.onClickMuteVideo(this, !mEnableVideo);
        } else if (id == R.id.mute_in_speaker) {
            listener.onClickMuteInSpeakerAudio(this, ((ToggleButton) v).isChecked());
        }
    }

    public void setIVideoLayoutListener(IVideoLayoutListener listener) {
        if (listener == null) {
            mWefListener = null;
        } else {
            mWefListener = new WeakReference<>(listener);
        }
    }

    public interface IVideoLayoutListener {
        void onClickFill(TRTCVideoViewLayout view, boolean enableFill);

        void onClickMuteAudio(TRTCVideoViewLayout view, boolean isMute);

        void onClickMuteVideo(TRTCVideoViewLayout view, boolean isMute);

        void onClickMuteInSpeakerAudio(TRTCVideoViewLayout view, boolean isMute);
    }
}
