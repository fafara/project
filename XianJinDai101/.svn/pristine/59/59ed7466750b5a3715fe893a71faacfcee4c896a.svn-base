package com.ryx.ryxcredit.ryd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.widget.Spinner;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CPreferenceUtil;
import com.ryx.ryxcredit.widget.RyxCreditLoadDialog;
import com.ryx.ryxcredit.xjd.MultiIFragmentListener;
import com.ryx.ryxcredit.xjd.bean.borrowrecord.MultiBorrowRecordRequest;
import com.ryx.ryxcredit.xjd.bean.borrowrecord.MultiBorrowRecordsResponse;
import com.ryx.ryxcredit.xjd.fragment.PayedFragment;
import com.ryx.ryxcredit.xjd.fragment.UnpayedFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RYDMultiBorrowRecordsActivity extends BaseActivity implements MultiIFragmentListener {

    private String tabFlag = "";
    private UnpayedFragment unPaymentFragment;
    private PayedFragment payedFragment;
    private boolean is_opened;//是否在业务时间内
    private String unborrowTime;
    private TabLayout tabLayout;
    private TabLayout.Tab tabA, tabB;
    private TextView tv1, tv2;
    private ImageView iv1, iv2 ;
    private Spinner sp_product;//产品列表
    private boolean isFirstRequest = true;
    private List<MultiBorrowRecordsResponse.ResultBean> unPayList;
    private List<MultiBorrowRecordsResponse.ResultBean> payedList;
    //产品列表，全部，瑞卡贷，现金贷
    private String[] productStrs = {"", RyxcreditConfig.rkd_procudtid,RyxcreditConfig.xjd_procudtId};
    private int pos = 0;//选中的产品
    private Spinner spn_label;
    private String mstatus;
    private String mflag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_borrow_records_new);
        setTitleLayout(getResources().getString(R.string.c_borrowing_records), true, false);
        unPayList = new ArrayList<MultiBorrowRecordsResponse.ResultBean>();
        payedList = new ArrayList<MultiBorrowRecordsResponse.ResultBean>();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("is_opened"))
                is_opened = intent.getBooleanExtra("is_opened", false);
            if (intent.hasExtra("unborrowTime"))
                unborrowTime = intent.getStringExtra("unborrowTime");
        }
        tabLayout = (TabLayout) findViewById(R.id.c_tl_borrow_records);
        initTab();
        sp_product = (Spinner)findViewById(R.id.sp_product);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tv1.setTextColor(getResources().getColor(R.color.blue));
                    tv2.setTextColor(getResources().getColor(R.color.threeblack));
                    //再次进入借还记录页面，不再重复请求，申请中记录
                    unPayAction();
                } else if (tab.getPosition() == 1) {
                    tv1.setTextColor(getResources().getColor(R.color.threeblack));
                    tv2.setTextColor(getResources().getColor(R.color.blue));
                    payedAction();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        unPaymentFragment  = UnpayedFragment.newInstance();
        payedFragment = PayedFragment.newInstance();
        unPayAction();
    }

    public void hideView(String status){

    }

    /**
     * 待还款
     */
    public void unPayAction() {
        tabFlag = "unpayed";
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.c_fl_borrow_records, unPaymentFragment, tabFlag);
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_opened", is_opened);
        bundle.putString("unborrowTime", unborrowTime);
        bundle.putSerializable("unpayedList", (Serializable) unPayList);
        unPaymentFragment.setArguments(bundle);
        fragmentTransaction.commit();
    }

    /**
     * 已结清
     */
    public void payedAction() {
        tabFlag = "payed";
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.c_fl_borrow_records, payedFragment, tabFlag);
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_FirstRequest", isFirstRequest);
        //非首次进入借还记录页面，显示之前首次请求过的记录
        if (!isFirstRequest) {
            bundle.putSerializable("payedList", (Serializable) payedList);
        }
        payedFragment.setArguments(bundle);
        fragmentTransaction.commit();
    }


    //校验是否有最新记录，有新记录，将旧的一批记录清除，将最新一批保存，无最新不保存
    private boolean  checkSaveUnpayment(String flag,List<MultiBorrowRecordsResponse.ResultBean> list){
        String contractIds = "";
        if("unpayment".equals(flag)){
            contractIds= CPreferenceUtil.getInstance(getApplicationContext())
                .getString("unpayedment_contractIds","");
        }else{
            contractIds= CPreferenceUtil.getInstance(getApplicationContext())
                    .getString("payedment_contractIds","");
        }
        String[] constractArray =null;
        int array_len = 0;
        if(contractIds.contains(",")){
            constractArray = contractIds.split(",");
            array_len= constractArray.length;
        }
        String newContracts = "";
        boolean hasNewId = false;
        int list_len = list.size();
        for(int i=0;i<list_len;i++){
            int num = 0;
            for(int j=0;j<array_len;j++){
                if(!TextUtils.isEmpty(constractArray[j])&&
                        constractArray[j].equals(list.get(i).getContract_id())){
                    num= num+1;
                }
            }
            if(num==0){
                hasNewId = true;
            }
            newContracts = newContracts+","+list.get(i).getContract_id();
        }
        if(hasNewId){
            if("unpayment".equals(flag)) {
                CPreferenceUtil.getInstance(getApplicationContext()).saveString("unpayedment_contractIds", newContracts.substring(0));
            }else{
                CPreferenceUtil.getInstance(getApplicationContext())
                        .saveString("payedment_contractIds", newContracts.substring(0));
            }
       }
        return hasNewId;
    }

    //刷新页面时使用
    @Override
    public void requestBorrowRecord(final String status, final String flag,final String product_id) {
        mstatus = status;
        mflag = flag;
        MultiBorrowRecordRequest request = new MultiBorrowRecordRequest();
        request.setLoan_status(status);
        request.setProduct_id(productStrs[spn_label.getSelectedItemPosition()]);
        httpsPost(this, request, ReqAction.LOAN_LIST, MultiBorrowRecordsResponse.class, new ICallback<MultiBorrowRecordsResponse>() {
            @Override
            public void success(MultiBorrowRecordsResponse borrowRecordsResponse) {
                int borrowRecordsCode = borrowRecordsResponse.getCode();
                if (borrowRecordsCode==5031) {
                    showMaintainDialog();
                } else {
                    if ("2".equals(status) && tabFlag.equals(flag)) {
                        unPayList.clear();
                        unPayList = borrowRecordsResponse.getResult();
                        unPaymentFragment.recordCallBackSuccess(unPayList);
                    } else if ("3".equals(status) && tabFlag.equals(flag)) {
                        payedList.clear();
                        payedList = borrowRecordsResponse.getResult();
                        payedFragment.recordCallBackSuccess(payedList);
                    }
                }
            }

            @Override
            public void failture(String tips) {
               if ("2".equals(status) && tabFlag.equals(flag)) {
                    unPaymentFragment.recordCallBackFailed(tips);
                } else if ("3".equals(status) && tabFlag.equals(flag)) {
                    payedFragment.recordCallBackFailed(tips);
                }
            }
        });
    }

    //首次进入借还记录页面，请求未还款和已还清的记录
    public void getData(final String status,String flag,final String product_id) {
        mstatus = status;
        mflag =flag;
        MultiBorrowRecordRequest request = new MultiBorrowRecordRequest();
        request.setLoan_status(status);
        request.setProduct_id(productStrs[pos]);
        httpsPost(this, request, ReqAction.APPLICATION_BORROW_RECORDS, MultiBorrowRecordsResponse.class, new ICallback<MultiBorrowRecordsResponse>() {
            @Override
            public void success(MultiBorrowRecordsResponse borrowRecordsResponse) {
                int borrowRecordsCode = borrowRecordsResponse.getCode();
                if (borrowRecordsCode==5031) {
                    showMaintainDialog();
                } else {
                    //待还款
                    if ("1".equals(status)) {
                        unPayList.clear();
                        unPayList = borrowRecordsResponse.getResult();
//                    getData("2");
                        isFirstRequest = false;
                    }
                    //已结清
                    else if ("2".equals(status)) {
                        payedList.clear();
                        payedList = borrowRecordsResponse.getResult();
                    }
                }
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(RYDMultiBorrowRecordsActivity.this, tips);
                RyxCreditLoadDialog.getInstance(getApplicationContext()).dismiss();
            }
        });
    }


    private void initTab() {

        View tab1View = LayoutInflater.from(this).inflate(R.layout.c_borrowrecord_tabitem_new, null);
        tv1 = (TextView) tab1View.findViewById(R.id.text1);
        iv1 = (ImageView) tab1View.findViewById(R.id.iv);
        tv1.setText("待还款");

        View tab2View = LayoutInflater.from(this).inflate(R.layout.c_borrowrecord_tabitem_new, null);
        tv2 = (TextView) tab2View.findViewById(R.id.text1);
        iv2 = (ImageView) tab2View.findViewById(R.id.iv);
        tv2.setText("已结清");

        tabA = tabLayout.newTab().setCustomView(tab1View);
        tabLayout.addTab(tabA);
        tabB = tabLayout.newTab().setCustomView(tab2View);
        tabLayout.addTab(tabB);
        initSpinner();
    }

    private void initSpinner() {
        spn_label = (Spinner) findViewById(R.id.sp_product);
        String[] items = new String[]{"全部", "瑞卡贷", "瑞商贷"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_spn, items);
        adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        spn_label.setAdapter(adapter);
        spn_label.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {
                   requestBorrowRecord(mstatus,mflag,productStrs[position]);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
