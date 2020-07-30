package com.jfkj.im.mvp.Auditing;

import com.jfkj.im.mvp.BaseView;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.http.PUT;

public interface AuditingView extends BaseView {
    //认证页面  在提交适配上去之后  马上将所有的认证信息提交上去

    public void uploadSuccess(ResponseBody responseBody);
    public void uploadfail(String string);


    public void uploadFileSuccess(ResponseBody responseBody);
    public void uploadFilefail(String string);
}
