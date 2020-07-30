package com.jfkj.im.download;

/**
 * <pre>
 * Description:
 * @author :   ys
 * @date :         2020/3/25
 * </pre>
 */
public interface DownLoadUrlListener {

    void onUpdate(long bytesRead, long contentLength, boolean done);

    void onError(Throwable e);

    void onComplete();
}
