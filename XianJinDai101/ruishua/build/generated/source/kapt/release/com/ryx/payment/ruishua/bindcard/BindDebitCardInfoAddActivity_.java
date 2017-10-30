//
// DO NOT EDIT THIS FILE.
// Generated using AndroidAnnotations 4.3.0.
// 
// You can create a larger work that contains this file and distribute that work under terms of your choice.
//

package com.ryx.payment.ruishua.bindcard;

import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.bindcard.adapter.BanksAdapter_;
import com.ryx.payment.ruishua.bindcard.adapter.BranchAdapter_;
import com.ryx.payment.ruishua.bindcard.adapter.CityAdapter_;
import com.ryx.payment.ruishua.bindcard.adapter.ProvinceAdapter_;
import org.androidannotations.api.builder.ActivityIntentBuilder;
import org.androidannotations.api.builder.PostActivityStarter;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;
import org.androidannotations.api.view.OnViewChangedNotifier;

public final class BindDebitCardInfoAddActivity_
    extends BindDebitCardInfoAddActivity
    implements HasViews, OnViewChangedListener
{
    private final OnViewChangedNotifier onViewChangedNotifier_ = new OnViewChangedNotifier();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        OnViewChangedNotifier previousNotifier = OnViewChangedNotifier.replaceNotifier(onViewChangedNotifier_);
        init_(savedInstanceState);
        super.onCreate(savedInstanceState);
        OnViewChangedNotifier.replaceNotifier(previousNotifier);
        setContentView(R.layout.activity_bind_debit_card_info_add);
    }

    private void init_(Bundle savedInstanceState) {
        OnViewChangedNotifier.registerOnViewChangedListener(this);
        this.banksAdapter = BanksAdapter_.getInstance_(this);
        this.provinceAdapter = ProvinceAdapter_.getInstance_(this);
        this.cityAdapter = CityAdapter_.getInstance_(this);
        this.branchAdapter = BranchAdapter_.getInstance_(this);
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

    public static BindDebitCardInfoAddActivity_.IntentBuilder_ intent(Context context) {
        return new BindDebitCardInfoAddActivity_.IntentBuilder_(context);
    }

    public static BindDebitCardInfoAddActivity_.IntentBuilder_ intent(android.app.Fragment fragment) {
        return new BindDebitCardInfoAddActivity_.IntentBuilder_(fragment);
    }

    public static BindDebitCardInfoAddActivity_.IntentBuilder_ intent(android.support.v4.app.Fragment supportFragment) {
        return new BindDebitCardInfoAddActivity_.IntentBuilder_(supportFragment);
    }

    @Override
    public void onViewChanged(HasViews hasViews) {
        this.edt_bankname = ((EditText) hasViews.findViewById(R.id.edt_bankname));
        this.edt_bankprovince = ((EditText) hasViews.findViewById(R.id.edt_bankprovince));
        this.edt_bankcity = ((EditText) hasViews.findViewById(R.id.edt_bankcity));
        this.edt_bank_branch = ((EditText) hasViews.findViewById(R.id.edt_bank_branch));
        this.edt_condition = ((EditText) hasViews.findViewById(R.id.edt_condition));
        this.btn_next = ((Button) hasViews.findViewById(R.id.btn_next));
        View view_iv_provinceright = hasViews.findViewById(R.id.iv_provinceright);
        View view_iv_cityright = hasViews.findViewById(R.id.iv_cityright);
        View view_iv_bank_branchright = hasViews.findViewById(R.id.iv_bank_branchright);
        View view_iv_nameright = hasViews.findViewById(R.id.iv_nameright);
        View view_tileleftImg = hasViews.findViewById(R.id.tileleftImg);

        if (this.edt_bankprovince!= null) {
            this.edt_bankprovince.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    BindDebitCardInfoAddActivity_.this.searchBankProvince();
                }
            }
            );
        }
        if (view_iv_provinceright!= null) {
            view_iv_provinceright.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    BindDebitCardInfoAddActivity_.this.searchBankProvince();
                }
            }
            );
        }
        if (this.edt_bankcity!= null) {
            this.edt_bankcity.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    BindDebitCardInfoAddActivity_.this.searchBankCity();
                }
            }
            );
        }
        if (view_iv_cityright!= null) {
            view_iv_cityright.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    BindDebitCardInfoAddActivity_.this.searchBankCity();
                }
            }
            );
        }
        if (this.edt_bank_branch!= null) {
            this.edt_bank_branch.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    BindDebitCardInfoAddActivity_.this.searchBankbranch();
                }
            }
            );
        }
        if (view_iv_bank_branchright!= null) {
            view_iv_bank_branchright.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    BindDebitCardInfoAddActivity_.this.searchBankbranch();
                }
            }
            );
        }
        if (this.edt_bankname!= null) {
            this.edt_bankname.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    BindDebitCardInfoAddActivity_.this.searchBankName();
                }
            }
            );
        }
        if (view_iv_nameright!= null) {
            view_iv_nameright.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    BindDebitCardInfoAddActivity_.this.searchBankName();
                }
            }
            );
        }
        if (this.btn_next!= null) {
            this.btn_next.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    BindDebitCardInfoAddActivity_.this.bindCard();
                }
            }
            );
        }
        if (view_tileleftImg!= null) {
            view_tileleftImg.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    BindDebitCardInfoAddActivity_.this.closeWindow();
                }
            }
            );
        }
        initViews();
    }

    public static class IntentBuilder_
        extends ActivityIntentBuilder<BindDebitCardInfoAddActivity_.IntentBuilder_>
    {
        private android.app.Fragment fragment_;
        private android.support.v4.app.Fragment fragmentSupport_;

        public IntentBuilder_(Context context) {
            super(context, BindDebitCardInfoAddActivity_.class);
        }

        public IntentBuilder_(android.app.Fragment fragment) {
            super(fragment.getActivity(), BindDebitCardInfoAddActivity_.class);
            fragment_ = fragment;
        }

        public IntentBuilder_(android.support.v4.app.Fragment fragment) {
            super(fragment.getActivity(), BindDebitCardInfoAddActivity_.class);
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