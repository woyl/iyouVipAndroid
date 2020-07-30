package com.jfkj.im.TIM.redpack.tips;

import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.utils.TUIKitLog;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMValueCallBack;

public class SendmessageUtils {

    public static void sendTipsMessage(TIMConversation conversation, TIMMessage message, final IUIKitCallBack callBack) {
        conversation.sendMessage(message, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(final int code, final String desc) {
                TUIKitLog.e("msg", "sendTipsMessage fail:" + code + "=" + desc);
                if (callBack != null) {
                    callBack.onError("msg", code, desc);
                }
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                TUIKitLog.e("msg", "sendTipsMessage onSuccess");
                if (callBack != null) {
                    callBack.onSuccess(timMessage);
                }
            }
        });
    }
}
