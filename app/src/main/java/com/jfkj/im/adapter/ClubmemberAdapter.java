package com.jfkj.im.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jfkj.im.Bean.ClubmemberBean;
import com.jfkj.im.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClubmemberAdapter extends BaseQuickAdapter<ClubmemberBean.DataBean.ArrayBean, BaseViewHolder> {


    public ClubmemberAdapter() {
        super(R.layout.item_clubmemberadapter);
    }


    @Override
    protected void convert(BaseViewHolder helper, ClubmemberBean.DataBean.ArrayBean item) {
        TextView tv_nickname = helper.getView(R.id.tv_nickname);
        TextView tv_main = helper.getView(R.id.tv_main);
        if(helper.getAdapterPosition()==0){
            tv_main.setVisibility(View.VISIBLE);
        }else {
            tv_main.setVisibility(View.GONE);
        }
        CircleImageView head_circleimageview = helper.getView(R.id.head_circleimageview);
        ImageView adddelect_iv = helper.getView(R.id.adddelect_iv);
        tv_nickname.setText(item.getNickName());
        Glide.with(helper.itemView.getContext()).load(item.getHead()).into(head_circleimageview);
        if (item.getAdddelete() != null) {
            if (item.getAdddelete().equals("addfriend")) {
                tv_nickname.setVisibility(View.GONE);
                head_circleimageview.setVisibility(View.GONE);
                adddelect_iv.setVisibility(View.VISIBLE);
                adddelect_iv.setBackgroundResource(R.mipmap.club_icon_groupadd_gray);
            }
            if (item.getAdddelete().equals("deletefriend")) {
                tv_nickname.setVisibility(View.GONE);
                head_circleimageview.setVisibility(View.GONE);
                adddelect_iv.setVisibility(View.VISIBLE);
                adddelect_iv.setBackgroundResource(R.mipmap.club_icon_groupdelete_gray);
            }
        }
    }
}
