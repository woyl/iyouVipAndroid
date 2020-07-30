package com.jfkj.im.ui.home;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.adapter.HomeVideoAdapter;
import com.jfkj.im.adapter.VideoAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.entity.HomeRecommend;
import com.jfkj.im.mvp.BaseLazyFragment;
import com.jfkj.im.mvp.Home.NearByPresent;
import com.jfkj.im.mvp.Home.NearByView;
import com.jfkj.im.ui.dialog.CommonDialog;
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
 * Description:
 * @author :   ys
 * @date :         2019/12/19
 * </pre>     BasicUserInformation
 */
public class NearByFragment2 extends BaseLazyFragment<NearByPresent> implements NearByView, SwipeRefreshLayout.OnRefreshListener, HomeVideoAdapter.OnItemClickListener, SwipeRecyclerView.LoadMoreListener, VideoAdapter.OnItemClickListener {

    @BindView(R.id.recycler_view)
    SwipeRecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    private int pageNo = 1;
    private int pageSize = 10;
    private String age = "20";
    private boolean isRefresh = true;
    private CommonDialog locationDialog = null;
    private HomeVideoAdapter mAdapter;
    private List<HomeRecommend.DataBean> mBeanList = new ArrayList<>();
    private String type = "0";
    private String addUserId;
    private List<GiftData.DataBean.ArrayBean> giftList = new ArrayList<>();
    private GiftBottomDialog mSheetDialog = null;
    //视频部分
    private VideoView mVideoView;
    private TikTokController mController;
    private int mCurPos = 0;
    private ViewPagerLayoutManger layoutManger = null;
    private boolean isChecked;


