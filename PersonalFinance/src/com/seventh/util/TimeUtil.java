package com.seventh.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {

	// ����ʱ��
	public static String GetTime() {
		String date1;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		date1 = formatter.format(curDate);
		String[] dataSplit = date1.split("-");

		return date1;
	}

	public static String Get_Year() {
		String date1;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		date1 = formatter.format(curDate);
		String[] dataSplit = date1.split("-");
		String year = dataSplit[0]; // ��ȡ��
		return year + "%";
	}
	
	public static String Get_Year_Month() {
		String date1;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		date1 = formatter.format(curDate);
		String[] dataSplit = date1.split("-");
		String year = dataSplit[0]; // ��ȡ��
		String month = dataSplit[1]; // ��ȡ�·ݣ�0��ʾ1�·�
		return year + "-" + month + "%";
	}
	
}
