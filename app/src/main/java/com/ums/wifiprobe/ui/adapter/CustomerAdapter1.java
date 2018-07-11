package com.ums.wifiprobe.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ums.wifiprobe.R;
import com.ums.wifiprobe.ui.ComparisonInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhy on 2017/9/23.
 */

public class CustomerAdapter1 extends BaseAdapter {
    private Context mContext;
    private List<ComparisonInfo> mList;

    public CustomerAdapter1(Context context, List<ComparisonInfo> list) {
        this.mContext = context;
        if (list == null || list.size() == 0) {
            this.mList = new ArrayList<>();
            mList.add(new ComparisonInfo("小时", "今日客流量", "昨日客流量", "对比"));
        } else {
            this.mList = list;
        }
        ;
    }

    public void setData(List<ComparisonInfo> list, boolean isWeek) {
        if (list == null || list.size() == 0) {
            this.mList = new ArrayList<>();
            if (isWeek) {
                mList.add(new ComparisonInfo("日期", "本周客流量", "上周客流量", "对比"));
            } else {
                mList.add(new ComparisonInfo("小时", "今日客流量", "昨日客流量", "对比"));
            }
        } else {
//            if (isWeek) {
//                mList.add(0,new ComparisonInfo("日期", "本周客流量", "上周客流量", "对比"));
//            } else {
//                mList.add(0,new ComparisonInfo("小时", "今日客流量", "昨日客流量", "对比"));
//            }
            this.mList = list;
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
        if (position == 0) {
            convertView = View.inflate(mContext, R.layout.listview_item_chartdetail_title, null);
        } else if (position % 2 == 0) {
            convertView = View.inflate(mContext, R.layout.listview_item_chartdetail_gray, null);
        } else {
            convertView = View.inflate(mContext, R.layout.listview_item_chartdetail_white, null);
        }

        TextView tvContent = (TextView) convertView.findViewById(R.id.tv_content);
        TextView tvCurValue = (TextView) convertView.findViewById(R.id.tv_curvalue);
        TextView tvLastValue = (TextView) convertView.findViewById(R.id.tv_lastvalue);
        TextView tvComparisonValue = (TextView) convertView.findViewById(R.id.tv_comparisonvalue);
        ComparisonInfo info = mList.get(position);
        tvContent.setText(info.getContent());
        tvCurValue.setText(info.getCurValue());
        tvLastValue.setText(info.getLastValue());
        tvComparisonValue.setText(info.getComparisonValue());
        return convertView;
    }

}
