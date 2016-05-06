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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);

		 //透明状态栏
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		 //透明导航栏
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	}

	public void baseSetContentView(int layoutResId) {
		LinearLayout llContent = (LinearLayout) findViewById(R.id.content);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(layoutResId, null);
		llContent.addView(v);
	}

	
	/**
	 * @param titleString
	 * 设置自定义标题
	 */
	public void setTitle(String titleString){
		title = (TextView) findViewById(R.id.title);
		title.setText(titleString);
	}
	
	/**
	 * 设置TextView是否隐藏
	 */
	public void setHideTitle(){
		if(title != null){
			title.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * @param onClickListener
	 * 设置按钮监听事件
	 */
	public void setButtonOnClickListener(OnClickListener onClickListener){
		leftButton = (Button) findViewById(R.id.leftButton);
		leftButton.setOnClickListener(onClickListener);
	}
}
