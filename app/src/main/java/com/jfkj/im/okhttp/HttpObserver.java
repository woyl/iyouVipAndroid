package com.jfkj.im.okhttp;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

public abstract class HttpObserver<T> extends DisposableObserver<T> {

    @Override
    public final void onComplete() {
        onFinished();
    }


    @Override
    public final void onError(Throwable e) {
        e.printStackTrace();

        onError2(e);
        onFinished();
    }


    public void onError2(Throwable e) {
        if (e instanceof HttpException) {
            onHttpException((Exception) e);
        } else if (e instanceof SocketTimeoutException) {
            onSocketTimeoutException((Exception) e);

        } else if (e instanceof UnknownHostException || e instanceof ConnectException) {
            onNetworkConnectException((Exception) e);
        } else if (e instanceof LoginException) {
            onLoginException((Exception) e);
        } else if (e instanceof FreezeException) {
            onFreezeException((Exception) e);
        } else if (e instanceof MultiDeviceException) {
            onMultiDeviceException((Exception) e);
        } else {
            onOtherException(e);
        }
    }

    public void onHttpException(Exception e) {

    }

    public void onSocketTimeoutException(Exception e) {

    }


    public void onNetworkConnectException(Exception e) {

    }

    public void onFreezeException(Exception e) {

    }

    public void onLoginException(Exception e) {

    }

    public void onMultiDeviceException(Exception e) {

    }

    public void onOtherException(Throwable e) {

    }


    public void onFinished() {

    }
}
