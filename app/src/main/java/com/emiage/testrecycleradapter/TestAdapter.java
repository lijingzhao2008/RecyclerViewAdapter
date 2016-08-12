package com.emiage.testrecycleradapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by LiJZ on 2016/8/12.
 */
public class TestAdapter extends BaseRVAdapter<String> {

	public TestAdapter(List mListData, Context context) {
		super(mListData, context);
	}

	public TestAdapter(Context context) {
		super(context);
	}

	@Override
	public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (position % 2 == 0) {
			MyHolder myHolder = (MyHolder) holder;
			myHolder.mtv.setText(mListData.get(position));
		} else {
			MyHolder myHolder = (MyHolder) holder;
			myHolder.mtv.setText(mListData.get(position));
		}
	}

	@Override
	public void onBindFootViewHolder(RecyclerView.ViewHolder holder) {

	}


	@Override
	public int getDisplayType(int postion) {
		if (postion % 2 == 0) {
			return 123;
		} else {
			return 456;
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
		if (viewType == 123) {
			View view = mLayoutInflater.inflate(R.layout.item_test2, parent, false);
			return new MyHolder(view);
		} else{
			View view = mLayoutInflater.inflate(R.layout.item_test, parent, false);
			return new MyHolder(view);
		}
	}

	static class MyHolder extends RecyclerView.ViewHolder {

		private final TextView mtv;

		public MyHolder(View itemView) {
			super(itemView);
			mtv = (TextView) itemView.findViewById(R.id.item_tv);
		}
	}
}
