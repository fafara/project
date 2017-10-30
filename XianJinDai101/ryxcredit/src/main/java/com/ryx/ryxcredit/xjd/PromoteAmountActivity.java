package com.ryx.ryxcredit.xjd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.contactrecords.CallRecordsActivity;
import com.ryx.ryxcredit.newactivity.bean.IncreaseAmountResponse;
import com.ryx.ryxcredit.newbean.userlevel.IncreaseAmountRequest;
import com.ryx.ryxcredit.newbean.userlevel.cashLimit.CashLimitRequest;
import com.ryx.ryxcredit.newbean.userlevel.cashLimit.CashLimitResponse;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * Created by Administrator on 2017/4/20.
 */

public class PromoteAmountActivity extends BaseActivity {

    private TextView frag_amount;
    private AutoRelativeLayout lay_center_bank_credit;
    private ImageView iv_center_bank_credit;
    private AutoRelativeLayout c_ll_provident_fund;
    private ImageView iv_provident_fund_credit;
    private  AutoRelativeLayout c_ll_mobile_phone_operators;
    private ImageView iv_mobile_phone_operators;
    private  AutoRelativeLayout c_ll_electricity_business_information_certification;
    private  ImageView iv__electricity_business_information_certification;
    private String limit_amount ;
    private String bank_credit_status;
    private String accumulation_fund_status ;
    private String mobile_operator_status;
    private String online_shopping_status;
    //现金贷用户级别，客户资料级别
    private String xjd_user_level, xjd_user_info_level;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_promote_amount);
        setTitleLayout("提升额度",true,false);
        initView();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        xjd_user_level = bundle.getString("xjd_user_level");
        xjd_user_info_level = bundle.getString("xjd_user_info_level");
        getCashLimit();
    }
    private void initView(){
        frag_amount = (TextView) findViewById(R.id.tv_promoteAmount_quota);
        iv_center_bank_credit = (ImageView) findViewById(R.id.iv_center_bank_credit);
        iv_provident_fund_credit = (ImageView) findViewById(R.id.iv_provident_fund_credit);
        iv_mobile_phone_operators = (ImageView) findViewById(R.id.iv_mobile_phone_operators);
        iv__electricity_business_information_certification = (ImageView) findViewById(R.id.iv__electricity_business_information_certification);
        lay_center_bank_credit = (AutoRelativeLayout)findViewById(R.id.lay_center_bank_credit);
        lay_center_bank_credit.setOnClickListener(onClickListener);
        c_ll_provident_fund = (AutoRelativeLayout)findViewById(R.id.c_ll_provident_fund);
        c_ll_provident_fund.setOnClickListener(onClickListener);
        c_ll_mobile_phone_operators = (AutoRelativeLayout)findViewById(R.id.c_ll_mobile_phone_operators);
        c_ll_mobile_phone_operators.setOnClickListener(onClickListener);
        c_ll_electricity_business_information_certification = (AutoRelativeLayout)findViewById(R.id.c_ll_electricity_business_information_certification);
        c_ll_electricity_business_information_certification.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //央行征信
            if(v.getId()==R.id.lay_center_bank_credit){
                if ("N".equalsIgnoreCase(bank_credit_status)){
                    //待认证
                    Intent intent = new Intent(PromoteAmountActivity.this, CenterBankCreditActivity.class);
                    startActivity(intent);
                }else  if ("U".equalsIgnoreCase(bank_credit_status)){
                    //审核中
                }else if("A".equalsIgnoreCase(bank_credit_status)){
                    //已认证
                }else if("O".equalsIgnoreCase(bank_credit_status)){
                    //已过期
                }
                else if("R".equalsIgnoreCase(bank_credit_status)){
                    //拒绝
                }else if("I".equalsIgnoreCase(bank_credit_status)){
                    //信息校验中（审核中）

                }else if("S".equalsIgnoreCase(bank_credit_status)){
                    //信息校验成功(审核中)

                }else if("E".equalsIgnoreCase(bank_credit_status)){
                    //用户名或密码错误（拒绝）
                    Intent intent = new Intent(PromoteAmountActivity.this, CenterBankCreditActivity.class);
                    startActivity(intent);
                }else if("F".equalsIgnoreCase(bank_credit_status)){
                    //身份验证码错误（拒绝）
                    Intent intent = new Intent(PromoteAmountActivity.this, CenterBankCreditActivity.class);
                    startActivity(intent);
                }
//公积金认证
            }else if(v.getId()==R.id.c_ll_provident_fund){
                Toast.makeText(PromoteAmountActivity.this,"尚未开放",Toast.LENGTH_LONG).show();
                if ("N".equalsIgnoreCase(accumulation_fund_status)){
                    //待认证
                }else if ("U".equalsIgnoreCase(accumulation_fund_status)){
                    //审核中
                }else if("A".equalsIgnoreCase(accumulation_fund_status)){
                    //已认证
                }else if("O".equalsIgnoreCase(accumulation_fund_status)){
                    //已过期
                }else if("R".equalsIgnoreCase(accumulation_fund_status)){
                    //拒绝
                }
//手机运营商认证
            }else if(v.getId()==R.id.c_ll_mobile_phone_operators){
                if ("N".equalsIgnoreCase(mobile_operator_status)){
                    //待认证
                    Intent intent = new Intent(PromoteAmountActivity.this, CallRecordsActivity.class);
                    startActivity(intent);
                }else if ("U".equalsIgnoreCase(mobile_operator_status)){
                    //审核中
                }else if("A".equalsIgnoreCase(mobile_operator_status)){
                    //已认证
                }else if("O".equalsIgnoreCase(mobile_operator_status)){
                    //已过期
                    Intent intent = new Intent(PromoteAmountActivity.this, CallRecordsActivity.class);
                    startActivity(intent);
                }else if("R".equalsIgnoreCase(mobile_operator_status)){
                    //拒绝
                }
//电商信息认证
            }else if(v.getId()==R.id.c_ll_electricity_business_information_certification){
                Toast.makeText(PromoteAmountActivity.this,"尚未开放",Toast.LENGTH_LONG).show();
                if ("N".equalsIgnoreCase(online_shopping_status)){
                    //待认证
                }else if ("U".equalsIgnoreCase(online_shopping_status)){
                    //审核中
                }else if("A".equalsIgnoreCase(online_shopping_status)){
                    //已认证
                }else if("O".equalsIgnoreCase(online_shopping_status)){
                    //已过期
                }else if("R".equalsIgnoreCase(online_shopping_status)){
                    //拒绝
                }

            }
        }
    };
    //提额状态接口(yang)
    private void getCashLimit() {
        final IncreaseAmountRequest request = new IncreaseAmountRequest();
/*        request.setProduct_id(RyxcreditConfig.xjd_procudtId);
        request.setCustomer_type(user_level);
        //提额
        request.setFlag("M");*/
        httpsPost(this, request, ReqAction.LIMIT_STATUS, IncreaseAmountResponse.class, new ICallback<IncreaseAmountResponse>() {

            @Override
            public void success(IncreaseAmountResponse increaseAmountResponse) {
                 limit_amount = increaseAmountResponse.getResult().getLimit_amount();
                 bank_credit_status = increaseAmountResponse.getResult().getBank_credit_status();
                 accumulation_fund_status = increaseAmountResponse.getResult().getAccumulation_fund_status();
                 mobile_operator_status = increaseAmountResponse.getResult().getMobile_operator_status();
                 online_shopping_status = increaseAmountResponse.getResult().getOnline_shopping_status();
                if ("N".equalsIgnoreCase(bank_credit_status)){
                    //待认证
                    iv_center_bank_credit.setBackgroundResource(R.drawable.waitcertification);
                }else if ("U".equalsIgnoreCase(bank_credit_status)){
                    //审核中
                    iv_center_bank_credit.setBackgroundResource(R.drawable.credit_under_auth);
                }else if("A".equalsIgnoreCase(bank_credit_status)){
                    //已认证
                    iv_center_bank_credit.setBackgroundResource(R.drawable.credit_finished);
                }else if("O".equalsIgnoreCase(bank_credit_status)){
                    //已过期
                    iv_center_bank_credit.setBackgroundResource(R.drawable.credit_outdate_img);
                }else if("R".equalsIgnoreCase(bank_credit_status)){
                    //未通过
                    iv_center_bank_credit.setBackgroundResource(R.drawable.credit_notpass);
                }else if("I".equalsIgnoreCase(bank_credit_status)){
                    //信息校验中（审核中）
                    iv_center_bank_credit.setBackgroundResource(R.drawable.credit_under_auth);
                }else if("S".equalsIgnoreCase(bank_credit_status)){
                    //信息校验成功(审核中)
                    iv_center_bank_credit.setBackgroundResource(R.drawable.credit_under_auth);
                }else if("E".equalsIgnoreCase(bank_credit_status)){
                    //用户名或密码错误（拒绝）
                    iv_center_bank_credit.setBackgroundResource(R.drawable.credit_notpass);
                }else if("F".equalsIgnoreCase(bank_credit_status)){
                    //身份验证码错误（拒绝）
                    iv_center_bank_credit.setBackgroundResource(R.drawable.credit_notpass);
                }

                if ("N".equalsIgnoreCase(accumulation_fund_status)){
                    //待认证
                    iv_provident_fund_credit.setBackgroundResource(R.drawable.waitcertification);
                }else if ("U".equalsIgnoreCase(accumulation_fund_status)){
                    //审核中
                    iv_provident_fund_credit.setBackgroundResource(R.drawable.credit_under_auth);
                }else if("A".equalsIgnoreCase(accumulation_fund_status)){
                    //已认证
                    iv_provident_fund_credit.setBackgroundResource(R.drawable.credit_finished);
                }else if("O".equalsIgnoreCase(accumulation_fund_status)){
                    //已过期
                    iv_provident_fund_credit.setBackgroundResource(R.drawable.credit_outdate_img);
                }else if("R".equalsIgnoreCase(accumulation_fund_status)){
                    //未通过
                    iv_provident_fund_credit.setBackgroundResource(R.drawable.credit_notpass);
                }

                if ("N".equalsIgnoreCase(mobile_operator_status)){
                    //待认证
                    iv_mobile_phone_operators.setBackgroundResource(R.drawable.waitcertification);
                }else if ("U".equalsIgnoreCase(mobile_operator_status)){
                    //审核中
                    iv_mobile_phone_operators.setBackgroundResource(R.drawable.credit_under_auth);
                }else if("A".equalsIgnoreCase(mobile_operator_status)){
                    //已认证
                    iv_mobile_phone_operators.setBackgroundResource(R.drawable.credit_finished);
                }else if("O".equalsIgnoreCase(mobile_operator_status)){
                    //已过期
                    iv_mobile_phone_operators.setBackgroundResource(R.drawable.credit_outdate_img);
                }else if("R".equalsIgnoreCase(mobile_operator_status)){
                    //未通过
                    iv_mobile_phone_operators.setBackgroundResource(R.drawable.credit_notpass);
                }

                if ("N".equalsIgnoreCase(online_shopping_status)){
                    //待认证
                    iv__electricity_business_information_certification.setBackgroundResource(R.drawable.waitcertification);
                }else if ("U".equalsIgnoreCase(online_shopping_status)){
                    //审核中
                    iv__electricity_business_information_certification.setBackgroundResource(R.drawable.credit_under_auth);
                }else if("A".equalsIgnoreCase(online_shopping_status)){
                    //已认证
                    iv__electricity_business_information_certification.setBackgroundResource(R.drawable.credit_finished);
                }else if("O".equalsIgnoreCase(online_shopping_status)){
                    //已过期
                    iv__electricity_business_information_certification.setBackgroundResource(R.drawable.credit_outdate_img);
                }else if("R".equalsIgnoreCase(online_shopping_status)){
                    //未通过
                    iv__electricity_business_information_certification.setBackgroundResource(R.drawable.credit_notpass);
                }
            }

            @Override
            public void failture(String tips) {
                Toast.makeText(getBaseContext(), tips, Toast.LENGTH_LONG).show();
            }
        });
    }
        //提额接口
    private void promoteAmount() {
        final CashLimitRequest request = new CashLimitRequest();
        request.setProduct_id(RyxcreditConfig.xjd_procudtId);
        request.setCustomer_type(xjd_user_level);
        //提额
        request.setFlag("M");
        httpsPost(this, request, ReqAction.CASH_LIMIT, CashLimitResponse.class, new ICallback<CashLimitResponse>() {

            public void success(CashLimitResponse cashLimitResponse) {
                Integer code = cashLimitResponse.getCode();
                String result = cashLimitResponse.getResult();
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(PromoteAmountActivity.this,tips+"");
            }
        });
    }
 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //可以根据多个请求代码来作相应的操作
        if(20==resultCode) {
            String status = data.getExtras().getString("status");
                if ("N".equalsIgnoreCase(status)) {
                    //待认证
                    iv_mobile_phone_operators.setBackgroundResource(R.drawable.waitcertification);
                } else if ("U".equalsIgnoreCase(status)) {
                    //审核中
                    iv_mobile_phone_operators.setBackgroundResource(R.drawable.credit_under_auth);
                } else if ("A".equalsIgnoreCase(status)) {
                    //已认证
                    iv_mobile_phone_operators.setBackgroundResource(R.drawable.credit_finished);
                } else if ("O".equalsIgnoreCase(status)) {
                    //已过期
                    iv_mobile_phone_operators.setBackgroundResource(R.drawable.credit_outdate_img);
                } else if ("R".equalsIgnoreCase(status)) {
                    //未通过
                    iv_mobile_phone_operators.setBackgroundResource(R.drawable.credit_notpass);
                }
            }
        super.onActivityResult(requestCode, resultCode, data);
    }*/
}
