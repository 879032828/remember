package com.seventh.personalfinance;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.seventh.base.BaseActivity;
import com.seventh.db.AccountDBdao;
import com.seventh.db.PersonDBdao;
import com.seventh.view.CornerListView;
import com.seventh.view.DataRange;
import com.seventh.view.MainActivityService;
import com.seventh.view.PieChart;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.seventh.util.*;

public class MainActivity extends BaseActivity implements OnClickListener, OnTouchListener {
	private Intent intent = null;// 定义一个意图
	private String name;// 账号
	private String pwd1;
	private String pwd2;
	AccountDBdao accountDBdao;// 数据库
	PersonDBdao persondbdao;

	private Button mButtonAddNodes;// 记一笔按钮

	private float totalOut;// 总支出
	private float totalInto;// 总收入

	private ListView listView1;
	private List<Map<String, String>> map_list1 = null;

	private CornerListView cornerListView2 = null;// 自定义listview2
	private List<DataRange> dataRanges;
	private LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.activity_mainactivity);
		
		setTitle("记着");
		setHideaddButton_left();
		setHidesetting();
		
		initView();
		ViewOperation();
	}

	public void ViewOperation() {
		intent = this.getIntent();
		name = intent.getStringExtra("name");// 接收登录界面的数据
		if (name == null) {
			intent = new Intent(this, Login.class);
			startActivity(intent);
			finish();
		} else {
			accountDBdao = new AccountDBdao(getApplicationContext());
			totalOut = accountDBdao.fillTotalOut(name);// 总支出
			totalInto = accountDBdao.fillTotalInto(name);// 总收入

			// 设置listview1 值
			map_list1 = MainActivityService.getDataSource1(totalInto, totalOut);
			// listview1适配器
			SimpleAdapter adapter1 = new SimpleAdapter(getApplicationContext(), map_list1,
					R.layout.main_listview_calculation, new String[] { "txtCalculationName", "txtMoney" },
					new int[] { R.id.ls_tv_txtCalculationName, R.id.ls_tv_txtMoney });
			// 填充listview1的数据
			listView1.setAdapter(adapter1);

			// listview1选项的点击事件 收入总额 支出总额 预算余额
			listView1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					switch (arg2) {
					case 0:
						TotalIntoData();
						break;
					case 1:
						TotalOutData();
						break;
					case 2:
						TotalAllData();
						break;
					}

				}
			});

			// // 设置listview2 值
			// inflater = LayoutInflater.from(this);
			// cornerListView2 = (CornerListView)
			// findViewById(R.id.lv_main_datareport);
			// try {
			// dataRanges = MainActivityService.getDataSource2(name,
			// getApplicationContext());
			// } catch (Exception e) {
			// Toast.makeText(this, "获取数据失败", 0).show();
			// e.printStackTrace();
			// }
			// // 填充listview2的数据
			// cornerListView2.setAdapter(new MyAdapter());
			//
			// // listview2选项的点击事件 一览表
			// cornerListView2.setOnItemClickListener(new OnItemClickListener()
			// {
			//
			// @Override
			// public void onItemClick(AdapterView<?> arg0, View arg1,
			// int arg2, long arg3) {
			// switch (arg2) {
			// case 0:
			// TodayData();
			// break;
			// case 1:
			// MonthData();
			// break;
			// case 2:
			// YearData();
			// break;
			// }
			// }
			// });

		}
	}

	public void initView() {
		listView1 = (ListView) findViewById(R.id.lv_main_calculation);// 总额显示
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_main_addnotes:
			intent = new Intent(this, AddNodes.class);
			intent.putExtra("name", name);
			// 传值 帐户名
			startActivity(intent);
			break;
		}
	}

	// 跳转到收入账单
	public void TotalIntoData() {
		intent = new Intent(this, SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "收入总额");
		startActivity(intent);
	}

	// 跳转到支出账单
	public void TotalOutData() {
		intent = new Intent(this, SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "支出总额");
		startActivity(intent);
	}

	// 跳转到详细账单
	public void TotalAllData() {
		intent = new Intent(this, SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "详细账单");
		startActivity(intent);
	}

	// 跳转到今日账单
	public void TodayData() {
		intent = new Intent(this, SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "今日账单");
		startActivity(intent);
	}

	// 跳转到本月账单
	public void MonthData() {
		intent = new Intent(this, SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "本月账单");
		startActivity(intent);
	}

	// 跳转到本年账单
	public void YearData() {
		intent = new Intent(this, SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "本年账单");
		startActivity(intent);
	}

	// 设置时间
//	public String GetTime() {
//		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")); // 获取东八区时间
//		int year = c.get(Calendar.YEAR); // 获取年
//		int month = c.get(Calendar.MONTH) + 1; // 获取月份，0表示1月份
//		int day = c.get(Calendar.DAY_OF_MONTH); // 获取当前天数
//		String time = year + "/" + month + "/" + day;
//		return time;
//	}

	// listview2适配器
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataRanges.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return dataRanges.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = inflater.inflate(R.layout.main_listview_datareport, null);
			DataRange aboutBillData = dataRanges.get(position);

			TextView tv2_text1 = (TextView) view.findViewById(R.id.ls_tv2_txtDataRange);
			TextView tv2_text2 = (TextView) view.findViewById(R.id.ls_tv2_txtInto);
			TextView tv2_text3 = (TextView) view.findViewById(R.id.ls_tv2_txtOut);
			tv2_text1.setText(aboutBillData.getText1());
			tv2_text2.setText(aboutBillData.getText2());
			tv2_text3.setText(aboutBillData.getText3());

			return view;
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		totalOut = accountDBdao.fillTotalOut(name);// 总支出
		totalInto = accountDBdao.fillTotalInto(name);// 总收入

		map_list1 = MainActivityService.getDataSource1(totalInto, totalOut);
		// listview1适配器
		SimpleAdapter adapter1 = new SimpleAdapter(getApplicationContext(), map_list1,
				R.layout.main_listview_calculation, new String[] { "txtCalculationName", "txtMoney" },
				new int[] { R.id.ls_tv_txtCalculationName, R.id.ls_tv_txtMoney });
		// 填充listview1的数据
		listView1.setAdapter(adapter1);

		try {
			dataRanges = MainActivityService.getDataSource2(name, getApplicationContext());
		} catch (Exception e) {
			Toast.makeText(this, "获取数据失败", 0).show();
			e.printStackTrace();
		}
		// // 填充listview2的数据
		// cornerListView2.setAdapter(new MyAdapter());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
