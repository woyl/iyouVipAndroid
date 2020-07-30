package com.jfkj.im.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jfkj.im.Bean.AppVersion;
import com.jfkj.im.R;
import com.jfkj.im.download.ApkDownUtil;
import com.jfkj.im.download.DownLoadUrlListener;
import com.jfkj.im.utils.AppUtils;
import com.jfkj.im.utils.ToastUtils;

import java.io.File;
import java.util.Locale;

import androidx.annotation.NonNull;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/3/24
 * </pre>
 */
public class AppUpdateDialog extends Dialog implements View.OnClickListener {

    TextView mTvUpdateTitle;
    TextView mTvContentTitle;
    TextView tvUpdateContent;
    TextView mTvUpdateNow;
    TextView mTvUpdateLater;
    ProgressBar progressBar;
    private Activity activity;

    private Context mContext;
    private AppVersion mVersion;
    private String downLoadUrl;
    public static final String GO_UPDATE = "现在升级";

    public static final String GO_INSERT = "立即安装";
    private int curProgress = 0;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            setProgress(curProgress);
        }
    };

    public AppUpdateDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public AppUpdateDialog(@NonNull Context context, int themeResId, AppVersion version, Activity activity) {
        super(context, themeResId);
        this.mContext = context;
        this.mVersion = version;
        this.activity = activity;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_updata);
        mTvUpdateTitle = findViewById(R.id.tv_update_title);
        mTvContentTitle = findViewById(R.id.tv_content_title);
        tvUpdateContent = findViewById(R.id.tv_update_content);
        progressBar = findViewById(R.id.progressbar);
        mTvUpdateNow = findViewById(R.id.tv_update_now);
        mTvUpdateLater = findViewById(R.id.tv_update_later);
        downLoadUrl = mVersion.getData().getDownloadurl();
        //加载HTML格式更新内容
        tvUpdateContent.setText(Html.fromHtml(mVersion.getData().getMessageX()));
        mTvUpdateNow.setText(GO_UPDATE);
        mTvUpdateNow.setOnClickListener(this);
        mTvUpdateLater.setOnClickListener(this);

        if (mVersion.getData().isForceUpdate()){
            mTvUpdateLater.setVisibility(View.GONE);
            setCancelable(false);
            setCanceledOnTouchOutside(false);
        }else {
            mTvUpdateLater.setVisibility(View.VISIBLE);
            setCancelable(true);
            setCanceledOnTouchOutside(true);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_update_now){
            //立即更新
            if (TextUtils.isEmpty(downLoadUrl)) {
                ToastUtils.showShortToast("获取更新数据失败!");
            }else {
                startDown();
            }
        }else if (v.getId() == R.id.tv_update_later){
            //暂不更新
            dismiss();
        }
    }

    /**
     * 开始下载
     */
    private void startDown() {
//        if (mTvUpdateNow.getText().toString().equals(GO_INSERT)) {
//            Log.d("@@@","到这儿，要安装apk");
//
//            AppUtils.installApk(mContext,activity,);
//
//        } else {

//            ApkDownUtil mApkDownUtil = new ApkDownUtil(downLoadUrl);
//
//            DownLoadUrlListener listener = new DownLoadUrlListener() {
//                @Override
//                public void onUpdate(long bytesRead, long contentLength, boolean done) {
//                    int progress = (int) ((bytesRead * 100) / contentLength);
//                    if ((curProgress == 0) || progress > curProgress) {
//                        curProgress = progress;
//                        mHandler.post(mRunnable);
//                    }
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    reDownLoad(e);
//                }
//
//                @Override
//                public void onComplete() {
//                    AppUtils.installApk(mContext,activity);
//                    successDownLoad();
//                }
//            };
//            mApkDownUtil.startDown(listener);

            showDownload();




            File filex;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                filex = new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),System.currentTimeMillis()+".apk");
            } else {
                filex = new File(activity.getFilesDir(),System.currentTimeMillis()+".apk");
            }
            Log.v("--->",filex.getPath()+""+downLoadUrl);
            RequestParams requestParams = new RequestParams(downLoadUrl);
            requestParams.setSaveFilePath(filex.getPath());
            x.http().get(requestParams, new Callback.ProgressCallback<File>() {
                @Override
                public void onSuccess(File result) {


                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    AppUtils.installApk(mContext,activity,filex);
                    successDownLoad();
                }

                @Override
                public void onWaiting() {

                }

                @Override
                public void onStarted() {

                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    int progress = (int) ((current * 100) / total);
                    if ((curProgress == 0) || progress > curProgress) {
                        curProgress = progress;
                        mHandler.post(mRunnable);
                    }
                }
            });








       // }
    }



    public void setProgress(int progress) {
        if (isShowing()) {
            if (progress > 0) {
                progressBar.setProgress(progress);
                tvUpdateContent.setText(String.format(Locale.CHINA, "当前进度：%d%%", progress));
            } else {
                progressBar.setProgress(0);
                tvUpdateContent.setText(String.format(Locale.CHINA, "当前进度：%d%%", 0));
            }
        }
    }

    /**
     * 点击确定后，修改ui
     * 1、不可以退出，2、button消失，3、title变为：正在下载，请勿退出！4、pb显示，message消失
     */
    private void showDownload() {
        mTvContentTitle.setText("正在下载");
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        mTvUpdateNow.setVisibility(View.GONE);
        mTvUpdateLater.setVisibility(View.GONE);
    }

    /**
     * 下载失败，显示原Ui
     * 1.pb进度为0且消失，message出现
     * 2、button出现
     */
    private void reDownLoad(Throwable e) {
        mTvContentTitle.setText("下载失败，重新下载");
        tvUpdateContent.setText(e.getMessage());
        progressBar.setVisibility(View.GONE);
        mTvUpdateNow.setVisibility(View.VISIBLE);
        mTvUpdateLater.setVisibility(View.VISIBLE);
    }

    private void successDownLoad() {
        mTvContentTitle.setText("下载成功");
        tvUpdateContent.setText("请安装最新版本!");
        progressBar.setVisibility(View.GONE);
        mTvUpdateNow.setText(GO_INSERT);
        mTvUpdateNow.setVisibility(View.VISIBLE);
        mTvUpdateLater.setVisibility(View.VISIBLE);
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = mContext.getResources().getDisplayMetrics().heightPixels;
        params.width = (int) (widthPixels * 0.8f);//设置布局属性占满宽度
        params.height = (int) (heightPixels * 0.6f);//设置布局属性适应高度
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;;//设置布局属性适应高度
        params.gravity = Gravity.CENTER;//设置dialog位于底部
        window.setAttributes(params);
    }
}
