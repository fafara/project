package com.ryx.ryxcredit.fragment.products;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.CheckBox;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.activity.ActivateLineActivity;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.activity.CreditActivity;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.beans.bussiness.agreement.AgreementRequest;
import com.ryx.ryxcredit.beans.bussiness.agreement.AgreementResponse;
import com.ryx.ryxcredit.beans.bussiness.product.CfindIndexRouteResponse;
import com.ryx.ryxcredit.beans.bussiness.product.CfindRouteRequest;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.CDensityUtil;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.widget.CRyxSlideUnlock;

/**
 * Created by DIY on 2016/9/8.
 */
public class RuiqiaoloanFragment extends Fragment {

    private BaseActivity mBaseActivity;
    private CheckBox cb_agree;
    private TextView tv_contract;

    private TextView tv_enter_main;//“猛戳进入”按钮
    private String service_status;//瑞卡贷服务显示按钮状态
    private String service_url;//瑞卡贷服务地址
    private String service_sign_status;//瑞卡贷服务显示勾选图标
    private String service_sign;//服务协议单选框是否选中
    private boolean isContractAg;//服务协议是否选择单选框
    private String agreement_url;//非正式协议地址
    private String agreementStatus;//非正式协议是否显示
    private String enter_status;//"猛戳进入"按钮是否显示
    private String enter_type;//点"猛戳进入"显示的内容类型
    private String enter_action;//点"猛戳进入"显示的内容
    //    private boolean isAgree;//俱乐部协议是否签署成功
//    private boolean isService;//是否签署服务协议成功
    private String agreementId = "1";//俱乐部服务协议
    private String serviceId = "2";//瑞卡贷服务协议

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.c_fragment_ruiqiaobao_pruduct, null, false);
        rootview.findViewById(R.id.c_ll_ruiqiaodai_product).setOnClickListener(clickListener);
        rootview.setOnClickListener(clickListener);
        tv_enter_main = (TextView) rootview.findViewById(R.id.tv_enter_main);
        tv_enter_main.setOnClickListener(clickListener);
        tv_contract = (TextView) rootview.findViewById(R.id.tv_contract);
        tv_contract.setOnClickListener(clickListener);
        tv_contract.setVisibility(View.GONE);
        cb_agree = (CheckBox) rootview.findViewById(R.id.cb_agree);
        cb_agree.setVisibility(View.GONE);
        return rootview;
    }

    @Override
    public void onAttach(Context context) {
        try {
            mBaseActivity = (BaseActivity) getActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getRoute();
        super.onActivityCreated(savedInstanceState);
    }

    NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {
            //瑞卡贷服务授权书
            if (view.getId() == R.id.tv_contract) {
                if ("3".equals(service_status)) {//服务协议可见可用才能点进来
                    Intent intent = null;
                    try {
                        intent = new Intent(getActivity(), Class.forName(getActivity().getApplicationContext().getPackageName() + ".activity.HtmlMessageActivity_"));
                        intent.putExtra("ccurl", service_url);
                        intent.putExtra("title", "瑞卡贷产品服务协议");
                        startActivity(intent);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else if (view.getId() == R.id.tv_enter_main) {
                //“猛戳进入”按钮状态
                if ("3".equals(enter_status)) {
                    //服务授权书和单选框不可见
                    if ("0".equals(service_status) || "0".equals(service_sign_status)) {
                        getRouteAgain();
                    } else {
                        //服务授权书签署成功
                        if ("1".equals(service_sign)) {
                            getRouteAgain();
                        } else if ("0".equals(service_sign)) {//服务协议签署未成功
                            if (!isContractAg) {
                                CLogUtil.showToast(getActivity(), "请先同意服务协议!");
                                return;
                            }
                            signService();
                        }
                    }
                } else if ("1".equals(enter_status)) {//猛戳进入按钮可见不可用
                    if ("TEXT".equals(enter_type) && !TextUtils.isEmpty(enter_action)) {
                        CLogUtil.showToast(getActivity(), enter_action);
                    }
                }
            }
        }
    };

    //页面跳转或者提示文字
    private void enterBtnClick() {
        //未签署协议，继续签署
        if ("TEXT".equals(enter_type) && !TextUtils.isEmpty(enter_action)) {
            CLogUtil.showToast(getActivity(), enter_action);
        } else if ("PAGE".equals(enter_type)) {
            jumpPage(enter_action);
        } else if ("URL".equals(enter_type)) {
            //显示预约活动页面
            if (!TextUtils.isEmpty(enter_action)) {
                try {
                    Intent intent = new Intent(getActivity(), Class.forName(getActivity().getPackageName() + ".activity.HtmlMessageActivity_"));
                    intent.putExtra("ccurl", enter_action + "?customerId=" +
                            RyxcreditConfig.getCustomerId() + "&phoneNo=" + RyxcreditConfig.getPhoneNo());
                    intent.putExtra("title", "");
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return;
        }

    }

    //点击猛戳进入的时候再次请求路由接口
    private void getRouteAgain() {
        final CfindRouteRequest request = new CfindRouteRequest();
        request.setKey("index_page");
        ((BaseActivity) getActivity()).httpsPost(getActivity(), request, ReqAction.APPLICATION_ROUTE, CfindIndexRouteResponse.class, new ICallback<CfindIndexRouteResponse>() {
            @Override
            public void success(CfindIndexRouteResponse routeResponse) {
                CfindIndexRouteResponse.ResultBean result = routeResponse.getResult();
                if (result != null) {
                    initView(result);
                }
                enterBtnClick();
            }

            @Override
            public void failture(String tips) {

            }
        });
    }

    //请求产品列表页路由接口
    private void getRoute() {
        final CfindRouteRequest request = new CfindRouteRequest();
        request.setKey("index_page");
        ((BaseActivity) getActivity()).httpsPost(getActivity(), request, ReqAction.APPLICATION_ROUTE, CfindIndexRouteResponse.class, new ICallback<CfindIndexRouteResponse>() {
            @Override
            public void success(CfindIndexRouteResponse routeResponse) {
                CfindIndexRouteResponse.ResultBean result = routeResponse.getResult();
                if (result != null) {
                    initView(result);
                }
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(getActivity(), tips);
            }
        });
    }

    //初始化路由接口
    private void initView(CfindIndexRouteResponse.ResultBean result) {
        service_status = result.getService_status();
        service_url = result.getService_url();
        service_sign_status = result.getService_sign_status();
        service_sign = result.getService_sign();
        agreementStatus = result.getAgreement_status();
        agreement_url = result.getAgreement_url();
        enter_status = result.getEnter_status();
        enter_type = result.getEnter_type();
        enter_action = result.getEnter_action();
        //瑞卡贷服务协议是否显示
        switch (service_status) {
            case "0"://0:不可见不可用
                tv_contract.setVisibility(View.GONE);
                break;
            case "1"://1:可见不可用
            case "3"://3:可用可见
                tv_contract.setVisibility(View.VISIBLE);
                break;
        }
        //服务协议单选框是否显示
        switch (service_sign_status) {
            case "0"://0:不可见不可用
                cb_agree.setVisibility(View.GONE);
                break;
            case "1"://1:可见不可用
            case "3"://3:可用可见
                cb_agree.setVisibility(View.VISIBLE);
                break;
        }
        //服务协议单选框是否勾选
        switch (service_sign) {
            case "0"://未勾选
                isContractAg = false;
                cb_agree.setChecked(false);
                break;
            case "1"://勾选
                isContractAg = true;
                cb_agree.setChecked(true);
                break;
        }
        //猛戳进入按钮是否显示
        switch (enter_status) {
            case "0":
                tv_enter_main.setVisibility(View.GONE);
                break;
            case "1":
            case "3":
                tv_enter_main.setVisibility(View.VISIBLE);
                break;
        }
        //服务授权书“可见不可用”，或者用户已经签署协议成功了
        if ("1".equals(service_sign_status) || "1".equals(service_sign)) {
            cb_agree.setClickable(false);
        }else{
            cb_agree.setClickable(true);
        }
        //服务协议单选框是否可以选中
        cb_agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isContractAg = isChecked;
            }
        });
        //俱乐部服务协议可见，可以右滑签署
        if ("3".equals(agreementStatus) && !TextUtils.isEmpty(agreement_url)) {
            showAgreeDialog();
        }
    }

    //页面跳转
    private void jumpPage(String enter_action) {
        if ("home_page".equals(enter_action)) {//已经激活，或者重新激活的
            Intent intent = new Intent(getActivity(), CreditActivity.class);
            startActivityForResult(intent, 101);
        } else if ("active_page".equals(enter_action)) {
            Intent intent = new Intent(getActivity(), ActivateLineActivity.class);
            startActivityForResult(intent, 102);
        }
    }

    /**
     * 同意瑞卡贷服务协议
     */
    private void signService() {
        AgreementRequest request = new AgreementRequest();
        request.setAgreement_id(serviceId);
        mBaseActivity.httpsPost(getActivity(), request, ReqAction.APPLICATION_AGREEMENT_AGREE, AgreementResponse.class,
                new ICallback<AgreementResponse>() {
                    @Override
                    public void success(AgreementResponse agreementResponse) {
                        getRouteAgain();
                    }

                    @Override
                    public void failture(String tips) {
                        CLogUtil.showToast(getActivity(), tips);
                    }
                });
    }

    /**
     * 同意非正式协议请求
     */
    private void agreement() {
        AgreementRequest request = new AgreementRequest();
        request.setAgreement_id(agreementId);
        mBaseActivity.httpsPost(getActivity(), request, ReqAction.APPLICATION_AGREEMENT_AGREE, AgreementResponse.class,
                new ICallback<AgreementResponse>() {
                    @Override
                    public void success(AgreementResponse agreementResponse) {

                    }

                    @Override
                    public void failture(String tips) {
                        CLogUtil.showToast(getActivity(), tips);
                    }
                });
    }

    /**
     * 显示用户同意协议对话框
     */
    public void showAgreeDialog() {
        Dialog.Builder builder = null;
        builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
            @Override
            protected void onBuildDone(final Dialog dialog) {
                WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics dm = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(dm);
                dialog.setCanceledOnTouchOutside(false);
                WebView webView = (WebView) dialog.findViewById(R.id.c_wv_agree);
                webView.loadUrl(agreement_url);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
                dialog.layoutParams(dm.widthPixels - CDensityUtil.dip2px(getActivity(), 32), ViewGroup.LayoutParams.WRAP_CONTENT);
                CRyxSlideUnlock cRyxSlideUnlock = (CRyxSlideUnlock) dialog.findViewById(R.id.c_slide_unlock);
                cRyxSlideUnlock.setListen(new CRyxSlideUnlock.ISlideToEnd() {
                    @Override
                    public void slideToEnd() {
                        agreement();
                        dialog.dismiss();
                    }
                });
            }
        };
        builder.contentView(R.layout.c_dialog_credit_enter);
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getActivity().getSupportFragmentManager(), null);
    }

}
