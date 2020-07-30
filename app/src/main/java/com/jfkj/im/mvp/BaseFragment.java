package com.jfkj.im.mvp;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jfkj.im.R;
import com.jfkj.im.TIM.utils.ScreenUtil;
import com.jfkj.im.entity.MessageEvent;
import com.jfkj.im.retrofit.ApiClient;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.ui.dialog.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

public abstract class BaseFragment<P extends BasePresenter> extends Fragment {
    public P mvpPresenter;
    public LoadingDialog progressDialog;
    public Activity mActivity;
    private Unbinder unBinder;
    public View view;
    private CompositeDisposable mCompositeDisposable;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //,R.style.dialog
        progressDialog = new LoadingDialog(getContext(), 0);

        mvpPresenter = createPresenter();
        mActivity = getActivity();

    }

    protected abstract P createPresenter();

    protected abstract int getLayoutId();

    public ApiStores apiStores() {
        return ApiClient.getmRetrofit().create(ApiStores.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);
        unBinder = ButterKnife.bind(this, view);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
//        view.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(getActivity()), R.color.c111111));
        return view;
    }

    public void toastShow(int resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    public void toastShow(String resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onMessageEvent(MessageEvent event) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onUnsubscribe();
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
//        if (mCompositeDisposable == null) {
        mCompositeDisposable = new CompositeDisposable();
//        }
        mCompositeDisposable.add(observer);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void fullScreen(Activity activity) {
        Window window = mActivity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.transparent));
    }

    public void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    public void setStaturBar(View view) {
        view.setPadding(0, ScreenUtil.getStatusBarHeight(), 0, 0);
    }

}
