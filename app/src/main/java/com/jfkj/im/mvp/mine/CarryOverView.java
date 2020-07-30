package com.jfkj.im.mvp.mine;

import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.entity.CardInfoBean;

public interface CarryOverView {
    void showBankCardInfo(CardInfoBean bean);

    void settlementSuccess(BaseResponse response);
}
