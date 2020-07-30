package com.jfkj.im.TIM.redpack;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.jfkj.im.TIM.modules.message.MessageInfo;

public class CustomSquareAtCollection {



    public static void onDraw(ICustomMessageViewGroup parent, final CustomMessage data, MessageInfo info, Context mContext) {
        View view = null;
        // 把自定义消息view添加到TUIKit内部的父容器里
        if (info.isSelf()) {
            view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.item_custom_tv_square_red_evelope, null, false);


            parent.addMessageContentView(view);
        } else {
            view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.item_custom_tv_square_red_evelope_left, null, false);
            parent.addMessageContentView(view);
        }



        TextView tvContent = view.findViewById(R.id.tv_content);

        tvContent.setText(data.getText());






    }
}
