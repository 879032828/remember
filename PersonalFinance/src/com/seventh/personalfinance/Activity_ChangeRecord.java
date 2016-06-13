package com.seventh.personalfinance;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.seventh.adapter.RecyclerViewAdapter;
import com.seventh.base.BaseActivity;
import com.seventh.db.Account;
import com.seventh.db.AccountDBdao;
import com.seventh.db.Type;
import com.seventh.db.TypeDBdao;
import com.seventh.util.TimeUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator 该Activity用于对记录的修改
 */
public class Activity_ChangeRecord extends BaseActivity {

	private EditText MoneySetting;
	private EditText TimeSetting;
	private EditText RemarkSetting;
	private EditText dialog_input_edittext;
	private RecyclerView mRecyclerView;
	private List<String> mDatas;
	private RecyclerViewAdapter mAdapter;
	private Button dialog_cannle, dialog_sure;
	private TextView dialog_input_text;
	private Dialog dialog;
	private String title;
	private String name;
	private String type;
	private String typeflag;
	TypeDBdao typeDBdao;

	private Account account;

	private static final String type_expend = "0";// 支出类型标志
	private static final String type_income = "1";// 收入类型标志
	private static final int earning_expend = 0;// 支出类型标志
	private static final int earning_income = 1;// 收入类型标志

