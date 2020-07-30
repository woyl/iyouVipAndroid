package com.jfkj.im.TIM.modules.conversation;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jfkj.im.R;
import com.jfkj.im.TIM.TUIKit;
import com.jfkj.im.TIM.modules.conversation.base.ConversationInfo;
import com.jfkj.im.TIM.modules.conversation.holder.ConversationBaseHolder;
import com.jfkj.im.TIM.modules.conversation.holder.ConversationCommonHolder;
import com.jfkj.im.TIM.modules.conversation.holder.ConversationCustomHolder;
import com.jfkj.im.TIM.modules.conversation.interfaces.IConversationAdapter;
import com.jfkj.im.TIM.modules.conversation.interfaces.IConversationProvider;
import com.jfkj.im.TIM.utils.ScreenUtil;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ConversationListAdapter extends IConversationAdapter {

    private boolean mHasShowUnreadDot = true;
    private int mItemAvatarRadius = ScreenUtil.getPxByDp(5);
    private int mTopTextSize;
    private int mBottomTextSize;
    private int mDateTextSize;
    public List<ConversationInfo> mDataSource = new ArrayList<>();
    private ConversationListLayout.OnItemClickListener mOnItemClickListener;
    private ConversationListLayout.OnItemLongClickListener mOnItemLongClickListener;

    public ConversationListAdapter() {

    }

    public void setOnItemClickListener(ConversationListLayout.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(ConversationListLayout.OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    public void setDataProvider(IConversationProvider provider) {
        mDataSource = provider.getDataSource();

        if (provider instanceof ConversationProvider) {
            provider.attachAdapter(this);

        }
        notifyDataSetChanged();
    }

    public List<ConversationInfo> getmDataSource() {
        return mDataSource;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(TUIKit.getAppContext());
        RecyclerView.ViewHolder holder = null;
        // 创建不同的 ViewHolder
        View view;
        // 根据ViewType来创建条目
        if (viewType == ConversationInfo.TYPE_CUSTOM) {
            view = inflater.inflate(R.layout.conversation_custom_adapter, parent, false);
            holder = new ConversationCustomHolder(view);
        } else {
            view = inflater.inflate(R.layout.conversation_adapter, parent, false);
            holder = new ConversationCommonHolder(view);
        }
        if (holder != null) {
            ((ConversationBaseHolder) holder).setAdapter(this);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        final ConversationInfo conversationInfo = getItem(position);

        ConversationBaseHolder baseHolder = (ConversationBaseHolder) holder;

        switch (getItemViewType(position)) {
            case ConversationInfo.TYPE_CUSTOM:
                break;
            default:
                //设置点击和长按事件
                if (mOnItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mOnItemClickListener.onItemClick(view, position, conversationInfo);
                        }
                    });
                }
                if (mOnItemLongClickListener != null) {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            mOnItemLongClickListener.OnItemLongClick(view, position, conversationInfo);
                            return true;
                        }
                    });
                }
                break;
        }
        baseHolder.layoutViews(conversationInfo, position);
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof ConversationCommonHolder) {
            ((ConversationCommonHolder) holder).conversationIconView.setBackgroundColor(Color.parseColor("#111111"));
        }
    }

    public ConversationInfo getItem(int position) {
        if (mDataSource.size() == 0)
            return null;
        return mDataSource.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataSource != null) {
            ConversationInfo conversation = mDataSource.get(position);
            return conversation.getType();
        }
        return 1;
    }

    public void addItem(int position, ConversationInfo info) {
        mDataSource.add(position, info);
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mDataSource.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void setItemTopTextSize(int size) {
        mTopTextSize = size;
    }

    public int getItemTopTextSize() {
        return mTopTextSize;
    }

    public void setItemBottomTextSize(int size) {
        mBottomTextSize = size;
    }

    public int getItemBottomTextSize() {
        return mBottomTextSize;
    }

    public void setItemDateTextSize(int size) {
        mDateTextSize = size;
    }

    public int getItemDateTextSize() {
        return mDateTextSize;
    }

    public void setItemAvatarRadius(int radius) {
        mItemAvatarRadius = radius;
    }

    public int getItemAvatarRadius() {
        return mItemAvatarRadius;
    }

    public void disableItemUnreadDot(boolean flag) {
        mHasShowUnreadDot = !flag;
    }

    public boolean hasItemUnreadDot() {
        return mHasShowUnreadDot;
    }
}
