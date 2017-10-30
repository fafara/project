package com.ryx.ryxcredit.newfragment.baseinfo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.services.UICallBack;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CPreferenceUtil;
import com.ryx.ryxcredit.utils.HttpUtil;
import com.ryx.ryxcredit.widget.RyxCreditLoadDialog;
import com.ryx.ryxcredit.xjd.BaseInfoSuccesActivity;
import com.ryx.ryxcredit.xjd.bean.EmailBill.EmailBillRequest;
import com.ryx.ryxcredit.xjd.bean.EmailBill.EmailBillResponse;
import com.ryx.ryxcredit.xjd.bean.EmailBillStatus.EmailBillStatusRequest;
import com.ryx.ryxcredit.xjd.bean.EmailBillStatus.EmailBillStatusResponse;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreditCardBillFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private BaseInfoSuccesActivity baseInfoActivity;
    private BaseInfoSuccesActivity callback;
    private CPreferenceUtil preferenceUtil;
    private View rootView;
    private Spinner sp_creditcardbill;
    // 数据源
    private String[] city = {"@qq.com","@163.com", "@sina.com", "@sina.cn"};
    private ArrayAdapter adapter = null;
    private String mailbox;
    private AutoLinearLayout ll_sitchooseone_creditcardbill_account;
    private EditText et_sitchooseone_creditcardbill_account;
    private AutoLinearLayout ll_sitchooseone_creditcardbill_password;
    private EditText et_sitchooseone_creditcardbill_password;
    private CheckBox sitchooseone_creditcardbill_agreementCb;
    private com.rey.material.widget.Button bt_sitchooseone_creditcardbill_next;
    private TextView tv_sitchooseone_creditcardbill_agreement;
    private int cb_sitchooseone_taobao_agreement_num;
    private String sitchooseone_creditcardbill_accountEt;
    private String sitchooseone_creditcardbill_passwordEt;
    private String emailBillResult;
    private Timer mtimer;
    private String emailBillStatusResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseInfoActivity = (BaseInfoSuccesActivity) getActivity();
        callback = (BaseInfoSuccesActivity) getActivity();
        preferenceUtil = CPreferenceUtil.getInstance(getActivity().getApplication());
        rootView = inflater.inflate(R.layout.xjd_employee_fragment_credit_card_bill, container, false);
        initView();
        return rootView;
    }
/*
* 初始化控件
* */
    private void initView() {
        ll_sitchooseone_creditcardbill_account = (AutoLinearLayout) rootView.findViewById(R.id.xjd_ll_sitchooseone_creditcardbill_account);
        et_sitchooseone_creditcardbill_account = (EditText) rootView.findViewById(R.id.xjd_et_sitchooseone_creditcardbill_account);
        ll_sitchooseone_creditcardbill_password = (AutoLinearLayout) rootView.findViewById(R.id.xjd_ll_sitchooseone_creditcardbill_password);
        et_sitchooseone_creditcardbill_password = (EditText) rootView.findViewById(R.id.xjd_et_sitchooseone_creditcardbill_password);
        sitchooseone_creditcardbill_agreementCb = (CheckBox) rootView.findViewById(R.id.xjd_sitchooseone_creditcardbill_agreement);
        bt_sitchooseone_creditcardbill_next = (com.rey.material.widget.Button) rootView.findViewById(R.id.xjd_bt_sitchooseone_creditcardbill_next);
        tv_sitchooseone_creditcardbill_agreement = (TextView) rootView.findViewById(R.id.xjd_tv_sitchooseone_creditcardbill_agreement);
        sp_creditcardbill = (Spinner) rootView.findViewById(R.id.xjd_sp_creditcardbill);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, city);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_creditcardbill.setAdapter(adapter);
        sp_creditcardbill.setOnItemSelectedListener(this);
        sitchooseone_creditcardbill_agreementCb.setOnClickListener(this);
        bt_sitchooseone_creditcardbill_next.setOnClickListener(this);
    }
