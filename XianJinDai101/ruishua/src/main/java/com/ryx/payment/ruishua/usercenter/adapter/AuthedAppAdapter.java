package com.ryx.payment.ruishua.usercenter.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.utils.AppIconUtil;
import com.ryx.quickadapter.inter.NoDoubleClickListener;
import com.ryx.quickadapter.inter.OnListItemClickListener;
import com.ryx.quickadapter.recyclerView.HelperAdapter;
import com.ryx.quickadapter.recyclerView.HelperViewHolder;
import com.ryx.swiper.utils.MapUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by XCC on 2017/2/14.
 */

public class AuthedAppAdapter extends HelperAdapter<Map<String,String>> {
    private OnListItemClickListener mOnItemClickListener = null;
    private View view;
    public void setOnItemClickListener(OnListItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public AuthedAppAdapter(List<Map<String,String>> data, Context context, int... layoutId) {
        super(data, context, layoutId);
    }

    @Override
    protected void HelperBindData(final HelperViewHolder viewHolder, final int position, final Map<String, String> item) {
        if (null != item) {


            view = viewHolder.itemView;
            viewHolder.setText(R.id.auth_app_name, item.get("branchid")+"("+ item.get("branchName")+")");
            viewHolder.setText(R.id.auth_app_username, "认证名称："+item.get("username"));
            String maplevel=item.get("level");
            int finalLevel= Integer.parseInt(maplevel)-1>2?2:Integer.parseInt(maplevel)-1;
            String level="V" +finalLevel;
            SpannableString sp = new SpannableString(level);
            //设置斜体
            sp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD_ITALIC), 0, level.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
          TextView textView= viewHolder.getView(R.id.auth_app_level);
            textView.setText(sp);
            ImageView imageView=viewHolder.getView(R.id.auth_applogo);
            imageView.setImageResource(AppIconUtil.selectIcoid( item.get("logoid")));

            ImageView iv_check_front=viewHolder.getView(R.id.iv_check_front);
         String check=   MapUtil.getString(item,"check","");
            if("1".equals(check)){
                iv_check_front.setVisibility(View.VISIBLE);
            }else{
                iv_check_front.setVisibility(View.INVISIBLE);
            }
//            int id = view.getResources().getIdentifier(item.getRes(), "drawable", view.getContext().getPackageName());
//            GlideUtils.getInstance().load(view.getContext(), id, (ImageView) viewHolder.getView(R.id.ivIcon));
        }
        viewHolder.getItemView().setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(viewHolder.getItemView(), position, item);
            }
        });
    }

}
