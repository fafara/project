package com.ryx.payment.ruishua.widget;

import android.content.Context;

import com.ryx.payment.ruishua.R;

/**
 * Created by XCC on 2016/8/19.
 */
public class RyxLoadDialog {
    private  RyxLoadDialogBuilder instance=null;
    public   RyxLoadDialogBuilder getInstance(Context context) {

        if (instance == null || !context.equals(instance.getmContext())) {
            synchronized (RyxLoadDialogBuilder.class) {
                if (instance == null || !context.equals(instance.getmContext())) {
                    if(instance!=null){
                        instance=null;
                    }
                    instance = new RyxLoadDialogBuilder(
                            context, R.style.CustomProgressDialog);
                    instance.setmContext( context);
                }
            }
        }
        return instance;
    }
}
