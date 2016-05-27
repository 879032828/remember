package com.seventh.personalfinance;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.seventh.base.BaseActivity;
import com.seventh.db.Account;
import com.seventh.db.AccountDBdao;
import com.seventh.view.CornerListView;
import com.seventh.util.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Administrator
 *
 */
public class SpecificData extends BaseActivity {
	private Intent intent = null;// ����һ����ͼ
	private String name;// �˺�
	private String title;// ����
	AccountDBdao accountDBdao;// ���ݿ�

	private String time1;
	private String time2;
	private String time3;

	private CornerListView cornerListView = null;// ���ݱ���
	private List<Account> accounts;// �˵�����
	private LayoutInflater inflater;

	//��������¼��������
	private static final int requestCode_addrecord = 0;
	//�޸ļ�¼��������
	private static final int requestCode_change = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.activity_specific_data);
		Log.i("specificData", "onCreate");

		intent = this.getIntent();
		name = intent.getStringExtra("name");// ���������������
		title = intent.getStringExtra("title");// ���������������

		setHideleftButton(title);// ���÷��ؼ�ͷ
		setHideaddButton_right();// �����ҼӰ�ť
		setBackgroudButton_right(R.drawable.bg_add_button_selector);
		setButtonOnClickListener("�Ұ�ť", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (title) {
				case "�����ܶ�":
					Intent intent = new Intent(SpecificData.this, AddRecordActivity.class);
					intent.putExtra("name", name);
					intent.putExtra("title", "��������¼");
					startActivityForResult(intent, requestCode_addrecord);
					break;
				default:
					break;
				}
			}
		});

		setButtonOnClickListener("���ذ�ť", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SpecificData.this, MainActivity.class);
				intent.putExtra("name", name);
				startActivity(intent);
				finish();
			}
		});

		accountDBdao = new AccountDBdao(getApplicationContext());
		// ʱ��
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")); // ��ȡ������ʱ��
		int year = c.get(Calendar.YEAR); // ��ȡ��
		int month = c.get(Calendar.MONTH) + 1; // ��ȡ�·ݣ�0��ʾ1�·�
		int day = c.get(Calendar.DAY_OF_MONTH); // ��ȡ��ǰ����
		time1 = year + "/" + month + "/" + day;
		time2 = year + "/" + month + "%";
		time3 = year + "%";

		// ����listview ֵ
		inflater = LayoutInflater.from(this);
		cornerListView = (CornerListView) findViewById(R.id.lv_specific_data_list);
		GetDataBytitle();

		// ���listview������
		cornerListView.setAdapter(new MyAdapter());
		// listviewѡ��ĵ���¼�
		cornerListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Account account = accounts.get(arg2);
				showMyDialog(account);
				// GoMoreAction(account.getId(), name);

			}
		});
	}

	private void GoMoreAction(int id, String name) {
		// TODO Auto-generated method stub
		intent = new Intent(this, MoreAction.class);
		intent.putExtra("name", name);
		intent.putExtra("id", id + "");
		intent.putExtra("title", title);
		startActivity(intent);
	}

	/**
	 * ����title��ȡ��Ӧ������
	 */
	private void GetDataBytitle() {
		try {
			if (title.equals("�����ܶ�")) {
				accounts = accountDBdao.findTotalIntoByName(name);
			} else if (title.equals("֧���˵�")) {
				accounts = accountDBdao.findTotalOutByName(name);
			} else if (title.equals("��ϸ�˵�")) {
				accounts = accountDBdao.findAllByName(name);
			} else if (title.equals("�����˵�")) {
				accounts = accountDBdao.findSomeTimeByName(name, time1);
			} else if (title.equals("�����˵�")) {
				accounts = accountDBdao.findSomeTimeByName(name, time2);
			} else if (title.equals("�����˵�")) {
				accounts = accountDBdao.findSomeTimeByName(name, time3);
			}
		} catch (Exception e) {
			Toast.makeText(this, "��ȡ����ʧ��", 0).show();
			e.printStackTrace();
		}
	}

	// listview������
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return accounts.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub

			return accounts.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = inflater.inflate(R.layout.specific_data_data, null);
			Account account = accounts.get(position);
			TextView tv_text_time = (TextView) view.findViewById(R.id.ls_sp_tv_time);
			TextView tv_text_type = (TextView) view.findViewById(R.id.ls_sp_tv_type);
			TextView tv_text_money = (TextView) view.findViewById(R.id.ls_sp_tv_money);
			ImageView iv_flag = (ImageView) view.findViewById(R.id.flag);

			tv_text_time.setText(account.getTime());
			tv_text_money.setText(account.getMoney() + "");
			if (account.getRemark().isEmpty()) {
				tv_text_type.setText(account.getType());
			} else {
				tv_text_type.setText(account.getType() + "--" + account.getRemark());
			}
			// ����ListView����ʾΪ����ʱ�����ý��ǰΪ�̵㣬����Ϊ���
			if (account.isEarnings()) {
				iv_flag.setBackgroundResource(R.drawable.point_green);
			} else {
				iv_flag.setBackgroundResource(R.drawable.point_red);
			}
			return view;
		}

	}

	/**
	 * @param account
	 */
	public void showMyDialog(final Account account) {
		View view = getLayoutInflater().inflate(R.layout.dialog_change_delete, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);// ���öԻ��򱳾�Ϊ��ɫ
		builder.setView(view).create();
		final AlertDialog alertDialog = builder.show();

		Button changeButton = (Button) view.findViewById(R.id.data_change);
		Button deleteButton = (Button) view.findViewById(R.id.data_delete);

		// �޸ļ�¼
		changeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SpecificData.this, ChangeRecordActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("changeAccount", account);
				intent.putExtras(bundle);
				intent.putExtra("name", name);
				intent.putExtra("title", "�޸������¼");
				alertDialog.dismiss();
				SpecificData.this.setResult(requestCode_change, intent);
			}
		});
		// ɾ����¼
		deleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDeleteDialog(account);
				alertDialog.dismiss();
			}
		});
	}

	/**
	 * ��ʾɾ���Ի���
	 */
	public void showDeleteDialog(final Account account) {
		View view = getLayoutInflater().inflate(R.layout.dialog_delete, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(SpecificData.this, AlertDialog.THEME_HOLO_LIGHT);
		builder.setView(view);
		final AlertDialog alertDialog = builder.show();

		Button dialog_delete_cannle = (Button) view.findViewById(R.id.dialog_delete_cannle);
		Button dialog_delete_sure = (Button) view.findViewById(R.id.dialog_delete_sure);

		// ȷ��ɾ��
		dialog_delete_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AccountDBdao accountDBdao = new AccountDBdao(SpecificData.this);
				accountDBdao.delete(account.getId(), name);
				onResume();
				alertDialog.dismiss();
			}
		});
		// ȡ��
		dialog_delete_cannle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("specificData", "onResume");
		GetDataBytitle();

		// ���listview������
		cornerListView.setAdapter(new MyAdapter());
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
