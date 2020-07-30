package com.jfkj.im.TIM.redpack.game;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.luck.picture.lib.PictureSelector;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

public class CustomGameTaskController {

    public static void onDraw(Activity activity,ICustomMessageViewGroup parent, final CustomMessage data, MessageInfo info) {
        View view;
        // 把自定义消息view添加到TUIKit内部的父容器里
        if (info.isSelf()) {
            view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.layout_sub_task_right, null, false);
            parent.addMessageContentView(view);
        } else {
            view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.layout_sub_task_left, null, false);
            parent.addMessageContentView(view);
        }
        LinearLayout ll_task = view.findViewById(R.id.ll_task);
        ll_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(activity).externalPictureVideo(data.getPackageCustom().getTaskUrl());
//                EventBus.getDefault().post(info);
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(data.getPackageCustom().getTaskUrl());
//                intent.setData(content_url);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                App.getAppContext().startActivity(intent);
            }
        });
        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        TextView textView = view.findViewById(R.id.tv_content);
        if (data.getPackageCustom() != null && !TextUtils.isEmpty(data.getPackageCustom().getAvatarUrl())){
//            Glide
//                    .with( App.getAppContext() )
//                    .load( Uri.fromFile( new File( data.getPackageCustom().getAvatarUrl() ) ) )
//                    .into( (ImageView) view.findViewById(R.id.img_head));
            Glide.with(App.getAppContext()).load(data.getPackageCustom().getTaskImage()).into((ImageView) view.findViewById(R.id.img_head));
        }
        final String text = "不支持的自定义消息";
        if (data == null) {
            textView.setText(text);
        } else {
            if (data.getPackageCustom()!= null && !TextUtils.isEmpty(data.getPackageCustom().getRedDesc())){
                textView.setText(data.getRedDesc());
            }
        }
    }
}
