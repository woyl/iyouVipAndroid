package com.jfkj.im.mvp.mine;

import com.jfkj.im.entity.CarryScaleBean;
import com.jfkj.im.mvp.BaseView;

public interface CarryScaleView extends BaseView {
    //用户结算比例
    void onUserCashRate(CarryScaleBean bean);

}
