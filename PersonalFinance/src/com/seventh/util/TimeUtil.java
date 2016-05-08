package com.seventh.util;

import java.util.Calendar;
import java.util.TimeZone;

public class TimeUtil {

	// 设置时间
		public static String GetTime() {
			Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")); // 获取东八区时间
			int year = c.get(Calendar.YEAR); // 获取年
			int month = c.get(Calendar.MONTH) + 1; // 获取月份，0表示1月份
			int day = c.get(Calendar.DAY_OF_MONTH); // 获取当前天数
			String time = year + "/" + month + "/" + day;
			return time;
		}
	
}
