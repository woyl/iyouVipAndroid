package com.jfkj.im.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jfkj.im.Bean.RankingBean;
import com.jfkj.im.R;
import com.jfkj.im.okhttp.HttpObserver;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.media.ScreenUtil;
import com.jfkj.im.view.GlideRoundTransform;

import java.util.List;

import butterknife.BindView;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankHolder> {
    List<RankingBean.DataBean> list;
    Context context;
    LayoutInflater layoutInflater;
    private int screenHeiht, screenWith;

    public RankAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        screenHeiht = ScreenUtil.getScreenHeight();
        screenWith = ScreenUtil.getScreenWidth();
    }

    public void setList(List<RankingBean.DataBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new RankHolder(layoutInflater.inflate(R.layout.item_rank, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RankHolder holder, int position) {
        RankingBean.DataBean dataBean = list.get(position);
        //设置图片圆角角度
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        holder.fl.post(new Runnable() {
            @Override
            public void run() {
                int w = holder.fl.getWidth();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) holder.fl.getLayoutParams();
                layoutParams.height = w;
                layoutParams.width = w;
                Glide.with(context).load(dataBean.getHead())
                        .transform(new CenterCrop(), new GlideRoundTransform(3))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.fl);
            }
        });

        holder.tv_name1.setText(dataBean.getNickname());
        holder.tv_number1.setText(dataBean.getMoney() + "");
//         holder.tv_position.setText("#"+(position+2));


        holder.fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("userId", list.get(position).getUserid());
                JumpUtil.overlay(context, UserDetailActivity.class, bundle);
            }
        });

        if (position + 2 == 2) {
//             holder.iv_ranking.setBackgroundResource(R.drawable.list_icon_second);
        } else if (position + 2 == 3) {
//             holder.iv_ranking.setBackgroundResource(R.drawable.list_icon_third);
        }

        int padding = ScreenUtil.dpToPx(5);

        if(position%2==0){
            if (position == 0) {
                holder.constraintLayout.setPadding(0,padding*2,padding,0);
            } else {
                holder.constraintLayout.setPadding(0,padding,padding,0);
            }

        }
        if(position%2!=0){
            if (position == 1){
                holder.constraintLayout.setPadding(0,padding * 2,padding,0);
            } else {
                holder.constraintLayout.setPadding(0,padding,padding,0);
            }

            if(position-1>0){
                holder.constraintLayout.setPadding(0,padding,padding,padding);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class RankHolder extends RecyclerView.ViewHolder {
        ImageView fl;
        TextView tv_name1;
        TextView tv_number1;
        TextView tv_position;
        ConstraintLayout constraintLayout;
        private final ImageView iv_ranking;

        public RankHolder(@NonNull View itemView) {
            super(itemView);
            fl = itemView.findViewById(R.id.fl);
            tv_name1 = itemView.findViewById(R.id.tv_name1);
            tv_number1 = itemView.findViewById(R.id.tv_number1);
            tv_position = itemView.findViewById(R.id.tv_position);
            constraintLayout = itemView.findViewById(R.id.constraintlayout);
            iv_ranking = itemView.findViewById(R.id.iv_ranking);
        }
    }
}
