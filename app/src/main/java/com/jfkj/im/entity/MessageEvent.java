package com.jfkj.im.entity;

/**
 * <pre>
 * Description:  eventBus  消息类型实体类
 * @author :   ys
 * @date :         2019/5/28
 * </pre>
 */
public class MessageEvent {

    public int code;
    public String mess;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public MessageEvent(int code, String mess) {
        this.code = code;
        this.mess = mess;
    }
}
