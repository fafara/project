package com.ryx.payment.payplug.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ryx.payment.payplug.base.PayPlugBaseActivity;
import com.ryx.payment.ruishua.R;

/**
 * 支付成功界面
 */
public class PaySuccessActivity extends PayPlugBaseActivity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_success);
        button=(Button) findViewById(R.id.button);
        String reqCode= getIntent().getStringExtra("reqCode");

        Intent intent1 = new Intent();
        intent1.setAction("com.ryxpay.payplug.server");
        intent1.putExtra("reqCode", reqCode);
        intent1.putExtra("code", "0000");
        intent1.putExtra("msg", "");
        intent1.putExtra("result", "¥400");
        sendBroadcast(intent1);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
