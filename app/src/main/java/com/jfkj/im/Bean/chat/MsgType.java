package com.jfkj.im.Bean.chat;

/**
 * 消息类型
 */
public enum MsgType {
    TEXT,//文本消息
    AUDIO,//语音消息
    VIDEO,//视频消息
    IMAGE,//图片消息
    FILE,//文件消息
    LOCATION, //位置消息
    TIME, //时间消息  包含了加群进群的
    READPACKET, //红包消息
    GIFT,//礼物的消息
    SENDVIDEOCALL,//视频通话
    CHARACTER,//性格类型的
}