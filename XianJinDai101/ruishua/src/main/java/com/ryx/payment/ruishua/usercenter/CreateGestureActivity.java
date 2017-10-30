package com.ryx.payment.ruishua.usercenter;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.utils.GesturesPaswdUtil;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.ryxgesturespswd.util.LockPatternUtil;
import com.ryx.ryxgesturespswd.widget.LockPatternIndicator;
import com.ryx.ryxgesturespswd.widget.LockPatternView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 创建手势密码
 */
@EActivity(R.layout.activity_create_gesture)
public class CreateGestureActivity extends BaseActivity {
    @ViewById(R.id.lockPatterIndicator)
    LockPatternIndicator lockPatternIndicator;
    @ViewById(R.id.lockPatternView)
    LockPatternView lockPatternView;
    @ViewById(R.id.messageTv)
    TextView messageTv;
    @ViewById(R.id.cancel_btn)
    Button cancel_btn;
    private List<LockPatternView.Cell> mChosenPattern = null;
    private static final long DELAYTIME = 600L;
  @AfterViews
  public void afterView(){
      setTitleLayout("手势密码设置",true,false);
      init();
  }
    private void init() {
        lockPatternView.setOnPatternListener(patternListener);
    }
    /**
     * 手势监听
     */
    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
            //updateStatus(Status.DEFAULT, null);
            lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            for(LockPatternView.Cell cell: pattern){
                LogUtil.showLog("pattern=="+cell.toString());
            }
            //Log.e(TAG, "--onPatternDetected--");
            if(mChosenPattern == null && pattern.size() >= 4) {
                mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
                updateStatus(Status.CORRECT, pattern);
            } else if (mChosenPattern == null && pattern.size() < 4) {
                updateStatus(Status.LESSERROR, pattern);
            } else if (mChosenPattern != null) {
                if (mChosenPattern.equals(pattern)) {
                    updateStatus(Status.CONFIRMCORRECT, pattern);
                } else {
                    updateStatus(Status.CONFIRMERROR, pattern);
                }
            }
        }
    };
    private enum Status {
        //默认的状态，刚开始的时候（初始化状态）
        DEFAULT("绘制解锁图案", R.color.threeblack),
        //第一次记录成功
        CORRECT("再次绘制解锁图案", R.color.threeblack),
        //连接的点数小于4（二次确认的时候就不再提示连接的点数小于4，而是提示确认错误）
        LESSERROR("至少连接4个点，请重新绘制", R.color.warning_icon_ff7a6b),
        //二次确认错误
        CONFIRMERROR("与上一次绘制不一致，请重新绘制", R.color.warning_icon_ff7a6b),
        //二次确认正确
        CONFIRMCORRECT("设置成功", R.color.threeblack);

        private Status(String strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
        private String strId;
        private int colorId;
    }

    /**
     * 更新状态
     * @param status
     * @param pattern
     */
    private void updateStatus(Status status, List<LockPatternView.Cell> pattern) {
        messageTv.setTextColor(getResources().getColor(status.colorId));
        messageTv.setText(status.strId);
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CORRECT:
                updateLockPatternIndicator();
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case LESSERROR:
                AnimationView(messageTv);
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CONFIRMERROR:
                AnimationView(messageTv);
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CONFIRMCORRECT:
                saveChosenPattern(pattern);
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                setLockPatternSuccess();
                break;
        }
    }



    /**
     * 更新 Indicator
     */
    private void updateLockPatternIndicator() {
        if (mChosenPattern == null)
            return;
        lockPatternIndicator.setIndicator(mChosenPattern);
    }
    /**
     * 保存手势密码
     */
    private void saveChosenPattern(List<LockPatternView.Cell> cells) {
        byte[] bytes = LockPatternUtil.patternToHash(cells);
        String paswd=Arrays.toString(bytes);
       LogUtil.showLog("密码："+paswd );
        String user_id=  RyxAppdata.getInstance(CreateGestureActivity.this).getCustomerId();
        GesturesPaswdUtil spUserid=new GesturesPaswdUtil(getApplicationContext(),user_id );
        spUserid.saveSharedPreferences("gesturepwd",paswd);
        spUserid.saveSharedPreferences("switch",1);
    }
    private void setLockPatternSuccess() {
        LogUtil.showToast(CreateGestureActivity.this,"设置密码成功");
        finish();
    }
    @Click(R.id.cancel_btn)
    public void cancelBtnOnclick(){
        mChosenPattern = null;
        lockPatternIndicator.setDefaultIndicator();
        updateStatus(Status.DEFAULT, null);
        lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
    }
    private void AnimationView(View view){
        TranslateAnimation animation = new TranslateAnimation(0, -10, 0, 0);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(100);
        animation.setRepeatCount(3);
        animation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(animation);
    }
}
