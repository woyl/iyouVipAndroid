package com.jfkj.im.TIM.redpack.chatroom;

import android.content.Intent;
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
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
import com.tencent.imsdk.TIMValueCallBack;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.jfkj.im.TIM.helper.CustomMessage.RED_PACKAGE_MST_TYPE_THREE;

public class CustomAnswerController {
    public static void onDraw(ICustomMessageViewGroup parent, final CustomMessage data, MessageInfo info) {
        View view;
        // 把自定义消息view添加到TUIKit内部的父容器里
        if (info.isSelf()) {
            view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.chat_answer_right, null, false);
            parent.addMessageContentView(view);
        } else {
            view = LayoutInflater.from(App.getAppContext()).inflate(R.layout.chat_answer_left, null, false);
            parent.addMessageContentView(view);
        }

        TextView tv_content = view.findViewById(R.id.tv_content);
        if (data.getPackageCustom() != null && !TextUtils.isEmpty(data.getPackageCustom().getOrderId())){
            String type = SPUtils.getInstance(App.getAppContext()).getString(data.getPackageCustom().getOrderId());
            if (!TextUtils.isEmpty(type)){
                if (type.equals(RED_PACKAGE_MST_TYPE_THREE)){
                    tv_content.setText("已过期");
                }
            }
        }

        LinearLayout ll_content = view.findViewById(R.id.ll_content);
        ll_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info.isSelf()){
                    Intent intent = new Intent(App.getAppContext(),MyCharacterTextDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("gameId",data.getPackageCustom().getOrderId());
                    intent.putExtra("cadddate",data.getPackageCustom().getCadddate());
                    intent.putExtra("type","2");
                    App.getAppContext().startActivity(intent);
                }else {
                    AnswerUtrils.getChatSelfAnswer(data.getPackageCustom().getOrderId(), new TIMValueCallBack<List<AnswerSelfBean>>() {
                        @Override
                        public void onError(int code, String desc) {
                            ToastUtils.showShortToast(desc);
                        }

                        @Override
                        public void onSuccess(List<AnswerSelfBean> answerSelfBeans) {
                            Intent intent = new Intent(App.getAppContext(),MyCharacterTextDetailsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("gameId",data.getPackageCustom().getOrderId());
                            intent.putExtra("cadddate",data.getPackageCustom().getCadddate());
                            intent.putExtra("type","2");
                            App.getAppContext().startActivity(intent);
                        }
                    });

                }
            }
        });


//        CustomMessage customMessage=new CustomMessage();
//        // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
//        TextView textView = view.findViewById(R.id.tv_bottom);
//        textView.setText("性格测试");
    }
}
