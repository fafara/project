package com.ryx.ryxkeylib.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ryx.ryxkeylib.R;
import com.ryx.ryxkeylib.listener.CardTipPwdListener;
import com.ryx.ryxkeylib.listener.EditPwdListener;
import com.ryx.ryxkeylib.service.CustomKeyBoardService;

import java.util.Timer;
import java.util.TimerTask;
/**
 * 弹出框密码键盘,只负责弹出展示密码输入框相关操作,不涉及业务
 * @author XCC
 *
 */
public class CardpwdInputDialog extends Dialog {

	Context context;
	CardTipPwdListener myListener;
	String address;

	LinearLayout ll_input;
	ImageView iv_1, iv_2, iv_3, iv_4, iv_5, iv_6;
	EditText et_pwd;
	TextView tv_tips, tv_ok, tv_cancel;
	CustomKeyBoardService customKeyBoardService;
	boolean isDebug;
	private String pwdStr="";
//	InputMethodManager inputManager;

	public CardpwdInputDialog(Context context) {
		super(context);
	}

	public CardpwdInputDialog(Context context, int theme,
			CardTipPwdListener myListener,boolean isDebug) {
		super(context, theme);
		this.context = context;
		this.myListener = myListener;
		this.setCanceledOnTouchOutside(false);
		this.isDebug=isDebug;	
	}
	EditPwdListener cardpwdListener=new EditPwdListener() {
		
		@Override
		public void getPwdVal(String realVal,String disVal) {
			pwdStr=realVal;
		}

		@Override
		public void getPwdDisVal(String disVal, int count) {
			et_pwd.setText(disVal);
		}

		@Override
		public void pwdViewOkbtnLisener() {
			onOkClickListen();
		}
	};
	public void setTip(String tips) {
		et_pwd.setText("");
		if ("".equals(tips)) {
			tv_tips.setVisibility(View.GONE);
		} else {
			tv_tips.setText(tips);
		}

		Timer timer = new Timer();
		timer.schedule(new TimerTask()
		{
			public void run()
			{
				et_pwd.post(new Runnable() {
					
					@Override
					public void run() {
						customKeyBoardService=CustomKeyBoardService.registerKeyBoardForEdit(context,isDebug, et_pwd,cardpwdListener, false);
						customKeyBoardService.setEditMaxLenth(6);
						customKeyBoardService.showKeyboard(false);
					}
				});
			}
		},500);

		
		
	}
	@Override
	public void show() {

		WindowManager wm = (WindowManager) getContext()
				.getSystemService(Context.WINDOW_SERVICE);

		int height = wm.getDefaultDisplay().getHeight();
		//获得当前窗体
		Window window = this.getWindow();
		//重新设置
		WindowManager.LayoutParams lp = window.getAttributes();
		window .setGravity(Gravity.TOP);
		lp.y =height/5; // 新位置Y坐标
		window .setAttributes(lp);
		super.show();
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_inquiry_inpwd);

		ll_input = (LinearLayout) findViewById(R.id.ll_input);
		iv_1 = (ImageView) findViewById(R.id.iv_1);
		iv_2 = (ImageView) findViewById(R.id.iv_2);
		iv_3 = (ImageView) findViewById(R.id.iv_3);
		iv_4 = (ImageView) findViewById(R.id.iv_4);
		iv_5 = (ImageView) findViewById(R.id.iv_5);
		iv_6 = (ImageView) findViewById(R.id.iv_6);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		tv_tips = (TextView) findViewById(R.id.tv_tips);
		tv_ok = (TextView) findViewById(R.id.tv_ok);
		tv_cancel = (TextView) findViewById(R.id.tv_cancel);
		ll_input.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				customKeyBoardService.showKeyboard(false);
			}
		});
		
//		inputManager = (InputMethodManager)et_pwd.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		et_pwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s != null) {
					if (s.length() == 0) {
						iv_1.setVisibility(View.INVISIBLE);
						iv_2.setVisibility(View.INVISIBLE);
						iv_3.setVisibility(View.INVISIBLE);
						iv_4.setVisibility(View.INVISIBLE);
						iv_5.setVisibility(View.INVISIBLE);
						iv_6.setVisibility(View.INVISIBLE);
					} else if (s.length() == 1) {
						iv_1.setVisibility(View.VISIBLE);
						iv_2.setVisibility(View.INVISIBLE);
						iv_3.setVisibility(View.INVISIBLE);
						iv_4.setVisibility(View.INVISIBLE);
						iv_5.setVisibility(View.INVISIBLE);
						iv_6.setVisibility(View.INVISIBLE);
					} else if (s.length() == 2) {
						iv_1.setVisibility(View.VISIBLE);
						iv_2.setVisibility(View.VISIBLE);
						iv_3.setVisibility(View.INVISIBLE);
						iv_4.setVisibility(View.INVISIBLE);
						iv_5.setVisibility(View.INVISIBLE);
						iv_6.setVisibility(View.INVISIBLE);
					}

					else if (s.length() == 3) {
						iv_1.setVisibility(View.VISIBLE);
						iv_2.setVisibility(View.VISIBLE);
						iv_3.setVisibility(View.VISIBLE);
						iv_4.setVisibility(View.INVISIBLE);
						iv_5.setVisibility(View.INVISIBLE);
						iv_6.setVisibility(View.INVISIBLE);
					}

					else if (s.length() == 4) {
						iv_1.setVisibility(View.VISIBLE);
						iv_2.setVisibility(View.VISIBLE);
						iv_3.setVisibility(View.VISIBLE);
						iv_4.setVisibility(View.VISIBLE);
						iv_5.setVisibility(View.INVISIBLE);
						iv_6.setVisibility(View.INVISIBLE);
					} else if (s.length() == 5) {
						iv_1.setVisibility(View.VISIBLE);
						iv_2.setVisibility(View.VISIBLE);
						iv_3.setVisibility(View.VISIBLE);
						iv_4.setVisibility(View.VISIBLE);
						iv_5.setVisibility(View.VISIBLE);
						iv_6.setVisibility(View.INVISIBLE);
					} else if (s.length() == 6) {
						iv_1.setVisibility(View.VISIBLE);
						iv_2.setVisibility(View.VISIBLE);
						iv_3.setVisibility(View.VISIBLE);
						iv_4.setVisibility(View.VISIBLE);
						iv_5.setVisibility(View.VISIBLE);
						iv_6.setVisibility(View.VISIBLE);
					}
				}

			}
		});

		
		
		tv_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onOkClickListen();
			}
		});
		tv_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CardpwdInputDialog.this.dismiss();
			}
		});
		
		
	}
	/**
	 * 密码框确定按钮点击事件
	 */
	private void onOkClickListen(){
		if (et_pwd.getText().toString().length() != 6) {
			Toast.makeText(context, "请输入密码", Toast.LENGTH_SHORT).show();
			return;
		}
//		Log.d("ryx", "onOkClickListen=="+pwdStr);
		myListener.getPwdVal(pwdStr);
		et_pwd.setText("");
		CardpwdInputDialog.this.dismiss();
	}
}
