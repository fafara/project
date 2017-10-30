
package com.ryx.payment.ruishua.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rey.material.app.ThemeManager;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.BaseFragment;
import com.ryx.payment.ruishua.activity.MainFragmentActivity;
import com.ryx.payment.ruishua.activity.MessageDetailActiviy_;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.adapter.MainAdGridAdapter;
import com.ryx.payment.ruishua.adapter.MainGridAdapter;
import com.ryx.payment.ruishua.authenticate.AuthResultActivity_;
import com.ryx.payment.ruishua.authenticate.Authenticate_;
import com.ryx.payment.ruishua.authenticate.MerchantCredentialsUpload_;
import com.ryx.payment.ruishua.authenticate.UserAuthPhotoUploadActivity_;
import com.ryx.payment.ruishua.authenticate.UserInfoAddActivity_;
import com.ryx.payment.ruishua.authenticate.newauthenticate.IdCardUploadAct_;
import com.ryx.payment.ruishua.authenticate.newauthenticate.NewAuthResultAct_;
import com.ryx.payment.ruishua.bean.AdBeanMap;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.IconBean;
import com.ryx.payment.ruishua.bean.MsgInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.listener.DialogListener;
import com.ryx.payment.ruishua.listener.FragmentListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.sjfx.ADHtmlActivity_;
import com.ryx.payment.ruishua.usercenter.adapter.AuthedAppAdapter;
import com.ryx.payment.ruishua.utils.DataUtil;
import com.ryx.payment.ruishua.utils.DateUtil;
import com.ryx.payment.ruishua.utils.IDCardUtil;
import com.ryx.payment.ruishua.utils.IntentHelper;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.payment.ruishua.widget.MarqueeView;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.ryx.ryxcredit.xjd.CreditActivity;
import com.ryx.swiper.utils.MapUtil;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

