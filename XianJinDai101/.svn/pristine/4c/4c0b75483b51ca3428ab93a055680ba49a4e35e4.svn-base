package com.ryx.ryxcredit.ryd;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.beans.bussiness.borrowrecords.BorrowRecordsRequest;
import com.ryx.ryxcredit.beans.bussiness.borrowrecords.BorrowRecordsResponse;
import com.ryx.ryxcredit.ryd.borrowrecord.ApplayPaymentFragment;
import com.ryx.ryxcredit.ryd.borrowrecord.PayedFragment;
import com.ryx.ryxcredit.ryd.borrowrecord.UnPaymentFragment;
import com.ryx.ryxcredit.inter.IFragmentListener;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CPreferenceUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BorrowRecordsActivity extends BaseActivity implements IFragmentListener {

    private String tabFlag = "";
    private ApplayPaymentFragment applayPaymentFragment;
    private PayedFragment payedFragment;
    private UnPaymentFragment unPaymentFragment;
    private boolean is_opened;//是否在业务时间内
    private String unborrowTime;
    private TabLayout tabLayout;
    private TabLayout.Tab tabA, tabB, tabC;
    private TextView tv1, tv2, tv3;
    private ImageView iv1, iv2, iv3;
    private boolean isFirstRequest = true;
    private List<BorrowRecordsResponse.ResultBean> applyedList;
    private List<BorrowRecordsResponse.ResultBean> unPayedList;
    private List<BorrowRecordsResponse.ResultBean> finishedList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_borrow_records);
        setTitleLayout(getResources().getString(R.string.c_borrowing_records), true, false);
        applyedList = new ArrayList<BorrowRecordsResponse.ResultBean>();
        unPayedList = new ArrayList<BorrowRecordsResponse.ResultBean>();
        finishedList = new ArrayList<BorrowRecordsResponse.ResultBean>();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("is_opened"))
                is_opened = intent.getBooleanExtra("is_opened", false);
            if (intent.hasExtra("unborrowTime"))
                unborrowTime = intent.getStringExtra("unborrowTime");
        }
        tabLayout = (TabLayout) findViewById(R.id.c_tl_borrow_records);
        initTab();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tv1.setTextColor(getResources().getColor(R.color.blue));
                    tv2.setTextColor(getResources().getColor(R.color.threeblack));
                    tv3.setTextColor(getResources().getColor(R.color.threeblack));
                    //再次进入借还记录页面，不再重复请求，申请中记录
                    applyAction();
                } else if (tab.getPosition() == 1) {
                    tv1.setTextColor(getResources().getColor(R.color.threeblack));
                    tv2.setTextColor(getResources().getColor(R.color.blue));
                    tv3.setTextColor(getResources().getColor(R.color.threeblack));
                    unPayAction();
                } else {
                    tv1.setTextColor(getResources().getColor(R.color.threeblack));
                    tv2.setTextColor(getResources().getColor(R.color.threeblack));
                    tv3.setTextColor(getResources().getColor(R.color.blue));
                    payAction();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        applayPaymentFragment = ApplayPaymentFragment.newInstance();
        payedFragment = PayedFragment.newInstance();
        unPaymentFragment = UnPaymentFragment.newInstance();
        applyAction();

    }

    /**
     * 已结清
     */
    public void payAction() {
        tabFlag = "pay";
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.c_fl_borrow_records, payedFragment, tabFlag);
        Bundle bundle = new Bundle();
        bundle.putSerializable("payedList", (Serializable) finishedList);
        payedFragment.setArguments(bundle);
        fragmentTransaction.commit();
    }

    /**
     * 待还款
     */
    public void unPayAction() {
        tabFlag = "unpay";
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.c_fl_borrow_records, unPaymentFragment, tabFlag);
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_opened", is_opened);
        bundle.putString("unborrowTime", unborrowTime);
        bundle.putSerializable("unpayedList", (Serializable) unPayedList);
        unPaymentFragment.setArguments(bundle);
        fragmentTransaction.commit();
    }

    /**
     * 申请中
     */
    public void applyAction() {
        tabFlag = "apply";
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.c_fl_borrow_records, applayPaymentFragment, tabFlag);
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_FirstRequest", isFirstRequest);
        //非首次进入借还记录页面，显示之前首次请求过的记录
        if (!isFirstRequest) {
            bundle.putSerializable("applyedList", (Serializable) applyedList);
        }
        applayPaymentFragment.setArguments(bundle);
        fragmentTransaction.commit();
    }

    //校验是否有最新记录，有新记录，将旧的一批记录清除，将最新一批保存，无最新不保存
    private boolean  checkSaveUnpayment(String flag,List<BorrowRecordsResponse.ResultBean> list){
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
    public void requestBorrowRecord(final String status, final String flag) {
        BorrowRecordsRequest request = new BorrowRecordsRequest();
        request.setLoan_status(status);
        httpsPost(this, request, ReqAction.APPLICATION_BORROW_RECORDS, BorrowRecordsResponse.class, new ICallback<BorrowRecordsResponse>() {
            @Override
            public void success(BorrowRecordsResponse borrowRecordsResponse) {
                if ("1".equals(status) && tabFlag.equals(flag)) {
                    applyedList.clear();
                    applyedList = borrowRecordsResponse.getResult();
                    applayPaymentFragment.recordCallBackSuccess(applyedList);
                } else if ("2".equals(status) && tabFlag.equals(flag)) {
                    unPayedList.clear();
                    unPayedList = borrowRecordsResponse.getResult();
                    unPaymentFragment.recordCallBackSuccess(unPayedList);
                } else if ("3".equals(status) && tabFlag.equals(flag)) {
                    finishedList.clear();
                    finishedList = borrowRecordsResponse.getResult();
                    payedFragment.recordCallBackSuccess(finishedList);
                }
            }

            @Override
            public void failture(String tips) {
                if ("1".equals(status) && tabFlag.equals(flag)) {
                    applayPaymentFragment.recordCallBackFailed(tips);
                } else if ("2".equals(status) && tabFlag.equals(flag)) {
                    unPaymentFragment.recordCallBackFailed(tips);
                } else if ("3".equals(status) && tabFlag.equals(flag)) {
                    payedFragment.recordCallBackFailed(tips);
                }
            }
        });
    }

    //首次进入借还记录页面，请求未还款和已还清的记录
    public void getData(final String status) {
        BorrowRecordsRequest request = new BorrowRecordsRequest();
        request.setLoan_status(status);
        httpsPost(this, request, ReqAction.APPLICATION_BORROW_RECORDS, BorrowRecordsResponse.class, new ICallback<BorrowRecordsResponse>() {
            @Override
            public void success(BorrowRecordsResponse borrowRecordsResponse) {
                //申请中记录
                if ("1".equals(status)) {
                    applyedList.clear();
                    applyedList = borrowRecordsResponse.getResult();
                    getData("2");
                    isFirstRequest = false;
                }
                //待还款记录
                else if ("2".equals(status)) {
                    unPayedList.clear();
                    unPayedList = borrowRecordsResponse.getResult();
                    if (checkSaveUnpayment("unpayment",unPayedList)) {
                        iv2.setVisibility(View.VISIBLE);
                    } else {
                        iv2.setVisibility(View.GONE);
                    }
                    getData("3");
                }
                //已结清记录
                else if ("3".equals(status)) {
                    finishedList.clear();
                    finishedList = borrowRecordsResponse.getResult();
                    if (checkSaveUnpayment("payedment",finishedList)) {
                        iv3.setVisibility(View.VISIBLE);
                    } else {
                        iv3.setVisibility(View.GONE);
                    }
                    applayPaymentFragment.recordCallBackSuccess(applyedList);

                }
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(BorrowRecordsActivity.this, tips);
                applayPaymentFragment.recordCallBackFailed(tips);
            }
        });
    }

    public void hideView(String status) {
        if ("2".equals(status)) {
            iv2.setVisibility(View.GONE);
        } else if ("3".equals(status)) {
            iv3.setVisibility(View.GONE);
        }
    }


    private void initTab() {
        View tab1View = LayoutInflater.from(this).inflate(R.layout.c_borrowrecord_tabitem, null);
        tv1 = (TextView) tab1View.findViewById(R.id.text1);
        iv1 = (ImageView) tab1View.findViewById(R.id.iv);
        tv1.setText("申请中");

        View tab2View = LayoutInflater.from(this).inflate(R.layout.c_borrowrecord_tabitem, null);
        tv2 = (TextView) tab2View.findViewById(R.id.text1);
        iv2 = (ImageView) tab2View.findViewById(R.id.iv);
        tv2.setText("待还款");

        View tab3View = LayoutInflater.from(this).inflate(R.layout.c_borrowrecord_tabitem, null);
        tv3 = (TextView) tab3View.findViewById(R.id.text1);
        iv3 = (ImageView) tab3View.findViewById(R.id.iv);
        tv3.setText("已还清");

        tabA = tabLayout.newTab().setCustomView(tab1View);
        tabLayout.addTab(tabA);
        tabB = tabLayout.newTab().setCustomView(tab2View);
        tabLayout.addTab(tabB);
        tabC = tabLayout.newTab().setCustomView(tab3View);
        tabLayout.addTab(tabC);
    }
}
