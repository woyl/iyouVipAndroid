package com.jfkj.im.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;

public class TimeUtil {
    static  TimeUtil timeUtil;
    private static SimpleDateFormat format;

    public static TimeUtil getInstance(){
        if(timeUtil==null){
            timeUtil=new TimeUtil();
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        return  timeUtil;
    }


    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * 返回文字描述的日期
     *
     * @param
     * @return
     */
    public   String getTimeFormatText( String time) throws ParseException {

        Date date =        format.parse(time);

        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }



   public String gettime(){
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
       Date date = new Date(System.currentTimeMillis());
       return simpleDateFormat.format(date);
   }

    public static String stringForTime(long timeMs){
        long totalSeconds = timeMs/1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds/60)%60;
        long hours = totalSeconds/3600;
        return new Formatter().format("%02d:%02d:%02d",hours,minutes,seconds).toString();
    }
}
