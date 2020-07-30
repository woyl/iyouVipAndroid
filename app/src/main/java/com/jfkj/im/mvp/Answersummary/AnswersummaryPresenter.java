package com.jfkj.im.mvp.Answersummary;

import com.jfkj.im.App;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class AnswersummaryPresenter extends BasePresenter<AnswersummaryView> {

    public AnswersummaryPresenter(AnswersummaryView answersummaryView){
        attachView(answersummaryView);
    }
    public void sendSquareGame(String questionIds,String answerIds,String money){
        Map<String, String> map = new HashMap<>();
        map.put(Utils.OSNAME, Utils.ANDROID);
        map.put(Utils.CHANNEL, Utils.ANDROID);
        map.put(Utils.APPVERSION, Utils.getVersionCode() + "");

        map.put(Utils.QUESTIONIDS,questionIds);
        map.put(Utils.ANSWERIDS, answerIds);
        map.put(Utils.MONEY, money);

        addSubscription(apiStores.sendSquareGame(map), new ApiCallback<ResponseBody>() {
            @Override
            public void onSuccess(ResponseBody model) {
                mvpView.sendSquareGameSuccess(model);
            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onFinish() {

            }
        });
    }
}
