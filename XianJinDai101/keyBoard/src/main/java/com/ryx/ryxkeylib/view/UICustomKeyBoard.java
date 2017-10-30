package com.ryx.ryxkeylib.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.ryx.ryxkeylib.R;
import com.ryx.ryxkeylib.listener.InteriorPwdListener;
import com.ryx.ryxkeylib.util.OrderEncoder;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * 自定义公用键盘(只负责键盘相关操作)
 *
 */
public class UICustomKeyBoard extends View {

	Context context;
	Activity activity;

	private ButtonOnClickListener listener_btn = null;

	private LayoutInflater layoutInflater;
	private ViewFlipper viewFilpper;
	public Dialog popup;
	private View keyboardsView;
	private View popView;
	private LinearLayout container;
	private LinearLayout bottomLayout;
	
	private Button btn_number, btn_character, btn_symbol;
	private ImageButton btn_shift,btn_clear;
	public boolean isCapital = false, isShowing = false;

	public ArrayList<HashMap<String, Object>> params = new ArrayList<HashMap<String, Object>>();
	InteriorPwdListener interiorPwdListener;
	
	//calculate
//	private int maxLength = 0;
	private int defaultShowType = View.VISIBLE;
	private UIKeyBoardStyle[] keyboardStyle = null;
	private int hasKeyboardClick = 0;
	private boolean letterisorder=true;
	/**
	 * DIGITAL  数字键盘
	 * LETTER   字母键盘
	 * SYMBOL   符号键盘
	 */
	public enum UIKeyBoardStyle {
		DIGITAL, LETTER, SYMBOL
	}
	
	public UICustomKeyBoard(Context context) {
		super(context);
		this.context = context;
		activity = (Activity) context;
		hasKeyboardClick = R.id.btn_number;
		listener_btn = new ButtonOnClickListener();
	}

	/**
	 *
	 * @param letterisorder
     */
	public void setletterisorder(boolean  letterisorder){
		this.letterisorder=letterisorder;
	}

	public void hideKeyboard() {
		final View v0 = activity.getWindow().peekDecorView();
		if (v0 != null && v0.getWindowToken() != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v0.getWindowToken(), 0);
		}
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	public void showKeyboard(InteriorPwdListener interiorPwdListen, boolean isS) {
		
//		WindowManager wm = activity.getWindowManager();
//		 
//	     int width = wm.getDefaultDisplay().getWidth();
		
		this.interiorPwdListener=interiorPwdListen;
		if(popup!=null&&popup.isShowing()){
			return ;
		} 

		layoutInflater = activity.getLayoutInflater();
		popView = layoutInflater.inflate(R.layout.popwindow_keyboard, null);
		if(popup==null)
		popup=new Dialog(activity,R.style.myryxkeyBoarddialog);
		popup.setContentView(popView);
		popup.setCancelable(true);
		popView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				  int height =popView.findViewById(R.id.linearLayout1).getTop();
//				  int y=(int) event.getY();
//				  Log.d("ryx", "popView--onTouch=====height="+height+",yy="+y);
                  if(event.getAction()==MotionEvent.ACTION_UP){
                        	  popup.dismiss();
                  }    
				return true;
			}
		});
		
		popup.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				hasKeyboardClick=0;
				interiorPwdListener.pwdViewDismiss();
			}
		});
		viewFilpper = (ViewFlipper) popView.findViewById(R.id.viewFlipper1);
