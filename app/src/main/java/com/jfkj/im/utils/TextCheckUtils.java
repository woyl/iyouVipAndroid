package com.jfkj.im.utils;

import android.text.TextUtils;


/**
 * Created by Administrator on 2017/11/27.
 */

public class TextCheckUtils {
    public static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";

    static public boolean isPhoneNum(final String s) {
        return s.matches(REGEX_MOBILE_SIMPLE);
    }

    static public boolean isPassword(final String s) {
        return !TextUtils.isEmpty(s) && s.length() >= 6 && s.length() <= 20;
    }

    static public boolean isCode(final String s) {
        return !TextUtils.isEmpty(s) && s.length() == 6;
    }

    public static String convertPhoneNum(String phone) {


        return phone != null && phone.length() == 11 ? phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4, phone.length()) : phone;


    }

    public static String convertNickName(String name) {
        final int length = name == null ? 0 : name.length();
        final int start = 1;
        int endNum = length - start;
        if (endNum <= 0) {
            return "*";
        } else if (endNum < 2) {
            return name.substring(0, start) + "*";
        } else {
            final StringBuilder sb = new StringBuilder(length);
            sb.append(name.substring(0, start));

            for (int i = start; i < endNum; ++i) {
                sb.append('*');
            }
            return sb.append(name.substring(length - 1, length)).toString();
        }


    }


}
