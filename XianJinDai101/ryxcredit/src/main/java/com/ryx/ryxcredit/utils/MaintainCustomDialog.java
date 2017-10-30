package com.ryx.ryxcredit.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.xjd.RKDQuotaActivity;

public class MaintainCustomDialog extends Dialog  {
    Context mContext;
    private ImageView iv_systemmaintenanceclosebutton;
    public MaintainCustomDialog(Context context){
        super(context);
        mContext = context;
    }
    public MaintainCustomDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_dialog, null);
        this.setContentView(layout);
        iv_systemmaintenanceclosebutton = (ImageView) findViewById(R.id.iv_systemmaintenanceclosebutton);
        iv_systemmaintenanceclosebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity CurrentActivity = (Activity) v .getContext();
                Intent intent = new Intent(CurrentActivity, RKDQuotaActivity.class );
                
            }
        });
    }

}