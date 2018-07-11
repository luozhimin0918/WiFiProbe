package com.ums.wifiprobe.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.ums.wifiprobe.R;
import com.ums.wifiprobe.data.DataResource;
import com.ums.wifiprobe.data.ProbeInfoDataRepository;
import com.ums.wifiprobe.ui.adapter.DetailAdapter;
import com.ums.wifiprobe.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by chenzhy on 2017/9/23.
 */

public class ProbeDetailActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.detail_linerlayout_date)
    LinearLayout layoutDate;
    @BindView(R.id.detail_linerlayout_type)
    LinearLayout layoutType;
    @BindView(R.id.detail_tv_date)
    TextView tvDate;
    @BindView(R.id.detail_tv_type)
    TextView tvType;
    @BindView(R.id.detail_lv)
    ListView listView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
//
//    @BindView(R.id.linelayout_back)
//    LinearLayout linearLayoutBack;
    @BindView(R.id.rotate_header_list_view_frame)
    PtrClassicFrameLayout mPtrFrame;


    private static final int ITEM_SIZE_OFFSET = 100;

    private int startOffset = 0;

    private List<String> optionList = new ArrayList<>(Arrays.asList("客流量","新客","老客","5分钟内客流"));


    DetailAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        initDatePicker();
        initSectionPicker();
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        String type = intent.getStringExtra("type");
        tvDate.setText(date);
        tvType.setText(type);
        mAdapter = new DetailAdapter(this, null);
        listView.setAdapter(mAdapter);

        layoutDate.setOnClickListener(this);
        layoutType.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                String scale = startOffset + "-" + ITEM_SIZE_OFFSET;
                loadData(tvType.getText().toString(), scale, tvDate.getText().toString(), 2);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                startOffset = 0;
                String scale = startOffset + "-" + ITEM_SIZE_OFFSET;
                loadData(tvType.getText().toString(), scale, tvDate.getText().toString(), 3);
            }


            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return super.checkCanDoRefresh(frame, content, header);
            }
        });
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        mPtrFrame.setPullToRefresh(false);
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 100);
    }


    //flag 1 normal 2 loadmore 3 refresh
    private void loadData(final String scaleValue, final String scale, final String date, final int flag) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                ProbeInfoDataRepository.getInstance().getTasks(scaleValue, scale, date, new DataResource.LoadTasksCallback() {
                    @Override
                    public void onTasksLoaded(final List list) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (flag == 2) {
                                    startOffset = startOffset + list.size();
                                    mAdapter.addData(list);
                                    mPtrFrame.refreshComplete();

                                } else if (flag == 3) {
                                    startOffset = list.size();
                                    mAdapter.setData(list);
                                    mPtrFrame.refreshComplete();
                                } else {
                                    mAdapter.setData(list);
                                }

                            }
                        });
                    }

                    @Override
                    public void onDataNotAvaliable() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.setData(null);
                                mPtrFrame.refreshComplete();
                            }
                        });
                    }
                });
            }
        }.start();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    public void CacheClearComplete() {
        mPtrFrame.autoRefresh();
        ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_linerlayout_date:
                showDatePicker();
                break;
            case R.id.detail_linerlayout_type:
                showSectionPicker();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private DatePickDialog datePicker;

    private void initDatePicker() {
        if (datePicker == null) {
            datePicker = new DatePickDialog(this);
            //设置上下年分限制
            datePicker.setYearLimt(5);
            //设置标题
            datePicker.setTitle("选择时间");
            //设置类型
            datePicker.setType(DateType.TYPE_YMD);
            //设置消息体的显示格式，日期格式
            datePicker.setMessageFormat("yyyy-MM-dd");
            //设置选择回调
            datePicker.setOnChangeLisener(null);
            //设置点击确定按钮回调
            datePicker.setOnSureLisener(new OnSureLisener() {
                @Override
                public void onSure(final Date date) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String tempDate = format.format(date);
                    tvDate.setText(tempDate);
                    if (tvDate.getText().toString().equals(TimeUtils.getDate(System.currentTimeMillis()))) {
                        if (!optionList.contains("5分钟内客流")) {
                            optionList.add("5分钟内客流");
                        }
                    } else {
                        if (optionList.contains("5分钟内客流")) {
                            optionList.remove("5分钟内客流");
                        }
                        if (tvType.getText().toString().equals("5分钟内客流")) {
                            tvType.setText("客流量");
                        }

                    }

                    mPtrFrame.autoRefresh();
                }
            });
        }
    }

    private void initSectionPicker() {
        if (optionsPickerView == null) {
            optionsPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {

                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    tvType.setText(optionList.get(options1));
                    mPtrFrame.autoRefresh();
                }
            }).build();
        }
    }

    public void showDatePicker() {
        if (datePicker == null) {
            initDatePicker();
        }
        optionsPickerView.setPicker(optionList);
        datePicker.show();
    }

    private OptionsPickerView optionsPickerView;


    public void showSectionPicker() {
        if (optionsPickerView == null) {
            initSectionPicker();
        }
        if (tvDate.getText().toString().equals(TimeUtils.getDate(System.currentTimeMillis()))) {
            if (!optionList.contains("5分钟内客流")) {
                optionList.add("5分钟内客流");
            }
        } else {
            if (optionList.contains("5分钟内客流")) {
                optionList.remove("5分钟内客流");
            }
            if (tvType.getText().toString().equals("5分钟内客流")) {
                tvType.setText("客流量");
            }
        }
        optionsPickerView.setPicker(optionList);
        int index=0;
        for(int i=0;i<optionList.size();i++){
            if(tvType.getText().equals(optionList.get(i))){
                index = i;
                break;
            }
        }
        optionsPickerView.setSelectOptions(index);
        optionsPickerView.show();
    }


}
