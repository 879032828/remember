package com.seventh.personalfinance;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class CalendarActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);// »•µÙ±ÍÃ‚
		setContentView(R.layout.activity_login);
		
		setContentView(R.layout.activity_calendar);
		
		
	}

	
}
