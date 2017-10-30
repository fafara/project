package com.ryx.ryxcredit.newfragment.baseinfo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.utils.CPreferenceUtil;
import com.ryx.ryxcredit.xjd.BaseInfoSuccesActivity;
import com.zhy.autolayout.AutoRelativeLayout;

import static com.ryx.ryxcredit.R.id.xjd_ll_quota_activation_credit;

/**
 * A simple {@link Fragment} subclass.
 */
public class SixChooseOneFragment extends Fragment implements View.OnClickListener {
    private String OCTOPUS_URL_STR = "http://taskid";
    String url = "https://open.shujumohe.com/box/gjj?box_token=14F98932A59D4802AA7A17B7920B44CF&cb=" + OCTOPUS_URL_STR;
    private BaseInfoSuccesActivity baseInfoActivity;
    private BaseInfoSuccesActivity callback;
    private CPreferenceUtil preferenceUtil;
    private View rootView;
    private AutoRelativeLayout ll_quota_activation_credit;//人行征信查询
    private AutoRelativeLayout ll_quota_fund_fnformation_certification;//公积金信息认证
    private AutoRelativeLayout ll_social_security_information_certification;//社保信息认证
//    private AutoRelativeLayout ll_credit_card_certification;//信用卡账单认证
    private AutoRelativeLayout ll_taobao_certification;//淘宝认证
    private AutoRelativeLayout ll_jingdong_certification;//京东认证
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseInfoActivity = (BaseInfoSuccesActivity) getActivity();
        callback = (BaseInfoSuccesActivity) getActivity();
        preferenceUtil = CPreferenceUtil.getInstance(getActivity().getApplication());
        rootView = inflater.inflate(R.layout.fragment_six_choose_one, container, false);
        initView();
        return rootView;
    }
/*
* 初始化控件
*
* */
    private void initView() {
        ll_quota_activation_credit= (AutoRelativeLayout) rootView.findViewById(xjd_ll_quota_activation_credit);
        ll_quota_activation_credit.setOnClickListener(this);
        ll_quota_fund_fnformation_certification = (AutoRelativeLayout) rootView.findViewById(R.id.xjd_ll_quota_fund_fnformation_certification);
        ll_quota_fund_fnformation_certification.setOnClickListener(this);
        ll_social_security_information_certification= (AutoRelativeLayout) rootView.findViewById(R.id.xjd_ll_social_security_information_certification);
        ll_social_security_information_certification.setOnClickListener(this);
 /*       ll_credit_card_certification= (AutoRelativeLayout) rootView.findViewById(R.id.xjd_ll_credit_card_certification);
        ll_credit_card_certification.setOnClickListener(this);*/
        ll_taobao_certification= (AutoRelativeLayout) rootView.findViewById(R.id.xjd_ll_taobao_certification);
        ll_taobao_certification.setOnClickListener(this);
        ll_jingdong_certification= (AutoRelativeLayout) rootView.findViewById(R.id.xjd_ll_jingdong_certification);
        ll_jingdong_certification.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.xjd_ll_quota_activation_credit) {
            //人行征信查询
            callback.setSixChooseOne();
        }else if(id == R.id.xjd_ll_quota_fund_fnformation_certification){
            //公积金信息认证
           /* Intent intent = new Intent(getActivity(), SixChooseOneActivity.class);
            intent.putExtra("url_address",url);
            intent.putExtra("title","公积金信息认证");
            startActivity(intent);*/
            callback.setSixChooseOneWebview();
        }
        else if(id == R.id.xjd_ll_social_security_information_certification){
            //社保信息认证
            callback.setFundSixChooseOneWebview();
        }
/*        else if(id == R.id.xjd_ll_credit_card_certification){
            //信用卡账单认证
            callback.setSixChooseOneCredit();
        }*/
        else if(id == R.id.xjd_ll_taobao_certification){
            //淘宝认证
            callback.setSixChooseOneTaoBao();
        }
        else if(id == R.id.xjd_ll_jingdong_certification){
            //京东认证
            callback.setSixChooseOneJingDong();
        }
    }
}
