package com.jfkj.im.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DecimalFormatUtils {

    /**保留两位四舍五入*/
    public static String doubleRounding(double num){
        DecimalFormat df = new DecimalFormat("#.0");
        return df.format(num);
    }

    /**保留两位不四舍五入*/
    public static String doubleNoRounding(double num){
        DecimalFormat df = new DecimalFormat("#####0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(num);
    }

    /**Android实现金额显示小数点后两位*/
    public static String doubleNoRounding1(double num){
        DecimalFormat df = new DecimalFormat("##.##");
        return df.format(num);
    }
    /**金钱数字保留4位小数且以“￥”开头*/
    public static String doubleNoRounding2(double num){
        DecimalFormat df = new DecimalFormat("$##.####");
        return df.format(num);
    }
    /**金钱数字保留4位小数且三位三位的隔开*/
    public static String doubleNoRounding3(double num){
        DecimalFormat df = new DecimalFormat("#,###.####0");
        return df.format(num);
    }

}
