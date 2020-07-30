package com.jfkj.im.retrofit;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 主要实现文件上传，和接收方绑定接收后信息导入参数传递
 * @author zyb
 *
 */
public class HttpURLConnectionServices {
    /**
     * @param fileName 要上传的文件，列：e:/upload/SSD4k对齐分区.zip

     * @param strSiteID 对方的站点编号
     * @param strColumnID 对方的栏目编号
     * @param strDespatcher 发送信息人
     * @param strMechanism 发送信息机构
     * @param strOther1
     */
    public static void post(String fileName ,String uploadurl,String strSiteID,String strColumnID, String strDespatcher,String strMechanism,String strOther1) {
        try {

            File file = new File(fileName);
            URL url = new URL(uploadurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setChunkedStreamingMode(1024 * 1024);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setConnectTimeout(50000);
            conn.setRequestProperty("Content-Type", "multipart/form-data;file="+ file.getName());
            conn.setRequestProperty("fileName",file.getName());
            conn.setRequestProperty("strSiteID", strSiteID);
            conn.setRequestProperty("strColumnID", strColumnID);
            conn.setRequestProperty("strDespatcher", strDespatcher);
            conn.setRequestProperty("strMechanism", strMechanism);
            conn.setRequestProperty("strOther1", strOther1);
            conn.connect();
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[2048];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();

            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            new File(fileName).delete();
        }
    }

}