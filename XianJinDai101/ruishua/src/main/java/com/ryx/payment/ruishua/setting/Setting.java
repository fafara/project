package com.ryx.payment.ruishua.setting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.HtmlMessageActivity_;
import com.ryx.payment.ruishua.activity.MainFragmentActivity_;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.usercenter.DeViceListActivity_;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.sobot.chat.SobotApi;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.MessageFormat;

@EActivity(R.layout.activity_main_setting)
public class Setting extends BaseActivity {

    @ViewById(R.id.tileleftImg)
    ImageView mBackImg;
    @ViewById(R.id.tilerightImg)
    ImageView mMsgImg;
    @ViewById(R.id.rl_setting_about_us)
    RelativeLayout mAboutUsRl;
    @ViewById(R.id.rl_setting_modify_password)
    RelativeLayout mModifyPassWordRl;
    @ViewById(R.id.rl_setting_share)
    RelativeLayout mShareRl;
    @ViewById(R.id.rl_setting_suggestion)
    RelativeLayout mSuggestionRl;
    @ViewById(R.id.rl_setting_version_info)
    RelativeLayout mVersionInfoRl;
    @ViewById(R.id.tv_setting_version_tip)
    TextView mVersionInfo;//版本信息显示
    @ViewById(R.id.rl_user_device)//我的设备
    RelativeLayout mDeviceRl;
//    @ViewById(R.id.rl_setting_modify_password)//修改密码
//    RelativeLayout modify_password;
    @ViewById(R.id.rl_user_account_safe)//账户安全
    RelativeLayout mUserNoteRl;
    @ViewById(R.id.btn_user_exit)
    Button  mExitBtn;

    @AfterViews
    public void initViews() {
        setTitleLayout("设置",true,false);
        String updateInfo = PreferenceUtil.getInstance(Setting.this).getString("updateInfo", "不需要更新");
        mVersionInfo.setText(updateInfo);
        initQtPatParams();
    }

    @Click(R.id.tileleftImg)
    public void backBtnClick() {
        finish();
    }

    @Click(R.id.rl_setting_modify_password)
    public void modifyPwdClick() {
//        UpdatePaswdActivity_
//        ChangePassWord_
        Intent intent = new Intent(this,ChangePassWord_.class);
        startActivity(intent);
    }
    @Click(R.id.btn_user_exit)
    public void setExitBtn() {
        showExitDialog();
    }

    @Click(R.id.rl_setting_share)
    public void shareClick() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Share");

            intent.putExtra(
                    Intent.EXTRA_TEXT,
                    MessageFormat.format(
                            getResources().getString(R.string.msg_share_content),
                            RyxAppdata.getInstance(this).getCurrentBranchName(),
                            RyxAppdata.getInstance(this).getCurrentBranchDownLoadAddress()));

//            if (RyxAppconfig.BRANCH.equals("01")) {
//                intent.putExtra(
//                        Intent.EXTRA_TEXT,
//                        MessageFormat.format(
//                                getResources().getString(R.string.msg_share_content),
//                                getResources().getString(R.string.app_name),
//                                getResources().getString(R.string.service_download)));
//            } else if (RyxAppconfig.BRANCH.equals("02")) {
//                intent.putExtra(
//                        Intent.EXTRA_TEXT,
//                        MessageFormat.format(
//                                getResources().getString(R.string.msg_share_content),
//                                getResources().getString(R.string.app_name_ryx),
//                                getResources().getString(R.string.service_download_ryx)));
//            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(Intent.createChooser(intent, getTitle()));
        } catch (Exception e) {
            LogUtil.showToast(getApplicationContext(),
                    getResources().getString(R.string.msg_error_not_support_email));
            return;
        }
    }

    @Click(R.id.rl_setting_version_info)
    public void versionInfoClick() {
        Intent intent = new Intent(this, Update_.class);
        startActivity(intent);
    }

    @Click(R.id.rl_setting_suggestion)
    public void suggestionClick() {
        Intent intent = new Intent(this, FeedBack_.class);
        startActivity(intent);
    }
    @Click(R.id.rl_user_device)
    public void setUserDevice() {
        //跳转我的设备
        Intent intent = new Intent(this, DeViceListActivity_.class);
        startActivity(intent);
    }
    @Click(R.id.rl_user_account_safe)
    public void setAccountSafe() {
        //跳转账户安全
        Intent intent = new Intent(this,UpdatePaswdActivity_.class);
        startActivity(intent);
    }



    @Click(R.id.rl_setting_about_us)
    public void aboutUsClick() {
        Intent intent = new Intent(this, HtmlMessageActivity_.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", "关于我们");
        bundle.putString("urlkey", RyxAppconfig.Notes_About);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void showExitDialog() {
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(Setting.this, new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                doExit();
            }

            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent("确认要安全退出吗？");
    }

    public void doExit() {
        qtpayApplication = new Param("application");
        qtpayApplication.setValue("UserLoginExit.Req");
        qtpayAttributeList.add(qtpayApplication);
        httpsPost(false, true, "UserLoginExit", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                QtpayAppData.getInstance(Setting.this)
                        .setLogin(false);
                QtpayAppData.getInstance(Setting.this).setRealName("");
                QtpayAppData.getInstance(Setting.this)
                        .setMobileNo("");
                QtpayAppData.getInstance(Setting.this)
                        .setPhone("");
                QtpayAppData.getInstance(Setting.this).setCustomerId("");
                QtpayAppData.getInstance(Setting.this).setAuthenFlag(0);
                QtpayAppData.getInstance(Setting.this).setCustomerName("");
                QtpayAppData.getInstance(Setting.this).setToken("");
                SobotApi.exitSobotChat(Setting.this);
                //清空接口AppKey集合
                RyxAppdata.resultKeyList.clear();
                finish();
                startActivity(new Intent(Setting.this, MainFragmentActivity_.class));
            }
        });
    }
}
