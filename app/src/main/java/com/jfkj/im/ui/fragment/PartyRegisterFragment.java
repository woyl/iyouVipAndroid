package com.jfkj.im.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.jfkj.im.Bean.AllMyPartysRedBean;
import com.jfkj.im.Bean.BaseBeans;
import com.jfkj.im.Bean.PartyBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.CommonRecyclerAdapter;
import com.jfkj.im.adapter.CommonRecyclerHolder;
import com.jfkj.im.banner.SpacesItemDecoration;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.party.PartyPresenter;
import com.jfkj.im.mvp.party.PartyView;
import com.jfkj.im.ui.party.PartyDetailsActivity;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PartyRegisterFragment  extends BaseFragment<PartyPresenter> implements PartyView, SwipeRecyclerView.LoadMoreListener,SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipe_recycler)
    SwipeRecyclerView swipe_recycler;
    @BindView(R.id.tv_no_party)
    TextView tv_no_party;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;

    private CommonRecyclerAdapter<PartyBean> commonRecyclerAdapter;
    private List<PartyBean> partyBeanList;
    private PartyPresenter partyPresenter;
    private int pageNo = 1;
    private String pageSize = "10";

    public static PartyRegisterFragment getInstance(){
        PartyRegisterFragment partyRegisterFragment = new PartyRegisterFragment();
        return partyRegisterFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_no_party.setText("您还没报名任何聚会~");
        partyBeanList = new ArrayList<>();
        partyPresenter = new PartyPresenter(this);
        partyPresenter.getPartyRegister(pageNo + "",pageSize);
//        swipe_recycler.addItemDecoration(new SpacesItemDecoration(10));
        swipe_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        swipe_recycler.useDefaultLoadMore();
        swipe_recycler.loadMoreFinish(false, true);
        swipe_recycler.setLoadMoreListener(this);
//        swipe_recycler.setAutoLoadMore(false);

        commonRecyclerAdapter = new CommonRecyclerAdapter<PartyBean>(getContext(),partyBeanList,R.layout.item_party) {
            @SuppressLint("SetTextI18n")
            @Override
            public void convert(CommonRecyclerHolder holder, PartyBean partyBean, int position) {
                TextView tv_title = holder.getView(R.id.tv_title);
                TextView tv_loaction = holder.getView(R.id.tv_loaction);
                TextView tv_time = holder.getView(R.id.tv_time);
                TextView tv_state = holder.getView(R.id.tv_state);
                View view = holder.getView(R.id.view);

                //已结束 已取消 背景灰色
                if(partyBean.getIstate().equals("1") || partyBean.getIstate().equals("2")){
                    view.setBackgroundResource(R.color.CB3000000);
                }else{
                    view.setBackgroundResource(R.color.C33000000);
                }

                ImageView ivBrg = holder.getView(R.id.iv_brg);

                //聚会未结束
                Glide.with(getActivity()).load(partyBean.getPagePhoto()).error(R.mipmap.party_bg).into(ivBrg);

                holder.getView(R.id.fl).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PartyDetailsActivity.class);
                        intent.putExtra("partyId",partyBean.getPartyId());
                        startActivity(intent);
                    }
                });


                tv_title.setText(partyBean.getTitle());
                tv_loaction.setText(partyBean.getPlace());
                tv_time.setText(partyBean.getCadddate()+"/限定"+partyBean.getMaxNumber()+"人");
                /**
                 * 状态 0进行中 1已结束 2已取消 3禁用
                 * */
                switch (partyBean.getIstate()){
                    case "0":
                        tv_state.setText("进行中");
                        break;
                    case "1":
                        tv_state.setText("已结束");
                        break;
                    case "2":
                        tv_state.setText("已取消");
                        break;
                    case "3":
                        tv_state.setText("禁用");
                        break;
                    default:break;
                }
            }
        };
        swipe_recycler.setAdapter(commonRecyclerAdapter);

        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_recycler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        partyBeanList.clear();
                        partyPresenter.getPartyRegister(1+"",pageSize);
                        commonRecyclerAdapter.notifyDataSetChanged();
                        //模拟网络请求需要3000毫秒，请求完成，设置setRefreshing 为false
                        swipe_layout.setRefreshing(false);
                        swipe_recycler.setVisibility(View.VISIBLE);
                        tv_no_party.setVisibility(View.GONE);
                        swipe_recycler.loadMoreFinish(false, true);
                    }
                },1000);

            }
        });
    }

    @Override
    protected PartyPresenter createPresenter() {
        return partyPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_party_fragment;
    }

    @Override
    public void PartySuccess(BaseBeans<PartyBean> partyBeanBaseBeans) {
        if (partyBeanBaseBeans.getData() != null && partyBeanBaseBeans.getData().size() > 0){
            partyBeanList.addAll(partyBeanBaseBeans.getData());
            commonRecyclerAdapter.notifyDataSetChanged();
            swipe_recycler.setVisibility(View.VISIBLE);
            tv_no_party.setVisibility(View.GONE);
            swipe_recycler.loadMoreFinish(false, true);
        } else if ( partyBeanList.size() > 0){
            swipe_recycler.loadMoreFinish(true, false);
            swipe_recycler.setVisibility(View.VISIBLE);
            tv_no_party.setVisibility(View.GONE);
        }else {
            swipe_recycler.loadMoreFinish(true, false);
            swipe_recycler.setVisibility(View.GONE);
            tv_no_party.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void PartyFails(String error) {
        toastShow(error);
    }

    @Override
    public void myPartysRedTypeSuccess(AllMyPartysRedBean allMyPartysRedBean) {

    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void onLoadMore() {

        swipe_recycler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (swipe_recycler != null) {
                    swipe_recycler.loadMoreFinish(false,true);
                    pageNo++;
                    partyPresenter.getPartyRegister(pageNo + "",pageSize);
                }

            }
        },1000);
    }

    @Override
    public void onRefresh() {
        Log.e("msg","|///////////onRefresh////////////");
    }
}
