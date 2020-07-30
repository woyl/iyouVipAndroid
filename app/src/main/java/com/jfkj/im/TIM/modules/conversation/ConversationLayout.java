package com.jfkj.im.TIM.modules.conversation;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.jfkj.im.R;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.component.TitleBarLayout;
import com.jfkj.im.TIM.modules.conversation.base.ConversationInfo;
import com.jfkj.im.TIM.modules.conversation.interfaces.IConversationAdapter;
import com.jfkj.im.TIM.modules.conversation.interfaces.IConversationLayout;
import com.jfkj.im.TIM.redpack.group.GroupInfoSetUtils;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.retrofit.Urls;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfoResult;
import com.tencent.imsdk.friendship.TIMFriendPendencyItem;

import java.util.ArrayList;
import java.util.List;

public class ConversationLayout extends RelativeLayout implements IConversationLayout {

    private TitleBarLayout mTitleBarLayout;
    private ConversationListLayout mConversationList;

    public ConversationLayout(Context context) {
        super(context);
        init();
    }

    public ConversationLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConversationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化相关UI元素
     */
    private void init() {
        inflate(getContext(), R.layout.conversation_layout, this);
        mTitleBarLayout = findViewById(R.id.conversation_title);
        mConversationList = findViewById(R.id.conversation_list);
    }

    public void initDefault(boolean flag,TIMValueCallBack<Boolean> callBack) {
        mTitleBarLayout.setTitle(getResources().getString(R.string.conversation_title), TitleBarLayout.POSITION.MIDDLE);
        mTitleBarLayout.getLeftGroup().setVisibility(View.GONE);
        mTitleBarLayout.setRightIcon(R.mipmap.nav_icon_friends_gray_white);
        IConversationAdapter adapter = new ConversationListAdapter();

        mConversationList.setAdapter(adapter);
        ConversationManagerKit.getInstance().loadConversation(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                ConversationProvider conversationProvider = (ConversationProvider) data;

                TIMGroupManager.getInstance().getGroupList(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
                    @Override
                    public void onError(int i, String s) {
                        callBack.onError(i, s);
                    }

                    @Override
                    public void onSuccess(List<TIMGroupBaseInfo> timGroupBaseInfos) {
                        if (timGroupBaseInfos.size() > 0) {
                            if (timGroupBaseInfos.size() == 1) {
                                for (TIMGroupBaseInfo baseInfo:timGroupBaseInfos){
                                    if (baseInfo.getGroupId().equals(Urls.square_chat)) {
                                        callBack.onSuccess(false);
                                    }
                                }
                            } else {
                                ConversationProvider conversationProviderNew  = new ConversationProvider();

                                for (TIMGroupBaseInfo baseInfo:timGroupBaseInfos){
                                    for (ConversationInfo conversationInfo : conversationProvider.getDataSource()){
                                        if (baseInfo.getGroupId().equals(conversationInfo.getId())) {
                                            conversationProviderNew.getDataSource().add(conversationInfo);
                                        }
                                    }
                                }
                                adapter.setDataProvider(conversationProviderNew);
                                callBack.onSuccess(true);
                            }
                        } else {
                            callBack.onSuccess(false);
                        }
                    }
                });

//                for (ConversationInfo conversationInfo:conversationProvider.getDataSource()){
//                    ids.add(conversationInfo.getId());
//                    GroupInfoSetUtils.getGroupInfo(ids, new TIMValueCallBack<List<TIMGroupDetailInfoResult>>() {
//                        @Override
//                        public void onError(int i, String s) {
//                        }
//
//                        @Override
//                        public void onSuccess(List<TIMGroupDetailInfoResult> timGroupDetailInfoResults) {
//
//                            for (TIMGroupDetailInfoResult result: timGroupDetailInfoResults){
//                                if (result.getResultCode() == 10010){
//                                    adapter.setDataProvider(conversationProvider);
//                                }
//                            }
//                        }
//                    });
//                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastLongMessage("加载消息失败");
            }
        });
    }

    public TitleBarLayout getTitleBar() {
        return mTitleBarLayout;
    }

    @Override
    public void setParentLayout(Object parent) {

    }

    @Override
    public ConversationListLayout getConversationList() {
        return mConversationList;
    }

    public void addConversationInfo(int position, ConversationInfo info) {
        mConversationList.getAdapter().addItem(position, info);
    }

    public void removeConversationInfo(int position) {
        mConversationList.getAdapter().removeItem(position);
    }

    @Override
    public void setConversationTop(int position, ConversationInfo conversation) {
        ConversationManagerKit.getInstance().setConversationTop(position, conversation);
    }

    @Override
    public void deleteConversation(int position, ConversationInfo conversation) {
        ConversationManagerKit.getInstance().deleteConversation(position, conversation);
    }
}
