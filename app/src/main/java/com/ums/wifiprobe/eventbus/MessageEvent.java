package com.ums.wifiprobe.eventbus;

/**
 * Created by Luozhimin on 2018-07-09.18:44
 */
public class MessageEvent {
    private String message;
    private String editQuery;
    private String editQueryBilie;
    private String editQueryMianji;

    public MessageEvent(String editQuery, String editQueryBilie, String editQueryMianji){
        this.editQuery=editQuery;
        this.editQueryBilie=editQueryBilie;
        this.editQueryMianji=editQueryMianji;
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
