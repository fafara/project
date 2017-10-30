//
// DO NOT EDIT THIS FILE.
// Generated using AndroidAnnotations 4.3.0.
// 
// You can create a larger work that contains this file and distribute that work under terms of your choice.
//

package com.ryx.payment.ruishua.authenticate.creditcard;

import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import com.ryx.payment.ruishua.R;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.builder.PostActivityStarter;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class CreditCardAuthStep1Activity_
    extends CreditCardAuthStep1Activity
    implements HasViews, OnViewChangedListener
{
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(R.layout.activity_credit_card_auth_step1);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        onViewChangedNotifier_.notifyViewChanged(this);
    }

    public static CreditCardAuthStep1Activity_.IntentBuilder_ intent(Context context) {
        return new CreditCardAuthStep1Activity_.IntentBuilder_(context);
    }

    public static CreditCardAuthStep1Activity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new CreditCardAuthStep1Activity_.IntentBuilder_(fragment);
    }

    public static CreditCardAuthStep1Activity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new CreditCardAuthStep1Activity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        this.tv_instruction1 = ((TextView) hasViews.findViewById(R.id.tv_instruction1));
        this.tv_instruction2 = ((TextView) hasViews.findViewById(R.id.tv_instruction2));
        View view_btn_next = hasViews.findViewById(R.id.btn_next);
        View view_tilerightImg = hasViews.findViewById(R.id.tilerightImg);
        View view_tileleftImg = hasViews.findViewById(R.id.tileleftImg);

        if (view_btn_next!= null) {
            view_btn_next.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    CreditCardAuthStep1Activity_.this.swipeCard();
                }
            }
            );
        }
        if (view_tilerightImg!= null) {
            view_tilerightImg.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    CreditCardAuthStep1Activity_.this.showHelp();
                }
            }
            );
        }
        if (view_tileleftImg!= null) {
            view_tileleftImg.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    CreditCardAuthStep1Activity_.this.closeWindow();
                }
            }
            );
        }
        initViews();
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<CreditCardAuthStep1Activity_.IntentBuilder_>
    {
        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, CreditCardAuthStep1Activity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), CreditCardAuthStep1Activity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), CreditCardAuthStep1Activity_.class);
            fragmentSupport_ = fragment;
        }

        @Override
        public PostActivityStarter startForResult(int requestCode) {
            if (fragmentSupport_!= null) {
                fragmentSupport_.startActivityForResult(intent, requestCode);
            } else {
                if (fragment_!= null) {
                    if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                        fragment_.startActivityForResult(intent, requestCode, lastOptions);
                    } else {
                        fragment_.startActivityForResult(intent, requestCode);
                    }
                } else {
                    if (context instanceof Activity) {
                        Activity activity = ((Activity) context);
                        ActivityCompat.startActivityForResult(activity, intent, requestCode, lastOptions);
                    } else {
                        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                            context.startActivity(intent, lastOptions);
                        } else {
                            context.startActivity(intent);
                        }
                    }
                }
            }
            return new PostActivityStarter(context);
        }
    }
}