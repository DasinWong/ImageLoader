package com.dasinwong.imageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;

import com.dasinwong.imageloader.request.BitmapRequest;

public class MemoryCache implements BitmapCache {

    private static MemoryCache instance;

    private Context context;
    private BitmapLruCache lruCache;

    private MemoryCache(Context context) {
        this.context = context;
        lruCache = new BitmapLruCache();
    }

    public static MemoryCache getInstance(Context context) {
        if (instance == null) {
            synchronized (MemoryCache.class) {
                if (instance == null) {
                    instance = new MemoryCache(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            lruCache.put(request.getUrlMD5(), bitmap);
        }
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        return lruCache.get(request.getUrlMD5());
    }

    @Override
    public void remove(BitmapRequest request) {
        lruCache.remove(request.getUrlMD5());
    }
}
