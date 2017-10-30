package com.ryx.payment.ruishua.recharge;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.rey.material.app.ThemeManager;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.CityInfo;
import com.ryx.payment.ruishua.bean.Order;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.ProvinceInfo;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.bindcard.adapter.BanksAdapter;
import com.ryx.payment.ruishua.bindcard.adapter.BranchAdapter;
import com.ryx.payment.ruishua.bindcard.adapter.CityAdapter;
import com.ryx.payment.ruishua.bindcard.adapter.ProvinceAdapter;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

@EActivity(R.layout.activity_bind_debit_card_info_add)
public class TransferCardInfoAddActivity extends BaseActivity {

    @ViewById(R.id.edt_bankname)
    EditText edt_bankname;
    @ViewById(R.id.edt_bankprovince)
    EditText edt_bankprovince;
    @ViewById(R.id.edt_bankcity)
    EditText edt_bankcity;
    @ViewById(R.id.edt_bank_branch)
    EditText edt_bank_branch;
    @ViewById(R.id.edt_condition)
    EditText edt_condition;
    @ViewById
    Button btn_next;


    private Param qtpayBankVersion;
    private BankCardInfo bankCardInfo;
    private String usertype;
    private int pPosition;
    private boolean isBankselected = false,
            isProvinceSelected = false,
            isCitySelected = false, isBranchSelected = false,
            isok = false;

    private ArrayList<ProvinceInfo> allcitys = new ArrayList<ProvinceInfo>();
    private ArrayList<BankCardInfo> bankList = new ArrayList<BankCardInfo>();
    private BankCardInfo cardinfo;
    private LinkedList<BankCardInfo> bankCardInfoList = new LinkedList<BankCardInfo>();

    private String bankId, bankName,branchId;
    final int SHOW_PROVINCE = 1;
    final int SHOW_CITY = 2;
    private String bankProvinceId = "";
    private String bankCityId = "";
    private String condition = "";
    private int offset = 0;
    private com.rey.material.app.Dialog branchDialog;
    private boolean isBranchDialogshow = false;
    private MaterialRefreshLayout materialRefreshLayout;

    @Bean
    BanksAdapter banksAdapter;
    @Bean
    ProvinceAdapter provinceAdapter;
    @Bean
    CityAdapter cityAdapter;
    @Bean
    BranchAdapter branchAdapter;
    Param qtpayBankId;
    Param qtpayBankCityId;
    Param qtpayBankProvinceId;
    Param qtpayCondition;
    Param qtpayOffset;
    private String islast = "0";
    private boolean isRefresh = false;

    @AfterViews
    public void initViews() {
        setTitleLayout("添加提款账户", true, false);
        bankCardInfo = (BankCardInfo) getIntent().getExtras().get("bankCardInfo");
        usertype = getIntent().getExtras().getString("usertype");
        initQtPatParams();
        pPosition = 0;
        initListData();
    }

