package com.jfkj.im.ui.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jfkj.im.App;
import com.jfkj.im.Bean.InteractionBean;
import com.jfkj.im.Bean.TIMConversationBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.TUIKit;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.bean.ConversationMessage;
import com.jfkj.im.TIM.component.TitleBarLayout;
import com.jfkj.im.TIM.component.action.PopActionClickListener;
import com.jfkj.im.TIM.component.action.PopDialogAdapter;
import com.jfkj.im.TIM.component.action.PopMenuAction;
import com.jfkj.im.TIM.component.photoview.Util;
import com.jfkj.im.TIM.menu.Menu;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;


import com.jfkj.im.TIM.modules.conversation.ConversationListAdapter;
import com.jfkj.im.TIM.modules.conversation.ConversationListLayout;

import com.jfkj.im.TIM.modules.conversation.ConversationProvider;
import com.jfkj.im.TIM.modules.conversation.base.ConversationInfo;
import com.jfkj.im.TIM.modules.conversation.interfaces.IConversationAdapter;
import com.jfkj.im.TIM.modules.conversation.interfaces.IConversationLayout;
import com.jfkj.im.TIM.modules.conversation.interfaces.IConversationProvider;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.redpack.FriendsUtils;
import com.jfkj.im.TIM.redpack.LastMessageUtils;
import com.jfkj.im.TIM.redpack.chatroom.ChatRoomController;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.PopWindowUtil;
import com.jfkj.im.TIM.utils.SharedPreferenceUtils;
import com.jfkj.im.TIM.utils.TUIKitLog;
import com.jfkj.im.TIM.utils.ToastUtil;

