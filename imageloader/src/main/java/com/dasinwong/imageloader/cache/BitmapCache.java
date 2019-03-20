package com.dasinwong.imageloader.cache;

import android.graphics.Bitmap;

import com.dasinwong.imageloader.request.BitmapRequest;

public interface BitmapCache {
    /**
     * 存入缓存数据
     */
    void put(BitmapRequest request, Bitmap bitmap);

    /**
     * 读取缓存数据
     */
    Bitmap get(BitmapRequest request);

    /**
     * 移除缓存数据
     */
    void remove(BitmapRequest request);
}
