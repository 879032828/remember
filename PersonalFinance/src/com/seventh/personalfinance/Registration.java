package com.seventh.personalfinance;



import com.seventh.db.PersonDBdao;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends Activity implements OnClickListener {
	PersonDBdao persondbdao;
	private EditText mEditTextName;// 账号
	private EditText mEditTextPwd1;// 密码
	private EditText mEditTextPwd2;// 密码
	private Button mButtonOK;
	private Button mButtonCancel;
	
	private String name;
	private String pwd1;
	private String pwd2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
		setContentView(R.layout.activity_registration);
		
		mEditTextName = (EditText) this.findViewById(R.id.et_register_username);
		mEditTextPwd1 = (EditText) this.findViewById(R.id.et_register_new_pwd);
		mEditTextPwd2 = (EditText) this.findViewById(R.id.et_register_confirm_pwd);
		mButtonOK=(Button) this.findViewById(R.id.bt_register_ok);
		mButtonCancel=(Button) this.findViewById(R.id.bt_register_cancel);
		mButtonOK.setOnClickListener(this);
		mButtonCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bt_register_ok:
			name=mEditTextName.getText().toString();
			pwd1=mEditTextPwd1.getText().toString().trim();
			pwd2=mEditTextPwd2.getText().toString().trim();
			
			if (name.equals("")) {
				Toast.makeText(getApplicationContext(), "账户名不能为空！",
						Toast.LENGTH_SHORT).show();
				break;
			}
			if (pwd1.equals("")) {
				Toast.makeText(getApplicationContext(), "密码不能为空！",
						Toast.LENGTH_SHORT).show();
				break;
			}
			if (!pwd1.equals(pwd2)) {
				Toast.makeText(getApplicationContext(), "确认密码不同！",
						Toast.LENGTH_SHORT).show();
				break;
			}
			persondbdao = new PersonDBdao(getApplicationContext());
			persondbdao.add(name, pwd2);
			Toast.makeText(getApplicationContext(), "注 册 成 功 ！",
					Toast.LENGTH_SHORT).show();
			finish();
			break;
		case R.id.bt_register_cancel:
			finish();
			break;
		}
		
	}

}
