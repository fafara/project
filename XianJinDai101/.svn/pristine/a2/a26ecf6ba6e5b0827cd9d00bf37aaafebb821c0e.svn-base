package com.ryx.ryxkeylib.service;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ryx.ryxkeylib.listener.EditPwdListener;
import com.ryx.ryxkeylib.listener.InteriorPwdListener;
import com.ryx.ryxkeylib.view.UICustomKeyBoard;

/**
 * 
 * 自定义键盘服务,只负责业务逻辑，不负责键盘实际操作
 *
 */
public class CustomKeyBoardService implements OnFocusChangeListener, OnTouchListener,InteriorPwdListener {
	
	public UICustomKeyBoard customKeyBoard;
	EditText editText;
	Context context;
	
	private boolean isShowBottomLayout = true;
	private boolean isDebug ;
	private EditPwdListener cardpwdListener;
	private StringBuffer realypwd=new StringBuffer();
	private StringBuffer dispwd=new StringBuffer();
	private int maxLength=-1; 
	/**
	 * 是否已经执行完成监听
	 */
	private boolean flag;
	private boolean letterisorder=true;
	/**
	 * CustomKeyBoardService 自定义软键盘构造创建方法
	 * @param context
	 * @param editText
	 * @param EditPwdListener 输入密码监听
	 * @param isShowBottomLayout   
	 * isShowBottomLayout：
	 * default:true[View.VISIBLE]
	 * 设置是否显示键盘底部的行Layout，原因场景：如只需数字输入的时候，其他字母、符号按钮式点击不了的。
	 * isShowBottomLayout[]只会获取第一个，因此，你写再多也没用。
	 */
	public CustomKeyBoardService(Context context, EditText editText,boolean isDebug, EditPwdListener cardpwdListener,boolean... isShowBottomLayout) {
		this.context = context;
		this.cardpwdListener=cardpwdListener;
		if(isShowBottomLayout.length == 0) {
			this.isShowBottomLayout = true;
		} else {
			this.isShowBottomLayout = isShowBottomLayout[0];
		}
		this.isDebug=isDebug;
		//创建键盘UI操作对象
		customKeyBoard = new UICustomKeyBoard(context);
		this.editText = editText;
		editText.setOnFocusChangeListener(this);
		editText.setOnTouchListener(this);
		editText.setCursorVisible(false);
	}
	/**
	 * 设置editText最大数
	 * @param maxLength
	 */
	public void setEditMaxLenth(int maxLength){
		this.maxLength=maxLength;
	}
	public void setletterisorder(boolean letterisorder){
		this.letterisorder=letterisorder;
	}
//	private void loadEditTextMaxLength() {
//		Log.d("ryx", "开始反射=============");
//	       Class editTextCla = (Class) editText.getClass();  
//	       try {
//	    	   Field[] fs = editTextCla.getDeclaredFields();  
//	    		Log.d("ryx", "反射1111============="+fs.length);
//	    	   for (int i = 0; i < fs.length; i++) {
//	    		   Log.d("ryx", "getName=="+fs[i].getName());
//			}
////		 Field maxLenFile=	editTextCla.getDeclaredField("maxLength");
////		int maxLenth=(int) maxLenFile.get(editText);
////		Log.d("ryx", "最大限制:"+maxLenth);
//		} catch (Exception e) {
//			Log.d("ryx", "反射ERROR============="+e.getMessage());
//		}
//	}
	/**
	 * 指定Edit注册RYX密码键盘
	 * @param context
	 * @param editText
	 * @param isShowBottomLayout 是否显示数字,字母,字符tab选项,默认显示，若为false则不显示只显示数字键盘
	 */
	public static CustomKeyBoardService registerKeyBoardForEdit(Context context, boolean isdebug ,EditText editText,EditPwdListener cardpwdListener ,boolean... isShowTabLayout){
		return new CustomKeyBoardService(context, editText,isdebug,cardpwdListener,isShowTabLayout);
	}
	
	public void showKeyboard(boolean isS){
		this.customKeyBoard.showKeyboard(this, isS);
	}
	
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (!hasFocus) {// 失去焦点
			editText.setFocusable(false);
			customKeyBoard.clearViews();
			customKeyBoard.isShowing = false;
		} else {}//得到焦点
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		final View v0 = ((Activity) context).getWindow().peekDecorView();
		if (v0 != null && v0.getWindowToken() != null) {
			InputMethodManager imm = (InputMethodManager) ((Activity) context).getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v0.getWindowToken(), 0);
		}
		((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		if (event.getAction() == MotionEvent.ACTION_UP) {

			v.setFocusableInTouchMode(true);
			v.setFocusable(true);
			v.requestFocus();

			if (customKeyBoard.popup == null || customKeyBoard.popup.isShowing() == false || customKeyBoard.isShowing == false) {
				customKeyBoard.clearViews();
				customKeyBoard.isCapital = false;
				customKeyBoard.isShowing = true;
				
//				int inputback = ((EditText) v).getInputType();
//				((EditText) v).setInputType(InputType.TYPE_NULL);
				customKeyBoard.showKeyboard(this, isShowBottomLayout);
//				((EditText) v).onTouchEvent(event);
//				((EditText) v).setInputType(inputback);
//				String str = editText.getText().toString();
//				editText.setSelection(str.length());
				return true;
			} else {
				return true;
			}
		}
		return false;
	}
	@Override
	public void getInteriorPwdVal(String text) {
		//当前未设置最大值,或者当前未达到最大值都会响应键盘值
		if(maxLength<0||dispwd.length()<maxLength){
			realypwd.append(text);
			if(isDebug){
				dispwd.append(text);
			}else{
				dispwd.append("*");
			}
		}
		cardpwdListener.getPwdDisVal(dispwd.toString(), dispwd.length());
	}
	@Override
	public void pwdViewDismiss() {
		if(!flag){
			pwdViewDismissLisener();
		}else{
			flag=false;
		}
	}
	@Override
	public void clearVal() {
		realypwd.delete(0,realypwd.length());
		dispwd.delete(0,dispwd.length());
		cardpwdListener.getPwdDisVal(dispwd.toString(), dispwd.length());
	}
	private void pwdViewDismissLisener(){
		
		if(isDebug){
			cardpwdListener.getPwdVal(realypwd.toString(),dispwd.toString());
		}else{
			cardpwdListener.getPwdVal(ryxEncryptUserPwd(realypwd.toString()),dispwd.toString());
		}
	}
	/**
	 * ryx密码加密
	 * @param value
	 */
	private String ryxEncryptUserPwd(String pwd){
		Log.d("ryx", "加密原值======"+pwd);
//		return QtPayEncode.encryptUserPwd(pwd, false);
		return pwd+"==111==";
	}
	@Override
	public void pwdViewOkbtnLisener() {
		flag=true;
		pwdViewDismissLisener();
		cardpwdListener.pwdViewOkbtnLisener();
	}


}
