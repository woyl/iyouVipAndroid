package com.jfkj.im.TIM.redpack;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.jfkj.im.TIM.modules.message.MessageInfo;

import org.greenrobot.eventbus.EventBus;

public class CustomSendGifController {
    private static final String TAG = CustomRedPackController.class.getSimpleName();


    public static void onDraw(ICustomMessageViewGroup parent, final CustomMessage data, MessageInfo info) {
        View view;
        ImageView imageView;
        // 把自定义消息view添加到TUIKit内部的父容器里
        view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.message_adapter_content_gift, null, false);
        parent.addMessageContentView(view);
        CustomMessage customMessage=new CustomMessage();
        imageView=view.findViewById(R.id.iv_gift);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
        TextView textView = view.findViewById(R.id.tv_title);

        final String text = "不支持的自定义消息";
        if (data == null) {
            textView.setText(text);
        } else {
            if (data.getSendRedPackageBean()!= null && !TextUtils.isEmpty(data.getSendRedPackageBean().getSendWord())){
                textView.setText("点击拆开礼物");
            }
        }
    }
}
