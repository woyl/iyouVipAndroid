package com.jfkj.im.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.jfkj.im.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;

import okio.Okio;

public class FileUtil {

    public static final String ROOT_DIR = getStoragePath() + "iyou/";
    public static final String CACHE_DIR = ROOT_DIR + "cache/";// 缓存信息
    public static final String CRASH_DIR = ROOT_DIR + "crash/";// 崩溃日志
    public static final String HEAD_DIR = ROOT_DIR + "head/";// 头像
    public static final String THUMB = ROOT_DIR + "thumb/";//缩略图
    public static final String APK_DIR = ROOT_DIR + "apk/";//更新包
    public static final String APK_NAME = "iyou.apk";//更新包

    public static final String[] DIRS = {ROOT_DIR, CACHE_DIR,CRASH_DIR,HEAD_DIR};
    private static final String[] cacheDir = {CACHE_DIR,CRASH_DIR,HEAD_DIR};

    /**
     * 根据URI获取文件真实路径（兼容多张机型）
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePathByUri(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {

            int sdkVersion = Build.VERSION.SDK_INT;
            if (sdkVersion >= 19) { // api >= 19
                return getRealPathFromUriAboveApi19(context, uri);
            } else { // api < 19
                return getRealPathFromUriBelowAPI19(context, uri);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String type = documentId.split(":")[0];
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};

                //
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                filePath = getDataColumn(context, contentUri, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                filePath = getDataColumn(context, contentUri, null, null);
            }else if (isExternalStorageDocument(uri)) {
                // ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    filePath = Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }else {
                //Log.e("路径错误");
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            filePath = getDataColumn(context, uri, null, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            filePath = uri.getPath();
        }
        return filePath;
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * 创建文件
     *
     * @param path
     * @return
     */
    public static File createFile(String path) {
        if (path == null) {
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return file;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     * @return
     */
    public static void createSDDir(String dirName) {
        File file = new File(dirName);
        if (!file.exists()) {
            boolean state = file.mkdir();
        }
    }

//    /**
//     * 创建项目文件目录
//     */
//    public static void createAppDir() {
//        for (int i = 0; i < DIRS.length; i++) {
//            String dir = DIRS[i];
//            createSDDir(dir);
//        }
//    }

    /**
     * 获取缓存文件的大小
     *
     * @return
     */
    public static String getCacheSize() {
        double size = 0;
        for (int i = 0; i < cacheDir.length; i++) {
            File file = new File(cacheDir[i]);
            size += getDirSize(file);
        }

        DecimalFormat df = new DecimalFormat("#.##");

        return df.format(size) + "M";
    }

    /**
     * 获取文件夹大小
     *
     * @param file
     * @return
     */
    public static double getDirSize(File file) {
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] childList = file.listFiles();
                double size = 0;
                for (File f : childList) {
                    size += getDirSize(f);
                }
                return size;
            } else {
                //如果是文件则直接返回其大小,以“兆”为单位
                double size = (double) file.length() / 1024 / 1024;
                return size;
            }
        }

        return 0;
    }


    /**
     * 删除缓存文件
     *
     * @param path
     */
    private static void delCacheFile(String path) {
        try {
            File file = new File(path);
            if (file == null || !file.exists()) {
                return;
            }

            if (file.isFile()) {
                file.delete();
            } else {
                File[] fileList = file.listFiles();
                if (fileList == null || fileList.length <= 0) {
                    return;
                }

                int listSize = fileList.length;
                for (int i = 0; i < listSize; i++) {
                    File childFile = fileList[i];

                    if (childFile.isFile()) {
                        childFile.delete();
                    } else {
                        delCacheFile(childFile.getAbsolutePath());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存bitmap
     *
     * @param bitmap
     * @param path
     * @return
     */
    public static boolean saveBitmap2File(Bitmap bitmap, String path) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap.compress(format, quality, stream);
    }

    /**
     * 得到SDCard的路径
     *
     * @return
     */
    public static String getStoragePath() {
        /* 检测是否存在SD卡 */
        File filePath = null;
        String exStorageState = Environment.getExternalStorageState();
        if (exStorageState != null && Environment.MEDIA_SHARED.equals(exStorageState)) {
            // 有SD卡，手机直接连接到电脑作为u盘使用之后的状态
            return null;
        }

        if (exStorageState != null && Environment.MEDIA_MOUNTED.equals(exStorageState)) {
            // sd卡在手机上正常使用状态
            // 是否存在外存储器(优先判断)
            filePath = Environment.getExternalStorageDirectory();
        } else if (isExsitInternalStorage()) {
            // 如果外存储器不存在，则判断内存储器
            filePath = App.getAppContext().getFilesDir();
        }

        if (filePath != null) {
            //            return filePath.getPath() + "/";
            return filePath.getAbsolutePath() + "/";
        } else {
            return null;
        }
    }

    // 是否存在内存储器
    private static boolean isExsitInternalStorage() {
        File file = App.getAppContext().getFilesDir();

        if (file != null) {
            return true;
        }

        return false;
    }

    /**
     * 判断SD卡上的文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (filePath == null) {
            return false;
        }

        File file = new File(filePath);
        return file.exists();
    }




    public static String readAssetsFileToString(Context context, String name) {
        StringBuilder result = new StringBuilder();
        String temp;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = context.getAssets().open(name);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while (bufferedReader != null && null != (temp = bufferedReader.readLine())) {
                result.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                    bufferedReader = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    public File getFile(Bitmap bmp) {
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        //getApplicationContext 更改为 getContext
        String defaultPath = App.getAppContext().getFilesDir()
                .getAbsolutePath() + "/defaultGoodInfo";
        File file = new File(defaultPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String defaultImgPath = defaultPath + "/messageImg.jpg";
        file = new File(defaultImgPath);
        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 20, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static boolean save(File src, File target) throws IOException {

        if (!target.getParentFile().exists()) {
            target.getParentFile().mkdirs();
        }

        long count = Okio.buffer(Okio.sink(target))
                .writeAll(Okio.source(src));
        Log.d("hhhhh", "save: " + count);
        return count > 0;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
}