package com.livedetect;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.PhoneNumber;
import com.livedetect.base.CIdentifyRequest;
import com.livedetect.base.CIdentifyResponse;
import com.livedetect.utils.FileUtils;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.beans.ReqAction;
import com.ryx.ryxcredit.beans.bussiness.activateline.CActivateLimitRequest;
import com.ryx.ryxcredit.beans.bussiness.activateline.CActivateLimitResponse;
import com.ryx.ryxcredit.beans.bussiness.addressbook.ContactsRequest;
import com.ryx.ryxcredit.beans.bussiness.addressbook.ContactsResponse;
import com.ryx.ryxcredit.beans.bussiness.loanapply.CLoanApplyRequest;
import com.ryx.ryxcredit.beans.bussiness.loanapply.CLoanApplyResponse;
import com.ryx.ryxcredit.services.ICallback;
import com.ryx.ryxcredit.utils.Base64Utils;
import com.ryx.ryxcredit.utils.CCommonDialog;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.PermissionResult;

import java.util.ArrayList;
import java.util.List;

public class FaceCollectSucUtil {
    private final String TAG = FaceCollectSucUtil.class.getSimpleName();
    private ImageView mReturnBtn, success_img;
    private ImageView returnImg;
    private ImageView mAgainImg;

    //    private Button btn_server;
    private Button btn_idenfy;
    private String dirPicSave = FileUtils.getSdcardPath() + "/DCIM/";
    private Bitmap bitmap;
    private byte[] pic_result;
    public static final int IDENTIFY_CODE = 0X1004;
    public static final int BORROW_RESULT_CODE = 0X1008;
    private Context mcontext;
    private String flag = "1";//1:激活页面；2：借款页面

    private static FaceCollectSucUtil instance;
    private CLoanApplyRequest mapplyRequest;//借款需要参数

    public static FaceCollectSucUtil getInstance() {
        if (instance == null) {
            instance = new FaceCollectSucUtil();
        }
        return instance;
    }

    public void initSucPage(Context context, Bundle result, CLoanApplyRequest applyRequest) {
        FileUtils.init(context);
        mcontext = context;
        dirPicSave = FileUtils.getSdcardPath() + "/DCIM/";
        //激活页面
        if (applyRequest == null) {
            flag = "1";
        }
        //借款页面
        else {
            mapplyRequest = applyRequest;
            flag = "2";
        }
        initView();
        boolean check_pass = result.getBoolean("check_pass");
        if (check_pass) {
            pic_result = result.getByteArray("pic_result");
            if (pic_result != null) {
                bitmap = FileUtils.getBitmapByBytesAndScale(pic_result, 1);
                if (null != bitmap) {
                    success_img.setImageBitmap(bitmap);
                }
            } else {
                CLogUtil.showToast(mcontext, "识别失败！");
            }
        }
    }

