package com.ums.wifiprobe.eventbus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luozhimin on 2018-07-09.18:44
 */
public class MessageEvent {
    private String message;
    private String editQuery;
    private String editQueryBilie;
    private String editQueryMianji;


    List<Float> LastMonth30Keliudangjie = new ArrayList<>();
    List<Float> Month30Keliudangjie = new ArrayList<>();

    List<String> monthDateStrList = new ArrayList<>();//本月30天的日期字符串list
    List<String> LastmonthDateStrList = new ArrayList<>();//上个月30天的日期字符串list


    List<Float> Week7Keliudangjie = new ArrayList<>();
    List<Float> LastWeek7Keliudangjie = new ArrayList<>();

    public MessageEvent(String editQuery, String editQueryBilie, String editQueryMianji){
        this.editQuery=editQuery;
        this.editQueryBilie=editQueryBilie;
        this.editQueryMianji=editQueryMianji;
    }
    public MessageEvent( List<Float> Month30Keliudangjie  , List<Float> LastMonth30Keliudangjie , List<String> monthDateStrList ,List<String> LastmonthDateStrList){
        this.Month30Keliudangjie=Month30Keliudangjie;
        this.LastMonth30Keliudangjie=LastMonth30Keliudangjie;
        this.monthDateStrList=monthDateStrList;
        this.LastmonthDateStrList=LastmonthDateStrList;
    }
    public MessageEvent(  List<Float> Week7Keliudangjie  , List<Float> LastWeek7Keliudangjie){

     this.Week7Keliudangjie =Week7Keliudangjie;
     this.LastWeek7Keliudangjie = LastWeek7Keliudangjie;
    }

    public List<Float> getWeek7Keliudangjie() {
        return Week7Keliudangjie;
    }

    public void setWeek7Keliudangjie(List<Float> week7Keliudangjie) {
        Week7Keliudangjie = week7Keliudangjie;
    }

    public List<Float> getLastWeek7Keliudangjie() {
        return LastWeek7Keliudangjie;
    }

    public void setLastWeek7Keliudangjie(List<Float> lastWeek7Keliudangjie) {
        LastWeek7Keliudangjie = lastWeek7Keliudangjie;
    }

    public List<String> getMonthDateStrList() {
        return monthDateStrList;
    }

    public void setMonthDateStrList(List<String> monthDateStrList) {
        this.monthDateStrList = monthDateStrList;
    }

    public List<String> getLastmonthDateStrList() {
        return LastmonthDateStrList;
    }

    public void setLastmonthDateStrList(List<String> lastmonthDateStrList) {
        LastmonthDateStrList = lastmonthDateStrList;
    }

    public List<Float> getLastMonth30Keliudangjie() {
        return LastMonth30Keliudangjie;
    }

    public void setLastMonth30Keliudangjie(List<Float> lastMonth30Keliudangjie) {
        LastMonth30Keliudangjie = lastMonth30Keliudangjie;
    }

    public List<Float> getMonth30Keliudangjie() {
        return Month30Keliudangjie;
    }

    public void setMonth30Keliudangjie(List<Float> month30Keliudangjie) {
        Month30Keliudangjie = month30Keliudangjie;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEditQuery() {
        return editQuery;
    }

    public void setEditQuery(String editQuery) {
        this.editQuery = editQuery;
    }

    public String getEditQueryBilie() {
        return editQueryBilie;
    }

    public void setEditQueryBilie(String editQueryBilie) {
        this.editQueryBilie = editQueryBilie;
    }

    public String getEditQueryMianji() {
        return editQueryMianji;
    }

    public void setEditQueryMianji(String editQueryMianji) {
        this.editQueryMianji = editQueryMianji;
    }
}
