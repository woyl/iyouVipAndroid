package com.jfkj.im.widget;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jfkj.im.R;
import com.nc.player.controller.ControlWrapper;
import com.nc.player.controller.IControlComponent;
import com.nc.player.player.VideoView;
import com.nc.player.util.PlayerUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <pre>
 * Description:  播放器顶部标题栏
 * @author :   ys
 * @date :         2020/1/11
 * </pre>
 */
public class TitleView extends FrameLayout implements IControlComponent {

    private ControlWrapper mControlWrapper;

    private LinearLayout mTitleContainer;
    private MarqueeTextView mTitle;
    private TextView mSysTime;//系统当前时间


    public TitleView(@NonNull Context context) {
        super(context);
    }

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setVisibility(GONE);
        LayoutInflater.from(getContext()).inflate(R.layout.dkplayer_layout_title_view, this, true);
        mTitleContainer = findViewById(R.id.title_container);
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = PlayerUtils.scanForActivity(getContext());
                if (activity != null && mControlWrapper.isFullScreen()) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mControlWrapper.stopFullScreen();
                }
            }
        });
        mTitle = findViewById(R.id.title);
        mSysTime = findViewById(R.id.sys_time);
        //电量
//        ImageView batteryLevel = findViewById(R.id.iv_battery);
//        mBatteryReceiver = new BatteryReceiver(batteryLevel);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void attach(@NonNull ControlWrapper controlWrapper) {
        mControlWrapper = controlWrapper;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onVisibilityChanged(boolean isVisible, Animation anim) {
        //只在全屏时才有效
        if (!mControlWrapper.isFullScreen()) return;
        if (isVisible) {
            if (getVisibility() == GONE) {
                mSysTime.setText(PlayerUtils.getCurrentSystemTime());
                setVisibility(VISIBLE);
                if (anim != null) {
                    startAnimation(anim);
                }
            }
        } else {
            if (getVisibility() == VISIBLE) {
                if (anim != null) {
                    setVisibility(GONE);
                    startAnimation(anim);
                }
            }
        }
    }

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            case VideoView.STATE_IDLE:
            case VideoView.STATE_START_ABORT:
            case VideoView.STATE_PREPARING:
            case VideoView.STATE_PREPARED:
            case VideoView.STATE_ERROR:
            case VideoView.STATE_PLAYBACK_COMPLETED:
                setVisibility(GONE);
                break;
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {
        if (playerState == VideoView.PLAYER_FULL_SCREEN) {
            if (mControlWrapper.isShowing()) {
                setVisibility(VISIBLE);
                mSysTime.setText(PlayerUtils.getCurrentSystemTime());
            }
            mTitle.setNeedFocus(true);
        } else {
            setVisibility(GONE);
            mTitle.setNeedFocus(false);
        }

        Activity activity = PlayerUtils.scanForActivity(getContext());
        if (activity != null && mControlWrapper.hasCutout()) {
            int orientation = activity.getRequestedOrientation();
            int cutoutHeight = mControlWrapper.getCutoutHeight();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                mTitleContainer.setPadding(0, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                mTitleContainer.setPadding(cutoutHeight, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                mTitleContainer.setPadding(0, 0, cutoutHeight, 0);
            }
        }
    }

    @Override
    public void setProgress(int duration, int position) {

    }

    @Override
    public void onLockStateChanged(boolean isLocked) {
        if (isLocked) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
    }

}
