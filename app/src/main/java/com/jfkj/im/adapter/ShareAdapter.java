package com.jfkj.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jfkj.im.Bean.ShareBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.PrivateConstants;
import com.jfkj.im.utils.WxShareUtils;
import com.nc.player.util.L;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import java.util.List;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareViewHolder> {
    List<ShareBean> shareBeanList;
    LayoutInflater layoutInflater;
    Context context;
    private OnShareItemClick itemClick;
    public ShareAdapter(Context context,OnShareItemClick itemClick) {
        this.context = context;
        this.itemClick = itemClick;
        layoutInflater= LayoutInflater.from(context);
    }

    public void setShareBeanList(List<ShareBean> shareBeanList) {
        this.shareBeanList = shareBeanList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.item_share,parent,false);
        return new ShareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareViewHolder holder, int position) {
        ShareBean shareBean = shareBeanList.get(position);
        holder.textView.setText(shareBean.getTitle());
        Glide.with(context).load(shareBean.getId()).into(holder.imageView);

        holder.llStartShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shareBeanList!=null?shareBeanList.size():0;
    }

    class ShareViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView imageView;
        TextView textView;
        private final LinearLayout llStartShare;

        public ShareViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv);
            imageView = itemView.findViewById(R.id.iv);
            llStartShare = itemView.findViewById(R.id.ll_start_share);
        }
    }

   public interface OnShareItemClick{
        void onItemClick(int position);
    }
}
