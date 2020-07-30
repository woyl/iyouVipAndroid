package com.jfkj.im.adapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jfkj.im.R;
import com.jfkj.im.entity.HomeRecommend;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.cache.PreloadManager;
import com.jfkj.im.videoPlay.TikTokView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * Description:  视频播放adapter
 * @author :   ys
 * @date :         2019/11/26
 * </pre>
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private Context mContext;
    private List<HomeRecommend.DataBean> mDataList = new ArrayList<>();
    private boolean isVideo;
    private OnItemClickListener mItemClickListener;

    public VideoAdapter(Context context, List<HomeRecommend.DataBean> list, boolean isVideo, OnItemClickListener listener) {
        this.mContext = context;
        this.mDataList = list;
        this.isVideo = isVideo;
        this.mItemClickListener = listener;
    }

    public void addData(List<HomeRecommend.DataBean> list){
        mDataList.addAll(list);
        notifyItemRangeChanged(mDataList.size(),mDataList.size() + list.size());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeRecommend.DataBean bean = mDataList.get(position);
        holder.mPosition = position;
        holder.mTvUserLevel.setText(bean.getVipLevel() + "");
        holder.mTvUserName.setText(bean.getNickname());
        holder.mTvUserAge.setText(bean.getAge() + "岁");
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if ("1".equals(bean.getGender())){
            holder.mIvUserSex.setBackgroundResource(R.mipmap.icon_male);
        }else {
            holder.mIvUserSex.setBackgroundResource(R.mipmap.icon_female);
        }

        if (!TextUtils.isEmpty(bean.getHomeVideo()) && !"".equals(bean.getHomeVideo())){
            holder.mTikTokView.setVisibility(View.VISIBLE);
            holder.mIvUserHead.setVisibility(View.GONE);
            Glide.with(mContext).load(bean.getHomeVideo()).into(holder.mIvThumb);
            PreloadManager.getInstance(holder.itemView.getContext()).addPreloadTask(bean.getHomeVideo(), position);
        }else {
            holder.mTikTokView.setVisibility(View.GONE);
            holder.mIvUserHead.setVisibility(View.VISIBLE);
            holder.mIvUserHead.setLayoutParams(lp);
            Glide.with(mContext).load(bean.getHead()).into(holder.mIvUserHead);
        }
        holder.mTvStar.setText(bean.getConstellation());
        holder.mLayoutInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userId",bean.getRecommenduid()+"");
                bundle.putString("adapter","From_adapter");
                Log.d("@@@","adapterUserID===" + bean.getRecommenduid());
                JumpUtil.overlay(mContext, UserDetailActivity.class,bundle);
            }
        });

        holder.mIvAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        HomeRecommend.DataBean dataBean = mDataList.get(holder.mPosition);
        PreloadManager.getInstance(holder.itemView.getContext()).removePreloadTask(dataBean.getHomeVideo());
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvUserName;
        TextView mTvUserLevel;
        TextView mTvUserAge;
        TextView mTvAddress;
        TextView mTvStar;
        ImageView mIvAddFriend;
        ImageView mIvUserHead;
        ImageView mIvUserSex;
        ConstraintLayout mLayoutInfo;
        public TikTokView mTikTokView;
        ImageView mIvThumb;
        public int mPosition;
        public FrameLayout container;
//        ConstraintLayout layoutPicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
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
            mIvThumb = mTikTokView.findViewById(R.id.iv_thumb);
//            layoutPicture = itemView.findViewById(R.id.layout_picture);
            //通过tag将viewholder和itemview绑定
            itemView.setTag(this);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
