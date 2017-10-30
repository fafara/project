package com.ryx.payment.ruishua.dialog;

import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

/**
 * Created by Administrator on 2016/5/30.
 */
public class CreditDialog {

    private  SimpleDialog.Builder builder;
    private DialogFragment dialogfragment;
    public CreditDialog(int styleId){
        builder  =new SimpleDialog.Builder(styleId){
            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                dialogfragment = fragment;
                nagtiveBtnClick();
            }
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                dialogfragment = fragment;
                positiveBtnClick();
            }
        };

    }

    public void setView(FragmentManager manager,String...msgandtile){
        int size = msgandtile.length;
        if(size>=1&&! TextUtils.isEmpty(msgandtile[0]))
            builder.message(msgandtile[0]);
        if(size>=2&&! TextUtils.isEmpty(msgandtile[1])){
            builder.title(msgandtile[1]);
        }else{
            builder.title("温馨提示");
        }
        if(size>=3&& !TextUtils.isEmpty(msgandtile[2]))
            builder.positiveAction(msgandtile[2]);
        if(size>=4&&! TextUtils.isEmpty(msgandtile[3]))
            builder.negativeAction(msgandtile[3]);
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(manager, null);
    }

     public void positiveBtnClick(){

     };
     public void nagtiveBtnClick(){

     };

    public void closeDialog(){
        if(dialogfragment!=null)
            dialogfragment.dismiss();
    }

}
