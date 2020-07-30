package com.jfkj.im.ui.party;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jfkj.im.Bean.BaseBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.GridImageAdapter;
import com.jfkj.im.adapter.GridImageOriginalAdapter;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.party.PartyComplaintsPresenter;
import com.jfkj.im.mvp.party.PartyComplaintsView;
import com.jfkj.im.ui.dialog.LottieDialog;
import com.jfkj.im.ui.dialog.PictureDialog;
import com.jfkj.im.utils.GlideEngine;
import com.jfkj.im.utils.StatusBarUtil;
import com.jfkj.im.utils.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.lzy.okgo.utils.OkLogger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 投訴
 */
public class PartyComplaintsActivity extends BaseActivity<PartyComplaintsPresenter> implements GridImageOriginalAdapter.onAddPicClickListener, PartyComplaintsView {

    private PartyComplaintsPresenter partyComplaintsPresenter;



    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @BindView(R.id.tv_confirm)
    TextView mTvConfirm;

    @BindView(R.id.info_1)
    CheckBox info1;


    @BindView(R.id.info_2)
    CheckBox info2;




    private List<String> types = new ArrayList<>();
    private GridImageOriginalAdapter mAdapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private int maxSelectNum = 3;
    private int themeId;
    private String iamgeUrl; //图片进行拼接
    private PictureDialog mDialog;
    private int chooseSize = 0;
    private String partyId;
    private String userId;
    private LottieDialog loadingDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_party_complaints;
    }

    @Override
    public PartyComplaintsPresenter createPresenter() {
//        return new ReportPresenter(this);
        return partyComplaintsPresenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarDarkTheme(this,false);
        themeId = R.style.picture_default_style;

        Intent intent = getIntent();
        partyId = intent.getStringExtra("partyId");
        userId = intent.getStringExtra("userId");

        partyComplaintsPresenter = new PartyComplaintsPresenter(this);

        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        mRecycler.setLayoutManager(layoutManager);
        mAdapter = new GridImageOriginalAdapter(this,this);
//        mAdapter.setList(selectList);
        mAdapter.setSelectMax(maxSelectNum);
        mRecycler.setAdapter(mAdapter);
        setTvListener();
        mAdapter.setOnItemClickListener((position, v) -> {
            if (selectList.size() > 0) {
                LocalMedia media = selectList.get(position);
                String mimeType = media.getMimeType();
                int mediaType = PictureMimeType.getMimeType(mimeType);
                switch (mediaType) {
                    case PictureConfig.TYPE_VIDEO:
                        // 预览视频
                        PictureSelector.create(this).externalPictureVideo(media.getPath());
                        break;
                    case PictureConfig.TYPE_AUDIO:
                        // 预览音频
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

    private void setTvListener() {
        if (types.size() == 0 && chooseSize == 0){
            mTvConfirm.setAlpha(0.5f);
        }else if (types.size() > 0 && selectList.size() > 0){
            mTvConfirm.setAlpha(1.0f);
        }
    }



    @OnClick({R.id.iv_title_back,R.id.info_1, R.id.info_2, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.info_1:

                types.clear();
                types = new ArrayList<>();

                if(info1.isChecked()){
                    types.add("1");
                }
                info2.setChecked(false);

                setTvListener();
                break;
            case R.id.info_2:
                types.clear();
                types = new ArrayList<>();
                if(info2.isChecked()){
                 types.add("2");
                }
                info1.setChecked(false);
                setTvListener();
                break;

            case R.id.tv_confirm:
                if (choose()) {
                    String type = "";
                    for (int i = 0 ; i < types.size() ; i++){
                        if(i==0){
                            type += types.get(i);
                        }else{
                            type += ","+types.get(i);
                        }

                    }

                  //  ToastUtils.showShortToast(type+"   "+ selectList.size());

                    OkLogger.e(type+"   "+ selectList.size());
//
                    partyComplaintsPresenter.upLoadFile(selectList,partyId,types.get(0),userId);
//                    mvpPresenter.setInfo(userId,type);
//                    mvpPresenter.upLoadFile(selectList);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 是否选择举报理由 好 图片
     * @return
     */
    private boolean choose() {
        if (types.size()==0){
            ToastUtils.showShortToast("请选择举报理由");
            return false;
        }

        if (selectList.size() == 0){
            ToastUtils.showShortToast("请添加图片证据");
            return false;
        }
        return true;
    }

    @Override
    public void onAddPicClick() {
        //进入相册
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
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
                .selectionMode(PictureConfig.MULTIPLE )// 多选 or 单选
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
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    public void getListSize(int size) {
        chooseSize = size;
        setTvListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case PictureConfig.CHOOSE_REQUEST:
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
                    setTvListener();
                    break;
            }
        }
    }

    @Override
    public void complaints(BaseBean baseBean) {
        ToastUtils.showShortToast(baseBean.getMessage());
        if(baseBean.getCode().equals("200")){
            finish();
        }
    }

    @Override
    public void showLoading() {
        loadingDialog = new LottieDialog(this);
        loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }

}
