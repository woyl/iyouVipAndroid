package com.jfkj.im.utils;

import android.annotation.SuppressLint;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * @author Administrator
 * 时间格式化
 */
public class TimeChange {
	/**
	 * 转换刷新时间格式
	 */
	public static String getFormatTime(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(time);

	}

	public static String getFormatTime(long time,String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(time);
	}

	/***
	 * 获取当前时分秒
	 * */
	public static String getSeconds() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String dateString = sdf.format(System.currentTimeMillis());
		return dateString;
	}

	/***
	 * 获取当前天
	 * */
	public static String getCurrDay() {
		@SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd");
		return sdf.format(System.currentTimeMillis());
	}




	/**
	 *将秒转化为年月日
	 * */
	private static SimpleDateFormat sdf=null;

	public static String formatDateBySeconds(long time){
		return formerlytime(time);
	}

	public static String formatTimeBySeconds(long time){
		if(null==sdf)
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(time*1000);
	}

	public static String formatDateBySecondAll(long time){
		if(null==sdf)
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(time*1000);
	}
    public static String formatTime(long time,String format){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(time);
	}

	// 设置时间
	public static String setTime(long total) {
		long hour = total / (1000 * 60 * 60);
		long letf1 = total % (1000 * 60 * 60);
		long minute = letf1 / (1000 * 60);
		long left2 = letf1 % (1000 * 60);
		long second = left2 / 1000;
		String h=hour<10?"0"+hour:hour+"";
		String m=minute<10?"0"+minute:minute+"";
		String s=second<10?"0"+second:second+"";
		return  h+ "'" + m + "'" + s + "''";
	}

	/**
	 * 格式化播放时间
	 * 
	 * @param mss
	 *            单位为毫秒值的时间
	 * 
	 */
	public static String formatDuring(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60) + days * 24;
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		// 将时间转化为字符串
		String HH = (hours > 0) ? String.valueOf(hours) : "00";
		String mm = (minutes > 0) ? String.valueOf(minutes) : "00";
		String ss = (seconds > 0) ? String.valueOf(seconds) : "00";
		// 如单位时间小于10就在数字前面添加一个0
		HH = (HH.length() == 1) ? ("0" + HH) : (HH);
		mm = (mm.length() == 1) ? ("0" + mm) : (mm);
		ss = (ss.length() == 1) ? ("0" + ss) : (ss);
		return mm + " : " + ss;
	}


	/**
	 * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytes2kb(long bytes) {
		BigDecimal filesize = new BigDecimal(bytes);
		BigDecimal megabyte = new BigDecimal(1024 * 1024);
		float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
		if (returnValue > 1)
			return (returnValue + "MB");
		BigDecimal kilobyte = new BigDecimal(1024);
		returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
		return (returnValue + "KB");
	}

	public static String getTime(int tm) {
		int h = tm / 3600;
		int m = tm / 60;
		int s = tm % 60;
		if (h > 0)
			return String.format("%02d:%02d:%02d", h, m, s);
		else if (m > 0)
			return String.format("%02d:%02d", m, s);
		return String.format("%02d", s);
	}

	/**
	 * 计算给定的时间过去了多久
	 * @param time
	 * @return
	 */
	public static String formerlytime(long time) {
		long currentTime = System.currentTimeMillis();
		long timeBad = currentTime - time*1000;
		String result ="";
		if (timeBad >= 0) {
			long ss = timeBad / 1000;//秒
			long mm = ss / 60;//分钟
			long hh = mm / 60;//小时
			long dd = hh / 24;//天数
			long MM = dd / 30;//月数
			long yy = MM / 12;//年数
			if (yy > 0) {
				result = formatTimeBySeconds(time);
			} else if (MM > 0) {
				result = formatTimeBySeconds(time);
			} else if (dd > 0) {
				result = dd + "天前";
			} else if (hh > 0) {
				result = hh + "小时前";
			} else if (mm > 0) {
				result = mm + "分钟前";
			} else if (ss >= 0) {
				result = "刚刚";
			} else {
				result = "时间错误";
			}
			return result;
		} else {
			return null;
		}
	}

}