@EFragment(R.layout.frag_main_home)
public class HomeFragment extends BaseFragment implements
        DialogListener {
//    private int reqflag = 0x0010;
    private int actiontype = -1; // 用来判断请求成功后 进一步判断该走哪一个分支
    private final int CARDBALANCE_SUCCESS = 11; // 余额查询成功
    private final int LOGIN_SUCCESS = 12; // 登陆成功
    private final int LOGINOUT = 13; // 登出
    private final int CAN_WITHDRAW = 10002; // 可以提款
    private final int CAN_NOT_WITHDRAW = 15; // 不可以提款，去完善信息
    private final int SHOW_CARD_LIST = 10001;
    private final int SHOW_CARD_LIST3 = 10004;
    private final int SHOW_CARD_FOR_SCAN_CODE = 10005;

    private Intent intent;
//    private ImageAdapter imageAdatper;
    private int galleryCurrent = 0;
    private int index = 0;
    private FragmentListener mListener;
    private static HomeFragment thisInstance;
    private String flag; // activity跳转标志位
    private String powermsg = "-1";// 实名认证有关
    private ArrayList<BankCardInfo> bankcardlist = new ArrayList<BankCardInfo>();// 已经绑定银行卡列表
    private ArrayList<IconBean.IconMsgBean> mTopIconMsgBean = new ArrayList<>();
    private ArrayList<IconBean.IconMsgBean> mMainIconMsgBean = new ArrayList<>();
    private ArrayList<MsgInfo> noticeTempNewList = new ArrayList<MsgInfo>();
    private   int lastItemPosition=0;
//    //底部GridView数据集合
//    private ArrayList<IconBean.IconMsgBean> mBottomIconMsgBean = new ArrayList<>();
    //所有数据集合
//    private ArrayList<IconBean.IconMsgBean> mOriginalData = new ArrayList<>();
//    private IconBean.IconMsgBean addActionBean = new IconBean.IconMsgBean
//            ("0", "0", "3", "MoreActivity_", "icon_add_normal", "更多", "add_action");
//    private ArrayList<BannerInfo> list;
    private String[] showStr;
    private ArrayList<String> flagList;
    private ArrayList<IconBean.IconMsgBean> showDatas;
    BankCardInfo bankCardInfo = null;
    private int checkposition=-1;
    private String[] temp;
//    @ViewById(R.id.gv_top)
    RecyclerView mTopRecyclerView;
    @ViewById(R.id.gv_bottom)
    XRecyclerView mBottomRecyclerView;
//    @ViewById(R.id.marqueeView)
    MarqueeView marqueeView;
//    @ViewById(R.id.materialRefreshLayout)
//    MaterialRefreshLayout materialRefreshLayout;
//    @ViewById(R.id.ll_point_group)
//    LinearLayout mPointGroup;
//    @ViewById(R.id.viewpager)
//    ViewPager mViewPager;
    @ViewById(R.id.v_line_1)
    View mMainLine1;
    @ViewById(R.id.v_line_2)
    View mMainLine2;
    @ViewById(R.id.ll_scan)
    LinearLayout mScanLl;//扫一扫
    @ViewById(R.id.iv_scan)
    ImageView mScanIv;
    @ViewById(R.id.tv_scan_title)
    TextView mScanTv;

    @ViewById(R.id.ll_scan_code_payment)
    LinearLayout mScanCodePaymentLl;//扫码收款
    @ViewById(R.id.iv_orcode_pay)
    ImageView mOrCodeIv;
    @ViewById(R.id.tv_orcode_pay_title)
    TextView mOrCodeTv;

    @ViewById(R.id.ll_payment)
    LinearLayout mPaymentLl;//刷卡收款
    @ViewById(R.id.iv_payment)
    ImageView mPaymentIv;
    @ViewById(R.id.tv_payment)
    TextView mPaymentTv;
    @ViewById(R.id.home_main_toplinerlayout)
    AutoLinearLayout home_main_toplinerlayout;
    //公告广告文本信息列表
    private String defaultMsginfo="----暂无公告信息----";
    List<String> info = new ArrayList<>();
    //广告位置信息列表数据
    final List<AdBeanMap> adBeanMapList=new ArrayList<>();
    private Map<String, IconBean.IconMsgBean> mMainBeanMap = new HashMap<>();
    private BaseActivity mBaseActivity;
    /**
     * 记录上一个页面的位置
     */
    private int lastPosition;
    private String level;
    private Param qtpayTransType;
    private Param qtpayAcctType;
    private boolean isSetDefaultCard = false;
    private MainGridAdapter mainTopGridAdapter;
//    private MainGridAdapter mainBottomGridAdapter;
    private MainAdGridAdapter mainAdGridAdapter;
    boolean isfirst=true;
    boolean Adreqisfirst=true;
    View header=null;
    private ArrayList<MsgInfo> noticeTempOldList = new ArrayList<MsgInfo>();
    private ArrayList<MsgInfo> noticeList = new ArrayList<MsgInfo>();
    @AfterInject
    public void create() {
        thisInstance = this;
    }
    @AfterViews
    public void afterView() {
        mBaseActivity = super.getBaseActivity();
        initQtPatParams();
        bindData();
        setListener();
        Intent intent=mBaseActivity.getIntent();
        if(intent!=null){
            boolean    LoginFlag=intent.getBooleanExtra("LoginFlag",false);
            LogUtil.showLog("getAppCustomerInfo==+++LoginFlag"+LoginFlag+"isfirst="+isfirst);
            if(isfirst&&LoginFlag){
                isfirst=false;
               httpGetetAppCustomerInfo();
            }
        }
        /**
         * 初始化广告和公告广告信息
         */
        if(Adreqisfirst){
            initAdAndAnnouncements();
            Adreqisfirst=false;
        }else{
            marqueeView.startWithList(info);
        }

    }

    private void initAdAndAnnouncements() {
        getNoticeList();
        if(noticeTempOldList.size()>0){
            noticeList.clear();
            noticeList.addAll(noticeTempOldList);
            info.clear();
            info.addAll(msgInfoListToInfoList(noticeList));
            marqueeView.startWithList(info);
        }else{
             mListener.doDataRequest("GetPublicNotice");
        }
            mListener.doDataRequest("RequestPhp_home");
    }
    /**
     * 将Msg列表转变为Info列表
     * @param noticesList
     * @return
     */
    private  List<String>  msgInfoListToInfoList(ArrayList<MsgInfo> noticesList){
    List<String> infoList = new ArrayList<>();
    for(int i=0;i<noticesList.size();i++){
        String noticeTitle=noticesList.get(i).getTitle();
        infoList.add(noticeTitle);
    }
    return infoList;
}

    /**
     * 网络请求后回调
     * @param type
     * @param ryxPayResult
     */
    public void send(String type,RyxPayResult ryxPayResult){
        try {
            if("RequestPhp_home_Completed".equals(type)){
                mBottomRecyclerView.refreshComplete();
            }else if("RequestPhp_home".equals(type)){
                String adJsonContent= ryxPayResult.getData();
                JSONObject adJsonObj=new JSONObject(adJsonContent);
                JSONObject posiRowObj= JsonUtil.getJSONObjectFromJsonObject(adJsonObj,"posiRow");
                String posiRowalert=JsonUtil.getValueFromJSONObject(posiRowObj,"alert");
                if("y040102".equals(posiRowalert)){
                    adBeanMapList.clear();
                    JSONArray advertRowsArray=adJsonObj.getJSONArray("advertRows");
                    for (int i=0;i<advertRowsArray.length();i++){
                       JSONObject advertRowObj= advertRowsArray.getJSONObject(i);
                        String advertRowalert=JsonUtil.getValueFromJSONObject(advertRowObj,"alert");
                        if("y080102".equals(advertRowalert)){
                            //广告链接
                            String advert_href=JsonUtil.getValueFromJSONObject(advertRowObj,"advert_href");
                            String advert_name=JsonUtil.getValueFromJSONObject(advertRowObj,"advert_name");
                            String advert_note=JsonUtil.getValueFromJSONObject(advertRowObj,"advert_note");
                            JSONObject mediaRowObj=advertRowObj.getJSONObject("mediaRow");
                            String mediaRow_alert=JsonUtil.getValueFromJSONObject(mediaRowObj,"alert");
                            if("y070102".equals(mediaRow_alert)){
                                String media_url=JsonUtil.getValueFromJSONObject(mediaRowObj,"media_url");
                                Map<String,String> map1=new HashMap<>();
                                map1.put("title",advert_name);
                                map1.put("imgurl",media_url);
                                map1.put("advert_href",advert_href);
                                map1.put("advert_note",advert_note);
                                AdBeanMap adBeanMap1=new AdBeanMap();
                                adBeanMap1.setMap(map1);
                                adBeanMapList.add(adBeanMap1);
                            }
                        }
                    }
                    LogUtil.showLog(adBeanMapList.size()+"==================");
                    mainAdGridAdapter.notifyDataSetChanged();
                }else{
                    String code=   JsonUtil.getValueFromJSONObject(adJsonObj,"code");
                    if(!RyxAppconfig.QTNET_SUCCESS.equals(code)){
                        String message=   JsonUtil.getValueFromJSONObject(adJsonObj,"message");
                        LogUtil.showToast(getActivity(),"活动信息:"+message);
                    }

                }
            }else if("GetPublicNotice".equals(type)){
                noticeList.clear();
                noticeTempNewList.clear();
                getNoticeList();
                analyzeNotices(ryxPayResult.getData());
                saveNoticeList();
                info.clear();
                info.addAll(msgInfoListToInfoList(noticeList));
                if(info.size()==0){
                    //如果没有消息就显示暂无消息
                    info.add(defaultMsginfo);
                }
                marqueeView.startWithList(info);
            }else if("httpGetetAppCustomerInfo".equals(type)){
                httpGetetAppCustomerInfo();
            }
        }catch (Exception e){
            LogUtil.showLog("数据异常=="+e.getLocalizedMessage());
        }

    }

    //解析通知内容
    private void analyzeNotices(String noticeData) {
        if (noticeData != null) {
            try {
                JSONObject noticeObj = new JSONObject(noticeData);
                if (!noticeObj.has("result")) {
                    return;
                }
                if (noticeObj.getJSONObject("result") == null) {
                    return;
                }
                if (!noticeObj.getJSONObject("result").has("message")) {
                    return;
                }
                if (!noticeObj.getJSONObject("result").has("resultCode")) {
                    return;
                }
                if (RyxAppconfig.QTNET_SUCCESS.equals(noticeObj.getJSONObject(
                        "result").getString("resultCode"))) {
                    if (!noticeObj.has("resultBean"))
                        return;
                    JSONArray noticeArray = noticeObj.getJSONArray("resultBean");
                    MsgInfo msgInfo = null;
                    int len = noticeArray.length();
                    for (int i = 0; i < len; i++) {
                        msgInfo = new MsgInfo();
                        String noticeCode = JsonUtil.getValueFromJSONObject(
                                noticeArray.getJSONObject(i), "noticeCode");
                        msgInfo.setNoticeCode(noticeCode);
                        msgInfo.setTitle(JsonUtil.getValueFromJSONObject(
                                noticeArray.getJSONObject(i), "title"));
                        msgInfo.setContent(JsonUtil.getValueFromJSONObject(
                                noticeArray.getJSONObject(i), "noticeContent"));
                        msgInfo.setTime(JsonUtil.getValueFromJSONObject(
                                noticeArray.getJSONObject(i), "effectTime"));
                        String noticeType = JsonUtil.getValueFromJSONObject(
                                noticeArray.getJSONObject(i), "noticeType");
                        msgInfo.setNoticeType(noticeType);
                        msgInfo.setActiveTime(JsonUtil.getValueFromJSONObject(
                                noticeArray.getJSONObject(i), "activeTime"));
                        String readFlag = "";
                        if ("0".equals(noticeType)) {
                            if (!hasNotice(noticeCode)) {
                                msgInfo.setReaded(false);
                                noticeTempNewList.add(msgInfo);
                            }
                        } else if ("1".equals(noticeType)) {
                            readFlag = JsonUtil.getValueFromJSONObject(
                                    noticeArray.getJSONObject(i), "readFlag");
                            msgInfo.setReadFlag(readFlag);
                        }
//                        // 个人消息，则根据readFlag判断消息状态
//                        if (("1".equals(noticeType) && "0".equals(readFlag))) {
//                            unreadPersonalNoticeNumber = unreadPersonalNoticeNumber + 1;
//                        }
                    }
                    noticeList.addAll(noticeTempNewList);
                    noticeList.addAll(noticeTempOldList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                noticeData = null;
            }
        }
    }
    private boolean hasNotice(String noticeCode) {
        int len = noticeTempOldList.size();
        for (int i = 0; i < len; i++) {
            if (noticeCode.equals(noticeTempOldList.get(i).getNoticeCode())) {
                return true;
            }
        }
        return false;
    }


    public void bindData() {
        initView();
        initMainData();
        initGridData();
//        initBanner();
        initAnnouncements();
    }

    private void initView() {
        header =   LayoutInflater.from(getActivity()).inflate(R.layout.main_home_xrecycle_header, null,false);
        mTopRecyclerView=(RecyclerView)header.findViewById(R.id.gv_top);
        marqueeView=(MarqueeView)header.findViewById(R.id.marqueeView);
    }

    private void initMainData() {
        String mainResult = "";
        mainResult = getFromRaw(RyxAppdata.getInstance(getActivity()).getCurrentBranchRawTopMainConfigId());
        IconBean mainIconBean = handleInputStream(mainResult);
        mMainIconMsgBean = (ArrayList<IconBean.IconMsgBean>) mainIconBean.getGetIconList();

        if(!TextUtils.isEmpty(mainIconBean.getMainbg())){
            //判断当前主页的主题
            int id = getResources().getIdentifier(mainIconBean.getMainbg(), "color",
                    getActivity().getPackageName());
            home_main_toplinerlayout.setBackgroundResource(id);
        }

        Comparator mainComparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                IconBean.IconMsgBean imb1 = (IconBean.IconMsgBean) o1;
                IconBean.IconMsgBean imb2 = (IconBean.IconMsgBean) o2;
                return (new Integer(imb1.getIdx())).compareTo(new Integer(imb2.getIdx()));
            }
        };
        Collections.sort(mMainIconMsgBean, mainComparator);//排序
        Iterator<IconBean.IconMsgBean> mainIterator = mMainIconMsgBean.iterator();
        while (mainIterator.hasNext()) {
            IconBean.IconMsgBean mainBean = mainIterator.next();
            if (mainBean.getShow().equals("1")) {
                //隐藏布局、移除对象、显示中间竖线
                String flag = mainBean.getFlag();
                if ("scanpay".equals(flag)) {
                    mScanLl.setVisibility(View.GONE);
                    mMainLine2.setVisibility(View.VISIBLE);
                } else if ("orcode".equals(flag)) {
                    mScanCodePaymentLl.setVisibility(View.GONE);
                    mMainLine2.setVisibility(View.VISIBLE);
                } else if ("impay".equals(flag) || "payment".equals(flag)) {
                    mPaymentLl.setVisibility(View.GONE);
                    mMainLine1.setVisibility(View.VISIBLE);
                }
                mainIterator.remove();
            }
        }
        setMainData(mMainIconMsgBean);
    }

    /**
     * 填充最顶部数据
     *
     * @param mMainIconMsgBean 对象
     */
    private void setMainData(ArrayList<IconBean.IconMsgBean> mMainIconMsgBean) {
        for (int i = 0; i < mMainIconMsgBean.size(); i++) {
            IconBean.IconMsgBean bean = mMainIconMsgBean.get(i);
            int id = getResources().getIdentifier(bean.getRes(), "drawable",
                    getActivity().getPackageName());
            String flag = bean.getFlag();
            String name = bean.getName();
            mMainBeanMap.put(flag, bean);//填充集合key:flag value:对象
            if ("scanpay".equals(flag)) {//二维码收款
                mScanTv.setText(name);
                mScanIv.setBackgroundResource(id);
            } else if ("orcode".equals(flag)) {//我的二维码
                mOrCodeTv.setText(name);
                mOrCodeIv.setBackgroundResource(id);
            } else if ("impay".equals(flag) || "payment".equals(flag)) {//刷卡收款
                mPaymentTv.setText(name);
                mPaymentIv.setBackgroundResource(id);
            }
        }
    }

    /**
     * 初始化公告信息
      */
    private void initAnnouncements(){
        marqueeView.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                if(defaultMsginfo.equals(textView.getText())){
                    return;
                }
                try {
                    Intent intent = new Intent(getActivity(), MessageDetailActiviy_.class);
                    intent.putExtra("msgMap", (Serializable) noticeList.get(position));
                    startActivity(intent);
                }catch (Exception e){
                    LogUtil.showToast(getActivity(),"消息内容有误!");
                }
            }
        });
    }
    /**
     * 初始化banner
     */
    private void initBanner() {
//        list = new ArrayList<>();
//        list.add(new BannerInfo(RyxAppdata.getInstance(getActivity().getApplicationContext()).getCurrentBranchHomeBannerId()));
//        initDots();
    }

