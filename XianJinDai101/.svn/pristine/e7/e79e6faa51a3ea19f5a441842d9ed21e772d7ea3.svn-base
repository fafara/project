package com.ryx.ryxcredit.newactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.livedetect.FaceCollectActivity;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.Button;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.quickadapter.widget.CItemDivider;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.activity.BillInfoActivity;
import com.ryx.ryxcredit.activity.CreditActivity;
import com.ryx.ryxcredit.adapter.CreditCardsAdapter;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.beans.bussiness.activateline.CActivateLimitRequest;
import com.ryx.ryxcredit.beans.bussiness.activateline.CActivateLimitResponse;
import com.ryx.ryxcredit.beans.bussiness.activateline.CActivateLineSearchRequest;
import com.ryx.ryxcredit.beans.bussiness.activateline.CActivateLineSearchResponse;
import com.ryx.ryxcredit.beans.bussiness.activateline.CfindActiveRouteResponse;
import com.ryx.ryxcredit.beans.bussiness.product.CfindRouteRequest;
import com.ryx.ryxcredit.beans.pojo.CCard;
import com.ryx.ryxcredit.beans.pojo.Customer;
import com.ryx.ryxcredit.contactrecords.CallRecordsActivity;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.CCommonDialog;
import com.ryx.ryxcredit.utils.CDensityUtil;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CPreferenceUtil;
import com.ryx.ryxcredit.xjd.RKDBossBaseInfoSuccessActivity;

import java.util.ArrayList;
import java.util.List;

import static com.ryx.ryxcredit.RyxcreditConfig.context;

//激活成功

/**
 * Created by DIY on 2016/9/5.
 */
public class ActivateLineActivity extends BaseActivity {
    private RecyclerView creditCardsRv;
    private LinearLayout layInfo;
   // private AutoRelativeLayout laytitle;
    public static final int CUSTOMER_REQ_CODE = 0X1001;
    public static final int CONTACT_RECORDS_CODE = 0X1002;
    public static final int IDENTIFY_RECORDS_CODE = 0X1004;
    private LinearLayout lay_addMore;
    private TextView tv_gofinish;
    private ImageView img_finished;
    private Customer customer;
    private List<CCard> datas;
    private List<CCard> tempData;
    private int cardNum = 0;//激活需要绑定信用卡数量
    private Button mActiveBtn;
    private TextView tv_dflist;//查看支持的信用卡列表
    private boolean infoFinished;//信息是否完善过

    private String active;//激活状态
    private String activateFlag;//激活状态
    private String active_text;//点击激活按钮显示内容
   // private AutoLinearLayout lay_cotactinfo;
    private TextView tv_contactgofn;
    private ImageView img_contactfn;

    private String mo_status;//是否采集手机运营商信息
    private String face_status;//是否采集活信息
    private String is_mo_auth;//是否授权手机运营商信息采集协议
    private String mo_auth_url;//手机运营商采集授权协议
    private String baseinfo;
    private String companyAddressDetailRequest;
    private CPreferenceUtil preferenceUtil;
/*
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }*/

    private String province;
    private String city;
    private String region;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_activity_activate_credit);
        setTitleLayout("资料管理", true, false);
        //laytitle = (AutoRelativeLayout) findViewById(R.id.lay1);
        tv_gofinish = (TextView) findViewById(R.id.tv_gofinish);
        img_finished = (ImageView) findViewById(R.id.img_finished);
