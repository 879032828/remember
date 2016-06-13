package com.seventh.base;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.seventh.personalfinance.R;
import com.seventh.util.TimeUtil;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
public class SlidingMenuBaseActivity extends SlidingFragmentActivity {

	private TextView title;
	private TextView time;
	private Button leftButton;
	private ImageButton addButton_left;
	private ImageButton addButton_right;
	private Button andButton_save;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		View view = findViewById(R.id.statusView);
		SettingStatusView(view);

		initview();

		 // 透明状态栏
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		 // 透明导航栏
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	}

	//修改了slidingmenu中的这一部分，改变了状态栏的颜色
	// protected boolean fitSystemWindows(Rect insets) {
	// int leftPadding = insets.left;
	// int rightPadding = insets.right;
	// int topPadding = insets.top;
	// int bottomPadding = insets.bottom;
	// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
	// topPadding = 0;
	// }
	// if (!mActionbarOverlay) {
	// Log.v(TAG, "setting padding!");
	// setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
	// }
	// return true;
	// }
	
	/**
	 * 获取状态栏和标题栏的高度，并设置View的高度
	 */
	public void SettingStatusView(View view) {
		Rect rect = new Rect();
		Window window = getWindow();
		view.getWindowVisibleDisplayFrame(rect);
		// 状态栏的高度
		int statusBarHeight = rect.top;

		// 设置控件的高度
		WindowManager windowManager = this.getWindowManager();
		int height = windowManager.getDefaultDisplay().getHeight();
		LayoutParams para = view.getLayoutParams();
		para.height = getStatusHeight(this);
		para.width = view.getLayoutParams().width;
		view.setLayoutParams(para);
	}
	
	/**
	 * 获得状态栏的高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusHeight(Context context) {
	 
	    int statusHeight = -1;
	    try {
	        Class<?> clazz = Class.forName("com.android.internal.R$dimen");
	        Object object = clazz.newInstance();
	        int height = Integer.parseInt(clazz.getField("status_bar_height")
	                .get(object).toString());
	        statusHeight = context.getResources().getDimensionPixelSize(height);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return statusHeight;
	}

	/**
	 * @param layoutResId
	 *            继承该BaseActivity的Activity需使用该方法填充布局文件
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

		// 一开始设置所有控件不可见，在具体界面中需要时才进行显示
		title.setVisibility(View.INVISIBLE);
		time.setVisibility(View.INVISIBLE);
		leftButton.setVisibility(View.INVISIBLE);
		addButton_left.setVisibility(View.INVISIBLE);
		addButton_right.setVisibility(View.INVISIBLE);
		andButton_save.setVisibility(View.INVISIBLE);
	}

	/**
	 * @param titleString
	 *            设置标题可见及自定义标题文本
	 */
	public void setTitle(String titleString) {
		title.setText(titleString);
		title.setVisibility(View.VISIBLE);

	}

	/**
	 * 设置标题时间
	 */
	public void setTime() {
		time.setText(TimeUtil.GetTime());
		time.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置Button可见及按钮文本
	 */
	public void setHideleftButton(String text) {
		leftButton.setVisibility(View.VISIBLE);// 返回按钮
		leftButton.setText(text);
	}

	public void setHideaddButton_left() {
		addButton_left.setVisibility(View.VISIBLE);// 左按钮
	}

	public void setBackgroudButton_left(int resid) {
		addButton_left.setImageResource(resid);
	}

	public void setHideaddButton_right() {
		addButton_right.setVisibility(View.VISIBLE);// 右按钮
	}

	public void setBackgroudButton_right(int resid) {
		addButton_right.setImageResource(resid);
		addButton_right.setScaleType(ScaleType.CENTER_INSIDE);
	}

	public void setHideaddButton_save() {
		andButton_save.setVisibility(View.VISIBLE);// 保存按钮
	}

	/**
	 * @param onClickListener
	 *            设置按钮监听事件
	 */
	public void setButtonOnClickListener(String buttonname, OnClickListener onClickListener) {
		switch (buttonname) {
		case "返回按钮":
			leftButton.setOnClickListener(onClickListener);
			break;
		case "左按钮":
			addButton_left.setOnClickListener(onClickListener);
			break;
		case "右按钮":
			addButton_right.setOnClickListener(onClickListener);
		case "保存按钮":
			andButton_save.setOnClickListener(onClickListener);
		default:
			break;
		}

	}
}
