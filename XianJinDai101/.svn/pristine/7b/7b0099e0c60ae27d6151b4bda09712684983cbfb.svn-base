package com.ryx.ryxcredit.fragment.baseinfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.rey.material.widget.TextView;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.activity.BaseInfoActivity;
import com.ryx.ryxcredit.beans.bussiness.supplementarymaterials.CSupplementaryMaterialsRequest;
import com.ryx.ryxcredit.beans.pojo.Customer;
import com.ryx.ryxcredit.inter.IBaseInfoFragmentCallback;
import com.ryx.ryxcredit.utils.CCommonDialog;
import com.ryx.ryxcredit.utils.CConstants;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CNummberUtil;
import com.ryx.ryxcredit.utils.CPreferenceUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author：lijing on 16/6/25 09:04
 * Mail：lijing1-jn@ruiyinxin.com
 * Description：工作信息
 */

public class BaseWorkInfoFragment extends Fragment {
    private IBaseInfoFragmentCallback callback;

    //地址详情
    private EditText workAddressDetailEt;
    //公司名称
    private EditText companyNameEt;
    //公司电话
    private EditText companyPhoneEt, company_phone_et1;
    //行业分类
    private TextView professionTv;
    //月收入
    private TextView monthMoneyTv;
    private BaseInfoActivity baseInfoActivity;
    private String profession;
    private String monthIncome;
    private String regEx = "[|｜\r\n]";
    private String company_phone, companyName, workAddressDetail;
    private boolean infoFinished;//信息是否完善过
    private CPreferenceUtil preferenceUtil;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseInfoActivity = (BaseInfoActivity) getActivity();
        callback = (BaseInfoActivity) getActivity();
        infoFinished = getArguments().getBoolean("infoFinished",false);
        preferenceUtil = CPreferenceUtil.getInstance(getActivity().getApplication());
        View rootView = inflater.inflate(R.layout.c_fragment_base_info_2, container, false);
        workAddressDetailEt = (EditText) rootView.findViewById(R.id.c_base_info_work_address_et);
        companyNameEt = (EditText) rootView.findViewById(R.id.c_base_info_work_company_name_et);
        company_phone_et1 = (EditText) rootView.findViewById(R.id.edt_company_phone_et1);
        companyPhoneEt = (EditText) rootView.findViewById(R.id.c_base_info_work_company_phone_et);
        professionTv = (TextView) rootView.findViewById(R.id.c_base_info_work_profession_et);
        monthMoneyTv = (TextView) rootView.findViewById(R.id.c_base_info_wrok_month_level_et);
        initView();
        rootView.findViewById(R.id.c_btn_complete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    Object result = null;
                    CSupplementaryMaterialsRequest cSupplementaryMaterialsRequest = new CSupplementaryMaterialsRequest();
                    company_phone = company_phone_et1.getText().toString().trim() + "-" + companyPhoneEt.getText().toString().trim();
                    cSupplementaryMaterialsRequest.setCompany_phone_num(company_phone);
                    companyName = rexgStr(companyNameEt.getText().toString().trim());
                    cSupplementaryMaterialsRequest.setCompany_name(companyName);
                    workAddressDetail = rexgStr(workAddressDetailEt.getText().toString().trim());
                    cSupplementaryMaterialsRequest.setCompany_address(workAddressDetail);
                    cSupplementaryMaterialsRequest.setIndustry_type(profession);
                    cSupplementaryMaterialsRequest.setMonthly_income(monthIncome);

                    //判断用户如果未完善信息过，则保存信息
                    if (!infoFinished) {
                        saveWorkInfo();
                    }
                    result = cSupplementaryMaterialsRequest;
                    callback.setOtherInfo(result);
                }
            }
        });
        initListen();
        return rootView;
    }
    @Override
    public void onStop() {
        super.onStop();
        if (!infoFinished)
            saveWorkInfo();
    }

    private void initView() {
        Customer c = baseInfoActivity.customer;
        if (c != null&&infoFinished) {
            profession = c.getIndustry_type();
            monthIncome = c.getMonthly_income();
            workAddressDetail = c.getCompany_address();
            companyName = c.getCompany_name();
            company_phone = c.getCompany_phone_num();
        } else {
            profession = preferenceUtil.getString("c_work_profession", "");
            monthIncome = preferenceUtil.getString("c_work_monthIncome", "");
            company_phone = preferenceUtil.getString("c_work_company_phone", "");
            companyName = preferenceUtil.getString("c_work_companyName", "");
            workAddressDetail = preferenceUtil.getString("c_work_workAddressDetail", "");
        }
        workAddressDetailEt.setText(workAddressDetail);
        companyNameEt.setText(companyName);
        if (!TextUtils.isEmpty(company_phone) && company_phone.contains("-")) {
            int index = company_phone.indexOf("-");
            company_phone_et1.setText(company_phone.substring(0, index));
            companyPhoneEt.setText(company_phone.substring(index + 1));
        } else {
            companyPhoneEt.setText(company_phone);
        }
        if (!TextUtils.isEmpty(profession))
            professionTv.setText(CConstants.getIndustryStatus(profession));
        if (!TextUtils.isEmpty(monthIncome))
            monthMoneyTv.setText(getResources().getStringArray(R.array.income_status_array)[CNummberUtil.parseIntRadix(monthIncome, 16, 0) - 1]);
    }

    //将未激活的用户公司信息保存在本地，
    private void saveWorkInfo() {
        company_phone = company_phone_et1.getText().toString().trim() + "-" + companyPhoneEt.getText().toString().trim();
        companyName = rexgStr(companyNameEt.getText().toString().trim());
        workAddressDetail = rexgStr(workAddressDetailEt.getText().toString().trim());
        preferenceUtil.saveString("c_work_company_phone", company_phone);
        preferenceUtil.saveString("c_work_companyName", companyName);
        preferenceUtil.saveString("c_work_workAddressDetail", workAddressDetail);
        preferenceUtil.saveString("c_work_profession", profession);
        preferenceUtil.saveString("c_work_monthIncome", monthIncome);
    }

    private String rexgStr(String str) {
        Pattern pattern = Pattern.compile(regEx);
        Matcher m = pattern.matcher(str);
        return m.replaceAll("").trim();
    }

    public boolean checkInput() {
        String companyName = companyNameEt.getText().toString().trim();
        if (TextUtils.isEmpty(companyName)) {
            CLogUtil.showToast(getActivity(), "公司名称不能为空！");
            return false;
        }
        String companyPhone1 = company_phone_et1.getText().toString().trim();
        if (TextUtils.isEmpty(companyPhone1)) {
            CLogUtil.showToast(getActivity(), "公司电话区号不能为空！");
            return false;
        }
        String companyPhone = companyPhoneEt.getText().toString().trim();
        if (TextUtils.isEmpty(companyPhone)) {
            CLogUtil.showToast(getActivity(), "公司电话不能为空！");
            return false;
        }
        String workAddress = workAddressDetailEt.getText().toString().trim();
        if (TextUtils.isEmpty(workAddress)) {
            CLogUtil.showToast(getActivity(), "公司地址不能为空！");
            return false;
        }
        String profession = professionTv.getText().toString().trim();
        if (TextUtils.isEmpty(profession)) {
            CLogUtil.showToast(getActivity(), "请选择行业分类！");
            return false;
        }
        String monthMoney = monthMoneyTv.getText().toString().trim();
        if (TextUtils.isEmpty(monthMoney)) {
            CLogUtil.showToast(getActivity(), "请选择月收入范围！");
            return false;
        }
        return true;
    }

    private void initListen() {
        workAddressDetailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                baseInfoActivity.customer.setCompany_address(workAddressDetailEt.getText().toString());
            }
        });
        companyNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                baseInfoActivity.customer.setCompany_name(companyNameEt.getText().toString());
            }
        });
        companyPhoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                baseInfoActivity.customer.setCompany_name(companyPhoneEt.getText().toString());
            }
        });
        professionTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CCommonDialog.showCommonSelected(getActivity(), "请选择行业状况", getActivity().getResources().getStringArray(R.array.profession_status_array), new CCommonDialog.ICommonSelectedListener() {

                    @Override
                    public void selectedKey(int key, String value) {
                        profession = CConstants.getIndustryKey(value);
                        professionTv.setText(value);
                        baseInfoActivity.customer.setIndustry_type(profession);

                    }
                });
            }
        });

        monthMoneyTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CCommonDialog.showCommonSelected(getActivity(), "请选择月收入状况", getActivity().getResources().getStringArray(R.array.income_status_array), new CCommonDialog.ICommonSelectedListener() {

                    @Override
                    public void selectedKey(int key, String value) {
                        monthIncome = CConstants.getSelectedKey(key);
                        monthMoneyTv.setText(value);
                        baseInfoActivity.customer.setMonthly_income(monthIncome);
                    }
                });
            }
        });

    }


}
