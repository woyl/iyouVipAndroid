package com.jfkj.im.TIM.redpack;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.event.RedPackageEvent;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import static com.jfkj.im.TIM.helper.CustomMessage.INVITE_CUS_TYPE_THREE;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PACKAGE_STATES_ONE;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PACKAGE_STATES_ZERO;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PAGE_TYPE_CODE_FIV;
import static com.jfkj.im.TIM.helper.CustomMessage.RED_PAGE_TYPE_CODE_FOUR;

public class CustomEveryDayRedpackageController {

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
        if (data.getPackageCustom() != null
                && !TextUtils.isEmpty(data.getPackageCustom().getRedId())
                && !TextUtils.isEmpty(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID+data.getPackageCustom().getRedId()))) {
            tv_state.setVisibility(View.VISIBLE);
            switch (SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + data.getPackageCustom().getRedId())) {
                case INVITE_CUS_TYPE_THREE:
                    tv_state.setText("红包已过期");
                    break;
                case RED_PAGE_TYPE_CODE_FOUR:
                    tv_state.setText("红包已领过");
                    break;
                case RED_PAGE_TYPE_CODE_FIV:
                    tv_state.setText("红包已领完");
                    break;
            }
            view.setAlpha(0.5f);
        }else {
            tv_state.setVisibility(View.INVISIBLE);
            view.setAlpha(1f);
        }
        LinearLayout fl_read_pack = view.findViewById(R.id.fl_read_pack);
        fl_read_pack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new RedPackageEvent(data,info));
            }
        });
        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        TextView textView = view.findViewById(R.id.tv_content);
        TextView tv_type = view.findViewById(R.id.tv_type);
        tv_type.setText("每日红包");
        final String text = "不支持的自定义消息";
        if (data == null) {
            textView.setText(text);
        } else {
            if (data.getPackageCustom() != null && !TextUtils.isEmpty(data.getPackageCustom().getRedDesc())) {
                textView.setText(data.getPackageCustom().getRedDesc());
            }
        }
    }
}
