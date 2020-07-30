package com.jfkj.im.mvp.ChatFriend;

import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface ChatFriendView extends BaseView {

    public void ChatFriend(String s);

    public void loadUserVerifyInfoSuccess(String s);
}
