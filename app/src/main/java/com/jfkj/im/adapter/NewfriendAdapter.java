package com.jfkj.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jfkj.im.Bean.NewfriendBean;
import com.jfkj.im.R;
import com.jfkj.im.TIM.component.photoview.Util;
import com.jfkj.im.TIM.redpack.FriendsUtils;
import com.jfkj.im.utils.Utils;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewfriendAdapter extends RecyclerView.Adapter<NewfriendAdapter.NewfriendHolder> {
    Context context;
    LayoutInflater layoutInflater;
    List<NewfriendBean> list;


    public NewfriendAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setList(List<NewfriendBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(NewfriendBean newfriendBean) {
        if (list != null) {
            list.add(newfriendBean);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public NewfriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_newfriend, parent, false);
        return new NewfriendHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewfriendHolder holder, int position) {
        NewfriendBean newfriendBean = list.get(position);

        FriendsUtils.getUsersProfileLoaclService(newfriendBean.getSendid(), new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(TIMUserProfile timUserProfile) {
                Glide.with(context).load(timUserProfile.getFaceUrl()).into(holder.circleImageView);
            }
        });


        holder.tv_user_name.setText(newfriendBean.getUser_name());
        try {
            Object typeObject = new JSONTokener(newfriendBean.getUser_name()).nextValue();

            JSONObject jsonObject = new JSONObject(newfriendBean.getHint_message());
            String[] split = jsonObject.getString("gift").split("_");
            holder.tv_sizegift.setText("×" + split[1]);
            for (int i = 0; i < Utils.giftList.size(); i++) {

                if (Utils.giftList.get(i).getGiftId().equals(split[0])) {

                    Glide.with(context).load(Utils.giftList.get(i).getPictureUrl()).into(holder.iv_gift);
                }
            }
            holder.tv_hint_message.setText(jsonObject.getString("des"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.tv_rejected.setVisibility(View.GONE);

        if (newfriendBean.getGiftsize().equals("0")) {
            holder.tv_sizegift.setVisibility(View.GONE);
        } else {
            holder.tv_sizegift.setVisibility(View.VISIBLE);

        }
        if (newfriendBean.getType().equals("2")) {
            holder.tv_rejected.setVisibility(View.VISIBLE);
            holder.tv_reject.setVisibility(View.GONE);
            holder.tv_accept.setVisibility(View.GONE);
            holder.tv_rejected.setText("已拒绝");
        }
        if (newfriendBean.getType().equals("1")) {
            holder.tv_rejected.setVisibility(View.VISIBLE);
            holder.tv_rejected.setText("已接受");
            holder.tv_reject.setVisibility(View.GONE);
            holder.tv_accept.setVisibility(View.GONE);
        }
        if (newfriendBean.getType().equals("0")) {
            holder.tv_rejected.setVisibility(View.GONE);
            holder.tv_reject.setVisibility(View.VISIBLE);
            holder.tv_accept.setVisibility(View.VISIBLE);
            holder.tv_rejected.setText("等待通过");
            holder.tv_message.setText("申请添加" + newfriendBean.getVisitorname() + "为好友");
        }
        if (newfriendBean.getType().equals("4")) {
            holder.tv_rejected.setVisibility(View.VISIBLE);
            holder.tv_reject.setVisibility(View.GONE);
            holder.tv_accept.setVisibility(View.GONE);
            holder.tv_hint_message.setText(newfriendBean.getHint_message());
            holder.tv_message.setText("申请添加" + newfriendBean.getVisitorname() + "为好友");
            holder.tv_rejected.setText(newfriendBean.getState());

            Glide.with(context).load(newfriendBean.getGiftidimg()).into(holder.iv_gift);
            if(Integer.parseInt(newfriendBean.getGiftsize())>0){
                holder.tv_sizegift.setText("×"+newfriendBean.getGiftsize());
                holder.tv_sizegift.setVisibility(View.VISIBLE);
            }else {
                holder.tv_sizegift.setVisibility(View.INVISIBLE);
            }


        }
        holder.tv_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("handlerresult");
                intent.putExtra(Utils.SENDID, newfriendBean.getSendid());
                intent.putExtra(Utils.ORDERID, newfriendBean.getOrderId());
                intent.putExtra(Utils.CONTENT, newfriendBean.getHint_message());
                intent.putExtra(Utils.NICKNAME, newfriendBean.getUser_name());
                intent.putExtra(Utils.HEAD_URL, newfriendBean.getHead_url());
                intent.putExtra(Utils.TRADEORDERNO, newfriendBean.getTradeOrderNo());
                intent.putExtra(Utils.VISITORID, newfriendBean.getVisitorid());
                intent.putExtra(Utils.TYPE, "2");
                intent.putExtra(Utils.POSITION, position);
                context.sendBroadcast(intent);
            }
        });
        holder.tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("handlerresult");
                intent.putExtra(Utils.SENDID, newfriendBean.getSendid());
                intent.putExtra(Utils.ORDERID, newfriendBean.getOrderId());
                intent.putExtra(Utils.CONTENT, newfriendBean.getHint_message());
                intent.putExtra(Utils.NICKNAME, newfriendBean.getUser_name());
                intent.putExtra(Utils.HEAD_URL, newfriendBean.getHead_url());
                intent.putExtra(Utils.TRADEORDERNO, newfriendBean.getTradeOrderNo());
                intent.putExtra(Utils.VISITORID, newfriendBean.getVisitorid());
                intent.putExtra(Utils.POSITION, position);
                intent.putExtra(Utils.TYPE, "1");
                context.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class NewfriendHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView tv_user_name;
        TextView tv_hint_message;
        TextView tv_rejected;
        TextView tv_reject;
        TextView tv_accept;
        TextView tv_sizegift;
        ImageView iv_gift;
        TextView tv_message;
        RelativeLayout ly_reject_accept;

        public NewfriendHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.head_iv);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            tv_hint_message = itemView.findViewById(R.id.tv_hint_message);
            tv_rejected = itemView.findViewById(R.id.tv_rejected);
            tv_reject = itemView.findViewById(R.id.tv_reject);
            tv_accept = itemView.findViewById(R.id.tv_accept);
            tv_sizegift = itemView.findViewById(R.id.tv_sizegift);
            iv_gift = itemView.findViewById(R.id.iv_gift);
            tv_message = itemView.findViewById(R.id.tv_message);
            ly_reject_accept = itemView.findViewById(R.id.ly_reject_accept);
        }
    }
}
