package com.seventh.personalfinance;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.seventh.base.BaseActivity;
import com.seventh.db.AccountDBdao;
import com.seventh.db.PersonDBdao;
import com.seventh.view.CornerListView;
import com.seventh.view.DataRange;
import com.seventh.view.MainActivityService;
import com.seventh.view.PieChart;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.seventh.util.*;

public class MainActivity extends BaseActivity implements OnClickListener, OnTouchListener {
	private Intent intent = null;// ����һ����ͼ
	private String name;// �˺�
	private String pwd1;
	private String pwd2;
	AccountDBdao accountDBdao;// ���ݿ�
	PersonDBdao persondbdao;

	private Button mButtonAddNodes;// ��һ�ʰ�ť

	private float totalOut;// ��֧��
	private float totalInto;// ������

	private ListView listView1;
	private List<Map<String, String>> map_list1 = null;

	private CornerListView cornerListView2 = null;// �Զ���listview2
	private List<DataRange> dataRanges;
	private LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.activity_mainactivity);
		
		setTitle("����");
		setHideaddButton_left();
		setHidesetting();
		
		initView();
		ViewOperation();
	}

	public void ViewOperation() {
		intent = this.getIntent();
		name = intent.getStringExtra("name");// ���յ�¼���������
		if (name == null) {
			intent = new Intent(this, Login.class);
			startActivity(intent);
			finish();
		} else {
			accountDBdao = new AccountDBdao(getApplicationContext());
			totalOut = accountDBdao.fillTotalOut(name);// ��֧��
			totalInto = accountDBdao.fillTotalInto(name);// ������

			// ����listview1 ֵ
			map_list1 = MainActivityService.getDataSource1(totalInto, totalOut);
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
						TotalIntoData();
						break;
					case 1:
						TotalOutData();
						break;
					case 2:
						TotalAllData();
						break;
					}

				}
			});

			// // ����listview2 ֵ
			// inflater = LayoutInflater.from(this);
			// cornerListView2 = (CornerListView)
			// findViewById(R.id.lv_main_datareport);
			// try {
			// dataRanges = MainActivityService.getDataSource2(name,
			// getApplicationContext());
			// } catch (Exception e) {
			// Toast.makeText(this, "��ȡ����ʧ��", 0).show();
			// e.printStackTrace();
			// }
			// // ���listview2������
			// cornerListView2.setAdapter(new MyAdapter());
			//
			// // listview2ѡ��ĵ���¼� һ����
			// cornerListView2.setOnItemClickListener(new OnItemClickListener()
			// {
			//
			// @Override
			// public void onItemClick(AdapterView<?> arg0, View arg1,
			// int arg2, long arg3) {
			// switch (arg2) {
			// case 0:
			// TodayData();
			// break;
			// case 1:
			// MonthData();
			// break;
			// case 2:
			// YearData();
			// break;
			// }
			// }
			// });

		}
	}

	public void initView() {
		listView1 = (ListView) findViewById(R.id.lv_main_calculation);// �ܶ���ʾ
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_main_addnotes:
			intent = new Intent(this, AddNodes.class);
			intent.putExtra("name", name);
			// ��ֵ �ʻ���
			startActivity(intent);
			break;
		}
	}

	// ��ת�������˵�
	public void TotalIntoData() {
		intent = new Intent(this, SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "�����ܶ�");
		startActivity(intent);
	}

	// ��ת��֧���˵�
	public void TotalOutData() {
		intent = new Intent(this, SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "֧���ܶ�");
		startActivity(intent);
	}

	// ��ת����ϸ�˵�
	public void TotalAllData() {
		intent = new Intent(this, SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "��ϸ�˵�");
		startActivity(intent);
	}

	// ��ת�������˵�
	public void TodayData() {
		intent = new Intent(this, SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "�����˵�");
		startActivity(intent);
	}

	// ��ת�������˵�
	public void MonthData() {
		intent = new Intent(this, SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "�����˵�");
		startActivity(intent);
	}

	// ��ת�������˵�
	public void YearData() {
		intent = new Intent(this, SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "�����˵�");
		startActivity(intent);
	}

	// ����ʱ��
//	public String GetTime() {
//		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00")); // ��ȡ������ʱ��
//		int year = c.get(Calendar.YEAR); // ��ȡ��
//		int month = c.get(Calendar.MONTH) + 1; // ��ȡ�·ݣ�0��ʾ1�·�
//		int day = c.get(Calendar.DAY_OF_MONTH); // ��ȡ��ǰ����
//		String time = year + "/" + month + "/" + day;
//		return time;
//	}

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

		map_list1 = MainActivityService.getDataSource1(totalInto, totalOut);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
