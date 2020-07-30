package com.jfkj.im.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.jfkj.im.R;
import com.jfkj.im.entity.RankListBean;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.JumpUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/25
 * </pre>
 */
public class RankListAdapter extends RecyclerView.Adapter<RankListAdapter.ViewHolder> {
    private Context mContext;
    private List<RankListBean> mListBeans = new ArrayList<>();

    public RankListAdapter(Context context, List<RankListBean> list) {
        this.mContext = context;
        this.mListBeans = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rank_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RankListBean rankListBean = mListBeans.get(position);
        int rank = position + 4;
        holder.mTvRank.setText(rank + "");
        Glide.with(mContext).load(rankListBean.getHead()).transform(new CircleCrop()).into(holder.mIvUserHead);
        holder.mTvMoney.setText(String.valueOf(rankListBean.getMoney()));
        holder.mTvName.setText(rankListBean.getNickname());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userId",rankListBean.getUserid()+"");
                JumpUtil.overlay(mContext, UserDetailActivity.class,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvRank;
        ImageView mIvUserHead;
        TextView mTvName;
        TextView mTvMoney;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvRank = itemView.findViewById(R.id.tv_rank);
            mIvUserHead = itemView.findViewById(R.id.iv_user_head);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvMoney = itemView.findViewById(R.id.tv_money);
        }
    }
}
