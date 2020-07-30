package com.jfkj.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.jfkj.im.R;
import com.jfkj.im.entity.InvteFriendBean;

import java.util.List;

public class InviteFriendAdapter extends RecyclerView.Adapter<InviteFriendAdapter.InviteFriendHolder> {
    InvteFriendBean list;
    Context context;
    LayoutInflater layoutInflater;

    public InviteFriendAdapter(Context context,InvteFriendBean list) {
        this.list = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setList(InvteFriendBean list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InviteFriendHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InviteFriendHolder(layoutInflater.inflate(R.layout.item_invitefriend, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InviteFriendHolder holder, int position) {
        holder.textView_number.setText(list.getData().get(position).getMobileNo());
        holder.textView_time.setText(list.getData().get(position).getAdddate());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.getData().size() : 0;
    }

    class InviteFriendHolder extends RecyclerView.ViewHolder {
        AppCompatTextView textView_number, textView_time;

        public InviteFriendHolder(@NonNull View itemView) {
            super(itemView);
            textView_number=itemView.findViewById(R.id.tv_phone);
            textView_time=itemView.findViewById(R.id.tv_time);

        }
    }
}
