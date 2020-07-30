package com.jfkj.im.TIM.modules.chat.layout.message.holder;

import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.utils.TUIKitConstants;

public class MessageCustomHolder extends MessageContentHolder implements ICustomMessageViewGroup {

    private MessageInfo mMessageInfo;
    private int mPosition;

    private TextView msgBodyText;
    private LinearLayout ly_chat;
    public MessageCustomHolder(View itemView) {
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
        msgBodyText.setTextSize(13.0f);
    }

    @Override
    public void layoutViews(MessageInfo msg, int position) {
        mMessageInfo = msg;
        mPosition = position;

        if(msg.isSelf()){
//            msgBodyText.setBackgroundResource(R.drawable.shape_chat_main);
//            msgBodyText.setTextColor(Color.parseColor("#ffffff"));

        }else{
//            ly_chat.setBackgroundResource(R.drawable.shape_chat_visitor);
//            msgBodyText.setTextColor(Color.parseColor("#333333"));

        }
        super.layoutViews(msg, position);
    }

    @Override
    public void layoutVariableViews(MessageInfo msg, int position) {
        msgBodyText.setVisibility(View.VISIBLE);

     //   msgBodyText.setText(Html.fromHtml(TUIKitConstants.covert2HTMLString("[不支持的自定义消息]")));

        if (msg.isSelf()) {
//            msgBodyText.setTextColor(Color.parseColor("#ffffff"));
//            msgBodyText.setBackgroundResource(R.drawable.shape_chat_main);
        } else {
//            msgBodyText.setTextColor(Color.parseColor("#333333"));
//            ly_chat.setBackgroundResource(R.drawable.shape_chat_visitor);
        }
    }

    private void hideAll() {
        for (int i = 0; i < ((RelativeLayout) rootView).getChildCount(); i++) {
            ((RelativeLayout) rootView).getChildAt(i).setVisibility(View.GONE);
        }
    }

    @Override
    public void addMessageItemView(View view) {
        hideAll();
        if (view != null) {
            ((RelativeLayout) rootView).removeView(view);
            if(mMessageInfo.isSelf()){
//                msgBodyText.setBackgroundResource(R.drawable.shape_chat_main);
//                msgBodyText.setTextColor(Color.parseColor("#ffffff"));
            }else {
//                view.setBackgroundResource(R.drawable.shape_chat_visitor);
            }
            ((RelativeLayout) rootView).addView(view);
        }
    }

    @Override
    public void addMessageContentView(View view) {
        // item有可能被复用，因为不能确定是否存在其他自定义view，这里把所有的view都隐藏之后重新layout
        hideAll();
        super.layoutViews(mMessageInfo, mPosition);

        if (view != null) {
            for (int i = 0; i < msgContentFrame.getChildCount(); i++) {
                msgContentFrame.getChildAt(i).setVisibility(View.GONE);
            }
            msgContentFrame.removeView(view);
            if(mMessageInfo.isSelf()){
//                msgBodyText.setBackgroundResource(R.drawable.shape_chat_main);
//                msgBodyText.setTextColor(Color.parseColor("#ffffff"));
            }else {
//                view.setBackgroundResource(R.drawable.shape_chat_visitor);
            }
            view.setPadding(15,9,15,9);
            msgContentFrame.addView(view);
        }
    }

}