//    private void initDots() {
//        for (int i = 0; i < list.size(); i++) {
//            // 添加指示点(一个ImageView)
//            ImageView pointImageView = new ImageView(getActivity());
//            // 指示点在线性布局中，设置布局参数
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            params.rightMargin = 12;// 右外边距
//            pointImageView.setLayoutParams(params);
//            pointImageView.setImageResource(R.drawable.point_bg);
//            if (i == 0) {
//                pointImageView.setEnabled(true);
//            } else {
//                pointImageView.setEnabled(false);
//            }
//            mPointGroup.addView(pointImageView);
//        }
//    }

    private void setListener() {
        mScanLl.setOnClickListener(clickListener);
        mScanCodePaymentLl.setOnClickListener(clickListener);
        mPaymentLl.setOnClickListener(clickListener);
//        mViewPager.setAdapter(new BannerAdapter(list, getActivity()));
//        // 第一页可以往前滚动并且无限循环（前后各一半的次数）
//        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2
//                - (Integer.MAX_VALUE / 2 % list.size()));
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
////                position = position % list.size();
////                // 改变指示点的状态,当前的指示点选中为true，上一个指示点选中为false
////                mPointGroup.getChildAt(position).setEnabled(true);
////                mPointGroup.getChildAt(lastPosition).setEnabled(false);
////                lastPosition = position;// 记录上一个点
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        //顶部网格
        mainTopGridAdapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object data) {
                IconBean.IconMsgBean msgBean = mTopIconMsgBean.get(position);
                if (null != msgBean && checkPermission(msgBean)) {
                    String activityName = msgBean.getId();
                    String flag = msgBean.getFlag();
                    Bundle bundle = new Bundle();
                    try {
                        if("creditclub".equals(flag)){
                            bundle.putString("title", "申请信用卡");
//                            bundle.putString("ccurl", RyxAppconfig.CREDIT_CLUB_KEY);
                            bundle.putString("urlkey", RyxAppconfig.Notes_OnlineCard);
                            intentToActivity(activityName, bundle);
                        }
                        else if ("usernote".equals(flag)) {//用户须知
                            bundle.putString("title", "用户须知");
                            bundle.putString("urlkey", RyxAppconfig.Notes_Notice);
                            intentToActivity(activityName, bundle);
                        } else if ("balance_query".equals(flag)) {//余额查询
                            bundle.putString("ActionType", "balance");
                            intentToActivity(activityName, bundle);
                        } else if ("withdrawim".equals(flag)) {//资金结算
                            getBankCardList(msgBean);
                        } else if ("credit".equals(flag)) {//小额贷款
                            enterCreditModule();//需要测试小贷放开
//                    intentToActivity(activityName, bundle);
                        } else if ("user_auth".equals(flag)) {//根据实名认证情况跳转对应
                            gotoRealName();
                        } else {
                            intentToActivity(activityName, bundle);//剩余统一跳转
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        protected void onNoDoubleClick(View view) {
            int id = view.getId();
            if (id == R.id.ll_scan) {//扫一扫
                IconBean.IconMsgBean bean = MapUtil.get(mMainBeanMap, "scanpay", null);
                if (null != bean) {
                    checkPermission(bean);
                }
            } else if (id == R.id.ll_payment) {//刷卡收款
                IconBean.IconMsgBean bean = MapUtil.get(mMainBeanMap, "impay", null);
                if (null != bean) {
                    checkPermission(bean);
                }
            } else if (id == R.id.ll_scan_code_payment) {//二维码收款
                IconBean.IconMsgBean bean = MapUtil.get(mMainBeanMap, "orcode", null);
                if (null != bean) {
                    checkPermission(bean);
                }
            }
        }
    };

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

    /**
     * 获取银行卡列表
     */
    private void getBankCardList(final IconBean.IconMsgBean bean) {
//        getOldBankCardList(bean);
          getNewBankCardList(bean);
    }

    /**
     * 调用新的卡管理中接口获取已绑定银行卡列表接口
     *
     * @param bean
     */
    private void getNewBankCardList(final IconBean.IconMsgBean bean) {
        mBaseActivity.qtpayApplication.setValue("BindCardList.Req");
        mBaseActivity.qtpayAttributeList.add(mBaseActivity.qtpayApplication);
        mBaseActivity.qtpayParameterList.add(new Param("cardType", "10"));
        mBaseActivity.httpsPost("BindCardListTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                newinitBankListData(payResult.getData());
                checkBindCard(bankcardlist, bean);
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
                    bankCardInfo = new BankCardInfo();
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
    /**
     * 初始化GridView数据
     */
    private void initGridData() {
        String topResult = "";
        topResult = getFromRaw(RyxAppdata.getInstance(getActivity()).getCurrentBranchRawTopGridConfigId());
        IconBean topIconBean = handleInputStream(topResult);
        mTopIconMsgBean = (ArrayList<IconBean.IconMsgBean>) topIconBean.getGetIconList();

//        IconBean bottomIconBean = getBottonListForShareStr();
//        mOriginalData = (ArrayList<IconBean.IconMsgBean>) bottomIconBean.getGetIconList();
        Comparator topComparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                IconBean.IconMsgBean imb1 = (IconBean.IconMsgBean) o1;
                IconBean.IconMsgBean imb2 = (IconBean.IconMsgBean) o2;
                return (new Integer(imb1.getIdx())).compareTo(new Integer(imb2.getIdx()));
            }
        };
        Collections.sort(mTopIconMsgBean, topComparator);
//        mBottomIconMsgBean.clear();
//        mBottomIconMsgBean.addAll(mOriginalData);
//        //添加更多到末尾
//        mBottomIconMsgBean.add(mBottomIconMsgBean.size(), addActionBean);

        Iterator<IconBean.IconMsgBean> topIterator = mTopIconMsgBean.iterator();
        while (topIterator.hasNext()) {
            IconBean.IconMsgBean topBean = topIterator.next();
            if (topBean.getShow().equals("1")) {
                //不显示的进行移除
                topIterator.remove();
            }
        }

//        Iterator<IconBean.IconMsgBean> bottomIterator = mBottomIconMsgBean.iterator();
//        while (bottomIterator.hasNext()) {
//            IconBean.IconMsgBean bottomBean = bottomIterator.next();
//            if (bottomBean.getShow().equals("1")) {
//                //不显示的进行移除
//                bottomIterator.remove();
//            }
//        }
        RecyclerViewHelper.init().setRVGridLayout(getActivity(), mTopRecyclerView, 4);//4列
        mainTopGridAdapter = new MainGridAdapter(mTopIconMsgBean, getActivity(), R.layout.gridview_main_item);
        mTopRecyclerView.setAdapter(mainTopGridAdapter);

//        RecyclerViewHelper.init().setRVGridLayout(getActivity(), mBottomRecyclerView, 4);//4列
//        mainBottomGridAdapter = new MainGridAdapter(mBottomIconMsgBean, getActivity(), R.layout.gridview_main_item);
//        mBottomRecyclerView.setAdapter(mainBottomGridAdapter);



        LinearLayoutManager linearLayoutManager=  new LinearLayoutManager(getActivity());
//        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBottomRecyclerView.setLayoutManager(linearLayoutManager);
//        mBottomRecyclerView.setHasFixedSize(true);
//        mBottomRecyclerView.setNestedScrollingEnabled(false);
        mainAdGridAdapter=new MainAdGridAdapter(adBeanMapList,getActivity(),R.layout.home_ad_item);
//        mainAdGridAdapter.openLoadAnimation(new ScaleInAnimation());//item动画
        mBottomRecyclerView.setPullRefreshEnabled(true);
        mBottomRecyclerView.setLoadingMoreEnabled(false);
//        mBottomRecyclerView.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL, 30, getResources().getColor(R.color.background_f0f3f5)));
//        mBottomRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mBottomRecyclerView.addHeaderView(header);
        mBottomRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        mBottomRecyclerView.setAdapter(mainAdGridAdapter);
        mainAdGridAdapter.setOnItemClickListener(new OnListItemClickListener(){
            @Override
            public void onItemClick(View view, int position, Object data) {
                Map<String,String> adBeanMap=  adBeanMapList.get(position).getMap();
               String advert_href= MapUtil.getString(adBeanMap,"advert_href");
//               String advert_note= MapUtil.getString(adBeanMap,"advert_note");
               String advert_name= MapUtil.getString(adBeanMap,"title");
                if(TextUtils.isEmpty(advert_href)){
                    return;
                }
//                if("invite".equals(advert_note)){
//                    //邀请码
//                    if (!QtpayAppData.getInstance(getActivity().getApplicationContext())
//                            .isLogin()) {
//                      //用户未登录跳转登录
//                        toAgainLogin(getActivity().getApplicationContext(),RyxAppconfig.TOLOGINACT);
//                        return;
//                    }
//                    invitationCode(advert_name,advert_href);
//
//                }else{
                    jumpHtmlPage(advert_name,advert_href);
//                }
            }
        });
        mBottomRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mListener.doDataRequest("GetPublicNotice");
                mListener.doDataRequest("RequestPhp_home");
            }

            @Override
            public void onLoadMore() {

            }
        });
    }

    /**
     * 跳转广告页面
     * @param title
     * @param url
     */
    private void jumpHtmlPage(String title,String url){
        String customerId=RyxAppdata.getInstance(getContext()).getCustomerId();
        Bundle bundle =new Bundle();
        bundle.putString("url", url+"&customerId="+customerId+"&appuser="+RyxAppconfig.APPUSER+"&version="+RyxAppconfig.CLIENTVERSION);
        bundle.putString("title",title);
        Intent intent = new Intent(getActivity(), ADHtmlActivity_.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public HomeFragment getInstance() {
        return thisInstance;
    }

    @Override
    public void onResume() {
        if (!QtpayAppData.getInstance(getActivity().getApplicationContext())
                .isLogin()) {
            actiontype = LOGINOUT;
        }
        super.onResume();
        mListener.doDataRequest("onResume");
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
        if (IntentHelper.getInstance().contains(ActivityName)) {
            Intent intent = new Intent(getActivity()
                    .getApplicationContext(), IntentHelper.getInstance()
                    .getActivityClass(ActivityName));
            intent.putExtras(bundle);
            startActivity(intent);
            return;
        }
        throw new Exception("尚未注册此ActivityName=" + ActivityName);
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
                toAgainLogin(getActivity().getApplicationContext(),RyxAppconfig.TOLOGINACT);
//                startActivityForResult(new Intent(getActivity()
//                        .getApplicationContext(), LoginActivity_.class),RyxAppconfig.TOLOGINACT);
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
        if (permissionStr.contains("2")) {//验证是否绑定银行卡
            getBankCardList(msgBean);
        }
        return true;
    }

    /**
     * 初始化银行卡列表
     */
    public void initBankListData(String banklistJson) {
        try {
            JSONObject jsonObj = new JSONObject(banklistJson);
            if ("0000".equals(jsonObj.getJSONObject("result").getString("resultCode"))) {
                // 解析银行卡信息
                JSONArray banks = jsonObj.getJSONArray("resultBean");
                bankcardlist.clear();
                for (int i = 0; i < banks.length(); i++) {
                    bankCardInfo = new BankCardInfo();
                    bankCardInfo.setBankCity(JsonUtil.getValueFromJSONObject(
                            banks.getJSONObject(i), "bankCity"));
                    bankCardInfo.setRemark(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i),
                            "remark"));
                    bankCardInfo.setBankProvinceId(JsonUtil.getValueFromJSONObject(
                            banks.getJSONObject(i), "bankProvinceId"));
                    bankCardInfo.setAccountNo(JsonUtil.getValueFromJSONObject(
                            banks.getJSONObject(i), "accountNo"));
                    bankCardInfo.setBankName(JsonUtil.getValueFromJSONObject(
                            banks.getJSONObject(i), "bankName"));
                    bankCardInfo.setBankProvince(JsonUtil.getValueFromJSONObject(
                            banks.getJSONObject(i), "bankProvince"));
                    bankCardInfo.setBankId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i),
                            "bankId"));
                    bankCardInfo.setFlagInfo(JsonUtil.getValueFromJSONObject(
                            banks.getJSONObject(i), "flagInfo"));
                    bankCardInfo.setBankCityId(JsonUtil.getValueFromJSONObject(
                            banks.getJSONObject(i), "bankCityId"));
                    bankCardInfo.setCardIdx(JsonUtil.getValueFromJSONObject(
                            banks.getJSONObject(i), "cardIdx"));
                    bankCardInfo.setName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i),
                            "name"));
                    bankCardInfo.setBranchBankId(JsonUtil.getValueFromJSONObject(
                            banks.getJSONObject(i), "branchBankId"));
                    bankCardInfo.setBranchBankName(JsonUtil.getValueFromJSONObject(
                            banks.getJSONObject(i), "branchBankName"));
                    bankcardlist.add(bankCardInfo);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

