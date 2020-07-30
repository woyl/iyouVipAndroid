package com.jfkj.im.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jfkj.im.App;
import com.jfkj.im.Bean.GroupBean;
import com.jfkj.im.Bean.InteractionBean;
import com.jfkj.im.Bean.chat.Message;
import com.jfkj.im.R;
import com.jfkj.im.TIM.bean.ConversationMessage;
import com.jfkj.im.TIM.component.TitleBarLayout;
import com.jfkj.im.TIM.component.action.PopActionClickListener;
import com.jfkj.im.TIM.component.action.PopDialogAdapter;
import com.jfkj.im.TIM.component.action.PopMenuAction;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.conversation.ConversationLayout;
import com.jfkj.im.TIM.modules.conversation.ConversationListLayout;
import com.jfkj.im.TIM.modules.conversation.base.ConversationInfo;
import com.jfkj.im.TIM.redpack.FriendsUtils;
import com.jfkj.im.TIM.redpack.LastMessageUtils;
import com.jfkj.im.TIM.redpack.UnreadMessageNumUtils;
import com.jfkj.im.TIM.redpack.chatroom.ChatRoomController;
import com.jfkj.im.TIM.redpack.group.GroupInfoSetUtils;
import com.jfkj.im.TIM.redpack.group.GroupMessageCenterActivity;
import com.jfkj.im.TIM.redpack.group.GroupMessageCenterUtils;
import com.jfkj.im.TIM.redpack.group.GroupUnreadMessageNumUtils;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.TIM.utils.PopWindowUtil;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.adapter.RecentMessageAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.event.ApplyJoinGroupEvent;
import com.jfkj.im.event.GroupUnreadMessageNumEvent;
import com.jfkj.im.event.UpChatRoomEvent;
import com.jfkj.im.event.UpdataClubEvent;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.Club.ClubPresenter;
import com.jfkj.im.mvp.Club.ClubView;
import com.jfkj.im.ui.activity.ChatActivity;
import com.jfkj.im.ui.activity.ClubnameActivity;
import com.jfkj.im.ui.activity.GroupActivity;
import com.jfkj.im.ui.activity.GroupcenterActivity;
import com.jfkj.im.ui.activity.UnmannedActivity;
import com.jfkj.im.ui.dialog.ClubDialog;
import com.jfkj.im.ui.dialog.VipSetGradeDialogFragment;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.OkGo;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupTipsElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMRefreshListener;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupBaseInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfo;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfoResult;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;

//登录注册，互动，俱乐部
public class ClubFragment extends BaseFragment<ClubPresenter> implements View.OnClickListener, ClubView, OnItemClickListener {
    @BindView(R.id.nav_icon_message_iv)
    ImageView nav_icon_message_iv;
    @BindView(R.id.nav_icon_add_iv)
    ImageView nav_icon_add_iv;
    static ClubFragment studyFragment;
    ClubDialog clubDialog;
    List<GroupBean.DataBean.ArrayBean> list;
    ClubPresenter clubPresenter;
    @BindView(R.id.btn_add_create)
    Button btn_add_create;
    /**
     * 有群
     */
    @BindView(R.id.ll_pic_bg)
    LinearLayout ll_pic_bg;

    /**
     * 没有群
     */
    @BindView(R.id.view_no_group)
    View view_no_group;

    @BindView(R.id.tv_title)
    TextView tvTitle;


    @BindView(R.id.conversation_layout)
    ConversationLayout conversation_layout;
    private List<PopMenuAction> mConversationPopActions = new ArrayList<>();
    private ListView mConversationPopList;
    private PopupWindow mConversationPopWindow;
    private PopDialogAdapter mConversationPopAdapter;

    public static ClubFragment getInstance() {
        if (studyFragment == null) {
            studyFragment = new ClubFragment();
        }
        return studyFragment;
    }

    Gson gson;
    String sort = "";
    List<InteractionBean> interactionBeanList;
    //头部
    @BindView(R.id.conversation_title)
    TitleBarLayout mTitleBarLayout;

    private boolean isShowCenter;

