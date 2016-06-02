package com.seventh.personalfinance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

public class Activity_AppStart extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity__app_start);

		TextView appname = (TextView) findViewById(R.id.appname);

		// 初始化透明度动画
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(2000);
		appname.startAnimation(alphaAnimation);
		// 设置动画监听事件
		alphaAnimation.setAnimationListener(new AnimationListener() {

			//当动画结束时，跳转到登录界面
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Activity_AppStart.this, Activity_Login.class);
				startActivity(intent);
				finish();
			}
		});
		// new Handler().postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// Intent intent = new
		// Intent(Activity_AppStart.this,Activity_Login.class);
		// startActivity(intent);
		// finish();
		// }
		// }, 2000);
	}

}
