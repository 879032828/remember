package com.seventh.base;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.Toolbar;

import com.seventh.personalfinance.R;
import com.seventh.personalfinance.R.id;
import com.seventh.personalfinance.R.layout;
import com.seventh.personalfinance.R.menu;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class BaseActivity extends Activity {

	private TextView title;
	private Button leftButton;
	private Button addButton_left;
	private Button addButton_right;
	private Button setting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);

		initview();
		
		 //͸��״̬��
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		 //͸��������
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	}

	
	/**
	 * @param layoutResId
	 * �̳и�BaseActivity��Activity��ʹ�ø÷�����䲼���ļ�
	 */
	public void baseSetContentView(int layoutResId) {
		LinearLayout llContent = (LinearLayout) findViewById(R.id.content);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(layoutResId, null);
		llContent.addView(v);
	}

	public void initview(){
		title = (TextView) findViewById(R.id.title);
		leftButton = (Button) findViewById(R.id.leftButton);
		addButton_left = (Button) findViewById(R.id.addButton_left);
		addButton_right =(Button) findViewById(R.id.addButton_right);
		setting = (Button) findViewById(R.id.setting);
		
		//һ��ʼ�������пؼ����ɼ����ھ����������Ҫʱ�Ž�����ʾ
		title.setVisibility(View.INVISIBLE);
		leftButton.setVisibility(View.INVISIBLE);
		addButton_left.setVisibility(View.INVISIBLE);
		addButton_right.setVisibility(View.INVISIBLE);
		setting.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * @param titleString
	 * �����Զ������
	 */
	public void setTitle(String titleString){
		title.setText(titleString);
		title.setVisibility(View.VISIBLE);
		
	}
	


	
	/**
	 * ����Button�ɼ�
	 */
	public void setHideleftButton(String text){
		leftButton.setVisibility(View.VISIBLE);//���ذ�ť
		leftButton.setText(text);
	}
	public void setHideaddButton_left(){
		addButton_left.setVisibility(View.VISIBLE);//��Ӱ�ť
	}
	public void setHideaddButton_right(){
		addButton_right.setVisibility(View.VISIBLE);//�ҼӰ�ť
	}
	public void setHidesetting(){
		setting.setVisibility(View.VISIBLE);//���ð�ť
	}
	/**
	 * @param onClickListener
	 * ���ð�ť�����¼�
	 */
	public void setButtonOnClickListener(String buttonname, OnClickListener onClickListener){
		switch (buttonname) {
		case "���ذ�ť":
			leftButton.setOnClickListener(onClickListener);
			break;
		case "��Ӱ�ť":
			addButton_left.setOnClickListener(onClickListener);
			break;
		case "�ҼӰ�ť":
			addButton_left.setOnClickListener(onClickListener);
		case "���ð�ť":
			setting.setOnClickListener(onClickListener);
		default:
			break;
		}
		
	}
}
