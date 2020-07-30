package com.jfkj.im.mvp.party;


import com.jfkj.im.Bean.BaseBean;
import com.jfkj.im.Bean.SignInBean;
import com.jfkj.im.mvp.BaseView;

public interface SignInView extends BaseView {
    void getSuccess(SignInBean signInBean);

    void getError();

    void confirmArrivalSuccess(BaseBean baseBean);
}
