package com.ryx.payment.ruishua.recharge;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.activity.QtpayAppData;
import com.ryx.payment.ruishua.bean.Order;
import com.ryx.payment.ruishua.bean.OrderInfo;
import com.ryx.payment.ruishua.bean.QcoinInfo;
import com.ryx.payment.ruishua.convenience.CreateOrder_;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.recharge.adapter.MobileAdapter;
import com.ryx.payment.ruishua.recharge.adapter.MobileAdapter_;
import com.ryx.payment.ruishua.utils.ContactHelper;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.swiper.utils.MoneyEncoder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.text.MessageFormat;
import java.util.ArrayList;

@EActivity(R.layout.activity_mobile_recharge)
public class MobileRechargeActivity extends BaseActivity {

    @ViewById(R.id.et_mobile)
    EditText mMobileEt;
    @ViewById(R.id.gv_mobile)
    GridView mMobileGridView;
    @ViewById(R.id.iv_select_contact)
    ImageView mSelectContacts;
    @ViewById(R.id.btn_recharge)
    Button mRechargeBtn;
    private OrderInfo orderinfo;
    private String mMoney;
    private ArrayList<QcoinInfo> mMobileList;
    private MobileAdapter mMobileAdapter;
    private String[] mPhoneString = null;
    private String mobile;

    @AfterViews
    public void initViews() {
        setTitleLayout("手机充值", true);
        setRightImgMessage("用户须知", RyxAppconfig.Notes_MobileRecharge);
        initData();
        mMobileAdapter = MobileAdapter_.getInstance_(MobileRechargeActivity.this);
        mMobileAdapter.setList(mMobileList);
        mMobileAdapter.setSelection(0);
        mMobileGridView.setAdapter(mMobileAdapter);
    }

    private void initData() {
        mMobileList = new ArrayList<>();
        QcoinInfo info = new QcoinInfo();
        info.setParvalue("￥50.00");
        mMobileList.add(info);

        QcoinInfo info1 = new QcoinInfo();
        info1.setParvalue("￥100.00");
        mMobileList.add(info1);

        QcoinInfo info2 = new QcoinInfo();
        info2.setParvalue("￥300.00");
        mMobileList.add(info2);

        mMobileEt.setText(QtpayAppData.getInstance(MobileRechargeActivity.this).getMobileNo());
        mMobileEt.setSelection(mMobileEt.getText().length());
        //默认值
        mMoney = mMobileList.get(0).getParvalue();
    }

    @ItemClick(R.id.gv_mobile)
    public void mobileClick(int position) {
        mMoney = MoneyEncoder.EncodeFormat(mMobileList.get(position).getParvalue());
        mMobileAdapter.setSelection(position);
        mMobileAdapter.notifyDataSetChanged();
    }

    @Click(R.id.iv_select_contact)
    public void selectContacts() {
        checkContactPermission();
    }

    @Click(R.id.btn_recharge)
    public void rechargeClick() {
        if (checkInput()) {
            Intent intent = new Intent(MobileRechargeActivity.this, CreateOrder_.class);
            orderinfo = Order.PHONE_RECHARGE;
            orderinfo.setOrderDesc(mobile); // 要充值的手机号
            orderinfo.setOrderAmt(mMoney);//金额
            orderinfo.setOrderRemark("");
            intent.putExtra("orderinfo", orderinfo);
            startActivityForResult(intent, RyxAppconfig.WILL_BE_CLOSED);
        }
    }

    private boolean checkInput() {
        mobile = mMobileEt.getText().toString().trim();
        if (TextUtils.isEmpty(mobile) || mobile.length() != 11) {
            LogUtil.showToast(MobileRechargeActivity.this, getResources().getString(R.string.please_enter_the_11_phone_numbers));
            return false;
        } else if (mobile.substring(0, 1).equals("0")) {
            LogUtil.showToast(MobileRechargeActivity.this, getResources().getString(R.string.not_a_phone_number));
            return false;
        } else if (TextUtils.isEmpty(mMoney)) {
            LogUtil.showToast(
                    MobileRechargeActivity.this, "请选择充值金额!!!");
            return false;
        }
        return true;
    }

