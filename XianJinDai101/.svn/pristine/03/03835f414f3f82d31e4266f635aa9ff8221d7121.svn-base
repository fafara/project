package com.ryx.payment.ruishua.recharge;

import android.content.Intent;
import android.view.View;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_transfer_card_list)
public class TransferCardListActivity extends BaseActivity {

    @AfterViews
    public void initView() {
        setRightImageSrc(R.drawable.ic_add_icon_bg);
        setTitleLayout("银行卡转账", true);
        final Intent intent = new Intent(TransferCardListActivity.this, TransferCardAddActivity_.class);
        getRightImgView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

    }

}
