package com.seventh.personalfinance;

import java.util.Calendar;

import com.seventh.base.BaseActivity;
import com.seventh.util.TimeUtil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;

public class AddRecordActivity extends BaseActivity {

	private EditText MoneySetting;
	private EditText TimeSetting;
	private EditText RemarkSetting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.activity_add_record);
		setHideleftButton("添加收入记录");
		setHideaddButton_right();
		
		initView();
		
		TimeSetting.setText(TimeUtil.GetTime());
		TimeSetting.setInputType(InputType.TYPE_NULL);
		TimeSetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AddRecordActivity.this, CalendarActivity.class);
				startActivity(intent);
			}
		});
	}

	public void initView(){
		MoneySetting = (EditText) findViewById(R.id.MoneySetting);
		TimeSetting = (EditText) findViewById(R.id.TimeSetting);
		RemarkSetting = (EditText) findViewById(R.id.RemarkSetting);
	}
}
