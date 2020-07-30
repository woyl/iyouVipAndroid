package com.jfkj.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.jfkj.im.Bean.MatchingdegreeBean;
import com.jfkj.im.Bean.loadSquareGameQuestionBean;
import com.jfkj.im.R;

import java.util.List;

public class MatchingdegreAdapter extends RecyclerView.Adapter<MatchingdegreAdapter.MatchingdegreHolder> {
    List<MatchingdegreeBean.DataBean.AnswerBean> list;
    Context context;
    LayoutInflater layoutInflater;
    List<loadSquareGameQuestionBean.DataBean.ArrayBean>  arrayBeans;
   public MatchingdegreAdapter(Context context){
       this.context=context;
       layoutInflater=LayoutInflater.from(context);
   }

    public void setList(List<MatchingdegreeBean.DataBean.AnswerBean> list, List<loadSquareGameQuestionBean.DataBean.ArrayBean> gameQuestionBean) {
        this.list = list;
        this.arrayBeans=gameQuestionBean;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MatchingdegreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view=layoutInflater.inflate(R.layout.item_matchingdegree,parent,false);
        return new MatchingdegreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchingdegreHolder holder, int position) {
        MatchingdegreeBean.DataBean.AnswerBean answerBean = list.get(position);
        loadSquareGameQuestionBean.DataBean.ArrayBean arrayBean = arrayBeans.get(position);
        holder.page_tv.setText(position+1+"/13");
        holder.title_tv.setText(arrayBean.getSubject());
        holder.tv_answer1.setText(arrayBean.getAnswer().getContent());
        holder.tv_answer2.setText(answerBean.getContent());
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    class MatchingdegreHolder extends RecyclerView.ViewHolder {
        AppCompatTextView page_tv, title_tv, tv_answer1, tv_answer2;

        public MatchingdegreHolder(@NonNull View itemView) {
            super(itemView);
            page_tv=itemView.findViewById(R.id.page_tv);
            title_tv=itemView.findViewById(R.id.title_tv);
            tv_answer1=itemView.findViewById(R.id.tv_answer1);
            tv_answer2=itemView.findViewById(R.id.tv_answer2);

        }
    }
}
