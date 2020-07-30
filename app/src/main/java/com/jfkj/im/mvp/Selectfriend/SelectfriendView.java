package com.jfkj.im.mvp.Selectfriend;

import com.jfkj.im.mvp.BaseView;
import com.tencent.imsdk.friendship.TIMFriend;

import java.util.List;

import okhttp3.ResponseBody;

public interface SelectfriendView extends BaseView {

    public void  Selectfriendsuccess(List<TIMFriend> responseBody);
    public void Selectfriendfail(String s);


    public void  deletefriendsuccess(ResponseBody responseBody,String userIds);
    public void deletefriendfail(String s);


    public void invitesuccess(ResponseBody responseBody);
    public void invitefail(String s);


    public void delGroupMembersuccess(ResponseBody responseBody);
    public void delGroupMemberfail(String s);


    public void loadGroupMemberListSuccess(ResponseBody responseBody);
    public void loadGroupMemberListfail(String responseBody);
}
