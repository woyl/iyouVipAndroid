package com.jfkj.im.mvp.party;


import com.jfkj.im.Bean.BaseBean;
import com.jfkj.im.Bean.SignInBean;
import com.jfkj.im.mvp.BaseView;

public interface ReleasePartyView extends BaseView {
    void playSuccess(BaseBean bean);

    void playError();
}
