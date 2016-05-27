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

	private String date = null;// ����Ĭ��ѡ�е����� ��ʽΪ ��2014-04-05�� ��׼DATE��ʽ
	private TextView popupwindow_calendar_month;
	private SignCalendar calendar;
	private ImageButton upMonth, downMonth;

	private List<String> list = new ArrayList<String>(); // ���ñ���б�
	boolean isinput = false;
	private String date1 = null;// ��������
	
	//��Ӧ��
	private static final int Calendar_resultCode = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ������

		setContentView(R.layout.activity_calendar);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		date1 = formatter.format(curDate);

		initView();

		popupwindow_calendar_month.setText(calendar.getCalendarYear() + "��" + calendar.getCalendarMonth() + "��");
		if (null != date) {
			int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
			int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
			popupwindow_calendar_month.setText(years + "��" + month + "��");

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

		// ������ѡ�е�����
		calendar.setOnCalendarClickListener(new OnCalendarClickListener() {

			public void onCalendarClick(int row, int col, String dateFormat) {
				int month = Integer
						.parseInt(dateFormat.substring(dateFormat.indexOf("-") + 1, dateFormat.lastIndexOf("-")));

				if (calendar.getCalendarMonth() - month == 1// ������ת
						|| calendar.getCalendarMonth() - month == -11) {
					calendar.lastMonth();

				} else if (month - calendar.getCalendarMonth() == 1 // ������ת
						|| month - calendar.getCalendarMonth() == -11) {
					calendar.nextMonth();

				} else {
					calendar.setCalendarDayBgColor(dateFormat, R.drawable.calendar_date_focused);
					date = dateFormat;// ��󷵻ظ�ȫ�� date
					Intent intent = new Intent(CalendarActivity.this, AddRecordActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("time", date);
					intent.putExtra("Calendar", bundle);
					CalendarActivity.this.setResult(Calendar_resultCode, intent);
					finish();
				}
			}
		});

		// ������ǰ�·�
		calendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
			public void onCalendarDateChanged(int year, int month) {
				popupwindow_calendar_month.setText(year + "��" + month + "��");
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
