package com.seventh.personalfinance;

import java.util.List;
import java.util.Map;

import com.seventh.base.BaseActivity;
import com.seventh.db.AccountDBdao;
import com.seventh.db.BudgetDBdao;
import com.seventh.db.PersonDBdao;
import com.seventh.view.CornerListView;
import com.seventh.view.DataRange;
import com.seventh.view.MainActivityService;

import android.R.integer;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
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

public class MainActivity extends BaseActivity {
	private Intent intent = null;// ����һ����ͼ
	private String name;// �˺�
	private String pwd1;
	private String pwd2;
	AccountDBdao accountDBdao;// ���ݿ�
	PersonDBdao persondbdao;

	private float totalOut;// ��֧��
	private float totalInto;// ������

	private ListView listView1;
	private List<Map<String, String>> map_list1 = null;

	private CornerListView cornerListView2 = null;// �Զ���listview2
	private List<DataRange> dataRanges;
	private LayoutInflater inflater;
	private ImageView ImageView_main;
	private EditText dialog_input_edittext;
	private TextView dialog_input_text;
	private Button dialog_input_sure;
	private Button dialog_input_cannle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.activity_mainactivity);

		ImageView_main = (ImageView) findViewById(R.id.ImageView_main);
		// �ڿؼ���ɺ�ִ��SettingView�������¿ؼ��ĸ߶�
		ImageView_main.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				SettingView();
			}
		});

		setTitle("����");
		// ����������ť�ɼ�������
		setHideaddButton_left();
		setBackgroudButton_left(R.drawable.shape_bg_add_button);
		setHideaddButton_right();
		setBackgroudButton_right(R.drawable.selector_bg_setting_button);

		initView();
		ViewOperation();

	}

	/**
	 * ��ȡ״̬���ͱ������ĸ߶ȣ�������ImageView�ĸ߶�
	 */
	public void SettingView() {
		Rect rect = new Rect();
		Window window = getWindow();
		ImageView_main.getWindowVisibleDisplayFrame(rect);
		// ״̬���ĸ߶�
		int statusBarHeight = rect.top;
		// �������ĸ߶�
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_relative);
		int contentViewTop = relativeLayout.getHeight();

		// ���ÿؼ��ĸ߶�
		WindowManager windowManager = this.getWindowManager();
		int height = windowManager.getDefaultDisplay().getHeight();
		LayoutParams para = ImageView_main.getLayoutParams();
		para.height = height / 2 - statusBarHeight - contentViewTop;
		para.width = ImageView_main.getLayoutParams().width;
		ImageView_main.setLayoutParams(para);
	}

	/**
	 * 
	 */
	public void ViewOperation() {
		intent = this.getIntent();
		name = intent.getStringExtra("name");// �������������Intent�е�nameֵ
		if (name == null) {
			intent = new Intent(this, Activity_Login.class);
			startActivity(intent);
			finish();
		} else {
			accountDBdao = new AccountDBdao(getApplicationContext());
			totalOut = accountDBdao.fillTotalOut(name);// ��ȡ��֧��
			totalInto = accountDBdao.fillTotalInto(name);// ��ȡ������

			// ����listview1 ֵ
			map_list1 = MainActivityService.getDataSource1(totalInto, totalOut, name, this);
			// listview1������
			SimpleAdapter adapter1 = new SimpleAdapter(getApplicationContext(), map_list1,
					R.layout.main_listview_calculation, new String[] { "txtCalculationName", "txtMoney" },
					new int[] { R.id.ls_tv_txtCalculationName, R.id.ls_tv_txtMoney });
			// ���listview1������
			listView1.setAdapter(adapter1);

			// listview1ѡ��ĵ���¼� �����ܶ� ֧���ܶ� Ԥ�����
			listView1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					switch (arg2) {
					case 0:
						// ����Ԥ�����
						BudgetBalance();
						break;
					case 1:
						// �����ܶ�
						TotalIntoData();
						break;
					case 2:
						// ֧���ܶ�
						TotalOutData();
						break;
					}

				}
			});
		}
	}

	/**
	 * ��ʼ������
	 */
	public void initView() {
		listView1 = (ListView) findViewById(R.id.lv_main_calculation);// �ܶ���ʾ

	}

	// ��ת���������˵�
	public void TotalIntoData() {
		intent = new Intent(this, Activity_SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "�����ܶ�");
		intent.putExtra("typeflag", "1");
		startActivity(intent);
		finish();
	}

	// ��ת����֧���˵�
	public void TotalOutData() {
		intent = new Intent(this, Activity_SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "֧���ܶ�");
		intent.putExtra("typeflag", "0");
		startActivity(intent);
		finish();
	}

	// Ԥ�����
	public void BudgetBalance() {
		View view = getLayoutInflater().inflate(R.layout.dialog_input, null);
		final Dialog dialog = new Dialog(this, R.style.AlertDialogStyle);
		dialog.setContentView(view);
		dialog.show();

		dialog_input_edittext = (EditText) view.findViewById(R.id.dialog_input_edittext);
		dialog_input_text = (TextView) view.findViewById(R.id.dialog_input_text);
		dialog_input_sure = (Button) view.findViewById(R.id.dialog_input_sure);
		dialog_input_cannle = (Button) view.findViewById(R.id.dialog_input_cannle);
		dialog_input_text.setText("���ñ�������Ԥ��");

		BudgetDBdao budgetDBdao = new BudgetDBdao(this);
		if (budgetDBdao.isExistBudget(name)) {
			dialog_input_edittext.setText(Float.toString(budgetDBdao.findBudget(name).getBudget()));
		} else {
			dialog_input_edittext.setText(0 + "");
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
					budgetDBdao.addBudget(Integer.parseInt(dialog_input_edittext.getText().toString()), name);
					Toast.makeText(MainActivity.this, "���óɹ�", Toast.LENGTH_SHORT).show();
					onResume();
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

	// ��ת����ϸ�˵�
	public void TotalAllData() {
		intent = new Intent(this, Activity_SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "��ϸ�˵�");
		startActivity(intent);
		finish();
	}

	// ��ת�������˵�
	public void TodayData() {
		intent = new Intent(this, Activity_SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "�����˵�");
		startActivity(intent);
		finish();
	}

	// ��ת�������˵�
	public void MonthData() {
		intent = new Intent(this, Activity_SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "�����˵�");
		startActivity(intent);
		finish();
	}

	// ��ת�������˵�
	public void YearData() {
		intent = new Intent(this, Activity_SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "�����˵�");
		startActivity(intent);
		finish();
	}

	// listview2������
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		totalOut = accountDBdao.fillTotalOut(name);// ��֧��
		totalInto = accountDBdao.fillTotalInto(name);// ������

		map_list1 = MainActivityService.getDataSource1(totalInto, totalOut, name, this);
		// listview1������
		SimpleAdapter adapter1 = new SimpleAdapter(getApplicationContext(), map_list1,
				R.layout.main_listview_calculation, new String[] { "txtCalculationName", "txtMoney" },
				new int[] { R.id.ls_tv_txtCalculationName, R.id.ls_tv_txtMoney });
		// ���listview1������
		listView1.setAdapter(adapter1);

		try {
			dataRanges = MainActivityService.getDataSource2(name, getApplicationContext());
		} catch (Exception e) {
			Toast.makeText(this, "��ȡ����ʧ��", 0).show();
			e.printStackTrace();
		}
		// // ���listview2������
		// cornerListView2.setAdapter(new MyAdapter());
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
