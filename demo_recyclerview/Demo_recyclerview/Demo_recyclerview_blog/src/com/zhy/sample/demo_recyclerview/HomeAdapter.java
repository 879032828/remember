package com.zhy.sample.demo_recyclerview;

import java.net.ContentHandler;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

	private List<String> mDatas;
	private LayoutInflater mInflater;
	private Context context;

	public interface OnItemClickLitener {
		void onItemClick(View view, int position);

		void onItemLongClick(View view, int position);
	}

	private OnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	public HomeAdapter(Context context, List<String> datas) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		mDatas = datas;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		MyViewHolder holder = new MyViewHolder(mInflater.inflate(R.layout.item_home, parent, false));
		return holder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {

		if (position == getItemCount() - 1) {
			holder.tv.setVisibility(View.INVISIBLE);
			holder.id_image.setVisibility(View.VISIBLE);

		} else {
			holder.tv.setText(mDatas.get(position));
			
		}
		// 如果设置了回调，则设置点击事件
		if (mOnItemClickLitener != null) {

			int count = getItemCount();
			if (position == getItemCount()) {

			} else {
				// itemView
				holder.tv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int pos = holder.getLayoutPosition();
						// 当点击的Item为最后一个时，可以在此处设置其单击事件
						mOnItemClickLitener.onItemClick(holder.itemView, pos);
					}
				});

				holder.tv.setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						int pos = holder.getLayoutPosition();
						// 当点击的Item为最后一个时，可以在此处设置其长按事件
						if (pos == getItemCount() - 1) {
							Toast.makeText(context, "这是最后一个长按事件", Toast.LENGTH_SHORT).show();
						} else {
							mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
							removeData(pos);
						}

						return false;
					}
				});
				
				holder.id_image.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						int pos = holder.getLayoutPosition();
						// 当点击的Item为最后一个时，可以在此处设置其单击事件
						mOnItemClickLitener.onItemClick(holder.itemView, pos);
					}
				});
			}

		}
	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	public void addData(int position) {
		mDatas.add(position, "Insert One" + position);
		notifyItemInserted(position);
	}

	public void removeData(int position) {
		mDatas.remove(position);
		notifyItemRemoved(position);
	}

	class MyViewHolder extends ViewHolder {

		Button tv;
		ImageButton id_image;

		public MyViewHolder(View view) {
			super(view);
			tv = (Button) view.findViewById(R.id.id_num);
			id_image = 	(ImageButton) view.findViewById(R.id.id_image);
		}
	}
}