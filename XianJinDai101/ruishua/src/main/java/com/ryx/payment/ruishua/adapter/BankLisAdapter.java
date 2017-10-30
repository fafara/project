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
public class BankLisAdapter extends BaseAdapter {
    Context context;
    ArrayList<BankCardInfo> cardInfoList;
    String selctedCardNo;
    boolean isTypeShow;
    public BankLisAdapter(Context context, ArrayList<BankCardInfo> cardInfoList,String selctedCardNo) {
        super();
        this.context = context;
        this.cardInfoList = cardInfoList;
        this.selctedCardNo=selctedCardNo;
    }
    public BankLisAdapter(Context context, ArrayList<BankCardInfo> cardInfoList,String selctedCardNo,boolean isTypeShow) {
        super();
        this.context = context;
        this.cardInfoList = cardInfoList;
        this.selctedCardNo=selctedCardNo;
        this.isTypeShow=isTypeShow;
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


        if(isTypeShow){
            String bankShortName= cardInfoList.get(position).getBankName();
            if("01".equals(cardInfoList.get(
                    position).getCardtype())||"".equals(cardInfoList.get(
                    position).getCardtype())){
                //储蓄卡
                holder.bankName.setText(bankShortName+"(储蓄卡)");
            }else{
                //信用卡
                holder.bankName.setText( bankShortName+"(信用卡)");
            }
        }else{
            holder.bankName.setText(cardInfoList.get(position).getBankName());
        }

        holder.bankAccount.setText(StringUnit.cardJiaMi(cardInfoList.get(position).getAccountNo()));
        if("1".equals(cardInfoList.get(position).getCardstatus())){
            view.setBackgroundColor(context.getResources().getColor(R.color.btn_dark));
        }else{
            view.setBackgroundColor(0);
        }


        if(cardInfoList.get(position).getAccountNo().equals(selctedCardNo)){
            holder.tv_checked.setVisibility(View.VISIBLE);
//            view.setBackgroundColor(context.getResources().getColor(R.color.btn_dark));
        }else{
//            view.setBackgroundColor(0);
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
