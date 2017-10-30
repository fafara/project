package com.ryx.payment.ruishua.bindcard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.utils.BanksUtils;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

/**
 * Created by xiepingping.
 */
@EBean
public class BanksAdapter extends BaseAdapter {

    @RootContext
    Context context;
    private ArrayList<BankCardInfo> banklist;

    public void setList(ArrayList<BankCardInfo> banklist){
        this.banklist = banklist;
    }
    @Override
    public int getCount() {
        return banklist.size();
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
        if(convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.bind_card_bank_listitem, null);

            holder.iv_ico= (ImageView)convertView.findViewById(R.id.bankimgview);
            holder.bankName= (TextView)convertView.findViewById(R.id.banknametextview);
            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        BanksUtils.selectIcoidToImgView(context,banklist.get(position).getBankId(), holder.iv_ico);
        holder.bankName.setText(banklist.get(position).getBankName());
        return convertView;
    }

    class ViewHolder {
        ImageView iv_ico;
        TextView bankName;
    }
}
