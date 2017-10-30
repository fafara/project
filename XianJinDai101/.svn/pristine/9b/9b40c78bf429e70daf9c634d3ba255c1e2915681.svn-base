package com.ryx.payment.ruishua.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.bean.AppListBean;
import com.ryx.payment.ruishua.utils.AppIconUtil;

import java.util.ArrayList;

/**
 * Created by muxin on 2016-08-11.
 */
public class BranchListAdapter extends BaseAdapter{

    Context context;
    ArrayList<AppListBean> appInfoList;

    public BranchListAdapter(Context context, ArrayList<AppListBean> appInfoList) {
        super();
        this.context = context;
        this.appInfoList = appInfoList;
    }
    @Override
    public int getCount() {
        return appInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return appInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.branch_item, null);
            viewHolder.iv_branch = (ImageView) convertView.findViewById(R.id.iv_branch_icon_item);
            viewHolder.tv_branch = (TextView) convertView.findViewById(R.id.tv_branch_name_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.iv_branch.setImageResource(AppIconUtil.selectIcoid(appInfoList.get(position)
                .getIconName()));
        viewHolder.tv_branch.setText(appInfoList.get(position).getAppName());
        return convertView;
    }

    class ViewHolder{
        ImageView iv_branch;
        TextView tv_branch;
    }
}
