package com.jfkj.im.TIM.redpack.chatroom;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.redpack.RedPackageCustom;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomController {

    public static String[] onNewMessage(List<TIMMessage> msgs, Context context) {
        String[] messageUrl = new String[3];
        List<RedPackageCustom> messageList = new ArrayList<>();
        CustomMessage customMessage = null;
        if (msgs != null) {
            for (TIMMessage msg : msgs) {
                TIMConversation conversation = msg.getConversation();
                TIMConversationType type = conversation.getType();
                List<MessageInfo> list = MessageInfoUtil.TIMMessage2MessageInfo(msg, false);
                for (MessageInfo info : list) {
                    if (info != null){
                        messageUrl[0] = info.getFromUser();
                        if (info.getExtra() != null && !TextUtils.isEmpty(info.getExtra().toString())){
                            messageUrl[1] = info.getExtra().toString();
                        }else {
                            messageUrl[1] = "";
                        }
                        boolean isself = info.isSelf();
                        if (isself){
                            messageUrl[2] = "1";
                        }else {
                            messageUrl[2] = "0";
                        }
                    }
                    if (info.getMsgType() != MessageInfo.MSG_TYPE_CUSTOM) {
                        continue;
                    }
                    // 获取到自定义消息的json数据
                    if (!(info.getElement() instanceof TIMCustomElem)) {
                        continue;
                    }
                    TIMCustomElem elem = (TIMCustomElem) info.getElement();
                    // 自定义的json数据，需要解析成bean实例
                    try {
                        customMessage = new Gson().fromJson(new String(elem.getData()), CustomMessage.class);
//                        messageUrl[0] = customMessage.getSendId();
//                        messageUrl[1] = customMessage.getText();
                    } catch (Exception e) {
                        Log.e("msg", "invalid json: " + new String(elem.getData()) + " " + e.getMessage());
                    }
                }
            }
        }
        return messageUrl;
    }
}
