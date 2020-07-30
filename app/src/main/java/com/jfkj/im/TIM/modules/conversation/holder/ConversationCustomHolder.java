package com.jfkj.im.TIM.modules.conversation.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.conversation.base.ConversationIconView;
import com.jfkj.im.TIM.modules.conversation.base.ConversationInfo;
import com.jfkj.im.utils.Utils;


/**
 * 自定义会话Holder
 */
public class ConversationCustomHolder extends ConversationBaseHolder {

    protected LinearLayout leftItemLayout;
    protected TextView titleText;
    protected TextView messageText;
    protected TextView timelineText;
    protected TextView unreadText;
    protected ConversationIconView conversationIconView;


    public ConversationCustomHolder(View itemView) {
        super(itemView);
        leftItemLayout = rootView.findViewById(R.id.item_left);
        conversationIconView = rootView.findViewById(R.id.conversation_icon);
        titleText = rootView.findViewById(R.id.conversation_title);
        messageText = rootView.findViewById(R.id.conversation_last_msg);
        timelineText = rootView.findViewById(R.id.conversation_time);
        unreadText = rootView.findViewById(R.id.conversation_unread);
    }

    @Override
    public void layoutViews(ConversationInfo conversation, int position) {
        if (conversation.isTop()) {
            leftItemLayout.setBackgroundColor(rootView.getResources().getColor(R.color.c111111));
        } else {
            leftItemLayout.setBackgroundColor(rootView.getResources().getColor(R.color.c111111));
        }
        conversationIconView.setConversation(conversation);
        if(conversation.getId().equals(Utils.SYSTEMID)){
            conversationIconView.setBitmapResId(R.mipmap.system_iv);
        }

        titleText.setText(conversation.getTitle());
        messageText.setText("");
        timelineText.setText("");

        if (conversation.getUnRead() > 0) {
            unreadText.setVisibility(View.VISIBLE);
            if (conversation.getUnRead() > 99) {
                unreadText.setText("99+");
            } else {
                unreadText.setText("" + conversation.getUnRead());
            }
        } else {
            unreadText.setVisibility(View.GONE);
        }

        if (mAdapter.getItemDateTextSize() != 0) {
            timelineText.setTextSize(mAdapter.getItemDateTextSize());
        }
        if (mAdapter.getItemBottomTextSize() != 0) {
            messageText.setTextSize(mAdapter.getItemBottomTextSize());
        }
        if (mAdapter.getItemTopTextSize() != 0) {
            titleText.setTextSize(mAdapter.getItemTopTextSize());
        }

    }

}
