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
import com.jfkj.im.Bean.MyDynamicMessage;
import com.jfkj.im.R;
import com.jfkj.im.ui.home.DynamicDetailActivity;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.JumpUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * Description:  我的消息adapter
 * @author :   ys
 * @date :         2019/12/2
 * </pre>
 */
public class MyMessageAdapter extends RecyclerView.Adapter<MyMessageAdapter.ViewHolder> {

    private Context mContext;
    private List<MyDynamicMessage.DataBean> mList = new ArrayList<>();

    public MyMessageAdapter(Context context, List<MyDynamicMessage.DataBean> list){
        this.mContext = context;
        this.mList = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_circle_my_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyDynamicMessage.DataBean bean = mList.get(position);
        if(bean.getHeads()!=null){
            Glide.with(mContext).load(bean.getHeads()).transform(new CircleCrop()).into(holder.mIvHead);
        }
        if(bean.getHead()!=null){
            Glide.with(mContext).load(bean.getHead()).transform(new CircleCrop()).into(holder.mIvHead);
        }

        String[] split = bean.getArticleImages().split(",");
        Glide.with(mContext).load(split[0]).into(holder.mIvRightImage);
        if(bean.getRuserNickName()!=null){
            holder.mTvName.setText(bean.getRuserNickName());
        }
        if(bean.getUserNickName()!=null){
            holder.mTvName.setText(bean.getUserNickName());
        }

        holder.mTvTime.setText(bean.getAdddate());
        if ("0".equals(bean.getType())){
            //点赞类型
            holder.mTvComment.setVisibility(View.GONE);
            holder.mIvImage.setVisibility(View.VISIBLE);
            holder.mIvImage.setImageResource(R.mipmap.paise_red);
        }else {
            //评论类型
            holder.mTvComment.setVisibility(View.VISIBLE);
            holder.mIvImage.setVisibility(View.GONE);
            holder.mTvComment.setText(bean.getContent());
        }
        holder.mIvHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userId",bean.getUserId());

                JumpUtil.overlay(mContext, UserDetailActivity.class,bundle);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("cuserId",bean.getUserId());
                bundle.putString("articleId",bean.getArticleId());
                JumpUtil.overlay(mContext, DynamicDetailActivity.class,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvHead;
        TextView mTvName;
        ImageView mIvRightImage;
        ImageView mIvImage;
        TextView mTvComment;
        TextView mTvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvHead = itemView.findViewById(R.id.iv_head);
            mTvName = itemView.findViewById(R.id.tv_name);
            mIvRightImage = itemView.findViewById(R.id.iv_right_image);
            mTvComment = itemView.findViewById(R.id.tv_comment);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mIvImage = itemView.findViewById(R.id.iv_image);
        }
    }
}
