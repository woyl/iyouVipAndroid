package com.jfkj.im.event;

public class CommentEvent {
    private int position;
    private int count;

    public CommentEvent(int position,int count) {
        this.position = position;
        this.count = count;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
