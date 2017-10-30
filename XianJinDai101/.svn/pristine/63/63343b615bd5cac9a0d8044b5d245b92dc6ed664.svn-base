package com.ryx.ryxcredit.widget;

import android.content.Context;

import com.ryx.ryxcredit.R;

/**
 * Created by DIY on 2016/9/9.
 */
public class RyxCreditLoadDialog {
    private static RyxCreditLoadDialogBuilder instance=null;
    public  static  RyxCreditLoadDialogBuilder getInstance(Context context) {

        if (instance == null || !(context).equals(instance.getmContext())) {
            synchronized (RyxCreditLoadDialogBuilder.class) {
                if (instance == null || !context.equals(instance.getmContext())) {
                    if(instance!=null){
                        instance=null;
                    }
                    instance = new RyxCreditLoadDialogBuilder(
                            context, R.style.CustomProgressDialog);
                    instance.setmContext( context);
                    instance.setCancelable(false);
                }
            }
        }
        return instance;
    }
}
