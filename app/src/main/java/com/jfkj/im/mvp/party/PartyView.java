package com.jfkj.im.mvp.party;


import com.jfkj.im.Bean.AllMyPartysRedBean;
import com.jfkj.im.Bean.BaseBeans;
import com.jfkj.im.Bean.PartyBean;
import com.jfkj.im.mvp.BaseView;

public interface PartyView extends BaseView {

    void PartySuccess(BaseBeans<PartyBean> partyBeanBaseBeans);

    void PartyFails(String error);


    void myPartysRedTypeSuccess(AllMyPartysRedBean allMyPartysRedBean);
}
