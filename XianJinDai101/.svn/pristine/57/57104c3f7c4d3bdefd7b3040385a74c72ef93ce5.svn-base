package com.ryx.payment.ruishua.dialog;

import com.rey.material.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.listener.LoadingStateDialogListener;
import com.ryx.payment.ruishua.widget.RyxSpinKitView;
import com.ryx.quickadapter.inter.NoDoubleClickListener;

/***
 * 等待处理结果等待框
 */
public class LoadingStateDialog extends Dialog {

	Context context;
    TextView tv_status;
    ImageView iv_loading_ok;
    RyxSpinKitView spin_kit_loading;
    LoadingStateDialogListener loadingStateDialogListener;
    Button loading_statedialog_colose;

	public LoadingStateDialog(Context context, LoadingStateDialogListener loadingStateDialogListener) {
		super(context, R.style.SimpleDialogLight);
        this.loadingStateDialogListener=loadingStateDialogListener;

	}

	public LoadingStateDialog(Context context, int theme,LoadingStateDialogListener loadingStateDialogListener) {
		super(context, theme);
		this.context = context;
        this.loadingStateDialogListener=loadingStateDialogListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loadingstate);
        tv_status=(TextView) findViewById(R.id.tv_status);
        iv_loading_ok=(ImageView) findViewById(R.id.iv_loading_ok);
        spin_kit_loading=(RyxSpinKitView) findViewById(R.id.spin_kit_loading);
        loading_statedialog_colose=(Button)findViewById(R.id.loading_statedialog_colose);
        loading_statedialog_colose.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if(loadingStateDialogListener!=null){
                    loadingStateDialogListener.onPositiveActionClicked(LoadingStateDialog.this);
                }

            }
        });
	}

    /**
     * 设置状态内容
     * @param content
     */
	public void setStatusContent(String content){
        tv_status.setText(content);
    }

    /**
     *动态设置处理状态
     * @param code 1等待处理中,2处理OK 3处理失败
     */
    public void setImgState(int code){
    if(code==1){
        iv_loading_ok.setVisibility(View.GONE);
        spin_kit_loading.setVisibility(View.VISIBLE);
    }else if(code==2){
        iv_loading_ok.setVisibility(View.VISIBLE);
        spin_kit_loading.setVisibility(View.GONE);
    }else if(code==3){
        iv_loading_ok.setImageResource(R.drawable.c_credit_error);
        iv_loading_ok.setVisibility(View.VISIBLE);
        spin_kit_loading.setVisibility(View.GONE);
    }
    }

}
