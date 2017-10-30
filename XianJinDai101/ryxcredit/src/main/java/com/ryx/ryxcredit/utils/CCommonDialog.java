package com.ryx.ryxcredit.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.app.BottomSheetDialog;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.DialogFragment;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.adapter.DateSelectAdapter;

import java.text.SimpleDateFormat;

/**
 * Created by DIY on 2016/9/2.
 */
public class CCommonDialog {
    //对话框消失延迟毫秒
    private static long DELAY_TIME_MILL_SECONDS = 2 * 1000;

    public static interface IAgreementListener {

        void agreeAction();
    }

    public static interface ICommonBankListener {
        void selectBank(String name);
    }

    public static interface ICommonSelectedListener {
        void selectedKey(int key, String value);
    }

    public static interface ICommonChooseDateListen {
        void chooseDate(String date);
    }

    /**
     * 协议对话框
     *
     * @param context
     * @param listener
     */
    public static void showAgreementDialog(Context context, IAgreementListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("同意协议");
        builder.show();

    }

    /**
     * 还款成功
     *
     * @param context
     */
    public static void showRepaymentOK(Context context, String status, String tip, final IMessage message) {
        LayoutInflater inflaterDl = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflaterDl.inflate(R.layout.c_dialog_credit_ok, null);
        TextView statusTv = (TextView) layout.findViewById(R.id.c_dialog_ok_status);
        TextView statustipTv = (TextView) layout.findViewById(R.id.c_dialog_ok_tip);
        statustipTv.setText(tip);
        statusTv.setText(status);
        //对话框
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                message.callback();
            }
        }, DELAY_TIME_MILL_SECONDS);
    }

    public interface IMessage {
        public void callback();
    }

    /**
     * 还款处理中
     *
     * @param context
     */
    public static void showRepaymentTreat(Context context, String status, String tip, final IMessage message) {
        LayoutInflater inflaterDl = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflaterDl.inflate(R.layout.c_dialog_credit_treat, null);
        TextView statusTv = (TextView) layout.findViewById(R.id.c_dialog_treat_status);
        TextView statustipTv = (TextView) layout.findViewById(R.id.c_dialog_treat_tip);
        statustipTv.setText(tip);
        statusTv.setText(status);
        //对话框
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                message.callback();
            }
        }, DELAY_TIME_MILL_SECONDS);
    }

    /**
     * 还款失败
     *
     * @param context
     */
    public static void showRepaymentError(Context context, String status, String tip) {

        LayoutInflater inflaterDl = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflaterDl.inflate(R.layout.c_dialog_credit_error, null);
        TextView statusTv = (TextView) layout.findViewById(R.id.c_dialog_error_status);
        TextView statustipTv = (TextView) layout.findViewById(R.id.c_dialog_error_tip);
        statustipTv.setText(tip);
        statusTv.setText(status);
        //对话框
        final Dialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, DELAY_TIME_MILL_SECONDS);
    }

    public static void showCommonBank(Context context, final String[] datas, final ICommonBankListener commonBankListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("目前支持银行");
        builder.setItems(datas, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                commonBankListener.selectBank(datas[which]);
            }
        });
        builder.show();
    }

    public static void showChoseBank(Context context, final String[] datas, final ICommonBankListener commonBankListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("请选择银行卡");
        builder.setItems(datas, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                commonBankListener.selectBank(datas[which]);
            }
        });
        builder.show();
    }

    public static void showCommonSelected(final Context context, String title, final String[] datas, final ICommonSelectedListener commonBankListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setItems(datas, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                commonBankListener.selectedKey(which, datas[which]);
            }
        });
        builder.show();
    }

    public static void showPayDays(final AppCompatActivity activity, String title, final ICommonChooseDateListen callback) {
        final String[] days = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(activity, R.style.Material_App_BottomSheetDialog);
        View boottomView = LayoutInflater.from(activity).inflate(R.layout.c_dialog_date_choose, null);
        final ListView lv = (ListView) boottomView.findViewById(R.id.lv_date);
        ImageView img_close = (ImageView) boottomView.findViewById(R.id.imgview_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetDialog.dismiss();
            }
        });
        DateSelectAdapter adapter = new DateSelectAdapter(activity, days);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView imgView = (ImageView) view.findViewById(R.id.img_selected);
                imgView.setVisibility(View.VISIBLE);
                callback.chooseDate(days[position] + "");
                mBottomSheetDialog.dismiss();
            }
        });
        mBottomSheetDialog.setContentView(boottomView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBottomSheetDialog.show();
                    }
                });

            }
        }).start();
    }

    public static void showCommonDateChoose(final AppCompatActivity activity, String title, final ICommonChooseDateListen callback) {

        DatePickerDialog.Builder builder = new DatePickerDialog.Builder(R.style.Material_App_Dialog_DatePicker_Light) {
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                DatePickerDialog dialog = (DatePickerDialog) fragment.getDialog();
                String date = dialog.getFormattedDate(new SimpleDateFormat("yyyyMMdd"));

                callback.chooseDate(date);
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {

                super.onNegativeActionClicked(fragment);
            }
        };

        builder.positiveAction("确定")
                .negativeAction("取消");

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(activity.getSupportFragmentManager(), null);
    }
}
