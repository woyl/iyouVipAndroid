package com.jfkj.im.TIM.redpack;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.helper.TRTCDialog;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.event.RedPackageEvent;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import static com.jfkj.im.TIM.helper.CustomMessage.INVITE_CUS_TYPE_THREE;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PAGE_TYPE_CODE_FIV;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PAGE_TYPE_CODE_FOUR;

public class CustomSquareRedEnvelopeCollection {



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

        tvContent.setText(Html.fromHtml("我的红包已被领取完，点击" + "<font color=\"#FF2B66\">查看详情</font>"));




        tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                redId
                Intent intent = new Intent(mContext,RedPackageDetailsActivity.class);
                intent.putExtra("redId",data.getRedId());
                mContext.startActivity(intent);
            }
        });

    }
}
