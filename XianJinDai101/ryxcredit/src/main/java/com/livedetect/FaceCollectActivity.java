package com.livedetect;

import android.Manifest;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.livedetect.data.ConstantValues;
import com.livedetect.utils.FileUtils;
import com.livedetect.utils.LogUtil;
import com.livedetect.utils.SerializableObjectForData;
import com.livedetect.utils.StringUtils;
import com.rey.material.widget.Button;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.RyxcreditConfig;
import com.ryx.ryxcredit.activity.BaseActivity;
import com.ryx.ryxcredit.beans.bussiness.loanapply.CLoanApplyRequest;
import com.ryx.ryxcredit.utils.CLogUtil;
import com.ryx.ryxcredit.utils.PermissionResult;
import com.zhy.autolayout.AutoLinearLayout;

import java.text.MessageFormat;


/**
 * 主类
 *
 * @ClassName: MainActivity
 * @Description:TODO
 */
public class FaceCollectActivity extends BaseActivity implements Callback {

    private Button startImg;
    private ImageView iv_return;
    private Handler mHandler;
    private final int START_LIVEDETECT = 0;
    private final String TAG = FaceCollectActivity.class.getSimpleName();
    public SerializableObjectForData mSerializableObjectForData;
    public static final int IDENTIFY_FAIL_CODE = 0X1005;
    private AutoLinearLayout lay_fail, lay_suc, lay_main;
    private int showPage = 1;//1:首页；2：成功；3:失败
    private CLoanApplyRequest applyRequest;//借款请求
    private String flag="1";//1:激活页面；2：借款页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Integer.parseInt(VERSION.SDK) >= 14) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
        setContentView(R.layout.htjc_activity_main);
        mSerializableObjectForData = new SerializableObjectForData();
        mHandler = new Handler(this);
        initConfig();
        initView();
        Intent intent = getIntent();
        if(intent!=null){
            if(intent.hasExtra("applyRequest")){
                flag = "2";
                applyRequest = (CLoanApplyRequest)intent.getSerializableExtra("applyRequest");
            }else{
                flag = "1";
            }
        }

    }

    private void initConfig() {
        FileUtils.init(this);
    }

    private void initView() {
        iv_return = (ImageView) findViewById(R.id.iv_return);
        iv_return.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initBack();
            }
        });
        startImg = (Button) findViewById(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_ID, "iv_start"));
        startImg.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                takephoto();
            }
        });
        lay_suc = (AutoLinearLayout) findViewById(R.id.lay_suc);
        lay_fail = (AutoLinearLayout) findViewById(R.id.lay_fail);
        lay_main = (AutoLinearLayout) findViewById(R.id.lay_main);
    }

    public void takephoto() {
        String waring = MessageFormat.format(getResources().getString(R.string.camerawaringmsg), RyxcreditConfig.getCurrentBranchName());
        requesDevicePermission(waring, 0x00011, new PermissionResult() {
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
        requesDevicePermission(waring, 0x00012, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                            Intent intent = new Intent(FaceCollectActivity.this,
                                    LiveDetectActivity.class);
                            Bundle bundle = new Bundle();
                            intent.putExtras(bundle);
                            startActivityForResult(intent, START_LIVEDETECT);
                        }
                    }

                    @Override
                    public void requestFailed() {

                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode && 0 == event.getRepeatCount()) {
            initBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initBack() {
        if (showPage == 2) {
            showMain();
        } else if (showPage == 3) {
            showMain();
        } else {
            finish();
        }
    }

    private void showMain() {
        showPage = 1;
        lay_main.setVisibility(View.VISIBLE);
        lay_suc.setVisibility(View.GONE);
        lay_fail.setVisibility(View.GONE);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case ConstantValues.LIVE_CALLBACK_10:
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
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
        FaceCollectSucUtil.getInstance().initSucPage(FaceCollectActivity.this, result,applyRequest);
    }

    public void showFailWindow(Bundle result) {
        showPage = 3;
        lay_main.setVisibility(View.GONE);
        lay_suc.setVisibility(View.GONE);
        lay_fail.setVisibility(View.VISIBLE);
        FaceCollectFailUtil.getInstance().initFailWindow(FaceCollectActivity.this, result);
    }


}
