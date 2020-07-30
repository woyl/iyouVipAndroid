package com.jfkj.im.TIM.modules.chat.base;


import com.jfkj.im.TIM.modules.chat.interfaces.IChatLayout;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.BasePresenter;

public class BaseInputFragment extends BaseFragment {

    private IChatLayout mChatLayout;

    public IChatLayout getChatLayout() {
        return mChatLayout;
    }

    public BaseInputFragment setChatLayout(IChatLayout layout) {
        mChatLayout = layout;
        return this;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }
}
