package com.jfkj.im.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.jfkj.im.Bean.PraiseIdBean;
import com.jfkj.im.Bean.UserDetailClubBean;
import com.jfkj.im.R;
import com.jfkj.im.adapter.dynamic.CircleDynamicAdapter;

import com.jfkj.im.adapter.dynamic.UserDetailDynamicAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.CircleBean;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.entity.UserDetail;
import com.jfkj.im.event.PraiseEvent;
import com.jfkj.im.listener.ItemClickListener;
import com.jfkj.im.listener.onItemClickListener;
import com.jfkj.im.mvp.BaseLazyFragment;
import com.jfkj.im.mvp.BasePresenter;
import com.jfkj.im.mvp.userDetail.UserDetailPresenter;
import com.jfkj.im.mvp.userDetail.UserDetailView;
import com.jfkj.im.ui.home.UserDetailActivity;
import com.jfkj.im.utils.Utils;
import com.luck.picture.lib.widget.FocusNoLayoutManager;
import com.lzy.okgo.utils.OkLogger;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class UserDetailDynamicFragment extends BaseLazyFragment<UserDetailPresenter> implements UserDetailView,    ItemClickListener, SwipeRecyclerView.LoadMoreListener {

    @BindView(R.id.recycler)
    SwipeRecyclerView mRecycler;

    @BindView(R.id.tv_no_data)
            TextView tvNoData;




    int pageNo = 1;
    private boolean isLike; // 是否点赞过

    private List<CircleBean.DataBean> mDynamicList = new ArrayList<>();


    private UserDetailPresenter mPresenter;

    private String userId;
    private FocusNoLayoutManager mFocusNoLayoutManager;
    private UserDetailDynamicAdapter mDynamicAdapter;


    @Override
    protected UserDetailPresenter createPresenter() {
        return mPresenter = new UserDetailPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_detail_dynamic;
    }

    @Override
    protected void loadData() {

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle arguments = getArguments();
        userId = arguments.getString("UserId");


        mDynamicAdapter = new UserDetailDynamicAdapter(getContext(), mDynamicList, this, true);

        mFocusNoLayoutManager = new FocusNoLayoutManager(getContext());
        mRecycler.setLayoutManager(mFocusNoLayoutManager);
        mRecycler.setAdapter(mDynamicAdapter);
        mRecycler.useDefaultLoadMore();
        mRecycler.setLoadMoreListener(this);
        mRecycler.loadMoreFinish(false, true);


        mvpPresenter.getUserCircleArticle(UserInfoManger.getLongitude(), UserInfoManger.getLatitude(), userId, getContext(),pageNo);

    }

    @Override
    public void showDetail(UserDetail detail) {

    }

    @Override
    public void showGiftList(List<GiftData.DataBean.ArrayBean> array) {

    }

    @Override
    public void showCircleArticle(CircleBean circleBean) {
        if (circleBean.getData().size() > 0) {
            mRecycler.loadMoreFinish(false, true);

            mDynamicList.addAll(circleBean.getData());
            mDynamicAdapter.notifyDataSetChanged();
//            mIvNoDynamic.setVisibility(View.GONE);
            mRecycler.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);

        } else {
            mRecycler.loadMoreFinish(false, false);

            if(pageNo == 1){
                mRecycler.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
            }


        }
    }

    @Override
    public void showPraise(PraiseIdBean bean, int position) {

        if (isLike) { //取消点赞
            mDynamicList.get(position).setIsPraise(0);
            mDynamicList.get(position).setPraisecount(String.valueOf(Integer.parseInt(mDynamicList.get(position).getPraisecount()) - 1));
            mDynamicList.get(position).setPraiseId(bean.getData().getPraiseid());

            mDynamicAdapter.notifyItemChanged(position,"1111");

        } else {
            mDynamicList.get(position).setIsPraise(1);
            mDynamicList.get(position).setPraisecount(String.valueOf(Integer.valueOf(mDynamicList.get(position).getPraisecount()) + 1));
            mDynamicList.get(position).setPraiseId(bean.getData().getPraiseid());
            //刷新单个控件， 不刷新整个item
            mDynamicAdapter.notifyItemChanged(position,"2222");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upPraise(PraiseEvent event){
        if (mDynamicList.size() == 0) return;
        int position = event.getPosition();
        if (mDynamicList.get(position).getIsPraise() == 1) {
            //点赞过的  点击要取消点赞
            isLike = true;
            mDynamicList.get(position).setIsPraise(0);
            mDynamicList.get(position).setPraisecount(String.valueOf(Integer.parseInt(mDynamicList.get(position).getPraisecount()) - 1));
            mDynamicList.get(position).setPraiseId(mDynamicList.get(position).getArticleId());

            mDynamicAdapter.notifyItemChanged(position,"1111");

        } else {
            //点赞
            isLike = false;
            mDynamicList.get(position).setIsPraise(1);
            mDynamicList.get(position).setPraisecount(String.valueOf(Integer.valueOf(mDynamicList.get(position).getPraisecount()) + 1));
            mDynamicList.get(position).setPraiseId(mDynamicList.get(position).getArticleId());
            //刷新单个控件， 不刷新整个item
            mDynamicAdapter.notifyItemChanged(position,"2222");
        }
    }

    @Override
    public void showSuccess() {

    }

    @Override
    public void showGroupList(UserDetailClubBean o) {

    }

    @Override
    public void Pullblacksuccess() {

    }

    @Override
    public void upLoadPic(String url) {

    }

    @Override
    public void fails(String error) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    @Override
    public void onPositionClick(int position, View v) {

    }

    @Override
    public void onPositionClick(int position, View v, String praiseid) {

        if(praiseid.equals("1")){
            isLike = true;
            //取消点赞
            mvpPresenter.getUserPraise(getContext(),mDynamicList.get(position).getArticleId(),Utils.PRAISE_CANCEL,mDynamicList.get(position).getPraiseId(),position);
        }else{
            isLike = false;
            //点赞
            mvpPresenter.getUserPraise(getContext(),mDynamicList.get(position).getArticleId(),Utils.PRAISE_TRUE,mDynamicList.get(position).getPraiseId(),position);
        }
    }



    /**
     * 加载更多
     */
    @Override
    public void onLoadMore() {
        pageNo++;
        mvpPresenter.getUserCircleArticle(UserInfoManger.getLongitude(), UserInfoManger.getLatitude(), userId, getContext(),pageNo);
    }
}
