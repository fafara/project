package com.ryx.ryxcredit.ryd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.inter.RecyclerViewHelper;
import com.ryx.quickadapter.recyclerView.animation.ScaleInAnimation;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.beans.bussiness.cardrepayment.CcardRepaymentRequest;
import com.ryx.ryxcredit.beans.bussiness.cardrepayment.CcardRepaymentResponse;
import com.ryx.ryxcredit.beans.bussiness.debitcard.CdebitCardAuthReponse;
import com.ryx.ryxcredit.beans.bussiness.debitcard.CdebitCardAuthRequest;
import com.ryx.ryxcredit.ryd.adapter.RYDSKBankListAdapter;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.CBanksUtils;
import com.ryx.ryxcredit.utils.CDensityUtil;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CStringUnit;
import com.ryx.ryxcredit.xjd.adapter.HKBankListAdapter;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.Serializable;
import java.util.List;

import static com.ryx.ryxcredit.RyxcreditConfig.context;

public class RYDBankSelectActivity extends BaseActivity {

    private RecyclerView c_rv_pay;
    private List<CcardRepaymentResponse.ResultBean> cardinfo;
    private RYDSKBankListAdapter skBankListAdapter;//收款账户
    private HKBankListAdapter hkBankListAdapter;//还款账户
    private boolean isHkBank;//是否是还款银行
    private String hk_phoneNo;
    private String paid_cash_cards;//支持的银行列表
    private TextView tv_support_banklist;
    private AutoRelativeLayout lay_add_debitCard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rydbank_select);
        c_rv_pay = (RecyclerView) findViewById(R.id.c_rv_pay);
        lay_add_debitCard = (AutoRelativeLayout) findViewById(R.id.lay_add_debitCard);
        lay_add_debitCard.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                try {
                    Intent intent = new Intent(RYDBankSelectActivity.this, Class.forName(getApplicationContext().getPackageName() + ".bindcard.BankCardAddActivity_"));
                    startActivityForResult(intent, 997);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        tv_support_banklist = (TextView) findViewById(R.id.tv_support_banklist);
        Intent intent = getIntent();
        isHkBank = intent.getBooleanExtra("is_hk", false);
        //isHkBank：是否是还款银行卡列表
        if (!isHkBank) {
            setTitleLayout("选择银行卡", true, false);
            cardinfo = (List<CcardRepaymentResponse.ResultBean>) getIntent().getSerializableExtra("cardinfo");
            if (getIntent().hasExtra("paid_cash_card")) {
                paid_cash_cards = getIntent().getStringExtra("paid_cash_card");
            }
            initskRecyleView(cardinfo);
        } else {
            setTitleLayout("选择银行卡", true, false);
            if (getIntent().hasExtra("paid_cash_card")) {
                paid_cash_cards = getIntent().getStringExtra("paid_cash_card");
            }
            inithkData();
        }
        //  tv_support_banklist.setText("支持银行：" + paid_cash_cards + "");
        tv_support_banklist.setText( paid_cash_cards );
    }


    //收款账户列表
    private void initskRecyleView(List<CcardRepaymentResponse.ResultBean> hkcardList) {
        RecyclerViewHelper.init().setRVGridLayout(this, c_rv_pay, 1);//1列
        skBankListAdapter = new RYDSKBankListAdapter(hkcardList, this, R.layout.c_impay_bottom_listitem);
        skBankListAdapter.openLoadAnimation(new ScaleInAnimation());
        skBankListAdapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object data) {
                CcardRepaymentResponse.ResultBean depositCardData = (CcardRepaymentResponse.ResultBean) data;
                if (depositCardData != null) {
                    int status = ((CcardRepaymentResponse.ResultBean) data).getStatus();
                    if (status != 1) {
                        CLogUtil.showToast(RYDBankSelectActivity.this, "该银行卡不支持！");
                        return;
                    }
                    Intent intent = getIntent();
                    intent.putExtra("skCardInfo", (Serializable) depositCardData);
                    setResult(994, intent);
                    finish();
                }
            }
        });
        c_rv_pay.setAdapter(skBankListAdapter);
    }

    //还款银行
    private void inithkData() {
        CcardRepaymentRequest requestRepayment = new CcardRepaymentRequest();
        requestRepayment.setVersion(RyxcreditConfig.getVersion());
        httpsPost(this, requestRepayment, ReqAction.APPLICATION_CARD_REPAYMENT, CcardRepaymentResponse.class, new ICallback<CcardRepaymentResponse>() {
            @Override
            public void success(CcardRepaymentResponse ccardRepaymentResponse) {
                int ccardRepaymentCode = ccardRepaymentResponse.getCode();
                if (ccardRepaymentCode==5031) {
                    showMaintainDialog();
                } else {
                    if (ccardRepaymentResponse.getResult() != null) {
                    }
                    initHKData(ccardRepaymentResponse.getResult());
                }
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(RYDBankSelectActivity.this, tips + "");
            }
        });
    }

    //还款账户
    private void initHKData(List<CcardRepaymentResponse.ResultBean> resultBeanList) {
        RecyclerViewHelper.init().setRVGridLayout(this, c_rv_pay, 1);//1列
        hkBankListAdapter = new HKBankListAdapter(resultBeanList, this, R.layout.c_impay_bottom_listitem);
        hkBankListAdapter.openLoadAnimation(new ScaleInAnimation());
        hkBankListAdapter.setOnItemClickListener(new OnListItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object data) {
                int status = ((CcardRepaymentResponse.ResultBean) data).getStatus();
                if (status != 1) {
                    CLogUtil.showToast(RYDBankSelectActivity.this, "该银行卡不支持！");
                    return;
                }
                int auth_status = ((CcardRepaymentResponse.ResultBean) data).getAuth_status();
                int collection_status = ((CcardRepaymentResponse.ResultBean) data).getCollection_status();
                if (auth_status != 1 || collection_status != 1) {
                    cardAuth((CcardRepaymentResponse.ResultBean) data);
                    return;
                }
                Intent intent = getIntent();
                intent.putExtra("hkCardInfo", (Serializable) data);
                setResult(995, intent);
                finish();
            }
        });
        c_rv_pay.setAdapter(hkBankListAdapter);
    }

    private boolean isAgreed = true;//用户是否同意委托代扣协议,默认同意

    //代扣卡认证
    private void cardAuth(final CcardRepaymentResponse.ResultBean cardInfo) {
        //弹出认证代扣卡对话框
        Dialog.Builder builder = null;
        builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
            @Override
            protected void onBuildDone(final Dialog dialog) {
                WindowManager wm = dialog.getWindow().getWindowManager();
                DisplayMetrics dm = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(dm);
                dialog.layoutParams(dm.widthPixels - CDensityUtil.dip2px(context, 32), ViewGroup.LayoutParams.WRAP_CONTENT);
                final EditText phoneEdt = (EditText) dialog.findViewById(R.id.c_bankPhoneNum);
                ImageView bankLogo = (ImageView) dialog.findViewById(R.id.c_banklogo);
                TextView bankNoTv = (TextView) dialog.findViewById(R.id.c_dfBankNo);
                bankNoTv.setText(CStringUnit.cardJiaMi(cardInfo.getCard_num()));
                CBanksUtils.selectIcoidToImgView(RYDBankSelectActivity.this, cardInfo.getBank_title_code(), bankLogo);
                TextView agreeMentView = (TextView) dialog.findViewById(R.id.c_authcontract);
                agreeMentView.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    protected void onNoDoubleClick(View view) {
                        try {
//                            Intent intent = new Intent(BankSelectActivity.this, Class.forName(getApplicationContext().getPackageName() + ".activity.HtmlMessageActivity_"));
//                            intent.putExtra("ccurl", product.getAgreement_url() + "?PayBankAcctName=" + RyxcreditConfig.getRealName()
//                                    + "&ddBankName=" + cardInfo.getBank_name() + "&ddBankAcctNbr=" + CStringUnit.cardJiaMi(cardInfo.getCard_num()) + "");
//                            intent.putExtra("title", "委托扣款服务协议");
//                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                CheckBox agreeCb = (CheckBox) dialog.findViewById(R.id.cb_agree);
                agreeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        isAgreed = isChecked;
                    }
                });
                Button sureBtn = (Button) dialog.findViewById(R.id.c_sure_btn);
                dialog.findViewById(R.id.c_close).setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    protected void onNoDoubleClick(View view) {
                        dialog.dismiss();
                    }
                });
                sureBtn.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        if (!isAgreed) {
                            CLogUtil.showToast(RYDBankSelectActivity.this, "请先同意委托扣款服务协议!");
                            return;
                        }
                        hk_phoneNo = phoneEdt.getText().toString().trim();
                        if (hk_phoneNo.length() != 11) {
                            CLogUtil.showToast(RYDBankSelectActivity.this, "请输入正确的银行预留手机号码!");
                            return;
                        }
                        doCardAuth(dialog, cardInfo);
                    }
                });

            }
        };
        builder.contentView(R.layout.c_dialog_auth_debit_card);
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    private void doCardAuth(final Dialog dialog, final CcardRepaymentResponse.ResultBean cardInfo) {
        CdebitCardAuthRequest request = new CdebitCardAuthRequest();
        request.setCard_num(cardInfo.getCard_num());
        request.setReserved_phone_num(hk_phoneNo);
        httpsPost(this, request, ReqAction.APPLICATION_WITHHELD_CARD_CERTIFICATE, CdebitCardAuthReponse.class, new ICallback<CdebitCardAuthReponse>() {
            @Override
            public void success(CdebitCardAuthReponse cdebitCardResponse) {
                boolean isResult = cdebitCardResponse.isResult();
                int cdebitCardCode = cdebitCardResponse.getCode();
                if (cdebitCardCode==5031) {
                    showMaintainDialog();
                } else {
                    String msg = (isResult) ? "认证成功！" : "认证失败！";
                    dialog.dismiss();
                    CLogUtil.showToast(RYDBankSelectActivity.this, msg);
                    if (isResult) {
                        Intent intent = getIntent();
                        intent.putExtra("hkCardInfo", (Serializable) cardInfo);
                        setResult(995, intent);
                        finish();
                    }
                }
            }

            @Override
            public void failture(String tips) {
                dialog.dismiss();
                CLogUtil.showToast(RYDBankSelectActivity.this, tips);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 997) {
            if (!isHkBank) {
                initskData();
            } else {
                inithkData();
            }
        }
    }

    //获取收款银行
    private void initskData() {
        CcardRepaymentRequest requestRepayment = new CcardRepaymentRequest();
        httpsPost(this, requestRepayment, ReqAction.CARD_PAYMENT_LIST, CcardRepaymentResponse.class, new ICallback<CcardRepaymentResponse>() {
            @Override
            public void success(CcardRepaymentResponse ccardRepaymentResponse) {
                if (ccardRepaymentResponse.getResult() != null) {
                    cardinfo = ccardRepaymentResponse.getResult();
                    int ccardRepaymentCode = ccardRepaymentResponse.getCode();
                    if (ccardRepaymentCode==5031) {
                        showMaintainDialog();
                    } else {
                        initskRecyleView(cardinfo);
                    }
                }
            }

            @Override
            public void failture(String tips) {

            }
        });
    }


}