	// 时间选择请求码
	private static final int Time_requestCode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.activity_add_record);

		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		title = intent.getStringExtra("title");
		typeflag = intent.getStringExtra("typeflag");
		Bundle bundle = intent.getExtras();
		// 从显示记录的Activity获取个人的Account
		account = (Account) bundle.getSerializable("changeAccount");

		setHideleftButton(title);
		setHideaddButton_save();

		initData();
		initView();
		ShowCalendar();
		initEvent();

		setButtonOnClickListener("保存按钮", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveData(account);
			}
		});

		setButtonOnClickListener("返回按钮", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Activity_ChangeRecord.this, Activity_SpecificData.class);
				intent.putExtra("name", name);
				intent.putExtra("title", "收入总额");
				Activity_ChangeRecord.this.setResult(RESULT_OK, intent);
				finish();
			}
		});

	}

	/**
	 * 设置时间选择操作
	 */
	public void ShowCalendar() {

		TimeSetting.setInputType(InputType.TYPE_NULL);
		TimeSetting.setFocusable(false);
		TimeSetting.setFocusableInTouchMode(false);
		TimeSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Activity_ChangeRecord.this, Activity_Calendar.class);
				startActivityForResult(intent, Time_requestCode);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent) 获取日历Activity返回的结果，并在文本编辑框中显示
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode == 0 && resultCode == 0) {
				Bundle bundle = data.getBundleExtra("Calendar");
				if (!bundle.getString("time").isEmpty()) {
					TimeSetting.setText(bundle.getString("time"));
				}
			}
		}
	}

	/**
	 * 初始化mDatas
	 */
	protected void initData() {
		mDatas = new ArrayList<String>() {
		};
		if (typeflag.equals(type_expend)) {
			mDatas.add("三餐");
			mDatas.add("住房");
			mDatas.add("服装");
			mDatas.add("交通");
			mDatas.add("其他");
		} else {
			mDatas.add("工资");
			mDatas.add("奖金");
			mDatas.add("兼职");
			mDatas.add("理财");
			mDatas.add("投资");
			mDatas.add("其他");
		}

		typeDBdao = new TypeDBdao(this);
		// 查询对应账号的所有收支类型
		List<Type> typecount = typeDBdao.findAllType(name, typeflag);
		// 如果没有对应的类型存在，则设置默认值
		if (typecount.size() == 0) {
			typeDBdao.setDefault(mDatas, typeflag, name);
			mDatas.add("++");// 设置增加按钮的内容，保证增加按钮一直处于列表最后面
		} else {
			// 若数据库中存在类型，则清空mDatas，并重新获取数据库中的类型，填充mDatas
			mDatas.clear();
			mDatas = typeDBdao.toListString(typecount);
			mDatas.add("++");// 设置增加按钮的内容，保证增加按钮一直处于列表最后面
		}
	}

	/**
	 * 设置适配器回调事件
	 */
	private void initEvent() {

		// 网上关于ListView默认选中的解决方法：
		// setAdapter() 其实是异步的 ，调用了这个方法， ListView 的 item 并没有立马创建，而是在下一轮消息处理时才创建。
		// 弄明白了这个，就有了前面代码中的解决办法：使用 post() 提交一个 Runnable() 对象，在 Runnable()
		// 内部来做默认选中这种初始化动作。
		mRecyclerView.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 通过account获取金额类型Type在mDatas中的索引，从而锁定该类型在RecyclerView中的位置
				// 将该类型所在的Item设置选中，并获取其值
				if (mRecyclerView.getChildCount() - 1 != 0) {
					ViewGroup parent = (ViewGroup) mRecyclerView.getChildAt(mDatas.indexOf(account.getType()));
					Button button = (Button) parent.findViewById(R.id.id_num);
					button.setBackgroundResource(R.drawable.shape_item_bg_textview_focused);
					button.setTextColor(getResources().getColor(R.color.black));
					type = mAdapter.getmDatas().get(mDatas.indexOf(account.getType()));
				}
			}
		});

		mAdapter.setRecOnItemClickLitener(new RecyclerViewAdapter.OnRecItemClickLitener() {
			@Override
			public void onRecItemClick(View view, int position) {

				if (position == mRecyclerView.getChildCount() - 1) {
					// 项目要求：此处设置增加按钮操作
					ShowDialog();
					Toast.makeText(Activity_ChangeRecord.this, "这是最后的单击事件", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(Activity_ChangeRecord.this, position + " click", Toast.LENGTH_SHORT).show();
					// 获取RecyclerView的Item个数，进行遍历
					for (int i = 0; i < mRecyclerView.getChildCount(); i++) {
						// 当点击的Item为RecyclerView中对应的Item时，将该Item背景设置为点击时的背景
						// 否则的话，则设置为未点击时的背景
						if (position == i) {
							ViewGroup parent = (ViewGroup) mRecyclerView.getChildAt(i);
							Button button = (Button) parent.findViewById(R.id.id_num);
							button.setBackgroundResource(R.drawable.shape_item_bg_textview_focused);
							button.setTextColor(getResources().getColor(R.color.black));
							type = mAdapter.getmDatas().get(position);
						} else {
							ViewGroup parent = (ViewGroup) mRecyclerView.getChildAt(i);
							Button button = (Button) parent.findViewById(R.id.id_num);
							button.setBackgroundResource(R.drawable.shape_item_bg_textview);
							button.setTextColor(getResources().getColor(R.color.gray_text));
						}
					}
				}
			}

			@Override
			public void onRecItemLongClick(View view, int position) {
				Toast.makeText(Activity_ChangeRecord.this, position + " long click", Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 显示收入类型输入对话框
	 */
	public void ShowDialog() {

		// 显示对话框
		View inputForm = getLayoutInflater().inflate(R.layout.dialog_input, null);
		dialog = new Dialog(Activity_ChangeRecord.this, R.style.AlertDialogStyle);
		dialog.setContentView(inputForm);
		dialog.show();

		dialog_input_text = (TextView) inputForm.findViewById(R.id.dialog_input_text);
		dialog_input_edittext = (EditText) inputForm.findViewById(R.id.dialog_input_edittext);
		dialog_input_edittext.setFocusable(true);
		dialog_cannle = (Button) inputForm.findViewById(R.id.dialog_input_cannle);
		dialog_sure = (Button) inputForm.findViewById(R.id.dialog_input_sure);

		if (typeflag.equals(type_expend)) {
			dialog_input_text.setText("添加支出类型");
		} else {
			dialog_input_text.setText("添加收入类型");
		}

		// 对话框退出
		dialog_cannle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		// 对话框确认
		dialog_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String incomeType = dialog_input_edittext.getText().toString().trim();
				if (incomeType.isEmpty()) {
					Toast.makeText(Activity_ChangeRecord.this, "请输入类型名称", Toast.LENGTH_SHORT).show();
				} else {
					// 判断输入类型是否已存在于数据库中
					if (typeDBdao.isExist(typeflag, name, incomeType)) {
						Toast.makeText(Activity_ChangeRecord.this, "该类型已存在", Toast.LENGTH_SHORT).show();
						;
					} else {
						// 若输入类型不存在数据库中，则将该类型添加到表中
						typeDBdao.addRecord(typeflag, incomeType, name);
						// 增加RecyclerView的Item
						mAdapter.addData(mAdapter.getItemCount() - 1, incomeType);
						dialog.dismiss();
					}
				}
			}
		});

	}

	public boolean saveData(Account account) {
		Float money;
		String time = TimeSetting.getText().toString().trim();
		String remark = RemarkSetting.getText().toString().trim();
		String name = this.name;
		if (MoneySetting.getText().toString().isEmpty()) {
			Toast.makeText(Activity_ChangeRecord.this, "请输入金融", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			money = Float.parseFloat(MoneySetting.getText().toString());
		}
		if (type == null || type.isEmpty()) {
			if (typeflag.equals(type_expend)) {
				Toast.makeText(Activity_ChangeRecord.this, "请选择支出类型", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(Activity_ChangeRecord.this, "请选择收入类型", Toast.LENGTH_SHORT).show();
			}
			return false;
		}

		AccountDBdao accountDBdao = new AccountDBdao(this);
		if (typeflag.equals(type_expend)) {
			accountDBdao.update(account.getId(), time, money, type, earning_expend, remark, name);
		} else {
			accountDBdao.update(account.getId(), time, money, type, earning_income, remark, name);
		}

		Toast.makeText(Activity_ChangeRecord.this, "修改纪录成功！", Toast.LENGTH_SHORT).show();

		Intent intent = new Intent(Activity_ChangeRecord.this, Activity_SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", title);
		Activity_ChangeRecord.this.setResult(RESULT_OK, intent);
		finish();

		return true;
	}

	/**
	 * 初始化控件
	 */
	public void initView() {
		MoneySetting = (EditText) findViewById(R.id.MoneySetting);
		TimeSetting = (EditText) findViewById(R.id.TimeSetting);
		RemarkSetting = (EditText) findViewById(R.id.RemarkSetting);
		mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
		mAdapter = new RecyclerViewAdapter(this, mDatas);
		mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
		mRecyclerView.setAdapter(mAdapter);

		MoneySetting.setText(account.getMoney() + "");
		TimeSetting.setText(account.getTime());
		RemarkSetting.setText(account.getRemark());
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("change", "onDestroy");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("change", "onPause");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("change", "onResume");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("change", "onStart");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("change", "onStop");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.i("change", "onRestart");
	}

}