    public void initListData() {
        // 获取中国省市区信息
        String jsonString = getRawCitys().toString();
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            // 获得一个省的数组
            JSONArray citysArray = jsonObj.getJSONArray("resultBean");
            for (int i = 0; i < citysArray.length(); i++) {
                // 创建一个新的省份
                ProvinceInfo provinceinfo = new ProvinceInfo();
                JSONObject province = citysArray.getJSONObject(i);

                JSONArray citys = province.getJSONArray("citys");
                // 创建该省的城市列表list
                ArrayList<CityInfo> cityslist = new ArrayList<CityInfo>();
                for (int j = 0; j < citys.length(); j++) {
                    JSONObject cityjson = citys.getJSONObject(j);
                    CityInfo city = new CityInfo(
                            cityjson.getString("cityCode"),
                            cityjson.getString("cityName"));
                    cityslist.add(city);
                }
                provinceinfo
                        .setProvinceCode(province.getString("provinceCode"));
                provinceinfo
                        .setProvinceName(province.getString("provinceName"));
                provinceinfo.setCityslist(cityslist);
                allcitys.add(provinceinfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Click({R.id.edt_bankprovince, R.id.iv_provinceright})
    public void searchBankProvince() {
        creatDialog(SHOW_PROVINCE);
    }

    @Click({R.id.edt_bankcity, R.id.iv_cityright})
    public void searchBankCity() {
        if (isProvinceSelected == true) {
            creatDialog(SHOW_CITY);
        } else {
            LogUtil.showToast(TransferCardInfoAddActivity.this, getResources().getString(R.string.please_select_province_bank));
        }
    }

    @Click({R.id.edt_bank_branch, R.id.iv_bank_branchright})
    public void searchBankbranch() {
        islast = "0";
        condition = "";
        offset = 0;
        isRefresh = false;
        isBranchDialogshow = false;
        bankCardInfoList.clear();
        if (isParamsOK()) {
            initBranchList();
        }

    }

    private void initBranchList() {
        if (!"0".equals(islast)) {
            LogUtil.showToast(TransferCardInfoAddActivity.this, "无记录！");
            if (isRefresh) {
                materialRefreshLayout.finishRefreshLoadMore();
            }
            return;
        }
        initQtPatParams();
        if (isRefresh) {
            isNeedThread = false;
        }
        qtpayApplication = new Param("application", "GetBankBranch.Req");
        qtpayBankId = new Param("bankId");
        qtpayBankCityId = new Param("bankCityId");
        qtpayBankProvinceId = new Param("bankProvinceId");
        qtpayCondition = new Param("condition");
        qtpayOffset = new Param("offset");
        qtpayCondition.setValue(condition);
        qtpayBankId.setValue(bankId);
        qtpayBankCityId.setValue(bankCityId);
        qtpayBankProvinceId.setValue(bankProvinceId);
        qtpayOffset.setValue(offset + "");
        offset = offset + 20;
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayBankId);
        qtpayParameterList.add(qtpayBankCityId);
        qtpayParameterList.add(qtpayBankProvinceId);
        qtpayParameterList.add(qtpayCondition);
        qtpayParameterList.add(qtpayOffset);
        httpsPost("GetBankBranch", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                if (isRefresh) {
                    materialRefreshLayout.finishRefreshLoadMore();
                    isRefresh = false;
                }
                if (payResult.getData() != null) {
                    LinkedList<BankCardInfo> list = getSubBranchList(payResult.getData());
                    if (list != null && list.size() > 0) {
                        bankCardInfoList.addAll(list);
                        list = null;
                        branchAdapter.notifyDataSetChanged();
                        if (!isBranchDialogshow) {
                            showBranchDialog();
                        }
                    }

                }
            }

            @Override
            public void onOtherState() {
                super.onOtherState();
                if (isRefresh) {
                    materialRefreshLayout.finishRefreshLoadMore();
                    isRefresh = false;
                }
            }

            @Override
            public void onTradeFailed() {
                super.onTradeFailed();
                if (isRefresh) {
                    materialRefreshLayout.finishRefreshLoadMore();
                    isRefresh = false;
                }
            }
        });
    }

