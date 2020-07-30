package com.jfkj.im.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.jfkj.im.Bean.MineCharacterttestBean;
import com.jfkj.im.R;

import java.util.List;

public class PartyAddressAdapter extends RecyclerView.Adapter<PartyAddressAdapter.ViewHolder> {

    List<PoiInfo> poiInfos ;

    Context context;

    OnItemClick onItemClick;

    public PartyAddressAdapter(List<PoiInfo> poiInfos, Context context,OnItemClick onItemClick) {
        this.poiInfos = poiInfos;
        this.context = context;
        this.onItemClick = onItemClick;
    }


    public void setData(List<PoiInfo> poiInfos){
        this.poiInfos =poiInfos;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_party_address, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tvAddress.setText(poiInfos.get(position).getAddress());
            holder.tvName.setText(poiInfos.get(position).getName());

            holder.ll_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.selectAddress(position,poiInfos.get(position).getName());
                }
            });
    }

    @Override
    public int getItemCount() {
        return  poiInfos==null?0:poiInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final TextView tvAddress;
        private final LinearLayout ll_address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            ll_address = itemView.findViewById(R.id.ll_address);
        }
    }


    public interface OnItemClick{
        void selectAddress(int position,String address);
    }
}
