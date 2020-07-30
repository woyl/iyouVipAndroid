package com.jfkj.im.mvp.Minecharacter;

import android.view.View;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface MinecharacterView extends BaseView {

    public void MinecharacterSuccess(String responseBody);
    public void loadSquareGameQuestionSuccess(ResponseBody responseBody);
}
