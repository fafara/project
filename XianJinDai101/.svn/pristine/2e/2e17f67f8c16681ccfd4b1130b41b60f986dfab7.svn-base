package com.ryx.ryxcredit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ryx.ryxcredit.R;

/**
 * Created by Administrator on 2016/10/17.
 */

public class DateSelectAdapter extends BaseAdapter {

    private Context cont;
    private String[] dateList;

    public DateSelectAdapter(Context context, String[] termList) {
        this.cont = context;
        this.dateList = termList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return dateList.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(cont).inflate(R.layout.c_listview_date_item, null);
        TextView tv_text = (TextView)convertView.findViewById(R.id.tv_day);
        tv_text.setText(dateList[position]);
        return convertView;
    }

}
