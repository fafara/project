
package com.ryx.payment.ruishua.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * 去掉惯性滚动 以及 短距离翻页的实现
 * 
 * @author Administrator
 */
public class CustomGallery extends Gallery {

    public CustomGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int kEvent;
        if (isScrollingLeft(e1, e2)) {
            // Check if scrolling left
            kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
        } else {
            // Otherwise scrolling right
            kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
        }
        onKeyDown(kEvent, null);
        return true;

    }

    private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
        return e2.getX() > e1.getX();
    }

    // @Override
    // public boolean onTouchEvent(MotionEvent event) {
    // final int action = event.getAction();
    // if(action==MotionEvent.ACTION_UP)
    // {
    // if(Constants.mainList.size()<=0)
    // {
    // System.out.println("=====s======");
    // }
    // }
    // return super.onTouchEvent(event);
    // }
}
