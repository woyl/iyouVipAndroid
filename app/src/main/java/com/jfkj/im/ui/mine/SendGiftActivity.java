package com.jfkj.im.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jfkj.im.R;
import com.jfkj.im.adapter.GiftListAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.MyGiftBean;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.mine.MyGiftPresenter;
import com.jfkj.im.mvp.mine.MyGiftView;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 * Description:  累计送出的礼物
 * @author :   ys
 * @date :         2019/12/26
 * </pre>
 */
public class SendGiftActivity extends BaseActivity<MyGiftPresenter> implements MyGiftView, SwipeRefreshLayout.OnRefreshListener, SwipeRecyclerView.LoadMoreListener {
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

    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.tv_gift_num)
    TextView mTvGiftNum;
    @BindView(R.id.tv_no_data)
    TextView mTvNoData;
    private int pageSize = 10;
    private int pageNo = 1;
    private String type = "1";
    private String giftNum;
    private GiftListAdapter mAdapter;
    private List<MyGiftBean.DataBean> mBeanList = new ArrayList<>();
    private boolean isRefresh = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_gift;
    }

    @Override
    public MyGiftPresenter createPresenter() {
        return new MyGiftPresenter(this);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setAndroidNativeLightStatusBar(this,false);
        mTvTitle.setText("送出的礼物");
        mTvType.setText("累计送出礼物：");
        mTvGiftNum.setText(UserInfoManger.getUserCost());
        mvpPresenter.getGiftList(pageSize +"",pageNo + "",type);

        mRecycler.useDefaultLoadMore();
        mRecycler.setLoadMoreListener(this);

        mAdapter = new GiftListAdapter(this,mBeanList);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(false);

    }

    @Override
    public void showGift(MyGiftBean bean) {
            if (bean.getData().size() > 0){
                mRecycler.loadMoreFinish(false,true);
                mBeanList.addAll(bean.getData());
                mAdapter.notifyDataSetChanged();
            }else {
                mRecycler.loadMoreFinish(true,false);
            }

    }

    @Override
    public void details() {
        mRecycler.loadMoreFinish(true,false);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick(R.id.iv_title_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        pageNo = 1;
        mvpPresenter.getGiftList(pageSize + "", pageNo + "", type);
    }

    @Override
    public void onLoadMore() {
        pageNo++;
        isRefresh = true;
        mvpPresenter.getGiftList(pageSize + "", pageNo + "", type);
    }
}
