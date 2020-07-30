package com.jfkj.im.ui.mine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.BindSettleAccountBean;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.mine.CertificationPresenter;
import com.jfkj.im.mvp.mine.CertificationView;
import com.jfkj.im.ui.dialog.PictureDialog;
import com.jfkj.im.utils.GlideEngine;
import com.jfkj.im.utils.IDUtils;
import com.jfkj.im.utils.LQRPhotoSelectUtils;
import com.jfkj.im.utils.PermissionsCheckUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.GlideRoundTransform;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.utils.OkLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.internal.operators.observable.ObservableElementAt;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

/**
 * 实名认证
 */
public class CertificationActivity extends BaseActivity<CertificationPresenter> implements CertificationView, LQRPhotoSelectUtils.PhotoSelectListener, TextWatcher {

    public static final int PERMISSION_EXTERNAL_STORAGE = 0xf1;
    public static final int PERMISSION_GALLERY_CODE = 0xf2;


    private CertificationPresenter presenter;

    private LQRPhotoSelectUtils photoUtils;

    @BindView(R.id.ed_card)
    EditText ed_card;

    @BindView(R.id.et_user_name)
    EditText edit_username;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_title_right)
    AppCompatImageView iv_title_right;

    @BindView(R.id.iv_cardPhoto1)
    ImageView img_cardPhoto1;


    @BindView(R.id.iv_cardPhoto2)
    ImageView img_cardPhoto2;

    @BindView(R.id.tv_submit)
    TextView tv_submit;


    @BindView(R.id.dialog_close1_iv)
    ImageView ivCloseOne;


    @BindView(R.id.dialog_close2_iv)
    ImageView ivCloseTwo;


    private BottomSheetDialog mDialog;


    //判断是哪个View点击的
    String iSClick = "";
    private PictureDialog loadingDialog;


    String filepath1= "";
    String filepath2= "";

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private Context context;



    /**
     * 底部弹窗选择dialog
     */
    private void showPhotoDialog() {
        mDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_photo, null);
        mDialog.setContentView(view);
        mDialog.show();
        view.findViewById(R.id.tv_take_photo).setOnClickListener(this::onClick);
        view.findViewById(R.id.tv_from_photo).setOnClickListener(this::onClick);
        view.findViewById(R.id.tv_cancel).setOnClickListener(this::onClick);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        presenter = new CertificationPresenter(this);

        photoUtils = new LQRPhotoSelectUtils(this,this,false);
        tv_title.setText("实名认证");
        iv_title_right.setVisibility(View.GONE);
        context = this;



        edit_username.addTextChangedListener(this);
        ed_card.addTextChangedListener(this);
    }


    @OnClick({R.id.dialog_close1_iv,R.id.dialog_close2_iv, R.id.iv_title_back,R.id.tv_submit,R.id.iv_cardPhoto1,R.id.iv_cardPhoto2})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.dialog_close1_iv:

                ivCloseOne.setVisibility(View.GONE);
                Glide.with(this).load(R.mipmap.icon_add_picture)
                        .transform(new CenterCrop(), new GlideRoundTransform(5))
                        .into(img_cardPhoto1);
                filepath1 = "";
                break;

            case R.id.dialog_close2_iv:
                ivCloseTwo.setVisibility(View.GONE);
                Glide.with(this).load(R.mipmap.icon_add_picture)
                        .transform(new CenterCrop(), new GlideRoundTransform(5))
                        .into(img_cardPhoto2);
                filepath2 = "";
                break;
            case R.id.tv_cancel:
                mDialog.dismiss();
                break;
            case R.id.tv_from_photo:
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_DENIED) {
//                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_GALLERY_CODE);
//                    } else {
//                        photoUtils.selectPhoto();
//                    }
//                } else {
//                    photoUtils.selectPhoto();
//                }
//                if (mDialog.isShowing()) {
//                    mDialog.dismiss();
//                }
                onAddPicClick();
                break;
            case R.id.tv_take_photo:
//                photoUtils.setImgPath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator  + String.valueOf(System.currentTimeMillis()) + ".jpg");
//
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PERMISSION_DENIED) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_EXTERNAL_STORAGE);
//                    }
//                } else {
//                    photoUtils.takePhoto();
//                }
//                if (mDialog.isShowing()) {
//                    mDialog.dismiss();
//                }
                onAddCramePicClick();
                break;
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.tv_submit:

                String userName = edit_username.getText().toString().trim();

                String cardID = ed_card.getText().toString().trim();

                OkLogger.e(cardID + "  \t\t "  + userName   + " \t\t " +  filepath1  + "  \t\t " + filepath2);


                if("".equals(userName) || "".equals(cardID) || "".equals(filepath1) || "".equals(filepath2)){
                    ToastUtils.showShortToast("请提交完整参数");
                }
