# ImageLoader
- Android图片加载框架
- 支持4.1以后版本（99.6%的设备）
- 链式调用，结构清晰，使用方便
- 加载过程可监听，便于调试
- 添加二级缓存，避免造成OOM
- 根据设备CPU进行并发处理，效率快不卡顿
- 生命周期管理和其他方法（待更新）
## 如何接入
Project层级下的build.gradle文件
```
allprojects {
    repositories {
    ...
    maven { url 'https://jitpack.io' }
    }
}
```
Module层级下的build.gradle文件
```
implementation 'com.github.dasinwong:permissionhelper:1.2'
```
## 类及其方法介绍
### ImageLoader
| 方法 | 描述 |
| :-------------: | :-------------: |
| with | 创建BitmapRequest对象的静态方法 |
### BitmapRequest
Bitmap请求对象，下载，缓存，显示等操作均有此对象实现

| 方法 | 描述 |
| :-------------: | :-------------: |
| load | 加载一个网络图片 |
| loading | 加载中显示的本地图片 |
| listen | 添加一个加载监听 |
| into | 显示图片到ImageView对象 |

### ImageListener
图片加载过程监听接口类

| 方法 | 描述 |
| :-------------: | :-------------: |
| onReady | 对加载出的图片进行操作后再展示 |
| onComplete | 图片加载完成 |
| onError | 图片加载错误 |

### ImageError
图片加载错误枚举

| 枚举值 | 描述 |
| :-------------: | :-------------: |
| EMPTY_BITMAP | 图片为空或已回收 |
| EMPTY_VIEW | 图片宿主为空 |

## 使用方法
```
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
```
