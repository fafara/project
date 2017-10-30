package com.ryx.payment.ruishua.usercenter.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ryx.payment.ruishua.R;
import com.ryx.swiper.utils.MapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ryx.swiper.utils.MapUtil.getString;

/**
 * Created by XCC on 2016/12/30.
 */

public class DeViceExpanListViewAdapter implements ExpandableListAdapter {
    private Context context;
    private List<Map> groupData=new ArrayList<>();
    private List<Map> childData=new ArrayList<>();
    public DeViceExpanListViewAdapter(Context context, List<Map> groupData, List<Map> childData) {
        this.context = context;
        this.groupData = groupData;
        this.childData = childData;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return groupData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childData.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_de_vice_list_item, null);
            holder = new GroupViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.tv_devname = (TextView) convertView.findViewById(R.id.tv_devname);
            holder.tv_devid = (TextView) convertView.findViewById(R.id.tv_devid);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
       Map groupDataMap= this.groupData.get(groupPosition);
        if (isExpanded) {
            holder.img.setImageResource(R.drawable.img_up);
        } else {
            holder.img.setImageResource(R.drawable.img_down);
        }
        holder.tv_devname.setText(getString(groupDataMap,"devname"));
        holder.tv_devid.setText(getString(groupDataMap,"devid"));
        convertView.setPadding(0,20,0,0);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_de_vice_list_child_item_table, null);
            holder = new ChildViewHolder();
            holder.child_item_layout1=(LinearLayout) convertView.findViewById(R.id.child_item_layout1);
            holder.child_item_layout2=(LinearLayout) convertView.findViewById(R.id.child_item_layout2);
            holder.child_item_layout3=(LinearLayout) convertView.findViewById(R.id.child_item_layout3);
            holder.child_item_layout4=(LinearLayout) convertView.findViewById(R.id.child_item_layout4);
            holder.child_item_layout5=(LinearLayout) convertView.findViewById(R.id.child_item_layout5);
            holder.child_item_layout6=(LinearLayout) convertView.findViewById(R.id.child_item_layout6);
            holder.child_item_layout7=(LinearLayout) convertView.findViewById(R.id.child_item_layout7);
            holder.child_item_layout8=(LinearLayout) convertView.findViewById(R.id.child_item_layout8);
            holder.child_item_layout9=(LinearLayout) convertView.findViewById(R.id.child_item_layout9);
            holder.nomsg_layout=(LinearLayout) convertView.findViewById(R.id.nomsg_layout);
            holder.nomsg_textview=(TextView) convertView.findViewById(R.id.nomsg_textview);

            //TextView_layout2
            holder.child_item_layout2_text1=(TextView) convertView.findViewById(R.id.child_item_layout2_text1);
            holder.child_item_layout2_text2=(TextView) convertView.findViewById(R.id.child_item_layout2_text2);
            holder.child_item_layout2_text3=(TextView) convertView.findViewById(R.id.child_item_layout2_text3);
            holder.child_item_layout2_text4=(TextView) convertView.findViewById(R.id.child_item_layout2_text4);
            //TextView_layout3
            holder.child_item_layout3_text1=(TextView) convertView.findViewById(R.id.child_item_layout3_text1);
            holder.child_item_layout3_text2=(TextView) convertView.findViewById(R.id.child_item_layout3_text2);
            holder.child_item_layout3_text3=(TextView) convertView.findViewById(R.id.child_item_layout3_text3);
            holder.child_item_layout3_text4=(TextView) convertView.findViewById(R.id.child_item_layout3_text4);
            //TextView_layout4
            holder.child_item_layout4_text1=(TextView) convertView.findViewById(R.id.child_item_layout4_text1);
            holder.child_item_layout4_text2=(TextView) convertView.findViewById(R.id.child_item_layout4_text2);
            holder.child_item_layout4_text3=(TextView) convertView.findViewById(R.id.child_item_layout4_text3);
            holder.child_item_layout4_text4=(TextView) convertView.findViewById(R.id.child_item_layout4_text4);
            //TextView_layout5
            holder.child_item_layout5_text1=(TextView) convertView.findViewById(R.id.child_item_layout5_text1);
            holder.child_item_layout5_text2=(TextView) convertView.findViewById(R.id.child_item_layout5_text2);
            holder.child_item_layout5_text3=(TextView) convertView.findViewById(R.id.child_item_layout5_text3);
            holder.child_item_layout5_text4=(TextView) convertView.findViewById(R.id.child_item_layout5_text4);
            //TextView_layout6
            holder.child_item_layout6_text1=(TextView) convertView.findViewById(R.id.child_item_layout6_text1);
            holder.child_item_layout6_text2=(TextView) convertView.findViewById(R.id.child_item_layout6_text2);
            holder.child_item_layout6_text3=(TextView) convertView.findViewById(R.id.child_item_layout6_text3);
            holder.child_item_layout6_text4=(TextView) convertView.findViewById(R.id.child_item_layout6_text4);
            //TextView_layout7
            holder.child_item_layout7_text1=(TextView) convertView.findViewById(R.id.child_item_layout7_text1);
            holder.child_item_layout7_text2=(TextView) convertView.findViewById(R.id.child_item_layout7_text2);
            holder.child_item_layout7_text3=(TextView) convertView.findViewById(R.id.child_item_layout7_text3);
            holder.child_item_layout7_text4=(TextView) convertView.findViewById(R.id.child_item_layout7_text4);
            //TextView_layout8
            holder.child_item_layout8_text1=(TextView) convertView.findViewById(R.id.child_item_layout8_text1);
            holder.child_item_layout8_text2=(TextView) convertView.findViewById(R.id.child_item_layout8_text2);
            holder.child_item_layout8_text3=(TextView) convertView.findViewById(R.id.child_item_layout8_text3);
            holder.child_item_layout8_text4=(TextView) convertView.findViewById(R.id.child_item_layout8_text4);
            //TextView_layout9
            holder.child_item_layout9_text1=(TextView) convertView.findViewById(R.id.child_item_layout9_text1);
            holder.child_item_layout9_text2=(TextView) convertView.findViewById(R.id.child_item_layout9_text2);
            holder.child_item_layout9_text3=(TextView) convertView.findViewById(R.id.child_item_layout9_text3);
            holder.child_item_layout9_text4=(TextView) convertView.findViewById(R.id.child_item_layout9_text4);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        //全都隐藏初始化
        holder.child_item_layout1.setVisibility(View.GONE);
        holder.child_item_layout2.setVisibility(View.GONE);
        holder.child_item_layout3.setVisibility(View.GONE);
        holder.child_item_layout4.setVisibility(View.GONE);
        holder.child_item_layout5.setVisibility(View.GONE);
        holder.child_item_layout6.setVisibility(View.GONE);
        holder.child_item_layout7.setVisibility(View.GONE);
        holder.child_item_layout8.setVisibility(View.GONE);
        holder.child_item_layout9.setVisibility(View.GONE);
        holder.nomsg_layout.setVisibility(View.GONE);

        Map childDataMap = this.childData.get(groupPosition);
        if(childDataMap.isEmpty()||!"0".equals(MapUtil.getString(childDataMap,"status"))){
            holder.nomsg_layout.setVisibility(View.VISIBLE);
            holder.nomsg_textview.setText(Html.fromHtml(MapUtil.getString(childDataMap,"msg")));
        }else{
            holder.child_item_layout1.setVisibility(View.VISIBLE);


           String layout2text1= MapUtil.getString(childDataMap,"layout2text1");
            if(!TextUtils.isEmpty(layout2text1)){
                holder.child_item_layout2.setVisibility(View.VISIBLE);
                holder.child_item_layout2_text1.setText(Html.fromHtml(layout2text1));

                String layout2text2= MapUtil.getString(childDataMap,"layout2text2");
                holder.child_item_layout2_text2.setText(Html.fromHtml(layout2text2));

                String layout2text3= MapUtil.getString(childDataMap,"layout2text3");
                holder.child_item_layout2_text3.setText(Html.fromHtml(layout2text3));

                String layout2text4= MapUtil.getString(childDataMap,"layout2text4");
//                holder.child_item_layout2_text4.setText(Html.fromHtml(layout2text4));
            }


            String layout3text1= MapUtil.getString(childDataMap,"layout3text1");
            if(!TextUtils.isEmpty(layout3text1)){
                holder.child_item_layout3.setVisibility(View.VISIBLE);
                holder.child_item_layout3_text1.setText(Html.fromHtml(layout3text1));

                String layout3text2= MapUtil.getString(childDataMap,"layout3text2");
                holder.child_item_layout3_text2.setText(Html.fromHtml(layout3text2));

                String layout3text3= MapUtil.getString(childDataMap,"layout3text3");
                holder.child_item_layout3_text3.setText(Html.fromHtml(layout3text3));

                String layout3text4= MapUtil.getString(childDataMap,"layout3text4");
//                holder.child_item_layout3_text4.setText(Html.fromHtml(layout3text4));
            }

            String layout4text1= MapUtil.getString(childDataMap,"layout4text1");
            if(!TextUtils.isEmpty(layout4text1)){
                holder.child_item_layout4.setVisibility(View.VISIBLE);
                holder.child_item_layout4_text1.setText(Html.fromHtml(layout4text1));

                String layout4text2= MapUtil.getString(childDataMap,"layout4text2");
                holder.child_item_layout4_text2.setText(Html.fromHtml(layout4text2));

                String layout4text3= MapUtil.getString(childDataMap,"layout4text3");
                holder.child_item_layout4_text3.setText(Html.fromHtml(layout4text3));

                String layout4text4= MapUtil.getString(childDataMap,"layout4text4");
//                holder.child_item_layout4_text4.setText(Html.fromHtml(layout4text4));
            }
            String layout5text1= MapUtil.getString(childDataMap,"layout5text1");
            if(!TextUtils.isEmpty(layout5text1)){
                holder.child_item_layout5.setVisibility(View.VISIBLE);
                holder.child_item_layout5_text1.setText(Html.fromHtml(layout5text1));

                String layout5text2= MapUtil.getString(childDataMap,"layout5text2");
                holder.child_item_layout5_text2.setText(Html.fromHtml(layout5text2));

                String layout5text3= MapUtil.getString(childDataMap,"layout5text3");
                holder.child_item_layout5_text3.setText(Html.fromHtml(layout5text3));

                String layout5text4= MapUtil.getString(childDataMap,"layout5text4");
//                holder.child_item_layout5_text4.setText(Html.fromHtml(layout5text4));
            }
            String layout6text1= MapUtil.getString(childDataMap,"layout6text1");
            if(!TextUtils.isEmpty(layout6text1)){
                holder.child_item_layout6.setVisibility(View.VISIBLE);
                holder.child_item_layout6_text1.setText(Html.fromHtml(layout6text1));

                String layout6text2= MapUtil.getString(childDataMap,"layout6text2");
                holder.child_item_layout6_text2.setText(Html.fromHtml(layout6text2));

                String layout6text3= MapUtil.getString(childDataMap,"layout6text3");
                holder.child_item_layout6_text3.setText(Html.fromHtml(layout6text3));

                String layout6text4= MapUtil.getString(childDataMap,"layout6text4");
//                holder.child_item_layout6_text4.setText(Html.fromHtml(layout6text4));
            }
            String layout7text1= MapUtil.getString(childDataMap,"layout7text1");
            if(!TextUtils.isEmpty(layout7text1)){
                holder.child_item_layout7.setVisibility(View.VISIBLE);
                holder.child_item_layout7_text1.setText(Html.fromHtml(layout7text1));

                String layout7text2= MapUtil.getString(childDataMap,"layout7text2");
                holder.child_item_layout7_text2.setText(Html.fromHtml(layout7text2));

                String layout7text3= MapUtil.getString(childDataMap,"layout7text3");
                holder.child_item_layout7_text3.setText(Html.fromHtml(layout7text3));

                String layout7text4= MapUtil.getString(childDataMap,"layout7text4");
//                holder.child_item_layout7_text4.setText(Html.fromHtml(layout7text4));
            }
            String layout8text1= MapUtil.getString(childDataMap,"layout8text1");
            if(!TextUtils.isEmpty(layout8text1)){
                holder.child_item_layout8.setVisibility(View.VISIBLE);
                holder.child_item_layout8_text1.setText(Html.fromHtml(layout8text1));

                String layout8text2= MapUtil.getString(childDataMap,"layout8text2");
                holder.child_item_layout8_text2.setText(Html.fromHtml(layout8text2));

                String layout8text3= MapUtil.getString(childDataMap,"layout8text3");
                holder.child_item_layout8_text3.setText(Html.fromHtml(layout8text3));

                String layout8text4= MapUtil.getString(childDataMap,"layout8text4");
//                holder.child_item_layout8_text4.setText(Html.fromHtml(layout8text4));
            }
            String layout9text1= MapUtil.getString(childDataMap,"layout9text1");
            if(!TextUtils.isEmpty(layout9text1)){
                holder.child_item_layout9.setVisibility(View.VISIBLE);
                holder.child_item_layout9_text1.setText(Html.fromHtml(layout9text1));

                String layout9text2= MapUtil.getString(childDataMap,"layout9text2");
                holder.child_item_layout9_text2.setText(Html.fromHtml(layout9text2));

                String layout9text3= MapUtil.getString(childDataMap,"layout9text3");
                holder.child_item_layout9_text3.setText(Html.fromHtml(layout9text3));

                String layout9text4= MapUtil.getString(childDataMap,"layout9text4");
//                holder.child_item_layout9_text4.setText(Html.fromHtml(layout9text4));
            }
        }
        return convertView;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }


    class GroupViewHolder {
        ImageView img;
        TextView tv_devname, tv_devid;
    }

    class ChildViewHolder {
        LinearLayout child_item_layout1,child_item_layout2,
                child_item_layout3,child_item_layout4,
                child_item_layout5,child_item_layout6,
                child_item_layout7,child_item_layout8,
                child_item_layout9,nomsg_layout;
        TextView nomsg_textview,
                child_item_layout2_text1,child_item_layout2_text2,child_item_layout2_text3,child_item_layout2_text4,
                 child_item_layout3_text1,child_item_layout3_text2,child_item_layout3_text3,child_item_layout3_text4,
                child_item_layout4_text1,child_item_layout4_text2,child_item_layout4_text3,child_item_layout4_text4,
                child_item_layout5_text1,child_item_layout5_text2,child_item_layout5_text3,child_item_layout5_text4,
                child_item_layout6_text1,child_item_layout6_text2,child_item_layout6_text3,child_item_layout6_text4,
                child_item_layout7_text1,child_item_layout7_text2,child_item_layout7_text3,child_item_layout7_text4,
                child_item_layout8_text1,child_item_layout8_text2,child_item_layout8_text3,child_item_layout8_text4,
                child_item_layout9_text1,child_item_layout9_text2,child_item_layout9_text3,child_item_layout9_text4;
    }

}
