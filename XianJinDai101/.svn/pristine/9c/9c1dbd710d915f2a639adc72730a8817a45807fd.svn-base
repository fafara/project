package com.ryx.ryxcredit.newactivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.beans.bussiness.activateline.CActivateLineSearchRequest;
import com.ryx.ryxcredit.beans.bussiness.activateline.CActivateLineSearchResponse;
import com.ryx.ryxcredit.beans.bussiness.supplementarymaterials.CModifyPersonMaterialsRequest;
import com.ryx.ryxcredit.beans.bussiness.supplementarymaterials.CSupplementaryMaterialsRequest;
import com.ryx.ryxcredit.beans.bussiness.supplementarymaterials.CSupplementaryMaterialsResponse;
import com.ryx.ryxcredit.beans.pojo.Customer;
import com.ryx.ryxcredit.inter.IBaseInfoFragmentCallback;
import com.ryx.ryxcredit.newbean.userlevel.cashLimit.CashLimitRequest;
import com.ryx.ryxcredit.newbean.userlevel.cashLimit.CashLimitResponse;
import com.ryx.ryxcredit.newfragment.baseinfo.BasePersonalInfoFragment;
import com.ryx.ryxcredit.newfragment.baseinfo.BaseWorkInfoFragment;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.CCommonDialog;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.views.LeadingDotsViews;

/**
 * 资料管理
 */
