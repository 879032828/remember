package com.seventh.personalfinance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.seventh.view.SignCalendar;
import com.seventh.view.SignCalendar.OnCalendarClickListener;
import com.seventh.view.SignCalendar.OnCalendarDateChangedListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class CalendarActivity extends Activity {

	private String date = null;// 设置默认选中的日期 格式为 “2014-04-05” 标准DATE格式
	private TextView popupwindow_calendar_month;
	private SignCalendar calendar;
	private ImageButton upMonth, downMonth;

	private List<String> list = new ArrayList<String>(); // 设置标记列表
	boolean isinput = false;
	private String date1 = null;// 单天日期
	
	//响应码
	private static final int Calendar_resultCode = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题

		setContentView(R.layout.activity_calendar);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		date1 = formatter.format(curDate);

		initView();

		popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年" + calendar.getCalendarMonth() + "月");
		if (null != date) {
			int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
			int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
			popupwindow_calendar_month.setText(years + "年" + month + "月");

			calendar.showCalendar(years, month);
			calendar.setCalendarDayBgColor(date, R.drawable.calendar_date_focused);
		}

		upMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				calendar.lastMonth();
			}
		});
		downMonth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				calendar.nextMonth();
			}
		});

		// 监听所选中的日期
		calendar.setOnCalendarClickListener(new OnCalendarClickListener() {

			public void onCalendarClick(int row, int col, String dateFormat) {
				int month = Integer
						.parseInt(dateFormat.substring(dateFormat.indexOf("-") + 1, dateFormat.lastIndexOf("-")));

				if (calendar.getCalendarMonth() - month == 1// 跨年跳转
						|| calendar.getCalendarMonth() - month == -11) {
					calendar.lastMonth();

				} else if (month - calendar.getCalendarMonth() == 1 // 跨年跳转
						|| month - calendar.getCalendarMonth() == -11) {
					calendar.nextMonth();

				} else {
					calendar.setCalendarDayBgColor(dateFormat, R.drawable.calendar_date_focused);
					date = dateFormat;// 最后返回给全局 date
					Intent intent = new Intent(CalendarActivity.this, AddRecordActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("time", date);
					intent.putExtra("Calendar", bundle);
					CalendarActivity.this.setResult(Calendar_resultCode, intent);
					finish();
				}
			}
		});

		// 监听当前月份
		calendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
			public void onCalendarDateChanged(int year, int month) {
				popupwindow_calendar_month.setText(year + "年" + month + "月");
			}
		});
	}

	public void initView() {
		popupwindow_calendar_month = (TextView) findViewById(R.id.popupwindow_calendar_month);
		calendar = (SignCalendar) findViewById(R.id.popupwindow_calendar);
		upMonth = (ImageButton) findViewById(R.id.upMonth);
		downMonth = (ImageButton) findViewById(R.id.downMonth);
	}

}
