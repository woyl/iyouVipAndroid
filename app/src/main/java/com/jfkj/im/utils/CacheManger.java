package com.jfkj.im.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.math.BigDecimal;

/**
 * <pre>
 * Description:  缓存管理工具类
 * @author :   ys
 * @date :         2020/1/16
 * </pre>
 */
public class CacheManger {
    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache)
     *
     * @param context
     */
    public static void clearInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     *
     * @param context
     */
    public static void clearExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 删除某个文件夹下的文件
     * @param directory
     */
    private static void deleteFilesByDirectory(File directory) {

        if (directory.isFile()) {
            directory.delete();
            return;
        }
        if (directory.isDirectory()) {
            File[] childFile = directory.listFiles();
            if (childFile == null || childFile.length == 0) {
                directory.delete();
                return;
            }
            for (File f : childFile) {
                deleteFilesByDirectory(f);
            }
        }
    }

    /**
     * 清除应用的缓存数据并返回当前缓存大小
     *
     * @param context
     * @return 缓存大小
     */
    public static String clearCacheSize(Context context) throws Exception {
        clearInternalCache(context);
        clearExternalCache(context);
        return getCacheSize(context);
    }

    /**
     * 获取应用缓存大小
     *
     * @return 缓存大小
     */
    public static String getCacheSize(Context context) throws Exception {
        long internalCacheSize = getFolderSize(context.getCacheDir());
        long externalCacheSize = 0;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            externalCacheSize = getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(internalCacheSize + externalCacheSize);
    }

    /**
     * 获取文件大小
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化缓存单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            String result = size + "B";
            if ("0.0B".equals(result)) {
                result = "0KB";
            }
            return result;
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }

        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }
}
