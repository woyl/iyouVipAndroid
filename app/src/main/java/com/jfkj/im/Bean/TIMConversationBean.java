package com.jfkj.im.Bean;

import com.tencent.imsdk.TIMConversation;

public class TIMConversationBean   {
    TIMConversation timConversation;
    int viprank;

    public TIMConversation getTimConversation() {
        return timConversation;
    }

    public void setTimConversation(TIMConversation timConversation) {
        this.timConversation = timConversation;
    }

    public int getViprank() {
        return viprank;
    }

    public void setViprank(int viprank) {
        this.viprank = viprank;
    }
}

