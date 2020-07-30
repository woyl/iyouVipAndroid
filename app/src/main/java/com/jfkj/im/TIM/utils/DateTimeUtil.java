package com.jfkj.im.TIM.utils;

import java.util.Calendar;
import java.util.Date;


public class DateTimeUtil {

    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * 返回文字描述的日期
     *
     * @param date
     * @return
     */
    public static String getTimeFormatText(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();

        int currentDayIndex = calendar.get(Calendar.DAY_OF_YEAR);
        int currentYear = calendar.get(Calendar.YEAR);

        calendar.setTime(date);
        int msgYear = calendar.get(Calendar.YEAR);
        int msgDayIndex = calendar.get(Calendar.DAY_OF_YEAR);
        int msgMinute = calendar.get(Calendar.MINUTE);

        String msgTimeStr = calendar.get(Calendar.HOUR_OF_DAY) + ":";

        if (msgMinute < 10) {
            msgTimeStr = msgTimeStr + "0" + msgMinute;
        } else {
            msgTimeStr = msgTimeStr + msgMinute;
        }

        int msgDayInWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (currentDayIndex == msgDayIndex) {
            return msgTimeStr;
        } else {
            if (currentDayIndex - msgDayIndex == 1 && currentYear == msgYear) {
                msgTimeStr = "昨天 " + msgTimeStr;
            } else if (currentDayIndex - msgDayIndex > 1 && currentYear == msgYear) { //本年消息
                //不同周显示具体月，日，注意函数：calendar.get(Calendar.MONTH) 一月对应0，十二月对应11
                msgTimeStr = (Integer.valueOf(calendar.get(Calendar.MONTH) + 1)) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日 " + msgTimeStr + " ";
            } else { // 1、非正常时间，如currentYear < msgYear，或者currentDayIndex < msgDayIndex
                //2、非本年消息（currentYear > msgYear），如：历史消息是2018，今年是2019，显示年、月、日
                msgTimeStr = msgYear + "年" + (Integer.valueOf(calendar.get(Calendar.MONTH) + 1)) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日" + msgTimeStr + " ";
            }
        }
        return msgTimeStr;
    }

    public static String formatSeconds(long seconds) {
        String timeStr = seconds + "秒";
        if (seconds > 60) {
            long second = seconds % 60;
            long min = seconds / 60;
            timeStr = min + "分" + second + "秒";
            if (min > 60) {
                min = (seconds / 60) % 60;
                long hour = (seconds / 60) / 60;
                timeStr = hour + "小时" + min + "分" + second + "秒";
                if (hour % 24 == 0) {
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day + "天";
                } else if (hour > 24) {
                    hour = ((seconds / 60) / 60) % 24;
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day + "天" + hour + "小时" + min + "分" + second + "秒";
                }
            }
        }
        return timeStr;
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

}
