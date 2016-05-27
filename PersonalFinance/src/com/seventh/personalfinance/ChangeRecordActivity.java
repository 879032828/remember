package com.seventh.personalfinance;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.seventh.base.BaseActivity;
import com.seventh.base.RecyclerViewAdapter;
import com.seventh.db.Account;
import com.seventh.db.AccountDBdao;
import com.seventh.db.Type;
import com.seventh.db.TypeDBdao;
import com.seventh.util.TimeUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
 * @author Administrator
 *	��Activity���ڶԼ�¼���޸�
 */
public class ChangeRecordActivity extends BaseActivity {

	private EditText MoneySetting;
	private EditText TimeSetting;
	private EditText RemarkSetting;
	private EditText dialog_input_edittext;
	private RecyclerView mRecyclerView;
	private List<String> mDatas;
	private RecyclerViewAdapter mAdapter;
	private Button dialog_cannle, dialog_sure;

	private String title;
	private String name;
	private String type;
	TypeDBdao typeDBdao;

	private Account account;

	// ʱ��ѡ��������
	private static final int Time_requestCode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.activity_add_record);

		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		title = intent.getStringExtra("title");
		Bundle bundle = intent.getExtras();
		//����ʾ��¼��Activity��ȡ���˵�Account
		account = (Account) bundle.getSerializable("changeAccount");

		setHideleftButton(title);
		setHideaddButton_save();

		initData();
		initView();
		ShowCalendar();
		initEvent();

		setButtonOnClickListener("���水ť", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveData(account);
			}
		});

		setButtonOnClickListener("���ذ�ť", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ChangeRecordActivity.this, SpecificData.class);
				intent.putExtra("name", name);
				intent.putExtra("title", "�����ܶ�");
				ChangeRecordActivity.this.setResult(RESULT_OK, intent);
				finish();
			}
		});

	}

	/**
	 * ����ʱ��ѡ�����
	 */
	public void ShowCalendar() {

		TimeSetting.setInputType(InputType.TYPE_NULL);
		TimeSetting.setFocusable(false);
		TimeSetting.setFocusableInTouchMode(false);
		TimeSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ChangeRecordActivity.this, CalendarActivity.class);
				startActivityForResult(intent, Time_requestCode);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent) ��ȡ����Activity���صĽ���������ı��༭������ʾ
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
	 * ��ʼ��mDatas
	 */
	protected void initData() {
		mDatas = new ArrayList<String>() {
		};
		mDatas.add("����");
		mDatas.add("����");
		mDatas.add("��ְ");
		mDatas.add("����");
		mDatas.add("Ͷ��");
		mDatas.add("����");

		typeDBdao = new TypeDBdao(this);
		// ��ѯ��Ӧ�˺ŵ�������������
		List<Type> typecount = typeDBdao.findAllType(name, "0");
		// ���û�ж�Ӧ�����ʹ��ڣ�������Ĭ��ֵ
		if (typecount.size() == 0) {
			typeDBdao.setDefault(mDatas, name);
			mDatas.add("++");// �������Ӱ�ť�����ݣ���֤���Ӱ�ťһֱ�����б������
		} else {
			// �����ݿ��д������ͣ������mDatas�������»�ȡ���ݿ��е����ͣ����mDatas
			mDatas.clear();
			mDatas = typeDBdao.toListString(typecount);
			mDatas.add("++");// �������Ӱ�ť�����ݣ���֤���Ӱ�ťһֱ�����б������
		}
	}

	/**
	 * �����������ص��¼�
	 */
	private void initEvent() {

		//���Ϲ���ListViewĬ��ѡ�еĽ��������
		// setAdapter() ��ʵ���첽�� ����������������� ListView �� item ��û��������������������һ����Ϣ����ʱ�Ŵ�����
		//Ū�����������������ǰ������еĽ���취��ʹ�� post() �ύһ�� Runnable() ������ Runnable() �ڲ�����Ĭ��ѡ�����ֳ�ʼ��������
		mRecyclerView.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//ͨ��account��ȡ�������Type��mDatas�е��������Ӷ�������������RecyclerView�е�λ��
				//�����������ڵ�Item����ѡ�У�����ȡ��ֵ
				if (mRecyclerView.getChildCount() - 1 != 0) {
					ViewGroup parent = (ViewGroup) mRecyclerView.getChildAt(mDatas.indexOf(account.getType()));
					Button button = (Button) parent.findViewById(R.id.id_num);
					button.setBackgroundResource(R.drawable.item_bg_textview_focused);
					button.setTextColor(getResources().getColor(R.color.black));
					type = mAdapter.getmDatas().get(mDatas.indexOf(account.getType()));
				}
			}
		});

		mAdapter.setRecOnItemClickLitener(new RecyclerViewAdapter.OnRecItemClickLitener() {
			@Override
			public void onRecItemClick(View view, int position) {

				if (position == mRecyclerView.getChildCount() - 1) {
					// ��ĿҪ�󣺴˴��������Ӱ�ť����
					ShowDialog();
					Toast.makeText(ChangeRecordActivity.this, "�������ĵ����¼�", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(ChangeRecordActivity.this, position + " click", Toast.LENGTH_SHORT).show();
					// ��ȡRecyclerView��Item���������б���
					for (int i = 0; i < mRecyclerView.getChildCount(); i++) {
						// �������ItemΪRecyclerView�ж�Ӧ��Itemʱ������Item��������Ϊ���ʱ�ı���
						// ����Ļ���������Ϊδ���ʱ�ı���
						if (position == i) {
							ViewGroup parent = (ViewGroup) mRecyclerView.getChildAt(i);
							Button button = (Button) parent.findViewById(R.id.id_num);
							button.setBackgroundResource(R.drawable.item_bg_textview_focused);
							button.setTextColor(getResources().getColor(R.color.black));
							type = mAdapter.getmDatas().get(position);
						} else {
							ViewGroup parent = (ViewGroup) mRecyclerView.getChildAt(i);
							Button button = (Button) parent.findViewById(R.id.id_num);
							button.setBackgroundResource(R.drawable.item_bg_textview);
							button.setTextColor(getResources().getColor(R.color.gray_text));
						}
					}
				}
			}

			@Override
			public void onRecItemLongClick(View view, int position) {
				Toast.makeText(ChangeRecordActivity.this, position + " long click", Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * ��ʾ������������Ի���
	 */
	public void ShowDialog() {

		// ��ʾ�Ի���
		View inputForm = getLayoutInflater().inflate(R.layout.dialog_input, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		builder.setView(inputForm).create();
		final AlertDialog alertDialog = builder.show();

		dialog_input_edittext = (EditText) inputForm.findViewById(R.id.dialog_input_edittext);
		dialog_input_edittext.setFocusable(true);
		dialog_cannle = (Button) inputForm.findViewById(R.id.dialog_cannle);
		dialog_sure = (Button) inputForm.findViewById(R.id.dialog_sure);
		// �Ի����˳�
		dialog_cannle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		// �Ի���ȷ��
		dialog_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String incomeType = dialog_input_edittext.getText().toString().trim();
				if (incomeType.isEmpty()) {
					Toast.makeText(ChangeRecordActivity.this, "��������������", Toast.LENGTH_SHORT).show();
				} else {
					// �ж����������Ƿ��Ѵ��������ݿ���
					if (typeDBdao.isExist("0", name, incomeType)) {
						Toast.makeText(ChangeRecordActivity.this, "�������Ѵ���", Toast.LENGTH_SHORT).show();
						;
					} else {
						// ���������Ͳ��������ݿ��У��򽫸��������ӵ�����
						typeDBdao.addRecord("0", incomeType, name);
						// ����RecyclerView��Item
						mAdapter.addData(mAdapter.getItemCount() - 1, incomeType);
						alertDialog.dismiss();
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
			Toast.makeText(ChangeRecordActivity.this, "���������", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			money = Float.parseFloat(MoneySetting.getText().toString());
		}
		if (type == null || type.isEmpty()) {
			Toast.makeText(ChangeRecordActivity.this, "��ѡ����������", Toast.LENGTH_SHORT).show();
			return false;
		}

		AccountDBdao accountDBdao = new AccountDBdao(this);
		accountDBdao.update(account.getId(),time, money, type, 1, remark, name);
		Toast.makeText(ChangeRecordActivity.this, "�޸ļ�¼�ɹ���", Toast.LENGTH_SHORT).show();

		Intent intent = new Intent(ChangeRecordActivity.this, SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "�����ܶ�");
		ChangeRecordActivity.this.setResult(RESULT_OK, intent);
		finish();

		return true;
	}

	/**
	 * ��ʼ���ؼ�
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