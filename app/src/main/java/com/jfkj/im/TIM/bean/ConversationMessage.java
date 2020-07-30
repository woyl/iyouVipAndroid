package com.jfkj.im.TIM.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.jfkj.im.TIM.modules.conversation.base.ConversationInfo;
import com.jfkj.im.TIM.modules.message.MessageInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConversationMessage implements Parcelable {


    public static final int TYPE_COMMON = 1;
    public static final int TYPE_CUSTOM = 2;
    /**
     * 会话类型，自定义会话or普通会话
     */
    private int type;

    /**
     * 消息未读数
     */
    private int unRead;
    /**
     * 会话ID
     */
    private String conversationId;
    /**
     * 会话标识，C2C为对方用户ID，群聊为群组ID
     */
    private String id;
    /**
     * 会话头像url
     */


    protected ConversationMessage(Parcel in) {
        type = in.readInt();
        unRead = in.readInt();
        conversationId = in.readString();
        id = in.readString();
        title = in.readString();
        icon = in.readParcelable(Bitmap.class.getClassLoader());
        isGroup = in.readByte() != 0;
        top = in.readByte() != 0;
        lastMessageTime = in.readLong();
    }

    public static final Creator<ConversationMessage> CREATOR = new Creator<ConversationMessage>() {
        @Override
        public ConversationMessage createFromParcel(Parcel in) {
            return new ConversationMessage(in);
        }

        @Override
        public ConversationMessage[] newArray(int size) {
            return new ConversationMessage[size];
        }
    };



    /**
     * 会话标题
     */
    private String title;

    /**
     * 会话头像
     */
    private Bitmap icon;
    /**
     * 是否为群会话
     */
    private boolean isGroup;
    /**
     * 是否为置顶会话
     */
    private boolean top;
    /**
     * 最后一条消息时间
     */
    private long lastMessageTime;
    /**
     * 最后一条消息，MessageInfo对象
     */
    private MessageInfo lastMessage;

    public ConversationMessage() {

    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUnRead() {
        return unRead;
    }

    public void setUnRead(int unRead) {
        this.unRead = unRead;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    /**
     * 获得最后一条消息的时间，单位是秒
     */
    public long getLastMessageTime() {
        return lastMessageTime;
    }

    /**
     * 设置最后一条消息的时间，单位是秒
     * @param lastMessageTime
     */
    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MessageInfo getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(MessageInfo lastMessage) {
        this.lastMessage = lastMessage;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeInt(unRead);
        dest.writeString(conversationId);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeParcelable(icon, flags);
        dest.writeByte((byte) (isGroup ? 1 : 0));
        dest.writeByte((byte) (top ? 1 : 0));
        dest.writeLong(lastMessageTime);
    }
}
