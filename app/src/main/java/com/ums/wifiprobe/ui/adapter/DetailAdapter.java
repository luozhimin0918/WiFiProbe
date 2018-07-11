package com.ums.wifiprobe.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ums.wifiprobe.R;
import com.ums.wifiprobe.service.greendao.MacBrandInfo;
import com.ums.wifiprobe.service.greendao.MacProbeInfo;
import com.ums.wifiprobe.ui.ComparisonInfo;
import com.ums.wifiprobe.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhy on 2017/9/23.
 */

public class DetailAdapter extends BaseAdapter {
    private Context mContext;
    private List<MacProbeInfo> mList;

    public DetailAdapter(Context context, List<MacProbeInfo> list) {
        this.mContext = context;
        if (list == null || list.size() == 0) {
            this.mList = new ArrayList<>();
        } else {
            this.mList = list;
        }
        ;
    }

    public void setData(List<MacProbeInfo> list) {
        if (list == null || list.size() == 0) {
            this.mList = new ArrayList<>();
        } else {
            this.mList = list;
        }
        notifyDataSetChanged();
    }

    public void addData(List<MacProbeInfo> list) {
        if (list == null || list.size() == 0) {

        } else {
            this.mList.addAll(list);
        }
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.listview_item_detail, null);
            viewHolder = new ViewHolder();
            viewHolder.tvBrandName = (TextView) convertView.findViewById(R.id.tv_brandname);
            viewHolder.tvBrandMac = (TextView) convertView.findViewById(R.id.tv_brandmac);
            viewHolder.tvTimes = (TextView) convertView.findViewById(R.id.tv_brandtime);
            viewHolder.tvDuration = (TextView) convertView.findViewById(R.id.tv_brandduration);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MacProbeInfo info = mList.get(position);
//        viewHolder.tvMac.setText(info.getMac());
        if (info.getMacBrandInfos() == null) {
            MacBrandInfo info1 = new MacBrandInfo();
            info1.setBrandName("其他品牌");
            info.setMacBrandInfo(info1);
        }
        viewHolder.tvBrandName.setText(info.getMacBrandInfos().getBrandName());
        viewHolder.tvBrandMac.setText(info.getMac());
        viewHolder.tvTimes.setText(TimeUtils.getDateTime(info.getCreateTime()) + "进，共" + info.getProbeTimes() + "次");
        int miniutes;
        long durationTime = info.getDurationTime();
        miniutes = Math.round(durationTime / 60000.0f);
        if (miniutes == 0) {
            viewHolder.tvDuration.setText("停留不足1分钟");
        } else {
            viewHolder.tvDuration.setText("共停留" + miniutes + "分钟");
        }

        return convertView;
    }

    class ViewHolder {
        public TextView tvBrandName;
        public TextView tvBrandMac;
        public TextView tvTimes;
        public TextView tvDuration;
    }

    private String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(time);
    }
}
