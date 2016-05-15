package com.zhy.sample.demo_recyclerview;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.zhy.sample.demo_recyclerview.HomeAdapter.OnItemClickLitener;

public class HomeActivity extends Activity {

	private RecyclerView mRecyclerView;
	private List<String> mDatas;
	private HomeAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_recyclerview);

		initData();

		initView();

		// mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,
		// StaggeredGridLayoutManager.VERTICAL));
		//设置为gridview布局
		mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
		mRecyclerView.setAdapter(mAdapter);

		// mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
		// // 设置item动画
		// mRecyclerView.setItemAnimator(new DefaultItemAnimator());

		initEvent();

	}

	private void initView(){
		mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
		mAdapter = new HomeAdapter(this, mDatas);
	}
	
	private void initEvent() {
		mAdapter.setOnItemClickLitener(new OnItemClickLitener() {
			@Override
			public void onItemClick(View view, int position) {

				if (position == mRecyclerView.getChildCount() - 1) {
					Toast.makeText(HomeActivity.this, "这是最后的单击事件", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(HomeActivity.this, position + " click", Toast.LENGTH_SHORT).show();
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
			public void onItemLongClick(View view, int position) {
				Toast.makeText(HomeActivity.this, position + " long click", Toast.LENGTH_SHORT).show();
			}
		});
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
		mDatas.add("++");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.id_action_add:
			mAdapter.addData(0);
			break;
		case R.id.id_action_delete:
			mAdapter.removeData(1);
			break;
		case R.id.id_action_gridview:
			mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
			break;
		case R.id.id_action_listview:
			mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
			break;
		case R.id.id_action_horizontalGridView:
			mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL));
			break;

		case R.id.id_action_staggeredgridview:
			Intent intent = new Intent(this, StaggeredGridLayoutActivity.class);
			startActivity(intent);
			break;
		}
		return true;
	}

}
