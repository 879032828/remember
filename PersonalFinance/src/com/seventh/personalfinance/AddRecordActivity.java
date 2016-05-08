package com.seventh.personalfinance;

import com.seventh.base.BaseActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AddRecordActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.activity_add_record);
		setHideleftButton("添加收入记录");
		setHideaddButton_right();
	}

	
}
