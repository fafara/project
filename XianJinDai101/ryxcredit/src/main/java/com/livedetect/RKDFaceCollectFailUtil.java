package com.livedetect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.livedetect.data.ConstantValues;
import com.livedetect.utils.FileUtils;
import com.livedetect.utils.LogUtil;
import com.livedetect.utils.StringUtils;
import com.rey.material.widget.Button;
import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.xjd.RYDBossBaseInfoActivity;

/**
 * Created by RYX on 2017/8/2.
 */

public class RKDFaceCollectFailUtil {
    private final String TAG = FaceCollectFailUtil.class.getSimpleName();
    private Button mAgainImg;

    private TextView mRezionTv = null;
    private ImageView returnImg;
    private final int START_LIVEDETECT = 0;
    private Context mcontext;
    private static RKDFaceCollectFailUtil instance;

    public static RKDFaceCollectFailUtil getInstance() {
        if(instance==null){
            instance  = new RKDFaceCollectFailUtil();
        }
        return instance;
    }

    public void initFailWindow(Context context, Bundle result) {
        mcontext = context;
        FileUtils.init(context);
        initView();
        if(result==null){
            mRezionTv.setText("识别有误！");
            return;
        }
        String mRezion="识别有误！";
        if(result.containsKey("mRezion"))
            mRezion= result.getString("mRezion");
        LogUtil.i(TAG, "35 mRezion = " + mRezion);
        if (StringUtils.isStrEqual(mRezion, "0")) {
            mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_default"));
        } else if (StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.NO_FACE)) {
            mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_noface"));
        } else if (StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.MORE_FACE)) {
            mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_moreface"));
        } else if (StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.NOT_LIVE)) {//非活体
            mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_notlive"));
        } else if (StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.BAD_MOVEMENT_TYPE)) {//互斥
            mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_badmovementtype"));
        } else if (StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.TIME_OUT)) {//超时
            mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_timeout"));
        } else if (StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.GET_PGP_FAILED)) {
            mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_pgp_fail"));
        } else if (StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.CHECK_3D_FAILED)) {//3d
            mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_3d"));
        } else if (StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.CHECK_SKIN_COLOR_FAILED)) {//肤色
            mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_badcolor"));
        } else if (StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.CHECK_CONTINUITY_COLOR_FAILED)) {//连续性
            mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_badcontinuity"));
        } else if (StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.CHECK_ABNORMALITY_FAILED)) {//
            mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_fail_remind_abnormality"));
        } else if (StringUtils.isStrEqual(mRezion, ConstantValues.BAD_REASON.GUIDE_TIME_OUT)) {//超时
            mRezionTv.setText(FileUtils.getResIdByTypeAndName(ConstantValues.RES_TYPE_STRING, "htjc_guide_time_out"));
        }
    }

    private void initView() {
        mAgainImg = (Button)((RYDBossBaseInfoActivity)mcontext).findViewById(R.id.btn_again);
        mRezionTv = (TextView) ((RYDBossBaseInfoActivity)mcontext).findViewById(R.id.rezion_tv);
        mAgainImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, LiveDetectActivity.class);
                ((RYDBossBaseInfoActivity)mcontext).startActivityForResult(intent,START_LIVEDETECT);
            }
        });

    }

    /* (non-Javadoc)
     * @see android.app.Activity#onPause()
     */
    protected void onPause() {
        FileUtils.setmContext(null);
    }
}