public class BaseInfoActivity extends BaseActivity implements IBaseInfoFragmentCallback {
    public static final int BASEINFO_RESULT_CODE = 0x1002;
    private TextView mMobileService;
    private boolean infoFinished;
    private String activation_status;
    private int active_status;
    private String province;
    private String city;
    private String region;
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
    }

    private enum BaseInfoEnum {

        PERSON_INFO,
        WORK_INFO,
        APPLY_INFO,
        OTHER_INFO,
        CALLRECORDS_INFO,
        FACERECOGNITION_INFO,
    }


    //新增对象
    private CSupplementaryMaterialsRequest cSupplementaryMaterialsRequest = null;
    //修改对象
    private CModifyPersonMaterialsRequest cModifyPersonMaterialsRequest = null;
    private String productId;//产品编码

    public static interface IPhoneListener {
        void phone(String phone);
    }

    private IPhoneListener iPhoneListener;
    //dot控制
    private LeadingDotsViews leadingDotsViews;

    public Customer customer;
    private String user_level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_promote_quota_activity);
        setTitleLayout("工作信息", true, false);
        initData();
        customer = (Customer) getIntent().getSerializableExtra("data");
        if (customer == null) {
            customer = new Customer();
        }
        if (getIntent().getExtras() != null) {
            infoFinished = getIntent().getExtras().getBoolean("infoFinished");
        }
        if (getIntent().getExtras() != null) {
            province = getIntent().getExtras().getString(province);
        }
        if (getIntent().getExtras() != null) {
            city = getIntent().getExtras().getString("city");
        }
        if (getIntent().getExtras() != null) {
            region = getIntent().getExtras().getString("region");
        }
        if(getIntent().hasExtra("product_id")){
            productId = getIntent().getStringExtra("product_id");
        }
        if(getIntent().hasExtra("user_level")){
            user_level = getIntent().getStringExtra("user_level");
        }
        setbottomMenu();
        leadingDotsViews = (LeadingDotsViews) findViewById(R.id.ldv_content);
        mMobileService = (TextView) findViewById(R.id.tv_service_mobile);
        mMobileService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转系统拨号
                Uri uri = Uri.parse("tel:" + getResources().getString(R.string.service_phone));
                Intent it = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(it);
            }
        });
       // changeFragmentPosition(BaseInfoEnum.WORK_INFO);

    }

    @Override
    public void setbottomMenu() {
        super.setbottomMenu();
    }

    /**
     * 切换fragment
     *
     * @param baseinfo
     */
    private void changeFragmentPosition(BaseInfoEnum baseinfo) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(baseinfo.name());
        if (baseinfo == BaseInfoEnum.PERSON_INFO) {
            if (fragment == null)
            {
                BasePersonalInfoFragment bfragment = new BasePersonalInfoFragment();
                iPhoneListener = bfragment;
                fragment = bfragment;
            }
            leadingDotsViews.change(1);
            setTitleLayout(getResources().getString(R.string.c_person_info), true, false);
        } else if (baseinfo == BaseInfoEnum.WORK_INFO) {
            if (fragment == null)
                fragment = new BaseWorkInfoFragment();
            leadingDotsViews.change(0);
            setTitleLayout(getResources().getString(R.string.c_work_info), true, false);
        }
        /*else if (baseinfo == BaseInfoEnum.CALLRECORDS_INFO) {
            if (fragment == null)
                fragment = new CallRecordsFragment();
            leadingDotsViews.change(2);
            setTitleLayout(getResources().getString(R.string.c_callRecords_info), true, false);

        }else if (baseinfo == BaseInfoEnum.FACERECOGNITION_INFO) {
            if (fragment == null)
                fragment = new FaceCollectFragment();
            leadingDotsViews.change(3);
            setTitleLayout(getResources().getString(R.string.c_facerecognition_info), true, false);
        }*/
        if (fragment != null) {
            Bundle args = new Bundle();
            args.putSerializable("data", cSupplementaryMaterialsRequest);
            args.putBoolean("infoFinished",infoFinished);
            args.putString("product_id",productId);
            args.putString("province  ",province  );
            args.putString("city ",city );
            args.putString("region ",region );
            fragment.setArguments(args);
            fragmentTransaction.replace(R.id.c_fl_base_info, fragment, baseinfo.name());
            fragmentTransaction.addToBackStack(baseinfo.name());
            fragmentTransaction.commit();
        }
    }

    @Override
    public void setPersonInfo(Object personInfo) {
        changeFragmentPosition(BaseInfoEnum.PERSON_INFO);
    }
    public void setFaceCollectInfo() {
        changeFragmentPosition(BaseInfoEnum.FACERECOGNITION_INFO);
    }

    public void setWorkInfo(Object workInfo) {
        cSupplementaryMaterialsRequest = (CSupplementaryMaterialsRequest) workInfo;
        submitSuppliementaryMaterials();

    }

    public void setCallRecordInfo(Object callRecordInfo) {
        cSupplementaryMaterialsRequest = (CSupplementaryMaterialsRequest) callRecordInfo;
        submitSuppliementaryMaterials();
        changeFragmentPosition(BaseInfoEnum.CALLRECORDS_INFO);
    }

    public void setCallRecordInfo( ) {
        changeFragmentPosition(BaseInfoEnum.CALLRECORDS_INFO);
    }

    public void setFaceCollectInfo(Object faceCollectInfo) {
        changeFragmentPosition(BaseInfoEnum.FACERECOGNITION_INFO);
    }

    @Override
    public void setApplyInfo(Object applyInfo) {
        changeFragmentPosition(BaseInfoEnum.APPLY_INFO);
    }

    @Override
    public void setOtherInfo(Object otherInfo) {
        cSupplementaryMaterialsRequest = (CSupplementaryMaterialsRequest) otherInfo;
        changeFragmentPosition(BaseInfoEnum.PERSON_INFO);
    }

    /**
     * 提交个人资料
     */
    private void submitSuppliementaryMaterials() {
        RyxcreditConfig.context = this;
        addPersonMaterial(cSupplementaryMaterialsRequest);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment_person = fragmentManager.findFragmentByTag(BaseInfoEnum.PERSON_INFO.name());
        Fragment fragment_work = fragmentManager.findFragmentByTag(BaseInfoEnum.WORK_INFO.name());
        if (null != fragment_work && fragment_work.isVisible()) {
            finish();
            return;
        }
        if (null != fragment_person && fragment_person.isVisible()) {
            leadingDotsViews.change(0);
            setTitleLayout(getResources().getString(R.string.c_work_info), true, false);
            super.onBackPressed();
        }
    }

    /**
     * 新增个人资料
     *
     * @param req
     */
    private void addPersonMaterial(CSupplementaryMaterialsRequest req) {
        httpsPost(this, req, ReqAction.APPLICATION_CREATE_OR_MODIFY_CUSTOMER, CSupplementaryMaterialsResponse.class, new ICallback<CSupplementaryMaterialsResponse>() {
            @Override
            public void success(CSupplementaryMaterialsResponse response) {
                customer.setContact_name(cSupplementaryMaterialsRequest.getContact_name());
                customer.setContact_phone_num(cSupplementaryMaterialsRequest.getCompany_phone_num());
                customer.setContact_relation(cSupplementaryMaterialsRequest.getContact_relation());
                customer.setMarital_status(cSupplementaryMaterialsRequest.getMarital_status());
                customer.setEducation_status(cSupplementaryMaterialsRequest.getEducation_status());
                customer.setCompany_address(cSupplementaryMaterialsRequest.getCompany_address());
                customer.setCompany_name(cSupplementaryMaterialsRequest.getCompany_name());
                customer.setCompany_phone_num(cSupplementaryMaterialsRequest.getCompany_phone_num());
                customer.setIndustry_type(cSupplementaryMaterialsRequest.getIndustry_type());
                customer.setMonthly_income(cSupplementaryMaterialsRequest.getMonthly_income());
                finish();
//                getCashLimit();
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(BaseInfoActivity.this, tips);
            }
        });
    }

    //客户级别接口
    private void getCashLimit() {
        final CashLimitRequest request = new CashLimitRequest();
        request.setProduct_id(productId);
        request.setCustomer_type(user_level);
        //激活A
        request.setFlag("A");
        httpsPost(BaseInfoActivity.this, request, ReqAction.CASH_LIMIT, CashLimitResponse.class, new ICallback<CashLimitResponse>() {

            public void success(CashLimitResponse cashLimitResponse) {
                Integer  code = cashLimitResponse.getCode();
                String result = cashLimitResponse.getResult();
                CCommonDialog.showRepaymentOK(BaseInfoActivity.this, "提交成功", "您的申请已提交，我们会尽快处理！", new CCommonDialog.IMessage() {
                    @Override
                    public void callback() {
                        Intent idata = new Intent();
                        idata.putExtra("data", customer);
                        setResult(BASEINFO_RESULT_CODE, idata);
//                        startActivity(new Intent(BaseInfoActivity.this, CreditActivity.class));
                        finish();
                    }
                });
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(BaseInfoActivity.this,tips);
            }
        });
    }
    //获取用户填写的基本信息
    private void initData() {
        CActivateLineSearchRequest request = new CActivateLineSearchRequest();
        request.setVersion(RyxcreditConfig.getVersion());
        httpsPost(this, request, ReqAction.APPLICATION_ACTIVATION_LIMIT, CActivateLineSearchResponse.class, new ICallback<CActivateLineSearchResponse>() {
            @Override
            public void success(final CActivateLineSearchResponse cActivateLineResponse) {
                customer = cActivateLineResponse.getResult().getCustomer();
                province = cActivateLineResponse.getResult().getProvince();
                city =cActivateLineResponse.getResult().getCity();
                region =cActivateLineResponse.getResult().getRegion();
                changeFragmentPosition(BaseInfoEnum.WORK_INFO);
            }


            @Override
            public void failture(String tips) {
                changeFragmentPosition(BaseInfoEnum.WORK_INFO);
            }
        });

    }
}
