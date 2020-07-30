package com.jfkj.im.mvp.discovery.circle;

import com.jfkj.im.Bean.PraiseIdBean;
import com.jfkj.im.entity.CircleBean;
import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/2
 * </pre>
 */
public interface CircleView extends BaseView {

    void showDynamic(CircleBean circleBean);

    void showPraise(PraiseIdBean bean, int position,String b);

    void showSuccess(int position);

    void addAccessSpace();


    public void CirclefriendSuccess(ResponseBody responseBody);
}
