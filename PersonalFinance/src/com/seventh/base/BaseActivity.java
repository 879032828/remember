package com.seventh.base;

import android.app.Activity;
import android.content.Context;

import com.seventh.personalfinance.R;
import com.seventh.personalfinance.R.id;
import com.seventh.personalfinance.R.layout;
import com.seventh.personalfinance.R.menu;
import com.seventh.util.TimeUtil;

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
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class BaseActivity extends Activity {

	private TextView title;
	private TextView time;
	private Button leftButton;
	private ImageButton addButton_left;
	private ImageButton addButton_right;
	private Button andButton_save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		initview();
		
		setStatus();
		
		// // ͸��״̬��
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// // ͸��������
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	}

	public void setStatus() {
		Window window = this.getWindow();
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		layoutParams.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		window.setAttributes(layoutParams);
	}
	
	/**
	 * @param layoutResId
	 *            �̳и�BaseActivity��Activity��ʹ�ø÷�����䲼���ļ�
	 */
	public void baseSetContentView(int layoutResId) {
		LinearLayout llContent = (LinearLayout) findViewById(R.id.content);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(layoutResId, null);
		llContent.addView(v);
	}

	public void initview() {
		title = (TextView) findViewById(R.id.title);
		time = (TextView) findViewById(R.id.time);
		leftButton = (Button) findViewById(R.id.leftButton);
		addButton_left = (ImageButton) findViewById(R.id.addButton_left);
		addButton_right = (ImageButton) findViewById(R.id.addButton_right);
		andButton_save = (Button) findViewById(R.id.save);

		// һ��ʼ�������пؼ����ɼ����ھ����������Ҫʱ�Ž�����ʾ
		title.setVisibility(View.INVISIBLE);
		time.setVisibility(View.INVISIBLE);
		leftButton.setVisibility(View.INVISIBLE);
		addButton_left.setVisibility(View.INVISIBLE);
		addButton_right.setVisibility(View.INVISIBLE);
		andButton_save.setVisibility(View.INVISIBLE);
	}

	/**
	 * @param titleString
	 *            ���ñ���ɼ����Զ�������ı�
	 */
	public void setTitle(String titleString) {
		title.setText(titleString);
		title.setVisibility(View.VISIBLE);
		
	}

	/**
	 * ���ñ���ʱ��
	 */
	public void setTime(){
		time.setText(TimeUtil.GetTime());
		time.setVisibility(View.VISIBLE);
	}
	
	/**
	 * ����Button�ɼ�����ť�ı�
	 */
	public void setHideleftButton(String text) {
		leftButton.setVisibility(View.VISIBLE);// ���ذ�ť
		leftButton.setText(text);
	}

	public void setHideaddButton_left() {
		addButton_left.setVisibility(View.VISIBLE);// ��ť
	}
	public void setBackgroudButton_left(int resid){
		addButton_left.setImageResource(resid);
	}

	public void setHideaddButton_right() {
		addButton_right.setVisibility(View.VISIBLE);// �Ұ�ť
	}
	
	public void setBackgroudButton_right(int resid){
		addButton_right.setImageResource(resid);
		addButton_right.setScaleType(ScaleType.CENTER_INSIDE);
	}

	public void setHideaddButton_save() {
		andButton_save.setVisibility(View.VISIBLE);// ���水ť
	}

	
	/**
	 * @param onClickListener
	 *            ���ð�ť�����¼�
	 */
	public void setButtonOnClickListener(String buttonname, OnClickListener onClickListener) {
		switch (buttonname) {
		case "���ذ�ť":
			leftButton.setOnClickListener(onClickListener);
			break;
		case "��ť":
			addButton_left.setOnClickListener(onClickListener);
			break;
		case "�Ұ�ť":
			addButton_right.setOnClickListener(onClickListener);
		case "���水ť":
			andButton_save.setOnClickListener(onClickListener);
		default:
			break;
		}

	}
}
