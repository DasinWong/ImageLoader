package com.dasinwong.imageloader.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class IO {

    public static void write(String filePath, String fileName, Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            File file = new File(filePath, fileName);
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap read(String filePath, String fileName) {
        try {
            File file = new File(filePath, fileName);
            if (file.exists()) {
                return BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void delete(String filePath, String fileName) {
        File file = new File(filePath, fileName);
        if (file.exists() && file.isFile())
            file.delete();
    }
}