package com.ryx.ryxcredit.ryd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.beans.bussiness.borrowrecords.BorrowRecordsRequest;
import com.ryx.ryxcredit.beans.bussiness.borrowrecords.BorrowRecordsResponse;
import com.ryx.ryxcredit.ryd.borrowrecord.PaymentRecordFragment;
import com.ryx.ryxcredit.services.ICallback;

/**
 * Created by laomao on 16/8/12.
 * 我要还款
 */
public class RepaymentActivity extends BaseActivity {

    private PaymentRecordFragment paymentRecordFragment;
    private boolean is_opened;//是否可以交易
    private String unborrowTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_repayment_list);
        setTitleLayout("我要还款", true, false);
        Intent intent = getIntent();
        paymentRecordFragment = PaymentRecordFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.c_fl_borrow_records_repayment, paymentRecordFragment, "repayment");
        if (intent != null) {
            Bundle bundle = new Bundle();
            if (intent.hasExtra("flag")) {
                String flag = intent.getStringExtra("flag");
                bundle.putString("flag", flag);
            }
            if (intent.hasExtra("is_opened")) {
                is_opened = intent.getBooleanExtra("is_opened",false);
                bundle.putBoolean("is_opened",is_opened);
            }
            if(intent.hasExtra("unborrowTime"))
                unborrowTime = intent.getStringExtra("unborrowTime");
            bundle.putString("unborrowTime",unborrowTime);
            paymentRecordFragment.setArguments(bundle);
        }
        fragmentTransaction.commit();
    }

    public void requestBorrowRecord(String status, String flag) {
        BorrowRecordsRequest request = new BorrowRecordsRequest();
        request.setLoan_status(status);
        httpsPost(this, request, ReqAction.APPLICATION_BORROW_RECORDS, BorrowRecordsResponse.class, new ICallback<BorrowRecordsResponse>() {
            @Override
            public void success(final BorrowRecordsResponse borrowRecordsResponse) {
                paymentRecordFragment.recordCallBackSuccess(borrowRecordsResponse.getResult());
            }

            @Override
            public void failture(String tips) {
                paymentRecordFragment.recordCallBackFailed(tips);
            }
        });
    }
}
