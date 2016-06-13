package com.seventh.personalfinance;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.seventh.adapter.SpecificAdapter;
import com.seventh.base.BaseActivity;
import com.seventh.db.Account;
import com.seventh.db.AccountDBdao;
import com.seventh.util.TimeUtil;
import com.seventh.view.CornerListView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author Administrator
 *
 */
public class Activity_SpecificData extends BaseActivity {
	private Intent intent = null;// 定义一个意图
	private String name;// 账号
	private String title;// 标题
	private String typeflag;// 收支类型标志
	private AccountDBdao accountDBdao;// 数据库
	private Dialog dialog;
	private Dialog dialog_delete;
	private CornerListView cornerListView = null;// 数据报表
	private List<Account> accounts;// 账单数据

	// 添加收入记录的请求码
	private static final int requestCode_addrecord = 0;
	// 修改记录的请求码
	private static final int requestCode_change = 1;

	private static final String earnings_expend = "0";
	private static final String earnings_income = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.activity_specific_data);
		Log.i("specificData", "onCreate");

		intent = this.getIntent();
		name = intent.getStringExtra("name");// 接收主界面的数据
		title = intent.getStringExtra("title");// 接收主界面的数据
		typeflag = intent.getStringExtra("typeflag");

		setHideleftButton(title);// 设置返回箭头
		setHideaddButton_right();// 设置右加按钮
		setBackgroudButton_right(R.drawable.shape_bg_add_button);
		setButtonOnClickListener("右按钮", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (title) {
				case "支出总额":
					Intent intent1 = new Intent(Activity_SpecificData.this, Activity_AddRecord.class);
					intent1.putExtra("name", name);
					intent1.putExtra("title", "添加支出记录");
					intent1.putExtra("typeflag", typeflag);
					startActivityForResult(intent1, requestCode_addrecord);
					break;
				case "收入总额":
					Intent intent2 = new Intent(Activity_SpecificData.this, Activity_AddRecord.class);
					intent2.putExtra("name", name);
					intent2.putExtra("title", "添加收入记录");
					intent2.putExtra("typeflag", typeflag);
					startActivityForResult(intent2, requestCode_addrecord);
					break;
				case "本月收入":
					Intent intent3 = new Intent(Activity_SpecificData.this, Activity_AddRecord.class);
					intent3.putExtra("name", name);
					intent3.putExtra("title", "添加收入记录");
					intent3.putExtra("typeflag", typeflag);
					startActivityForResult(intent3, requestCode_addrecord);
					break;
				case "本月支出":
					Intent intent4 = new Intent(Activity_SpecificData.this, Activity_AddRecord.class);
					intent4.putExtra("name", name);
					intent4.putExtra("title", "添加支出记录");
					intent4.putExtra("typeflag", typeflag);
					startActivityForResult(intent4, requestCode_addrecord);
					break;
				default:
					break;
				}
			}
		});

		setButtonOnClickListener("返回按钮", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Activity_SpecificData.this, MainActivity.class);
				intent.putExtra("name", name);
				startActivity(intent);
				finish();
			}
		});

		accountDBdao = new AccountDBdao(getApplicationContext());
		// 设置listview 值
		cornerListView = (CornerListView) findViewById(R.id.lv_specific_data_list);
		GetDataBytitle();
		// 填充listview的数据
		cornerListView.setAdapter(new SpecificAdapter(accounts, this));
		// listview选项的点击事件
		cornerListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Account account = accounts.get(arg2);
				showMyDialog(account);
			}
		});
	}

	/**
	 * 根据title获取对应的数据
	 */
	private void GetDataBytitle() {
		try {
			if (title.equals("收入总额")) {
				accounts = accountDBdao.findTotalIntoByName(name);
			} else if (title.equals("支出总额")) {
				accounts = accountDBdao.findTotalOutByName(name);
			} else if (title.equals("本月收入")) {
				accounts = accountDBdao.findSomeTimeByName(earnings_income, name, TimeUtil.Get_Year_Month());
			} else if (title.equals("本月支出")) {
				accounts = accountDBdao.findSomeTimeByName(earnings_expend, name, TimeUtil.Get_Year_Month());
			}
		} catch (Exception e) {
			Toast.makeText(this, "获取数据失败", 0).show();
			e.printStackTrace();
		}
	}

	/**
	 * @param account
	 */
	public void showMyDialog(final Account account) {
		View view = getLayoutInflater().inflate(R.layout.dialog_change_delete, null);
		dialog = new Dialog(this, R.style.AlertDialogStyle);
		dialog.setContentView(view);
		dialog.show();

		Button changeButton = (Button) view.findViewById(R.id.data_change);
		Button deleteButton = (Button) view.findViewById(R.id.data_delete);

		// 修改记录
		changeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Activity_SpecificData.this, Activity_ChangeRecord.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("changeAccount", account);
				intent.putExtras(bundle);
				intent.putExtra("name", name);
				if (typeflag.equals(earnings_expend)) {
					intent.putExtra("title", "修改支出记录");
				} else {
					intent.putExtra("title", "修改收入记录");
				}
				intent.putExtra("typeflag", typeflag);
				dialog.dismiss();
				startActivityForResult(intent, requestCode_change);
			}
		});
		// 删除记录
		deleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDeleteDialog(account);
				dialog.dismiss();
			}
		});
	}

	/**
	 * 显示删除对话框
	 */
	public void showDeleteDialog(final Account account) {
		View view = getLayoutInflater().inflate(R.layout.dialog_delete, null);

		dialog_delete = new Dialog(Activity_SpecificData.this, R.style.AlertDialogStyle);
		dialog_delete.setContentView(view);
		dialog_delete.show();

		Button dialog_delete_cannle = (Button) view.findViewById(R.id.dialog_delete_cannle);
		Button dialog_delete_sure = (Button) view.findViewById(R.id.dialog_delete_sure);

		// 确定删除
		dialog_delete_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AccountDBdao accountDBdao = new AccountDBdao(Activity_SpecificData.this);
				accountDBdao.delete(account.getId(), name);
				onResume();
				dialog_delete.dismiss();
			}
		});
		// 取消
		dialog_delete_cannle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog_delete.dismiss();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("specificData", "onResume");
		GetDataBytitle();

		// 填充listview的数据
		cornerListView.setAdapter(new SpecificAdapter(accounts, this));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("specificData", "onDestroy");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("specificData", "onPause");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i("specificData", "onRestart");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("specificData", "onStart");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("specificData", "onStop");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(Activity_SpecificData.this, MainActivity.class);
			intent.putExtra("name", name);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case requestCode_addrecord:
			switch (resultCode) {
			case RESULT_OK:
				Intent intent = getIntent();
				name = intent.getStringExtra("name");
				title = intent.getStringExtra("title");
				break;

			default:
				break;
			}
			break;
		case requestCode_change:
			switch (resultCode) {
			case RESULT_OK:
				Intent intent = getIntent();
				name = intent.getStringExtra("name");
				title = intent.getStringExtra("title");
				break;

			default:
				break;
			}
			break;
		default:
			break;
		}
	}

}
