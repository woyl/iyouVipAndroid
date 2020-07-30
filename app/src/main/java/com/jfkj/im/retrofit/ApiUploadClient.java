package com.jfkj.im.retrofit;

import com.jfkj.im.App;
import com.jfkj.im.okhttp.ErrorInterceptor;
import com.jfkj.im.utils.SPUtils;

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

public class ApiUploadClient {
    private static Retrofit mRetrofit = null;
    private static ApiUploadClient apiClient = null;

    public ApiUploadClient() {
    }


    public static Retrofit getUpRetrofit() {
        mRetrofit = null;
        if (mRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(20, TimeUnit.SECONDS);//设置读取超时时间;
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            //设置error code拦截
            ErrorInterceptor errorInterceptor = new ErrorInterceptor();
            builder.addInterceptor(errorInterceptor);
            //  设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
//            FormBody.Builder bodyBuilder = new FormBody.Builder();
//            bodyBuilder.add("cfrom", "cfrom").add("channel", "channel").add("version", "version").build();//这个是添加公共参数
            if (SPUtils.getInstance(App.getAppContext()).getString("token") != null) {//这个是添加到头部
                if(SPUtils.getInstance(App.getAppContext()).getString("token").trim().length()>0){
                    Interceptor interceptor = new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request()
                                    .newBuilder()
                                    //   .post(bodyBuilder.build())
                                    .addHeader("token", SPUtils.getInstance(App.getAppContext()).getString("token"))
                                    .build();
                            return chain.proceed(request);
                        }
                    };
                    builder.addInterceptor(interceptor);
                }
            }
            if(SPUtils.getInstance(App.getAppContext()).getString("sign")!=null){
                if(SPUtils.getInstance(App.getAppContext()).getString("sign").trim().length()>0){
                    Interceptor interceptor1=new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request()
                                    .newBuilder()
                                    .addHeader("sign",SPUtils.getInstance(App.getAppContext()).getString("sign"))
                                    .build();
                            return chain.proceed(request);
                        }
                    };
                    builder.addInterceptor(interceptor1);
                }
            }
            OkHttpClient okHttpClient = builder.build();
            mRetrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(ApiStores.baseupload_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return mRetrofit;
    }


    public  static ApiUploadClient getInstance() {
        if (apiClient == null) {
            apiClient = new ApiUploadClient();
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

            //设置error code拦截
            ErrorInterceptor errorInterceptor = new ErrorInterceptor();
            builder.addInterceptor(errorInterceptor);
          //  设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
//            FormBody.Builder bodyBuilder = new FormBody.Builder();
//            bodyBuilder.add("cfrom", "cfrom").add("channel", "channel").add("version", "version").build();//这个是添加公共参数
            if (SPUtils.getInstance(App.getAppContext()).getString("token") != null) {//这个是添加到头部
                if(SPUtils.getInstance(App.getAppContext()).getString("token").trim().length()>0){
                    Interceptor interceptor = new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request()
                                    .newBuilder()
                                    //   .post(bodyBuilder.build())
                                    .addHeader("token", SPUtils.getInstance(App.getAppContext()).getString("token"))
                                    .build();
                            return chain.proceed(request);
                        }
                    };
                    builder.addInterceptor(interceptor);
                }
            }
            if(SPUtils.getInstance(App.getAppContext()).getString("sign")!=null){
                if(SPUtils.getInstance(App.getAppContext()).getString("sign").trim().length()>0){
                    Interceptor interceptor1=new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request()
                                    .newBuilder()
                                    .addHeader("sign",SPUtils.getInstance(App.getAppContext()).getString("sign"))
                                    .build();
                            return chain.proceed(request);
                        }
                    };
                    builder.addInterceptor(interceptor1);
                }
            }
            OkHttpClient okHttpClient = builder.build();
            mRetrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(ApiStores.baseupload_url)
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
