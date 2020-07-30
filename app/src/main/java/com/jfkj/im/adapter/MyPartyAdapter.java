package com.jfkj.im.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.internal.$Gson$Preconditions;
import com.jfkj.im.Bean.PartyBean;
import com.jfkj.im.R;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.ui.party.PartyDetailsActivity;
import com.lzy.okgo.utils.OkLogger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MyPartyAdapter extends RecyclerView.Adapter<MyPartyAdapter.ViewHolder> {

    Context context;

    private List<PartyBean> partyBeanList;
    private SimpleDateFormat format;
    private SimpleDateFormat twentyFourFormat;
    private long time = 999;
    private List<Disposable> disposables;

    long presentTime;
    private final long time1;

    public MyPartyAdapter(Context context, List<PartyBean> partyBeanList) {
        this.context = context;
        this.partyBeanList = partyBeanList;
        format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        twentyFourFormat =  new SimpleDateFormat("HH:mm:ss");


        Date parse1 = null;
        try {
            parse1 = format.parse("2020-10-10 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        time1 = parse1.getTime();

        presentTime = new Date().getTime(); //当前时间戳
        disposables = new ArrayList<>();
    }

    public void setPartyBeanList(List<PartyBean> partyBeanList) {
        this.partyBeanList = partyBeanList;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_party, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {    


        try {

            PartyBean partyBean = partyBeanList.get(position);

            //聚会未结束
            Glide.with(context).load(partyBean.getPagePhoto()).error(R.mipmap.party_bg).into(holder.ivBrg);



            holder.tv_title.setText(partyBean.getTitle());
            holder. tv_loaction.setText(partyBean.getPlace());
            holder. tv_time.setText(partyBean.getCadddate()+"/限定"+partyBean.getMaxNumber()+"人");

            holder.fl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PartyDetailsActivity.class);
                    intent.putExtra("partyId",partyBean.getPartyId());
                    context.startActivity(intent);
                }
            });
            /**
             * 状态 0进行中 1已结束 2已取消 3禁用
             * */
            switch (partyBean.getIstate()){
                case "0":
                    holder.tv_state.setText("进行中");
                    break;
                case "1":
                    holder. tv_state.setText("已结束");
                    break;
                case "2":
                    holder.  tv_state.setText("已取消");

                    break;
                case "3":
                    holder.   tv_state.setText("禁用");
                    break;
            }


            //已结束 已取消 背景灰色
            if(partyBean.getIstate().equals("1") || partyBean.getIstate().equals("2")){
                holder.view.setBackgroundResource(R.color.CB3000000);
            }else{
                holder.view.setBackgroundResource(R.color.C33000000);
            }

            //1:已结束 && 显示小红点  && 聚会是自己发布的
            if(partyBean.getIstate().equals("1")  &&  partyBean.getRedType() .equals("1")  && partyBean.getUserId().equals(UserInfoManger.getUserId()) ){


                try {
                    //聚会结束时间
                    holder.cadddate = partyBean.getCadddate();
                    Date parse = format.parse(holder.cadddate);

                    long endTime = parse.getTime();  //结束时间戳

                    long  difference = presentTime- endTime;  //两个时间相差

                    long  twentyFourLong= 24*60*60*1000;   //24小时时间戳



                    //结束 + 24 - presentTime- endTime
                    holder.countdownLong = endTime + twentyFourLong -presentTime + time1;




                    //聚会已结束时长小于24小时
                    if(difference < twentyFourLong && difference >0){
                        //显示倒计时
                        holder. tv_countdown.setVisibility(View.VISIBLE);

                        holder.subscribe = Observable.interval(1, 1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                            @SuppressLint("CheckResult")
                            @Override
                            public void accept(Long aLong) throws Exception {

                                holder.countdownLong = holder.countdownLong  - 1000;

                                OkLogger.e("------" + holder.countdownLong);

                                String timeDate = twentyFourFormat.format(new Date(holder.countdownLong));//OkLogger.e();

                                holder.tv_countdown.setText("点评倒计时" + timeDate);


                            }
                        });
                        if(disposables!=null){
                            disposables.add( holder.subscribe);
                        }
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else{
                holder.tv_countdown.setVisibility(View.GONE);
            }

        }catch (Exception e){
            OkLogger.e(e.toString());
        }

    }


//    @Override
//    public void onViewRecycled(@NonNull ViewHolder holder) {
//        super.onViewRecycled(holder);
//
//        if(holder.subscribe!=null){
//            holder.subscribe.dispose();
//        }
//    }



    public void cancelAllTimers() {
        if (disposables == null) {
            return;
        }
        for (int i = 0,length = disposables.size(); i < length; i++) {
            try {
                if(disposables.get(i) != null){

                    disposables.get(i).dispose();
                    disposables = null;
                }
            }catch (Exception e){

            }

        }
    }


    @Override
    public int getItemCount() {
        return partyBeanList == null ? 0 : partyBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public final TextView tv_title;
        public final TextView tv_loaction;
        public final TextView tv_time;
        public final TextView tv_state;
        public final TextView tv_countdown;
        public final ImageView ivBrg;
        public final View view;
        private final FrameLayout fl;
        private Disposable subscribe;
        private  String cadddate;
        private long countdownLong;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_loaction = itemView.findViewById(R.id.tv_loaction);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_state = itemView.findViewById(R.id.tv_state);
            tv_countdown = itemView.findViewById(R.id.tv_countdown);
            ivBrg = itemView.findViewById(R.id.iv_brg);
            view = itemView.findViewById(R.id.view);
            fl = itemView.findViewById(R.id.fl);
        }
    }
}
