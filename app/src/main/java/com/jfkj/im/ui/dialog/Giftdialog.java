package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.utils.GlideUtils;
import com.jfkj.im.utils.SvgaUtils;
import com.opensource.svgaplayer.SVGAImageView;

public class Giftdialog extends Dialog implements SvgaUtils.OnDismissListener {

    Context context;
    private SVGAImageView svgaImage;
    private SvgaUtils svgaUtils;
    private ImageView imageView;


    public Giftdialog(@NonNull Context context) {
        //Dialog_Light 背景不变暗的style
        super(context, R.style.dialogstyle);
        this.context = context;
    }

    Animation mAnimation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gift);
        svgaImage = findViewById(R.id.svgaImage);
        imageView = findViewById(R.id.imgView);

        svgaUtils = new SvgaUtils(context,svgaImage,this);
        svgaUtils.initAnimator();
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
    }

    public void setGiftId(String id){
        svgaUtils.startAnimator(id);
    }

    public void setUrl(String url) {
        GlideUtils.loadChatImage(context,url,imageView);

        mAnimation = AnimationUtils.loadAnimation(App.getAppContext(), R.anim.animation_gift);
        imageView.setAnimation(mAnimation);
        mAnimation.start();

        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        },2500);


    }

    @Override
    public void onDissmiss() {
        this.dismiss();
    }

}
