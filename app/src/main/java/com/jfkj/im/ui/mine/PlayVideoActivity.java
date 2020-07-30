package com.jfkj.im.ui.mine;


import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.jfkj.im.R;
import com.jfkj.im.listener.CountDownTimeListener;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.utils.TimeCountUtil;
import com.jfkj.im.view.TopToBottomFinishLayout;
import com.lzy.okgo.utils.OkLogger;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

public class PlayVideoActivity extends BaseActivity implements View.OnClickListener, CountDownTimeListener {

    private static final String TAG = "PlayVideoActivity";
    @BindView(R.id.video_view)
    VideoView mVideoView;

    @BindView(R.id.iv_start_stop)
    ImageView ivStartStop;


    @BindView(R.id.seekBar)
    SeekBar seekBar;

    @BindView(R.id.tv_start_time)
    TextView tvStartTime;

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.iv_start_stop_center)
    ImageView iv_start_stop_center;

    @BindView(R.id.rl_bottom)
    RelativeLayout rl_bottom;

    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.layout)
    TopToBottomFinishLayout layout;
    private String url, type;
    private Uri uri;
    private  StringBuilder mFormatBuilder;
    private  Formatter mFormatter;
    private TimeCountUtil mCountUtil = null;
    private boolean mIsShow = false;
    private boolean mIsOnclick = false;
    private int width;
    private int height;
    private double scale;

    //拖动 seekBar
    private boolean isDrag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = getIntent().getStringExtra("type");

        if (type.equals("1")) {
            mVideoView.setVideoPath(getIntent().getStringExtra("video_url"));
        } else {
            uri = Uri.parse(getIntent().getStringExtra("video_url"));
            mVideoView.setVideoURI(uri);
        }
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        mVideoView.start();
//        mVideoView.setMediaController(new MediaController(this));

        mVideoView.requestFocus();//让VideoView获取焦点
        ivStartStop.setOnClickListener(this);
        seekBar.setMax(1000);
        seekBar.setEnabled(false);

        Glide.with(this).load(R.mipmap.play_goon_nol).into(ivStartStop);

        if (mCountUtil == null) {
            mCountUtil = new TimeCountUtil(5000, 1000, this);
        }

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                handler.postDelayed(runnable, 0);
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Glide.with(mActivity).load(R.mipmap.play_stop_nol).into(ivStartStop);
                iv_start_stop_center.setVisibility(View.VISIBLE);
            }
        });

        mVideoView.setOnClickListener(this);
        iv_start_stop_center.setOnClickListener(this);
        img_back.setOnClickListener(this);




        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();




        layout.setOnFinishListener(new TopToBottomFinishLayout.OnFinishListener() {
            @Override
            public void onFinish() {
                finish();
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser){
                    if(isDrag){
                        isDrag =false;

                        mVideoView.pause();

                        int duration = mVideoView.getDuration();

                        scale = duration * ((double)progress/1000);

                        tvStartTime.setText(stringForTime((int) scale));



//                        Log.e("msg","视频总长度 " + duration  + "\t\t  拖动进度" + scale);

//                        Log.e("msg","当前显示进度" + stringForTime((int) scale));

                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //开始拖动

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //拖动停止
                isDrag = true;
                int progress = seekBar.getProgress();
                int duration = mVideoView.getDuration();

                int scale = (int) (duration * ((double)progress/1000));


                //视频总长度  * 百分比
                //毫秒
                mVideoView.seekTo((int) scale);


                double seekto = scale / 1000;

                if (mVideoView != null && !mVideoView.isPlaying()) {
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, (long) (1000 - (scale % 1000)));
//                    mVideoView.start();
                    if (!mIsOnclick){
                        hide();
                    }
                } else {
                    show();
                }

            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_start_stop:
            case R.id.iv_start_stop_center:
                play();
                break;
            case R.id.video_view:
                showButton2();
                mIsOnclick = true;
                mCountUtil.start();
                if (mVideoView != null && mVideoView.isPlaying()){
                    if (mIsShow){
                        mIsShow = false;
                        hide();
                    } else  {
                        mIsShow = true;
                        show();
                    }
                } else  {
                    if (mIsShow){
                        mIsShow = false;
                        hide();
                    } else  {
                        mIsShow = true;
                        show();
                    }
                }

                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    private void showButton2() {
        if (mVideoView != null && mVideoView.isPlaying()){
            iv_start_stop_center.setVisibility(View.GONE);
        } else  {
            iv_start_stop_center.setVisibility(View.VISIBLE);
        }
    }




    protected void play() {
        if (mVideoView != null && mVideoView.isPlaying()){
            Glide.with(this).load(R.mipmap.play_stop_nol).into(ivStartStop);
            mVideoView.pause();
        }else {
            handler.postDelayed(runnable, 0);
            Glide.with(this).load(R.mipmap.play_goon_nol).into(ivStartStop);
            iv_start_stop_center.setVisibility(View.GONE);
            mVideoView.start();
        }
    }


    /***/
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            int pos = setProgresser();
            if (mVideoView != null && mVideoView.isPlaying()) {
                handler.postDelayed(runnable, 1000 - (pos % 1000));
                if (!mIsOnclick){
                    hide();
                }
            } else {
                show();
            }
        }
    };

    private void hide() {
        iv_start_stop_center.setVisibility(View.GONE);
        rl_bottom.setVisibility(View.GONE);
        img_back.setVisibility(View.GONE);
    }

    private void show() {
        if (mVideoView != null && mVideoView.isPlaying()){
            iv_start_stop_center.setVisibility(View.GONE);
        } else {
            iv_start_stop_center.setVisibility(View.VISIBLE);
        }

        rl_bottom.setVisibility(View.VISIBLE);
        img_back.setVisibility(View.VISIBLE);
    }



    protected String time(long millionSeconds) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millionSeconds);
        return simpleDateFormat.format(c.getTime());
    }

    /***/

    private int setProgresser() {
        if (mVideoView == null) {
            return 0;
        }
        int position = mVideoView.getCurrentPosition();
        int duration = mVideoView.getDuration();


        if (seekBar != null) {
            if (duration > 0) {
                long pos = 1000L * position / duration;
                seekBar.setProgress((int) pos);
            }
            int percent = mVideoView.getBufferPercentage();
            seekBar.setSecondaryProgress(percent * 10);

            if (tv_time != null)
                tv_time.setText(stringForTime(duration));
            if (tvStartTime != null)
                tvStartTime.setText(stringForTime(position));
        }
        return position;
    }


    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play_video;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView = null;
        }
        handler.removeCallbacks(runnable);
        if (mCountUtil != null) {
            mCountUtil.cancel();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mVideoView != null && mVideoView.isPlaying()){
            mVideoView.stopPlayback();
        }
    }

    @Override
    public void onTimeFinish() {
        mIsOnclick = false;
        mIsShow = false;
        hide();
    }

    @Override
    public void onTimeTick(long millisUntilFinished) {

    }





}
