package com.jfkj.im.event;

import com.jfkj.im.TIM.redpack.chatroom.AnswerSelfBean;

import java.util.List;

public class MatchCharacterEvent {
    private List<AnswerSelfBean> answerSelfBeans;

    public MatchCharacterEvent(List<AnswerSelfBean> answerSelfBeans) {
        this.answerSelfBeans = answerSelfBeans;
    }

    public List<AnswerSelfBean> getAnswerSelfBeans() {
        return answerSelfBeans;
    }

    public void setAnswerSelfBeans(List<AnswerSelfBean> answerSelfBeans) {
        this.answerSelfBeans = answerSelfBeans;
    }
}
