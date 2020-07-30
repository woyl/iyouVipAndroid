package com.jfkj.im.okhttp;

import android.content.Context;
import android.util.Log;

import com.jfkj.im.App;
import com.jfkj.im.ui.activity.Loginregister_phone_Activity;
import com.jfkj.im.ui.dialog.CommonDialog;
import com.jfkj.im.utils.JumpUtil;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.ToastUtils;


public abstract class HttpErrorMsgObserver<T> extends HttpObserver<T> {

    final boolean mLogoutEnabled;//登录异常时，是否跳转至首页进行注销操作
    private CommonDialog commonDialog = null;

    public HttpErrorMsgObserver() {//默认打开
        this(true);
    }

    public HttpErrorMsgObserver(boolean logoutEnabled) {
        mLogoutEnabled = logoutEnabled;
    }


    @Override
    public final void onError2(Throwable e) {
        super.onError2(e);
    }

    @Override
    public void onHttpException(Exception e) {
        ToastUtils.showShortToast("请求失败");
        onNetworkException(e);
    }

    @Override
    public void onSocketTimeoutException(Exception e) {
        ToastUtils.showShortToast("连接超时");
        onNetworkException(e);
    }

    @Override
    public void onNetworkConnectException(Exception e) {
        ToastUtils.showShortToast("连接失败，请检查网络");
        onNetworkException(e);
    }

    public void onNetworkException(Exception e) {

    }

    @Override
    public void onFreezeException(Exception e) {
        ToastUtils.showShortToast(e.getMessage());

        if (mLogoutEnabled) {
            final Context context = App.getAppContext();
//            if (context != null) {
//                ARouterNavigator.navigateHomeActivityIfFreeze(CommonApplication.getsContext(), Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            }

        }

    }

    @Override
    public void onLoginException(Exception e) {
        ToastUtils.showShortToast(e.getMessage());
        if (mLogoutEnabled) {
            final Context context = App.getAppContext();
            if (context != null) {
                JumpUtil.overlay(context,Loginregister_phone_Activity.class);
            }
        }
    }

    @Override
    public void onMultiDeviceException(Exception e) {
        Log.d("@@@","message====" + e.getMessage());
        ToastUtils.showShortToast(e.getMessage());
        if (mLogoutEnabled) {
            final Context context = App.getAppContext();
            if (context != null) {

                SPUtils.getInstance(context).remove("token");
//                Log.d("@@@",SPUtils.getInstance(context).getString("token"));
                JumpUtil.overlay(context,Loginregister_phone_Activity.class);
//                commonDialog = new CommonDialog(context, true, new CommonDialog.CustomizeDialogListener() {
//                    @Override
//                    public void onClick(View view) {
//                        if (view.getId() == R.id.tv_confirm){
//                            commonDialog.dismiss();
//                            JumpUtil.overlay(context,Loginregister_phone_Activity.class);
//                        }
//                    }
//                });
//
//                commonDialog.setTitleText("温馨提示").setContentText(e.getMessage()).show();
            }
        }

    }

    @Override
    public void onOtherException(Throwable e) {

        ToastUtils.showShortToast(e.getMessage());
    }
}
