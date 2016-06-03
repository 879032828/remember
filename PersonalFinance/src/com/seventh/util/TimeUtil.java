package com.seventh.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {

	// 设置时间
	public static String GetTime() {
		String date1;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		date1 = formatter.format(curDate);
		String[] dataSplit = date1.split("-");

		return date1;
	}

	public static String Get_Year() {
		String date1;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		date1 = formatter.format(curDate);
		String[] dataSplit = date1.split("-");
		String year = dataSplit[0]; // 获取年
		return year + "%";
	}
	
	public static String Get_Year_Month() {
		String date1;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		date1 = formatter.format(curDate);
		String[] dataSplit = date1.split("-");
		String year = dataSplit[0]; // 获取年
		String month = dataSplit[1]; // 获取月份，0表示1月份
		return year + "-" + month + "%";
	}
	
}
