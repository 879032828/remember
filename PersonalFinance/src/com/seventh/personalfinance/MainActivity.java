package com.seventh.personalfinance;

import java.util.List;
import java.util.Map;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.seventh.base.SlidingMenuBaseActivity;
import com.seventh.db.AccountDBdao;
import com.seventh.db.BudgetDBdao;
import com.seventh.db.PersonDBdao;
import com.seventh.view.CornerListView;
import com.seventh.view.DataRange;
import com.seventh.view.MainActivityService;
import com.seventh.view.TasksCompletedView;

import android.R.integer;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
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

public class MainActivity extends SlidingMenuBaseActivity {
	private Intent intent = null;// 定义一个意图
	private String name;// 账号
	private String pwd1;
	private String pwd2;
	private AccountDBdao accountDBdao;// 数据库
	private PersonDBdao persondbdao;

	private float totalOut;// 总支出
	private float totalInto;// 总收入

	private ListView listView1;
	private List<Map<String, String>> map_list1 = null;

	private TasksCompletedView mTasksCompletedView;
	private CornerListView cornerListView2 = null;// 自定义listview2
	private List<DataRange> dataRanges;
	private LayoutInflater inflater;
	private ImageView ImageView_main;
	private EditText dialog_input_edittext;
	private TextView dialog_input_text;
	private Button dialog_input_sure;
	private Button dialog_input_cannle;
	private SlidingMenu slidingMenu;