    /**
     * 输入时格式化手机号
     @TextChange(R.id.et_mobile) public void mobileTextChanged(CharSequence str, TextView hello, int before, int start, int count) {
     String contents = str.toString();
     int length = contents.length();
     if (length == 4) {
     if (contents.substring(3).equals(new String(" "))) { // -
     contents = contents.substring(0, 3);
     mMobileEt.setText(contents);
     mMobileEt.setSelection(contents.length());
     } else { // +
     contents = contents.substring(0, 3) + " " + contents.substring(3);
     mMobileEt.setText(contents);
     mMobileEt.setSelection(contents.length());
     }
     } else if (length == 9) {
     if (contents.substring(8).equals(new String(" "))) { // -
     contents = contents.substring(0, 8);
     mMobileEt.setText(contents);
     mMobileEt.setSelection(contents.length());
     } else {// +
     contents = contents.substring(0, 8) + " " + contents.substring(8);
     mMobileEt.setText(contents);
     mMobileEt.setSelection(contents.length());
     }
     }
     }
     **/

    /**
     * 手机访问权限
     */
    public void checkContactPermission() {
        String   waring = MessageFormat.format(getResources().getString(R.string.swiperContactwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
//        if (RyxAppconfig.BRANCH.equals("01")) {
//            waring = MessageFormat.format(getResources().getString(R.string.swiperContactwaringmsg), getResources().getString(R.string.app_name));
//        }else if (RyxAppconfig.BRANCH.equals("02")) {
//            waring = MessageFormat.format(getResources().getString(R.string.swiperContactwaringmsg), getResources().getString(R.string.app_name_ryx));
//        }
        requesDevicePermission(waring, 0x0011, new PermissionResult() {

                    @Override
                    public void requestSuccess() {
                        startActivityForResult(new Intent(Intent.ACTION_PICK,
                                ContactsContract.Contacts.CONTENT_URI), 0);
                    }

                    @Override
                    public void requestFailed() {

                    }
                },
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE);
    }

    /**
     * 处理返回的手机号
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            setAccount(data);
        }
    }

    private void setAccount(Intent intent) {
        if (intent == null) {
            return;
        } else {
            ArrayList<String> tPhone = ContactHelper.getContactPhoneNo(intent.getData(), this);
            if (tPhone.size() > 1) {
                mPhoneString = new String[tPhone.size()];
                for (int i = 0; i < tPhone.size(); i++) {
                    mPhoneString[i] = tPhone.get(i);
                }
                SimpleDialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        SimpleDialog dialog = (SimpleDialog) fragment.getDialog();
                        if (dialog == null)
                            return;
                        final int index = dialog.getSelectedIndex();
                        fragment.dismiss();
                        String phoneNumber = mPhoneString[index];
                        String mobileNumber=phoneNumber.replace("+86", "").replace(" ", "").replace("-", "");
                        mMobileEt.setText(mobileNumber);
                        mMobileEt.setSelection(mobileNumber.length());
                    }

                    @Override
                    public void onNeutralActionClicked(DialogFragment fragment) {
                        fragment.dismiss();
                    }
                };
                builder.items(mPhoneString, 0).title("请选择电话号码").positiveAction("确定").negativeAction("取消");
                DialogFragment fragment = DialogFragment.newInstance(builder);
                fragment.show(getSupportFragmentManager(), null);
            } else if (tPhone.size() == 1) {
                String mobileNumber=tPhone.get(0).replace("+86", "").replace(" ", "").replace("-", "");
                mMobileEt.setText(mobileNumber);
                mMobileEt.setSelection(mobileNumber.length());
            } else {
                mMobileEt.setText("");
            }
        }
    }
}
