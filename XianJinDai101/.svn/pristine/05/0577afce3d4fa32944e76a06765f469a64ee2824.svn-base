package com.ryx.ryxpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.ryxpay.R;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/9.
 */
@EBean
public class LoginAccountListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private  ArrayList list = new ArrayList();
    private ViewHolder viewHolder = null;
    @RootContext
    Context context;

    public void setList(ArrayList list){
        this.list = list;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.adapter_login_account_list_item,null);
            viewHolder.tv =(TextView)convertView.findViewById(R.id.tv_account);
        }
        viewHolder.tv.setText((String)list.get(position));
        return convertView;
    }


    class ViewHolder{
             ImageView img;
             TextView tv;
        }
}
