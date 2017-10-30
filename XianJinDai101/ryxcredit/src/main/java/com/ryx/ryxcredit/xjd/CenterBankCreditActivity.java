package com.ryx.ryxcredit.xjd;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.services.UICallBack;
import com.ryx.ryxcredit.utils.CConstants;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CPreferenceUtil;
import com.ryx.ryxcredit.utils.HttpUtil;
import com.ryx.ryxcredit.xjd.bean.CentralBankCreditStatus.CentralBankCreditStatusRequest;
import com.ryx.ryxcredit.xjd.bean.CentralBankCreditStatus.CentralBankCreditStatusResponse;
import com.ryx.ryxcredit.xjd.bean.centralBankCredit.CentralBankCreditRequest;
import com.ryx.ryxcredit.xjd.bean.centralBankCredit.CentralBankCreditResponse;

import static com.ryx.ryxcredit.R.id.et_login_loginname_input;
import static com.ryx.ryxcredit.R.id.et_tv_login_authentication_input;
import static com.umeng.analytics.pro.x.F;
import static com.umeng.analytics.pro.x.I;
import static com.umeng.analytics.pro.x.S;

public class CenterBankCreditActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mtileleftImg;//左边的按钮
    private ImageView mtilerightImg;//右边的按钮
    private TextView mtv_title;//title
    private TextView mtv_login_report ;//授权瑞扶小贷获取你的央行征信报告
    private TextView mtv_login_loginname;//登录名
    private EditText met_login_loginname_input;//请输入
    private TextView mtv_login__loginpassword;//登录密码
    private EditText met_login_loginpassword_inpput;//请输入
 //   private TextView mtv_login_imageverificationcode;//图片验证码
    private EditText met_login_imageverificationcode_input;//请输入
    private TextView mtv_login_authentication;//身份验证码
    private EditText met_tv_login_authentication_input;//请输入
    private TextView mtv_login_how_get_idcardverification;//如何获取身份证验证码
    private Button mbt_login_submit;//提交
    private CheckBox mcb_login_Agreement;//同意授权瑞扶小贷获取您的征信报告
    private ImageView iv_showCode;
    private CentralBankCreditRequest centralBankCreditRequest ;
    private String loginName;
    private String loginPassword;
    private String imageVerificationCode;
    private String authentication;
    private CPreferenceUtil preferenceUtil;
    private String code;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_center_bank_credit);
        setTitleLayout("央行征信认证", true, false);
        preferenceUtil = CPreferenceUtil.getInstance(this.getApplication());
        initView();
    }
    //控件初始化
    private void initView() {
        mtileleftImg = (ImageView) findViewById(R.id.tileleftImg);
        mtilerightImg = (ImageView) findViewById(R.id.tilerightImg);
        mtv_title = (TextView) findViewById(R.id.tv_title);
        mtv_login_report = (TextView) findViewById(R.id.tv_login_report);
        mtv_login_loginname= (TextView) findViewById(R.id.tv_login_loginname);
        met_login_loginname_input= (EditText) findViewById(et_login_loginname_input);
        mtv_login__loginpassword= (TextView) findViewById(R.id.tv_login__loginpassword);
        met_login_loginpassword_inpput= (EditText) findViewById(R.id.et_login_loginpassword_inpput);
 //       mtv_login_imageverificationcode= (TextView) findViewById(R.id.tv_login_imageverificationcode);
 //       met_login_imageverificationcode_input= (EditText) findViewById(R.id.et_login_imageverificationcode_input);
        mtv_login_authentication= (TextView) findViewById(R.id.tv_login_authentication);
        met_tv_login_authentication_input= (EditText) findViewById(et_tv_login_authentication_input);
        mtv_login_how_get_idcardverification= (TextView) findViewById(R.id.tv_login_how_get_idcardverification);
        mbt_login_submit= (Button) findViewById(R.id.bt_login_submit);
        mcb_login_Agreement= (CheckBox) findViewById(R.id.cb_login_Agreement);
     //   iv_showCode = (ImageView) findViewById(R.id.iv_showCode);
        mtv_title.setText("央行征信认证");
        mtileleftImg.setVisibility(View.VISIBLE);
        //将验证码用图片的形式显示出来
    /*    iv_showCode.setImageBitmap(CCode.getInstance().createBitmap());
        code = CCode.getInstance().getCode();
        iv_showCode.setOnClickListener(this);*/
        mbt_login_submit.setOnClickListener(this);
        mtv_login_how_get_idcardverification.setOnClickListener(this);
    }



    private boolean checkInput() {
        loginName = met_login_loginname_input.getText().toString().trim();
        if (TextUtils.isEmpty(loginName)) {
            CLogUtil.showToast(CenterBankCreditActivity.this, "登录名不能为空");
            return false;
        }
         loginPassword = met_login_loginpassword_inpput.getText().toString().trim();
        if (TextUtils.isEmpty(loginPassword)) {
            CLogUtil.showToast(CenterBankCreditActivity.this, "登录密码不能为空！");
            return false;
        }
/*         imageVerificationCode = met_login_imageverificationcode_input.getText().toString().trim();
        if (TextUtils.isEmpty(imageVerificationCode)) {
            CLogUtil.showToast(CenterBankCreditActivity.this, "图片验证码不能为空！");
            return false;
        }*/
         authentication = met_tv_login_authentication_input.getText().toString().trim();
        if (TextUtils.isEmpty(authentication)) {
            CLogUtil.showToast(CenterBankCreditActivity.this, "身份验证码不能为空！");
            return false;
        }
/*        if (code.equalsIgnoreCase(imageVerificationCode)){

        }else {
            CLogUtil.showToast(CenterBankCreditActivity.this, "请填写正确的图片验证码！");
            return false;
        }*/
        return true;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        /*if (i == R.id.iv_showCode) {
            iv_showCode.setImageBitmap(CCode.getInstance().createBitmap());

        } else*/
            if (i == R.id.bt_login_submit) {
            //将数据保存在本地
            if(checkInput()){
                CentralBankCreditRequest centralBankCreditRequest = new CentralBankCreditRequest();
                centralBankCreditRequest.setBankcredit_name( met_login_loginname_input.getText().toString().trim());
                centralBankCreditRequest.setBankcredit_pass_word(met_login_loginpassword_inpput.getText().toString().trim());
                centralBankCreditRequest.setBankcredit_verification(met_tv_login_authentication_input.getText().toString().trim());
                savePersonalInfo();
                CLogUtil.showToast(CenterBankCreditActivity.this, "修改资料成功！");
               submitData();
            }else {
                savePersonalInfo();
            }


        }else if (i== R.id.tv_login_how_get_idcardverification){
            try {
            Intent intent = new Intent(CenterBankCreditActivity.this,Class.forName(getApplicationContext().getPackageName() + ".activity.HtmlMessageActivity_"));
            intent.putExtra("title","如何获取身份证验证码");
            intent.putExtra("urlkey", CConstants.CREDIT_ID_NUM_ADDRESS);
            startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                CLogUtil.showToast(CenterBankCreditActivity.this, "数据异常！");
            }
        }else  if(i==R.id.tileleftImg){
            finish();
        }

    }

    private void submitData() {
        CentralBankCreditRequest  centralBankCreditRequest = new CentralBankCreditRequest();
        centralBankCreditRequest.setBankcredit_name(loginName);
        centralBankCreditRequest.setBankcredit_pass_word(loginPassword);
        centralBankCreditRequest.setBankcredit_verification(authentication);
        HttpUtil.getInstance(this).httpsPost(centralBankCreditRequest, ReqAction.LOAN_BANKCREDIT_VERIFICATION, CentralBankCreditResponse.class, new ICallback<CentralBankCreditResponse>() {
            @Override
            public void success(CentralBankCreditResponse centralBankCreditResponse) {
              // CLogUtil.showToast(CenterBankCreditActivity.this, "客户信息审核中");
                bCreditStatus();
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(CenterBankCreditActivity.this, tips);

            }
        },new UICallBack() {
            @Override
            public void complete() {

            }
        });
    }
    //激活时人行征信状态
    private void bCreditStatus() {
        CentralBankCreditStatusRequest centralBankCreditStatusRequest = new CentralBankCreditStatusRequest();
        HttpUtil.getInstance(this).httpsPost(centralBankCreditStatusRequest, ReqAction.LOAN_BCREDITSTATUS, CentralBankCreditStatusResponse.class, new ICallback<CentralBankCreditStatusResponse>() {
            @Override
            public void success(CentralBankCreditStatusResponse centralBankCreditStatusResponse) {
               // CLogUtil.showToast(CenterBankCreditActivity.this, "客户信息审核中");
                result =centralBankCreditStatusResponse.getResult();
                if (I.equalsIgnoreCase(result)) {
                    CLogUtil.showToast(CenterBankCreditActivity.this, "信息校验中");
                }else if (S.equalsIgnoreCase(result)) {
                    CLogUtil.showToast(CenterBankCreditActivity.this, "信息校验成功");
                }else if (F.equalsIgnoreCase(result)) {
                    CLogUtil.showToast(CenterBankCreditActivity.this, "用户名或密码错误");
                }else if ("E".equalsIgnoreCase(result)) {
                    CLogUtil.showToast(CenterBankCreditActivity.this, "身份验证码错误");
                }

            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(CenterBankCreditActivity.this, tips);

            }
        },new UICallBack() {
            @Override
            public void complete() {

            }
        });
    }

    private void savePersonalInfo() {
        preferenceUtil.saveString("loginName",loginName);
        preferenceUtil.saveString("loginPassword",loginPassword);
        preferenceUtil.saveString("authentication",authentication);
    }
}
