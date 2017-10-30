package com.ryx.payment.ruishua.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.app.Dialog;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.quickadapter.inter.NoDoubleClickListener;

/**
 * Created by xucc on 2017/6/12.
 */
public class UserAuthResultDialog extends Dialog {
    private Button next_btn,return_btn;
    private ImageView dialogg_userauth_resultimg;
    private TextView main_content,second_content_tv;
    UserAuthResultDialogBtnListen userAuthResultDialogBtnListen;
    public UserAuthResultDialog(Context context,UserAuthResultDialogBtnListen userAuthResultDialogBtnListen) {
        super(context, R.style.SimpleDialogLight);
        this.userAuthResultDialogBtnListen=userAuthResultDialogBtnListen;
    }
    public UserAuthResultDialog(Context context) {
        super(context, R.style.SimpleDialogLight);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_userauthsuccess);


        dialogg_userauth_resultimg=(ImageView) findViewById(R.id.dialogg_userauth_resultimg);
        main_content=(TextView) findViewById(R.id.main_content);
        second_content_tv=(TextView) findViewById(R.id.second_content_tv);
        next_btn=(Button) findViewById(R.id.next_btn);
        return_btn = (Button) findViewById(R.id.return_btn);
    }

    /**
     *展示成功Dialog
     */
    public void showSuccessDialog(){
        UserAuthResultDialog.this.show();
        dialogg_userauth_resultimg.setImageResource(R.drawable.dialog_userauth_success_img);
        main_content.setText("恭喜你! 认证通过");
        second_content_tv.setText("完成高级认证可以大幅增加交易额度,赶快认证吧!");
        next_btn.setText("立即认证");
        next_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if(userAuthResultDialogBtnListen!=null){
                    userAuthResultDialogBtnListen.btnOkClick(view);
                }
            }
        });
        return_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if(userAuthResultDialogBtnListen!=null){
                    userAuthResultDialogBtnListen.btnReturnClick(view);
                }
            }
        });
    }
    /**
     *展示失败Dialog
     */
    public void showFailDialog(){
        UserAuthResultDialog.this.show();
        dialogg_userauth_resultimg.setImageResource(R.drawable.dialog_authresult_fail);
        main_content.setText("对不起! 认证失败!");
        second_content_tv.setText("请仔细核对认证信息准确无误后再次认证!");
        next_btn.setText("重新认证");
        next_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if(userAuthResultDialogBtnListen!=null){
                    userAuthResultDialogBtnListen.btnOkClick(view);
                }
            }
        });
        return_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if(userAuthResultDialogBtnListen!=null){
                    userAuthResultDialogBtnListen.btnReturnClick(view);
                }
            }
        });
    }
    /**
     *展示处理中Dialog
     */
    public void showProcessingDialog(){
        UserAuthResultDialog.this.show();
        dialogg_userauth_resultimg.setImageResource(R.drawable.dialog_authresult_processing);
        main_content.setText("正在人工审核中!");
        second_content_tv.setText("请你耐心等待,注意短信通知");
        next_btn.setText("好的");
        next_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if(userAuthResultDialogBtnListen!=null){
                    userAuthResultDialogBtnListen.btnOkClick(view);
                }
            }
        });
    }
    /**
     *展示自定义内容Dialog
     */
    public void showDefinedDialog(int resId, String mainContent, String second_content, String btnContent, final UserAuthResultDialogBtnListen resultDialogBtnListen, @Nullable String returnContent){
        UserAuthResultDialog.this.show();
        UserAuthResultDialog.this.setCancelable(false);
        UserAuthResultDialog.this.setCanceledOnTouchOutside(false);
        dialogg_userauth_resultimg.setImageResource(resId);
        main_content.setText(mainContent);
        second_content_tv.setText(second_content);
        next_btn.setText(btnContent);
        next_btn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                resultDialogBtnListen.btnOkClick(view);
            }
        });
        if(!TextUtils.isEmpty(returnContent)){
            return_btn.setVisibility(View.VISIBLE);
            return_btn.setText(returnContent);
            return_btn.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View view) {
                    resultDialogBtnListen.btnReturnClick(view);
                }
            });
        }
    }
    /**
     * 处理按钮下一步操作监听
     */
    public  interface   UserAuthResultDialogBtnListen{
//    /**
//     * 进行高级认证
//     * @param view
//     */
//    public  void btnToCreditClick(View view);
//
//    /**
//     * 重新认证
//     * @param view
//     */
//    public  void btnToAgainAuthClick(View view);

    /**
     * 好的按钮点击，上面
     * @param view
     */
    public  void btnOkClick(View view);

    /**
         *返回首页按钮点击，下面
         * @param view
         */
    public void btnReturnClick(View view);
    }


}
