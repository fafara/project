package com.ryx.payment.ruishua.usercenter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.convenience.Swiper_;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.usercenter.adapter.DeViceExpanListViewAdapter;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的设备列表
 */
@EActivity(R.layout.activity_de_vice_list)
public class DeViceListActivity extends BaseActivity {
@ViewById
    ExpandableListView expandableListView;
    @ViewById
    AutoLinearLayout lay_top;
    DeViceExpanListViewAdapter deViceExpanListViewAdapter;
    List<Map> groupList=new ArrayList<>();
    List<Map> childList=new ArrayList<>();
    private int TOSWIPERREQ=0x1704;
    private int TODEVADDREQ=0x1705;
    private int TOACTIVATIONDEV=0x1706;
    private int TOTERPLEDGERULE=0x1707;//去设备规则展示页面
    ImageView imgView;
    @AfterViews
    public void initView(){
        setTitleLayout("设备列表",true,true);
         imgView=getRightImgView();
        imgView.setImageResource(R.drawable.ic_add_icon_bg);
        imgView.setOnClickListener(new NoDoubleClickListener(){
            @Override
            public void onNoDoubleClick(View v) {
                toSwiper();
            }
        });
        initQtPatParams();
        Intent intent=getIntent();
        if(intent!=null&&"banding".equals(intent.getStringExtra("flag"))){
            toSwiper();
        }else{
            getData();
        }
    }

    /**
     * 初始化PopWindow
     */
//    private void initRyxPoOWindow() {
//        PopModel feedPopModel = new PopModel();
//        feedPopModel.setDrawableId(R.drawable.drawable_checked);
//        feedPopModel.setItemDesc("设备激活");
//
//        PopModel messagePopMode = new PopModel();
//        //如果设置了图标，则会显示，否则不显示
//        messagePopMode.setDrawableId(R.drawable.drawable_checked);
//        messagePopMode.setItemDesc("设备绑定");
//
//        /** 初始化数据源 **/
//        final List<PopModel> list = new ArrayList<>();
//        list.add(feedPopModel);
//        list.add(messagePopMode);
//
//        PopCommon popCommon = new PopCommon(this, list, new PopCommon.OnPopCommonListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Toast.makeText(DeViceListActivity.this, "点击了：" + list.get(position).getItemDesc(), Toast.LENGTH_LONG).show();
//                if(position==0){
//                    //设备激活
//                    toActivationDev();
//                }else{
//                    //设备绑定
//                    toSwiper();
//                }
//            }
//
//            @Override
//            public void onDismiss() {
//
//            }
//        });
//        /** 是否显示黑色背景，默认不显示 **/
//        popCommon.showPop(imgView, DisplayUtil.dp2px(getApplicationContext(), 5), imgView.getHeight() / 4 * 5);
//
//    }

//    /**
//     * 激活设备
//     */
//    private void toActivationDev() {
//        Intent swiperIntent=new Intent(DeViceListActivity.this, Swiper_.class);
//        swiperIntent.putExtra("ActionType","DEVBINDING");
//        startActivityForResult(swiperIntent,TOACTIVATIONDEV);
//
//    }

