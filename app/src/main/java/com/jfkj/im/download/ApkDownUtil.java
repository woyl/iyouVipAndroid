package com.jfkj.im.download;


import android.os.Environment;

import com.jfkj.im.App;
import com.jfkj.im.retrofit.ApiStores;
import com.jfkj.im.utils.FileUtil;

import java.io.File;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/3/25
 * </pre>
 */
public class ApkDownUtil {
    private String downUrl;

    public ApkDownUtil(String url){
        this.downUrl = url;
    }

    public void startDown(DownLoadUrlListener listener){

//        File outputFile = new File(FileUtil.APK_DIR, FileUtil.APK_NAME);




        File outputFile;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            outputFile = new File(App.getAppContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),FileUtil.APK_NAME);
        } else {
            outputFile = new File(App.getAppContext().getFilesDir(),FileUtil.APK_NAME);
        }


        if (outputFile.exists()) {
            outputFile.delete();
        }

        new DownloadManager(ApiStores.base_url, listener)
                .download(downUrl, outputFile, new Observer() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object value) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (outputFile.exists()) {
                            outputFile.delete();
                        }
                        listener.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        listener.onComplete();
                    }
                });
    }
}
