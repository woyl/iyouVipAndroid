package com.jfkj.im.ui.mine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.adapter.ResultRecordAdapter;
import com.jfkj.im.entity.ResultRecordBean;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.mine.ResultRecordPresenter;
import com.jfkj.im.mvp.mine.ResultRecordView;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

public class ResultRecordActivity extends BaseActivity<ResultRecordPresenter> implements ResultRecordView, SwipeRefreshLayout.OnRefreshListener, SwipeRecyclerView.LoadMoreListener {

   private  int pageSize =20 ;

    private  int pageNo = 1;

    private ResultRecordPresenter presenter;

    @BindView(R.id.recycler)
    SwipeRecyclerView mRecycler;

    @BindView(R.id.tv_money)
    TextView tv_money;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private ResultRecordAdapter adapter;



    private boolean isRefresh = true;

    private ResultRecordBean bean  ;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);

        presenter = new ResultRecordPresenter(this);


        mRecycler.setLayoutManager(new LinearLayoutManager(this));

        presenter.settlementRecord(pageNo,pageSize);
        mRecycler.useDefaultLoadMore();
        mRecycler.setLoadMoreListener(this);

        mRecycler.setHasFixedSize(false);
        adapter = new ResultRecordAdapter(this,bean);
        mRecycler.setAdapter(adapter);
        tv_title.setText("结算记录");
        String resultMoney = getIntent().getStringExtra("ResultMoney");
        tv_money.setText(resultMoney);
    }


    @OnClick({R.id.iv_title_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_title_back:
                finish();
                break;

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_result_record;
    }



    @Override
    public ResultRecordPresenter createPresenter() {
        return presenter;
    }

    @Override
    public void showMentRecord(ResultRecordBean bean) {
            if (bean.getData().size() > 0){
                mRecycler.loadMoreFinish(false,true);

                if(this.bean == null){
                    this.bean = bean;
                }else{
                    this.bean.getData().addAll(bean.getData());
                }

                adapter.setDate(this.bean);
                adapter.notifyDataSetChanged();
            }else {
                mRecycler.loadMoreFinish(true,false);
            }

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        pageNo = 1;
        presenter.settlementRecord(pageNo,pageSize);
    }

    @Override
    public void onLoadMore() {
        pageNo++;
        isRefresh = true;
        presenter.settlementRecord(pageNo,pageSize);
    }
}
