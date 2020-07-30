package com.jfkj.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jfkj.im.R;
import com.jfkj.im.TIM.modules.chat.base.ChatInfo;
import com.jfkj.im.TIM.utils.Constants;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.HomeRecommend;
import com.jfkj.im.ui.activity.ChatActivity;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.utils.cache.PreloadManager;
import com.jfkj.im.videoPlay.TikTokView;
import com.jfkj.im.view.ResizableImageView;
import com.lzy.okgo.utils.OkLogger;
import com.tencent.imsdk.TIMConversationType;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * Description:  视频播放adapter
 * @author :   ys
 * @date :         2019/11/26
 * </pre>
 */
public class HomeVideoAdapter extends RecyclerView.Adapter<HomeVideoAdapter.ViewHolder> {

    private Context mContext;
    private List<HomeRecommend.DataBean> mDataList = new ArrayList<>();
    private boolean isVideo;
    private OnItemClickListener mItemClickListener;

    private int  thisPosition;

    public HomeVideoAdapter(Context context, List<HomeRecommend.DataBean> list, boolean isVideo, OnItemClickListener listener) {
        this.mContext = context;
        this.mDataList = list;
        this.isVideo = isVideo;
        this.mItemClickListener = listener;
    }

    public void addData(List<HomeRecommend.DataBean> list) {
        mDataList.addAll(list);
        notifyItemRangeChanged(mDataList.size() - list.size(), mDataList.size());
    }

