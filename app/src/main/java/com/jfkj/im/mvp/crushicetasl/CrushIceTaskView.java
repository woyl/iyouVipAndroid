package com.jfkj.im.mvp.crushicetasl;

import com.jfkj.im.Bean.BaseBeans;
import com.jfkj.im.Bean.CrushIceTaskHallBean;
import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface CrushIceTaskView extends BaseView {
    void getCrushIce(BaseBeans<CrushIceTaskHallBean> crushIceTaskHallBean);

    void getCrushFails(String error);

    void upLoadPic(String s);

    void addFriend();

}