    private void initView() {
        success_img = (ImageView) ((FaceCollectActivity) mcontext).findViewById(R.id.success_img);
        btn_idenfy = (Button) ((FaceCollectActivity) mcontext).findViewById(R.id.btn_idenfy);
        if("1".equals(flag)){
            btn_idenfy.setText("激活");
        }else{
            btn_idenfy.setText("确认借款");
        }
        btn_idenfy.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                CIdentifyRequest request = new CIdentifyRequest();
                if (pic_result == null) {
                    return;
                }
                request.setImage_data(Base64Utils.encode(pic_result));
                ((FaceCollectActivity) mcontext).httpsPost(mcontext, request, ReqAction.FACE_IDENTIFY, CIdentifyResponse.class, new ICallback<CIdentifyResponse>() {
                    @Override
                    public void success(CIdentifyResponse cResponse) {
                        CIdentifyResponse.ResultBean result = cResponse.getResult();
                        if (result != null) {
                            String score = result.getScore();
                            String face_id = result.getFace_id();
                            if ("1".equals(flag)) {
                                doActive(face_id, score);
                            } else {
                                doBorrow(face_id, score, mapplyRequest);
                            }
                        }
                    }

                    @Override
                    public void failture(String tips) {
                        CCommonDialog.showRepaymentError(mcontext, "提交失败", tips + "");
                    }
                });
            }
        });
    }

    private void doBorrow(String face_id, String score, CLoanApplyRequest mapplyRequest) {
        mapplyRequest.setFace_id(face_id);
        mapplyRequest.setScore(score);
        ((FaceCollectActivity) mcontext).httpsPost((FaceCollectActivity) mcontext, mapplyRequest, ReqAction.APPLICATION_LOAN_APPLY, CLoanApplyResponse.class, new ICallback<CLoanApplyResponse>() {
            @Override
            public void success(CLoanApplyResponse cLoanApplyResponse) {
                if (!TextUtils.isEmpty(cLoanApplyResponse.getResult())) {
                    CCommonDialog.showRepaymentOK((FaceCollectActivity) mcontext,
                            "借款申请成功", "请随时关注银行卡到账信息！",
                            new CCommonDialog.IMessage() {
                                @Override
                                public void callback() {
                                    checkContactPermission();
                                }
                            });

                } else {
                    CLogUtil.showToast(mcontext,"借款失败！");
                    ((FaceCollectActivity) mcontext).setResult(BORROW_RESULT_CODE);
                    ((FaceCollectActivity) mcontext).finish();
                }
            }

            @Override
            public void failture(String tips) {
                CLogUtil.showToast(mcontext,tips);
                ((FaceCollectActivity) mcontext).setResult(BORROW_RESULT_CODE);
                ((FaceCollectActivity) mcontext).finish();
            }
        });
    }

    /**
     * 手机访问联系人权限
     */
    public void checkContactPermission() {
        final String waring = ((FaceCollectActivity) mcontext).getResources().getString(R.string.contacts_waring_msg);
        ((FaceCollectActivity) mcontext).requesDevicePermission(waring, 0x0011, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        CLogUtil.showLog("checkContactPermission---", "requestSuccess");
                        uploadContacts();
                    }

                    @Override
                    public void requestFailed() {
                        CLogUtil.showLog("checkContactPermission---", "requestFailed");
                        ((FaceCollectActivity) mcontext).setResult(BORROW_RESULT_CODE);
                        ((FaceCollectActivity) mcontext).finish();
                    }
                },
                Manifest.permission.READ_CONTACTS);
    }

    //上传通讯录
    private void uploadContacts() {
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
            ((FaceCollectActivity) mcontext).setResult(BORROW_RESULT_CODE);
            ((FaceCollectActivity) mcontext).finish();
            return;
        }
        ((FaceCollectActivity) mcontext).httpsPost((FaceCollectActivity) mcontext, contactsRequest, ReqAction.APPLICATION_ADDRESSBOOK_CREATE,
                ContactsResponse.class, new ICallback<ContactsResponse>() {
                    @Override
                    public void success(ContactsResponse contactsResponse) {
                        CLogUtil.showLog("checkContactPermission---", "uploadContacts--success");
                        ((FaceCollectActivity) mcontext).setResult(BORROW_RESULT_CODE);
                        ((FaceCollectActivity) mcontext).finish();
                    }

                    @Override
                    public void failture(String tips) {
                        CLogUtil.showLog("checkContactPermission---", "uploadContacts--failture");
                        ((FaceCollectActivity) mcontext).setResult(BORROW_RESULT_CODE);
                        ((FaceCollectActivity) mcontext).finish();
                    }
                });
    }

    private void doActive(String face_id, String score) {
        CActivateLimitRequest request = new CActivateLimitRequest();
        request.setFace_id(face_id);
        request.setScore(score);
        ((FaceCollectActivity) mcontext).httpsPost(mcontext, request, ReqAction.APPLICATION_LOAN_APPLY_LIMIT, CActivateLimitResponse.class, new ICallback<CActivateLimitResponse>() {
            @Override
            public void success(CActivateLimitResponse cActivateLimitResponse) {
                CCommonDialog.showRepaymentOK(mcontext, "提交成功", "您的申请已提交，我们会尽快处理！", new CCommonDialog.IMessage() {
                    @Override
                    public void callback() {
                        ((FaceCollectActivity) mcontext).setResult(IDENTIFY_CODE);
                        ((FaceCollectActivity) mcontext).finish();

                    }
                });
            }

            @Override
            public void failture(String tips) {
                CCommonDialog.showRepaymentError(mcontext, "提交失败", tips + "");
            }
        });
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    protected void onPause() {
        FileUtils.setmContext(null);
    }
}
