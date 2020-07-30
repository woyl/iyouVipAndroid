package com.jfkj.im.retrofit;

import com.jfkj.im.App;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.okhttp.ErrorInterceptor;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;

import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 可以统一添加公共参数 具体看ApiClient2这个类
 */
public class ApiClient {
    private static Retrofit mRetrofit = null;
    private static ApiClient apiClient = null;

    public ApiClient() {
    }

    public static ApiClient getInstance() {
        if (apiClient == null) {
            apiClient = new ApiClient();
        }
        return apiClient;
    }

    public static Retrofit getmRetrofit() {
        if (mRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(20, TimeUnit.SECONDS);//设置读取超时时间;
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //  设置 Debug Log 模式
            builder.addNetworkInterceptor(loggingInterceptor);
            //添加错误码拦截器
            ErrorInterceptor errorInterceptor = new ErrorInterceptor();
            builder.addInterceptor(errorInterceptor);
            //添加请求头
            if (SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN) != null && SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN).trim().length() > 0) {//这个是添加到头部
                Interceptor headerInterceptor = new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                                .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                                .build();

                        return chain.proceed(request);
                    }
                };
                builder.addInterceptor(headerInterceptor);
            } else {
                Interceptor headerInterceptor = new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY + AppUtils.getReqTime()))
                                .build();
                        return chain.proceed(request);
                    }
                };
                builder.addInterceptor(headerInterceptor);
            }
// OkHttpClient okHttpClient = builder.proxy(Proxy.NO_PROXY).build();
            OkHttpClient okHttpClient = builder.build();
            mRetrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(ApiStores.base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    public static void onDestroy() {
        mRetrofit = null;
        apiClient = null;
    }
}
