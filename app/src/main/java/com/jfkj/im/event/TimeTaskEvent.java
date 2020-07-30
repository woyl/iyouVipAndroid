package com.jfkj.im.event;

import com.jfkj.im.TIM.redpack.game.TimeTaskBean;

public class TimeTaskEvent {
    private TimeTaskBean bean;

    public TimeTaskEvent(TimeTaskBean bean) {
        this.bean = bean;
    }

    public TimeTaskBean getBean() {
        return bean;
    }

    public void setBean(TimeTaskBean bean) {
        this.bean = bean;
    }
}
