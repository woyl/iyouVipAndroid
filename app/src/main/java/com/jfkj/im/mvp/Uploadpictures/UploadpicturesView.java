package com.jfkj.im.mvp.Uploadpictures;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface UploadpicturesView extends BaseView {

    public void uploadImageSuccess(ResponseBody responseBody);
    public void uploadImagefail(String s);
}
