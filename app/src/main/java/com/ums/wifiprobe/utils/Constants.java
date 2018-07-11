package com.ums.wifiprobe.utils;

/**
 * 部分常量。
 */
public class Constants {
    /**
     * 双屏服务包名及类名。
     */
    public static final String DUAL_SCREEN_MANAGER_PKGNAME = "com.ums.ecr.service.aidl";
    public static final String DUAL_SCREEN_MANAGER_CLSNAME = "com.ums.ecr.service.aidl.IEcrService";
    public static final String DUAL_SCREEN_MANAGER_ACTION = "com.ums.ecr.SERVICE";

    /**
     * 当前的双屏模式。
     * MODE_NONE: 未进入双屏模式。
     * MODE_SINGLE_SYS: 单系统模式，指主屏、客屏两个屏幕同属于同一台android机器的模式。C10属于此种模式。
     * MODE_DUAL_SYS: 双系统模式，指主屏、客屏两个屏幕其实分别属于两台独立机器的模式。暂未使用。
     */
    public static final int MODE_NONE = 0;
    public static final int MODE_SINGLE_SYS = 1;
    public static final int MODE_DUAL_SYS = 2;

    /**
     * 若通过startOnSubScreen()启动客屏的activity时，startOnSubScreen()参数传递了数据，那么客屏activity的intent中
     * 就会带有一个ByteArrayExtra，其extra名称及最大长度如下。
     */
    public static final String EXTRA_DATA = "DualScreenManagerData";
    public static final int MAX_EXTRA_DATA_SIZE = 500 * 1024;

    /**
     * sendData()最大支持的传输buffer大小。
     */
    public static final int MAX_TRANSFER_DATA_SIZE = 1000 * 1024;
    
    /**
     * 错误码。
     */
    // 内部错误，需要具体查看log。
    public static final int ERROR_INTERNAL_ERROR = -1;
    // 当前没有客屏。
    public static final int ERROR_NO_SUBSCREEN = -2;
    // 参数错误。
    public static final int ERROR_BAD_PARAMETER = -3;
    // 不支持该功能。
    public static final int ERROR_NO_SUPPORT = -4;
    // 数据传输失败。
    public static final int ERROR_TRANSFER_ERROR = -5;
    // 传输的数据过大。
    public static final int ERROR_TOO_LARGE = -6;
    // 用于registerDataListener()，说明该ID已经被注册了。
    public static final int ERROR_ALREADY_EXIST = -7;
}
