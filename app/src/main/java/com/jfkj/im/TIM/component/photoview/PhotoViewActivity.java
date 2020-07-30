package com.jfkj.im.TIM.component.photoview;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.FileUtil;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PhotoViewActivity extends Activity {

    public static TIMImage mCurrentOriginalImage;
    private PhotoView mPhotoView;
    private Matrix mCurrentDisplayMatrix = null;
    private TextView mViewOriginalBtn;
    private List<LocalMedia> medias = new ArrayList<>();
    private LocalMedia localMedia = new LocalMedia();
    ImageView iv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo_view);

        mCurrentDisplayMatrix = new Matrix();
        mPhotoView = findViewById(R.id.photo_view);
        iv = findViewById(R.id.iv);
        mPhotoView.setDisplayMatrix(mCurrentDisplayMatrix);

        mViewOriginalBtn = findViewById(R.id.view_original_btn);
        String url = getIntent().getStringExtra(TUIKitConstants.IMAGE_DATA);
//        Glide.with(this).load(url).into(iv);


        localMedia.setPath(url);
        medias.add(localMedia);
        PictureSelector.create(this)
                //.themeStyle(themeId) // xml设置主题
                .setPictureStyle(new PictureParameterStyle())// 动态自定义相册主题
                //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
                .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                .loadImageEngine(com.jfkj.im.utils.GlideEngine.createGlideEngine())
                .openExternalPreview(0, medias);   // 外部传入图片加载引擎，必传项

        findViewById(R.id.photo_view_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
