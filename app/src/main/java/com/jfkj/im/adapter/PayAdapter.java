package com.jfkj.im.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.jfkj.im.Bean.CzModeListBean;
import com.jfkj.im.R;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.Paydialog;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayViewHolder> {
    CzModeListBean czModeListBean;
    Context context;
    LayoutInflater layoutInflater;


    Paydialog.OnClickOutListener listener;


    public PayAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setCzModeListBean(CzModeListBean czModeListBean, Paydialog.OnClickOutListener listener) {
        this.czModeListBean = czModeListBean;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PayViewHolder(layoutInflater.inflate(R.layout.item_pay, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PayViewHolder holder, int position) {
        CzModeListBean.DataBean dataBean = czModeListBean.getData().get(position);
        holder.tv_name.setText(dataBean.getIway());
        Glide.with(context).load(ApiStores.baseJsonUrl + dataBean.getCimgurl()).transform(new CircleCrop()).into(holder.img_icon);
        if (dataBean.isselected) {
            holder.iv.setBackgroundResource(R.drawable.dialog_radio_btn_sel);
        } else {
            holder.iv.setBackgroundResource(R.drawable.pop_radio_btn_unsel);
        }

    }

    @Override
    public int getItemCount() {
        return czModeListBean != null ? czModeListBean.getData().size() : 0;
    }

    class PayViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_name;
        private final ImageView iv;
        private final ImageView img_icon;
        private final LinearLayout ll_play;


        public PayViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            iv = itemView.findViewById(R.id.iv);
            img_icon = itemView.findViewById(R.id.img_icon);
            ll_play = itemView.findViewById(R.id.ll_play);
        }
    }

}
