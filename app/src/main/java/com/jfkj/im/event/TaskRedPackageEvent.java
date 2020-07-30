package com.jfkj.im.event;

public class TaskRedPackageEvent {
    private boolean isTaskRedpage;

    public TaskRedPackageEvent(boolean isTaskRedpage) {
        this.isTaskRedpage = isTaskRedpage;
    }

    public boolean isTaskRedpage() {
        return isTaskRedpage;
    }

    public void setTaskRedpage(boolean taskRedpage) {
        isTaskRedpage = taskRedpage;
    }
}
