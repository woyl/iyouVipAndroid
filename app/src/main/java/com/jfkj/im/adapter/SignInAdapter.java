package com.jfkj.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.jfkj.im.Bean.SignInBean;
import com.jfkj.im.R;
import com.jfkj.im.ui.home.UserDetailActivity;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class SignInAdapter extends RecyclerView.Adapter<SignInAdapter.ViewHolder>   {


    private LayoutInflater layoutInflater;
    private Context context;
    private SignInBean signInBean;
    private OnItemClick onItemClick;


    public SignInAdapter(Context context,SignInBean signInBean,OnItemClick onItemClick) {
        this.context = context;
        this.signInBean = signInBean;
        this.layoutInflater = LayoutInflater.from(context);
        this.onItemClick  = onItemClick;
    }


    public  void setItem(SignInBean bean,int position){
        this.signInBean = bean;
        notifyItemChanged(position);
    }


    public void setDate(SignInBean signInBean){
        this.signInBean = signInBean;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view =   layoutInflater.inflate(R.layout.item_signin_layout,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //0未操作 1确认到场 2投诉审核中 3投诉成功 4投诉未通过
        holder.tvPosition.setText(position + 1 + "");
        Glide.with(context).load(signInBean.getData().getUserList().get(position).getHead()).transform(new CircleCrop()).into(holder.imgHead);
        holder.tvName.setText(signInBean.getData().getUserList().get(position).getNickName());
        holder.tvVipLevel.setText("VIP" + signInBean.getData().getUserList().get(position).getVipLevel());

        String result = "";


        //0:未结束  1 结束
        if(signInBean.getData().getIsEnd() == 1){
            if(signInBean.getData().getUserList().get(position).getIstate() == 0){
                result = signInBean.getData().getUserList().get(position).getNote();
                holder.rl_button.setVisibility(View.VISIBLE);
            }else if(signInBean.getData().getUserList().get(position).getIstate()  ==1 ){
                result = "<font color=\"#FF2B66\">参与成功</font>";
                holder.rl_button.setVisibility(View.GONE);
            }else if(signInBean.getData().getUserList().get(position).getIstate() ==2 ){
                Drawable drawable = ContextCompat.getDrawable(context,R.drawable.shap_shu_gray);
                Objects.requireNonNull(drawable).setBounds(0,0,10,10);
                result = "<font color=\"#767676\">投诉审核中</font>";
                holder.tvResult.setCompoundDrawables(drawable,null,null,null);
                holder.rl_button.setVisibility(View.GONE);
            }else if (signInBean.getData().getUserList().get(position).getIstate() ==3 ){
                result = "<font color=\"#FFFFFF\">投诉成功</font>";
                holder.rl_button.setVisibility(View.GONE);
            }else if (signInBean.getData().getUserList().get(position).getIstate() ==4 ){
                result = "<font color=\"#767676\">投诉未通过</font>";

                holder.rl_button.setVisibility(View.VISIBLE);
            }

            // 如要字体颜色对应状态 ，  可用   Html
            holder.tvResult.setText(Html.fromHtml(result));

        }else{
            holder.tvResult.setText(signInBean.getData().getUserList().get(position).getNote());
            holder.rl_button.setVisibility(View.GONE);
        }


        holder.tvComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.confirmPresence(signInBean.getData().getUserList().get(position).getUserId(),2,position);

            }
        });

        holder.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInBean.getData().getUserList().get(position).getUserId();
                onItemClick.confirmPresence(signInBean.getData().getUserList().get(position).getUserId(),1,position);

            }
        });

        holder.imgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击头像 进入用户详情

                Intent intent = new Intent(context, UserDetailActivity.class);
                intent.putExtra("userId", signInBean.getData().getUserList().get(position).getUserId());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
       // return signInBean == null ? 0 :  signInBean.getData().getUserList() == null ? 0 : signInBean.getData().getUserList().size();

        return signInBean == null? 0 : signInBean.getData()==null?0:signInBean.getData().getUserList()==null?0:signInBean.getData().getUserList().size();
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView tvPosition;
        private final ImageView imgHead;
        private final TextView tvName;
        private final TextView tvVipLevel;
        private final TextView tvResult;
        private final TextView tvComplaint;
        private final TextView tvSubmit;
        private final RelativeLayout rl_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPosition = itemView.findViewById(R.id.tv_position);
            imgHead = itemView.findViewById(R.id.img_icon);
            tvName = itemView.findViewById(R.id.tv_name);
            tvVipLevel = itemView.findViewById(R.id.tv_vip_level);
            tvResult = itemView.findViewById(R.id.tv_result);
            tvComplaint = itemView.findViewById(R.id.tv_complaint);
            tvSubmit = itemView.findViewById(R.id.tv_submit);
            rl_button = itemView.findViewById(R.id.rl_button);

        }
    }


  public  interface OnItemClick{
        //type 1: 确认到场   2.投诉
        void confirmPresence(String userId,int type,int position);

    }
}
