package com.jfkj.im.ui.discovery;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.adapter.GridImageAdapter;
import com.jfkj.im.banner.GridSpacingItemDecoration;
import com.jfkj.im.banner.SpacesItemDecoration;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.discovery.PostingPresenter;
import com.jfkj.im.mvp.discovery.PostingView;
import com.jfkj.im.ui.dialog.PictureDialog;
import com.jfkj.im.utils.DisplayUtil;
import com.jfkj.im.utils.GlideEngine;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.PermissionsCheckUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.videocompression.SiliCompressor;
import com.jfkj.im.view.ByteLimitWatcher;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
 * Description:  发布动态
 * @author :   ys
 * @date :         2019/12/10
 *
 * </pre>
 */
public class PostingActivity extends BaseActivity<PostingPresenter> implements PostingView, GridImageAdapter.onAddPicClickListener, View.OnClickListener {
    @BindView(R.id.iv_title_back)
    AppCompatImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    AppCompatTextView mTvTitle;
    @BindView(R.id.tv_title_right)
    AppCompatTextView mTvTitleRight;
    @BindView(R.id.iv_divide)
    View mIvDivide;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.iv_divide2)
    View mIvDivide2;
    @BindView(R.id.iv_location)
    ImageView mIvLocation;
    @BindView(R.id.tv_location)
    TextView mTvLocation;
    @BindView(R.id.iv_choose_location)
    ImageView mIvChooseLocation;
    @BindView(R.id.iv_divide3)
    View mIvDivide3;
    @BindView(R.id.length_tv)
    TextView mTvLength;
    @BindView(R.id.layout_location)
    ConstraintLayout mLayoutLocation;
    private GridImageAdapter mAdapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private int maxSelectNum = 9;
    private int themeId;
    private String postingType = "";
    private int openType;
    private String content = "";
    private String locality = "un";
    private String isLocation = "1";
    private static final int SET_LOCATION = 0x121;
    private PictureDialog loadingDialog = null;
    private int len = 150;
    double latitudeDouble = 0L;
    double longitude = 0L;
    static TIMConversation conversation;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_posting;
    }

    @Override
    public PostingPresenter createPresenter() {
        return new PostingPresenter(this);
    }

    @Override
    public void showLoading() {
        progressDialog.show();

    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeId = R.style.picture_default_style;
        mTvTitle.setText("发布动态");
        setAndroidNativeLightStatusBar(mActivity, false);
        // latitudeDouble = Double.valueOf(UserInfoManger.getLatitude());
        //   longitude = Double.parseDouble(UserInfoManger.getLongitude());
        //    locality = LocationUtils.getLocality(this, latitudeDouble, longitude);
        // mTvLocation.setText(locality);
        //   mTvLocation.setText(UserInfoManger.getcity());


        mTvLocation.setText(UserInfoManger.getcity());

        initLocationOption();//获取位置信息
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            postingType = bundle.getString("POSTING_TYPE");
        }
        if ("type_image".equals(postingType)) {
            openType = PictureMimeType.ofImage();
        } else if ("type_video".equals(postingType)) {
            openType = PictureMimeType.ofVideo();
            maxSelectNum = 1;
        }
        conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, Utils.CIRCLEROOMID);
        loadingDialog = new PictureDialog(this);
        mTvTitleRight.setOnClickListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.addItemDecoration(new GridSpacingItemDecoration(3, DisplayUtil.dip2px(this,5),false));
        mAdapter = new GridImageAdapter(this, this);
        mAdapter.setSelectMax(maxSelectNum);
        mRecycler.setAdapter(mAdapter);
        mEtContent.addTextChangedListener(new ByteLimitWatcher(mEtContent, null, 150));

        mAdapter.setOnItemClickListener((position, v) -> {
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String mimeType = media.getMimeType();
                int mediaType = PictureMimeType.getMimeType(mimeType);
                switch (mediaType) {
                    case PictureConfig.TYPE_VIDEO:
                        // 预览视频
                        if (media.getAndroidQToPath() != null) {
                            if (media.getAndroidQToPath().trim().length() > 0) {
                                media.setPath(media.getAndroidQToPath().trim());
                            }
                        }
                        if (media.getPath() != null) {
                            if (media.getPath().trim().length() > 0) {
                                media.setAndroidQToPath(media.getPath().trim());
                            }
                        }
                        PictureSelector.create(this).externalPictureVideo(media.getPath());
                        break;
                    case PictureConfig.TYPE_AUDIO:
                        // 预览音频
                        if (media.getAndroidQToPath() != null) {
                            if (media.getAndroidQToPath().trim().length() > 0) {
                                media.setPath(media.getAndroidQToPath().trim());
                            }
                        }
                        if (media.getPath() != null) {
                            if (media.getPath().trim().length() > 0) {
                                media.setAndroidQToPath(media.getPath().trim());
                            }
                        }
                        PictureSelector.create(this).externalPictureAudio(media.getPath());
                        break;
                    default:

                        // 预览图片 可自定长按保存路径
                        //                        PictureWindowAnimationStyle animationStyle = new PictureWindowAnimationStyle();
                        //                        animationStyle.activityPreviewEnterAnimation = R.anim.picture_anim_up_in;
                        //                        animationStyle.activityPreviewExitAnimation = R.anim.picture_anim_down_out;
                        PictureSelector.create(this)
                                //.themeStyle(themeId) // xml设置主题
                                .setPictureStyle(new PictureParameterStyle())// 动态自定义相册主题
                                //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
                                .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                                .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                                .openExternalPreview(position, selectList);
                        break;
                }
            }
        });

    }

    @OnClick({R.id.iv_title_back, R.id.tv_location, R.id.iv_choose_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_location:
            case R.id.iv_choose_location:
                // todo choose location
                Bundle bundle = new Bundle();
                bundle.putString("location", locality);
                JumpUtil.startForResult(this, ChooseLocationActivity.class, SET_LOCATION, bundle);

                break;
        }
    }


    private void initLocationOption() {

//定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        LocationClient locationClient = new LocationClient(getApplicationContext());
//声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
//注册监听函数
        locationClient.registerLocationListener(myLocationListener);
//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("gcj02");
//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
//可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
//可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);

//可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(false);
//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
//可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
//可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationOption.setIsNeedAltitude(false);
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        locationClient.setLocOption(locationOption);
//开始定位

        locationClient.start();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            //获取纬度信息
            latitudeDouble = location.getLatitude();
            //获取经度信息
            mTvLocation.setText(location.getCity());

            longitude = location.getLongitude();

            locality = location.getCity() + "";
            UserInfoManger.savecity(location.getCity());
            UserInfoManger.saveLongitude(location.getLongitude() + "");
            UserInfoManger.saveLatitude(location.getLatitude() + "");

        }
    }

    @Override
    public void onAddPicClick() {
        //进入相册

        PictureSelector.create(this)
                .openGallery(openType)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                //                .setLanguage(language)// 设置语言，默认中文
                //                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                //                .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                //                .setPictureWindowAnimationStyle(windowAnimationStyle)// 自定义相册启动退出动画
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(3)// 每行显示个数
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                //                .isOriginalImageControl(cb_original.isChecked())// 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，压缩、裁剪功能将会失效
                //.cameraFileName("test.png")    // 重命名拍照文件名、注意这个只在使用相机时可以使用，如果使用相机又开启了压缩或裁剪 需要配合压缩和裁剪文件名api
                //.renameCompressFile("test.png")// 重命名压缩文件名、 注意这个不要重复，只适用于单张图压缩使用
                //.renameCropFileName("test.png")// 重命名裁剪文件名、 注意这个不要重复，只适用于单张图裁剪使用
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                //                .isSingleDirectReturn(cb_single_back.isChecked())// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                .previewImage(true)// 是否可预览图片
                .previewVideo(true)// 是否可预览视频
                //.querySpecifiedFormatSuffix(PictureMimeType.ofPNG())// 查询指定后缀格式资源
                //                .enablePreviewAudio(cb_preview_audio.isChecked()) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .compressQuality(80)// 图片压缩后输出质量 0~ 100
                .synOrAsy(true)//同步false或异步true 压缩 默认同步
                //.queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效 注：已废弃
                //.glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度 注：已废弃
                //                .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                //                .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
                .isGif(false)// 是否显示gif图片
                //                .freeStyleCropEnabled(cb_styleCrop.isChecked())// 裁剪框是否可拖拽
                //                .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
                //                .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                //                .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                //                .openClickSound(cb_voice.isChecked())// 是否开启点击声音
                .selectionMedia(selectList)// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                //                        .videoMaxSecond(15)
                //                        .videoMinSecond(10)
                //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                //.cropCompressQuality(90)// 注：已废弃 改用cutOutQuality()
                .cutOutQuality(90)// 裁剪输出质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(true) // 裁剪是否可旋转图片
                //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径  注：已废弃
                .videoMaxSecond(60)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(5)// 显示多少秒以内的视频or音频也可适用 int
                .recordVideoSecond(30)//视频秒
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    public void getListSize(int size) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:

                    selectList.clear();
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回五种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 4.media.getOriginalPath()); media.isOriginal());为true时此字段才有值
                    // 5.media.getAndroidQToPath();为Android Q版本特有返回的字段，此字段有值就用来做上传使用
                    // 如果同时开启裁剪和压缩，则取压缩路径为准因为是先裁剪后压缩
                    for (LocalMedia media : selectList) {
                        Log.i("reportActivity", "压缩::" + media.getCompressPath());
                        Log.i("reportActivity", "原图::" + media.getPath());
                        Log.i("reportActivity", "裁剪::" + media.getCutPath());
                        Log.i("reportActivity", "是否开启原图::" + media.isOriginal());
                        Log.i("reportActivity", "原图路径::" + media.getOriginalPath());
                        Log.i("reportActivity", "Android Q 特有Path::" + media.getAndroidQToPath());
                    }

                    mAdapter.setList(selectList);
                    mAdapter.notifyDataSetChanged();
                    break;
                case SET_LOCATION:
                    isLocation = "2";
                    mLayoutLocation.setVisibility(View.GONE);
                    break;
            }
        } else if (resultCode == 10) {
            if (data != null) {
                locality = data.getStringExtra("user_location");
                mTvLocation.setText(locality);
            }
        }
    }

    @Override
    public void publishSuccess(BaseResponse o) {
        MessageInfo messageInfo = MessageInfoUtil.buildPraise("7", "4", Utils.CIRCLEROOMID);
        conversation.sendMessage(messageInfo.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(final int code, final String desc) {


            }

            @Override
            public void onSuccess(TIMMessage timMessage) {

            }
        });


        ToastUtils.showShortToast(o.getMessage());
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_right:
                if (TextUtils.isEmpty(mEtContent.getText().toString().trim())) {
                    toastShow("请输入文字");
                    return;
                }
                if (selectList.size() == 0) {
                    toastShow("请上传图片或者文件");
                    return;
                }

                if (openType == PictureMimeType.ofImage()) {
                    //图片
                    mvpPresenter.setPrams("1", "2", UserInfoManger.getUserId(), mEtContent.getText().toString().trim(), mTvLocation.getText().toString().trim(), isLocation);
                    mvpPresenter.upLoadFile(selectList);
                } else if (openType == PictureMimeType.ofVideo()) {
                    //视频

                    mvpPresenter.setPrams("4", "2", UserInfoManger.getUserId(), mEtContent.getText().toString().trim(), mTvLocation.getText().toString().trim(), isLocation);

                    String path;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        path = selectList.get(0).getAndroidQToPath();
                    } else {
                        path = selectList.get(0).getPath();
                    }

                    showLoading();

                    compressVideo(path, this.getCacheDir().getAbsolutePath());

                }
                //这里是发送消息到所有人通知有人发布了动态的


                break;
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
                        LocalMedia imageBean = new LocalMedia();
                        imageBean.setPath(outputPath);
                        mvpPresenter.upLoadVideo(imageBean);


                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        hideLoading();

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
