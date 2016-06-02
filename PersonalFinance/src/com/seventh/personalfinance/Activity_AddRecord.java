package com.seventh.personalfinance;

import java.util.ArrayList;
import java.util.List;

import com.seventh.adapter.RecyclerViewAdapter;
import com.seventh.base.BaseActivity;
import com.seventh.db.Account;
import com.seventh.db.AccountDBdao;
import com.seventh.db.Type;
import com.seventh.db.TypeDBdao;
import com.seventh.util.TimeUtil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_AddRecord extends BaseActivity {

	private EditText MoneySetting;
	private EditText TimeSetting;
	private EditText RemarkSetting;
	private EditText dialog_input_edittext;
	private TextView dialog_input_text;
	private RecyclerView mRecyclerView;
	private List<String> mDatas;
	private RecyclerViewAdapter mAdapter;
	private Button dialog_cannle, dialog_sure;

	private String title;
	private String name;
	private String type;
	private String typeflag;
	TypeDBdao typeDBdao;

	private Account account;
	Dialog alertDialog;

	private static final String type_expend = "0";// ֧�����ͱ�־
	private static final String type_income = "1";// �������ͱ�־
	private static final int earning_expend = 0;// ֧�����ͱ�־
	private static final int earning_income = 1;// �������ͱ�־

	// ʱ��ѡ��������
	private static final int Time_requestCode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.activity_add_record);

		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		title = intent.getStringExtra("title");
		typeflag = intent.getStringExtra("typeflag");

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
				saveData();
			}
		});

		setButtonOnClickListener("���ذ�ť", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-gefninerated method stub
				Intent intent = new Intent(Activity_AddRecord.this, Activity_SpecificData.class);
				intent.putExtra("name", name);
				intent.putExtra("title", title);
				setResult(RESULT_OK, intent);
				finish();
			}
		});

	}

	/**
	 * ����ʱ��ѡ�����
	 */
	public void ShowCalendar() {

		TimeSetting.setText(TimeUtil.GetTime());
		TimeSetting.setInputType(InputType.TYPE_NULL);
		TimeSetting.setFocusable(false);
		TimeSetting.setFocusableInTouchMode(false);
		TimeSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Activity_AddRecord.this, Activity_Calendar.class);
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
			switch (requestCode) {
			case 0:
				switch (resultCode) {
				case 0:
					Bundle bundle = data.getBundleExtra("Calendar");
					if (!bundle.getString("time").isEmpty()) {
						TimeSetting.setText(bundle.getString("time"));
					}
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

	/**
	 * ��ʼ��mDatas
	 */
	protected void initData() {
		mDatas = new ArrayList<String>() {
		};
		if (typeflag.equals(type_expend)) {
			mDatas.add("����");
			mDatas.add("ס��");
			mDatas.add("��װ");
			mDatas.add("��ͨ");
			mDatas.add("����");
		} else {
			mDatas.add("����");
			mDatas.add("����");
			mDatas.add("��ְ");
			mDatas.add("���");
			mDatas.add("Ͷ��");
			mDatas.add("����");
		}

		typeDBdao = new TypeDBdao(this);
		// ��ѯ��Ӧ�˺ŵ�������֧����
		List<Type> typecount = typeDBdao.findAllType(name, typeflag);
		// ���û�ж�Ӧ�����ʹ��ڣ�������Ĭ��ֵ
		if (typecount.size() == 0) {
			typeDBdao.setDefault(mDatas, typeflag, name);
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

		// ���Ϲ���ListViewĬ��ѡ�еĽ��������
		// setAdapter() ��ʵ���첽�� ����������������� ListView �� item ��û������������������һ����Ϣ����ʱ�Ŵ�����
		// Ū�����������������ǰ������еĽ���취��ʹ�� post() �ύһ�� Runnable() ������ Runnable()
		// �ڲ�����Ĭ��ѡ�����ֳ�ʼ��������
		mRecyclerView.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (mRecyclerView.getChildCount() - 1 != 0) {
					ViewGroup parent = (ViewGroup) mRecyclerView.getChildAt(0);
					Button button = (Button) parent.findViewById(R.id.id_num);
					button.setBackgroundResource(R.drawable.shape_item_bg_textview_focused);
					button.setTextColor(getResources().getColor(R.color.black));
					type = mAdapter.getmDatas().get(0);
				}
			}
		});

		mAdapter.setRecOnItemClickLitener(new RecyclerViewAdapter.OnRecItemClickLitener() {
			@Override
			public void onRecItemClick(View view, int position) {

				if (position == mRecyclerView.getChildCount() - 1) {
					// ��ĿҪ�󣺴˴��������Ӱ�ť����
					ShowDialog();
					Toast.makeText(Activity_AddRecord.this, "�������ĵ����¼�", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(Activity_AddRecord.this, position + " click", Toast.LENGTH_SHORT).show();
					// ��ȡRecyclerView��Item���������б���
					for (int i = 0; i < mRecyclerView.getChildCount(); i++) {
						// �������ItemΪRecyclerView�ж�Ӧ��Itemʱ������Item��������Ϊ���ʱ�ı���
						// ����Ļ���������Ϊδ���ʱ�ı���
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
				Toast.makeText(Activity_AddRecord.this, position + " long click", Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * ��ʾ��֧��������Ի���
	 */
	public void ShowDialog() {

		// ��ʾ�Ի���
		View inputForm = getLayoutInflater().inflate(R.layout.dialog_input, null);

		alertDialog = new Dialog(this, R.style.AlertDialogStyle);
		alertDialog.setContentView(inputForm);
		alertDialog.show();
		dialog_input_text = (TextView) inputForm.findViewById(R.id.dialog_input_text);
		dialog_input_edittext = (EditText) inputForm.findViewById(R.id.dialog_input_edittext);
		dialog_input_edittext.setFocusable(true);
		dialog_cannle = (Button) inputForm.findViewById(R.id.dialog_input_cannle);
		dialog_sure = (Button) inputForm.findViewById(R.id.dialog_input_sure);

		if (typeflag.equals(type_expend)) {
			dialog_input_text.setText("���֧������");
		} else {
			dialog_input_text.setText("�����������");
		}

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
					Toast.makeText(Activity_AddRecord.this, "��������������", Toast.LENGTH_SHORT).show();
				} else {
					// �ж����������Ƿ��Ѵ��������ݿ���
					if (typeDBdao.isExist(typeflag, name, incomeType)) {
						Toast.makeText(Activity_AddRecord.this, "�������Ѵ���", Toast.LENGTH_SHORT).show();
						;
					} else {
						// ���������Ͳ��������ݿ��У��򽫸�������ӵ�����
						typeDBdao.addRecord(typeflag, incomeType, name);
						// ����RecyclerView��Item
						mAdapter.addData(mAdapter.getItemCount() - 1, incomeType);
						alertDialog.dismiss();
					}
				}
			}
		});

	}

	public boolean saveData() {
		Float money;
		String time = TimeSetting.getText().toString().trim();
		String remark = RemarkSetting.getText().toString().trim();
		String name = this.name;
		if (MoneySetting.getText().toString().isEmpty()) {
			Toast.makeText(Activity_AddRecord.this, "���������", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			money = Float.parseFloat(MoneySetting.getText().toString());
		}
		if (type == null || type.isEmpty()) {
			if (typeflag.equals(type_expend)) {
				Toast.makeText(Activity_AddRecord.this, "��ѡ��֧������", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(Activity_AddRecord.this, "��ѡ����������", Toast.LENGTH_SHORT).show();
			}
			return false;
		}

		AccountDBdao account = new AccountDBdao(this);
		if (typeflag.equals(type_expend)) {
			account.add(time, money, type, earning_expend, remark, name);
		} else {
			account.add(time, money, type, earning_income, remark, name);
		}

		Toast.makeText(Activity_AddRecord.this, "��Ӽ�¼�ɹ���", Toast.LENGTH_SHORT).show();

		Intent intent = new Intent(Activity_AddRecord.this, Activity_SpecificData.class);
		intent.putExtra("name", name);
		Activity_AddRecord.this.setResult(RESULT_OK, intent);
		finish();

		return true;
	}

	public void initView() {
		MoneySetting = (EditText) findViewById(R.id.MoneySetting);
		TimeSetting = (EditText) findViewById(R.id.TimeSetting);
		RemarkSetting = (EditText) findViewById(R.id.RemarkSetting);
		mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
		mAdapter = new RecyclerViewAdapter(this, mDatas);
		mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
		mRecyclerView.setAdapter(mAdapter);
	}
}
