package com.jfkj.im.mvp.replacequestion;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface ReplaceQuestionView  extends BaseView {


    public void ReplaceQuestionSuccess(ResponseBody responseBody);

    public void getTestQuestionSuccess(ResponseBody responseBody);
    public void submitUserAnswerSuccess(ResponseBody responseBody);

}
