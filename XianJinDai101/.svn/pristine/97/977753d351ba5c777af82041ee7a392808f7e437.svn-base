package com.ryx.payment.payplug.base;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.utils.BitmapUntils;
import com.ryx.payment.ruishua.utils.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.Map;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

import static com.ryx.payment.ruishua.RyxAppdata.getCurrentBranchMipmapLogoId;

/**
 *瑞银信支付组件父类
 */
public class PayPlugBaseActivity extends BaseActivity {
    /**
     * 根据内容生成二维码
     * @param content 二维码内容
     * @param qrcodeimg 展现二维码的Img
     */
    public void createQrcodeImg(final String content, final ImageView qrcodeimg){
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap logoBitMap= BitmapFactory.decodeResource(getResources(), getCurrentBranchMipmapLogoId());
                Bitmap qrcodeBitMap=  QRCodeEncoder.syncEncodeQRCode(content,600, Color.BLACK, Color.WHITE,logoBitMap);
                return qrcodeBitMap;
            }
            @Override
            protected void onPostExecute(Bitmap qrcodeBitMap) {
                qrcodeimg.setImageBitmap(qrcodeBitMap);
            }
        }.execute();
    }


    /**
     *
     * 将布局文件保存于磁盘中
     */
    public void dirSaveView(final View qrcodeallaout){
        String waring = MessageFormat.format(getResources().getString(R.string.dirwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
        final String finalWaring = waring;
        requesDevicePermission(waring, 0x0021, new PermissionResult() {
                    @Override
                    public void requestSuccess() {
                        StringBuffer stringBuffer=new StringBuffer();
                        stringBuffer.append(RyxAppdata.getInstance(PayPlugBaseActivity.this).getCurrentFileBranchRootName());
                        stringBuffer.append("PayQrCode");
                        //保存二维码图片到手机qrcodeallaout
                        Map<String,String> ssMap = BitmapUntils.saveQrcodeAsFile(qrcodeallaout,
                                stringBuffer.toString());
                        if(TextUtils.isEmpty(ssMap.get("path"))||TextUtils.isEmpty(ssMap.get("fileName"))){
                            LogUtil.showToast(PayPlugBaseActivity.this,  ssMap.get("result"));
                            return;
                        }
                        // 其次把文件插入到系统图库
                        try {
                            MediaStore.Images.Media.insertImage(PayPlugBaseActivity.this.getContentResolver(),
                                    ssMap.get("path") + File.separator + ssMap.get("fileName")
                                            + ".jpg", ssMap.get("fileName"), null);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        // 最后通知图库更新
                        PayPlugBaseActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + ssMap.get("path") + File.separator + ssMap.get("fileName")
                                + ".jpg")));
                        LogUtil.showToast(PayPlugBaseActivity.this, ssMap.get("result"));
                    }

                    @Override
                    public void requestFailed() {
                        LogUtil.showToast(PayPlugBaseActivity.this, finalWaring);
                    }
                },
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

}
