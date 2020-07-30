package com.jfkj.im.TIM.modules.chat.layout.message.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.message.MessageInfo;

public class MessageGiftHolder extends MessageContentHolder{
    private TextView msgBodyText;
    LinearLayout ly_chat ;
    public MessageGiftHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void layoutVariableViews(MessageInfo msg, int position) {
        if (msg.isSelf()) {

        }else {

        }
    }

    @Override
    public int getVariableLayout() {
        return R.layout.message_adapter_content_gift;
    }

    @Override
    public void initVariableViews() {

    }
}
