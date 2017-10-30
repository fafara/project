package com.ryx.payment.ruishua.authenticate.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.utils.BanksUtils;
import com.ryx.payment.ruishua.utils.StringUnit;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by xipingping on 2016/5/30.
 */
@EBean
public class CreditcardListAdapter  extends BaseAdapter{

    private ArrayList<Map<String,String>> cardInfoList;

    private int height, width;
    private ViewHolder holder = null;
    private View contentView;

    @RootContext
    Context context;

    public void setList(ArrayList<Map<String,String>> banklist){
            this.cardInfoList = banklist;
    }
    @AfterInject
    void init() {
        cardInfoList = new ArrayList<Map<String,String>>();
    }
    @Override
    public int getCount() {
        return cardInfoList.size();
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
        holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.adapter_auth_creditcard_item, null);
            contentView = convertView;
            holder.iv_ico = (ImageView) convertView.findViewById(R.id.iv_logo);
            holder.bankName = (TextView) convertView.findViewById(R.id.tv_bankname);
            holder.bankAccount = (TextView) convertView.findViewById(R.id.tv_bankno);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_check_msg);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_valid_date);
//            holder.autoRelativeLayout = (AutoRelativeLayout) convertView.findViewById(R.id.lay_logo_bg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String bankId = cardInfoList.get(
                position).get("bankId");
        if(!TextUtils.isEmpty(bankId)&&bankId.length()>6){
            bankId = bankId.substring(1,4);
        }
        BanksUtils.selectIcoidToImgView(context,bankId, holder.iv_ico);
        holder.bankName.setText(cardInfoList.get(position).get("bankName").replaceAll("\r|\n", ""));
//        if("102".equals(bankId)||"104".equals(bankId)||"302".equals(bankId)
//                ||"304".equals(bankId)||"306".equals(bankId)||"308".equals(bankId)){
//            holder.autoRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.red_bank));
//        }else if("105".equals(bankId)||"301".equals(bankId)||"309".equals(bankId)
//                ||"310".equals(bankId)){
//            holder.autoRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.blue_bank));
//        }else if("103".equals(bankId)||"305".equals(bankId)||"403".equals(bankId)){
//            holder.autoRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.green_bank));
//        }else if("303".equals(bankId)||"307".equals(bankId)||"".equals(bankId)){
//            holder.autoRelativeLayout.setBackgroundColor(context.getResources().getColor(R.color.yellow_bank));
//        }
        holder.bankAccount.setText(StringUnit.cardJiaMi(cardInfoList.get(
                position).get("account")));

        holder.tv_status.setText(cardInfoList.get(position).get("statusDesc"));
        holder.tv_date.setText(cardInfoList.get(position).get("feeDesc"));
        if ("0".equals(cardInfoList.get(position).get("status"))
                ||"-1".equals(cardInfoList.get(position).get("status"))
                ||"1".equals(cardInfoList.get(position).get("status"))) {
            holder.tv_date.setVisibility(View.VISIBLE);

        }  else if ("2".equals(cardInfoList.get(position).get("status"))
                ||"3".equals(cardInfoList.get(position).get("status"))) {
            holder.tv_date.setVisibility(View.GONE);
        }
        return convertView;

    }
    class ViewHolder {
        ImageView iv_ico;
        TextView bankName;
        TextView bankAccount;
        TextView tv_status;
        TextView tv_date;
//        AutoRelativeLayout autoRelativeLayout;
    }


}