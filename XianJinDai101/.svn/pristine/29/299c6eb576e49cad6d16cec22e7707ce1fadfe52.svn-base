package com.ryx.ryxcredit.newfragment.baseinfo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.PhoneNumber;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.TextView;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.beans.bussiness.addressbook.ContactsRequest;
import com.ryx.ryxcredit.beans.bussiness.addressbook.ContactsResponse;
import com.ryx.ryxcredit.beans.bussiness.supplementarymaterials.CSupplementaryMaterialsRequest;
import com.ryx.ryxcredit.beans.pojo.Customer;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.services.UICallBack;
import com.ryx.ryxcredit.utils.CCommonDialog;
import com.ryx.ryxcredit.utils.CConstants;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.CPreferenceUtil;
import com.ryx.ryxcredit.utils.ContactHelper;
import com.ryx.ryxcredit.utils.HttpUtil;
import com.ryx.ryxcredit.utils.PermissionResult;
import com.ryx.ryxcredit.widget.RyxCreditLoadDialog;
import com.ryx.ryxcredit.xjd.BaseInfoSuccesActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author：lijing on 16/6/25 09:04
 * Mail：lijing1-jn@ruiyinxin.com
 * Description：个人信息
 */

public class BasePersonalInfoFragmentJHSuccessed extends Fragment implements BaseInfoSuccesActivity.IPhoneListener {

