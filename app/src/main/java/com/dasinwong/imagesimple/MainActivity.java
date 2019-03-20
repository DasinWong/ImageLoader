package com.dasinwong.imagesimple;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.dasinwong.imageloader.core.ImageError;
import com.dasinwong.imageloader.core.ImageLoader;
import com.dasinwong.imageloader.listener.ImageListener;
import com.dasinwong.permissionhelper.PermissionHelper;
import com.dasinwong.permissionhelper.PermissionListener;
import com.dasinwong.permissionhelper.PermissionResult;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button btn_single;
    private Button btn_more;
    private ScrollView scrollView;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionHelper.with(this).auto(new PermissionListener() {
            @Override
            public void onComplete(Map<String, PermissionResult> resultMap) {

            }
        });
        initView();
    }

    private void initView() {
        btn_single = findViewById(R.id.btn_single);
        btn_more = findViewById(R.id.btn_more);
        scrollView = findViewById(R.id.scrollView);
        layout = findViewById(R.id.layout);
        btn_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                single();
            }
        });
        btn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                more();
            }
        });
    }

    private void more() {
        for (int i = 1; i <= 24; i++) {
            ImageView imageView = new ImageView(this);
            layout.addView(imageView);
            ImageLoader
                    .with(this)
                    .load("http://dn.dengpaoedu.com/glide/" + i + ".jpeg")
                    .loading(R.mipmap.ic_launcher)
                    .listen(new ImageListener() {
                        @Override
                        public Bitmap onReady(Bitmap bitmap) {
                            return null;
                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(ImageError imageError) {

                        }
                    })
                    .into(imageView);
        }
    }

    private void single() {
        ImageView imageView = new ImageView(this);
        layout.addView(imageView);
        ImageLoader
                .with(this)
                .load("http://dn.dengpaoedu.com/glide/1.jpeg")
                .loading(R.mipmap.ic_launcher)
                .listen(new ImageListener() {
                    @Override
                    public Bitmap onReady(Bitmap bitmap) {
                        return rotateBitmap(bitmap, 90);
                    }

                    @Override
                    public void onComplete() {
                        Log.e("ImageListener", "图片加载完成");
                    }

                    @Override
                    public void onError(ImageError imageError) {
                        Log.e("ImageListener", imageError + "");
                    }
                })
                .into(imageView);
    }

    /**
     * 旋转Bitmap
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, float alpha) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
    }
}
