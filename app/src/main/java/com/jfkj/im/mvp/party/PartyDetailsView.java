package com.jfkj.im.mvp.party;

import com.jfkj.im.Bean.PartyInfoBean;
import com.jfkj.im.mvp.BaseView;

public interface PartyDetailsView extends BaseView {
    void getPartyDetailsSuccess(PartyInfoBean partyInfoBean);

}
