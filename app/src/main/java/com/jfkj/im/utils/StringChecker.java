package com.jfkj.im.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.jfkj.im.R;


public class StringChecker {
    public static String checkPhone(String phone, Context context) {
        if (TextUtils.isEmpty(phone)) {
            return (context.getString(R.string.login_empty_phone));
        } else if (!TextCheckUtils.isPhoneNum(phone)) {
            return (context.getString(R.string.login_invalid_phone));
        }
        return null;

    }

    public static String checkPassword(String password, Context context) {
        if (TextUtils.isEmpty(password)) {
            return (context.getString(R.string.login_empty_password));


        } else if (!TextCheckUtils.isPassword(password)) {
            return (context.getString(R.string.login_invalid_password));

        }
        return null;
    }

    public static String checkPassword2(String password, Context context) {
        if (TextUtils.isEmpty(password)) {
            return (context.getString(R.string.login_empty_password2));


        } else if (!TextCheckUtils.isPassword(password)) {
            return (context.getString(R.string.login_invalid_password));

        }
        return null;
    }

    public static String checkCode(String code, Context context) {
        if (TextUtils.isEmpty(code)) {
            return (context.getString(R.string.login_empty_code));


        } else if (!TextCheckUtils.isCode(code)) {
            return (context.getString(R.string.login_invalid_code));

        }
        return null;
    }

    public static String checkInformation(Bitmap bitmap, String nickName, String birthday, String birthdayPlace, String residence, Context context) {
        if (bitmap == null)
            return "请添加头像";
        return checkInformation(nickName, birthday, birthdayPlace, residence, context);

    }

    public static String checkInformation(String nickName, String birthday, String birthdayPlace, String residence, Context context) {
        if (TextUtils.isEmpty(nickName))
            return "昵称不能为空";

        if (TextUtils.isEmpty(birthday))
            return "生辰不能为空";
        if (TextUtils.isEmpty(birthdayPlace))
            return "出生地不能为空";
        if (TextUtils.isEmpty(residence))
            return "居住地不能为空";
        return null;

    }

}