	private static final int BudgetBalance = 0;
	private static final int TotalIntoData = 1;
	private static final int TotalOutData = 2;
	private static final int MonthIntoData = 3;
	private static final int MonthOutData = 4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.activity_mainactivity);

		initView();
		ViewOperation();

		// SlidingMenu初始化
		initRightMenu();

		// 在控件完成后，执行SettingView操作更新控件的高度
		mTasksCompletedView.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SettingView(mTasksCompletedView);
			}
		});

		setTitle("记着");
		setTime();
		// 设置两个按钮可见及背景
		setHideaddButton_right();
		setBackgroudButton_right(R.drawable.widget_bar_news_over);
		setButtonOnClickListener("右按钮", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				slidingMenu.showMenu();
			}
		});

	}

	/**
	 * 获取状态栏和标题栏的高度，并设置ImageView的高度
	 */
	public void SettingView(View view) {
		Rect rect = new Rect();
		Window window = getWindow();
		view.getWindowVisibleDisplayFrame(rect);
		// 状态栏的高度
		int statusBarHeight = rect.top;
		// 标题栏的高度
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_relative);
		int contentViewTop = relativeLayout.getHeight();

		// 设置控件的高度
		WindowManager windowManager = this.getWindowManager();
		int height = windowManager.getDefaultDisplay().getHeight();
		LayoutParams para = view.getLayoutParams();
		para.height = height / 2 - statusBarHeight - contentViewTop;
		para.width = view.getLayoutParams().width;
		view.setLayoutParams(para);
	}

	/**
	 * RecyclerView列表初始化
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
					case BudgetBalance:
						// 设置预算余额
						BudgetBalance();
						break;
					case TotalIntoData:
						// 收入总额
						TotalIntoData();
						break;
					case TotalOutData:
						// 支出总额
						TotalOutData();
						break;
					case MonthIntoData:
						// 本月收入
						MonthIntoData();
						break;
					case MonthOutData:
						// 本月支出
						MonthOutData();
						break;
					}

				}
			});

			Float budget = (float) 0;// 预算余额
			Float ThisMonth_expend = (float) 0;// 本月支出
			for (Map<String, String> map : map_list1) {
				switch (map.get("txtCalculationName")) {
				case "预算余额":
					budget = Float.parseFloat(map.get("txtMoney"));
					break;
				case "本月支出":
					ThisMonth_expend = Float.parseFloat(map.get("txtMoney"));
				default:
					break;
				}
			}

			Float residue = budget - ThisMonth_expend;
			if (residue >= 0) {
				mTasksCompletedView.setProgress(new Float(ThisMonth_expend).intValue());
				mTasksCompletedView.setTotalProgress(new Float(budget).intValue());
				mTasksCompletedView.setText("预算剩余_" + new Float(residue).intValue() + "元");
			} else {
				mTasksCompletedView.setProgress(new Float(ThisMonth_expend).intValue());
				mTasksCompletedView.setTotalProgress(new Float(budget).intValue());
				mTasksCompletedView.setText("预算超支_" + new Float(residue).intValue() + "元");
			}
		}
	}

	/**
	 * 初始化界面
	 */
	public void initView() {
		listView1 = (ListView) findViewById(R.id.lv_main_calculation);// 总额显示
		mTasksCompletedView = (TasksCompletedView) findViewById(R.id.tasks_view);// 圆环进度条

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

	// 跳转到本月收入
	public void MonthIntoData() {
		intent = new Intent(this, Activity_SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "本月收入");
		intent.putExtra("typeflag", "1");
		startActivity(intent);
		finish();
	}

	// 跳转到本月支出
	public void MonthOutData() {
		intent = new Intent(this, Activity_SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "本月支出");
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
			// 如果存在预算，则显示
			String budget = Float.toString(budgetDBdao.findBudget(name).getBudget());
			dialog_input_edittext.setText(budget);
		} else {
			dialog_input_edittext.setText(0 + "");
			budgetDBdao.addBudget(0, name);
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
					Float budgetInt = Float.parseFloat(dialog_input_edittext.getText().toString());
					budgetDBdao.updateBudget(budgetInt, name);
					Toast.makeText(MainActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
					updateView();
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

	private void initRightMenu() {
		Fragment leftMenuFragment = new MenuLeftFragment();
		setBehindContentView(R.layout.layout_left_menu);
		Bundle bundle = new Bundle();
		bundle.putString("name", name);
		leftMenuFragment.setArguments(bundle);
		getSupportFragmentManager().beginTransaction().replace(R.id.id_left_menu_frame, leftMenuFragment).commit();

		slidingMenu = getSlidingMenu();
		slidingMenu.setMode(SlidingMenu.LEFT);
		// 设置触摸屏幕的模式
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setShadowDrawable(R.drawable.shape_shadow);
		// 设置滑动菜单视图的宽度
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// menu.setBehindWidth()
		// 设置渐入渐出效果的值
		slidingMenu.setFadeDegree(0.35f);
	}

	public void updateView() {
		totalOut = accountDBdao.fillTotalOut(name);// 总支出
		totalInto = accountDBdao.fillTotalInto(name);// 总收入

		map_list1 = MainActivityService.getDataSource1(totalInto, totalOut, name, this);
		// listview1适配器
		SimpleAdapter adapter1 = new SimpleAdapter(getApplicationContext(), map_list1,
				R.layout.main_listview_calculation, new String[] { "txtCalculationName", "txtMoney" },
				new int[] { R.id.ls_tv_txtCalculationName, R.id.ls_tv_txtMoney });
		// 填充listview1的数据
		listView1.setAdapter(adapter1);

		Float budget = (float) 0;// 预算余额
		Float ThisMonth_expend = (float) 0;// 本月支出
		for (Map<String, String> map : map_list1) {
			switch (map.get("txtCalculationName")) {
			case "预算余额":
				budget = Float.parseFloat(map.get("txtMoney"));
				break;
			case "本月支出":
				ThisMonth_expend = Float.parseFloat(map.get("txtMoney"));
			default:
				break;
			}
		}

		Float residue = budget - ThisMonth_expend;
		if (residue >= 0) {
			mTasksCompletedView.setProgress(new Float(ThisMonth_expend).intValue());
			mTasksCompletedView.setTotalProgress(new Float(budget).intValue());
			mTasksCompletedView.setText("预算剩余_" + new Float(residue).intValue() + "元");
		} else {
			mTasksCompletedView.setProgress(new Float(ThisMonth_expend).intValue());
			mTasksCompletedView.setTotalProgress(new Float(budget).intValue());
			mTasksCompletedView.setText("预算超支_" + new Float(residue).intValue() + "元");
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
