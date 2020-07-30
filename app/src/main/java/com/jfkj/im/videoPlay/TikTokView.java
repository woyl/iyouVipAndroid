package com.jfkj.im.videoPlay;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jfkj.im.R;
import com.nc.player.controller.ControlWrapper;
import com.nc.player.controller.IControlComponent;
import com.nc.player.player.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * <pre>
 * Description:  视频显示控制页  首页播放暂停
 * @author :   ys
 * @date :         2020/1/2
 * </pre>
 */
public class TikTokView extends FrameLayout implements IControlComponent {

    private ControlWrapper mControlWrapper;
    private ImageView mIvThumb;
    private ImageView mIvPlay;
    private int scaledDoubleTapSlop;
    private int mStartX, mStartY;
    private ProgressBar progressBar;

    {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_tiktok, this, true);
        mIvThumb = findViewById(R.id.iv_thumb);
        mIvPlay = findViewById(R.id.iv_play);
        progressBar = findViewById(R.id.progress);
        progressBar.setMax(100);

        mIvPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setShow();
                    mControlWrapper.togglePlay();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    mControlWrapper.togglePlay();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        });

        scaledDoubleTapSlop = ViewConfiguration.get(getContext()).getScaledDoubleTapSlop();


    }

    public TikTokView(@NonNull Context context) {
        super(context);
    }

    public TikTokView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TikTokView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 解决点击和VerticalViewPager滑动冲突问题
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) event.getX();
                mStartY = (int) event.getY();
                return true;

            case MotionEvent.ACTION_UP:
                int endX = (int) event.getX();
                int endY = (int) event.getY();
                if (Math.abs(endX - mStartX) < scaledDoubleTapSlop
                        && Math.abs(endY - mStartY) < scaledDoubleTapSlop) {
                    performClick();
                }
                break;
        }

        return false;

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

    }

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            case VideoView.STATE_IDLE:
                Log.d("@@@", "STATE_IDLE " + hashCode());
                mIvThumb.setVisibility(VISIBLE);
                break;
            case VideoView.STATE_PLAYING:
                Log.d("@@@", "STATE_PLAYING " + hashCode());
                mIvThumb.setVisibility(GONE);
//                mIvPlay.setVisibility(GONE);
                break;
            case VideoView.STATE_PAUSED:
                Log.d("@@@", "STATE_PAUSED " + hashCode());
                mIvThumb.setVisibility(GONE);

                break;
            case VideoView.STATE_PREPARED:
                Log.d("@@@", "STATE_PREPARED " + hashCode());
                break;
            case VideoView.STATE_ERROR:
                Log.d("@@@", "STATE_ERROR " + hashCode());
                Toast.makeText(getContext(), R.string.player_error_message, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {
        setShow();
        new MyThread(this,mControlWrapper,progressBar).start();
    }

    private static class MyThread extends Thread{
        WeakReference<TikTokView> weakReference;
        private ControlWrapper mControlWrapper;
        private ProgressBar progressBar;

        public MyThread (TikTokView tikTokView,ControlWrapper mControlWrapper,ProgressBar progressBar) {
            weakReference = new WeakReference<TikTokView>(tikTokView);
            this.mControlWrapper = mControlWrapper;
            this.progressBar = progressBar;
        }

        @Override
        public void run() {
            super.run();
            while (true) {
                if (mControlWrapper != null && mControlWrapper.isPlaying()){
                    double one = mControlWrapper.getCurrentPosition();
                    double two =  mControlWrapper.getDuration();
                    int num = (int) (one/ two * 100);
                    progressBar.setProgress(num);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void setShow(){
        if (mControlWrapper != null && mControlWrapper.isPlaying()){
            Glide.with(getContext()).load(R.mipmap.icon_camera_stop).into(mIvPlay);
        } else {
            Glide.with(getContext()).load(R.mipmap.icon_camera).into(mIvPlay);
        }
    }

    public void setShow2(){
        Glide.with(getContext()).load(R.mipmap.icon_camera_stop).into(mIvPlay);
    }


    @Override
    public void setProgress(int duration, int position) {
    }

    @Override
    public void onLockStateChanged(boolean isLocked) {

    }
}
