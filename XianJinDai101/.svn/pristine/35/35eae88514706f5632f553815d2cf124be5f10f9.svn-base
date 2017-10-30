package com.ryx.payment.ruishua.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.app.SimpleDialog;
import com.ryx.payment.ruishua.R;

/**
 * Created by XCC on 2016/5/27.
 */
public class AudioTypesDialog {
    private ListView list_device;
    Context context;
    private String[] devsNames={} ;
    MyAdapter myadapter;
    AudioTypeItemSlectListener myListener;
    SimpleDialog simpleDialog;
    public AudioTypesDialog(Context context, AudioTypeItemSlectListener audioTypeItemSlectListener){
        simpleDialog = new SimpleDialog(context);
        this.myListener=audioTypeItemSlectListener;
        View view=  LayoutInflater.from(context).inflate(R.layout.audiotypes,null);
        list_device = (ListView)view.findViewById(R.id.listdevices);

        myadapter = new MyAdapter(context);
        list_device.setAdapter(myadapter);

        list_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                myListener.onItemClick(position);
                simpleDialog.dismiss();
            }
        });
        simpleDialog.setContentView(view);
    }
    /**
     * 展示
     */
    public void show(){
        simpleDialog.show();
    }

    public void RefreshList(String[] devsNames) {
        this.devsNames = devsNames;
        myadapter.notifyDataSetChanged();
    }
    class MyAdapter extends BaseAdapter {

        private Context context;

        // private List<String> arrayList;

        public MyAdapter(Context context) {
            this.context = context;

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub

            return devsNames.length;
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
                viewHolder.title.setGravity(Gravity.CENTER);
                viewHolder.title.setHeight(100);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.title.setText(devsNames[position]);

            return convertView;
        }
    }
    public interface AudioTypeItemSlectListener {
        public void onItemClick(int itemId);
    }
}
