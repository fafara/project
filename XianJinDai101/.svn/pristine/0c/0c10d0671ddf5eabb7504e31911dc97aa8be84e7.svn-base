package com.ryx.payment.ruishua.recharge.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.bean.QcoinInfo;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/16.
 */
@EBean
public class MobileAdapter extends BaseAdapter {

    @RootContext
    Context context;

    private ArrayList<QcoinInfo> list = new ArrayList<>();

    private int selectedPos = 0;

    public void setList(ArrayList<QcoinInfo> list) {
        this.list = list;
    }

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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_mobile_gridview, null);
            viewHolder.tvmoney = (TextView) convertView.findViewById(R.id.tv_mobile_money);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == selectedPos) {
            convertView.setBackgroundResource(R.drawable.qqcoin_gridview_item_pressed_bg);
            viewHolder.tvmoney.setTextColor(Color.WHITE);
        } else if ((position + 1) % 3 == 0 || position == list.size() - 1) {
            convertView.setBackgroundResource(R.drawable.qqcoin_gridview_last_item_unpressed_bg);
            viewHolder.tvmoney.setTextColor(ContextCompat.getColor(context, R.color.graytexts));
        } else {
            convertView.setBackgroundResource(R.drawable.qqcoin_gridview_item_unpressed_bg);
            viewHolder.tvmoney.setTextColor(ContextCompat.getColor(context, R.color.graytexts));
        }
        String bigDidsName = list.get(position).getBigDidsName();
        if (TextUtils.isEmpty(bigDidsName)) {
            int money = (int) Double.parseDouble(list.get(position).getParvalue().replace("￥", ""));
            viewHolder.tvmoney.setText("￥" + money);
        } else {
            viewHolder.tvmoney.setText(bigDidsName.replace("元", ""));
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvmoney;
    }
}
