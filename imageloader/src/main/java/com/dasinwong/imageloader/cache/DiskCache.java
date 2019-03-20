package com.dasinwong.imageloader.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;

import com.dasinwong.imageloader.request.BitmapRequest;
import com.dasinwong.imageloader.util.IO;

import java.io.File;

public class DiskCache implements BitmapCache {

    private static DiskCache instance;

    private Context context;
    private String cacheDir;

    private DiskCache(Context context) {
        this.context = context;
        cacheDir = initDiskCacheDir(this.context);
    }

    public static DiskCache getInstance(Context context) {
        if (instance == null) {
            synchronized (DiskCache.class) {
                if (instance == null) {
                    instance = new DiskCache(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void put(BitmapRequest request, Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            IO.write(cacheDir, request.getUrlMD5(), bitmap);
        }
    }

    @Override
    public Bitmap get(BitmapRequest request) {
        return IO.read(cacheDir, request.getUrlMD5());
    }

    @Override
    public void remove(BitmapRequest request) {
        IO.delete(cacheDir, request.getUrlMD5());
    }

    /**
     * 初始化缓存目录
     */
    private String initDiskCacheDir(Context context) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
            return null;
        File cacheDir = new File(Environment.getExternalStorageDirectory(), File.separator + getPackageName(context) + File.separator + "cache");
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir.getAbsolutePath();
    }

    /**
     * 获取应用包名
     */
    private String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
