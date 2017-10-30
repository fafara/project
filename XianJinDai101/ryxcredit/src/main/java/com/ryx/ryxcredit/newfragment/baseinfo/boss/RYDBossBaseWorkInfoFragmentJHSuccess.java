package com.ryx.ryxcredit.newfragment.baseinfo.boss;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.lljjcoder.citypickerview.widget.CityPicker;
import com.rey.material.widget.TextView;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.beans.bussiness.supplementarymaterials.CSupplementaryMaterialsRequest;
import com.ryx.ryxcredit.beans.pojo.Customer;
import com.ryx.ryxcredit.utils.CCommonDialog;
import com.ryx.ryxcredit.utils.CConstants;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CPreferenceUtil;
import com.ryx.ryxcredit.xjd.RKDBossBaseInfoSuccessActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ryx.ryxcredit.R.id.c_base_info_work_company_name_et;
import static com.ryx.ryxcredit.R.id.c_baseinfo_work_detailaddress_et;

/**
 * A simple {@link Fragment} subclass.
 */
public class RYDBossBaseWorkInfoFragmentJHSuccess  extends Fragment {
    private RKDBossBaseInfoSuccessActivity callback;

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
    //统一社会信用代码
    private EditText c_unified_social_credit_code_et;
    //月营业额
    private EditText c_monthly_turnover_et;

    private EditText c_base_info_work_detailaddress;
    //协议
    //  private TextView tv_base_info_Agreement;
    private RKDBossBaseInfoSuccessActivity baseInfoActivity;

