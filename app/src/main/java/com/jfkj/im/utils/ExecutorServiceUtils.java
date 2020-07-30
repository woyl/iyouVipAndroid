package com.jfkj.im.utils;
import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceUtils {
    private Context mContext;
    private ExecutorService globalThreadPool = Executors.newCachedThreadPool();

    private static ExecutorServiceUtils serviceUtils = new ExecutorServiceUtils();

    private ExecutorServiceUtils() {

    }

    // 获得单例对象
    public static ExecutorServiceUtils getInstance() {
        return serviceUtils;
    }

    // 初始化
    public void init(Context context) {
        mContext = context;
    }

    // 获取全局线程池
    public ExecutorService getGlobalThreadPool() {
        return globalThreadPool;
    }


}
