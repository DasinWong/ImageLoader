package com.dasinwong.imageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dasinwong.imageloader.request.BitmapRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class L2Cache implements BitmapCache {

    private static L2Cache instance;

    private Context context;
    private DiskCache diskCache;
    private MemoryCache memoryCache;

    private L2Cache(Context context) {
        this.context = context;
        diskCache = DiskCache.getInstance(this.context);
        memoryCache = MemoryCache.getInstance(this.context);
    }

    public static L2Cache getInstance(Context context) {
        if (instance == null) {
            synchronized (L2Cache.class) {
                if (instance == null) {
                    instance = new L2Cache(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        diskCache.put(request, bitmap);
        memoryCache.put(request, bitmap);
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        Bitmap bitmap = null;
        bitmap = memoryCache.get(request); //从内存中读取
        if (bitmap == null) {
            bitmap = diskCache.get(request); //从硬盘中读取
            if (bitmap == null) {
                bitmap = download(request); //从网络下载图片
                this.put(request, bitmap); //缓存到硬盘与内存
            } else {
                memoryCache.put(request, bitmap); //缓存到内存
            }
        }
        return bitmap;
    }

    @Override
    public void remove(BitmapRequest request) {
        diskCache.remove(request);
        memoryCache.remove(request);
    }

    /**
     * 从网络下载图片
     */
    private Bitmap download(BitmapRequest request) {
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            conn = (HttpURLConnection) new URL(request.getUrl()).openConnection();
            conn.setConnectTimeout(30 * 1000);
            conn.setReadTimeout(30 * 1000);
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (200 == responseCode) {
                inputStream = conn.getInputStream();
                return BitmapFactory.decodeStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
