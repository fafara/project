package com.ryx.payment.ruishua.authenticate.newauthenticate;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.utils.BitmapUntils;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.swiper.utils.CryptoUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.MessageFormat;

import exocr.engine.DataCallBack;
import exocr.engine.EngineManager;
import exocr.exocrengine.EXIDCardResult;
import exocr.idcard.IDCardManager;

/**
 * 身份证图片上传
 */
@EActivity(R.layout.activity_id_card_upload)
public class IdCardUploadAct extends BaseActivity {
    @ViewById
    ImageView img_front_idcard,img_back_idcard;
    private int selectiv;
    private Bitmap idCardfrontImg,idCardBackImg;
    private String addresss,cardnum,userName,validDate;
private int TOIDCARDMSGACT=0x0010;
    @AfterViews
    public void initView(){
        setTitleLayout("实名认证",true,false);
        EngineManager.getInstance().initEngine(this);
    }
//    @Override
//    public void backUpImgOnclickListen(){
//        setResult(RyxAppconfig.CLOSE_ALL);
//        finish();
//    }
    @Click(R.id.btn_next)
    public void BtnNextClick(){
        if(disabledTimerAnyView()){
            return;
        }
        if(TextUtils.isEmpty(addresss)||TextUtils.isEmpty(cardnum)||TextUtils.isEmpty(userName)||idCardfrontImg==null){
            LogUtil.showToast(IdCardUploadAct.this,"请正确拍摄身份证正面照!");
            return;
        }
        if(TextUtils.isEmpty(validDate)||idCardBackImg==null){
            LogUtil.showToast(IdCardUploadAct.this,"请正确拍摄身份证国徽面!");
            return;
        }
      byte[] idCardFrontbyte= BitmapUntils.getContent(idCardfrontImg);
     String idCardFrontStr=   BitmapUntils.bytesToHexString(idCardFrontbyte);
    String idcardFrontMd5= CryptoUtils.getInstance().EncodeDigest(idCardFrontbyte);

      byte[] idCardBackbyte= BitmapUntils.getContent(idCardBackImg);
        String idCardBackbyteMd5= CryptoUtils.getInstance().EncodeDigest(idCardBackbyte);
     String idCardBackStr=   BitmapUntils.bytesToHexString(idCardBackbyte);
        Intent intent=new Intent(IdCardUploadAct.this,IdCardMsgAct_.class);
        intent.putExtra("idCardFront",idCardFrontStr);
        intent.putExtra("idcardFrontMd5",idcardFrontMd5);
        intent.putExtra("idCardBack",idCardBackStr);
        intent.putExtra("idCardBackbyteMd5",idCardBackbyteMd5);
//        intent.putExtra("addresss",addresss);
        intent.putExtra("cardnum",cardnum);
        intent.putExtra("userName",userName);
        intent.putExtra("validDate",validDate);
        startActivityForResult(intent,TOIDCARDMSGACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RyxAppconfig.CLOSE_NEWAUTHRESULT_RSP){
            setResult(RyxAppconfig.CLOSE_NEWAUTHRESULT_RSP);
            finish();
        }
    }

    /**
     * 正面身份证
     */
    @Click(R.id.img_front_idcard)
    public void imgFrontIdcardClick(){
        selectiv=1;
        takephoto(new CompleteResultListen() {
            @Override
            public void compleResultok() {
                takeIdCardFrontPhoto();
            }
        });

    }


    /**
     * 反面身份证
     */
    @Click(R.id.img_back_idcard)
    public void imgBackIdcard(){
        selectiv=2;
        takephoto(new CompleteResultListen() {
            @Override
            public void compleResultok() {
                takeIdCardBlackPhoto();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EngineManager.getInstance().finishEngine();

    }

    /**
     * 正面照
     */
    private void takeIdCardFrontPhoto() {
            IDCardManager.getInstance().setFront(true);
            IDCardManager.getInstance().setShowPhoto(false);
            IDCardManager.getInstance().setShowLogo(false);
            IDCardManager.getInstance().recognize(new DataCallBack() {
              @Override
              public void onCardDetected(boolean issuccess) {
                  if (issuccess) {
                      IDCardManager.getInstance().stopRecognize();
                      EXIDCardResult result = IDCardManager.getInstance().getResult();
                      idCardfrontImg = result.stdCardIm;
                     userName= result.name;
                      addresss = result.address;
                      cardnum=result.cardnum;
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              img_front_idcard.setImageBitmap(idCardfrontImg);
                          }
                      });


                      }


              }

              @Override
              public void onCameraDenied() {
                  LogUtil.showToast(IdCardUploadAct.this,"相机初始化失败,请检查相机权限是否正常!");

              }
            }, IdCardUploadAct.this);

    }

    /**
     * 背面照
     */
    private void takeIdCardBlackPhoto(){
        IDCardManager.getInstance().setFront(false);
        IDCardManager.getInstance().setShowPhoto(false);
        IDCardManager.getInstance().setShowLogo(false);
        IDCardManager.getInstance().recognize(new DataCallBack() {
            @Override
            public void onCardDetected(boolean issuccess) {
                if(issuccess){
                    IDCardManager.getInstance().stopRecognize();
                    EXIDCardResult result = IDCardManager.getInstance().getResult();
                  String    validDateVar = result.validdate;
                    if(!TextUtils.isEmpty(validDateVar)){
                        validDate = validDateVar.replace("/","").replace("-","");
                    }
                    idCardBackImg = result.stdCardIm;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img_back_idcard.setImageBitmap(idCardBackImg);
                        }
                    });
//                    if (!TextUtils.isEmpty(validDate)) {
//                        if(validDate.contains("-")){
//                            String[] strs=validDate.split("-");
//                            if(strs!=null&&strs.length>0) {
//                                validDateStart = strs[0];
//                                validDateEnd = strs[1];
//                                if(validDateStart.contains("/")){
//                                    validDateStart = validDateStart.replace("/","");
//                                }
//                                if(validDateEnd.contains("/")){
//                                    validDateEnd =  validDateEnd.replace("/","");
//                                }
//                            }
//                        }
//                    }
                }
            }

            @Override
            public void onCameraDenied() {
                LogUtil.showToast(IdCardUploadAct.this,"相机初始化失败,请检查相机权限是否正常!");
            }
        },IdCardUploadAct.this);
    }

    public void takephoto(final CompleteResultListen completeResultListen) {
        String waring = MessageFormat.format(getResources().getString(R.string.camerawaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
        requesDevicePermission(waring, 0x00011, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        requestStrorage(completeResultListen);
                    }

                    @Override
                    public void requestFailed() {

                    }
                },
                Manifest.permission.CAMERA);

    }

    private void requestStrorage(final CompleteResultListen completeResultListen) {
        String waring = MessageFormat.format(getResources().getString(R.string.dirwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
        requesDevicePermission(waring, 0x00012, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        completeResultListen.compleResultok();
                    }

                    @Override
                    public void requestFailed() {
                    }
                }

                ,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

}
