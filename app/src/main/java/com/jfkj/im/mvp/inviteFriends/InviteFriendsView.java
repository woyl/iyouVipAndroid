package com.jfkj.im.mvp.inviteFriends;

import com.jfkj.im.entity.InviteBean;
import com.jfkj.im.entity.InvteFriendBean;
import com.jfkj.im.entity.inviteFriendAddressBean;
import com.jfkj.im.mvp.BaseView;

import okhttp3.ResponseBody;

public interface InviteFriendsView extends BaseView {

    void InviteFriendsSuccess(InvteFriendBean bean);


    void  inviteFriendAddressS(inviteFriendAddressBean inviteFriendAddressBean);


    void inviteFriendTail(InviteBean bean);
}
