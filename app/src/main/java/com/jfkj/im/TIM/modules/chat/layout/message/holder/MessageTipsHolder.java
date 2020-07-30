package com.jfkj.im.TIM.modules.chat.layout.message.holder;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.redpack.TypesUtils;
import com.jfkj.im.TIM.utils.TUIKitConstants;

public class MessageTipsHolder extends MessageEmptyHolder {

    private TextView mChatTipsTv;

    public MessageTipsHolder(View itemView) {
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

        if (properties.getTipsMessageBubble() != null) {
            mChatTipsTv.setBackground(properties.getTipsMessageBubble());
        }
        if (properties.getTipsMessageFontColor() != 0) {
            mChatTipsTv.setTextColor(properties.getTipsMessageFontColor());
        }
        if (properties.getTipsMessageFontSize() != 0) {
            mChatTipsTv.setTextSize(properties.getTipsMessageFontSize());
        }

        if (msg.getStatus() == MessageInfo.MSG_STATUS_REVOKE) {

            if (msg.isSelf()) {
                msg.setExtra("您撤回了一条消息");
            } else if (msg.isGroup()) {
                String message = TUIKitConstants.covert2HTMLString(
                        (TextUtils.isEmpty(msg.getGroupNameCard())
                                ? msg.getFromUser()
                                : msg.getGroupNameCard()));
                msg.setExtra(message + "撤回了一条消息");
            } else {
                msg.setExtra("对方撤回了一条消息");
            }
        }

        if (msg.getStatus() == MessageInfo.MSG_STATUS_REVOKE || (msg.getMsgType() >= MessageInfo.MSG_TYPE_GROUP_CREATE
                && msg.getMsgType() <= MessageInfo.MSG_TYPE_GROUP_MODIFY_NOTICE)) {

            if (msg.getExtra() != null) {
                String colors = msg.getExtra().toString().replace("#7F7F7F","#EF4769");
                mChatTipsTv.setText(Html.fromHtml(colors));
                mChatTipsTv.setVisibility(View.VISIBLE);
            }
        }else {

            TypesUtils.setTipsType(msg,mChatTipsTv);
        }
    }

}
