package com.ryx.ryxcredit.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ryx.ryxcredit.R;

import java.util.List;

/**
 * Created by Administrator on 2016/9/8.
 */
public class ComProblemAdapter extends RecyclerView.Adapter<ComProblemAdapter.PayedViewHolder> {

    private List<Object> datas;
    private  OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        public void onClick(View parent, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ComProblemAdapter(List<Object> datas) {
        this.datas = datas;
    }

    @Override
    public PayedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_adapter_com_problem, parent, false);
        return new PayedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PayedViewHolder holder, final int position) {
        holder.tv_problem.setText((String)datas.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onClick(holder.itemView,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(datas!=null)
        return datas.size();
        return 0;
    }

    /**
     * 已结清ViewHolder
     */
    public class PayedViewHolder extends RecyclerView.ViewHolder{
        public  TextView tv_problem;
        public PayedViewHolder(View itemView) {
            super(itemView);
            tv_problem =  (TextView)itemView.findViewById(R.id.tv_problem);
        }
    }
}

