package com.ryx.payment.ruishua.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.bean.IconBean;
import com.ryx.payment.ruishua.utils.LogUtil;
import com.ryx.payment.ruishua.utils.PreferenceUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

@EActivity(R.layout.activity_more)
public class MoreActivity extends BaseActivity {
    public static int resultCode = 0x0011;
    @ViewById(R.id.tileleftImg)
    ImageView mBackImg;
    @ViewById(R.id.tv_title)
    TextView mTitle;
    @ViewById(R.id.tilerightImg)
    ImageView mMsgImg;
    @ViewById(R.id.rv_list)
    RecyclerView mListView;
    @ViewById(R.id.btn_more)
    Button mMoreBtn;

    private List<IconBean.IconMsgBean> mShowIconMsgBean = new ArrayList<>();
    private ArrayList<IconBean.IconMsgBean> mBottomIconMsgBean;
    private ArrayList<IconBean.IconMsgBean> mOriginalData;

    @AfterViews
    public void initViews() {
        mTitle.setText("更多");
        mBackImg.setVisibility(View.VISIBLE);
        mMsgImg.setVisibility(View.GONE);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mListView.setLayoutManager(manager);
        //判断用户是否存储过配置
        mBottomIconMsgBean = (ArrayList) getBottonListForShareStr().getGetIconList();
        Comparator bottomComparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                IconBean.IconMsgBean imb1 = (IconBean.IconMsgBean) o1;
                IconBean.IconMsgBean imb2 = (IconBean.IconMsgBean) o2;
                return imb1.getShow().compareTo(imb2.getShow());
            }
        };
        Collections.sort(mBottomIconMsgBean, bottomComparator);
        LogUtil.showLog("muxin", "size:" + mBottomIconMsgBean.size());
        RecyclerAdapter adapter = new RecyclerAdapter(this, mBottomIconMsgBean);
        mListView.setAdapter(adapter);
    }

    /**
     * 根据share中值获取底部值
     *
     * @return
     */
    public IconBean getBottonListForShareStr() {
        String bottomListStr = getFromRaw(R.raw.bottom_grid_common);
        IconBean bottomIconBean = handleInputStream(bottomListStr);
        ArrayList<IconBean.IconMsgBean> inconList = (ArrayList<IconBean.IconMsgBean>) bottomIconBean.getGetIconList();
        //去除show为1的
        Iterator<IconBean.IconMsgBean> iterator = inconList.iterator();
        while (iterator.hasNext()) {
            IconBean.IconMsgBean topBean = iterator.next();
            if (topBean.getShow().equals("1")) {
                //不显示的进行移除
                iterator.remove();
            }
        }
        if (PreferenceUtil.getInstance(MoreActivity.this).hasKey("rechargeflags")) {
            String str = PreferenceUtil.getInstance(MoreActivity.this).getString("rechargeflags", "");
            LogUtil.showLog("获取到shareStr==" + str);
            for (IconBean.IconMsgBean iconMsgBean : inconList) {
                iconMsgBean.setShow("1");
            }
            if (!TextUtils.isEmpty(str)) {
                try {
                    JSONArray jsonArray = new JSONObject(str).getJSONArray("getIconflags");
                    for (IconBean.IconMsgBean iconMsgBean : inconList) {
                        f:
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String flag = (String) jsonArray.get(i);
                            if (iconMsgBean.getFlag().equals(flag)) {
                                iconMsgBean.setShow("0");
                                break f;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return bottomIconBean;
    }

    // 从resources中的raw 文件夹中获取文件并读取数据
    public String getFromRaw(int id) {
        String result = "";
        try {
            InputStream in = getResources().openRawResource(id);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = new String(buffer, 0, buffer.length, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Click(R.id.btn_more)
    public void moreClickBtn() {
        saveShareFlags();
        Intent intent = new Intent();
        intent.putExtra("showArray", mBottomIconMsgBean);
        setResult(resultCode, intent);
        finish();
    }

    /**
     * 将选中的Flas保存到share中
     */
    public void saveShareFlags() {
        String jsonFlags = "";
        JSONObject object = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            object.put("desc", "icon功能列表");
            for (int i = 0; i < mBottomIconMsgBean.size(); i++) {
                IconBean.IconMsgBean bean = mBottomIconMsgBean.get(i);
                if (bean.getShow().equals("0")) {
                    jsonArray.put(bean.getFlag());
                }
            }
            object.put("getIconflags", jsonArray);
            String shareStr = object.toString();
            LogUtil.showLog("保存shareStr==" + shareStr);
            PreferenceUtil.getInstance(MoreActivity.this).saveString("rechargeflags", shareStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String saveJson() {
        String result = "";
        JSONObject object = new JSONObject();
        try {
            object.put("desc", "icon功能列表");
            object.put("result", "0");
            object.put("serviceid", "getIconlist");
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < mBottomIconMsgBean.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                IconBean.IconMsgBean bean = mBottomIconMsgBean.get(i);
                jsonObject.put("flag", bean.getFlag());
                jsonObject.put("id", bean.getId());
                jsonObject.put("idx", bean.getIdx());
                jsonObject.put("name", bean.getName());
                jsonObject.put("res", bean.getRes());
                jsonObject.put("show", bean.getShow());
                jsonArray.put(jsonObject);
            }
            object.put("getIconList", jsonArray);
            result = object.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * json转换
     */
    public IconBean handleInputStream(String inputString) {
        IconBean iconBean = new IconBean();
        try {
            JSONArray jsonArray = new JSONObject(inputString).getJSONArray("getIconList");
            ArrayList<IconBean.IconMsgBean> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                IconBean.IconMsgBean msgBean = new IconBean.IconMsgBean();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                msgBean.setFlag(jsonObject.getString("flag"));
                msgBean.setId(jsonObject.getString("id"));
                msgBean.setIdx(jsonObject.getString("idx"));
                msgBean.setName(jsonObject.getString("name"));
                msgBean.setRes(jsonObject.getString("res"));
                msgBean.setShow(jsonObject.getString("show"));
                msgBean.setPermission(jsonObject.getString("permission"));
                list.add(msgBean);
            }
            iconBean.setGetIconList(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iconBean;
    }

    @Click(R.id.tileleftImg)
    public void backImgClick() {
        finish();
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        private List<IconBean.IconMsgBean> mList;
        private Context mContext;

        public RecyclerAdapter(Context context, List<IconBean.IconMsgBean> list) {
            this.mList = list;
            this.mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.more_item_view, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final IconBean.IconMsgBean bean = mList.get(position);
            if (bean.getShow().equals("1")) {
                holder.mCheckBox.setChecked(false);
            } else if (bean.getShow().equals("0")) {
                holder.mCheckBox.setChecked(true);
            }
            holder.mTitle.setText(bean.getName());
            int id = getResources().getIdentifier(bean.getRes(), "drawable",
                    getApplicationContext().getPackageName());
            holder.mIconImg.setImageResource(id);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.mCheckBox.isChecked()) {
                        holder.mCheckBox.setChecked(false);
                        bean.setShow("1");
                    } else {
                        holder.mCheckBox.setChecked(true);
                        bean.setShow("0");
                    }
                }
            });

            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        bean.setShow("0");
                    } else {
                        bean.setShow("1");
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView mIconImg;
            private TextView mTitle;
            private CheckBox mCheckBox;

            public ViewHolder(View itemView) {
                super(itemView);
                mTitle = (TextView) itemView.findViewById(R.id.tv_more_title);
                mIconImg = (ImageView) itemView.findViewById(R.id.iv_more_img);
                mCheckBox = (CheckBox) itemView.findViewById(R.id.cb_show);
            }
        }
    }
}
