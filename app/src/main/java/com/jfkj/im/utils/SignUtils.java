package com.jfkj.im.utils;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SignUtils {
    static SignUtils signUtils;

    public SignUtils() {

    }

    public synchronized static SignUtils getInstance() {
        if (signUtils == null) {
            signUtils = new SignUtils();
        }
        return signUtils;
    }

    public String getSignToken(Map<String, String> map) {
        String result = "";
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (item.getKey() != null || item.getKey() != "") {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (!(val == "" || val == null)) {
                        sb.append(key + "=" + val + "&");
                    }
                }
            }
            result = sb.toString() + "key=3ae50f0fc8475cc11ea3c1c0c74d7f73";

            result = getMD5Value(result);
        } catch (Exception e) {
            return null;
        }

        return result;
    }
    public String getSignToken() {
        return getMD5Value("3ae50f0fc8475cc11ea3c1c0c74d7f73"+TimeUtil.getInstance().gettime());
    }
    public String getMD5Value(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

}