import com.jfkj.im.TIM.zhf.ConversationManagerKit;
import com.jfkj.im.adapter.InteractionAdapter;
import com.jfkj.im.event.UpChatRoomEvent;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.Interaction.InteractionPresenter;
import com.jfkj.im.mvp.Interaction.InteractionView;
import com.jfkj.im.ui.activity.ChatActivity;
import com.jfkj.im.ui.activity.MinefriendActivity;
import com.jfkj.im.ui.activity.SystemActivity;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SharedPreferencesUtils;
import com.jfkj.im.utils.StatusBarUtil;
import com.jfkj.im.utils.TextCheckUtils;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.widget.EmojiEdittext;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupSystemElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.conversation.Conversation;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class InteractionFragment extends BaseFragment<InteractionPresenter> implements View.OnClickListener, OnItemClickListener, InteractionView, OnItemMenuClickListener, IConversationLayout {
    InteractionPresenter interactionPresenter;
    static InteractionFragment interactionfragment;
    List<InteractionBean> interactionBeanList = new ArrayList<>();
    private List<PopMenuAction> mConversationPopActions = new ArrayList<>();
    private ListView mConversationPopList;
    private PopupWindow mConversationPopWindow;
    private ConversationProvider mProvider;
    private PopDialogAdapter mConversationPopAdapter;
    Dialog dialog;
    ConversationListAdapter adapter;
    @BindView(R.id.conversation_title)
    TitleBarLayout mTitleBarLayout;
    @BindView(R.id.conversation_list)
    ConversationListLayout mConversationList;
    TextView tv_cancel, tv_enter, tv_subtitle;

    @BindView(R.id.ry_addfriend_iv)
    RelativeLayout ry_addfriend_iv;

    public static InteractionFragment getInstance() {
        if (interactionfragment == null) {
            interactionfragment = new InteractionFragment();
        }
        return interactionfragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SPUtils.getInstance(mActivity).getInt(Utils.APPID + Utils.ADDFRIENDNUMBER, 0) > 0) {
            mTitleBarLayout.setRightIcon(R.mipmap.nav_icon_friends_gray_red);
        } else {
            mTitleBarLayout.setRightIcon(R.mipmap.nav_icon_friends_gray_white);
        }
    }

    @Override
    protected InteractionPresenter createPresenter() {
        return interactionPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_interaction;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            fullScreen(mActivity);
//        }
        initTitleAction();

    }

    private void initTitleAction() {
        mProvider = new ConversationProvider();
        dialog = new Dialog(mActivity);
        View dialogview = LayoutInflater.from(mActivity).inflate(R.layout.dialog_delete, null);
        dialog = new Dialog(mActivity, R.style.dialogstyle);
        dialog.setContentView(dialogview);
        Window codedialogWindow = dialog.getWindow();
        WindowManager.LayoutParams attributes = codedialogWindow.getAttributes();
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.gravity = Gravity.CENTER;
        codedialogWindow.setAttributes(attributes);
        tv_cancel = dialogview.findViewById(R.id.tv_cancel);
        tv_enter = dialogview.findViewById(R.id.tv_enter);
        tv_subtitle = dialogview.findViewById(R.id.tv_subtitle);
        tv_cancel.setOnClickListener(this);
        tv_enter.setOnClickListener(this);
        tv_subtitle.setText("你确定要删除对话吗");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("removefriend");
        intentFilter.addAction("addfriend");
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
        mTitleBarLayout.setTitle(getResources().getString(R.string.conversation_title), TitleBarLayout.POSITION.MIDDLE);
        mTitleBarLayout.getLeftGroup().setVisibility(View.GONE);
        mTitleBarLayout.setBackgroundColor(Color.parseColor("#1e1e1e"));

        adapter = new ConversationListAdapter();
        mTitleBarLayout.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, MinefriendActivity.class);
                mActivity.startActivity(intent);
            }
        });
        mConversationList.setAdapter(adapter);

        mConversationList.setOnItemClickListener(new ConversationListLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ConversationInfo conversationInfo) {
                if (conversationInfo.getId().equals(Utils.SYSTEMID)) {
                    Intent intent = new Intent(mActivity, SystemActivity.class);
                    startActivity(intent);
                } else {
                    ChatInfo chatInfo = new ChatInfo();
                    chatInfo.setType(conversationInfo.isGroup() ? TIMConversationType.Group : TIMConversationType.C2C);
                    chatInfo.setId(conversationInfo.getId());
                    chatInfo.setChatName(conversationInfo.getTitle());
                    chatInfo.setUserid(conversationInfo.getUserid());
                    chatInfo.setChatRoom(false);
                    Intent intent = new Intent(App.getAppContext(), ChatActivity.class);
                    intent.putExtra(Constants.CHAT_INFO, chatInfo);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ConversationMessage conversationMessage = new ConversationMessage();
                    conversationMessage.setConversationId(conversationInfo.getConversationId());
                    conversationMessage.setGroup(conversationInfo.isGroup());
                    conversationMessage.setId(conversationInfo.getId());
                    conversationMessage.setTop(conversationInfo.isTop());
                    conversationMessage.setTitle(conversationInfo.getTitle());
                    conversationMessage.setUnRead(conversationInfo.getUnRead());
                    conversationMessage.setType(conversationInfo.getType());
                    conversationMessage.setLastMessageTime(conversationInfo.getLastMessageTime());
                    conversationMessage.setLastMessage(conversationInfo.getLastMessage());
                    intent.putExtra(Utils.CONVERSATIONMESSAGE, conversationMessage);
                    getActivity().startActivity(intent);
                }
            }
        });

        mConversationList.setOnItemLongClickListener(new ConversationListLayout.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(View view, int position, ConversationInfo messageInfo) {
                dialog.show();
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                tv_enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        TIMConversation timConversation= TIMManager.getInstance().getConversation(TIMConversationType.C2C, messageInfo.getId());
                        ConversationManagerKit conversationManagerKit=ConversationManagerKit.getInstance();
                        conversationManagerKit.deleteConversation(messageInfo.getId(),false);
                        timConversation.setReadMessage(timConversation.getLastMsg(), new TIMCallBack() {
                            @Override
                            public void onError(int code, String desc) {

                            }
                            @Override
                            public void onSuccess() {
                                LoadMessage();
                            }
                        });

                      //  adapter.removeItem(position);
                        TIMManager.getInstance().deleteConversation(TIMConversationType.C2C, messageInfo.getId());
                    }
                });

            }
        });
       LoadMessage();
    }

    private void LoadMessage() {
        ConversationManagerKit.getInstance().loadConversation(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                if (data instanceof IConversationProvider){
                    if (((IConversationProvider) data).getDataSource().size() > 0) {
                        ry_addfriend_iv.setVisibility(View.GONE);
                        adapter.setDataProvider((ConversationProvider) data);
                    } else {
                        ry_addfriend_iv.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastLongMessage("加载消息失败");
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ry_addfriend_iv:
                startActivity(new Intent(mActivity, MinefriendActivity.class));
                break;
            case R.id.title_right_tv:
                if (Utils.netWork()) {
                    startActivity(new Intent(App.getAppContext(), MinefriendActivity.class));

                } else {
                    toastShow(R.string.nonetwork);
                }
                break;
            case R.id.tv_cancel:

                break;
            case R.id.tv_enter:

                break;
        }
    }

    @Override
    public void onItemClick(View view, int adapterPosition) {

        EmojiEdittext tv_message = view.findViewById(R.id.tv_message);
        tv_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemcliick(adapterPosition);
            }
        });
        InteractionBean interactionBean = new InteractionBean();
        Intent intent = new Intent(mActivity, ChatActivity.class);
        intent.putExtra(Utils.NICKNAME, interactionBean.getName());
        intent.putExtra(Utils.RECEIVEID, interactionBean.getGroupId() + "");
        intent.putExtra(Utils.HEADIMGURL, interactionBean.getHeadurl());
        intent.putExtra(Utils.NODISTURB, interactionBean.getNoDisturb());
        intent.putExtra(Utils.TOP, interactionBean.getTop());
        startActivity(intent);
    }

    public void onItemcliick(int adapterPosition) {
        InteractionBean interactionBean = interactionBeanList.get(adapterPosition);
        Intent intent = new Intent(mActivity, ChatActivity.class);
        intent.putExtra(Utils.NICKNAME, interactionBean.getName());
        intent.putExtra(Utils.RECEIVEID, interactionBean.getGroupId() + "");
        intent.putExtra(Utils.HEADIMGURL, interactionBean.getHeadurl());
        intent.putExtra(Utils.NODISTURB, interactionBean.getNoDisturb());
        intent.putExtra(Utils.TOP, interactionBean.getTop());
        startActivity(intent);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "removefriend":


                    if (adapter != null) {
                        List<ConversationInfo> conversationInfoList = adapter.getmDataSource();
                        for (int i = 0; i < conversationInfoList.size(); i++) {

                            if (conversationInfoList.get(i).getId().equals(intent.getStringExtra("removefriend"))) {

                                conversationInfoList.get(i).getUnRead();
                                adapter.removeItem(i);

                            }
                        }
                    }
                    break;
                case "addfriend":
                    if (intent.getIntExtra(Utils.NUMBER, 0) > 0) {
                        mTitleBarLayout.setRightIcon(R.mipmap.nav_icon_friends_gray_red);
                    } else {
                        mTitleBarLayout.setRightIcon(R.mipmap.nav_icon_friends_gray_white);
                    }

                    break;
            }
        }
    };

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onItemClick(SwipeMenuBridge menuBridge, int adapterPosition) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public ConversationListLayout getConversationList() {
        return mConversationList;
    }

    @Override
    public void setConversationTop(int position, ConversationInfo conversation) {
        ConversationManagerKit.getInstance().setConversationTop(position, conversation);
    }

    @Override
    public void deleteConversation(int position, ConversationInfo conversation) {
        ConversationManagerKit.getInstance().deleteConversation(position, conversation);
    }

    @Override
    public TitleBarLayout getTitleBar() {
        return mTitleBarLayout;
    }

    @Override
    public void setParentLayout(Object parent) {

    }
}
