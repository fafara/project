package com.ryx.payment.ruishua.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.ryx.payment.ruishua.R;

/**
 * 类名 :QQListView
 * @author tianyingzhong <br/>
 *	自定义的滑动删除listview，实现跟QQ类似的功能
 *	创建日期 : 2014-06-26。
*/
public class SlidableListView extends ListView
{

	// private static final int VELOCITY_SANP = 200;
	// private VelocityTracker mVelocityTracker;
	/**
	 * 用户滑动的最小距离
	 */
	private int touchSlop;

	/**
	 * 是否响应滑动
	 */
	private boolean isSliding;


	private int  bindPosition = -1;
	/**
	 * 手指按下时的x坐标
	 */
	private int xDown;
	/**
	 * 手指按下时的y坐标
	 */
	private int yDown;
	/**
	 * 手指移动时的x坐标
	 */
	private int xMove;
	/**
	 * 手指移动时的y坐标
	 */
	private int yMove;

	private LayoutInflater mInflater;

	private PopupWindow mPopupWindow;
	private int mPopupWindowHeight;
	private int mPopupWindowWidth;

	private Button delBtn;
	private Button boundBtn;

	/**
	 * 为删除按钮提供一个回调接口
	 */
	private SlidableListviewItemClickListener delListener;
	/**
	 * 为绑定按钮提供一个回调接口
	 */
	private SlidableListviewItemClickListener boundListener;
	/**
	 * 当前手指触摸的View
	 */
	private View mCurrentView;

	private Context mcontext;

	/**
	 * 当前手指触摸的位置
	 */
	private int mCurrentViewPos;

	/**
	 * 必要的一些初始化
	 *
	 * @param context
	 * @param attrs
	 */
	public SlidableListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mcontext = context;
		mInflater = LayoutInflater.from(context);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

		View pview = mInflater.inflate(R.layout.widget_del_or_bound_listview, null);
		delBtn = (Button) pview.findViewById(R.id.del_btn);
		boundBtn = (Button) pview.findViewById(R.id.bound_btn);
		boundBtn.setVisibility(View.GONE);

		mPopupWindow = new PopupWindow(pview, LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		/**
		 * 先调用下measure,否则拿不到宽和高
		 */
		mPopupWindow.getContentView().measure(0, 0);
		mPopupWindowHeight = mPopupWindow.getContentView().getMeasuredHeight();
		mPopupWindowWidth = mPopupWindow.getContentView().getMeasuredWidth();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		int action = ev.getAction();
		int x = (int) ev.getX();
		int y = (int) ev.getY();
		switch (action)
		{

		case MotionEvent.ACTION_DOWN:
			xDown = x;
			yDown = y;
			/**
			 * 如果当前popupWindow显示，则直接隐藏，然后屏蔽ListView的touch事件的下传
			 */
			if (mPopupWindow!=null && mPopupWindow.isShowing())
			{
				dismissPopWindow();
				return false;
			}
			// 获得当前手指按下时的item的位置
			mCurrentViewPos = pointToPosition(xDown, yDown);
//			if(bindPosition!=-1){
//				
//			}else{
//				View pview = mInflater.inflate(R.layout.listview_del, null);
//				delBtn = (Button) pview.findViewById(R.id.del_btn);
//				mPopupWindow = new PopupWindow(pview, LinearLayout.LayoutParams.WRAP_CONTENT,
//						LinearLayout.LayoutParams.WRAP_CONTENT);
//				/**
//				 * 先调用下measure,否则拿不到宽和高
//				 */
//				mPopupWindow.getContentView().measure(0, 0);
//				mPopupWindowHeight = mPopupWindow.getContentView().getMeasuredHeight();
//				mPopupWindowWidth = mPopupWindow.getContentView().getMeasuredWidth();
//			}
//			

			// 获得当前手指按下时的item
			View view = getChildAt(mCurrentViewPos - getFirstVisiblePosition());
			mCurrentView = view;
			break;
		case MotionEvent.ACTION_MOVE:
			xMove = x;
			yMove = y;
			int dx = xMove - xDown;
			int dy = yMove - yDown;
			/**
			 * 判断是否是从右到左的滑动
			 */
//			if(mCurrentViewPos != bindPosition){
				if (xMove < xDown && Math.abs(dx) > touchSlop && Math.abs(dy) < touchSlop)
				{
					isSliding = true;
				}
//			}
//			if(bindPosition!=-1){
//				boundBtn.setVisibility(View.GONE);
//			}

			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		int action = ev.getAction();
		/**
		 * 如果是从右到左的滑动才相应
		 */

		if (isSliding)
		{
			switch (action)
			{
			case MotionEvent.ACTION_MOVE:

				showPopupWindow();
				// Log.e(TAG, "mPopupWindow.getHeight()=" + mPopupWindowHeight);

				break;
			case MotionEvent.ACTION_UP:
				isSliding = false;

			}
			// 相应滑动期间屏幕itemClick事件，避免发生冲突
			return true;
		}

		return super.onTouchEvent(ev);
	}

	/**
	 * 隐藏popupWindow
	 */
	private void dismissPopWindow()
	{
		if (mPopupWindow != null && mPopupWindow.isShowing())
		{
			mPopupWindow.dismiss();
		}
	}

	public void setDelButtonClickListener(SlidableListviewItemClickListener dellistener)
	{
		this.delListener = dellistener;
	}

	public void setBoundListener(SlidableListviewItemClickListener boundListener) {
		this.boundListener = boundListener;
	}


	public int getBindPosition() {
		return bindPosition;
	}

	public void setBindPosition(int bindPosition) {
		this.bindPosition = bindPosition;
	}
	private void showPopupWindow(){
		int[] location = new int[2];

		if(mCurrentView==null)
			return;
		// 获得当前item的位置x与y
		mCurrentView.getLocationOnScreen(location);
//		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
//		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
//		mCurrentView.measure(w, h);
		if(getChildCount()<=0)
		{
			return;
		}
		int height =getChildAt(0).getHeight();
		int width = 150;
		if(height/91>1)
		width = width*(height/91);
		// 设置popupWindow的动画
		mPopupWindow.setAnimationStyle(R.style.popwindow_delete_btn_anim_style);
		mPopupWindow.update();
		mPopupWindow.showAtLocation(mCurrentView, Gravity.LEFT | Gravity.TOP,
				location[0] + mCurrentView.getWidth(), location[1] );
		mPopupWindow.update(width,height);
		// 设置绑定按钮的回调
		boundBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (boundListener != null)
				{
					boundListener.clickHappend(mCurrentViewPos);
					mPopupWindow.dismiss();
				}
			}
		});
		// 设置删除按钮的回调
		delBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (delListener != null)
				{
					delListener.clickHappend(mCurrentViewPos);
					mPopupWindow.dismiss();
				}
			}
		});
	}
	/**
	 * 设置点击响应滑动的展示pop
	 */
	public void setOnListClickSliding(){
		showPopupWindow();
	}

}
