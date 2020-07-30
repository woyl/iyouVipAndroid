package com.jfkj.im.event;

public class GroupUnreadMessageNumEvent {
    private long UnreadMessageNum;
    private String id;



    public GroupUnreadMessageNumEvent(long unreadMessageNum,String id) {
        this.UnreadMessageNum = unreadMessageNum;
        this.id = id;
    }

    public long getUnreadMessageNum() {
        return UnreadMessageNum;
    }

    public void setUnreadMessageNum(long unreadMessageNum) {
        UnreadMessageNum = unreadMessageNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
