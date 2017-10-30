package com.ryx.quickadapter.recyclerView;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.ryx.quickadapter.recyclerView.animation.AlphaInAnimation;
import com.ryx.quickadapter.recyclerView.animation.BaseAnimation;

import java.util.List;

/**
 * <p>万能适配Adapter,减少赘于代码和加快开发流程</p>
 *
 * @author muxin
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BH> {
    protected List<T> mList;
    protected Context mContext;
    protected LayoutInflater mLInflater;
    protected int[] mLayoutId;
    private SparseArray<View> mConvertViews = new SparseArray<View>();
    //动画
    private int mLastPosition = -1;
    private boolean mOpenAnimationEnable = false;
    private BaseAnimation mSelectAnimation = new AlphaInAnimation();
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mDuration = 300;
    /**
     * @param data     数据源
     * @param context  上下文
     * @param layoutId 布局Id
     */
    public BaseAdapter(List<T> data, Context context, int... layoutId) {
        this.mList = data;
        this.mLayoutId = layoutId;
        this.mContext = context;
        this.mLInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        return checkLayoutIndex(mList.get(position), position);
    }

    @Override
    public BH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType < 0 || viewType > mLayoutId.length) {
            throw new ArrayIndexOutOfBoundsException("checkLayoutIndex > LayoutId.length");
        }
        if (mLayoutId.length == 0) {
            throw new IllegalArgumentException("not layoutId");
        }
        int layoutId = mLayoutId[viewType];
        View view = inflateItemView(layoutId, parent);
        BaseViewHolder viewHolder = (BaseViewHolder) view.getTag();
        if (viewHolder == null || viewHolder.getLayoutId() != layoutId) {
            viewHolder = new BaseViewHolder(mContext, layoutId, view);
            return viewHolder;
        }
        return viewHolder;
    }

    /**
     * 解析布局资源
     *
     * @param layoutId
     * @param viewGroup
     * @return
     */
    protected View inflateItemView(int layoutId, ViewGroup viewGroup) {
        View convertView = mConvertViews.get(layoutId);
        if (convertView == null) {
            convertView = mLInflater.inflate(layoutId,
                    viewGroup, false);
        }
        return convertView;
    }

    @Override
    public void onBindViewHolder(BH holder, int position) {
        final T item = mList.get(position);
        //添加动画
        addAnimation(holder);
        // 绑定数据
        onBindData(holder, position, item);
    }
    /**
     * 设置动画
     *
     * @param animation ObjectAnimator
     */
    public void openLoadAnimation(BaseAnimation animation) {
        this.mOpenAnimationEnable = true;
        this.mSelectAnimation = animation;
    }
    /**
     * add animation when you want to show time
     *
     * @param holder
     */
    public void addAnimation(RecyclerView.ViewHolder holder) {
        if (mOpenAnimationEnable) {
            if (holder.getLayoutPosition() > mLastPosition) {
                BaseAnimation animation = null;
                if (mSelectAnimation != null) {
                    animation = mSelectAnimation;
                }
                for (Animator anim : animation.getAnimators(holder.itemView)) {
                    startAnim(anim, holder.getLayoutPosition());
                    Log.d("animline",mLastPosition+"");
                }
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }

    /**
     * set anim to start when loading
     *
     * @param anim
     * @param index
     */
    protected void startAnim(Animator anim, int index) {
        anim.setDuration(mDuration).start();
        anim.setInterpolator(mInterpolator);
    }
    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    /**
     * 绑定数据到Item View上
     *
     * @param viewHolder
     * @param position   数据的位置
     * @param item       数据项
     */
    protected abstract void onBindData(BH viewHolder, int position, T item);

    public int checkLayoutIndex(T item, int position) {
        return 0;
    }

}