    private boolean isShowMainUi;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_club;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            fullScreen(mActivity);
//        }
        gson = new Gson();
        interactionBeanList = new ArrayList<>();
        mTitleBarLayout.setVisibility(View.GONE);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        recentrecyclerview.setLayoutManager(linearLayoutManager);
        nav_icon_message_iv.setOnClickListener(this);
        nav_icon_add_iv.setOnClickListener(this);
        btn_add_create.setOnClickListener(this);
        clubDialog = new ClubDialog(getContext(), getActivity());
//        androidx.recyclerview.widget.DividerItemDecoration divider = new androidx.recyclerview.widget.DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL);
//        divider.setDrawable(ContextCompat.getDrawable(mActivity, R.drawable.shape_recyclerview));
//        recentrecyclerview.addItemDecoration(divider);
//        recentmessageadapter = new RecentMessageAdapter(mActivity);
//        recentrecyclerview.setOnItemClickListener(this);
//        recentrecyclerview.setAdapter(recentmessageadapter);
        list = new ArrayList<>();
        clubPresenter = new ClubPresenter(this);

        clubPresenter.messagerecord(mActivity);
        if (UserInfoManger.getGender().equals("1")) {//"0未设置性别,1男 2女",
            btn_add_create.setText("立即创建");
        } else if (UserInfoManger.getGender().equals("2")) {
            btn_add_create.setText("立即加入");
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("creategroup");
        intentFilter.addAction("removegroup");
        intentFilter.addAction("group_change_number");
        intentFilter.addAction("finsh_success");
        intentFilter.addAction("exitgroupsuccess");
        intentFilter.addAction("invite_add_group");
        intentFilter.addAction("passive_add_group");
        intentFilter.addAction("add_group");
        intentFilter.addAction("reject_add_group");
        intentFilter.addAction("invite_group_success");
        intentFilter.addAction("groupdisbandedsuccessfully");
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
//        initPopMenuAction();
        message();
    }

    private void initPopMenuAction() {

        // 设置长按conversation显示PopAction
        List<PopMenuAction> conversationPopActions = new ArrayList<PopMenuAction>();
        PopMenuAction action = new PopMenuAction();
        action.setActionName(getResources().getString(R.string.chat_top));
        action.setActionClickListener(new PopActionClickListener() {
            @Override
            public void onActionClick(int position, Object data) {
                conversation_layout.setConversationTop(position, (ConversationInfo) data);
            }
        });
        conversationPopActions.add(action);
        action = new PopMenuAction();
        action.setActionClickListener(new PopActionClickListener() {
            @Override
            public void onActionClick(int position, Object data) {
                conversation_layout.deleteConversation(position, (ConversationInfo) data);
            }
        });
        action.setActionName(getResources().getString(R.string.chat_delete));
        conversationPopActions.add(action);
        mConversationPopActions.clear();
        mConversationPopActions.addAll(conversationPopActions);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upDateClub(UpdataClubEvent event) {
        if (event.isUp()) {
            message();
        }
    }


    private void message() {
        tvTitle.setVisibility(View.GONE);
        showLoading();
        // 初始化聊天面板
        conversation_layout.initDefault(false, new TIMValueCallBack<Boolean>() {
            @Override
            public void onError(int i, String s) {
                hideLoading();
                toastShow(i + s);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                hideLoading();
                if (aBoolean) {
                    showUI();
                } else {
                    hideUi();
                }
            }
        });
        //获取会话计数
        GroupUnreadMessageNumUtils.getConversationNumbers();
        conversation_layout.getConversationList().setOnItemClickListener(new ConversationListLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ConversationInfo conversationInfo) {
                //此处为demo的实现逻辑，更根据会话类型跳转到相关界面，开发者可根据自己的应用场景灵活实现
                startChatActivity(conversationInfo);
            }
        });
        conversation_layout.getConversationList().setOnItemLongClickListener(new ConversationListLayout.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(View view, int position, ConversationInfo conversationInfo) {
                startPopShow(view, position, conversationInfo);
            }
        });
    }

    private void hideUi() {
        isShowMainUi = false;
        conversation_layout.setVisibility(View.GONE);
        view_no_group.setBackground(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.mipmap.pic_bg_club));
        ll_pic_bg.setVisibility(View.GONE);
        view_no_group.setVisibility(View.VISIBLE);
        btn_add_create.setVisibility(View.VISIBLE);
        Glide.with(this).load(R.mipmap.nav_icon_message_white).into(nav_icon_message_iv);
        Glide.with(this).load(R.mipmap.nav_icon_add_white).into(nav_icon_add_iv);
        tvTitle.setVisibility(View.VISIBLE);
    }

    private void showUI() {
        isShowMainUi = true;
        conversation_layout.setVisibility(View.VISIBLE);
        ll_pic_bg.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.c111111));
        ll_pic_bg.setVisibility(View.VISIBLE);
        view_no_group.setVisibility(View.GONE);
        btn_add_create.setVisibility(View.GONE);
