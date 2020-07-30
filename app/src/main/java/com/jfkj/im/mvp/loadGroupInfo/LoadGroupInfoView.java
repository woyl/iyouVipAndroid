package com.jfkj.im.mvp.loadGroupInfo;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface LoadGroupInfoView extends BaseView {

    public void LoadGroupInfoSuccess(ResponseBody responseBody);
    public void LoadGroupInfofail(String responseBody);
    public void loadmenberSucess(ResponseBody responseBody);
    public void loadmenberfail(String string);

    //
    public void updateGroupSuccess(ResponseBody responseBody);
    public void updateGroupfail(String responseBody);


    public void quitGroupSuccess(ResponseBody responseBody);

    public void loadGroupMemberInfo(ResponseBody responseBody);

}
