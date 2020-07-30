package com.jfkj.im.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jfkj.im.R;
import com.jfkj.im.entity.MessageEvent;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * <pre>
 * Description:  懒加载fragment
 * @author :   ys
 * @date :         2019/12/12
 * </pre>
 */
public abstract class BaseLazyFragment<P extends BasePresenter> extends Fragment {
    public P mvpPresenter;
    public LoadingDialog progressDialog;
    public Activity mActivity;
    public View view;
    /**
     * 是否第一次加载数据
     */
    protected boolean isFirstLoad = true;

    /**
     * 是否对用户可见
     */
    private boolean isVisibleToUser = false;

    /**
     * view 是否已经创建
     */
    private boolean isViewCreat = false;

    private Unbinder unBinder;
    private CompositeDisposable mCompositeDisposable;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //        StatusBarUtil.setRootViewFitsSystemWindows(getActivity(),true);
        //        StatusBarUtil.setTranslucentStatus(getActivity());
        //        if (!StatusBarUtil.setStatusBarDarkTheme(getActivity(), true)) {
        //            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
        //            //这样半透明+白=灰, 状态栏的文字能看得清
        //            StatusBarUtil.setStatusBarColor(getActivity(),0x55000000);
        //        }
        progressDialog = new LoadingDialog(getContext(),0);


        mActivity = getActivity();
        isViewCreat = true;
        onLazyLoad();

    }

    private void onLazyLoad() {
        if (isVisibleToUser && isViewCreat && isFirstLoad) {
            isFirstLoad = false;
            loadData();
        }
    }


    public ApiStores apiStores() {
        return ApiClient.getmRetrofit().create(ApiStores.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);
        unBinder = ButterKnife.bind(this, view);
        mvpPresenter = createPresenter();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
//        initView();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        onLazyLoad();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onMessageEvent(MessageEvent event) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onUnsubscribe();
        isFirstLoad = false;
        if (unBinder != null) {
            unBinder.unbind();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void onUnsubscribe() {
        //取消注册，以避免内存泄露
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }

    public void addSubscription(DisposableObserver observer) {
        mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(observer);
    }


    protected abstract P createPresenter();

    protected abstract int getLayoutId();

    /**
     * 懒加载
     */
    protected abstract void loadData();

//    /**
//     * 初始化布局洗洗
//     */
//    protected abstract void initView();
}
