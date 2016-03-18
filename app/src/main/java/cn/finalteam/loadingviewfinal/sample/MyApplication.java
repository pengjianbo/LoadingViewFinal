package cn.finalteam.loadingviewfinal.sample;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;
import cn.finalteam.okhttpfinal.Part;
import okhttp3.Headers;
import okhttp3.Interceptor;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/9 下午6:30
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initOkHttpFinal();
    }

    private void initOkHttpFinal() {

        List<Part> commomParams = new ArrayList<>();
        Headers commonHeaders = new Headers.Builder().build();

        List<Interceptor> interceptorList = new ArrayList<>();
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder()
                .setCommenParams(commomParams)
                .setCommenHeaders(commonHeaders)
                .setTimeout(35000)
                .setInterceptors(interceptorList)
                .setDebug(BuildConfig.DEBUG)
                //.setCookieJar(CookieJar.NO_COOKIES)
                //.setCertificates(...)
                //.setHostnameVerifier(new SkirtHttpsHostnameVerifier())
                .setDebug(true);
        OkHttpFinal.getInstance().init(builder.build());
    }
}
