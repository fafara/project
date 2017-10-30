package com.ryx.ryxcredit.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by laomao on 16/4/19.
 * Log工具类
 */
public class CLogUtil {
    public static boolean logdebug=true;


    public static void setLogdebug(boolean logdebug) {
        CLogUtil.logdebug = logdebug;
    }


    public static void showLog(String msg) {
        if (logdebug) {
            Log.i("ryx", msg);
        }
//		Log.i("imobpay", TAG);
    }
    public static void showLog(String TAG, String msg) {
        if (logdebug) {
            Log.i(TAG,  msg);
        }
    }

    public static void showToast(final Context mContext, final String content) {
        Toast toast = Toast.makeText(mContext, content,
                Toast.LENGTH_SHORT);
        LinearLayout linearLayout = (LinearLayout) toast.getView();
        TextView messageTextView = (TextView) linearLayout.getChildAt(0);
//        messageTextView.setTextSize(mContext.getResources().getDimension(R.dimen.toasttextsize));
        messageTextView.setTextSize(12);
        toast.show();

    }

    public static void showToast(final Context mContext, final String content,String up) {
        Toast toast = Toast.makeText(mContext, content,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public static void setTextViewContent(final AppCompatActivity activity,
                                          final TextView textView, final String content) {
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (textView != null) {
                    textView.setText(content);
                }

            }
        });
    }
}
