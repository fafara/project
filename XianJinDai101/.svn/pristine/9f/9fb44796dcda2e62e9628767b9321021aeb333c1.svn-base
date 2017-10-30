package com.ryx.ryxcredit.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * 提高额度矩形图像
 */
public class CPromoteRectangleDrawable extends Drawable {

    private int height;
    private int color;

    private static final float LONG_RATION_HEIGHT=1.25f;
    private static final float WIDTH_RATION_HEIGHT=0.7f;
    private static final int STROKE_WIDTH=3;
    private static final int padding=0;
    public CPromoteRectangleDrawable(int height, int color) {
        this.height = height;
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas) {

        Paint paint=new Paint();
        paint.setColor(this.color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(STROKE_WIDTH);

        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        Path path = new Path();//三角形

        path.moveTo(padding,padding);

        path.lineTo(height*LONG_RATION_HEIGHT-padding,padding);


        path.lineTo(height*WIDTH_RATION_HEIGHT-padding, height-padding);

        path.lineTo(padding,height-padding);

        path.close();

        canvas.drawPath(path, paint);


    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
