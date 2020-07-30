package com.jfkj.im.TIM.component.video;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jfkj.im.R;
import com.jfkj.im.TIM.component.video.proxy.IPlayer;
import com.jfkj.im.TIM.utils.ImageUtil;
import com.jfkj.im.TIM.utils.ScreenUtil;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.TIM.utils.TUIKitLog;
import com.jfkj.im.utils.ChangeUtils;
import com.jfkj.im.utils.cache.PreloadManager;
import com.jfkj.im.videoPlay.TikTokController;
import com.jfkj.im.videoPlay.TikTokView;
import com.nc.player.media.AndroidMediaController;
import com.nc.player.media.IjkVideoView;
import com.nc.player.media.MeasureHelper;
import com.nc.player.media.Settings;
import com.nc.player.player.VideoView;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class VideoViewActivity extends AppCompatActivity {

    /**old*/
    private static final String TAG = VideoViewActivity.class.getSimpleName();

//    private UIKitVideoView mVideoView;
//    private int videoWidth = 0;
//    private int videoHeight = 0;
    /**视频地址*/
    Intent getIntent;
    /**new*/
   //视频部分
    private VideoView mVideoView;
    private TikTokController mController;
    private Uri videoUri;
    private TikTokView tiktok_view;

    private IjkVideoView video_view;
    private AndroidMediaController mMediaController;
    private TextView mToastTextView;
    private TableLayout mHudView;
    private Settings mSettings;
    private boolean mBackPressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TUIKitLog.i(TAG, "onCreate start");
        super.onCreate(savedInstanceState);
        //去除标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_view);
        getIntent = getIntent();
        String imagePath = getIntent.getStringExtra(TUIKitConstants.CAMERA_IMAGE_PATH);
        videoUri = getIntent.getParcelableExtra(TUIKitConstants.CAMERA_VIDEO_PATH);
        //old
//        mVideoView = findViewById(R.id.video_play_view);
//        getIntent = getIntent();
//        Bitmap firstFrame = ImageUtil.getBitmapFormPath(imagePath);
//        if (firstFrame != null) {
//            videoWidth = firstFrame.getWidth();
//            videoHeight = firstFrame.getHeight();
//            updateVideoView();
//        }
//        mVideoView.setVideoURI(videoUri);
//        mVideoView.setOnPreparedListener(new IPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(IPlayer mediaPlayer) {
//                mVideoView.start();
//            }
//        });
//        mVideoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mVideoView.isPlaying()) {
//                    mVideoView.pause();
//                } else {
//                    mVideoView.start();
//                }
//            }
//        });
//
//        findViewById(R.id.video_view_back).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mVideoView.stop();
//                finish();
//            }
//        });
//        TUIKitLog.i(TAG, "onCreate end");


        /**new*/
        init();
    }

    private void init() {
//        tiktok_view = findViewById(R.id.tiktok_view);
//        mVideoView = new VideoView(this);
//        mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_DEFAULT);
//        mVideoView.setLooping(true);
//        mController = new TikTokController(this);
//        mVideoView.setVideoController(mController);
//        startPlay();


        video_view = findViewById(R.id.video_view);

        mSettings = new Settings(this);

        // init UI
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        mMediaController = new AndroidMediaController(this, false);
        mMediaController.setSupportActionBar(actionBar);

        mToastTextView = findViewById(R.id.toast_text_view);
        mHudView = findViewById(R.id.hud_view);

        video_view.setMediaController(mMediaController);
        video_view.setHudView(mHudView);

        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        video_view.setVideoURI(videoUri);
        video_view.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_toggle_ratio) {
            int aspectRatio = video_view.toggleAspectRatio();
            String aspectRatioText = MeasureHelper.getAspectRatioText(this, aspectRatio);
            mToastTextView.setText(aspectRatioText);
            mMediaController.showOnce(mToastTextView);
            return true;
        } else if (id == R.id.action_toggle_player) {
            int player = video_view.togglePlayer();
            String playerText = IjkVideoView.getPlayerText(this, player);
            mToastTextView.setText(playerText);
            mMediaController.showOnce(mToastTextView);
            return true;
        } else if (id == R.id.action_toggle_render) {
            int render = video_view.toggleRender();
            String renderText = IjkVideoView.getRenderText(this, render);
            mToastTextView.setText(renderText);
            mMediaController.showOnce(mToastTextView);
            return true;
        } else if (id == R.id.action_show_info) {
            video_view.showMediaInfo();
        }

        return super.onOptionsItemSelected(item);
    }

    private void startPlay() {
        mVideoView.release();
        String url = ChangeUtils.getRealFilePath(this,videoUri);
        if (!TextUtils.isEmpty(url)){
            PreloadManager.getInstance(this).addPreloadTask(url, 0);
            String playUrl = PreloadManager.getInstance(this).getPlayUrl(url);
            mVideoView.setUrl(playUrl);
            mController.addControlComponent(tiktok_view, true);
            mVideoView.start();
        }
    }


    /**old*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        TUIKitLog.i(TAG, "onConfigurationChanged start");
        super.onConfigurationChanged(newConfig);
//        updateVideoView();
        TUIKitLog.i(TAG, "onConfigurationChanged end");
    }

    private void updateVideoView() {
//        TUIKitLog.i(TAG, "updateVideoView videoWidth: " + videoWidth + " videoHeight: " + videoHeight);
//        if (videoWidth <= 0 && videoHeight <= 0) {
//            return;
//        }
//        boolean isLandscape = true;
//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            isLandscape = false;
//        }
//
//        int deviceWidth;
//        int deviceHeight;
//        if (isLandscape) {
//            deviceWidth = Math.max(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this));
//            deviceHeight = Math.min(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this));
//        } else {
//            deviceWidth = Math.min(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this));
//            deviceHeight = Math.max(ScreenUtil.getScreenWidth(this), ScreenUtil.getScreenHeight(this));
//        }
//        int[] scaledSize = ScreenUtil.scaledSize(deviceWidth, deviceHeight, videoWidth, videoHeight);
//        TUIKitLog.i(TAG, "scaled width: " + scaledSize[0] + " height: " + scaledSize[1]);
//        ViewGroup.LayoutParams params = mVideoView.getLayoutParams();
//        params.width = scaledSize[0];
//        params.height = scaledSize[1];
//        mVideoView.setLayoutParams(params);
    }

    @Override
    protected void onStop() {
        TUIKitLog.i(TAG, "onStop");
        super.onStop();
        /**old*/
//        if (mVideoView != null) {
//            mVideoView.stop();
//        }

        /**new*/
        if (mBackPressed || !video_view.isBackgroundPlayEnabled()) {
            video_view.stopPlayback();
            video_view.release(true);
            video_view.stopBackgroundPlay();
        } else {
            video_view.enterBackground();
        }
        IjkMediaPlayer.native_profileEnd();
    }

    @Override
    public void onBackPressed() {
        mBackPressed = true;

        super.onBackPressed();
    }
}
