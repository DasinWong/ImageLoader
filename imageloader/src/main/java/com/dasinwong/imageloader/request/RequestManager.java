package com.dasinwong.imageloader.request;

import android.content.Context;

import java.util.concurrent.LinkedBlockingQueue;

public class RequestManager {

    private static RequestManager instance;

    private Context context;
    private LinkedBlockingQueue<BitmapRequest> requestQueue;
    private BitmapDispatcher[] dispatchers; //并发管理器

    private RequestManager(Context context) {
        this.context = context;
        requestQueue = new LinkedBlockingQueue<>();
        start(); //初始化后立刻执行并发
    }

    public static RequestManager getInstance(Context context) {
        if (instance == null) {
            synchronized (RequestManager.class) {
                if (instance == null) {
                    instance = new RequestManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * 添加请求到队列
     */
    public void add(BitmapRequest request) {
        if (!requestQueue.contains(request)) {
            requestQueue.add(request);
        }
    }

    /**
     * 创建Dispatcher并发处理请求
     */
    private void start() {
        stop();
        int cpuCount = Runtime.getRuntime().availableProcessors();
        dispatchers = new BitmapDispatcher[cpuCount];
        for (int i = 0; i < cpuCount; i++) {
            BitmapDispatcher dispatcher = new BitmapDispatcher(context, requestQueue);
            dispatchers[i] = dispatcher;
            dispatcher.start();
        }
    }

    /**
     * 阻塞所有Dispatcher
     */
    private void stop() {
        if (dispatchers != null && dispatchers.length > 0) {
            for (BitmapDispatcher dispatcher : dispatchers) {
                if (!dispatcher.isInterrupted())
                    dispatcher.interrupt();
            }
        }
    }

}
