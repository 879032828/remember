package com.seventh.util;

import java.util.Calendar;
import java.util.TimeZone;

public class TimeUtil {

	// ����ʱ��
		public static String GetTime() {
			Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")); // ��ȡ������ʱ��
			int year = c.get(Calendar.YEAR); // ��ȡ��
			int month = c.get(Calendar.MONTH) + 1; // ��ȡ�·ݣ�0��ʾ1�·�
			int day = c.get(Calendar.DAY_OF_MONTH); // ��ȡ��ǰ����
			String time = year + "/" + month + "/" + day;
			return time;
		}
	
}
