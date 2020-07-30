package com.jfkj.im.mvp.updateFriend;

import com.jfkj.im.App;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.retrofit.ApiCallback;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;

public class UpdateFriendpresenter extends BasePresenter<UpdateFriendView> {

    public UpdateFriendpresenter(UpdateFriendView updateFriendView) {
        attachView(updateFriendView);
    }

}
