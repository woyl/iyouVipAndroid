package com.jfkj.im.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.CustomVideoView;

import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class StartvideoActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    @BindView(R.id.videoview)
    VideoView videoview;
    @BindView(R.id.btn_start)
    Button btn_start;



//    @BindView(R.id.video_view)
//    VideoView videoView;

    private static String[] mRecordVideoPermission = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static final int RC_RECORD_VIDEO_PERMISSION = 10;
    Intent getIntent;

    SurfaceView mSvVideoPlayer;

    private MediaPlayer mMediaPlayer;
    private int mPosition = 0;
    private boolean hasActiveHolder = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(mActivity, false);
        btn_start.setOnClickListener(this);
        getIntent = getIntent();
        MediaController localMediaController = new MediaController(this);
        videoview.setMediaController(localMediaController);
        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/raw/video"));
        videoview.start();
        mSvVideoPlayer = findViewById(R.id.surfaceview);
        playVideo();
       // video();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 播放视频
     */
    public void playVideo() {
        if (mMediaPlayer == null) {
            //实例化MediaPlayer对象
            mMediaPlayer = new MediaPlayer();
            mSvVideoPlayer.setVisibility(View.VISIBLE);
            boolean mHardwareDecoder = false;
            // 不维持自身缓冲区，直接显示
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB && mHardwareDecoder) {
                mSvVideoPlayer.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            }
            mSvVideoPlayer.getHolder().setFixedSize(this.getScreenWidth(), this.getScreenHeight());
            mSvVideoPlayer.getHolder().setKeepScreenOn(true);//保持屏幕常亮
            mSvVideoPlayer.getHolder().addCallback(new SurFaceCallback());
        }
    }

    /**
     * 向player中设置dispay，也就是SurfaceHolder。但此时有可能SurfaceView还没有创建成功，所以需要监听SurfaceView的创建事件
     */
    private final class SurFaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (mMediaPlayer == null) {
                return;
            }
            if (!hasActiveHolder) {
                play(mPosition);
                hasActiveHolder = true;
            }
            if (mPosition > 0) {
                play(mPosition);
                mPosition = 0;
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mMediaPlayer == null) {
                return;
            }
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mPosition = mMediaPlayer.getCurrentPosition();
            }
        }

        private void play(int position) {
            try {
                //添加播放视频的路径与配置MediaPlayer
                AssetFileDescriptor fileDescriptor = getResources().openRawResourceFd(R.raw.video);
                mMediaPlayer.reset();
                //给mMediaPlayer添加预览的SurfaceHolder，将播放器和SurfaceView关联起来
                mMediaPlayer.setDisplay(mSvVideoPlayer.getHolder());

                mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                        fileDescriptor.getStartOffset(),
                        fileDescriptor.getLength());
                // 缓冲
                mMediaPlayer.prepare();
                mMediaPlayer.setOnBufferingUpdateListener(new BufferingUpdateListener());
                mMediaPlayer.setOnPreparedListener(new PreparedListener(position));
                mMediaPlayer.setOnCompletionListener(new CompletionListener());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 缓冲变化时回调
     */
    private final class BufferingUpdateListener implements MediaPlayer.OnBufferingUpdateListener {

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
        }
    }

    private final class PreparedListener implements MediaPlayer.OnPreparedListener {
        private int position;

        public PreparedListener(int position) {
            this.position = position;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mMediaPlayer.start();
            if (position > 0) {
                mMediaPlayer.seekTo(position);
            }
        }
    }

    /**
     * 播放结束时回调
     */
    private final class CompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            mMediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoview.stopPlayback();
        //释放内存，MediaPlayer底层是运行C++的函数方法，不使用后必需释放内存
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                mPosition = mMediaPlayer.getCurrentPosition();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_startvideo;
    }

    private int getScreenWidth() {
        return ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }

    private int getScreenHeight() {
        return ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getHeight();
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                startRecordVideo();
                break;
        }
    }

    @AfterPermissionGranted(RC_RECORD_VIDEO_PERMISSION)
    private void startRecordVideo() {
        if (EasyPermissions.hasPermissions(this, mRecordVideoPermission)) {

            Intent intent = new Intent(App.getAppContext(), RecordvideoActivity.class);
            intent.putExtra(Utils.PHOTO1, getIntent.getStringExtra(Utils.PHOTO1));
            intent.putExtra(Utils.PHOTO2, getIntent.getStringExtra(Utils.PHOTO2));
//            intent.putExtra(Utils.MOBILENO, getIntent.getStringExtra(Utils.MOBILENO));
//            intent.putExtra(Utils.PASSWORD, getIntent.getStringExtra(Utils.PASSWORD));
//            intent.putExtra(Utils.Birthday, getIntent.getStringExtra(Utils.Birthday));
//            intent.putExtra(Utils.SEX, getIntent.getStringExtra(Utils.SEX));
//            intent.putExtra(Utils.HEAD_URL, getIntent.getStringExtra(Utils.HEAD_URL));
//            intent.putExtra(Utils.NICKNAME, getIntent.getStringExtra(Utils.NICKNAME));
            intent.putExtra(Utils.TOKEN, getIntent.getStringExtra(Utils.TOKEN));
            startActivity(intent);

        } else {
            EasyPermissions.requestPermissions(this, "录制视频需要相机，麦克风，存储权限",
                    RC_RECORD_VIDEO_PERMISSION, mRecordVideoPermission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //startRecordVideo();
        }
    }

//    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            switch (intent.getAction()) {
//                case "record_video":
//                    videoview.setVideoPath(intent.getStringExtra("record_video"));
//                    videoview.start();
//                    break;
//            }
//        }
//    };
}