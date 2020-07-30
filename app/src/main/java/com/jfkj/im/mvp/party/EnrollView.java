package com.jfkj.im.mvp.party;


import com.jfkj.im.Bean.BaseBean;
import com.jfkj.im.Bean.BaseBeans;
import com.jfkj.im.Bean.PartyBean;
import com.jfkj.im.mvp.BaseView;

public interface EnrollView extends BaseView {
    void joinPartySuccess(BaseBean baseBean);

    void joinPartyError();

}