    public static NearByFragment2 getInstance(String param1, String param2) {
        NearByFragment2 fragment = new NearByFragment2();
        Bundle args = new Bundle();
        args.putString(Utils.ARG_PARAM1, param1);
        args.putString(Utils.ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected NearByPresent createPresenter() {
        return new NearByPresent(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycler_view;
    }

    @Override
    protected void loadData() {
        Log.d("@@@","UserInfoManger.getLongitude()" + UserInfoManger.getLongitude() +
                "UserInfoManger.getLatitude()" + UserInfoManger.getLatitude() );
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString(Utils.ARG_PARAM1);
            Log.d("@@@","Utils.ARG_PARAM1Utils.ARG_PARAM1 + type " + type);
        }

        mvpPresenter.homeNearBy(pageNo + "", pageSize + "", UserInfoManger.getLongitude(), UserInfoManger.getLatitude(),type);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("###","初始化 刷新s362565ss");
        mRefreshLayout.setRefreshing(true);
        mRefreshLayout.setOnRefreshListener(this);

        //播放器
        mVideoView = new VideoView(getContext());
        mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_DEFAULT);
        mVideoView.setLooping(true);
        mController = new TikTokController(getContext());
        mVideoView.setVideoController(mController);
        initRecyclerview();
    }

//    @Override
//    protected void initView() {
//
//    }

    private void initRecyclerview() {

        layoutManger = new ViewPagerLayoutManger(getContext(), OrientationHelper.VERTICAL);

        mAdapter = new HomeVideoAdapter(getContext(), mBeanList, false, this);
        mRecyclerView.setLayoutManager(layoutManger);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLoadMoreListener(this);
        mRecyclerView.useDefaultLoadMore();
        mRecyclerView.loadMoreFinish(false,true);

    }

    private void startPlay(int position) {
        if(mBeanList.size()==0){
            return;
        }
        if (mRecyclerView == null || mRecyclerView.getChildAt(0) == null){
            return;
        }
        View itemView = mRecyclerView.getChildAt(0);
        HomeVideoAdapter.ViewHolder viewHolder = (HomeVideoAdapter.ViewHolder) itemView.getTag();
        mVideoView.release();
        ViewUtils.removeViewFormParent(mVideoView);
        HomeRecommend.DataBean dataBean = mBeanList.get(position);
        if (dataBean.getHomeVideo() != null) {
            String playUrl = PreloadManager.getInstance(getContext()).getPlayUrl(dataBean.getHomeVideo());
            Log.d("@@@", "playUrlplayUrl===" + playUrl);
            mVideoView.setUrl(playUrl);
         //   mController.addControlComponent(viewHolder.mTikTokView, true);
            mController.addControlComponent(viewHolder.mTikTokView,true);
            viewHolder.container.addView(mVideoView, 0);
            mVideoView.start();
            mCurPos = position;
        }else {
            Log.d("@@@","NearByFragment2 -----当前psoition===" + position + "不是视频 不播放");
        }
    }

    @Override
    public void showData(List<HomeRecommend.DataBean> list) {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            }, 1000);
        }
        if (isRefresh) {
            mBeanList.clear();
            mBeanList.addAll(list);
            mAdapter.notifyDataSetChanged();
         //   mRecyclerView.scrollToPosition(0);
            Log.d("@@@","下拉刷新nearby" + list.size());
        } else {
            if (list.size() > 0) {
                mAdapter.addData(list);
                mRecyclerView.loadMoreFinish(false, true);
            } else {
                mRecyclerView.loadMoreFinish(false, false);
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
                mCurPos = position;
            }
        });
    }

    @Override
    public void showGiftList(List<GiftData.DataBean.ArrayBean> array) {
        giftList.clear();
        giftList.addAll(array);
        showBottomDialog(giftList);
    }

    private void showBottomDialog(List<GiftData.DataBean.ArrayBean> giftList) {
        mSheetDialog = new GiftBottomDialog(getContext(),R.style.Dialog_Light,giftList,getActivity());
        mSheetDialog.setUserId(addUserId);
        mSheetDialog.show();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
            mRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(false);
                }
            }, 1000);
        }
    }

    @Override
    public void onRefresh() {
        Log.d("@@@","附近页面，要刷新");
        mRefreshLayout.setRefreshing(true);
        isRefresh = true;
        pageNo = 1;
        mvpPresenter.homeNearBy(pageNo + "", pageSize + "", UserInfoManger.getLongitude(), UserInfoManger.getLatitude(),type);
    }

    /**
     * 位置信息dialog
     */
    public void showLocationDialog() {
        locationDialog = new CommonDialog(getContext(), R.style.dialogstyle, new CommonDialog.CustomizeDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_cancel:
                        locationDialog.dismiss();
                        break;
                    case R.id.tv_confirm:
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", App.getAppContext().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        locationDialog.setTitleText("无法获取位置信息").setContentText("您尚未开启定位，请去手机设置开启 定位功能")
                .setConfirmBtnText("去开启")
                .setCancelBtnText("以后再说")
                .show();

    }

    @Override
    public void onItemClick(int position) {
        addUserId = mBeanList.get(position).getRecommenduid();
        if (Utils.giftList != null && Utils.giftList.size() > 0){
            showGiftList(Utils.giftList);
        }else {
            mvpPresenter.getGiftList();
        }
    }

    /**
     * 筛选
     * @param sexType
     */
    public void setFilter(String sexType) {
        mRefreshLayout.setRefreshing(true);
        isRefresh = true;
        pageNo=1;
        mBeanList.clear();
        mAdapter.notifyDataSetChanged();
        mvpPresenter.homeNearBy(pageNo + "", pageSize + "", UserInfoManger.getLongitude(), UserInfoManger.getLatitude(),sexType);
        type = sexType;
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        pageNo++;
        mvpPresenter.homeNearBy(pageNo + "", pageSize + "", UserInfoManger.getLongitude(), UserInfoManger.getLatitude(),type);
    }

    public void releaseVideo() {
        if (mVideoView != null){
            mVideoView.pause();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("@@@","onPause");
        if (mVideoView != null){
            mVideoView.pause();
        }
    }

    private void releaseVideoView() {
        Log.d("@@@","onPause");
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
        if (mVideoView != null && isChecked){
            mVideoView.resume();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser){
            if (mVideoView != null && mVideoView.isPlaying()){
                mVideoView.pause();
            }
        }
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

}