    public LinkedList<BankCardInfo> getSubBranchList(String jsonstring) {
        LinkedList<BankCardInfo> list = null;
        String toastmsg = "";
        try {
            if (!islast.equals("1")) {    // 如果不是最后一页，才可以进一步加载更多
                JSONObject jsonObj = new JSONObject(jsonstring);
                islast = (String) jsonObj.getJSONObject("summary").getString("isLast");

                toastmsg = (String) jsonObj.getJSONObject("result").getString("message");
                if (RyxAppconfig.QTNET_SUCCESS.equals(jsonObj.getJSONObject("result").getString("resultCode"))) {
                    // 解析银行信息
                    JSONArray banks = jsonObj.getJSONArray("resultBean");
                    list = new LinkedList<BankCardInfo>();
                    for (int i = 0; i < banks.length(); i++) {
                        cardinfo = new BankCardInfo();
                        cardinfo.setBranchBankName(banks.getJSONObject(i).getString("bankName"));
                        cardinfo.setBranchBankId(banks.getJSONObject(i).getString("bankId"));
                        list.add(cardinfo);
                        cardinfo = null;
                    }
                    return list;
                } else {
                    LogUtil.showToast(TransferCardInfoAddActivity.this, toastmsg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 检查参数
     */
    public boolean isParamsOK() {

        if (!isBankselected || bankId.length() == 0) {
            LogUtil.showToast(TransferCardInfoAddActivity.this, getResources().getString(R.string.please_select_head_bank));
            return false;
        }
        if (!isProvinceSelected && bankProvinceId.length() == 0) {
            LogUtil.showToast(TransferCardInfoAddActivity.this, getResources().getString(R.string.please_select_province_bank));
            return false;
        }
        if (!isCitySelected && bankCityId.length() == 0) {
            LogUtil.showToast(TransferCardInfoAddActivity.this, getResources().getString(R.string.please_select_bank_city));
            return false;
        }
        return true;
    }

    //选择银行
    @Click({R.id.edt_bankname, R.id.iv_nameright})
    public void searchBankName() {
        initQtPatParams();
        qtpayApplication = new Param("application", "GetBankHeadQuarter.Req");
        qtpayBankVersion = new Param("bankVersion", "");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayBankVersion);
        httpsPost("GetBankHeadQuarter", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                initListData(payResult.getData());
            }
        });
    }

    @Click(R.id.btn_next)
    public void bindCard(){
        if (isParamsOK()) {
            if(isBranchSelected){
                Intent intent = new Intent(TransferCardInfoAddActivity.this,RepaymentActivity_.class);
                intent.putExtra("bankCardInfo", bankCardInfo);
                OrderInfo orderinfo = Order.KUAI_SU_ZHUAN_ZHANG;
                intent.putExtra("orderinfo", orderinfo);
                startActivityForResult(intent,RyxAppconfig.WILL_BE_CLOSED);
            }else{
                LogUtil.showToast(TransferCardInfoAddActivity.this, getResources().getString(R.string.select_the_account_where_the_branch));
            }
        }
    }
    @Click(R.id.tileleftImg)
    public void closeWindow(){
        finish();
    }

    private void Bindcard(){
        initQtPatParams();
        qtpayApplication= new Param("application", "BankCardBind.Req");
        Param   qtpayBindType = new Param("bindType","01");
        Param  qtpayAccountNo = new Param("accountNo");
        Param  qtpayUsertype = new Param("userType",usertype);
        qtpayBankId = new Param("bankId");
        qtpayBankId.setValue(branchId);
        qtpayAccountNo.setValue(bankCardInfo.getAccountNo().trim().replace(" ", ""));

        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayBindType);
        qtpayParameterList.add(qtpayBankId);
        qtpayParameterList.add(qtpayAccountNo);
        qtpayParameterList.add(qtpayUsertype);
        LogUtil.showLog("usertype",usertype+"---"+branchId+"---"+bankCardInfo.getAccountNo());
        httpsPost("BankCardBind", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                setResult(RESULT_OK);
                finish();
            }
        });



    }
    /**
     * 初始化银行列表
     */
    public void initListData(String data) {
        bankList.clear();
        String toastmsg = "";
        try {
            JSONObject jsonObj = new JSONObject(data);
            toastmsg = jsonObj.getJSONObject("result").getString("message");
            if ("0000".equals(jsonObj.getJSONObject("result").getString("resultCode"))) {
                //	解析银行信息
                JSONArray banks = jsonObj.getJSONArray("resultBean");
                for (int i = 0; i < banks.length(); i++) {
                    String bankname = banks.getJSONObject(i).getString("bankName");
                    String bankid = banks.getJSONObject(i).getString("bankId");
                    cardinfo = new BankCardInfo();
                    cardinfo.setBankName(bankname);
                    cardinfo.setBankId(bankid);
                    bankList.add(cardinfo);
                    cardinfo = null;
                }
            } else {
                LogUtil.showToast(TransferCardInfoAddActivity.this, toastmsg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showDialog();
    }

    private void showDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        AutoLinearLayout layout = (AutoLinearLayout) inflater.inflate(R.layout.bind_card_bank_list, null);
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
        com.rey.material.app.Dialog.Builder builder =
                new com.rey.material.app.Dialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog);
        final com.rey.material.app.Dialog dialog = builder.build(TransferCardInfoAddActivity.this);
        ListView lv_bank = (ListView) layout.findViewById(R.id.lv_bank);
        ImageView imgview_close = (ImageView) layout.findViewById(R.id.imgview_close);
        imgview_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        banksAdapter.setList(bankList);
        lv_bank.setAdapter(banksAdapter);
        lv_bank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                bankId = bankList.get(position).getBankId();
                bankName = bankList.get(position).getBankName();
                edt_bankname.setText(bankName);
                isBankselected = true;
                isProvinceSelected = false;
                edt_bankprovince.setText("");
                isCitySelected = false;
                edt_bankcity.setText("");
                isBranchSelected = false;
                edt_bank_branch.setText("");
                dialog.dismiss();
            }
        });
        dialog.setContentView(layout);
        dialog.show();
    }

    //显示分行
    private void showBranchDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        AutoLinearLayout layout = (AutoLinearLayout) inflater.inflate(R.layout.bind_card_branch_bank_list, null);
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
        com.rey.material.app.Dialog.Builder builder =
                new com.rey.material.app.Dialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog);
        branchDialog = builder.build(TransferCardInfoAddActivity.this);
        materialRefreshLayout = (MaterialRefreshLayout) layout.findViewById(R.id.materialRefreshLayout);
        final EditText edt_condition = (EditText) layout.findViewById(R.id.edt_condition);
        ;
        ImageView btn_search = (ImageView) layout.findViewById(R.id.btn_search);
        ;
        ListView lv_bank = (ListView) layout.findViewById(R.id.lv_bank);
        ImageView imgview_close = (ImageView) layout.findViewById(R.id.imgview_close);
        imgview_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                branchDialog.dismiss();
            }
        });
        branchAdapter.setList(bankCardInfoList);
        lv_bank.setAdapter(branchAdapter);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bankCardInfoList != null) {
                    bankCardInfoList.clear();
                    branchAdapter.notifyDataSetChanged();
                    offset = 0;
                    condition = edt_condition.getText().toString();
                    initBranchList();
                }
            }
        });
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.finishRefresh();
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                initBranchList();
                isRefresh = true;
            }

        });
        lv_bank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                branchDialog.dismiss();
                isBranchDialogshow = false;
                isBranchSelected=true;
                branchId= bankCardInfoList.get(position).getBranchBankId();
                edt_bank_branch.setText(bankCardInfoList.get(position).getBranchBankName());
            }
        });
        branchDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isBranchDialogshow = false;
            }
        });
        branchDialog.setContentView(layout);
        branchDialog.show();
        isBranchDialogshow = true;
    }
