package com.emiage.testrecycleradapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

	private RecyclerView mRv;
	private int j = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mRv = (RecyclerView) findViewById(R.id.rv);
		final TestAdapter mAdapter = new TestAdapter(this);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRv.setLayoutManager(linearLayoutManager);

		//模拟数据
		final ArrayList<String> list = new ArrayList<>();
		for (int i = 0; i < 25; i++) {
			list.add("data" + i);
		}

		mRv.setAdapter(mAdapter);
		//item的点击事件
		mAdapter.setOnRVItemClickListener(new BaseRVAdapter.OnRVItemClickListener() {
			@Override
			public void setItemClickListener(View v, int position) {
				Toast.makeText(MainActivity.this, "position" + position, Toast.LENGTH_SHORT).show();
			}
		});

		//自动加载更多的事件
		mAdapter.setMoreListerner(new BaseRVAdapter.OnLoadMoreListerner() {
			@Override
			public void loadMore() {
				for (int i = 0; i < 5; i++) {
					list.add("第" + j + "次加载更多" + i);
				}
				mAdapter.setListData(list);
				j++;
				if (j == 4) {//取消加载更多
					mAdapter.setIsLoadMore(false);
				}
			}
		});
		mAdapter.setIsAlwaysShowFootView(true);//如果有多重布局时，必须设置，否则不显示
		mAdapter.setIsMoreType(true);
		//设置数据
		mAdapter.setListData(list);

	}
}
