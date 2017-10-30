package com.ryx.ryxcredit.utils;

import android.view.View;

/**
 * Created by laomao on 16/6/29.
 */
public class CRecyclerViewUtil {
    /**
     * 展开
     *
     * @param v
     */
    public static void expand(final View v) {
        // item显示的条目
        /*
        final LinearLayout textLayout = (LinearLayout) v.findViewById(R.id.c_repaylist_text_layout);
        final LinearLayout ImageLayout = (LinearLayout) v.findViewById(R.id.c_repaylist_detail_layout);

        ImageLayout.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = ImageLayout.getMeasuredHeight();
        final int textMissHeight = textLayout.getMeasuredHeight();

        ImageLayout.getLayoutParams().height = 0;
        ImageLayout.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                ImageLayout.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                textLayout.getLayoutParams().height = textMissHeight - (int) (textMissHeight * interpolatedTime);
                if (interpolatedTime == 1) {
                    textLayout.setVisibility(View.GONE);
                }
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);*/
    }

    /**
     * 收缩
     *
     * @param v
     */
    public static void collapse(final View v) {
        // item显示的条目
        /*
        final LinearLayout textLayout = (LinearLayout) v.findViewById(R.id.c_repaylist_text_layout);
        final LinearLayout ImageLayout = (LinearLayout) v.findViewById(R.id.c_repaylist_detail_layout);

        //// TODO: laomao 16/6/29 获取WRAP_CONTENT分配控件不正常，看动画代码的小伙伴研究下。
        textLayout.measure(LinearLayout.LayoutParams.MATCH_PARENT, 60);

        final int initialHeight = ImageLayout.getMeasuredHeight();
        final int targetHeight = textLayout.getMeasuredHeight();

        textLayout.getLayoutParams().height = 0;
        textLayout.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                textLayout.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                if (interpolatedTime == 1) {
                    ImageLayout.setVisibility(View.GONE);
                    textLayout.setVisibility(View.VISIBLE);
                } else {
                    ImageLayout.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);*/
    }

}
