package com.ryx.payment.ruishua.recharge;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.Order;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.bindcard.adapter.DebitCardListAdapter;
import com.ryx.payment.ruishua.dialog.CreditDialog;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/***
 * 信用卡还款
 */
@EActivity(R.layout.activity_credit_card_list)
public class CreditCardListActivity extends BaseActivity {
    /** 绑定卡类型 */
    Param qtpayBindType;
    Param qtpayCardIdx;
    Param qtpayCardNum;

    @ViewById
    View  lay_instruction,lay_addcard;
    @ViewById
    SwipeMenuListView lv_debitcard;
    @Bean
    DebitCardListAdapter cardListAdapter;
    ArrayList<BankCardInfo> bankcardinfolist = new ArrayList<BankCardInfo>();
    @AfterViews
    public void initView(){
        setRightImageSrc(R.drawable.ic_add_icon_bg);
        setTitleLayout("信用卡还款",true);
        final Intent intent=new Intent(CreditCardListActivity.this,CreditCardAddActivity_.class);
        getRightImgView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        initQtPatParams();
        initMenuListView();
    }
    /**
     * 初始化网络请求参数
     */
    @Override
    public void initQtPatParams() {
        // TODO Auto-generated method stub
        super.initQtPatParams();
        qtpayBindType = new Param("bindType", "04"); // 04：信用卡还款
        qtpayCardNum = new Param("cardNum", "05");
    }

