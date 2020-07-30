package com.jfkj.im.mvp.Home;

import com.jfkj.im.Bean.AppVersion;
import com.jfkj.im.Bean.TodayStarBean;
import com.jfkj.im.entity.BaseBean;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.mvp.BaseView;

import java.util.List;

import okhttp3.ResponseBody;

public interface HomeView extends BaseView {
    void getDataSuccess(ResponseBody s);

    void getDataFail(String msg);


    public void GetImSuccess(ResponseBody responseBody);

    public void GetImfail(String s);


    public void UserSigSuccess(ResponseBody responseBody);

    public void UserSigfail(String s);

    public void getgiftlist(String s);//获得礼物列表


    void showGiftList(List<GiftData.DataBean.ArrayBean> array);


    void loginOutSuccess(ResponseBody body);

    void showUpdateDialog(AppVersion version);

    void getTodayStar(List<TodayStarBean> todayStarBean);

}
