package com.jfkj.im.TIM.modules.chat.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.jfkj.im.R;
import com.jfkj.im.TIM.component.NoticeLayout;
import com.jfkj.im.TIM.component.TitleBarLayout;
import com.jfkj.im.TIM.modules.chat.interfaces.IChatLayout;
import com.jfkj.im.TIM.modules.chat.layout.input.InputLayout;
import com.jfkj.im.TIM.modules.chat.layout.message.MessageLayout;
import com.jfkj.im.TIM.modules.contact.ContactItemBean;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.utils.ToastUtils;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMCheckFriendResult;
import com.tencent.imsdk.friendship.TIMFriendCheckInfo;
import com.tencent.imsdk.friendship.TIMFriendRelationType;

import java.util.ArrayList;
import java.util.List;

public abstract class ChatLayoutUI extends LinearLayout implements IChatLayout {

    protected NoticeLayout mGroupApplyLayout;
    protected View mRecordingGroup;
    protected ImageView mRecordingIcon;
    protected TextView mRecordingTips;
    private TitleBarLayout mTitleBar;
    private MessageLayout mMessageLayout;
    protected InputLayout mInputLayout;
    private NoticeLayout mNoticeLayout;
    private ChatInfo mChatInfo;
    public NoticeLayout mChatRoomLayout;

    public RelativeLayout rl_time_task;
    public ImageView time_head;
    public TextView tv_time,tv_content,tv_sure;

    public ConstraintLayout constraint_daily_tasks;
    public AppCompatTextView tv_daily_time,tv_daily_content,tv_daily_sure;

    public ChatLayoutUI(Context context) {
        super(context);
        initViews();
    }

    public ChatLayoutUI(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public ChatLayoutUI(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        inflate(getContext(), R.layout.chat_layout, this);

        mTitleBar = findViewById(R.id.chat_title_bar);
        mMessageLayout = findViewById(R.id.chat_message_layout);
        mInputLayout = findViewById(R.id.chat_input_layout);



        mRecordingGroup = findViewById(R.id.voice_recording_view);
        mRecordingIcon = findViewById(R.id.recording_icon);
        mRecordingTips = findViewById(R.id.recording_tips);
        mGroupApplyLayout = findViewById(R.id.chat_group_apply_layout);
        mNoticeLayout = findViewById(R.id.chat_notice_layout);
        mChatRoomLayout = findViewById(R.id.chat_room_notice_layout);

        rl_time_task = findViewById(R.id.rl_time_task);
        time_head = findViewById(R.id.time_head);
        tv_time = findViewById(R.id.tv_time);
        tv_content = findViewById(R.id.tv_content);
        tv_sure = findViewById(R.id.tv_sure);

        constraint_daily_tasks = findViewById(R.id.constraint_daily_tasks);
        tv_daily_time = findViewById(R.id.tv_daily_time);
        tv_daily_content = findViewById(R.id.tv_daily_content);
        tv_daily_sure = findViewById(R.id.tv_daily_sure);

        init();
    }

    protected void init() {
        mTitleBar.setBackgroundColor(Color.parseColor("#1E1E1E"));
    }

    @Override
    public InputLayout getInputLayout() {
        return mInputLayout;
    }

    @Override
    public MessageLayout getMessageLayout() {
        return mMessageLayout;
    }

    @Override
    public NoticeLayout getNoticeLayout() {
        return mNoticeLayout;
    }

    @Override
    public ChatInfo getChatInfo() {
        return mChatInfo;
    }

    @Override
    public void setChatInfo(ChatInfo chatInfo) {
        mChatInfo = chatInfo;
        if (chatInfo == null) {
            return;
        }
        mInputLayout.setChatInfo(mChatInfo);
        String chatTitle = chatInfo.getChatName();
        getTitleBar().setTitle(chatTitle, TitleBarLayout.POSITION.MIDDLE);
        mInputLayout.setName(mChatInfo.getChatName()+"");
        mInputLayout.setUerid(mChatInfo.getId());

        TIMFriendCheckInfo checkinfo=new TIMFriendCheckInfo();
        List<String>  list=new ArrayList<>();
        list.add(mChatInfo.getId());
        checkinfo.setUsers(list);
        TIMFriendshipManager.getInstance().checkFriends(checkinfo, new TIMValueCallBack<List<TIMCheckFriendResult>>() {
            @Override
            public void onError(int code, String desc) {

            }
            @Override
            public void onSuccess(List<TIMCheckFriendResult> timCheckFriendResults) {

                if(timCheckFriendResults.get(0).getResultType()== TIMFriendRelationType.TIM_FRIEND_RELATION_TYPE_NONE){
                    mInputLayout.setVisibility(GONE);
                }else {
                    mInputLayout.setVisibility(VISIBLE);
                }
            }
        });


    }

    @Override
    public void exitChat() {
    }

    @Override
    public void initDefault(FragmentManager fragmentManager,ChatInfo mChatInfo) {

    }

    @Override
    public void loadMessages() {

    }

    @Override
    public void sendMessage(MessageInfo msg, boolean retry) {

    }

    @Override
    public TitleBarLayout getTitleBar() {
        return mTitleBar;
    }

    @Override
    public void setParentLayout(Object parent) {

    }
}
