package com.jfkj.im.videoPlay;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.jfkj.im.R;
import com.jfkj.im.utils.ToastUtils;
import com.nc.player.player.VideoView;
import com.nc.player.util.PlayerUtils;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <pre>
 * Description:  直播点播控制器
 * @author :   ys
 * @date :         2020/1/3
 * </pre>
 */
public class StandardVideoController extends GestureVideoController implements View.OnClickListener {

    protected ImageView mLockButton;

    protected ProgressBar mLoadingProgress;
    private String TAG = "VIDEOPLAYER";

    public StandardVideoController(@NonNull Context context) {
        this(context, null);
    }

    public StandardVideoController(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StandardVideoController(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.player_layout_standard_controller;
    }

    @Override
    protected void initView() {
        super.initView();
        mLockButton = findViewById(R.id.lock);
        mLockButton.setOnClickListener(this);
        mLoadingProgress = findViewById(R.id.loading);
    }

    /**
     * 快速添加各个组件
     * @param title  标题
     * @param isLive 是否为直播
     */
    public void addDefaultControlComponent(String title, boolean isLive) {
        CompleteView completeView = new CompleteView(getContext());
        ErrorView errorView = new ErrorView(getContext());
        PrepareView prepareView = new PrepareView(getContext());
        prepareView.setClickStart();
//        TitleView titleView = new TitleView(getContext());
//        titleView.setTitle(title);
//        addControlComponent(completeView, errorView, prepareView, titleView);
        addControlComponent(completeView, errorView, prepareView);
        if (isLive) {
//            addControlComponent(new LiveControlView(getContext()));
        } else {
            addControlComponent(new VodControlView(getContext()));
        }
        addControlComponent(new GestureView(getContext()));
        setCanChangePosition(!isLive);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.lock) {
            mControlWrapper.toggleLockState();
        }
    }

    @Override
    protected void onLockStateChanged(boolean isLocked) {
        if (isLocked) {
            mLockButton.setSelected(true);
            ToastUtils.showShortToast("已锁定");
        } else {
            mLockButton.setSelected(false);
            ToastUtils.showShortToast("已解锁");
        }
    }

    @Override
    protected void onVisibilityChanged(boolean isVisible, Animation anim) {
        if (mControlWrapper.isFullScreen()) {
            if (isVisible) {
                if (mLockButton.getVisibility() == GONE) {
                    mLockButton.setVisibility(VISIBLE);
                    if (anim != null) {
                        mLockButton.startAnimation(anim);
                    }
                }
            } else {
                mLockButton.setVisibility(GONE);
                if (anim != null) {
                    mLockButton.startAnimation(anim);
                }
            }
        }
    }

    @Override
    protected void onPlayerStateChanged(int playerState) {
        super.onPlayerStateChanged(playerState);
        switch (playerState) {
            case VideoView.PLAYER_NORMAL:
                Log.e(TAG,"PLAYER_NORMAL");
                setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                mLockButton.setVisibility(GONE);
                break;
            case VideoView.PLAYER_FULL_SCREEN:
                Log.e(TAG,"PLAYER_FULL_SCREEN");
                if (isShowing()) {
                    mLockButton.setVisibility(VISIBLE);
                } else {
                    mLockButton.setVisibility(GONE);
                }
                break;
        }

        if (mActivity != null && hasCutout()) {
            int orientation = mActivity.getRequestedOrientation();
            int dp24 = PlayerUtils.dp2px(getContext(), 24);
            int cutoutHeight = getCutoutHeight();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                FrameLayout.LayoutParams lblp = (LayoutParams) mLockButton.getLayoutParams();
                lblp.setMargins(dp24, 0, dp24, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                FrameLayout.LayoutParams layoutParams = (LayoutParams) mLockButton.getLayoutParams();
                layoutParams.setMargins(dp24 + cutoutHeight, 0, dp24 + cutoutHeight, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                FrameLayout.LayoutParams layoutParams = (LayoutParams) mLockButton.getLayoutParams();
                layoutParams.setMargins(dp24, 0, dp24, 0);
            }
        }

    }

    @Override
    protected void onPlayStateChanged(int playState) {
        super.onPlayStateChanged(playState);
        switch (playState) {
            //调用release方法会回到此状态
            case VideoView.STATE_IDLE:
                Log.e(TAG,"STATE_IDLE");
                mLockButton.setSelected(false);
                mLoadingProgress.setVisibility(GONE);
                break;
            case VideoView.STATE_PLAYING:
                Log.e(TAG,"STATE_PLAYING");
                mLoadingProgress.setVisibility(GONE);
                break;
            case VideoView.STATE_PAUSED:
                Log.e(TAG,"STATE_PAUSED");
                mLoadingProgress.setVisibility(GONE);
                break;
            case VideoView.STATE_PREPARING:
                Log.e(TAG,"STATE_PREPARING");
                mLoadingProgress.setVisibility(VISIBLE);
                break;
            case VideoView.STATE_PREPARED:
                Log.e(TAG,"STATE_PREPARED");
                mLoadingProgress.setVisibility(GONE);
                break;
            case VideoView.STATE_ERROR:
                Log.e(TAG,"STATE_ERROR");
                mLoadingProgress.setVisibility(GONE);
                break;
            case VideoView.STATE_BUFFERING:
                Log.e(TAG,"STATE_BUFFERING");
                mLoadingProgress.setVisibility(VISIBLE);
                break;
            case VideoView.STATE_BUFFERED:
                Log.e(TAG,"STATE_BUFFERED");
                mLoadingProgress.setVisibility(GONE);
                break;
            case VideoView.STATE_PLAYBACK_COMPLETED:
                Log.e(TAG,"STATE_PLAYBACK_COMPLETED");
                mLoadingProgress.setVisibility(GONE);
                mLockButton.setVisibility(GONE);
                mLockButton.setSelected(false);
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        if (isLocked()) {
            show();
            ToastUtils.showShortToast("请先解锁屏幕");
            return true;
        }
        if (mControlWrapper.isFullScreen()) {
            return stopFullScreen();
        }
        return super.onBackPressed();
    }
}
