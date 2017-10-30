package com.ryx.ryxcredit.newfragment.baseinfo;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.livedetect.utils.LogUtil;
import com.rey.material.widget.Button;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.services.UICallBack;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CPreferenceUtil;
import com.ryx.ryxcredit.utils.HttpUtil;
import com.ryx.ryxcredit.widget.RyxCreditLoadDialog;
import com.ryx.ryxcredit.xjd.BaseInfoSuccesActivity;
import com.ryx.ryxcredit.xjd.CommonH5Activity;
import com.ryx.ryxcredit.xjd.bean.CreateTask.CreateTaskRequest;
import com.ryx.ryxcredit.xjd.bean.CreateTask.CreateTaskResponse;
import com.ryx.ryxcredit.xjd.bean.SearchTask.SearchTaskRequest;
import com.ryx.ryxcredit.xjd.bean.SearchTask.SearchTaskResponse;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaoBaoPhonenumberFragment extends Fragment implements View.OnClickListener {
    private BaseInfoSuccesActivity baseInfoActivity;
    private BaseInfoSuccesActivity callback;
    private CPreferenceUtil preferenceUtil;
    private View rootView;
    private AutoLinearLayout ll_sitchooseone_taobao_account;
    private EditText et_sitchooseone_taobao_account;
    private AutoLinearLayout ll_sitchooseone_taobao_verification_code;
    private EditText et_sitchooseone_taobao_verification_code;
    private CheckBox cb_sitchooseone_taobao_agreement;
    private Button bt_sitchooseone_taobao_next;
    private int cb_sitchooseone_taobao_agreement_num;
    private TextView tv_sitchooseone_taobao_agreement;
    private int secondsRremaining;
    private int inputCount;
    private String sitchooseone_taobao_accountEt;
    private String sitchooseone_taobao_verification_codeEt;
    private Timer mtimer;
    private SearchTaskResponse.ResultBean searchTaskResult;
    private String searchTaskStatus;
    private TextView tv_get_verification_code;
    private int frequency;
    private String authorise_info_url;
    private String CreateTaskResult;
    private String submitDataResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseInfoActivity = (BaseInfoSuccesActivity) getActivity();
        callback = (BaseInfoSuccesActivity) getActivity();
        preferenceUtil = CPreferenceUtil.getInstance(getActivity().getApplication());
        rootView = inflater.inflate(R.layout.xjd_sixchooseone_fragment_tao_bao_phonenumber, container, false);
        authorise_info_url = ((BaseInfoSuccesActivity) getActivity()).getAuthorise_info_url();
        initView();
        cb_sitchooseone_taobao_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_sitchooseone_taobao_agreement.isChecked()) {
                    cb_sitchooseone_taobao_agreement_num = 0;
                } else {
                    cb_sitchooseone_taobao_agreement_num = 1;
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        inputCount=0;
    }
    /*
    * 初始化控件
    * */
    private void initView() {
        ll_sitchooseone_taobao_account = (AutoLinearLayout) rootView.findViewById(R.id.xjd_ll_sitchooseone_taobao_account);
        et_sitchooseone_taobao_account = (EditText) rootView.findViewById(R.id.xjd_et_sitchooseone_taobao_account);
        ll_sitchooseone_taobao_verification_code = (AutoLinearLayout) rootView.findViewById(R.id.xjd_ll_sitchooseone_taobao_verification_code);
        et_sitchooseone_taobao_verification_code = (EditText) rootView.findViewById(R.id.xjd_et_sitchooseone_taobao_verification_code);
        cb_sitchooseone_taobao_agreement = (CheckBox) rootView.findViewById(R.id.xjd_cb_sitchooseone_taobao_agreement);
        bt_sitchooseone_taobao_next = (Button) rootView.findViewById(R.id.xjd_bt_sitchooseone_taobao_next);
        tv_sitchooseone_taobao_agreement = (TextView) rootView.findViewById(R.id.xjd_tv_sitchooseone_taobao_agreement);
        tv_get_verification_code = (TextView) rootView.findViewById(R.id.xjd_tv_get_verification_code);
        ll_sitchooseone_taobao_account.setOnClickListener(this);
        ll_sitchooseone_taobao_verification_code.setOnClickListener(this);
        tv_get_verification_code.setOnClickListener(this);
        cb_sitchooseone_taobao_agreement.setOnClickListener(this);
        bt_sitchooseone_taobao_next.setOnClickListener(this);
        tv_sitchooseone_taobao_agreement.setOnClickListener(this);
    }

    /*
    * 检测内容是否输入完整
    * */
    private boolean checkInput() {
         sitchooseone_taobao_accountEt = et_sitchooseone_taobao_account.getText().toString().trim();
        if (TextUtils.isEmpty(sitchooseone_taobao_accountEt)) {
            CLogUtil.showToast(getActivity(), "手机号不能为空!");
            return false;
        }
         sitchooseone_taobao_verification_codeEt = et_sitchooseone_taobao_verification_code.getText().toString().trim();
        if (TextUtils.isEmpty(sitchooseone_taobao_verification_codeEt)) {
            CLogUtil.showToast(getActivity(), "淘宝验证码不能为空!");
            return false;
        }
        if (cb_sitchooseone_taobao_agreement_num == 1) {
            CLogUtil.showToast(getActivity(), "请同意授权信息使用协议!");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.xjd_bt_sitchooseone_taobao_next) {
            if (checkInput()) {
                //如果第一次进入调用第一个接口，否则调用第三个接口（接口设计缺陷）
                updataData();
            }
        }else if (id==R.id.xjd_tv_get_verification_code){
            sitchooseone_taobao_accountEt = et_sitchooseone_taobao_account.getText().toString().trim();
            sitchooseone_taobao_accountEt = et_sitchooseone_taobao_account.getText().toString().trim();
            if (TextUtils.isEmpty(sitchooseone_taobao_accountEt)) {
                CLogUtil.showToast(getActivity(), "手机号不能为空!");
            }else{
                setVertifyText();
                sendVertifyCode();
                //如果第一次进入调用第一个接口，否则调用第三个接口（接口设计缺陷）
                submitData();
            }
           /* if(inputCount==0) {
            }else{
                updataData();
            }*/

        }else if (id == R.id.xjd_cb_sitchooseone_taobao_agreement) {

            cb_sitchooseone_taobao_agreement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cb_sitchooseone_taobao_agreement.isChecked()) {
                        cb_sitchooseone_taobao_agreement_num = 0;
                    } else {
                        cb_sitchooseone_taobao_agreement_num = 1;
                    }
                }
            });
        }else if(id==R.id.xjd_tv_sitchooseone_taobao_agreement){
            //协议地址
            Intent intent = new Intent(baseInfoActivity, CommonH5Activity.class);
            intent.putExtra("url_address",authorise_info_url);
            intent.putExtra("title","授权信息使用协议");
            startActivity(intent);
            CLogUtil.showLog("协议");
        }
    }

    /*
   *淘宝登陆接口
   * */
    private void submitData() {
        CreateTaskRequest createTaskRequest = new CreateTaskRequest();
        createTaskRequest.setLogin_phone(sitchooseone_taobao_accountEt);
        /*createTaskRequest.setLogin_name("");
        createTaskRequest.setLogin_password("");*/
        HttpUtil.getInstance(getActivity()).httpsPost(createTaskRequest, ReqAction.TAOBAO_CREATTASK, CreateTaskResponse.class, new ICallback<CreateTaskResponse>() {
            @Override
            public void success(CreateTaskResponse CreateTaskResponse) {
                submitDataResult = CreateTaskResponse.getResult();
                int CreateTaskCode = CreateTaskResponse.getCode();
                if (CreateTaskCode==5031) {
                    baseInfoActivity.showMaintainDialog();
                } else {
                    if ("SUCCESS".equalsIgnoreCase(submitDataResult)) {
                        CLogUtil.showToast(getActivity(), "客户信息审核中");
                        bt_sitchooseone_taobao_next.setVisibility(View.VISIBLE);
                        RyxCreditLoadDialog.getInstance(getActivity()).setMessage("授权需要1-2分钟，请不要退出!");
                        RyxCreditLoadDialog.getInstance(getActivity()).show();
                        doResearch();
                    } else if ("FALSE".equalsIgnoreCase(submitDataResult)) {
                        CLogUtil.showToast(getActivity(), "请稍后重试");
                    }
                }
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(getActivity(), tips);
                RyxCreditLoadDialog.getInstance(getActivity()).dismiss();
            }

        },new UICallBack() {
            @Override
            public void complete() {

            }
        });
    }
    /*
    * 淘宝登陆接口(第二次进入的时候)
    * */
    private void updataData() {
        CreateTaskRequest createTaskRequest = new CreateTaskRequest();
        createTaskRequest.setLogin_phone(sitchooseone_taobao_accountEt);
        createTaskRequest.setSmsCode(et_sitchooseone_taobao_verification_code.getText().toString().trim());
        HttpUtil.getInstance(getActivity()).httpsPost(createTaskRequest, ReqAction.TAOBAO_UPDATATASK, CreateTaskResponse.class, new ICallback<CreateTaskResponse>() {
            @Override
            public void success(CreateTaskResponse CreateTaskResponse) {
                CreateTaskResult = CreateTaskResponse.getResult();
                int CreateTaskCode = CreateTaskResponse.getCode();
                if (CreateTaskCode==5031) {
                    baseInfoActivity.showMaintainDialog();
                } else {
                    if ("SUCCESS".equalsIgnoreCase(CreateTaskResult)) {
                        CLogUtil.showToast(getActivity(), "客户信息审核中");
                        RyxCreditLoadDialog.getInstance(getActivity()).setMessage("授权需要1-2分钟，请不要退出!");
                        RyxCreditLoadDialog.getInstance(getActivity()).show();
                        doResearch();
                    } else if ("FALSE".equalsIgnoreCase(CreateTaskResult)) {
                        CLogUtil.showToast(getActivity(), "请稍后重试");
                    } else if ("OTHER".equalsIgnoreCase(CreateTaskResult)) {
                        CLogUtil.showToast(getActivity(), "请正确填写");
                    }
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
                bTaobaoStatus();

            }
        }, 10000, 10000);
    }
    //淘宝状态接口
    private void bTaobaoStatus() {
        SearchTaskRequest searchTaskRequest = new SearchTaskRequest();
        HttpUtil.getInstance(getActivity()).httpsPost(searchTaskRequest, ReqAction.TAOBAO_SEARCHTASK, SearchTaskResponse.class, new ICallback<SearchTaskResponse>() {
            @Override
            public void success(SearchTaskResponse searchTaskResponse) {
                RyxCreditLoadDialog.getInstance(getActivity()).dismiss();
                searchTaskResult =searchTaskResponse.getResult();
                searchTaskStatus = searchTaskResult.getStatus();
                int searchTaskCode = searchTaskResponse.getCode();
                if (searchTaskCode==5031) {
                    baseInfoActivity.showMaintainDialog();
                } else {
                    if ("login".equalsIgnoreCase(searchTaskStatus)) {
                        //CLogUtil.showToast(getActivity(), "正在登陆");
                        RyxCreditLoadDialog.getInstance(getActivity()).setMessage("授权需要1-2分钟，请不要退出!");
                        RyxCreditLoadDialog.getInstance(getActivity()).show();
                        if (frequency == 12) {
                            CLogUtil.showToast(getActivity(), "审核超时，请重试.....");
                            RyxCreditLoadDialog.getInstance(getActivity()).dismiss();
                            mtimer.cancel();
                        } else {
                            frequency++;
                        }
                    } else if ("smsCode".equalsIgnoreCase(searchTaskStatus)) {
                        CLogUtil.showToast(getActivity(), "请输入验证码");
                        mtimer.cancel();
                    } else if ("smsReady".equalsIgnoreCase(searchTaskStatus)) {
                        CLogUtil.showToast(getActivity(), "验证码有误，请重新获取!");
                        mtimer.cancel();
                    } else if ("password".equalsIgnoreCase(searchTaskStatus)) {
                        CLogUtil.showToast(getActivity(), "请重新输入密码");
                        mtimer.cancel();
                    } else if ("nameAndWord".equalsIgnoreCase(searchTaskStatus)) {
                        CLogUtil.showToast(getActivity(), "请重新登录输入账号密码");
                        mtimer.cancel();
                    } else if ("A".equalsIgnoreCase(searchTaskStatus)) {
                        // CLogUtil.showToast(getActivity(), "用户可以进行下一步");
                        /*mtimer.cancel();
                        callback.setFaceCollectInfo();*/
                        if (mtimer != null) {
                            mtimer.cancel();
                            mtimer = null;
                            RyxCreditLoadDialog.getInstance(getActivity()).dismiss();
                            if(mtimer == null){
                                callback.setFaceCollectInfo();
                            }
                        }
                    }
                }
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(getActivity(), tips);
                RyxCreditLoadDialog.getInstance(getActivity()).dismiss();
                mtimer.cancel();
            }
        }, new UICallBack() {
            @Override
            public void complete() {

            }
        });
    }

    //设置“发送验证码”文字样式
    private void setVertifyText() {
        if (smsTimer == null) {
            tv_get_verification_code.setText(getResources().getString(R.string.c_common_send_verify_code));
            tv_get_verification_code.setClickable(true);
            tv_get_verification_code.setTextColor(Color.parseColor("#1db7f0"));
        } else {
            tv_get_verification_code.setTextColor(getResources().getColor(R.color.text_a));
            tv_get_verification_code.setText(getResources().getString(R.string.resend)
                    + "(" + secondsRremaining + ")");
            tv_get_verification_code.setClickable(false);
        }
    }

    private void sendVertifyCode() {
        if (smsTimer != null) {
            return;
        }
        smsTimer = new Timer();
        startCountdown();
    }
    /**
     * 开始倒计时60秒
     */
    public void startCountdown() {
        secondsRremaining = 60;
        TimerTask task = new TimerTask() {

            public void run() {
                Message msg = Message.obtain();
                msg.what = secondsRremaining--;
                int i = msg.what;
                LogUtil.i("我是i",i+"");
                timeHandler.sendMessage(msg);
            }
        };
        smsTimer.schedule(task, 1000, 1000);
    }

    private Timer smsTimer;
    Handler timeHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what > 0) {
                if (isAdded()) {
                    tv_get_verification_code.setTextColor(getResources().getColor(R.color.text_a));
                    tv_get_verification_code.setText(getResources().getString(R.string.resend)
                            + "(" + msg.what + ")");
                    tv_get_verification_code.setClickable(false);
                }
            } else {
                smsTimer.cancel();
                smsTimer = null;
                if (isAdded()) {
                    tv_get_verification_code.setText(getResources().getString(
                            R.string.resend_verification_code));
                    tv_get_verification_code.setClickable(true);
                    tv_get_verification_code.setTextColor(Color.parseColor("#1db7f0"));
                }
            }
        }
    };
}