package com.ums.wifiprobe.app;

/**
 * Created by chenzhy on 2017/10/23.
 */

public class ThreadPoolProxyFactory {

    private static ThreadPoolProxy mUpdateThreadPoolProxy,mQueryThreadPoolProxy;

    /**
     * 返回普通线程池的代理 获取数据库增删操作线程池对象
     * 双重检查加锁,保证只有第一次实例化的时候才启用同步机制,提高效率
     * @return
     */
    public static ThreadPoolProxy getUpdateThreadPoolProxy() {
        if (mUpdateThreadPoolProxy == null) {
            synchronized (ThreadPoolProxyFactory.class) {
                if (mUpdateThreadPoolProxy == null) {
                    mUpdateThreadPoolProxy = new ThreadPoolProxy(3, 3, 3000);
                }
            }
        }
        return mUpdateThreadPoolProxy;
    }

    /**
     * 返回下载线程池的代理 获取数据库数据查询线程池对象，批量查询用
     */
    public static ThreadPoolProxy getQueryThreadPoolProxy() {
        if (mQueryThreadPoolProxy == null) {
            synchronized (ThreadPoolProxyFactory.class) {
                if (mQueryThreadPoolProxy == null) {
                    mQueryThreadPoolProxy = new ThreadPoolProxy(5, 5, 3000);
                }
            }
        }
        return mQueryThreadPoolProxy;
    }
}
