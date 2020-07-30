package com.jfkj.im.event;

public class ApplyJoinGroupEvent {
    private boolean applyJoin;

    public ApplyJoinGroupEvent(boolean applyJoin) {
        this.applyJoin = applyJoin;
    }

    public boolean isApplyJoin() {
        return applyJoin;
    }

    public void setApplyJoin(boolean applyJoin) {
        this.applyJoin = applyJoin;
    }
}
