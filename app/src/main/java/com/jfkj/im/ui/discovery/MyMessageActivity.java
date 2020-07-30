package com.jfkj.im.ui.discovery;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jfkj.im.App;
import com.jfkj.im.Bean.MyDynamicMessage;
import com.jfkj.im.R;
import com.jfkj.im.adapter.MyMessageAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.discovery.MyMessagePresenter;
import com.jfkj.im.mvp.discovery.MyMessageView;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.SignUtils;
import com.jfkj.im.utils.SoftKeyBoardListener;
import com.jfkj.im.utils.Utils;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 * Description: 我的消息页
 * @author :   ys
 * @date :         2019/12/2
 * </pre>
 */
public class MyMessageActivity extends BaseActivity<MyMessagePresenter> implements MyMessageView, SwipeRefreshLayout.OnRefreshListener, SwipeRecyclerView.LoadMoreListener {
    @BindView(R.id.iv_title_back)
    AppCompatImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    AppCompatTextView mTvTitle;
    @BindView(R.id.tv_title_right)
    AppCompatTextView mTvTitleRight;
    @BindView(R.id.iv_title_right)
    AppCompatImageView mIvTitleRight;
    @BindView(R.id.recycler_view)
    SwipeRecyclerView mRecyclerView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.img_no_message)
    ImageView img_no_message;
    private MyMessageAdapter mAdapter;
    private int pageNo = 1;
    private int pageSize = 15;
    private boolean isRefresh = true;
    private List<MyDynamicMessage.DataBean> messageList = new ArrayList<>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_message;
    }

    @Override
    public MyMessagePresenter createPresenter() {
        return new MyMessagePresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTvTitle.setText("我的消息");
        //        mRefresh.setRefreshing(true);

        setAndroidNativeLightStatusBar(mActivity, false);
        mRefresh.setOnRefreshListener(this);
        mRefresh.setRefreshing(true);
        mRecyclerView.setLoadMoreListener(this);
        mRecyclerView.useDefaultLoadMore();
        mRecyclerView.loadMoreFinish(false, true);
        mvpPresenter.getDynamicMessage(pageNo + "", pageSize + "");
        mAdapter = new MyMessageAdapter(this, messageList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        SPUtils.getInstance(mActivity).put(Utils.APPID + Utils.MESSAGE_NUMBER, 0 + "");

    }


    @OnClick(R.id.iv_title_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void showMessage(MyDynamicMessage o) {
        if (mRefresh != null && mRefresh.isRefreshing()) {

            mRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefresh.setRefreshing(false);
                }
            }, 1000);
        }

        if (isRefresh) {
            if (o.getData().size() > 0) {
                img_no_message.setVisibility(View.GONE);
                mRefresh.setVisibility(View.VISIBLE);
            } else {
                img_no_message.setVisibility(View.VISIBLE);
                mRefresh.setVisibility(View.GONE);
            }
            messageList.clear();
            messageList.addAll(o.getData());
            mAdapter.notifyDataSetChanged();
            mRecyclerView.loadMoreFinish(false, true);
        } else {
            if (o.getData().size() > 0) {
                mRecyclerView.loadMoreFinish(false, true);
                messageList.addAll(o.getData());
//                mAdapter.notifyItemRangeChanged(messageList.size() - o.getData().size(), messageList.size());
                mAdapter.notifyDataSetChanged();
            } else {
                mRecyclerView.loadMoreFinish(false, false);
            }

        }

        if (o.getData().size() < pageSize) {
            mRecyclerView.loadMoreFinish(false, false);
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
        mvpPresenter.getDynamicMessage(pageNo + "", pageSize + "");
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        pageNo++;
        mvpPresenter.getDynamicMessage(pageNo + "", pageSize + "");
    }
}
