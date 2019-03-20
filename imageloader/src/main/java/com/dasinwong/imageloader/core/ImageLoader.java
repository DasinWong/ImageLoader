package com.dasinwong.imageloader.core;

import android.app.Activity;

import com.dasinwong.imageloader.request.BitmapRequest;
public class ImageLoader {

    public static BitmapRequest with(Activity activity) {
        return new BitmapRequest(activity);
    }

}
