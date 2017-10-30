package com.ryx.payment.ruishua.adapter;

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
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.StringUnit;

import java.util.ArrayList;

/**
 * Created by XCC on 2016/5/25.
 */
public class CashBankFlowLisAdapter extends BaseAdapter {
    Context context;
    ArrayList<BankCardInfo> cardInfoList;
    String selctedCardNo;
    public CashBankFlowLisAdapter(Context context, ArrayList<BankCardInfo> cardInfoList, String selctedCardNo) {
        super();
        this.context = context;
        this.cardInfoList = cardInfoList;
        this.selctedCardNo=selctedCardNo;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return cardInfoList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return cardInfoList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if(view==null){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.impay_bottom_listitem, null);

            holder.iv_ico= (ImageView)view.findViewById(R.id.bankimgview);
            holder.bankName= (TextView)view.findViewById(R.id.banknametextview);
            holder.bankAccount= (TextView)view.findViewById(R.id.tv_paybankcards_account);
            holder.tv_checked= (ImageView)view.findViewById(R.id.tv_checked);
            view.setTag(holder);
        }else{
            holder =(ViewHolder) view.getTag();
        }
        LogUtil.showLog("ryx","--getView--");
        BanksUtils.selectIcoidToImgView(context,cardInfoList.get(position).getBankId(), holder.iv_ico);
        holder.bankName.setText(cardInfoList.get(position).getBankName());
        holder.bankAccount.setText(StringUnit.cardJiaMi(cardInfoList.get(position).getAccountNo()));
        if(cardInfoList.get(position).getAccountNo().equals(selctedCardNo)){
            holder.tv_checked.setVisibility(View.VISIBLE);
        }else{
            holder.tv_checked.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    class ViewHolder {
        ImageView iv_ico;
        TextView bankName;
        TextView bankAccount;
        ImageView tv_checked;
    }
}