    /**
     * 绑定设备或者激活设备
     */
    private void toSwiper(){
        Intent swiperIntent=new Intent(DeViceListActivity.this, Swiper_.class);
        swiperIntent.putExtra("ActionType","DEVBINDING");
        startActivityForResult(swiperIntent,TOSWIPERREQ);
    }
    private void initExpandaView(){
        deViceExpanListViewAdapter=new DeViceExpanListViewAdapter(this,groupList,childList);
        expandableListView.setAdapter(deViceExpanListViewAdapter);
//        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                if (parent.isGroupExpanded(groupPosition)) {
//                    parent.collapseGroup(groupPosition);
//                } else {
//                    //第二个参数false表示展开时是否触发默认滚动动画
//                    parent.expandGroup(groupPosition, false);
//                }
//                return true;
//            }
//        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                parent.collapseGroup(groupPosition);
                return true;
            }
        });
    }
    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
    }
    void getData()
    {
        qtpayApplication = new Param("application",
                "FindTermLs.Req");
        qtpayAttributeList.add(qtpayApplication);
        httpsPost("getDataTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {

              String  jsondata=payResult.getData();
                try {
                    JSONObject dataObj=new JSONObject(jsondata);
                   JSONArray resultBeanArry =dataObj.getJSONArray("resultBean");
                    groupList.clear();
                    childList.clear();
                    for(int i=0;i<resultBeanArry.length(); i++){
                        JSONObject devInforObj=   resultBeanArry.getJSONObject(i);
                      String psamid=  devInforObj.getString("psamid");
                      String terminid=  devInforObj.getString("terminalid");
                      String status=  devInforObj.getString("status");

                        StringBuffer stringBuffer=new StringBuffer();
                        if (terminid.length() > 6 ) {
                            if (terminid.subSequence(4, 6).equals("11")) {
                                stringBuffer.append("音频点付宝");
                            } else if (terminid.subSequence(4, 6).equals("17") || terminid.subSequence(4, 6).equals("41")) {
                                stringBuffer.append("音频POS打印机");
                            } else if (terminid.subSequence(4, 6).equals("29") || terminid.subSequence(4, 6).equals("30")) {
                                stringBuffer.append("蓝牙POS打印机");
                            } else if (terminid.subSequence(4, 6).equals("59")) {
                                stringBuffer.append("蓝牙点付宝");
                            } else if (terminid.contains("3150007") || terminid.contains("92782307")) {
                                stringBuffer.append("迷你手机IC刷卡器");
                            } else if (terminid.contains("8002887100")) {
                                stringBuffer.append("二维码");
                            } else {
                                stringBuffer.append("迷你手机刷卡器");
                            }
                            Map groupDataMap = new HashMap();
                            groupDataMap.put("devname", stringBuffer.toString());
                            groupDataMap.put("devid", psamid);
                            groupList.add(groupDataMap);
                            Map childDataMap=new HashMap();
                            if("0".equals(status)){
                             JSONArray feeinfoArray= devInforObj.getJSONArray("feeinfo");
                                for(int j=0;j<feeinfoArray.length(); j++){
//                                    终端类型 01 刷卡器 02 二维码
//                                    0002000043	0000000000  --T0结算  0002000043	0000000000 01
//                                    0007000004	0007000000  --支付宝   0007000004	0007000000 01和02
//                                    0007000003	0007000000  --微信        0007000003	0007000000 01和02
//                                    0002000043	0000000001  --普通结算    0002000043	0000000001 01
//                                    0007000005  0007000000 快捷支付     0007000005  0007000000 01和02
                                    JSONObject  feeinfoObject=   feeinfoArray.getJSONObject(j);
                                    String typestr=feeinfoObject.getString("type");
                                    if("0002000043000000000001".equals(typestr)){
                                        StringBuffer feeinfosB=new StringBuffer("闪付刷卡器");
                                        childDataMap.put("layout2text1",feeinfosB);
                                        getFreeInfoStr(childDataMap,feeinfoObject,"2");
                                    }else if("0007000004000700000001".equals(typestr)){
                                        StringBuffer feeinfosB= new StringBuffer("支付宝刷卡器");
                                        childDataMap.put("layout3text1",feeinfosB);
                                        getFreeInfoStr(childDataMap,feeinfoObject,"3");
                                    }else if("0007000004000700000002".equals(typestr)){
                                        StringBuffer feeinfosB= new StringBuffer("支付宝扫码");
                                        childDataMap.put("layout4text1",feeinfosB);
                                        getFreeInfoStr(childDataMap,feeinfoObject,"4");
                                    }else if("0007000003000700000001".equals(typestr)){
                                        StringBuffer feeinfosB= new StringBuffer("微信刷卡器");
                                        childDataMap.put("layout5text1",feeinfosB);
                                        getFreeInfoStr(childDataMap,feeinfoObject,"5");
                                    }else if("0007000003000700000002".equals(typestr)){
                                        StringBuffer feeinfosB= new StringBuffer("微信扫码");
                                        childDataMap.put("layout6text1",feeinfosB);
                                        getFreeInfoStr(childDataMap,feeinfoObject,"6");
                                    }else if("0002000043000000000101".equals(typestr)){
                                        StringBuffer feeinfosB= new StringBuffer("刷卡器普通结算");
                                        childDataMap.put("layout7text1",feeinfosB);
                                        getFreeInfoStr(childDataMap,feeinfoObject,"7");
                                    }else if("0007000005000700000001".equals(typestr)){
                                        StringBuffer feeinfosB= new StringBuffer("快捷支付刷卡器");
                                        childDataMap.put("layout8text1",feeinfosB);
                                        getFreeInfoStr(childDataMap,feeinfoObject,"8");
                                    }else if("0007000005000700000002".equals(typestr)){
                                        StringBuffer feeinfosB= new StringBuffer("快捷支付扫码");
                                        childDataMap.put("layout9text1",feeinfosB);
                                        getFreeInfoStr(childDataMap,feeinfoObject,"9");
                                    }
                                }
                            }else{

                                String msg= JsonUtil.getValueFromJSONObject(devInforObj,"msg");
                                childDataMap.put("msg","<font color='#000'>"+msg+"</font>");
                            }
                            childDataMap.put("status",status);
                            childList.add(childDataMap);
                        }}
                    myinitView();
                } catch (Exception e) {
                    LogUtil.showToast(DeViceListActivity.this,"数据异常,请稍后再试");
                    e.printStackTrace();
                    expandableListView.setVisibility(View.GONE);
                    lay_top.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTradeFailed() {
                super.onTradeFailed();
                expandableListView.setVisibility(View.GONE);
                lay_top.setVisibility(View.VISIBLE);
            }

            @Override
            public void onOtherState() {
                super.onOtherState();
                expandableListView.setVisibility(View.GONE);
                lay_top.setVisibility(View.VISIBLE);
            }
        });
    }

    public void  getFreeInfoStr(Map map,JSONObject feeinfoObject,String number){
        try {
            String feelowstr=feeinfoObject.getString("feelow");
            String feehighstr=feeinfoObject.getString("feehigh");
            String feefixedstr=feeinfoObject.getString("feefixed");
            String feeratestr=feeinfoObject.getString("feerate");
            try {
                Double  feefixedDouMoney= Double.parseDouble(feefixedstr)/100;
                Double  feerateDouMoney= Double.parseDouble(feeratestr)/100;
                map.put("layout"+number+"text2",feerateDouMoney+"%"+"+"+feefixedDouMoney);
            }catch (Exception e){
                map.put("layout"+number+"text2","--");
            }
            try {
                Double  feelowDouMoney= Double.parseDouble(feelowstr)/100;
                map.put("layout"+number+"text3",feelowDouMoney+"");
            }catch (Exception e){
                map.put("layout"+number+"text3","--");
            }
            try {
                Double  feehighDouMoney= Double.parseDouble(feehighstr)/100;
                map.put("layout"+number+"text4",feehighDouMoney+"");
            }catch (Exception e){
                map.put("layout"+number+"text4","--");
            }
        }catch (Exception e){
        }

    }
    private void myinitView(){
        if(groupList.size()==0){
            expandableListView.setVisibility(View.GONE);
            lay_top.setVisibility(View.VISIBLE);
        }else{
            expandableListView.setVisibility(View.VISIBLE);
            lay_top.setVisibility(View.GONE);
            initExpandaView();
        }
    }
    private void initData(){
        Map groupDataMap=new HashMap();
            groupDataMap.put("devname","联迪小蓝牙");
            groupDataMap.put("devid","222222222222222");
        Map groupDataMap2=new HashMap();
        groupDataMap2.put("devname","绿地小蓝牙");
        groupDataMap2.put("devid","3333333333333");
        Map groupDataMap3=new HashMap();
        groupDataMap3.put("devname","动联小蓝牙");
        groupDataMap3.put("devid","44444444444444444444");
        Map groupDataMap4=new HashMap();
        groupDataMap4.put("devname","科普小蓝牙");
        groupDataMap4.put("devid","55555555555555555");
        groupList.add(groupDataMap);
        groupList.add(groupDataMap2);
        groupList.add(groupDataMap3);
        groupList.add(groupDataMap4);

        for (int i = 0; i < groupList.size(); i++) {
                Map childDataMap=new HashMap();
                 childDataMap.put("textview1","1扫码支付：交易续费0.89%+2,最低限额2元,最大单笔消费金额2000元.");
                 childDataMap.put("textview2","1刷卡消费:交易续费0.65%+3,最低限额10元,最大单笔消费金额2000元.");
                 childDataMap.put("textview3","1增值业务消费:交易续费0.65%+3,最低限额10元,最大单笔消费金额2000元.");
                 childDataMap.put("textview4","1提现交易:交易续费0.72%+3,最低限额1元,最大单笔消费金额2000元.");
            childList.add(childDataMap);


            List<Map> childListMap1=new ArrayList<>();
            Map childDataMap1=new HashMap();
            childDataMap1.put("textview1","2扫码支付：交易续费0.89%+2,最低限额2元,最大单笔消费金额2000元.");
            childDataMap1.put("textview2","2提现交易:交易续费0.72%+3,最低限额1元,最大单笔消费金额2000元.");
            childDataMap1.put("textview3","2增值业务消费:交易续费0.65%+3,最低限额10元,最大单笔消费金额2000元.");
            childDataMap1.put("textview4","2刷卡消费:交易续费0.65%+3,最低限额10元,最大单笔消费金额2000元.");
            childList.add(childDataMap1);

            Map childDataMap2=new HashMap();
            childDataMap2.put("textview1","3扫码支付：交易续费0.89%+2,最低限额2元,最大单笔消费金额2000元.");
            childDataMap2.put("textview2","3扫码支付：交易续费0.89%+2,最低限额2元,最大单笔消费金额2000元.");
            childDataMap2.put("textview3","3扫码支付：交易续费0.89%+2,最低限额2元,最大单笔消费金额2000元.");
            childDataMap2.put("textview4","3扫码支付：交易续费0.89%+2,最低限额2元,最大单笔消费金额2000元.");
            childList.add(childDataMap2);

            Map childDataMap3=new HashMap();
            childDataMap3.put("textview1","4扫码支付：交易续费0.89%+2,最低限额2元,最大单笔消费金额2000元.");
            childDataMap3.put("textview2","4扫码支付：交易续费0.89%+2,最低限额2元,最大单笔消费金额2000元.");
            childDataMap3.put("textview3","4扫码支付：交易续费0.89%+2,最低限额2元,最大单笔消费金额2000元.");
            childDataMap3.put("textview4","4扫码支付：交易续费0.89%+2,最低限额2元,最大单笔消费金额2000元.");
            childList.add(childDataMap3);

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==TOSWIPERREQ&&resultCode==TOSWIPERREQ){
            //刷卡设备返回信息
          String ic_ksn=  data.getStringExtra("ic_ksn");
            String psamid="";
            String termid="";
                if(ic_ksn!=null&&ic_ksn.length()==36){
                    termid=ic_ksn.substring(0, 20);
                    psamid=ic_ksn.substring(20);
                    dialogShow("TermPledgeRule","确定绑定"+psamid+"设备吗?",psamid,termid);
                }else{
                    dialogShow("TermPledgeRule","设备信息获取失败!",null,null);
                }
        }
//        else if(requestCode==TOACTIVATIONDEV&&resultCode==TOSWIPERREQ){
//            String ic_ksn=  data.getStringExtra("ic_ksn");
////            termPledgeRule(ic_ksn);
//            String psamid="";
//            String termid="";
//            if(ic_ksn!=null&&ic_ksn.length()==36){
//                termid=ic_ksn.substring(0, 20);
//                psamid=ic_ksn.substring(20);
//                dialogShow("TermPledgeRule","确定要激活"+psamid+"设备吗?",psamid,termid);
//            }else{
//                dialogShow("TermPledgeRule","设备信息获取失败!",null,null);
//            }
//        }
        else{
            getData();
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void dialogShow(final String tag, String content, final String psamid, final String termid){
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(DeViceListActivity.this, new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                if(!TextUtils.isEmpty(psamid)){
                   if("TermPledgeRule".equals(tag)){
                        termPledgeRule(psamid,termid);
                    }
                }
            }
            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                getData();
            }
        });
        ryxSimpleConfirmDialog.show();
        if(TextUtils.isEmpty(psamid)){
            ryxSimpleConfirmDialog.setOnlyokLinearlayout();
        }
        ryxSimpleConfirmDialog.setContent(content);
    }

    /**
     * 绑定设备(已经废弃)
     * @deprecated
     */
    private void terminalBind(String psamId,String termid){
        qtpayApplication.setValue("TerminalBind.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(new Param("psamid",psamId));
        qtpayParameterList.add(new Param("termId",termid));
        httpsPost("TerminalBindTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                 getData();
            }

            @Override
            public void onOtherState() {
                super.onOtherState();
                getData();
            }

            @Override
            public void onTradeFailed() {
                super.onTradeFailed();
                getData();
            }
        });

    }


    /**
     * 获取设备激活规则
     */
    private void termPledgeRule(String psamid,String termId){
            qtpayApplication.setValue("TermPledgeRule.Req");
            qtpayAttributeList.add(qtpayApplication);
            qtpayParameterList.add(new Param("psamid",psamid));
            qtpayParameterList.add(new Param("termId",termId));
            httpsPost("TermPledgeRuleTag", new XmlCallback() {
                @Override
                public void onTradeSuccess(RyxPayResult payResult) {
                    String reaultdata=payResult.getData();
                    try {
                        JSONObject jsonObject=new JSONObject(reaultdata);
                       String isbind= JsonUtil.getValueFromJSONObject(jsonObject,"isbind");

                        if("5000".equals(isbind)){
                            Intent intent=new Intent(DeViceListActivity.this,TermPledgeRuleActivity_.class);
                            intent.putExtra("termRules",payResult.getData());
                            startActivityForResult(intent,TOTERPLEDGERULE);
                        }else{
                            //0000的代表绑定成功
                            String bindresult= JsonUtil.getValueFromJSONObject(jsonObject,"bindresult");
                            LogUtil.showToast(DeViceListActivity.this,bindresult);
                            getData();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.showToast(DeViceListActivity.this,"数据错误！");
                    }
                }
                @Override
                public void onOtherState() {
                    super.onOtherState();
                }

                @Override
                public void onTradeFailed() {
                    super.onTradeFailed();
                }
            });


    }
}
