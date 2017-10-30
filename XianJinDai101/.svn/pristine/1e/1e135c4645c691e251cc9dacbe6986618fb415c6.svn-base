package com.ryx.ryxcredit.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.utils.CCode;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_center_bank_credit);
        initView();
    }
    //控件初始化
    private void initView() {
        mtileleftImg = (ImageView) findViewById(R.id.tileleftImg);
        mtilerightImg = (ImageView) findViewById(R.id.tilerightImg);
        mtv_title = (TextView) findViewById(R.id.tv_title);
        mtv_login_report = (TextView) findViewById(R.id.tv_login_report);
        mtv_login_loginname= (TextView) findViewById(R.id.tv_login_loginname);
        met_login_loginname_input= (EditText) findViewById(R.id.et_login_loginname_input);
        mtv_login__loginpassword= (TextView) findViewById(R.id.tv_login__loginpassword);
        met_login_loginpassword_inpput= (EditText) findViewById(R.id.et_login_loginpassword_inpput);
     //   mtv_login_imageverificationcode= (TextView) findViewById(R.id.tv_login_imageverificationcode);
     //   met_login_imageverificationcode_input= (EditText) findViewById(R.id.et_login_imageverificationcode_input);
        mtv_login_authentication= (TextView) findViewById(R.id.tv_login_authentication);
        met_tv_login_authentication_input= (EditText) findViewById(R.id.et_tv_login_authentication_input);
        mtv_login_how_get_idcardverification= (TextView) findViewById(R.id.tv_login_how_get_idcardverification);
        mbt_login_submit= (Button) findViewById(R.id.bt_login_submit);
        mcb_login_Agreement= (CheckBox) findViewById(R.id.cb_login_Agreement);
     //   iv_showCode = (ImageView) findViewById(R.id.iv_showCode);
        mtv_title.setText("央行征信认证");
        mtileleftImg.setVisibility(View.VISIBLE);
        //将验证码用图片的形式显示出来
        iv_showCode.setImageBitmap(CCode.getInstance().createBitmap());
        iv_showCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        iv_showCode.setImageBitmap(CCode.getInstance().createBitmap());
    }
}
