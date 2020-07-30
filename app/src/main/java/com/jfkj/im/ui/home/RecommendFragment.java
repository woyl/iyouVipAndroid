package com.jfkj.im.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jfkj.im.R;
import com.jfkj.im.adapter.HomeVideoAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.entity.HomeRecommend;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.Home.RecommendPresenter;
import com.jfkj.im.mvp.Home.RecommendView;
import com.jfkj.im.utils.Utils;
import com.jfkj.im.utils.ViewUtils;
import com.jfkj.im.utils.cache.PreloadManager;
import com.jfkj.im.videoPlay.TikTokController;
import com.jfkj.im.widget.GiftBottomDialog;
import com.jfkj.im.widget.ViewPagerLayoutManger;
import com.nc.player.player.VideoView;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.OrientationHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

/**
 * <pre>
 * Description:  推荐
 *
 * @author :   ys
 * @date :         2019/11/22
 * </pre>
 */
public class RecommendFragment extends BaseFragment<RecommendPresenter> implements RecommendView, HomeVideoAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, SwipeRecyclerView.LoadMoreListener {

    @BindView(R.id.recycler)
    SwipeRecyclerView mRecycler;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mRefreshLayout;
    private String userId;
    private HomeVideoAdapter mAdapter;
    private List<HomeRecommend.DataBean> list = new ArrayList<>();
    private String text;
    private GiftBottomDialog mSheetDialog;
    private List<GiftData.DataBean.ArrayBean> giftList = new ArrayList<>();
    private String addUserId;
    private int pageNo = 1;
    private int pageSize = 10;
    private boolean isRefresh = true;
    private String type = "0";
    //视频部分
    private VideoView mVideoView;
    private TikTokController mController;
    private int mCurPos = 0;
    private ViewPagerLayoutManger layoutManger;

    public static RecommendFragment getInstance(String param1, String param2) {
        RecommendFragment fragment = new RecommendFragment();
        Bundle args = new Bundle();
        args.putString(Utils.ARG_PARAM1, param1);
        args.putString(Utils.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RecommendPresenter createPresenter() {
        return new RecommendPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_comment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("@@@","token:::" + UserInfoManger.getToken());
        mRefreshLayout.setRefreshing(true);
        mRefreshLayout.setOnRefreshListener(this);
        mvpPresenter.getHomeRecommend( pageNo +"",pageSize + "",type);

        //播放器
        mVideoView = new VideoView(getContext());
        mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_CENTER_CROP);
        mVideoView.setLooping(true);
        mController = new TikTokController(getContext());
        mVideoView.setVideoController(mController);

        layoutManger = new ViewPagerLayoutManger(getContext(), OrientationHelper.VERTICAL);

        mAdapter = new HomeVideoAdapter(getContext(), list, false, this);
        mRecycler.setLayoutManager(layoutManger);
        mRecycler.setAdapter(mAdapter);

        mRecycler.useDefaultLoadMore();
        mRecycler.setLoadMoreListener(this);
        mRecycler.loadMoreFinish(false,true);

    }

    private void startPlay(int position) {
        View itemView = mRecycler.getChildAt(0);
        HomeVideoAdapter.ViewHolder viewHolder = (HomeVideoAdapter.ViewHolder) itemView.getTag();
        mVideoView.release();
        ViewUtils.removeViewFormParent(mVideoView);
        HomeRecommend.DataBean dataBean = list.get(position);
        Log.d("@@@","mPositionPOsition===" + position);
        String playUrl = PreloadManager.getInstance(getContext()).getPlayUrl(dataBean.getHomeVideo());
        Log.d("@@@","playUrlplayUrl===" + playUrl);
        mVideoView.setUrl(playUrl);
        mController.addControlComponent(viewHolder.mTikTokView,true);
        viewHolder.container.addView(mVideoView, 0);
        mVideoView.start();
        mCurPos = position;
    }

    @Override
    public void onItemClick(int position) {

        addUserId = list.get(position).getRecommenduid();
        mvpPresenter.getGiftList();

    }

    /**
     * 礼物viewpager
     */
    private void showBottomDialog(List<GiftData.DataBean.ArrayBean> list) {
        mSheetDialog = new GiftBottomDialog(getContext(),R.style.Dialog_Light,list,getActivity());
        mSheetDialog.setUserId(addUserId);
        mSheetDialog.show();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()){
            mRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                    mRecycler.scrollToPosition(0);
                }
            },1000);
        }
    }

    @Override
    public void showGiftList(List<GiftData.DataBean.ArrayBean> array) {
        giftList.clear();
        giftList.addAll(array);
        showBottomDialog(giftList);
    }

    @Override
    public void showHomeRecommend(HomeRecommend recommend) {
        if (isRefresh){
            list.clear();
            list.addAll(recommend.getData());
            mAdapter.notifyDataSetChanged();
            mRecycler.scrollToPosition(0);
            Log.d("@@@","下拉刷新Recommend" + list.size());
        }else {
            if (recommend.getData().size() > 0){
                mAdapter.addData(recommend.getData());
                mRecycler.loadMoreFinish(false,true);
            }else {
                mRecycler.loadMoreFinish(false,false);
            }
        }

        layoutManger.setOnViewPagerListener(new ViewPagerLayoutManger.OnViewPagerListener() {
            @Override
            public void onInitComplete() {
                startPlay(0);
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                if (position == mCurPos){
                    mVideoView.release();
                }

            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                if (mCurPos == position) {
                    return;
                }
                startPlay(position);
            }
        });
    }

    /**
     * 筛选
     * @param sexType
     */
    public void setFilter(String sexType) {
        isRefresh = true;
        mvpPresenter.getHomeRecommend(pageNo + "",pageSize + "",sexType);
        type = sexType;
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        isRefresh = true;
        pageNo = 1;
        Log.d("@@@","要属性fragmentaaaaaaaa");
        mvpPresenter.getHomeRecommend(pageNo + "" ,pageSize + "",type);
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        pageNo++;
        mvpPresenter.getHomeRecommend(pageNo+"",pageSize+"",type);
    }

    public void releaseVideo() {
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("@@@","onPause");
        releaseVideoView();
    }

    private void releaseVideoView() {
        if (mVideoView != null) {

            mVideoView.release();
            if (mVideoView.isFullScreen()) {
                mVideoView.stopFullScreen();
            }
            mCurPos = -1;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("@@@","onResume");
        mVideoView.resume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser){
            Log.d("@@@","rererereererer");
            releaseVideoView();
        }else {

        }
    }
}
