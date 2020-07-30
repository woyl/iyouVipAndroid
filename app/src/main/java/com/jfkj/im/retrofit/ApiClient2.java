package com.jfkj.im.retrofit;

import android.util.Log;

import com.jfkj.im.App;
import com.jfkj.im.TIM.utils.MD5Utils;
import com.jfkj.im.data.UserInfoManger;
import com.jfkj.im.okhttp.ErrorInterceptor;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.SPUtils;
import com.jfkj.im.utils.Utils;
import com.lzy.okgo.utils.OkLogger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient2 {
    private static final String COMMON_PARAMS_NAME_OS_NAME = "osName";
    private static final String COMMON_PARAMS_VALUE_OS_NAME = "android";

    private static Retrofit mRetrofit = null;
    private static ApiClient2 apiClient = null;

    public ApiClient2() {
    }

    public  static ApiClient2 getInstance() {
        if (apiClient == null) {
            apiClient = new ApiClient2();
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

            //添加公共参数
            BasicParamsInterceptor basicParamsInterceptor = new BasicParamsInterceptor.Builder()
                    .addQueryParam(Utils.DEVICENAME, Utils.getdeviceName()) //设备名称
                    .addQueryParam(Utils.DEVICEID, AppUtils.getIMEI(App.getAppContext()))
                    .addQueryParam(Utils.APPVERSION, AppUtils.getVersionName(App.getAppContext()))
                    .addQueryParam(Utils.CHANNEL, Utils.APP_CHANNEL)
                    .addQueryParam(Utils.OSNAME, Utils.ANDROID)
                    .addQueryParam(Utils.REQ_TIME, AppUtils.getReqTime())
                    .build();
            builder.addInterceptor(basicParamsInterceptor);

            //错误拦截器
            ErrorInterceptor errorInterceptor = new ErrorInterceptor();
            builder.addInterceptor(errorInterceptor);


            //添加请求头
            if (SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN) != null && SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN).trim().length() > 0) {//这个是添加到头部
                Interceptor headerInterceptor = new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        OkLogger.e("header  " + Utils.KEY+AppUtils.getReqTime() );
                        Request request = chain.request().newBuilder()
                                .addHeader(Utils.TOKEN, SPUtils.getInstance(App.getAppContext()).getString(Utils.TOKEN))
                                .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY+AppUtils.getReqTime()))
                                .build();

                        return chain.proceed(request);
                    }
                };
                builder.addInterceptor(headerInterceptor);
                Log.d("@@@", "apiclient ==== " + UserInfoManger.getToken());

            } else {
                Interceptor headerInterceptor = new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader(Utils.SIGN, MD5Utils.getMD5String(Utils.KEY+AppUtils.getReqTime()))
                                .build();
                        return chain.proceed(request);
                    }
                };
                builder.addInterceptor(headerInterceptor);
                Log.d("@@@", "Utils.SIGN ==== " + MD5Utils.getMD5String( Utils.KEY+AppUtils.getReqTime()));
            }
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

    public static void onDestroy(){
        mRetrofit=null;
        apiClient=null;
    }
}
