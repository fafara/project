package com.ryx.payment.ruishua.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppconfig;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.adapter.RyxHomemsgViewPageAdapter;
import com.ryx.payment.ruishua.bean.MsgInfo;
import com.ryx.payment.ruishua.utils.DataUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;
import com.ryx.ryxcredit.xjd.CreditActivity;
import com.zhy.autolayout.AutoLinearLayout;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by XCC on 2017/09/22
 */
public class RyxHomeMessageDialog extends android.app.Dialog {
    List<MsgInfo> msginfos = new ArrayList<>();
    ViewPager viewpage_home_message;
    AutoLinearLayout home_message_point_group;
    ImageView iv_home_message_close;
    Context context;
    List<View> views = new ArrayList<View>();
    private int lastPosition;
    RyxHomeMessageDialogListen ryxHomeMessageDialogListen;
    public interface RyxHomeMessageDialogListen{
       public void  updatePublicNoticePerson(String codeId);

    }

    public RyxHomeMessageDialog(Context context) {
        super(context, R.style.ryxhomeMsgDialog);
        this.context = context;
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                saveNoticeList();
            }
        });
    }
public void setRyxHomeMessageDialogListen(RyxHomeMessageDialogListen ryxHomeMessageDialogListen){
    this.ryxHomeMessageDialogListen=ryxHomeMessageDialogListen;
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_home_message);
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        layout.width = d.getWidth() * 80 / 100;
        layout.height=d.getHeight()* 80 / 100;
        getWindow().setAttributes(layout);
        viewpage_home_message = (ViewPager) findViewById(R.id.viewpage_home_message);
        home_message_point_group = (AutoLinearLayout) findViewById(R.id.home_message_point_group);
        iv_home_message_close = (ImageView) findViewById(R.id.iv_home_message_close);
        iv_home_message_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RyxHomeMessageDialog.this.dismiss();
            }
        });
    }


    public void saveNoticeList() {
        List<MsgInfo> writemsginfos = new ArrayList<>();
        if(msginfos!=null&&msginfos.size()>0){
            for  ( int  i  =   0 ; i  <  msginfos.size(); i ++ )  {
                MsgInfo msgInfo=msginfos.get(i);
                String type=msgInfo.getNoticeType();
                if("0".equals(type)){
                    //公共的
                    LogUtil.showLog("writemsginfos=="+msgInfo.toString());
                    writemsginfos.add(msgInfo);
                }
            }
        }
        ObjectInputStream in = null;
        try {
            InputStream is = context.openFileInput("notice_" + RyxAppdata.getInstance(context).getCustomerId() + ".obj");
            in = new ObjectInputStream(is);
            if (in != null) {
                writemsginfos.addAll (((ArrayList<MsgInfo>) in.readObject()));
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObjectOutputStream out = null;
        try {
            writemsginfos = DataUtil.removeDuplicate(writemsginfos);
            FileOutputStream os = context.openFileOutput("notice_"
                    + RyxAppdata.getInstance(context).getCustomerId()
                    + ".obj", MODE_PRIVATE);
            out = new ObjectOutputStream(os);
            out.writeObject(writemsginfos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void initMessageView(List<MsgInfo> mymsginfos) {
        this.msginfos = mymsginfos;
        msginfos.get(0).setPopup("0");
        msginfos.get(0).setReaded(true);
        for (int i = 0; i < msginfos.size(); i++) {
            View view1 = LayoutInflater.from(context).inflate(
                    R.layout.dialog_home_message_viewpage_webview, null);
            WebView webview = (WebView) view1.findViewById(R.id.webview_viewpage);
            webview.getSettings().setAllowFileAccess(true);
            //开启脚本支持
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new JavaScriptinterface(context), "ryx");
            webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webview.getSettings().setDomStorageEnabled(true);//DOM Storage
            webview.getSettings().setGeolocationEnabled(true);


            TextView titleTextView = (TextView) view1.findViewById(R.id.home_message_title);
            titleTextView.setText(msginfos.get(i).getTitle());
            webview.loadDataWithBaseURL(null, msginfos.get(i).getContent(), "text/html", "utf-8", null);
            views.add(view1);
        }
        RyxHomemsgViewPageAdapter adapter = new RyxHomemsgViewPageAdapter(context, views);
        viewpage_home_message.setAdapter(adapter);
        viewpage_home_message.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                LogUtil.showLog("===========================position=="+position);
                // 改变指示点的状态,当前的指示点选中为true，上一个指示点选中为false
                home_message_point_group.getChildAt(position % views.size()).setEnabled(true);
                home_message_point_group.getChildAt(lastPosition % views.size()).setEnabled(false);
                lastPosition = position;// 记录上一个点
                if("1".equals(msginfos.get(position % views.size()).getNoticeType())&&!msginfos.get(position % views.size()).isReaded()){
                  if(ryxHomeMessageDialogListen!=null){
                      ryxHomeMessageDialogListen.updatePublicNoticePerson(msginfos.get(0).getNoticeCode());
                  }
                }
                msginfos.get(position % views.size()).setPopup("0");
                msginfos.get(position % views.size()).setReaded(true);
                msginfos.get(position % views.size()).setReadFlag("1");

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpage_home_message.setCurrentItem(0);
        initDots(R.drawable.point_bg_homemsgdialog);
        if("1".equals(msginfos.get(0).getNoticeType())){
            if(ryxHomeMessageDialogListen!=null){
                ryxHomeMessageDialogListen.updatePublicNoticePerson(msginfos.get(0).getNoticeCode());
            }
        }
    }


    /**
     * 初始化点点
     *
     * @param resId
     */
    private void initDots(int resId) {
        for (int i = 0; i < views.size(); i++) {
            // 添加指示点(一个ImageView)
            ImageView pointImageView = new ImageView(context);
            // 指示点在线性布局中，设置布局参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = 12;// 右外边距
            pointImageView.setLayoutParams(params);
            pointImageView.setImageResource(resId);
            if (i == 0) {
                pointImageView.setEnabled(true);
            } else {
                pointImageView.setEnabled(false);
            }
            home_message_point_group.addView(pointImageView);
        }
    }


    /**
     * Android与HTML页面交互接口
     */
    public class JavaScriptinterface {
        Context context;

        public JavaScriptinterface(Context c) {
            context = c;
        }

        /**
         * 与js交互时用到的方法，在js里直接调用的
         */
        @JavascriptInterface
        public void alert(String ssss) {
            Toast.makeText(context, ssss, Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void jumpPage(final String pageClassStr) {
//          例如jumpPage(".authenticate.Authenticate_");
            try {
                if(RyxHomeMessageDialog.this.isShowing()){
                    RyxHomeMessageDialog.this.dismiss();
                }
            }catch (Exception e){

            }
            Intent intent = null;
            if (pageClassStr.equals("credit")) {
                PreferenceUtil preferenceUtil = PreferenceUtil.getInstance(context.getApplicationContext());
                preferenceUtil.saveString("c_appUser", RyxAppconfig.APPUSER);
                preferenceUtil.saveString("c_cardId", RyxAppdata.getInstance(context).getCertPid());
                preferenceUtil.saveString("c_version", RyxAppconfig.CLIENTVERSION);
                preferenceUtil.saveString("c_bankimg_url", RyxAppconfig.BANKIMG_URL);
                intent = new Intent(context, CreditActivity.class);
                context.startActivity(intent);
            } else {
                try {
                    intent = new Intent(context, Class.forName(context.getPackageName() + pageClassStr));
                    context.startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }


        }

    }
}



