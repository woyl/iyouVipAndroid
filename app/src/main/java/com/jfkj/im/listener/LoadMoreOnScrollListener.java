package com.jfkj.im.listener;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class LoadMoreOnScrollListener extends RecyclerView.OnScrollListener {

    /**声明一个LinearLayoutManager*/
    private LinearLayoutManager mLinearLayoutManager;

    /**当前页，从0开始*/
    private int currentPage = 1;

    /**已经加载出来的Item的数量*/
    private int totalItemCount;

    /**主要用来存储上一个totalItemCount*/
    private int previousTotal = 0;

    /**在屏幕上可见的item数量*/
    private int visibleItemCount;

    /**在屏幕可见的Item中的第一个*/
    private int firstVisibleItem;

    /**是否正在上拉数据*/
    private boolean loading = true;

    public LoadMoreOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        //可视item数
        visibleItemCount = recyclerView.getChildCount();
        //item总数
        totalItemCount = mLinearLayoutManager.getItemCount();
        //第一个可见item
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal) {
                //数据成功获取才会执行
                //说明数据已经加载结束
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        //如果获取数据失败，则不会这行此处，因为loading始终为true
        //当最后一个item可见时，执行加载
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem) {
            currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }
    }

    public abstract void onLoadMore(int currentPage);
}