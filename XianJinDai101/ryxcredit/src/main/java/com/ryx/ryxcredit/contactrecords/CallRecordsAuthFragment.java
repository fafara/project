package com.ryx.ryxcredit.contactrecords;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.rey.material.widget.Button;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.IDCardUtil;
import com.ryx.ryxcredit.widget.RyxCreditLoadDialog;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/3/9.
 */

public class CallRecordsAuthFragment extends Fragment {

    private EditText edt_userName;//
    private EditText edt_userId;//服务密码
    private Button btn_sure;
    private CallRecordsActivity callback;
    private View rootView;
    private String phoneNo;//手机号码
    private String serviceCode;//服务密码

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        callback = (CallRecordsActivity) getActivity();
        rootView = inflater.inflate(R.layout.c_fragment_call_records_auth, container, false);
        initView();
        return rootView;
    }

    private void initView() {
        edt_userName = (EditText) rootView.findViewById(R.id.edt_userName);
        edt_userId = (EditText) rootView.findViewById(R.id.edt_userId);
        btn_sure = (Button) rootView.findViewById(R.id.btn_sure);
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
                map.put("phoneNo",phoneNo);
                map.put("password",serviceCode);
                map.put("userName",userName);
                map.put("userId",userId);
                RyxCreditLoadDialog.getInstance(getActivity()).setMessage("授权需要1-2分钟，请不要退出!");
                RyxCreditLoadDialog.getInstance(getActivity()).show();
                callback.doSubmit(map);
            }
        });
    }




}
