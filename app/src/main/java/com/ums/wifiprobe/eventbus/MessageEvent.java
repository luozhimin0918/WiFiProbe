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
    public MessageEvent(String editQuery, String editQueryBilie, String editQueryMianji){
        this.editQuery=editQuery;
        this.editQueryBilie=editQueryBilie;
        this.editQueryMianji=editQueryMianji;
    }
    public MessageEvent( List<Float> Month30Keliudangjie  , List<Float> LastMonth30Keliudangjie ){
        this.Month30Keliudangjie=Month30Keliudangjie;
        this.LastMonth30Keliudangjie=LastMonth30Keliudangjie;
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
