package com.jfkj.im.mvp.Answersummary;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface AnswersummaryView  extends BaseView {
    public void sendSquareGameSuccess(ResponseBody responseBody);
}
