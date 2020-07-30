package com.jfkj.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jfkj.im.Bean.loadUserAnswerList;
import com.jfkj.im.R;
import com.lzy.okgo.OkGo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Characteruseradapter extends RecyclerView.Adapter<Characteruseradapter.CharacteruserHolder> {
    List<loadUserAnswerList.DataBean.ArrayBean> list;
    Context context;
    LayoutInflater layoutInflater;

    public Characteruseradapter(Context context){
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
    }

    public void setList(List<loadUserAnswerList.DataBean.ArrayBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CharacteruserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CharacteruserHolder(layoutInflater.inflate(R.layout.item_characteruser,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CharacteruserHolder holder, int position) {
        loadUserAnswerList.DataBean.ArrayBean arrayBean = list.get(position);
        holder.number_tv.setText(position+1+"");
        Glide.with(context).load(arrayBean.getHead()).into(holder.head_iv);
        holder.percent_tv.setText("匹配度:"+arrayBean.getPercentage()+"%");
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    class CharacteruserHolder extends RecyclerView.ViewHolder{
        AppCompatTextView number_tv,name_tv,percent_tv;
        CircleImageView head_iv;
        public CharacteruserHolder(@NonNull View itemView) {
            super(itemView);
            number_tv=itemView.findViewById(R.id.number_tv);
            name_tv=itemView.findViewById(R.id.name_tv);
            percent_tv=itemView.findViewById(R.id.percent_tv);
            head_iv=itemView.findViewById(R.id.head_iv);
        }
    }
}
