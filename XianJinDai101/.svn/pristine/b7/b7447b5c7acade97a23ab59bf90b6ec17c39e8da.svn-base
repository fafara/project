package com.ryx.ryxcredit.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ryx.ryxcredit.R;

/**
 * Created by laomao on 16/8/10.
 */
public class CRyxSlideUnlock extends RelativeLayout {

    private Drawable track;
    private View background;

    public interface ISlideToEnd {
        void slideToEnd();
    }

    private ISlideToEnd listener;
    private SeekBar seekbar;
    private TextView label;
    private int thumbWidth;

    public CRyxSlideUnlock(Context context) {
        super(context);
        init(context, null);
    }

    public CRyxSlideUnlock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CRyxSlideUnlock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setListen(ISlideToEnd listener) {
        this.listener = listener;
    }

    private void init(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.c_widget_slideunlock, this, true);

        label = (TextView) findViewById(R.id.slider_label);
        seekbar = (SeekBar) findViewById(R.id.slider_seekbar);
        background = findViewById(R.id.slider_bg);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SlideToUnlockView);
        String text = attributes.getString(R.styleable.SlideToUnlockView_c_text);
        Drawable thumb = attributes.getDrawable(R.styleable.SlideToUnlockView_c_thumb);

        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        seekbar.measure(w, h);

        if (thumb == null) {
//            thumb = getResources().getDrawable(R.drawable.c_slidetounlock_thumb);
            thumb=getNewDrawable(context,R.drawable.c_slidetounlock_thumb,seekbar.getMeasuredHeight()*2,seekbar.getMeasuredHeight());
        }
        track = attributes.getDrawable(R.styleable.SlideToUnlockView_c_track);
        attributes.recycle();

        thumbWidth = thumb.getIntrinsicWidth();

        if (track != null) {
            background.setBackgroundDrawable(track);
        }

        if (text != null) {
            label.setText(text);
        }
        label.setPadding(thumbWidth, 0, 0, 0);

        int defaultOffset = seekbar.getThumbOffset();
        seekbar.setThumb(thumb);
        seekbar.setThumbOffset(20);


        seekbar.setOnTouchListener(new OnTouchListener() {
            private boolean isInvalidMove;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return isInvalidMove = motionEvent.getX() > thumbWidth;
                    case MotionEvent.ACTION_MOVE:
                        return isInvalidMove;
                    case MotionEvent.ACTION_UP:
                        return isInvalidMove;
                }
                return false;
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
//                label.setAlpha(1f - progress * 0.02f);
//                if(progress==1)
//                    label.setAlpha(1);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() < 100) {
                    ObjectAnimator anim = ObjectAnimator.ofInt(seekBar, "progress", 0);
                    anim.setInterpolator(new AccelerateDecelerateInterpolator());
                    anim.setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime));
                    anim.start();
                }
                else {
                    if (listener != null) {
                        listener.slideToEnd();
                        label.setText("确认开通");
                    }
                }
            }
        });
    }
    //调用函数缩小图片
    public BitmapDrawable getNewDrawable(Context context, int restId, int dstWidth, int dstHeight){
        Bitmap Bmp = BitmapFactory. decodeResource(
                context.getResources(), restId);
        Bitmap bmp = Bmp.createScaledBitmap(Bmp, dstWidth, dstHeight, true);
        BitmapDrawable d = new BitmapDrawable(getResources(),bmp);
        Bitmap bitmap = d.getBitmap();
        if (bitmap.getDensity() == Bitmap.DENSITY_NONE) {
            d.setTargetDensity(context.getResources().getDisplayMetrics());
        }
        return d;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isInEditMode()) {
            return;
        }
        int h=seekbar.getMeasuredHeight();

        if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            background.getLayoutParams().height = seekbar.getHeight() + fromDpToPx(3);
        }
    }
    private int fromDpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}
