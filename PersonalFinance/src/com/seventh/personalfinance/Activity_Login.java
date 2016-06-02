package com.seventh.personalfinance;

import com.seventh.db.Person;
import com.seventh.db.PersonDBdao;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_Login extends Activity implements OnClickListener {
	PersonDBdao persondbdao;
	private Person person;
	private EditText mEditTextName;// 账号
	private EditText mEditTextPwd;// 密码
	private Button mButtonOK;// 登录按钮
	private Button mButtonCancel;// 取消按钮
	private Button mButtonRegister;// 注册按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题
		setContentView(R.layout.activity_login);
		LoginOk();
		initView();
	}

	/**
	 * 初始化视图控件
	 */
	public void initView() {
		mEditTextName = (EditText) this.findViewById(R.id.et_login_name);
		mEditTextPwd = (EditText) this.findViewById(R.id.et_login_password);
		mButtonOK = (Button) this.findViewById(R.id.bt_login_ok);
		mButtonCancel = (Button) this.findViewById(R.id.bt_login_cancel);
		mButtonRegister = (Button) this.findViewById(R.id.tv_login_register_link);

		mButtonOK.setOnClickListener(this);
		mButtonCancel.setOnClickListener(this);
		mButtonRegister.setOnClickListener(this);
		
		
	}

	private void LoginOk() {
		persondbdao = new PersonDBdao(getApplicationContext());
		person = persondbdao.findLoginOk();
		if (person == null) {

		} else {
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("name", person.getName());
			// 传值帐户名
			startActivity(intent);
			finish();
		}
		
	}

	// 按钮对应的点击事件
	// 参数 v 代表的就是当前被点击的条目对应的view对象
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_login_ok:// 登录按钮
			// 相应按钮的点击事件
			Action_Login();
			break;
		case R.id.bt_login_cancel:// 登录取消
			finish();
			break;
		case R.id.tv_login_register_link:
			Intent intent = new Intent(this, Activity_Registration.class);
			startActivity(intent);
			finish();
			break;
		}

	}

	/**
	 * @return 登录按钮操作
	 */
	public Boolean Action_Login() {
		if (mEditTextName.getText().toString().trim().equals("")) {
			Toast.makeText(getApplicationContext(), "账户名不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (mEditTextPwd.getText().toString().equals("")) {
			Toast.makeText(getApplicationContext(), "密码不能为空！", Toast.LENGTH_SHORT).show();
			return false;
		}

		persondbdao = new PersonDBdao(getApplicationContext());
		boolean result = persondbdao.find(mEditTextName.getText().toString());

		if (result) {
			result = persondbdao.findLogin(mEditTextName.getText().toString(), mEditTextPwd.getText().toString());
			if (result) {
				persondbdao.updateLoginOK(mEditTextName.getText().toString());
				Intent intent = new Intent(this, MainActivity.class);

				intent.putExtra("name", mEditTextName.getText().toString().trim());
				// 传值 帐户名
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "密码有误", 0).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "不存在该账号", 0).show();
		}
		return false;
	}
}
