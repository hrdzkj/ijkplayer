package tv.danmaku.ijk.media.example;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import okhttp3.OkHttpClient;
import tv.danmaku.ijk.media.example.util.HttpUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created by LiuYi on 2018/9/7.
 */

public class MyApplication extends Application {
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        initOkHttp();
        instance = this;
    }

    private void initOkHttp()
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        HttpUtil.initClient(okHttpClient);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized Application getInstance()
    {
        return instance;
    }
}
