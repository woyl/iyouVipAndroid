package com.jfkj.im.mvp.mine;

import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.entity.SettleAccountBean;
import com.jfkj.im.mvp.BaseView;

public interface SettleAccountView extends BaseView {
    void showSettlement(SettleAccountBean bean);

    void showList(ExChangeBean bean);

    void exchangeSuccess();
}
