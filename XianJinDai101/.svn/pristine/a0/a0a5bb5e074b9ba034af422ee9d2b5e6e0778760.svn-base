package com.ryx.quickadapter.inter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ryx.quickadapter.R;
import com.ryx.quickadapter.widget.CItemDivider;
import com.ryx.quickadapter.widget.RecyclerViewDivider;

/**
 * XRecyclerView工具类
 *
 * @author muxin
 * @time 2016-08-22 11:13
 */
public class RecyclerViewHelper {

    static RecyclerViewHelper ourInstance;

    private RecyclerViewHelper() {
    }

    public static RecyclerViewHelper init() {
        if (ourInstance == null)
            ourInstance = new RecyclerViewHelper();
        return ourInstance;
    }

    /**
     * RecyclerView 线性列表相关属性(默认方向垂直、带分割线)
     * @param context 上下文
     * @param recyclerView recyclerView对象
     */
    public void setRVLinearLayout(Context context, RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new CItemDivider(context, R.drawable.c_recyclerview_divide));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * RecyclerView 网格列表相关属性(默认带网格线)
     * @param context 上下文
     * @param recyclerView recyclerView对象
     * @param spanCount 列数
     */
    public void setRVGridLayout(Context context, RecyclerView recyclerView, int spanCount) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //横线
        RecyclerViewDivider horizontalLine = new RecyclerViewDivider(
                context, LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor(context, R.color.grey_line));
        recyclerView.addItemDecoration(horizontalLine);
        //竖线
        RecyclerViewDivider verticalLine = new RecyclerViewDivider(
                context, LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(context, R.color.grey_line));
        recyclerView.addItemDecoration(verticalLine);
    }

    /**
     * XRecyclerView相关属性(默认方向垂直、带分割线、带下拉刷新及加载更多)
     * @param context
     * @param xRecyclerView
     */
    public void setXRVLinearLayout(Context context, XRecyclerView xRecyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.addItemDecoration(new CItemDivider(context, R.drawable.c_recyclerview_divide));
        xRecyclerView.setItemAnimator(new DefaultItemAnimator());
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setArrowImageView(R.drawable.abc_icon_down_arrow);
    }
}
