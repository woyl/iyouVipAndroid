package com.jfkj.im.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jfkj.im.Bean.MineCharacterttestBean;
import com.jfkj.im.R;
import com.jfkj.im.entity.ResultRecordBean;

import java.util.List;

public class ResultRecordAdapter extends RecyclerView.Adapter<ResultRecordAdapter.MineCharacterttestaolder> {

    private ResultRecordBean  bean;
    Context context;
    LayoutInflater layoutInflater;

    public ResultRecordAdapter(Context context,ResultRecordBean bean) {
        this.context = context;
        this.bean = bean;
        layoutInflater = LayoutInflater.from(context);
    }


    public void setDate(ResultRecordBean bean){
        this.bean = bean;

    }


    @NonNull
    @Override
    public MineCharacterttestaolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_result_record, parent, false);
        return new MineCharacterttestaolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MineCharacterttestaolder holder, int position) {

        ResultRecordBean.DataBean dataBean = bean.getData().get(position);

        holder.tv_cashdate.setText(dataBean.getCashdate());

            holder.tv_money.setText("¥" +dataBean.getMoney());

            if("0".equals(dataBean.getSuccess())){
                holder.tv_success.setText("未处理");
            }else if("1".equals(dataBean.getSuccess())){
                holder.tv_success.setText("成功");
                holder.tv_success.setTextColor(ContextCompat.getColor(context,R.color.white));
            }else{
                holder.tv_success.setText("失败");
                holder.tv_success.setTextColor(ContextCompat.getColor(context,R.color.cFF2B66));
            }

            holder.tv_cashid.setText("结算编号：" +dataBean.getCashid());
    }

    @Override
    public int getItemCount() {
        if(bean == null) return 0;
        return bean.getData() != null ? bean.getData().size() : 0;
    }

    class MineCharacterttestaolder extends RecyclerView.ViewHolder {

        private final TextView tv_cashid;
        private final TextView tv_success;
        private final TextView tv_money;
        private final TextView tv_cashdate;

        public MineCharacterttestaolder(@NonNull View itemView) {
            super(itemView);

            tv_cashid = itemView.findViewById(R.id.tv_cashid);
            tv_success = itemView.findViewById(R.id.tv_success);
            tv_money = itemView.findViewById(R.id.tv_money);
            tv_cashdate = itemView.findViewById(R.id.tv_cashdate);
        }
    }
}
