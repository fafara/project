package com.ryx.ryxcredit.newfragment.baseinfo.boss;


import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.livedetect.FaceCollectActivity;
import com.livedetect.LiveDetectActivity;
import com.livedetect.RKDFaceCollectFailUtil;
import com.livedetect.RKDFaceCollectSucUtil;
import com.livedetect.data.ConstantValues;
import com.livedetect.utils.FileUtils;
import com.livedetect.utils.LogUtil;
import com.livedetect.utils.SerializableObjectForData;
import com.livedetect.utils.StringUtils;
import com.rey.material.widget.Button;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.newfragment.baseinfo.FaceCollectFragment;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.PermissionResult;
import com.ryx.ryxcredit.xjd.RYDBossBaseInfoActivity;
import com.zhy.autolayout.AutoLinearLayout;

import java.text.MessageFormat;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class RYDBossFaceCollectFragment extends Fragment {
    private RYDBossBaseInfoActivity baseInfoActivity;
    private Button startImg;
    private Handler mHandler;
    private View rootView;
    private final int START_LIVEDETECT = 0;
    private final String TAG = FaceCollectActivity.class.getSimpleName();
    public SerializableObjectForData mSerializableObjectForData;
    public static final int IDENTIFY_FAIL_CODE = 0X1005;
    private AutoLinearLayout lay_fail, lay_suc, lay_main;
    private int showPage = 1;//1:首页；2：成功；3:失败
    private static FaceCollectFragment instance;
    private String productId ;
    private String user_level;
    private String credit_type ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseInfoActivity = (RYDBossBaseInfoActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_rydboss_face_collect, container, false);
        if (Integer.parseInt(Build.VERSION.SDK) >= 14) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        mSerializableObjectForData = new SerializableObjectForData();
        productId = getArguments().getString("productId");
        user_level = getArguments().getString("user_level");
        credit_type = getArguments().getString("credit_type");
        initConfig();
        initView();
        baseInfoActivity = (RYDBossBaseInfoActivity) getActivity();
        return rootView;
    }

    private void initConfig() {
        FileUtils.init(getContext());
    }

    private void initView() {
        startImg = (Button) rootView.findViewById(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_ID, "iv_start"));
        startImg.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                takephoto();
            }
        });
        lay_suc = (AutoLinearLayout) rootView.findViewById(R.id.lay_suc);
        lay_fail = (AutoLinearLayout) rootView.findViewById(R.id.lay_fail);
        lay_main = (AutoLinearLayout)rootView. findViewById(R.id.lay_main);
    }

    public void takephoto() {
        String waring = MessageFormat.format(getResources().getString(R.string.camerawaringmsg), RyxcreditConfig.getCurrentBranchName());
        baseInfoActivity.requesDevicePermission(waring, 0x00011, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        requestStrorage();
                    }

                    @Override
                    public void requestFailed() {

                    }
                },
                Manifest.permission.CAMERA);

    }

    private void requestStrorage() {
        String waring = MessageFormat.format(getResources().getString(R.string.dirwaringmsg), RyxcreditConfig.getCurrentBranchName());
        baseInfoActivity.requesDevicePermission(waring, 0x00012, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                            Intent intent = new Intent(getContext(),
                                    LiveDetectActivity.class);
                            Bundle bundle = new Bundle();
                            intent.putExtras(bundle);
                            baseInfoActivity.startActivityForResult(intent, START_LIVEDETECT);
                        }
                    }

                    @Override
                    public void requestFailed() {

                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && 0 == event.getRepeatCount()) {
            initBack();
            return true;
        }
        return super.getActivity().onKeyDown(keyCode, event);
    }

    private void initBack() {
        if (showPage == 2) {
            showMain();
        } else if (showPage == 3) {
            showMain();
        } else {
            getActivity().finish();
        }
    }

    private void showMain() {
        showPage = 1;
        lay_main.setVisibility(View.VISIBLE);
        lay_suc.setVisibility(View.GONE);
        lay_fail.setVisibility(View.GONE);
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case ConstantValues.LIVE_CALLBACK_10:
                break;
            default:
                break;
        }
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        CLogUtil.showLog("onActivityResult----", " 109 requestCode = " + requestCode + " resultCode = " + resultCode + "---" + intent);
        if (requestCode == START_LIVEDETECT) {
            switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
                case RESULT_OK:
                    if (null != intent) {
                        Bundle result = intent.getBundleExtra("result");
                        if (null != result) {
                            //只有失败时返回  失败动作  l代表静止凝视动作  s代表摇头动作，n代表点头动作
                            String mMove = result.getString("mMove");
                            //只有失败时返回  失败的原因: 0代表默认失败提示，1代表无人脸，2代表多人脸，3代表活检动作失败，4代表错误动作的攻击，5代表超时，6代表图片加密失败，7代表3D检测失败，8代表肤色检测失败
                            String mRezion = result.getString("mRezion");
                            //活检是否通过 true代表检测通过，false代表检测失败
                            boolean isLivePassed = result.getBoolean("check_pass");
                            //图片的byte[]的形式，格式为jpg。失败时返回值为null
                            byte[] picbyte = result.getByteArray("pic_result");
                            if (StringUtils.isNotNull(mMove)) {
                                LogUtil.i(TAG, " mMove = " + mMove);
                            }
                            if (StringUtils.isNotNull(mRezion)) {//10为 初始化 失败 ，11为授权过期
                                LogUtil.i(TAG, " mRezion = " + mRezion);
                            }
                            LogUtil.i(TAG, " isLivePassed= " + isLivePassed);
                            if (null != picbyte) {
                                LogUtil.i(TAG, " picbyte = " + picbyte.length);
                            }
                            result.putString("productId",productId);
                            result.putString("user_level",user_level);
                            result.putString("credit_type",credit_type);
                            //识别成功
                            if (isLivePassed) {
                                showSucWindow(result);
                            } else {
                                showFailWindow(result);
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void showSucWindow(Bundle result) {
        showPage = 2;
        lay_main.setVisibility(View.GONE);
        lay_suc.setVisibility(View.VISIBLE);
        lay_fail.setVisibility(View.GONE);
        RKDFaceCollectSucUtil.getInstance().initSucPage(getActivity(), result);
    }

    public void showFailWindow(Bundle result) {
        showPage = 3;
        lay_main.setVisibility(View.GONE);
        lay_suc.setVisibility(View.GONE);
        lay_fail.setVisibility(View.VISIBLE);
        RKDFaceCollectFailUtil.getInstance().initFailWindow(getActivity(), result);
    }




}
