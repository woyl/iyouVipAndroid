package com.jfkj.im.download;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/3/25
 * </pre>
 */
public class DownloadInterceptor implements Interceptor {

    private DownLoadUrlListener listener;

    public DownloadInterceptor(DownLoadUrlListener listener) {
        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        return originalResponse.newBuilder()
                .body(new DownloadResponseBody(originalResponse.body(), listener))
                .build();
    }
}
