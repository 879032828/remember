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
	private EditText mEditTextName;// �˺�
	private EditText mEditTextPwd;// ����
	private Button mButtonOK;// ��¼��ť
	private Button mButtonCancel;// ȡ����ť
	private Button mButtonRegister;// ע�ᰴť

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ������
		setContentView(R.layout.activity_login);
		LoginOk();
		initView();
	}

	/**
	 * ��ʼ����ͼ�ؼ�
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
			// ��ֵ�ʻ���
			startActivity(intent);
			finish();
		}
		
	}

	// ��ť��Ӧ�ĵ���¼�
	// ���� v ����ľ��ǵ�ǰ���������Ŀ��Ӧ��view����
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_login_ok:// ��¼��ť
			// ��Ӧ��ť�ĵ���¼�
			Action_Login();
			break;
		case R.id.bt_login_cancel:// ��¼ȡ��
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
	 * @return ��¼��ť����
	 */
	public Boolean Action_Login() {
		if (mEditTextName.getText().toString().trim().equals("")) {
			Toast.makeText(getApplicationContext(), "�˻�������Ϊ�գ�", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (mEditTextPwd.getText().toString().equals("")) {
			Toast.makeText(getApplicationContext(), "���벻��Ϊ�գ�", Toast.LENGTH_SHORT).show();
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
				// ��ֵ �ʻ���
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(getApplicationContext(), "��������", 0).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "�����ڸ��˺�", 0).show();
		}
		return false;
	}
}