//        if (getIntent().getExtras() != null) {
//            activateFlag = getIntent().getExtras().getInt("isActive");
//        }
          if (getIntent().getExtras() != null) {
               baseinfo = getIntent().getExtras().getString("bossInfo");
           }
        creditCardsRv = (RecyclerView) findViewById(R.id.c_ry_credit_cards);
        lay_addMore = (LinearLayout) findViewById(R.id.lay_addMore);
        tv_contactgofn = (TextView) findViewById(R.id.tv_contactgofn);
       // lay_cotactinfo = (AutoLinearLayout) findViewById(R.id.lay_cotactinfo);
        img_contactfn = (ImageView) findViewById(R.id.img_contactfn);
       mActiveBtn = (Button) findViewById(R.id.c_btn_activate_apply_limit);
        tv_dflist = (TextView) findViewById(R.id.c_tv_dfbank_list);
        tv_dflist.setOnClickListener(new NoDoubleClickListener() {

            @Override
            protected void onNoDoubleClick(View view) {
                //查看33家代付卡支持列表，注意url为空的情况
                String repaybankurl = "https://mposprepo.ruiyinxin.com:444/ryx-xiaodai-aid/views/banklistcredit/bank_list_credit.html";
                searchBankList(repaybankurl);
            }
        });

        lay_addMore.setOnClickListener(new NoDoubleClickListener() {

            @Override
            protected void onNoDoubleClick(View view) {
                try {
                    Intent intent = new Intent(ActivateLineActivity.this, Class.forName(getApplicationContext().getPackageName() + ".bindcard.BankCardAddActivity_"));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        layInfo = (LinearLayout) findViewById(R.id.lay_info);
        layInfo.setOnClickListener(new NoDoubleClickListener() {

            @Override
            protected void onNoDoubleClick(View view) {
                if ("Y".equalsIgnoreCase(baseinfo)){
                    //员工
                    Intent intent = new Intent(ActivateLineActivity.this, BaseInfoActivity.class);
                    intent.putExtra("data", customer);
                    intent.putExtra("province ", province );
                    intent.putExtra("city ", city );
                    intent.putExtra("region ",region  );
                    intent.putExtra("infoFinished", infoFinished);
                    startActivityForResult(intent, CUSTOMER_REQ_CODE);
                }else{
                    //老板
                    Intent intent = new Intent(ActivateLineActivity.this, RKDBossBaseInfoSuccessActivity.class);
                    intent.putExtra("data", customer);
                    intent.putExtra("province ", province );
                    intent.putExtra("city ", city );
                    intent.putExtra("region ",region  );
                    intent.putExtra("infoFinished", infoFinished);
                    startActivityForResult(intent, CUSTOMER_REQ_CODE);
                }
            }
        });

        mActiveBtn.setOnClickListener(new NoDoubleClickListener() {

            @Override
            protected void onNoDoubleClick(View view) {
                //人脸识别
                if ("1".equals(face_status)) {
                    doNext();
                }
                // 直接激活
                else if ("0".equals(face_status)){
                    if ("1".equals(activateFlag)) {
                        CLogUtil.showToast(ActivateLineActivity.this, active_text);
                    } else if ("3".equals(activateFlag)) {
                        doActive();
                    }
                }
            }
        });
     /*   lay_cotactinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("2".equals(mo_status) || "3".equals(mo_status)) {
                    CLogUtil.showToast(ActivateLineActivity.this, "已完成授权!");
                } else if ("1".equals(mo_status) || "0".equals(mo_status)) {
                    getContactRecords();
                }

            }
        });*/
        setbottomMenu();
    }


    //激活
    private void doActive() {
        mActiveBtn.setEnabled(false);
        mActiveBtn.setBackgroundDrawable(ContextCompat.getDrawable(
                ActivateLineActivity.this, R.drawable.roundshape_gray));
        if (!checkStatus()) {
            mActiveBtn.setEnabled(true);
            mActiveBtn.setBackgroundDrawable(ContextCompat.getDrawable(
                    ActivateLineActivity.this, R.drawable.roundshape));
            return;
        }
        CActivateLimitRequest request = new CActivateLimitRequest();
        httpsPost(ActivateLineActivity.this, request, ReqAction.APPLICATION_LOAN_APPLY_LIMIT, CActivateLimitResponse.class, new ICallback<CActivateLimitResponse>() {
            @Override
            public void success(CActivateLimitResponse cActivateLimitResponse) {
                CCommonDialog.showRepaymentOK(ActivateLineActivity.this, "提交成功", "您的申请已提交，我们会尽快处理！", new CCommonDialog.IMessage() {
                    @Override
                    public void callback() {
                        startActivity(new Intent(ActivateLineActivity.this, CreditActivity.class));
                        finish();
                    }
                });
            }

            @Override
            public void failture(String tips) {
                mActiveBtn.setEnabled(true);
                mActiveBtn.setBackgroundDrawable(ContextCompat.getDrawable(
                        ActivateLineActivity.this, R.drawable.roundshape));
                CCommonDialog.showRepaymentError(ActivateLineActivity.this, "提交失败", tips + "");
            }
        });
    }

    //进入人脸识别
    private void doNext() {
        mActiveBtn.setEnabled(false);
        mActiveBtn.setBackgroundDrawable(ContextCompat.getDrawable(
                ActivateLineActivity.this, R.drawable.roundshape_gray));
        if (!checkStatus()) {
            mActiveBtn.setEnabled(true);
            mActiveBtn.setBackgroundDrawable(ContextCompat.getDrawable(
                    ActivateLineActivity.this, R.drawable.roundshape));
            return;
        }
        Intent intent = new Intent(ActivateLineActivity.this, FaceCollectActivity.class);
        startActivityForResult(intent, IDENTIFY_RECORDS_CODE);
        mActiveBtn.setEnabled(true);
        mActiveBtn.setBackgroundDrawable(ContextCompat.getDrawable(
                ActivateLineActivity.this, R.drawable.roundshape));

    }

    private void getContactRecords() {
        startActivityForResult(new Intent(ActivateLineActivity.this, CallRecordsActivity.class), CONTACT_RECORDS_CODE);
    }

    //请求激活页路由接口
    private void getRoute() {
        final CfindRouteRequest request = new CfindRouteRequest();
        request.setKey("active_page");
        httpsPost(this, request, ReqAction.APPLICATION_ROUTE, CfindActiveRouteResponse.class, new ICallback<CfindActiveRouteResponse>() {
            @Override
            public void success(CfindActiveRouteResponse routeResponse) {
                CfindActiveRouteResponse.ResultBean result = routeResponse.getResult();
                if (result != null) {
                    cardNum = result.getPayment_card_count();
                    activateFlag = result.getActive_status();
                    active_text = result.getActive_text();
                    face_status = result.getFace_status();
                    //需要人脸识别
                    if ("1".equals(face_status)) {
                        //mActiveBtn.setVisibility(View.VISIBLE);
                       // laytitle.setVisibility(View.VISIBLE);
                       // mActiveBtn.setText("下一步");
                    }
                    //不需要人脸识别
                    else if ("0".equals(face_status)) {
                        //激活按钮可见
                        if (("1".equals(activateFlag) || "3".equals(activateFlag))) {
                            //mActiveBtn.setVisibility(View.VISIBLE);
                           // laytitle.setVisibility(View.VISIBLE);
                           // mActiveBtn.setText("激活");
                        }//激活按钮不可见
                        else if ("0".equals(activateFlag)) {
                           // mActiveBtn.setVisibility(View.GONE);
                          //  laytitle.setVisibility(View.GONE);
                        }
                    }

                    mo_status = result.getMo_status();//电商数据采集
                    face_status = result.getFace_status();//进行活体采集
                    is_mo_auth = result.getIs_mo_auth();
                    mo_auth_url = result.getMo_auth_url();

                    //是否需要采集通话记录
                    //2不需要采集，0不需要采集不显示，1需要采集，3采集成功
                    if ("0".equals(mo_status)) {
                      //  lay_cotactinfo.setVisibility(View.GONE);
                    } else if ("1".equals(mo_status)) {
                       // lay_cotactinfo.setVisibility(View.VISIBLE);
                        tv_contactgofn.setVisibility(View.VISIBLE);
                        img_contactfn.setVisibility(View.GONE);
                    } else if ("2".equals(mo_status)) {
                       // lay_cotactinfo.setVisibility(View.VISIBLE);
                        tv_contactgofn.setVisibility(View.GONE);
                        img_contactfn.setVisibility(View.VISIBLE);
                    } else if ("3".equals(mo_status)) {
                      //  lay_cotactinfo.setVisibility(View.VISIBLE);
                        tv_contactgofn.setVisibility(View.GONE);
                        img_contactfn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void failture(String tips) {
            }
        });
    }

    //判断绑定卡的数量
    private boolean checkStatus() {
        CCard card = null;
        if(datas==null){
            return false;
        }
        int len = datas.size();
        //如果电信采集状态是1，则不可激活
        if ("1".equals(mo_status)) {
            CLogUtil.showToast(ActivateLineActivity.this, "您的电信数据尚未采集！");
            return false;
        }
        if (len < cardNum) {
            CLogUtil.showToast(ActivateLineActivity.this, "您最少需要绑定" + cardNum + "张信用卡！");
            return false;
        }
        for (int i = 0; i < cardNum; i++) {
            card = datas.get(i);
            if (card == null || TextUtils.isEmpty(card.getCreate_datetime())) {
                CLogUtil.showToast(ActivateLineActivity.this, "请完善信用卡信息！");
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        CActivateLineSearchRequest request = new CActivateLineSearchRequest();
        request.setVersion(RyxcreditConfig.getVersion());
        httpsPost(this, request, ReqAction.APPLICATION_ACTIVATION_LIMIT, CActivateLineSearchResponse.class, new ICallback<CActivateLineSearchResponse>() {
            @Override
            public void success(final CActivateLineSearchResponse cActivateLineResponse) {
                if (cActivateLineResponse.getResult() == null)
                    return;
                datas = cActivateLineResponse.getResult().getCards();
                int len = datas.size();
                tempData = new ArrayList<CCard>();
                tempData.addAll(datas);
                //如果用户已经绑定的信用卡数量，少于规定最少需绑定数量，则显示响应未绑定的空白栏
                if (len < cardNum) {
                    int dur = cardNum - len;
                    if (len < cardNum) {
                        for (int i = 0; i < dur; i++) {
                            CCard cCard = new CCard();
                            tempData.add(cCard);
                        }
                    }
                }
                CreditCardsAdapter cca = new CreditCardsAdapter(tempData, ActivateLineActivity.this);
                customer = cActivateLineResponse.getResult().getCustomer();
                province = cActivateLineResponse.getResult().getProvince();
                city =cActivateLineResponse.getResult().getCity();
                region =cActivateLineResponse.getResult().getRegion();
                preferenceUtil = CPreferenceUtil.getInstance(getApplicationContext());
                companyAddressDetailRequest=province+"|"+"-"+"|"+city+"|"+"-"+"|"+region+"|"+"-"+"|";
                preferenceUtil.saveString(RyxcreditConfig.getPhoneNo()+"c_baseinfo_work_detailaddress", companyAddressDetailRequest);
                if (!TextUtils.isEmpty(customer.getCreate_datetime())) {
                    tv_gofinish.setVisibility(View.GONE);
                    img_finished.setVisibility(View.VISIBLE);
                    infoFinished = true;
                } else {
                    tv_gofinish.setVisibility(View.VISIBLE);
                    img_finished.setVisibility(View.GONE);
                    infoFinished = false;
                }
                creditCardsRv.setLayoutManager(new LinearLayoutManager(ActivateLineActivity.this));
                creditCardsRv.addItemDecoration(new CItemDivider(ActivateLineActivity.this, R.drawable.c_recyclerview_credit_divide));
                creditCardsRv.setAdapter(cca);
                cca.setOnItemClickListener(new CreditCardsAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(View parent, int position) {
                        CCard ccd = tempData.get(position);
                        if (TextUtils.isEmpty(ccd.getCard_num())) {
                            try {
                                Intent intent = new Intent(ActivateLineActivity.this, Class.forName(getApplicationContext().getPackageName() + ".bindcard.BankCardAddActivity_"));
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Intent intent = new Intent(ActivateLineActivity.this, BillInfoActivity.class);
                            intent.putExtra("data", ccd);
                            startActivity(intent);
                        }

                    }
                });
                getRoute();
            }


            @Override
            public void failture(String tips) {
                getRoute();
            }
        });

    }

    @Override
    public void setbottomMenu() {
        super.setbottomMenu();
    }

    //查看支持的银行列表
    private void searchBankList(final String bankUrl) {

        Dialog.Builder builder = null;
        builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
            @Override
            protected void onBuildDone(final Dialog dialog) {
                WindowManager wm = dialog.getWindow().getWindowManager();
                DisplayMetrics dm = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(dm);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                WebView webView = (WebView) dialog.findViewById(R.id.bank_list_webview);
//                webView.loadUrl(CConstants.BANK_LIST_URL);
                webView.loadUrl(bankUrl);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return true;
                    }
                });
                dialog.layoutParams(dm.widthPixels - CDensityUtil.dip2px(context, 32), ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        builder.contentView(R.layout.c_dialog_bank_list);
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CUSTOMER_REQ_CODE || requestCode == CONTACT_RECORDS_CODE) {
            if (data != null) {
                customer = (Customer) data.getExtras().getSerializable("data");
                CLogUtil.showLog(customer.toString());

            }

        } else if (resultCode == IDENTIFY_RECORDS_CODE) {
            startActivity(new Intent(ActivateLineActivity.this, CreditActivity.class));
            finish();
        }
    }

}
