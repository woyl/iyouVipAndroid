package com.jfkj.im.event;

import com.jfkj.im.TIM.modules.message.MessageInfo;

public class MessageFailEvent {
    private MessageInfo msg;

    public MessageFailEvent(MessageInfo msg) {
        this.msg = msg;
    }

    public MessageInfo getMsg() {
        return msg;
    }

    public void setMsg(MessageInfo msg) {
        this.msg = msg;
    }
}
