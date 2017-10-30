package com.ryx.payment.ruishua.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.RyxApplication;
import com.ryx.payment.ruishua.authenticate.newauthenticate.NewAuthResultAct_;
import com.ryx.payment.ruishua.bean.MsgInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.RyxHomeMessageDialog;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.fragment.HomeFragment_;
import com.ryx.payment.ruishua.fragment.MemberFragment;
import com.ryx.payment.ruishua.fragment.MemberFragment_;
import com.ryx.payment.ruishua.fragment.SJFXhomeFragment_;
import com.ryx.payment.ruishua.fragment.UserFragment_;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.listener.FragmentListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.setting.Setting_;
import com.ryx.payment.ruishua.utils.DataUtil;
import com.ryx.payment.ruishua.utils.DateUtil;
import com.ryx.payment.ruishua.utils.HttpUtil;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.payment.ruishua.utils.SnackbarUtil;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.sobot.chat.SobotApi;
import com.sobot.chat.api.enumtype.SobotChatTitleDisplayMode;
import com.sobot.chat.api.model.Information;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.majiajie.pagerbottomtabstrip.Controller;
import me.majiajie.pagerbottomtabstrip.PagerBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.TabItemBuild;
import me.majiajie.pagerbottomtabstrip.TabItemBuilder;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectListener;

import static com.landicorp.mpos.reader.model.MPosContext.getContext;
import static com.ryx.payment.ruishua.RyxAppconfig.CLOSE_HOMEFRG_REQ;

@EActivity(R.layout.activity_main_frag)
public class MainFragmentActivity extends BaseActivity implements FragmentListener {

    @ViewById
    PagerBottomTabLayout pagerBottomTabLayout;
    List<Fragment> mFragments;
    int[] testColors = {0xFF607D8B, 0xFFF57C00};
    Controller controller;
    private FragmentManager fragmentManager;
    private TextView mMainTitle;
    private ImageView mMsgImg;
    @ViewById(R.id.tv_msg)
    TextView tvMsg;
    @ViewById(R.id.iv_back)
    ImageView iv_back;
    RyxHomeMessageDialog ryxHomeMessageDialog;
    @ViewById
    View appbar;
    private android.support.v4.app.Fragment homeFragment;
    private android.support.v4.app.Fragment centerFragment;
    private android.support.v4.app.Fragment memerFragment;
    private android.support.v4.app.Fragment setFragment;
    private android.support.v4.app.Fragment storeFragment;
    private android.support.v4.app.Fragment bankFragment;
    private long exitTime = 0;
    /**
     * actionType =1 展示卡列表
     */
    public final static int SHOW_CARD_LIST = 10001;
    public final static int CAN_WITHDRAW = 10002; // 可以提款
    public final static int CAN_NOT_WITHDRAW = 10003; // 不可以提款，去完善信息
    public final static int SHOW_CARD_LIST3 = 10004;
    public final static int SHOW_CARD_FOR_SCAN_CODE = 10005;


    int actiontype = -1; // 用来判断请求成功后 进一步判断该走哪一个分支
    private int unreadNoticeNumber = 0;//通知数量
    private int unreadPersonalNoticeNumber = 0;//个人通知数量
    Boolean isfirst = true;
    private String noticeCode = "0000";
    private String tagStr = "main";
    private int currentFragmentIndex = 0;

    @AfterViews
    public void initViews() {
        mMainTitle = (TextView) findViewById(R.id.tv_main_title);
        mMsgImg = (ImageView) findViewById(R.id.iv_msg);
        View view = findViewById(R.id.appbar);
        view.setBackgroundResource(RyxAppdata.getInstance(MainFragmentActivity.this).getCurrentBranchTitleBagColor());
        initFragment();
        initBottomTab();
        initQtPatParams();
        initLoginData();
//        initQuickPayGuide();
    }

