package com.ryx.ryxpay.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.ryx.ryxpay.adapter.items.ItemView;
import com.ryx.ryxpay.adapter.items.ItemView_;
import com.ryx.ryxpay.bean.IconBean;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laomao on 16/4/27.
 */
@EBean
public class MainGridviewAdapter extends BaseAdapter {
    private ArrayList<IconBean.IconMsgBean> listData;
    @RootContext
    Context mContext;

    private int itemWidth;

    @AfterInject
    void init() {
        listData = new ArrayList<IconBean.IconMsgBean>();
    }

    @Override
    public int getCount() {
        return this.listData.size();
    }

    @Override
    public Object getItem(int position) {
        return this.listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemView view = null;

        if (convertView == null) {
            view = ItemView_.build(mContext);
        } else {
            view = (ItemView) convertView;
        }
        view.bindView((IconBean.IconMsgBean) getItem(position));
        AbsListView.LayoutParams layoutparams = new AbsListView.LayoutParams(
                itemWidth-3, itemWidth);
        view.setLayoutParams(layoutparams);
        return view;
    }

    public void setSize(int itemSize){
        itemWidth = itemSize;
    }

    public void update(List<IconBean.IconMsgBean> items) {
        this.listData.clear();
        this.listData.addAll(items);
        notifyDataSetChanged();
    }

    public void append(List<IconBean.IconMsgBean> items) {
        this.listData.addAll(items);
        notifyDataSetChanged();
    }

    public void delete(IconBean.IconMsgBean item) {
        this.listData.remove(item);
        notifyDataSetChanged();
    }

}
