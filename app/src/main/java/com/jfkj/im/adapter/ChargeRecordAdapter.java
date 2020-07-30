package com.jfkj.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jfkj.im.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/5
 * </pre>
 */
public class ChargeRecordAdapter extends RecyclerView.Adapter<ChargeRecordAdapter.ViewHolder> {

    private Context mContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_charge_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_charge)
        TextView mTvCharge;
        @BindView(R.id.tv_charge_number)
        TextView mTvChargeNumber;
        @BindView(R.id.tv_diamond_number)
        TextView mTvDiamondNumber;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_total_diamond)
        TextView mTvTotalDiamond;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
