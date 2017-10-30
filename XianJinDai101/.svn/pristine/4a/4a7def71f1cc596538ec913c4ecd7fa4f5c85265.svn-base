package com.ryx.ryxcredit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.beans.bussiness.cardpayment.CcardPaymentResponse;
import com.ryx.ryxcredit.utils.CBanksUtils;
import com.ryx.ryxcredit.utils.CStringUnit;

import java.util.List;

/**
 * Created by laomao on 16/9/12.
 */
public class CPayBankListAdapter extends BaseAdapter {
    Context context;
    List<CcardPaymentResponse.ResultBean> cardInfoList;
    String selctedCardNo;
    public CPayBankListAdapter(Context context, List<CcardPaymentResponse.ResultBean> cardInfoList, String selctedCardNo) {
        super();
        this.context = context;
        this.cardInfoList = cardInfoList;
        this.selctedCardNo=selctedCardNo;
    }

    @Override
    public int getCount() {
        if(cardInfoList!=null)
        return cardInfoList.size();
        return 0;
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
            view = LayoutInflater.from(context).inflate(R.layout.c_impay_bottom_listitem, null);

            holder.iv_ico= (ImageView)view.findViewById(R.id.bankimgview);
            holder.bankName= (TextView)view.findViewById(R.id.banknametextview);
            holder.bankAccount= (TextView)view.findViewById(R.id.tv_paybankcards_account);
            holder.tv_status= (TextView)view.findViewById(R.id.tv_status);
            view.setTag(holder);
        }else{
            holder =(ViewHolder) view.getTag();
        }
        holder.iv_ico.setImageResource(R.drawable.bank_ceb);
        holder.bankName.setText(cardInfoList.get(position).getBank_name());

        holder.bankName.setText(cardInfoList.get(position).getBank_name());
        holder.bankAccount.setText(CStringUnit.cardJiaMi(cardInfoList.get(position).getCard_num()));
        if(cardInfoList.get(position).getStatus()==0){
            holder.tv_status.setVisibility(View.VISIBLE);
            view.setBackgroundColor(Color.parseColor("#e5e5e5"));
        }else{
            holder.tv_status.setVisibility(View.INVISIBLE);
            view.setBackgroundColor(Color.WHITE);
        }
        CBanksUtils.selectIcoidToImgView(context,cardInfoList.get(position).getBank_title_code(),holder.iv_ico);
        return view;
    }

    class ViewHolder {
        ImageView iv_ico;
        TextView bankName;
        TextView bankAccount;
        TextView tv_status;
    }
}