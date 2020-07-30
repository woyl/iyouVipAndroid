package com.jfkj.im.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.jfkj.im.App;
import com.jfkj.im.R;
import com.jfkj.im.adapter.HomeVideoAdapter;
import com.jfkj.im.adapter.VideoAdapter;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.entity.GiftData;
import com.jfkj.im.entity.HomeRecommend;
import com.jfkj.im.mvp.BaseFragment;
import com.jfkj.im.mvp.Home.NearByPresent;
import com.jfkj.im.mvp.Home.NearByView;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.utils.PermissionsCheckUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;
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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2019/12/19
 * </pre>
 */
public class NearByFragment extends BaseFragment<NearByPresent> implements NearByView, SwipeRefreshLayout.OnRefreshListener, HomeVideoAdapter.OnItemClickListener, SwipeRecyclerView.LoadMoreListener, VideoAdapter.OnItemClickListener {

    @BindView(R.id.recycler_view)
    SwipeRecyclerView mRecyclerView;
//    @BindView(R.id.refresh_layout)
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
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 0x14;
    private static final String[] LOCATIONGROUPS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    public static NearByFragment getInstance(String param1, String param2) {
        NearByFragment fragment = new NearByFragment();
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mvpPresenter.homeNearBy(pageNo + "", pageSize + "", UserInfoManger.getLongitude(), UserInfoManger.getLatitude(),"0");
//        checkLocation();
        initView();

    }

    private void initView() {
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRefreshLayout.setRefreshing(true);
        mRefreshLayout.setOnRefreshListener(this);
        Bundle bundle = getArguments();

        //播放器
        mVideoView = new VideoView(getContext());
        mVideoView.setScreenScaleType(VideoView.SCREEN_SCALE_CENTER_CROP);
        mVideoView.setLooping(true);
        mController = new TikTokController(getContext());
        mVideoView.setVideoController(mController);

        initRecyclerview();
    }

    private void initRecyclerview() {

        layoutManger = new ViewPagerLayoutManger(getContext(), OrientationHelper.VERTICAL);

        mAdapter = new HomeVideoAdapter(getContext(), mBeanList, false, this);
        mRecyclerView.setLayoutManager(layoutManger);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLoadMoreListener(this);
        mRecyclerView.useDefaultLoadMore();
        mRecyclerView.loadMoreFinish(false,true);
        PermissionsUtil.requestPermission(mActivity, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permissions) {



            }

            @Override
            public void permissionDenied(@NonNull String[] permissions) {
                showLocationDialog();

            }
        }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    private void startPlay(int position) {
        View itemView = mRecyclerView.getChildAt(0);
        HomeVideoAdapter.ViewHolder viewHolder = (HomeVideoAdapter.ViewHolder) itemView.getTag();
        mVideoView.release();
        ViewUtils.removeViewFormParent(mVideoView);
        HomeRecommend.DataBean dataBean = mBeanList.get(position);
        if (dataBean.getHomeVideo() != null) {
            String playUrl = PreloadManager.getInstance(getContext()).getPlayUrl(dataBean.getHomeVideo());
            Log.d("@@@", "playUrlplayUrl===" + playUrl);
            mVideoView.setUrl(playUrl);
            mController.addControlComponent(viewHolder.mTikTokView, true);
            viewHolder.container.addView(mVideoView, 0);
            mVideoView.start();
            mCurPos = position;
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
            mRecyclerView.scrollToPosition(0);
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
        mvpPresenter.homeNearBy(pageNo + "", pageSize + "", UserInfoManger.getLongitude(), UserInfoManger.getLatitude(),"0");
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
        mvpPresenter.getGiftList();
    }

    /**
     * 筛选
     * @param sexType
     */
    public void setFilter(String sexType) {
        isRefresh = true;
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
        releaseVideoView();
    }

    @SuppressLint("SourceLockedOrientationActivity")
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
