package com.jfkj.im.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.jfkj.im.R;
import com.jfkj.im.entity.ChargeRecordBean;
import com.jfkj.im.entity.MyGiftBean;
import com.jfkj.im.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/27
 * </pre>
 */
public class ChargAdapter extends RecyclerView.Adapter<ChargAdapter.ViewHolder> {
    private Context mContext;



    private ChargeRecordBean bean;



    public void setData(ChargeRecordBean bean){
      this.bean = bean;

    }

    public ChargAdapter(Context context ,ChargeRecordBean bean) {
        this.mContext = context;
        this.bean=  bean;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_charge_list, parent, false);
        return new ViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_cz_money.setText("充值¥" + StringUtil.subZeroAndDot(bean.getData().get(position).getRmb()));
        holder.tv_date_time.setText(bean.getData().get(position).getAdddate());
        holder.tv_money.setText("+" + StringUtil.subZeroAndDot(bean.getData().get(position).getMoney()));
        holder.tv_balance.setText("余额：" +  bean.getData().get(position).getBalance());

    }

    @Override
    public int getItemCount() {
        if(bean == null){
            return 0;
        }
        return bean.getData()!=null?bean.getData().size():0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        private final TextView tv_cz_money;
        private final TextView tv_date_time;
        private final TextView tv_money;
        private final TextView tv_balance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_cz_money = itemView.findViewById(R.id.tv_cz_money);
            tv_date_time = itemView.findViewById(R.id.tv_date_time);
            tv_money = itemView.findViewById(R.id.tv_money);
            tv_balance = itemView.findViewById(R.id.tv_balance);
        }
    }
}
