package com.ums.wifiprobe.service;

import com.ums.wifiprobe.app.GlobalValueManager;

/**
 * Created by chenzhy on 2017/9/13.
 */

public class ProbeServiceConstant {

    public static int uploadInterval=60000 ;//上送时间间隔
    public static int leaveInterval=GlobalValueManager.getInstance().getLeaveIntervalTime();//离开时间间隔
    public static boolean autoProbe;//是否开机自动启动
    public static long probeAgainInterval = 1200000;//20分钟
    public static int locationDistance = GlobalValueManager.getInstance().getLocationDistance();//50米/若Pos移动距离超过这距离，则需提示是否清除之前数据

    public static int closeDistance=10;//0-10wifi探针探测的距离范围
    public static int middleDistance=20;//10-20
    public static int farDistance=30;//20-30 超过30米以上清除掉？


    public static int minRssi=-200;//0-10wifi探针探测的距离范围
    public static int middleRssi=-80;//10-20
    public static int maxRssi=-60;//20-30 超过30米以上清除掉？

    public static boolean probeIsOpened = false;
    public static boolean probeIsOpenedLaststate = false;
    public static boolean probeIsOpening = false;

    public static final String APP_KEY = "27fb08a43d254ac09e48492a82bd60a3";

    public static String longitude = GlobalValueManager.getInstance().getLongitude();
    public static String latitude=GlobalValueManager.getInstance().getLatitude();

    public final static String RECEIVER_ACTION_UPDATE_UPLOADINTERVALTIME = "com.ums.wifiprobe.receiver.action.UPLOADTIME";
    public final static String RECEIVER_ACTION_LEAVE_INTERVALTIME = "com.ums.wifiprobe.receiver.action.LEAVETIME";
    public final static String RECEIVER_ACTION_UPDATE_AUTOPROBE = "com.ums.wifiprobe.receiver.action.AUTOPROBE";
    public final static String RECEIVER_ACTION_OPEN_PROBE = "com.ums.wifiprobe.receiver.action.OPENPROBE";
    public final static String RECEIVER_ACTION_CLOSE_PROBE = "com.ums.wifiprobe.receiver.action.CLOSEPROBE";
    public final static String RECEIVER_ACTION_CLOSE_PROBE_COMPLETE = "com.ums.wifiprobe.receiver.action.CLOSEPROBECOMPLETE";

    public final static String RECEIVER_ACTION_LEAVE_PROBEAGAININTERVALTIME = "com.ums.wifiprobe.receiver.action.PROBEAGAINLEAVETIME";
    public final static String RECEIVER_ACTION_DISTANCE_LOCATION = "com.ums.wifiprobe.receiver.action.LOCATIONDISTANCE";
    public final static String RECEIVER_ACTION_DISTANCE_CLOSE = "com.ums.wifiprobe.receiver.action.CLOSEDISTANCE";
    public final static String RECEIVER_ACTION_DISTANCE_MIDDLE = "com.ums.wifiprobe.receiver.action.MIDDLEDISTANCE";
    public final static String RECEIVER_ACTION_DISTANCE_FAR = "com.ums.wifiprobe.receiver.action.FARDISTANCE";

    public final static String RECEIVER_ACTION_OPEN_PROBE_SUCCESS = "com.ums.wifiprobe.receiver.action.OPENPROBESUCCESS";
    public final static String RECEIVER_ACTION_OPEN_PROBE_FAILURE = "com.ums.wifiprobe.receiver.action.OPENPROBEFAILURE";
    public final static String RECEIVER_ACTION_CLOSE_PROBE_SUCCESS = "com.ums.wifiprobe.receiver.action.CLOSEPROBESUCCESS";

    public final static String RECEIVER_ACTION_CLEAR_QUEEN_DATA = "com.ums.wifiprobe.receiver.action.CLEARQUEENDATA";

    public final static String RECEIVER_ACTION_SERVICE_REPLACED = "com.ums.wifiprobe.receiver.action.SERVICEREPLACED";
}
