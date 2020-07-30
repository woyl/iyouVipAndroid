package com.jfkj.im.TIM.redpack.tips;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.jfkj.im.TIM.modules.message.MessageInfo;

public class CustomTipsController {
    @SuppressLint("SetTextI18n")
    public static void onDraw(ICustomMessageViewGroup parent, final CustomMessage data, MessageInfo info) {
        View view;
        // 把自定义消息view添加到TUIKit内部的父容器里
        view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.layout_tips_controller, null, false);
        parent.addMessageContentView(view);
        //CustomMessage customMessage=new CustomMessage();

        TextView textView = view.findViewById(R.id.tv_center);
        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        if (data.getPackageCustom().getTipsType().equals("1")){
            if (data.getPackageCustom().getRedIsDone().equals("0")){
                if (info.isSelf()){
                    textView.setText("你领取了自己红包");
                }else {
                    textView.setText("。。。领取了你的红包");
                }
            }else {
                if (info.isSelf()){
                    textView.setText("xx领取了你的红包");
                }else {
                    textView.setText("你领取了xx的红包");
                }
            }
        }
    }
}