/*
* 检测内容是否输入完整
* */
    private boolean checkInput() {
         sitchooseone_creditcardbill_accountEt = et_sitchooseone_creditcardbill_account.getText().toString().trim();
        if (TextUtils.isEmpty(sitchooseone_creditcardbill_accountEt)) {
            CLogUtil.showToast(getActivity(), "邮箱账号不能为空!");
            return false;
        }

        if (sitchooseone_creditcardbill_accountEt.contains("@")) {
            CLogUtil.showToast(getActivity(), "请填写正确的邮箱账号!");
            return false;
        }
         sitchooseone_creditcardbill_passwordEt = et_sitchooseone_creditcardbill_password.getText().toString().trim();
        if (TextUtils.isEmpty(sitchooseone_creditcardbill_passwordEt)) {
            CLogUtil.showToast(getActivity(), "邮箱密码不能为空!");
            return false;
        }
        if (cb_sitchooseone_taobao_agreement_num ==1) {
            CLogUtil.showToast(getActivity(), "请同意授权信息使用协议!");
            return false;
        }
        return true;
    }
/*
*
* spinner的点击事件
* */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         mailbox = (String) adapter.getItem(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
/*
*button按钮的点击事件
* */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id==R.id.xjd_bt_sitchooseone_creditcardbill_next){
                if (checkInput()){
                    submitData();
                }
        }else if(id==R.id.xjd_sitchooseone_creditcardbill_agreement){
            sitchooseone_creditcardbill_agreementCb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sitchooseone_creditcardbill_agreementCb.isChecked()) {
                        cb_sitchooseone_taobao_agreement_num =0;
                    }else{
                        cb_sitchooseone_taobao_agreement_num =1;
                    }
                }
            });
        }else if(id==R.id.xjd_tv_sitchooseone_creditcardbill_agreement){
            //跳转协议
            CLogUtil.showLog("协议");
        }
    }
/*
* 电子账单登陆
* */
    private void submitData() {
        EmailBillRequest emailBillRequest = new EmailBillRequest();
        emailBillRequest.setLogin_name(sitchooseone_creditcardbill_accountEt);
        emailBillRequest.setLogin_password(sitchooseone_creditcardbill_passwordEt);
        HttpUtil.getInstance(getActivity()).httpsPost(emailBillRequest, ReqAction.CREDIT_EMAILBILL,
                EmailBillResponse.class, new ICallback<EmailBillResponse>() {
                    @Override
                    public void success(EmailBillResponse emailBillResponse) {
                        RyxCreditLoadDialog.getInstance(getActivity()).setMessage("授权需要1-2分钟，请不要退出!");
                        RyxCreditLoadDialog.getInstance(getActivity()).show();
                        emailBillResult= emailBillResponse.getResult();
                        doResearch();
                    }

                    @Override
                    public void failture(String tips) {
                        CLogUtil.showToast(getActivity(), tips);
                    }
                }, new UICallBack() {
                    @Override
                    public void complete() {

                    }
    });

    }
/*
* 电子账单状态接口
* */
    private void bCreditStatus() {
        EmailBillStatusRequest emailBillStatusRequest = new EmailBillStatusRequest();
        HttpUtil.getInstance(getActivity()).httpsPost(emailBillStatusRequest, ReqAction.LOAN_BANKCREDIT_VERIFICATION, EmailBillStatusResponse.class, new ICallback<EmailBillStatusResponse>() {
            @Override
            public void success(EmailBillStatusResponse emailBillStatusResponse) {
                emailBillStatusResult =emailBillStatusResponse.getResult();
                if ("I".equalsIgnoreCase(emailBillStatusResult)){
                    CLogUtil.showToast(getActivity(), "信息校验中");
                }else if ("S".equalsIgnoreCase(emailBillStatusResult)){
                    CLogUtil.showToast(getActivity(), "信息抓取成功");
                }else if ("F".equalsIgnoreCase(emailBillStatusResult)){
                    CLogUtil.showToast(getActivity(), "用户名或密码错误");
                }else if ("D".equalsIgnoreCase(emailBillStatusResult)){
                    CLogUtil.showToast(getActivity(), "信息校验失败");
                }
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(getActivity(), tips);

            }
        },new UICallBack() {
            @Override
            public void complete() {

            }
        });
    }

    //轮询search接口
    private void doResearch() {
        mtimer = new Timer();
        mtimer.schedule(new TimerTask() {
            @Override
            public void run() {
                bCreditStatus();

            }
        }, 10000, 10000);
    }
}
