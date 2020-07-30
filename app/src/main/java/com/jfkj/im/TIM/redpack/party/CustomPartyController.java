package com.jfkj.im.TIM.redpack.party;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.redpack.chatroom.AnswerSelfBean;
import com.jfkj.im.TIM.redpack.chatroom.AnswerUtrils;
import com.jfkj.im.ui.party.PartyDetailsActivity;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.tencent.imsdk.TIMValueCallBack;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PACKAGE_MST_TYPE_THREE;

public class CustomPartyController {
    public static void onDraw(ICustomMessageViewGroup parent, final CustomMessage data, MessageInfo info, Context mContext) {
        View view;
        // 把自定义消息view添加到TUIKit内部的父容器里
        if (info.isSelf()) {
            view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.right_chat_party, null, false);
            TextView tv_content = view.findViewById(R.id.tv_content);

            tv_content.setVisibility(View.GONE);

            parent.addMessageContentView(view);
        } else {
            view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.right_chat_party, null, false);
            TextView tv_content = view.findViewById(R.id.tv_content);


            parent.addMessageContentView(view);
        }



        ImageView img = view.findViewById(R.id.img_1);
        Glide.with(mContext).load(data.getPagePhoto()).into(img);



        LinearLayout ll_content = view.findViewById(R.id.ll_content);
        ll_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.getAppContext(), PartyDetailsActivity.class);
                intent.putExtra("partyId",data.getPartyId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                App.getAppContext().startActivity(intent);
            }
        });


    }
}