    private String profession;
    private String monthIncome;
    private String regEx = "[|｜\r\n]";
    private String company_phone, companyName, workAddressDetail,c_unified_social_credit_code;
    private Integer c_monthly_turnover;
    private boolean infoFinished;//信息是否完善过
    private CPreferenceUtil preferenceUtil;
    private String companyAddressDetail;
    private String credit_code;
    private String monthly_turnover;
    private String companyAddressDetailRequest;
    private String province;
    private String city;
    private String region;
    private String bossCompanyAddress;
    private String monthlyTurnover;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseInfoActivity = (RKDBossBaseInfoSuccessActivity) getActivity();
        callback = (RKDBossBaseInfoSuccessActivity) getActivity();
        infoFinished = getArguments().getBoolean("infoFinished",false);
        preferenceUtil = CPreferenceUtil.getInstance(getActivity().getApplication());
        View rootView = inflater.inflate(R.layout.fragment_rydboss_base_work_info_fragment_jhsuccess, container, false);
        province = ((RKDBossBaseInfoSuccessActivity) getActivity()).getProvince();
        city = ((RKDBossBaseInfoSuccessActivity) getActivity()).getCity();
        region = ((RKDBossBaseInfoSuccessActivity) getActivity()).getRegion();
        workAddressDetailEt = (EditText) rootView.findViewById(R.id.c_base_info_work_address_et);
        companyNameEt = (EditText) rootView.findViewById(c_base_info_work_company_name_et);
        company_phone_et1 = (EditText) rootView.findViewById(R.id.edt_company_phone_et1);
        companyPhoneEt = (EditText) rootView.findViewById(R.id.c_base_info_work_company_phone_et);
        professionTv = (TextView) rootView.findViewById(R.id.c_base_info_work_profession_et);
        monthMoneyTv = (TextView) rootView.findViewById(R.id.c_base_info_wrok_month_level_et);
        c_base_info_work_detailaddress = (EditText) rootView.findViewById(c_baseinfo_work_detailaddress_et);
        c_base_info_work_detailaddress.setFocusable(false);
        c_unified_social_credit_code_et = (EditText) rootView.findViewById(R.id.c_unified_social_credit_code_et);
        c_monthly_turnover_et = (EditText) rootView.findViewById(R.id.c_monthly_turnover_et);
        c_monthly_turnover_et.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        //  tv_base_info_Agreement = (TextView) rootView.findViewById(R.id.tv_base_info_Agreement);
      /*  tv_base_info_Agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),InstructionActivity.class);
                startActivity(intent);
            }
        });*/
        initView();
        rootView.findViewById(c_baseinfo_work_detailaddress_et).setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                show();
            }
        });
        rootView.findViewById(R.id.c_btn_workinfo_complete).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (checkInput()) {
                    Object result = null;
                    CSupplementaryMaterialsRequest cSupplementaryMaterialsRequest = new CSupplementaryMaterialsRequest();
                    company_phone = company_phone_et1.getText().toString().trim() + "-" + companyPhoneEt.getText().toString().trim();
                    cSupplementaryMaterialsRequest.setCompany_phone_num(company_phone);
                    companyName = rexgStr(companyNameEt.getText().toString().trim());
                    cSupplementaryMaterialsRequest.setCompany_name(companyName);
                    workAddressDetail = rexgStr(workAddressDetailEt.getText().toString().trim());
                    companyAddressDetailRequest= preferenceUtil.getString(RyxcreditConfig.getPhoneNo()+"c_baseinfo_work_detailaddress", "");
                    cSupplementaryMaterialsRequest.setCompany_address(companyAddressDetailRequest+workAddressDetail);
                    cSupplementaryMaterialsRequest.setIndustry_type(profession);
                    cSupplementaryMaterialsRequest.setCredit_code(c_unified_social_credit_code);
                    cSupplementaryMaterialsRequest.setMonthly_turnover(monthly_turnover);

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
        Customer c = callback.customer;
        if (c != null) {
            companyName = c.getCompany_name();
            company_phone = c.getCompany_phone_num();
            workAddressDetail = c.getCompany_address();
            profession = c.getIndustry_type();
            credit_code = c.getCredit_code();
            monthlyTurnover = c.getMonthly_turnover();
            if (province==null&&city==null
                    &&region==null){
                bossCompanyAddress ="";
            }else {
                bossCompanyAddress = province+city+region;
            }
        } else {
            profession = preferenceUtil.getString("c_boss_work_profession", "");
            company_phone = preferenceUtil.getString("c_boss_work_company_phone", "");
            companyName = preferenceUtil.getString("c_boss_work_companyName", "");
            workAddressDetail = preferenceUtil.getString("c_boss_work_workAddressDetail", "");
            //公司地址
            companyAddressDetail = preferenceUtil.getString(RyxcreditConfig.getPhoneNo()+"c_baseinfo_work_detailaddress", "");
            //社会统一信用编码
            credit_code = preferenceUtil.getString("c_boss_work_unified_social_credit_code", "");
            //月营业额
            monthlyTurnover = preferenceUtil.getString("c_boss_work_monthly_turnover","");
        }
        workAddressDetailEt.setText(workAddressDetail);
        if (!TextUtils.isEmpty(companyName)) {
            companyNameEt.setText(companyName);
        }
        c_base_info_work_detailaddress.setText(province+city+region);

        if (!TextUtils.isEmpty(company_phone) && company_phone.contains("-")) {
            int index = company_phone.indexOf("-");
            company_phone_et1.setText(company_phone.substring(0, index));
            companyPhoneEt.setText(company_phone.substring(index + 1));
        } else {
            companyPhoneEt.setText(company_phone);
        }
        if (!TextUtils.isEmpty(profession))
            professionTv.setText(CConstants.getIndustryStatus(profession));
        if (!TextUtils.isEmpty(credit_code))
            c_unified_social_credit_code_et.setText(credit_code);

        c_monthly_turnover_et.setText(monthlyTurnover);
    }

    //将未激活的用户公司信息保存在本地，
    private void saveWorkInfo() {
        company_phone = company_phone_et1.getText().toString().trim() + "-" + companyPhoneEt.getText().toString().trim();
        companyName = rexgStr(companyNameEt.getText().toString().trim());
        workAddressDetail = workAddressDetailEt.getText().toString().trim();
        companyAddressDetail = rexgStr(c_base_info_work_detailaddress.getText().toString().trim());
        credit_code = c_unified_social_credit_code_et.getText().toString().trim();
        monthlyTurnover = c_monthly_turnover_et.getText().toString().trim();
        preferenceUtil.saveString("c_boss_work_company_phone", company_phone);
        preferenceUtil.saveString("c_boss_work_companyName", companyName);
        preferenceUtil.saveString("c_boss_work_workAddressDetail", workAddressDetail);
        preferenceUtil.saveString(RyxcreditConfig.getPhoneNo()+"c_boss_work_workAddressDetail", companyAddressDetail);
        preferenceUtil.saveString("c_boss_work_profession", profession);
        preferenceUtil.saveString("c_boss_work_unified_social_credit_code", credit_code);
        preferenceUtil.saveString("c_boss_work_monthly_turnover",monthlyTurnover);
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
        Log.i("companyPhone","companyPhone"+companyPhone);
        if (TextUtils.isEmpty(companyPhone)) {
            CLogUtil.showToast(getActivity(), "公司电话不能为空！");
            return false;
        }else if (companyPhone.length()<7||companyPhone.length()>8){
            CLogUtil.showToast(getActivity(), "请填写正确的公司电话！");
            return false;
        }
          String companyAddressDetail = c_base_info_work_detailaddress.getText().toString().trim();
         if (TextUtils.isEmpty(companyAddressDetail)) {
            CLogUtil.showToast(getActivity(), "经营地址不能为空！");
            return false;
        }
        String workAddress = workAddressDetailEt.getText().toString().trim();
        if (TextUtils.isEmpty(workAddress)) {
            CLogUtil.showToast(getActivity(), "公司详细地址不能为空！");
            return false;
        }
        String profession = professionTv.getText().toString().trim();
        if (TextUtils.isEmpty(profession)) {
            CLogUtil.showToast(getActivity(), "请选择行业分类！");
            return false;
        }
        c_unified_social_credit_code = c_unified_social_credit_code_et.getText().toString().trim();
        if (TextUtils.isEmpty(c_unified_social_credit_code)) {
            CLogUtil.showToast(getActivity(), "请输入统一社会信用代码！");
            return false;
        }
        monthly_turnover = c_monthly_turnover_et.getText().toString().trim();
        if (TextUtils.isEmpty(c_monthly_turnover_et.getText())) {
            CLogUtil.showToast(getActivity(), "请输入月营业额！");
            return false;
        }
/*        String monthMoney = monthMoneyTv.getText().toString().trim();
        if (TextUtils.isEmpty(monthMoney)) {
            CLogUtil.showToast(getActivity(), "请选择月收入范围！");
            return false;
        }*/
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
    public void show() {
        CityPicker cityPicker = new CityPicker.Builder(getActivity())
                .textSize(20)
                .title("地址选择")
                .backgroundPop(0xa0000000)
                .titleBackgroundColor("#ffffff")
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province("江苏省")
                .city("常州市")
                .district("天宁区")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();

        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];
                companyAddressDetailRequest=province+"|"+"-"+"|"+city+"|"+"-"+"|"+district+"|"+"-"+"|";
                c_base_info_work_detailaddress.setText(province+city+district);
                preferenceUtil.saveString(RyxcreditConfig.getPhoneNo()+"c_baseinfo_work_detailaddress", companyAddressDetailRequest);
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "已取消", Toast.LENGTH_LONG).show();
            }
        });
    }

}

