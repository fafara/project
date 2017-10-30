package com.ryx.payment.ruishua.sjfx;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.bean.RyxPayResult;
import com.ryx.payment.ruishua.dialog.RyxSimpleConfirmDialog;
import com.ryx.payment.ruishua.inteface.PermissionResult;
import com.ryx.payment.ruishua.listener.ConFirmDialogListener;
import com.ryx.payment.ruishua.net.XmlCallback;
import com.ryx.payment.ruishua.usercenter.DeViceListActivity_;
import com.ryx.payment.ruishua.utils.BitmapUntils;
import com.ryx.payment.ruishua.utils.JsonUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.zhy.autolayout.AutoLinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.Map;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

import static com.ryx.payment.ruishua.RyxAppdata.getCurrentBranchMipmapLogoId;

@EActivity(R.layout.activity_my_invitation_code)
public class MyInvitationCodeActivity extends BaseActivity {
    @ViewById(R.id.iv_qrcodeimg)
    ImageView iv_qrcodeimg;
    @ViewById(R.id.bt_save_local)
    Button bt_save_local;
    @ViewById(R.id.tv_url)
    TextView tv_url;
    @ViewById(R.id.bt_shareUrl)
    Button bt_shareUrl;
    @ViewById(R.id.tv_invitationCode)
    TextView tv_invitationCode;
    @ViewById(R.id.bt_copy)
    Button bt_copy;
    @ViewById(R.id.qrcode_layout)
    AutoLinearLayout qrcode_layout;
    @ViewById(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @ViewById(R.id.nodatalayout)
    AutoLinearLayout nodatalayout;
    @ViewById(R.id.textmsg_tv)
    TextView textmsg_tv;

    @AfterViews
    public void afterView(){
        setTitleLayout("我的邀请码", true, true);
        setRightImgMessage("邀请码帮助",RyxAppconfig.Notes_invitationCode);
        initQtPatParams();
        invitationCode();
    }

    /**
     * 获取我的邀请码信息
     */
    private void invitationCode(){
    qtpayApplication.setValue("InvitationCode.Req");
    qtpayAttributeList.add(qtpayApplication);
        httpsPost("invitationCodeTag", new XmlCallback() {
            @Override
            public void onTradeSuccess(RyxPayResult payResult) {
                try {
                    String data=payResult.getData();
                    JSONObject dataJsonObj=new JSONObject(data);
                  String code=  JsonUtil.getValueFromJSONObject(dataJsonObj,"code");
                    if(RyxAppconfig.QTNET_SUCCESS.equals(code)){
                      JSONObject resultOj=  dataJsonObj.getJSONObject("result");
                        final String code_val=JsonUtil.getValueFromJSONObject(resultOj,"code_val");
                        final String code_url=JsonUtil.getValueFromJSONObject(resultOj,"code_url");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nestedScrollView.setVisibility(View.VISIBLE);
                                nodatalayout.setVisibility(View.GONE);
                                createQrcodeImg(code_url,iv_qrcodeimg);
                                tv_url.setText(code_url);
                                tv_invitationCode.setText(code_val);
                            }
                        });
                    }

                }catch (Exception e){

                }
            }
            @Override
            public void onOtherState(String rescode,String resDesc){
                nodatalayout.setVisibility(View.VISIBLE);
                nestedScrollView.setVisibility(View.GONE);
                textmsg_tv.setText(resDesc);
                if("9127".equals(rescode)){
                    showDialog("因为您未绑定终端无法获取邀请码,是否现在去绑定?");
                }
            }
        });
    }
