package com.ryx.ryxkeylib.listener;

public interface EditPwdListener {
		/**
		 * 密码键盘关闭后回调返回真实密码和显示密码
		 * @param realVal 返回密码值,如果Debug模式则返回明文否则密文
		 */
		public void getPwdVal(String realVal, String disVal);
		
		/**
		 * 键盘点击按键响应值
		 * @param disVal 返回键盘点击后所有显示值,如果debug模式则显示明文,否则显示*
		 * @param count 返回当前键盘点击值个数
		 */
		public void getPwdDisVal(String disVal, int count);
		/**
		 * 密码键盘完成按钮点击事件监听
		 */
		 public void pwdViewOkbtnLisener();
}
