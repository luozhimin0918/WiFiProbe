
package com.ums.wifiprobe.ui.customview;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.ums.wifiprobe.R;

/**
 * Custom implementation of the MarkerView.
 * 
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView {

    private TextView tvContent;
    private String datasetName;

    public MyMarkerView(Context context, int layoutResource,String setNmae) {
        super(context, layoutResource);

        tvContent = (TextView) findViewById(R.id.tvContent);
        this.datasetName = setNmae;
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText(datasetName + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {

            tvContent.setText(datasetName+ Utils.formatNumber(e.getY(), 0, true));
        }

        super.refreshContent(e, highlight);
    }


    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
