package com.jfkj.im.TIM.redpack;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.jfkj.im.TIM.modules.message.MessageInfo;

/**
 * 单聊提示
 */
public class C2CCueController {


    @SuppressLint("SetTextI18n")
    public static void onDraw(ICustomMessageViewGroup parent, final CustomMessage data, MessageInfo info) {

        View view;
        // 把自定义消息view添加到TUIKit内部的父容器里
        view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.layout_tips_controller, null, false);
        parent.addMessageContentView(view);
        //CustomMessage customMessage=new CustomMessage();

        TextView textView = view.findViewById(R.id.tv_center);
        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转


                textView.setText("若在聊天中使用涉黄、暴力、辱骂等不文明用语将依法办理和永久封号");



    }
}
