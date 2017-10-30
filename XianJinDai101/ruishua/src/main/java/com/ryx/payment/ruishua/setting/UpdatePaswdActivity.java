package com.ryx.payment.ruishua.setting;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.RyxAppdata;
import com.ryx.payment.ruishua.activity.BaseActivity;
import com.ryx.payment.ruishua.usercenter.CreateGestureActivity_;
import com.ryx.payment.ruishua.usercenter.GesturePawdCheckActivity_;
import com.ryx.payment.ruishua.usercenter.LoginActivity_;
import com.ryx.payment.ruishua.utils.GesturesPaswdUtil;
import com.ryx.payment.ruishua.utils.LogUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import static com.ryx.payment.ruishua.RyxAppconfig.LOGINACTFINISH;

/**
 * 修改密码（展示各项密码列表）
 */
@EActivity(R.layout.activity_update_paswd)
public class UpdatePaswdActivity extends BaseActivity {
    private int TOCREATEGESTUREACTIVITY=0x0021;
    @ViewById(R.id.gesture_togglebtn)
    CheckBox gesture_togglebtn;

    @ViewById(R.id.rl_update_gesture_paswd)
    RelativeLayout rl_update_gesture_paswd;
    @AfterViews
    public void initView(){
        setTitleLayout("密码修改",true,false);
        String user_id=  RyxAppdata.getInstance(UpdatePaswdActivity.this).getCustomerId();
        GesturesPaswdUtil spUserid=new GesturesPaswdUtil(getApplicationContext(),user_id );
        int switchFlag= spUserid.loadIntSharedPreference("switch");
        if(switchFlag==1){
            gesture_togglebtn.setChecked(true);
            rl_update_gesture_paswd.setVisibility(View.VISIBLE);
        }else{
            rl_update_gesture_paswd.setVisibility(View.GONE);
            gesture_togglebtn.setChecked(false);
        }
        gesture_togglebtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String user_id=  RyxAppdata.getInstance(UpdatePaswdActivity.this).getCustomerId();
                GesturesPaswdUtil spUserid=new GesturesPaswdUtil(getApplicationContext(),user_id );
                if (isChecked) {
                    LogUtil.showToast(UpdatePaswdActivity.this,"手势密码已开启");
                    String gesturepwd = spUserid.loadStringSharedPreference("gesturepwd");
                    rl_update_gesture_paswd.setVisibility(View.VISIBLE);
                    if(TextUtils.isEmpty(gesturepwd)){
                        //用户未设置过手势密码
                        Intent intent = new Intent(UpdatePaswdActivity.this, CreateGestureActivity_.class);
                       startActivityForResult(intent,TOCREATEGESTUREACTIVITY);
                    }else{
                        spUserid.saveSharedPreferences("switch",1);
                    }
                } else {
                    spUserid.saveSharedPreferences("switch",0);
                    LogUtil.showToast(UpdatePaswdActivity.this,"手势密码已关闭");
                    rl_update_gesture_paswd.setVisibility(View.GONE);
                }
            }
        });
    }
    /**
     * 创建手势密码
     */
    private int CREATEGESTURETAG=0x00101;
    /**
     *校验手势密码
     */
    private int CHECKGESTURETAG=0x00201;
    /**
     *手势密码验证成功
     */
    private static final int GESTUREPAWDCHECKSUCCESS=0x00801;
    /**
     * 登录密码方式验证成功
     */
    private static final int LOGINPAWDCHECKSUCCESS=0x00802;
    /**
     *手势密码验证失败
     */
    private static final int GESTUREPAWDCHECKFAIL=0x00803;
    /**
     * 登录密码修改
     */
    @Click(R.id.rl_update_login_password)
    public void updateLoginPaswd(){
        Intent intent = new Intent(this, ChangePassWord_.class);
        startActivity(intent);
    }

    /**
     * 手势密码修改
     */
    @Click(R.id.rl_update_gesture_paswd)
    public void updateGesturePaswd(){
      String user_id=  RyxAppdata.getInstance(UpdatePaswdActivity.this).getCustomerId();
        GesturesPaswdUtil spUserid=new GesturesPaswdUtil(getApplicationContext(),user_id );
        String gesturepwd = spUserid.loadStringSharedPreference("gesturepwd");
        int switchFlag= spUserid.loadIntSharedPreference("switch");
        if(switchFlag!=1){
            LogUtil.showToast(UpdatePaswdActivity.this,"请先打开手势密码开关后再进行修改操作!");
            return;
        }
        if(TextUtils.isEmpty(gesturepwd)){
            //用户未设置过手势密码
            Intent intent = new Intent(this, CreateGestureActivity_.class);
            startActivity(intent);
        }else{
            int errorcount= spUserid.loadIntSharedPreference("errorcount");
            if(errorcount>=3){
                LogUtil.showToast(UpdatePaswdActivity.this,"手势密码输入错误次数已超限,请密码登录验证!");
                doExit();
                toAgainLogin(UpdatePaswdActivity.this,0x0011,true);
//                Intent intent=new Intent(this,LoginActivity_.class);
//                startActivityForResult(intent,CHECKGESTURETAG);
            }else{
                //用户设置过手势密码
                Intent intent = new Intent(this, GesturePawdCheckActivity_.class);
                intent.putExtra("nohttpcheck",true);
                startActivityForResult(intent,CHECKGESTURETAG);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHECKGESTURETAG&&resultCode==GESTUREPAWDCHECKSUCCESS){
            //手势密码验证成功
            Intent intent = new Intent(this, CreateGestureActivity_.class);
            startActivity(intent);
        }else if(requestCode==CHECKGESTURETAG&&resultCode==LOGINPAWDCHECKSUCCESS){
            //登录密码验证成功
            Intent intent = new Intent(this, CreateGestureActivity_.class);
            startActivity(intent);

        }else if(requestCode==CHECKGESTURETAG&&resultCode==GESTUREPAWDCHECKFAIL){
            //手势密码验证失败直接跳转登录页
            Intent intent=new Intent(this,LoginActivity_.class);
            startActivityForResult(intent,CHECKGESTURETAG);
        }else if(requestCode==CHECKGESTURETAG&&resultCode==LOGINACTFINISH){
            //手势密码验证失败后直接跳转登录成功后
            Intent intent = new Intent(this, CreateGestureActivity_.class);
            startActivity(intent);
        }
        else if(requestCode==TOCREATEGESTUREACTIVITY){
            //创建密码完成
            String user_id=  RyxAppdata.getInstance(UpdatePaswdActivity.this).getCustomerId();
            GesturesPaswdUtil spUserid=new GesturesPaswdUtil(getApplicationContext(),user_id );
            String gesturepwd = spUserid.loadStringSharedPreference("gesturepwd");
            if(TextUtils.isEmpty(gesturepwd)){
                gesture_togglebtn.setChecked(false);
                rl_update_gesture_paswd.setVisibility(View.GONE);
            }else{
                gesture_togglebtn.setChecked(true);
                rl_update_gesture_paswd.setVisibility(View.VISIBLE);
            }
        }

    }
}
