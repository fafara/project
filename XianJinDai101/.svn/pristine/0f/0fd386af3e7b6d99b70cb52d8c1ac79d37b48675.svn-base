package com.ryx.ryxcredit.newfragment.baseinfo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ryx.ryxcredit.R;
import com.ryx.ryxcredit.utils.CPreferenceUtil;
import com.ryx.ryxcredit.xjd.BaseInfoSuccesActivity;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaoBaoFragment extends Fragment implements View.OnClickListener {
    private BaseInfoSuccesActivity baseInfoActivity;
    private BaseInfoSuccesActivity callback;
    private CPreferenceUtil preferenceUtil;
    private View rootView;
    private AutoRelativeLayout rl_sixchooseone_taobaoaccount_login;
    private TextView tv_sixchooseone_taobaoaccount_login;
    private TextView tv_sixchooseone_taobaoaccount_line;
    private AutoRelativeLayout rl_sixchooseone_taobaophonenum;
    private TextView tv_sixchooseone_taobaophonenum_login;
    private TextView tv_sixchooseone_taobaophonenum_line;
    private Bundle args;
    private String authorise_info_url;

    private enum BaseInfoEnum {

        TAOBAOACCOUNT_INFO,
        TAOBAOPHONE_INFO,

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseInfoActivity = (BaseInfoSuccesActivity) getActivity();
        callback = (BaseInfoSuccesActivity) getActivity();
        preferenceUtil = CPreferenceUtil.getInstance(getActivity().getApplication());
        rootView = inflater.inflate(R.layout.xjd_employee_fragment_tao_bao, container, false);
        initView();
        authorise_info_url = ((BaseInfoSuccesActivity) getActivity()).getAuthorise_info_url();
        return rootView;
    }
/*
* 初始化化控件
* */
    private void initView() {
        rl_sixchooseone_taobaoaccount_login = (AutoRelativeLayout) rootView.findViewById(R.id.xjd_rl_sixchooseone_taobaoaccount_login);
        tv_sixchooseone_taobaoaccount_login = (TextView) rootView.findViewById(R.id.xjd_tv_sixchooseone_taobaoaccount_login);
        tv_sixchooseone_taobaoaccount_line = (TextView) rootView.findViewById(R.id.xjd_tv_sixchooseone_taobaoaccount_line);
        rl_sixchooseone_taobaophonenum = (AutoRelativeLayout) rootView.findViewById(R.id.xjd_rl_sixchooseone_taobaophonenum);
        tv_sixchooseone_taobaophonenum_login = (TextView) rootView.findViewById(R.id.xjd_tv_sixchooseone_taobaophonenum_login);
        tv_sixchooseone_taobaophonenum_line = (TextView) rootView.findViewById(R.id.xjd_tv_sixchooseone_taobaophonenum_line);
        rl_sixchooseone_taobaoaccount_login.setOnClickListener(this);
        rl_sixchooseone_taobaophonenum.setOnClickListener(this);
        Fragment taoBaoAccountFragment = getChildFragmentManager().findFragmentByTag(BaseInfoEnum.TAOBAOACCOUNT_INFO.name());
        if (taoBaoAccountFragment == null) {
            Log.i("LOG_TAG", "add new FragmentA !!");
            taoBaoAccountFragment = new TaoBaoAccountFragment();
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.xjd_sixchooseone_taobao, taoBaoAccountFragment, BaseInfoEnum.TAOBAOACCOUNT_INFO.name()).commit();
        } else {
            Log.i("LOG_TAG", "found existing FragmentA, no need to add it again !!");
        }
    }
/*
* 点击事件
* */

    @Override
    public void onClick(View v) {
        int id =v.getId();
        if (id==R.id.xjd_rl_sixchooseone_taobaoaccount_login){
                tv_sixchooseone_taobaoaccount_login.setTextColor(getResources().getColor(R.color.xjd_sixchooseone_blue));
                tv_sixchooseone_taobaoaccount_line.setBackgroundColor(getResources().getColor(R.color.xjd_sixchooseone_blue));
                tv_sixchooseone_taobaophonenum_login.setTextColor(getResources().getColor(R.color.xjd_sixchooseone_black));
                tv_sixchooseone_taobaophonenum_line.setBackgroundColor(getResources().getColor(R.color.xjd_sixchooseone_grey));
            Fragment taoBaoAccountFragment = getChildFragmentManager().findFragmentByTag(BaseInfoEnum.TAOBAOACCOUNT_INFO.name());
          //  if (taoBaoAccountFragment == null) {
                Log.i("LOG_TAG", "add new FragmentA !!");
                taoBaoAccountFragment = new TaoBaoAccountFragment();
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                args = new Bundle();
                args.putString("authorise_info_url", authorise_info_url);
                taoBaoAccountFragment.setArguments(args);
                fragmentTransaction.add(R.id.xjd_sixchooseone_taobao, taoBaoAccountFragment, BaseInfoEnum.TAOBAOACCOUNT_INFO.name()).commit();
           /* } else {
                Log.i("LOG_TAG", "found existing FragmentA, no need to add it again !!");
            }*/
        }else if(id==R.id.xjd_rl_sixchooseone_taobaophonenum){
                tv_sixchooseone_taobaoaccount_login.setTextColor(getResources().getColor(R.color.xjd_sixchooseone_black));
                tv_sixchooseone_taobaoaccount_line.setBackgroundColor(getResources().getColor(R.color.xjd_sixchooseone_grey));
                tv_sixchooseone_taobaophonenum_login.setTextColor(getResources().getColor(R.color.xjd_sixchooseone_blue));
                tv_sixchooseone_taobaophonenum_line.setBackgroundColor(getResources().getColor(R.color.xjd_sixchooseone_blue));

            Fragment taoBaoPhonenumberFragment = getChildFragmentManager().findFragmentByTag(BaseInfoEnum.TAOBAOPHONE_INFO.name());
            //       if (taoBaoPhonenumberFragment == null) {
                Log.i("LOG_TAG", "add new FragmentA !!");
                taoBaoPhonenumberFragment = new TaoBaoPhonenumberFragment();
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                args = new Bundle();
                args.putString("authorise_info_url", authorise_info_url);
                taoBaoPhonenumberFragment.setArguments(args);
                fragmentTransaction.replace(R.id.xjd_sixchooseone_taobao, taoBaoPhonenumberFragment, BaseInfoEnum.TAOBAOPHONE_INFO.name()).commit();
           /* } else {
                Log.i("LOG_TAG", "found existing FragmentA, no need to add it again !!");
            }*/

        }

    }
}
