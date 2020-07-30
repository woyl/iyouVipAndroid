package com.jfkj.im.TIM.redpack;

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
    public static void onDraw(ICustomMessageViewGroup parent, final CustomMessage data, MessageInfo info) {
        View view;
        // 把自定义消息view添加到TUIKit内部的父容器里
        view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.layout_tips_controller, null, false);
        parent.addMessageContentView(view);
        CustomMessage customMessage=new CustomMessage();

        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        TextView textView = view.findViewById(R.id.tv_center);
        final String text = "不支持的自定义消息";
        if (data == null) {
            textView.setText(text);
        } else {
            textView.setText("已经领取了");
        }
    }
}
