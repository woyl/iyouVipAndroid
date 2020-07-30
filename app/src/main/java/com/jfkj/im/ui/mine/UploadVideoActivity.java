package com.jfkj.im.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.MessageEvent;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.mine.UpLoadVideoPresent;
import com.jfkj.im.mvp.mine.UploadView;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.activity.MainActivity;
import com.jfkj.im.ui.dialog.PictureDialog;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.GlideEngine;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.StatusBarUtil;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.videocompression.SiliCompressor;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.utils.OkLogger;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * <pre>
 * Description: 上传视频页
 * @author :   ys
 * @date :         2019/12/16
 * </pre>
 */
public class UploadVideoActivity extends BaseActivity<UpLoadVideoPresent> implements UploadView, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private static final String TAG = "UploadVideoActivity";
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_hint1)
    TextView mTvHint1;
    @BindView(R.id.tv_hint2)
    TextView mTvHint2;
    @BindView(R.id.tv_choose_video)
    TextView mTvChooseVideo;
    @BindView(R.id.iv_video)
    VideoView mIvVideo;
    @BindView(R.id.tv_delete)
    TextView mTvDelete;
    @BindView(R.id.layout_title)
    LinearLayout mLayoutTitle;
    @BindView(R.id.tv_upload)
    TextView mTvUpload;
    @BindView(R.id.tv_choose)
    TextView mTvChoose;


    /**自定义进度条*/
    @BindView(R.id.iv_start_stop_center)
    ImageView iv_start_stop_center;
    @BindView(R.id.rl_bottom)
    RelativeLayout rl_bottom;
    @BindView(R.id.iv_start_stop)
    ImageView iv_start_stop;
    @BindView(R.id.tv_start_time)
    TextView tv_start_time;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.tv_time)
    TextView tv_time;

    private  StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private boolean mIsOnclick = false;


    private int themeId;
    private List<LocalMedia> selectList = new ArrayList<>();
    private MediaController mMediaController;
    private PictureDialog mDialog;
    Map<String, String> map = new HashMap<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_upload_video;
    }

    @Override
    public UpLoadVideoPresent createPresenter() {
        return new UpLoadVideoPresent(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setAndroidNativeLightStatusBar(this,false);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.DEVICENAME, Utils.getdeviceName());
        map.put(Utils.DEVICEID, Utils.ANDROID);
        map.put(Utils.REQTIME, AppUtils.getReqTime());


        themeId = R.style.picture_default_style;
        mDialog = new PictureDialog(this);
//        StatusBarUtil.setStatusBarDarkTheme(this,false);
        Log.d("@@@", "uuuuu" + UserInfoManger.getUserVideoUrl());
        if (!TextUtils.isEmpty(UserInfoManger.getUserVideoUrl())) {
            showUploadSuccess();

        } else {
            showNoVideo();
        }

    }

    /**
     * 没有上传视频
     */
    private void showNoVideo() {
        mTvDelete.setVisibility(View.GONE);
        mTvChoose.setVisibility(View.GONE);
        mTvHint1.setVisibility(View.VISIBLE);
        mTvHint2.setVisibility(View.VISIBLE);
        mIvVideo.setVisibility(View.GONE);
        mTvUpload.setVisibility(View.GONE);
        mTvChooseVideo.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoading() {

        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @OnClick({R.id.iv_back, R.id.tv_choose_video, R.id.tv_delete, R.id.tv_upload, R.id.tv_choose,R.id.iv_start_stop,R.id.iv_start_stop_center,R.id.iv_video})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_choose_video:
            case R.id.tv_choose:
                chooseVideo();
                break;
            case R.id.tv_delete:
                mvpPresenter.delVideo("");
                selectList.clear();
                mTvHint1.setVisibility(View.VISIBLE);
                mTvHint2.setVisibility(View.VISIBLE);
                mIvVideo.setVisibility(View.GONE);
                mTvUpload.setVisibility(View.GONE);
                mTvChooseVideo.setVisibility(View.VISIBLE);
                UserInfoManger.saveUserVideo("");
                SPUtils.getInstance(mActivity).remove(Utils.SP_KEY_USER_VIDEO_URL);
                EventBus.getDefault().post(new MessageEvent(Utils.SET_USER_DELETE_VIDEO, ""));


//                if(Utils.netWork()){
//                    OkGo.<String>post(ApiStores.base_url+"/my/publishDynamic")
//                            .tag(mActivity)
//                            .headers(Utils.TOKEN,SPUtils.getInstance(mActivity).getString(Utils.TOKEN))
//                            .headers(Utils.SIGN, SignUtils.getInstance().getSignToken(map))
//                            .params(map)
//                            .execute(new StringCallback() {
//                                @Override
//                                public void onSuccess(Response<String> response) {
//                                    try {
//                                        String body = response.body();
//                                        JSONObject jsonObject=new JSONObject(body);
//                                        toastShow(jsonObject.getString("message"));
//                                        if(jsonObject.getString("code").equals("200")){
                mDialog.dismiss();
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });
//
//                }else {
//                    toastShow(R.string.nonetwork);
//                }
                hide();

                break;
            case R.id.tv_upload:
                String path = selectList.get(0).getPath();


                showLoading();
                compressVideo(path, this.getCacheDir().getAbsolutePath());


                break;
            case R.id.iv_start_stop:
            case R.id.iv_start_stop_center:
                play();
                break;
            case R.id.iv_video:
                showButton2();
                mIsOnclick = true;
                if (mIvVideo != null && mIvVideo.isPlaying()){
                    hide();
                } else  {
                    show();
                }
                break;
            default:
                break;
        }
    }

    private void showButton2() {
        if (mIvVideo != null && mIvVideo.isPlaying()){
            iv_start_stop_center.setVisibility(View.GONE);
        } else  {
            iv_start_stop_center.setVisibility(View.VISIBLE);
        }
    }

    protected void play() {
        if (mIvVideo != null && mIvVideo.isPlaying()){
            Glide.with(this).load(R.mipmap.play_stop_nol).into(iv_start_stop);
            mIvVideo.pause();
        }else {
            handler.postDelayed(runnable, 0);
            Glide.with(this).load(R.mipmap.play_goon_nol).into(iv_start_stop);
            iv_start_stop_center.setVisibility(View.GONE);
            mIvVideo.start();
        }
    }


    /**
     * 视频压缩
     */
    private void compressVideo(final String srcPath, final String destDirPath) {
        // final String destDirPath =  getTrimmedVideoDir(this, "small_video");
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    int outWidth = 0;
                    int outHeight = 0;

                    //竖屏
                    outWidth = 720;
                    outHeight = 480;

                    String compressedFilePath = SiliCompressor.with(App.getAppContext())
                            .compressVideo(srcPath, destDirPath, outWidth, outHeight, 7200000);
                    emitter.onNext(compressedFilePath);
                } catch (Exception e) {
                    emitter.onError(e);
                }
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
//                        subscribe(d);
                    }

                    @Override
                    public void onNext(String outputPath) {

                        Log.e(TAG, "压缩成功" + outputPath);

                        LocalMedia imageBean = new LocalMedia();
                        imageBean.setPath(outputPath);

                        //文件上传
                        mvpPresenter.upLoadFile(imageBean);

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(TAG, "compressVideo---onError:" + e.toString());

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    private void chooseVideo() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofVideo())
                .loadImageEngine(GlideEngine.createGlideEngine())
                .theme(themeId)
                .maxSelectNum(1)
                .isWeChatStyle(true)
                .previewImage(true)
                .compress(true)
                .enablePreviewAudio(false)
                .compressQuality(20)
                .previewVideo(true)
                .isCamera(true)
                .videoMaxSecond(30)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(5)// 显示多少秒以内的视频or音频也可适用 int
                .recordVideoSecond(60)//视频秒
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    selectList = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectList) {
                        Log.i("reportActivity", "压缩::" + media.getCompressPath());
                        Log.i("reportActivity", "原图::" + media.getPath());
                        Log.i("reportActivity", "裁剪::" + media.getCutPath());
                        Log.i("reportActivity", "是否开启原图::" + media.isOriginal());
                        Log.i("reportActivity", "原图路径::" + media.getOriginalPath());
                        Log.i("reportActivity", "Android Q 特有Path::" + media.getAndroidQToPath());
                        if (media.getAndroidQToPath() != null) {
                            if (media.getAndroidQToPath().trim().length() > 0) {
                                media.setPath(media.getAndroidQToPath().trim());
                            }
                        }
                    }


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        selectList.get(0).setPath(selectList.get(0).getAndroidQToPath());
                    }else{

                    }

                    mTvDelete.setVisibility(View.GONE);
                    mTvChoose.setVisibility(View.VISIBLE);
                    mTvHint1.setVisibility(View.GONE);
                    mTvHint2.setVisibility(View.GONE);
                    mIvVideo.setVisibility(View.VISIBLE);
                    mTvUpload.setVisibility(View.VISIBLE);
                    mTvChooseVideo.setVisibility(View.GONE);
                    mMediaController = new MediaController(this);

                    playVideo(selectList.get(0).getPath());
                    break;
            }
        }
    }

    private void playVideo(String path) {
        Log.d("@@@", "path" + path);
        mIvVideo.setOnCompletionListener(this);
        mIvVideo.setOnPreparedListener(this);
//        mIvVideo.setMediaController(mMediaController);

        Uri uri = Uri.parse(path);
        mIvVideo.setVideoURI(uri);
//        mIvVideo.start();

        seekBar.setMax(1000);

        mIvVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mFormatBuilder = new StringBuilder();
                mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
                Glide.with(mActivity).load(R.mipmap.play_goon_nol).into(iv_start_stop);
                iv_start_stop_center.setVisibility(View.GONE);
                handler.postDelayed(runnable, 0);
                mIvVideo.start();
            }
        });
    }


    /***/
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            int pos = setProgresser();
            if (mIvVideo != null && mIvVideo.isPlaying()) {
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
    }

    private void show() {
        if (mIvVideo != null && mIvVideo.isPlaying()){
            iv_start_stop_center.setVisibility(View.GONE);
        } else {
            iv_start_stop_center.setVisibility(View.VISIBLE);
        }

        rl_bottom.setVisibility(View.VISIBLE);
    }

    /***/

    private int setProgresser() {
        if (mIvVideo == null) {
            return 0;
        }
        int position = mIvVideo.getCurrentPosition();
        int duration = mIvVideo.getDuration();

        if (seekBar != null) {
            if (duration > 0) {
                long pos = 1000L * position / duration;
                seekBar.setProgress((int) pos);
            }
            int percent = mIvVideo.getBufferPercentage();
            seekBar.setSecondaryProgress(percent * 10);

            if (tv_time != null)
                tv_time.setText(stringForTime(duration));
            if (tv_start_time != null)
                tv_start_time.setText(stringForTime(position));
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
    public void onCompletion(MediaPlayer mp) {
        Glide.with(this).load(R.mipmap.play_stop_nol).into(iv_start_stop);
     }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setOnInfoListener((mp1, what, extra) -> {
            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                // video started
                mIvVideo.setBackgroundColor(Color.TRANSPARENT);
                return true;
            }
            return false;
        });
    }

    @Override
    public void onPause() {
        // Stop video when the activity is pause.
//        mPositionWhenPaused = mIvVideo.getCurrentPosition();
        mIvVideo.stopPlayback();

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMediaController = null;
        mIvVideo = null;
        super.onDestroy();
    }

    @Override
    public void showUploadSuccess() {

        mTvDelete.setVisibility(View.VISIBLE);
        mTvChoose.setVisibility(View.GONE);
        mTvHint1.setVisibility(View.GONE);
        mTvHint2.setVisibility(View.GONE);
        mIvVideo.setVisibility(View.VISIBLE);
        mTvUpload.setVisibility(View.GONE);
        mTvChooseVideo.setVisibility(View.GONE);

        playVideo(UserInfoManger.getUserVideoUrl());

        EventBus.getDefault().post(new MessageEvent(Utils.SET_USER_UPLOAD_VIDEO, ""));
    }
}
