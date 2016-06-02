package com.seventh.personalfinance;

import java.util.List;
import java.util.Map;

import com.seventh.base.BaseActivity;
import com.seventh.db.AccountDBdao;
import com.seventh.db.BudgetDBdao;
import com.seventh.db.PersonDBdao;
import com.seventh.view.CornerListView;
import com.seventh.view.DataRange;
import com.seventh.view.MainActivityService;

import android.R.integer;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	private Intent intent = null;// 定义一个意图
	private String name;// 账号
	private String pwd1;
	private String pwd2;
	AccountDBdao accountDBdao;// 数据库
	PersonDBdao persondbdao;

	private float totalOut;// 总支出
	private float totalInto;// 总收入

	private ListView listView1;
	private List<Map<String, String>> map_list1 = null;

	private CornerListView cornerListView2 = null;// 自定义listview2
	private List<DataRange> dataRanges;
	private LayoutInflater inflater;
	private ImageView ImageView_main;
	private EditText dialog_input_edittext;
	private TextView dialog_input_text;
	private Button dialog_input_sure;
	private Button dialog_input_cannle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.activity_mainactivity);

		ImageView_main = (ImageView) findViewById(R.id.ImageView_main);
		// 在控件完成后，执行SettingView操作更新控件的高度
		ImageView_main.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				SettingView();
			}
		});

		setTitle("记着");
		// 设置两个按钮可见及背景
		setHideaddButton_left();
		setBackgroudButton_left(R.drawable.shape_bg_add_button);
		setHideaddButton_right();
		setBackgroudButton_right(R.drawable.selector_bg_setting_button);

		initView();
		ViewOperation();

	}

	/**
	 * 获取状态栏和标题栏的高度，并设置ImageView的高度
	 */
	public void SettingView() {
		Rect rect = new Rect();
		Window window = getWindow();
		ImageView_main.getWindowVisibleDisplayFrame(rect);
		// 状态栏的高度
		int statusBarHeight = rect.top;
		// 标题栏的高度
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_relative);
		int contentViewTop = relativeLayout.getHeight();

		// 设置控件的高度
		WindowManager windowManager = this.getWindowManager();
		int height = windowManager.getDefaultDisplay().getHeight();
		LayoutParams para = ImageView_main.getLayoutParams();
		para.height = height / 2 - statusBarHeight - contentViewTop;
		para.width = ImageView_main.getLayoutParams().width;
		ImageView_main.setLayoutParams(para);
	}

	/**
	 * 
	 */
	public void ViewOperation() {
		intent = this.getIntent();
		name = intent.getStringExtra("name");// 接收其他界面的Intent中的name值
		if (name == null) {
			intent = new Intent(this, Activity_Login.class);
			startActivity(intent);
			finish();
		} else {
			accountDBdao = new AccountDBdao(getApplicationContext());
			totalOut = accountDBdao.fillTotalOut(name);// 获取总支出
			totalInto = accountDBdao.fillTotalInto(name);// 获取总收入

			// 设置listview1 值
			map_list1 = MainActivityService.getDataSource1(totalInto, totalOut, name, this);
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
						// 设置预算余额
						BudgetBalance();
						break;
					case 1:
						// 收入总额
						TotalIntoData();
						break;
					case 2:
						// 支出总额
						TotalOutData();
						break;
					}

				}
			});
		}
	}

	/**
	 * 初始化界面
	 */
	public void initView() {
		listView1 = (ListView) findViewById(R.id.lv_main_calculation);// 总额显示

	}

	// 跳转到总收入账单
	public void TotalIntoData() {
		intent = new Intent(this, Activity_SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "收入总额");
		intent.putExtra("typeflag", "1");
		startActivity(intent);
		finish();
	}

	// 跳转到总支出账单
	public void TotalOutData() {
		intent = new Intent(this, Activity_SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "支出总额");
		intent.putExtra("typeflag", "0");
		startActivity(intent);
		finish();
	}

	// 预算余额
	public void BudgetBalance() {
		View view = getLayoutInflater().inflate(R.layout.dialog_input, null);
		final Dialog dialog = new Dialog(this, R.style.AlertDialogStyle);
		dialog.setContentView(view);
		dialog.show();

		dialog_input_edittext = (EditText) view.findViewById(R.id.dialog_input_edittext);
		dialog_input_text = (TextView) view.findViewById(R.id.dialog_input_text);
		dialog_input_sure = (Button) view.findViewById(R.id.dialog_input_sure);
		dialog_input_cannle = (Button) view.findViewById(R.id.dialog_input_cannle);
		dialog_input_text.setText("设置本月消费预算");

		BudgetDBdao budgetDBdao = new BudgetDBdao(this);
		if (budgetDBdao.isExistBudget(name)) {
			dialog_input_edittext.setText(Float.toString(budgetDBdao.findBudget(name).getBudget()));
		} else {
			dialog_input_edittext.setText(0 + "");
		}

		dialog_input_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String budget = dialog_input_edittext.getText().toString();
				if (budget.trim().isEmpty()) {
					dialog.dismiss();
				} else {
					BudgetDBdao budgetDBdao = new BudgetDBdao(MainActivity.this);
					budgetDBdao.addBudget(Integer.parseInt(dialog_input_edittext.getText().toString()), name);
					Toast.makeText(MainActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
					onResume();
					dialog.dismiss();
				}
			}
		});
		dialog_input_cannle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}

	// 跳转到详细账单
	public void TotalAllData() {
		intent = new Intent(this, Activity_SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "详细账单");
		startActivity(intent);
		finish();
	}

	// 跳转到今日账单
	public void TodayData() {
		intent = new Intent(this, Activity_SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "今日账单");
		startActivity(intent);
		finish();
	}

	// 跳转到本月账单
	public void MonthData() {
		intent = new Intent(this, Activity_SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "本月账单");
		startActivity(intent);
		finish();
	}

	// 跳转到本年账单
	public void YearData() {
		intent = new Intent(this, Activity_SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "本年账单");
		startActivity(intent);
		finish();
	}

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

		map_list1 = MainActivityService.getDataSource1(totalInto, totalOut, name, this);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
