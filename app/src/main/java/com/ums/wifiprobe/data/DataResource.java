package com.ums.wifiprobe.data;

import java.util.Collection;
import java.util.List;

/**
 * Created by chenzhy on 2017/9/17.
 */

public interface DataResource<T extends Object> {
    interface LoadTasksCallback<T>{
        void onTasksLoaded(List<T> list);
        void onDataNotAvaliable();
    }
    interface GetTaskCallback<T>{
        void OnTaskLoaded(T info);
        void onDataNotAvaliable();
    }
    void getTask(String scaleValue,String scale,String time,GetTaskCallback<T> callback);
    void getTasks(String scaleValue,String scale,String time,LoadTasksCallback<T> callback);
    void saveTask(T info);
    void saveTasks(List<T> list);
    void clearTasks();
}
