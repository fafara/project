package com.ryx.payment.ruishua.recharge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.utils.LogUtil;

import java.util.List;

/**
 * Created by XCC on 2016/5/25.
 */
public class CreditCardAddBootSheetAdapter extends RecyclerView.Adapter<CreditCardAddBootSheetAdapter.ViewHolder> {
    private List<String> mList;
    private Context mContext;
    CreditCardAddBootSheetItemClickListen creditCardAddBootSheetItemClickListen;
   public CreditCardAddBootSheetAdapter(Context context, List<String> list,CreditCardAddBootSheetItemClickListen creditCardAddBootSheetItemClickListen){
        this.mContext=context;
         this.mList=list;
       this.creditCardAddBootSheetItemClickListen=creditCardAddBootSheetItemClickListen;
       LogUtil.showLog("dateListSize=="+mList.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.creditaddbootsheet_item_view, parent, false));
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
            String str=mList.get(position);
             holder.textView.setText(str);
        holder.bootsheetitemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(creditCardAddBootSheetItemClickListen != null){
                    creditCardAddBootSheetItemClickListen.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView;
        private View bootsheetitemview;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textview);
            bootsheetitemview=itemView.findViewById(R.id.bootsheetitemview);
        }
        /**
         * 点击监听
         */
        @Override
        public void onClick(View v) {

        }
    }



    public interface CreditCardAddBootSheetItemClickListen{

        public void onItemClick(int positions);
    }
}

