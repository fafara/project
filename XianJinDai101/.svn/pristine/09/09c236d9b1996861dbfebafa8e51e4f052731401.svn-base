package com.ryx.payment.ruishua.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by xucc on 2016/8/29.
 */
@EBean
public class PayTypeAdapter extends BaseAdapter {

    @RootContext
    Context context;

    private ArrayList<Map<String,String>> list = new ArrayList<>();

    private int selectedPos = 0;

    public void setList(ArrayList<Map<String,String>> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_paytype_gridview, null);
            viewHolder.tvtitle = (TextView) convertView.findViewById(R.id.tv_title);
//            viewHolder.tvdescribe = (TextView) convertView.findViewById(R.id.tv_describe);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == selectedPos) {
            convertView.setBackgroundResource(R.drawable.qqcoin_gridview_item_pressed_bg);
            viewHolder.tvtitle.setTextColor(Color.WHITE);
//            viewHolder.tvdescribe.setTextColor(Color.WHITE);
        } else if ((position + 1) % 3 == 0 || position == list.size() - 1) {
            convertView.setBackgroundResource(R.drawable.qqcoin_gridview_last_item_unpressed_bg);
            viewHolder.tvtitle.setTextColor(ContextCompat.getColor(context, R.color.graytexts));
//            viewHolder.tvdescribe.setTextColor(ContextCompat.getColor(context, R.color.graytexts));
        } else {
            convertView.setBackgroundResource(R.drawable.qqcoin_gridview_item_unpressed_bg);
            viewHolder.tvtitle.setTextColor(ContextCompat.getColor(context, R.color.graytexts));
//            viewHolder.tvdescribe.setTextColor(ContextCompat.getColor(context, R.color.graytexts));
        }
        viewHolder.tvtitle.setText(list.get(position).get("merchantName"));
//        viewHolder.tvdescribe.setText(list.get(position).get("describe"));
        return convertView;
    }

    class ViewHolder {
        TextView tvtitle;
//        TextView tvdescribe;
    }
}