    @NonNull
    @Override
    public HomeVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_video, parent, false);
        return new HomeVideoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeVideoAdapter.ViewHolder holder, int position) {
        HomeRecommend.DataBean bean = mDataList.get(position);
        holder.mPosition = position;
        Log.d("@@@","adapter positionposition " + position);

        this.thisPosition = position;

        if (bean.getVipLevel() != null && !"".equals(bean.getVipLevel())) {
            holder.mTvUserLevel.setVisibility(View.VISIBLE);
            holder.mTvUserLevel.setText("VIP" + bean.getVipLevel());
        } else {
            holder.mTvUserLevel.setVisibility(View.GONE);
        }
//        if (!TextUtils.isEmpty(bean.getVipCard())) {
//            holder.mIvVipCard.setVisibility(View.VISIBLE);
//        } else {
//            holder.mIvVipCard.setVisibility(View.GONE);
//        }
        holder.mTvUserName.setText(bean.getNickname().trim());
        holder.mTvUserAge.setText(bean.getAge() + "岁");
        if (!TextUtils.isEmpty(bean.getPosition())){
            holder.mTvAddress.setText(bean.getPosition());
        }else {
            holder.mTvAddress.setText("");
        }
//        if ("3".equals(bean.getVipCard())){
//            holder.mIvVipCard.setVisibility(View.VISIBLE);
//            holder.mIvVipCard.setImageResource(R.drawable.vip_icon_ferrari);
//        }else {
//            holder.mIvVipCard.setVisibility(View.GONE);
//        }
        if (bean.getGender() == 1) {
            holder.mIvUserSex.setBackgroundResource(R.mipmap.icon_male);
        } else {
            holder.mIvUserSex.setBackgroundResource(R.mipmap.icon_female);
        }
        if (!TextUtils.isEmpty(bean.getHomeVideo())) {
            holder.mTikTokView.setVisibility(View.VISIBLE);

            holder.mIvUserHead.setVisibility(View.GONE);
            Glide.with(mContext).load(bean.getHomeVideo()).into(holder.mIvThumb);
            PreloadManager.getInstance(holder.itemView.getContext()).addPreloadTask(bean.getHomeVideo(), position);
        } else {
            holder.mTikTokView.setVisibility(View.GONE);

            holder.mIvUserHead.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(bean.getHead()).placeholder(R.color.color_ff161823).error(R.color.color_ff161823).into(holder.mIvUserHead);
        }
        holder.mTvStar.setText(bean.getConstellation());
        holder.mIvUserHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userId", bean.getRecommenduid());
                bundle.putString("adapter", "From_adapter");
                JumpUtil.overlay(mContext, UserDetailActivity.class, bundle);
            }
        });

        if (!TextUtils.isEmpty(bean.getHomeVideo())){
            holder.mTikTokView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("userId",mDataList.get(position).getRecommenduid());
                    bundle.putString("adapter", "From_adapter");
                    JumpUtil.overlay(mContext, UserDetailActivity.class, bundle);
                    holder.mTikTokView.setShow2();
                }
            });
        }
        holder.mIvAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(position);
            }
        });
        if (bean.getIsfriend() == 1) {
            holder.layout_chat_gift.setVisibility(View.VISIBLE);
            holder.mIvAddFriend.setVisibility(View.GONE);
            holder.iv_gift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("sendgift");
                    intent.putExtra(Utils.NICKNAME, bean.getNickname());
                    intent.putExtra(Utils.RECEIVEID, bean.getRecommenduid());
                    intent.putExtra(Utils.CHAT_HEAD_URL, bean.getHead());
                    mContext.sendBroadcast(intent);
                }
            });

            holder.iv_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentchat = new Intent(mContext, ChatActivity.class);
                    ChatInfo chatInfo = new ChatInfo();
                    chatInfo.setType(TIMConversationType.C2C);
                    chatInfo.setId(bean.getRecommenduid());
                    chatInfo.setChatName(bean.getNickname());
                    intentchat.putExtra(Constants.CHAT_INFO, chatInfo);
                    intentchat.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intentchat);

                }
            });
        } else {
            holder.layout_chat_gift.setVisibility(View.GONE);
            holder.mIvAddFriend.setVisibility(View.VISIBLE);
            if (bean.getRecommenduid().equals(Utils.APPID)) {
                holder.mIvAddFriend.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull HomeVideoAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
            PreloadManager.getInstance(holder.itemView.getContext()).removeAllPreloadTask();
//        try {
//            HomeRecommend.DataBean dataBean = mDataList.get(holder.mPosition);
//            Log.d("@@@","holder.mPositionholder.mPosition === " + holder.mPosition);
//        }catch (Throwable e){
//            e.printStackTrace();
//        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TikTokView mTikTokView;
        public int mPosition;
        public FrameLayout container;
        TextView mTvUserName;
        TextView mTvUserLevel;
        TextView mTvUserAge;
        TextView mTvAddress;
        TextView mTvStar;
        ImageView mIvAddFriend;
        ResizableImageView mIvUserHead;
        ImageView mIvUserSex;
        ImageView mIvVipCard;
        ConstraintLayout mLayoutInfo;
        ImageView mIvThumb;
        LinearLayout layout_chat_gift;
        ImageView iv_message;
        ImageView iv_gift;
//        private final View view;


        //        ConstraintLayout layoutPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            view = itemView.findViewById(R.id.view);
            mIvAddFriend = itemView.findViewById(R.id.iv_add_friend);
            mLayoutInfo = itemView.findViewById(R.id.layout_info);
            mIvUserHead = itemView.findViewById(R.id.iv_user_head);
            mTvUserName = itemView.findViewById(R.id.tv_user_name);
            mTvUserLevel = itemView.findViewById(R.id.tv_user_level);
            mIvUserSex = itemView.findViewById(R.id.iv_user_sex);
            mTvUserAge = itemView.findViewById(R.id.tv_user_age);
            mTvAddress = itemView.findViewById(R.id.tv_user_address);
            mTvStar = itemView.findViewById(R.id.tv_user_constellation);
            mTikTokView = itemView.findViewById(R.id.tiktok_view);
            container = itemView.findViewById(R.id.container);
//            mIvVipCard = itemView.findViewById(R.id.iv_vip_card);
            mIvThumb = mTikTokView.findViewById(R.id.iv_thumb);

            layout_chat_gift = itemView.findViewById(R.id.layout_chat_gift);
            iv_message = itemView.findViewById(R.id.iv_message);
            iv_gift = itemView.findViewById(R.id.iv_gift);
            //            layoutPicture = itemView.findViewById(R.id.layout_picture);
            //通过tag将viewholder和itemview绑定
            itemView.setTag(this);

        }
    }
}
