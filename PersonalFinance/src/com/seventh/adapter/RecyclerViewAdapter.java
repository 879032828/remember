package com.seventh.adapter;

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

import com.seventh.personalfinance.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

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

		// �����ص��¼�
		void onRecItemClick(View view, int position);

		// �����ص��¼�
		void onRecItemLongClick(View view, int position);
	}

	// ����RecyclerViewÿ��Item�ĵ������
	private OnRecItemClickLitener mOnItemClickLitener;

	public void setRecOnItemClickLitener(OnRecItemClickLitener onRecItemClickLitener) {
		// TODO Auto-generated method stub
		this.mOnItemClickLitener = onRecItemClickLitener;
	}

	public RecyclerViewAdapter(Context context, List<String> datas) {
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
			// ��ItemΪ���һ��ʱ��TextView���ɼ���ImageView����Ϊ�ɼ�
			holder.tv.setVisibility(View.INVISIBLE);
			holder.id_image.setVisibility(View.VISIBLE);
		} else {
			holder.tv.setText(mDatas.get(position));

		}
		// ��������˻ص��������õ���¼�
		if (mOnItemClickLitener != null) {

			int count = getItemCount();
			if (position == getItemCount()) {

			} else {
				// itemView
				holder.tv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						int pos = holder.getLayoutPosition();
						// �������ItemΪ���һ��ʱ�������ڴ˴������䵥���¼�
						mOnItemClickLitener.onRecItemClick(holder.itemView, pos);
					}
				});

				holder.tv.setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						int pos = holder.getLayoutPosition();
						// �������ItemΪ���һ��ʱ�������ڴ˴������䳤���¼�
						if (pos == getItemCount() - 1) {
							Toast.makeText(context, "�������һ�������¼�", Toast.LENGTH_SHORT).show();
						} else {
							mOnItemClickLitener.onRecItemLongClick(holder.itemView, pos);
							// removeData(pos);
						}

						return false;
					}
				});

				holder.id_image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int pos = holder.getLayoutPosition();
						// �������ItemΪ���һ��ʱ�������ڴ˴������䵥���¼�
						mOnItemClickLitener.onRecItemClick(holder.itemView, pos);
					}
				});
			}

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
		ImageButton id_image;

		public MyViewHolder(View view) {
			super(view);
			tv = (Button) view.findViewById(R.id.id_num);
			id_image = (ImageButton) view.findViewById(R.id.id_image);
		}
	}

}