    @Override
    protected void onResume() {

        super.onResume();
        bankcardinfolist.clear();
        if (QtpayAppData.getInstance(CreditCardListActivity.this).isLogin()) {
            getCreditCardList();
        }
    }
    private void initMenuListView() {
        // 创建一个SwipeMenuCreator供ListView使用
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // 创建一个侧滑菜单
                SwipeMenuItem delItem = new SwipeMenuItem(getApplicationContext());
                // 给该侧滑菜单设置背景
                delItem.setBackground(new ColorDrawable(
                        ContextCompat.getColor(CreditCardListActivity.this,R.color.swipedelcolor)));
                // 设置宽度
                delItem.setWidth(dp2px(70));
                // 设置名称
                delItem.setTitle("删除");
                // 字体大小
                delItem.setTitleSize(15);
                // 字体颜色
                delItem.setTitleColor(Color.WHITE);
                // 加入到侧滑菜单中
                menu.addMenuItem(delItem);
            }
        };
        lv_debitcard.setMenuCreator(creator);
    }
    /**
     * 获取绑定的信用卡列表数据
     */
    public void getCreditCardList() {
        qtpayApplication = new Param("application", "GetBankCardList2.Req");
        qtpayCardIdx = new Param("cardIdx", "00");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayBindType);
        qtpayParameterList.add(qtpayCardIdx);
        qtpayParameterList.add(qtpayCardNum);
        httpsPost("GetBankCardList2Tag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
               String cardListJson = payResult.getData();
                resolveCreditCardList(cardListJson);
                LogUtil.showLog("bankcardinfolistSize=="+bankcardinfolist.size());
                cardListAdapter.setList(bankcardinfolist);
                cardListAdapter.notifyDataSetChanged();
                lv_debitcard.setAdapter(cardListAdapter);
                lv_debitcard.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(int position, SwipeMenu swipeMenu, int index) {
                        switch (index) {
                            case 0:
                                showDeleteDialog(position);
                                break;
                        }
                        return false;
                    }
                });

                lv_debitcard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        BankCardInfo   bankcardinfo = bankcardinfolist.get(position);
                        Intent  intent = new Intent(CreditCardListActivity.this, RepaymentActivity_.class);
                      OrderInfo orderinfo = Order.CREDIT_CARD_RECHARGE_TRUE_TIME;
                        orderinfo.setOrderDesc(bankcardinfolist.get(position)
                                .getAccountNo()); // 添加还款的卡号
                        orderinfo.setOrderRemark(bankcardinfo.getName() + ","
                                + bankcardinfo.getRepaydate());
                        intent.putExtra("bankCardInfo", bankcardinfo);
                        intent.putExtra("orderinfo", orderinfo);
                        startActivityForResult(intent,
                                RyxAppconfig.WILL_BE_CLOSED);
                    }
                });

            }
        });
    }
    //删除卡片
    public void showDeleteDialog(final int unBindCard)
    {
        CreditDialog dialog = new CreditDialog(R.style.SimpleDialogLight) {
            @Override
            public void positiveBtnClick() {
                closeDialog();
                unBindCard(unBindCard);
            }

            @Override
            public void nagtiveBtnClick() {
                closeDialog();
            }
        };
        dialog.setView(getSupportFragmentManager(), "确定删除该卡片，此操作不可逆！", "", "确认", "取消");
    }
    /**
     * 删除信用卡以后取消绑定
     */
    public void unBindCard(final int unbindpostion) {
        qtpayApplication = new Param("application", "BankCardUnBind.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayBindType);
        qtpayCardIdx = new Param("cardIdx", bankcardinfolist.get(unbindpostion)
                .getCardIdx());
        qtpayParameterList.add(qtpayCardIdx);
        qtpayParameterList.add(qtpayCardNum);
        httpsPost("BankCardUnBindTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(CreditCardListActivity.this,
                        "删除了 " + bankcardinfolist.get(unbindpostion).getBankName()
                                + "的卡");
                bankcardinfolist.clear();
                getCreditCardList();
            }
        });
    }
    /**
     * 解析信用卡列表数据为list
     */
    public void resolveCreditCardList(String cardListJson) {
        String toastmsg = "";
        try {
            JSONObject jsonObj = new JSONObject(cardListJson);
            toastmsg = (String) jsonObj.getJSONObject("result").getString(
                    "message");
            if (RyxAppconfig.QTNET_SUCCESS.equals(jsonObj.getJSONObject(
                    "result").getString("resultCode"))) {
                // 解析信用卡信息
                JSONArray banks = jsonObj.getJSONArray("resultBean");
                if(banks.length()>0){
                    lay_instruction.setVisibility(View.VISIBLE);
                    lay_addcard.setVisibility(View.GONE);
                }else{
                    lay_instruction.setVisibility(View.INVISIBLE);
                    lay_addcard.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < banks.length(); i++) {
                    BankCardInfo  bankcardinfo = new BankCardInfo();
                    bankcardinfo.setBankCity(JsonUtil.getValueFromJSONObject(
                            banks.getJSONObject(i), "bankCity"));
                    bankcardinfo.setRemark(JsonUtil.getValueFromJSONObject(
                            banks.getJSONObject(i), "remark"));
                    bankcardinfo.setBankProvinceId(JsonUtil
                            .getValueFromJSONObject(banks.getJSONObject(i),
                                    "bankProvinceId"));
                    bankcardinfo.setAccountNo(JsonUtil.getValueFromJSONObject(
                            banks.getJSONObject(i), "accountNo"));
                    bankcardinfo.setBankName(JsonUtil.getValueFromJSONObject(
                            banks.getJSONObject(i), "bankName"));
                    bankcardinfo.setBankProvince(JsonUtil
                            .getValueFromJSONObject(banks.getJSONObject(i),
                                    "bankProvince"));
                    bankcardinfo.setBankId(JsonUtil.getValueFromJSONObject(
                            banks.getJSONObject(i), "bankId"));
                    bankcardinfo.setFlagInfo(JsonUtil.getValueFromJSONObject(
                            banks.getJSONObject(i), "flagInfo"));
                    bankcardinfo.setBankCityId(JsonUtil
                            .getValueFromJSONObject(banks.getJSONObject(i),
                                    "bankCityId"));
                    bankcardinfo.setCardIdx(JsonUtil.getValueFromJSONObject(
                            banks.getJSONObject(i), "cardIdx"));
                    bankcardinfo.setName(JsonUtil.getValueFromJSONObject(
                            banks.getJSONObject(i), "name"));
                    bankcardinfo.setBranchBankId(JsonUtil
                            .getValueFromJSONObject(banks.getJSONObject(i),
                                    "branchBankId"));
                    bankcardinfo.setBranchBankName(JsonUtil
                            .getValueFromJSONObject(banks.getJSONObject(i),
                                    "branchBankName"));

                    bankcardinfolist.add(bankcardinfo);

                }
            } else {
                LogUtil.showToast(CreditCardListActivity.this, toastmsg);
                lay_instruction.setVisibility(View.INVISIBLE);
                lay_addcard.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    // 将dp转化为px
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
