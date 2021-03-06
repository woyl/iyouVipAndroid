package com.jfkj.im.TIM.modules.chat.interfaces;


import androidx.fragment.app.FragmentManager;

import com.jfkj.im.TIM.base.ILayout;
import com.jfkj.im.TIM.component.NoticeLayout;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.chat.layout.input.InputLayout;
import com.jfkj.im.TIM.modules.chat.layout.message.MessageLayout;
import com.jfkj.im.TIM.modules.message.MessageInfo;

/**

 *  提醒区 {@link NoticeLayout}，
 *  消息区 {@link MessageLayout}，
 *  输入区 {@link InputLayout}，</pre>
 * 每个区域提供了多样的方法以供定制使用。
 */
public interface IChatLayout extends ILayout {

    /**
     * 获取聊天窗口 Input 区域 Layout
     *
     * @return
     */
    InputLayout getInputLayout();

    /**
     * 获取聊天窗口 Message 区域 Layout
     *
     * @return
     */
    MessageLayout getMessageLayout();

    /**
     * 获取聊天窗口 Notice 区域 Layout
     *
     * @return
     */
    NoticeLayout getNoticeLayout();

    /**
     * 获取当前的会话信息
     */
    ChatInfo getChatInfo();

    /**
     * 设置当前的会话信息
     *
     * @param chatInfo
     */
    void setChatInfo(ChatInfo chatInfo);

    /**
     * 退出聊天，释放相关资源（一般在 activity finish 时调用）
     */
    void exitChat();

    /**
     * 初始化参数
     */
    void initDefault(FragmentManager fragmentManager,ChatInfo mChatInfo);

    /**
     * 加载聊天消息
     */
    void loadMessages();

    /**
     * 发送消息
     *
     * @param msg   消息
     * @param retry 是否重试
     */
    void sendMessage(MessageInfo msg, boolean retry);
}
