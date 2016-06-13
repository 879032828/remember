package com.seventh.personalfinance;

import com.seventh.db.PersonDBdao;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView.EGLWindowSurfaceFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Toast;

/**
 * @author Administrator
 *
 */
public class MenuLeftFragment extends Fragment {
	private String name;
	private View mView;
	private Button button_user_exit;
	private LinearLayoutManager mLinearLayoutManager;
	private ListAdapter mAdapter;
	private static final String TAG = "left_menu_fragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		if (mView == null) {
			initView(inflater, container);
			Bundle bundle = new Bundle();
			bundle = getArguments();
			name = bundle.getString("name");
		}
		button_user_exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				PersonDBdao personDBdao = new PersonDBdao(getActivity());
				personDBdao.updateLoginCancel(name);
				Intent intent = new Intent(getActivity(),Activity_Login.class);
				getActivity().startActivity(intent);
				getActivity().finish();
				Toast.makeText(getActivity(), "用户退出", Toast.LENGTH_SHORT).show();
			}
		});
		return mView;
	}

	/**
	 * @param inflater
	 * @param container
	 *            初始化界面
	 */
	private void initView(LayoutInflater inflater, ViewGroup container) {
		mView = inflater.inflate(R.layout.activity_left_menu, container, false);
		button_user_exit = (Button) mView.findViewById(R.id.button_user_exit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 * 当Fragment所在的Activity创建时调用
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.i(TAG, "onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "onDestroy");
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.i(TAG, "onDestroyView");
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		Log.i(TAG, "onDetach");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i(TAG, "onPause");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i(TAG, "onResume");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i(TAG, "onStart");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i(TAG, "onStop");
	}

}