//    /**
//     * 根据share中值获取底部值
//     *
//     * @return
//     */
//    public IconBean getBottonListForShareStr() {
//        IconBean iconBean = new IconBean();
//        List<IconBean.IconMsgBean> realiconList = new ArrayList<>();
//        if (PreferenceUtil.getInstance(getActivity()).hasKey("rechargeflags")) {
//            String str = PreferenceUtil.getInstance(getActivity()).getString("rechargeflags", "");
//            LogUtil.showLog("获取到shareStr==" + str);
//            if (!TextUtils.isEmpty(str)) {
//                try {
//                    JSONArray jsonArray = new JSONObject(str).getJSONArray("getIconflags");
//                    String bottomListStr = getFromRaw(R.raw.bottom_grid_common);
//                    IconBean bottomIconBean = handleInputStream(bottomListStr);
//                    List<IconBean.IconMsgBean> inconList = bottomIconBean.getGetIconList();
//                    for (IconBean.IconMsgBean iconMsgBean : inconList) {
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            String flag = (String) jsonArray.get(i);
//                            if (iconMsgBean.getFlag().equals(flag)) {
//                                iconMsgBean.setShow("0");
//                                realiconList.add(iconMsgBean);
//                                break;
//                            }
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            iconBean.setGetIconList(realiconList);
//            return iconBean;
//        } else {
//            String bottomListStr = getFromRaw(R.raw.bottom_grid_common);
//            IconBean bottomIconBean = handleInputStream(bottomListStr);
//            return bottomIconBean;
//        }
//    }

    public IconBean handleInputStream(String inputString) {
        IconBean iconBean = new IconBean();
        try {
            JSONObject iconBeanjsonObject= new JSONObject(inputString);
            JSONArray jsonArray = iconBeanjsonObject.getJSONArray("getIconList");
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
          String mainbg=  JsonUtil.getValueFromJSONObject(iconBeanjsonObject,"mainbg");
            iconBean.setGetIconList(list);
            iconBean.setMainbg(mainbg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iconBean;
    }

    /**
     * 检查 是否绑定了银行卡
     */
    private void checkBindCard(final ArrayList<BankCardInfo> list, final IconBean.IconMsgBean bean) {
        final Bundle bundle = new Bundle();
        String flag = bean.getFlag();
        if (list.isEmpty()) {
            LogUtil.showToast(getActivity().getApplicationContext(), "您还未绑定有效结算卡,请先绑定有效结算卡!");
            return;
        }
        try {
            if ("impay".equals(flag)|| "withdrawim".equals(flag)||"orcode".equals(flag)) {//刷卡收款、扫码收款、资金结算
                bundle.putSerializable("bankcardlist", list);
                intentToActivity(bean.getId(), bundle);
//                for (BankCardInfo bankCardInfo : list) {
//                    if (bankCardInfo.getFlagInfo().equals("1")) {//设置了默认卡
//                        isSetDefaultCard = true;
//                        break;
//                    }
//                }
//                if (isSetDefaultCard) {
//                    intentToActivity(bean.getId(), bundle);
//                } else {
//                    LogUtil.showToast(getActivity().getApplicationContext(), "您还未设置默认结算卡，请进入绑定银行卡设置！");
//                }
            }else if("scanpay".equals(flag)){
                    //二维码扫一扫功能在此确定权限
                String waring = MessageFormat.format(getResources().getString(R.string.camerawaringmsgs), RyxAppdata.getInstance(getContext()).getCurrentBranchName());
                ((BaseActivity) getActivity()).requesDevicePermission(waring, 0x00018, new PermissionResult() {

                    @Override
                    public void requestSuccess() {
                        bundle.putSerializable("bankcardlist", list);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    ((BaseActivity) getActivity()).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                            intentToActivity(bean.getId(), bundle);
                                            }catch (Exception e){
                                            }
                                        }
                                    });
                                }
                            }).start();


                    }

                    @Override
                    public void requestFailed() {

                    }
                }, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 小贷模块入口
     * modify by laomao 2016-09-03 10:41:22 直接进入小贷
     */
    private void enterCreditModule() {
        PreferenceUtil preferenceUtil = PreferenceUtil.getInstance(getActivity().getApplicationContext());
        preferenceUtil.saveString("c_appUser", RyxAppconfig.APPUSER);
        preferenceUtil.saveString("c_cardId",RyxAppdata.getInstance(getActivity()).getCertPid());
        preferenceUtil.saveString("c_version",RyxAppconfig.CLIENTVERSION);
        preferenceUtil.saveString("c_bankimg_url", RyxAppconfig.BANKIMG_URL);
        Intent intent = new Intent(getActivity(), CreditActivity.class);
        startActivity(intent);
    }

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
                    if(RyxAppdata.getInstance(getActivity().getApplicationContext()).isAuthswitchOpen()){
                        //启用新的实名认证流程
                        intent = new Intent(getActivity().getApplicationContext(), IdCardUploadAct_.class);
                    }else{
                         //旧的流程
                         intent = new Intent(getActivity().getApplicationContext(), UserInfoAddActivity_.class);
                    }
                }
                break;
            case 1:
            case 5:
                if (QtpayAppData
                        .getInstance(getActivity().getApplicationContext())
                        .getUserType().equals("00")) {
                    if(RyxAppdata.getInstance(getActivity().getApplicationContext()).isAuthswitchOpen()){
                        intent = new Intent(getActivity().getApplicationContext(), IdCardUploadAct_.class);
                    }else{
                        intent = new Intent(getActivity().getApplicationContext(),
                                UserAuthPhotoUploadActivity_.class);
                    }

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
                if(RyxAppdata.getInstance(getActivity().getApplicationContext()).isAuthswitchOpen()){
                    intent = new Intent(getActivity().getApplicationContext(),
                            NewAuthResultAct_.class).putExtra("PowerMsg", powermsg);
                }else {
                    intent = new Intent(getActivity().getApplicationContext(),
                           AuthResultActivity_.class).putExtra("PowerMsg", powermsg);
                }
                break;
        }
        try {
            mListener.doDataRequest(intent);
        } catch (Exception e) {
            LogUtil.showToast(getActivity().getApplicationContext(), "当前用户状态有误!");
        }
    }
    @Override
    public void getConfirm() {
    }

    private void initQtPatParams() {
        mBaseActivity.initQtPatParams();
        qtpayTransType = new Param("transType", "00");
    }

