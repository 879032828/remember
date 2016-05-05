package com.seventh.personalfinance;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.seventh.db.Account;
import com.seventh.db.AccountDBdao;
import com.seventh.view.CornerListView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;



public class SpecificData extends Activity {
	private Intent intent = null;// 定义一个意图
	private String name;// 账号
	private String title;// 标题
	AccountDBdao accountDBdao;// 数据库

	private TextView mTextViewTime;// 标题

	private String time1;
	private String time2;
	private String time3;
	
	private CornerListView cornerListView = null;// 数据报表
	private List<Account> accounts;// 账单数据
	private LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_specific_data);

		intent = this.getIntent();
		name = intent.getStringExtra("name");// 接收主界面的数据
		title = intent.getStringExtra("title");// 接收主界面的数据

		// 设置标题
		mTextViewTime = (TextView) this
				.findViewById(R.id.tv_specific_data_txtDataRange);
		mTextViewTime.setText(title);

		accountDBdao = new AccountDBdao(getApplicationContext());

		// 时间
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")); // 获取东八区时间
		int year = c.get(Calendar.YEAR); // 获取年
		int month = c.get(Calendar.MONTH) + 1; // 获取月份，0表示1月份
		int day = c.get(Calendar.DAY_OF_MONTH); // 获取当前天数
		time1 = year + "/" + month + "/" + day;
		time2 = year + "/" + month + "%";
		time3 = year + "%";

		// 设置listview 值
		inflater = LayoutInflater.from(this);
		cornerListView = (CornerListView) findViewById(R.id.lv_specific_data_list);
		GetData();

		// 填充listview的数据
		cornerListView.setAdapter(new MyAdapter());
		// listview选项的点击事件
		cornerListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Account account = accounts.get(arg2);
				GoMoreAction(account.getId(), name);

			}
		});
	}

	private void GoMoreAction(int id, String name) {
		// TODO Auto-generated method stub
		intent = new Intent(this, MoreAction.class);
		intent.putExtra("name", name);
		intent.putExtra("id", id + "");
		intent.putExtra("title", title);
		startActivity(intent);
	}

	// 获取数据
	private void GetData() {
		try {
			if (title.equals("收入账单")) {
				accounts = accountDBdao.findTotalIntoByName(name);
			} else if (title.equals("支出账单")) {
				accounts = accountDBdao.findTotalOutByName(name);
			} else if (title.equals("详细账单")) {
				accounts = accountDBdao.findAllByName(name);
			} else if (title.equals("今日账单")) {
				accounts = accountDBdao.findSomeTimeByName(name, time1);
			} else if (title.equals("本月账单")) {
				accounts = accountDBdao.findSomeTimeByName(name, time2);
			} else if (title.equals("本年账单")) {
				accounts = accountDBdao.findSomeTimeByName(name, time3);
			}
		} catch (Exception e) {
			Toast.makeText(this, "获取数据失败", 0).show();
			e.printStackTrace();
		}
	}

	// listview适配器
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return accounts.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub

			return accounts.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = inflater.inflate(R.layout.specific_data_data, null);
			Account account = accounts.get(position);
			TextView tv_text1 = (TextView) view
					.findViewById(R.id.ls_sp_tv_time);
			TextView tv_text2 = (TextView) view
					.findViewById(R.id.ls_sp_tv_type);
			TextView tv_text3 = (TextView) view
					.findViewById(R.id.ls_sp_tv_money);
			tv_text1.setText("时间 :" + account.getTime());
			tv_text2.setText("类型: " + account.getType());
			tv_text3.setText("金额:" + account.getMoney() + "");
			return view;
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		GetData();

		// 填充listview的数据
		cornerListView.setAdapter(new MyAdapter());
	}

}
