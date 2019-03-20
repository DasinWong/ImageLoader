package com.dasinwong.imageloader.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

public class BitmapLruCache extends LruCache<String, Bitmap> {

    public BitmapLruCache() {
        super((int) Runtime.getRuntime().maxMemory() / 8);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

}
