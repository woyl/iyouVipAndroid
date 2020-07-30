package com.jfkj.im.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.jfkj.im.Bean.TestQuestionBean;
import com.jfkj.im.R;

import java.util.List;

import retrofit2.http.PUT;

public class CharacterttestAdapter extends RecyclerView.Adapter<CharacterttestAdapter.CharacterttestHolder> {
    LayoutInflater layoutInflater;
    Context context;
    List<TestQuestionBean.DataBean.ArrayBean.AnswerBean> arrayBeans;
    public CharacterttestAdapter(Context context){
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
    }

    public void setArrayBeans(List<TestQuestionBean.DataBean.ArrayBean.AnswerBean> arrayBeans) {
        this.arrayBeans = arrayBeans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CharacterttestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.item_characterttest,parent,false);
        return new CharacterttestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharacterttestHolder holder, int position) {
        TestQuestionBean.DataBean.ArrayBean.AnswerBean answerBean = arrayBeans.get(position);
        holder.tv_answer.setText(answerBean.getContent());
             if(answerBean.isIsselect()){
                 holder.tv_answer.setTextColor(Color.parseColor("#ffffff"));
                 holder.tv_answer.setBackgroundResource(R.color.color_5E58B0);
             }else {
                 holder.tv_answer.setBackgroundResource(R.color.color_ffffff);
                 holder.tv_answer.setTextColor(Color.parseColor("#5E58B0"));
             }

    }

    @Override
    public int getItemCount() {
        return arrayBeans!=null?arrayBeans.size():0;
    }

    class CharacterttestHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tv_answer;

        public CharacterttestHolder(@NonNull View itemView) {
            super(itemView);
            tv_answer = itemView.findViewById(R.id.tv_answer);
        }
    }

}