//    /**
//     * 查询用户等级
//     */
//    private void doQueryUserLevel() {
//        mBaseActivity.qtpayApplication.setValue("UserInfoQuery.Req");
//        mBaseActivity.qtpayAttributeList.add(mBaseActivity.qtpayApplication);
//        mBaseActivity.qtpayParameterList.add(qtpayTransType);
//        mBaseActivity.qtpayMobileNO.setValue(QtpayAppData.getInstance(getActivity()).getMobileNo());
//        mBaseActivity.httpsPost(false, true, "queryUserLevel1", new XmlCallback() {
//            @Override
//            public void onTradeSuccess(RyxPayResult payResult) {
//                String flag = analyzeJson(payResult.getData());
//                if (TextUtils.isEmpty(flag) || Integer.parseInt(flag) < 2) {
//                    LogUtil.showToast(getActivity(), "您未通过信用卡认证，请先进入用户认证完成！");
//                } else {
//                    bankcardlist.clear();
//                    mListener.doDataRequest("GetBankCardListForWithScanCode");
//                }
//            }
//        });
//    }

    public String analyzeJson(String jsonstring) {
        try {
            if (jsonstring != null && jsonstring.length() > 0) {
                JSONObject jsonObj = new JSONObject(jsonstring);
                if (RyxAppconfig.QTNET_SUCCESS.equals(jsonObj.getJSONObject(
                        "result").getString("resultCode"))) {
                    JSONObject userinfo = jsonObj.getJSONObject("resultBean");
                    if (userinfo.has("vipLevel")) {
                        level = userinfo.getString("vipLevel");
                        //此值为1 未通过信用卡认证，值为2 等级是vip1，此值为3等级是vip2
                        return level;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.showLog("Home===onActivityResult==="+requestCode+"=="+resultCode+"=="+RyxAppconfig.TOLOGINACT+"=="+RyxAppconfig.LOGINACTFINISH);
//        if (requestCode == reqflag && resultCode == resultCode && data != null) {
//            //所有数据
//            showDatas = (ArrayList<IconBean.IconMsgBean>) data.getSerializableExtra("showArray");
//            mBottomIconMsgBean.clear();
//            mOriginalData.clear();
//            for (int i = 0; i < showDatas.size(); i++) {
//                IconBean.IconMsgBean bean = showDatas.get(i);
//                mOriginalData.add(bean);
//                if (bean.getShow().equals("0")) {
//                    mBottomIconMsgBean.add(bean);
//                }
//            }
//            mBottomIconMsgBean.add(mBottomIconMsgBean.size(), addActionBean);
//            mainBottomGridAdapter.notifyDataSetChanged();
//        }else

            if(requestCode==RyxAppconfig.TOLOGINACT&&resultCode==RyxAppconfig.LOGINACTFINISH){
                //登录成功返回当前页
            httpGetetAppCustomerInfo();
                String  loginMessage= data.getStringExtra("loginMessage");
                mListener.doDataRequest("showSnakeMsg",loginMessage);
                LogUtil.showLog("Home===showSnakeMsg=="+loginMessage);
            }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 获取当前手机号已经绑定过的所有贴牌信息
     */
    public  void httpGetetAppCustomerInfo(){
        mBaseActivity.initQtPatParams();
        int authenFlag =RyxAppdata.getInstance(getContext()).getAuthenFlag();
        LogUtil.showLog("getAppCustomerInfo=="+authenFlag);
        if(1==RyxAppdata.getInstance(getContext()).getCurrentIsNeedUpdateUserInfoType()&&!(2==authenFlag||3==authenFlag)){
            //当前app支持从其他贴牌更新资料并且当前用户状态没有实名认证完的则进行展示贴牌列表
            mBaseActivity.qtpayApplication.setValue("GetAppCustomerInfo.Req");
            mBaseActivity.qtpayAttributeList.add( mBaseActivity.qtpayApplication);
            mBaseActivity.httpsPost(false, true,"GetAppCustomerInfoTag", new XmlCallback() {
                @Override
                public void onTradeSuccess(RyxPayResult payResult) {
                    //判断返回已经实名认证过的贴牌列表,如果列表不为空则进行提示，如果为空则直接登录即可
                    String data=payResult.getData();
                    try {
                        JSONObject jsonObject= new JSONObject(data);
                        final JSONArray resutBeanArray=jsonObject.getJSONArray("resultBean");
                        LogUtil.showLog("getAppCustomerInfoRsut==1111111111"+resutBeanArray.length());
                        if(resutBeanArray.length()>0){
                                mBaseActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(getContext(), new ConFirmDialogListener() {

                                            @Override
                                            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                                                ryxSimpleConfirmDialog.dismiss();
                                                List<Map<String,String>> applist=new ArrayList<Map<String,String>>();
                                                for(int i=0;i<resutBeanArray.length();i++){
                                                    try {
                                                        JSONObject appJsonObj=resutBeanArray.getJSONObject(i);
                                                        Map<String,String> app=new HashMap<>();
                                                        app.put("logoid", JsonUtil.getValueFromJSONObject(appJsonObj,"appuser"));
                                                        app.put("branchid",JsonUtil.getValueFromJSONObject(appJsonObj,"branchid"));
                                                        app.put("branchName",JsonUtil.getValueFromJSONObject(appJsonObj,"branchname"));
                                                        app.put("username",JsonUtil.getValueFromJSONObject(appJsonObj,"username"));
                                                        app.put("level",JsonUtil.getValueFromJSONObject(appJsonObj,"viplevel"));
                                                        app.put("customerid",JsonUtil.getValueFromJSONObject(appJsonObj,"customerid"));
                                                        applist.add(app);
                                                    } catch (JSONException e) {
                                                    }
                                                }
                                                createRealNameAuthenApplist(applist);
                                            }
                                            @Override
                                            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                                                ryxSimpleConfirmDialog.dismiss();

                                            }
                                        });
                                        ryxSimpleConfirmDialog.show();
                                        ryxSimpleConfirmDialog.setOkbtnText("自动导入");
                                        ryxSimpleConfirmDialog.setCancelbtnText("不需要");
                                        ryxSimpleConfirmDialog.setContent("当前账号已经认证过其他产品,是否自动完善认证?");
                                    }
                                });
                        }else{
                            mListener.doDataRequest("checkMsgShowDialog");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.showLog("getAppCustomerInfo=="+e.getLocalizedMessage());

                    }
                }
            });
        }else{
            mListener.doDataRequest("checkMsgShowDialog");
        }
    }
    /**
     * 展示当前手机号已经注册过并认证通过的产品列表
     */
    private void createRealNameAuthenApplist(final List<Map<String,String>> appList) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        AutoLinearLayout layout = (AutoLinearLayout) inflater.inflate(R.layout.authedapp_list, null);
        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
        com.rey.material.app.Dialog.Builder builder = new com.rey.material.app.Dialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog);
        final com.rey.material.app.Dialog dialog = builder.build(getContext());

        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView_applist);

        ImageView imgview_close = (ImageView) layout.findViewById(R.id.imgview_close);
        imgview_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button cancel_btn =(Button) layout.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mListener.doDataRequest("checkMsgShowDialog");
            }
        });
        final EditText edt_userCardNo =(EditText) layout.findViewById(R.id.edt_userCardNo);
        Button ok_btn =(Button) layout.findViewById(R.id.ok_btn);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkposition==-1){
                    LogUtil.showToast(getContext(),"请先选择要导入的产品!");
                    return;
                }
                String userCardNo= edt_userCardNo.getText().toString();
                if(!IDCardUtil.isIDCard(userCardNo)){
                    LogUtil.showToast(getContext(),"请正确输入身份证号!");
                    return;
                }
                mBaseActivity.qtpayApplication.setValue("BindAppCustomerIn.Req");
                mBaseActivity.qtpayAttributeList.add( mBaseActivity.qtpayApplication);
                Map appMap=appList.get(checkposition);
                String customerid=  MapUtil.getString(appMap,"customerid");
                mBaseActivity. qtpayParameterList.add(new Param("bindCustomerId",customerid));
                mBaseActivity.qtpayParameterList.add(new Param("identityProve",userCardNo));
