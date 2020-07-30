package com.jfkj.im.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.jfkj.im.TIM.component.photoview.Util;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.Uploadpictures.UploadpicturesPresenter;
import com.jfkj.im.mvp.Uploadpictures.UploadpicturesView;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.GlideEngine;
import com.jfkj.im.utils.LQRPhotoSelectUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.CustomVideoView;
import com.jfkj.im.view.GlideRoundTransform;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.utils.OkLogger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class UploadpicturesActivity extends BaseActivity<UploadpicturesPresenter> implements View.OnClickListener, UploadpicturesView {
    private static final int PERMISSION_READ_WRITE = 999;
    LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    @BindView(R.id.upload1_add_iv)
    ImageView upload1_add_iv;
    @BindView(R.id.dialog_close1_iv)
    ImageView dialog_close1_iv;
    @BindView(R.id.upload2_add_iv)
    ImageView upload2_add_iv;
    @BindView(R.id.dialog_close2_iv)
    ImageView dialog_close2_iv;
    @BindView(R.id.btn_next)
    Button btn_next;

    @BindView(R.id.rl_head)
    RelativeLayout rl_head;

    UploadpicturesPresenter uploadpicturesPresenter;
    List<String> list = new ArrayList<>();
    String url1 = "";
    String url2 = "";
    int type = 0;
    Intent getIntent;
    String token;


    private static final String[] permissions = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String PERMISSION_TIPS = "为保证软件正常使用请开启电话和读写权限";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(mActivity, false);
        setStaturBar(rl_head);
        upload1_add_iv.setOnClickListener(this);
        dialog_close1_iv.setOnClickListener(this);
        upload2_add_iv.setOnClickListener(this);
        dialog_close2_iv.setOnClickListener(this);
        btn_next.setEnabled(false);
        btn_next.setOnClickListener(this);
        getIntent = getIntent();
        token = getIntent.getStringExtra(Utils.TOKEN);

        uploadpicturesPresenter = new UploadpicturesPresenter(this);

        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), outputFile);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", outputFile.getName().replace("com.jfkj.im", ""), requestBody);
                HashMap<String,String> map = new HashMap<>();
                map.put(Utils.FILE_TYPE,"1");
                map.put(Utils.PATH_TYPE,"1");
                map.put(Utils.USERID,SPUtils.getInstance(getApplicationContext()).getString(Utils.USERID));

                    uploadpicturesPresenter.getuploadImage(ApiStores.baseupload_url+"/file/uploadFiles",map, body);

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        uploadpicturesPresenter.getuploadImage(ApiStores.base_file+"/file/uploadFiles",map, body);
//                    }
//                },9000);

            }
        }, false);

    }






    @Override
    protected int getLayoutId() {
        return R.layout.activity_uploadpictures;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public UploadpicturesPresenter createPresenter() {
        return uploadpicturesPresenter;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.upload1_add_iv:
                type = 1;

                requestPermission();


                setNextBg();
                break;
            case R.id.dialog_close1_iv:
                url1 = "";
                Glide.with(App.getAppContext()).load(R.mipmap.icon_add_pic_white).into(upload1_add_iv);
                setNextBg();
                break;
            case R.id.upload2_add_iv:
                type = 2;
                requestPermission();

                setNextBg();
                break;
            case R.id.dialog_close2_iv:
                url2 = "";
                Glide.with(App.getAppContext()).load(R.mipmap.icon_add_pic_white).into(upload2_add_iv);
                setNextBg();
                break;
            case R.id.btn_next:
                if (url1.trim().length() > 0 && url2.trim().length() > 0) {
                    Intent intent = new Intent(App.getAppContext(), StartvideoActivity.class);
                    intent.putExtra(Utils.PHOTO1, url1);
                    intent.putExtra(Utils.PHOTO2, url2);
//                    intent.putExtra(Utils.PASSWORD, getIntent.getStringExtra(Utils.PASSWORD));
                    intent.putExtra(Utils.TOKEN, token);
//                    intent.putExtra(Utils.MOBILENO, getIntent.getStringExtra(Utils.MOBILENO));
//                    intent.putExtra(Utils.Birthday, getIntent.getStringExtra(Utils.Birthday));
//                    intent.putExtra(Utils.SEX, getIntent.getStringExtra(Utils.SEX));
//                    intent.putExtra(Utils.HEAD_URL,  getIntent.getStringExtra(Utils.HEAD_URL));
//                    intent.putExtra(Utils.NICKNAME,getIntent.getStringExtra(Utils.NICKNAME));
                    startActivity(intent);
                    finish();
                } else {
                    toastShow("请上传两张图片");
                }
                break;
        }
    }

    private void setNextBg() {
        if (!TextUtils.isEmpty(url1) && !TextUtils.isEmpty(url2)){
            btn_next.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_base_btn_white_20dp));
            btn_next.setAlpha(1f);
            btn_next.setEnabled(true);
        }else {
            btn_next.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_base_btn_white_20dp));
            btn_next.setAlpha(0.4f);
            btn_next.setEnabled(false);
        }
        if (!TextUtils.isEmpty(url1)){
            dialog_close1_iv.setVisibility(View.VISIBLE);
        }else {
            dialog_close1_iv.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(url2)){
            dialog_close2_iv.setVisibility(View.VISIBLE);
        }else {
            dialog_close2_iv.setVisibility(View.GONE);
        }
    }

    private boolean hasPermission() {
        return EasyPermissions.hasPermissions(this, permissions);
    }

    @AfterPermissionGranted(PERMISSION_READ_WRITE)
    private void requestPermission() {
        if (hasPermission()) {
            PermissionGen.needPermission(UploadpicturesActivity.this, LQRPhotoSelectUtils.REQ_SELECT_PHOTO, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE});

            //有权限
        } else {
            EasyPermissions.requestPermissions(this, PERMISSION_TIPS, PERMISSION_READ_WRITE, permissions);
        }
    }

    @Override
    public void uploadImageSuccess(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            //toastShow(jsonObject.getString("message"));
            if (jsonObject.getString("code").equals("200")) {
                if (type == 1) {
                    url1 = jsonObject.getJSONObject("data").getString("fileUrls");


                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.nim_default_img_failed) //预加载图片
                            .error(R.drawable.nim_default_img_failed) //加载失败图片
                            .priority(Priority.HIGH) //优先级
                            .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
                            .transform(new GlideRoundTransform(5)); //圆角




                    Glide.with(App.getAppContext()).load(jsonObject.getJSONObject("data").getString("fileUrls")).apply(options).into(upload1_add_iv);
                }
                if (type == 2) {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.nim_default_img_failed) //预加载图片
                            .error(R.drawable.nim_default_img_failed) //加载失败图片
                            .priority(Priority.HIGH) //优先级
                            .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
                            .transform(new GlideRoundTransform(5)); //圆角


                    url2 = jsonObject.getJSONObject("data").getString("fileUrls");
                    Glide.with(App.getAppContext()).load(jsonObject.getJSONObject("data").getString("fileUrls")).apply(options).into(upload2_add_iv);
                }
                setNextBg();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uploadImagefail(String s) {
        OkLogger.e(s);
        toastShow(s);
    }

    @Override
    protected void onPause() {
        super.onPause();
    //    SPUtils.getInstance(mActivity).put(Utils.TOKEN, "");
    }

    @Override
    public void showLoading() {
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        mLqrPhotoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
     //   mLqrPhotoSelectUtils.selectPhoto();
        onAddPicClick();
    }
    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void showTip1() {
        showDialog();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2() {
        showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2、在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : localMedia) {
                        File file=new File(media.getCompressPath());

                        Log.v("--->media", "压缩::" + media.getCompressPath());
                        Log.v("--->media", "原图::" + media.getPath());
                        Log.v("--->media", "裁剪::" + media.getCutPath());
                        Log.v("--->media", "是否开启原图::" + media.isOriginal());
                        Log.v("--->media", "原图路径::" + media.getOriginalPath());
                        Log.v("--->media", "Android Q 特有Path::" + media.getAndroidQToPath());
                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName().replace("com.jfkj.im", ""), requestBody);
                        HashMap<String,String> map = new HashMap<>();
                        map.put(Utils.FILE_TYPE,"1");
                        map.put(Utils.PATH_TYPE,"1");
                        map.put(Utils.USERID,SPUtils.getInstance(getApplicationContext()).getString(Utils.USERID));

                        uploadpicturesPresenter.getuploadImage(ApiStores.baseupload_url+"/file/uploadFiles",map, body);
                    }
                    break;
            }
        }
    }

    public void showDialog() {
        //创建对话框创建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框显示小图标
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置标题
        builder.setTitle("权限申请");
        //设置正文
        builder.setMessage("在设置-应用-虎嗅-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能");

        //添加确定按钮点击事件
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {//点击完确定后，触发这个事件

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这里用来跳到手机设置页，方便用户开启权限
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //添加取消按钮点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //使用构建器创建出对话框对象
        AlertDialog dialog = builder.create();
        dialog.show();//显示对话框
    }
    public void onAddPicClick() {
        //进入相册

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
                .isCamera(false)// 是否显示拍照按钮
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
}