//    /**
//     * 根据输入判断按钮的显示颜色和提示文字的显示
//     */
//    public void changgeState() {
//        isok = (isBankselected && isProvinceSelected && isCitySelected && isBranchSelected);
//        if(!isBankselected){
//            tv_bank.setText(getResources().getString(R.string.select_bank));
//        }
//        if(!isProvinceSelected){
//            tv_province.setText(getResources().getString(R.string.select_province_bank));
//        }
//        if(!isCitySelected){
//            tv_city.setText(getResources().getString(R.string.select_bank_city));
//        }
//        if(!isBranchSelected){
//            tv_branch.setText(getResources().getString(R.string.select_the_account_where_the_branch));
//        }
//        if (isok) {
//            bt_next.setBackgroundResource(R.drawable.bg_bt_blue);
//        } else {
//            bt_next.setBackgroundResource(R.drawable.bg_bt_gray);
//        }
//    }

    /**
     * 获取城市列表数据
     */
    public String getRawCitys() {
        InputStream in = getResources().openRawResource(R.raw.cities);
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            br.close();
            isr.close();
            in.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return sb.toString();
    }


    private void creatDialog(final int dialogType) {
        LayoutInflater inflater = LayoutInflater.from(this);
        AutoLinearLayout layout = (AutoLinearLayout) inflater.inflate(R.layout.bind_debit_card_select_povince, null);
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
        com.rey.material.app.Dialog.Builder builder =
                new com.rey.material.app.Dialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog);
        final com.rey.material.app.Dialog dialog = builder.build(TransferCardInfoAddActivity.this);
        ImageView imgview_close = (ImageView) layout.findViewById(R.id.imgview_close);
        imgview_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ListView lv_bank = (ListView) layout.findViewById(R.id.lv_location);
        TextView tv_title = (TextView) layout.findViewById(R.id.tv_title);
        if (dialogType == SHOW_PROVINCE) {
            tv_title.setText(getResources().getString(R.string.please_select_a_provinces));
            provinceAdapter.setList(allcitys);
            lv_bank.setAdapter(provinceAdapter);
        } else {
            tv_title.setText(getResources().getString(R.string.please_select_a_city));
            cityAdapter.setList(allcitys.get(pPosition).getCityslist());
            lv_bank.setAdapter(cityAdapter);
        }

        lv_bank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (dialogType == SHOW_PROVINCE) {
                    pPosition = position;
                    bankProvinceId = allcitys.get(pPosition).getProvinceCode();
                    edt_bankprovince.setText(allcitys.get(pPosition).getProvinceName());
                    // 判断该省下是否有市级
                    if (allcitys.get(pPosition).getCityslist().size() < 1) {
                        edt_bankcity.setText("");

                    } else {
                        edt_bankcity.setText(allcitys.get(pPosition).getCityslist()
                                .get(0).getCityName());
                        bankCityId = allcitys.get(pPosition).getCityslist()
                                .get(0).getCityCode();
                    }
                    isProvinceSelected = true;
                    isCitySelected = false;
                    isBranchSelected = false;
                    bankCityId ="";
                    edt_bank_branch.setText("");
                    creatDialog(SHOW_CITY);
                } else {
                    bankCityId = allcitys.get(pPosition).getCityslist()
                            .get(position).getCityCode();
                    edt_bankcity.setText(allcitys.get(pPosition).getCityslist()
                            .get(position).getCityName());
                    isCitySelected = true;
                    isBranchSelected = false;
                    edt_bank_branch.setText("");
                }
                dialog.dismiss();
            }
        });
        dialog.setContentView(layout);
        dialog.show();
    }


}
