package com.seventh.personalfinance;

import java.util.List;
import java.util.Map;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.seventh.base.SlidingMenuBaseActivity;
import com.seventh.db.AccountDBdao;
import com.seventh.db.BudgetDBdao;
import com.seventh.db.PersonDBdao;
import com.seventh.view.CornerListView;
import com.seventh.view.DataRange;
import com.seventh.view.MainActivityService;
import com.seventh.view.TasksCompletedView;

import android.R.integer;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
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

public class MainActivity extends SlidingMenuBaseActivity {
	private Intent intent = null;// ����һ����ͼ
	private String name;// �˺�
	private String pwd1;
	private String pwd2;
	private AccountDBdao accountDBdao;// ���ݿ�
	private PersonDBdao persondbdao;

	private float totalOut;// ��֧��
	private float totalInto;// ������

	private ListView listView1;
	private List<Map<String, String>> map_list1 = null;

	private TasksCompletedView mTasksCompletedView;
	private CornerListView cornerListView2 = null;// �Զ���listview2
	private List<DataRange> dataRanges;
	private LayoutInflater inflater;
	private ImageView ImageView_main;
	private EditText dialog_input_edittext;
	private TextView dialog_input_text;
	private Button dialog_input_sure;
	private Button dialog_input_cannle;
	private SlidingMenu slidingMenu;

