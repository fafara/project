package com.ryx.payment.ruishua.recharge.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/16.
 */
@EBean
public class RefuelGridviewAdapter extends BaseAdapter{

    @RootContext
    Context context;

    private ArrayList<String> list = new ArrayList<String>();

    private ViewHolder viewHolder;

    private int selectedPos=0;
    @AfterInject
    void init() {
        list = new ArrayList<String>();
    }
    public void setSelection(int pos){
        selectedPos = pos;
    }

    public void setList(ArrayList<String> list){
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = null;
        if(convertView==null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_refuel_gridview, null);
            viewHolder.tvmoney = (TextView)convertView.findViewById(R.id.tv_refuelmoney);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        if(position==selectedPos){
            convertView.setBackgroundResource(R.drawable.qqcoin_gridview_item_pressed_bg);
            viewHolder.tvmoney.setTextColor(Color.WHITE);
        }else if((position+1)%3==0||position==list.size()-1){
            convertView.setBackgroundResource(R.drawable.qqcoin_gridview_last_item_unpressed_bg);
            viewHolder.tvmoney.setTextColor(context.getResources().getColor(R.color.graytexts));
        }else{
            convertView.setBackgroundResource(R.drawable.qqcoin_gridview_item_unpressed_bg);
            viewHolder.tvmoney.setTextColor(context.getResources().getColor(R.color.graytexts));
        }
        viewHolder.tvmoney.setText(list.get(position).toString());
        return convertView;
    }
    class ViewHolder {
        TextView tvmoney;
    }
}
