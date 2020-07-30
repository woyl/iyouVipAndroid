package com.jfkj.im.TIM.modules.chat.layout.message.holder;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.component.face.FaceManager;
import com.jfkj.im.TIM.modules.chat.GroupChatManagerKit;
import com.jfkj.im.TIM.modules.chat.base.AbsChatLayout;
import com.jfkj.im.TIM.modules.chat.base.ChatManagerKit;
import com.jfkj.im.TIM.modules.chat.layout.input.InputLayout;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.utils.BackgroundTasks;
import com.jfkj.im.TIM.utils.ToastUtil;

public class MessageTextHolder extends MessageContentHolder {

    private TextView msgBodyText;
    LinearLayout ly_chat ;
    public MessageTextHolder(View itemView) {
        super(itemView);
    }
    @Override
    public int getVariableLayout() {
        return R.layout.message_adapter_content_text;
    }
    @Override
    public void initVariableViews() {
        msgBodyText = rootView.findViewById(R.id.msg_body_tv);
        ly_chat = rootView.findViewById(R.id.ly_chat);
    }
    @Override
    public void layoutVariableViews(MessageInfo msg, int position) {
        msgBodyText.setVisibility(View.VISIBLE);

        if (msg.isSelf()) {
            if (properties.getRightChatContentFontColor() != 0) {
//                msgBodyText.setTextColor(properties.getRightChatContentFontColor());
            }
            msgBodyText.setTextColor(Color.parseColor("#ffffff"));
           ly_chat.setBackgroundResource(R.drawable.shape_chat_main);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ly_chat.getLayoutParams();
            layoutParams.leftMargin=5;
            layoutParams.rightMargin=0;
            ly_chat.setLayoutParams(layoutParams);
        } else {
            if (properties.getLeftChatContentFontColor() != 0) {
             //   msgBodyText.setTextColor(properties.getLeftChatContentFontColor());
            }
            msgBodyText.setTextColor(Color.parseColor("#000000"));
           ly_chat.setBackgroundResource(R.drawable.shape_chat_visitor);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ly_chat.getLayoutParams();
            layoutParams.rightMargin=5;
            layoutParams.leftMargin=0;
            ly_chat.setLayoutParams(layoutParams);
        }
        FaceManager.handlerEmojiText(msgBodyText, msg.getExtra().toString(), false);
    }
}
