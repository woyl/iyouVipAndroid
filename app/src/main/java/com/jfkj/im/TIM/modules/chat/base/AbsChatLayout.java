package com.jfkj.im.TIM.modules.chat.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.TIM.base.IUIKitCallBack;
import com.jfkj.im.TIM.component.AudioPlayer;
import com.jfkj.im.TIM.modules.chat.interfaces.IChatLayout;
import com.jfkj.im.TIM.modules.chat.interfaces.IChatProvider;
import com.jfkj.im.TIM.modules.chat.layout.input.InputLayout;
import com.jfkj.im.TIM.modules.chat.layout.message.MessageLayout;
import com.jfkj.im.TIM.modules.chat.layout.message.MessageListAdapter;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.modules.message.MessageInfoUtil;
import com.jfkj.im.TIM.redpack.chatroom.PayChatRoomUtils;
import com.jfkj.im.TIM.utils.BackgroundTasks;
import com.jfkj.im.TIM.utils.NetWorkUtils;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.interfaces.ResponListener;
import com.jfkj.im.ui.dialog.ChargeDialog;
import com.jfkj.im.ui.dialog.PublicComCancelDialog;
import com.jfkj.im.ui.dialog.SendMessageDialogFragment;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMValueCallBack;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;


public abstract class AbsChatLayout extends ChatLayoutUI implements IChatLayout {

    protected MessageListAdapter mAdapter;

    private AnimationDrawable mVolumeAnim;
    private Runnable mTypingRunnable = null;
    private ChatProvider.TypingListener mTypingListener = new ChatProvider.TypingListener() {
        @Override
        public void onTyping() {
            final String oldTitle = getTitleBar().getMiddleTitle().getText().toString();
            getTitleBar().getMiddleTitle().setText(R.string.typing);
            if (mTypingRunnable == null) {
                mTypingRunnable = new Runnable() {
                    @Override
                    public void run() {
                        getTitleBar().getMiddleTitle().setText(oldTitle);
                    }
                };
            }
            getTitleBar().getMiddleTitle().removeCallbacks(mTypingRunnable);
            getTitleBar().getMiddleTitle().postDelayed(mTypingRunnable, 3000);
        }
    };

    public AbsChatLayout(Context context) {
        super(context);
    }

    public AbsChatLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AbsChatLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initListener() {
        getMessageLayout().setPopActionClickListener(new MessageLayout.OnPopActionClickListener() {
            @Override
            public void onCopyClick(int position, MessageInfo msg) {
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard == null || msg == null) {
                    return;
                }
                if (msg.getElement() instanceof TIMTextElem) {
                    TIMTextElem textElem = (TIMTextElem) msg.getElement();
                    ClipData clip = ClipData.newPlainText("message", textElem.getText());
                    clipboard.setPrimaryClip(clip);
                }
            }

            @Override
            public void onSendMessageClick(MessageInfo msg, boolean retry) {
                sendMessage(msg, retry);
            }

            @Override
            public void onDeleteMessageClick(int position, MessageInfo msg) {
                deleteMessage(position, msg);
            }

            @Override
            public void onRevokeMessageClick(int position, MessageInfo msg) {
                revokeMessage(position, msg);
            }
        });
        getMessageLayout().setLoadMoreMessageHandler(new MessageLayout.OnLoadMoreHandler() {
            @Override
            public void loadMore() {
                loadMessages();
            }
        });
        getMessageLayout().setEmptySpaceClickListener(new MessageLayout.OnEmptySpaceClickListener() {
            @Override
            public void onClick() {
                getInputLayout().hideSoftInput();
            }
        });

