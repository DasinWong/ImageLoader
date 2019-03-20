package com.dasinwong.imageloader.listener;

import android.graphics.Bitmap;

import com.dasinwong.imageloader.core.ImageError;

public interface ImageListener {

    Bitmap onReady(Bitmap bitmap);

    void onComplete();

    void onError(ImageError imageError);
}
