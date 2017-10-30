package com.ryx.payment.ruishua.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.utils.AppIconUtil;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/10.
 */
@EBean
public class AppLisDialogAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Map<String,String>> list;
    private ViewHolder viewHolder = null;

    @RootContext
    Context context;

    @AfterInject
    void init() {
        list = new ArrayList<Map<String,String>>();
    }

    public void setList(ArrayList list) {
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
            convertView = layoutInflater.inflate(R.layout.dialog_login_applist_item,null);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.img_logo);
            viewHolder.tv =(TextView)convertView.findViewById(R.id.tv_app);
            viewHolder.img.setBackgroundResource(AppIconUtil.selectIcoid(list.get(position).get("appuser")));
            viewHolder.tv.setText((String)list.get(position).get("branchid"));
        }


        return convertView;
    }

    class ViewHolder{
        ImageView img;
        TextView tv;
    }
}
