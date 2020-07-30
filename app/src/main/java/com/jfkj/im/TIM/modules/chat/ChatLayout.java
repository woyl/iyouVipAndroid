package com.jfkj.im.TIM.modules.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.component.TitleBarLayout;
import com.jfkj.im.TIM.modules.chat.base.AbsChatLayout;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.modules.chat.base.ChatManagerKit;
import com.jfkj.im.TIM.modules.group.apply.GroupApplyInfo;
import com.jfkj.im.TIM.modules.group.apply.GroupApplyManagerActivity;
import com.jfkj.im.TIM.modules.group.info.GroupInfo;
import com.jfkj.im.TIM.modules.group.info.GroupInfoActivity;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.redpack.HeadUrlNameUtils;
import com.jfkj.im.TIM.redpack.chatroom.PayChatRoomUtils;
import com.jfkj.im.TIM.redpack.chatroom.SquareRuleActivity;
import com.jfkj.im.TIM.redpack.game.SubmissionTaskDialogFragment;
import com.jfkj.im.TIM.redpack.group.GroupMessageCenterActivity;
import com.jfkj.im.TIM.utils.TUIKitConstants;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.entity.SendPayChatRoomEvent;
import com.jfkj.im.event.ShowDialogEvent;
import com.jfkj.im.listener.CountDownTimeListener;
import com.jfkj.im.ui.dialog.SendMessageDialogFragment;
import com.jfkj.im.utils.DataFormatUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.TimeCountUtil;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMValueCallBack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Objects;

import retrofit2.http.PUT;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class ChatLayout extends AbsChatLayout implements GroupChatManagerKit.GroupNotifyHandler {

    private GroupInfo mGroupInfo;
    private GroupChatManagerKit mGroupChatManager;
    private C2CChatManagerKit mC2CChatManager;
    public boolean isGroup = false;
    public boolean isChatRoom = false;

    public ChatLayout(Context context) {
        super(context);
    }

    public ChatLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setChatInfo(ChatInfo chatInfo) {
        Log.v("--->loadChatMessages","1");
        super.setChatInfo(chatInfo);
        if (chatInfo == null) {
            return;
        }

        if (chatInfo.getType() == TIMConversationType.C2C) {
            isGroup = false;
        } else {
            isGroup = true;
            isChatRoom = chatInfo.isChatRoom();
        }

        if (isGroup && !isChatRoom) {

            mGroupChatManager = GroupChatManagerKit.getInstance();
            mGroupChatManager.setGroupHandler(this);
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.setId(chatInfo.getId());
            groupInfo.setChatName(chatInfo.getChatName());
            mGroupChatManager.setCurrentChatInfo(groupInfo);
            mGroupInfo = groupInfo;
            loadChatMessages(null);
            loadApplyList();
            getTitleBar().getRightIcon().setImageResource(R.mipmap.icon_more_black);


            if(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID +chatInfo.getId()+Utils.NODISTURB, "closenodisturb")!=null){
                if(SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID +chatInfo.getId()+Utils.NODISTURB, "closenodisturb").length()>0){
                    String closenodisturb = SPUtils.getInstance(App.getAppContext()).getString(Utils.APPID + chatInfo.getId() + Utils.NODISTURB, "closenodisturb");
                    if(closenodisturb.equals("closenodisturb")){
                        getTitleBar().getMiddleIcon().setVisibility(View.GONE);
                    }else {
                        getTitleBar().getMiddleIcon().setVisibility(View.VISIBLE);
                        getTitleBar().getMiddleIcon().setImageResource(R.mipmap.no_rao);
                    }
                }
            }

            getTitleBar().setOnRightClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mGroupInfo != null) {
                        //这种方式是区分用户设置那个群设置为免打扰
                        SPUtils.getInstance(App.getAppContext()).put(Utils.APPID+Utils.ISDISTURB+Utils.DISTURB,mGroupInfo.getId());

                        Intent intent = new Intent(getContext(), GroupInfoActivity.class);
                        intent.putExtra(TUIKitConstants.Group.GROUP_ID, mGroupInfo.getId());

                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        App.getAppContext().startActivity(intent);
                    } else {
                        ToastUtil.toastLongMessage("请稍后再试试~");
                    }
                }
            });
            mGroupApplyLayout.setOnNoticeClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(getContext(), GroupApplyManagerActivity.class);
//                    intent.putExtra(TUIKitConstants.Group.GROUP_INFO, mGroupInfo);
                    Intent intent = new Intent(getContext(), GroupMessageCenterActivity.class);
                    getContext().startActivity(intent);
                }
            });
        } else if (isGroup && isChatRoom) {

            mGroupChatManager = GroupChatManagerKit.getInstance();
            mGroupChatManager.setGroupHandler(this);
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.setId(chatInfo.getId());
            groupInfo.setChatName(chatInfo.getChatName());
            mGroupChatManager.setCurrentChatInfo(groupInfo);
            mGroupInfo = groupInfo;
            loadChatMessages(null);
            loadApplyList();
            getTitleBar().getRightTitle().setText("规则");
            getTitleBar().getRightTitle().setTextColor(Color.parseColor("#ffbbbbbb"));
            getTitleBar().getRightIcon().setVisibility(GONE);
            getTitleBar().setOnRightClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), SquareRuleActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    App.getAppContext().startActivity(intent);
                }
            });



//            mInputLayout.mSendTextButton.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (SPUtils.getInstance(App.getAppContext()).getBoolean(Utils.APPID+Utils.IS_TIPS)) {
//                        EventBus.getDefault().post(new SendPayChatRoomEvent(true));
//                    } else {
//                        EventBus.getDefault().post(new SendPayChatRoomEvent(false));
//                    }
//                }
//            });
        } else {

            getTitleBar().getRightIcon().setImageResource(R.mipmap.icon_more_black);
            mC2CChatManager = C2CChatManagerKit.getInstance();
            mC2CChatManager.setCurrentChatInfo(chatInfo);
            loadChatMessages(null);
        }

        getTitleBar().setBackgroundColor(Color.parseColor("#111111"));
    }

    @Override
    public ChatManagerKit getChatManager() {
        if (isGroup) {
            return mGroupChatManager;
        } else {
            return mC2CChatManager;
        }
    }

    private void loadApplyList() {
        mGroupChatManager.getProvider().loadGroupApplies(new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                List<GroupApplyInfo> applies = (List<GroupApplyInfo>) data;
                if (applies != null && applies.size() > 0) {
                    mGroupApplyLayout.getContent().setText(getContext().getString(R.string.group_apply_tips, applies.size()));
                    mGroupApplyLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastLongMessage("loadApplyList onError: " + errMsg);
            }
        });
    }

    @Override
    public void onGroupForceExit() {
        if (getContext() instanceof Activity) {
            ((Activity) getContext()).finish();
        }
    }

    public Activity getActivity() {
        return getActivity();
    }

    @Override
    public void onGroupNameChanged(String newName) {
        getTitleBar().setTitle(newName, TitleBarLayout.POSITION.MIDDLE);
    }

    @Override
    public void onApplied(int size) {
        if (size == 0) {
            mGroupApplyLayout.setVisibility(View.GONE);
        } else {
            mGroupApplyLayout.getContent().setText(getContext().getString(R.string.group_apply_tips, size));
            mGroupApplyLayout.setVisibility(View.VISIBLE);
        }
    }
}
