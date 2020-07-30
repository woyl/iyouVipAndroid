package com.jfkj.im.TIM.modules.group.member;

import com.jfkj.im.TIM.modules.group.info.GroupInfo;

public interface IGroupMemberRouter {

    void forwardListMember(GroupInfo info);

    void forwardAddMember(GroupInfo info);

    void forwardDeleteMember(GroupInfo info);
}
