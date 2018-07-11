package com.ums.wifiprobe.ui;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.ums.wifiprobe.service.greendao.MacTotalInfo;
import com.ums.wifiprobe.update.AppUpdateInfo;

import java.util.List;

/**
 * Created by chenzhy on 2017/9/17.
 */

public interface CustomerInfoShowContract {
    interface Presenter extends BasePresenter{
        void updateDayChartData(String date);
        void updateWeekChartData(String date);
        void updateMonthChartData(String date);
        void checkUpdate(AppUpdateInfo info);
        void startUpdate(AppUpdateInfo info,boolean showDialog);
    }
    interface View extends BaseView<Presenter>{
        void updateBarChartView(String date,String type, List<BarEntry> list);
        void updateCustomerBarChartView(String date,String type,List<BarEntry> list);
        void updatePieChartView(String date,String type, List<PieEntry> list,float totalValue);;
        void updateTotalView(String date,String type,MacTotalInfo info,int fiveMinuteCounts);
        void updateDetailListView(String date ,String type,List<MacTotalInfo> last, List<MacTotalInfo> now);
        void showLoadingDialog();
        void hideLoadingDialog();
        void showConfirmDialog(String title,String content,AppUpdateInfo info);
        void showDownloadDialog(String title,AppUpdateInfo info);
        void updateDownloadProgress(String title,int i,AppUpdateInfo info);
        void hideDownloadDialog();
    }
}
