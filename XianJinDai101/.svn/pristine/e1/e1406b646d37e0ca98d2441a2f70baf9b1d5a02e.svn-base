package com.ryx.payment.ruishua.sjfx;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.utils.BitmapUntils;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.Map;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

import static com.ryx.payment.ruishua.RyxAppdata.getCurrentBranchMipmapLogoId;

/**
 * 设备申购二维码支付页面
 */
@EActivity(R.layout.activity_dev_purchase_qrcode)
public class DevPurchaseQrcodeActivity extends BaseActivity {
@ViewById(R.id.qrcodeallaout)
AutoLinearLayout qrcodeallaout;
    @ViewById(R.id.qrcodeimg)
    ImageView qrcodeimg;
    //展示订单页面
    private int MYORDERFLAG=0x908;
    @AfterViews
    public void initView(){
        setTitleLayout("设备申购支付",true,false);
        Intent intent=getIntent();
        String qrcodeUrl=intent.getStringExtra("qrcodeurl");
        createQrcodeImg(qrcodeUrl,qrcodeimg);
    }
    @Override
    public void  backUpImgOnclickListen(){
        setResult(MYORDERFLAG);
        super.backUpImgOnclickListen();
    }
    @Click(R.id.bt_saveqrcodeimg)
    public void btSaveqrcodeimgClick(){
        dirpermissionCheck();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                ) {
            setResult(MYORDERFLAG);
            finish();
        }
        return false;
    }
    /**
     * 根据内容生成二维码
     * @param content 二维码内容
     * @param qrcodeimg 展现二维码的Img
     */
    public void createQrcodeImg(final String content, final ImageView qrcodeimg){
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                //解码资源ID引用的图像。
                Bitmap logoBitMap= BitmapFactory.decodeResource(getResources(), getCurrentBranchMipmapLogoId());
                Bitmap qrcodeBitMap=  QRCodeEncoder.syncEncodeQRCode(content,600, Color.BLACK, Color.WHITE,logoBitMap);
                return qrcodeBitMap;
            }
            @Override
            protected void onPostExecute(Bitmap qrcodeBitMap) {
                try {
                    qrcodeimg.setImageBitmap(qrcodeBitMap);
                }catch (Exception e){
                    qrcodeimg.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.qrcodefail));
                }
            }
        }.execute();
    }
    /**
     *
     * 文件操作权限判断
     */
    public void dirpermissionCheck(){
        String waring = MessageFormat.format(getResources().getString(R.string.dirwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
        final String finalWaring = waring;
        requesDevicePermission(waring, 0x0021, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        StringBuffer stringBuffer=new StringBuffer();
                        stringBuffer.append(RyxAppdata.getInstance(DevPurchaseQrcodeActivity.this).getCurrentFileBranchRootName());
                        stringBuffer.append("PayQrCode");
                        //确定
                        //保存二维码图片到手机qrcodeallaout
                        Map<String,String> ssMap = BitmapUntils.saveQrcodeAsFile(qrcodeallaout,
                                stringBuffer.toString());
                        if(TextUtils.isEmpty(ssMap.get("path"))||TextUtils.isEmpty(ssMap.get("fileName"))){
                            LogUtil.showToast(DevPurchaseQrcodeActivity.this,  ssMap.get("result"));
                            return;
                        }
                        // 其次把文件插入到系统图库
                        try {
                            MediaStore.Images.Media.insertImage(DevPurchaseQrcodeActivity.this.getContentResolver(),
                                    ssMap.get("path") + File.separator + ssMap.get("fileName")
                                            + ".jpg", ssMap.get("fileName"), null);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        // 最后通知图库更新
                        DevPurchaseQrcodeActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + ssMap.get("path") + File.separator + ssMap.get("fileName")
                                + ".jpg")));
                        LogUtil.showToast(DevPurchaseQrcodeActivity.this, ssMap.get("result"));
                    }
                    @Override
                    public void requestFailed() {
                        LogUtil.showToast(DevPurchaseQrcodeActivity.this, finalWaring);
                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}
