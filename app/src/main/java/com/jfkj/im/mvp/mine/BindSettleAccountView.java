package com.jfkj.im.mvp.mine;

import com.jfkj.im.entity.BindSettleAccountBean;
import com.jfkj.im.mvp.BaseView;

public interface BindSettleAccountView extends BaseView {
    //绑定成功
    void bindSuccess(BindSettleAccountBean bean);

    //获取绑定信息
    void getBindSuccess(String s);
}
