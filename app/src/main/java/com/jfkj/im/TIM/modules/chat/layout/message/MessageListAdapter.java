package com.jfkj.im.TIM.modules.chat.layout.message;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.jfkj.im.TIM.helper.CustomMessage;
import com.jfkj.im.TIM.modules.chat.C2CChatManagerKit;
import com.jfkj.im.TIM.modules.chat.interfaces.IChatProvider;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.IOnCustomMessageDrawListener;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.MessageBaseHolder;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.MessageContentHolder;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.MessageCustomHolder;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.MessageEmptyHolder;
import com.jfkj.im.TIM.modules.chat.layout.message.holder.MessageHeaderHolder;
import com.jfkj.im.TIM.modules.message.MessageInfo;
import com.jfkj.im.TIM.utils.BackgroundTasks;
import com.tencent.imsdk.TIMCustomElem;

import java.util.ArrayList;
import java.util.List;

import static com.jfkj.im.TIM.helper.CustomMessage.VIDEO_CALL_ACTION_ACCEPTED;
import static com.jfkj.im.TIM.helper.CustomMessage.VIDEO_CALL_ACTION_DIALING;
import static com.jfkj.im.TIM.helper.CustomMessage.VIDEO_CALL_ACTION_LINE_BUSY;


public class MessageListAdapter extends RecyclerView.Adapter {

    public static final int MSG_TYPE_HEADER_VIEW = -99;
    private static final String TAG = MessageListAdapter.class.getSimpleName();
    private boolean mLoading = true;
    private MessageLayout mRecycleView;
    private List<MessageInfo> mDataSource = new ArrayList<>();
    private MessageLayout.OnItemClickListener mOnItemClickListener;
    private IOnCustomMessageDrawListener mOnCustomMessageDrawListener;
    private boolean isUpdate;
    private int position;

    public void setOnCustomMessageDrawListener(IOnCustomMessageDrawListener listener,boolean isUpdata) {
        mOnCustomMessageDrawListener = listener;
        this.isUpdate = isUpdata;
    }

    public MessageLayout.OnItemClickListener getOnItemClickListener() {
        return this.mOnItemClickListener;
    }

    public void setOnItemClickListener(MessageLayout.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = MessageEmptyHolder.Factory.getInstance(parent, this, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        this.position = position;
        final MessageInfo msg = getItem(position);
        MessageBaseHolder baseHolder = (MessageBaseHolder) holder;
        baseHolder.setOnItemClickListener(mOnItemClickListener);
        switch (getItemViewType(position)) {
            case MSG_TYPE_HEADER_VIEW:
                ((MessageHeaderHolder) baseHolder).setLoadingStatus(mLoading);
                break;
            case MessageInfo.MSG_TYPE_TEXT:
            case MessageInfo.MSG_TYPE_IMAGE:
            case MessageInfo.MSG_TYPE_VIDEO:
            case MessageInfo.MSG_TYPE_CUSTOM_FACE:
            case MessageInfo.MSG_TYPE_AUDIO:
            case MessageInfo.MSG_TYPE_FILE:
            case MessageInfo.MSG_TYPE_CUSTOM:
                break;
            default:
                break;
        }

        baseHolder.layoutViews(msg, position);
        // 对于自定义消息，需要在正常布局之后，交给外部调用者重新加载渲染
        if (getItemViewType(position) == MessageInfo.MSG_TYPE_CUSTOM) {
            MessageCustomHolder customHolder = (MessageCustomHolder) holder;
            if (mOnCustomMessageDrawListener != null) {
                mOnCustomMessageDrawListener.onDraw(customHolder, msg);
                //
                if (msg.getElement() instanceof TIMCustomElem) {
                    TIMCustomElem elem = (TIMCustomElem)msg.getElement();
                    CustomMessage data = new Gson().fromJson(new String(elem.getData()), CustomMessage.class);
                    if(data!=null)
                    if (data.videoState == VIDEO_CALL_ACTION_DIALING || data.videoState == VIDEO_CALL_ACTION_ACCEPTED||data.videoState == VIDEO_CALL_ACTION_LINE_BUSY) {
                        C2CChatManagerKit.getInstance().deleteMessage(position-1,msg);
                    }
                }
            }
        }

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecycleView = (MessageLayout) recyclerView;
    }

    public void showLoading() {
        if (mLoading) {
            return;
        }
        mLoading = true;
        notifyItemChanged(0);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof MessageContentHolder) {
            ((MessageContentHolder) holder).msgContentFrame.setBackground(null);
        }
    }

    public void notifyDataSourceChanged(final int type, final int value) {
        BackgroundTasks.getInstance().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoading = false;
                if (type == MessageLayout.DATA_CHANGE_TYPE_REFRESH) {
                    notifyDataSetChanged();
                    if (!isUpdate){
                        mRecycleView.scrollToEnd();
                    }
                } else if (type == MessageLayout.DATA_CHANGE_TYPE_ADD_BACK) {
                    notifyItemRangeInserted(mDataSource.size() + 1, value);
                    notifyDataSetChanged();
                    mRecycleView.scrollToEnd();
                } else if (type == MessageLayout.DATA_CHANGE_TYPE_UPDATE) {
//                    notifyItemChanged(value + 1);
                    notifyDataSetChanged();
                } else if (type == MessageLayout.DATA_CHANGE_TYPE_LOAD || type == MessageLayout.DATA_CHANGE_TYPE_ADD_FRONT) {
                    //加载条目为数0，只更新动画
                    if (value == 0) {
                        notifyItemChanged(0);
                    } else {
                        //加载过程中有可能之前第一条与新加载的最后一条的时间间隔不超过5分钟，时间条目需去掉，所以这里的刷新要多一个条目
                        if (getItemCount() > value) {
                            notifyItemRangeInserted(0, value);
                        } else {
                            notifyItemRangeInserted(0, value);
                        }
                    }
                } else if (type == MessageLayout.DATA_CHANGE_TYPE_DELETE) {
                    notifyItemRemoved(value + 1);
                    notifyDataSetChanged();
                    mRecycleView.scrollToEnd();
                } else if (type == MessageLayout.DATA_CHANGE_TYPE_GIF) {

                    mRecycleView.scrollToEnd();
                }
            }

        }, 100);
    }

    @Override
    public int getItemCount() {
        return mDataSource.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MSG_TYPE_HEADER_VIEW;
        }
        MessageInfo msg = getItem(position);
        return msg.getMsgType();
    }

    public void setDataSource(IChatProvider provider) {
        if (provider == null) {
            mDataSource.clear();
        } else {
            mDataSource = provider.getDataSource();
            provider.setAdapter(this);
        }
        notifyDataSourceChanged(MessageLayout.DATA_CHANGE_TYPE_REFRESH, getItemCount());
    }

    public MessageInfo getItem(int position) {
        if (position == 0 || mDataSource.size() == 0) {
            return null;
        }
        MessageInfo info = mDataSource.get(position - 1);
        return info;
    }
}