private void showDialog(String content){
    RyxSimpleConfirmDialog ryxSimpleConfirmDialog = new RyxSimpleConfirmDialog(MyInvitationCodeActivity.this, new ConFirmDialogListener() {

        @Override
        public void onPositiveActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
            ryxSimpleConfirmDialog.dismiss();
            Intent intent=new Intent(MyInvitationCodeActivity.this, DeViceListActivity_.class);
            intent.putExtra("flag","banding");
            startActivity(intent);
            finish();
        }
        @Override
        public void onNegativeActionClicked(RyxSimpleConfirmDialog ryxSimpleConfirmDialog) {
            ryxSimpleConfirmDialog.dismiss();
        }
    });
    ryxSimpleConfirmDialog.show();
    ryxSimpleConfirmDialog.setContent(content);
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
         * 将布局文件保存于磁盘中
         */
        @Click(R.id.bt_save_local)
        public void dirSaveView ( ){
            String waring = MessageFormat.format(getResources().getString(R.string.dirwaringmsg), RyxAppdata.getInstance(this).getCurrentBranchName());
            final String finalWaring = waring;
            requesDevicePermission(waring, 0x0021, new PermissionResult() {
                        @Override
                        public void requestSuccess() {
                            StringBuffer stringBuffer = new StringBuffer();
                            stringBuffer.append(RyxAppdata.getInstance(MyInvitationCodeActivity.this).getCurrentFileBranchRootName());
                            stringBuffer.append("PayQrCode");
                            //保存二维码图片到手机qrcodeallaout
                            Map<String, String> ssMap = BitmapUntils.saveQrcodeAsFile(iv_qrcodeimg,
                                    stringBuffer.toString());
                            if (TextUtils.isEmpty(ssMap.get("path")) || TextUtils.isEmpty(ssMap.get("fileName"))) {
                                LogUtil.showToast(MyInvitationCodeActivity.this, ssMap.get("result"));
                                return;
                            }
                            // 其次把文件插入到系统图库
                            try {
                                MediaStore.Images.Media.insertImage(MyInvitationCodeActivity.this.getContentResolver(),
                                        ssMap.get("path") + File.separator + ssMap.get("fileName")
                                                + ".jpg", ssMap.get("fileName"), null);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            // 最后通知图库更新
                            MyInvitationCodeActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + ssMap.get("path") + File.separator + ssMap.get("fileName")
                                    + ".jpg")));
                            LogUtil.showToast(MyInvitationCodeActivity.this, ssMap.get("result"));
                        }

                        @Override
                        public void requestFailed() {
                            LogUtil.showToast(MyInvitationCodeActivity.this, finalWaring);
                        }
                    },
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        @Click(R.id.bt_shareUrl)
        public void shareClick() {
            String tv_urlStr=tv_url.getText().toString();
            if(TextUtils.isEmpty(tv_urlStr)){
                LogUtil.showToast(MyInvitationCodeActivity.this,"分享链接不能为空");
                return;
            }
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(
                        Intent.EXTRA_TEXT,tv_urlStr
                        );
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(Intent.createChooser(intent, getTitle()));
            } catch (Exception e) {
                LogUtil.showToast(MyInvitationCodeActivity.this,"调取分享失败,请尝试长按链接复制!");
            }
        }

        @Click(R.id.bt_copy)
        public  void copy(View view){
        String invitationCode=    tv_invitationCode.getText().toString();
            if(TextUtils.isEmpty(invitationCode)){
                LogUtil.showToast(MyInvitationCodeActivity.this,"邀请码不允许为空");
                return;
            }
            ClipboardManager clip = (ClipboardManager)getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
            clip.setText(invitationCode); // 复制
            LogUtil.showToast(getApplicationContext(),"复制成功");
            clip.getText(); // 粘贴

            }
         @LongClick(R.id.tv_url)
        public  void  copyUrl(View view){
             String tv_urlStr=tv_url.getText().toString();
             if(TextUtils.isEmpty(tv_urlStr)){
                 LogUtil.showToast(MyInvitationCodeActivity.this,"分享链接不能为空");
                 return;
             }
               ClipboardManager clip = (ClipboardManager)getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
               clip.setText(tv_urlStr); // 复制
               LogUtil.showToast(getApplicationContext(),"复制成功");
               clip.getText(); // 粘贴
            }

}
