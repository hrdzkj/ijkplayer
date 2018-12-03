package tv.danmaku.ijk.media.example.util;


import java.io.IOException;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Platform;

/**
 * Created by LiuYi on 2018/8/29.
 */

public class HttpUtil {
    private volatile static HttpUtil mInstance;
    private static OkHttpClient mOkHttpClient;
    private Platform mPlatform;
    private static String mHttpSuccess="0";


    public static boolean isHttpCallSuccess(String resultCode)
    {
        return mHttpSuccess.equals(resultCode);
    }

    public HttpUtil(OkHttpClient okHttpClient)
    {
        if (okHttpClient == null)
        {
            mOkHttpClient = new OkHttpClient();
        } else
        {
            mOkHttpClient = okHttpClient;
        }

        mPlatform = Platform.get();
    }

    public static HttpUtil initClient(OkHttpClient okHttpClient)
    {
        if (mInstance == null)
        {
            synchronized (HttpUtil.class)
            {
                if (mInstance == null)
                {
                    mInstance = new HttpUtil(okHttpClient);
                }
            }
        }
        return mInstance;
    }



    public  static void postFormBody(Object tag, String url, Map<String,String> params, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params!=null && params.size()>0){
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(),entry.getValue());
            }
        }
        String tagString = tag ==null?"":tag.getClass().getName();
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .tag(tagString)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }

    // bug response.body().string()，从响应中获取响应体属于网络操作

    public static Observable<String> post(Object tag, String url, Map<String,String> params) {
        Observable<String> observable = Observable.create((ObservableOnSubscribe<String>) userObservable -> {
            FormBody.Builder builder = new FormBody.Builder();
            if (params!=null && params.size()>0){
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.add(entry.getKey(),entry.getValue());
                }
            }
            String tagString = tag ==null?"":tag.getClass().getName();

            Request request = new Request.Builder()
                    .url(url)
                    .post(builder.build())
                    .tag(tagString)
                    .build();
            Call call = mOkHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                   String resultSting = response.body().string();
                    userObservable.onNext(resultSting);
                    userObservable.onComplete();
                }
                @Override
                public void onFailure(Call call, IOException e) {
                 userObservable.onError(e);
                }
            });
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return observable;
    }


    public static void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

}
