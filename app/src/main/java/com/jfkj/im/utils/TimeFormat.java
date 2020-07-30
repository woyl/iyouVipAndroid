package com.jfkj.im.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Created by huazhao on 2017/8/8.
 */

public class TimeFormat {
    /**
     * 获取当前时间格式成某种格式
     *
     * @param format 时间格式
     * @return
     */
    public static String Time(String format) {
        long time = System.currentTimeMillis();
        //将long类型的时间转换成日历格式
        Date data = new Date(time);
        // 转换格式，年月日  星期  的格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(data);
    }

    /**
     * @param time   格式化的时间
     * @param format 时间格式
     * @return
     */
    public static String DateForMat(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date parse = sdf.parse(time);
            SimpleDateFormat sdf2 = new SimpleDateFormat(format);
            return sdf2.format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 计算和0点的时间差  String类型
     *
     * @param time
     * @return
     */
    public static float DateForMat(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        Date sDate = null;
        try {
            date = format.parse(time);
            sDate = getNeedTime(0, 0, 0, 0);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //9.499954166666667
        BigDecimal decimal = new BigDecimal((float) (date.getTime() - sDate.getTime()) / 1000 / 3600);
        //保留小数点后一位
        return (float) decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 计算和0点的时间差  Long 类型
     *
     * @param time
     * @return
     */
    public static float DateForMat(Long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date sDate = getNeedTime(0, 0, 0, 0);
        // 9.499954166666667
        BigDecimal decimal = new BigDecimal(
                (float) (time - sDate.getTime()) / 1000 / 3600);
        // 保留小数点后一位
        return (float) decimal.setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

//    public static float DateForMat(Long time) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        Date sDate = getNeedTime(0, 0, 0, 0);
//
//        //9.499954166666667
//        BigDecimal decimal = new BigDecimal((float) (time - sDate.getTime()) / 1000 / 3600);
//        //保留小数点后一位
//        return (float) decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//    }


    /**
     * 获得当前0点的时间
     *
     * @param hour
     * @param minute
     * @param second
     * @param addDay
     * @param args   getNeedTime(23,59,59,1);
     *               getNeedTime(0,0,0,1);
     * @return
     */
    private static Date getNeedTime(int hour, int minute, int second, int addDay,
                                    int... args) {
        Calendar calendar = Calendar.getInstance();
        if (addDay != 0) {
            calendar.add(Calendar.DATE, addDay);
        }
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        if (args.length == 1) {
            calendar.set(Calendar.MILLISECOND, args[0]);
        }
        return calendar.getTime();
    }

    /**
     * 将当时时间转化成毫秒数
     *
     * @param time
     * @return
     */
    public static long Times2MilliSecond(String time) {
        if (time != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                long millionSeconds = sdf.parse(time).getTime();//毫秒
                return millionSeconds;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;

    }

    /**
     * 将毫秒数转化为 format 格式
     *
     * @param time
     * @param format
     * @return
     */
    public static String TimeMillis2Time(long time, String format) {
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);

    }

    /**
     * 毫秒数转化成时间
     *
     * @param time
     * @return
     */
    public static String MilliSecond2Time(long time) {
        if (time > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date date = new Date();
            date.setTime(time);

        //    LogUtil.E(sdf.format(date));
            return sdf.format(date);

        }
        return null;
    }

    /**
     * 比较两个时间的大小
     *
     * @param time1
     * @param time2
     * @param format
     */
    public static boolean Time1CompareTime2(String time1, String time2, String format) {

        DateFormat df = new SimpleDateFormat(format);//创建日期转换对象HH:mm:ss为时分秒，年月日为yyyy-MM-dd
        try {
            Date dt1 = df.parse(time1);//将字符串转换为date类型
            Date dt2 = df.parse(time2);
            return dt1.getTime() > dt2.getTime() ? true : false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 和当前毫秒是对比
     *
     * @param time
     * @return
     */
    public static boolean Time1CompareTime2(long time) {
        Date dt2 = new Date(time);
        return new Date().getTime() > dt2.getTime() ? true : false;
    }

    /**
     * 和当前时间判断
     *
     * @param time
     * @param format
     * @return
     */
    public static boolean Time1CompareTime2(String time, String format) {

        DateFormat df = new SimpleDateFormat(format);//创建日期转换对象HH:mm:ss为时分秒，年月日为yyyy-MM-dd
        try {
            Date dt1 = df.parse(df.format(new Date()));//将字符串转换为date类型
            Date dt2 = df.parse(time);
            return dt1.getTime() < dt2.getTime() ? true : false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据时区获得时间,  在此之前要定位 ， 计算出你在哪个时区
     *
     * @param timeZoneOffset 0   格林尼治时间
     *                       +8   中国时间
     * @return
     */
    public static String getFormatedDateString(float timeZoneOffset) {
        if (timeZoneOffset > 13 || timeZoneOffset < -12) {
            timeZoneOffset = 0;
        }

        int newTime = (int) (timeZoneOffset * 60 * 60 * 1000);
        TimeZone timeZone;
        String[] ids = TimeZone.getAvailableIDs(newTime);
        if (ids.length == 0) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = new SimpleTimeZone(newTime, ids[0]);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(timeZone);
        return sdf.format(new Date());
    }
}
