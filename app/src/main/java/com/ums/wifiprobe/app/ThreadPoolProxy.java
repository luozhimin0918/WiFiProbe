package com.ums.wifiprobe.app;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenzhy on 2017/10/23.
 */

public class ThreadPoolProxy {
    private int mCorePoolSize;
    private int mMaxPoolSize;
    private long mKeepAliveTime;
    private ThreadPoolExecutor mThreadPoolExecutor;

    public ThreadPoolProxy(int corePoolSize,int maxPoolSize,long aliveTime){
        mCorePoolSize = corePoolSize;
        mMaxPoolSize = maxPoolSize;
        mKeepAliveTime = aliveTime;
    }

    private void initThreadPoolExecutor() {
        if (mThreadPoolExecutor == null || mThreadPoolExecutor.isShutdown() || mThreadPoolExecutor.isTerminated()) {
            synchronized (ThreadPoolProxy.class) {
                if (mThreadPoolExecutor == null || mThreadPoolExecutor.isShutdown() || mThreadPoolExecutor
                        .isTerminated()) {
                    TimeUnit unit = TimeUnit.MILLISECONDS;
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue();
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
                    mThreadPoolExecutor = new ThreadPoolExecutor(
                            mCorePoolSize, //核心池的大小
                            mMaxPoolSize, //最大线程数
                            mKeepAliveTime,//保持时间
                            unit,//保持时间的单位
                            workQueue,//工作队列
                            threadFactory,//线程工厂
                            handler//异常捕获器
                    );
                }
            }
        }
    }
    /**
     * 提交任务
     */
    public Future<?> submit(Runnable task) {
        initThreadPoolExecutor();
        Future<?> submitResult = mThreadPoolExecutor.submit(task);
        return submitResult;
    }


    /**
     * 执行任务
     */
    public void execute(Runnable task) {
        initThreadPoolExecutor();
        mThreadPoolExecutor.execute(task);
    }

    /**
     * 移除任务
     */
    public void remove(Runnable task) {
        initThreadPoolExecutor();
        mThreadPoolExecutor.remove(task);
    }



}
