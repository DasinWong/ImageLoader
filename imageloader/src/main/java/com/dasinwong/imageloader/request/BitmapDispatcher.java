package com.dasinwong.imageloader.request;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;


import com.dasinwong.imageloader.cache.L2Cache;
import com.dasinwong.imageloader.core.ImageError;
import com.dasinwong.imageloader.listener.ImageListener;

import java.util.concurrent.LinkedBlockingQueue;

public class BitmapDispatcher extends Thread {

    private Handler handler = new Handler(Looper.getMainLooper());
    private LinkedBlockingQueue<BitmapRequest> requestQueue;
    private L2Cache cache;

    public BitmapDispatcher(Context context, LinkedBlockingQueue<BitmapRequest> requestQueue) {
        this.requestQueue = requestQueue;
        cache = L2Cache.getInstance(context);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                BitmapRequest request = requestQueue.take();
                showLoading(request); //显示加载中图片资源
                showBitmap(request, cache.get(request)); //显示网络图片
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 显示Bitmap图片
     */
    private void showBitmap(final BitmapRequest request, final Bitmap bitmap) {

        final ImageView imageView = request.getImageView();
        final ImageListener listener = request.getListener();

        if (listener != null) {
            if (imageView == null) {
                listener.onError(ImageError.EMPTY_VIEW);
                listener.onComplete();
                return;
            }
            if (bitmap == null || bitmap.isRecycled()) {
                listener.onError(ImageError.EMPTY_BITMAP);
                listener.onComplete();
                return;
            }
        }

        if (bitmap != null && !bitmap.isRecycled() && imageView != null && imageView.getTag().equals(request.getUrlMD5())) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null) {
                        Bitmap readyBitmap = listener.onReady(bitmap);
                        if (readyBitmap != null && !readyBitmap.isRecycled()) {
                            imageView.setImageBitmap(readyBitmap);
                        } else {
                            imageView.setImageBitmap(bitmap);
                        }
                    } else
                        imageView.setImageBitmap(bitmap);
                    if (listener != null)
                        listener.onComplete();
                }
            });
        }
    }

    /**
     * 显示本地图片资源
     */
    private void showLoading(BitmapRequest request) {

        final int loadingRes = request.getLoadingRes();
        final ImageView imageView = request.getImageView();
        final ImageListener listener = request.getListener();

        if (listener != null && imageView == null)
            listener.onError(ImageError.EMPTY_VIEW); //空组件错误

        if (loadingRes > 0 && imageView != null && imageView.getTag().equals(request.getUrlMD5())) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(loadingRes);
                }
            });
        }
    }
}
