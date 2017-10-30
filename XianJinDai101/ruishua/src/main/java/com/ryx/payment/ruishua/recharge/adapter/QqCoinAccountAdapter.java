package com.ryx.payment.ruishua.recharge.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.bean.QcoinInfo;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/16.
 */
@EBean
public class QqCoinAccountAdapter extends BaseAdapter {

    @RootContext
    Context context;

    private ArrayList<QcoinInfo> list;

    private int selectedPos = 0;

    public void setList(ArrayList<QcoinInfo> list) {
        this.list = list;
    }

    @AfterInject
    void init() {
        list = new ArrayList<QcoinInfo>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelection(int position) {
        selectedPos = position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_qqcoin_gridview, null);
            viewHolder.tvqqcoin = (TextView) convertView.findViewById(R.id.tv_qqcoin);
            viewHolder.tvmoney = (TextView) convertView.findViewById(R.id.tv_money);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == selectedPos) {
            convertView.setBackgroundResource(R.drawable.qqcoin_gridview_item_pressed_bg);
            viewHolder.tvqqcoin.setTextColor(Color.WHITE);
            viewHolder.tvmoney.setTextColor(Color.WHITE);
        } else if ((position + 1) % 3 == 0 || position == list.size() - 1) {
            convertView.setBackgroundResource(R.drawable.qqcoin_gridview_last_item_unpressed_bg);
            viewHolder.tvqqcoin.setTextColor(context.getResources().getColor(R.color.graytexts));
            viewHolder.tvmoney.setTextColor(context.getResources().getColor(R.color.graytexts));
        } else {
            convertView.setBackgroundResource(R.drawable.qqcoin_gridview_item_unpressed_bg);
            viewHolder.tvqqcoin.setTextColor(context.getResources().getColor(R.color.graytexts));
            viewHolder.tvmoney.setTextColor(context.getResources().getColor(R.color.graytexts));
        }
        String bigDisName = list.get(position).getBigDidsName();
        if (TextUtils.isEmpty(bigDisName)) {
            viewHolder.tvqqcoin.setText(list.get(position).getOnlinename());
        } else {
            viewHolder.tvqqcoin.setText(bigDisName);
        }
        String tipName = list.get(position).getTipName();
        if (TextUtils.isEmpty(tipName)) {
            viewHolder.tvmoney.setText("￥"+list.get(position).getParvalue());
        } else {
            viewHolder.tvmoney.setText(tipName);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvqqcoin;
        TextView tvmoney;
    }
}