    private BaseInfoSuccesActivity callback;
    //婚姻状况
    private TextView maritalStatusTv;
    //婚姻关系
    private TextView relationshipTv;
    //亲属姓名
    private android.widget.TextView relationshipNameEt;
    //亲属电话
    private EditText relationshipPhoneEt;
    //选择手机号
    private ImageView selectPhone;
    //教育情况
    private TextView educationLevelTv;
    public static final int PHONE_RESULT_CODE = 1;
    private String[] mPhoneString;
    private String regEx = "[|｜\r\n]";
    private String phoneNoregEx = "^[1][3,4,5,7,8][0-9]{9}$";
    private CSupplementaryMaterialsRequest cSupplementaryMaterialsRequest;
    private String relationshipName, relationshipPhone, contactName, contactPhoneNum;
    private CPreferenceUtil preferenceUtil;
    private String user_info_level;
    private String productId;
    private String mobileNumber;
    //请求参数
    Object result = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        callback = (BaseInfoSuccesActivity) getActivity();
        preferenceUtil = CPreferenceUtil.getInstance(getActivity().getApplication());
        View rootView = inflater.inflate(R.layout.c_new_fragment_base_info_jssuccessed, container, false);
        Object data = getArguments().getSerializable("data");
        user_info_level = ((BaseInfoSuccesActivity) getActivity()).getUser_info_level();
        productId = getArguments().getString("productId");
        cSupplementaryMaterialsRequest = (CSupplementaryMaterialsRequest) data;
        maritalStatusTv = (TextView) rootView.findViewById(R.id.c_base_info_marital_status_tv);
        relationshipTv = (TextView) rootView.findViewById(R.id.c_base_info_relationship_tv);
        relationshipNameEt = (android.widget.TextView) rootView.findViewById(R.id.c_base_info_relationship_name_et);
        relationshipPhoneEt = (EditText) rootView.findViewById(R.id.c_base_info_relationship_phone_et);
        educationLevelTv = (TextView) rootView.findViewById(R.id.c_base_info_home_education_level_tv);
        selectPhone = (ImageView) rootView.findViewById(R.id.c_ib_select_phone);
/*        relationshipPhoneEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // checkContactPermission();
            }
        });*/
        initView();
        initOnClickListene();
        rootView.findViewById(R.id.c_btn_baseinfo_next).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (checkInput()) {
                    //checkContactPermission();
                    uploadContactsClickNextButton();
                    relationshipName = rexgStr(relationshipNameEt.getText().toString().trim());
                    cSupplementaryMaterialsRequest.setContact_name(relationshipName);
                    cSupplementaryMaterialsRequest.setContact_relation(relationShipStatus);
                    relationshipPhone = relationshipPhoneEt.getText().toString().trim();
                    cSupplementaryMaterialsRequest.setContact_phone_num(relationshipPhone);
                    cSupplementaryMaterialsRequest.setMarital_status(maritalStatus);
                    cSupplementaryMaterialsRequest.setEducation_status(educationStatus);
                    //判断用户如果未完善信息过，则保存信息
                    savePersonalInfo();
                    result = cSupplementaryMaterialsRequest;
                    callback.customer.setContact_name(cSupplementaryMaterialsRequest.getContact_name());
                    callback.customer.setContact_relation(relationShipStatus);
                    callback.customer.setContact_phone_num(relationshipPhoneEt.getText().toString().trim());
                    callback.customer.setMarital_status(maritalStatus);
                    callback.customer.setEducation_status(educationStatus);
                    //运营商
                    if ("C".equalsIgnoreCase(user_info_level)) {
                        callback.setCallRecordInfo(result);
                    }//人行征信
                    else if (("A".equalsIgnoreCase(user_info_level))) {
                        callback.setPedestrianLetter();
                    }//电信运营商+人行
                    else if ("D".equalsIgnoreCase(user_info_level)){
                        callback.setCallRecordInfo(result);
                    }
                    else if ("H".equalsIgnoreCase(user_info_level)) {
                        callback.setFaceCollectInfo();
                    }
                    }

            }
        });
        return rootView;
    }

    private void initView() {
        Customer c = callback.customer;
        if (c != null) {
            maritalStatus = c.getMarital_status();
            educationStatus = c.getEducation_status();
            relationShipStatus = c.getContact_relation();
            contactName = c.getContact_name();
            contactPhoneNum = c.getContact_phone_num();
        } else {
            maritalStatus = preferenceUtil.getString(RyxcreditConfig.getPhoneNo()+"c_personal_maritalStatus", "");
            educationStatus = preferenceUtil.getString(RyxcreditConfig.getPhoneNo()+"c_personal_educationStatus", "");
            relationShipStatus = preferenceUtil.getString(RyxcreditConfig.getPhoneNo()+"c_personal_relationShipStatus", "");
            contactName = preferenceUtil.getString(RyxcreditConfig.getPhoneNo()+"c_personal_relationshipName", "");
            contactPhoneNum = preferenceUtil.getString(RyxcreditConfig.getPhoneNo()+"c_personal_relationshipPhone", "");
        }
        if (!TextUtils.isEmpty(maritalStatus))
            maritalStatusTv.setText(CConstants.getMarryStatus(maritalStatus));
        if (!TextUtils.isEmpty(relationShipStatus)) {
            relationshipTv.setText(CConstants.getRelationShipStatus(relationShipStatus));
        }
        relationshipNameEt.setText(contactName);
        relationshipPhoneEt.setText(contactPhoneNum);
        if (!TextUtils.isEmpty(educationStatus))
            educationLevelTv.setText(CConstants.getEducationStatus(educationStatus));
        relationshipPhoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String textStr = s.toString();
                if (!phonNorexgStr(textStr)) {
                    CLogUtil.showToast(getActivity(), "请输入正确手机号！");
                    return ;
                }
                callback.customer.setContact_phone_num(textStr);
            }
        });