//		viewFilpper.setInAnimation(AnimationUtils.loadAnimation(context, R.drawable.animation_in_from_bottom));
		viewFilpper.setFlipInterval(600000);
		if(!popup.isShowing())
		popup.show();
		
		viewFilpper.startFlipping();
		
		bottomLayout = (LinearLayout) popView.findViewById(R.id.linearLayout5);
		bottomLayout.setVisibility(View.VISIBLE);
		setIsShowKeyBoardBottomLayout(isS);
		btn_number = (Button) popView.findViewById(R.id.btn_number);
		btn_number.setOnClickListener(listener_btn);
		btn_character = (Button) popView.findViewById(R.id.btn_character);
		btn_character.setOnClickListener(listener_btn);
		btn_symbol = (Button) popView.findViewById(R.id.btn_symbol);
		btn_symbol.setOnClickListener(listener_btn);

		popView.findViewById(R.id.btn_keyboard_ok).setOnClickListener(listener_btn);

		container = (LinearLayout) popView.findViewById(R.id.llkeyboard);

		switchView(UIKeyBoardStyle.DIGITAL,true);
	}
	/**
	 * 重新绘制键盘布局
	 * @param style
	 * @param isAgain 是否需要打乱排序,默认是不打乱
	 */
	public void switchView(UIKeyBoardStyle style,boolean... isAgain) {
		container.removeAllViews();

		if (style == UIKeyBoardStyle.DIGITAL) {
			
			keyboardsView = activity.getLayoutInflater().inflate(R.layout.keyboard_digitals, null);
			
			int[] digital = OrderEncoder.reOrder();
			
			btn_clear = (ImageButton) keyboardsView.findViewById(R.id.btn_clear);
			btn_clear.setOnClickListener(listener_btn);

			for (int i = 0; i < 10; i++) {
				String btnStr = MessageFormat.format("btn_digital{0}", Integer.toString(i + 1));
				int id = context.getResources().getIdentifier(btnStr, "id", context.getPackageName());
				Button btn_digital = (Button) keyboardsView.findViewById(id);
				btn_digital.setText(Integer.toString(digital[i]));
				btn_digital.setTag(digital[i]);
				btn_digital.setOnClickListener(listener_btn);
			}
			
			container.addView(keyboardsView);
			
		} else if (style == UIKeyBoardStyle.LETTER) {

			if (isCapital)
				keyboardsView = activity.getLayoutInflater().inflate(R.layout.keyboard_character_capital, null);
			else {
				keyboardsView = activity.getLayoutInflater().inflate(R.layout.keyboard_character, null);
			}
			
			btn_clear = (ImageButton) keyboardsView.findViewById(R.id.btn_clear);
			btn_clear.setOnClickListener(listener_btn);
			btn_shift = (ImageButton) keyboardsView.findViewById(R.id.btn_shift);
			btn_shift.setOnClickListener(listener_btn);
			int[] letter=null;
			if(letterisorder){
				letter= OrderEncoder.getSequence(false);
			}else{
				letter= OrderEncoder.getSequence(isAgain);
			}
			for (int i = 0; i < 26; i++) {
				String btnStr = MessageFormat.format("btn_character{0}", Integer.toString(i + 1));
				int id = context.getResources().getIdentifier(btnStr, "id", context.getPackageName());
				Button btn_character = (Button) keyboardsView.findViewById(id);
				char val=(char)(0x41+letter[i]);
				String str = String.valueOf(val);
				btn_character.setTag( i);
				if(isCapital){
					btn_character.setText(str.toUpperCase());
				}else{
					btn_character.setText(str.toLowerCase());
				}
				btn_character.setOnClickListener(listener_btn);
			}
		
			container.addView(keyboardsView);
			
		} else if(style == UIKeyBoardStyle.SYMBOL) {
			keyboardsView = activity.getLayoutInflater().inflate(R.layout.keyboard_symbol_all, null);
			btn_clear = (ImageButton) keyboardsView.findViewById(R.id.btn_clear);
			btn_clear.setOnClickListener(listener_btn);
			
			for (int i = 0; i < 28; i++) {
				String btnStr = MessageFormat.format("btn_symbol{0}", Integer.toString(i + 1));
				int id = context.getResources().getIdentifier(btnStr, "id", context.getPackageName());
				Button btn_symbol = (Button) keyboardsView.findViewById(id);
				btn_symbol.setTag(i);
				btn_symbol.setOnClickListener(listener_btn);
			}
			
			container.addView(keyboardsView);
		}
	}
	
	private void checkArraySize() {
		if(keyboardStyle == null) {
			keyboardStyle = new UIKeyBoardStyle[] {UIKeyBoardStyle.DIGITAL, UIKeyBoardStyle.LETTER, UIKeyBoardStyle.SYMBOL};
		}
	}
	
	
	class ButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final View v0 = activity.getWindow().peekDecorView();
			
			if (v0 != null && v0.getWindowToken() != null) {
				
				InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v0.getWindowToken(), 0);
			}

			if (v.getId() == R.id.btn_keyboard_ok) {
				hideKeyBoard();
				interiorPwdListener.pwdViewOkbtnLisener();
			} else if (v.getId() == R.id.btn_number) {
				checkArraySize();
				for (int i = 0; i < keyboardStyle.length; i++) {
					if(UIKeyBoardStyle.DIGITAL == keyboardStyle[i]) {
						if (hasKeyboardClick != R.id.btn_number) {
							btn_character.setTextColor(getResources().getColor(R.color.keytabNoselectTextBc));
							btn_symbol.setTextColor(getResources().getColor(R.color.keytabNoselectTextBc));
							((Button) v).setTextColor(getResources().getColor(R.color.keytabselectTextBc));
							switchView(UIKeyBoardStyle.DIGITAL,true);
						}
					}
				}
				hasKeyboardClick = R.id.btn_number;
				
			} else if (v.getId() == R.id.btn_character) {
				checkArraySize();
				for (int i = 0; i < keyboardStyle.length; i++) {
					if(UIKeyBoardStyle.LETTER == keyboardStyle[i]) {
						if (hasKeyboardClick != R.id.btn_character) {
							btn_number.setTextColor(getResources().getColor(R.color.keytabNoselectTextBc));
							btn_symbol.setTextColor(getResources().getColor(R.color.keytabNoselectTextBc));
							((Button) v).setTextColor(getResources().getColor(R.color.keytabselectTextBc));
							switchView(UIKeyBoardStyle.LETTER,true);
						}
					}
				}
				hasKeyboardClick = R.id.btn_character;
				
			} else if (v.getId() == R.id.btn_symbol) {
				
				checkArraySize();
				for (int i = 0; i < keyboardStyle.length; i++) {
					if(UIKeyBoardStyle.SYMBOL == keyboardStyle[i]) {
						if (hasKeyboardClick != R.id.btn_symbol) {
							btn_number.setTextColor(getResources().getColor(R.color.keytabNoselectTextBc));
							btn_character.setTextColor(getResources().getColor(R.color.keytabNoselectTextBc));
							((Button) v).setTextColor(getResources().getColor(R.color.keytabselectTextBc));
							
							switchView(UIKeyBoardStyle.SYMBOL,true);
						}
					}
				}
				hasKeyboardClick = R.id.btn_symbol;
			
			} else if (v.getId() == R.id.btn_clear) {
				interiorPwdListener.clearVal();

			} else if (v.getId() == R.id.btn_shift) {
				
				if (isCapital) {
					// v.setBackgroundResource(R.drawable.shift_btn_normal);
					isCapital = false;
				} else {
					// v.setBackgroundResource(R.drawable.shift_btn_touch);
					isCapital = true;
				}
				switchView(UIKeyBoardStyle.LETTER,false);
				
			} else {
				checkArraySize();
				String str = "";
//				int index = et_password.getSelectionEnd();
				
				for (int i = 0; i < keyboardStyle.length; i++) {
					
					if(UIKeyBoardStyle.DIGITAL == keyboardStyle[i] || UIKeyBoardStyle.SYMBOL == keyboardStyle[i]) {
						str = ((Button) v).getText().toString().replaceAll(" ", "").trim();
						break;
					} else if(UIKeyBoardStyle.LETTER == keyboardStyle[i]) {
						
						if (isCapital) {
							str = ((Button) v).getText().toString().toUpperCase();
						} else {
							str = ((Button) v).getText().toString().toLowerCase();
						}
						break;
					}
				}
//				Log.d("ryx","字母=="+((Button) v).getText().toString());
				interiorPwdListener.getInteriorPwdVal(str);
//				StringBuffer strNew = new StringBuffer(et_password.getText().toString());
//				str = strNew.insert(index, str).toString();

//				index++;
//
//				if(maxLength >= str.length() && maxLength != 0) {
//					et_password.setText(str);
//					et_password.setSelection(index);
//				} else if(maxLength == 0) {
//					et_password.setText(str);
//					et_password.setSelection(index);
//				}
			}
		}
	};
	/**
	 * 设置是否显示键盘底部的行Layout，原因场景：如只需数字输入的时候，其他字母、符号按钮式点击不了的。
	 * @param isS
	 */
	private void setIsShowKeyBoardBottomLayout(boolean isS) {
		if(!isS) {
			defaultShowType = View.GONE;
			bottomLayout.setVisibility(View.INVISIBLE);
		} else {
			bottomLayout.setVisibility(View.VISIBLE);
		}
		
	}
	
