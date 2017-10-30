package com.ryx.payment.ruishua.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.bean.MsgInfo;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/2.
 */
@EBean
public class MsgListAdapter extends BaseAdapter {
    @RootContext
    Context context;

    private ArrayList<MsgInfo> msgList = new ArrayList<MsgInfo>();;

    public void setList(ArrayList<MsgInfo> list) {
        this.msgList = list;
    }

    @Override
    public int getCount() {
        return msgList.size();
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.activity_msg_list_item, null);
            holder.msg_title = (TextView) convertView.findViewById(R.id.tv_msg_title);
            holder.msg_content = (TextView) convertView.findViewById(R.id.tv_msg_content);
            holder.iv_left = (ImageView) convertView.findViewById(R.id.iv_left);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (!msgList.get(position).isReaded() &&"0".equals(msgList.get(position).getNoticeType())) {
            holder.iv_left.setVisibility(View.VISIBLE);
            holder.msg_title.setTextColor(context.getResources().getColor(R.color.BLACK));
        } else if("0".equals(msgList.get(position).getReadFlag()) &&"1".equals(msgList.get(position).getNoticeType())){
            holder.iv_left.setVisibility(View.VISIBLE);
            holder.msg_title.setTextColor(context.getResources().getColor(R.color.BLACK));
        }else{
            holder.msg_title.setTextColor(context.getResources().getColor(R.color.threeblack));
            holder.iv_left.setVisibility(View.INVISIBLE);
        }
        holder.msg_title.setText(msgList.get(position).getTitle());
        holder.msg_content.setText(msgList.get(position).getContent());
        return convertView;
    }

    class ViewHolder {
        TextView msg_title;
        TextView msg_content;
        ImageView iv_left;
    }
}
