package com.ryx.payment.ruishua.bindcard;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.bindcard.adapter.DebitCardListAdapter;
import com.ryx.payment.ruishua.dialog.CreditDialog;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 绑定银行卡功能列表页
 */
@EActivity(R.layout.activity_binded_debit_card_list)
public class BindedDebitCardListActivity extends BaseActivity {

    @ViewById(R.id.tilerightImg)
    ImageView tilerightImg;
    @ViewById(R.id.img_no_item)
    ImageView img_no_item;

    @ViewById(R.id.lay_instruction)
    AutoRelativeLayout lay_instruction;
    @ViewById(R.id.lay_addcard)
    AutoLinearLayout lay_addcard;


    @ViewById(R.id.lv_debitcard)
    SwipeMenuListView lv_debitcard;

    /**
     * 绑定卡类型
     */
    Param qtpayBindType;
    Param qtpayCardIdx;
    Param qtpayCardNum;
    @Bean
    DebitCardListAdapter cardListAdapter;

    ArrayList<BankCardInfo> bankcardlist = new ArrayList<BankCardInfo>();
    String banklistJson; // 接收查询返回的 银行列表json数据
    BankCardInfo bankCardInfo = null;
    boolean hasBound = false;
    private int currentpostion = -1;
    private int ADDCARDINFO = 1;
    Param qtpayAccountNo;

    @AfterViews
    public void initViews() {
        setTitleLayout("绑定银行卡", true, true);
        tilerightImg.setBackgroundResource(R.drawable.ic_add_icon_bg);
        lay_instruction.setVisibility(View.GONE);
        initQtPatParams();
        initMenuListView();
    }

    private void initMenuListView() {


        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                switch (menu.getViewType()) {
                    case 0:
                        //不是默认
                        // 创建一个侧滑菜单
                        SwipeMenuItem setitem = new SwipeMenuItem(getApplicationContext());
                        // 给该侧滑菜单设置背景
                        setitem.setBackground(new ColorDrawable(
                                ContextCompat.getColor(BindedDebitCardListActivity.this,R.color.blue_second)));
                        // 设置宽度
                        setitem.setWidth(dp2px(70));
                        // 设置名称
                        setitem.setTitle("设置默认");
                        // 字体大小
                        setitem.setTitleSize(15);
                        // 字体颜色
                        setitem.setTitleColor(Color.WHITE);
                        menu.addMenuItem(setitem);
                        break;
                    case 1:
                        //默认
                        //不是默认
                        // 创建一个侧滑菜单
                        SwipeMenuItem setitem1 = new SwipeMenuItem(getApplicationContext());
                        // 给该侧滑菜单设置背景
                        setitem1.setBackground(new ColorDrawable(
                                ContextCompat.getColor(BindedDebitCardListActivity.this,R.color.blue_second)));
                        // 设置宽度
                        setitem1.setWidth(dp2px(70));
                        // 设置名称
                        setitem1.setTitle("取消默认");
                        // 字体大小
                        setitem1.setTitleSize(15);
                        // 字体颜色
                        setitem1.setTitleColor(Color.WHITE);
                        menu.addMenuItem(setitem1);
                        break;
                }
                // 创建一个侧滑菜单
                SwipeMenuItem delItem = new SwipeMenuItem(getApplicationContext());
                // 给该侧滑菜单设置背景
                delItem.setBackground(new ColorDrawable(
                        ContextCompat.getColor(BindedDebitCardListActivity.this,R.color.swipedelcolor)));
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

    @Override
    protected void onResume() {
        super.onResume();
        bankcardlist.clear();
        if (QtpayAppData.getInstance(BindedDebitCardListActivity.this).isLogin()) {
            getDebitCardList();
        }
    }

    /**
     * 初始化网络请求参数
     */
    @Override
    public void initQtPatParams() {
        super.initQtPatParams();
        qtpayApplication = new Param("application");
        qtpayCardIdx = new Param("cardIdx");
    }

    /**
     * 获取绑定的卡列表
     */
    public void getDebitCardList() {
        qtpayBindType = new Param("bindType", "01");
        qtpayCardNum = new Param("cardNum", "05");
        qtpayCardIdx.setValue("00");
        qtpayApplication.setValue("GetBankCardList2.Req");

        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayBindType);
        qtpayParameterList.add(qtpayCardIdx);
        qtpayParameterList.add(qtpayCardNum);
        httpsPost("GetBankCardList2", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                showBindList(payResult.getData());
            }
        });
    }

    /**
     * 初始化银行卡列表
     */
    public void initListData() {
        try {
            JSONObject jsonObj = new JSONObject(banklistJson);
            if ("0000".equals(jsonObj.getJSONObject("result").getString("resultCode"))) {
                // 解析银行卡信息
                JSONArray banks = jsonObj.getJSONArray("resultBean");
                for (int i = 0; i < banks.length(); i++) {
                    bankCardInfo = new BankCardInfo();
                    bankCardInfo.setBankCity(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankCity"));
                    bankCardInfo.setRemark(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "remark"));
                    bankCardInfo.setBankProvinceId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankProvinceId"));
                    bankCardInfo.setAccountNo(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "accountNo"));
                    bankCardInfo.setBankName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankName"));
                    bankCardInfo.setBankProvince(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankProvince"));
                    bankCardInfo.setBankId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankId"));
                    //flagInfo 默认结算卡标志 1默认
                    bankCardInfo.setFlagInfo(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "flagInfo"));
                    bankCardInfo.setBankCityId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankCityId"));
                    bankCardInfo.setCardIdx(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "cardIdx"));
                    bankCardInfo.setName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "name"));
                    bankCardInfo.setBranchBankId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "branchBankId"));
                    bankCardInfo.setBranchBankName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "branchBankName"));
