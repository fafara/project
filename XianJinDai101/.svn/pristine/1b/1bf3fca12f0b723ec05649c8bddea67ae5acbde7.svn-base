package com.ryx.ryxkeylib;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ryx.ryxkeylib.listener.CardTipPwdListener;
import com.ryx.ryxkeylib.listener.EditPwdListener;
import com.ryx.ryxkeylib.service.CustomKeyBoardService;
import com.ryx.ryxkeylib.view.CardpwdInputDialog;

import java.text.MessageFormat;

public class MainActivity extends Activity implements OnClickListener{
private EditText editText1,editText2;
private TextView textView1;
CardpwdInputDialog inputDialog;// 卡密码输入框
public static boolean isDebug=false;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		innitView();
		regKeyBoard();
	}
	private void regKeyBoard() {
		inputDialog = new CardpwdInputDialog(MainActivity.this, R.style.mydialog,
				new CardTipPwdListener() {
				@Override
				public void getPwdVal(String text) {
					textView1.setText("密码:"+text);
//					Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
				}
				},isDebug);
		
		CustomKeyBoardService customKeyBoardService=CustomKeyBoardService.registerKeyBoardForEdit(MainActivity.this,isDebug,editText1, new EditPwdListener() {
			@Override
			public void getPwdVal(String realVal,String disVal) {
				textView1.setText("密码:"+realVal);
				editText1.setText(disVal);
			}
			@Override
			public void getPwdDisVal(String disVal, int count) {
				editText1.setText(disVal);
			}
			@Override
			public void pwdViewOkbtnLisener() {
				// TODO Auto-generated method stub
				
			}

		});
		customKeyBoardService.setEditMaxLenth(10);
//		customKeyBoardService.setletterisorder(true);

		CustomKeyBoardService.registerKeyBoardForEdit(MainActivity.this,isDebug, editText2,new EditPwdListener() {
			
			@Override
			public void getPwdVal(String realVal,String disVal) {
				textView1.setText("密码:"+realVal);
				editText2.setText(disVal);
			}

			@Override
			public void getPwdDisVal(String disVal, int count) {
				editText2.setText(disVal);
			}

			@Override
			public void pwdViewOkbtnLisener() {
				
			}
		},false);
	}
	private void innitView() {
		findViewById(R.id.toggleButton1).setOnClickListener(this);
		editText1=(EditText) findViewById(R.id.editText1);
		editText2=(EditText) findViewById(R.id.editText2);
		textView1=(TextView) findViewById(R.id.textView1);
		findViewById(R.id.button1).setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		
			
			if(v.getId()==R.id.button1){
				
			
			inputDialog.show();
//			inputDialog.setTip("");
			inputDialog.setTip(MessageFormat.format(getResources()
					.getString(R.string.tips_mini_itron_pay),
					"3.20"));
			}
			if(v.getId()== R.id.toggleButton1){
				if(((ToggleButton)v).isChecked()){
					isDebug=true;
				}else{
					isDebug=false;
				}
				regKeyBoard();
			}
	}

}
