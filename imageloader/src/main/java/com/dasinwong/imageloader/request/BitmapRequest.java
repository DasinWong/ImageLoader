package com.dasinwong.imageloader.request;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.dasinwong.imageloader.listener.ImageListener;
import com.dasinwong.imageloader.util.MD5;

import java.lang.ref.SoftReference;

public class BitmapRequest {

    private Context context;

    private String url;
    private String urlMD5; //用于缓存的key
    private SoftReference<ImageView> softReference;
    private int loadingRes;
    private ImageListener listener;

    public BitmapRequest(Activity activity) {
        this.context = activity;
    }

    public BitmapRequest load(String url) {
        this.url = url;
        urlMD5 = MD5.format(url);
        return this;
    }

    public BitmapRequest loading(int res) {
        loadingRes = res;
        return this;
    }

    public BitmapRequest listen(ImageListener listener) {
        this.listener = listener;
        return this;
    }

    public void into(ImageView imageView) {
        imageView.setTag(urlMD5);
        softReference = new SoftReference<>(imageView);
        RequestManager.getInstance(context).add(this);
    }

    public String getUrl() {
        return url;
    }

    public String getUrlMD5() {
        return urlMD5;
    }

    public ImageView getImageView() {
        return softReference.get();
    }

    public int getLoadingRes() {
        return loadingRes;
    }

    public ImageListener getListener() {
        return listener;
    }
}
