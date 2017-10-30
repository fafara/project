package com.ryx.payment.ruishua.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.BaseFragment;
import com.ryx.payment.ruishua.activity.MainFragmentActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.adapter.MainGridAdapter;
import com.ryx.payment.ruishua.authenticate.Authenticate_;
import com.ryx.payment.ruishua.authenticate.MerchantCredentialsUpload_;
import com.ryx.payment.ruishua.authenticate.newauthenticate.IdCardUploadAct_;
import com.ryx.payment.ruishua.authenticate.newauthenticate.NewAuthResultAct_;
import com.ryx.payment.ruishua.bean.AdavertisementInfo;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.IconBean;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.listener.FragmentListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.sjfx.ADHtmlActivity_;
import com.ryx.payment.ruishua.sjfx.CirclefriendsActivity;
import com.ryx.payment.ruishua.sjfx.adapter.AdavertisementAdapter;
import com.ryx.payment.ruishua.usercenter.WithdrawListImActivity_;
import com.ryx.payment.ruishua.utils.CNummberUtil;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.SJFXIntentHelper;
import com.ryx.payment.ruishua.widget.CMoneyTextView;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.ryx.quickadapter.recyclerView.animation.ScaleInAnimation;
import com.ryx.ryxcredit.utils.CJSONUtils;
import com.ryx.swiper.utils.MoneyEncoder;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

@EFragment(R.layout.fragment_sjfx_home_page)
public class SJFXhomeFragment extends BaseFragment {

    private View header;
    RecyclerView rc_top;
    private MainGridAdapter mainGridAdapter;
    private ArrayList<IconBean.IconMsgBean> mIconMsgBean = new ArrayList<>();
    private BaseActivity baseActivity;
    private FragmentListener mListener;

    CMoneyTextView tv_ydIncome;//昨日收益
    TextView tv_people_amount;//帮客人数
    CMoneyTextView mv_tlIncome;//累计收益
    CMoneyTextView mv_freeIncome;//可用收益
    AutoLinearLayout mv_freeIncome_layout,mv_tlIncome_layout,people_amount_layout;
    @ViewById
    XRecyclerView rv_advert;//广告位列表
    private static SJFXhomeFragment thisInstance;
    private String yd_income;//当前收益
    private String pl_amount;//bank人数
    private String tl_income;//累计收益
    private String income_his1_amount,income_his2_amount,income_his3_amount;//累计收益金额
    private String income_his1_name,income_his2_name,income_his3_name;//累计收益中描述
    private String bank1_name,bank2_name,bank3_name;//下三级圈友描述
    private String bank1_count,bank2_count,bank3_count;//下三级圈友个数
    private String availableAmt;//可提取金额
    private boolean isFirstIn = true;
    AdavertisementInfo adverInfo = new AdavertisementInfo();
    private AdavertisementAdapter adverAdapter;
    private List<AdavertisementInfo.AdvertRowsBean> adRowsBeen = new ArrayList<AdavertisementInfo.AdvertRowsBean>();
    private ArrayList<BankCardInfo> bankcardlist = new ArrayList<BankCardInfo>();// 已经绑定银行卡列表
    @AfterViews
    public void afterView() {
        initGridData();
        baseActivity = getBaseActivity();
        baseActivity.initQtPatParams();
        initRecyleView();
        if (isFirstIn) {
            mListener.doDataRequest("RequestPhp__bank");
            //查询我的收益
            getIncomeData(false);
            isFirstIn = false;
        }else{
            showInComData();
            mv_freeIncome.setText(availableAmt);
        }
    }