//                else if(IDUtils.isIDNumber(cardID)){
//                    ToastUtils.showShortToast("身份证格式不合法");
//                }
                else{
                    presenter.authentication(cardID,filepath1,userName,filepath2);
                }



                break;
            case R.id.iv_cardPhoto1:
                iSClick = "iv_cardPhoto1";
                showBtDialog();
                break;

            case R.id.iv_cardPhoto2:
                iSClick = "iv_cardPhoto2";
                showBtDialog();
                break;


        }
    }

    private void onAddCramePicClick() {
        //进入相册
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        List<LocalMedia> selectList=new ArrayList<>();
        PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .theme( R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                //                .setLanguage(language)// 设置语言，默认中文
                //                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                //                .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                //                .setPictureWindowAnimationStyle(windowAnimationStyle)// 自定义相册启动退出动画
                .maxSelectNum(1)// 最大图片选择数量
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
                .recordVideoSecond(60)//视频秒
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    public void onAddPicClick() {
        //进入相册
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
        List<LocalMedia> selectList=new ArrayList<>();
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .loadImageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .theme( R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
                .isWeChatStyle(true)// 是否开启微信图片选择风格
                //                .setLanguage(language)// 设置语言，默认中文
                //                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
                //                .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
                //                .setPictureWindowAnimationStyle(windowAnimationStyle)// 自定义相册启动退出动画
                .maxSelectNum(1)// 最大图片选择数量
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
                .recordVideoSecond(60)//视频秒
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    private void showBtDialog() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,1 );
            }else{
                showPhotoDialog();
            }
        }else{

            showPhotoDialog();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_EXTERNAL_STORAGE:
                if (PermissionsCheckUtils.hasAllPermissionsGranted(grantResults)) {

                    photoUtils.takePhoto();
                } else {
                    mDialog.dismiss();
                }
                break;
            case PERMISSION_GALLERY_CODE:
                if (PermissionsCheckUtils.hasAllPermissionsGranted(grantResults)) {
                    photoUtils.selectPhoto();
                } else {
                    mDialog.dismiss();
                }
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_certification;
    }

    @Override
    public CertificationPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void bindSuccess(BindSettleAccountBean bean) {
        if("200".equals(bean.getCode())){
            ToastUtils.showShortToast(bean.getMessage());
            finish();
        }else{
            ToastUtils.showShortToast(bean.getMessage());
        }
    }



    @Override
    public void showLoading() {
        loadingDialog = new PictureDialog(this);
        loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if(loadingDialog!=null)
        loadingDialog.dismiss();

    }


    /**
     * 文件上传成功
     * @param filepath 返回图片URL
     */
    @Override
    public void upFileSuccess(String filepath) {

        String card = ed_card.getText().toString().trim();
        String username = edit_username.getText().toString().trim();

        if(iSClick.equals("iv_cardPhoto1")){
            filepath1 = filepath;
        }else if(iSClick.equals("iv_cardPhoto2")){
            filepath2 = filepath;
        }

        if("".equals(card) || "".equals(username) || "".equals(filepath1) || "".equals(filepath2)){
            tv_submit.setClickable(false);
            tv_submit.setAlpha(0.5f);
            tv_submit.setBackgroundResource(R.drawable.shap_bt_two_bg);
        }else{
            tv_submit.setClickable(true);
            tv_submit.setAlpha(1f);
            tv_submit.setBackgroundResource(R.drawable.shap_bt_two_bg);
        }



    }

    /**
     * 拍照 相册返回地址
     *
     */
    @Override
    public void onFinish(File outputFile, Uri outputUri) {
        OkLogger.e("图片地址  = " + outputFile.getAbsolutePath());
        //上传文件
        presenter.uploadUserHead(outputFile, UserInfoManger.getUserId(), SPUtils.getInstance(this).getString(Utils.USERID) + System.currentTimeMillis());

        if(iSClick.equals("iv_cardPhoto1")){
            Glide.with(this).load(outputFile.getAbsoluteFile())
                    .transform(new CenterCrop(), new GlideRoundTransform(5))
                    .into(img_cardPhoto1);
            ivCloseOne.setVisibility(View.VISIBLE);


        }else if(iSClick.equals("iv_cardPhoto2")){

                Glide.with(this).load(outputFile.getAbsoluteFile())
                        .transform(new CenterCrop(), new GlideRoundTransform(5))
                        .into(img_cardPhoto2);

            ivCloseTwo.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 2、在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        photoUtils.attachToActivityForResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : localMedia) {
                        File file=new File(media.getCompressPath());
                        //上传文件
                        presenter.uploadUserHead(file, UserInfoManger.getUserId(), SPUtils.getInstance(this).getString(Utils.USERID) + System.currentTimeMillis());

                        if(iSClick.equals("iv_cardPhoto1")){
                            Glide.with(this).load(file.getAbsoluteFile())
                                    .transform(new CenterCrop(), new GlideRoundTransform(5))
                                    .into(img_cardPhoto1);
                            ivCloseOne.setVisibility(View.VISIBLE);


                        }else if(iSClick.equals("iv_cardPhoto2")){

                            Glide.with(this).load(file.getAbsoluteFile())
                                    .transform(new CenterCrop(), new GlideRoundTransform(5))
                                    .into(img_cardPhoto2);

                            ivCloseTwo.setVisibility(View.VISIBLE);
                        }
                        Log.v("--->media", "压缩::" + media.getCompressPath());
                        Log.v("--->media", "原图::" + media.getPath());
                        Log.v("--->media", "裁剪::" + media.getCutPath());
                        Log.v("--->media", "是否开启原图::" + media.isOriginal());
                        Log.v("--->media", "原图路径::" + media.getOriginalPath());
                        Log.v("--->media", "Android Q 特有Path::" + media.getAndroidQToPath());
                    }
                    break;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String card = ed_card.getText().toString().trim();
        String username = edit_username.getText().toString().trim();
        OkLogger.e(card + "  \t\t "  + username   + " \t\t " +  filepath1  + "  \t\t " + filepath2);

                if("".equals(card) || "".equals(username) || "".equals(filepath1) || "".equals(filepath2)){
                    tv_submit.setClickable(false);
                    tv_submit.setAlpha(0.5f);
                    tv_submit.setBackgroundResource(R.drawable.shap_bt_two_bg);
                }else{
                    tv_submit.setClickable(true);
                    tv_submit.setAlpha(1);
                    tv_submit.setBackgroundResource(R.drawable.shap_bt_two_bg);
                }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