//	/**
//	 * 设置显示键盘类型，目前有纯数字、字母、符号3种.
//	 * @param boardStyle
//	 */
//	public void setShowType(UIKeyBoardStyle... boardStyle) {
//		
//		if(boardStyle.length < 4) {
//			keyboardStyle = boardStyle;
//		} else {
//			keyboardStyle = new UIKeyBoardStyle[3];
//			for (int i = 0; i < boardStyle.length; i++) {
//				if(i < 3) {
//					keyboardStyle[i] = boardStyle[i];
//				} else {
//					break;
//				}
//			}
//		}
//	}
	
//	/**
//	 * 设置最大长度，请不要在<EditText>里设置maxLength，否则会报错
//	 * @param max
//	 */
//	public void setMaxLength(int max) {
//		if(max <= 0) {
//			maxLength = 0;
//		}else if(max > 0) {
//			maxLength = max;
//		}
//	}

	public void clearViews() {
		keyboardsView = null;
		if (container != null) {
			container.removeAllViews();
		}
		container = null;
		btn_character = null;
		btn_symbol = null;
		btn_clear = null;
		btn_shift = null;

		viewFilpper = null;
		popView = null;

		if (popup != null && popup.isShowing())
			popup.dismiss();
		popup = null;
		System.gc();
	}
	/**
	 * 隐藏密码软键盘
	 */
	public void hideKeyBoard(){
		if (popup != null && popup.isShowing())
			popup.dismiss();
		popup = null;
	}
}