    /**
     * 初始化GridView数据
     */
    private void initGridData() {
        header = LayoutInflater.from(getActivity()).inflate(R.layout.sjfx_home_page_header, null, false);
        rc_top = (RecyclerView) header.findViewById(R.id.rc_top);
        tv_ydIncome = (CMoneyTextView) header.findViewById(R.id.tv_ydIncome);
        tv_people_amount = (TextView) header.findViewById(R.id.tv_people_amount);
        mv_tlIncome = (CMoneyTextView) header.findViewById(R.id.mv_tlIncome);
        mv_freeIncome = (CMoneyTextView) header.findViewById(R.id.mv_freeIncome);
        mv_freeIncome_layout=(AutoLinearLayout)header.findViewById(R.id.mv_freeIncome_layout);
        mv_tlIncome_layout=(AutoLinearLayout)header.findViewById(R.id.mv_tlIncome_layout);
        people_amount_layout=(AutoLinearLayout)header.findViewById(R.id.people_amount_layout);
        //圈友数
        people_amount_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                Intent intent = new Intent(getActivity(), CirclefriendsActivity.class);
                intent.putExtra("titleStr","圈友分布");
                intent.putExtra("oneImgUrl","https://mposprepo.ruiyinxin.com/web_content/pic/ban_income/gold.png");
                intent.putExtra("onedisName",bank1_name);
                intent.putExtra("oneResult",bank1_count);
                intent.putExtra("twoImgUrl","https://mposprepo.ruiyinxin.com/web_content/pic/ban_income/silver.png");
                intent.putExtra("twodisName",bank2_name);
                intent.putExtra("twoResult",bank2_count);
                intent.putExtra("threeImgUrl","https://mposprepo.ruiyinxin.com/web_content/pic/ban_income/bronze.png");
                intent.putExtra("threedisName",bank3_name);
                intent.putExtra("threeResult",bank3_count);
                startActivity(intent);
            }
        });
        //累计收益
        mv_tlIncome_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                Intent intent = new Intent(getActivity(), CirclefriendsActivity.class);
                intent.putExtra("titleStr","收益分布");
                intent.putExtra("oneImgUrl","https://mposprepo.ruiyinxin.com/web_content/pic/ban_income/gold.png");
                intent.putExtra("onedisName",income_his1_name);
                intent.putExtra("oneResult",String.format("%.2f", Double.parseDouble(income_his1_amount)));
                intent.putExtra("twoImgUrl","https://mposprepo.ruiyinxin.com/web_content/pic/ban_income/silver.png");
                intent.putExtra("twodisName",income_his2_name);
                intent.putExtra("twoResult", String.format("%.2f", Double.parseDouble(income_his2_amount)));
                intent.putExtra("threeImgUrl","https://mposprepo.ruiyinxin.com/web_content/pic/ban_income/bronze.png");
                intent.putExtra("threedisName",income_his3_name);
                intent.putExtra("threeResult",String.format("%.2f", Double.parseDouble(income_his3_amount)));
                startActivity(intent);
            }
        });
        //可提取
        mv_freeIncome_layout.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                getNewBankCardList(new BaseActivity.CompleteResultListen() {
                    @Override
                    public void compleResultok() {
                        final Bundle bundle = new Bundle();
                        bundle.putSerializable("bankcardlist", bankcardlist);
                        Intent intent = new Intent(getActivity(), WithdrawListImActivity_.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

            }
        });
        LinearLayoutManager linearLayoutManager=  new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_advert.setLayoutManager(linearLayoutManager);
        rv_advert.setPullRefreshEnabled(true);
        rv_advert.setLoadingMoreEnabled(false);
        rv_advert.addHeaderView(header);
        rv_advert.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        rv_advert.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //查询我的收益
                getIncomeData(true);
            }

            @Override
            public void onLoadMore() {

            }
        });

        String topResult = "";
        topResult = getFromRaw(R.raw.sjfx_top_grid);
        IconBean topIconBean = handleInputStream(topResult);
        mIconMsgBean = (ArrayList<IconBean.IconMsgBean>) topIconBean.getGetIconList();
        Comparator topComparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                IconBean.IconMsgBean imb1 = (IconBean.IconMsgBean) o1;
                IconBean.IconMsgBean imb2 = (IconBean.IconMsgBean) o2;
                return (new Integer(imb1.getIdx())).compareTo(new Integer(imb2.getIdx()));
            }
        };
        Collections.sort(mIconMsgBean, topComparator);

        Iterator<IconBean.IconMsgBean> topIterator = mIconMsgBean.iterator();
        while (topIterator.hasNext()) {
            IconBean.IconMsgBean topBean = topIterator.next();
//            if (topBean.getShow().equals("1")) {
//                //不显示的进行移除
//                topIterator.remove();
//            }
        }
        RecyclerViewHelper.init().setRVGridLayout(getActivity(), rc_top, 2);//4列
        mainGridAdapter = new MainGridAdapter(mIconMsgBean, getActivity(), R.layout.gridview_sjfx_main_item);
        rc_top.setAdapter(mainGridAdapter);
        mainGridAdapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object data) {
                if (checkPermission(mIconMsgBean.get(position))) {
                    initIntent(mIconMsgBean.get(position));
                }
            }
        });
    }

    private void initRecyleView() {
        adverAdapter = new AdavertisementAdapter(adRowsBeen, getActivity(), R.layout.adapter_sjfx_advertisement);
        adverAdapter.openLoadAnimation(new ScaleInAnimation());
        adverAdapter.setOnItemClickListener(new OnListItemClickListener() {

            @Override
            public void onItemClick(View view, int position, Object data) {
                onviewClick(view, position, (AdavertisementInfo.AdvertRowsBean) data);
            }
        });
        rv_advert.setAdapter(adverAdapter);
    }

    @AfterInject
    public void create() {
        thisInstance = this;
    }

    public SJFXhomeFragment getInstance() {
        return thisInstance;
    }

    /**
     * Mainfrgment回调
     *
     * @param type
     * @param ryxPayResult
     */
    public void send(String type, RyxPayResult ryxPayResult) {
        String result = ryxPayResult.getData();
        adverInfo = CJSONUtils.getInstance()
                .parseJSONObject(result, AdavertisementInfo.class);
        List<AdavertisementInfo.AdvertRowsBean> adRowsBeenList = adverInfo.getAdvertRows();
        adRowsBeen.clear();
        if(adRowsBeenList==null){
           String code= adverInfo.getCode();
            if(!RyxAppconfig.QTNET_SUCCESS.equals(code)){
                String mesage=adverInfo.getMessage();
                LogUtil.showToast(getContext(),"广告信息:"+mesage);
            }
        }else{
            for (int i = 0; i < adRowsBeenList.size(); i++) {
                adRowsBeen.add(adRowsBeenList.get(i));
            }
        }
        adverAdapter.notifyDataSetChanged();
    }

    //点击图片
    public void onviewClick(View view, int position, AdavertisementInfo.AdvertRowsBean item) {
        String url = item.getAdvert_href();
        String title = item.getAdvert_name();
        if(TextUtils.isEmpty(url)){
            return;
        }
//        //需要请求“我的邀请码”
//        if ("invite".equals(item.getAdvert_note())) {
//            invitationCode(title, url);
//        } else {
            jumpHtmlPage(title, url);
//        }
    }

    private void jumpHtmlPage(String title, String url) {
        String customerId=RyxAppdata.getInstance(getContext()).getCustomerId();
        Bundle bundle = new Bundle();
        bundle.putString("url", url+"&customerId="+customerId+"&appuser="+RyxAppconfig.APPUSER+"&version="+RyxAppconfig.CLIENTVERSION);
        bundle.putString("title", title);
        Intent intent = new Intent(getActivity(), ADHtmlActivity_.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 获取我的邀请码信息
     */
    private void invitationCode(final String title, final String baseUrl) {
        baseActivity.qtpayApplication.setValue("InvitationCode.Req");
        baseActivity.qtpayAttributeList.add(baseActivity.qtpayApplication);
        baseActivity.httpsPost("invitationCodeTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                try {
                    String data = payResult.getData();
                    JSONObject dataJsonObj = new JSONObject(data);
                    String code = JsonUtil.getValueFromJSONObject(dataJsonObj, "code");
                    if (RyxAppconfig.QTNET_SUCCESS.equals(code)) {
                        JSONObject resultOj = dataJsonObj.getJSONObject("result");
//                        final String code_val = JsonUtil.getValueFromJSONObject(resultOj, "code_val");
                        final String code_url = JsonUtil.getValueFromJSONObject(resultOj, "code_url");
                        String loadUrl = baseUrl + "&inviteCode=" + code_url + "";
                        jumpHtmlPage(title, loadUrl);
                        LogUtil.showLog("loadUrl---", loadUrl + "----");
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onOtherState(String rescode, String resDesc) {
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        LogUtil.showLog("SJFXhomeFragment---", "onResume----");

    }

    private void getIncomeData(final boolean isFresh) {
        baseActivity.qtpayApplication.setValue("Income.Req");
        baseActivity.qtpayAttributeList.add(baseActivity.qtpayApplication);
        baseActivity.httpsPost("Income", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                String result = payResult.getData();
                LogUtil.showLog("onTradeSuccess---" + result + "---");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject resultObject = JsonUtil.getJSONObjectFromJsonObject(jsonObject, "result");
                    //当前收益
                     yd_income = JsonUtil.getValueFromJSONObject(resultObject, "new_amount");
                    //帮客人数
                    pl_amount = JsonUtil.getValueFromJSONObject(resultObject, "bank");
                    //累积收益
                    tl_income = JsonUtil.getValueFromJSONObject(resultObject, "income");
//                    income_his1=JsonUtil.getValueFromJSONObject(resultObject);;
                   JSONObject income_his1Obj= JsonUtil.getJSONObjectFromJsonObject(resultObject,"income_his1");
                    income_his1_amount=JsonUtil.getValueFromJSONObject(income_his1Obj,"amount");
                    income_his1_name=JsonUtil.getValueFromJSONObject(income_his1Obj,"name");

                    JSONObject income_his2Obj= JsonUtil.getJSONObjectFromJsonObject(resultObject,"income_his2");
                    income_his2_amount=JsonUtil.getValueFromJSONObject(income_his2Obj,"amount");
                    income_his2_name=JsonUtil.getValueFromJSONObject(income_his2Obj,"name");

                    JSONObject income_his3Obj= JsonUtil.getJSONObjectFromJsonObject(resultObject,"income_his3");
                    income_his3_amount=JsonUtil.getValueFromJSONObject(income_his3Obj,"amount");
                    income_his3_name=JsonUtil.getValueFromJSONObject(income_his3Obj,"name");

                    JSONObject bank1Obj= JsonUtil.getJSONObjectFromJsonObject(resultObject,"bank1");
                    bank1_name=JsonUtil.getValueFromJSONObject(bank1Obj,"name");
                    bank1_count=JsonUtil.getValueFromJSONObject(bank1Obj,"count");

                    JSONObject bank2Obj= JsonUtil.getJSONObjectFromJsonObject(resultObject,"bank2");
                    bank2_name=JsonUtil.getValueFromJSONObject(bank2Obj,"name");
                    bank2_count=JsonUtil.getValueFromJSONObject(bank2Obj,"count");

                    JSONObject bank3Obj= JsonUtil.getJSONObjectFromJsonObject(resultObject,"bank3");
                    bank3_name=JsonUtil.getValueFromJSONObject(bank3Obj,"name");
                    bank3_count=JsonUtil.getValueFromJSONObject(bank3Obj,"count");


                    showInComData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                doQueryTradeAmt(isFresh);
            }

            @Override
            public void onOtherState() {
                doQueryTradeAmt(isFresh);
            }

            @Override
            public void onTradeFailed() {
                super.onTradeFailed();
                doQueryTradeAmt(isFresh);
            }
        });
    }

    /**
     * 展示收益数据
     */
    private void showInComData(){
        tv_ydIncome.withNumber(CNummberUtil.parseFloat(yd_income, 0.00f)).start();

        if (!TextUtils.isEmpty(pl_amount)) {
            tv_people_amount.setText(pl_amount);
        } else {
            tv_people_amount.setText("0");
        }

        mv_tlIncome.withNumber(CNummberUtil.parseFloat(tl_income, 0.00f)).start();
    }
    /**
     * 当日收款金额
     */
    private void doQueryTradeAmt(final boolean isFresh) {
        baseActivity.qtpayApplication.setValue("JFPalAcctEnquiry.Req");
        baseActivity.qtpayAttributeList.add(baseActivity.qtpayApplication);
        Param qtpayAcctType = new Param("acctType", "00");
        baseActivity.qtpayParameterList.add(qtpayAcctType);
        baseActivity.httpsPost("JFPalAcctEnquiryTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                if(isFresh){
                    rv_advert.refreshComplete();
                }
                    availableAmt = MoneyEncoder.decodeFormat(payResult
                        .getAvailableAmt());
                mv_freeIncome.setText(availableAmt);
//                LogUtil.showLog("resultObject",payResultData+"---");
//                try {
//                    if (!TextUtils.isEmpty(payResultData)) {
////                        JSONObject jsonObj = new JSONObject(payResultData);
////                        JSONObject amountcontrolObject = jsonObj.getJSONObject("amountcontrol");
////                        JSONObject sfObject = amountcontrolObject.getJSONObject("sf");
////                        String dayamtStr = sfObject.getString("dayamt");
////                        LogUtil.showLog("resultObject",dayamtStr+"---");
//                        //可用收益
//                        mv_freeIncome.withNumber(CNummberUtil.parseFloat(payResultData, 0.00f)).start();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onLoginAnomaly() {
                if(isFresh){
                    rv_advert.refreshComplete();
                }
            }

            @Override
            public void onOtherState() {
                if(isFresh){
                    rv_advert.refreshComplete();
                }
            }

            @Override
            public void onTradeFailed() {
                if(isFresh){
                    rv_advert.refreshComplete();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        try {
            mListener = (MainFragmentActivity) getBaseActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        super.onAttach(context);
    }

    /**
     * 统一跳转前检测
     *
     * @param msgBean 对象
     * @return 是否通过
     */
    private boolean checkPermission(IconBean.IconMsgBean msgBean) {
        String permissionStr = msgBean.getPermission();
        /**
         * 0   需要登录
         * 1   需要实名通过
         * 2   需要绑定默认结算卡
         */
        if (permissionStr.contains("0")) {//验证登录
            if (!QtpayAppData.getInstance(
                    getActivity().getApplicationContext()).isLogin()) {
                toAgainLogin(getActivity().getApplicationContext(), RyxAppconfig.TOLOGINACT);
                return false;
            }
        }
        if (permissionStr.contains("1")) {//验证实名认证是否通过
            int flag = QtpayAppData.getInstance(getActivity().getApplicationContext()).getAuthenFlag();
            if (flag != 3) {
                showAuthDialog();
                return false;
            }
        }
        return true;
    }

    /**
     * 展示实名认证框
     */
    private void showAuthDialog() {
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(getContext(), new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                gotoRealName();
            }

            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent("亲爱的用户，为了确保您的资金安全，进行此业务前需通过实名认证。");
    }

    private Intent intent;
    private String powermsg = "-1";// 实名认证有关

    /**
     * 跳转对应实名认证步骤
     */
    protected void gotoRealName() {
        // case 0: "未实名");
        // case 1:
        // case 5: "未认证");
        // case 2: "认证中");
        // case 4: "认证失败");
        // case 3: "已认证");
        int tag = QtpayAppData.getInstance(getActivity().getApplicationContext()).getAuthenFlag();
        //是否开启商户认证功能
        int isOpenMerchanttag = RyxAppdata.getInstance(getActivity().getApplicationContext()).getIsOpenMerchantFlag();
        LogUtil.showLog("tag==" + tag);
        switch (tag) {
            case 0:
                if (isOpenMerchanttag == 1) {
                    intent = new Intent(getActivity().getApplicationContext(), Authenticate_.class);
                } else {
                    //intent = new Intent(getActivity().getApplicationContext(), UserInfoAddActivity_.class);
                    //启用新的实名认证流程
                    intent = new Intent(getActivity().getApplicationContext(), IdCardUploadAct_.class);
                }
                break;
            case 1:
            case 5:
                if (QtpayAppData
                        .getInstance(getActivity().getApplicationContext())
                        .getUserType().equals("00")) {
                    intent = new Intent(getActivity().getApplicationContext(), IdCardUploadAct_.class);
//                    intent = new Intent(getActivity().getApplicationContext(),
//                            UserAuthPhotoUploadActivity_.class);
                } else if (QtpayAppData
                        .getInstance(getActivity().getApplicationContext())
                        .getUserType().equals("01")) {
                    intent = new Intent(getActivity().getApplicationContext(),
                            MerchantCredentialsUpload_.class);
                }
                break;
            case 2:
            case 3:
            case 4:
                //                if (QtpayAppData
//                        .getInstance(getActivity().getApplicationContext())
//                        .getUserType().equals("00")) {
//                    intent = new Intent(getActivity().getApplicationContext(),
//                            AuthResultActivity_.class).putExtra("PowerMsg", powermsg);
                intent = new Intent(getActivity().getApplicationContext(),
                        NewAuthResultAct_.class).putExtra("PowerMsg", powermsg);
//                }
                break;
        }
        try {
            startActivity(intent);
        } catch (Exception e) {
            LogUtil.showToast(getActivity().getApplicationContext(), "当前用户状态有误!");
        }
    }

    private void initIntent(IconBean.IconMsgBean msgBean) {
        if (null != msgBean) {
            String activityName = msgBean.getId();
            String flag = msgBean.getFlag();
            Bundle bundle = new Bundle();
            try {
//                if ("income_guide".equals(flag)) {
//                    bundle.putString("ccurl", PreferenceUtil.getInstance(getActivity()).getString("RevenueGuide.info", ""));
//                    bundle.putString("title", "收益指南");
//                }
                intentToActivity(activityName, bundle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 统一跳转对应模块
     *
     * @param ActivityName
     * @param bundle
     * @throws Exception
     */
    private void intentToActivity(String ActivityName, Bundle bundle) throws Exception {

        if (null == ActivityName || "".equals(ActivityName)) {
            throw new Exception("参数为空");
        }
        if (SJFXIntentHelper.getInstance().contains(ActivityName)) {
            Intent intent = new Intent(getActivity()
                    .getApplicationContext(), SJFXIntentHelper.getInstance()
                    .getActivityClass(ActivityName));
            intent.putExtras(bundle);
            startActivity(intent);
            return;
        }
        throw new Exception("尚未注册此ActivityName=" + ActivityName);
    }

    // 从resources中的raw 文件夹中获取文件并读取数据
    public String getFromRaw(int id) {
        String result = "";
        try {
            InputStream in = getResources().openRawResource(id);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = new String(buffer, 0, buffer.length, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public IconBean handleInputStream(String inputString) {
        IconBean iconBean = new IconBean();
        try {
            JSONArray jsonArray = new JSONObject(inputString).getJSONArray("getIconList");
            ArrayList<IconBean.IconMsgBean> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                IconBean.IconMsgBean msgBean = new IconBean.IconMsgBean();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                msgBean.setFlag(jsonObject.getString("flag"));
                msgBean.setId(jsonObject.getString("id"));
                msgBean.setIdx(jsonObject.getString("idx"));
                msgBean.setName(jsonObject.getString("name"));
                msgBean.setRes(jsonObject.getString("res"));
                msgBean.setShow(jsonObject.getString("show"));
                msgBean.setPermission(jsonObject.getString("permission"));
                list.add(msgBean);
            }
            iconBean.setGetIconList(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iconBean;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.showLog("SJFXHome===onActivityResult");
    }

    private void getNewBankCardList(final BaseActivity.CompleteResultListen completeResultListen) {
        baseActivity.qtpayApplication.setValue("BindCardList.Req");
        baseActivity.qtpayAttributeList.add(baseActivity.qtpayApplication);
        baseActivity.qtpayParameterList.add(new Param("cardType", "10"));
        baseActivity.httpsPost("BindCardListTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                newinitBankListData(payResult.getData());
                if (bankcardlist.isEmpty()) {
                    LogUtil.showToast(getActivity().getApplicationContext(), "您还未绑定有效结算卡,请先绑定有效结算卡!");
                    return;
                }
                completeResultListen.compleResultok();
            }
        });


    }

    /**
     * 初始化银行卡列表(new)
     *
     * @param banklistJson
     */
    private void newinitBankListData(String banklistJson) {
        try {
            JSONObject jsonObj = new JSONObject(banklistJson);
            bankcardlist.clear();
            if ("0000".equals(jsonObj.getString("code"))) {
                // 解析银行卡信息
                JSONArray banks = jsonObj.getJSONObject("result").getJSONArray("cardlist");
                for (int i = 0; i < banks.length(); i++) {
                    BankCardInfo   bankCardInfo = new BankCardInfo();
                    bankCardInfo.setCardIdx(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardidx"));
                    bankCardInfo.setBankId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankid"));
                    bankCardInfo.setAccountNo(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardno"));
                    bankCardInfo.setBankName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankname"));
                    bankCardInfo.setQuick(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "quick"));
                    bankCardInfo.setDaikou(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "msdk"));
                    bankCardInfo.setDaifustatus(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "daifustatus"));
                    bankCardInfo.setFlagInfo(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "flaginfo"));//1为默认结算卡
                    bankCardInfo.setCardtype(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardtype"));
                    bankCardInfo.setBranchBankName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "branchBankName"));
                    bankCardInfo.setName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "customername"));
                    bankCardInfo.setCardstatus(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardstatus"));
                    bankCardInfo.setCardnote(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardnote"));
                    bankcardlist.add(bankCardInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