    /**
     * 初始化展示快捷支付引导页
     */
    private void initQuickPayGuide() {
        int quickpayGuide = PreferenceUtil.getInstance(MainFragmentActivity.this).getInt("quickpayGuide", 0);
        if (quickpayGuide == 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainFragmentActivity.this, QuickpayGuideActivity_.class);
                            startActivity(intent);
                        }
                    });

                }
            }).start();

        }
    }

    /**
     * 初始化登录数据
     */
    private void initLoginData() {
        Intent intent = getIntent();
        if (intent != null) {
            Boolean loginFlag = intent.getBooleanExtra("LoginFlag", false);
            String loginMessage = intent.getStringExtra("loginMessage");
            if (loginFlag && !TextUtils.isEmpty(loginMessage)) {
                showSnackbar(loginMessage);
                LogUtil.showLog("MainFrag=initLoginData==showSnakeMsg==" + loginMessage);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Click(R.id.iv_back)
    public void ivBackOnCLick() {
        //跳转系统拨号
//        Uri uri = Uri.parse("tel:" + getResources().getString(R.string.service_phone));
//        Intent it = new Intent(Intent.ACTION_DIAL, uri);
//        startActivity(it);
        if (!QtpayAppData.getInstance(
                getApplicationContext()).isLogin()) {
            toAgainLogin(getApplicationContext(), RyxAppconfig.TOLOGINACT);
        } else {
            initKeFu();
        }
    }

    private void initKeFu() {
        Information info = new Information();
        info.setAppkey(RyxAppdata.getInstance(MainFragmentActivity.this).getCurrentBranchKefuKey());
        info.setUname(RyxAppdata.getInstance(MainFragmentActivity.this).getRealName());
        //用户姓名，选填
        info.setRealname(RyxAppdata.getInstance(MainFragmentActivity.this).getRealName());
        //用户电话，选填
        info.setTel(RyxAppdata.getInstance(MainFragmentActivity.this).getMobileNo());

        //设置标题栏的背景图片，选填
//        info.setTitleImgId(R.drawable.sobot_delete_hismsg_normal);
//设置标题栏的背景颜色，如果背景颜色和背景图片都设置，则以背景图片为准，选填
//     int styleTag=RyxAppdata.getInstance(MainFragmentActivity.this).getCurrentBranchMainStyleTag();
//        if(styleTag==1){
//        //蓝色调
//            info.setColor("#1db7f0");
//        }else if(styleTag==3){
//            //灰色调
//            info.setColor("#404352");
//        }
        info.setTitleImgId(R.color.blue);
        String infoUid = RyxAppdata.getInstance(MainFragmentActivity.this).getMobileNo() + RyxAppdata.getInstance(MainFragmentActivity.this).getCustomerId();
        info.setUid(infoUid);
        /**
         * 设置聊天界面标题显示模式
         * @param context 上下文对象
         * @param mode titile的显示模式
         *              SobotChatTitleDisplayMode.Default:显示客服昵称(默认)
         *              SobotChatTitleDisplayMode.ShowFixedText:显示固定文本
         *              SobotChatTitleDisplayMode.ShowCompanyName:显示console设置的企业名称
         * @param content 如果需要显示固定文本，需要传入此参数，其他模式可以不传
         */
        SobotApi.setChatTitleDisplayMode(MainFragmentActivity.this, SobotChatTitleDisplayMode.Default, "");

        //默认false：显示转人工按钮。true：智能转人工
        info.setArtificialIntelligence(false);
        //当未知问题或者向导问题显示超过(X)次时，显示转人工按钮。
        //注意：只有ArtificialIntelligence参数为true时起作用
        info.setArtificialIntelligenceNum(5);
        //是否使用语音功能 true使用 false不使用
//                info.setUseVoice(true);
        //客服模式控制 -1不控制 按照服务器后台设置的模式运行
        //1仅机器人 2仅人工 3机器人优先 4人工优先
        info.setInitModeType(3);
        //返回时是否弹出满意度评价
        info.setShowSatisfaction(true);
        /**
         * @param context 上下文对象
         * @param information 初始化参数
         */
        SobotApi.startSobotChat(MainFragmentActivity.this, info);
    }


    //获取未读的公共消息和个人消息的数量
    private void getAllNum() {
        unreadNoticeNumber = PreferenceUtil.getInstance(MainFragmentActivity.this).getInt(
                "unreadNoticeNumber_"
                        + RyxAppdata.getInstance(MainFragmentActivity.this)
                        .getMobileNo(), 0);
        unreadPersonalNoticeNumber = PreferenceUtil.getInstance(MainFragmentActivity.this).getInt(
                "unreadNoticePersonNumber_"
                        + RyxAppdata.getInstance(MainFragmentActivity.this)
                        .getMobileNo(), 0);
    }

    private void getNoticeNum() {
        if (!QtpayAppData.getInstance(MainFragmentActivity.this).isLogin()) {
            tvMsg.setVisibility(View.GONE);
            return;
        }
        getAllNum();
        LogUtil.showLog("NoticeNum---main", (unreadNoticeNumber + "---" + unreadPersonalNoticeNumber));
        //第一次进入主页，显示未读公共消息对话框
        if (isfirst) {
            isfirst = false;
            if ((unreadNoticeNumber + unreadPersonalNoticeNumber) > 0)
//                showmsgDialog();
                setMsgNum();
        }
        //非第一次进入主页，显示本地存储的公共和个人消息总数
        else {
            setMsgNum();
        }
        if ("user".equals(tagStr)) {
            tvMsg.setVisibility(View.GONE);
        }
    }

    private void setMsgNum() {
        LogUtil.showLog("NoticeNum---mainset", (unreadNoticeNumber + "---" + unreadPersonalNoticeNumber)
                + "---" + (unreadPersonalNoticeNumber + unreadNoticeNumber));
        if ((unreadPersonalNoticeNumber + unreadNoticeNumber) > 0) {
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText((unreadPersonalNoticeNumber + unreadNoticeNumber) + "");
        } else {
            tvMsg.setVisibility(View.GONE);
        }
    }

//    private void showmsgDialog() {
//
//        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(this, new ConFirmDialogListener() {
//
//            @Override
//            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
//                ryxSimpleConfirmDialog.dismiss();
//                Intent intent = new Intent(MainFragmentActivity.this,
//                        MessageScreenActivity_.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
//                ryxSimpleConfirmDialog.dismiss();
//            }
//        });
//        ryxSimpleConfirmDialog.show();
//        ryxSimpleConfirmDialog.setContent("您有重要消息，是否查看？");
//    }

    @Click(R.id.iv_msg)
    public void showMsg() {
        if (!QtpayAppData.getInstance(
                getApplicationContext()).isLogin()) {
            toAgainLogin(getApplicationContext(), RyxAppconfig.TOLOGINACT);
//            startActivityForResult(new Intent(getApplicationContext(), LoginActivity_.class), RyxAppconfig.TOLOGINACT);
            return;
        }
        startActivity(new Intent(MainFragmentActivity.this, MessageScreenActivity_.class));
    }

    private void initFragment() {
        mFragments = new ArrayList<>();
        homeFragment = new HomeFragment_();
        memerFragment = new MemberFragment_();
        centerFragment = new UserFragment_();
        mFragments.add(homeFragment);//1
        if(RyxAppconfig.isSuportMember){
            mFragments.add(memerFragment);//会员中心
        }
        if (RyxAppdata.getInstance(this).getCurrentBranchIsSupportBank()) {
            bankFragment = new SJFXhomeFragment_();
            mFragments.add(bankFragment);//2
        }
        mFragments.add(centerFragment);//3
        //初始化主页
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.push_up_in, R.anim.push_up_out);
        transaction.add(R.id.frameLayout, mFragments.get(0));
        currentFragmentIndex = 0;
        transaction.commitAllowingStateLoss();
    }

    private void initBottomTab() {
        //用TabItemBuilder构建一个导航按钮
//        TabItemBuilder tabItemBuilder = new TabItemBuilder(this).create()
//                .setDefaultIcon(android.R.drawable.ic_menu_send)
//                .setText("主页")
//                .setSelectedColor(testColors[0])
//                .setTag("A")
//                .build();
        //首页
        TabItemBuild homeBuild = new TabItemBuilder(this).create();
        if (2 == RyxAppdata.getInstance(MainFragmentActivity.this).getCurrentBranchMainStyleTag()) {
            homeBuild.setDefaultIcon(R.drawable.ic_home_noselect_rhb).setSelectedIcon(R.drawable.ic_home_select_rhb).setSelectedColor(getResources().getColor(R.color.rhb_main_bc));
        } else {
            homeBuild.setDefaultIcon(R.drawable.ic_home_noselect).setSelectedIcon(R.drawable.ic_home_select);
        }
        TabItemBuilder homeBuilder = homeBuild.setText("首页").setTag("main").build();


        //会员中心
        TabItemBuild memberBuild = new TabItemBuilder(this).create();
        if (2 == RyxAppdata.getInstance(MainFragmentActivity.this).getCurrentBranchMainStyleTag()) {
            memberBuild.setDefaultIcon(R.drawable.ic_member_normal_rhb).setSelectedIcon(R.drawable.ic_member_selected_rhb).setSelectedColor(getResources().getColor(R.color.rhb_main_bc));
        } else {
            memberBuild.setDefaultIcon(R.drawable.ic_member_normal).setSelectedIcon(R.drawable.ic_member_selected);
        }
        TabItemBuilder memberBuilder = memberBuild.setText("会员").setTag("member").build();


        //我的
        TabItemBuild userBuild = new TabItemBuilder(this).create();
        if (2 == RyxAppdata.getInstance(MainFragmentActivity.this).getCurrentBranchMainStyleTag()) {
            userBuild.setDefaultIcon(R.drawable.ic_user_noselect_rhb).setSelectedIcon(R.drawable.ic_user_select_rhb).setSelectedColor(getResources().getColor(R.color.rhb_main_bc));
        } else {
            userBuild.setDefaultIcon(R.drawable.ic_user_noselect).setSelectedIcon(R.drawable.ic_user_select);
        }
        TabItemBuilder userBuilder = userBuild.setText("我的").setTag("user").build();

        if (RyxAppdata.getInstance(this).getCurrentBranchIsSupportBank()) {
            //帮客商城
            TabItemBuilder bankBuilder = new TabItemBuilder(this).create()
                    .setDefaultIcon(R.drawable.ic_bank_normal)
                    .setText("收益圈")
                    .setSelectedIcon(R.drawable.ic_bank_selected)
                    .setTag("bank")
                    .build();
            if(RyxAppconfig.isSuportMember){
                //构建导航栏,得到Controller进行后续控制
                controller = pagerBottomTabLayout.builder()
                        .addTabItem(homeBuilder)//1
                        .addTabItem(memberBuilder)
                        .addTabItem(bankBuilder)//2
                        .addTabItem(userBuilder)//3
                        .build();
            }else{
                controller = pagerBottomTabLayout.builder()
                        .addTabItem(homeBuilder)//1
                        .addTabItem(bankBuilder)//2
                        .addTabItem(userBuilder)//3
                        .build();
            }

        } else {
            if(RyxAppconfig.isSuportMember){
                //构建导航栏,得到Controller进行后续控制
                controller = pagerBottomTabLayout.builder()
                        .addTabItem(homeBuilder)
                        .addTabItem(memberBuilder)
                        .addTabItem(userBuilder)
                        .build();
            }else{
                controller = pagerBottomTabLayout.builder()
                        .addTabItem(homeBuilder)
                        .addTabItem(userBuilder)
                        .build();
            }

        }
//        controller.setMessageNumber("user", 99);
//        controller.setMessageNumber("A",2);
//        controller.setDisplayOval(0,true);

        controller.addTabItemClickListener(listener);
    }

    private long userlastClickTime = 0;
    private long banklastClickTime = 0;
    private long memberlastClickTime = 0;
    OnTabItemSelectListener listener = new OnTabItemSelectListener() {
        @Override
        public void onSelected(int index, Object tag) {
            changeFrg(index, tag);

        }

        @Override
        public void onRepeatClick(int index, Object tag) {
            Log.i("asd", "onRepeatClick:" + index + "   TAG: " + tag.toString());
        }
    };

    private void replaceFrg(int targetIndex, String currentTag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.setCustomAnimations(R.anim.push_up_in, R.anim.push_up_out);
        Fragment fromFrg = mFragments.get(currentFragmentIndex);
        Fragment currentFrg = mFragments.get(targetIndex);
        if (currentFrg.isAdded()) {
            transaction.hide(fromFrg).show(currentFrg).commitAllowingStateLoss();
            LogUtil.showLog("currentFrg.isAdded()==");
            ((BaseFragment) currentFrg).refreshData();
        } else {
            LogUtil.showLog("add(R.id.frameLayout,currentFrg)==");
            transaction.hide(fromFrg).add(R.id.frameLayout, currentFrg).commitAllowingStateLoss();
        }
        currentFragmentIndex = targetIndex;
    }

    private void changeFrg(int index, Object tag) {
        LogUtil.showLog("asd", "onSelected:" + index + "   TAG: " + tag.toString());
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if ("user".equals(tag.toString())) {
            appbar.setVisibility(View.VISIBLE);
            if (!QtpayAppData.getInstance(getApplicationContext()).isLogin()) {
                controller.setSelect(0);
                if (currentTime - userlastClickTime < 1000) {
                    userlastClickTime = currentTime;
                } else {
                    userlastClickTime = currentTime;
                    toAgainLogin(getApplicationContext(), RyxAppconfig.TOLOGINACT);
                }
                return;
            }
            tagStr = "user";
            mMainTitle.setText("我的");
            mMsgImg.setImageResource(R.drawable.setting_img);
            mMsgImg.setVisibility(View.VISIBLE);
            tvMsg.setVisibility(View.GONE);
            iv_back.setVisibility(View.GONE);
            mMsgImg.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    Intent intent = new Intent(MainFragmentActivity.this,
                            Setting_.class);
                    startActivity(intent);
                }
            });
        } else if ("main".equals(tag.toString())) {
            appbar.setVisibility(View.VISIBLE);
            controller.setSelect(0);
            iv_back.setVisibility(View.VISIBLE);
            tagStr = "main";
            mMainTitle.setText(RyxAppdata.getInstance(MainFragmentActivity.this).getCurrentBranchName());
            mMsgImg.setImageResource(R.drawable.icon_toolbar_msg);
            mMsgImg.setVisibility(View.VISIBLE);
            getAllNum();
            if (!QtpayAppData.getInstance(MainFragmentActivity.this).isLogin() || (unreadNoticeNumber + unreadPersonalNoticeNumber) <= 0) {
                tvMsg.setVisibility(View.GONE);
            } else if ((unreadNoticeNumber + unreadPersonalNoticeNumber) > 0) {
                tvMsg.setVisibility(View.VISIBLE);
                tvMsg.setText((unreadPersonalNoticeNumber + unreadNoticeNumber) + "");
            }
            mMsgImg.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View view) {
                    showMsg();
                }
            });
        } else if ("bank".equals(tag.toString())) {
            appbar.setVisibility(View.VISIBLE);
            if (!QtpayAppData.getInstance(MainFragmentActivity.this).isLogin()) {
                controller.setSelect(0);
                if (currentTime - banklastClickTime < 1000) {
                    banklastClickTime = currentTime;
                } else {
                    banklastClickTime = currentTime;
                    toAgainLogin(MainFragmentActivity.this, RyxAppconfig.TOLOGINACT);
                }
                return;
            }
            tagStr = "bank";
            mMainTitle.setText("收益圈");
            mMsgImg.setVisibility(View.GONE);
            tvMsg.setVisibility(View.GONE);
            iv_back.setVisibility(View.GONE);

        } else if ("member".equals(tag.toString())) {
            appbar.setVisibility(View.GONE);
            tagStr = "member";
            if (!QtpayAppData.getInstance(MainFragmentActivity.this).isLogin()) {
                controller.setSelect(0);
                if (currentTime - memberlastClickTime < 1000) {
                    memberlastClickTime = currentTime;
                } else {
                    memberlastClickTime = currentTime;
                    toAgainLogin(MainFragmentActivity.this, RyxAppconfig.TOLOGINACT);
                }
                return;
            }
        }
        replaceFrg(index, tag.toString());
    }
