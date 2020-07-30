package com.jfkj.im.mvp.mine;

import com.jfkj.im.entity.BindSettleAccountBean;
import com.jfkj.im.mvp.BaseView;

public interface CertificationView extends BaseView
{
    //实名认证成功
    void bindSuccess(BindSettleAccountBean bean);


    //文件上传成功
    void upFileSuccess(String filepath);

}