	private static final int BudgetBalance = 0;
	private static final int TotalIntoData = 1;
	private static final int TotalOutData = 2;
	private static final int MonthIntoData = 3;
	private static final int MonthOutData = 4;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.activity_mainactivity);

		initView();
		ViewOperation();

		// SlidingMenu��ʼ��
		initRightMenu();

		// �ڿؼ���ɺ�ִ��SettingView�������¿ؼ��ĸ߶�
		mTasksCompletedView.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SettingView(mTasksCompletedView);
			}
		});

		setTitle("����");
		setTime();
		// ����������ť�ɼ�������
		setHideaddButton_right();
		setBackgroudButton_right(R.drawable.widget_bar_news_over);
		setButtonOnClickListener("�Ұ�ť", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				slidingMenu.showMenu();
			}
		});

	}

	/**
	 * ��ȡ״̬���ͱ������ĸ߶ȣ�������ImageView�ĸ߶�
	 */
	public void SettingView(View view) {
		Rect rect = new Rect();
		Window window = getWindow();
		view.getWindowVisibleDisplayFrame(rect);
		// ״̬���ĸ߶�
		int statusBarHeight = rect.top;
		// �������ĸ߶�
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.main_relative);
		int contentViewTop = relativeLayout.getHeight();

		// ���ÿؼ��ĸ߶�
		WindowManager windowManager = this.getWindowManager();
		int height = windowManager.getDefaultDisplay().getHeight();
		LayoutParams para = view.getLayoutParams();
		para.height = height / 2 - statusBarHeight - contentViewTop;
		para.width = view.getLayoutParams().width;
		view.setLayoutParams(para);
	}

	/**
	 * RecyclerView�б��ʼ��
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
					case BudgetBalance:
						// ����Ԥ�����
						BudgetBalance();
						break;
					case TotalIntoData:
						// �����ܶ�
						TotalIntoData();
						break;
					case TotalOutData:
						// ֧���ܶ�
						TotalOutData();
						break;
					case MonthIntoData:
						// ��������
						MonthIntoData();
						break;
					case MonthOutData:
						// ����֧��
						MonthOutData();
						break;
					}

				}
			});

			Float budget = (float) 0;// Ԥ�����
			Float ThisMonth_expend = (float) 0;// ����֧��
			for (Map<String, String> map : map_list1) {
				switch (map.get("txtCalculationName")) {
				case "Ԥ�����":
					budget = Float.parseFloat(map.get("txtMoney"));
					break;
				case "����֧��":
					ThisMonth_expend = Float.parseFloat(map.get("txtMoney"));
				default:
					break;
				}
			}

			Float residue = budget - ThisMonth_expend;
			if (residue >= 0) {
				mTasksCompletedView.setProgress(new Float(ThisMonth_expend).intValue());
				mTasksCompletedView.setTotalProgress(new Float(budget).intValue());
				mTasksCompletedView.setText("Ԥ��ʣ��_" + new Float(residue).intValue() + "Ԫ");
			} else {
				mTasksCompletedView.setProgress(new Float(ThisMonth_expend).intValue());
				mTasksCompletedView.setTotalProgress(new Float(budget).intValue());
				mTasksCompletedView.setText("Ԥ�㳬֧_" + new Float(residue).intValue() + "Ԫ");
			}
		}
	}

	/**
	 * ��ʼ������
	 */
	public void initView() {
		listView1 = (ListView) findViewById(R.id.lv_main_calculation);// �ܶ���ʾ
		mTasksCompletedView = (TasksCompletedView) findViewById(R.id.tasks_view);// Բ��������

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

	// ��ת����������
	public void MonthIntoData() {
		intent = new Intent(this, Activity_SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "��������");
		intent.putExtra("typeflag", "1");
		startActivity(intent);
		finish();
	}

	// ��ת������֧��
	public void MonthOutData() {
		intent = new Intent(this, Activity_SpecificData.class);
		intent.putExtra("name", name);
		intent.putExtra("title", "����֧��");
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
			// �������Ԥ�㣬����ʾ
			String budget = Float.toString(budgetDBdao.findBudget(name).getBudget());
			dialog_input_edittext.setText(budget);
		} else {
			dialog_input_edittext.setText(0 + "");
			budgetDBdao.addBudget(0, name);
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
					Float budgetInt = Float.parseFloat(dialog_input_edittext.getText().toString());
					budgetDBdao.updateBudget(budgetInt, name);
					Toast.makeText(MainActivity.this, "���óɹ�", Toast.LENGTH_SHORT).show();
					updateView();
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

	private void initRightMenu() {
		Fragment leftMenuFragment = new MenuLeftFragment();
		setBehindContentView(R.layout.layout_left_menu);
		Bundle bundle = new Bundle();
		bundle.putString("name", name);
		leftMenuFragment.setArguments(bundle);
		getSupportFragmentManager().beginTransaction().replace(R.id.id_left_menu_frame, leftMenuFragment).commit();

		slidingMenu = getSlidingMenu();
		slidingMenu.setMode(SlidingMenu.LEFT);
		// ���ô�����Ļ��ģʽ
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
		slidingMenu.setShadowDrawable(R.drawable.shape_shadow);
		// ���û����˵���ͼ�Ŀ��
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// menu.setBehindWidth()
		// ���ý��뽥��Ч����ֵ
		slidingMenu.setFadeDegree(0.35f);
	}

	public void updateView() {
		totalOut = accountDBdao.fillTotalOut(name);// ��֧��
		totalInto = accountDBdao.fillTotalInto(name);// ������

		map_list1 = MainActivityService.getDataSource1(totalInto, totalOut, name, this);
		// listview1������
		SimpleAdapter adapter1 = new SimpleAdapter(getApplicationContext(), map_list1,
				R.layout.main_listview_calculation, new String[] { "txtCalculationName", "txtMoney" },
				new int[] { R.id.ls_tv_txtCalculationName, R.id.ls_tv_txtMoney });
		// ���listview1������
		listView1.setAdapter(adapter1);

		Float budget = (float) 0;// Ԥ�����
		Float ThisMonth_expend = (float) 0;// ����֧��
		for (Map<String, String> map : map_list1) {
			switch (map.get("txtCalculationName")) {
			case "Ԥ�����":
				budget = Float.parseFloat(map.get("txtMoney"));
				break;
			case "����֧��":
				ThisMonth_expend = Float.parseFloat(map.get("txtMoney"));
			default:
				break;
			}
		}

		Float residue = budget - ThisMonth_expend;
		if (residue >= 0) {
			mTasksCompletedView.setProgress(new Float(ThisMonth_expend).intValue());
			mTasksCompletedView.setTotalProgress(new Float(budget).intValue());
			mTasksCompletedView.setText("Ԥ��ʣ��_" + new Float(residue).intValue() + "Ԫ");
		} else {
			mTasksCompletedView.setProgress(new Float(ThisMonth_expend).intValue());
			mTasksCompletedView.setTotalProgress(new Float(budget).intValue());
			mTasksCompletedView.setText("Ԥ�㳬֧_" + new Float(residue).intValue() + "Ԫ");
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
