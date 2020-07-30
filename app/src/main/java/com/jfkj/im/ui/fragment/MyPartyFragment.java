package com.jfkj.im.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.JetPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
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
import com.jfkj.im.adapter.MyPartyAdapter;
import com.jfkj.im.banner.SpacesItemDecoration;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.party.PartyPresenter;
import com.jfkj.im.mvp.party.PartyView;
import com.jfkj.im.ui.party.PartyDetailsActivity;
import com.jfkj.im.utils.ToastUtils;
import com.lzy.okgo.utils.OkLogger;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MyPartyFragment extends BaseFragment<PartyPresenter> implements PartyView, SwipeRecyclerView.LoadMoreListener,SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe_recycler)
    SwipeRecyclerView swipe_recycler;
    @BindView(R.id.tv_no_party)
    TextView tv_no_party;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipe_layout;

    private List<PartyBean> partyBeanList;
    private PartyPresenter partyPresenter;
    private int pageNo = 1;
    private String pageSize = "10";

    private SimpleDateFormat format;
    private SimpleDateFormat twentyFourFormat;
    private Disposable subscribe;

    private MyPartyAdapter myPartyAdapter;


    public static MyPartyFragment getInstance(){
        MyPartyFragment partyFragment = new MyPartyFragment();
        return partyFragment;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        partyBeanList = new ArrayList<>();

        myPartyAdapter = new MyPartyAdapter(getContext(),partyBeanList);
//
//        format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        twentyFourFormat =  new SimpleDateFormat("HH:mm:ss");


        tv_no_party.setText("您还没参加任何聚会~");

        swipe_recycler.useDefaultLoadMore();
        swipe_recycler.loadMoreFinish(false, true);
        swipe_recycler.setLoadMoreListener(this);

        partyPresenter = new PartyPresenter(this);
        partyPresenter.getMyParty(pageNo+"",pageSize);
//        swipe_recycler.addItemDecoration(new SpacesItemDecoration(10));
        swipe_recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        swipe_recycler.setAdapter(myPartyAdapter);

        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_recycler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        partyBeanList.clear();
                        partyPresenter.getMyParty(1+"",pageSize);
                        myPartyAdapter.notifyDataSetChanged();
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
    public void onStop() {
        super.onStop();

        myPartyAdapter.cancelAllTimers();

    }



    @Override
    public void PartySuccess(BaseBeans<PartyBean> partyBeanBaseBeans) {


        if(partyBeanBaseBeans.getData().size() > 0){
            swipe_recycler.setVisibility(View.VISIBLE);
            swipe_recycler.loadMoreFinish(false, true);
            if(pageNo == 1 ){
                partyBeanList = partyBeanBaseBeans.getData();
            }else{
                partyBeanList.addAll(partyBeanBaseBeans.getData());
            }
            swipe_layout.setVisibility(View.VISIBLE);
            myPartyAdapter.setPartyBeanList(partyBeanList);
        }else{
            if(pageNo == 1){
                swipe_layout.setVisibility(View.GONE);
                tv_no_party.setVisibility(View.VISIBLE);
                 swipe_recycler.setVisibility(View.GONE);
            }
            swipe_recycler.loadMoreFinish(false, false);
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
                swipe_recycler.loadMoreFinish(false,true);
                pageNo++;
                partyPresenter.getMyParty(pageNo + "",pageSize);
                Log.e("msg","|///////////onLoadMore////////////"+pageNo);
            }
        },1000);
    }

    @Override
    public void onRefresh() {

    }
}
