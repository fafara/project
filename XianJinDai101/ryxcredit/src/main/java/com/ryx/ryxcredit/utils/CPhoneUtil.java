package com.ryx.ryxcredit.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static java.lang.Integer.parseInt;

/**
 * Created by Administrator on 2016/12/29.
 */

public class CPhoneUtil {

    /**
     * 利用系统CallLog获取通话历史记录
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static HashMap<String,Object> getCallHistoryList(Context context, ContentResolver cr) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        HashMap<String,Object> contactMap = new HashMap<String,Object>();
        Cursor cs = cr.query(CallLog.Calls.CONTENT_URI, //系统方式获取通讯录存储地址
                new String[]{
                        CallLog.Calls.CACHED_NAME,  //姓名
                        CallLog.Calls.NUMBER,    //号码
                        CallLog.Calls.TYPE,  //呼入/呼出(2)/未接
                        CallLog.Calls.DATE,  //拨打时间
                        CallLog.Calls.DURATION   //通话时长
                }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);

        int i = 0;
        if (cs != null && cs.getCount() > 0) {
            int len = cs.getCount();
            for (cs.moveToFirst(); !cs.isAfterLast() & i <len; cs.moveToNext()) {
                String callName = cs.getString(0);
                String callNumber = cs.getString(1);
                //如果没有获取到通话记录中的姓名
                if (TextUtils.isEmpty(callName)) {
                    Cursor cursor = cr.query( //根据号码查询ID
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?",
                            new String[]{callNumber}, null);
                    if (cursor.moveToFirst()) {
                        String strId = cursor.getString(cursor
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                        Cursor cursorDisplayName = cr.query(// 如果存在该号码，则根据ID在联系人表中查询姓名
                                ContactsContract.Contacts.CONTENT_URI, null,
                                ContactsContract.Contacts._ID + " = ? ",
                                new String[]{strId}, null);
                        if (cursorDisplayName.moveToFirst()) {
                            // 返回用户名称
                            callName = cursorDisplayName
                                    .getString(cursorDisplayName
                                            .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        }
                    }
                }
                if (TextUtils.isEmpty(callName)) {
                    callName = callNumber;
                }
                //通话类型
                int callType = parseInt(cs.getString(2));
                String callTypeStr = "";
                switch (callType) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callTypeStr = "call_In";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callTypeStr = "call_Out";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callTypeStr = "call_missed";
                        break;
                }
                //拨打时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date callDate = new Date(Long.parseLong(cs.getString(3)));
                String callDateStr = sdf.format(callDate);
                //通话时长
                int callDuration = 0;
                try{
                callDuration = Integer.parseInt(cs.getString(4));
                }catch(NumberFormatException e){
                    e.printStackTrace();;
                }
                int min = callDuration / 60;
                int sec = callDuration % 60;
                String callDurationStr = min + "分" + sec + "秒";
                contactMap.put("type",callTypeStr);
                contactMap.put("callName",callName);
                contactMap.put("callNumber",callNumber);
                contactMap.put("callDurationStr",callDurationStr);
                contactMap.put("callDateStr",callDateStr);
//                String callOne = "类型：" + callTypeStr + ", 称呼：" + callName + ", 号码："
//                        + callNumber + ", 通话时长：" + callDurationStr + ", 时间:" + callDateStr
//                        + "\n---------------------\n";

                i++;
            }
        }

        return contactMap;
    }
}
