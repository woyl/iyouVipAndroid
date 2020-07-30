package com.jfkj.im.TIM.component.gatherimage;

import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.utils.ScreenUtil;

public abstract class DynamicChatUserIconView extends DynamicLayoutView<MessageInfo> {

    private int iconRadius = -1;

    public int getIconRadius() {
        return iconRadius;
    }

    /**
     * 设置聊天头像圆角
     *
     * @param iconRadius
     */
    public void setIconRadius(int iconRadius) {
        this.iconRadius = ScreenUtil.getPxByDp(iconRadius);
    }
}
