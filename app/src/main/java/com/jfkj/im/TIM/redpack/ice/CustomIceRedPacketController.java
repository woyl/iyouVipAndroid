package com.jfkj.im.TIM.redpack.ice;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.event.JumpRedPackageDetailsEvent;
import com.jfkj.im.event.RedPackageIceEvent;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import static com.jfkj.im.TIM.helper.CustomMessage.INVITE_CUS_TYPE_THREE;
import static com.jfkj.im.TIM.helper.CustomMessage.READ_PACKAGE_CUS_TYPE_SIX;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PAGE_TYPE_CODE_FIV;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PAGE_TYPE_CODE_FOUR;

public class CustomIceRedPacketController {

    public static void onDraw(ICustomMessageViewGroup parent, final CustomMessage data, MessageInfo info) {
        View view = null;
        // 把自定义消息view添加到TUIKit内部的父容器里
        if (info.isSelf()) {
            view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.layout_read_package, null, false);
            parent.addMessageContentView(view);
        } else {
            view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.layout_red_left_package, null, false);
            parent.addMessageContentView(view);
        }
        TextView tv_state = view.findViewById(R.id.tv_state);
        tv_state.setVisibility(View.VISIBLE);
        tv_state.setText("点击打开");
        if (!TextUtils.isEmpty(data.getRedId())
                && !TextUtils.isEmpty(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID+data.getRedId()))) {
            switch (SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + data.getRedId())) {
                case INVITE_CUS_TYPE_THREE:
                    tv_state.setText("红包已过期");
                    break;
                case RED_PAGE_TYPE_CODE_FOUR:
                    tv_state.setText("红包已领过");
                    break;
                case RED_PAGE_TYPE_CODE_FIV:
                    tv_state.setText("红包已领完");
                    break;
                case READ_PACKAGE_CUS_TYPE_SIX:
                    tv_state.setText("红包被领取");
                    break;
            }
            view.setAlpha(0.5f);
        }else {
            view.setAlpha(1f);
        }
        LinearLayout fl_read_pack = view.findViewById(R.id.fl_read_pack);
        fl_read_pack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent("com.jfkj.im.red.package");
//                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(App.getAppContext());
//                localBroadcastManager.sendBroadcast(intent);
                if (!info.isSelf()){
                    EventBus.getDefault().post(new RedPackageIceEvent(data,info));
                } else {
                    EventBus.getDefault().post(new JumpRedPackageDetailsEvent(data.getRedId(),info.getFromUser(),"4"));
                }
            }
        });
        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        TextView textView = view.findViewById(R.id.tv_content);
        TextView tv_type = view.findViewById(R.id.tv_type);
        tv_type.setText("奖励红包");
        textView.setText("谢谢你完成我的游戏");
    }
}