//            if (isShowCenter) {
//                Glide.with(this).load(R.drawable.nav_icon_message_red).into(nav_icon_message_iv);
//            } else {
//                Glide.with(this).load(R.drawable.nav_icon_message_iv).into(nav_icon_message_iv);
//            }

        if (isShowCenter) {
            Glide.with(this).load(R.mipmap.mess_red).into(nav_icon_message_iv);
        } else {
            Glide.with(this).load(R.mipmap.nav_icon_message_white).into(nav_icon_message_iv);
        }
        if (SPUtils.getInstance(App.getAppContext()).getLong(UserInfoManger.getUserId()+Utils.UN_READ_NUM) != -1L) {
            Glide.with(this).load(R.mipmap.mess_red).into(nav_icon_message_iv);
        }
//            Glide.with(this).load(R.drawable.nav_icon_add_iv).into(nav_icon_add_iv);
    }

    private void startChatActivity(ConversationInfo conversationInfo) {
        GroupInfoSetUtils.getGroupMembers(conversationInfo.getId(), new TIMValueCallBack<List<TIMGroupMemberInfo>>() {
            @Override
            public void onError(int code, String desc) {
                toastShow("321" +desc);
            }

            @Override
            public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
                if (timGroupMemberInfos.size() == 1) {
                    for (TIMGroupMemberInfo timGroupMemberInfo : timGroupMemberInfos) {
                        if (timGroupMemberInfo.getUser().equals(TIMManager.getInstance().getLoginUser())) {
                            TIMGroupDetailInfo groupDetailInfo = GroupInfoSetUtils.queryGroupInfo(conversationInfo.getId());
                            if (groupDetailInfo != null) {
                                Intent intent = new Intent(App.getAppContext(), UnmannedActivity.class);
                                intent.putExtra(Utils.GROUPID, conversationInfo.getId());
                                intent.putExtra(Utils.GROUPNAME, groupDetailInfo.getGroupName());
                                intent.putExtra(Utils.GROUP_HEAD_URL, groupDetailInfo.getFaceUrl());
                                intent.putExtra("notice", groupDetailInfo.getGroupNotification());
                                intent.putExtra("name", groupDetailInfo.getGroupName());
                                startActivity(intent);
                            } else {
                                List<String> stringList = new ArrayList<>();
                                stringList.add(conversationInfo.getId());
                                GroupInfoSetUtils.getGroupInfo(stringList, new TIMValueCallBack<List<TIMGroupDetailInfoResult>>() {
                                    @Override
                                    public void onError(int code, String desc) {
                                        toastShow("344 + \t\t" + code + desc);
                                    }

                                    @Override
                                    public void onSuccess(List<TIMGroupDetailInfoResult> timGroupDetailInfoResults) {
                                        for (TIMGroupDetailInfoResult result : timGroupDetailInfoResults) {
                                            if (result.getGroupOwner().equals(TIMManager.getInstance().getLoginUser())) {
                                                Intent intent = new Intent(App.getAppContext(), UnmannedActivity.class);
                                                intent.putExtra(Utils.GROUPID, result.getGroupId());
                                                intent.putExtra(Utils.GROUPNAME, result.getGroupName());
                                                intent.putExtra(Utils.GROUP_HEAD_URL, result.getFaceUrl());
                                                intent.putExtra("notice", result.getGroupNotification());
                                                intent.putExtra("name", result.getGroupName());
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                } else if (timGroupMemberInfos.size() > 1) {
                    ChatInfo chatInfo = new ChatInfo();
                    chatInfo.setType(conversationInfo.isGroup() ? TIMConversationType.Group : TIMConversationType.C2C);
                    chatInfo.setId(conversationInfo.getId());
                    chatInfo.setChatName(conversationInfo.getTitle());
                    chatInfo.setUserid(conversationInfo.getUserid());

                    Intent intent = new Intent(getActivity(), ChatActivity.class);
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

                    Objects.requireNonNull(getActivity()).startActivity(intent);
                }
            }
        });

    }

    private void startPopShow(View view, int position, ConversationInfo info) {
        showItemPopMenu(position, info, view.getX(), view.getY() + view.getHeight() / 2);
    }

    /**
     * 长按会话item弹框
     *
     * @param index            会话序列号
     * @param conversationInfo 会话数据对象
     * @param locationX        长按时X坐标
     * @param locationY        长按时Y坐标
     */
    private void showItemPopMenu(final int index, final ConversationInfo conversationInfo, float locationX, float locationY) {
        if (mConversationPopActions == null || mConversationPopActions.size() == 0)
            return;
        View itemPop = LayoutInflater.from(getActivity()).inflate(R.layout.pop_menu_layout, null);
        mConversationPopList = itemPop.findViewById(R.id.pop_menu_list);
        mConversationPopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopMenuAction action = mConversationPopActions.get(position);
                if (action.getActionClickListener() != null) {
                    action.getActionClickListener().onActionClick(index, conversationInfo);
                }
                mConversationPopWindow.dismiss();
            }
        });

        for (int i = 0; i < mConversationPopActions.size(); i++) {
            PopMenuAction action = mConversationPopActions.get(i);
            if (conversationInfo.isTop()) {
                if (action.getActionName().equals(getResources().getString(R.string.chat_top))) {
                    action.setActionName(getResources().getString(R.string.quit_chat_top));
                }
            } else {
                if (action.getActionName().equals(getResources().getString(R.string.quit_chat_top))) {
                    action.setActionName(getResources().getString(R.string.chat_top));
                }

            }
        }
        mConversationPopAdapter = new PopDialogAdapter();
        mConversationPopList.setAdapter(mConversationPopAdapter);
        mConversationPopAdapter.setDataSource(mConversationPopActions);
        mConversationPopWindow = PopWindowUtil.popupWindow(itemPop, view, (int) locationX, (int) locationY);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                mConversationPopWindow.dismiss();
            }
        }, 10000); // 10s后无操作自动消失
    }

    @Override
    protected ClubPresenter createPresenter() {
        return clubPresenter;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!isShowMainUi) {
                message();
                unMessage();
            }
        }
    }

    private void unMessage() {
        GroupUnreadMessageNumUtils.getConversationNumbers(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
            @Override
            public void onError(int code, String desc) {
                toastShow("468 + \t\t" + code + desc);
            }

            @Override
            public void onSuccess(List<TIMGroupBaseInfo> timGroupBaseInfos) {
                for (TIMGroupBaseInfo timGroupBaseInfo : timGroupBaseInfos) {
                    LastMessageUtils.getLocalLastMessage(TIMConversationType.Group, timGroupBaseInfo.getGroupId(), new TIMValueCallBack<List<TIMMessage>>() {
                        @Override
                        public void onError(int code, String desc) {
                            toastShow("477 + \t\t" + code + desc);
                        }

                        @Override
                        public void onSuccess(List<TIMMessage> timMessages) {
                            if (timMessages == null) {
                                EventBus.getDefault().postSticky(new GroupUnreadMessageNumEvent(0, timGroupBaseInfo.getGroupId()));
                            }
                        }
                    });

                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void sendApplyGroup(ApplyJoinGroupEvent applyJoinGroupEvent) {
        isShowCenter = applyJoinGroupEvent.isApplyJoin();
        if (applyJoinGroupEvent.isApplyJoin()) {
            if (SPUtils.getInstance(App.getAppContext()).getLong(UserInfoManger.getUserId()+Utils.UN_READ_NUM) != -1L) {
                Glide.with(this).load(R.mipmap.mess_red).into(nav_icon_message_iv);
            }
            Glide.with(this).load(R.mipmap.mess_red).into(nav_icon_message_iv);
        } else {
            Glide.with(this).load(R.drawable.nav_icon_message_iv).into(nav_icon_message_iv);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

//        clubPresenter.messagerecord(mActivity);
//        Intent intent = new Intent("changegroupmenber");
//        getActivity().sendBroadcast(intent);
//        if (SPUtils.getInstance(mActivity).getBoolean(Utils.APPID + Utils.GROUPMESSAGECENTER, false)) {
//
//        } else {
//
//        }
//        if (SPUtils.getInstance(mActivity).getBoolean(Utils.GROUP_CENTER_MESSAGE_ISSHOW,false)){
//            nav_icon_message_iv.setBackgroundResource(R.drawable.nav_icon_message_point);
//        }else {
//            nav_icon_message_iv.setBackgroundResource(R.drawable.nav_icon_message_iv);
//        }
        message();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nav_icon_message_iv:
//                SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.GROUPMESSAGECENTER, false);
//                SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.RECENTGROUPMESSAGE, gson.toJson(interactionBeanList));
//                Intent groupmessagecenter_intent = new Intent(mActivity, GroupcenterActivity.class);
//                startActivity(groupmessagecenter_intent);
                Intent groupmessagecenter_intent = new Intent(mActivity, GroupMessageCenterActivity.class);
                startActivity(groupmessagecenter_intent);
                break;
            case R.id.nav_icon_add_iv:
                clubDialog.show();
                break;
            case R.id.btn_add_create:
                if (btn_add_create.getText().toString().equals("立即创建")) {
                    if (Utils.netWork()) {
                        if (UserInfoManger.getGender().equals("1")) {
                            if (Integer.parseInt(UserInfoManger.getUserVipLevel().trim()) >= 3) {
                                jumpGreateGroup();
                            } else {
                                VipSetGradeDialogFragment dialogFragment
                                        = new VipSetGradeDialogFragment(true, Gravity.CENTER, "VIP等级达到3级以后，才可解锁创建俱乐部功能。", "确认");
                                dialogFragment.setRsp(new ResponListener<Boolean>() {
                                    @Override
                                    public void Rsp(Boolean s) {
                                        if (s) {

                                        }
                                    }
                                });
                                if (getFragmentManager() != null) {
                                    dialogFragment.show(getFragmentManager(), "");
                                }

                            }
                        } else {
                            jumpGreateGroup();
                        }

                    } else {
                        toastShow(R.string.nonetwork);
                    }
                } else if (btn_add_create.getText().toString().equals("立即加入")) {
                    Intent intent = new Intent(App.getAppContext(), GroupActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
        }
    }

    private void jumpGreateGroup() {
        Intent intent = new Intent(App.getAppContext(), ClubnameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("type", "club");
        intent.putExtra(TUIKitConstants.Selection.CONTENT, bundle);
        Objects.requireNonNull(getActivity()).startActivity(intent);
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void loadClubSuccess(ResponseBody responseBody) {
        try {
            String string = responseBody.string();
            JSONObject jsonObject = new JSONObject(string);
            if (jsonObject.getString("code").equals("200")) {
                GroupBean groupBean = JSON.parseObject(string, GroupBean.class);
                if (groupBean.getData().getArray().size() == 0) {
                    Intent intent = new Intent("finsh_success");
                    mActivity.sendBroadcast(intent);
                    return;
                }
                if (groupBean.getData().getArray().size() > 0) {
                    sort = groupBean.getData().getArray().get(groupBean.getData().getArray().size() - 1).getSort() + "";
                    clubPresenter.getdata(sort);
                    list.addAll(groupBean.getData().getArray());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadClubfail(String responseBody) {
    }

    @Override
    public void messagerecord(List<InteractionBean> responseBody) {

        if (interactionBeanList.size() > 0) {
            btn_add_create.setVisibility(View.GONE);
//            recentrecyclerview.setVisibility(View.VISIBLE);
            conversation_layout.setVisibility(View.VISIBLE);
        } else {
            btn_add_create.setVisibility(View.VISIBLE);
//            recentrecyclerview.setVisibility(View.GONE);
            conversation_layout.setVisibility(View.GONE);
        }
        if (responseBody.size() == 0) {
            return;
        }
        interactionBeanList.clear();
        interactionBeanList.addAll(responseBody);

//        recentmessageadapter.setList(interactionBeanList);
        SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.RECENTGROUPMESSAGE, JSON.toJSONString(interactionBeanList));
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case "creategroup":
                    InteractionBean creategroupinteractionBean = new InteractionBean();
                    creategroupinteractionBean.setHeadurl(intent.getStringExtra(Utils.GROUP_HEAD_URL));
                    creategroupinteractionBean.setName(intent.getStringExtra(Utils.GROUPNAME));
                    creategroupinteractionBean.setGroupId(Long.parseLong(intent.getStringExtra(Utils.GROUPID)));
                    interactionBeanList.add(0, creategroupinteractionBean);
//                    recentmessageadapter.setList(interactionBeanList);
                    SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.RECENTGROUPMESSAGE, JSON.toJSONString(interactionBeanList));
                    break;
                case "group_change_number":
                    clubPresenter.messagerecord(mActivity);
                    break;
                case "finsh_success":
                    for (int i = 0; i < list.size(); i++) {
                        GroupBean.DataBean.ArrayBean arrayBean = list.get(i);
                        InteractionBean interactionBean = new InteractionBean();
                        interactionBean.setHeadurl(arrayBean.getGroupHead());
                        interactionBean.setName(arrayBean.getGroupName());
                        interactionBean.setGroupId(arrayBean.getGroupId());

                        interactionBeanList.add(interactionBean);
                    }
//                    recentmessageadapter.setList(interactionBeanList);
                    SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.RECENTGROUPMESSAGE, JSON.toJSONString(interactionBeanList));
                    break;
                case "exitgroupsuccess":
                    long groupid = Long.parseLong(intent.getStringExtra(Utils.GROUPID));
                    for (int i = 0; i < interactionBeanList.size(); i++) {
                        if (groupid == interactionBeanList.get(i).getGroupId()) {
                            interactionBeanList.remove(i--);
                        }
                    }
//                    recentmessageadapter.setList(interactionBeanList);
                    SPUtils.getInstance(mActivity).put(Utils.APPID + intent.getStringExtra(Utils.GROUPID), "");
                    SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.RECENTGROUPMESSAGE, JSON.toJSONString(interactionBeanList));
                    break;
                case "invite_add_group":
                case "passive_add_group":
                case "add_group":
                case "reject_add_group":
                case "invite_group_success":
                case "groupdisbandedsuccessfully":
                    SPUtils.getInstance(mActivity).getBoolean(Utils.APPID + Utils.GROUPMESSAGECENTER, true);
//                    if (SPUtils.getInstance(mActivity).getBoolean(Utils.APPID + Utils.GROUPMESSAGECENTER, false)) {
//                        nav_icon_message_iv.setBackgroundResource(R.drawable.nav_icon_message_point);
//                    } else {
//                        nav_icon_message_iv.setBackgroundResource(R.drawable.nav_icon_message_iv);
//                    }
                    break;
                case "removegroup":
//                    recentrecyclerview.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            List<InteractionBean> exitgrouparrayList = new ArrayList<>();
//                            if (SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.RECENTGROUPMESSAGE) != null) {
//                                exitgrouparrayList.addAll(JSON.parseArray(SPUtils.getInstance(mActivity).getString(Utils.APPID + Utils.RECENTGROUPMESSAGE), InteractionBean.class));
//                            }
//
//                            for (int i = 0; i < exitgrouparrayList.size(); i++) {
//                                if (String.valueOf(exitgrouparrayList.get(i).getGroupId()).equals(intent.getStringExtra(Utils.GROUPID))) {
//                                    exitgrouparrayList.remove(i--);
//                                }
//                            }
//                            SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.RECENTGROUPMESSAGE, JSON.toJSONString(exitgrouparrayList));
//                            clubPresenter.messagerecord(mActivity);
//                        }
//                    },1000);
                    break;


            }
        }
    };

    @Override
    public void onItemClick(View view, int adapterPosition) {

        /**
         * 进入聊天
         * */
        InteractionBean interactionBean = interactionBeanList.get(adapterPosition);
        Intent intent = new Intent(mActivity, ChatActivity.class);
        Gson gson = new Gson();
        intent.putExtra(Utils.RECEIVEID, interactionBean.getGroupId() + "");
        intent.putExtra(Utils.NICKNAME, interactionBean.getName() + "");
        intent.putExtra(Utils.HEADIMGURL, interactionBean.getHeadurl() + "");
        SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.RECENTGROUPMESSAGE, gson.toJson(interactionBeanList));
        intent.putExtra(Utils.ISPRIVATEFRIEND, false);
        mActivity.startActivity(intent);


    }

    @Override
    public void onDestroy() {
        //  SPUtils.getInstance(mActivity).put(Utils.APPID+Utils.RECENTGROUPMESSAGE, gson.toJson(interactionBeanList));
        if (broadcastReceiver != null) {
            getActivity().unregisterReceiver(broadcastReceiver);
        }
        super.onDestroy();
    }


}
