package com.ryx.payment.ruishua.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rey.material.app.SimpleDialog;
import com.ryx.payment.ruishua.R;

public class ICDownloadDialog  {

	Context context;
	String address;

	ProgressBar pro_state;
	TextView tv_state;

	int process = 0;

	SimpleDialog simpleDialog;

	public ICDownloadDialog(Context context) {
		this.context = context;
		simpleDialog = new SimpleDialog(context);
		View view=  LayoutInflater.from(context).inflate(R.layout.dialog_ic_download,null);
		pro_state = (ProgressBar) view.findViewById(R.id.pro_state);
		tv_state = (TextView)view.findViewById(R.id.tv_state);
		pro_state.setProgress(0);
		tv_state.setText("已完成"+0+"%");
		simpleDialog.setContentView(view);
	}
public void setCanceledOnTouchOutside(boolean cancle){
	simpleDialog.setCanceledOnTouchOutside(cancle);
}
	public void dismiss(){
		simpleDialog.dismiss();
	}
	public void show(){
		simpleDialog.show();
	}
	public void setTip(int index, int total) {

		process = (index* 100) / total ;
		pro_state.setProgress(process);
		tv_state.setText("已完成"+process+"%");

	}
}
