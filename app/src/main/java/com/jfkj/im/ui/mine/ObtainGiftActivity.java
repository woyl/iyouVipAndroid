package com.jfkj.im.ui.mine;

import android.os.Bundle;
import android.widget.TextView;

import com.jfkj.im.R;

import com.jfkj.im.adapter.ObtainGiftListAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.MyGiftBean;
import com.jfkj.im.entity.MyObtainGiftBean;
import com.jfkj.im.mvp.BaseActivity;
import com.jfkj.im.mvp.mine.MyGiftPresenter;
import com.jfkj.im.mvp.mine.MyGiftView;
import com.jfkj.im.mvp.mine.ObtainGiftPresenter;
import com.jfkj.im.mvp.mine.ObtainGiftView;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * <pre>
 * Description:  获得的礼物
 * @author :   ys
 * @date :         2019/12/26
 * </pre>
 */
public class ObtainGiftActivity extends BaseActivity<ObtainGiftPresenter> implements ObtainGiftView, SwipeRefreshLayout.OnRefreshListener, SwipeRecyclerView.LoadMoreListener {
    @BindView(R.id.iv_title_back)
    AppCompatImageView mIvTitleBack;
    @BindView(R.id.tv_title)
    AppCompatTextView mTvTitle;
    @BindView(R.id.tv_title_right)
    AppCompatTextView mTvTitleRight;
    @BindView(R.id.iv_title_right)
    AppCompatImageView mIvTitleRight;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.tv_gift_num)
    TextView mTvGiftNum;
    @BindView(R.id.recycler)
    SwipeRecyclerView mRecycler;

    private String giftNum;
    private ObtainGiftListAdapter mAdapter;
    private int pageSize = 20;
    private int pageNo = 1;
    private String type = "0";
    private List<MyObtainGiftBean.DataBean> mBeanList = new ArrayList<>();
    private boolean isRefresh = true;
    private String obtainGift;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_obtain_gift;
    }

    @Override
    public ObtainGiftPresenter createPresenter() {
        return new ObtainGiftPresenter(this);
    }

    @Override
    public void showGift(MyObtainGiftBean bean) {
        if (isRefresh){
            mBeanList.clear();
            mBeanList.addAll(bean.getData());
            mAdapter.notifyDataSetChanged();
        }else {
            if (bean.getData().size() > 0){
                mRecycler.loadMoreFinish(false,true);
                mBeanList.addAll(bean.getData());
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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndroidNativeLightStatusBar(this,false);
        mTvTitle.setText("获得的礼物");
        mTvType.setText("累计获得的礼物：");


        Bundle bundle = getIntent().getExtras();
        if (bundle!= null){
            obtainGift = bundle.getString("obtain_gift");

            if(obtainGift.equals("null")){
                mTvGiftNum.setText(0 + "");
            }else{

                mTvGiftNum.setText(UserInfoManger.getAccumulatedGifts());
            }
        }
        mvpPresenter.getGiftList(pageSize +"",pageNo + "",type);

        mRecycler.useDefaultLoadMore();
        mRecycler.setLoadMoreListener(this);

        mAdapter = new ObtainGiftListAdapter(this,mBeanList);

        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(false);
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
