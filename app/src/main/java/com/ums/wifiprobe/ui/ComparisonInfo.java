package com.ums.wifiprobe.ui;

/**
 * Created by chenzhy on 2017/9/23.
 */

public class ComparisonInfo {
    private String content;
    private String curValue;
    private String lastValue;
    private String comparisonValue;

    public ComparisonInfo() {
    }

    public ComparisonInfo(String content, String curValue, String lastValue, String comparisonValue) {
        this.content = content;
        this.curValue = curValue;
        this.lastValue = lastValue;
        this.comparisonValue = comparisonValue;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCurValue() {
        return curValue;
    }

    public void setCurValue(String curValue) {
        this.curValue = curValue;
    }

    public String getLastValue() {
        return lastValue;
    }

    public void setLastValue(String lastValue) {
        this.lastValue = lastValue;
    }

    public String getComparisonValue() {
        return comparisonValue;
    }

    public void setComparisonValue(String comparisonValue) {
        this.comparisonValue = comparisonValue;
    }
}
