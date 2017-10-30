package com.ryx.payment.ruishua.usercenter.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.utils.BanksUtils;
import com.ryx.payment.ruishua.utils.StringUnit;
import com.ryx.quickadapter.recyclerView.HelperAdapter;
import com.ryx.quickadapter.recyclerView.HelperViewHolder;

import java.util.List;

/**
 * 存量用户，开通快捷支付，银行卡列表
 * Created by xiepp.
 */

public class QuickPayBankListAdapter extends HelperAdapter<BankCardInfo> {

    private Context mcontext;


    /**
     * @param data     数据源
     * @param context  上下文
     * @param layoutId 布局Id
     */
    public QuickPayBankListAdapter(List<BankCardInfo> data, Context context, int... layoutId) {
        super(data, context, layoutId);
        mcontext = context;
    }
    @Override
    protected void HelperBindData(HelperViewHolder viewHolder, int position, final BankCardInfo item) {
        View view = viewHolder.getItemView();
        if(position%2==0){
            view.setBackgroundColor(mcontext.getResources().getColor(R.color.white));
        }else{
            view.setBackgroundColor(mcontext.getResources().getColor(R.color.background_ed));
        }
        if(item!=null){
            View itemView = viewHolder.getItemView();
            ImageView iv_carcIcon = (ImageView)itemView.findViewById(R.id.iv_bank_icon);
            String bankId = item.getBankId();
            if(!TextUtils.isEmpty(bankId)&&bankId.length()>6){
                bankId = bankId.substring(1,4);
            }
            BanksUtils.selectIcoidToImgView(mcontext,bankId,iv_carcIcon);
            TextView tv_carcNo = (TextView)itemView.findViewById(R.id.tv_bankno);
            tv_carcNo.setText(StringUnit.cardJiaMi(item.getAccountNo()));
        }
    }

}
