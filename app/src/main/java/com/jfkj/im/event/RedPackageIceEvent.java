package com.jfkj.im.event;


import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.message.MessageInfo;

public class RedPackageIceEvent {

    private CustomMessage customMessage;
    private MessageInfo info;

    public RedPackageIceEvent(CustomMessage customMessage, MessageInfo info) {
        this.customMessage = customMessage;
        this.info = info;
    }

    public CustomMessage getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(CustomMessage customMessage) {
        this.customMessage = customMessage;
    }

    public MessageInfo getInfo() {
        return info;
    }

    public void setInfo(MessageInfo info) {
        this.info = info;
    }
}
