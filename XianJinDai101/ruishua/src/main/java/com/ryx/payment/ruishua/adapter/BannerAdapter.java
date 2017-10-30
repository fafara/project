package com.ryx.payment.ruishua.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.bean.BannerInfo;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter extends PagerAdapter {

    private List<BannerInfo> mList;
    private Context mContext;
    private List<ViewHolder> mViewHolderList = new ArrayList<>();

    public BannerAdapter(List<BannerInfo> list, Context context) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ViewHolder) object).itemView;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final ViewHolder viewHolder;
        if (mViewHolderList.size() > 0) {
            viewHolder = mViewHolderList.get(0);
            mViewHolderList.remove(0);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.banner_img, null);
            viewHolder = new ViewHolder(view);
        }
        BannerInfo bannerInfo = mList.get(position % mList.size());
        Glide.with(mContext).load(bannerInfo.getIconResId()).asBitmap().dontAnimate().into(viewHolder.mImageView);
        container.addView(viewHolder.itemView, -1, null);
        return viewHolder;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewHolder viewHolder = (ViewHolder) object;
        (container).removeView(viewHolder.itemView);
        mViewHolderList.add(viewHolder);
        container.removeView(viewHolder.itemView);
        //container.removeView((View) object);
    }

    public static class ViewHolder {

        View itemView;
        ImageView mImageView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
            mImageView = (ImageView) itemView.findViewById(R.id.banner_img);
        }
    }
}