//    private Fragment createFragment(String content) {
//        Fragment fragment = null;
//        if ("首页".equals(content)) {
//
//        } else if ("我的".equals(content)) {
//            fragment = new UserFragment();
//        }
//        return fragment;
////        AFragment fragment = new AFragment();
////        Bundle bundle = new Bundle();
////        bundle.putString("content", content);
////        fragment.setArguments(bundle);
//    }

    @Override
    public void doDataRequest(Object data) {
        if (data instanceof Intent) {
            startActivityForResult((Intent) data, CLOSE_HOMEFRG_REQ);
        }
        if (data instanceof String && "mainFrg_change".equals(data)) {
            changeFrg(0, "main");
        }
        if (data instanceof String && "Balance".equals(data) || "Balance3".equals(data)) {
//            doBalanceInquiry();
        } else if (data instanceof String && "dayTradeAmt".equals(data) || "dayTradeAmt3".equals(data)) {// 当日交易查询
//            dayTradeAmtInquiry();
        }
//        else if ("GetBankCardList".equals(data)) {// 银行卡绑定列表
//            getBankCardList2();
//        }
        else if (data instanceof String && "GetAdvancedVipList".equals(data)) {// 信用卡绑定认证
//            getCreditCardList();
        } else if (data instanceof String && "Withdraw".equals(data) || "Withdraw3".equals(data)
                || "WithBankcard".equals(data)) {
            doQueryUserCash((String) data);
        } else if (data instanceof String && "Take3".equals(data)) {
//            takephoto();
        } else if (data instanceof String && "Select3".equals(data)) {
//            selectphoto();
        } else if (data instanceof String && "PublicPic2".equals(data)) {

//            SelectPublicPic();

        } else if (data instanceof String && "POINT3".equals(data)) {
//            doRequestPoint();
        } else if (data instanceof String && "onResume".equals(data)) {
            getNoticeNum();
        } else if (data instanceof String && !TextUtils.isEmpty((String) data) && ((String) data).contains("RequestPhp")) {
            //首页或者是三级分销页广告位信息
            if (data instanceof String && ((String) data).endsWith("_home")) {
                String posid = RyxAppdata.getInstance(MainFragmentActivity.this).getCurrentHomeAdposiId();
                doRequestPhp((String) data, posid);
            } else if (data instanceof String && ((String) data).endsWith("_bank")) {
                doRequestPhp((String) data, "3");
            }
        } else if (data instanceof String && "GetPublicNotice".equals(data)) {
            getPublicNoticeRequest((String) data);
        } else if (data instanceof String && "toNewAuthResult".equals(data)) {
            //跳转实名认证结果页
            Intent intent = new Intent(this,
                    NewAuthResultAct_.class).putExtra("PowerMsg", "-1");
            startActivity(intent);
        } else if (data instanceof String && "checkMsgShowDialog".equals(data)) {
            checkMsgShowDialog();
        }
    }

    @Override
    public void doDataRequest(String type, Object data) {
        LogUtil.showLog("MainFrag===showSnakeMsg==" + data);
        if ("showSnakeMsg".equals(type)) {
            showSnackbar(String.valueOf(data));
        }
    }

    /**
     * 获取公共消息
     *
     * @param data
     */
    private void getPublicNoticeRequest(final String data) {
        qtpayApplication = new Param("application", "GetPublicNotice.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("noticeCode", "0000"));
        qtpayParameterList.add(new Param("noticeType", "2"));
        qtpayParameterList.add(new Param("readFlag", "2"));
        qtpayParameterList.add(new Param("offset", "1"));
        httpsPost(false, false, "GetPublicNotice", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                doAfterRequestSuccess(data, payResult);
            }
        });
    }

    /**
     * 首页文本广告和图文广告数据请求
     */
    private void doRequestPhp(final String reqTag, String posiId) {
        initQtPatParams();
        qtpayApplication = new Param("application", "RequestPhp.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("posiId", posiId));
        httpsPost("RequestPhpTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                doAfterRequestCompleted(reqTag);
                doAfterRequestSuccess(reqTag, payResult);
            }

            @Override
            public void onTradeFailed() {
                super.onTradeFailed();
                doAfterRequestCompleted(reqTag);
            }

            @Override
            public void onOtherState(String rescode, String resDesc) {
                super.onOtherState(rescode, resDesc);
                doAfterRequestCompleted(reqTag);
            }
        });

    }

    private void doQueryUserCash(final String reqtag) {
        qtpayApplication.setValue("QueryUserCash.Req");
        qtpayAttributeList.add(qtpayApplication);
        httpsPost("getUserCrash", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                actiontype = CAN_WITHDRAW;
                doAfterRequestSuccess(reqtag, payResult);
            }

            @Override
            public void onOtherState() {
            }
        });
    }

    /**
     * 当前仅代表请求完成，结果未知
     *
     * @param reqTag
     */
    public void doAfterRequestCompleted(String reqTag) {
        if ("RequestPhp_home".equals(reqTag)) {
            if (homeFragment == null) {
                homeFragment = new HomeFragment_();
            }
            if (homeFragment.isVisible()) {
                ((HomeFragment_) homeFragment).getInstance().send(reqTag + "_Completed", null);
            }
        }
    }

    public void doAfterRequestSuccess(String reqTag, RyxPayResult qtpayResult) {
        if (reqTag.contains("3")) {

            if (centerFragment == null)
                centerFragment = new UserFragment_();

            if (centerFragment.isVisible()) {
                ((UserFragment_) centerFragment).getInstance().send(actiontype,
                        qtpayResult);
            }
        } else if (reqTag.contains("WithBankcard")) {

            if (centerFragment == null)
                centerFragment = new HomeFragment_();

            if (centerFragment.isVisible()) {
//                ((HomeFragment_) centerFragment).getInstance().send(actiontype,
//                        qtpayResult);
            }
        } else if (reqTag.contains("GetBankCardList") || reqTag.contains("GetAdvancedVipList")) {//绑定银行卡列表查看
            if (homeFragment == null)
                homeFragment = new HomeFragment_();
            LogUtil.showLog("ryx", "11111111111");
            if (homeFragment.isVisible()) {
                LogUtil.showLog("ryx", "222222222222");
            }
            LogUtil.showLog("ryx", "333333333333333");
        } else if (reqTag.contains("RequestPhp")) {
            if (reqTag.endsWith("_home")) {
                if (homeFragment == null) {
                    homeFragment = new HomeFragment_();
                }
                if (homeFragment.isVisible()) {
                    ((HomeFragment_) homeFragment).getInstance().send(reqTag, qtpayResult);
                }
            } else if (reqTag.endsWith("_bank")) {
                if (bankFragment == null) {
                    bankFragment = new SJFXhomeFragment_();
                }
                if (bankFragment.isVisible()) {
                    ((SJFXhomeFragment_) bankFragment).getInstance().send(reqTag, qtpayResult);
                }
            }
        } else if ("GetPublicNotice".equals(reqTag)) {
            if (homeFragment == null) {
                homeFragment = new HomeFragment_();
            }
            if (homeFragment.isVisible()) {
                ((HomeFragment_) homeFragment).getInstance().send(reqTag, qtpayResult);
            }
        } else if (reqTag.equals("Withdraw")) {
            if (homeFragment == null)
                homeFragment = new HomeFragment_();
            if (homeFragment.isVisible()) {
//                ((HomeFragment_) homeFragment).getInstance().send(actiontype,
//                        qtpayResult);
            }
        } else {
            if (homeFragment == null)
                homeFragment = new HomeFragment_();

            if (homeFragment.isVisible()) {
//                ((HomeFragment_) homeFragment).getInstance().send(actiontype,
//                        qtpayResult);
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ("member".equals(tagStr)) {
                if (memerFragment != null && memerFragment.isVisible()) {
                    if (((MemberFragment) memerFragment).onKeyDown(keyCode, event)) {
                        return true;
                    }
                }
            }


            if ((System.currentTimeMillis() - exitTime) > 2000) {
                LogUtil.showToast(MainFragmentActivity.this, getResources()
                        .getString(R.string.press_again));
                exitTime = System.currentTimeMillis();
                return true;
            } else {
                if (HttpUtil.checkNet(getApplicationContext())) {
                    qtpayApplication = new Param("application");
                    qtpayApplication.setValue("UserLoginExit.Req");
                    qtpayAttributeList.add(qtpayApplication);
                    httpsPost(false, true, "UserLoginExit", new XmlCallback() {
                        @Override
                        public void onTradeSuccess(RyxPayResult payResult) {
                            doExit();
                            RyxApplication.getInstance().exit();
                        }
                    });
                } else {
                    doExit();
                    RyxApplication.getInstance().exit();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
//        doExit();
        super.onDestroy();
    }

    public void doExit() {
        QtpayAppData.getInstance(MainFragmentActivity.this).setLogin(false);
        QtpayAppData.getInstance(MainFragmentActivity.this).setRealName("");
        QtpayAppData.getInstance(MainFragmentActivity.this).setMobileNo("");
        QtpayAppData.getInstance(MainFragmentActivity.this).setPhone("");
        QtpayAppData.getInstance(MainFragmentActivity.this).setCustomerId("");
        QtpayAppData.getInstance(MainFragmentActivity.this).setAuthenFlag(0);
        QtpayAppData.getInstance(MainFragmentActivity.this).setCustomerName("");
        QtpayAppData.getInstance(MainFragmentActivity.this).setToken("");
        SobotApi.exitSobotChat(MainFragmentActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.showLog("onActivityResult===MainfragmentActivity" + requestCode + "==" + resultCode);
        if (requestCode == RyxAppconfig.TOLOGINACT && resultCode == RyxAppconfig.LOGINACTFINISH) {
            if (homeFragment == null) {
                homeFragment = new HomeFragment_();
            }
            if (homeFragment.isVisible()) {
                ((HomeFragment_) homeFragment).getInstance().send("httpGetetAppCustomerInfo", null);
            }
            String loginMessage = data.getStringExtra("loginMessage");
            showSnackbar(loginMessage);
            LogUtil.showLog("MainFrag=onActivityResult==showSnakeMsg==" + data);
        } else if (requestCode == RyxAppconfig.CLOSE_HOMEFRG_REQ && resultCode == RyxAppconfig.CLOSE_ALL) {
            doDataRequest("mainFrg_change");
        } else if (requestCode == RyxAppconfig.CLOSE_HOMEFRG_REQ && resultCode == RyxAppconfig.CLOSE_NEWAUTHRESULT_RSP) {
            doDataRequest("toNewAuthResult");
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    /**
     * 读取本地已经存储的消息
     */
    private void getNoticeList() {
        ObjectInputStream in = null;
        try {
            InputStream is = openFileInput("notice_"
                    + RyxAppdata.getInstance(MainFragmentActivity.this).getCustomerId()
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
        noticeTempOldList = DataUtil.removeDuplicate(noticeTempOldList);
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
    }

    /**
     * 展示公告信息框
     */
    public void checkMsgShowDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final List<MsgInfo> msgInfos = new ArrayList<>();
                getNoticeList();
                qtpayApplication = new Param("application", "GetPublicNotice.Req");
                qtpayAttributeList.add(qtpayApplication);
                qtpayParameterList.add(new Param("noticeCode", "0000"));
                qtpayParameterList.add(new Param("noticeType", "2"));
                qtpayParameterList.add(new Param("readFlag", "2"));
                qtpayParameterList.add(new Param("offset", "1"));
                httpsPost(false, false, "GetPublicNotice", new XmlCallback() {
                    @Override
                    public void onTradeSuccess(final RyxPayResult payResult) {
                        MainFragmentActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                analyzeNotices(payResult.getData());
                                if(noticeList.size()==0){
                                    return;
                                }
                                msgInfos.addAll(noticeList);
                                if (msgInfos.size() == 0) {
                                    return;
                                }
                                LogUtil.showLog("msgInfos==" + msgInfos.toString());
                                if (ryxHomeMessageDialog == null) {
                                    ryxHomeMessageDialog = new RyxHomeMessageDialog(MainFragmentActivity.this);
                                    ryxHomeMessageDialog.show();
                                    ryxHomeMessageDialog.setRyxHomeMessageDialogListen(new RyxHomeMessageDialog.RyxHomeMessageDialogListen() {
                                        @Override
                                        public void updatePublicNoticePerson(final String codeId) {

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    updatePersonalMsg(codeId);
                                                }
                                            });
                                        }
                                    });
                                    ryxHomeMessageDialog.initMessageView(msgInfos);
                                } else {
                                    if (!ryxHomeMessageDialog.isShowing()) {
                                        ryxHomeMessageDialog.show();
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
    }
    private void updatePersonalMsg(String noticeCode) {
        qtpayApplication = new Param("application", "UpdatePublicNoticePerson.Req");
        Param qtpayNoticeCode = new Param("noticeCode");
        qtpayNoticeCode.setValue(noticeCode + "");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayNoticeCode);
        httpsPost("UpdatePublicNoticePerson", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {

            }
        });

    }
    private ArrayList<MsgInfo> noticeTempOldList = new ArrayList<MsgInfo>();
    private ArrayList<MsgInfo> noticeTempNewList = new ArrayList<MsgInfo>();
    protected ArrayList<MsgInfo> noticeList = new ArrayList<MsgInfo>();//通知列表
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
                        String popup=JsonUtil.getValueFromJSONObject(
                                noticeArray.getJSONObject(i), "popup");
                        msgInfo.setPopup(popup);
                        if(!("1".equals(popup))){
                            continue;
                        }
                        String readFlag = "";
                        if ("0".equals(noticeType)) {
                            //公共的
                            if (!hasNotice(noticeCode)) {
                                msgInfo.setReaded(false);
                                noticeTempNewList.add(msgInfo);
                            }
                        } else if ("1".equals(noticeType)) {
                            //个人
                            readFlag = JsonUtil.getValueFromJSONObject(
                                    noticeArray.getJSONObject(i), "readFlag");
//                            msgInfo.setReadFlag(readFlag);
//                            noticeTempNewList.add(msgInfo);
                            if("0".equals(readFlag)){
                                //未读
                                msgInfo.setReadFlag(readFlag);
                                msgInfo.setReaded(false);
                                noticeTempNewList.add(msgInfo);
                            }
                        }
                    }
                    noticeList.addAll(noticeTempNewList);
                    for (int i=0;i<noticeTempOldList.size();i++){
                     boolean isRead=   noticeTempOldList.get(i).isReaded();
                       String popup= noticeTempOldList.get(i).getPopup();
                        if(!isRead&&"1".equals(popup)){
                            noticeList.add(noticeTempOldList.get(i));
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                noticeData = null;
            }
        }
    }
    private boolean getReadState(String noticeCode) {
        int len = noticeTempOldList.size();
        for (int i = 0; i < len; i++) {
            if (noticeCode.equals(noticeTempOldList.get(i).getNoticeCode())) {
                return noticeTempOldList.get(i).isReaded();
            }
        }
        return false;
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

    /**
     * 底部展示SnakeBar提示上次登录时间信息
     */
    public void showSnackbar(String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if ("null".equals(content)) {
            return;
        }
        SnackbarUtil.LongSnackbarBtn(mMainTitle, content, "关闭", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
//        Snackbar.make(mMainTitle, content,Snackbar.LENGTH_LONG)
//                .setAction("关闭", new View.OnClickListener(){
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                })
//                .show();
    }

    private void showExitDialog() {
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(getContext(), new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                doExit();
            }

            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent("确认要安全退出吗？");
    }
}
