package com.ryx.ryxcredit.newactivity.contactrecords;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.rey.material.widget.Button;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.newfragment.baseinfo.CallRecordsFragment;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.IDCardUtil;
import com.ryx.ryxcredit.xjd.BaseInfoSuccesActivity;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/9.
 */

public class CallRecordsAuthUtil extends Fragment {

    private EditText edt_userName;//
    private EditText edt_userId;//服务密码
    private Button btn_sure;
    private String phoneNo;//手机号码
    private String serviceCode;//服务密码
    private CallRecordsFragment mfragment;
    private BaseInfoSuccesActivity mainActivity;
    private View mrootView;

    public void onCreateView(CallRecordsFragment fragment,View rootView) {
        mfragment = fragment;
        mrootView= rootView;
        mainActivity = (BaseInfoSuccesActivity) mfragment.getActivity();
        initView();
    }

    public void setNumAndserviceCode(String num,String code){
        phoneNo = num;
        serviceCode= code;

    }


    private void initView() {
        edt_userName = (EditText) mrootView.findViewById(R.id.edt_userName);
        edt_userId = (EditText) mrootView.findViewById(R.id.edt_userId);
        btn_sure = (Button) mrootView.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = edt_userName.getText().toString().trim();
                String userId = edt_userId.getText().toString().trim();
                if(TextUtils.isEmpty(userName)){
                    CLogUtil.showToast(getActivity(),"请输入姓名！");
                    return;
                }
                if(TextUtils.isEmpty(userId)){
                    CLogUtil.showToast(getActivity(),"请输入身份证号码！");
                    return;
                }
                //判断用户是否填写了正确的身份证号码
                if (!IDCardUtil.isIDCard(userId)) {
                    CLogUtil.showToast(getActivity(), getResources().getString(R.string.please_enter_the_correct_id_number));
                    return ;
                }
                HashMap map = new HashMap();
 /*               map.put("phoneNo",phoneNo);
                map.put("password",serviceCode);*/
                map.put("userName",userName);
                map.put("userId",userId);
                /*RyxCreditLoadDialog.getInstance(getActivity()).setMessage("授权需要1-2分钟，请不要退出!");
                RyxCreditLoadDialog.getInstance(getActivity()).show();*/
                mfragment.doSubmit(map);
            }
        });
    }




}
