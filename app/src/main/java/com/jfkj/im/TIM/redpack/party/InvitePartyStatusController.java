package com.jfkj.im.TIM.redpack.party;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;

import org.json.JSONObject;

/**
 * 邀请聚会
 */
public class InvitePartyStatusController {




    @SuppressLint("SetTextI18n")
    public static void onDraw(ICustomMessageViewGroup parent, final CustomMessage data, MessageInfo info) {

        View view;
        // 把自定义消息view添加到TUIKit内部的父容器里
        view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.layout_tips_controller, null, false);
        parent.addMessageContentView(view);
        //CustomMessage customMessage=new CustomMessage();

        TextView textView = view.findViewById(R.id.tv_center);
        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转


//        View view;
//        // 把自定义消息view添加到TUIKit内部的父容器里
        if (info.isSelf()) {
//            view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.item_invite_party_status, null, false);
//            parent.addMessageContentView(view);
        } else {

            if(data.getAgreeOrReject().equals("1")){
                textView.setText("对方同意了你的报名申请");
            }else{
                textView.setText("对方同意了你的报名申请");
            }

        }


    }


}
