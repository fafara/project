package com.ryx.payment.ruishua.activity;

import com.ryx.payment.ruishua.R;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
//    MediaPlayer mediaPlayerfail;
//    private final String TAG32 = "FaceLiveDetectSDK32";
//    private String mStr = "";
//    private byte[] mSuiJiShuShuJu;
//    private Short mSuiJiShuShuJuChangDu;
//    private Bitmap bmPhoto;
//    private int seriNo = 1;
//    @AfterViews
//    public void initViews() {
//        initQtPatParams();
//        mediaPlayerfail = MediaPlayer.create(getApplicationContext(),
//                ToolsUtilty.getResIdByTypeAndName(getApplicationContext(),
//                        "raw", "ctidfail"));
//        mediaPlayerfail = MediaPlayer.create(getApplicationContext(),
//                ToolsUtilty.getResIdByTypeAndName(getApplicationContext(), "raw", "ctidfail"));
//// imgPhoto = (ImageView) findViewById(R.id.img_photo);
//        imgLogo = (ImageView) findViewById(R.id.img_logo);
//        img_success = (ImageView) findViewById(R.id.img_success);
//        img_fail = (ImageView) findViewById(R.id.img_fail);
//        img_logo1 = (ImageView) findViewById(R.id.img_logo1);
//        txtMessage = (TextView) findViewById(R.id.txt_message);
//        vercode = (TextView) findViewById(R.id.vercode);
//        vercode.setText("版本号:" + CTIDLiveDetectActivity.getVersion());
//        infotest = (TextView) findViewById(R.id.infotest);
//        btnStartCheck = (Button) findViewById(R.id.btn_start);
//        mInfoNoPassreazionTv = (TextView) findViewById(R.id.infotestrezion);
//        mInfoNoPassreazionTv2 = (TextView) findViewById(R.id.infotestrezion2);
//        btnStartCheck.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                img_logo1.setVisibility(View.VISIBLE);
//                imgLogo.setVisibility(View.INVISIBLE);
//                infotest.setVisibility(View.GONE);
//                img_success.setVisibility(View.GONE);
//                img_fail.setVisibility(View.GONE);
//                mInfoNoPassreazionTv.setVisibility(View.INVISIBLE);
//                mInfoNoPassreazionTv.setText("");
//                mInfoNoPassreazionTv2.setVisibility(View.INVISIBLE);
//                mInfoNoPassreazionTv2.setText("");
//                startLiveDect();
//            }
//        });
//    }
//
//    private void startLiveDect() {
//        Intent intent = new Intent();
//        intent.setClass(MainActivity.this, CTIDLiveDetectActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("mIdCodStr", mIdCodStr);
//        intent.putExtras(bundle);
//        startActivityForResult(intent, 20);
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 20) {
//            // randomNomber = new byte[1];
//            if (resultCode == Activity.RESULT_OK && data != null) {
//
//                Bundle result = data.getBundleExtra("result");
//                boolean check_pass = result.getBoolean("check_pass");
//
//                String mBadReason = result.getString("mBadReason");
//
//                if (check_pass) {
//                    img_logo1.setVisibility(View.GONE);
//                    imgLogo.setVisibility(View.VISIBLE);
//                    byte[] pic_thumbnail = result.getByteArray("pic_thumbnail");
//                    byte[] pic_result = result.getByteArray("encryption");
//                    // String pic_encryption = result.getString("encryption");
//                    // for (byte b: pic_result) {
//                    //
//                    // Log.d(TAG32, b+"");
//                    // }
//                    // Log.i("mBadReason", "mBadReason=");
//                    if (pic_thumbnail != null && pic_result != null) {
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(
//                                pic_thumbnail, 0, pic_thumbnail.length);
//                        bmPhoto = bitmap;
//                        imgLogo.setImageBitmap(bitmap);
//                        infotest.setVisibility(View.VISIBLE);
//                        img_success.setVisibility(View.VISIBLE);
//                        img_fail.setVisibility(View.GONE);
//                        infotest.setText("照片采集成功");
//                        btnStartCheck.setVisibility(View.GONE);
//
//                    } else {
//                        img_fail.setVisibility(View.VISIBLE);
//                        img_success.setVisibility(View.GONE);
//                        infotest.setVisibility(View.VISIBLE);
//
//                        infotest.setText("抱歉！您的动作不符合");
//                        img_logo1.setVisibility(View.VISIBLE);
//                        imgLogo.setVisibility(View.GONE);
//                        imgLogo.setImageResource(R.drawable.ctid_forntali_just);
//
//                    }
//
//                } else {
//                    img_fail.setVisibility(View.VISIBLE);
//                    img_success.setVisibility(View.GONE);
//                    infotest.setVisibility(View.VISIBLE);
//                    if (mBadReason.equalsIgnoreCase("001")) {
//
//                        infotest.setText("抱歉！请确保人脸始终在屏幕中");
//
//                    } else if (mBadReason.equalsIgnoreCase("002")) {
//                        infotest.setText("抱歉！请确保屏幕中只有一张脸");
//                    } else if (mBadReason.equalsIgnoreCase("003")) {
//                        infotest.setText("抱歉！您的动作不符合");
//                    } else if (mBadReason.equalsIgnoreCase("004")) {
//                        infotest.setText("抱歉！您的照片损坏太大");
//                    } else if (mBadReason.equalsIgnoreCase("005")) {
//                        infotest.setText("抱歉！您周围的环境光线过暗");
//                    } else if (mBadReason.equalsIgnoreCase("006")) {
//                        infotest.setText("抱歉！您周围的环境光线过亮");
//                    } else if (mBadReason.equalsIgnoreCase("007")) {
//                        infotest.setText("活检受到攻击");
//                    } else if (mBadReason.equalsIgnoreCase("008")) {
//                        infotest.setText("抱歉！超时");
//                    } else if (mBadReason.equalsIgnoreCase("009")) {
//                        infotest.setText("获取随机数失败");
//                    } else if (mBadReason.equalsIgnoreCase("101")) {
//                        infotest.setText("抱歉！请您保持静止不动");
//                    } else if (mBadReason.equalsIgnoreCase("010")) {
//                        infotest.setText("获取认证证书失败");
//                    } else {
//                        infotest.setText("抱歉！您的动作不符合");
//                    }
//
//                    img_logo1.setVisibility(View.VISIBLE);
//                    imgLogo.setVisibility(View.GONE);
//                    imgLogo.setImageResource(R.drawable.ctid_forntali_just);
//
//                }
//
//            }
//
//        } else {
//
//            // Toast.makeText(getApplicationContext(), "null2", 0).show();
//        }
//    }
//
//    private  TextView  txtTip,  txtMessage,  infotest,  vercode,  mInfoNoPassreazionTv,
//            mInfoNoPassreazionTv2;
//    private ImageView imgPhoto, imgLogo, img_success, img_fail, img_logo1;
//    private Button btnStartCheck, mReinputBtn;
//    private EditText mInputIdCodEdt;
//    private static String mIdCodStr;
//    private int miIdCodStr = 0;
//    private static boolean isNet = false;

}
