package com.jfkj.im.TIM.modules.chat.layout.message.holder;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.utils.DateTimeUtil;
import com.jfkj.im.TIM.utils.TUIKitConstants;

import java.util.Date;

public   class MessageReceivinggiftsHolder  extends MessageEmptyHolder {



    private TextView mChatTipsTv;

    public MessageReceivinggiftsHolder(View itemView) {
        super(itemView);
    }

    @Override
    public int getVariableLayout() {
        return R.layout.message_adapter_content_tips;
    }

    @Override
    public void initVariableViews() {
        mChatTipsTv = rootView.findViewById(R.id.chat_tips_tv);
    }

    @Override
    public void layoutViews(MessageInfo msg, int position) {
        super.layoutViews(msg, position);

    }

}
