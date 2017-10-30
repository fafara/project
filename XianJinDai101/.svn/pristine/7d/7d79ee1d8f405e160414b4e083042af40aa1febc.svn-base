package com.ryx.payment.ruishua.adapter

import android.content.Context
import android.view.View
import com.ryx.payment.ruishua.R
import com.ryx.payment.ruishua.bean.AdBeanMap
import com.ryx.quickadapter.inter.NoDoubleClickListener
import com.ryx.quickadapter.inter.OnListItemClickListener
import com.ryx.quickadapter.recyclerView.HelperAdapter
import com.ryx.quickadapter.recyclerView.HelperViewHolder
import com.ryx.swiper.utils.MapUtil

/**
 * Created by xucc on 2017/5/26.
 */
class RuiBeanGridAdapter(mList: MutableList<AdBeanMap>?, context: Context?, vararg layoutIds: Int) : HelperAdapter<AdBeanMap>(mList, context, *layoutIds) {
    private var  mOnItemClickListener: OnListItemClickListener<Any>?=null
    private var selectedIndex :Int = -1;
    override fun HelperBindData(viewHolder: HelperViewHolder ,position: Int, item: AdBeanMap?) {
        if(position==selectedIndex){
            viewHolder.setBackgroundColorRes(R.id.ruibeanitem_layot,R.color.blue)
            viewHolder.setTextColorRes(R.id.tv_disName,R.color.white)
            viewHolder.setTextColorRes(R.id.discount_tv,R.color.white)
            viewHolder.setTextColorRes(R.id.money_tv,R.color.white)
        }else{
            viewHolder.setBackgroundColorRes(R.id.ruibeanitem_layot,R.drawable.ruibeanrcyitem_bg)
            viewHolder.setTextColorRes(R.id.tv_disName,R.color.black)
            viewHolder.setTextColorRes(R.id.discount_tv,R.color.graytexts)
            viewHolder.setTextColorRes(R.id.money_tv,R.color.graytexts)
        }
        if (null != item && item.map != null) {
            val aditemMap = item.map
//            val title = MapUtil.get(aditemMap, "disPlame", "")
            viewHolder.setText(R.id.tv_disName, MapUtil.getString(aditemMap,"name"))
            viewHolder.setText(R.id.discount_tv, MapUtil.getString(aditemMap,"discount"))
            viewHolder.setText(R.id.money_tv, MapUtil.getString(aditemMap,"discountamount"))
//            //            int id = view.getResources().getIdentifier(item.getRes(), "drawable", view.getContext().getPackageName());
//            //            GlideUtils.getInstance().load(view.getContext(), id, (ImageView) viewHolder.getView(R.id.ivIcon));
//            val imgurl = MapUtil.get(aditemMap, "imgurl", "")
//            val defaultColor = GlideUtils.getInstance().randomDefaultColor
//            Glide.with(view.getContext())
//                    .load(imgurl)
//                    .placeholder(defaultColor)//显示加载时的图片
//                    .error(defaultColor)//加载失败默认显示的图片
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)//磁盘缓存
//                    .dontAnimate()//无动画
//                    .into(viewHolder.getView<View>(R.id.home_ad_item_imgview) as ImageView)
            //            GlideUtils.getInstance().load(view.getContext(),imgurl,(ImageView)viewHolder.getView(R.id.home_ad_item_imgview));
        }
        viewHolder.getItemView().setOnClickListener(object : NoDoubleClickListener() {
            override fun onNoDoubleClick(view: View) {
                if (mOnItemClickListener != null)
//                    view.setBackgroundColor(R.color.blue)
                    setSelectedIndex(position);
                    mOnItemClickListener!!.onItemClick(viewHolder.getItemView(), position, item)
            }
        })
    }

    /**
     * 设置选中的标
     */
    fun setSelectedIndex(index :Int){
        this.selectedIndex=index
    }
    fun setOnItemClickListener(mOnItemClickListener: OnListItemClickListener<Any>) {
        this.mOnItemClickListener = mOnItemClickListener

    }
}