//                    if ("01".equals(bankCardInfo.getFlagInfo())) {
//                        hasBound = true;
//                        lv_debitcard.setBindPosition(i);
//                    }
                    bankcardlist.add(bankCardInfo);
                }

                cardListAdapter.setList(bankcardlist);
                cardListAdapter.notifyDataSetChanged();
                lv_debitcard.setAdapter(cardListAdapter);
                lv_debitcard.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(int position, SwipeMenu swipeMenu, int index) {
                        switch (index) {
                            case 0:
                                if ("1".equals(bankcardlist.get(position).getFlagInfo())) {
                                    currentpostion = position;
                                    cancleDefaultCardDialog();
                                } else {
                                    currentpostion = position;
                                    setDefaultCardDialog();
                                }
                                break;
                            case 1:
                                currentpostion = position;
                                showDeleteDialog();
                                break;
                        }
                        return false;
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showBoundCardDialog() {
        CreditDialog dialog = new CreditDialog(R.style.SimpleDialogLight) {
            @Override
            public void positiveBtnClick() {
                closeDialog();
                doBankCardBindTX();
            }

            @Override
            public void nagtiveBtnClick() {
                closeDialog();
            }
        };
        String msg = "你正设置" + bankCardInfo.getBankName() + "账户" + bankCardInfo.getAccountNo();
        dialog.setView(getSupportFragmentManager(), msg, getResources().getString(R.string.binding_ach_card), "确认", "取消");
    }

    /**
     * 银行卡自动清算卡绑定
     */
    public void doBankCardBindTX() {
        qtpayAccountNo = new Param("accountNo", bankCardInfo.getAccountNo());
        qtpayCustomerId = new Param("customerId", QtpayAppData.getInstance(BindedDebitCardListActivity.this).getCustomerId());
        qtpayApplication.setValue("BankCardBindTX.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayAttributeList.add(qtpayCustomerId);
        qtpayParameterList.add(qtpayAccountNo);
        httpsPost("BankCardBindTX", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                bankcardlist.get(currentpostion).setFlagInfo("01");
                hasBound = true;
                //lv_debitcard.setBindPosition(currentpostion);
                cardListAdapter.notifyDataSetChanged();
                LogUtil.showToast(BindedDebitCardListActivity.this, payResult.getRespDesc());
            }
        });
    }
    //取消默认卡
    public void cancleDefaultCardDialog() {
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(this, new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                canceltDefaultCard();
            }

            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent("确定要取消默认结算卡吗！");
    }
    //设置为默认卡
    public void setDefaultCardDialog() {
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(this, new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                setDefaultCard();
            }

            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent("确定要设置为默认结算卡吗！");
    }
    //删除卡片
    public void showDeleteDialog() {
        RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(this, new ConFirmDialogListener() {

            @Override
            public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
                unBindCard();
            }

            @Override
            public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
                ryxSimpleConfirmDialog.dismiss();
            }
        });
        ryxSimpleConfirmDialog.show();
        ryxSimpleConfirmDialog.setContent("确定删除该卡片，此操作不可逆！");
    }
    /**
     * 取消默认银行卡
     */
    private void canceltDefaultCard() {
        qtpayCardIdx.setValue(bankcardlist.get(currentpostion).getCardIdx());
        ;
        qtpayApplication.setValue("BankCardSetDefault.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayCardIdx);
        qtpayParameterList.add( new Param("bindType", "0"));
        httpsPost("BankCardSetDefaultTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(BindedDebitCardListActivity.this, payResult.getRespDesc());
                bankcardlist.clear();
                getDebitCardList();
            }
        });
    }
    /**
     * 设置为默认银行卡
     */
    private void setDefaultCard() {
        qtpayCardIdx.setValue(bankcardlist.get(currentpostion).getCardIdx());
        ;
        qtpayApplication.setValue("BankCardSetDefault.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayCardIdx);
        qtpayParameterList.add( new Param("bindType", "1"));
        httpsPost("BankCardUnBind", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(BindedDebitCardListActivity.this, payResult.getRespDesc());
                bankcardlist.clear();
                getDebitCardList();
            }
        });
    }
    private void unBindCard() {
        qtpayCardIdx.setValue(bankcardlist.get(currentpostion).getCardIdx());
        qtpayApplication.setValue("BankCardUnBind.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayCardIdx);
        httpsPost("BankCardUnBind", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(BindedDebitCardListActivity.this, payResult.getRespDesc());
                bankcardlist.clear();
                getDebitCardList();
            }
        });
    }

    private void showBindList(String data) {
        banklistJson = data;
        initListData();
        initMode();
    }

    public void initMode() {
        if (bankcardlist.size() == 0) {
            lay_instruction.setVisibility(View.GONE);
            lv_debitcard.setVisibility(View.GONE);
            img_no_item.setVisibility(View.VISIBLE);
            lay_addcard.setVisibility(View.VISIBLE);
        } else {
//            if (PreferenceUtil.checkIsFirstLogin(BindedDebitCardListActivity.this,"FirstBankCard")) {
//                Intent intent = new Intent(BindedDebitCardListActivity.this,WithdrawImListFirstBankCardAdded.class);
//                startActivity(intent);
//            }
            lv_debitcard.setVisibility(View.VISIBLE);
            img_no_item.setVisibility(View.GONE);
            lay_instruction.setVisibility(View.VISIBLE);
            lay_addcard.setVisibility(View.GONE);
        }

    }

    @Click({R.id.img_no_item, R.id.tilerightImg})
    public void addCard() {
        Intent addintent = new Intent();
//        LogUtil.showLog("UserType = " + QtpayAppData.getInstance(BindedDebitCardListActivity.this).getUserType());
//        if (QtpayAppData.getInstance(BindedDebitCardListActivity.this).getUserType().equals("01")) {
////            addintent = new Intent(BindedDebitCardListActivity.this, WithdrawMerchantInfoAdd.class);
////            startActivityForResult(addintent, ADDCARDINFO);
//        } else if (QtpayAppData.getInstance(BindedDebitCardListActivity.this).getUserType().equals("00")) {
            addintent = new Intent(BindedDebitCardListActivity.this, BindDebitCardActivity_.class);
            startActivityForResult(addintent, ADDCARDINFO);
//        }
    }

    @Click(R.id.tileleftImg)
    public void closeWindow() {
        finish();
    }

    @Click(R.id.close_instruction)
    public void closeinstruct() {
        lay_instruction.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == ADDCARDINFO) {
                BindedDebitCardListActivity.this.finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // 将dp转化为px
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
