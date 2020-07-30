package com.jfkj.im.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.ToastUtil;
import com.jfkj.im.adapter.ChargAdapter;
import com.jfkj.im.adapter.ChargeRecordAdapter;
import com.jfkj.im.adapter.GiftListAdapter;
import com.jfkj.im.entity.ChargeRecordBean;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.mine.ChargeRecordPresenter;
import com.jfkj.im.mvp.mine.ChargeRecordView;
import com.jfkj.im.utils.Utils;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.List;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.VISIBLE;

/**
 * <pre>
 * Description: 充值记录
 * @author :   ys
 * @date :         2019/12/5
 * </pre>
 */
public class ChargeRecordActivity extends BaseActivity<ChargeRecordPresenter> implements ChargeRecordView, SwipeRefreshLayout.OnRefreshListener, SwipeRecyclerView.LoadMoreListener {
    @BindView(R.id.iv_title_back)
    AppCompatImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    AppCompatTextView mTvTitle;
    @BindView(R.id.tv_title_right)
    AppCompatTextView mTvTitleRight;
    @BindView(R.id.iv_title_right)
    AppCompatImageView mIvTitleRight;

    @BindView(R.id.recycler)
    SwipeRecyclerView mRecycler;
//    @BindView(R.id.swipe_refresh)
//    SwipeRefreshLayout mSwipeRefresh;

    @BindView(R.id.ll_layout)
    LinearLayout linearLayout;


    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.netWork()) {
            mvpPresenter.rechargeRecord(pageSize+ "",pageNo+"");
            linearLayout.setVisibility(View.GONE);
        }else{
            toastShow(R.string.nonetwork);
            linearLayout.setVisibility(VISIBLE);
        }
    }


    int pageSize= 20;

    int pageNo = 1;
    private ChargeRecordBean bean;
    private boolean isRefresh ;
    private ChargAdapter mAdapter;


    @OnClick({R.id.tv_chong})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_chong:
                if (Utils.netWork()) {
                    linearLayout.setVisibility(View.GONE);
                }else{
                    toastShow(R.string.nonetwork);
                    linearLayout.setVisibility(VISIBLE);
                }
                break;
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_charge_record;
    }

    @Override
    public ChargeRecordPresenter createPresenter() {
        return new ChargeRecordPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        mTvTitle.setText("充值记录");




      //  mvpPresenter.rechargeRecord(pageSize+ "",pageNo+"");
    //   mSwipeRefresh.setRefreshing(true);
     //   mSwipeRefresh.setOnRefreshListener(this);
        mRecycler.useDefaultLoadMore();
        mRecycler.setLoadMoreListener(this);

        mAdapter = new ChargAdapter(this,bean);

        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(false);

    }

    @OnClick(R.id.iv_title_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void showRecord( ChargeRecordBean bean) {
        ToastUtils.showShort(bean.getMessage());

        if(bean.getCode().equals("200")){

                if (bean.getData().size() > 0){
                    mRecycler.loadMoreFinish(false,true);
                    if(this.bean!=null){
                        this.bean.getData().addAll(bean.getData());
                    }else{
                        this.bean = bean;
                    }

                    mAdapter.setData(this.bean);
                    mAdapter.notifyDataSetChanged();
                }else {
                    mRecycler.loadMoreFinish(true,false);
                }
            }


    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
//        if (mSwipeRefresh.isRefreshing()) {
//            mSwipeRefresh.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mSwipeRefresh.setRefreshing(false);
//                }
//            }, 1000);
//        }
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        pageNo = 1;
        mvpPresenter.rechargeRecord(pageSize+ "",pageNo+"");
    }

    @Override
    public void onLoadMore() {
        pageNo++;
        isRefresh = true;
        mvpPresenter.rechargeRecord(pageSize+ "",pageNo+"");
    }


}
