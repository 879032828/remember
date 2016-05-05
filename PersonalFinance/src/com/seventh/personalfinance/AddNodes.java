package com.seventh.personalfinance;

import java.util.Calendar;
import java.util.TimeZone;

import com.seventh.db.AccountDBdao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddNodes extends Activity implements OnClickListener {
	private String name;// 账号
	AccountDBdao accountDBdao;

	private EditText mEditTextMoney;// 金额
	private EditText mEditTextRemark;// 备注
	private EditText mEditTextTime; // 时间
	private Spinner mSpinnerType; // 类型
	private Spinner mSpinnerEarnings; // 收益
	private Button mButtonAdd;
	private Button mButtonCancel;

	private String time;
	private float money;
	private String type;
	private boolean earning;
	private String remark;

	private static final String[] types = { "衣服装饰", "工资奖金", "投资盈利", "出行交通",
			"娱乐聚会", "生活用品", "水电房租", "缴费清单", "股票收益", "其他" };
	private static final String[] earnings = { "支出", "收入" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnodes);

		Intent intent = this.getIntent();
		name = intent.getStringExtra("name");// 接收登录界面的数据

		mEditTextMoney = (EditText) this.findViewById(R.id.et_addnodes_money);// 金额
		mEditTextRemark = (EditText) this.findViewById(R.id.et_addnodes_remark);// 备注
		mEditTextTime = (EditText) this.findViewById(R.id.et_addnodes_time);// 时间

		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")); // 获取东八区时间
		int year = c.get(Calendar.YEAR); // 获取年
		int month = c.get(Calendar.MONTH) + 1; // 获取月份，0表示1月份
		int day = c.get(Calendar.DAY_OF_MONTH); // 获取当前天数
		time = year + "/" + month + "/" + day;// 获取系统当前时间
		mEditTextTime.setText(time);// 设置时间

		// 类型
		mSpinnerType = (Spinner) this.findViewById(R.id.sp_addnodes_type);
		// 将可选内容与mSpinnerType连接起来
		ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this,
				R.layout.addnodes_earnings, types);
		// 设置下拉列表的风格
		adapterType
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapterType 添加到mSpinnerType中
		mSpinnerType.setAdapter(adapterType);

		// 收益
		mSpinnerEarnings = (Spinner) this
				.findViewById(R.id.sp_addnodes_earnings);
		ArrayAdapter<String> adapterEarnings = new ArrayAdapter<String>(this,
				R.layout.addnodes_earnings, earnings);
		adapterEarnings
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinnerEarnings.setAdapter(adapterEarnings);

		mButtonAdd = (Button) this.findViewById(R.id.bt_addnodes_add);
		mButtonCancel = (Button) this.findViewById(R.id.bt_addnodes_cancel);
		mButtonAdd.setOnClickListener(this);
		mButtonCancel.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_addnodes_add:// 添加按钮
			if (mEditTextMoney.getText().toString().trim().equals("")) {
				Toast.makeText(getApplicationContext(), "金额不能为空！", 0).show();
				break;
			} else {
				money = Float.parseFloat(mEditTextMoney.getText().toString()
						.trim());
			}
			time = mEditTextTime.getText().toString().trim();
			type = mSpinnerType.getSelectedItem().toString();
			if (mSpinnerEarnings.getSelectedItem().toString().equals("收入")) {
				earning = true;
			} else {
				earning = false;
			}
			remark = mEditTextRemark.getText().toString().trim();

			accountDBdao = new AccountDBdao(getApplicationContext());
			accountDBdao.add(time, money, type, earning, remark, name);
			Toast.makeText(getApplicationContext(), "添 加 账 单 条 目 成 功 ！", 0)
					.show();
			break;
		case R.id.bt_addnodes_cancel:// 取消按钮
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("name", name);
			// 传值 帐户名
			startActivity(intent);
			break;

		}

	}

}
