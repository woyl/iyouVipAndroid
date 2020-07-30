package com.jfkj.im.TIM.modules.group.interfaces;

import com.jfkj.im.TIM.base.ILayout;
import com.jfkj.im.TIM.modules.group.info.GroupInfo;


public interface IGroupMemberLayout extends ILayout {

    void setDataSource(GroupInfo dataSource);

}
