package com.jfkj.im.mvp.Recordvideo;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface RecordvideoView extends BaseView {

    public void uploadFileSuccess(ResponseBody responseBody);
    public void uploadFilefail(String string);

    public void uploadSuccess(ResponseBody responseBody);
    public void uploadfaail(String s);


    public void setExamine(String setExamine);
}
