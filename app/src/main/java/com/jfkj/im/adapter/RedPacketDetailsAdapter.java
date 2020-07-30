package com.jfkj.im.adapter;

import android.annotation.SuppressLint;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jfkj.im.Bean.loadRedPacketReceiveListBean;
import com.jfkj.im.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RedPacketDetailsAdapter extends BaseQuickAdapter<loadRedPacketReceiveListBean.DataBean.ArrayBean, BaseViewHolder> {
    public RedPacketDetailsAdapter(@Nullable List<loadRedPacketReceiveListBean.DataBean.ArrayBean> data) {
        super(R.layout.item_redpacketdetails, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, loadRedPacketReceiveListBean.DataBean.ArrayBean item) {
        CircleImageView head_iv = helper.getView(R.id.head_iv);
        TextView tv_name = helper.getView(R.id.tv_name);
        TextView tv_time = helper.getView(R.id.tv_time);
        TextView tv_number = helper.getView(R.id.tv_number);
        tv_name.setText(item.getUserNickName());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
        try {
            Date date = format1.parse(item.getAddDate());
            tv_time.setText(format2.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tv_number.setText(item.getMoney() + "");
        Glide.with(helper.itemView.getContext()).load(item.getUserHead()).into(head_iv);
    }
}