        /**
         * 设置消息列表空白处点击处理
         */
        getMessageLayout().addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_UP) {
                    View child = rv.findChildViewUnder(e.getX(), e.getY());
                    if (child == null) {
                        getInputLayout().hideSoftInput();
                    } else if (child instanceof ViewGroup) {
                        ViewGroup group = (ViewGroup) child;
                        final int count = group.getChildCount();
                        float x = e.getRawX();
                        float y = e.getRawY();
                        View touchChild = null;
                        for (int i = count - 1; i >= 0; i--) {
                            final View innerChild = group.getChildAt(i);
                            int position[] = new int[2];
                            innerChild.getLocationOnScreen(position);
                            if (x >= position[0]
                                    && x <= position[0] + innerChild.getMeasuredWidth()
                                    && y >= position[1]
                                    && y <= position[1] + innerChild.getMeasuredHeight()) {
                                touchChild = innerChild;
                                break;
                            }
                        }
                        if (touchChild == null)
                            getInputLayout().hideSoftInput();
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        getInputLayout().setChatInputHandler(new InputLayout.ChatInputHandler() {
            @Override
            public void onInputAreaClick() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        scrollToEnd();
                    }
                });
            }

            @Override
            public void onRecordStatusChanged(int status) {
                switch (status) {
                    case RECORD_START:
                        startRecording();
                        break;
                    case RECORD_STOP:
                        stopRecording();
                        break;
                    case RECORD_CANCEL:
                        cancelRecording();
                        break;
                    case RECORD_TOO_SHORT:
                    case RECORD_FAILED:
                        stopAbnormally(status);
                        break;
                    default:
                        break;
                }
            }

            private void startRecording() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        AudioPlayer.getInstance().stopPlay();
                        mRecordingGroup.setVisibility(View.VISIBLE);
                        mRecordingIcon.setImageResource(R.drawable.recording_volume);
                        mVolumeAnim = (AnimationDrawable) mRecordingIcon.getDrawable();
                        mVolumeAnim.start();
                        mRecordingTips.setTextColor(Color.WHITE);
                        mRecordingTips.setText("手指上滑，取消发送");
                    }
                });
            }

            private void stopRecording() {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVolumeAnim.stop();
                        mRecordingGroup.setVisibility(View.GONE);
                    }
                }, 500);
            }

            private void stopAbnormally(final int status) {
                post(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        mVolumeAnim.stop();
                        mRecordingIcon.setImageResource(R.drawable.ic_volume_dialog_length_short);
                        mRecordingTips.setTextColor(Color.WHITE);
                        if (status == RECORD_TOO_SHORT) {
                            mRecordingTips.setText("说话时间太短");
                        } else {
                            mRecordingTips.setText("录音至少1S以上");
                        }
                    }
                });
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecordingGroup.setVisibility(View.GONE);
                    }
                }, 1000);
            }

            private void cancelRecording() {
                post(new Runnable() {
                    @Override
                    public void run() {
                        mRecordingIcon.setImageResource(R.drawable.ic_volume_dialog_cancel);
                        mRecordingTips.setText("松开手指，取消发送");
                    }
                });
            }
        });
    }

    @Override
    public void initDefault(FragmentManager fragmentManager,ChatInfo mChatInfo) {
        getTitleBar().getLeftGroup().setVisibility(View.VISIBLE);
        getTitleBar().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).finish();
                }
            }
        });
        getInputLayout().setMessageHandler(new InputLayout.MessageHandler() {
            @Override
            public void sendMessage(MessageInfo msg) {
                if (mChatInfo.isChatRoom()){
                    if (SPUtils.getInstance(App.getAppContext()).getBoolean(Utils.APPID+Utils.IS_TIPS)) {
                        paychatroom(fragmentManager,msg,mChatInfo);
                    } else {
                        SendMessageDialogFragment dialogFragment = new SendMessageDialogFragment(true, Gravity.CENTER);
                        dialogFragment.setResponListener(new ResponListener<Boolean>() {
                            @Override
                            public void Rsp(Boolean s) {
                                if (s){
                                    paychatroom(fragmentManager,msg,mChatInfo);
                                }
                            }
                        });
                        if (fragmentManager != null) {
                            dialogFragment.show(fragmentManager,"");
                        }
                    }
                }else {
                    AbsChatLayout.this.sendMessage(msg, false);
                }
            }
        });
        getInputLayout().clearCustomActionList();
        if (getMessageLayout().getAdapter() == null) {
            mAdapter = new MessageListAdapter();
            getMessageLayout().setAdapter(mAdapter);
        }
        initListener();
    }

    private void paychatroom(FragmentManager fragmentManager,MessageInfo msg,ChatInfo mChatInfo) {
        PayChatRoomUtils.pay(new TIMValueCallBack<Boolean>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (aBoolean) {
                    AbsChatLayout.this.sendMessage(msg, false);
                }
            }
        }, new TIMValueCallBack<Boolean>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {
//                ImageSpan imageSpan = new ImageSpan(Objects.requireNonNull(getActivity()), R.mipmap.icon_diamond_yellow);
//                SpannableString spannableString = new SpannableString("您的余额不足，请及时充值！");
//                spannableString.setSpan(imageSpan, 13, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                PublicComCancelDialog dialog = new PublicComCancelDialog(true, Gravity.CENTER, "温馨提示",
                        "您的余额不足，请及时充值！", "立即充值",true);
                dialog.setRsp(new ResponListener<Boolean>() {
                    @Override
                    public void Rsp(Boolean s) {
                        if (s){
                            ChargeDialog mChargeDialog = new ChargeDialog(Objects.requireNonNull(getContext()), (Activity) getContext());
                            mChargeDialog.show();
                        }
                    }
                });
                if (fragmentManager != null) {
                    dialog.show(fragmentManager, "");
                }
            }
        });
    }

    @Override
    public void setParentLayout(Object parentContainer) {

    }

    public void scrollToEnd() {
        getMessageLayout().scrollToEnd();
    }

    public void setDataProvider(IChatProvider provider) {
        if (provider != null) {
            ((ChatProvider) provider).setTypingListener(mTypingListener);
        }

        if (mAdapter != null) {
            mAdapter.setDataSource(provider);
        }
    }

    public abstract ChatManagerKit getChatManager();

    @Override
    public void loadMessages() {

        loadChatMessages(mAdapter.getItemCount() > 0 ? mAdapter.getItem(1) : null);
    }

    public void loadChatMessages(final MessageInfo lastMessage) {

       //群组离线消息需要加载
        if(getChatManager().isGroup()){
            long num = getChatManager().mCurrentConversation.getUnreadMessageNum();
            if(num>0){
                getChatManager().loadChatMessages(lastMessage, new IUIKitCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        if (lastMessage == null && data != null) {
                            setDataProvider((ChatProvider) data);
                        }
                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {

                        ToastUtil.toastLongMessage(errCode + "\t\t" + errMsg);
                        if (lastMessage == null) {
                            setDataProvider(null);
                        }
                    }
                });
            }
        }
        //加载本地消息  //加载远程消息loadChatMessages  loadLocalChatMessages
        getChatManager().loadChatMessages(lastMessage, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                if (lastMessage == null && data != null) {
                    setDataProvider((ChatProvider) data);
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

                ToastUtil.toastLongMessage(errMsg);
                if (lastMessage == null) {
                    setDataProvider(null);
                }
            }
        });
    }

    protected void deleteMessage(int position, MessageInfo msg) {
        getChatManager().deleteMessage(position, msg);
    }

    protected void revokeMessage(int position, MessageInfo msg) {
        getChatManager().revokeMessage(true,position, msg);
    }

    @Override
    public void sendMessage(MessageInfo msg, boolean retry) {
        getChatManager().sendMessage(msg, retry, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        scrollToEnd();
                    }
                });
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

                ToastUtil.toastLongMessage(errMsg+errCode);

            }
        });
    }


    @Override
    public void exitChat() {
        getTitleBar().getMiddleTitle().removeCallbacks(mTypingRunnable);
        AudioPlayer.getInstance().stopRecord();
        AudioPlayer.getInstance().stopPlay();
        if (getChatManager() != null) {
            getChatManager().destroyChat();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        exitChat();
    }


}
