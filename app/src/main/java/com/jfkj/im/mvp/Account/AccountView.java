package com.jfkj.im.mvp.Account;

import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.entity.AddMoneyBean;
import com.jfkj.im.entity.BaseResponse;
import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface AccountView extends BaseView {

      void AccountSuccess(String s);

      void showList(ExChangeBean bean);

      void playSuccess(AddMoneyBean response);

      void accountBalanceSuccess();
}
