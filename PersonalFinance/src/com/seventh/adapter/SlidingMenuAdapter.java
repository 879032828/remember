package com.seventh.adapter;

import java.util.List;

import com.seventh.personalfinance.R;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class SlidingMenuAdapter extends RecyclerView.Adapter<SlidingMenuAdapter.MyViewHolder> {

	private List<String> mDatas;

	public List<String> getmDatas() {
		return mDatas;
	}

	public void setmDatas(List<String> mDatas) {
		this.mDatas = mDatas;
	}

	private LayoutInflater mInflater;
	private Context context;

	public interface OnRecItemClickLitener {

		// 单击回调事件
		void onRecItemClick(View view, int position);

		// 长按回调事件
		void onRecItemLongClick(View view, int position);
	}

	// 设置RecyclerView每个Item的点击监听
	private OnRecItemClickLitener mOnItemClickLitener;

	public void setRecOnItemClickLitener(OnRecItemClickLitener onRecItemClickLitener) {
		// TODO Auto-generated method stub
		this.mOnItemClickLitener = onRecItemClickLitener;
	}

	public SlidingMenuAdapter(Context context, List<String> datas) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		mDatas = datas;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		MyViewHolder holder = new MyViewHolder(mInflater.inflate(R.layout.item_left_menu, parent, false));
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {

		holder.tv.setText(mDatas.get(position));
		
		// 如果设置了回调，则设置点击事件
		if (mOnItemClickLitener != null) {

			holder.tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int pos = holder.getPosition();
					mOnItemClickLitener.onRecItemClick(holder.itemView, pos);
				}
			});

			holder.tv.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					int pos = holder.getPosition();
					mOnItemClickLitener.onRecItemLongClick(holder.itemView, pos);
					return false;
				}
			});
		}
	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	public void addData(int position, String type) {
		mDatas.add(position, type);
		notifyItemInserted(position);
	}

	public void removeData(int position) {
		mDatas.remove(position);
		notifyItemRemoved(position);
	}

	class MyViewHolder extends ViewHolder {

		Button tv;

		public MyViewHolder(View view) {
			super(view);
			tv = (Button) view.findViewById(R.id.id_num_left);
		}
	}

}