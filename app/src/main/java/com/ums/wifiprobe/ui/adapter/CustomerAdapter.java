package com.ums.wifiprobe.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ums.wifiprobe.R;
import com.ums.wifiprobe.ui.ComparisonInfo;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhy on 2017/9/23.
 */

public class CustomerAdapter extends BaseAdapter {
    private Context mContext;
    private List<ComparisonInfo> mList;

    public CustomerAdapter(Context context, List<ComparisonInfo> list) {
        this.mContext = context;
        this.mList = list;

    }

    public void setData(List<ComparisonInfo> list) {
        this.mList = list;
        notifyDataSetChanged();

    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position==0 ? 0 : 1;
//    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
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
        if (position==0){

        }
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder(parent.getContext());
            convertView = viewHolder.getRootView();
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setDate(mList.get(position),position);
        return convertView;
    }

    static class ViewHolder {
        private static int bg0 = Color.rgb(0xf5, 0xf5, 0xf5);
        private static int bg1= Color.rgb(0xff, 0xff, 0xff);

        private View rootView;
        private TextView tvContent;
        private TextView tvCurValue;
        private TextView tvLastValue;
        private TextView tvComparisonValue;
        private ImageView ivComparisonState;

        public ViewHolder(Context context) {
            rootView = View.inflate(context, R.layout.listview_item_chartdetail_gray, null);
            tvContent = (TextView) rootView.findViewById(R.id.tv_content);
            tvCurValue = (TextView) rootView.findViewById(R.id.tv_curvalue);
            tvLastValue = (TextView) rootView.findViewById(R.id.tv_lastvalue);
            tvComparisonValue = (TextView) rootView.findViewById(R.id.tv_comparisonvalue);
            ivComparisonState = (ImageView) rootView.findViewById(R.id.iv_comparisonstate);
            rootView.setTag(this);
        }
        public View getRootView(){
            return  rootView;
        }

        public void setDate(ComparisonInfo info, int position) {
            rootView.setBackgroundColor(position % 2 == 1 ? bg0 : bg1);
            tvContent.setText(info.getContent());
            tvCurValue.setText(info.getCurValue());
            tvLastValue.setText(info.getLastValue());
            float comparisonValue = Float.valueOf(info.getComparisonValue());
            tvComparisonValue.setText(getPercentValue(comparisonValue));
            if(Float.valueOf(comparisonValue)>0){
                ivComparisonState.setImageResource(R.mipmap.icon_comparison_green);
                tvComparisonValue.setTextColor(Color.parseColor("#70ad46"));
            }else if(comparisonValue==0){
                ivComparisonState.setImageResource(R.mipmap.icon_comparison_blue);
                tvComparisonValue.setTextColor(Color.parseColor("#5a9bd5"));
            }else{
                ivComparisonState.setImageResource(R.mipmap.icon_comparison_red);
                tvComparisonValue.setTextColor(Color.parseColor("#c20000"));
            }
        }
    }

    private static String getPercentValue(float value) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
        return numberFormat.format(value*100) + "%";
    }
    public static class TitleViewHolder {

        private View rootView;
        private TextView tvContent;
        private TextView tvCurValue;
        private TextView tvLastValue;
        private TextView tvComparisonValue;
        private ImageView ivComparisonState;

        public TitleViewHolder(Context context) {
            rootView = View.inflate(context, R.layout.listview_item_chartdetail_title, null);
            tvContent = (TextView) rootView.findViewById(R.id.tv_content);
            tvCurValue = (TextView) rootView.findViewById(R.id.tv_curvalue);
            tvLastValue = (TextView) rootView.findViewById(R.id.tv_lastvalue);
            tvComparisonValue = (TextView) rootView.findViewById(R.id.tv_comparisonvalue);
            rootView.setTag(this);
        }
        public View getRootView(){
            return  rootView;
        }

        public void setDate(ComparisonInfo info) {
            tvContent.setText(info.getContent());
            tvCurValue.setText(info.getCurValue());
            tvLastValue.setText(info.getLastValue());
            tvComparisonValue.setText(info.getComparisonValue());
        }
    }


}
