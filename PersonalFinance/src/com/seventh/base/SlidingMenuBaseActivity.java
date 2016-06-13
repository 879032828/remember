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

		 // ͸��״̬��
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		 // ͸��������
		 getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	}

	//�޸���slidingmenu�е���һ���֣��ı���״̬������ɫ
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
	 * ��ȡ״̬���ͱ������ĸ߶ȣ�������View�ĸ߶�
	 */
	public void SettingStatusView(View view) {
		Rect rect = new Rect();
		Window window = getWindow();
		view.getWindowVisibleDisplayFrame(rect);
		// ״̬���ĸ߶�
		int statusBarHeight = rect.top;

		// ���ÿؼ��ĸ߶�
		WindowManager windowManager = this.getWindowManager();
		int height = windowManager.getDefaultDisplay().getHeight();
		LayoutParams para = view.getLayoutParams();
		para.height = getStatusHeight(this);
		para.width = view.getLayoutParams().width;
		view.setLayoutParams(para);
	}
	
	/**
	 * ���״̬���ĸ߶�
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
	public void setTime() {
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

	public void setBackgroudButton_left(int resid) {
		addButton_left.setImageResource(resid);
	}

	public void setHideaddButton_right() {
		addButton_right.setVisibility(View.VISIBLE);// �Ұ�ť
	}

	public void setBackgroudButton_right(int resid) {
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