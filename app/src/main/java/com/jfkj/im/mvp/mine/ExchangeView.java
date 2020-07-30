package com.jfkj.im.mvp.mine;

import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.entity.SettleAccountBean;
import com.jfkj.im.mvp.BaseView;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/1/16
 * </pre>
 */
public interface ExchangeView extends BaseView {
    void showList(ExChangeBean bean);

    void exchangeSuccess();

    void showSettlement(SettleAccountBean bean);
}
