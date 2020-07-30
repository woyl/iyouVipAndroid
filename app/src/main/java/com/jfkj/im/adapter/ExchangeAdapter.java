package com.jfkj.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jfkj.im.Bean.ExChangeBean;
import com.jfkj.im.R;
import com.jfkj.im.listener.onItemPositionClickListener;
import com.jfkj.im.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/23
 * </pre>
 */
public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.ViewHolder> {

    private List<ExChangeBean.DataBean> mList = new ArrayList<>();
    private Context mContext;
    private onItemPositionClickListener mListener;

    public ExchangeAdapter(Context context, List<ExChangeBean.DataBean> list,onItemPositionClickListener listener) {
        this.mContext = context;
        this.mList = list;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_excharge_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < mList.size()) {
            ExChangeBean.DataBean chargeBean = mList.get(position);
            holder.mTvDiamond.setText(chargeBean.getImoney() + "");
            holder.mTvMoney.setText(chargeBean.getIexchangemoney() + "");
            holder.mTvDiamond.setVisibility(View.VISIBLE);
            holder.mTvMoney.setVisibility(View.VISIBLE);
            holder.mTvService.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onPositionClick(position);
                }
            });
        } else {
            holder.mTvDiamond.setVisibility(View.GONE);
            holder.mTvMoney.setVisibility(View.GONE);
            holder.mTvService.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showShortToast("联系客服");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvDiamond;
        TextView mTvMoney;
        TextView mTvService;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvDiamond = itemView.findViewById(R.id.tv_diamond);
            mTvMoney = itemView.findViewById(R.id.tv_money);
            mTvService = itemView.findViewById(R.id.tv_service);
        }
    }

}
