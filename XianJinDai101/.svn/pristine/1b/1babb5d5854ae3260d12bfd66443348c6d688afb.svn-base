package com.ryx.payment.ruishua.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rey.material.app.SimpleDialog;
import com.ryx.payment.ruishua.R;
import com.ryx.payment.ruishua.bean.DeviceInfo;
import com.ryx.payment.ruishua.inteface.BlueToothListener;

import java.util.ArrayList;

/**
 * Created by XCC on 2016/5/26.
 */
public class BluetoothDialog {
    private ListView list_device;
    SimpleDialog simpleDialog;
    private TextView tv_state, tv_title;
    private ProgressBar pro_state;
    private com.rey.material.widget.Button againsearchbtn;
    MyAdapter myadapter;
    private ArrayList<DeviceInfo> deviceinfo = new ArrayList<DeviceInfo>();
    BlueToothListener myListener;
    private boolean isInited;
    public BluetoothDialog(Context context, BlueToothListener listener){
        initBluetoothDialog(context,null,listener);
    }
    public BluetoothDialog(Context context,ArrayList<DeviceInfo> deviceinfoList, BlueToothListener listener) {
        initBluetoothDialog(context,deviceinfoList,listener);
    }
    public void initBluetoothDialog(Context context,ArrayList<DeviceInfo> deviceinfoList, BlueToothListener listener){
        if(deviceinfoList!=null){
            this.deviceinfo=deviceinfoList;
        }
        this.myListener=listener;
        simpleDialog = new SimpleDialog(context);
        View view=  LayoutInflater.from(context).inflate(R.layout.bluedevice,null);
        tv_title = (TextView)view.findViewById(R.id.tv_title);
        tv_title.setText("蓝牙设备列表");
        againsearchbtn=(com.rey.material.widget.Button) view.findViewById(R.id.againsearchbtn);
        againsearchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                initViewData();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        myListener.againsearch();
                    }
                }).start();

            }
        });
        tv_state = (TextView) view.findViewById(R.id.tv_state);
        pro_state = (ProgressBar) view.findViewById(R.id.pro_state);
        tv_state.setText("蓝牙设备正在努力搜索中……");
        list_device = (ListView)view.findViewById(R.id.listdevices);
        myadapter = new MyAdapter(context);
        list_device.setAdapter(myadapter);
        simpleDialog.setContentView(view);
        list_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myListener.getBlueToothMac(deviceinfo.get(position));
                simpleDialog.dismiss();
            }
        });
    }

//public void setattributes(Context context){
//    Window dialogWindow= simpleDialog.getWindow();
//    WindowManager m =((Activity) context).getWindowManager();
//        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = (int) (d.getHeight()); // 高度设置为屏幕的0.6
//        p.width = (int) (d.getWidth() ); // 宽度设置为屏幕的0.65
//        dialogWindow.setAttributes(p);
//}
    /**
     * 设置点击外部是否可取消
     * @param cancle
     */
    public void setCanceledOnTouchOutside(boolean cancle){
    simpleDialog.setCanceledOnTouchOutside(cancle);
}

    public boolean isShowing(){
      return  simpleDialog.isShowing();
    }
    /**
     * 当前蓝牙对话框是否已经初始化完毕
     */
    public boolean  isinitedBlueDialog(){

          return isInited;
//        if(tv_state!=null){
//            return true;
//        }else{
//            return false;
//        }
    }

    /**
     *初始化下蓝牙搜索框的初始文字及布局
     */
    public void initViewData(){
        pro_state.setVisibility(View.VISIBLE);
        tv_state.setText("蓝牙设备正在努力搜索中……");
        againsearchbtn.setVisibility(View.GONE);
    }
    public void SearchComplete(boolean iscomplete) {

        if (iscomplete) {
            if(deviceinfo.size()>0){
                tv_state.setText("蓝牙设备搜索完成,搜索结果如下:");
                againsearchbtn.setVisibility(View.GONE);
            }else{
                tv_state.setText("搜索完成,没有搜索到蓝牙设备!");
                againsearchbtn.setVisibility(View.VISIBLE);
            }
            pro_state.setVisibility(View.GONE);
        } else {
            tv_state.setText("蓝牙努力设备搜索中……");
            pro_state.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 展示对话框
     */
    public void show(){
        isInited=true;
        simpleDialog.show();
    }

    /**
     * 取消对话框
     */
    public void dismiss(){
        isInited=false;
        simpleDialog.dismiss();
    }
    public void RefreshList(ArrayList<DeviceInfo> deviceinfo) {
        this.deviceinfo = deviceinfo;
        myadapter.notifyDataSetChanged();
    }

    class MyAdapter extends BaseAdapter {

        private Context context;


        public MyAdapter(Context context) {
            this.context = context;

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub

            return deviceinfo.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }



        class ViewHolder {
            TextView title;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = ((Activity) context)
                        .getLayoutInflater();
                convertView = inflater.inflate(R.layout.device_name, null);
                viewHolder.title = (TextView) convertView
                        .findViewById(R.id.device_name);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.title.setText(deviceinfo.get(position).getDevicename()
                    + "\n" + deviceinfo.get(position).getDeviceid());

            return convertView;
        }
    }
}
