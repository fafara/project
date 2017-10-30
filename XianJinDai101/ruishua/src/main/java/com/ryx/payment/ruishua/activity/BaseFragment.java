package com.ryx.payment.ruishua.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.ryx.payment.ruishua.usercenter.LoginActivity_;
import com.ryx.payment.ruishua.utils.LogUtil;

/**
 * Created by muxin on 2016-05-24.
 */
public class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.showLog("currentAct:===="+this.getClass());
    }

    private BaseActivity mActivity;

    public void refreshData(){}
    /**
     * 设置View2秒内不能重复点击
     * @param v
     */
    public  void disabledTimerView(final View v) {
        if(v!=null){
            v.setClickable(false);
            v.setEnabled(false);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    v.setClickable(true);
                    v.setEnabled(true);
                }
            }, 2000);
        }
    }
    /**
     * 获取Activity
     *
     * @return
     */
    public BaseActivity getBaseActivity() {
        if (mActivity == null) {
            mActivity = (BaseActivity) getActivity();
        }
        return mActivity;
    }
    /**
     * 去重新登录
     */
    public void toAgainLogin(Context context, int requestCode, boolean... iscleartask){
//        String user_id=  RyxAppdata.getInstance(context).getCustomerId();
//        int switchFlag=0;
//        if(!TextUtils.isEmpty(user_id)&&!"0000".equals(user_id)){
//            //如果连用户信息都没有则当前没有登录过,应走LoginActivity_登录密码验证
//            GesturesPaswdUtil spUserid=new GesturesPaswdUtil(context,user_id );
//            switchFlag = spUserid.loadIntSharedPreference("switch");
//        }
//        LogUtil.showLog("user_id=="+user_id+",switchFlag=="+switchFlag);
//        Intent intent;
//        if(switchFlag==1){
//            //手势密码开关开着
//            intent = new Intent(context, GesturePawdCheckActivity_.class);
//        }else{
        Intent intent = new Intent(context, LoginActivity_.class);
//        }
        if(iscleartask.length > 0&&iscleartask[0]){
            //清空activity栈
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            //添加token失效的标示
            intent.putExtra("tokenIntent", true);
        }
        startActivityForResult(intent,requestCode);
    }
}
