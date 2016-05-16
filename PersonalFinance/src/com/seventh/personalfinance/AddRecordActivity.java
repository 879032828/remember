package com.seventh.personalfinance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.seventh.base.BaseActivity;
import com.seventh.base.RecyclerViewAdapter;
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

public class AddRecordActivity extends BaseActivity {

	private EditText MoneySetting;
	private EditText TimeSetting;
	private EditText RemarkSetting;
	private EditText dialog_input_edittext;
	private RecyclerView mRecyclerView;
	private List<String> mDatas;
	private RecyclerViewAdapter mAdapter;
	private Button dialog_cannle, dialog_sure;

	private String name;
	TypeDBdao typeDBdao;

	// 时间选择请求码
	private static final int Time_requestCode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseSetContentView(R.layout.activity_add_record);
		setHideleftButton("添加收入记录");
		setHideaddButton_right();
		
		Intent intent = getIntent();
		name = intent.getStringExtra("name");

		initData();
		initView();
		ShowCalendar();
		initEvent();

		mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
		mRecyclerView.setAdapter(mAdapter);

	}
	
	/**
	 * 设置时间选择操作
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
				Intent intent = new Intent(AddRecordActivity.this, CalendarActivity.class);
				startActivityForResult(intent, Time_requestCode);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent) 获取日历Activity返回的结果，并在文本编辑框中显示
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

	protected void initData() {
		mDatas = new ArrayList<String>() {
		};
		mDatas.add("工资");
		mDatas.add("奖金");
		mDatas.add("兼职");
		mDatas.add("理财");
		mDatas.add("投资");
		mDatas.add("其他");
		
		typeDBdao = new TypeDBdao(this);
		// 查询对应账号的所有收入类型
		List<Type> typecount = typeDBdao.findAllType(name, "0");
		// 如果没有对应的类型存在，则设置默认值
		if (typecount.size() == 0) {
			typeDBdao.setDefault(mDatas, name);
			mDatas.add("++");// 设置增加按钮的内容，保证增加按钮一直处于列表最后面
		} else {
			// 若数据库中存在类型，则清空mDatas，并重新获取数据库中的类型，填充mDatas
			mDatas.clear();
			mDatas = typeDBdao.toListString(typecount);
			mDatas.add("++");// 设置增加按钮的内容，保证增加按钮一直处于列表最后面
		}
	}

	private void initEvent() {

		mAdapter.setRecOnItemClickLitener(new RecyclerViewAdapter.OnRecItemClickLitener() {
			@Override
			public void onRecItemClick(View view, int position) {

				if (position == mRecyclerView.getChildCount() - 1) {
					// 项目要求：此处设置增加按钮操作
					ShowDialog();
					Toast.makeText(AddRecordActivity.this, "这是最后的单击事件", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(AddRecordActivity.this, position + " click", Toast.LENGTH_SHORT).show();
					// 获取RecyclerView的Item个数，进行遍历
					for (int i = 0; i < mRecyclerView.getChildCount(); i++) {
						// 当点击的Item为RecyclerView中对应的Item时，将该Item背景设置为点击时的背景
						// 否则的话，则设置为未点击时的背景
						if (position == i) {
							ViewGroup parent = (ViewGroup) mRecyclerView.getChildAt(i);
							Button button = (Button) parent.findViewById(R.id.id_num);
							button.setBackgroundResource(R.drawable.item_bg_textview_focused);
							button.setTextColor(getResources().getColor(R.color.black));
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
				Toast.makeText(AddRecordActivity.this, position + " long click", Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void ShowDialog() {

		// 显示对话框
		View inputForm = getLayoutInflater().inflate(R.layout.dialog_input, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(inputForm).create();
		final AlertDialog alertDialog = builder.show();

		dialog_input_edittext = (EditText) inputForm.findViewById(R.id.dialog_input_edittext);
		dialog_input_edittext.setFocusable(true);
		dialog_cannle = (Button) inputForm.findViewById(R.id.dialog_cannle);
		dialog_sure = (Button) inputForm.findViewById(R.id.dialog_sure);
		// 对话框退出
		dialog_cannle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog.dismiss();
			}
		});
		// 对话框确认
		dialog_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String incomeType = dialog_input_edittext.getText().toString().trim();
				if (incomeType.isEmpty()) {
					Toast.makeText(AddRecordActivity.this, "请输入类型名称", Toast.LENGTH_SHORT).show();
				} else {
					// 判断输入类型是否已存在于数据库中
					if (typeDBdao.isExist("0", name, incomeType)) {
						Toast.makeText(AddRecordActivity.this, "该类型已存在", Toast.LENGTH_SHORT).show();
						;
					} else {
						// 若输入类型不存在数据库中，则将该类型添加到表中
						typeDBdao.addRecord("0", incomeType, name);
						// 增加RecyclerView的Item
						mAdapter.addData(mAdapter.getItemCount() - 1, incomeType);
						alertDialog.dismiss();
					}

				}
			}
		});

	}

	public void initView() {
		MoneySetting = (EditText) findViewById(R.id.MoneySetting);
		TimeSetting = (EditText) findViewById(R.id.TimeSetting);
		RemarkSetting = (EditText) findViewById(R.id.RemarkSetting);
		mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
		mAdapter = new RecyclerViewAdapter(this, mDatas);
	}
}
