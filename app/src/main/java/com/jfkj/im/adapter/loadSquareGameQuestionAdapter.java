package com.jfkj.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.jfkj.im.Bean.loadSquareGameQuestionBean;
import com.jfkj.im.R;

import java.util.List;

public class loadSquareGameQuestionAdapter extends RecyclerView.Adapter<loadSquareGameQuestionAdapter.MineCharacterttestaolder> {
    List<loadSquareGameQuestionBean.DataBean.ArrayBean> loadSquareGameQuestionBeans;
    LayoutInflater layoutInflater;
    Context context;

    public loadSquareGameQuestionAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setLoadSquareGameQuestionBeans(List<loadSquareGameQuestionBean.DataBean.ArrayBean> loadSquareGameQuestionBeans) {
        this.loadSquareGameQuestionBeans = loadSquareGameQuestionBeans;
    }

    @NonNull
    @Override
    public MineCharacterttestaolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.item_minecharacter,parent,false);
        return new MineCharacterttestaolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MineCharacterttestaolder holder, int position) {
        loadSquareGameQuestionBean.DataBean.ArrayBean arrayBean = loadSquareGameQuestionBeans.get(position);
        holder.page_tv.setText(position+1+"");
        holder.title_tv.setText(arrayBean.getSubject());
      //   holder.tv_answer.setText(arrayBean.getAnswer().getContent());
    }

    @Override
    public int getItemCount() {
        return loadSquareGameQuestionBeans!=null?loadSquareGameQuestionBeans.size():0;
    }

    class MineCharacterttestaolder extends RecyclerView.ViewHolder {

        AppCompatTextView page_tv, title_tv, tv_answer;

        public MineCharacterttestaolder(@NonNull View itemView) {
            super(itemView);
            tv_answer = itemView.findViewById(R.id.tv_answer);
            title_tv = itemView.findViewById(R.id.title_tv);
            page_tv = itemView.findViewById(R.id.page_tv);
        }
    }
}
