package com.jfkj.im.TIM.redpack.ice;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.redpack.FriendsUtils;
import com.jfkj.im.event.GiveThumbsUpEvent;
import com.jfkj.im.utils.GlideEngine;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.view.GlideRoundTransform;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.ResponListener;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.tencent.imsdk.TIMValueCallBack;

import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_MSG_TYPE_ONE;

public class CustomIceController {

    public static void onDraw(final Activity activity, ICustomMessageViewGroup parent, final CustomMessage data, MessageInfo info) {
        List<LocalMedia> urlList = new ArrayList<>();
        View view = null;
        // 把自定义消息view添加到TUIKit内部的父容器里
        if (info.isSelf()) {
            view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.ice_right, null, false);
        } else {
            view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.ice_left, null, false);
        }
        parent.addMessageContentView(view);
        TextView tv_bottom = view.findViewById(R.id.tv_bottom);
        if (!TextUtils.isEmpty(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + data.getTaskId() + data.getSendId())) && TextUtils.equals(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + data.getTaskId() + data.getSendId()), READ_PACKAGE_MSG_TYPE_ONE)) {
            tv_bottom.setTextColor(ContextCompat.getColor(activity, R.color.cEF4769));
            Drawable img = ContextCompat.getDrawable(activity, R.mipmap.icom_fabulous_red);
            // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
            if (img != null) {
                img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            }
            tv_bottom.setCompoundDrawables(null, null, img, null);
            tv_bottom.setText("赞赏成功");

        } else {
            tv_bottom.setTextColor(ContextCompat.getColor(activity, R.color.text_color_gray));
            Drawable img = ContextCompat.getDrawable(activity, R.mipmap.icom_fabulous);
            // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
            if (img != null) {
                img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            }
            tv_bottom.setCompoundDrawables(null, null, img, null);
            tv_bottom.setText("点赞");
        }
        LinearLayout ll_1 = view.findViewById(R.id.ll_1);
        LinearLayout ll_content = view.findViewById(R.id.ll_content);
        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getTaskType().equals("1")) {
                    LocalMedia imageBean = new LocalMedia();
                    imageBean.setPath(data.getTaskURL());
                    urlList.add(imageBean);
                    PictureSelector.create(activity)
                            //.themeStyle(themeId) // xml设置主题
                            .setPictureStyle(new PictureParameterStyle())// 动态自定义相册主题
                            //.setPictureWindowAnimationStyle(animationStyle)// 自定义页面启动动画
                            .isNotPreviewDownload(true)// 预览图片长按是否可以下载
                            .loadImageEngine(GlideEngine.createGlideEngine())
                            .openExternalPreviewBack(0, urlList, new ResponListener<Boolean>() {
                                @Override
                                public void Rsp(Boolean s) {
                                    if (s){
                                        if (!info.isSelf()){
                                            if (TextUtils.isEmpty(SPUtils.getInstance(App.getAppContext()).getString( Utils.APPID + data.getTaskId() + data.getSendId()))) {
                                                EventBus.getDefault().postSticky(new GiveThumbsUpEvent(true, data.getTaskId(), data.getSendId(), data.getMoney(), data.getTaskContent(),data.getSendId()));
                                            } else if (!TextUtils.equals(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + data.getTaskId() + data.getSendId()), READ_PACKAGE_MSG_TYPE_ONE)){
                                                EventBus.getDefault().postSticky(new GiveThumbsUpEvent(true, data.getTaskId(), data.getSendId(), data.getMoney(), data.getTaskContent(),data.getSendId()));
                                            }
                                        }

                                    }
                                }
                            });   // 外部传入图片加载引擎，必传项
                    ;//
                } else {
                    PictureSelector.create(activity).externalPictureVideoBack(data.getTaskURL(), new ResponListener<Boolean>() {
                        @Override
                        public void Rsp(Boolean s) {
                            if (s){
                                if (!info.isSelf()){
                                    if (TextUtils.isEmpty(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + data.getTaskId() + data.getSendId()))) {
                                        EventBus.getDefault().postSticky(new GiveThumbsUpEvent(true, data.getTaskId(), data.getSendId(), data.getMoney(), data.getTaskContent(),data.getSendId()));
                                    } else if (!TextUtils.equals(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + data.getTaskId() + data.getSendId()), READ_PACKAGE_MSG_TYPE_ONE)){
                                        EventBus.getDefault().postSticky(new GiveThumbsUpEvent(true, data.getTaskId(), data.getSendId(), data.getMoney(), data.getTaskContent(),data.getSendId()));
                                    }
                                }
//                                FriendsUtils.checkFriend(data.getSendId(), new TIMValueCallBack<Boolean>() {
//                                    @Override
//                                    public void onError(int i, String s) {
//                                        ToastUtils.showLongToast(s);
//                                    }
//
//                                    @Override
//                                    public void onSuccess(Boolean aBoolean) {
//                                        if (aBoolean){
//                                            if (!info.isSelf()){
//                                                EventBus.getDefault().postSticky(new GiveThumbsUpEvent(true, data.getTaskId(), data.getSendId(), data.getMoney(), data.getTaskContent()));
//                                            }
//                                        }
//                                    }
//                                });
                            }
                        }
                    });
                }
        }
        });

        tv_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + data.getTaskId() + data.getSendId())) && TextUtils.equals(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + data.getTaskId() + data.getSendId()), READ_PACKAGE_MSG_TYPE_ONE)) {
                    ToastUtils.showLongToast("你已经点过赞了");
                } else {
                    EventBus.getDefault().postSticky(new GiveThumbsUpEvent(true, data.getTaskId(), data.getSendId(), data.getMoney(), data.getTaskContent(),data.getSendId()));
                }
            }
        });
        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        RequestOptions options = new RequestOptions()
                .centerCrop()
//                .placeholder(R.mipmap.ic_launcher_round) //预加载图片
//                .error(R.drawable.ic_launcher_foreground) //加载失败图片
                .priority(Priority.HIGH) //优先级
                .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存.
                .transform(new GlideRoundTransform(5)); //圆角
        TextView textView = view.findViewById(R.id.tv_content);
        ImageView img_1 = view.findViewById(R.id.img_1);
        textView.setText(data.getTaskContent());
        if (data.getTaskType().equals("1")) {
            Glide.with(App.getAppContext()).load(data.getTaskURL()).apply(options).into(img_1);
        } else {
            String url = data.getTaskURL().replace(".mp4", ".jpg");
            Glide.with(App.getAppContext()).load(url).apply(options).into(img_1);
        }

    }

}
