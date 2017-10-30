package com.ryx.payment.ruishua.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.bean.SwiperDeviceInfo;

import java.util.ArrayList;

/**
 * Created by laomao on 16/6/4.
 */
public class DevsAdapter extends BaseAdapter {
    public ArrayList<SwiperDeviceInfo> devsList = new ArrayList<SwiperDeviceInfo>();
    private Context context;
    String terminid = "";
    public DevsAdapter(Context context, ArrayList<SwiperDeviceInfo> devsList) {
        this.context = context;
        this.devsList = devsList;
    }

    @Override
    public int getCount() {
        return devsList.size();
    }

    @Override
    public Object getItem(int position) {
        return devsList.get(position);
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
                    R.layout.devslist_item, null);
            holder.tv_device_type = (TextView) convertView
                    .findViewById(R.id.tv_devname);
            holder.tv_content = (TextView) convertView
                    .findViewById(R.id.tv_devid);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // holder.tv_device_type.setText(DateUtil.DateToShortStr(DateUtil
        // .StrToDate(noticeList.get(position).getTime())));
        // holder.tv_content.setText(noticeList.get(position).getContent());

        terminid=devsList.get(position).getTerminalId();
        if (terminid.length() > 6 ){
            if(terminid.subSequence(4, 6).equals("11")){
                holder.tv_device_type.setText("音频点付宝");
            }else if(terminid.subSequence(4, 6).equals("17")||terminid.subSequence(4, 6).equals("41")){
                holder.tv_device_type.setText("音频POS打印机");
            }else if(terminid.subSequence(4, 6).equals("29")||terminid.subSequence(4, 6).equals("30")){
                holder.tv_device_type.setText("蓝牙POS打印机");
            }else if(terminid.subSequence(4, 6).equals("59")){
                holder.tv_device_type.setText("蓝牙点付宝");
            }else if(terminid.contains("3150007") || terminid.contains("92782307")){
                holder.tv_device_type.setText("迷你手机IC刷卡器");
            }else if(terminid.contains("8002887100")) {
                holder.tv_device_type.setText("二维码");
            } else{
                holder.tv_device_type.setText("迷你手机刷卡器");
            }
        }

        holder.tv_content.setText(terminid);

        return convertView;
    }
    class ViewHolder {
        TextView tv_device_type;
        TextView tv_content;
    }
}
