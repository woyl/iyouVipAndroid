package com.jfkj.im.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jfkj.im.App;
import com.jfkj.im.Bean.ShareBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.adapter.ShareAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.utils.FileUtils;
import com.jfkj.im.utils.QrCodeUtil;
import com.lzy.okgo.utils.OkLogger;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import butterknife.BindView;

public class Sharedialog extends Dialog implements View.OnClickListener, OnItemClickListener {
    AppCompatTextView tv_cancel,tv_share;
    AppCompatImageView  iv_head;
    SwipeRecyclerView swiperecyclerview;
    ShareAdapter shareAdapter;
    List<ShareBean> list;
    private ImageView img;
    private Bitmap qrbitmap;
    private Context context;
    private View viewById;
    private ShareAdapter.OnShareItemClick onShareItemClick;


    public Sharedialog(@NonNull Context context, ShareAdapter.OnShareItemClick click) {
        super(context,R.style.dialogstyle);
        this.onShareItemClick = click;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareAdapter=new ShareAdapter(getContext(),onShareItemClick);
        list=new ArrayList<>();
        list.add(new ShareBean("微信",R.drawable.share_icon_wechat_nor));
        list.add(new ShareBean("朋友圈",R.drawable.share_icon_moments_nor));
        setContentView(R.layout.dialog_share);
        tv_cancel=findViewById(R.id.tv_cancel);
        swiperecyclerview=findViewById(R.id.swiperecyclerview);

        viewById = findViewById(R.id.rl_layout);
        shareAdapter.setShareBeanList(list);
        swiperecyclerview.setLayoutManager(new GridLayoutManager(getContext(),2));
        swiperecyclerview.setOnItemClickListener(this);
        swiperecyclerview.setAdapter(shareAdapter);
        tv_share=findViewById(R.id.tv_share);
        tv_cancel.setOnClickListener(this);
        tv_share.setOnClickListener(this);
        iv_head=findViewById(R.id.iv_head);
        Glide.with(App.getAppContext()).load(UserInfoManger.getMineUserHeadUrl()).into(iv_head);

        img = findViewById(R.id.iv_qrcode);
        findViewById(R.id.iv_down).setOnClickListener(this);



    }
    @Override
    public void show() {
        super.show();
        img.setImageBitmap(qrbitmap);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_share:

                break;
            case R.id.iv_down:

                Bitmap bitmap = captureView(viewById);


                saveImageToGallery(context,bitmap);
//                Glide.with(App.getAppContext())
//                        .load(bitmap)
//                        .into(new SimpleTarget<Drawable>() {
//                            @Override
//                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                                boolean b = FileUtils.saveImageToGallery(App.getAppContext(), FileUtils.drawableToBitmap(resource), System.currentTimeMillis() + ".jpg");
//                                if(b){
//
//                                    ToastUtils.showLong("图片保存成功");
//                                }
//
//                            }
//                        });

                break;
        }
    }


    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                    file.getAbsolutePath(), fileName, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        ToastUtils.showShort("图片保存成功");
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
    }



    public Bitmap captureView(View view) {
        // 根据View的宽高创建一个空的Bitmap
        Bitmap bitmap = Bitmap.createBitmap(
                view.getWidth(),
                view.getHeight(),
                Bitmap.Config.RGB_565);
        // 利用该Bitmap创建一个空的Canvas
        Canvas canvas = new Canvas(bitmap);
        // 绘制背景(可选)
        canvas.drawColor(Color.WHITE);
        // 将view的内容绘制到我们指定的Canvas上
        view.draw(canvas);
        return bitmap;
    }


    public   int dp2Px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    public void setQrString(String str){



        qrbitmap = QrCodeUtil.createQRImage(str, dp2Px(120), dp2Px(120));

    }

    @Override
    public void onItemClick(View view, int adapterPosition) {
        if(adapterPosition==1){

        }
    }

}
