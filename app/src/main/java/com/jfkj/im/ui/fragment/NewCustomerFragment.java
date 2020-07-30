package com.jfkj.im.ui.fragment;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jfkj.im.R;
import com.jfkj.im.adapter.HomeVideoAdapter;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.entity.HomeRecommend;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.Home.NewCustomerPresenter;
import com.jfkj.im.mvp.Home.NewCustomerView;
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
 * Description:  新人fragment
 * @author :   ys
 * @date :         2019/12/18
 * </pre>
 */
public class NewCustomerFragment extends BaseFragment<NewCustomerPresenter> implements NewCustomerView, SwipeRefreshLayout.OnRefreshListener, HomeVideoAdapter.OnItemClickListener, SwipeRecyclerView.LoadMoreListener {

    @BindView(R.id.recycler)
    SwipeRecyclerView mRecycler;
    SwipeRefreshLayout mSwipeRefresh;
    private List<HomeRecommend.DataBean> mBeanList = new ArrayList<>();
    private int pageSize = 10;
    private int pageNo = 1;
    private HomeVideoAdapter mAdapter;
    private GiftBottomDialog mSheetDialog;
    private List<GiftData.DataBean.ArrayBean> giftList = new ArrayList<>();
    private boolean isRefresh = true;
    private String type = "0";
    private String addUserId;
    //视频部分
    private VideoView mVideoView;
    private TikTokController mController;
    private int mCurPos = 0;
    private ViewPagerLayoutManger layoutManger = null;

    public static NewCustomerFragment getInstance(String param1, String param2) {
        NewCustomerFragment fragment = new NewCustomerFragment();
        Bundle args = new Bundle();
        args.putString(Utils.ARG_PARAM1, param1);
        args.putString(Utils.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh);
        Log.d("@@@","走这儿，不可能的吧");
        mvpPresenter.homeNewPeople(pageNo + "", pageSize + "", type);
        mSwipeRefresh.setRefreshing(true);
        mSwipeRefresh.setOnRefreshListener(this);

        //播放器
        mVideoView = new VideoView(getContext());
        mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_CENTER_CROP);
        mVideoView.setLooping(true);
        mController = new TikTokController(getContext());
        mVideoView.setVideoController(mController);

        initRecyclerView();

    }

    private void initRecyclerView() {
        layoutManger = new ViewPagerLayoutManger(getContext(), OrientationHelper.VERTICAL);
        mAdapter = new HomeVideoAdapter(getContext(), mBeanList, false, this);
        mRecycler.setLayoutManager(layoutManger);
        mRecycler.setAdapter(mAdapter);

        mRecycler.setLoadMoreListener(this);
        mRecycler.useDefaultLoadMore();
        mRecycler.loadMoreFinish(false,true);

    }

    private void startPlay(int position) {
        View itemView = mRecycler.getChildAt(0);
        HomeVideoAdapter.ViewHolder viewHolder = (HomeVideoAdapter.ViewHolder) itemView.getTag();
        mVideoView.release();
        ViewUtils.removeViewFormParent(mVideoView);
        HomeRecommend.DataBean dataBean = mBeanList.get(position);
        Log.d("@@@","mPositionPOsition===" + position);
        if (dataBean.getHomeVideo() != null ){
            String playUrl = PreloadManager.getInstance(getContext()).getPlayUrl(dataBean.getHomeVideo());
            Log.d("@@@","playUrlplayUrl===" + playUrl);
            mVideoView.setUrl(playUrl);
            mController.addControlComponent(viewHolder.mTikTokView,true);
            viewHolder.container.addView(mVideoView, 0);
            mVideoView.start();
            mCurPos = position;
        }
    }

    @Override
    protected NewCustomerPresenter createPresenter() {
        return new NewCustomerPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_customer;
    }

    @Override
    public void onRefresh() {
        Log.d("@@@","新人页面，要刷新");
        mSwipeRefresh.setRefreshing(true);
        isRefresh = true;
        pageNo = 1;
        mvpPresenter.homeNewPeople(pageNo+ "",pageSize +"",type);
    }

    @Override
    public void showList(List<HomeRecommend.DataBean> list) {
        if (mSwipeRefresh != null && mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefresh.setRefreshing(false);
                }
            }, 1000);
        }

        if (isRefresh){
            mBeanList.clear();
            mBeanList.addAll(list);
            mAdapter.notifyDataSetChanged();
            mRecycler.scrollToPosition(0);
            Log.d("@@@","下拉刷新newcustom" + list.size());
        }else {
            if (list.size() > 0){
                mAdapter.addData(list);
                mRecycler.loadMoreFinish(false, true);
            }else {
                mRecycler.loadMoreFinish(false,false);
            }
        }

        layoutManger.setOnViewPagerListener(new ViewPagerLayoutManger.OnViewPagerListener() {
            @Override
            public void onInitComplete() {
//                startPlay(0);
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

    @Override
    public void showGiftList(List<GiftData.DataBean.ArrayBean> array) {
        giftList.clear();
        giftList.addAll(array);
        showBottomDialog(giftList);
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
        if (mSwipeRefresh != null && mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefresh.setRefreshing(false);
//                    mRecycler.scrollToPosition(0);
                }
            }, 1000);
        }
    }

    @Override
    public void onItemClick(int position) {
        addUserId = mBeanList.get(position).getRecommenduid();
        mvpPresenter.getGiftList();
    }

    /**
     * 筛选
     * @param sexType
     */
    public void setFilter(String sexType) {
        isRefresh = true;
        Log.d("@@@","走这儿，筛选");
        mvpPresenter.homeNewPeople(pageNo + "", pageSize + "", sexType);
        type = sexType;
    }

    @Override
    public void onLoadMore() {
        Log.d("@@@","走这儿，上拉加载刷新");
        isRefresh = false;
        pageNo++;
        mvpPresenter.homeNewPeople(pageNo + "", pageSize + "", type);
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

    @SuppressLint("SourceLockedOrientationActivity")
    private void releaseVideoView() {
        if (mVideoView != null) {
            mVideoView.release();
            if (mVideoView.isFullScreen()) {
                mVideoView.stopFullScreen();
            }
            if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        }
    }
}
