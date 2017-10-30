package com.ryx.ryxcredit.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.ryx.ryxcredit.R;


/**
 * Created by laomao on 16/5/6.
 */
public class RyxCreditLoadDialogBuilder extends Dialog implements DialogInterface {
    private static RyxCreditLoadDialogBuilder instance;
    private static Context mContext;
    private View mLoadDialogView;
    private RyxCreditSpinKitView spinKitView;
    private Sprite drawable;
    private TextView tvMsg;
    private String showMsg;
    public  void setmContext(Context context){
        this.mContext=context;
    }
    public Context getmContext(){
        return this.mContext;
    }
    public static RyxCreditLoadDialogBuilder getInstance(Context context) {

        if (instance == null || !mContext.equals(context)) {
            synchronized (RyxCreditLoadDialogBuilder.class) {
                if (instance == null || !mContext.equals(context)) {
                    instance = new RyxCreditLoadDialogBuilder (
                            context, R.style.CustomProgressDialog);
                }
            }
        }
        mContext = context;
        return instance;
    }

    public void setMessage(String showMsg) {
        this.showMsg = showMsg;
        if (!TextUtils.isEmpty(this.showMsg)) {
            tvMsg.setText(this.showMsg);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private RyxCreditLoadDialogBuilder(Context context) {

        super(context);
        initView(context);
    }

    public void show() {
        //xucc将位置替换到setMessage方法中,动态设置提示信息
//        if (!TextUtils.isEmpty(this.showMsg)) {
//            tvMsg.setText(this.showMsg);
//        }
        super.show();
    }


    public RyxCreditLoadDialogBuilder(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    private RyxCreditLoadDialogBuilder(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    private void initView(Context context) {
        mLoadDialogView = View.inflate(context, R.layout.c_widget_ryxloading, null);
        spinKitView = (RyxCreditSpinKitView) mLoadDialogView.findViewById(R.id.spin_kit);
        tvMsg = (TextView) mLoadDialogView.findViewById(R.id.tv_loading);
//        drawable = new CubeGrid();
        drawable = new Circle();
        spinKitView.setIndeterminateDrawable(drawable);
        mLoadDialogView.getBackground().setAlpha(180);
        setContentView(mLoadDialogView);
    }

}