/*        relationshipNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                callback.customer.setContact_name(s.toString());
            }
        });*/
    }

    //将未激活的用户个人信息保存在本地，
    private void savePersonalInfo() {
        //将未激活的用户公司信息保存在本地，
        relationshipName = rexgStr(relationshipNameEt.getText().toString().trim());
        relationshipPhone = relationshipPhoneEt.getText().toString().trim();
        preferenceUtil.saveString(RyxcreditConfig.getPhoneNo()+"c_personal_relationshipName", relationshipName);
        preferenceUtil.saveString(RyxcreditConfig.getPhoneNo()+"c_personal_relationShipStatus", relationShipStatus);
        preferenceUtil.saveString(RyxcreditConfig.getPhoneNo()+"c_personal_relationshipPhone", relationshipPhone);
        preferenceUtil.saveString(RyxcreditConfig.getPhoneNo()+"c_personal_maritalStatus", maritalStatus);
        preferenceUtil.saveString(RyxcreditConfig.getPhoneNo()+"c_personal_educationStatus", educationStatus);
    }

    private String rexgStr(String str) {
        Pattern pattern = Pattern.compile(regEx);
        Matcher m = pattern.matcher(str);
        return m.replaceAll("").trim();
    }

    private boolean phonNorexgStr(String str) {
        Pattern pattern = Pattern.compile(phoneNoregEx);
        Matcher m = pattern.matcher(str);
        return m.matches();
    }

    public boolean checkInput() {
        String relationName = relationshipNameEt.getText().toString().trim();
        if (TextUtils.isEmpty(relationName)) {
            CLogUtil.showToast(getActivity(), "联系人姓名不能为空");
            return false;
        }
        String relationShip = relationshipTv.getText().toString().trim();
        if (TextUtils.isEmpty(relationShip)) {
            CLogUtil.showToast(getActivity(), "请选择联系人关系！");
            return false;
        }
        String relationPhone = relationshipPhoneEt.getText().toString().trim();
        if (TextUtils.isEmpty(relationPhone)) {
            CLogUtil.showToast(getActivity(), "联系人手机号码不能为空！");
            return false;
        }
        if(relationPhone.equalsIgnoreCase(RyxcreditConfig.getPhoneNo())) {
            CLogUtil.showToast(getActivity(), "申请人不得与联系人为同一人哦，请您重新选择");
            return false;
        }
        if (!phonNorexgStr(relationPhone)) {
            CLogUtil.showToast(getActivity(), "请输入正确手机号！");
            return false;
        }
        String maritalStatus = maritalStatusTv.getText().toString().trim();
        if (TextUtils.isEmpty(maritalStatus)) {
            CLogUtil.showToast(getActivity(), "请选择婚姻状况！");
            return false;
        }
        String educationLevle = educationLevelTv.getText().toString().trim();
        if (TextUtils.isEmpty(educationLevle)) {
            CLogUtil.showToast(getActivity(), "请选择教育信息！");
            return false;
        }
        return true;
    }

    //婚姻状况
    private String maritalStatus;
    //教育程度
    private String educationStatus;
    //关系
    private String relationShipStatus;

    private void initOnClickListene() {

        selectPhone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkContactPermission();
            }
        });
        relationshipNameEt.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
               /* relationshipNameEt.setText("");
                relationshipPhoneEt.setText("");*/
                checkContactPermission();
            }
        });

        relationshipTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                CCommonDialog.showCommonSelected(getActivity(), "请选择联系人关系", getActivity().getResources().getStringArray(R.array.relationship_status_array),
                        new CCommonDialog.ICommonSelectedListener() {

                            @Override
                            public void selectedKey(int key, String value) {
                                relationShipStatus = CConstants.getRelationShipKey(value);
                                relationshipTv.setText(value);
                                callback.customer.setContact_relation(relationShipStatus);
                            }
                        });

            }
        });
        educationLevelTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CCommonDialog.showCommonSelected(getActivity(), "请选择教育程度", getActivity().getResources().getStringArray(R.array.education_status_array), new CCommonDialog.ICommonSelectedListener() {

                    @Override
                    public void selectedKey(int key, String value) {
                        educationStatus = CConstants.getEducationKey(value);
                        educationLevelTv.setText(value);
                        callback.customer.setEducation_status(educationStatus);
                    }
                });
            }
        });

        maritalStatusTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CCommonDialog.showCommonSelected(getActivity(), "请选择婚姻状况", getActivity().getResources().getStringArray(R.array.marital_status_array), new CCommonDialog.ICommonSelectedListener() {

                    @Override
                    public void selectedKey(int key, String value) {
                        maritalStatus = CConstants.getMarryKey(value);
                        maritalStatusTv.setText(value);
                        callback.customer.setMarital_status(maritalStatus);
                    }
                });
            }
        });
    }


    /**
     * 手机访问联系人权限
     */
    public void checkContactPermission() {
        final String waring = getResources().getString(R.string.contacts_waring_msg);
        ((BaseActivity) getActivity()).requesDevicePermission(waring, 0x0011, new PermissionResult() {

                    @Override
                    public void requestSuccess() {
                        startActivityForResult(new Intent(Intent.ACTION_PICK,
                                ContactsContract.Contacts.CONTENT_URI), 0);
                        uploadContacts();
                    }

                    @Override
                    public void requestFailed() {
                        ((BaseActivity) getActivity()).showMissingPermissionDialog(waring);
                    }
                },
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE);
    }

    private void uploadContacts() {
        try {
            List<Contact> contacts = Contacts.getQuery().find();
            ContactsRequest contactsRequest = new ContactsRequest();
            List<ContactsRequest.ContactsBean> list = new ArrayList<ContactsRequest.ContactsBean>();
            for (Contact c : contacts) {
                ContactsRequest.ContactsBean bean = new ContactsRequest.ContactsBean();
                bean.setName(c.getBestDisplayName());
                List<String> listString = new ArrayList<String>();
                for (PhoneNumber p : c.getPhoneNumbers()) {
                    listString.add(p.getNormalizedNumber() + ",");
                }
                bean.setPhone_nums(listString);
                list.add(bean);
            }
            contactsRequest.setContacts(list);
            if (list == null || list.size() == 0) {
                return;
            }

            HttpUtil.getInstance(getActivity()).httpsPost(contactsRequest, ReqAction.APPLICATION_ADDRESSBOOK_CREATE, ContactsResponse.class, new ICallback<ContactsResponse>() {
                @Override
                public void success(ContactsResponse contactsResponse) {
                    int contactsCode = contactsResponse.getCode();
                    if (contactsCode==5031) {
                        callback.showMaintainDialog();
                    }
                }

                @Override
                public void failture(String tips) {
                    CLogUtil.showToast(getActivity(), tips);
                    RyxCreditLoadDialog.getInstance(getActivity()).dismiss();
                }
            }, new UICallBack() {
                @Override
                public void complete() {

                }
            });
        }catch (Exception e) {
          //  CLogUtil.showToast(getActivity(), "请到系统设置里面打开读取联系人的权限");
            final String waring = getResources().getString(R.string.contacts_waring_msg);
            ((BaseActivity) getActivity()).showMissingPermissionDialog(waring);
        }
    }


    private void uploadContactsClickNextButton() {
        try {
            List<Contact> contacts = Contacts.getQuery().find();
            ContactsRequest contactsRequest = new ContactsRequest();
            List<ContactsRequest.ContactsBean> list = new ArrayList<ContactsRequest.ContactsBean>();
            for (Contact c : contacts) {
                ContactsRequest.ContactsBean bean = new ContactsRequest.ContactsBean();
                bean.setName(c.getBestDisplayName());
                List<String> listString = new ArrayList<String>();
                for (PhoneNumber p : c.getPhoneNumbers()) {
                    listString.add(p.getNormalizedNumber() + ",");
                }
                bean.setPhone_nums(listString);
                list.add(bean);
            }
            contactsRequest.setContacts(list);
            if (list == null || list.size() == 0) {
                return;
            }

            HttpUtil.getInstance(getActivity()).httpsPost(contactsRequest, ReqAction.APPLICATION_ADDRESSBOOK_CREATE, ContactsResponse.class, new ICallback<ContactsResponse>() {
                @Override
                public void success(ContactsResponse contactsResponse) {
                    int contactsCode = contactsResponse.getCode();
                    if (contactsCode==5031) {
                        callback.showMaintainDialog();
                    }
                }

                @Override
                public void failture(String tips) {
                    CLogUtil.showToast(getActivity(), tips);
                    RyxCreditLoadDialog.getInstance(getActivity()).dismiss();
                }
            }, new UICallBack() {
                @Override
                public void complete() {

                }
            });
        }catch (Exception e) {
            final String waring = getResources().getString(R.string.contacts_waring_msg);
            ((BaseActivity) getActivity()).showMissingPermissionDialog(waring);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        savePersonalInfo();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            setAccount(data);
        }*/
        switch (resultCode){
            case Activity.RESULT_OK:
                if (data == null) {
                    return;
                }
                //处理返回的data,获取选择的联系人信息
                Uri uri=data.getData();
                String[] contacts=getPhoneContacts(uri);

                try {
                    //relationshipNameEt.setText(contacts[0]);
                    if (relationshipNameEt.getText().toString().trim()!=null){
                        mobileNumber = (contacts[1]).replace("+86", "").replace(" ", "").replace("-", "");
                    if (TextUtils.isEmpty(mobileNumber)) {
                        CLogUtil.showToast(getActivity(), "联系人手机号码不能为空");
                    }
                    /*else if(!phonNorexgStr(contacts[1])) {
                        CLogUtil.showToast(getActivity(), "请输入正确手机号");
                    }*/
                    else if(mobileNumber.equalsIgnoreCase(RyxcreditConfig.getPhoneNo())) {
                        CLogUtil.showToast(getActivity(), "申请人不得与联系人为同一人哦，请您重新选择");
                    }else {
                        mobileNumber = (contacts[1]).replace("+86", "").replace(" ", "").replace("-", "");
                        relationshipNameEt.setText(contacts[0]);
                        relationshipPhoneEt.setText(mobileNumber);
                    }
                    }
                }catch (Exception e) {
                    final String waring = getResources().getString(R.string.contacts_waring_msg);
                    ((BaseActivity) getActivity()).showMissingPermissionDialog(waring);
                }
            //    setAccount(data);
                break;
            default:
                break;

        }
    }

    private void setAccount(Intent intent) {
        if (intent == null) {
            return;
        } else {
            ArrayList<String> tPhone = ContactHelper.getContactPhoneNo(intent.getData(), getActivity());
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
                        String mobileNumber = phoneNumber.replace("+86", "").replace(" ", "").replace("-", "");
                        relationshipPhoneEt.setText(mobileNumber);
                        relationshipPhoneEt.setSelection(mobileNumber.length());
                    }

                    @Override
                    public void onNeutralActionClicked(DialogFragment fragment) {
                        fragment.dismiss();
                    }
                };
                builder.items(mPhoneString, 0).title("请选择联系人电话号码").positiveAction("确定").negativeAction("取消");
                DialogFragment fragment = DialogFragment.newInstance(builder);
//                fragment.show(getActivity().getSupportFragmentManager(), null);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(fragment, null);
                ft.commitAllowingStateLoss();
            } else if (tPhone.size() == 1) {
                String mobileNumber = tPhone.get(0).replace("+86", "").replace(" ", "").replace("-", "");
                relationshipPhoneEt.setText(mobileNumber);
                relationshipPhoneEt.setSelection(mobileNumber.length());
            } else {
                relationshipPhoneEt.setText("");
            }
        }
    }

    @Override
    public void phone(String phone) {
        relationshipPhoneEt.setText(phone);
    }

    private String[] getPhoneContacts(Uri uri){
        String[] contact=new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContext().getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor=cr.query(uri,null,null,null,null);

            int count = cursor.getCount();


            if(count > 0 && cursor != null){
                try {
                    while (cursor.moveToFirst()) {
                        //取得联系人姓名
                        int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                        contact[0] = cursor.getString(nameFieldColumnIndex);
                        //取得电话号码
                        String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
                        if (phone.getCount() > 0) {
                            phone.moveToFirst();
                            contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        phone.close();
                        cursor.close();
                    }
                }catch (Exception e){

                }
            }else{
                return null;
            }


        return contact;
    }
}
