package com.ryx.ryxpay.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.ryx.ryxpay.R;

/**
 * Created by laomao on 16/5/6.
 */
public class RyxLoadDialogBuilder extends Dialog implements DialogInterface {
    private static RyxLoadDialogBuilder instance;
    private static Context mContext;
    private View mLoadDialogView;
    private RyxSpinKitView spinKitView;
    private Sprite drawable;
    private TextView tvMsg;
    private String showMsg;

    public static RyxLoadDialogBuilder getInstance(Context context) {

        if (instance == null || !mContext.equals(context)) {
            synchronized (RyxLoadDialogBuilder.class) {
                if (instance == null || !mContext.equals(context)) {
                    instance = new RyxLoadDialogBuilder(
                            context, R.style.CustomProgressDialog);
                }
            }
        }
        mContext = context;
        return instance;
    }

    public void setMessage(String showMsg) {
        this.showMsg = showMsg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private RyxLoadDialogBuilder(Context context) {

        super(context);
        initView(context);
    }

    public void show() {
        if (!TextUtils.isEmpty(this.showMsg)) {
            tvMsg.setText(this.showMsg);
        }
        super.show();
    }

    private RyxLoadDialogBuilder(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    private RyxLoadDialogBuilder(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    private void initView(Context context) {
        mLoadDialogView = View.inflate(context, R.layout.widget_ryxloading, null);
        spinKitView = (RyxSpinKitView) mLoadDialogView.findViewById(R.id.spin_kit);
        tvMsg = (TextView) mLoadDialogView.findViewById(R.id.tv_loading);
//        drawable = new CubeGrid();
        drawable = new Circle();
        spinKitView.setIndeterminateDrawable(drawable);
        setContentView(mLoadDialogView);
    }

}
