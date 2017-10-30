package com.ryx.payment.ruishua.bindcard;

import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.BankCardInfo;
import com.ryx.payment.ruishua.bean.Param;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.bindcard.adapter.BindedCardListAdapter;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 卡管理列表页
 */
@EActivity(R.layout.activity_binded_card_list)
public class BindedCardListActivity extends BaseActivity {
    /**
     * 绑定卡类型
     */
//    Param qtpayBindType;
    Param qtpayCardIdx;
//    Param qtpayCardNum;
    @ViewById(R.id.lv_bindedcard)
    SwipeMenuListView lv_bindedcard;
    ArrayList<BankCardInfo> bankcardlist = new ArrayList<BankCardInfo>();
    String banklistJson; // 接收查询返回的 银行列表json数据
    BankCardInfo bankCardInfo = null;
    @Bean
    BindedCardListAdapter cardListAdapter;
    private int currentpostion = -1;
    @ViewById
    AutoLinearLayout lay_binded_addcard;
    @ViewById
    ImageView img_no_item;
    @ViewById(R.id.bindcard_lay_instruction)
    AutoRelativeLayout lay_instruction;
@AfterViews
    public void afterInitView()
    {
        setRightImageSrc(R.drawable.ic_add_icon_bg);
        setTitleLayout("我的银行卡", true, true);
        ImageView imageView= getRightImgView();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BindedCardListActivity.this,BankCardAddActivity_.class));
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
        super.initQtPatParams();
        qtpayApplication = new Param("application");
        qtpayCardIdx = new Param("cardIdx");
    }

    @Override
    protected void onResume() {
        super.onResume();
        bankcardlist.clear();
        if (QtpayAppData.getInstance(BindedCardListActivity.this).isLogin()) {
            getDebitCardList();
        }
    }
    private void initMenuListView() {
        //左滑删除屏蔽
//        SwipeMenuCreator creator = new SwipeMenuCreator() {
//            @Override
//            public void create(SwipeMenu menu) {
//                // 创建一个侧滑菜单
//                SwipeMenuItem delItem = new SwipeMenuItem(getApplicationContext());
//                // 给该侧滑菜单设置背景
//                delItem.setBackground(new ColorDrawable(
//                        ContextCompat.getColor(BindedCardListActivity.this,R.color.swipedelcolor)));
//                // 设置宽度
//                delItem.setWidth(dp2px(70));
//                // 设置名称
//                delItem.setTitle("删除");
//                // 字体大小
//                delItem.setTitleSize(15);
//                // 字体颜色
//                delItem.setTitleColor(Color.WHITE);
//                // 加入到侧滑菜单中
//                menu.addMenuItem(delItem);
//            }
//
//        };
//        lv_bindedcard.setMenuCreator(creator);
        lv_bindedcard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             BankCardInfo bankCardInfo=   bankcardlist.get(position);
                Intent intent=   new Intent(BindedCardListActivity.this,MyBindCardItemActivity_.class);
                intent.putExtra("bankObj",bankCardInfo);
                startActivity(intent);
            }
        });
    }
    // 将dp转化为px
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    /**
     * 获取绑定的卡列表
     */
    public void getDebitCardList() {
//        qtpayBindType = new Param("bindType", "01");
//        qtpayCardNum = new Param("cardNum", "05");
//        qtpayCardIdx.setValue("00");
        qtpayApplication.setValue("BindCardList.Req");

        qtpayAttributeList.add(qtpayApplication);
//        qtpayParameterList.add(qtpayBindType);
//        qtpayParameterList.add(qtpayCardIdx);
//        qtpayParameterList.add(qtpayCardNum);
        httpsPost("BindCardListTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                showBindList(payResult.getData());
            }
        });
    }
    private void showBindList(String data) {
        banklistJson = data;
        initListData();
        initMode();
    }
    /**
     * 初始化银行卡列表
     */
    public void initListData() {
        try {
            JSONObject jsonObj = new JSONObject(banklistJson);
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
//                    bankCardInfo.setBankCity(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankcity"));
//                    bankCardInfo.setRemark(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "remark"));
//                    bankCardInfo.setBankProvinceId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankProvinceId"));
//
//
//                    bankCardInfo.setBankProvince(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankProvince"));
//
//
//                    bankCardInfo.setBankCityId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "bankCityId"));
//
//                    bankCardInfo.setName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "name"));
//                    bankCardInfo.setBranchBankId(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "branchBankId"));
                    bankCardInfo.setBranchBankName(JsonUtil.getValueFromJSONObject(banks.getJSONObject(i), "branchBankName"));
                    bankcardlist.add(bankCardInfo);
                }

                cardListAdapter.setList(bankcardlist);
                cardListAdapter.notifyDataSetChanged();
                lv_bindedcard.setAdapter(cardListAdapter);
//                lv_bindedcard.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(int position, SwipeMenu swipeMenu, int index) {
//                        currentpostion = position;
//                        showDeleteDialog();
//                        return false;
//                    }
//                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
    private void unBindCard() {
        qtpayCardIdx.setValue(bankcardlist.get(currentpostion).getCardIdx());
        qtpayApplication.setValue("BankCardUnBind.Req");
        qtpayAttributeList.add(qtpayApplication);
        qtpayParameterList.add(qtpayCardIdx);
        httpsPost("BankCardUnBind", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                LogUtil.showToast(BindedCardListActivity.this, payResult.getRespDesc());
                bankcardlist.clear();
                getDebitCardList();
            }
        });
    }
    public void initMode() {
        if (bankcardlist.size() == 0) {
            lay_binded_addcard.setVisibility(View.VISIBLE);
            img_no_item.setVisibility(View.VISIBLE);
            lv_bindedcard.setVisibility(View.GONE);
//            lay_instruction.setVisibility(View.GONE);
        } else {
            lay_binded_addcard.setVisibility(View.GONE);
            img_no_item.setVisibility(View.GONE);
            lv_bindedcard.setVisibility(View.VISIBLE);
//            lay_instruction.setVisibility(View.VISIBLE);
        }

    }
}
