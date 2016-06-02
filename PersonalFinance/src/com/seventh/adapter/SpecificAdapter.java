package com.seventh.adapter;

import java.util.List;

import com.seventh.db.Account;
import com.seventh.personalfinance.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpecificAdapter extends BaseAdapter {

	private List<Account> accounts;
	private Context context;
	
	public SpecificAdapter(List<Account> accounts,Context context) {
		// TODO Auto-generated constructor stub
		this.accounts = accounts;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return accounts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub

		return accounts.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.specific_data_data, null);
		Account account = accounts.get(position);
		TextView tv_text_time = (TextView) view.findViewById(R.id.ls_sp_tv_time);
		TextView tv_text_type = (TextView) view.findViewById(R.id.ls_sp_tv_type);
		TextView tv_text_money = (TextView) view.findViewById(R.id.ls_sp_tv_money);
		ImageView iv_flag = (ImageView) view.findViewById(R.id.flag);

		tv_text_time.setText(account.getTime());
		tv_text_money.setText(account.getMoney() + "");
		if (account.getRemark().isEmpty()) {
			tv_text_type.setText(account.getType());
		} else {
			tv_text_type.setText(account.getType() + "--" + account.getRemark());
		}
		// 当在ListView中显示为收入时，设置金额前为绿点，否则为红点
		if (account.isEarnings()) {
			iv_flag.setBackgroundResource(R.drawable.point_green);
		} else {
			iv_flag.setBackgroundResource(R.drawable.point_red);
		}
		return view;
	}

}