//                qtpayAttributeList.add(new Param("customerId",qtpayResult.getCustomerId()));
                mBaseActivity. httpsPost("BindAppCustomerInTag", new XmlCallback() {
                    @Override
                    public void onTradeSuccess(final RyxPayResult payResult) {
                        dialog.dismiss();
                        try {
                            String data=payResult.getData();
                            JSONObject dataObj=new JSONObject(data);
                            JSONArray resultBeanArray=dataObj.getJSONArray("resultBean");
                            JSONObject resulBeanObj=resultBeanArray.getJSONObject(0);
                            String authFlag=JsonUtil.getValueFromJSONObject(resulBeanObj,"authenflag");
                            String customerid =JsonUtil.getValueFromJSONObject(resulBeanObj,"customerid");
                            String email=JsonUtil.getValueFromJSONObject(resulBeanObj,"email");
                            String realname=JsonUtil.getValueFromJSONObject(resulBeanObj,"realname");
                            String username=JsonUtil.getValueFromJSONObject(resulBeanObj,"username");
                            String usertype=JsonUtil.getValueFromJSONObject(resulBeanObj,"usertype");
                            RyxAppdata.getInstance(getContext()).setAuthenFlag(Integer.parseInt(authFlag));
                            RyxAppdata.getInstance(getContext()).setCustomerId(customerid);
                            RyxAppdata.getInstance(getContext()).setEmail(email);
                            RyxAppdata.getInstance(getContext()).setRealName(realname);
                            RyxAppdata.getInstance(getContext()).setUserType(usertype);
                            RyxAppdata.getInstance(getContext()).setCustomerName(username);
                            LogUtil.showToast(getContext(),"更新成功!");
                            mListener.doDataRequest("checkMsgShowDialog");
                        } catch (Exception e) {
                            LogUtil.showLog("BindAppCustomerIn的JSON数据异常");
                        }
                    }
                    @Override
                    public void onOtherState(String rescode, String resDesc) {
                        super.onOtherState(rescode, resDesc);
                        if("0660".equals(rescode)){
                            //用户以前更新过，则无需更新直接进入
                            mListener.doDataRequest("checkMsgShowDialog");
                        }
                    }
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final AuthedAppAdapter authedAppAdapter=new AuthedAppAdapter(appList,getContext(),R.layout.authedapplist_item);
        authedAppAdapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object data) {
                checkposition=position;
                for (Map<String,String> itemMap:appList) {
                    itemMap.put("check","");
                }
                Map<String,String> appMap=appList.get(position);
                appMap.put("check","1");
                authedAppAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(authedAppAdapter);
        dialog.setContentView(layout);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
    }

    /**
     * 读取本地已经存储的消息
     */
    private void getNoticeList() {
        ObjectInputStream in = null;
        try {
            InputStream is = getActivity().openFileInput("notice_"
                    + RyxAppdata.getInstance(getActivity()).getCustomerId()
                    + ".obj");
            in = new ObjectInputStream(is);
            if (in != null) {
                noticeTempOldList = (ArrayList<MsgInfo>) in.readObject();
            }
        } catch (Exception e) {
            if (noticeTempOldList == null) {
                noticeTempOldList = new ArrayList<MsgInfo>();
            }
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        noticeTempOldList=DataUtil.removeDuplicate(noticeTempOldList);
        int len = noticeTempOldList.size();
        for (int i = 0; i < len; i++) {
            String activeTime = noticeTempOldList.get(i).getActiveTime();
            if (TextUtils.isEmpty(activeTime)) {
                continue;
            }
            if (activeTime.length() > 8)
                activeTime = activeTime.substring(0, 8);
            Date date = DateUtil.parseDate(activeTime, "yyyyMMdd");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            String str = formatter.format(curDate);
            Date tempCurDate = DateUtil.parseDate(str, "yyyyMMdd");
            boolean isActive = compareDate(date, tempCurDate);
            if (!isActive) {
                noticeTempOldList.remove(i);
                len = len - 1;
            }
        }
        LogUtil.dshowLog(noticeTempOldList);
    }
    /**
     * 保存消息到本地
     */
    public void saveNoticeList() {
        ObjectOutputStream out = null;
        try {
            noticeList=DataUtil.removeDuplicate(noticeList);
            FileOutputStream os = getActivity().openFileOutput("notice_"
                    + RyxAppdata.getInstance(getActivity()).getCustomerId()
                    + ".obj", MODE_PRIVATE);
            out = new ObjectOutputStream(os);
            out.writeObject(noticeList);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private boolean compareDate(Date activedate, Date curdate) {
        if (DateUtil.getYear(activedate) < DateUtil.getYear(curdate)) {
            return false;
        } else if (DateUtil.getYear(activedate) == DateUtil.getYear(curdate)) {
            if (DateUtil.getMonth(activedate) < DateUtil.getMonth(curdate)) {
                return false;
            } else if (DateUtil.getMonth(activedate) == DateUtil
                    .getMonth(curdate)) {
                if (DateUtil.getDay(activedate) < DateUtil
                        .getDay(curdate)) {
                    return false;
                } else {
                    return true;
                }
            }
            return true;
        } else {
            return true;
        }
    }
}
