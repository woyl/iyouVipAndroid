package com.jfkj.im.TIM.modules.chat;


import android.util.Log;

import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.chat.base.ChatManagerKit;


public class C2CChatManagerKit extends ChatManagerKit {

    private static final String TAG = C2CChatManagerKit.class.getSimpleName();
    private static C2CChatManagerKit mKit;
    private ChatInfo mCurrentChatInfo;

    private C2CChatManagerKit() {
        super.init();
    }

    public static C2CChatManagerKit getInstance() {

        if (mKit == null) {
            mKit = new C2CChatManagerKit();
        }
        return mKit;
    }

    @Override
    public void destroyChat() {
        super.destroyChat();
        mCurrentChatInfo = null;
        mIsMore = true;
    }

    @Override
    public ChatInfo getCurrentChatInfo() {
        return mCurrentChatInfo;
    }

    @Override
    public void setCurrentChatInfo(ChatInfo info) {
        super.setCurrentChatInfo(info);
        mCurrentChatInfo = info;
    }

    @Override
    protected boolean isGroup() {
        return false;
    }
}
