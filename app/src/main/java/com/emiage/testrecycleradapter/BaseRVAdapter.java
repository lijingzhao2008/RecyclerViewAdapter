package com.emiage.testrecycleradapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by LiJZ on 2016/5/16.
 */
public abstract class BaseRVAdapter<T> extends RecyclerView.Adapter {
	//每次的数据不同，需要重写定义
	protected List<T> mListData;
	protected Context mContext;
	//item的布局view使用LayoutInflater加载
	protected final LayoutInflater mLayoutInflater;
	//item点击事件的接口
	private OnRVItemClickListener mRVItemClickListener;
	//item是否有点击事件，默认为false
	private boolean isItemClick = false;
	//是否使用自定义的footview，标志位
	private boolean isCustomFootView = false;
	//是否加载更多，默认为false不加载
	private boolean isLoadMore = false;
	//显示footview的标志位
	private boolean isAlwaysShowFootView = false;
	//footview的布局
	private View footView;
	//是否多种布局的标志位
	private boolean isMoreType = false;
	//footview的type
	private int FOOT_TYPE = 0430;
	private OnLoadMoreListerner mLoadMoreListerner;


	public BaseRVAdapter(List<T> mListData, Context context) {
		this.mContext = context;
		this.mListData = mListData;
		mLayoutInflater = LayoutInflater.from(context);
	}

	public BaseRVAdapter(Context context) {
		this.mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
	}

	/**
	 * 设置数据
	 *
	 * @param listData
	 */
	public void setListData(List<T> listData) {
		this.mListData = listData;
		notifyDataSetChanged();
	}

	/**
	 * 获取数据
	 *
	 * @return
	 */
	public List<T> getListData() {
		return mListData;
	}


	/**
	 * 显示footview，默认跟随loadmore，当loadmore为true是显示footview否则不显示
	 * 当设为true时，都会显示一个footview
	 */
	public void setIsAlwaysShowFootView(boolean isAlwaysShowFootView) {
		this.isAlwaysShowFootView = isAlwaysShowFootView;
	}

	/**
	 * @param footView
	 */
	public void setFootView(View footView) {
		isLoadMore = true;
		isCustomFootView = true;
		this.footView = footView;
	}

	/**
	 * 设置是否底部显示加载更多，默认不显示
	 *
	 * @param isLoadMore
	 */
	public void setIsLoadMore(boolean isLoadMore) {
		this.isLoadMore = isLoadMore;
	}

	/**
	 * 设置是否是多种布局，默认单一布局
	 */
	public void setIsMoreType(boolean isMoreType) {
		this.isMoreType = isMoreType;
	}


	@Override
	public int getItemCount() {
		if ((isAlwaysShowFootView || isLoadMore))
			return mListData == null ? 0 : mListData.size() + 1;
		return mListData == null ? 0 : mListData.size() ;
	}

	@Override
	public int getItemViewType(int position) {
		if ((isAlwaysShowFootView || isLoadMore) && position == getItemCount() - 1) {
			return FOOT_TYPE;
		}
		if (isMoreType) {
			return getDisplayType(position);
		}
		return super.getItemViewType(position);
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		if ((isAlwaysShowFootView || isLoadMore) && viewType == FOOT_TYPE) {
			View view = null;
			if (footView != null) {
				view = footView;
			} else {
				//默认情况的footview，可以修改
				view = mLayoutInflater.inflate(R.layout.item_footview, null);
				RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				view.setLayoutParams(params);

			}
			return new FootHolder(view);
		}

		return onCreateDisplayHolder(parent, viewType);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

		//当是最底部加载更多的view时
		if ((isAlwaysShowFootView || isLoadMore) && position == getItemCount() - 1) {
			if (!isCustomFootView) {//使用默认的footview
				FootHolder footHolder = (FootHolder) holder;
				if (isLoadMore) {
					footHolder.mllLoadMore.setVisibility(View.VISIBLE);
					footHolder.mtvNoMore.setVisibility(View.GONE);
					if (mLoadMoreListerner != null) {
//						handler.sendEmptyMessage(0);
						handler.sendEmptyMessageDelayed(0, 500);
					}
				} else {
					footHolder.mllLoadMore.setVisibility(View.GONE);
					footHolder.mtvNoMore.setVisibility(View.VISIBLE);
				}
			} else {
				onBindFootViewHolder(holder);
			}
			return;
		}

		//给recyclerview的item设置点击事件
		if (isItemClick) {
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mRVItemClickListener != null) {
						mRVItemClickListener.setItemClickListener(v, position);
					}
				}
			});
		}
		onBindItemViewHolder(holder, position);

	}

	/**
	 * 给item的viewholder绑定数据
	 *
	 * @param holder
	 * @param position
	 */
	public abstract void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position);

	/**
	 * 给footview设置事件
	 *
	 * @param holder
	 */
	public abstract void onBindFootViewHolder(RecyclerView.ViewHolder holder);

	/**
	 * 如果你的item布局是多种，需要复写这个方法
	 *
	 * @param postion
	 * @return
	 */
	public abstract int getDisplayType(int postion);

	/**
	 * 创建item的viewholder
	 *
	 * @param parent
	 * @param viewType
	 * @return
	 */
	public abstract RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType);

	/**
	 * footview的viewholder
	 */
	class FootHolder extends RecyclerView.ViewHolder {
		private TextView mtvNoMore;
		private LinearLayout mllLoadMore;

		public FootHolder(View itemView) {
			super(itemView);
			mtvNoMore = (TextView) itemView.findViewById(R.id.tv_no_more);
			mllLoadMore = (LinearLayout) itemView.findViewById(R.id.ll_load_more);
		}
	}


	/**
	 * 定义一个接口，处理recyclerview的item的点击事件
	 */
	interface OnRVItemClickListener {
		/**
		 * @param v        该条目的view对象
		 * @param position 该位置对应的position
		 */
		void setItemClickListener(View v, int position);
	}

	/**
	 * 加载更多的接口,滑动到底部自动加载数据
	 */
	interface OnLoadMoreListerner {
		void loadMore();
	}

	/**
	 * 设置item的点击事件，不包含footview
	 *
	 * @param mRVItemClickListener
	 */
	public void setOnRVItemClickListener(OnRVItemClickListener mRVItemClickListener) {//对外暴露的接口
		this.mRVItemClickListener = mRVItemClickListener;
		isItemClick = true;
	}

	/**
	 * 设置加载更多的监听事件
	 *
	 * @param moreListerner
	 */
	public void setMoreListerner(OnLoadMoreListerner moreListerner) {//对外暴露的接口  设置加载更多的监听事件
		isLoadMore = true;
		this.mLoadMoreListerner = moreListerner;
	}

	/**
	 * 获取加载更多的监听事件
	 *
	 * @return
	 */
	public OnLoadMoreListerner getMoreListerner() {//对外暴露的接口  获取加载更多的监听事件
		return mLoadMoreListerner;
	}


	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mLoadMoreListerner.loadMore();

		}
	};

	public void addData(int position, T t) {
		if (mListData != null) {
			mListData.add(t);
			notifyItemInserted(position);
		}
	}


}
